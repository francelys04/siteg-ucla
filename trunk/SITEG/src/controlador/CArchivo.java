package controlador;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.swing.JFileChooser;

import modelo.Archivo;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;

import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.UploadEvent;

import servicio.SActividad;
import servicio.SArchivo;
import configuracion.GeneradorBeans;



@Controller
public class CArchivo extends CGeneral {
	
	
	SArchivo servicioArchivo = GeneradorBeans.getServicioArchivo();


	@Wire
	private Textbox txtNombreArchivo;
	@Wire
	private Textbox txtDescripcionArchivo;	
	@Wire
	private Button btnArchivo;
	@Wire
	private Media media;
	private long id = 0;
	private FileInputStream archivo;
	private int longitudByte;
	private Archivo archi = new Archivo();
	private String nombreDoc;
	
	/* Metodo para inicializar componentes al momento que se ejecuta las vistas
	  tanto VDescarga como VCatalogoDescarga*/

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
	}
	
	@Listen("onUpload = #btnArchivo")
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
			
				archi.setNombre(media.getName());
				archi.setContenidoDocumento(media.getByteData());
				archi.setTipoArchivo(media.getContentType());
				nombreDoc= archi.getNombre();
				System.out.println(nombreDoc);
				System.out.println(media.getName());
				System.out.println(archi.getNombre());
				
				txtNombreArchivo.setValue(archi.getNombre());
				
				
				
			} else {
				Messagebox.show(media.getName()+" No es un tipo de archivo valido!", "Error", Messagebox.OK, Messagebox.ERROR);
			}
		} 
	}



	//Aca se mandan a limpiar los campos de textos de la vista
	@Listen("onClick = #btnCancelarArchivo")
	public void cancelar() {
	
		txtNombreArchivo.setValue("");
		txtDescripcionArchivo.setValue("");
		archi.equals(null);
	
	
	}
	
	@Listen("onClick = #btnGuardarArchivo")
	public void guardarDescarga() {
		if (archi == null || txtNombreArchivo.getText().compareTo("")==0 || txtDescripcionArchivo.getText().compareTo("")==0) {
			Messagebox.show("Debe completar los campos", "Error", Messagebox.OK, Messagebox.ERROR);
		} 
		else {
		Estudiante estudiante = ObtenerUsuarioEstudiante();		
		Programa programa = new Programa();			
		programa = estudiante.getPrograma();		
		archi.setId(id);
		archi.setPrograma(programa);
		archi.setDescripcion(txtDescripcionArchivo.getValue());
		archi.setEstatus(true);
		servicioArchivo.guardar(archi);
		Messagebox.show("Archivo guardado exitosamente", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
		}
}
	
	
	
	
//METODO LOCOOOOO DEL MODELO
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