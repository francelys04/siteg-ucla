package controlador.reporte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Categoria;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Tematica;
import modelo.AreaInvestigacion;
import modelo.Programa;
import modelo.Teg;
import modelo.compuesta.id.ProgramaAreaId;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Iframe;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import antlr.Parser;
import servicio.SAreaInvestigacion;
import servicio.SCronograma;
import servicio.SLapso;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STematica;
import configuracion.GeneradorBeans;
import controlador.CGeneral;
import controlador.catalogo.CCatalogoPrograma;
import controlador.catalogo.CCatalogoReporteInformeFact;
@Controller
public class CReporteInformeFactibilidad extends CGeneral {
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProgramaArea servicioProgramaArea = GeneradorBeans.getServicioProgramaArea();
	CCatalogoReporteInformeFact catalogo = new CCatalogoReporteInformeFact();
	
	@Wire
	private Window wdwReporteInformeFactibilidad;
	@Wire
	private Combobox cmbProgramaFactibilidad;
	@Wire
	private Combobox  cmbArea;
	@Wire
	private Combobox  cmbTematica;
	@Wire
	private Combobox  cmbTipoFactibilidad;
	@Wire
	private Datebox  dtbFechaInicio;
	@Wire
	private Datebox  dtbFechaFin;
	@Wire
	private Textbox  txtProyecto;
	@Wire
	private Jasperreport jstVistaPrevia;

	private Connection con;
	 
	private String[] estatusproyectos = {"Proyecto Factible","Proyecto No Factible"};
	
	long id = 0;


    private String reporteFact;
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		cmbArea.setDisabled(false);
		cmbTematica.setDisabled(false);
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(10000000, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbProgramaFactibilidad.setModel(new ListModelList<Programa>(programas));
		cmbTipoFactibilidad.setModel(new ListModelList<String>(estatusproyectos));

		
		Selectors.wireComponents(comp, this, false);
		
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions.getCurrent().getAttribute("itemsCatalogo");
		
		if (map != null) {
		/*if (map.get("id") != null){
				txtProyecto.setValue((String) map.get("id"));
				}*/
		if ((Long) map.get("id") != null) {
			id = ((Long) map.get("id"));
			/*
			 * Creacion de objeto cargado con el servicioPrograma mediante
			 * el metodo buscar dado a su id y asi llenar los textbox de la
			 * vista VReporteInformeFactibilidad
			 */
			Teg teg = servicioTeg.buscarTeg(id);
			List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg);
			txtProyecto.setValue(String.valueOf(teg.getId()));
			/*Programa programa = servicioPrograma.buscar((Long) map
					.get("programa"));*/
			cmbProgramaFactibilidad.setValue(est.get(0).getPrograma().getNombre());
			cmbProgramaFactibilidad.setModel(new ListModelList<Programa>(programas));
			cmbArea.setValue(teg.getTematica().getareaInvestigacion().getNombre());
			cmbTematica.setValue(teg.getTematica().getNombre());
			/*cmbTipoFactibilidad.setModel(new ListModelList<String>(estatusproyectos));*/
			cmbTipoFactibilidad.setValue(teg.getEstatus());
			dtbFechaInicio.setValue(teg.getFechaInicio());
			dtbFechaFin.setValue(teg.getFechaEntrega());
			map.clear();
			map = null;
			
		}	
		}
	}
	
	@Listen("onSelect = #cmbProgramaFactibilidad")
	public void seleccionarPrograma() throws JRException {
		String idPrograma = cmbProgramaFactibilidad.getSelectedItem().getId();
		String nombrePrograma = cmbProgramaFactibilidad.getValue();
		if (nombrePrograma.equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
			cmbArea.setDisabled(true);
			cmbTematica.setDisabled(true);

		} else {
			Programa programa = servicioPrograma.buscar(Long.parseLong(idPrograma));
			List<AreaInvestigacion> programaAreas = servicioProgramaArea.buscarAreasDePrograma(programa);
			AreaInvestigacion areaInvestigacion = new AreaInvestigacion(1000000, "Todos", "", true);
			programaAreas.add(areaInvestigacion);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(programaAreas));
			cmbArea.setDisabled(false);
			cmbTematica.setDisabled(false);
		}
	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() throws JRException {
		String idArea = cmbArea.getSelectedItem().getId();
		String nombreArea = cmbArea.getValue();
		if (nombreArea.equals("Todos")) {
			cmbTematica.setValue("Todos");
			cmbTematica.setDisabled(true);
		} else {
			cmbTematica.setDisabled(false);
			AreaInvestigacion area = servicioArea.buscarArea(Long.parseLong(idArea));
			List<Tematica> tematicasTodos = servicioTematica
					.buscarTematicasDeArea(area);
			Tematica tematicaTodos = new Tematica(10000000, "Todos", "", true,
					null);
			tematicasTodos.add(tematicaTodos);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicasTodos));
		}
	}
	
	/*@Listen("onSelect = #cmbTematica")
	public void tomarIdTematica() {
		idTematica = Long.parseLong(cmbTematica.getSelectedItem().getId());
	}*/

	
	// Metodo que permite abrir la vista VCatalagoinformefact en forma modal //
		@Listen("onClick = #btnCatalogoProyectoTeg")
		public void buscarProyecto() {
			
			if ((cmbProgramaFactibilidad.getValue() =="") || (cmbArea.getValue()=="") || (cmbTematica.getValue()=="")
					|| (cmbTipoFactibilidad.getValue()=="")) {
				    Messagebox.show(
						  "Datos imcompletos",
						     "Informacion", Messagebox.OK,
						   Messagebox.INFORMATION);
		   }else {
			   	String nombrePrograma = cmbProgramaFactibilidad.getValue();
				String nombreArea = cmbArea.getValue();
				String nombreTematica = cmbTematica.getValue();
				String nombreTipoFact = cmbTipoFactibilidad.getValue();
				Date fechaInicio = dtbFechaInicio.getValue();
				Date fechaFin = dtbFechaFin.getValue();
								
				Window window = (Window) Executions.createComponents(
						"/vistas/catalogos/VCatalogoReporteInformeFact.zul", null, null);
				window.doModal();
				catalogo.recibir("reportes/estructurados/VReporteInformeFactibilidad");
		   }
			
			
			
			
			/*else{
				List<Teg> TegProyectoFactible = new ArrayList();
				System.out.println("das:"+TegProyectoFactible.size());
				
				if (fechaFin == null || fechaInicio == null
						|| fechaInicio.after(fechaFin)) {
					Messagebox.show(
							"La fecha de inicio debe ser menor que la fecha de fin",
							"Error", Messagebox.OK, Messagebox.ERROR);
					
					} else {
						if (nombrePrograma.equals("Todos")) {
							
							TegProyectoFactible = servicioTeg.buscarTegSegunEstatus(estatus,fechaInicio,fechaFin);
						}
						else
						if (!nombrePrograma.equals("Todos") && nombreArea.equals("Todos")) {
							TegProyectoFactible=servicioTeg.buscarDefensaTegSegunEstatusPrograma(estatus, programa, fechaInicio, fechaFin);
							}
						else
						if (!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && !nombreTematica.equals("Todos")) {
							    Tematica tematica= servicioTematica.buscarTematicaPorNombre(nombreTematica);
							    TegProyectoFactible=servicioTeg.buscarDefensaTegSegunEstatusTematica(estatus, tematica, fechaInicio, fechaFin);
							}else
						if(!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && nombreTematica.equals("Todos")){
							TegProyectoFactible=servicioTeg.buscarDefensaTegSegunEstatusArea(estatus, area, fechaInicio, fechaFin);
							}

					}
				}
*/		
		}
		
		
	 	@Listen("onClick = #btnGenerarReporteInformeFactibilidad")
		 public void GenerarInforme()  {	
	 		if (txtProyecto.getText().equals("")){ 
				 Messagebox.show( "Debe seleccionar un Proyecto",
						     "Error", Messagebox.OK,Messagebox.INFORMATION);
				 System.out.println("Entro");
			}else {
	 		
		 		Teg teg = servicioTeg.buscarTeg(id);
			    Long proyecto = teg.getId();
			    String estatus = teg.getEstatus();
			    List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg);
			    String ndirector = est.get(0).getPrograma().getDirectorPrograma().getNombre();
			    String adirector = est.get(0).getPrograma().getDirectorPrograma().getApellido();
			    
				if (estatus.equals("Proyecto Factible")){
			    	reporteFact = "SITEG/vistas/reportes/estructurados/compilados/ReporteInformeFactible.jasper";
					 System.out.println(estatus);
				} else{
						if (estatus.equals("Proyecto No Factible")){
							reporteFact = "SITEG/vistas/reportes/estructurados/compilados/ReporteInformeNoFactible.jasper";
					}
				}
				   
			    try {
					Class.forName("org.postgresql.Driver");
					con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/siteg","postgres","equipo2");
				}catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			    
			    try {
				    FileSystemView filesys = FileSystemView.getFileSystemView();
				    Map p = new HashMap();
					String rutaUrl = obtenerDirectorio();
					
					String reporteSrc = rutaUrl + reporteFact;
					String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
					p.put("fecha", new Date().toLocaleString());
				    p.put("proyecto", proyecto);
				    p.put("ndirector", ndirector);
				    p.put("adirector", adirector);
				    p.put("logoUcla", reporteImage + "logo ucla.png");
				 	p.put("logoCE", reporteImage + "logo CE.png");
				    p.put("logoSiteg", reporteImage + "logo.png");			    
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, con);
				    JasperViewer.viewReport(jasperPrint);
				    con.close();
					
				    }catch (JRException | SQLException e) {
						e.printStackTrace();
				}
			}
	 	} 
	
	@Listen("onClick = #btnCancelarReporteInformeFactibilidad")
   public void CancelarInformeFactibilidad(){
		cmbProgramaFactibilidad.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setValue("");
		cmbTematica.setDisabled(true);
		cmbTipoFactibilidad.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		jstVistaPrevia.setSrc("");
		jstVistaPrevia.setDatasource(null);

   }
	
	@Listen("onClick = #btnSalirReporteInformeFactibilidad")
	public void salirPromedioTiempoTeg() throws JRException {
		CancelarInformeFactibilidad();
		wdwReporteInformeFactibilidad.onClose();
	}
	
	
}


