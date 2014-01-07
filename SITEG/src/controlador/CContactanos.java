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
import org.zkoss.zul.Messagebox;
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
	@Wire
	private Window wdwContactanos;
	
	

		
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
		String remitente = "siteg.ucla@gmail.com";
		String contraseña = "Equipo.2";
		 String destino = "siteg.ucla@gmail.com";
	    String cuerpo = "Nombre: " + nombre + " \n\n Correo: " + correo + " \n\n Mensaje: " + mensaje + ".";
	
	    //Obtenemos los destinatarios
        String destinos[] = destino.split(",");     
	    
	    
	        // Construimos el mensaje
	        MimeMessage message = new MimeMessage(session);
	         
	        message.setFrom(new InternetAddress(remitente));
	 
	        //Forma 3
            Address [] receptores = new Address [ destinos.length ];
            int j = 0;
            while(j<destinos.length){                  
            receptores[j] = new InternetAddress ( destinos[j] ) ;                 
            j++;              
            }
	         
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
	    	    	 Messagebox.show("Mensaje enviado","Informacion", Messagebox.OK,Messagebox.INFORMATION);  
	    	    	 cancelarCorreo();
	    	    	 wdwContactanos.onClose();
                 } else {
                	 Messagebox.show("Disculpe en estos momentos no se envio el Mensaje","Informacion", Messagebox.OK,Messagebox.INFORMATION);
                	 cancelarCorreo();
                	 wdwContactanos.onClose();
                 }
                  
                      
         }
	    
	    @Listen("onClick = #btnCancelarCorreo")
	    public void cancelarCorreo() {

	    	 txtNombre.setValue("");
	    	
	    	 txtCorreo.setValue("");
	    	 txtAsunto.setValue("");
	    	 txtMensaje.setValue("");
	    	
	    }
	  
	    	
	    }
	     
	
		
	

