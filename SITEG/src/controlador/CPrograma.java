package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Condicion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.compuesta.CondicionPrograma;
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

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
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoPrograma;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Programa*/
@Controller
public class CPrograma extends CGeneral {

	private static final long serialVersionUID = 7118786801690906815L;
	CCatalogoPrograma catalogo = new CCatalogoPrograma();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	@Wire
	private Textbox txtNombrePrograma;
	@Wire
	private Textbox txtDescripcionPrograma;
	@Wire
	private Textbox txtCorreoPrograma;
	@Wire
	private Listbox ltbPrograma;
	@Wire
	private Window wdwCatalogoPrograma;
	@Wire
	private Textbox txtNombreMostrarPrograma;
	@Wire
	private Textbox txtDescripcionMostrarPrograma;
	@Wire
	private Button btnEliminarPrograma;
	@Wire
	private Button btnGuardarPrograma;
	@Wire
	private Textbox txtDirectorPrograma;
	@Wire
	private Window wdwPrograma;
	@Wire
	private Image imagenx;
	private static String cedulaProfesor;
	long id = 0;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	public void inicializar(Component comp) {

		txtDirectorPrograma.setDisabled(true);
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if (map != null) {
			if (map.get("cedula") != null) {

				cedulaProfesor = (String) map.get("cedula");
				Profesor profesorDirector = servicioProfesor
						.buscarProfesorPorCedula(cedulaProfesor);
				txtDirectorPrograma.setValue(profesorDirector.getNombre() + " "
						+ profesorDirector.getApellido());
			}

			if (map.get("idPrograma") != null)
				id = ((Long) map.get("idPrograma"));
			if (map.get("nombrePrograma") != null)
				txtNombrePrograma.setValue((String) map.get("nombrePrograma"));
			if (map.get("descripcionPrograma") != null)
				txtDescripcionPrograma.setValue((String) map
						.get("descripcionPrograma"));
			if (map.get("correoPrograma") != null)
				txtCorreoPrograma.setValue((String) map.get("correoPrograma"));
			if ((Long) map.get("id") != null) {

				id = ((Long) map.get("id"));
				Programa programa = servicioPrograma.buscar(id);
				txtNombrePrograma.setValue(programa.getNombre());
				txtDescripcionPrograma.setValue(programa.getDescripcion());
				txtCorreoPrograma.setValue(programa.getCorreo());

				cedulaProfesor = (String) programa.getDirectorPrograma()
						.getCedula();
				Profesor profesorDirector = servicioProfesor
						.buscarProfesorPorCedula(cedulaProfesor);
				txtDirectorPrograma.setValue(profesorDirector.getNombre() + " "
						+ profesorDirector.getApellido());
				btnEliminarPrograma.setDisabled(false);
				map.clear();
				map = null;
			}
		}

		txtCorreoPrograma
		.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un correo como: ejemplo@ejemplo.com");
		
	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnBuscarPrograma")
	public void buscarPrograma() {
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoPrograma.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VPrograma");

	}

	/*
	 * Metodo que permite abrir el catalogo de profesores que no son directores
	 * de programa se envia al metodo del catalogo el nombre de la vista a la
	 * que deben regresar los valores, asi como los valores q se encuentran en
	 * la vista actualmente para que no sean eliminados cuando regrese el
	 * catalogo con la informacion
	 */
	@Listen("onClick = #btnCatalogoDirectorPrograma")
	public void buscarDirector() {

		txtCorreoPrograma.setConstraint("");
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("director", "director");
		if ((txtNombrePrograma.getText().compareTo("") != 0))
			map.put("nombrePrograma", txtNombrePrograma.getValue());

		if (id != 0)
			map.put("idPrograma", id);

		if ((txtDescripcionPrograma.getText().compareTo("") != 0))
			map.put("descripcionPrograma", txtDescripcionPrograma.getValue());
		if ((txtCorreoPrograma.getText().compareTo("") != 0))
			map.put("correoPrograma", txtCorreoPrograma.getValue());
		

		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoDirectorPrograma.zul", null, null);
		window.doModal();
		txtCorreoPrograma
		.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un texto como: ejemplo@ejemplo.com");
		catalogoProfesor.recibir("maestros/VPrograma");
	}

	/*
	 * Metodo que permite el guardado o modificacion de una entidad Programa,
	 * asi como la asignacion, de ser necesario, de las condiciones vigentes a
	 * dicho programa
	 */
	@Listen("onClick = #btnGuardarPrograma")
	public void guardarPrograma() {
		if ((txtNombrePrograma.getText().compareTo("") == 0)
				|| (txtDescripcionPrograma.getText().compareTo("") == 0)
				|| (txtCorreoPrograma.getText().compareTo("") == 0)
				|| (txtDirectorPrograma.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			Messagebox.show("¿Desea guardar los datos del programa?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Programa programaBuscado = servicioPrograma
										.buscar(id);
								if (programaBuscado != null) {
									Grupo grupo = servicioGrupo
											.BuscarPorNombre("ROLE_DIRECTOR");
									Set<Grupo> gruposUsuario = new HashSet<Grupo>();
									Usuario usuario = programaBuscado
											.getDirectorPrograma().getUsuario();
									List<Grupo> grupos = servicioGrupo
											.buscarGruposDelUsuario(usuario);
									for (int i = 0; i < grupos.size(); i++) {

										if (grupos.get(i).getId() == grupo
												.getId()) {
											grupos.remove(i);
											i--;
										} else {

											gruposUsuario.add(grupos.get(i));
										}
									}
									usuario.setGrupos(gruposUsuario);
									servicioUsuario.guardar(usuario);
								}
								Profesor directorPrograma = servicioProfesor
										.buscarProfesorPorCedula(cedulaProfesor);
								crearUsuarioProfesor(imagenx, directorPrograma,
										"ROLE_DIRECTOR");
								String mensaje = "Ha sido seleccionado como director de programa de: "
										+ txtNombrePrograma.getValue()
										+ ","
										+ " su usuario es: "
										+ directorPrograma.getCedula()
										+ " y su contrasena: "
										+ directorPrograma.getCedula();
								enviarEmailNotificacion(
										directorPrograma.getCorreoElectronico(),
										mensaje);

								String nombre = txtNombrePrograma.getValue();
								String descripcion = txtDescripcionPrograma
										.getValue();
								String correo = txtCorreoPrograma.getValue();
								Boolean estatus = true;

								Programa programa = new Programa(id, nombre,
										descripcion, correo, estatus,
										directorPrograma);
								servicioPrograma.guardar(programa);
								if (programaBuscado == null) {
									programaBuscado = servicioPrograma
											.buscarUltimo();
								}
								if (servicioLapso.buscarActivos().size() != 0) {
									List<CondicionPrograma> condicionesPrograma = new ArrayList<CondicionPrograma>();
									List<Condicion> condiciones = servicioCondicion
											.buscarActivos();
									Lapso lapso = servicioLapso
											.BuscarLapsoActual();
									for (int i = 0; i < condiciones.size(); i++) {
										Condicion condicion = condiciones
												.get(i);
										CondicionPrograma condicionPrograma = new CondicionPrograma();
										condicionPrograma = servicioCondicionPrograma
												.buscarPorCondicionProgramaYLapso(
														condicion,
														programaBuscado, lapso);
										if (condicionPrograma == null) {
											condicionPrograma = new CondicionPrograma();
											condicionPrograma
													.setPrograma(programaBuscado);
											condicionPrograma.setLapso(lapso);
											condicionPrograma
													.setCondicion(condicion);
											condicionPrograma.setValor(0);
										}
										condicionesPrograma
												.add(condicionPrograma);
									}

									servicioCondicionPrograma
											.guardar(condicionesPrograma);
								}
								cancelarPrograma();
								Messagebox.show(
										"Programa registrado exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
								id = 0;
							}
						}
					});

		}
	}

	/* Metodo que permite la eliminacion logica de una entidad Programa */
	@Listen("onClick = #btnEliminarPrograma")
	public void eliminarPrograma() {
		Messagebox.show("¿Desea eliminar los datos del programa?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Programa programa = servicioPrograma.buscar(id);
							programa.setEstatus(false);
							servicioPrograma.guardar(programa);
							cancelarPrograma();
							Messagebox.show("Programa eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
							btnEliminarPrograma.setDisabled(true);
						}
					}
				});
	}

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarPrograma")
	public void cancelarPrograma() {
		id = 0;
		txtNombrePrograma.setValue("");
		txtDescripcionPrograma.setValue("");
		btnEliminarPrograma.setDisabled(true);
		txtDirectorPrograma.setValue("");
		txtCorreoPrograma.setConstraint("");
		txtCorreoPrograma.setValue("");
		txtCorreoPrograma
				.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un texto como: ejemplo@ejemplo.com");
	}

	/* Metodo que permite cerrar la ventana correspondiente a los programas */
	@Listen("onClick = #btnSalirPrograma")
	public void salirPrograma() {
		wdwPrograma.onClose();
	}
	
	/*
	 * Metodo que permite buscar si un programa existe, de acuerdo al nombre del
	 * programa
	 */
	@Listen("onChange = #txtNombrePrograma")
	public void buscarNombrePrograma() {
		Programa programa = servicioPrograma
				.buscarPorNombrePrograma(txtNombrePrograma.getValue());
		if (programa != null) {

			Profesor profesorDirector = servicioProfesor
					.buscarProfesorPorCedula(programa.getDirectorPrograma()
							.getCedula());
			txtDirectorPrograma.setValue(profesorDirector.getNombre() + " "
					+ profesorDirector.getApellido());
			txtNombrePrograma.setValue(programa.getNombre());
			txtDescripcionPrograma.setValue(programa.getDescripcion());
			txtCorreoPrograma.setValue(programa.getCorreo());
			id = programa.getId();
			btnEliminarPrograma.setDisabled(false);

		}

	}
	
}
