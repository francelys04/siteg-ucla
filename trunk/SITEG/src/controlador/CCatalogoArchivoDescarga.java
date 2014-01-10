package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Archivo;
import modelo.Descarga;
import modelo.Lapso;

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
import servicio.SDescarga;

import configuracion.GeneradorBeans;

@Controller
public class CCatalogoArchivoDescarga extends CGeneral {

	SDescarga servicioDescarga = GeneradorBeans.getServicioDescarga();

	@Wire
	private Listbox ltbArchivoDescarga;
	@Wire
	private Textbox txtNombreMostrarArchivoDescarga;
	@Wire
	private Textbox txtDescripcionMostrarArchivoDescarga;
	@Wire
	private Textbox txtProgramaMostrarArchivoDescarga;
	@Wire
	private Window wdwCatalogoArchivoDescarga;
	@Wire
	private Textbox txtNombreActividadDescarga;
	@Wire
	private Textbox txtDescripcionActividadDescarga;
	
	private long id = 0;
	private static boolean h=true;
	
    

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
		List<Descarga> descarga = servicioDescarga.buscarActivos();
		ltbArchivoDescarga.setModel(new ListModelList<Descarga>(descarga));
			
		

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
	public void metodoApagar(){
		h=false;
	}
	public void metodoPrender(){
		h=true;
	}

	// Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	// descripcion
	@Listen("onChange = #txtNombreMostrarArchivoDescarga,#txtDescripcionMostrarArchivoDescarga,#txtProgramaMostrarArchivoDescarga")
	public void filtrarDatosCatalogo() {
		List<Descarga> descarga1 = servicioDescarga.buscarActivos();
		List<Descarga> descarga2 = new ArrayList<Descarga>();

		for (Descarga descarga : descarga1) {
			if (descarga
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarArchivoDescarga.getValue().toLowerCase())
					&& descarga
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArchivoDescarga.getValue()
											.toLowerCase())
						&& descarga
							.getPrograma().getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarArchivoDescarga.getValue()
											.toLowerCase()))
			{
				descarga2.add(descarga);
			}
		}

		ltbArchivoDescarga.setModel(new ListModelList<Descarga>(descarga2));

	}

	

	@Listen("onClick = #ltbArchivoDescarga")
	public void descargarArchivo(){
		if (h==true) {
			Listitem listItem = ltbArchivoDescarga.getSelectedItem();
			Descarga descarga3= (Descarga) listItem.getValue();		
			Descarga descarga4 = servicioDescarga.buscarArchivo(descarga3.getId());
			Filedownload.save(descarga4.getContenidoDocumento(), descarga4.getTipoArchivo(), descarga4.getNombre());
			
		}else{
			Listitem listItem = ltbArchivoDescarga.getSelectedItem();
			Descarga descarga = (Descarga) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", descarga.getId());
			String vista = "portal-web/VSubirArchivoDescarga";
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoArchivoDescarga.onClose();
			
			
		}
		
	}
	

}
