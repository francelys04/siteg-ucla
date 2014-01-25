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
import modelo.CondicionPrograma;
import modelo.Estudiante;
import modelo.Grupo;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
import servicio.SGrupo;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.STeg;
import servicio.SUsuario;
import servicio.SLapso;
import servicio.SCondicionPrograma;
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
				txtNombreTutorComision.setValue(teg2.getTutor().getNombre());
				txtApellidoTutorComision
						.setValue(teg2.getTutor().getApellido());
				txtTituloComision.setValue(teg2.getTitulo());
				txtCedulaTutorComision.setValue(teg2.getTutor().getCedula());
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
				.setValue("Recuerde que los integrantes de la comision es de: "
						+ buscarCondicionVigenteEspecifica(
								"Numero de integrantes de la comision", programa)
								.getValor());
				
				
				llenarListas();
				id = teg2.getId();

				map.clear();
				map = null;
			}
		}

	}

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

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	public int valorCondicion() {

		Teg tegComision = new Teg();
		int valor = 0;
		tegComision = servicioTeg.buscarTeg(auxiliarId);
		// se debe guiar con el programa del estudiante
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

		int valorcondicion = valorCondicion();
		int valorItem = 0;

		Listitem list1 = lsbProfesoresDisponibles.getSelectedItem();

		if (list1 == null)
			Messagebox.show("Debe Seleccionar un profesor", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		else

			list1.setParent(lsbProfesoresSeleccionados);
		valorItem = lsbProfesoresSeleccionados.getItemCount();

		if (valorItem > valorcondicion) {
			
			
			Messagebox.show(
					"El numero de profesores en la comisión no es el correcto, debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes de la comision", programa)
									.getValor(), "Error", Messagebox.OK,
					Messagebox.ERROR);

		}
	}

	// Metodo que permite quitar los profesores de la lista de integrantes de la
	// comision evaluadora
	@Listen("onClick = #btnRemoverProfesoresComision")
	public void removerProfesor() {
		Listitem list2 = lsbProfesoresSeleccionados.getSelectedItem();
		if (list2 == null)
			Messagebox.show("Debe Seleccionar un profesor", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		else
			list2.setParent(lsbProfesoresDisponibles);
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
					.show("Debe Seleccionar los profesores que conformarán la comisión evaluadora",
							"Error", Messagebox.OK, Messagebox.ERROR);
		else {

			int valorItem = lsbProfesoresSeleccionados.getItemCount();

			if (valorItem > valorcondicion) {

				Messagebox
						.show("El número de profesores seleccionados excede al número de integrantes de la comisión evaluadora, ya que debe ser: "+ buscarCondicionVigenteEspecifica(
								"Numero de integrantes de la comision", programa)
								.getValor(), "Error", Messagebox.OK,
				Messagebox.ERROR);
				
										

			} else {

				if (valorItem == valorcondicion) {

					Messagebox
							.show("El número de profesores que conformán la comisión evaluadora está completo, por favor seleccione el botón de finalizar",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);

				} else {
					Set<Grupo> gruposUsuario = new HashSet<Grupo>();
					Grupo grupo = servicioGrupo.BuscarPorNombre("ROLE_COMISION");
					gruposUsuario.add(grupo);
					byte[] imagenUsuario = null;
					URL url = getClass().getResource("/configuracion/usuario.png");
					try {
						imagenx.setContent(new AImage(url));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					imagenUsuario = imagenx.getContent().getByteData();

					Set<Profesor> profesoresSeleccionados = new HashSet<Profesor>();
					for (int i = 0; i < lsbProfesoresSeleccionados
							.getItemCount(); i++) {
						Profesor profesor = lsbProfesoresSeleccionados
								.getItems().get(i).getValue();
						profesoresSeleccionados.add(profesor);
						Usuario user = servicioUsuario.buscarUsuarioPorNombre(profesor.getCedula());
						if(user==null){
						System.out.println(user);
						Usuario usuario = new Usuario(0, profesor.getCedula(), passwordEncoder.encode(profesor.getCedula()), true, gruposUsuario, imagenUsuario);
						servicioUsuario.guardar(usuario);
						user = servicioUsuario.buscarUsuarioPorNombre(profesor.getCedula());
						profesor.setUsuario(user);
						servicioProfesor.guardarProfesor(profesor);
						}	
						else
						{
						
						List<Grupo> grupino = new ArrayList<Grupo>();
						grupino = serviciogrupo.buscarGruposDelUsuario(user);
						
						
						
							Grupo grupo2 = servicioGrupo.BuscarPorNombre("ROLE_COMISION");
						
						
							
							
							
							Set<Grupo> gruposU = new HashSet<Grupo>();
							for (int f = 0; f<grupino.size(); ++f)
							{
								Grupo g = grupino.get(f);
								System.out.println(grupino.get(f).getNombre());
								gruposU.add(g);
							}
							gruposU.add(grupo2);
							
							user.setGrupos(gruposU);
							
							servicioUsuario.guardar(user);
						}
					}

					Teg tegSeleccionado = servicioTeg.buscarTeg(auxiliarId);
					tegSeleccionado.setProfesores(profesoresSeleccionados);
					servicioTeg.guardar(tegSeleccionado);
					Messagebox
							.show("Los datos de la comisión fueron guardados exitosamente, pero todavía faltan miembros",
									"Información", Messagebox.OK,
									Messagebox.INFORMATION);
					salir();
				}

			}

		}

	}

	// Metodo que permite guardar los integrantes de la comision evaluadora
	@Listen("onClick = #btnFinalizarComision")
	public void finalizarComision() {

		System.out.println("entro en el boton finalizar");

		int valorcondicion = valorCondicion();

		Listitem listProfesoresSeleccionados = lsbProfesoresSeleccionados
				.getSelectedItem();
		if (listProfesoresSeleccionados == null)
			Messagebox
					.show("Debe Seleccionar los profesores que conformarán la comisión evaluadora",
							"Error", Messagebox.OK, Messagebox.ERROR);
		else {

			int valorItem = lsbProfesoresSeleccionados.getItemCount();

			if (valorItem > valorcondicion) {

				Messagebox
						.show("El número de profesores seleccionados excede al número de integrantes de la comisión evaluadora para este programa",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				if (valorItem == valorcondicion) {

					Set<Profesor> profesoresSeleccionados = new HashSet<Profesor>();
					for (int i = 0; i < lsbProfesoresSeleccionados
							.getItemCount(); i++) {
						Profesor profesor = lsbProfesoresSeleccionados
								.getItems().get(i).getValue();
						profesoresSeleccionados.add(profesor);
					}

					Teg tegSeleccionado = servicioTeg.buscarTeg(auxiliarId);
					tegSeleccionado.setProfesores(profesoresSeleccionados);
					tegSeleccionado.setEstatus("Comision Asignada");
					servicioTeg.guardar(tegSeleccionado);
					Messagebox.show("Comisión asignada exitosamente",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
					salir();
				} else {

					Messagebox
							.show("El número de profesores seleccionados excede al número de integrantes de la comisión evaluadora para este programa",
									"Error", Messagebox.OK, Messagebox.ERROR);

				}

			}

		}

	}

}
