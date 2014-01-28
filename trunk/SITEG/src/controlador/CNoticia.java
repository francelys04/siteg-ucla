package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import modelo.Noticia;
import modelo.Profesor;
import modelo.Usuario;

import org.springframework.stereotype.Controller;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SNoticia;
import servicio.SUsuario;
import configuracion.GeneradorBeans;

@Controller
public class CNoticia extends CGeneral {

	SNoticia servicioNoticia = GeneradorBeans.getServicioNoticia();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	@Wire
	private Textbox txtNombreNoticia;
	@Wire
	private Textbox txtDescripcionNoticia;
	@Wire
	private Textbox txtNombreMostrarNoticia;
	@Wire
	private Textbox txtDescripcionMostrarNoticia;
	@Wire
	private Listbox ltbNoticia;
	@Wire
	private Window wdwCatalogoNoticia;
	@Wire
	private Image imagen;
	@Wire
	private Fileupload fudImagenNoticia;
	@Wire
	private Media media;
	@Wire
	private Button btnEliminarNoticia;
	
	
	private long id = 0;
	 
	
	/* Metodo para inicializar componentes al momento que se ejecuta las vistas
	  tanto VDescarga como VCatalogoNoticia*/
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		/*Listado de todos las Noticias que se encuentran activas, cuyo
		 * estatus=true con el servicioNoticia mediante el metodo buscarActivos
		 */

	
		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VNoticia
		 */
	
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("noticiaCatalogo");
		/*
		 * Validacion para vaciar la informacion del VNoticia a la vista
		 * VNoticiaa.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if ( map.get("id") != null) {

				long codigo = (long) map.get("id");
				Noticia noticia2 = servicioNoticia.buscarNoticia(codigo);
				txtNombreNoticia.setValue( noticia2.getNombre());
				txtDescripcionNoticia.setValue( noticia2.getDescripcion());
				btnEliminarNoticia.setDisabled(false);
				BufferedImage imag;
				try {
					imag = ImageIO.read(new ByteArrayInputStream( noticia2.getImagen()));
					imagen.setContent(imag);
					id =  noticia2.getId();
					map.clear();
					map = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}

	//Aca se muestra el catalogo de las Noticias Registradas
	@Listen("onClick = #btnCatalogoNoticia")
	public void buscarDescarga() {

	Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoNoticia.zul", null, null);
		window.doModal();
	}
	
	//Aca se guardan las Noticias
	@Listen("onClick = #btnGuardarNoticia")
	public void guardarDescarga() {
		List <Noticia> noticia = servicioNoticia.buscarActivos();
		if(noticia.size() == 3){
			Messagebox.show("Ya existen tres noticias debe eliminar una","Informacion", Messagebox.OK,Messagebox.INFORMATION);
		}else{
			Messagebox.show("¿Desea guardar la noticia?",
					"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onOK")) {
		String nombre = txtNombreNoticia.getValue();
		String descripcion = txtDescripcionNoticia.getValue();
		Boolean estatus = true;
		byte[] image = imagen.getContent().getByteData();
		Profesor profesor = ObtenerUsuarioProfesor();
		Usuario usuario = profesor.getUsuario();
		Noticia noticia1 = new Noticia(id,nombre,descripcion, estatus,image,usuario);
		servicioNoticia.guardar(noticia1);
		cancelarNoticia();
		Messagebox.show("Noticia resgistrada satisfactoriamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
							}
						}
					});
		}
	}

	//Aca se eliminan logicamente las Noticias
	@Listen("onClick = #btnEliminarNoticia")
	public void eliminarNoticia() {	
		Messagebox.show("¿Desea eliminar la noticia?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
		Noticia noticia = servicioNoticia.buscarNoticia(id);
		noticia.setEstatus(false);
		servicioNoticia.guardar(noticia);
		cancelarNoticia();
		Messagebox.show("Noticia eliminada satisfactoriamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
						}
					}
				});
	}

	//Aca se mandan a limpiar los campos de textos de la vista
	@Listen("onClick = #btnCancelarNoticia")
	public void cancelarNoticia() {
		id = 0;
		txtNombreNoticia.setValue("");
		txtDescripcionNoticia.setValue("");
		imagen.setSrc(null);
	
	}

	@Listen("onUpload = #fudImagenNoticia")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

	
}