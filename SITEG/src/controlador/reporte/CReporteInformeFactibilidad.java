package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.Tematica;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteInformeFactibilidad extends CGeneral {

	@Wire
	private Window wdwReporteInformeFactibilidad;

	private static TegEstatus tegestatus;
	private static Teg ultimoTeg;
	private static Estudiante estudiante;

	private String[] estatusproyectos = { "Proyecto Factible",
			"Proyecto No Factible" };

	long id = 0;

	private String reporteFact;
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		estudiante = ObtenerUsuarioEstudiante();

		if (estudiante != null) {

			ultimoTeg = servicioTeg.ultimoTeg(estudiante);

			if (ultimoTeg != null) {
				TegEstatus tegFactible = servicioTegEstatus.buscarTegEstatus(
						"Proyecto Factible", ultimoTeg);

				TegEstatus tegNoFactible = servicioTegEstatus.buscarTegEstatus(
						"Proyecto No Factible", ultimoTeg);

				if (tegFactible == null && tegNoFactible == null) {

					Messagebox
							.show("Para generar el informe de factibilidad, su proyecto debe tener la factibilidad evaluada",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);
					wdwReporteInformeFactibilidad.onClose();

				}

			}else {

				Messagebox
						.show("Debe poseer un proyecto de trabajo especial de grado asociado",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
				 wdwReporteInformeFactibilidad.onClose();

			}

		} else {

			Messagebox
					.show("No tiene permisos para generar el informe de factibilidad",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
			wdwReporteInformeFactibilidad.onClose();
		}
	}

	@Listen("onClick = #btnGenerarReporteInformeFactibilidad")
	public void GenerarInforme() throws JRException {

		try {
			if (ultimoTeg != null) {

				String estatus1 = "Proyecto Factible";

				tegestatus = servicioTegEstatus.buscarTegEstatus(estatus1,
						ultimoTeg);
				if (tegestatus != null) {

					Long proyecto = ultimoTeg.getId();
					String estatus = ultimoTeg.getEstatus();

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
							+ "SITEG/vistas/reportes/estructurados/compilados/ReporteInformeFactible.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									elementos));
					JasperViewer.viewReport(jasperPrint, false);

	

				} else {
					String estatus2 = "Proyecto No Factible";
					tegestatus = servicioTegEstatus.buscarTegEstatus(estatus2,
							ultimoTeg);
					if (tegestatus != null) {

						Long proyecto = ultimoTeg.getId();
						String estatus = ultimoTeg.getEstatus();

						String tutor = ultimoTeg.getTutor().getNombre() + " "
								+ ultimoTeg.getTutor().getApellido();
						String nestudiante = estudiante.getNombre() + " "
								+ estudiante.getApellido();
						String cestudiante = estudiante.getCedula();
						String titulo = ultimoTeg.getTitulo();
						String observacion = ultimoTeg.getFactibilidad()
								.getObservacion();
						String programateg = estudiante.getPrograma()
								.getNombre();
						String director = estudiante.getPrograma()
								.getDirectorPrograma().getNombre()
								+ " "
								+ estudiante.getPrograma()
										.getDirectorPrograma().getApellido();

						List<InformeFactibilidad> elementos = new ArrayList<InformeFactibilidad>();
						elementos.add(new InformeFactibilidad(estatus,
								nestudiante, cestudiante, titulo, programateg,
								tutor, observacion, director));
						
						Map<String, Object> p = new HashMap<String, Object>();

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						System.out.println(rutaUrl);
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteInformeNoFactible.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

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

	@Listen("onClick = #btnSalirReporteInformeFactibilidad")
	public void salirPromedioTiempoTeg() throws JRException {

		wdwReporteInformeFactibilidad.onClose();
	}

}
