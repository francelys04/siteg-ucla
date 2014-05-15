package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.compuesta.CondicionPrograma;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoProfesorTematica;

/**
 * Controlador que permite enviar una solicitud de tutoria a determinado
 * profesor, esta debe realizarla un estudiante previamente registrado
 * dentro del sistema SITEG
 */
@Controller
public class CSolicitarTutor extends CGeneral {

	private static final long serialVersionUID = -3742947611091908225L;
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private long id = 0;
	private static long valor;
	public static String estado;
	public static String combo1;
	public static String combo2;
	public static String descripcion;
	public static long idcombo;
	public static Programa programa;
	public static String area;
	public static String tematica;
	public static String titulo;
	public static String cedulaProfesor;
	public static Estudiante estudiante1;
	public static Date fecha;

	@Wire
	private Datebox db1;
	@Wire
	private Button btnAgregarEstudiante;
	@Wire
	private Listbox ltbEstudiantes;
	@Wire
	private Combobox cmbAreaSolicitud;
	@Wire
	private Combobox cmbTematicaSolicitud;
	@Wire
	private Textbox txtTituloSolicitud;
	@Wire
	private Textbox txtCedulaEstudiante;
	@Wire
	private Textbox txtCedulaProfesor;
	@Wire
	private Textbox txtNombreProfesor;
	@Wire
	private Textbox txtApellidoProfesor;
	@Wire
	private Textbox txtCorreoProfesor;
	@Wire
	private Image imagenTutor;
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;
	@Wire
	private Window wdwSolicitarTutoria;
	@Wire
	private Window wdwCatalogoProfesorArea;

	private static List<Estudiante> EstudiantesColocados = new ArrayList<Estudiante>();

	List<Estudiante> gridEstudiante = new ArrayList<Estudiante>();

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {

		cmbAreaSolicitud.setModel(new ListModelList<AreaInvestigacion>());
		if (combo1 == null) {
			// cmbAreaSolicitud.setDisabled(false);
		} else {
			cmbAreaSolicitud.setValue(combo1);
			combo1 = null;
			cmbAreaSolicitud.setDisabled(true);
			cmbAreaSolicitud.setStyle("color:black !important;");
			
		}
		if (combo2 == null) {
			// cmbTematicaSolicitud.setDisabled(false);
		} else {
			cmbTematicaSolicitud.setValue(combo2);
			combo2 = null;
			cmbTematicaSolicitud.setDisabled(true);
			cmbTematicaSolicitud.setStyle("color:black !important;");
		}
		if (descripcion != null) {
			txtTituloSolicitud.setValue(descripcion);
			descripcion = null;
		}
		if (EstudiantesColocados.size() != 0) {
			for (int i = 0; i < EstudiantesColocados.size(); i++) {

				ltbEstudiantes.setModel(new ListModelList<Estudiante>(
						EstudiantesColocados));
			}
			EstudiantesColocados.clear();
		}
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesor.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
				txtNombreProfesor.setValue(profesor.getNombre());
				txtApellidoProfesor.setValue(profesor.getApellido());
				txtCorreoProfesor.setValue(profesor.getCorreoElectronico());
			}
			map.clear();
			map = null;
		}
	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	public void limpiarDatosEstudiante() {
		txtCedulaEstudiante.setValue("");
	}

	/**
	 * Metodo que permite llenar el combo de areas asociadas al programa de los
	 * estudiantes de la solicitud; este combo se puede llenar luego de que al
	 * menos un estudiante es agregado a la solicitud
	 */
	public void llenarcombo() {

		idcombo = programa.getId();
		List<AreaInvestigacion> a = servicioProgramaArea
				.buscarAreasDePrograma(programa);
		cmbAreaSolicitud.setModel(new ListModelList<AreaInvestigacion>(a));

	}

	/**
	 * Metodo que permite llenar el combo de tematicas luego de que se
	 * selecciona un area
	 */
	@Listen("onSelect = #cmbAreaSolicitud")
	public void tematicaSolicitud() {

		cmbTematicaSolicitud.setValue("");
		AreaInvestigacion area2 = servicioArea.buscarArea(Long
				.parseLong(cmbAreaSolicitud.getSelectedItem().getId()));
		List<Tematica> tematicas = servicioTematica
				.buscarTematicasDeArea(area2);
		cmbTematicaSolicitud.setModel(new ListModelList<Tematica>(tematicas));

	}

	/**
	 * Metodo que permite obtener el id de la tematica seleccionada en el combo
	 * respectivo
	 */
	@Listen("onSelect = #cmbTematicaSolicitud")
	public void tematica() {
		valor = Long.parseLong(cmbTematicaSolicitud.getSelectedItem().getId());

	}

	/**
	 * Metodo que permite mostrar el catalogo de profesores asociados a la
	 * tematica expuesta en la solicitud
	 */
	@Listen("onClick = #btnCatalogoProfesorArea")
	public void buscarProfesor() {
		combo1 = cmbAreaSolicitud.getValue();
		combo2 = cmbTematicaSolicitud.getValue();
		descripcion = txtTituloSolicitud.getValue();
		for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
			Estudiante e = ltbEstudiantes.getItems().get(i).getValue();
			EstudiantesColocados.add(e);
		}
		if (combo2.compareTo("") == 0) {
			Messagebox.show("Debe elegir la tematica", "Error",
					Messagebox.OK, Messagebox.ERROR);
		}
		if ((combo2.compareTo("") != 0)) {
			catalogo.recibir("transacciones/VSolicitarTutor", idcombo, valor, 0);
			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesorTematica.zul", null,
					null);
			window.doModal();
			wdwSolicitarTutoria.onClose();
		}

	}

	/**
	 * Metodo que permite enviar una solicitud de tutoria a determinado
	 * profesor, donde se hace el llamado a varios metodos dentro de este
	 * controlador
	 */
	@Listen("onClick = #btnEnviarSolicitudtutoria")
	public void enviarSolicitud() {

		fecha = db1.getValue();
		estado = "Por Revisar";
		area = cmbAreaSolicitud.getValue();
		tematica = cmbTematicaSolicitud.getValue();
		titulo = txtTituloSolicitud.getValue();
		cedulaProfesor = txtCedulaProfesor.getValue();
		String nombreProfesor = txtNombreProfesor.getValue();
		String apellidoProfesor = txtApellidoProfesor.getValue();
		String correoProfesor = txtCorreoProfesor.getValue();
		for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
			Estudiante estudiante = ltbEstudiantes.getItems().get(i).getValue();
			gridEstudiante.add(estudiante);
		}
		if (gridEstudiante.size() == 0) {
			Messagebox.show("Debe agregar al menos un estudiante", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			if (cmbAreaSolicitud.getText().compareTo("") == 0
					|| cmbTematicaSolicitud.getText().compareTo("") == 0 
					|| txtTituloSolicitud.getText().compareTo("") == 0
					|| txtCedulaProfesor.getText().compareTo("") == 0 
					|| txtNombreProfesor.getText().compareTo("") == 0
					|| txtApellidoProfesor.getText().compareTo("") == 0 
					|| txtCorreoProfesor.getText().compareTo("") == 0) {
				Messagebox.show("Debes completar todos los campos", "Error",
						Messagebox.OK, Messagebox.ERROR);
			} else {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(cedulaProfesor);

				if (profesor == null) {
					Messagebox.show("El profesor no existe", "Advertencia",
							Messagebox.OK, Messagebox.EXCLAMATION);
				} else {
					Messagebox.show("¿Desea enviar la solicitud de tutoria?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {
										Tematica tematica2 = servicioTematica
												.buscarTematicaPorNombre(tematica);
										Profesor profesor = servicioProfesor
												.buscarProfesorPorCedula(cedulaProfesor);

										Set<Estudiante> estudiante2 = new HashSet<Estudiante>();
										for (int i = 0; i < ltbEstudiantes
												.getItemCount(); i++) {
											Estudiante estudiante3 = ltbEstudiantes
													.getItems().get(i)
													.getValue();
											estudiante2.add(estudiante3);
										}

										SolicitudTutoria solicitud2 = new SolicitudTutoria(
												id, fecha, titulo, estado,
												profesor, tematica2,
												estudiante2);

										servicioSolicitudTutoria
												.guardarSolicitud(solicitud2);
										crearUsuarioProfesor(imagenTutor,
												profesor, "ROLE_TUTOR");
										enviarEmailNotificacion();
										cancelarSolicitud();
										Messagebox
												.show("Su solicitud de tutoria ha sido enviada exitosamente",
														"Informacion",
														Messagebox.OK,
														Messagebox.INFORMATION);
										wdwSolicitarTutoria.onClose();
									}
								}
							});
				}
			}
		}
	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarSolicitudTutoria")
	public void cancelarSolicitud() {
		id = 0;
		idcombo = 0;
		cmbAreaSolicitud.getItems().clear();
		cmbTematicaSolicitud.getItems().clear();
		cmbAreaSolicitud.setValue("");
		cmbTematicaSolicitud.setValue("");
		cmbAreaSolicitud.setDisabled(false);
		cmbTematicaSolicitud.setDisabled(false);
		txtTituloSolicitud.setValue("");
		txtCedulaEstudiante.setValue("");
		ltbEstudiantes.getItems().clear();
		txtCedulaProfesor.setValue("");
		txtNombreProfesor.setValue("");
		txtApellidoProfesor.setValue("");
		txtCorreoProfesor.setValue("");
		programa = null;
		combo1 = null;
		combo2 = null;
		gridEstudiante.clear();

	}

	/**
	 * Metodo que permite añadir un estudiante a la solicitud, se puede añadir
	 * mas de un estudiante segun la cantidad de estudiantes que pueden realizar
	 * un mismo teg configurada en el programa correspondiente al estudiante
	 */
	@Listen("onClick = #btnAgregarEstudiante")
	public void AgregarEstudiante() {

		String cedulaEstudiante = txtCedulaEstudiante.getValue();
		if (cedulaEstudiante == "") {
			Messagebox.show("Debe ingresar su cedula", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Estudiante estudiante = servicioEstudiante
					.buscarEstudiante(cedulaEstudiante);
			if (estudiante == null) {
				Messagebox.show("Debe estar previamente Registrado",
						"Error", Messagebox.OK, Messagebox.ERROR);
				txtCedulaEstudiante.setValue("");
			} else {
				SolicitudTutoria solicitud = servicioSolicitudTutoria
						.buscarSolicitudEstudiantePorRevisar(estudiante);
				SolicitudTutoria solicitudAceptada = servicioSolicitudTutoria
						.buscarSolicitudAceptadaEstudiante(estudiante);
				if (solicitud != null) {
					Messagebox
							.show("Ya tiene una solicitud de tutoria que se encuentra por revisar",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);
				} else {
					if (solicitudAceptada != null) {
						Messagebox.show(
								"Ya tiene una solicitud de tutoria aceptada",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
					} else {
						boolean registro = true;
						Teg teg = servicioTeg.ultimoTeg(estudiante);
						if (teg != null) {
							if (!teg.getEstatus().equals("TEG Reprobado") && !teg.getEstatus().equals("Proyecto No Factible")) {
								registro = false;
							}
						}
						if (registro) {
							String condicion = "Numero de estudiantes por trabajo";

							System.out.println(estudiante.getPrograma().getNombre());
		
							CondicionPrograma cm = buscarCondicionVigenteEspecifica(
									condicion, estudiante.getPrograma());
							
							int valor = cm.getValor();
							
							System.out.println(ltbEstudiantes.getItemCount());
							
							if (ltbEstudiantes.getItemCount() >= valor) {
								
								Messagebox
										.show("No se permiten mas estudiantes por Trabajo Especial de Grado",
												"Error", Messagebox.OK,
												Messagebox.ERROR);
							} else {
								if (programa != null) {

									if (estudiante.getPrograma().getId() != programa
											.getId()) {
										Messagebox
												.show("Los estudiantes deben ser del mismo programa",
														"Error",
														Messagebox.OK,
														Messagebox.ERROR);
										txtCedulaEstudiante.setValue("");
									} else {
										programa = estudiante.getPrograma();
										gridEstudiante.add(estudiante);
										ltbEstudiantes
												.setModel(new ListModelList<Estudiante>(
														gridEstudiante));
										limpiarDatosEstudiante();
										llenarcombo();
									}
								} else {
									programa = estudiante.getPrograma();
									gridEstudiante.add(estudiante);
									ltbEstudiantes
											.setModel(new ListModelList<Estudiante>(
													gridEstudiante));
									limpiarDatosEstudiante();
									llenarcombo();
								}
							}
						} else {
							Messagebox.show("Ya tiene un TEG en proceso",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					}
				}
			}
		}
	}

	/**
	 * Metodo que envia un correo electronico al profesor de la solicitud de
	 * tutoria para informarle que posee una nueva solicitud, asi como su
	 * usuario y contrasenia
	 */
	private void enviarEmailNotificacion() {
		for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
			Estudiante estudiante = ltbEstudiantes.getItems().get(i).getValue();
			Profesor profesor = servicioProfesor
					.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
			String mensaje = "Ha recibido una nueva solicitud de tutoria de un Trabajo Especial de Grado del estudiante: "
					+ estudiante.getNombre() + ", " + estudiante.getApellido()
					+ ", titulado: "
					+ txtTituloSolicitud.getValue() +"."
					+ "\n\n Su usuario es: "
					+ profesor.getCedula() + " y su contrasena: "
					+ profesor.getCedula();
			enviarEmailNotificacion(profesor.getCorreoElectronico(), mensaje);
				
		}
	}

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirSolicitudTutoria")
	public void salirSolicitudTutoria() {
		cancelarSolicitud();
		wdwSolicitarTutoria.onClose();
	}

}