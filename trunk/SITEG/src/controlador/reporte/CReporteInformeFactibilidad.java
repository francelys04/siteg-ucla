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

import modelo.Defensa;
import modelo.Tematica;
import modelo.AreaInvestigacion;
import modelo.Programa;
import modelo.Teg;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

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

import com.sun.xml.internal.ws.wsdl.parser.ParserUtil;

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
	
	private String[] estatusproyectos = {"Proyecto Factible","Proyecto No Factible"};
	
	long id = 0;

/*	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	Tematica tematica = new Tematica();
	Teg teg = new Teg();*/
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(10000000, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbProgramaFactibilidad.setModel(new ListModelList<Programa>(programas));
		cmbTipoFactibilidad.setModel(new ListModelList<String>(estatusproyectos));
		System.out.println(estatusproyectos[1]);
		
		Selectors.wireComponents(comp, this, false);
		
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		
		if (map != null) {
			/*if (map.get("id") != null){
				txtProyecto.setValue((String) map.get("id"));
				}*/
		if ((Long) map.get("id") != null) {
			id = ((Long) map.get("id"));
			/*
			 * Creacion de objeto cargado con el servicioPrograma mediante
			 * el metodo buscar dado a su id y asi llenar los textbox de la
			 * vista VPrograma
			 */
			Teg teg = servicioTeg.buscarTeg(id);
			
			txtProyecto.setValue(String.valueOf(teg.getId()));
			map.clear();
			map = null;
		}	
		}
	}

/*	@Listen("onClick = #btnGenerarReporteInformeFactibilidad")
	 public void GenerarInforme() throws JRException, IOException{
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		String nombreTipo = cmbTipo.getValue();
		
		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		fechaInicio = dtbFechaInicio.getValue();
		fechaFin = dtbFechaFin.getValue();
		
		
		if ((cmbPrograma.getValue() =="") || (cmbArea.getValue()=="") || (cmbTematica.getValue()==""))
				|| (cmbTipo.getValue()=="") {
			    Messagebox.show(
					  "Datos imcompletos",
					     "Informacion", Messagebox.OK,
					   Messagebox.INFORMATION);
		   }
		else{
			List<Teg> TegProyectoFactible = new ArrayList();
			System.out.println("das:"+TegProyectoFactible.size());
			
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				Messagebox.show(
						"La fecha de inicio debe ser primero que la fecha de fin",
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
				
		     AreaInvestigacion area = servicioArea.buscarArea(Long
				.parseLong(cmbArea.getSelectedItem().getId()));
		     Programa programa = servicioPrograma
				.buscar((Long.parseLong(cmbPrograma
						.getSelectedItem().getId())));
		     FileSystemView filesys = FileSystemView.getFileSystemView();
		     List<Cronograma> cronograma = servicioCronograma.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		     Map p = new HashMap();
		
		     p.put("programa", cmbPrograma.getValue());
		     System.out.println(cmbPrograma.getValue());
		     p.put("area", cmbArea.getValue());	
		     JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("SITEG/WebContent/vistas/reportes/estructurados/compilados/Factible.jasper"));
		    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, new JRBeanCollectionDataSource(programa));
		     JasperExportManager.exportReportToPdfFile(jasperPrint, filesys.getHomeDirectory().toString()+"SITEG/WebContent/vistas/reportes/estructurados/diseños/Factible.jrxml"); 
		     Messagebox.show(
				"Se ha generado exitosamente el reporte",
				"Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
		}
		
	}*/
	
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
			cmbArea.setDisabled(false);
			Programa programa = servicioPrograma.buscar(Long.parseLong(idPrograma));
			List<AreaInvestigacion> programaAreas = servicioProgramaArea.buscarAreasDePrograma(programa);
			AreaInvestigacion areaInvestigacion = new AreaInvestigacion(1000000, "Todos", "", true);
			programaAreas.add(areaInvestigacion);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(programaAreas));
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
	
	// Metodo que permite abrir la vista VCatalago en forma modal //
		@Listen("onClick = #btnCatalogoProyectoTeg")
		public void buscarProyecto() {
			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoReporteInformeFact.zul", null, null);
			window.doModal();
			catalogo.recibir("reportes/estructurados/VReporteInformeFactibilidad");

		}
	
	@Listen("onClick = #btnCancelarReporteInformeFactibilidad")
   public void Salir(){
		wdwReporteInformeFactibilidad.onClose();
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
	
	
}


