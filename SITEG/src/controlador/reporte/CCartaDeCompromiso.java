package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Estudiante;
import modelo.SolicitudTutoria;
import modelo.reporte.Proyecto;
import modelo.reporte.Solicitud;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import servicio.SRequisito;

import controlador.CGeneral;


@Controller
public class CCartaDeCompromiso extends CGeneral {
	
	@Wire
	private Window wdwCarta;	
	@Wire
	private Jasperreport jstVistaPrevia;
	
	@Override
	public void inicializar(Component comp)  {
		 
	}
		
	
	@Listen("onClick = #btnGenerar")
	public void carta() throws JRException{
		
	
	Estudiante estudiante= ObtenerUsuarioEstudiante();
	SolicitudTutoria solicitud = servicioSolicitudTutoria.buscarSolicitudAceptadaEstudiante(estudiante);
	

	
		String nombre = solicitud.getProfesor().getNombre();
		String apellido = solicitud.getProfesor().getApellido();
		String cedula = solicitud.getProfesor().getCedula();
		String titulo = solicitud.getDescripcion();
		String cedulae = estudiante.getCedula();
		String nombree = estudiante.getNombre();
		String apellidoe = estudiante.getApellido();
		Date fecha = solicitud.getFecha();
		List<Solicitud> elementos = new ArrayList<Solicitud>();
		elementos.add(new Solicitud(titulo,fecha,cedula,nombre,apellido,cedulae,apellidoe,nombree));
		
		
	
	
	Map<String, Object> mapa = new HashMap<String, Object>();
	
	// Metodo utilizado para los que de error el preview
	FileSystemView filesys = FileSystemView
			.getFileSystemView();
	String rutaUrl = obtenerDirectorio();
	System.out.println(rutaUrl);
	String reporteSrc = rutaUrl
			+ "SITEG/vistas/reportes/estructurados/compilados/CartadeCompromiso.jasper";
	String reporteImage = rutaUrl
			+ "SITEG/public/imagenes/reportes/";
	


	//JasperReport jasperReport = (JasperReport) JRLoader
		//	.loadObject(reporteSrc);

	//JasperPrint jasperPrint = JasperFillManager.fillReport(
		//	jasperReport,null,
			//new JRBeanCollectionDataSource(elementos));

	//JasperViewer.viewReport(jasperPrint, false);
	jstVistaPrevia.setSrc(reporteSrc);
	 jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
			 elementos));
	 jstVistaPrevia.setType("pdf");
	 jstVistaPrevia.setParameters(null);

  }
	@Listen("onClick = #btnSalir")
	public void salir() throws JRException {
		
		wdwCarta.onClose();
	}
	
	
	

}
