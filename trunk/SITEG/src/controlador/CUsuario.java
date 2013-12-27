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
import modelo.Estudiante;
import modelo.Grupo;
import modelo.Profesor;
import modelo.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SGrupo;
import servicio.SProfesor;
import servicio.SUsuario;

import configuracion.GeneradorBeans;

@Controller
public class CUsuario extends CGeneral {


	CCatalogoUsuario catalogoUsuario = new CCatalogoUsuario();
	CCatalogoEstudiante catalogoEstudiante = new CCatalogoEstudiante();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();

	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();

	// Declaracion de la clase que permite encriptar las contraseñas
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private long id = 0;

	private String profesorEstudiante;
	private String cedulaProfesorEstudiante;
	URL url = getClass().getResource("/configuracion/usuario.png");
	List<Usuario> usuarios = servicioUsuario.buscarActivos();
	List<Grupo> grupos = servicioGrupo.buscarActivos();

	@Wire
	private Textbox txtNombreUsuario;
	@Wire
	private Textbox txtPasswordUsuario;
	@Wire
	private Listbox ltbGruposDisponibles;
	@Wire
	private Listbox ltbGruposAgregados;
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
	@Wire
	private Button btnCatalgoProfesorEstudiante;
	@Wire
	private Radiogroup rdgProfesorEstudiante;
	@Wire
	private Label lblCedulaProfesorEstudiante;
	@Wire
	private Radio rdoProfesor;
	@Wire
	private Radio rdoEstudiante;

	void inicializar(Component comp) {

		if (txtNombreUsuario != null)
			ltbGruposDisponibles.setModel(new ListModelList<Grupo>(grupos));

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");

				Usuario usuario = servicioUsuario.buscarUsuarioPorId(codigo);
				System.out.println("Id:" + codigo);
				System.out.println("nombre:" + usuario.getNombre());

				txtNombreUsuario.setValue(usuario.getNombre());
				txtPasswordUsuario.setValue(usuario.getPassword());
				txtPasswordUsuario.setDisabled(true);

				llenarGrupos(usuario);
				Estudiante estudiante = servicioEstudiante
						.buscarEstudianteLoggeado(usuario);
				Profesor profesor = servicioProfesor
						.buscarProfesorLoggeado(usuario);
				if (estudiante != null) {
					lblCedulaProfesorEstudiante
							.setValue(estudiante.getCedula());
					rdoEstudiante.setSelected(true);
					rdoEstudiante.setDisabled(true);
					rdoProfesor.setDisabled(true);
				}
				if (profesor != null) {
					lblCedulaProfesorEstudiante.setValue(profesor.getCedula());
					rdoProfesor.setSelected(true);
					rdoEstudiante.setDisabled(true);
					rdoProfesor.setDisabled(true);

				}

				btnEliminarUsuario.setDisabled(false);

				BufferedImage imag;
				try {
					imag = ImageIO.read(new ByteArrayInputStream(usuario
							.getImagen()));
					imagen.setContent(imag);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				id = usuario.getId();

			}
			if (map.get("cedula") != null) {

				cedulaProfesorEstudiante = (String) map.get("cedula");
				lblCedulaProfesorEstudiante.setValue(cedulaProfesorEstudiante);

			}
			if (map.get("profesorEstudiante") != null) {

				profesorEstudiante = (String) map.get("profesorEstudiante");
				if (profesorEstudiante.equals("Estudiante")) {
					rdoEstudiante.setSelected(true);
				} else {
					rdoProfesor.setSelected(true);
				}

			}
			if (map.get("nombreUsuario") != null) {
				String nombreUsuario = (String) map.get("nombreUsuario");
				txtNombreUsuario.setValue(nombreUsuario);
			}
			if (map.get("passwordUsuario") != null) {
				String passwordUsuario = (String) map.get("passwordUsuario");
				txtPasswordUsuario.setValue(passwordUsuario);
			}
			/*
			 * if (map.get("imagen") != null) {
			 * 
			 * org.zkoss.image.Image image = (org.zkoss.image.Image) map
			 * .get("imagen"); imagen.setContent(image); }
			 */
		}

	}

	@Listen("onClick = #btnGuardarUsuario")
	public void guardarUsuario() throws IOException {

		if (txtNombreUsuario.getText().compareTo("") == 0
				|| txtPasswordUsuario.getText().compareTo("") == 0
				|| lblCedulaProfesorEstudiante.getValue().compareTo("") == 0
				|| (rdoEstudiante.isChecked() == false && rdoProfesor
						.isChecked() == false)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			String nombre = txtNombreUsuario.getValue();
			String password = txtPasswordUsuario.getValue();
			Boolean estatus = true;
			Set<Grupo> gruposUsuario = new HashSet<Grupo>();
			byte[] imagenUsuario = null;
			if (media instanceof org.zkoss.image.Image) {
				imagenUsuario = imagen.getContent().getByteData();

			} else {

				imagen.setContent(new AImage(url));
				imagenUsuario = imagen.getContent().getByteData();
			}
			for (int i = 0; i < ltbGruposAgregados.getItemCount(); i++) {
				Grupo grupo = ltbGruposAgregados.getItems().get(i).getValue();
				gruposUsuario.add(grupo);
			}
			Usuario usuario = servicioUsuario.buscarUsuarioPorNombre(nombre);
			if (id != 0 && usuario != null) {

				usuario.setGrupos(gruposUsuario);
				usuario.setImagen(imagenUsuario);
			
				
				Estudiante estudiante = servicioEstudiante
						.buscarEstudianteLoggeado(usuario);
				
				Profesor profesor = servicioProfesor
						.buscarProfesorLoggeado(usuario);
				
				if(estudiante==null && profesor==null){
					if (rdgProfesorEstudiante.getSelectedItem().getLabel()
							.equals("Estudiante")) {

						
						Estudiante estudiante2 = servicioEstudiante
								.buscarEstudiante(cedulaProfesorEstudiante);
						estudiante2.setUsuario(usuario);
						servicioEstudiante.guardar(estudiante2);
						
					} else {
						Profesor profesor2= servicioProfesor
							.buscarProfesorPorCedula(cedulaProfesorEstudiante);
						profesor2.setUsuario(usuario);
						servicioProfesor.guardarProfesor(profesor2);
					}
				}
				servicioUsuario.guardar(usuario);
				
				
				

				Messagebox.show("Usuario registrado exitosamente",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);
				cancelarItem();

				id = 0;

			}
			else				
			if (id == 0 && usuario != null) {

				
				Messagebox.show(
						"El nombre de usuario no se encuentra disponible",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);

				txtPasswordUsuario.setValue("");
				txtNombreUsuario.setValue("");

			}
			else
			if (id == 0 && usuario == null) {
			
								
				Usuario usuario2 = new Usuario(id, nombre,
						passwordEncoder.encode(password), estatus,
						gruposUsuario, imagenUsuario);
				System.out.println("Imagen:"+imagenUsuario);
				servicioUsuario.guardar(usuario2);

				Usuario usuario1 = servicioUsuario
						.buscarUsuarioPorNombre(nombre);

				if (rdgProfesorEstudiante.getSelectedItem().getLabel()
						.equals("Estudiante")) {

					Estudiante estudiante = servicioEstudiante
							.buscarEstudiante(cedulaProfesorEstudiante);
					estudiante.setUsuario(usuario1);
					servicioEstudiante.guardar(estudiante);

				} else {
					Profesor profesor = servicioProfesor
							.buscarProfesorPorCedula(cedulaProfesorEstudiante);
					profesor.setUsuario(usuario1);
					servicioProfesor.guardarProfesor(profesor);
				}
				Messagebox.show("Usuario registrado exitosamente",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);
				cancelarItem();
				id = 0;
			}
						

			}}

	@Listen("onClick = #btnCancelarUsuario")
	public void cancelarItem() throws IOException {
		txtNombreUsuario.setValue("");
		txtPasswordUsuario.setValue("");
		rdgProfesorEstudiante.setSelectedItem(null);
		lblCedulaProfesorEstudiante
				.setValue("No ha seleccionado al Profesor o Estudiante");
		id = 0;
		ltbGruposDisponibles.setModel(new ListModelList<Grupo>(grupos));
		ltbGruposAgregados.getItems().clear();
		imagen.setContent(new AImage(url));
		rdoEstudiante.setDisabled(false);
		rdoProfesor.setDisabled(false);
		txtPasswordUsuario.setDisabled(false);
		btnEliminarUsuario.setDisabled(true);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			map.clear();
			map = null;
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);

		}
		System.out.println("Map:" + map);

	}

	@Listen("onClick = #btnEliminarUsuario")
	public void eliminarUsuario() throws IOException {

		Messagebox.show("Desea eliminar el usuario?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Usuario usuario = servicioUsuario
									.buscarUsuarioPorId(id);
							usuario.setEstatus(false);
							servicioUsuario.guardar(usuario);

							Messagebox.show("Usuario eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
							try {
								cancelarItem();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

	}

	@Listen("onClick = #btnCatalogoUsuario")
	public void buscarItem() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoUsuario.zul", null, null);
		window.doModal();
		catalogoUsuario.recibir("maestros/VCrearUsuario");

	}

	@Listen("onClick = #btnCatalogoProfesorEstudiante")
	public void buscarItemProfesorEstudiante() {

		if (rdgProfesorEstudiante.getSelectedItem() != null) {
			String profesorEstudiante = rdgProfesorEstudiante.getSelectedItem()
					.getLabel();

			HashMap<String, Object> map = new HashMap<String, Object>();
			HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
					.getCurrent().getAttribute("itemsCatalogo");

			if (map2 != null) {
				map = map2;
			}
			if (txtNombreUsuario.getText().compareTo("") != 0)
				map.put("nombreUsuario", txtNombreUsuario.getValue());

			if (txtPasswordUsuario.getText().compareTo("") != 0)
				map.put("passwordUsuario", txtPasswordUsuario.getValue());

			map.put("profesorEstudiante", rdgProfesorEstudiante
					.getSelectedItem().getLabel());
			// map.put("imagen", imagen.getContent().getByteData());

			map.put("usuario", true);

			Sessions.getCurrent().setAttribute("itemsCatalogo", map);

			Window window = (Window) Executions
					.createComponents("/vistas/catalogos/VCatalogo"
							+ profesorEstudiante + ".zul", null, null);
			window.doModal();

			if (rdgProfesorEstudiante.getSelectedItem().getLabel()
					.equals("Estudiante")) {

				catalogoEstudiante.recibir("maestros/VCrearUsuario");
			} else {
				catalogoProfesor.recibir("maestros/VCrearUsuario");
			}

		}
	}

	@Listen("onClick = #pasar1")
	public void moverDerecha() {
		Listitem list1 = ltbGruposDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbGruposAgregados);

	}

	@Listen("onClick = #pasar2")
	public void moverIzquierda() {
		Listitem list2 = ltbGruposAgregados.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbGruposDisponibles);
	}

	@Listen("onUpload = #fudImagenUsuario")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

	public void llenarGrupos(Usuario usuario) {
		List<Grupo> gruposActuales = servicioGrupo
				.buscarGruposDelUsuario(usuario);
		ltbGruposAgregados.setModel(new ListModelList<Grupo>(gruposActuales));
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < gruposActuales.size(); i++) {
			long id = gruposActuales.get(i).getId();
			ids.add(id);
		}
		if (ids.toString() != "[]") {
			List<Grupo> gruposDisponibles = servicioGrupo
					.buscarGruposDisponibles(ids);
			ltbGruposDisponibles.setModel(new ListModelList<Grupo>(
					gruposDisponibles));
		}
	}

	/*
	 * @Listen("onChange = #txtNombreUsuario,#txtPasswordUsuario") public void
	 * guardarDatosTemporal() {
	 * 
	 * HashMap<String, Object> map = new HashMap<String, Object>();
	 * HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
	 * .getCurrent().getAttribute("itemsCatalogo");
	 * 
	 * if (map2 != null) { map = map2; } if (txtNombreUsuario.getValue() != "")
	 * { map.put("nombreUsuario", txtNombreUsuario.getValue());
	 * 
	 * /* Usuario usuario=
	 * servicioUsuario.buscarUsuarioPorNombre(txtNombreUsuario .getValue());
	 * if(usuario!=null){ Messagebox.show(
	 * "El Nombre de Usuario no se encuentra disponible", "Informacion",
	 * Messagebox.OK, Messagebox.ERROR);
	 * 
	 * txtPasswordUsuario.setValue(""); txtNombreUsuario.setValue(""); }
	 */

	/*
	 * System.out.println("Entro1"); } if
	 * (txtPasswordUsuario.getValue().compareTo("") != 0) {
	 * map.put("passwordUsuario", txtPasswordUsuario.getValue());
	 * System.out.println("Entro2"); }
	 * 
	 * Sessions.getCurrent().setAttribute("itemsCatalogo", map);
	 * 
	 * }
	 */
}
