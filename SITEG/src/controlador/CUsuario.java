package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import modelo.AreaInvestigacion;
import modelo.Grupo;
import modelo.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SGrupo;
import servicio.SUsuario;

import configuracion.GeneradorBeans;

@Controller
public class CUsuario extends CGeneral {

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	// Declaracion de la clase que permite encriptar las contraseñas
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private long id = 0;
	@Wire
	private Textbox txtNombreUsuario;
	@Wire
	private Textbox txtPasswordUsuario;
	@Wire
	private Listbox ltbUsuario;
	@Wire
	private Window wdwCatalogoUsuario;
	@Wire
	private Textbox txtNombreMostrarUsuario;
	@Wire
	private Textbox txtPasswordMostrarUsuario;
	@Wire
	private Listbox listaGrupos;
	@Wire
	private Listbox listaGruposAgregados;
	@Wire
	private Button pasar1;
	@Wire
	private Button pasar2;
	@Wire
	private Button btnEliminarUsuario;
	@Wire
	private Image imagen;
	@Wire
	private Fileupload fudImagenUsuario;
	@Wire
	private Media media;
	
	void inicializar(Component comp) {

		List<Usuario> usuarios = servicioUsuario.buscarActivos();
		List<Grupo> grupos = servicioGrupo.buscarActivos();

		if (txtNombreUsuario == null) 
			ltbUsuario.setModel(new ListModelList<Usuario>(usuarios));
		else
			listaGrupos.setModel(new ListModelList<Grupo>(grupos));
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Usuario usuario = servicioUsuario.buscarUsuarioPorId(codigo);
				llenarGrupos(usuario);
				txtNombreUsuario.setValue(usuario.getNombre());
				txtPasswordUsuario.setValue(usuario.getPassword());
				BufferedImage imag;
				try {
					imag = ImageIO.read(new ByteArrayInputStream(usuario
							.getImagen()));
					imagen.setContent(imag);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				btnEliminarUsuario.setDisabled(false);
				id = usuario.getId();
				map.clear();
				map = null;
			}
		}

	}

	@Listen("onClick = #btnGuardarUsuario")
	public void guardarEstudiante() throws IOException {

		String nombre = txtNombreUsuario.getValue();
		String password = txtPasswordUsuario.getValue();
		Boolean estatus = true;
		Set<Grupo> gruposUsuario = new HashSet<Grupo>();
		byte[] imagenUsuario = null;
		if (media instanceof org.zkoss.image.Image){
			imagenUsuario = imagen.getContent().getByteData();
			
		}
		else
		{
			URL url = getClass().getResource("/configuracion/usuario.png");
			imagen.setContent(new AImage(url));
			imagenUsuario = imagen.getContent().getByteData();	
		}
		for (int i = 0; i < listaGruposAgregados.getItemCount(); i++) {
			Grupo grupo = listaGruposAgregados.getItems().get(i).getValue();
			gruposUsuario.add(grupo);
		}
		Usuario usu = new Usuario(id, nombre, passwordEncoder.encode(password),
				estatus, gruposUsuario, imagenUsuario);
		servicioUsuario.guardar(usu);
		cancelarItem();
		id = 0;
	}

	@Listen("onClick = #btnCancelarUsuario")
	public void cancelarItem() {
		txtNombreUsuario.setValue("");
		txtPasswordUsuario.setValue("");
		id = 0;
	}

	@Listen("onClick = #btnEliminarUsuario")
	public void eliminarLapso() {

		Usuario usuario = servicioUsuario.buscarUsuarioPorId(id);
		usuario.setEstatus(false);
		servicioUsuario.guardar(usuario);
		cancelarItem();
		System.out.println("User Eliminado");
	}

	@Listen("onClick = #btnCatalogoUsuario")
	public void buscarItem() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoUsuario.zul", null, null);
		window.doModal();

	}

	@Listen("onDoubleClick = #ltbUsuario")
	public void mostrarDatosCatalogo() {
		Listitem listItem = ltbUsuario.getSelectedItem();
		Usuario itemDatosCatalogo = (Usuario) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", itemDatosCatalogo.getId());
		String vista = "maestros/VCrearUsuario";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoUsuario.onClose();

	}

	@Listen("onChange = #txtNombreMostrarUsuario,#txtPasswordMostrarUsuario")
	public void filtrarDatosCatalogo() {
		List<Usuario> usuario = servicioUsuario.buscarActivos();
		List<Usuario> usuario2 = new ArrayList<Usuario>();

		for (Usuario usuario1 : usuario) {
			if (usuario1.getNombre().toLowerCase()
					.contains(txtNombreMostrarUsuario.getValue().toLowerCase())) {
				usuario2.add(usuario1);
			}

		}

		ltbUsuario.setModel(new ListModelList<Usuario>(usuario2));

	}
	
	@Listen("onClick = #pasar1")
	public void moverDerecha() {
		Listitem list1 = listaGrupos.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(listaGruposAgregados);
	}
	
	@Listen("onClick = #pasar2")
	public void moverIzquierda() {
		Listitem list2 = listaGruposAgregados.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(listaGrupos);
	}
	
	@Listen("onUpload = #fudImagenUsuario")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

	public void llenarGrupos(Usuario usuario){
		List<Grupo> gruposActuales = servicioGrupo.buscarGruposDelUsuario(usuario);
		listaGruposAgregados.setModel(new ListModelList<Grupo>(
				gruposActuales));
		List<Long> ids = new ArrayList<Long>();
		for(int i=0; i<gruposActuales.size();i++){
			long id = gruposActuales.get(i).getId();
			ids.add(id);
		}
		if(ids.toString()!="[]"){
			List<Grupo> gruposDisponibles = servicioGrupo.buscarGruposDisponibles(ids);
			listaGrupos.setModel(new ListModelList<Grupo>(
					gruposDisponibles));
		}
	}
}
