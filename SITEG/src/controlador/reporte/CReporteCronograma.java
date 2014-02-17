package controlador.reporte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.Cronograma;
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
import net.sf.jasperreports.view.JasperViewer;

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

import servicio.SCronograma;
import servicio.SLapso;
import servicio.SPrograma;
import configuracion.GeneradorBeans;
import controlador.CGeneral;
@Controller
public class CReporteCronograma extends CGeneral {
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SCronograma servicioCronograma = GeneradorBeans.getServicioCronograma();
	
	@Wire
	private Combobox cmbCronogramaPrograma;
	@Wire
	private Combobox  cmbCronogramaLapso;
	@Wire
	private Window wdwReporteCronograma;
	@Wire
	private Jasperreport jstVistaPrevia;


	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		List<Lapso> lapsos = servicioLapso.buscarActivos();
		
		cmbCronogramaLapso.setModel(new ListModelList<Lapso>(lapsos));
		cmbCronogramaPrograma.setModel(new ListModelList<Programa>(
				programas));
		
	}

	@Listen("onClick = #btnGenerarReporteCronograma")
	 public void GenerarCronograma() throws JRException, IOException{
		if ((cmbCronogramaPrograma.getValue() =="") || (cmbCronogramaLapso.getValue()=="")) {
			    Messagebox.show(
					  "Debe completar todos los campos",
					     "Error", Messagebox.OK,
					   Messagebox.ERROR);
		   }
		else{
		     Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbCronogramaLapso.getSelectedItem().getId()));
		     Programa programa = servicioPrograma
				.buscar((Long.parseLong(cmbCronogramaPrograma
						.getSelectedItem().getId())));
		     FileSystemView filesys = FileSystemView.getFileSystemView();
		     List<Cronograma> cronograma = servicioCronograma.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		
		     if(cronograma.size()!=0){
		         Map<String, Object> p = new HashMap<String, Object>();
				 String rutaUrl = obtenerDirectorio();
				 String reporteSrc = rutaUrl
				  +
				  "SITEG/vistas/reportes/salidas/compilados/ReporteCronograma.jasper";
				  String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
		     
		          p.put("programa", cmbCronogramaPrograma.getValue());
		          p.put("lapso", cmbCronogramaLapso.getValue());	
		          p.put("logoUcla", reporteImage + "logo ucla.png");
		 	      p.put("logoCE", reporteImage + "logo CE.png");
		          p.put("logoSiteg", reporteImage + "logo.png"); 

		          JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					
					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									cronograma));
					JasperViewer.viewReport(jasperPrint, false);
		        /* jstVistaPrevia.setSrc(reporteSrc);
			     jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(cronograma));
			     jstVistaPrevia.setType("pdf");
			     jstVistaPrevia.setParameters(p); */
		      }
		     else{
		    	 Messagebox.show("No hay información disponible");
		    	 Cancelar();
		         }

		}
		
	}
	@Listen("onClick = #btnCancelarReporteCronograma")
	public void Cancelar(){
		cmbCronogramaPrograma.setValue("");
		cmbCronogramaLapso.setValue("");
	}
	
	@Listen("onClick = #btnSalirReporteCronograma")
    public void Salir(){
	   wdwReporteCronograma.onClose();
   
   }
	
	
	}


