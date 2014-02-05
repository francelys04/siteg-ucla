package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.compuesta.CondicionPrograma;
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SActividad;
import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.STeg;
import servicio.SLapso;
import servicio.SCondicionPrograma;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;

public class CAsignarComision extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans
			.getServicioCondicionPrograma();
	SGrupo serviciogrupo = GeneradorBeans.getServicioGrupo();

	
	@Wire
	private Datebox dbfecha;
	@Wire
	private Textbox txtAreaComision;
	@Wire
	private Textbox txtTematicaComision;
	@Wire
	private Textbox txtTituloComision;
	@Wire
	private Textbox txtNombreTutorComision;
	@Wire
	private Textbox txtCedulaTutorComision;
	@Wire
	private Textbox txtApellidoTutorComision;
	@Wire
	private Textbox txtTutorAsignarComision;
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

	private static String vistaRecibida;

	@Wire
	private Image imagenx;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private List<Profesor> profesores;
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;
	private static Programa programa;
	public static int j;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				txtTutorAsignarComision.setValue(teg2.getTutor().getNombre() + " " + teg2.getTutor().getApellido());
				txtTituloComision.setValue(teg2.getTitulo());
				
				txtAreaComision.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaComision.setValue(teg2.getTematica().getNombre());
				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				// se toma es el programa del estudiante asociado a el teg.
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

	// Llena la lista de los profesores disponibles y los que ya han sido
	// asignados si es el caso
	public void llenarListas() {

		Programa programa = new Programa();

		Teg teg2 = servicioTeg.buscarTeg(auxiliarId);

		// para guiarse por el programa del estudiante
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
		lsbProfesoresDisponibles.setMultiple(true);
		lsbProfesoresSeleccionados.setMultiple(true);

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// busca la condicion de cantidad de miembros de la comision
	// se debe guiar con el programa del estudiante
	public int valorCondicion() {

		Teg tegComision = new Teg();
		int valor = 0;
		tegComision = servicioTeg.buscarTeg(auxiliarId);

		List<Estudiante> est = servicioEstudiante
				.buscarEstudiantesDelTeg(tegComision);
		auxIdPrograma = est.get(0).getPrograma().getId();
		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa = servicioPrograma.buscar(auxIdPrograma);
		List<CondicionPrograma> condicion = servicioCondicionPrograma
				.buscarCondicionesPrograma(programa, lapso);

		for (int i = 0; i < condicion.size(); i++) {

			if (condicion.get(i).getCondicion().getNombre()
					.equals("Numero de integrantes de la comision")) {
				valor = condicion.get(i).getValor();

			}
		}

		return valor;

	}

	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAsignarComision.onClose();
	}

	Teg teg2 = servicioTeg.buscarTeg(auxiliarId);

	// Metodo que permite agregar los profesores a la lista de integrantes de la
	// comision evaluadora
	@Listen("onClick = #btnAgregarProfesoresComision")
	public void agregarProfesor() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable)lsbProfesoresDisponibles.getModel()).getSelection();
		int valor= selectedItems.size()+lsbProfesoresSeleccionados.getItemCount();
	    System.out.println(valor);
		if (valor <= valorCondicion())
		{
		
			((ListModelList)lsbProfesoresSeleccionados.getModel()).addAll(selectedItems);
			((ListModelList)lsbProfesoresDisponibles.getModel()).removeAll(selectedItems);
		}
		else

		{
			Messagebox.show("El numero de profesores seleccionados excede al numero de integrantes de la comision evaluadora, ya que debe ser: "+ buscarCondicionVigenteEspecifica(
					"Numero de integrantes de la comision", programa)
					.getValor(), "Error", Messagebox.OK,Messagebox.ERROR);


		}

		
	}

	// Metodo que permite quitar los profesores de la lista de integrantes de la
	// comision evaluadora
	@Listen("onClick = #btnRemoverProfesoresComision")
	public void removerProfesor() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable)lsbProfesoresSeleccionados.getModel()).getSelection();
		((ListModelList)lsbProfesoresDisponibles.getModel()).addAll(selectedItems);
		((ListModelList)lsbProfesoresSeleccionados.getModel()).removeAll(selectedItems);
		
	}

	// Metodo que permite limpiar las listas tanto de los profesores disponibles
	// como el de los seleccionados para formar parte de la comision evaluador
	@Listen("onClick = #btnCancelarComision")
	public void limpiarCampos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		lsbProfesoresDisponibles.setModel(new ListModelList<Profesor>(
				profesores));
		lsbProfesoresSeleccionados.getItems().clear();

	}

	// Metodo que permite guardar los integrantes de la comision evaluadora
	@Listen("onClick = #btnGuardarComision")
	public void GuardarComision() {

		int valorcondicion = valorCondicion();

		Listitem listProfesoresSeleccionados = lsbProfesoresSeleccionados
				.getSelectedItem();

		if (listProfesoresSeleccionados == null)
			Messagebox
					.show("Debe Seleccionar los profesores que conformaran la comision evaluadora",
							"Error", Messagebox.OK, Messagebox.ERROR);
		else {

			int valorItem = lsbProfesoresSeleccionados.getItemCount();

			if (valorItem > valorcondicion) {

				Messagebox
						.show("El numero de profesores seleccionados excede al numero de integrantes de la comision evaluadora, ya que debe ser: "
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes de la comision",
										programa).getValor(), "Error",
								Messagebox.OK, Messagebox.ERROR);

			} else {

				if (valorItem == valorcondicion) {

					Messagebox
							.show("El numero de profesores que conforman la comision evaluadora esta completo, por favor seleccione el boton de finalizar",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);

				} else {
					Messagebox.show(
							"¿Desea guardar los miembros de la comision?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {

										

										Set<Profesor> profesoresSeleccionados = new HashSet<Profesor>();
										
										Teg tegSeleccionado = servicioTeg
												.buscarTeg(auxiliarId);
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

	}

	// Metodo que permite guardar los integrantes de la comision evaluadora
	@Listen("onClick = #btnFinalizarComision")
	public void finalizarComision() {

		int valorcondicion = valorCondicion();

		Listitem listProfesoresSeleccionados = lsbProfesoresSeleccionados
				.getSelectedItem();
		

			int valorItem = lsbProfesoresSeleccionados.getItemCount();

	

				if (valorItem == valorcondicion) {
					Messagebox
							.show("¿Desea finalizar la asignacion de la comision evaluadora?",
									"Dialogo de confirmacion", Messagebox.OK
											| Messagebox.CANCEL,
									Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener() {
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
														String mensaje= "Su usuario es: "+usuario.getNombre()+"y su contraseÃ±a:" +usuario.getPassword();
														enviarEmailNotificacion(profesor.getCorreoElectronico(), mensaje);
													} else {

														List<Grupo> grupino = new ArrayList<Grupo>();
														grupino = serviciogrupo
																.buscarGruposDelUsuario(user);
														Grupo grupo2 = servicioGrupo
																.BuscarPorNombre("ROLE_COMISION");
														Set<Grupo> gruposU = new HashSet<Grupo>();
														for (int f = 0; f < grupino
																.size(); ++f) {
															Grupo g = grupino.get(f);
															System.out
																	.println(grupino
																			.get(f)
																			.getNombre());
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
												servicioTeg
														.guardar(tegSeleccionado);
												Messagebox
														.show("Comision asignada exitosamente",
																"Informacion",
																Messagebox.OK,
																Messagebox.INFORMATION);
												salir();
												
											}
										}
									});
				} 

			}

		

	

	@Listen("onClick = #btnSalirComision")
	public void salirComision() {

		wdwAsignarComision.onClose();

	}

}