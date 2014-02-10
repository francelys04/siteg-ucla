package controlador;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.Condicion;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.compuesta.CondicionPrograma;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

import servicio.SAreaInvestigacion;
import servicio.SCondicionPrograma;
import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STegEstatus;
import servicio.STematica;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;
import java.net.URL;

@Controller
public abstract class CGeneral extends SelectorComposer<Component> {

	protected SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	protected SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	protected SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	protected SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	protected SCondicionPrograma servicioCondicionPrograma = GeneradorBeans.getServicioCondicionPrograma();
	protected STegEstatus servicioTegEstatus = GeneradorBeans.getServicioTegEstatus();
	protected SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	protected SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	protected STematica servicioTematica = GeneradorBeans.getSTematica();
	protected SSolicitudTutoria servicioSolicitudTutoria = GeneradorBeans.getServicioTutoria();
	/*
	 * Metodo para inicializar componentes implementado en todos los controladores
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		inicializar(comp);
	}

	public abstract void inicializar(Component comp);
	
	public Profesor ObtenerUsuarioProfesor() {
		// Agarrar datos del usuario
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		Profesor profesor = servicioProfesor.buscarProfesorLoggeado(u);
		return profesor;
	}
	
	public CondicionPrograma buscarCondicionVigenteEspecifica(String nombre, Programa programa){
		List<CondicionPrograma> condicionesActuales = servicioCondicionPrograma.buscarUltimasCondiciones(programa);
		CondicionPrograma condicionBuscada = new CondicionPrograma();
		for(int i=0; i<condicionesActuales.size(); i++){
			if(condicionesActuales.get(i).getCondicion().getNombre().equals(nombre)){
				condicionBuscada = condicionesActuales.get(i);
			}
		}
		return condicionBuscada;
	}
	
	public Estudiante ObtenerUsuarioEstudiante() {
		// Agarrar datos del usuario
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		Estudiante estudiante = servicioEstudiante.buscarEstudianteLoggeado(u);
		return estudiante;
	}
	
	public boolean enviarEmailNotificacion(String correo, String mensajes){
        try
        {                           
  
      
       
    	// Propiedades de la conexion
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.port", "587");
		props.setProperty("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		// Recoger los datos
		String asunto = "Notificacion de SITEG";
		String remitente = "siteg.ucla@gmail.com";
		String contrasena = "Equipo.2";
		System.out.println("correo");
		System.out.println(correo);
		 String destino = correo;
         String mensaje = mensajes;

		// Obtenemos los destinatarios
		String destinos[] = destino.split(",");

		// Construimos el mensaje
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(remitente));

		// Forma 3
		Address[] receptores = new Address[destinos.length];
		int j = 0;
		while (j < destinos.length) {
			receptores[j] = new InternetAddress(destinos[j]);
			j++;
		}

		// receptores.
		message.addRecipients(Message.RecipientType.TO, receptores);
		message.setSubject(asunto);
		message.setText(mensaje);

		// Lo enviamos.
		Transport t = session.getTransport("smtp");
		t.connect(remitente, contrasena);
		 t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

		// Cierre de la conexion.
		t.close();
	
	return true;
}

catch (Exception e) {
	e.printStackTrace();
	return false;
   }
  }

	/**
	 * 
	 * @return El directorio actual de trabajo
	 */
	public static String obtenerDirectorio() {
		URL rutaURL = CGeneral.class.getProtectionDomain()
				.getCodeSource().getLocation();
		String ruta = rutaURL.getPath();
		return "/"+ruta.substring(1, ruta.indexOf("SITEG"));
	}
}

