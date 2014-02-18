package controlador;

import java.util.HashMap;
import java.util.List;

import modelo.Archivo;
import modelo.Programa;

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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoArchivoDescarga;
import controlador.catalogo.CCatalogoItem;

/*
 * Controlador que permite subir un archivo para ser mostrado en la seccion
 * de descargas
 */
@Controller
public class CSubirArchivoDescarga extends CGeneral {

	private long id = 0;
	private Archivo archivo = new Archivo();
	private String nombreDoc;
	private static long idAux;
	@Wire
	private Textbox txtNombreArchivoDescarga;
	@Wire
	private Textbox txtDescripcionArchivoDescarga;
	@Wire
	private Button btnArchivoDescarga;
	@Wire
	private Button btnEliminarArchivoDescarga;
	@Wire
	private Media media;
	@Wire
	private Listbox ltbArchivoDescarga;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Window wdwSubirArchivoDescarga;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programa = servicioPrograma.buscarActivas();
		cmbPrograma.setModel(new ListModelList<Programa>(programa));
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Archivo archivo = servicioArchivo.buscarArchivo(codigo);
				cmbPrograma.setValue(archivo.getPrograma().getNombre());
				txtNombreArchivoDescarga.setValue(archivo.getNombre());
				txtDescripcionArchivoDescarga
						.setValue(archivo.getDescripcion());
				id = archivo.getId();
				btnArchivoDescarga.setDisabled(true);
				idAux = id;
				btnEliminarArchivoDescarga.setDisabled(false);
				map.clear();
				map = null;
			}
		}

	}

	/*
	 * Metodo que permite validar que el documento posee una extension adecuada
	 * asi como tambien permite subirlo a la vista
	 */
	@Listen("onUpload = #btnArchivoDescarga")
	public void subirArchivo(UploadEvent event) {

		media = event.getMedia();
		if (media != null) {

			if (media.getContentType().equals("image/jpeg")
					|| media.getContentType().equals("application/pdf")
					|| media.getContentType().equals("application/msword")
					|| media.getContentType()
							.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
					|| media.getContentType().equals(
							"application/vnd.oasis.opendocument.text")
					|| media.getContentType().equals(
							"application/x-vnd.oasis.opendocument.text"))

			{

				archivo.setNombre(media.getName());
				archivo.setContenidoDocumento(media.getByteData());
				archivo.setTipoDocumento(media.getContentType());

				nombreDoc = archivo.getNombre();

				txtNombreArchivoDescarga.setValue(archivo.getNombre());

			} else {
				Messagebox.show(media.getName()
						+ " No es un tipo de archivo valido!", "Error",
						Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado origianl
	 */
	@Listen("onClick = #btnCancelarArchivoDescarga")
	public void cancelar() {

		txtNombreArchivoDescarga.setValue("");
		txtDescripcionArchivoDescarga.setValue("");
		archivo.equals(null);
		cmbPrograma.setValue("");
		btnEliminarArchivoDescarga.setDisabled(true);
		btnArchivoDescarga.setDisabled(false);

	}

	/* Metodo que permite guardar un archivo para luego ser descargado */
	@Listen("onClick = #btnGuardarArchivoDescarga")
	public void guardarDescarga() {
		if (archivo == null
				|| txtNombreArchivoDescarga.getText().compareTo("") == 0
				|| txtDescripcionArchivoDescarga.getText().compareTo("") == 0
				|| cmbPrograma.getValue() == "") {
			Messagebox.show("Debe completar los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show("¿Desea guardar los datos del archivo?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								String idPrograma = cmbPrograma
										.getSelectedItem().getId();
								long idPrograma1 = Long.parseLong(idPrograma);
								Programa programa = servicioPrograma
										.buscarPorId(idPrograma1);
								archivo.setId(id);
								archivo.setPrograma(programa);
								archivo.setDescripcion(txtDescripcionArchivoDescarga
										.getValue());
								archivo.setEstatus(true);
								archivo.setTipoArchivo("Descarga");
								servicioArchivo.guardar(archivo);
								cancelar();
								Messagebox.show(
										"Archivo guardado exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);

							}
						}
					});

		}
	}

	/* Metodo que permite eliminar de manera logica un archivo */
	@Listen("onClick = #btnEliminarArchivoDescarga")
	public void eliminarArchivo() {
		Messagebox.show("¿Desea eliminar los datos del archivo?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Archivo archivo = servicioArchivo
									.buscarArchivo(idAux);
							archivo.setEstatus(false);
							servicioArchivo.guardar(archivo);
							cancelar();
							btnEliminarArchivoDescarga.setDisabled(true);
							Messagebox.show("Archivo eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);

						}
					}
				});
	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoDescarga")
	public void verCatalogo() {
		CCatalogoArchivoDescarga cata = new CCatalogoArchivoDescarga();
		cata.metodoApagar();
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArchivoDescarga.zul", null, null);
		window.doModal();
		CCatalogoItem catalogo = new CCatalogoItem();
	}

	/*
	 * Metodo que permite cerrar la ventana correspondiente a la subida de
	 * archivos
	 */
	@Listen("onClick = #btnSalirArchivoDescarga")
	public void salirArchivoDescarga() {

		wdwSubirArchivoDescarga.onClose();

	}

}