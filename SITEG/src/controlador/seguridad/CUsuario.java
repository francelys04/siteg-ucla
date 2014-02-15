 package controlador.seguridad;

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

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

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
import servicio.SProfesor;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;

import configuracion.GeneradorBeans;
import controlador.CGeneral;
import controlador.catalogo.CCatalogoEstudiante;
import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoUsuario;

@Controller
public class CUsuario extends CGeneral {

	CCatalogoUsuario catalogoUsuario = new CCatalogoUsuario();
	CCatalogoEstudiante catalogoEstudiante = new CCatalogoEstudiante();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();

	// Declaracion de la clase que permite encriptar las contraseñas
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private long id = 0;

	private String profesorEstudiante;
	private String cedulaProfesorEstudiante;
	URL url = getClass().getResource("/configuracion/usuario.png");
	
	
	
	
	List<Usuario> usuarios = servicioUsuario.buscarActivos();
	//List<Grupo> grupos = servicioGrupo.buscarActivos();
	List<Grupo> gruposDisponibles=servicioGrupo.buscarActivos();
	List<Grupo> gruposSeleccionados=new ArrayList();
	
	ArrayList<Boolean> valorCorreo = new ArrayList<Boolean>();
	
	private String mensaje = "Su solicitud ha sido exitosamente procesada, le enviamos su usuario y contraseña,";
	
	
	
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
	private Button btnCatalogoProfesorEstudiante;
	@Wire
	private Button btnCatalogoUsuario;
	
	@Wire
	private Radiogroup rdgProfesorEstudianteOtro;
	@Wire
	private Radio rdoProfesor;
	@Wire
	private Radio rdoEstudiante;
	@Wire
	private Radio rdoOtro;
	@Wire
	private Textbox txtCorreo;
	@Wire
	private Label lblCorreo;

	public void inicializar(Component comp) {

//		if (txtNombreUsuario != null)
		ltbGruposDisponibles.setModel(new ListModelList<Grupo>(gruposDisponibles));

		txtNombreUsuario.setDisabled(true);
		txtPasswordUsuario.setDisabled(true);
		btnCatalogoUsuario.setVisible(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);
		try {
			imagen.setContent(new AImage(url));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		
		 if (map != null) {
			 
			 if (map.get("id") != null) {
		 
		 
		 long codigo = (Long) map.get("id");
		 Usuario usuario = servicioUsuario.buscarUsuarioPorId(codigo);
		 txtNombreUsuario.setValue(usuario.getNombre());
		 txtPasswordUsuario.setValue(usuario.getPassword());
		 txtNombreUsuario.setDisabled(true);
		 txtPasswordUsuario.setDisabled(true);
		  
		 llenarGrupos(usuario); 
		 btnEliminarUsuario.setDisabled(false);
		  btnCatalogoProfesorEstudiante.setVisible(false);
		  rdoProfesor.setDisabled(true);
		  rdoEstudiante.setDisabled(true);
		  BufferedImage imag; try { imag = ImageIO.read(new
		  ByteArrayInputStream(usuario .getImagen())); imagen.setContent(imag);
		  } catch (IOException e) { // TODO Auto-generated catch block
		  e.printStackTrace(); }
		  
		  id = usuario.getId();
		  
		  } 
		  
		  if (map.get("profesorEstudiante") != null) {
		  profesorEstudiante = (String) map.get("profesorEstudiante"); 
		  if (profesorEstudiante.equals("Estudiante")) {
		  rdoEstudiante.setSelected(true);
		  } 
		  if (profesorEstudiante.equals("Profesor")) {
		  rdoProfesor.setSelected(true); }
		  
		  if (profesorEstudiante.equals("Otro")) {
			  rdoOtro.setSelected(true); }
		  
		  } 
		  if (map.get("nombreUsuario") != null) { 
			  String nombreUsuario =(String) map.get("nombreUsuario");
			  txtNombreUsuario.setValue(nombreUsuario); }
		  if (map.get("passwordUsuario") != null) {
		  	  String passwordUsuario =(String) map.get("passwordUsuario");
		  	  txtPasswordUsuario.setValue(passwordUsuario); } 
		  if (map.get("cedula") != null) {
			  String cedulaProfesorEstudiante=(String) map.get("cedula");
			
			  Estudiante estudiante=servicioEstudiante.buscarEstudiante(cedulaProfesorEstudiante);
			  Profesor profesor=servicioProfesor.buscarProfesorPorCedula(cedulaProfesorEstudiante);
			  
			  if(estudiante!=null){
				  txtCorreo.setValue(estudiante.getCorreoElectronico());
				  rdoProfesor.setDisabled(true);
				  rdoOtro.setDisabled(true);
	    	  }
			  if(profesor!=null){
				  txtCorreo.setValue(profesor.getCorreoElectronico());
				  rdoEstudiante.setDisabled(true);
				  rdoOtro.setDisabled(true);
		      }
			  txtCorreo.setDisabled(true);
    	      
			  
			  txtNombreUsuario.setValue(cedulaProfesorEstudiante);
			  txtNombreUsuario.setDisabled(true);
			  txtPasswordUsuario.setValue(cedulaProfesorEstudiante);
    		  txtPasswordUsuario.setDisabled(true);
    		  btnCatalogoUsuario.setVisible(false);
    		} 
		  map.clear();
		  map = null;
		 } 
		 txtCorreo.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un texto como: ejemplo@ejemplo.com");
	}

	@Listen("onClick = #rdoOtro,#rdoEstudiante,#rdoProfesor")
	public void seleccionarOtro() {

		if (rdoOtro.isSelected()) {
			txtNombreUsuario.setDisabled(false);
			txtPasswordUsuario.setDisabled(false);
			btnCatalogoUsuario.setVisible(true);
			btnCatalogoProfesorEstudiante.setVisible(false);
		} else {
			txtNombreUsuario.setValue("");
			txtPasswordUsuario.setValue("");
			txtCorreo.setValue("");
			txtNombreUsuario.setDisabled(true);
			txtPasswordUsuario.setDisabled(true);
			btnCatalogoUsuario.setVisible(false);
			btnCatalogoProfesorEstudiante.setVisible(true);

		}

	}

	@Listen("onClick = #btnGuardarUsuario")
	public void guardarUsuario() throws IOException {

		String nombre = txtNombreUsuario.getValue();
		String password = txtPasswordUsuario.getValue();
		String correoUsuario = txtCorreo.getValue();
		
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

		if (id == 0 && usuario == null) {

			Usuario usuario1 = new Usuario(id, nombre,
					passwordEncoder.encode(password), estatus, gruposUsuario,
					imagenUsuario);
			servicioUsuario.guardar(usuario1);

			Usuario usuario2= servicioUsuario.buscarUsuarioPorNombre(nombre);
			
			cedulaProfesorEstudiante = nombre;

			if (rdgProfesorEstudianteOtro.getSelectedItem().getLabel()
					.equals("Estudiante")) {

				Estudiante estudiante = servicioEstudiante
						.buscarEstudiante(cedulaProfesorEstudiante);
				estudiante.setUsuario(usuario2);
				servicioEstudiante.guardar(estudiante);

			}
			if (rdgProfesorEstudianteOtro.getSelectedItem().getLabel()
					.equals("Profesor")) {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(cedulaProfesorEstudiante);
				profesor.setUsuario(usuario2);
				servicioProfesor.guardarProfesor(profesor);
			}
			valorCorreo.add(enviarEmailNotificacion(correoUsuario, mensaje+" Usuario: "+nombre+"  "+"Contraseña: "+nombre));
			//confirmacion(valorCorreo);
			
			Messagebox.show("Usuario registrado exitosamente", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
			cancelarUsuario();
		}
		if (id != 0) {

			usuario.setNombre(nombre);
			usuario.setGrupos(gruposUsuario);
			usuario.setImagen(imagenUsuario);

			servicioUsuario.guardar(usuario);
			
			cancelarUsuario();
			
		}
		if (id == 0 && usuario != null) {

			Messagebox.show("El nombre de usuario no se encuentra disponible",
					"Informacion", Messagebox.OK, Messagebox.INFORMATION);

			txtPasswordUsuario.setValue("");
			txtNombreUsuario.setValue("");
			txtCorreo.setValue("");
			ltbGruposAgregados.getItems().clear();
			imagen.setContent(new AImage(url));
			ltbGruposDisponibles.setModel(new ListModelList<Grupo>(gruposDisponibles));
		}
	}

	@Listen("onClick = #btnCancelarUsuario")
	public void cancelarUsuario() throws IOException {
		id = 0;
		txtNombreUsuario.setValue("");
		txtPasswordUsuario.setValue("");
		rdgProfesorEstudianteOtro.setSelectedItem(null);
		ltbGruposDisponibles.setModel(new ListModelList<Grupo>(gruposDisponibles));
		ltbGruposAgregados.getItems().clear();
		imagen.setContent(new AImage(url));
		rdoEstudiante.setDisabled(false);
		rdoProfesor.setDisabled(false);
		txtNombreUsuario.setDisabled(false);
		txtPasswordUsuario.setDisabled(false);
		btnEliminarUsuario.setDisabled(true);
		btnCatalogoUsuario.setVisible(false);
		txtCorreo.setConstraint("");
		txtCorreo.setValue("");
		txtCorreo.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un texto como: ejemplo@ejemplo.com");
		txtCorreo.setDisabled(false);
	    btnCatalogoProfesorEstudiante.setVisible(true);
		rdoProfesor.setDisabled(false);
		rdoEstudiante.setDisabled(false);
		rdoOtro.setDisabled(false);
		ltbGruposAgregados.setMultiple(false);
		ltbGruposAgregados.setCheckmark(false);
		ltbGruposAgregados.setMultiple(true);
		ltbGruposAgregados.setCheckmark(true);

		ltbGruposDisponibles.setMultiple(false);
		ltbGruposDisponibles.setCheckmark(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);
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
								cancelarUsuario();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

	}

	/*private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for(int w=0; w<valor2.size();w++){
			if(valor2.get(w).equals(false)){
				//return Messagebox.show("Correo electronico no enviado", "Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
		//return Messagebox.show("Correo electronico enviado","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
	}*/
	
	@Listen("onClick = #btnCatalogoUsuario")
	public void buscarUsuario() {

		if(rdoOtro.isChecked()){
		
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoUsuario.zul", null, null);
		window.doModal();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map2 != null) {
			map = map2;
		}
		map.put("profesorEstudiante", rdgProfesorEstudianteOtro
				.getSelectedItem().getLabel());

		Sessions.getCurrent().setAttribute("itemsCatalogo", map);

		catalogoUsuario.recibir("maestros/VCrearUsuario");
		}
	}

	@Listen("onClick = #btnCatalogoProfesorEstudiante")
	public void buscarProfesorEstudiante() {

		if (rdgProfesorEstudianteOtro.getSelectedItem() != null) {
			String profesorEstudiante = rdgProfesorEstudianteOtro.getSelectedItem()
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

			map.put("profesorEstudiante", rdgProfesorEstudianteOtro
					.getSelectedItem().getLabel());

			map.put("usuario", true);

			Sessions.getCurrent().setAttribute("itemsCatalogo", map);

			Window window = (Window) Executions
					.createComponents("/vistas/catalogos/VCatalogo"
							+ profesorEstudiante + ".zul", null, null);
			window.doModal();

			if (rdgProfesorEstudianteOtro.getSelectedItem().getLabel()
					.equals("Estudiante")) {

				catalogoEstudiante.recibir("maestros/VCrearUsuario");
			} else {
				catalogoProfesor.recibir("maestros/VCrearUsuario");
			}

		}
	}

	@Listen("onClick = #pasar1")
	public void moverDerecha() {
		List<Integer> itemEliminar = new ArrayList();
		List<Listitem> listitemEliminar = new ArrayList();
		List<Listitem> listItem = ltbGruposDisponibles.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {

				if (listItem.get(i).isSelected()) {

					Grupo grupo = listItem.get(i).getValue();
					gruposDisponibles.remove(grupo);
					gruposSeleccionados.add(grupo);
					ltbGruposAgregados
							.setModel(new ListModelList<Grupo>(gruposSeleccionados));
					listitemEliminar.add(listItem.get(i));
				}

			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbGruposDisponibles.removeItemAt(listitemEliminar.get(i)
					.getIndex());
		}

		ltbGruposAgregados.setMultiple(false);
		ltbGruposAgregados.setCheckmark(false);
		ltbGruposAgregados.setMultiple(true);
		ltbGruposAgregados.setCheckmark(true);

System.out.println("tamaño izuquierda:"+gruposDisponibles.size());
	}

	@Listen("onClick = #pasar2")
	public void moverIzquierda() {
		
		System.out.println("tamaño:"+gruposDisponibles.size());
		
		List<Integer> itemEliminar = new ArrayList();
		List<Listitem> listitemEliminar = new ArrayList();

		List<Listitem> listItem2 = ltbGruposAgregados.getItems();
		if (listItem2.size() != 0) {
			for (int i = 0; i < listItem2.size(); i++) {

				if (listItem2.get(i).isSelected()) {

					Grupo grupo = listItem2.get(i).getValue();
					gruposSeleccionados.remove(grupo);

					
					gruposDisponibles.add(grupo);
								
					ltbGruposDisponibles
							.setModel(new ListModelList<Grupo>(gruposDisponibles));
				
					listitemEliminar.add(listItem2.get(i));
				}

			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbGruposAgregados.removeItemAt(listitemEliminar.get(i)
					.getIndex());
		}
		ltbGruposDisponibles.setMultiple(false);
		ltbGruposDisponibles.setCheckmark(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);


	}

	@Listen("onUpload = #fudImagenUsuario")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

	public void llenarGrupos(Usuario usuario) {
		gruposSeleccionados = servicioGrupo
				.buscarGruposDelUsuario(usuario);
		ltbGruposAgregados.setModel(new ListModelList<Grupo>(gruposSeleccionados));
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < gruposSeleccionados.size(); i++) {
			long id = gruposSeleccionados.get(i).getId();
			ids.add(id);
		}
		if (ids.toString() != "[]") {
			gruposDisponibles = servicioGrupo
					.buscarGruposDisponibles(ids);
			ltbGruposDisponibles.setModel(new ListModelList<Grupo>(
					gruposDisponibles));
		}
		ltbGruposAgregados.setMultiple(false);
		ltbGruposAgregados.setCheckmark(false);
		ltbGruposAgregados.setMultiple(true);
		ltbGruposAgregados.setCheckmark(true);

		ltbGruposDisponibles.setMultiple(false);
		ltbGruposDisponibles.setCheckmark(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);
	}

	
}
