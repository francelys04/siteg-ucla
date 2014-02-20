package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*Controlador que se encarga de añadir los profesores integrantes de la
 * comision evaluadura para un determinado teg*/
@Controller
public class CAsignarComision extends CGeneral {

	private static final long serialVersionUID = -6196104502013500872L;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;
	private static Programa programa;
	private static String vistaRecibida;

	@Wire
	private Datebox dbfecha;
	@Wire
	private Textbox txtAreaComision;
	@Wire
	private Textbox txtTematicaComision;
	@Wire
	private Textbox txtTituloComision;
	@Wire
	private Textbox txtNombreTutorAsignarComision;
	@Wire
	private Textbox txtApellidoTutorAsignarComision;
	@Wire
	private Textbox txtProgramaComision;
	@Wire
	private Label lblCondicionAsignarComision;
	@Wire
	private Listbox lsbProfesoresDisponibles;
	@Wire
	private Listbox lsbProfesoresSeleccionados;
	@Wire
	private Listbox lsbEstudiantesTeg;
	@Wire
	private Window wdwAsignarComision;
	@Wire
	private Window wdwCatalogoAsignarComision;
	@Wire
	private Button btnGuardarComision;
	@Wire
	private Image imagenx;
	

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				txtNombreTutorAsignarComision.setValue(teg2.getTutor()
						.getNombre());
				txtApellidoTutorAsignarComision.setValue(teg2.getTutor()
						.getApellido());
				txtTituloComision.setValue(teg2.getTitulo());

				txtAreaComision.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaComision.setValue(teg2.getTematica().getNombre());
				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegPorCodigo);
				programa = estudiantes.get(0).getPrograma();
				txtProgramaComision.setValue(programa.getNombre());
				lsbEstudiantesTeg.setModel(new ListModelList<Estudiante>(
						estudiantes));

				lblCondicionAsignarComision
						.setValue("Recuerde que el numero de integrantes de la comision es de:  "
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes de la comision",
										programa).getValor());

				llenarListas();
				id = teg2.getId();

				map.clear();
				map = null;
			}
		}

	}

	/*
	 * Metodo que se encarga de llenar la lista de profesores disponibles, asi
	 * como la lista de los profesores que integran la comision evaluadora
	 */
	public void llenarListas() {

		Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
		List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg2);
		programa = est.get(0).getPrograma();
		List<Profesor> profesoresComision = servicioProfesor
				.buscarComisionDelTeg(teg2);

		if (profesoresComision.size() == 0) {

			List<Profesor> profesores = servicioProfesor.buscarActivos();
			lsbProfesoresDisponibles.setModel(new ListModelList<Profesor>(
					profesores));
			lsbProfesoresSeleccionados.setModel(new ListModelList<Profesor>());

		} else {

			lsbProfesoresSeleccionados.setModel(new ListModelList<Profesor>(
					profesoresComision));

			List<String> cedulas = new ArrayList<String>();
			for (int i = 0; i < profesoresComision.size(); i++) {
				String cedulasComision = profesoresComision.get(i).getCedula();
				cedulas.add(cedulasComision);
			}

			List<Profesor> profesoresDisponibles = servicioProfesor
					.buscarProfesoresSinComision(cedulas);

			lsbProfesoresDisponibles.setModel(new ListModelList<Profesor>(
					profesoresDisponibles));

		}
		lsbProfesoresDisponibles.setMultiple(false);
		lsbProfesoresDisponibles.setMultiple(true);
		lsbProfesoresSeleccionados.setMultiple(false);
		lsbProfesoresSeleccionados.setMultiple(true);

	}

	/*
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/*
	 * Metodo que permite buscar la cantidad de integrantes de la comision en el
	 * lapso actual y para el programa del trabajo asociado
	 */
	public int valorCondicion() {

		Teg tegComision = new Teg();
		int valor = 0;
		tegComision = servicioTeg.buscarTeg(auxiliarId);

		List<Estudiante> est = servicioEstudiante
				.buscarEstudiantesDelTeg(tegComision);
		auxIdPrograma = est.get(0).getPrograma().getId();
		Programa programa = servicioPrograma.buscar(auxIdPrograma);
		valor = buscarCondicionVigenteEspecifica(
				"Numero de integrantes de la comision", programa).getValor();
		return valor;
	}

	/*
	 * Metodo que permite cerrar la ventana, actualizando los cambios realizados
	 * en el resto del sistema
	 */
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAsignarComision.onClose();
	}

	/*
	 * Metodo que permite añadir a los profesores de la lista de disponibles
	 * hacia la lista de integrantes de la comision
	 */
	@Listen("onClick = #btnAgregarProfesoresComision")
	public void agregarProfesor() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable) lsbProfesoresDisponibles
				.getModel()).getSelection();
		int valor = selectedItems.size()
				+ lsbProfesoresSeleccionados.getItemCount();
		if (valor <= valorCondicion()) {

			if (selectedItems.size() == 0) {

				Messagebox.show("Debe seleccionar un profesor ", "Advertencia",
						Messagebox.OK, Messagebox.EXCLAMATION);

			} else {

				((ListModelList) lsbProfesoresSeleccionados.getModel())
						.addAll(selectedItems);
				((ListModelList) lsbProfesoresDisponibles.getModel())
						.removeAll(selectedItems);

			}
		} else

		{
			Messagebox
					.show("El numero de profesores seleccionados excede al numero de integrantes de la comision evaluadora, ya que debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes de la comision",
									programa).getValor(), "Error",
							Messagebox.OK, Messagebox.ERROR);

		}

	}

	/*
	 * Metodo que permite añadir a los profesores de la lista de integrantes de
	 * la comision hacia la lista de profesores disponibles
	 */
	@Listen("onClick = #btnRemoverProfesoresComision")
	public void removerProfesor() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable) lsbProfesoresSeleccionados
				.getModel()).getSelection();

		if (selectedItems.size() == 0) {

			Messagebox.show("Debe seleccionar un profesor ", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);

		} else {

			((ListModelList) lsbProfesoresDisponibles.getModel())
					.addAll(selectedItems);
			((ListModelList) lsbProfesoresSeleccionados.getModel())
					.removeAll(selectedItems);

		}

	}

	/*
	 * Metodo que permite hacer un llamado al metodo de actualizacion de las
	 * listas
	 */
	@Listen("onClick = #btnCancelarComision")
	public void limpiarCampos() {
		llenarListas();
	}

	/*
	 * Metodo que permite almacenar en la base de datos a los integrantes de la
	 * comision evaluadora en el respectivo teg
	 */
	@Listen("onClick = #btnGuardarComision")
	public void GuardarComision() {

		int valorcondicion = valorCondicion();

		Listitem listProfesoresSeleccionados = lsbProfesoresSeleccionados
				.getSelectedItem();

		int valorItem = lsbProfesoresSeleccionados.getItemCount();

		if (valorItem == 0)
			Messagebox
					.show("Debe Seleccionar los profesores que conformaran la comision evaluadora",
							"Error", Messagebox.OK, Messagebox.ERROR);
		else {

			if (valorItem > valorcondicion) {

				Messagebox
						.show("El numero de profesores seleccionados excede al numero de integrantes de la comision evaluadora, ya que debe ser: "
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes de la comision",
										programa).getValor(), "Error",
								Messagebox.OK, Messagebox.ERROR);

			} else {

				Messagebox.show("¿Desea guardar los miembros de la comision?",
						"Dialogo de confirmacion", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {

									Teg tegSeleccionado = servicioTeg
											.buscarTeg(auxiliarId);

									Set<Profesor> profesores = null;
									tegSeleccionado.setProfesores(profesores);

									/*
									 * Metodo para eliminar integrantes de la
									 * comision
									 */
									Set<Profesor> profesoresSeleccionados = new HashSet<Profesor>();

									for (int i = 0; i < lsbProfesoresSeleccionados
											.getItemCount(); i++) {
										Profesor profesor = lsbProfesoresSeleccionados
												.getItems().get(i).getValue();
										profesoresSeleccionados.add(profesor);
									}

									tegSeleccionado
											.setProfesores(profesoresSeleccionados);

									servicioTeg.guardar(tegSeleccionado);
									Messagebox
											.show("Los datos de la comision fueron guardados exitosamente, pero todavia faltan miembros",
													"Informacion",
													Messagebox.OK,
													Messagebox.INFORMATION);
									salir();
								}
							}
						});
			}

		}

	}

	/*
	 * Metodo que permite almacenar en la base de datos a los integrantes de la
	 * comision evaluadora en el respectivo teg, asi como tambien permite el
	 * cambio de estatus en el trabajo especial de grado, actualizando la tabla
	 * respectiva de cambios de estatus en la fecha actual. Ademas de que envia un
	 * usuario a cada miembro de la comision
	 */
	@Listen("onClick = #btnFinalizarComision")
	public void finalizarComision() {

		int valorcondicion = valorCondicion();
		int valorItem = lsbProfesoresSeleccionados.getItemCount();
		if (valorItem == 0) {
			Messagebox
					.show("Debe Seleccionar los profesores que conformaran la comision evaluadora",
							"Error", Messagebox.OK, Messagebox.ERROR);

		} else if (valorItem == valorcondicion) {
			Messagebox
					.show("¿Desea finalizar la asignacion de la comision evaluadora?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {
										Set<Grupo> gruposUsuario = new HashSet<Grupo>();
										Grupo grupo = servicioGrupo
												.BuscarPorNombre("ROLE_COMISION");
										gruposUsuario.add(grupo);
										byte[] imagenUsuario = null;
										URL url = getClass().getResource(
												"/configuracion/usuario.png");
										try {
											imagenx.setContent(new AImage(url));
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										imagenUsuario = imagenx.getContent()
												.getByteData();

										Set<Profesor> profesoresSeleccionados = new HashSet<Profesor>();
										for (int i = 0; i < lsbProfesoresSeleccionados
												.getItemCount(); i++) {
											Profesor profesor = lsbProfesoresSeleccionados
													.getItems().get(i)
													.getValue();
											profesoresSeleccionados
													.add(profesor);
											Usuario user = servicioUsuario
													.buscarUsuarioPorNombre(profesor
															.getCedula());
											if (user == null) {
												Usuario usuario = new Usuario(
														0,
														profesor.getCedula(),
														passwordEncoder
																.encode(profesor
																		.getCedula()),
														true, gruposUsuario,
														imagenUsuario);
												servicioUsuario
														.guardar(usuario);
												user = servicioUsuario
														.buscarUsuarioPorNombre(profesor
																.getCedula());
												profesor.setUsuario(user);
												servicioProfesor
														.guardarProfesor(profesor);
												String mensaje = "Su usuario es: "
														+ usuario.getNombre()
														+ "y su contraseÃ±a:"
														+ usuario.getPassword();
												enviarEmailNotificacion(
														profesor.getCorreoElectronico(),
														mensaje);
											} else {

												List<Grupo> grupino = new ArrayList<Grupo>();
												grupino = servicioGrupo
														.buscarGruposDelUsuario(user);
												Grupo grupo2 = servicioGrupo
														.BuscarPorNombre("ROLE_COMISION");
												Set<Grupo> gruposU = new HashSet<Grupo>();
												for (int f = 0; f < grupino
														.size(); ++f) {
													Grupo g = grupino.get(f);
													gruposU.add(g);
												}
												gruposU.add(grupo2);

												user.setGrupos(gruposU);

												servicioUsuario.guardar(user);
											}

										}

										Teg tegSeleccionado = servicioTeg
												.buscarTeg(auxiliarId);
										tegSeleccionado
												.setProfesores(profesoresSeleccionados);

										tegSeleccionado
												.setEstatus("Comision Asignada");

										/* Guardar datos en la tabla teg_estatus */
										java.util.Date fechaEstatus = new Date();
										TegEstatus tegEstatus = new TegEstatus(
												0, tegSeleccionado,
												"Comision Asignada",
												fechaEstatus);
										servicioTegEstatus.guardar(tegEstatus);

										servicioTeg.guardar(tegSeleccionado);
										Messagebox
												.show("Comision asignada exitosamente",
														"Informacion",
														Messagebox.OK,
														Messagebox.INFORMATION);
										salir();

									}
								}
							});
		} else {

			Messagebox
					.show("El numero de profesores seleccionados como integrantes de la comision evaluadora debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes de la comision",
									programa).getValor(), "Error",
							Messagebox.OK, Messagebox.ERROR);

		}

	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirComision")
	public void salirComision() {

		wdwAsignarComision.onClose();

	}

}