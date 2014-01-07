package controlador;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@Controller
	public class CContactanos  extends SelectorComposer<Component> {
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtCorreo;
	@Wire
	private Textbox txtAsunto;
	@Wire
	private Textbox txtMensaje;
	
	

		
		 //Devuelve true en caso de que se envie el email de manera correcta, o
	    //devuelve false si no se pudo enviar el email
	    private boolean enviarEmail() {
	    try
	    {
	        // Propiedades de la conexiÃ³n
	        Properties props = new Properties();
	        props.setProperty("mail.smtp.host",  "smtp.gmail.com");
	        props.setProperty("mail.smtp.starttls.enable", "true");
	        props.setProperty("mail.smtp.port", "587"); 
	        props.setProperty("mail.smtp.auth", "true");
	
	
	        // Preparamos la sesion
	        Session session = Session.getDefaultInstance(props);
	                 
	    //Recoger los datos
	        
	    String nombre = txtNombre.getText();
		String asunto = txtAsunto.getText(); 
		String correo = txtCorreo.getText();
	    String mensaje = txtMensaje.getText();
		String remitente = "Contacto.tegucla@gmail.com";
		String contraseña = "trabajosespecialesdegrado";
	    String cuerpo = "Nombre: " + nombre + " \n\n Correo: " + correo + " \n\n Mensaje: " + mensaje + ".";
	
	                 
	        // Construimos el mensaje
	        MimeMessage message = new MimeMessage(session);
	         
	        message.setFrom(new InternetAddress(remitente));
	 
		
	         
	        //receptores.
	        message.addRecipients(Message.RecipientType.TO, remitente);       
	        message.setSubject(asunto);
	        message.setText(cuerpo);
	             
	        // Lo enviamos.
	        Transport t = session.getTransport("smtp");
	        t.connect(remitente,contraseña);
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
	     
	    
	    @Listen("onClick = #btnEnviarCorreo")
		public void enviarCorreo() {
	    	     if(enviarEmail() ) {
                     JOptionPane.showMessageDialog(null,"Mensaje enviado!");                 
                 } else {
                     JOptionPane.showMessageDialog(null,"Por el momentono se pudo enviar el mensaje.");
                 }
                  
                      
         }   
	  
	    	
	    }
	     
	
		
	

