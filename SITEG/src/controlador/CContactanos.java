package controlador;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import modelo.Programa;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SPrograma;

@Controller
public class CContactanos extends CGeneral {
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtCorreo;
	@Wire
	private Combobox cmbProgramaContactanos;
	@Wire
	private Textbox txtAsunto;
	@Wire
	private Textbox txtMensaje;
	@Wire
	private Window wdwContactanos;
	
	public static String correoProgramas;
	

	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Programa> programas = servicioPrograma.buscarActivas();
		cmbProgramaContactanos.setModel(new ListModelList<Programa>(programas));

	}
	
	// Devuelve true en caso de que se envie el email de manera correcta, o
	// devuelve false si no se pudo enviar el email

	@Listen("onClick = #btnEnviarCorreo")
	public void enviarCorreo() {
		String nombre = txtNombre.getText();
		String asunto = txtAsunto.getText();
		String correo = txtCorreo.getText();
		String mensaje = txtMensaje.getText();
		
		String cuerpo = "Nombre: " + nombre + "\n\n Asunto: " + asunto +  " \n\n Correo: " + correo + " \n\n Mensaje: " + mensaje + ".";
		String n = cmbProgramaContactanos.getValue();
		Programa programa = servicioPrograma.buscarPorNombrePrograma(n);
		correoProgramas = programa.getCorreo();
		
		System.out.println(correoProgramas);
		boolean valor = enviarEmailNotificacion(correoProgramas, cuerpo);
		
		
		if (valor == true) {
			Messagebox.show("Mensaje enviado", "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
			cancelarCorreo();
			wdwContactanos.onClose();
		} else {
			Messagebox.show(
					"Disculpe en estos momentos no se envio el Mensaje",
					"Informacion", Messagebox.OK, Messagebox.INFORMATION);
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
		cmbProgramaContactanos.setValue("");

	}

}
