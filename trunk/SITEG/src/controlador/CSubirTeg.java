package controlador;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/** Controlador que permite subir un TEG una vez que ha sido aprobado */
@Controller
public class CSubirTeg extends CGeneral {

	private static final long serialVersionUID = -7446118760957677763L;
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
	private Window wdwSubirTeg;
	public static Programa programaUsuario;

	/**
	 * Metodo heredado del Controlador CGeneral donde se llenan los objetos
	 * empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		programaUsuarioLoggeado();

	}

	/**
	 * Metodo que permite obtener el programa del usuario loggeado y validar que
	 * un estudiante para subir un teg, este debe tener el estatus TEG Aprobado
	 */
	public void programaUsuarioLoggeado() {

		programaUsuario = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());

		if (programaUsuario == null) {

			Estudiante estudiante = ObtenerUsuarioEstudiante();
			if (estudiante != null) {
				programaUsuario = estudiante.getPrograma();
				Teg tegEstudiante = servicioTeg
						.buscarTegEstudiantePorEstatusAprobado(estudiante);

				if (tegEstudiante == null) {
					Messagebox.show(
							"Para subir el TEG, el mismo debe estar aprobado",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
					wdwSubirTeg.onClose();

				}
			} else {
				Messagebox
						.show("No tiene permisos para subir un Trabajo Especial de Grado",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
				wdwSubirTeg.onClose();
			}

		}

	}

	/**
	 * Metodo que permite validar que el documento posee una extension adecuada
	 * asi como tambien permite subirlo a la vista
	 * 
	 * @param event
	 *            archivo subido al sistema por el usuario
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

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarArchivo")
	public void cancelar() {

		txtNombreArchivo.setValue("");
		txtDescripcionArchivo.setValue("");
		archivo.equals(null);

	}

	/**
	 * Metodo que permite cerrar la vista subir Teg
	 */
	@Listen("onClick = #btnSalirSubirTeg")
	public void salirSubirTeg() {

		wdwSubirTeg.onClose();

	}

	/** Metodo que permite guardar el archivo del TEG */
	@Listen("onClick = #btnGuardarArchivo")
	public void guardarDescarga() {
		if (archivo == null || txtNombreArchivo.getText().compareTo("") == 0
				|| txtDescripcionArchivo.getText().compareTo("") == 0) {
			Messagebox.show("Debe completar los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
				
				String nombreInterfaz = txtNombreArchivo.getText();
				Archivo nuevoArchivo = servicioArchivo.buscarNombreArchivo(nombreInterfaz);
				String archivoEncontrado = nuevoArchivo.getNombre();
					if (archivoEncontrado.equals(nombreInterfaz)){
						Messagebox.show("El archivo ya existe", "Error",
						Messagebox.OK, Messagebox.ERROR);
					} else {
				Messagebox.show("¿Desea guardar los datos del archivo?",
						"Dialogo de confirmacion", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
	
									archivo.setId(id);
									archivo.setPrograma(programaUsuario);
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
}