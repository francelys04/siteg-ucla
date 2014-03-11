package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.Noticia;
import modelo.seguridad.Usuario;

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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Noticia*/
@Controller
public class CNoticia extends CGeneral {

	private static final long serialVersionUID = -502940297770512719L;
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
	private Window wdwNoticia;
	@Wire
	private Button btnGuardarNoticia;

	private long id = 0;
	private static boolean noticiaCatalogo;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		noticiaCatalogo = false;
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (long) map.get("id");
				noticiaCatalogo = true;
				Noticia noticia2 = servicioNoticia.buscarNoticia(codigo);
				txtNombreNoticia.setValue(noticia2.getNombre());
				txtDescripcionNoticia.setValue(noticia2.getDescripcion());
				BufferedImage imag;
				try {
					imag = ImageIO.read(new ByteArrayInputStream(noticia2
							.getImagen()));
					imagen.setContent(imag);
					id = noticia2.getId();
					map.clear();
					map = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoNoticia")
	public void buscarDescarga() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoNoticia.zul", null, null);
		window.doModal();
	}

	/* Metodo que permite el guardado o modificacion de una entidad Noticia */
	@Listen("onClick = #btnGuardarNoticia")
	public void guardarDescarga() {
		List<Noticia> noticia = servicioNoticia.buscarActivos();
		if (noticia.size() == 3 && noticiaCatalogo == false) {
			Messagebox
					.show("Ya existen tres noticias, si desea agregar una nueva debe modificar una ya existente",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
		} else {

			try {
				if ((txtNombreNoticia.getText().compareTo("") == 0)
						|| (txtDescripcionNoticia.getText().compareTo("") == 0)
						|| imagen.getContent().getByteData() == null) {
					Messagebox.show("Debe completar todos los campos", "Error",
							Messagebox.OK, Messagebox.ERROR);
				} else {

					Messagebox.show("¿Desea guardar los datos de la noticia?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {
										String nombre = txtNombreNoticia
												.getValue();
										String descripcion = txtDescripcionNoticia
												.getValue();
										Boolean estatus = true;
										byte[] image = imagen.getContent()
												.getByteData();
										Usuario usuario = ObtenerUsuario();
										Noticia noticia1 = new Noticia(id,
												nombre, descripcion, estatus,
												image, usuario);
										servicioNoticia.guardar(noticia1);
										noticiaCatalogo = false;
										cancelarNoticia();
										Messagebox
												.show("Noticia resgistrada satisfactoriamente",
														"Informacion",
														Messagebox.OK,
														Messagebox.INFORMATION);
									}
								}
							});
				}
			} catch (Exception e) {

				Messagebox.show("Debe completar todos los campos", "Error",
						Messagebox.OK, Messagebox.ERROR);

			}
		}

	}

	/* Metodo que permite la eliminacion logica de una entidad Noticia */
	/**
	 * @Listen("onClick = #btnEliminarNoticia") public void eliminarNoticia() {
	 *                  Messagebox
	 *                  .show("¿Desea eliminar los datos de la noticia?",
	 *                  "Dialogo de confirmacion", Messagebox.OK |
	 *                  Messagebox.CANCEL, Messagebox.QUESTION, new
	 *                  org.zkoss.zk.ui.event.EventListener<Event>() { public
	 *                  void onEvent(Event evt) throws InterruptedException { if
	 *                  (evt.getName().equals("onOK")) { Noticia noticia =
	 *                  servicioNoticia.buscarNoticia(id);
	 *                  noticia.setEstatus(false);
	 *                  servicioNoticia.guardar(noticia); cancelarNoticia();
	 *                  Messagebox.show( "Noticia eliminada satisfactoriamente",
	 *                  "Informacion", Messagebox.OK, Messagebox.INFORMATION); }
	 *                  } }); }
	 */

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarNoticia")
	public void cancelarNoticia() {
		id = 0;
		txtNombreNoticia.setValue("");
		txtDescripcionNoticia.setValue("");
		imagen.setSrc(null);
		noticiaCatalogo = false;

	}

	/*
	 * Metodo que permite subir una imagen al formulario
	 */
	@Listen("onUpload = #fudImagenNoticia")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

	/* Metodo que permite cerrar la ventana correspondiente a las noticias */
	@Listen("onClick = #btnSalirNoticia")
	public void salirNoticia() {

		wdwNoticia.onClose();

	}

}