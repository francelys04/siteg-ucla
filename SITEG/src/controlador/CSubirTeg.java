package controlador;

import java.util.List;

import modelo.Archivo;
import modelo.Estudiante;
import modelo.Programa;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/* Controlador que permite subir un TEG una vez que ha sido aprobado */
@Controller
public class CSubirTeg extends CGeneral {

	private long id = 0;
	private Archivo archivo = new Archivo();
	private String nombreDoc;
	@Wire
	private Textbox txtNombreArchivo;
	@Wire
	private Textbox txtDescripcionArchivo;
	@Wire
	private Button btnArchivo;
	@Wire
	private Media media;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Window wdwSubirTeg;
	/*
	 * Metodo heredado del Controlador CGeneral donde se llenan los objetos
	 * empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Programa> programa = servicioPrograma.buscarActivas();
		if (cmbPrograma != null) {
			cmbPrograma.setModel(new ListModelList<Programa>(programa));

		}

	}

	/*
	 * Metodo que permite validar que el documento posee una extension adecuada
	 * asi como tambien permite subirlo a la vista
	 */
	@Listen("onUpload = #btnArchivo")
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
				txtNombreArchivo.setValue(archivo.getNombre());

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
	@Listen("onClick = #btnCancelarArchivo")
	public void cancelar() {

		txtNombreArchivo.setValue("");
		txtDescripcionArchivo.setValue("");
		cmbPrograma.setValue("");
		archivo.equals(null);

	}

	/* Metodo que permite guardar el archivo del TEG */
	@Listen("onClick = #btnGuardarArchivo")
	public void guardarDescarga() {
		if (archivo == null || txtNombreArchivo.getText().compareTo("") == 0
				|| txtDescripcionArchivo.getText().compareTo("") == 0) {
			Messagebox.show("Debe completar los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show("�Desea guardar los datos del archivo?",
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
								archivo.setDescripcion(txtDescripcionArchivo
										.getValue());
								archivo.setEstatus(true);
								archivo.setTipoArchivo("Teg");
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
}