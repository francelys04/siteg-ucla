package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Image;

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

/*
 * Controlador heredado por los demas controladores, permite la
 * inicializacion de los componentes de la vista, ademas de que posee la
 * declaracion de todos los servicios para que sean utilizados por los demas
 * controladores como atributos obtenidos por herencia. Ademas posee ciertos
 * metodos genericos, que son empleados en algunos controladores con el
 * objeto de minimizar codigo y mejorar las practicas de programacion
 */
@Controller
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class CGeneral extends SelectorComposer<Component> {

	private static final long serialVersionUID = 445877799825285911L;
	protected PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@WireVariable("SActividad")
	protected SActividad servicioActividad;
	@WireVariable("SArbol")
	protected SArbol servicioArbol;
	@WireVariable("SArchivo")
	protected SArchivo servicioArchivo;
	@WireVariable("SAreaInvestigacion")
	protected SAreaInvestigacion servicioArea;
	@WireVariable("SAvance")
	protected SAvance servicioAvance;
	@WireVariable("SCategoria")
	protected SCategoria servicioCategoria;
	@WireVariable("SCondicion")
	protected SCondicion servicioCondicion;
	@WireVariable("SCondicionPrograma")
	protected SCondicionPrograma servicioCondicionPrograma;
	@WireVariable("SCronograma")
	protected SCronograma servicioCronograma;
	@WireVariable("SDefensa")
	protected SDefensa servicioDefensa;
	@WireVariable("SEnlaceInteres")
	protected SEnlaceInteres servicioEnlace;
	@WireVariable("SEstudiante")
	protected SEstudiante servicioEstudiante;
	@WireVariable("SFactibilidad")
	protected SFactibilidad servicioFactibilidad;
	@WireVariable("SGrupo")
	protected SGrupo servicioGrupo;
	@WireVariable("SItem")
	protected SItem servicioItem;
	@WireVariable("SItemDefensa")
	protected SItemDefensa servicioItemDefensa;
	@WireVariable("SItemFactibilidad")
	protected SItemFactibilidad servicioItemFactibilidad;
	@WireVariable("SJurado")
	protected SJurado servicioJurado;
	@WireVariable("SLapso")
	protected SLapso servicioLapso;
	@WireVariable("SMencion")
	protected SMencion servicioMencion;
	@WireVariable("SNoticia")
	protected SNoticia servicioNoticia;
	@WireVariable("SProfesor")
	protected SProfesor servicioProfesor;
	@WireVariable("SPrograma")
	protected SPrograma servicioPrograma;
	@WireVariable("SProgramaArea")
	protected SProgramaArea servicioProgramaArea;
	@WireVariable("SProgramaItem")
	protected SProgramaItem servicioProgramaItem;
	@WireVariable("SProgramaRequisito")
	protected SProgramaRequisito servicioProgramaRequisito;
	@WireVariable("SRequisito")
	protected SRequisito servicioRequisito;
	@WireVariable("SSolicitudTutoria")
	protected SSolicitudTutoria servicioSolicitudTutoria;
	@WireVariable("STeg")
	protected STeg servicioTeg;
	@WireVariable("STegEstatus")
	protected STegEstatus servicioTegEstatus;
	@WireVariable("STegRequisito")
	protected STegRequisito servicioTegRequisito;
	@WireVariable("STematica")
	protected STematica servicioTematica;
	@WireVariable("STipoJurado")
	protected STipoJurado servicioTipoJurado;
	@WireVariable("SUsuario")
	protected SUsuario servicioUsuario;

	/*
	 * Metodo que permite inicializar los componentes de las vistas, es
	 * implementado en todos los controladores
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		inicializar(comp);
	}

	public abstract void inicializar(Component comp);

	/*
	 * Metodo que permite obtener el profesor que se encuentra actualmente en
	 * sesion
	 */
	public Profesor ObtenerUsuarioProfesor() {
		Profesor profesor = servicioProfesor
				.buscarProfesorLoggeado(ObtenerUsuario());
		return profesor;
	}

	/*
	 * Metodo que permite obtener el estudiante que se encuentra actualmente en
	 * sesion
	 */
	public Estudiante ObtenerUsuarioEstudiante() {
		Estudiante estudiante = servicioEstudiante
				.buscarEstudianteLoggeado(ObtenerUsuario());
		return estudiante;
	}

	/*
	 * Metodo que permite captar el usuario que se encuentra en sesion,
	 * obteniendolo a traves de la autentificacion del mismo
	 */
	public Usuario ObtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario usuario = servicioUsuario
				.buscarUsuarioPorNombre(auth.getName());
		return usuario;
	}

	/*
	 * Metodo que permite obtener cierta condicion dado su nombre, en el lapso
	 * vigente y para determinado programa
	 */
	public CondicionPrograma buscarCondicionVigenteEspecifica(String nombre,
			Programa programa) {
		List<CondicionPrograma> condicionesActuales = servicioCondicionPrograma
				.buscarUltimasCondiciones(programa);
		CondicionPrograma condicionBuscada = new CondicionPrograma();
		for (int i = 0; i < condicionesActuales.size(); i++) {
			if (condicionesActuales.get(i).getCondicion().getNombre()
					.equals(nombre)) {
				condicionBuscada = condicionesActuales.get(i);
			}
		}
		return condicionBuscada;
	}

	/* Metodo que permite enviar un correo electronico a cualquier destinatario */
	public boolean enviarEmailNotificacion(String correo, String mensajes) {
		try {

			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(props);
			String asunto = "Notificacion de SITEG";
			String remitente = "siteg.ucla@gmail.com";
			String contrasena = "Equipo.2";
			String destino = correo;
			String mensaje = mensajes;

			String destinos[] = destino.split(",");

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(remitente));

			Address[] receptores = new Address[destinos.length];
			int j = 0;
			while (j < destinos.length) {
				receptores[j] = new InternetAddress(destinos[j]);
				j++;
			}

			message.addRecipients(Message.RecipientType.TO, receptores);
			message.setSubject(asunto);
			message.setText(mensaje);

			Transport t = session.getTransport("smtp");
			t.connect(remitente, contrasena);
			t.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));

			t.close();

			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/* Metodo que permite obtener el directorio actual del proyecto */
	public static String obtenerDirectorio() {
		URL rutaURL = CGeneral.class.getProtectionDomain().getCodeSource()
				.getLocation();
		String ruta = rutaURL.getPath();
		return "/" + ruta.substring(1, ruta.indexOf("SITEG"));
	}

	/*
	 * Metodo generico que permite la creacion de un usuario para un profesor,
	 * que recibe la imagen que tendra dicho usuario, asi como el rol y el
	 * objeto profesor al que se le asignara el usuario
	 */
	public void crearUsuarioProfesor(Image imagenTutor, Profesor profesor,
			String rol) {
		Usuario usuario = new Usuario();
		Set<Grupo> gruposUsuario = new HashSet<Grupo>();
		List<Grupo> grupos = new ArrayList<Grupo>();
		Grupo grupo = new Grupo();
		grupo = servicioGrupo.BuscarPorNombre(rol);
		byte[] imagenUsuario = null;
		URL url = getClass().getResource("/configuracion/usuario.png");
		try {
			imagenTutor.setContent(new AImage(url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imagenUsuario = imagenTutor.getContent().getByteData();
		gruposUsuario.add(grupo);
		usuario = servicioUsuario.buscarUsuarioPorNombre(profesor.getCedula());
		if (usuario == null) {
			usuario = new Usuario(0, profesor.getCedula(),
					passwordEncoder.encode(profesor.getCedula()), true,
					gruposUsuario, imagenUsuario);
			servicioUsuario.guardar(usuario);
			usuario = servicioUsuario.buscarUsuarioPorNombre(profesor
					.getCedula());
			profesor.setUsuario(usuario);
			servicioProfesor.guardarProfesor(profesor);
		} else {
			usuario = profesor.getUsuario();
			grupos = servicioGrupo.buscarGruposDelUsuario(usuario);
			for (int j = 0; j < grupos.size(); j++) {
				gruposUsuario.add(grupos.get(j));
			}
			usuario.setGrupos(gruposUsuario);
			servicioUsuario.guardar(usuario);
		}
	}
}
