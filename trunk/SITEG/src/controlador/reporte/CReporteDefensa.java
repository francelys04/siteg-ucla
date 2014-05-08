package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Programa;
import modelo.Tematica;
import modelo.reporte.DefensaTeg;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteDefensa extends CGeneral {

	Programa programa = new Programa();
	private String[] estatusDefensa = { "Todos", "Defensa Programada",
			"Defensa Evaluada" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;

	@Wire
	private Window wdwReporteDefensa;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Jasperreport jstVistaPrevia;
	private static Programa programa1;
	private static AreaInvestigacion area1;
	private static long idarea;
	private static Date fechaInicio;
	private static Date fechaFin;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos", junto a esto se tiene una lista
	 * previamente cargada de manera estatica, los estatus de la defensa y se
	 * llena una lista del mismo en el componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas = servicioPrograma.buscarActivas();
		programas.add(programaa);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusDefensa));
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);

	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos" en el combo de las areas
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() {
		try {
			if (cmbPrograma.getValue().equals("Todos")) {

				areas = servicioArea.buscarActivos();
				AreaInvestigacion area = new AreaInvestigacion(10000000,
						"Todos", "", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
				cmbArea.setDisabled(false);

			} else {

				cmbArea.setDisabled(false);
				cmbArea.setValue("");
				cmbTematica.setValue("");
				programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
				areas = servicioProgramaArea
						.buscarAreasDePrograma(servicioPrograma
								.buscar(programa1.getId()));
				AreaInvestigacion area = new AreaInvestigacion(10000000,
						"Todos", "", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
	}

	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		try {
			if (cmbArea.getValue().equals("Todos")) {

				cmbTematica.setValue("Todos");
				cmbTematica.setDisabled(true);
				cmbEstatus.setDisabled(false);

			} else {

				cmbTematica.setDisabled(false);
				cmbTematica.setValue("");
				area1 = (AreaInvestigacion) cmbArea.getSelectedItem()
						.getValue();
				tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
						.buscarArea(area1.getId()));
				Tematica tema = new Tematica(10000, "Todos", "", true, null);
				tematicas.add(tema);
				cmbTematica.setModel(new ListModelList<Tematica>(tematicas));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
		idarea = Long.parseLong(cmbArea.getSelectedItem().getId());
	}

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {

		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
		cmbEstatus.setDisabled(false);

	}

	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y tipo de defensa se generara un pdf donde se muestra una lista de las
	 * defensas de esta seleccion, mediante el componente "Jasperreport" donde
	 * se mapea una serie de parametros y una lista previamente cargada que
	 * seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteDefensa")
	public void generarReporteDefensa() throws JRException {

		if ((cmbPrograma.getText().compareTo("") == 0)
				|| (cmbArea.getText().compareTo("") == 0)
				|| (cmbTematica.getText().compareTo("") == 0)
				|| (cmbEstatus.getText().compareTo("") == 0)
				|| (dtbFechaInicio.getValue() == null)
				|| (dtbFechaFin.getValue() == null)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			fechaInicio = dtbFechaInicio.getValue();
			fechaFin = dtbFechaFin.getValue();

			if (fechaInicio.after(fechaFin)) {

				Messagebox
						.show("La fecha de fin debe ser posterior a la fecha de inicio",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				Date fechaInicio = dtbFechaInicio.getValue();
				Date fechaFin = dtbFechaFin.getValue();
				String nombreArea = cmbArea.getValue();
				String nombreTematica = cmbTematica.getValue();
				String nombrePrograma = cmbPrograma.getValue();
				AreaInvestigacion area = servicioArea.buscarArea(idArea);
				Tematica tematica = servicioTematica.buscarTematica(idTematica);
				String tipoDefensa = cmbEstatus.getValue();

				List<Defensa> defensas = new ArrayList();
				List<DefensaTeg> elementos = new ArrayList<DefensaTeg>();

				// ESTATUS DIFERENTE DE TODOS
				if (!tipoDefensa.equals("Todos")) {

					//TODOS LOS PROGRAMAS
					if (nombrePrograma.equals("Todos")) {

						defensas = servicioDefensa
								.buscarDefensaTegSegunEstatus2(tipoDefensa,
										fechaInicio, fechaFin);
						
					//UN PROGRAMA Y TODAS LAS AREAS
					} else if (!nombrePrograma.equals("Todos")
							&& nombreArea.equals("Todos")) {
						defensas = servicioDefensa
								.buscarDefensaTegSegunEstatusPrograma2(
										tipoDefensa, programa1, fechaInicio,
										fechaFin);
					
					//UN PROGRAMA, UN AREA, UNA TEMATICA
					} else if (!nombrePrograma.equals("Todos")
							&& !nombreArea.equals("Todos")
							&& !nombreTematica.equals("Todos")) {
						tematica = servicioTematica
								.buscarTematicaPorNombre(nombreTematica);
						defensas = servicioDefensa
								.buscarDefensaTegSegunEstatusTematica2(
										tipoDefensa, tematica, fechaInicio,
										fechaFin);
					//UN PROGRAMA, UN AREA, TODAS LAS TEMATICAS
					} else if (!nombrePrograma.equals("Todos")
							&& !nombreArea.equals("Todos")
							&& nombreTematica.equals("Todos")) {
						
						defensas = servicioDefensa
								.buscarDefensaTegSegunEstatusArea2(tipoDefensa,
										area, fechaInicio, fechaFin);
					}
				} else {
					//TODOS LOS PROGRAMAS, TODOS LOS ESTATUS
					if (nombrePrograma.equals("Todos")) {

						defensas = servicioDefensa.buscarDefensaTeg(
								fechaInicio, fechaFin);
					//UN PROGRAMA, TODAS LAS AREAS
					} else if (!nombrePrograma.equals("Todos")
							&& nombreArea.equals("Todos")) {
						defensas = servicioDefensa
								.buscarDefensaTegSegunPrograma(programa1,
										fechaInicio, fechaFin);
						
					//UN PROGRAMA, UN AREA, TODAS LAS TEMATICAS
					} else if (!nombrePrograma.equals("Todos")
							&& !nombreArea.equals("Todos")
							&& !nombreTematica.equals("Todos")) {
						tematica = servicioTematica
								.buscarTematicaPorNombre(nombreTematica);
						defensas = servicioDefensa
								.buscarDefensaTegSegunTematica(tematica,
										fechaInicio, fechaFin);
					//UN PROGRAMA, UN AREA, TODAS LAS TEMATICAS
					} else if (!nombrePrograma.equals("Todos")
							&& !nombreArea.equals("Todos")
							&& nombreTematica.equals("Todos")) {
						defensas = servicioDefensa.buscarDefensaTegSegunArea(
								area, fechaInicio, fechaFin);
					}
				}

				if (defensas.size() != 0) {
					for (Defensa defensa : defensas) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(defensa.getTeg());

						String nombreEstudiantes = "";
						for (Estudiante e : estudiantes) {
							nombreEstudiantes += e.getNombre() + " "
									+ e.getApellido() + " ";
						}
						elementos.add(new DefensaTeg(defensa.getTeg()
								.getTitulo(), defensa.getProfesor().getNombre()
								+ " " + defensa.getProfesor().getApellido(),
								nombreEstudiantes, defensa.getEstatus(),
								defensa.getFecha()));
					}

					FileSystemView filesys = FileSystemView.getFileSystemView();
					Map<String, Object> p = new HashMap<String, Object>();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estructurados/compilados/RReporteDefensa.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					p.put("fecha", new Date());
					p.put("fecha1", fechaInicio);
					p.put("fecha2", fechaFin);
					p.put("area", nombreArea);
					p.put("tematica", nombreTematica);
					p.put("programa", nombrePrograma);
					p.put("total", elementos.size());
					p.put("estatus", tipoDefensa);
					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									elementos));

					JasperViewer.viewReport(jasperPrint, false);
					cancelarReporteDefensa();
				} else {
					Messagebox
							.show("No hay informacion disponible para esta seleccion",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
				}

			}

		}
	}

	/* Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarReporteDefensa")
	public void cancelarReporteDefensa() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		cmbEstatus.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);

	}

	/* Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirReporteDefensa")
	public void salirReporteDefensa() throws JRException {

		cancelarReporteDefensa();
		wdwReporteDefensa.onClose();

	}

}
