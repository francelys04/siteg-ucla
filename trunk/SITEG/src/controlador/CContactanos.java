package controlador;

import java.util.List;

import modelo.Programa;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Metodo que permite enviar un correo electronico con sugerencias a un
 * determinado programa
 */
@Controller
public class CContactanos extends CGeneral {
	
	private static final long serialVersionUID = -3009397611972912065L;
	public static String correoProgramas;
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

	/*
	 * Metodo heredado del Controlador CGeneral donde se llena el combo
	 * correspondiente a la vista
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Programa> programas = servicioPrograma.buscarActivas();
		cmbProgramaContactanos.setModel(new ListModelList<Programa>(programas));

	}

	/*
	 * Metodo que se encarga de validar que los cambios esten completos y se
	 * hace llamado del metodo de envio de correo en el controlador CGeneral,
	 * para enviar un correo electronico
	 */
	@Listen("onClick = #btnEnviarCorreo")
	public void enviarCorreo() {

		if ((cmbProgramaContactanos.getText().compareTo("") == 0)
				|| (txtNombre.getText().compareTo("") == 0)
				|| (txtCorreo.getText().compareTo("") == 0)
				|| (txtAsunto.getText().compareTo("") == 0)
				|| (txtMensaje.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			Messagebox.show("¿Desea enviar el correo?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombre.getText();
								String asunto = txtAsunto.getText();
								String correo = txtCorreo.getText();
								String mensaje = txtMensaje.getText();

								String cuerpo = "Nombre: " + nombre
										+ "\n\n Asunto: " + asunto
										+ " \n\n Correo: " + correo
										+ " \n\n Mensaje: " + mensaje + ".";
								String n = cmbProgramaContactanos.getValue();
								Programa programa = servicioPrograma
										.buscarPorNombrePrograma(n);
								correoProgramas = programa.getCorreo();

								boolean valor = enviarEmailNotificacion(
										correoProgramas, cuerpo);

								if (valor == true) {
									Messagebox.show(
											"Correo enviado exitosamente",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
									cancelarCorreo();
									wdwContactanos.onClose();
								} else {
									Messagebox
											.show("Disculpe en estos momentos no se envio el Mensaje",
													"Informacion",
													Messagebox.OK,
													Messagebox.INFORMATION);
									cancelarCorreo();
									wdwContactanos.onClose();
								}
							}
						}
					});
		}

	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarCorreo")
	public void cancelarCorreo() {

		txtNombre.setValue("");
		txtCorreo.setValue("");
		txtAsunto.setValue("");
		txtMensaje.setValue("");
		cmbProgramaContactanos.setValue("");

	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirCorreo")
	public void salirCorreo() {
		wdwContactanos.onClose();
	}

}
