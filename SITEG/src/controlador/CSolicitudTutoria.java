package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Estudiante;
import modelo.SolicitudTutoria;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite al tutor aceptar o rechazar una solicitud de
 * tutoria
 */
@Controller
public class CSolicitudTutoria extends CGeneral {

	private static final long serialVersionUID = 490757979306802411L;
	private long id = 0;
	SolicitudTutoria solicitud;
	ArrayList<Boolean> valor = new ArrayList<Boolean>();
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static String vistaRecibida;
	private static String estatusSolicitud;
	private String[] mensaje = {
			"Su Solicitud de Tutoria ha sido aprobada, se envia su usuario y contrasena",
			"Su Solicitud de Tutoria ha sido rechazada, por favor intente con otro tutor" };
	@Wire
	private Datebox dtbFechaEvaluarTutoria;
	@Wire
	private Textbox txtProgramaEvaluarTutorias;
	@Wire
	private Textbox txtAreaEvaluarTutorias;
	@Wire
	private Textbox txtTematicaEvaluarTutorias;
	@Wire
	private Textbox txtTituloSolicitud;
	@Wire
	private Listbox ltbSolicitudesEstudiantes;
	@Wire
	private Image imagenx;
	@Wire
	private Window wdwEvaluarTutorias;

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
				.getCurrent().getAttribute("catalogoSolicitud");
		if (map != null) {
			if (map.get("id") != null) {
				id = (Long) map.get("id");
				solicitud = servicioSolicitudTutoria.buscarSolicitud(id);
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarSolicitudesEstudiante(solicitud);
				ltbSolicitudesEstudiantes
						.setModel(new ListModelList<Estudiante>(estudiantes));
				txtAreaEvaluarTutorias.setValue(solicitud.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaEvaluarTutorias.setValue(solicitud.getTematica()
						.getNombre());
				txtTituloSolicitud.setValue(solicitud.getDescripcion());
				txtProgramaEvaluarTutorias.setValue(estudiantes.get(0)
						.getPrograma().getNombre());
				map.clear();
				map = null;
			}
		}
	}

	/*
	 * Metodo que permite aceptar la solicitud de tutoria donde se cambia el
	 * estatus de la solicitud a Aceptada. Ademas se crea un usuario para cada
	 * estudiante que integra la solicitud y este es enviado a su correo
	 */
	@Listen("onClick = #btnAceptarTutoria")
	public void aceptarTutoria() throws IOException {
		Messagebox.show("¿Desea aceptar la tutoria de este proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							solicitud.setEstatus("Aceptada");
							estatusSolicitud = "Aceptada";
							Set<Grupo> gruposUsuario = new HashSet<Grupo>();
							Grupo grupo = servicioGrupo
									.BuscarPorNombre("ROLE_ESTUDIANTE");
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
							imagenUsuario = imagenx.getContent().getByteData();
							for (int i = 0; i < ltbSolicitudesEstudiantes
									.getItemCount(); i++) {
								List<Grupo> grupos = new ArrayList<Grupo>();
								Estudiante estudiante = ltbSolicitudesEstudiantes
										.getItems().get(i).getValue();
								Usuario user = servicioUsuario
										.buscarUsuarioPorNombre(estudiante
												.getCedula());
								if (user == null) {

									Usuario usuario = new Usuario(0, estudiante
											.getCedula(), passwordEncoder
											.encode(estudiante.getCedula()),
											true, gruposUsuario, imagenUsuario);
									servicioUsuario.guardar(usuario);
									user = servicioUsuario
											.buscarUsuarioPorNombre(estudiante
													.getCedula());
									estudiante.setUsuario(user);
									servicioEstudiante.guardar(estudiante);
								}else {
									grupos = servicioGrupo.buscarGruposDelUsuario(user);
									for (int j = 0; j < grupos.size(); j++) {
										gruposUsuario.add(grupos.get(j));
									}
									user.setGrupos(gruposUsuario);
									servicioUsuario.guardar(user);
									servicioEstudiante.guardar(estudiante);
								}
								valor.add(enviarEmailNotificacion(
										estudiante.getCorreoElectronico(),
										mensaje[0] + " Usuario: "
												+ user.getNombre() + "  "
												+ "Contrasena: "
												+ user.getNombre()));
							}
							servicioSolicitudTutoria
									.guardarSolicitud(solicitud);
							confirmacion(valor);
							salir();
						}
					}
				});
	}

	/*
	 * Metodo que permite rechazar la solicitud de tutoria, donde se cambia el
	 * estatus de la solicitud a Rechazada y se envia un correo a los
	 * integrantes de la solicitud notificando la novedad
	 */
	@Listen("onClick = #btnRechazarTutoria")
	public void rechazarTutoria() {
		Messagebox.show("¿Desea rechazar la tutoria de este proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							solicitud.setEstatus("Rechazada");
							estatusSolicitud = "Rechazada";
							servicioSolicitudTutoria
									.guardarSolicitud(solicitud);
							for (int i = 0; i < ltbSolicitudesEstudiantes
									.getItemCount(); i++) {
								Estudiante estudiante = ltbSolicitudesEstudiantes
										.getItems().get(i).getValue();
								valor.add(enviarEmailNotificacion(
										estudiante.getCorreoElectronico(),
										mensaje[1]));
							}
							confirmacion(valor);
							salir();
						}
					}
				});
	}

	/*
	 * Metodo que permite confirmar que se logro enviar efectivamente cada
	 * correo electronico
	 */
	private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for (int w = 0; w < valor2.size(); w++) {
			if (valor2.get(w).equals(false)) {
				return Messagebox.show("Solicitud " + estatusSolicitud
						+ ", no se envio el correo al Estudiante",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);
			}
		}
		return Messagebox.show("Solicitud " + estatusSolicitud
				+ ", el correo electronico ha sido enviado al Estudiante",
				"Informacion", Messagebox.OK, Messagebox.INFORMATION);
	}

	/*
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

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
		wdwEvaluarTutorias.onClose();
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirTutoria")
	public void salirTutoria() {
		wdwEvaluarTutorias.onClose();
	}
}
