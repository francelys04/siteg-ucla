package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Tematica;
import modelo.reporte.MasSolicitados;
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

import controlador.CGeneral;

@Controller
public class CReporteProfesoresMasSolicitados extends CGeneral {

	private static final long serialVersionUID = 4396558516832165477L;
	private String[] estatusSolicitud = { "Aceptada", "Rechazada",
			"Por Revisar", "Finalizada" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;

	@Wire
	private Window wdwReporteProfesorMasSolicitados;
	@Wire
	private Datebox dtbInicioReporteProfesoresSolicitados;
	@Wire
	private Datebox dtbFinReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbProgramaReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbAreaReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbTematicaReporteProfesoresSolicitados;
	private static Date fechaInicio;
	private static Date fechaFin;
	java.util.Date hoy = new Date();

	/**
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos" y se llena una lista del mismo en el
	 * componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		dtbFinReporteProfesoresSolicitados.setValue(hoy);
		dtbInicioReporteProfesoresSolicitados.setValue(hoy);
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(100000001, "Todos", "", "", true,
				null);
		programas.add(programaa);
		cmbProgramaReporteProfesoresSolicitados
				.setModel(new ListModelList<Programa>(programas));
		cmbAreaReporteProfesoresSolicitados.setDisabled(true);
		cmbTematicaReporteProfesoresSolicitados.setDisabled(true);

	}

	/**
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas.
	 */
	@Listen("onSelect = #cmbProgramaReporteProfesoresSolicitados")
	public void buscarArea() {
		cmbAreaReporteProfesoresSolicitados.setValue("");
		cmbTematicaReporteProfesoresSolicitados.setValue("");
		if (cmbProgramaReporteProfesoresSolicitados.getValue().equals("Todos")) {

			areas = servicioArea.buscarActivos();
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbAreaReporteProfesoresSolicitados
					.setModel(new ListModelList<AreaInvestigacion>(areas));
			cmbAreaReporteProfesoresSolicitados.setDisabled(false);

		} else {

			cmbAreaReporteProfesoresSolicitados.setDisabled(false);
			cmbAreaReporteProfesoresSolicitados.setValue("");
			cmbTematicaReporteProfesoresSolicitados.setValue("");

			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long
							.parseLong(cmbProgramaReporteProfesoresSolicitados
									.getSelectedItem().getId())));
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbAreaReporteProfesoresSolicitados
					.setModel(new ListModelList<AreaInvestigacion>(areas));

		}
	}

	/**
	 * Metodo que permite cargar las tematicas dado al area seleccionado.
	 */
	@Listen("onSelect = #cmbAreaReporteProfesoresSolicitados")
	public void seleccionarTematica() {
		if (cmbAreaReporteProfesoresSolicitados.getValue().equals("Todos")) {
			if (cmbProgramaReporteProfesoresSolicitados.getValue().equals(
					"Todos"))
				tematicas = servicioTematica.buscarActivos();
			else {
				cmbTematicaReporteProfesoresSolicitados.setValue("");
				areas = servicioProgramaArea
						.buscarAreasDePrograma(servicioPrograma.buscar(Long
								.parseLong((cmbProgramaReporteProfesoresSolicitados
										.getSelectedItem().getId()))));
				List<Tematica> tematicasArea = new ArrayList<Tematica>();
				for (int i = 0; i < areas.size(); i++) {
					tematicasArea.addAll(servicioTematica
							.buscarTematicasDeArea(areas.get(i)));
				}
				tematicas.clear();
				tematicas.addAll(tematicasArea);
				tematicasArea.clear();
			}
		} else {
			cmbTematicaReporteProfesoresSolicitados.setValue("");

			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(Long
							.parseLong(cmbAreaReporteProfesoresSolicitados
									.getSelectedItem().getId())));

		}
		Tematica tema = new Tematica(10000, "Todos", "", true, null);
		tematicas.add(tema);
		cmbTematicaReporteProfesoresSolicitados
				.setModel(new ListModelList<Tematica>(tematicas));
		cmbTematicaReporteProfesoresSolicitados.setDisabled(false);

	}

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirReporteProfesoresSolicitados")
	public void salir() {
		wdwReporteProfesorMasSolicitados.onClose();
	}

	/**
	 * Metodo que permite limpiar los campos de la vista y colocarlos en el
	 * estado inicial
	 */
	@Listen("onClick = #btnCancelarReporteProfesoresSolicitados")
	public void limpiarCampos() {
		cmbAreaReporteProfesoresSolicitados.setValue("");
		cmbProgramaReporteProfesoresSolicitados.setValue("");
		cmbTematicaReporteProfesoresSolicitados.setValue("");
		dtbFinReporteProfesoresSolicitados.setValue(hoy);
		dtbInicioReporteProfesoresSolicitados.setValue(hoy);
		cmbAreaReporteProfesoresSolicitados.setDisabled(true);
		cmbTematicaReporteProfesoresSolicitados.setDisabled(true);
	}

	/**
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematicaReporteProfesoresSolicitados")
	public void tomarIdTematica() {

		idTematica = Long.parseLong(cmbTematicaReporteProfesoresSolicitados
				.getSelectedItem().getId());
	}

	/**
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * se generara un pdf donde se muestra una lista de los cinco profesores con
	 * mas solicitades de esta seleccion, por medio de unos condicionales seran
	 * cargados unos contadores que seran seteados a un objeto que permitira
	 * reflejar en un grafico la variabilidad de las solicitudes por profesor,
	 * donde mediante el componente "Jasperreport" se mapea una serie de
	 * parametros y una lista previamente cargada que seran los datos que se
	 * muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteProfesoresSolicitados")
	public void generarReporte() {

		if ((cmbProgramaReporteProfesoresSolicitados.getText().compareTo("") == 0)
				|| (cmbAreaReporteProfesoresSolicitados.getText().compareTo("") == 0)
				|| (cmbTematicaReporteProfesoresSolicitados.getText()
						.compareTo("") == 0)
				|| (dtbInicioReporteProfesoresSolicitados.getValue() == null)
				|| (dtbFinReporteProfesoresSolicitados.getValue() == null)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			fechaInicio = dtbInicioReporteProfesoresSolicitados.getValue();
			fechaFin = dtbFinReporteProfesoresSolicitados.getValue();

			if (fechaInicio.after(fechaFin)) {

				Messagebox
						.show("La fecha de fin debe ser posterior a la fecha de inicio",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				boolean datosVacios = false;
				fechaInicio = dtbInicioReporteProfesoresSolicitados.getValue();
				fechaFin = dtbFinReporteProfesoresSolicitados.getValue();
				AreaInvestigacion areaI = null;
				Programa programaI = null;
				Tematica tematicaI = null;
				if (!cmbTematicaReporteProfesoresSolicitados.getValue().equals(
						"Todos"))
					tematicaI = servicioTematica.buscarTematica(idTematica);
				if (!cmbAreaReporteProfesoresSolicitados.getValue().equals(
						"Todos"))
					areaI = servicioArea.buscarArea(Long
							.parseLong(cmbAreaReporteProfesoresSolicitados
									.getSelectedItem().getId()));
				if (!cmbProgramaReporteProfesoresSolicitados.getValue().equals(
						"Todos"))
					programaI = servicioPrograma.buscar(Long
							.parseLong(cmbProgramaReporteProfesoresSolicitados
									.getSelectedItem().getId()));
				List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
				List<SolicitudTutoria> solicitudesFinales = new ArrayList<SolicitudTutoria>();
				List<String> profesores = new ArrayList<String>();
				List<Integer> contadores = new ArrayList<Integer>();
				List<MasSolicitados> masSolicitados = new ArrayList<MasSolicitados>();
				Map<String, Object> map = new HashMap<String, Object>();
				int valor = 0;

				if (cmbTematicaReporteProfesoresSolicitados.getValue().equals(
						"Todos")) {
					if (cmbProgramaReporteProfesoresSolicitados.getValue()
							.equals("Todos")) {
						if (cmbAreaReporteProfesoresSolicitados.getValue()
								.equals("Todos")) {
							valor = 1;
							solicitudes = servicioSolicitudTutoria
									.buscarTodasSolicitudesEntreFechas(
											fechaInicio, fechaFin);
						} else {
							valor = 2;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorAreaYFechas(areaI,
											fechaInicio, fechaFin);
						}
					} else {
						if (cmbAreaReporteProfesoresSolicitados.getValue()
								.equals("Todos")) {
							valor = 3;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorProgramaYFechas(
											programaI, fechaInicio, fechaFin);
						} else {
							valor = 4;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorProgramaYAreaYFechas(
											programaI, areaI, fechaInicio,
											fechaFin);
						}
					}
					if (solicitudes.size() == 0) {
						datosVacios = true;
					} else {
						map = ordenar(solicitudes);
						profesores = (List<String>) map.get("Profesores");
						contadores = (List<Integer>) map.get("Contadores");
						for (int i = 0; i < profesores.size(); i++) {
							Profesor profesor = servicioProfesor
									.buscarProfesorPorCedula(profesores.get(i));
							switch (valor) {
							case 1: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorProfesorEntreFechas(profesor,
												fechaInicio, fechaFin);
							}
								break;
							case 2: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorProfesorYAreaEntreFechas(
												profesor, areaI, fechaInicio,
												fechaFin);
							}
								break;
							case 3: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorProfesorYProgramaEntreFechas(
												profesor, programaI,
												fechaInicio, fechaFin);
							}
								break;
							case 4: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorProfesorYProgramaYAreaEntreFechas(
												profesor, programaI, areaI,
												fechaInicio, fechaFin);
							}
								break;
							}

							long primerValor = 0;
							long segundoValor = 0;
							long tercerValor = 0;
							for (int j = 0; j < solicitudesFinales.size(); j++) {
								if (solicitudesFinales.get(j).getEstatus()
										.equals(estatusSolicitud[1]))
									primerValor++;
								else {
									if (solicitudesFinales.get(j).getEstatus()
											.equals(estatusSolicitud[2]))
										segundoValor++;
									else
										tercerValor++;
								}
							}
							MasSolicitados masSolicita = new MasSolicitados(
									primerValor, segundoValor, tercerValor,
									profesor, null);
							masSolicitados.add(masSolicita);
						}
					}
				} else {
					if (cmbProgramaReporteProfesoresSolicitados.getValue()
							.equals("Todos")) {
						if (cmbAreaReporteProfesoresSolicitados.getValue()
								.equals("Todos")) {
							valor = 1;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorTematicaEntreFechas(
											tematicaI, fechaInicio, fechaFin);
						} else {
							valor = 2;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorTematicayAreaYFechas(
											tematicaI, areaI, fechaInicio,
											fechaFin);
						}
					} else {
						if (cmbAreaReporteProfesoresSolicitados.getValue()
								.equals("Todos")) {
							valor = 3;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorProgramaYFechas(
											tematicaI, programaI, fechaInicio,
											fechaFin);
						} else {
							valor = 4;
							solicitudes = servicioSolicitudTutoria
									.buscarSolicitudesPorProgramaYAreaYFechas(
											tematicaI, programaI, areaI,
											fechaInicio, fechaFin);
						}
					}

					if (solicitudes.size() == 0) {
						datosVacios = true;
					} else {
						map = ordenar(solicitudes);
						profesores = (List<String>) map.get("Profesores");
						contadores = (List<Integer>) map.get("Contadores");
						for (int i = 0; i < profesores.size(); i++) {
							Profesor profesor = servicioProfesor
									.buscarProfesorPorCedula(profesores.get(i));

							switch (valor) {
							case 1: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorProfesorTematicaEntreFechas(
												profesor, tematicaI,
												fechaInicio, fechaFin);
							}
								break;
							case 2: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorTematicaProfesorYAreaEntreFechas(
												tematicaI, profesor, areaI,
												fechaInicio, fechaFin);
							}
								break;
							case 3: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorTematicaProfesorYProgramaEntreFechas(
												tematicaI, profesor, programaI,
												fechaInicio, fechaFin);
							}
								break;
							case 4: {
								solicitudesFinales = servicioSolicitudTutoria
										.buscarPorTematicaProfesorYProgramaYAreaEntreFechas(
												tematicaI, profesor, programaI,
												areaI, fechaInicio, fechaFin);
							}
								break;
							}
							long primerValor = 0;
							long segundoValor = 0;
							long tercerValor = 0;
							for (int j = 0; j < solicitudesFinales.size(); j++) {
								if (solicitudesFinales.get(j).getEstatus()
										.equals(estatusSolicitud[1]))
									primerValor++;
								else {
									if (solicitudesFinales.get(j).getEstatus()
											.equals(estatusSolicitud[2]))
										segundoValor++;
									else
										tercerValor++;
								}
							}
							MasSolicitados masSolicita = new MasSolicitados(
									primerValor, segundoValor, tercerValor,
									profesor, null);
							masSolicitados.add(masSolicita);
						}
					}
				}
				if (!datosVacios) {
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("inicio", fechaInicio);
					mapa.put("fin", fechaFin);
					if (cmbTematicaReporteProfesoresSolicitados.getValue()
							.equals("Todos"))
						mapa.put("tematica", "Todas las tematicas");
					else
						mapa.put("tematica", tematicaI.getNombre());
					if (cmbProgramaReporteProfesoresSolicitados.getValue()
							.equals("Todos"))
						mapa.put("programa", "Todos los programas");
					else
						mapa.put("programa",
								cmbProgramaReporteProfesoresSolicitados
										.getValue());
					if (cmbAreaReporteProfesoresSolicitados.getValue().equals(
							"Todos"))
						mapa.put("area", "Todas las areas");
					else
						mapa.put("area",
								cmbAreaReporteProfesoresSolicitados.getValue());
					FileSystemView filesys = FileSystemView.getFileSystemView();
					JasperReport jasperReport;
					String rutaUrl = obtenerDirectorio();

					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estadisticos/compilados/RProfesoresMasSolicitados.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
					mapa.put("logoUcla", reporteImage + "logo ucla.png");
					mapa.put("logoCE", reporteImage + "logo CE.png");
					mapa.put("logoSiteg", reporteImage + "logo.png");
					// URL url =
					// getClass().getResource("/configuracion/logo ucla.png");
					// URL url2 =
					// getClass().getResource("/configuracion/logo CE.png");
					// URL url3 =
					// getClass().getResource("/configuracion/logo.png");
					// mapa.put("logoUcla", url);
					// mapa.put("logoCE", url2);
					// mapa.put("logoSiteg", url3);

					try {
						// jasperReport = (JasperReport)
						// JRLoader.loadObject(getClass().getResource("RProfesoresMasSolicitados.jasper"));
						jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);
						JasperPrint jasperPrint = JasperFillManager.fillReport(
								jasperReport, mapa,
								new JRBeanCollectionDataSource(masSolicitados));
						JasperViewer.viewReport(jasperPrint, false);

					} catch (JRException e) {
						// TODO Auto-generated catch block
						Messagebox.show(
								"Error en reporte, casua: " + e.getMessage(),
								"Informacion", Messagebox.OK,
								Messagebox.INFORMATION);
					}
					limpiarCampos();
				} else {
					Messagebox
							.show("No hay informacion disponible para esta seleccion",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
				}

			}
		}
	}

	/**
	 * Metodo que permite mapear dos listas ordenadas de profesores y
	 * contadores, donde es cargada mediante dos operaciones internas, la
	 * primera es para contar la ocurrencia con respecto a los profesores en
	 * base a las solicitudes recibidas y la segunda es para ordenarlos de mayor
	 * a menor donde luego dado a esta lista cargada, se llena una lista con los
	 * cinco profesores con mas solicitudes.
	 */
	public Map<String, Object> ordenar(List<SolicitudTutoria> solicitudes) {
		List<String> profesores = new ArrayList<String>();
		List<Integer> contadores = new ArrayList<Integer>();
		List<SolicitudTutoria> solicitudesTutoria = new ArrayList<SolicitudTutoria>();
		SolicitudTutoria solicitud = solicitudes.get(0);
		String profesor = solicitudes.get(0).getProfesor().getCedula();

		int contadorProfesores = 0;
		for (int i = 0; i < solicitudes.size(); i++) {
			solicitud = solicitudes.get(i);
			if (profesor == solicitudes.get(i).getProfesor().getCedula()) {
				contadorProfesores = contadorProfesores + 1;
			} else {
				profesores.add(profesor);
				contadores.add(contadorProfesores);
				solicitudesTutoria.add(solicitud);
				solicitud = solicitudes.get(i);
				profesor = solicitudes.get(i).getProfesor().getCedula();
				contadorProfesores = 1;
			}
		}
		profesores.add(profesor);
		contadores.add(contadorProfesores);
		solicitudesTutoria.add(solicitud);
		/**************************** Ordenado de Lista ************* ******************/
		List<String> profesoresOrdenados = new ArrayList<String>();
		List<Integer> contadoresOrdenados = new ArrayList<Integer>();
		List<SolicitudTutoria> solicitudesFinales = new ArrayList<SolicitudTutoria>();
		int valor = 0;
		int valor2 = 0;
		SolicitudTutoria solicit = new SolicitudTutoria();
		String valorTematica = "";
		for (int i = 0; i < contadores.size(); i++) {
			valor = contadores.get(i);
			for (int j = 0; j < contadores.size(); j++) {
				valor2 = contadores.get(j);
				valorTematica = profesores.get(j);
				solicit = solicitudesTutoria.get(j);
				if (valor2 > valor) {
					contadores.remove(j);
					profesores.remove(j);
					solicitudesTutoria.remove(j);
					contadoresOrdenados.add(valor2);
					profesoresOrdenados.add(valorTematica);
					solicitudesFinales.add(solicit);
					j = contadores.size();
				}
			}
		}
		if (contadoresOrdenados.size() < 5 && contadores.size() != 0) {
			int cantidadFaltante = 5 - contadoresOrdenados.size();
			for (int i = 0; i < cantidadFaltante; i++) {
				if (i < contadores.size()) {
					contadoresOrdenados.add(contadores.get(i));
					profesoresOrdenados.add(profesores.get(i));
					solicitudesFinales.add(solicitudesTutoria.get(i));
				}
			}
		}
		if (contadoresOrdenados.size() > 5) {
			for (int i = 5; i < contadoresOrdenados.size(); i++) {
				contadoresOrdenados.remove(i);
				profesoresOrdenados.remove(i);
				solicitudesFinales.remove(i);
			}
		}
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Profesores", profesoresOrdenados);
		map.put("Contadores", contadoresOrdenados);
		return map;
	}
}
