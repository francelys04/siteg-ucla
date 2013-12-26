package controlador;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SUsuario;
import configuracion.GeneradorBeans;

@Controller
public abstract class CGeneral extends SelectorComposer<Component> {

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		inicializar(comp);
	}

	abstract void inicializar(Component comp);
	
	public Profesor ObtenerUsuarioProfesor() {
		// Agarrar datos del usuario
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(auth.getName());
		
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		Profesor profesor = servicioProfesor.buscarProfesorLoggeado(u);
		return profesor;
	}
	
	public Estudiante ObtenerUsuarioEstudiante() {
		// Agarrar datos del usuario
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(auth.getName());
		
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		Estudiante estudiante = servicioEstudiante.buscarEstudianteLoggeado(u);
		return estudiante;
	}
	
	public boolean enviarEmailNotificacion(String correo, String mensajes){
        try
        {                           
             // Propiedades de la conexion
            Properties props = new Properties();
            props.setProperty("mail.smtp.host",  "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");
          
            Session session = Session.getDefaultInstance(props);
            //Recoger los datos
            String asunto = "Notificacion de SITEG";
            String remitente = "siteg.ucla@gmail.com";
            String contrasena = "Equipo.2";
            String destino = correo;
            String mensaje = mensajes;
           
          
            //Obtenemos los destinatarios
            String destinos[] = destino.split(",");
                   
            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress( remitente ));
     
            //Forma 3
            Address [] receptores = new Address [ destinos.length ];
            int j = 0;
            while(j<destinos.length){                  
            receptores[j] = new InternetAddress ( destinos[j] ) ;                 
            j++;              
            }
     
           
            //receptores.
            message.addRecipients(Message.RecipientType.TO, receptores);      
            message.setSubject(asunto);      
            message.setText(mensaje);
                
            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect(remitente,contrasena);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                    
            // Cierre de la conexion.
            t.close();
            return true;
        }
      
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
