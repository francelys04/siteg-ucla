package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Archivo;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SActividad;
import servicio.SArchivo;

import configuracion.GeneradorBeans;

@Controller
public class CCatalogoArchivo extends CGeneral {

	SArchivo servicioArchivo = GeneradorBeans.getServicioArchivo();

	@Wire
	private Listbox ltbArchivo;
	@Wire
	private Textbox txtNombreMostrarArchivo;
	@Wire
	private Textbox txtDescripcionMostrarArchivo;
	@Wire
	private Textbox txtProgramaMostrarArchivo;
	@Wire
	private Window wdwCatalogoArchivo;
	@Wire
	private Textbox txtNombreActividad;
	@Wire
	private Textbox txtDescripcionActividad;
	private long id = 0;
	@Wire
	private Window wdwActividad;
	


	private static String vistaRecibida;

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VActividad como VCatalogoActividad
	 * 
	 * @date 09-12-2013
	 */
	void inicializar(Component comp) {

		/*
		 * Listado de todos las actividades que se encuentran activos, cuyo
		 * estatus=true con el servicioActividad mediante el metodo
		 * buscarActivos
		 */
		List<Archivo> archivo = servicioArchivo.buscarActivos("Teg");
		ltbArchivo.setModel(new ListModelList<Archivo>(archivo));
			
		

		Selectors.wireComponents(comp, this, false);

	

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("archivoCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	// descripcion
	@Listen("onChange = #txtNombreMostrarArchivo,#txtDescripcionMostrarArchivo,#txtProgramaMostrarArchivo")
	public void filtrarDatosCatalogo() {
		List<Archivo> archivo1 = servicioArchivo.buscarActivos("teg");
		List<Archivo> archivo2 = new ArrayList<Archivo>();

		for (Archivo archivo : archivo1) {
			if (archivo
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarArchivo.getValue().toLowerCase())
					&& archivo
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArchivo.getValue()
											.toLowerCase())
						&& archivo
							.getPrograma().getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarArchivo.getValue()
											.toLowerCase()))
			{
				archivo2.add(archivo);
			}
		}

		ltbArchivo.setModel(new ListModelList<Archivo>(archivo2));

	}
	
	@Listen("onDoubleClick = #ltbArchivo")
	public void descargarArchivo(){
		if(ltbArchivo.getItemCount()!=0){
		Listitem listItem = ltbArchivo.getSelectedItem();
		Archivo archivo3= (Archivo) listItem.getValue();		
		Archivo archivo4 = servicioArchivo.buscarArchivo(archivo3.getId());
		Filedownload.save(archivo4.getContenidoDocumento(), archivo4.getTipoDocumento(), archivo4.getNombre());
		}
	}
	

}
