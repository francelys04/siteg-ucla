package controlador.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import modelo.Tematica;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;

import servicio.SEstudiante;
import servicio.SPrograma;
import servicio.SSolicitudTutoria;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CReporteSolicitudes extends CGeneral {

	long idTematica = 0;
	private static Date fechaInicio;
	private static Date fechaFin;

	private String[] estatusSolicitud = { "Aceptada", "Rechazada",
			"Por Revisar", "Todos" };
	private static Programa programa1;
	private static AreaInvestigacion area1;
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	SSolicitudTutoria servicioSolicitud = GeneradorBeans.getServicioTutoria();
	List<SolicitudTutoria> solicitud = new ArrayList<SolicitudTutoria>();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	private static long idarea;
	@Wire
	private Datebox dtbInicioReporteSolicitud;
	@Wire
	private Datebox dtbFinReporteSolicitud;
	@Wire
	private Combobox cmbProgramaReporteSolicitud;
	@Wire
	private Combobox cmbAreaReporteSolicitud;
	@Wire
	private Combobox cmbTematicaReporteSolicitud;
	@Wire
	private Combobox cmbEstatusReporteSolicitud;
	@Wire
	private Window wdwReporteSolicitudesTutoria;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos" y se llena una lista del mismo en el
	 * componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		cmbEstatusReporteSolicitud.setModel(new ListModelList<String>(
				estatusSolicitud));

		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas = servicioPrograma.buscarActivas();
		programas.add(programaa);
		cmbProgramaReporteSolicitud.setModel(new ListModelList<Programa>(
				programas));

	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas
	 */
	@Listen("onSelect =  #cmbProgramaReporteSolicitud")
	public void seleccionarPrograma() {
		if (cmbProgramaReporteSolicitud.getValue().equals("Todos")) {
			cmbAreaReporteSolicitud.setValue("Todos");
			cmbTematicaReporteSolicitud.setValue("Todos");
			areas = servicioArea.buscarActivos();
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);

		} else {
			cmbAreaReporteSolicitud.setValue("");
			cmbTematicaReporteSolicitud.setValue("");
			programa1 = (Programa) cmbProgramaReporteSolicitud
					.getSelectedItem().getValue();
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(programa1.getId()));
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbAreaReporteSolicitud
					.setModel(new ListModelList<AreaInvestigacion>(areas));

		}
	}

	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica, ademas se adiciona un nuevo item donde se
	 * puede seleccionar la opcion de "Todos" en el combo de las tematicas.
	 */
	@Listen("onSelect = #cmbAreaReporteSolicitud")
	public void seleccionarArea() {
		if (cmbAreaReporteSolicitud.getValue().equals("Todos")) {

			cmbTematicaReporteSolicitud.setValue("Todos");
			tematicas = servicioTematica.buscarActivos();
			Tematica tema = new Tematica(10000, "Todos", "", true, null);
			tematicas.add(tema);
			cmbTematicaReporteSolicitud.setModel(new ListModelList<Tematica>(
					tematicas));
		} else {
			cmbTematicaReporteSolicitud.setValue("");
			area1 = (AreaInvestigacion) cmbAreaReporteSolicitud
					.getSelectedItem().getValue();
			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(area1.getId()));
			Tematica tema = new Tematica(10000, "Todos", "", true, null);
			tematicas.add(tema);
			cmbTematicaReporteSolicitud.setModel(new ListModelList<Tematica>(
					tematicas));
		}
		idarea = Long.parseLong(cmbAreaReporteSolicitud.getSelectedItem()
				.getId());
	}

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematicaReporteSolicitud")
	public void seleccionarTematica() {
		if (cmbAreaReporteSolicitud.getValue().equals("Todos")) {
			Tematica tematica = (Tematica) cmbTematicaReporteSolicitud
					.getSelectedItem().getValue();
			cmbAreaReporteSolicitud.setValue(tematica.getareaInvestigacion()
					.getNombre());
			System.out.println("pase por el todo");

		}
		Tematica tematica = (Tematica) cmbTematicaReporteSolicitud
				.getSelectedItem().getValue();
		idTematica = tematica.getId();
	}

	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y estatus de solicitud, se generara un pdf donde se muestra una lista de
	 * las solicitudes de tutorias de esta seleccion, mediante el componente
	 * "Jasperreport" se mapea una serie de parametros y una lista previamente
	 * cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteSolicitud")
	public void GenerarReporte() throws JRException {

		if ((cmbProgramaReporteSolicitud.getText().compareTo("") == 0)
				|| (cmbAreaReporteSolicitud.getText().compareTo("") == 0)
				|| (cmbTematicaReporteSolicitud.getText().compareTo("") == 0)
				|| (cmbEstatusReporteSolicitud.getText().compareTo("") == 0)
				|| (dtbInicioReporteSolicitud.getValue() == null)
				|| (dtbFinReporteSolicitud.getValue() == null)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			fechaInicio = dtbInicioReporteSolicitud.getValue();
			fechaFin = dtbFinReporteSolicitud.getValue();

			if (fechaInicio.after(fechaFin)) {

				Messagebox.show(
						"La fecha de inicio no puede ser mayor a la fecha fin",
						"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				String nombreArea = cmbAreaReporteSolicitud.getValue();
				String nombrePrograma = cmbProgramaReporteSolicitud.getValue();
				String nombreTematica = cmbTematicaReporteSolicitud.getValue();
				String nombreEstatus = cmbEstatusReporteSolicitud.getValue();
				fechaInicio = dtbInicioReporteSolicitud.getValue();
				fechaFin = dtbFinReporteSolicitud.getValue();
				List<Solicitud> elementos = new ArrayList<Solicitud>();

				/*
				 * SELECIONO TODO TODO
				 */
				if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& nombreEstatus.equals("Todos")) {
					solicitud = servicioSolicitud
							.buscarTodasSolicitudesEntreFechas(fechaInicio,
									fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}

				/*
				 * seleccione un estatus y todo en todo
				 */
				else if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !nombreEstatus.equals("Todos")) {

					solicitud = servicioSolicitud
							.buscarSolicitudesPorEstatusEntreFechas(
									nombreEstatus, fechaInicio, fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud4.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccione uno de cada uno
				 */
				else if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& !nombreEstatus.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					solicitud = servicioSolicitud
							.buscarSolicitudesPorTematicaEstatusEntreFechas(
									tematica1, nombreEstatus, fechaInicio,
									fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud5.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccione area tematica y estatus
				 */
				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& !nombreEstatus.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					solicitud = servicioSolicitud
							.buscarSolicitudesPorTematicaEstatusEntreFechas(
									tematica1, nombreEstatus, fechaInicio,
									fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud3.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccione solo un area diferente
				 */
				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& nombreEstatus.equals("Todos")) {

					AreaInvestigacion area1 = servicioArea.buscarArea(idarea);

					solicitud = servicioSolicitud
							.buscarSolicitudSegunAreaUnEstatus1(area1,
									fechaInicio, fechaFin, nombreEstatus);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud2.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}

				/*
				 * seleccione programa area tematica
				 */
				else if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& nombreEstatus.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					solicitud = servicioSolicitud
							.buscarSolicitudesPorTematicaEntreFechas(tematica1,
									fechaInicio, fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud5.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccionno solo programa y estatus
				 */
				else if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !nombreEstatus.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					solicitud = servicioSolicitud
							.buscarSolicitudPorProgramaUnEstatus1(
									nombreEstatus, programa1, fechaInicio,
									fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccionan solo programa
				 */
				else if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& nombreEstatus.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					solicitud = servicioSolicitud.buscarSolicitudPorPrograma(
							programa1, fechaInicio, fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud1.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccionan solo tematica
				 */
				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& nombreEstatus.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					solicitud = servicioSolicitud
							.buscarSolicitudesPorTematicaEntreFechas(tematica1,
									fechaInicio, fechaFin);
					if (solicitud.size() != 0) {
						elementos.clear();
						for (SolicitudTutoria s : solicitud) {

							String titulo = s.getDescripcion();
							String tutor = s.getProfesor().getNombre() + " "
									+ s.getProfesor().getApellido() + " ";
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(s);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String tematica = s.getTematica().getNombre();
							String area = s.getTematica()
									.getareaInvestigacion().getNombre();
							String estatus = s.getEstatus();
							elementos.add(new Solicitud(titulo, tutor,
									nombreEstudiante, programa, tematica, area,
									estatus));

						}
						Map<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("fechainicio", fechaInicio);
						mapa.put("fechafin", fechaFin);

						// Metodo utilizado para los que de error el preview
						FileSystemView filesys = FileSystemView
								.getFileSystemView();
						String rutaUrl = obtenerDirectorio();
						String reporteSrc = rutaUrl
								+ "SITEG/vistas/reportes/estructurados/compilados/ReporteSolicitud3.jasper";
						String reporteImage = rutaUrl
								+ "SITEG/public/imagenes/reportes/";

						JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);

						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(elementos));

						JasperViewer.viewReport(jasperPrint, false);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}

			}

		}
	}

	/* Metodo que permite limpiar los campos de los filtros de busqueda */
	@Listen("onClick = #btnCancelarReporteSolicitud")
	public void cancelar() throws JRException {

		cmbProgramaReporteSolicitud.setValue("");
		cmbAreaReporteSolicitud.setValue("");
		cmbEstatusReporteSolicitud.setValue("");
		cmbTematicaReporteSolicitud.setValue("");
		dtbInicioReporteSolicitud.setValue(new Date());
		dtbFinReporteSolicitud.setValue(new Date());

	}

	/* Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirReporteSolicitud")
	public void salirReporteSolicitud() throws JRException {

		wdwReporteSolicitudesTutoria.onClose();

	}

}
