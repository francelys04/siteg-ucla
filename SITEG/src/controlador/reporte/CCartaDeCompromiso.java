package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Estudiante;
import modelo.SolicitudTutoria;
import modelo.reporte.InformeFactibilidad;
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

import controlador.CGeneral;

@Controller
public class CCartaDeCompromiso extends CGeneral {

	@Wire
	private Window wdwCarta;
	@Wire
	private Jasperreport jstVistaPrevia;
	private static Estudiante estudiante;

	@Override
	public void inicializar(Component comp) {

		estudiante = ObtenerUsuarioEstudiante();

		if (estudiante != null) {

			SolicitudTutoria solicitud = servicioSolicitudTutoria
					.ultimaSolicitud(estudiante);

			if (!solicitud.getEstatus().equals("Aceptada")) {

				Messagebox
						.show("Para generar la carta de compromiso su solicitud de tutoria debe ser aceptada",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
				wdwCarta.onClose();

			}

		} else {

			Messagebox.show(
					"No tiene permisos para generar la carta de compromiso",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
			wdwCarta.onClose();
		}

	}

	@Listen("onClick = #btnGenerar")
	public void carta() throws JRException {

		try {
			Estudiante estudiante = ObtenerUsuarioEstudiante();
			SolicitudTutoria solicitud = servicioSolicitudTutoria
					.buscarSolicitudAceptadaEstudiante(estudiante);

			String nombreTutor = solicitud.getProfesor().getNombre() + " "
					+ solicitud.getProfesor().getApellido();
			String cedulaProfesor = solicitud.getProfesor().getCedula();
			String tituloTeg = solicitud.getDescripcion();
			String cedulaEstudiante = estudiante.getCedula();
			String nombreEstudiante = estudiante.getNombre() + " "
					+ estudiante.getApellido();

			List<Solicitud> elementos = new ArrayList<Solicitud>();
			elementos.add(new Solicitud(tituloTeg, cedulaProfesor, nombreTutor,
					cedulaEstudiante, nombreEstudiante));

			Map<String, Object> mapa = new HashMap<String, Object>();

			// Metodo utilizado para los que de error el preview
			FileSystemView filesys = FileSystemView.getFileSystemView();
			String rutaUrl = obtenerDirectorio();
			System.out.println(rutaUrl);
			String reporteSrc = rutaUrl
					+ "SITEG/vistas/reportes/estructurados/compilados/CartadeCompromiso.jasper";
			String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
			mapa.put("logoUcla", reporteImage + "logo ucla.png");
			mapa.put("logoCE", reporteImage + "logo CE.png");
			mapa.put("logoSiteg", reporteImage + "logo.png");

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(reporteSrc);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, mapa, new JRBeanCollectionDataSource(
							elementos));
			JasperViewer.viewReport(jasperPrint, false);

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("");
		}

	}

	@Listen("onClick = #btnSalir")
	public void salir() throws JRException {

		wdwCarta.onClose();
	}

}
