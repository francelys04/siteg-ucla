package controlador;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import modelo.Actividad;
import modelo.Archivo;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import servicio.SActividad;
import servicio.SArchivo;
import servicio.SPrograma;
import configuracion.GeneradorBeans;



@Controller
public class CSubirArchivoDescarga extends CGeneral {
	
	
	SArchivo servicioArchivo = GeneradorBeans.getServicioArchivo();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();

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

	private long id = 0;
	private int longitudByte;
	private Archivo archivo = new Archivo();
	private String nombreDoc;
	private static long idAux;
	
	/* Metodo para inicializar componentes al momento que se ejecuta las vistas
	  tanto VDescarga como VCatalogoDescarga*/

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programa = servicioPrograma.buscarActivas();
		if (cmbPrograma != null) {
			cmbPrograma.setModel(new ListModelList<Programa>(programa));

		}
		//permite obtener los datos desde el catalogo si el map es diferente de null
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Archivo archivo  = servicioArchivo.buscarArchivo(codigo);
				cmbPrograma.setValue(archivo.getPrograma().getNombre());
				txtNombreArchivoDescarga.setValue(archivo.getNombre());
				txtDescripcionArchivoDescarga.setValue(archivo.getDescripcion());
				id = archivo.getId();
				btnArchivoDescarga.setDisabled(true);
				idAux=id;
				btnEliminarArchivoDescarga.setDisabled(false);
				map.clear();
				map = null;
			}
		}
		
	}
	//permite subir el archivo
	@Listen("onUpload = #btnArchivoDescarga")
	public void subirArchivo(UploadEvent event){
	
		media = event.getMedia();
		if (media != null) {
			
			if (media.getContentType().equals("image/jpeg") ||
				media.getContentType().equals("application/pdf") ||
				media.getContentType().equals("application/msword") ||
				media.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
				media.getContentType().equals("application/vnd.oasis.opendocument.text") ||
				media.getContentType().equals("application/x-vnd.oasis.opendocument.text"))
				
			{
			
				archivo.setNombre(media.getName());
				archivo.setContenidoDocumento(media.getByteData());
				archivo.setTipoDocumento(media.getContentType());
				
				nombreDoc= archivo.getNombre();
							
				txtNombreArchivoDescarga.setValue(archivo.getNombre());
				
				
				
			} else {
				Messagebox.show(media.getName()+" No es un tipo de archivo valido!", "Error", Messagebox.OK, Messagebox.ERROR);
			}
		} 
	}



	//Aca se mandan a limpiar los campos de textos de la vista
	@Listen("onClick = #btnCancelarArchivoDescarga")
	public void cancelar() {
	
		txtNombreArchivoDescarga.setValue("");
		txtDescripcionArchivoDescarga.setValue("");
		archivo.equals(null);
		cmbPrograma.setValue("");
		btnEliminarArchivoDescarga.setDisabled(true);
		btnArchivoDescarga.setDisabled(false);
	
	
	}
	//permite guardar un archivo en la base de datos
	@Listen("onClick = #btnGuardarArchivoDescarga")
	public void guardarDescarga() {
		if (archivo == null || txtNombreArchivoDescarga.getText().compareTo("")==0 || txtDescripcionArchivoDescarga.getText().compareTo("")==0 || cmbPrograma.getValue() == "") {
			Messagebox.show("Debe completar los campos", "Error", Messagebox.OK, Messagebox.ERROR);
		} 
		else {
			Messagebox.show("¿Desea guardar los datos del archivo?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								
								String idPrograma = cmbPrograma.getSelectedItem().getId();
								long idPrograma1 = Long.parseLong(idPrograma);
								Programa programa = servicioPrograma.buscarPorId(idPrograma1);		
								archivo.setId(id);
								archivo.setPrograma(programa);
								archivo.setDescripcion(txtDescripcionArchivoDescarga.getValue());
								archivo.setEstatus(true);
								archivo.setTipoArchivo("Descarga");
								servicioArchivo.guardar(archivo);
								cancelar();
								Messagebox.show("Archivo guardado exitosamente", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
								
							}
						}
					});	
	
		}
}
	//Permite eliminar un archivo
	@Listen("onClick = #btnEliminarArchivoDescarga") 
	public void eliminarArchivo (){
		Messagebox.show("¿Desea eliminar los datos del archivo?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Archivo archivo = servicioArchivo.buscarArchivo(idAux);
							archivo.setEstatus(false);
							servicioArchivo.guardar(archivo);
							cancelar();
							btnEliminarArchivoDescarga.setDisabled(true);
							Messagebox.show(
									"Archivo eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
							
						}
					}
				});
			}
	
	//permite ver los la lista de archivos registrados
	@Listen("onClick = #btnCatalogoDescarga") 
		public void verCatalogo(){
		CCatalogoArchivoDescarga cata = new controlador.CCatalogoArchivoDescarga();
		cata.metodoApagar();
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArchivoDescarga.zul", null, null);
		window.doModal();
		CCatalogoItem catalogo = new CCatalogoItem();
	}
	
	@Listen("onClick = #btnSalirArchivoDescarga") 
	public void salirArchivoDescarga(){
	
		wdwSubirArchivoDescarga.onClose();
		
		
}
	
	
	
//metodos para obtener el archivo

public static byte[] toByteArray(InputStream is) throws SQLException,
	IOException {

ByteArrayOutputStream baos = new ByteArrayOutputStream();

byte[] buf = new byte[4000];
try {
	for (;;) {
		int dataSize = is.read(buf);
		if (dataSize == -1) {
			break;
		}
		baos.write(buf, 0, dataSize);
	}
} finally {
	if (is != null) {
		try {
			is.close();
		} catch (IOException ex) {
		}
	}
}
return baos.toByteArray();
}

public static byte[] toByteArray(File archivo) throws IOException {

FileInputStream fileInput = new FileInputStream(archivo);

ByteArrayOutputStream baos = new ByteArrayOutputStream();

byte[] buf = new byte[4000];
InputStream is = fileInput;
try {
	for (;;) {
		int dataSize = is.read(buf);
		if (dataSize == -1) {
			break;
		}
		baos.write(buf, 0, dataSize);
	}
} finally {
	if (is != null) {
		try {
			is.close();
		} catch (IOException ex) {
		}
	}
}

return baos.toByteArray();
}
	
	
}