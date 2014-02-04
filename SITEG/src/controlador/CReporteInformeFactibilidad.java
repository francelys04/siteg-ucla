package controlador;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SCronograma;
import servicio.SLapso;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STematica;
import configuracion.GeneradorBeans;
@Controller
public class CReporteInformeFactibilidad extends CGeneral {
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProgramaArea servicioProgramaArea = GeneradorBeans.getServicioProgramaArea();
	
	
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox  cmbArea;
	@Wire
	private Combobox  cmbTematica;
	@Wire
	private Combobox  cmbTipo;
	@Wire
	private Datebox  dtbFechaInicio;
	@Wire
	private Datebox  dtbFechaFin;
	@Wire
	private Window wdwReporteInformeFactibilidad;


	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		List<AreaInvestigacion> areas = servicioArea.buscarActivos();
		
		cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		cmbPrograma.setModel(new ListModelList<Programa>(
				programas));
		
	}

	@Listen("onClick = #btnGenerarReporteInformeFactibilidad")
	 public void GenerarInforme() throws JRException, IOException{
		if ((cmbPrograma.getValue() =="") || (cmbArea.getValue()=="")) {
			    Messagebox.show(
					  "Datos imcompletos",
					     "Informacion", Messagebox.OK,
					   Messagebox.INFORMATION);
		   }
		else{
		     AreaInvestigacion area = servicioArea.buscarArea(Long
				.parseLong(cmbArea.getSelectedItem().getId()));
		     Programa programa = servicioPrograma
				.buscar((Long.parseLong(cmbPrograma
						.getSelectedItem().getId())));
		     FileSystemView filesys = FileSystemView.getFileSystemView();
		    /* List<Cronograma> cronograma = servicioCronograma.buscarCronogramaPorLapsoYPrograma(programa, lapso);*/
		     Map p = new HashMap();
		
		     p.put("programa", cmbPrograma.getValue());
		     System.out.println(cmbPrograma.getValue());
		     p.put("area", cmbArea.getValue());	
		   /*  JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("SITEG/WebContent/vistas/reportes/estructurados/compilados/Factible.jasper"));
		    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, new JRBeanCollectionDataSource(programa));
		     JasperExportManager.exportReportToPdfFile(jasperPrint, filesys.getHomeDirectory().toString()+"SITEG/WebContent/vistas/reportes/estructurados/diseños/Factible.jrxml");*/ 
		     Messagebox.show(
				"Se ha generado exitosamente el reporte",
				"Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
		}
		
	}
	@Listen("onClick = #btnCancelarReporteInformeFactibilidad")
   public void Salir(){
		wdwReporteInformeFactibilidad.onClose();
   
   }
	
	
	}


