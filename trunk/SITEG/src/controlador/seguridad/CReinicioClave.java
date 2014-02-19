package controlador.seguridad;

import java.util.ArrayList;

import modelo.seguridad.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/*
 * Controlador que permite reiniciar la constraseña de acceso al sistema a
 * cierto usuario
 */
@Controller
public class CReinicioClave extends CGeneral {

	private static final long serialVersionUID = 1708954473285437786L;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	ArrayList<Boolean> valorCorreo = new ArrayList<Boolean>();
	private String mensaje = "Su solicitud ha sido exitosamente procesada, le enviamos su usuario y contraseña,";
	@Wire
	private Window wdwReinicioClave;
	@Wire
	private Textbox txtNombreUsuario;
	@Wire
	private Textbox txtCorreoUsuario;
	@Wire
	private Label lblNombreUsuario;
	@Wire
	private Checkbox cbxUsuario;
	@Wire
	private Checkbox cbxClave;

	/*
	 * Metodo heredado del Controlador CGeneral que inicializa los componentes
	 * de la vista
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
	}

	/*
	 * Metodo que genera una clave alfanumerica de manera aleatoria y sustituye
	 * la contraseña actual del usuario por dicha clave, ademas se envia dicha
	 * clave al correo del usuario
	 */
	@Listen("onClick = #btnEnviarCorreo")
	public void reiniciarClave() {
		String password = KeyGenerators.string().generateKey();
		if (txtNombreUsuario.getText().compareTo("") != 0
				&& txtCorreoUsuario.getText().compareTo("") != 0) {
			String nombreUsuario = txtNombreUsuario.getValue();
			String correoUsuario = txtCorreoUsuario.getValue();
			Usuario usuario = servicioUsuario
					.buscarUsuarioPorNombre(txtNombreUsuario.getValue());
			usuario.setPassword(passwordEncoder.encode(password));
			servicioUsuario.guardar(usuario);
			valorCorreo.add(enviarEmailNotificacion(correoUsuario, mensaje
					+ " Usuario: " + nombreUsuario + "  " + "Contraseña: "
					+ password));
			confirmacion(valorCorreo);
			wdwReinicioClave.onClose();
		}
	}

	/*
	 * Metodo que permite confirmar que se logro enviar efectivamente el correo
	 * electronico
	 */
	private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for (int w = 0; w < valor2.size(); w++) {
			if (valor2.get(w).equals(false)) {
				return Messagebox.show("Correo electronico no enviado",
						"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
		return Messagebox.show("Correo electronico enviado", "Informacion",
				Messagebox.OK, Messagebox.INFORMATION);
	}

	/*
	 * Metodo que permite limpiar los campos de la vista
	 */
	@Listen("onClick = #btnCancelarCorreo")
	public void cancelarSolicitud() {
		txtNombreUsuario.setValue("");
		txtCorreoUsuario.setValue("");
	}
	
	/*
	 * Metodo que permite cerrar la vista de reiniciar contraseña
	 */
	@Listen("onClick = #btnSalirReninicioClave")
	public void salirReinicioClave() {
		
		wdwReinicioClave.onClose();
	
	}
	
	
	

}
