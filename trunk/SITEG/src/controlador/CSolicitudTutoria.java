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
import modelo.Grupo;
import modelo.ItemEvaluacion;
import modelo.SolicitudTutoria;
import modelo.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
import servicio.SGrupo;
import servicio.SSolicitudTutoria;
import servicio.SUsuario;

@Controller
public class CSolicitudTutoria extends CGeneral {

	SSolicitudTutoria servicioTutoria = GeneradorBeans.getServicioTutoria();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	@Wire
	private Datebox dtbFechaEvaluarTutoria;
	@Wire
	private Label txtProgramaEvaluarTutorias;
	@Wire
	private Label txtAreaEvaluarTutorias;
	@Wire
	private Label txtTematicaEvaluarTutorias;
	@Wire
	private Label txtTituloSolicitud;
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
	private String[] mensaje = {
			"Su Tutoria ha sido aprobada, le enviamos su usuario y contraseña",
			"Su Tutoria ha sido rechazada, por favor intente con otro tutor" };
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);	
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitud");

		if (map != null) {
			if (map.get("id") != null) {
				id = (Long) map.get("id");
				solicitud = servicioTutoria.buscarSolicitud(id);
				List<Estudiante> estudiantes = servicioEstudiante.buscarSolicitudesEstudiante(solicitud);
				ltbSolicitudesEstudiantes.setModel(new ListModelList<Estudiante>(estudiantes));
				txtAreaEvaluarTutorias.setValue(solicitud.getTematica().getareaInvestigacion().getNombre());
				txtTematicaEvaluarTutorias.setValue(solicitud.getTematica().getNombre());
				txtTituloSolicitud.setValue(solicitud.getDescripcion());
				txtProgramaEvaluarTutorias.setValue(solicitud.getProfesor().getPrograma().getNombre());
				map.clear();
				map = null;
			}
		}
	}
	
	@Listen("onClick = #btnAceptarTutoria")
	public void aceptarTutoria() throws IOException{
		System.out.println(solicitud.getDescripcion());
		solicitud.setEstatus("Aprobada");
		
		Set<Grupo> gruposUsuario = new HashSet<Grupo>();
		Grupo grupo = servicioGrupo.BuscarPorNombre("ROLE_USER");
		gruposUsuario.add(grupo);
		byte[] imagenUsuario = null;
		URL url = getClass().getResource("/configuracion/usuario.png");
		System.out.println(url);
		imagenx.setContent(new AImage(url));
		imagenUsuario = imagenx.getContent().getByteData();
		for (int i = 0; i < ltbSolicitudesEstudiantes.getItemCount(); i++){
			Estudiante estudiante = ltbSolicitudesEstudiantes.getItems().get(i)
					.getValue();
			Usuario user = servicioUsuario.buscarUsuarioPorNombre(estudiante.getCedula());
			if(user==null){
			System.out.println("Aqui");
			Usuario usuario = new Usuario(0, estudiante.getCedula(), passwordEncoder.encode(estudiante.getCedula()), true, gruposUsuario, imagenUsuario);
			servicioUsuario.guardar(usuario);
			user = servicioUsuario.buscarUsuarioPorNombre(estudiante.getCedula());
			System.out.println(user.getNombre());
			estudiante.setUsuario(user);
			servicioEstudiante.guardar(estudiante);
			}			
			valor.add(enviarEmailNotificacion(estudiante.getCorreoElectronico(), mensaje[0]+" Usuario: "+user.getNombre()+"  "+"Contraseña: "+user.getNombre()));
		}
		servicioTutoria.guardarSolicitud(solicitud);
		confirmacion(valor);
		salir();
	}

	@Listen("onClick = #btnRechazarTutoria")
	public void rechazarTutoria(){
		solicitud.setEstatus("Rechazada");
		servicioTutoria.guardarSolicitud(solicitud);
		for (int i = 0; i < ltbSolicitudesEstudiantes.getItemCount(); i++){
			Estudiante estudiante = ltbSolicitudesEstudiantes.getItems().get(i)
					.getValue();
			valor.add(enviarEmailNotificacion(estudiante.getCorreoElectronico(), mensaje[1]));
		}
		confirmacion(valor);
		salir();
	}

	private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for(int w=0; w<valor2.size();w++){
			if(valor2.get(w).equals(false)){
				return Messagebox.show("Correo electronico no enviado", "Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
		return Messagebox.show("Correo electronico enviado","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
	}
	
	public void recibir (String vista)
	{
		vistaRecibida = vista;

	}
	
	private void salir(){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwEvaluarTutorias.onClose();
	}
}
