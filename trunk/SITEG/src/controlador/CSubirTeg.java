package controlador;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFileChooser;

import modelo.Archivo;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
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
import servicio.STeg;

import configuracion.GeneradorBeans;



@Controller
public class CSubirTeg extends CGeneral {
	
	
	SArchivo servicioArchivo = GeneradorBeans.getServicioArchivo();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();

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
	private long id = 0;
	private FileInputStream archivo;
	private int longitudByte;
	private Archivo archi = new Archivo();
	private String nombreDoc;
	/* Metodo para inicializar componentes al momento que se ejecuta las vistas
	  tanto VDescarga como VCatalogoDescarga*/

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
		Estudiante estudiante = ObtenerUsuarioEstudiante();
		if(estudiante!= null){
		Teg tegEstudiante = servicioTeg.buscarTegEstudiantePorEstatusAprobado(estudiante);
		if(tegEstudiante == null){
			wdwSubirTeg.onClose();
			alert("no puede");
		}
		}
		List<Programa> programa = servicioPrograma.buscarActivas();
		if (cmbPrograma != null) {
			cmbPrograma.setModel(new ListModelList<Programa>(programa));

		}
		
	}
	//Metodo para subir archivo
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
				archi.setTipoDocumento(media.getContentType());
				nombreDoc= archi.getNombre();
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
		cmbPrograma.setValue("");
		archi.equals(null);
		
	
	
	}
	///Metodo para guardar los archivos
	@Listen("onClick = #btnGuardarArchivo")
	public void guardarDescarga() {
		if (archi == null || txtNombreArchivo.getText().compareTo("")==0 || txtDescripcionArchivo.getText().compareTo("")==0) {
			Messagebox.show("Debe completar los campos", "Error", Messagebox.OK, Messagebox.ERROR);
		} 
		else {
			Messagebox.show("�Desea guardar los datos del archivo?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								
								String idPrograma = cmbPrograma.getSelectedItem().getId();
								long idPrograma1 = Long.parseLong(idPrograma);
								Programa programa = servicioPrograma.buscarPorId(idPrograma1);	
								archi.setId(id);
								archi.setPrograma(programa);
								archi.setDescripcion(txtDescripcionArchivo.getValue());
								archi.setEstatus(true);
								archi.setTipoArchivo("Teg");
								servicioArchivo.guardar(archi);
								cancelar();
								Messagebox.show("Archivo guardado exitosamente", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
								
							}
						}
					});

		}
}
	
//Metodos que permiten obtener el archivo
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