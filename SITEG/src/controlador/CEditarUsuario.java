package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import modelo.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import servicio.SUsuario;

import configuracion.GeneradorBeans;

@Controller
public class CEditarUsuario extends CGeneral {

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	URL url = getClass().getResource("/configuracion/usuario.png");
	
	@Wire
	private Label lblNombreUsuarioEditar;
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
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(auth.getName());
		Usuario usuario = servicioUsuario
				.buscarUsuarioPorNombre(auth.getName());
		id = usuario.getId();
		lblNombreUsuarioEditar.setValue(usuario.getNombre());
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

	@Listen("onClick = #btnGuardarUsuarioEditar")
	public void editarUsuario(Event event) throws IOException {

		if(txtClaveUsuarioNueva.getValue().equals(txtClaveUsuarioConfirmar.getValue())){
			
		Usuario usuarioAuxiliar = servicioUsuario.buscarUsuarioPorId(id);
		String nombre = lblNombreUsuarioEditar.getValue();
		Boolean estatus = true;
		String password = "";
		byte[] imagenUsuario = null;
		if (mediaUsuarioEditar instanceof org.zkoss.image.Image) {
			imagenUsuario = imagenUsuarioEditar.getContent().getByteData();

		} else {
			imagenUsuarioEditar.setContent(new AImage(url));
			imagenUsuario = imagenUsuarioEditar.getContent().getByteData();
		}

		if (txtClaveUsuarioConfirmar.getText().compareTo("") != 0) {
			password = txtClaveUsuarioConfirmar.getValue();
			usuarioAuxiliar.setPassword(passwordEncoder.encode(password));

		}
		usuarioAuxiliar.setImagen(imagenUsuario);
		usuarioAuxiliar.setEstatus(true);

		servicioUsuario.guardar(usuarioAuxiliar);
		Messagebox.show("Datos del usuario modificados con èxito", "Informado",
				Messagebox.OK, Messagebox.INFORMATION);
		
		txtClaveUsuarioConfirmar.setValue("");
		txtClaveUsuarioNueva.setValue("");
		imagenUsuarioEditar.setContent(new AImage(url));
		
	}
	
	else {
		Messagebox.show("No coincide las claves", "Error",
				Messagebox.OK, Messagebox.ERROR);
		
		
	}
}

}
