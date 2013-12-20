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
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;

import servicio.SUsuario;

import configuracion.GeneradorBeans;

@Controller
public class CEditarUsuario extends CGeneral {

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Wire
	private Textbox txtNombreUsuarioEditar;
	@Wire
	private Textbox txtContraseniaUsuarioEditar;
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
		Usuario usuario = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		id = usuario.getId();
		txtContraseniaUsuarioEditar.setValue(usuario.getPassword());
		txtNombreUsuarioEditar.setValue(usuario.getNombre());
		try {
			BufferedImage imag;
			imag = ImageIO.read(new ByteArrayInputStream(usuario
					.getImagen()));
			imagenUsuarioEditar.setContent(imag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Listen("onUpload = #fudImagenUsuarioEditar")
	public void processMedia(UploadEvent event) {
		mediaUsuarioEditar = event.getMedia();
		imagenUsuarioEditar.setContent((org.zkoss.image.Image) mediaUsuarioEditar);

	}
	
	@Listen("onClick = #btnGuardarUsuarioEditar")
	public void editarUsuario() throws IOException{
		Usuario usuarioAuxiliar = servicioUsuario.buscarUsuarioPorId(id);
		String nombre = txtNombreUsuarioEditar.getValue();
		String password = txtContraseniaUsuarioEditar.getValue();
		Boolean estatus = true;
		byte[] imagenUsuario = null;
		if (mediaUsuarioEditar instanceof org.zkoss.image.Image){
			imagenUsuario = imagenUsuarioEditar.getContent().getByteData();
			
		}
		else
		{
			URL url = getClass().getResource("/configuracion/usuario.png");
			imagenUsuarioEditar.setContent(new AImage(url));
			imagenUsuario = imagenUsuarioEditar.getContent().getByteData();	
		}
		Usuario usuarioEditado = new Usuario(id, nombre, passwordEncoder.encode(password),
				estatus, usuarioAuxiliar.getGrupos(), imagenUsuario);
		servicioUsuario.guardar(usuarioEditado);
	}
}

