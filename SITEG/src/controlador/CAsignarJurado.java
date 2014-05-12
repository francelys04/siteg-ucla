package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.TipoJurado;
import modelo.compuesta.Jurado;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Controlador asociado a la vista de asignar jurado, que permite agregar los
 * integrantes del jurado a un determinado TEG
 */
@Controller
public class CAsignarJurado extends CGeneral {

	private static final long serialVersionUID = -5599367529884634981L;
	private static String vistaRecibida;
	long idTeg = 0;
	private static Programa programa;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	List<Profesor> juradoDisponible = new ArrayList<Profesor>();
	List<Jurado> juradoOcupado = new ArrayList<Jurado>();
	Profesor tutor = new Profesor();
	Jurado jurado = new Jurado();
	Boolean tutorEnJurado = false;
	ListModelList<TipoJurado> tipos;

	@Wire
	private Textbox txtProgramaAtenderDefensa;
	@Wire
	private Textbox txtAreaAtenderDefensa;
	@Wire
	private Textbox txtTematicaAtenderDefensa;
	@Wire
	private Textbox txtTituloAtenderDefensa;
	@Wire
	private Label lblCondicionAtenderDefensa;
	@Wire
	private Listbox ltbJuradoDisponible;
	@Wire
	private Listbox ltbJuradoSeleccionado;
	@Wire
	private Listbox ltbEstudiantesAtenderDefensa;
	@Wire
	private Datebox dtbFechaAtenderDefensa;
	@Wire
	private Window wdwAsignarJurado;
	@Wire
	private Textbox txtNombreTutorAsignarJurado;
	@Wire
	private Textbox txtApellidoTutorAsignarJurado;
	@Wire
	private Image imagenx;

	/**
	 * Metodo que permite asignar una lista a un combo de manera dinamica, de
	 * tal forma que el id del combo no se repita
	 */
	public ListModelList<TipoJurado> getTipos() {
		tipos = new ListModelList<TipoJurado>(
				servicioTipoJurado.buscarActivos());
		return tipos;
	}

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				tutorEnJurado = false;
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);
				tutor = teg.getTutor();
				txtNombreTutorAsignarJurado
						.setValue(teg.getTutor().getNombre());
				txtApellidoTutorAsignarJurado.setValue(teg.getTutor()
						.getApellido());

				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(teg);
				programa = estudiantes.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(estudiantes.get(0)
						.getPrograma().getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());
				lblCondicionAtenderDefensa
						.setValue("Recuerde que la cantidad de integrantes del juarado es de: "
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes del jurado",
										programa).getValor());

			}
		}
	}

	/**
	 * Metodo que permite mover uno o varios profesores hacia la lista de
	 * integrantes del jurado. Y verifica que la cantidad de jurados asignados
	 * no supere el limite establecido en la configuracion del programa
	 */
	@Listen("onClick = #btnAgregarJurado")
	public void moverDerechaJurado() {

		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem = ltbJuradoDisponible.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {
				if (listItem.get(i).isSelected()) {
					if ((juradoOcupado.size() + 1) <= buscarCondicionVigenteEspecifica(
							"Numero de integrantes del jurado", programa)
							.getValor()) {
						Profesor profesor = listItem.get(i).getValue();
						juradoDisponible.remove(profesor);
						Jurado jurado = new Jurado();
						jurado.setProfesor(profesor);
						juradoOcupado.add(jurado);
						ltbJuradoSeleccionado
								.setModel(new ListModelList<Jurado>(
										juradoOcupado));
						listitemEliminar.add(listItem.get(i));
					} else {
						Messagebox
								.show("Ha excedido el limite de integrantes del jurado. El limite es de: "
										+ buscarCondicionVigenteEspecifica(
												"Numero de integrantes del jurado",
												programa).getValor()
										+ " integrante(s)", "Error",
										Messagebox.OK, Messagebox.ERROR);
						i = listItem.size();
					}
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbJuradoDisponible
					.removeItemAt(listitemEliminar.get(i).getIndex());
		}
		ltbJuradoSeleccionado.setMultiple(false);
		ltbJuradoSeleccionado.setCheckmark(false);
		ltbJuradoSeleccionado.setMultiple(true);
		ltbJuradoSeleccionado.setCheckmark(true);

	}

	/**
	 * Metodo que permite mover uno o varios profesores integrantes del jurado
	 * hacia la lista de la izquierda (profesores disponibles).
	 */
	@Listen("onClick = #btnRemoverJurado")
	public void moverIzquierdaJurado() {

		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem2 = ltbJuradoSeleccionado.getItems();
		if (listItem2.size() != 0) {
			for (int i = 0; i < listItem2.size(); i++) {
				if (listItem2.get(i).isSelected()) {
					Jurado jurado = listItem2.get(i).getValue();
					juradoOcupado.remove(jurado);
					juradoDisponible.add(jurado.getProfesor());
					ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
							juradoDisponible));
					listitemEliminar.add(listItem2.get(i));
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbJuradoSeleccionado.removeItemAt(listitemEliminar.get(i)
					.getIndex());
		}
		ltbJuradoDisponible.setMultiple(false);
		ltbJuradoDisponible.setCheckmark(false);
		ltbJuradoDisponible.setMultiple(true);
		ltbJuradoDisponible.setCheckmark(true);
	}

	/**
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 * 
	 * @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/**
	 * Metodo que permite llenar las listas de jurados disponibles, es decir,
	 * por asignar al teg, como los integrantes actuales del jurado
	 * 
	 * @param teg
	 *            trabajo especial de grado seleccionado para realizar la
	 *            asignacion del jurado
	 */
	public void llenarListas(Teg teg) {
		List<Estudiante> estudiantesTeg = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		ltbEstudiantesAtenderDefensa.setModel(new ListModelList<Estudiante>(
				estudiantesTeg));
		juradoDisponible = servicioProfesor.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
		juradoOcupado = servicioJurado.buscarJuradoDeTeg(teg);
		ltbJuradoSeleccionado
				.setModel(new ListModelList<Jurado>(juradoOcupado));
		ltbJuradoSeleccionado.setDisabled(true);
		ltbJuradoSeleccionado.setMultiple(false);
		ltbJuradoSeleccionado.setCheckmark(false);
		ltbJuradoSeleccionado.setMultiple(true);
		ltbJuradoSeleccionado.setCheckmark(true);
		ltbJuradoDisponible.setMultiple(false);
		ltbJuradoDisponible.setCheckmark(false);
		ltbJuradoDisponible.setMultiple(true);
		ltbJuradoDisponible.setCheckmark(true);

	}

	/**
	 * Metodo que permite finalizar la asignacion del jurado al teg, en el cual
	 * se cambia de estatus al teg, ademas de que se actualiza el historial de
	 * cambios del teg y se asignan usuarios a los integrantes del jurado. Todo
	 * esto se cumple si y solo si los metodos implicados dentro de este
	 * retornan valor verdadero
	 */
	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensaDefinitiva() {

		Messagebox.show("¿Desea guardar los miembros del jurado?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							if (validarJurado() && guardardatos()) {
								String estatus = "Jurado Asignado";
								java.util.Date fechaEstatus = new Date();
								Teg teg = servicioTeg.buscarTeg(idTeg);
								TegEstatus tegEstatus = new TegEstatus(0, teg,
										estatus, fechaEstatus);
								servicioTegEstatus.guardar(tegEstatus);
								teg.setEstatus(estatus);
								servicioTeg.guardar(teg);
								crearUsuariosJurado();
								Messagebox.show(
										"Jurados asignados exitosamente",
										"Información", Messagebox.OK,
										Messagebox.INFORMATION);
								salirAsignarJurado();
							}
						}
					}
				});

	}

	/**
	 * Metodo que permite la creacion de usuarios a todos los integrantes del
	 * jurado. Estos usuarios cuentan con el rol de jurado. Ademas se envia el
	 * usuario y la contraseña a cada integrante via email
	 */
	void crearUsuariosJurado() {

		for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
			Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
			long cedula = ((Intbox) ((listItem.getChildren().get(0)))
					.getFirstChild()).getValue();
			Profesor profesorJurado = servicioProfesor
					.buscarProfesorPorCedula(String.valueOf(cedula));
			crearUsuarioProfesor(imagenx, profesorJurado, "ROLE_JURADO");
			String mensaje = "Ha sido seleccionado para formar parte del Jurado de un nuevo TEG. Su usuario es: "
					+ profesorJurado.getCedula()
					+ " y su contrasena: "
					+ profesorJurado.getCedula();
			enviarEmailNotificacion(profesorJurado.getCorreoElectronico(),
					mensaje);
		}
	}

	/**
	 * Metodo que permite guardar de manera temporal la asignacion de jurados,
	 * si y solo, el metodo de validacion retorna verdadero
	 */
	@Listen("onClick = #btnAceptarDefensaMientrasTanto")
	public void aceptarDefensa() {
		if (guardardatos()) {
			Messagebox
					.show("Integrantes del jurado guardados para futuras modificaciones",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			salirAsignarJurado();
		}
	}

	/**
	 * Metodo que permite validar que la cantidad de integrantes del jurado sea
	 * la misma que la configurada para el programa en el lapso actual, retorna
	 * verdadero si se cumple y falso si no se cumple
	 */
	public boolean validarJurado() {
		if (ltbJuradoSeleccionado.getItemCount() == buscarCondicionVigenteEspecifica(
				"Numero de integrantes del jurado", programa).getValor()) {
			return true;
		} else
			Messagebox.show(
					"El numero de profesores en el jurado no es el correcto, debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes del jurado",
									programa).getValor(), "Error",
					Messagebox.OK, Messagebox.ERROR);
		return false;
	}

	/**
	 * Metodo que permite almacenar los datos de los jurados en la base de
	 * datos, si y solo si, se cumple que cada integrante del jurado posee un
	 * tipo asociado. Retorna verdadero si se cumple y falso en caso contrario
	 */
	public boolean guardardatos() {

		Teg teg = servicioTeg.buscarTeg(idTeg);
		List<Jurado> jurados = new ArrayList<Jurado>();
		boolean error = false;
		jurados = servicioJurado.buscarJuradoDeTeg(teg);
		if (!jurados.isEmpty()) {
			servicioJurado.limpiar(jurados);
			jurados.clear();
		}
		for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {

			Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
			String tipojurado = ((Combobox) ((listItem.getChildren().get(3)))
					.getFirstChild()).getValue();
			if (tipojurado == "") {
				error = true;
			}
			long cedula = ((Intbox) ((listItem.getChildren().get(0)))
					.getFirstChild()).getValue();
			Profesor profesorJurado = servicioProfesor
					.buscarProfesorPorCedula(String.valueOf(cedula));
			if (tutor.getCedula().equals(
					String.valueOf(profesorJurado.getCedula()))) {
				tutorEnJurado = true;
			}
			TipoJurado tipo = servicioTipoJurado.buscarPorNombre(tipojurado);
			Jurado jurado = new Jurado(teg, profesorJurado, tipo);
			jurados.add(jurado);
		}
		if (!error && tutorEnJurado) {
			servicioJurado.guardar(jurados);
			tutorEnJurado = false;
			return true;
		} else {
			if (!tutorEnJurado) {
				Messagebox.show("El tutor debe formar parte del jurado",
						"Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				Messagebox.show("Debe seleccionar un tipo para cada jurado",
						"Error", Messagebox.OK, Messagebox.ERROR);
			}
			return false;
		}
	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		Teg teg = servicioTeg.buscarTeg(idTeg);
		tutorEnJurado = false;
		llenarListas(teg);

	}

	/**
	 * Metodo que permite cerrar la pantalla actualizando los cambios realizados
	 */
	@Listen("onClick = #btnSalirAsignarJurado")
	public void salirAsignarJurado() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAsignarJurado.onClose();
	}

}
