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
import modelo.Estudiante;
import modelo.Programa;
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

import servicio.SEstudiante;
import servicio.SLapso;
import servicio.SPrograma;
import configuracion.GeneradorBeans;
import controlador.CGeneral;
@Controller
public class CReporteEstudiantes extends CGeneral {
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	
	@Wire
	private Combobox cmbProgramaEstudiante;
	@Wire
	private Window wdwReporteEstudiantes;
	@Wire
	private Jasperreport jstVistaPrevia;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();;
		
		cmbProgramaEstudiante.setModel(new ListModelList<Programa>(
				programas));
		
	}

	@Listen("onClick = #btnGenerarReporteEstudiantes")
	 public void GenerarListado() throws JRException, IOException{
		if ((cmbProgramaEstudiante.getValue() =="")) {
			    Messagebox.show(
					  "Datos imcompletos",
					     "Informacion", Messagebox.OK,
					   Messagebox.INFORMATION);
		   }
		else{
		     Programa programa = servicioPrograma
				.buscar((Long.parseLong(cmbProgramaEstudiante
						.getSelectedItem().getId())));
		     FileSystemView filesys = FileSystemView.getFileSystemView();
		     List<Estudiante> estudiantes = servicioEstudiante.buscarEstudiantesPorPrograma(programa);
		     Map parametro = new HashMap();
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/ReporteEstudiantes.jasper";
			String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";

			System.out.println("ruta:"+reporteSrc);
				parametro.put("programa", cmbProgramaEstudiante.getValue());
				parametro.put("logoUcla", reporteImage + "logo ucla.png");
				parametro.put("logoCE", reporteImage + "logo CE.png");
				parametro.put("logoSiteg", reporteImage + "logo.png");
				jstVistaPrevia.setSrc(reporteSrc);
				jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
						estudiantes));
				jstVistaPrevia.setType("pdf");
				jstVistaPrevia.setParameters(parametro);
		}
		
	}
	@Listen("onClick = #btnCancelarReporteEstudiantes")
   public void Salir(){
		wdwReporteEstudiantes.onClose();
   
   }
	
	
	}


