package controlador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Cronograma;
import modelo.Estudiante;
import modelo.Jurado;
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
import servicio.SEstudiante;

@Controller
public class CReporteProyecto extends CGeneral {
	public static class ElementoReporte {
		private Teg teg;
		private String nombreEstudiantes;
		
		public ElementoReporte(Teg teg, String nombreEstudiantes) {
			super();
			this.teg = teg;
			this.nombreEstudiantes = nombreEstudiantes;
		}
		public Teg getTeg() {
			return teg;
		}
		public void setTeg(Teg teg) {
			this.teg = teg;
		}
		public String getNombreEstudiantes() {
			return nombreEstudiantes;
		}
		public void setNombreEstudiantes(String nombreEstudiantes) {
			this.nombreEstudiantes = nombreEstudiantes;
		}

}
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	@Wire
	private Window wdwReporteTeg;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	
	private String[] estatusTeg = { "Todos", "Proyecto Registrado","Proyecto Factible","Proyecto deTrabajo Especial de Grado en Desarrollo",
			"Avances Finalizados del Proyecto"};
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);

		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusTeg));
	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		if (cmbPrograma.getValue().equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
		}
		else{
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
		else{
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
		//idTematica = tematica.getId();
	}
	
	@Listen("onClick = #btnGenerarReporteProyecto")
    public void generarReporteTEG() throws JRException{
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		Date fechaInicio = dtbFechaInicio.getValue();
		Date fechaFin = dtbFechaFin.getValue();
		String estatus = cmbEstatus.getValue();
		List<Teg> teg = null;
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			        Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
	   } 
		 else {
			 if (!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && !estatus.equals("Todos")) {
					String idTematica = cmbTematica.getSelectedItem().getId();
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
					teg = servicioTeg.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(estatus,
							                                       tematica1, fechaInicio, fechaFin);
					 Messagebox.show("1",
							     "Informacion", Messagebox.OK,
							   Messagebox.INFORMATION);
				}
			 
				 if (!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && estatus.equals("Todos")) {
					 String idTematica = cmbTematica.getSelectedItem().getId();
					 Tematica tematica1 = servicioTematica.buscarTematica(Long
								.parseLong(idTematica));
					
					 String estatusTeg1="Proyecto Registrado";
					 String estatusTeg2="Proyecto Factible";
					 String estatusTeg3="Proyecto deTrabajo Especial de Grado en Desarrollo";
					 String estatusTeg4="Avances Finalizados del Proyecto";
					 teg = servicioTeg.buscarTegDeUnaTematicaPorDosFechasyVariosEstatus1(estatusTeg1, 
							          estatusTeg2, estatusTeg3, estatusTeg4, tematica1,fechaInicio, fechaFin);
					 Messagebox.show(
							  "2",
							     "Informacion", Messagebox.OK,
							   Messagebox.INFORMATION);
				 }
					 
				 if(!nombrePrograma.equals("Todos") && nombreArea.equals("Todos") && !estatus.equals("Todos")){
					 String idPrograma = cmbPrograma.getSelectedItem().getId();
					 Programa programa1 = servicioPrograma.buscar(Long
								.parseLong(idPrograma));
					 teg = servicioTeg.buscarTegPorProgramaVariasAreasUnEstatus(estatus, programa1, fechaInicio, fechaFin);
					 Messagebox.show(
							  "3",
							     "Informacion", Messagebox.OK,
							   Messagebox.INFORMATION);
						
				 }
				 if(!nombrePrograma.equals("Todos") && nombreArea.equals("Todos") && estatus.equals("Todos")){
					 String idPrograma = cmbPrograma.getSelectedItem().getId();
					 Programa programa1 = servicioPrograma.buscar(Long
								.parseLong(idPrograma));
					 String estatusTeg1="Proyecto Registrado";
					 String estatusTeg2="Proyecto Factible";
					 String estatusTeg3="Proyecto deTrabajo Especial de Grado en Desarrollo";
					 String estatusTeg4="Avances Finalizados del Proyecto";
					 teg = servicioTeg.buscarTegPorProgramaVariasAreasVariosEstatus1(estatusTeg1, 
					                estatusTeg2, estatusTeg3, estatusTeg4, programa1,fechaInicio, fechaFin);
					 Messagebox.show(
							  "4",
							     "Informacion", Messagebox.OK,
							   Messagebox.INFORMATION);	
				 }
				 if(nombrePrograma.equals("Todos") && !estatus.equals("Todos")){
					 teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(estatus, fechaInicio, 
							         fechaFin);
					 Messagebox.show(
							  "5",
							     "Informacion", Messagebox.OK,
							   Messagebox.INFORMATION);
				 }
				 
				 if(nombrePrograma.equals("Todos") && estatus.equals("Todos")){
					 String estatusTeg1="Proyecto Registrado";
					 String estatusTeg2="Proyecto Factible";
					 String estatusTeg3="Proyecto deTrabajo Especial de Grado en Desarrollo";
					 String estatusTeg4="Avances Finalizados del Proyecto";
					 teg = servicioTeg.buscarTegPorVariosProgramasVariosEstatus1(estatusTeg1, estatusTeg2, estatusTeg3, estatusTeg4, fechaInicio, fechaFin);
					 Messagebox.show(
							  "6",
							     "Informacion", Messagebox.OK,
							   Messagebox.INFORMATION);
					
				 }
		
				 List<ElementoReporte> elementos = new ArrayList<ElementoReporte>();
				 for (Teg t : teg) {
				 List<Estudiante> estudiantes= servicioEstudiante.buscarEstudiantePorTeg(t);
				 
						String nombreEstudiantes = "";
						for (Estudiante e : estudiantes) {
						nombreEstudiantes += e.getNombre() +" "+e.getApellido()+" ";
						}
					
				 elementos.add(new ElementoReporte(t, nombreEstudiantes));
				 }
				    FileSystemView filesys = FileSystemView.getFileSystemView();
					Map p = new HashMap();
				    p.put("programa", cmbPrograma.getValue());
					p.put("FechaInicio",dtbFechaInicio.getValue());
					p.put("FechaFin",dtbFechaFin.getValue());
					p.put("Area",cmbArea.getValue());
					p.put("Tematica",cmbTematica.getValue());
					p.put("Estatus",cmbEstatus.getValue());
					JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("/reporte/ReporteTEG.jasper"));
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, new JRBeanCollectionDataSource(elementos));
					JasperExportManager.exportReportToPdfFile(jasperPrint, filesys.getHomeDirectory().toString()+"/ListaProyecto.pdf"); 
		}
	
	}

	@Listen("onClick = #btnSalirReporteProyecto")
	public void cancelarItem() {
		cmbEstatus.setValue("");
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		//wdwReporteTeg.onClose();
		
	}
}
