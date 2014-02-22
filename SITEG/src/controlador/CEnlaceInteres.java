package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

import modelo.Actividad;
import modelo.EnlaceInteres;
import modelo.Noticia;
import modelo.Profesor;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SNoticia;
import servicio.SEnlaceInteres;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;
import controlador.catalogo.CCatalogoEnlaceInteres;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad EnlaceInteres*/
@Controller
public class CEnlaceInteres extends CGeneral {

	private static final long serialVersionUID = -6297699990954496428L;
	CCatalogoEnlaceInteres catalogo = new CCatalogoEnlaceInteres();
	@Wire
	private Textbox txtNombreEnlace;
	@Wire
	private Textbox txtUrlEnlace;
	@Wire
	private Textbox txtNombreMostrarEnlace;
	@Wire
	private Textbox txtUrlMostrarEnlace;
	@Wire
	private Listbox ltbEnlace;
	@Wire
	private Window wdwCatalogoEnlace;
	@Wire
	private Image imagen;
	@Wire
	private Fileupload fudImagenEnlace;
	@Wire
	private Media media;
	@Wire
	private Window wdwEnlace;
	private static boolean enlaceCatalogo;

	private long id = 0;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		enlaceCatalogo = false;
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		/*
		 * Validacion para vaciar la informacion del catalogo a la vista
		 * VEnlacesInteres.zul si la varible map tiene algun dato contenido
		 */

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (long) map.get("id");
				enlaceCatalogo = true;
				EnlaceInteres enlaceInteres = servicioEnlace
						.buscarEnlace(codigo);
				txtNombreEnlace.setValue(enlaceInteres.getNombre());
				txtUrlEnlace.setValue(enlaceInteres.getUrl());
				BufferedImage imag;
				try {
					imag = ImageIO.read(new ByteArrayInputStream(enlaceInteres
							.getImagen()));
					imagen.setContent(imag);
					id = enlaceInteres.getId();
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
	@Listen("onClick = #btnCatalogoEnlace")
	public void buscarDescarga() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoEnlaceInteres.zul", null, null);
		catalogo.recibir("portal-web/VEnlaceInteres");
		window.doModal();
	}

	/*
	 * Metodo que permite cerrar la vista de registrar el enlace de interes
	 */
	@Listen("onClick = #btnSalirEnlace")
	public void salirDescarga() {

		wdwEnlace.onClose();
	}

	/*
	 * Metodo que permite el guardado o modificacion de una entidad
	 * EnlaceInteres
	 */
	@Listen("onClick = #btnGuardarEnlace")
	public void guardarEnlace() {

		List<EnlaceInteres> enlaceInteres = servicioEnlace.buscarActivos();
		if (enlaceInteres.size() == 3 && enlaceCatalogo == false) {
			Messagebox
					.show("Ya existen tres enlaces de interes, si desea agregar uno nuevo debe modificar uno ya existente",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
		} else {

			try {
				if ((txtNombreEnlace.getText().compareTo("") == 0)
						|| (txtUrlEnlace.getText().compareTo("") == 0)
						|| imagen.getContent().getByteData() == null) {

					Messagebox.show("Debe completar todos los campos", "Error",
							Messagebox.OK, Messagebox.ERROR);
				} else {
					Messagebox.show(
							"¿Desea guardar los datos del enlace de interes?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {

										String nombre = txtNombreEnlace
												.getValue();
										String url = txtUrlEnlace.getValue();
										Boolean estatus = true;
										byte[] image = imagen.getContent()
												.getByteData();
										Profesor profesor = ObtenerUsuarioProfesor();
										Usuario usuario = servicioUsuario
												.buscarUsuarioPorId(profesor
														.getUsuario().getId());
										EnlaceInteres enlace = new EnlaceInteres(
												id, nombre, url, estatus,
												image, usuario);
										servicioEnlace.guardar(enlace);
										cancelarEnlace();
										Messagebox
												.show("Enlace de interes resgistrado satisfactoriamente",
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

	/* Metodo que permite la eliminacion logica de una entidad EnlaceInteres */
	/**
	 * @Listen("onClick = #btnEliminarEnlace") public void eliminarEnlace() {
	 * 
	 *                  Messagebox.show(
	 *                  "¿Desea eliminar los datos del enlace de interes?",
	 *                  "Dialogo de confirmacion", Messagebox.OK |
	 *                  Messagebox.CANCEL, Messagebox.QUESTION, new
	 *                  org.zkoss.zk.ui.event.EventListener<Event>() { public
	 *                  void onEvent(Event evt) throws InterruptedException { if
	 *                  (evt.getName().equals("onOK")) { EnlaceInteres enlace =
	 *                  servicioEnlace .buscarEnlace(id);
	 *                  enlace.setEstatus(false);
	 *                  servicioEnlace.guardar(enlace); cancelarEnlace();
	 *                  Messagebox.show(
	 *                  "Enlace de interes eliminado exitosamente",
	 *                  "Informacion", Messagebox.OK, Messagebox.INFORMATION);
	 * 
	 *                  } } }); }
	 */

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarEnlace")
	public void cancelarEnlace() {
		id = 0;
		txtNombreEnlace.setValue("");
		txtUrlEnlace.setValue("");
		imagen.setSrc(null);

	}

	/*
	 * Metodo que permite subir una imagen al formulario
	 */
	@Listen("onUpload = #fudImagenEnlace")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

}