
package controlador.reporte;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import configuracion.GeneradorBeans;
import controlador.CGeneral;
import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SDefensa;
import modelo.Teg;
import modelo.reporte.DefensaTeg;
@Controller
public class CReporteDefensa extends CGeneral {
	
	public CReporteDefensa() {
		super();
		// TODO Auto-generated constructor stub
	}

	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();

	
	@Wire
	private Window wdwReporteDefensa;	
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Jasperreport jstVistaPrevia;
	
	Programa programa = new Programa(); 
	private String[] estatusDefensa = { "Todos", "Por Defender", "Defensa Finalizada"};
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	@Override
	
	public void inicializar(Component comp) {
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas = servicioPrograma.buscarActivas();
		programas.add(programaa);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusDefensa));

	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() {
		if (cmbPrograma.getValue().equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
		}
		else {
			 cmbArea.setValue("");
			 cmbTematica.setValue("");
		Programa programa = (Programa) cmbPrograma.getSelectedItem().getValue();
		areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
				.buscar(programa.getId()));
		AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos", "", true);
	 	areas.add(area);
		cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

	}
}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		if (cmbArea.getValue().equals("Todos")) {	
			cmbTematica.setValue("Todos");
		}
		else {
		cmbTematica.setValue("");
		AreaInvestigacion tematica = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(tematica.getId()));
		cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		}
	}

	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
	}
	
	@Listen("onClick = #btnGenerarReporteDefensa")
	public void generarReporteDefensa() throws JRException {
		Date fechaInicio = dtbFechaInicio.getValue();
		Date fechaFin = dtbFechaFin.getValue();
		String nombreArea = cmbArea.getValue();
		String nombreTematica = cmbTematica.getValue();
		String nombrePrograma= cmbPrograma.getValue();
		AreaInvestigacion area = servicioArea.buscarArea(idArea);
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		String tipoDefensa = (String) cmbEstatus.getSelectedItem().getValue();
		System.out.println(tipoDefensa);
		System.out.println(nombrePrograma);
		System.out.println(nombreTematica);
		
		List<Defensa> defensas =new ArrayList();		
		List<DefensaTeg> elementos = new ArrayList<DefensaTeg>();

		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else if(!tipoDefensa.equals("Todos")) {
			
			if (nombrePrograma.equals("Todos")) {
				
			defensas = servicioDefensa.buscarDefensaTegSegunEstatus2(tipoDefensa,fechaInicio,fechaFin);
					}
			else
			if (!nombrePrograma.equals("Todos") && nombreArea.equals("Todos")) {
				defensas=servicioDefensa.buscarDefensaTegSegunEstatusPrograma2(tipoDefensa, programa, fechaInicio, fechaFin);
				}
			else
			if (!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && !nombreTematica.equals("Todos")) {
				    tematica= servicioTematica.buscarTematicaPorNombre(nombreTematica);
				    defensas=servicioDefensa.buscarDefensaTegSegunEstatusTematica2(tipoDefensa, tematica, fechaInicio, fechaFin);
				}else
			if(!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && nombreTematica.equals("Todos")){
				defensas=servicioDefensa.buscarDefensaTegSegunEstatusArea2(tipoDefensa, area, fechaInicio, fechaFin);
				}
		}else{
			if (nombrePrograma.equals("Todos")) {
				
				defensas = servicioDefensa.buscarDefensaTeg(fechaInicio,fechaFin);
				}
				else
				if (!nombrePrograma.equals("Todos") && nombreArea.equals("Todos")) {
					defensas=servicioDefensa.buscarDefensaTegSegunPrograma(programa, fechaInicio, fechaFin);
					}
				else
				if (!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && !nombreTematica.equals("Todos")) {
					    tematica= servicioTematica.buscarTematicaPorNombre(nombreTematica);
					    defensas=servicioDefensa.buscarDefensaTegSegunTematica(tematica, fechaInicio, fechaFin);
					}else
				if(!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && nombreTematica.equals("Todos")){
					defensas=servicioDefensa.buscarDefensaTegSegunArea( area, fechaInicio, fechaFin);
					}
		}
		
		if(defensas.size()!=0){
			for (Defensa defensa : defensas) {
				 List<Estudiante> estudiantes= servicioEstudiante.buscarEstudiantePorTeg(defensa.getTeg());
				 
						String nombreEstudiantes = "";
						for (Estudiante e : estudiantes) {
						nombreEstudiantes += e.getNombre() +" "+e.getApellido()+" ";
						}
							elementos.add(new DefensaTeg(
								defensa.getTeg().getTitulo(),
								defensa.getProfesor().getNombre() + " " + defensa.getProfesor().getApellido(),
								nombreEstudiantes,
								defensa.getEstatus(),
								defensa.getFecha()));
			}
			
		FileSystemView filesys = FileSystemView.getFileSystemView();
		Map<String, Object> p = new HashMap<String, Object>();
		String rutaUrl = obtenerDirectorio();
		 String reporteSrc = rutaUrl
		 +
		 "SITEG/vistas/reportes/estructurados/compilados/RReporteDefensa.jasper";
		 String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
		
		
		p.put("fecha", new Date());
		p.put("fecha1", fechaInicio);
		p.put("fecha2", fechaFin);
		p.put("area", nombreArea);
		p.put("tematica", nombreTematica);
		p.put("programa", nombrePrograma);
		p.put("estatus", tipoDefensa);
		p.put("logoUcla", reporteImage + "logo ucla.png");
		p.put("logoCE", reporteImage + "logo CE.png");
		p.put("logoSiteg", reporteImage + "logo.png");
		     
		 jstVistaPrevia.setSrc(reporteSrc);
		 jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(elementos));
		 jstVistaPrevia.setType("pdf");
		 jstVistaPrevia.setParameters(p);
		}
		else{
			Messagebox.show("No ha informacion disponible para este intervalo");
		}
	}
	
	@Listen("onClick = #btnSalirReporteSolicitud")
	public void cancelarTematicasSolicitadas() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());	
	}
	
}
 