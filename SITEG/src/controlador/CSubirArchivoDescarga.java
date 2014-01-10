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
import modelo.Descarga;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;

import servicio.SActividad;
import servicio.SArchivo;
import servicio.SDescarga;
import configuracion.GeneradorBeans;



@Controller
public class CSubirArchivoDescarga extends CGeneral {
	
	
	SDescarga servicioDescarga = GeneradorBeans.getServicioDescarga();


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
	private long id = 0;
	private FileInputStream archivo;
	private int longitudByte;
	private Descarga descarga = new Descarga();
	private String nombreDoc;
	private static long idAux;
	
	/* Metodo para inicializar componentes al momento que se ejecuta las vistas
	  tanto VDescarga como VCatalogoDescarga*/

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
		
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Descarga descarga  = servicioDescarga.buscarArchivo(codigo);
				txtNombreArchivoDescarga.setValue(descarga.getNombre());
				txtDescripcionArchivoDescarga.setValue(descarga.getDescripcion());
				id = descarga.getId();
				idAux=id;
				btnEliminarArchivoDescarga.setDisabled(false);
				map.clear();
				map = null;
			}
		}
		
	}
	
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
			
				descarga.setNombre(media.getName());
				descarga.setContenidoDocumento(media.getByteData());
				descarga.setTipoArchivo(media.getContentType());
				nombreDoc= descarga.getNombre();
							
				txtNombreArchivoDescarga.setValue(descarga.getNombre());
				
				
				
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
		descarga.equals(null);
	
	
	}
	
	@Listen("onClick = #btnGuardarArchivoDescarga")
	public void guardarDescarga() {
		if (descarga == null || txtNombreArchivoDescarga.getText().compareTo("")==0 || txtDescripcionArchivoDescarga.getText().compareTo("")==0) {
			Messagebox.show("Debe completar los campos", "Error", Messagebox.OK, Messagebox.ERROR);
		} 
		else {
			Messagebox.show("Desea guardar el archivo?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								
								Profesor profesor = ObtenerUsuarioProfesor();		
								Programa programa = new Programa();			
								programa = profesor.getPrograma();		
								descarga.setId(id);
								descarga.setPrograma(programa);
								descarga.setDescripcion(txtDescripcionArchivoDescarga.getValue());
								descarga.setEstatus(true);
								servicioDescarga.guardar(descarga);
								cancelar();
								Messagebox.show("Archivo guardado exitosamente", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
								
							}
						}
					});	
	
		}
}
	
	@Listen("onClick = #btnEliminarArchivoDescarga") 
	public void eliminarArchivo (){
		Messagebox.show("Desea eliminar archivo?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Descarga descarga = servicioDescarga.buscarArchivo(idAux);
							descarga.setEstatus(false);
							servicioDescarga.guardar(descarga);
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
	
	
	@Listen("onClick = #btnCatalogoDescarga") 
		public void verCatalogo(){
		CCatalogoArchivoDescarga cata = new controlador.CCatalogoArchivoDescarga();
		cata.metodoApagar();
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArchivoDescarga.zul", null, null);
		window.doModal();
		CCatalogoItem catalogo = new CCatalogoItem();
		//catalogo.recibir("maestros/VItem");
	}
	
	
	
	

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