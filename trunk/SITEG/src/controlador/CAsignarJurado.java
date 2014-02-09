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
import modelo.TipoJurado;
import modelo.compuesta.Jurado;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SCondicionPrograma;
import servicio.SDefensa;
import servicio.SJurado;
import servicio.STeg;
import servicio.STipoJurado;
import servicio.seguridad.SGrupo;

@Controller
public class CAsignarJurado extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();
	SJurado servicioJurado = GeneradorBeans.getServicioJurado();
	STipoJurado servicioTipoJurado = GeneradorBeans.getServicioTipoJurado();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans
			.getServicioCondicionPrograma();

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
	private static String vistaRecibida;
	long idTeg = 0;
	private static Programa programa;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	List<Profesor> juradoDisponible = new ArrayList();
	List<Jurado> juradoOcupado = new ArrayList();

	Jurado jurado = new Jurado();

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);

				txtNombreTutorAsignarJurado
						.setValue(teg.getTutor().getNombre());
				txtApellidoTutorAsignarJurado.setValue(teg.getTutor()
						.getApellido());

				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				// para que se guie por el programa del estudiante
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg);
				programa = est.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(est.get(0).getPrograma()
						.getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());
				lblCondicionAtenderDefensa
						.setValue("Recuerde que la cantidad de integrantes del juarado es de: "
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes del jurado",
										programa).getValor());

			}
		}
	}

	// metodo que permite mover un profesor de disponible a seleccionado
	@Listen("onClick = #btnAgregarJurado")
	public void moverDerechaJurado() {

		List<Listitem> listitemEliminar = new ArrayList();
		List<Listitem> listItem = ltbJuradoDisponible.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {

				if (listItem.get(i).isSelected()) {

					Profesor profesor = listItem.get(i).getValue();
					juradoDisponible.remove(profesor);
					Jurado jurado = new Jurado();
					jurado.setProfesor(profesor);
					juradoOcupado.add(jurado);
					ltbJuradoSeleccionado.setModel(new ListModelList<Jurado>(
							juradoOcupado));
					listitemEliminar.add(listItem.get(i));
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

	// metodo que permite mover un profesor de seleccionado a disponible
	@Listen("onClick = #btnRemoverJurado")
	public void moverIzquierdaJurado() {

		List<Listitem> listitemEliminar = new ArrayList();
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

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// Metodo para llenar las listas de jurados tanto disponibles (para ser
	// seleccionado)
	// como los que ya han sido seleccionado en un teg (se los trae de la bd)
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

	// Metodo que permite finalizar la asignacion del jurado
	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensaDefinitiva() {
		if (validarJurado() && guardardatos()) {

			Messagebox.show("¿Desea guardar los miembros del jurado?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String estatus = "Jurado Asignado";
								java.util.Date fechaEstatus = new Date();
								Teg teg = servicioTeg.buscarTeg(idTeg);
								TegEstatus tegEstatus = new TegEstatus(0, teg, estatus, fechaEstatus);
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
					});
		} else {
			Messagebox.show(
					"El numero de profesores en el jurado no es el correcto, debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes del jurado",
									programa).getValor(), "Error",
					Messagebox.OK, Messagebox.ERROR);
		}
	}

	void crearUsuariosJurado() {
		Usuario user = new Usuario();
		Set<Grupo> gruposUsuario = new HashSet<Grupo>();
		List<Grupo> grupos = new ArrayList<Grupo>();
		Grupo grupo = new Grupo();
		grupo = servicioGrupo.BuscarPorNombre("ROLE_JURADO");
		byte[] imagenUsuario = null;
		URL url = getClass().getResource("/configuracion/usuario.png");
		try {
			imagenx.setContent(new AImage(url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imagenUsuario = imagenx.getContent().getByteData();
		gruposUsuario.add(grupo);
		for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
			Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
			long cedula = ((Spinner) ((listItem.getChildren().get(0)))
					.getFirstChild()).getValue();
			Profesor profesorJurado = servicioProfesor
					.buscarProfesorPorCedula(String.valueOf(cedula));
			user = servicioUsuario.buscarUsuarioPorNombre(String
					.valueOf(cedula));
			if (user == null) {
				Usuario usuario = new Usuario(0, String.valueOf(cedula),
						passwordEncoder.encode(String.valueOf(cedula)), true,
						gruposUsuario, imagenUsuario);
				servicioUsuario.guardar(usuario);
				user = servicioUsuario.buscarUsuarioPorNombre(String
						.valueOf(cedula));
				profesorJurado.setUsuario(user);
				servicioProfesor.guardarProfesor(profesorJurado);
			} else {
				grupos = servicioGrupo.buscarGruposDelUsuario(user);
				for (int j = 0; j < grupos.size(); j++) {
					gruposUsuario.add(grupos.get(j));
				}
				user.setGrupos(gruposUsuario);
				servicioUsuario.guardar(user);
			}
			String mensaje = "Usted foma parte del Jurado de un nuevo teg su usuario es: "
					+ user.getNombre()
					+ " y su contrasena: "
					+ profesorJurado.getCedula();
			enviarEmailNotificacion(profesorJurado.getCorreoElectronico(),
					mensaje);
		}
	}

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

	public boolean validarJurado() {
		if (ltbJuradoSeleccionado.getItemCount() == buscarCondicionVigenteEspecifica(
				"Numero de integrantes del jurado", programa).getValor()) {
			System.out.println("aqui");
			return true;
		} else
			System.out.println("alla");
		return false;
	}

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
			long cedula = ((Spinner) ((listItem.getChildren().get(0)))
					.getFirstChild()).getValue();
			Profesor profesorJurado = servicioProfesor
					.buscarProfesorPorCedula(String.valueOf(cedula));
			TipoJurado tipo = servicioTipoJurado.buscarPorNombre(tipojurado);
			Jurado jurado = new Jurado(teg, profesorJurado, tipo);
			jurados.add(jurado);
		}
		if (!error) {
			servicioJurado.guardar(jurados);
			return true;
		} else {
			Messagebox.show("Debe seleccionar un tipo para cada jurado",
					"Error", Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}

	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		Teg teg = servicioTeg.buscarTeg(idTeg);
		List<Profesor> juradoDisponible = servicioProfesor
				.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
		ltbJuradoSeleccionado.getItems().clear();

	}

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
