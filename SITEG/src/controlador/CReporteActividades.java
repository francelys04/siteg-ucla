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

import modelo.Actividad;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import servicio.SActividad;
import configuracion.GeneradorBeans;
@Controller
public class CReporteActividades extends CGeneral {
	SActividad servicioActividad = GeneradorBeans.getServicioActividad();

	
	@Wire
	private Window wdwReporteActividades;


	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Actividad> actividades = servicioActividad.buscarActivos();
		
		
	}

	@Listen("onClick = #btnGenerarReporteActividades")
	 public void GenerarActividades() throws JRException, IOException{
		
		
		     FileSystemView filesys = FileSystemView.getFileSystemView();
		     List<Actividad> actividades = servicioActividad.buscarActivos();
		     String rutaUrl = obtenerDirectorio();
		    /* String reporteSrc = rutaUrl
					 +
					 "SITEG/vistas/reportes/estructurados/compilados/RActividades.jasper";*/
		     String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
		     Map p = new HashMap();
		     p.put("logoUcla", reporteImage + "logo ucla.png");
			 p.put("logoCE", reporteImage + "logo CE.png");
		     
		     //p.put("nombre", );
		     //System.out.println(cmbCronogramaPrograma.getValue());
		     //p.put("lapso", cmbCronogramaLapso.getValue());	
		     JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResource("/reporte/RActividades.jasper"));
		     JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,p, new JRBeanCollectionDataSource(actividades));
		     JasperExportManager.exportReportToPdfFile(jasperPrint, filesys.getHomeDirectory().toString()+"/ReporteActividades.pdf"); 
		     Messagebox.show(
				"Se ha generado exitosamente el reporte",
				"Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
		}
		
	@Listen("onClick = #btnCancelarReporteCronograma")
   public void Salir(){
	   wdwReporteActividades.onClose();
   
   }
	
	
	}


