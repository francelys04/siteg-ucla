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

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.compuesta.CondicionPrograma;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

import servicio.SActividad;
import servicio.SArchivo;
import servicio.SAreaInvestigacion;
import servicio.SAvance;
import servicio.SCategoria;
import servicio.SCondicion;
import servicio.SCondicionPrograma;
import servicio.SCronograma;
import servicio.SDefensa;
import servicio.SEnlaceInteres;
import servicio.SEstudiante;
import servicio.SFactibilidad;
import servicio.SItem;
import servicio.SItemDefensa;
import servicio.SItemFactibilidad;
import servicio.SJurado;
import servicio.SLapso;
import servicio.SMencion;
import servicio.SNoticia;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SProgramaItem;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STegEstatus;
import servicio.STegRequisito;
import servicio.STematica;
import servicio.STipoJurado;
import servicio.seguridad.SArbol;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;

@Controller
public abstract class CGeneral extends SelectorComposer<Component> {

	protected SActividad servicioActividad = GeneradorBeans
			.getServicioActividad();
	protected SArbol servicioArbol = GeneradorBeans.getServicioArbol();
	protected SArchivo servicioArchivo = GeneradorBeans.getServicioArchivo();
	protected SAreaInvestigacion servicioArea = GeneradorBeans
			.getServicioArea();
	protected SAvance servicioAvance = GeneradorBeans.getServicioAvance();
	protected SCategoria servicioCategoria = GeneradorBeans
			.getServicioCategoria();
	protected SCondicion servicioCondicion = GeneradorBeans
			.getServicioCondicion();
	protected SCondicionPrograma servicioCondicionPrograma = GeneradorBeans
			.getServicioCondicionPrograma();
	protected SCronograma servicioCronograma = GeneradorBeans.getServicioCronograma();
	protected SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();
	protected SEnlaceInteres servicioEnlace = GeneradorBeans
			.getServicioEnlace();
	protected SEstudiante servicioEstudiante = GeneradorBeans
			.getServicioEstudiante();
	protected SFactibilidad servicioFactibilidad = GeneradorBeans
			.getServicioFactibilidad();
	protected SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	protected SItem servicioItem = GeneradorBeans.getServicioItem();
	protected SItemDefensa servicioItemDefensa = GeneradorBeans
			.getServicioItemDefensa();
	protected SItemFactibilidad servicioItemFactibilidad = GeneradorBeans
			.getServicioItemFactibilidad();
	protected SJurado servicioJurado = GeneradorBeans.getServicioJurado();
	protected SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	protected SMencion servicioMencion = GeneradorBeans.getServicioMencion();
	protected SNoticia servicioNoticia = GeneradorBeans.getServicioNoticia();
	protected SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	protected SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	protected SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	protected SProgramaItem servicioProgramaItem = GeneradorBeans
			.getServicioProgramaItem();
	protected SProgramaRequisito servicioProgramaRequisito = GeneradorBeans
			.getServicioProgramaRequisito();
	protected SRequisito servicioRequisito = GeneradorBeans
			.getServicioRequisito();
	protected SSolicitudTutoria servicioSolicitudTutoria = GeneradorBeans
			.getServicioTutoria();
	protected STeg servicioTeg = GeneradorBeans.getServicioTeg();
	protected STegEstatus servicioTegEstatus = GeneradorBeans
			.getServicioTegEstatus();
	protected STegRequisito servicioTegRequisito = GeneradorBeans
			.getServicioTegRequisito();
	protected STematica servicioTematica = GeneradorBeans.getSTematica();
	protected STipoJurado servicioTipoJurado = GeneradorBeans
			.getServicioTipoJurado();
	protected SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

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

