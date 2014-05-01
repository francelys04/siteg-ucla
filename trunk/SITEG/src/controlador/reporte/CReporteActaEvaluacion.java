package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Estudiante;
import modelo.Teg;
import modelo.reporte.InformeFactibilidad;
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
public class CReporteActaEvaluacion extends CGeneral {

	@Wire
	private Window wdwReporteActaEvaluacion;
	private static Teg ultimoTeg;
	private static Estudiante estudiante;
	private String actaEvaluacion;

	@Override
	public void inicializar(Component comp) {

		estudiante = ObtenerUsuarioEstudiante();

		if (estudiante != null) {

			ultimoTeg = servicioTeg.ultimoTeg(estudiante);

			if (ultimoTeg != null) {

				if (!ultimoTeg.getEstatus().equals("TEG Aprobado")
						&& !ultimoTeg.getEstatus().equals("TEG Reprobado")) {

					Messagebox
							.show("Para generar el acta de evaluacion el TEG debe estar calificado",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);
					wdwReporteActaEvaluacion.onClose();

				}

			}else {

				Messagebox
						.show("Debe poseer un trabajo especial de grado asociado",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
				wdwReporteActaEvaluacion.onClose();

			}

		} else {

			Messagebox.show(
					"No tiene permisos para generar el acta de evaluacion",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
			wdwReporteActaEvaluacion.onClose();
		}
	}

	@Listen("onClick = #btnGenerar")
	public void carta() throws JRException {

		try {
			if (ultimoTeg != null) {

				if (ultimoTeg.getEstatus().equals("TEG Aprobado")) {

					Long proyecto = ultimoTeg.getId();
					String estatus = "Aprobado";

					String tutor = ultimoTeg.getTutor().getNombre() + " "
							+ ultimoTeg.getTutor().getApellido();
					String nestudiante = estudiante.getNombre() + " "
							+ estudiante.getApellido();
					String cestudiante = estudiante.getCedula();
					String titulo = ultimoTeg.getTitulo();
					String observacion = ultimoTeg.getFactibilidad()
							.getObservacion();
					String programateg = estudiante.getPrograma().getNombre();
					String director = estudiante.getPrograma()
							.getDirectorPrograma().getNombre()
							+ " "
							+ estudiante.getPrograma().getDirectorPrograma()
									.getApellido();
					List<InformeFactibilidad> elementos = new ArrayList<InformeFactibilidad>();
					elementos.add(new InformeFactibilidad(estatus, nestudiante,
							cestudiante, titulo, programateg, tutor,
							observacion, director));


					Map<String, Object> p = new HashMap<String, Object>();
					
					// Metodo utilizado para los que de error el preview
					FileSystemView filesys = FileSystemView.getFileSystemView();
					String rutaUrl = obtenerDirectorio();
					System.out.println(rutaUrl);
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estructurados/compilados/ReporteActaFinal.jasper";
					String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";

					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									elementos));
					JasperViewer.viewReport(jasperPrint, false);

				} else {
					if (ultimoTeg.getEstatus().equals("TEG Reprobado")) {

						Long proyecto = ultimoTeg.getId();
						String estatus = "Aplazado";

						String tutor = ultimoTeg.getTutor().getNombre() + " "
								+ ultimoTeg.getTutor().getApellido();
						String nestudiante = estudiante.getNombre() + " "
								+ estudiante.getApellido();
						String cestudiante = estudiante.getCedula();
						String titulo = ultimoTeg.getTitulo();
						String observacion = ultimoTeg.getFactibilidad()
								.getObservacion();
						String programateg = estudiante.getPrograma().getNombre();
						String director = estudiante.getPrograma()
								.getDirectorPrograma().getNombre()
								+ " "
								+ estudiante.getPrograma().getDirectorPrograma()
										.getApellido();
						List<InformeFactibilidad> elementos = new ArrayList<InformeFactibilidad>();
						elementos.add(new InformeFactibilidad(estatus, nestudiante,
								cestudiante, titulo, programateg, tutor,
								observacion, director));


						Map<String, Object> p = new HashMap<String, Object>();
						
						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						System.out.println(rutaUrl);
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteActaFinal.jasper";
						String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";

						p.put("logoUcla", reporteImage + "logo ucla.png");
						p.put("logoCE", reporteImage + "logo CE.png");
						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, p, new JRBeanCollectionDataSource(
										elementos));
						JasperViewer.viewReport(jasperPrint, false);

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("");
		}

	}

	@Listen("onClick = #btnSalir")
	public void salir() throws JRException {

		wdwReporteActaEvaluacion.onClose();
	}

}
