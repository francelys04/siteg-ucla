
package controlador;

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
import modelo.ProgramaArea;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SDefensa;
import modelo.Teg;
@Controller
public class CReporteDefensa extends CGeneral {
	
	public CReporteDefensa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static class ElementoReporte {
		
		private String tituloTeg;
		private String nombreTutor;
		private String nombreEstudiante;
		private String estatusDefensa;
		private Date fechaDefensa ;


	 public ElementoReporte(String tituloteg, String nombretutor, String nombreEstudiante, String estatusdefensa, Date fechaDefensa) {
			super();
			this.tituloTeg = tituloteg;
			this.nombreTutor = nombretutor;
			this.nombreEstudiante = nombreEstudiante;
			this.estatusDefensa = estatusdefensa;
			this.fechaDefensa= fechaDefensa;
		}


	public String getTituloTeg() {
		return tituloTeg;
	}


	public void setTituloTeg(String tituloTeg) {
		this.tituloTeg = tituloTeg;
	}


	public String getNombreTutor() {
		return nombreTutor;
	}


	public void setNombreTutor(String nombreTutor) {
		this.nombreTutor = nombreTutor;
	}


	public String getEstatusDefensa() {
		return estatusDefensa;
	}


	public void setEstatusDefensa(String estatusDefensa) {
		this.estatusDefensa = estatusDefensa;
	}


	public String getNombreEstudiante() {
		return nombreEstudiante;
	}


	public void setNombrestudiante(String nombrestudiante) {
		this.nombreEstudiante = nombrestudiante;
	}


	public Date getFechaDefensa() {
		return fechaDefensa;
	}


	public void setFechaDefensa(Date fechaDefensa) {
		this.fechaDefensa = fechaDefensa;
	}

	 
	 
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
	
	
	
	private String[] estatusDefensa = { "Todos", "Por Defender", "Defensa Finalizada"};
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	@Override
	
	void inicializar(Component comp) {
		cmbEstatus.setModel(new ListModelList<String>(estatusDefensa));
		List<Programa> programas = servicioPrograma.buscarActivas();
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		
	}
	

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() {
		cmbArea.setValue("");
		cmbTematica.setValue("");

		Programa programa = (Programa) cmbPrograma.getSelectedItem().getValue();
		areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
				.buscar(programa.getId()));
		cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		cmbTematica.setValue("");
		AreaInvestigacion tematica = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(tematica.getId()));
		cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
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
		String area1 = cmbArea.getValue();
		String tematica1 = cmbTematica.getValue();
		String programa1= cmbPrograma.getValue();
		String estatusdefensa =cmbEstatus.getValue();
		AreaInvestigacion area = servicioArea.buscarArea(idArea);
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		String tipoDefensa = (String) cmbEstatus.getSelectedItem().getValue();
		System.out.println(tipoDefensa);
		
		
		List<ElementoReporte> elementos = new ArrayList<ElementoReporte>();

		if (fechaFin == null || fechaInicio == null || fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			List<Defensa> defensas = servicioDefensa.buscardefensaPorDosFechas(fechaInicio, fechaFin);
			for (Defensa defensa : defensas) {
				 
					 List<Estudiante> estudiantes= servicioEstudiante.buscarEstudiantePorTeg(defensa.getTeg());
					 
							String nombreEstudiantes = "";
							for (Estudiante e : estudiantes) {
							nombreEstudiantes += e.getNombre() +" "+e.getApellido()+" ";
							}
						
				if (defensa.getEstatus().equals("Por Defender") || defensa.getEstatus().equals("Todos")) {
					elementos.add(new ElementoReporte(
							defensa.getTeg().getTitulo(),
							defensa.getProfesor().getNombre() + " " + defensa.getProfesor().getApellido(),
							nombreEstudiantes,
							defensa.getEstatus(),
							defensa.getFecha()));
				}
				else 
				if (defensa.getEstatus().equals("Defensa Finalizada") || defensa.getEstatus().equals("Todos")) {
							elementos.add(new ElementoReporte(
							defensa.getTeg().getTitulo(),
							defensa.getProfesor().getNombre() + " " + defensa.getProfesor().getApellido(),
							nombreEstudiantes,
							defensa.getEstatus(),
							defensa.getFecha()));
				}
			}
			
			Collections.sort(elementos, new Comparator<ElementoReporte>() {
				public int compare(ElementoReporte a, ElementoReporte b) {
				return a.tituloTeg.compareTo(b.tituloTeg);
				}});
		}

		FileSystemView filesys = FileSystemView.getFileSystemView();
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("fecha", new Date());
		p.put("fecha1", fechaInicio);
		p.put("fecha2", fechaFin);
		p.put("area", area1);
		p.put("tematica", tematica1);
		p.put("programa", programa1);
		p.put("estatus", estatusdefensa);

		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(getClass().getResource(
						"/reporte/RReporteDefensa.jasper"));

		JasperPrint jasperPrint = JasperFillManager.fillReport(
				jasperReport, p, new JRBeanCollectionDataSource(elementos));
		
		String ruta = filesys.getHomeDirectory().toString() + "/reporte.pdf";
		JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
		
		Messagebox.show("Su reporte fue guardado en: " + ruta,
				"Informaci�n", Messagebox.OK,
				Messagebox.INFORMATION);

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
 