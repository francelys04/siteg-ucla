package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.Estudiante;

import modelo.ItemEvaluacion;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import servicio.SEstudiante;

import servicio.SSolicitudTutoria;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;


@Controller
public class CSolicitudTutoria extends CGeneral {
	@Wire
	private Datebox dtbFechaEvaluarTutoria;
	@Wire
	private Textbox txtProgramaEvaluarTutorias;
	@Wire
	private Textbox  txtAreaEvaluarTutorias;
	@Wire
	private Textbox  txtTematicaEvaluarTutorias;
	@Wire
	private Textbox  txtTituloSolicitud;
	@Wire
	private Listbox ltbSolicitudesEstudiantes;
	@Wire
	private Image imagenx;
	@Wire
	private Window wdwEvaluarTutorias;
	private long id = 0;
	SolicitudTutoria solicitud;
	ArrayList<Boolean> valor = new ArrayList<Boolean>();
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static String vistaRecibida;
	private static String estatusSolicitud;
	private String[] mensaje = {
			"Su Solicitud de Tutoria ha sido aprobada, le enviamos su usuario y contrasena",
			"Su Solicitud de Tutoria ha sido rechazada, por favor intente con otro tutor" };
	
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);	
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitud");
			//Permite mapear los datos del catalogo de solicitudes de tutoria
			//para atender una en especifico		
		if (map != null) {
			if (map.get("id") != null) {
				id = (Long) map.get("id");
				solicitud = servicioSolicitudTutoria.buscarSolicitud(id);
				List<Estudiante> estudiantes = servicioEstudiante.buscarSolicitudesEstudiante(solicitud);
				ltbSolicitudesEstudiantes.setModel(new ListModelList<Estudiante>(estudiantes));
				txtAreaEvaluarTutorias.setValue(solicitud.getTematica().getareaInvestigacion().getNombre());
				txtTematicaEvaluarTutorias.setValue(solicitud.getTematica().getNombre());
				txtTituloSolicitud.setValue(solicitud.getDescripcion());
				//debe guiarse con el programa del estudiante que esta pidiendo la tutoria.
				txtProgramaEvaluarTutorias.setValue(estudiantes.get(0).getPrograma().getNombre());
				map.clear();
				map = null;
			}
		}
	}
	//Metodo para aceptar la tutoria
	@Listen("onClick = #btnAceptarTutoria")
	public void aceptarTutoria() throws IOException{
		Messagebox.show("¿Desea aceptar la tutoria de este proyecto?",
				"Dialogo de confirmacion", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt)
							throws InterruptedException {
						if (evt.getName().equals("onOK")) {
		solicitud.setEstatus("Aceptada");
		estatusSolicitud = "Aceptada";
		Set<Grupo> gruposUsuario = new HashSet<Grupo>();
		Grupo grupo = servicioGrupo.BuscarPorNombre("ROLE_ESTUDIANTE");
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
		for (int i = 0; i < ltbSolicitudesEstudiantes.getItemCount(); i++){
			Estudiante estudiante = ltbSolicitudesEstudiantes.getItems().get(i)
					.getValue();
			Usuario user = servicioUsuario.buscarUsuarioPorNombre(estudiante.getCedula());
			if(user==null){
			
			Usuario usuario = new Usuario(0, estudiante.getCedula(), passwordEncoder.encode(estudiante.getCedula()), true, gruposUsuario, imagenUsuario);
			servicioUsuario.guardar(usuario);
			user = servicioUsuario.buscarUsuarioPorNombre(estudiante.getCedula());
			estudiante.setUsuario(user);
			servicioEstudiante.guardar(estudiante);
			}			
			valor.add(enviarEmailNotificacion(estudiante.getCorreoElectronico(), mensaje[0]+" Usuario: "+user.getNombre()+"  "+"Contrasena: "+user.getNombre()));
		}
		servicioSolicitudTutoria.guardarSolicitud(solicitud);
		confirmacion(valor);
		salir();
						}
					}
				});
	}
	//Metodo para rechazar la tutoria
	@Listen("onClick = #btnRechazarTutoria")
	public void rechazarTutoria(){
		Messagebox.show("¿Desea rechazar la tutoria de este proyecto?",
				"Dialogo de confirmacion", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt)
							throws InterruptedException {
						if (evt.getName().equals("onOK")) {
		solicitud.setEstatus("Rechazada");
		estatusSolicitud = "Rechazada";
		servicioSolicitudTutoria.guardarSolicitud(solicitud);
		for (int i = 0; i < ltbSolicitudesEstudiantes.getItemCount(); i++){
			Estudiante estudiante = ltbSolicitudesEstudiantes.getItems().get(i)
					.getValue();
			valor.add(enviarEmailNotificacion(estudiante.getCorreoElectronico(), mensaje[1]));
		}
		confirmacion(valor);
		salir();
						}
					}
				});
	}
	//Metodo para confirmar que le ha llegado el correo al estudiante
	private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for(int w=0; w<valor2.size();w++){
			if(valor2.get(w).equals(false)){
				return Messagebox.show("Solicitud "+estatusSolicitud+", no se envio el correo al Estudiante", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
			}
		}
		return Messagebox.show("Solicitud "+estatusSolicitud+", el correo electronico ha sido enviado al Estudiante","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
	}
	
	public void recibir (String vista)
	{
		vistaRecibida = vista;

	}
	//metodo que permite salir y refrescar el catalogo de solicitudes de tutorias
	private void salir(){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwEvaluarTutorias.onClose();
	}
	
	
	@Listen("onClick = #btnSalirTutoria")
	public void salirTutoria() {
		wdwEvaluarTutorias.onClose();	
	}
}
