package controlador.reporte;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.Proyecto;
import modelo.reporte.Solicitud;
import net.sf.jasperreports.engine.JRException;

import org.springframework.stereotype.Controller;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteSolicitud extends CGeneral {

	static Programa programa1 = new Programa();
	static AreaInvestigacion area1 = new AreaInvestigacion();
	private static String estatusDefensa = "Solicitando Defensa";
	private static String estatusProyecto = "Solicitando Registro";
	private static String estatusTeg = "TEG Registrado";
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	private static int chequeoEstatus = 0;
	static List<Proyecto> elementos1 = new ArrayList<Proyecto>();
	static List<Solicitud> elementos = new ArrayList<Solicitud>();
	private static String NombreTxt;

	@Wire
	private Window wdwReporteSolicitud;
	@Wire
	private Radiogroup rdgTutoria;
	@Wire
	private Radio rdoTutoria;
	@Wire
	private Radio rdoTEG;
	@Wire
	private Radio rdoProyecto;
	@Wire
	private Radio rdoDefensa;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Groupbox grbReporteSolicitud;
	@Wire
	private Jasperreport jstVistaPrevia;
	@Wire
	private Listbox ltbElegiPrograma;
	@Wire
	private Listbox ltbElegiProgramaArea;
	@Wire
	private Listbox ltbTodo;
	@Wire
	private Listbox ltbElegiArea;

	@Wire
	private Listbox ltbElegiProgramaAreaTematica;

	@Wire
	private Listbox ltbElegiAreaTematica1;
	@Wire
	private Listbox ltbElegiPrograma1;
	@Wire
	private Listbox ltbElegiProgramaArea1;
	@Wire
	private Listbox ltbTodo1;
	@Wire
	private Listbox ltbElegiArea1;

	@Wire
	private Listbox ltbElegiProgramaAreaTematica1;

	@Wire
	private Listbox ltbElegiAreaTematica;
	@Wire
	private Button btnExportarPlano;
	private static Date fechaInicio;
	private static Date fechaFin;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos", y se llena una lista del mismo en el
	 * componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		ltbElegiArea.setVisible(false);

		cancelar();
		btnExportarPlano.setDisabled(true);
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));

		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);

	}

	/* Metodo que permite limpiar todas las listas de la vista. */
	public void cancelar() {

		ltbElegiPrograma.setVisible(false);
		ltbElegiProgramaArea.setVisible(false);
		ltbElegiProgramaAreaTematica.setVisible(false);
		ltbElegiProgramaAreaTematica.setVisible(false);
		ltbElegiAreaTematica.setVisible(false);
		ltbTodo.setVisible(false);
		ltbElegiArea.setVisible(false);
		ltbElegiPrograma1.setVisible(false);
		ltbElegiProgramaArea1.setVisible(false);
		ltbElegiProgramaAreaTematica1.setVisible(false);
		ltbElegiProgramaAreaTematica1.setVisible(false);
		ltbElegiAreaTematica1.setVisible(false);
		ltbTodo1.setVisible(false);
		ltbElegiArea1.setVisible(false);

	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas
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
	 * valor en el campo tematica, ademas se adiciona un nuevo item donde se
	 * puede seleccionar la opcion de "Todos" en el combo de las tematicas
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		try {

			if (cmbArea.getValue().equals("Todos")) {

				cmbTematica.setValue("Todos");
				cmbTematica.setDisabled(true);

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
	}

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {

		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
	}

	/*
	 * Metodo que permite dado a un programa, area y tematica, generar una lista
	 * con las solicitudes de tutorias, proyectos,tegs o defensas, dependiendo
	 * de esta seleccion, donde se cargara un fichero con los datos resultante
	 * de esta.
	 */
	@Listen("onClick = #btnGenerar")
	public void generarReporteDefensa() throws JRException {
		cancelar();

		if (cmbPrograma.getText().compareTo("") == 0
				|| cmbArea.getText().compareTo("") == 0
				|| cmbTematica.getText().compareTo("") == 0) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			if ((rdoDefensa.isChecked() == false)
					&& (rdoTutoria.isChecked() == false)
					&& (rdoProyecto.isChecked() == false)
					&& (rdoTEG.isChecked() == false)) {
				Messagebox
						.show("Debe Seleccionar el tipo de solicitud que desea consultar",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				Date fechaInicio = dtbDesde.getValue();
				Date fechaFin = dtbHasta.getValue();

				if (fechaInicio.after(fechaFin)) {

					Messagebox
							.show("La fecha de fin debe ser posterior a la fecha de inicio",
									"Error", Messagebox.OK, Messagebox.ERROR);

				} else {

					if (rdoTutoria.isChecked() == true) {
						cancelar();
					}

					/*
					 * Solicitudes de Proyecto, teg o Defensa
					 */
					else if ((rdoProyecto.isChecked() == true)
							|| (rdoTEG.isChecked() == true)
							|| (rdoDefensa.isChecked() == true)) {

						String nombreArea = cmbArea.getValue();
						String nombrePrograma = cmbPrograma.getValue();
						String nombreTematica = cmbTematica.getValue();
						fechaInicio = dtbDesde.getValue();
						fechaFin = dtbHasta.getValue();

						Tematica tematica1 = servicioTematica
								.buscarTematica(idTematica);

						List<Teg> teg = null;
						if (fechaFin == null || fechaInicio == null
								|| fechaInicio.after(fechaFin)) {
							Messagebox
									.show("La fecha de fin debe ser posterior a la fecha de inicio",
											"Error", Messagebox.OK,
											Messagebox.ERROR);
							btnExportarPlano.setDisabled(true);
						} else {
							btnExportarPlano.setDisabled(false);
							/*
							 * SELECIONO TODO TODO
							 */
							if (nombrePrograma.equals("Todos")
									&& nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 6;

								if (rdoProyecto.isChecked() == true) {
									teg = servicioTeg
											.buscarTegPorEstatus(
													estatusProyecto,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesProyecto";
								} else if (rdoTEG.isChecked() == true) {
									teg = servicioTeg
											.buscarTegPorEstatus(
													estatusTeg, fechaInicio,
													fechaFin);
									NombreTxt = "SolicitudesTEG";
								} else if (rdoDefensa.isChecked() == true) {
									teg = servicioTeg
											.buscarTegPorEstatus(
													estatusDefensa,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesDefensa";
								}

								if (teg.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos1.clear();
									for (Teg t : teg) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarEstudiantePorTeg(t);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String titulo = t.getTitulo();
										String descripcionTeg = t
												.getDescripcion();
										Date fecha = t.getFecha();
										String area = t.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = t
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = t.getTematica()
												.getNombre();
										String descripcionTematica = t
												.getTematica().getDescripcion();
										String cedulaTutor = t.getTutor()
												.getCedula();
										String nombreTutor = t.getTutor()
												.getNombre();
										String apellidoTutor = t.getTutor()
												.getApellido();
										String correoTutor = t.getTutor()
												.getCorreoElectronico();
										String direccionTutor = t.getTutor()
												.getDireccion();

										elementos1.add(new Proyecto(titulo,
												descripcionTeg, fecha,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbTodo.setModel(new ListModelList<Proyecto>(
											elementos1));
									ltbTodo.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}
							/*
							 * seleccione un programa
							 */
							else if (!nombrePrograma.equals("Todos")
									&& nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 1;
								if (rdoProyecto.isChecked() == true) {
									teg = servicioTeg
											.buscarTegPorProgramaEstatus(
													programa1, estatusProyecto, 
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesProyecto";
								} else if (rdoTEG.isChecked() == true) {
									teg = servicioTeg
											.buscarTegPorProgramaEstatus(
													programa1, estatusTeg, 
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesTEG";
								} else if (rdoDefensa.isChecked() == true) {
									teg = servicioTeg
											.buscarTegPorProgramaEstatus(
													programa1, estatusDefensa, 
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesDefensa";
								}

								if (teg.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos1.clear();
									for (Teg t : teg) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarEstudiantePorTeg(t);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String titulo = t.getTitulo();
										String descripcionTeg = t
												.getDescripcion();
										Date fecha = t.getFecha();
										String area = t.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = t
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = t.getTematica()
												.getNombre();
										String descripcionTematica = t
												.getTematica().getDescripcion();
										String cedulaTutor = t.getTutor()
												.getCedula();
										String nombreTutor = t.getTutor()
												.getNombre();
										String apellidoTutor = t.getTutor()
												.getApellido();
										String correoTutor = t.getTutor()
												.getCorreoElectronico();
										String direccionTutor = t.getTutor()
												.getDireccion();

										elementos1.add(new Proyecto(titulo,
												descripcionTeg, fecha,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiPrograma
											.setModel(new ListModelList<Proyecto>(
													elementos1));
									ltbElegiPrograma.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}
							/*
							 * Elijo uno de cada cosa
							 */
							else if (!nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& !nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 3;

								tematica1 = servicioTematica
										.buscarTematica(idTematica);

								if (rdoProyecto.isChecked() == true) {
									teg = servicioTeg
											.buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
													estatusProyecto, tematica1,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesProyecto";
								} else if (rdoTEG.isChecked() == true) {
									teg = servicioTeg
											.buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
													estatusTeg, tematica1,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesTEG";
								} else if (rdoDefensa.isChecked() == true) {
									teg = servicioTeg
											.buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
													estatusDefensa, tematica1,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesDefensa";
								}

								if (teg.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos1.clear();
									for (Teg t : teg) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarEstudiantePorTeg(t);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String titulo = t.getTitulo();
										String descripcionTeg = t
												.getDescripcion();
										Date fecha = t.getFecha();
										String area = t.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = t
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = t.getTematica()
												.getNombre();
										String descripcionTematica = t
												.getTematica().getDescripcion();
										String cedulaTutor = t.getTutor()
												.getCedula();
										String nombreTutor = t.getTutor()
												.getNombre();
										String apellidoTutor = t.getTutor()
												.getApellido();
										String correoTutor = t.getTutor()
												.getCorreoElectronico();
										String direccionTutor = t.getTutor()
												.getDireccion();

										elementos1.add(new Proyecto(titulo,
												descripcionTeg, fecha,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiProgramaAreaTematica
											.setModel(new ListModelList<Proyecto>(
													elementos1));
									ltbElegiProgramaAreaTematica
											.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}
							/*
							 * Eligi� programa area pero todos las tematicas
							 */
							else if (!nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 2;
								if (rdoProyecto.isChecked() == true) {
									teg = servicioTeg
											.buscarTegsSegunAreaUnEstatus(
													area1, fechaInicio,
													fechaFin, estatusProyecto);
									NombreTxt = "SolicitudesProyecto";
								} else

								if (rdoTEG.isChecked() == true) {
									teg = servicioTeg
											.buscarTegsSegunAreaUnEstatus(
													area1, fechaInicio,
													fechaFin, estatusTeg);
									NombreTxt = "SolicitudesTEG";
								} else if (rdoDefensa.isChecked() == true) {
									teg = servicioTeg
											.buscarTegsSegunAreaUnEstatus(
													area1, fechaInicio,
													fechaFin, estatusDefensa);
									NombreTxt = "SolicitudesDefensa";
								}

								if (teg.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos1.clear();
									for (Teg t : teg) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarEstudiantePorTeg(t);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String titulo = t.getTitulo();
										String descripcionTeg = t
												.getDescripcion();
										Date fecha = t.getFecha();
										String area = t.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = t
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = t.getTematica()
												.getNombre();
										String descripcionTematica = t
												.getTematica().getDescripcion();
										String cedulaTutor = t.getTutor()
												.getCedula();
										String nombreTutor = t.getTutor()
												.getNombre();
										String apellidoTutor = t.getTutor()
												.getApellido();
										String correoTutor = t.getTutor()
												.getCorreoElectronico();
										String direccionTutor = t.getTutor()
												.getDireccion();

										elementos1.add(new Proyecto(titulo,
												descripcionTeg, fecha,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiProgramaArea
											.setModel(new ListModelList<Proyecto>(
													elementos1));
									ltbElegiProgramaArea.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}

							}
							/*
							 * Eligi� todo los programas una area y todos los
							 * tematicos
							 */

							else if (nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 4;
								if (rdoProyecto.isChecked() == true) {
									teg = servicioTeg
											.buscarTegsSegunAreaUnEstatus(
													area1, fechaInicio,
													fechaFin, estatusProyecto);
									NombreTxt = "SolicitudesProyecto";
								} else

								if (rdoTEG.isChecked() == true) {

									teg = servicioTeg
											.buscarTegsSegunAreaUnEstatus(
													area1, fechaInicio,
													fechaFin, estatusTeg);
									NombreTxt = "SolicitudesTEG";
								} else if (rdoDefensa.isChecked() == true) {

									teg = servicioTeg
											.buscarTegsSegunAreaUnEstatus(
													area1, fechaInicio,
													fechaFin, estatusProyecto);
									NombreTxt = "SolicitudesDefensa";
								}

								if (teg.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos1.clear();
									for (Teg t : teg) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarEstudiantePorTeg(t);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String titulo = t.getTitulo();
										String descripcionTeg = t
												.getDescripcion();
										Date fecha = t.getFecha();
										String area = t.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = t
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = t.getTematica()
												.getNombre();
										String descripcionTematica = t
												.getTematica().getDescripcion();
										String cedulaTutor = t.getTutor()
												.getCedula();
										String nombreTutor = t.getTutor()
												.getNombre();
										String apellidoTutor = t.getTutor()
												.getApellido();
										String correoTutor = t.getTutor()
												.getCorreoElectronico();
										String direccionTutor = t.getTutor()
												.getDireccion();

										elementos1.add(new Proyecto(titulo,
												descripcionTeg, fecha,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiProgramaArea
											.setModel(new ListModelList<Proyecto>(
													elementos1));
									ltbElegiProgramaArea.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}

							}
							/*
							 * Eligio todo los programas una area y una tematica
							 */
							else if (nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& !nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 5;
								tematica1 = servicioTematica
										.buscarTematica(idTematica);
								if (rdoProyecto.isChecked() == true) {
									teg = servicioTeg
											.buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
													estatusProyecto, tematica1,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesProyecto";
								} else

								if (rdoTEG.isChecked() == true) {

									teg = servicioTeg
											.buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
													estatusTeg, tematica1,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesTEG";
								} else if (rdoDefensa.isChecked() == true) {

									teg = servicioTeg
											.buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
													estatusDefensa, tematica1,
													fechaInicio, fechaFin);
									NombreTxt = "SolicitudesDefensa";
								}

								if (teg.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos1.clear();
									for (Teg t : teg) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarEstudiantePorTeg(t);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String titulo = t.getTitulo();
										String descripcionTeg = t
												.getDescripcion();
										Date fecha = t.getFecha();
										String area = t.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = t
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = t.getTematica()
												.getNombre();
										String descripcionTematica = t
												.getTematica().getDescripcion();
										String cedulaTutor = t.getTutor()
												.getCedula();
										String nombreTutor = t.getTutor()
												.getNombre();
										String apellidoTutor = t.getTutor()
												.getApellido();
										String correoTutor = t.getTutor()
												.getCorreoElectronico();
										String direccionTutor = t.getTutor()
												.getDireccion();

										elementos1.add(new Proyecto(titulo,
												descripcionTeg, fecha,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiAreaTematica
											.setModel(new ListModelList<Proyecto>(
													elementos1));
									ltbElegiAreaTematica.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}

						}

					}
					if (rdoTutoria.isChecked() == true) {

						String nombreArea = cmbArea.getValue();
						String nombrePrograma = cmbPrograma.getValue();
						String nombreTematica = cmbTematica.getValue();
						fechaInicio = dtbDesde.getValue();
						fechaFin = dtbHasta.getValue();

						Tematica tematica1 = servicioTematica
								.buscarTematica(idTematica);

						List<SolicitudTutoria> solicitud = null;
						if (fechaFin == null || fechaInicio == null
								|| fechaInicio.after(fechaFin)) {
							Messagebox
									.show("La fecha de inicio debe ser primero que la fecha de fin",
											"Error", Messagebox.OK,
											Messagebox.ERROR);
							btnExportarPlano.setDisabled(true);
						} else {
							btnExportarPlano.setDisabled(false);
							/*
							 * SELECIONO TODO TODO
							 */
							if (nombrePrograma.equals("Todos")
									&& nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 7;
								solicitud = servicioSolicitudTutoria
										.buscarSolicitudPorVariosProgramaUnEstatus(
												fechaInicio, fechaFin);

								if (solicitud.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos.clear();
									for (SolicitudTutoria s : solicitud) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarSolicitudesEstudiante(s);

										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String descripcionTeg = s
												.getDescripcion();
										Date fecha = s.getFecha();
										String estatus = s.getEstatus();
										String area = s.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = s
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = s.getTematica()
												.getNombre();
										String descripcionTematica = s
												.getTematica().getDescripcion();
										String cedulaTutor = s.getProfesor()
												.getCedula();
										String nombreTutor = s.getProfesor()
												.getNombre();
										String apellidoTutor = s.getProfesor()
												.getApellido();
										String correoTutor = s.getProfesor()
												.getCorreoElectronico();
										String direccionTutor = s.getProfesor()
												.getDireccion();

										elementos.add(new Solicitud(
												descripcionTeg, fecha, estatus,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbTodo1.setModel(new ListModelList<Solicitud>(
											elementos));
									ltbTodo1.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}
							/*
							 * seleccione un programa
							 */
							else if (!nombrePrograma.equals("Todos")
									&& nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 8;
								solicitud = servicioSolicitudTutoria
										.buscarSolicitudPorProgramaUnEstatus(
												programa1, fechaInicio,
												fechaFin);

								if (solicitud.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos.clear();
									for (SolicitudTutoria s : solicitud) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarSolicitudesEstudiante(s);

										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String descripcionTeg = s
												.getDescripcion();
										Date fecha = s.getFecha();
										String estatus = s.getEstatus();
										String area = s.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = s
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = s.getTematica()
												.getNombre();
										String descripcionTematica = s
												.getTematica().getDescripcion();
										String cedulaTutor = s.getProfesor()
												.getCedula();
										String nombreTutor = s.getProfesor()
												.getNombre();
										String apellidoTutor = s.getProfesor()
												.getApellido();
										String correoTutor = s.getProfesor()
												.getCorreoElectronico();
										String direccionTutor = s.getProfesor()
												.getDireccion();

										elementos.add(new Solicitud(
												descripcionTeg, fecha, estatus,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiPrograma1
											.setModel(new ListModelList<Solicitud>(
													elementos));
									ltbElegiPrograma1.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}
							/*
							 * Elijo uno de cada cosa
							 */
							else if (!nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& !nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 9;
								tematica1 = servicioTematica
										.buscarTematica(idTematica);

								solicitud = servicioSolicitudTutoria
										.buscarSolicitudDeUnaTematicaPorDosFechasyUnEstatus(
												tematica1, fechaInicio,
												fechaFin);

								if (solicitud.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos.clear();
									for (SolicitudTutoria s : solicitud) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarSolicitudesEstudiante(s);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String descripcionTeg = s
												.getDescripcion();
										Date fecha = s.getFecha();
										String estatus = s.getEstatus();
										String area = s.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = s
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = s.getTematica()
												.getNombre();
										String descripcionTematica = s
												.getTematica().getDescripcion();
										String cedulaTutor = s.getProfesor()
												.getCedula();
										String nombreTutor = s.getProfesor()
												.getNombre();
										String apellidoTutor = s.getProfesor()
												.getApellido();
										String correoTutor = s.getProfesor()
												.getCorreoElectronico();
										String direccionTutor = s.getProfesor()
												.getDireccion();

										elementos.add(new Solicitud(
												descripcionTeg, fecha, estatus,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiProgramaAreaTematica1
											.setModel(new ListModelList<Solicitud>(
													elementos));
									ltbElegiProgramaAreaTematica1
											.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
									;
								}
							}
							/*
							 * Eligi programa area pero todos las tematicas
							 */
							else if (!nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 10;

								solicitud = servicioSolicitudTutoria
										.buscarSolicitudSegunAreaUnEstatus(
												area1, fechaInicio, fechaFin);

								if (solicitud.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos.clear();
									for (SolicitudTutoria s : solicitud) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarSolicitudesEstudiante(s);
										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String descripcionTeg = s
												.getDescripcion();
										Date fecha = s.getFecha();
										String estatus = s.getEstatus();
										String area = s.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = s
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = s.getTematica()
												.getNombre();
										String descripcionTematica = s
												.getTematica().getDescripcion();
										String cedulaTutor = s.getProfesor()
												.getCedula();
										String nombreTutor = s.getProfesor()
												.getNombre();
										String apellidoTutor = s.getProfesor()
												.getApellido();
										String correoTutor = s.getProfesor()
												.getCorreoElectronico();
										String direccionTutor = s.getProfesor()
												.getDireccion();

										elementos.add(new Solicitud(
												descripcionTeg, fecha, estatus,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiProgramaArea1
											.setModel(new ListModelList<Solicitud>(
													elementos));
									ltbElegiProgramaArea1.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}

							}
							/*
							 * Eligio todo los programas una area y todos los
							 * tematicos
							 */

							else if (nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 11;

								solicitud = servicioSolicitudTutoria
										.buscarSolicitudSegunAreaUnEstatus(
												area1, fechaInicio, fechaFin);

								if (solicitud.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos.clear();
									for (SolicitudTutoria s : solicitud) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarSolicitudesEstudiante(s);

										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String descripcionTeg = s
												.getDescripcion();
										Date fecha = s.getFecha();
										String estatus = s.getEstatus();
										String area = s.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = s
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = s.getTematica()
												.getNombre();
										String descripcionTematica = s
												.getTematica().getDescripcion();
										String cedulaTutor = s.getProfesor()
												.getCedula();
										String nombreTutor = s.getProfesor()
												.getNombre();
										String apellidoTutor = s.getProfesor()
												.getApellido();
										String correoTutor = s.getProfesor()
												.getCorreoElectronico();
										String direccionTutor = s.getProfesor()
												.getDireccion();

										elementos.add(new Solicitud(
												descripcionTeg, fecha, estatus,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiProgramaArea1
											.setModel(new ListModelList<Solicitud>(
													elementos));
									ltbElegiProgramaArea1.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}

							}
							/*
							 * Eligio todo los programas una area y una tematica
							 */
							else if (nombrePrograma.equals("Todos")
									&& !nombreArea.equals("Todos")
									&& !nombreTematica.equals("Todos")) {
								cancelar();
								chequeoEstatus = 12;
								tematica1 = servicioTematica
										.buscarTematicaPorNombre(nombreTematica);

								solicitud = servicioSolicitudTutoria
										.buscarSolicitudDeUnaTematicaPorDosFechasyUnEstatus(
												tematica1, fechaInicio,
												fechaFin);

								if (solicitud.size() != 0) {

									wdwReporteSolicitud.setWidth("3200px");
									grbReporteSolicitud.setWidth("3150px");

									elementos.clear();
									for (SolicitudTutoria s : solicitud) {
										List<Estudiante> estudiantes = servicioEstudiante
												.buscarSolicitudesEstudiante(s);

										String programa = "";
										String descripcionPrograma = "";
										String correoPrograma = "";
										String cedulaEstudiante = "";
										String nombreEstudiante = "";
										String apellidoEstudiante = "";
										String correoEstudiante = "";
										String direccionEstudiante = "";
										for (Estudiante e : estudiantes) {
											cedulaEstudiante = e.getCedula();
											nombreEstudiante = e.getNombre();
											apellidoEstudiante = e
													.getApellido();
											correoEstudiante = e
													.getCorreoElectronico();
											direccionEstudiante = e
													.getDireccion();
											programa = e.getPrograma()
													.getNombre();
											descripcionPrograma = e
													.getPrograma()
													.getDescripcion();
											correoPrograma = e.getPrograma()
													.getCorreo();
										}

										String descripcionTeg = s
												.getDescripcion();
										Date fecha = s.getFecha();
										String estatus = s.getEstatus();
										String area = s.getTematica()
												.getareaInvestigacion()
												.getNombre();
										String descripcionArea = s
												.getTematica()
												.getareaInvestigacion()
												.getDescripcion();
										String tematica = s.getTematica()
												.getNombre();
										String descripcionTematica = s
												.getTematica().getDescripcion();
										String cedulaTutor = s.getProfesor()
												.getCedula();
										String nombreTutor = s.getProfesor()
												.getNombre();
										String apellidoTutor = s.getProfesor()
												.getApellido();
										String correoTutor = s.getProfesor()
												.getCorreoElectronico();
										String direccionTutor = s.getProfesor()
												.getDireccion();

										elementos.add(new Solicitud(
												descripcionTeg, fecha, estatus,
												cedulaTutor, nombreTutor,
												apellidoTutor, correoTutor,
												direccionTutor,
												cedulaEstudiante,
												nombreEstudiante,
												apellidoEstudiante,
												correoEstudiante,
												direccionEstudiante, programa,
												correoPrograma,
												descripcionPrograma, tematica,
												descripcionTematica, area,
												descripcionArea));

									}
									ltbElegiAreaTematica1
											.setModel(new ListModelList<Solicitud>(
													elementos));
									ltbElegiAreaTematica1.setVisible(true);

								} else {
									Messagebox
									.show("No hay informacion disponible para esta seleccion",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									btnExportarPlano.setDisabled(true);
								}
							}

						}

					}

				}

			}
		}

	}

	@Listen("onClick = #btnCancelar")
	public void cancelarSolicitudes() throws JRException {
		cancelar();
		rdoDefensa.setChecked(false);
		rdoProyecto.setChecked(false);
		rdoTEG.setChecked(false);
		rdoTutoria.setChecked(false);
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		cmbTematica.setDisabled(true);
		cmbArea.setDisabled(true);
		dtbDesde.setValue(new Date());
		dtbHasta.setValue(new Date());
		wdwReporteSolicitud.setWidth("auto");
		grbReporteSolicitud.setWidth("auto");

		btnExportarPlano.setDisabled(true);
	}

	// *************************
	// EXPORTAR DOCUMENTO
	// *************************
	void exportarDocumento() throws IOException {
		if ((rdoProyecto.isChecked() == true) || (rdoTEG.isChecked() == true)
				|| (rdoDefensa.isChecked() == true)) {
			if (elementos1.size() != 0) {

				String rutaUrl = obtenerDirectorio();
				int cantidad = rutaUrl.indexOf(".");
				String rutaUrl2 = rutaUrl.substring(0, cantidad);
				String ruta = rutaUrl2
						+ "SITEG/WebContent/vistas/reportes/no-estructurados/";
				String sFichero = "";
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" + "_"
						+ "hhmmss");
				Calendar cal = Calendar.getInstance(); // hoy
				String valor = sdf.format(cal.getTime());
				sFichero = ruta + "SolicitudesDefensa.txt";
				File fichero = new File(sFichero);
				if (chequeoEstatus == 1) {
					// Elegi Programa
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));
						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getArea());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionArea());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 2) {
					// Elegi Programa y area
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 3) {
					// Elegi Programa, area y tematica
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCorreoEstudiante());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 4) {
					// ELEGI AREA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 5) {
					// ELEGI AREA Y TEMATICA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION PROGRAMA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionPrograma());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 6) {
					// ELEGI NADA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA TEG");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x).getArea());
							linea.append("\t");
							linea.append(elementos1.get(x).getDescripcionArea());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos1.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}
			}

		} else if (rdoTutoria.isChecked() == true) {
			NombreTxt = "SolictudesTutoria";
			if (elementos.size() != 0) {

				String rutaUrl = obtenerDirectorio();
				int cantidad = rutaUrl.indexOf(".");
				String rutaUrl2 = rutaUrl.substring(0, cantidad);
				String ruta = rutaUrl2
						+ "SITEG/WebContent/vistas/reportes/no-estructurados/";
				String sFichero = "";
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" + "_"
						+ "hhmmss");
				Calendar cal = Calendar.getInstance(); // hoy
				String valor = sdf.format(cal.getTime());
				sFichero = ruta + "SolicitudesDefensa.txt";
				File fichero = new File(sFichero);
				if (chequeoEstatus == 8) {
					// Elegi Programa
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));
						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("DESCRIPCION");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTATUS");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE PROFESRO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getEstatus());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDireccionEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getArea());
							linea.append("\t");
							linea.append(elementos.get(x).getDescripcionArea());
							linea.append("\t");
							linea.append(elementos.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 10) {
					// Elegi Programa y area
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("DESCRIPCION");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTATUS");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getEstatus());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDireccionEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 9) {
					// Elegi Programa, area y tematica
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("DESCRIPCION");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTATUS");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION ESTUDIANTE");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getEstatus());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDireccionEstudiante());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 11) {
					// ELEGI AREA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("DESCRIPCION");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTATUS");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getEstatus());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDireccionEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionPrograma());
							linea.append("\t");
							linea.append(elementos.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 12) {
					// ELEGI AREA Y TEMATICA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("DESCRIPCION");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTATUS");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION PROGRAMA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getEstatus());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDireccionEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionPrograma());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 7) {
					// ELEGI NADA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("DESCRIPCION");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("FECHA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTATUS");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION PROFESOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CEDULA ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("NOMBRE ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("APELLIDO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("CORREO ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DIRECCION ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("DESCRIPCION TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos.get(x).getDescripcionTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getFechaTeg());
							linea.append("\t");
							linea.append(elementos.get(x).getEstatus());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaProfesor());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getApellidoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getDireccionTutor());
							linea.append("\t");
							linea.append(elementos.get(x).getCedulaEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getApellidoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getCorreoEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDireccionEstudiante());
							linea.append("\t");
							linea.append(elementos.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionPrograma());
							linea.append("\t");
							linea.append(elementos.get(x).getArea());
							linea.append("\t");
							linea.append(elementos.get(x).getDescripcionArea());
							linea.append("\t");
							linea.append(elementos.get(x).getTematica());
							linea.append("\t");
							linea.append(elementos.get(x)
									.getDescripcionTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", NombreTxt + valor
									+ ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}
			}

		}
	}

	/* Metdo que permite cerrar la vista. */
	@Listen("onClick= #btnSalir")
	public void salir() {
		wdwReporteSolicitud.onClose();
	}

	/*
	 * Metdo que permite exportar en un archivo plano, los datos previamente
	 * cargados.
	 */
	@Listen("onClick= #btnExportarPlano")
	public void exportarReporte() {
		try {

			exportarDocumento();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
