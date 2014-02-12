package controlador.seguridad;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import servicio.seguridad.SUsuario;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CEditarUsuario extends CGeneral {

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	URL url = getClass().getResource("/configuracion/usuario.png");
	
	@Wire
	private Textbox txtNombreUsuarioEditar;
	@Wire
	private Textbox txtClaveUsuarioNueva;
	@Wire
	private Textbox txtClaveUsuarioConfirmar;
	@Wire
	private Image imagenUsuarioEditar;
	@Wire
	private Fileupload fudImagenUsuarioEditar;
	@Wire
	private Media mediaUsuarioEditar;
	private long id = 0;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(auth.getName());
		Usuario usuario = servicioUsuario
				.buscarUsuarioPorNombre(auth.getName());
		id = usuario.getId();
		txtNombreUsuarioEditar.setValue(usuario.getNombre());
		try {
			BufferedImage imag;
			imag = ImageIO.read(new ByteArrayInputStream(usuario.getImagen()));
			imagenUsuarioEditar.setContent(imag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Listen("onUpload = #fudImagenUsuarioEditar")
	public void processMedia(UploadEvent event) {
		mediaUsuarioEditar = event.getMedia();
		imagenUsuarioEditar
				.setContent((org.zkoss.image.Image) mediaUsuarioEditar);

	}

	@Listen("onClick = #btnGuardarEditarUsuario")
	public void editarUsuario(Event event) throws IOException {

		if(txtClaveUsuarioNueva.getValue().equals(txtClaveUsuarioConfirmar.getValue())){
			Messagebox.show("�Desea guardar los cambios?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {	
		Usuario usuarioAuxiliar = servicioUsuario.buscarUsuarioPorId(id);
		String nombre = txtNombreUsuarioEditar.getValue();
		Boolean estatus = true;
		String password = "";
		byte[] imagenUsuario = null;
		if (mediaUsuarioEditar instanceof org.zkoss.image.Image) {
			imagenUsuario = imagenUsuarioEditar.getContent().getByteData();

		} else {
			try {
				imagenUsuarioEditar.setContent(new AImage(url));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imagenUsuario = imagenUsuarioEditar.getContent().getByteData();
		}

		if (txtClaveUsuarioConfirmar.getText().compareTo("") != 0) {
			password = txtClaveUsuarioConfirmar.getValue();
			usuarioAuxiliar.setPassword(passwordEncoder.encode(password));

		}
		usuarioAuxiliar.setImagen(imagenUsuario);
		usuarioAuxiliar.setEstatus(true);

		servicioUsuario.guardar(usuarioAuxiliar);
		Messagebox.show("Datos del usuario modificados con exito", "Informacion",
				Messagebox.OK, Messagebox.INFORMATION);
		
		txtClaveUsuarioConfirmar.setValue("");
		txtClaveUsuarioNueva.setValue("");
		try {
			imagenUsuarioEditar.setContent(new AImage(url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
							}
						}
					});
	}
	
	else {
		Messagebox.show("No coinciden las claves", "Error",
				Messagebox.OK, Messagebox.ERROR);
	}
}
	
	@Listen("onClick = #btnCancelarEditarUsuario")
	public void cancelarUsuario() throws IOException {
		txtClaveUsuarioNueva.setValue("");
		txtClaveUsuarioConfirmar.setValue("");
		imagenUsuarioEditar.setContent(new AImage(url));

	}

}
