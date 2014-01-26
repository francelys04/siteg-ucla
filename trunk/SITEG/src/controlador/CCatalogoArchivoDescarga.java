package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.Archivo;
import org.springframework.stereotype.Controller;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SArchivo;


import configuracion.GeneradorBeans;

@Controller
public class CCatalogoArchivoDescarga extends CGeneral {

	SArchivo servicioArchivo = GeneradorBeans.getServicioArchivo();

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
	private static boolean encontrado=true;
	
    

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
		List<Archivo> archivo = servicioArchivo.buscarActivos("Descarga");
		ltbArchivoDescarga.setModel(new ListModelList<Archivo>(archivo));
			
		

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
		encontrado=false;
	}
	public void metodoPrender(){
		encontrado=true;
	}

	// Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	// descripcion
	@Listen("onChange = #txtNombreMostrarArchivoDescarga,#txtDescripcionMostrarArchivoDescarga,#txtProgramaMostrarArchivoDescarga")
	public void filtrarDatosCatalogo() {
		List<Archivo> archivo1 = servicioArchivo.buscarActivos("Descarga");
		List<Archivo> archivo2 = new ArrayList<Archivo>();

		for (Archivo archivo : archivo1) {
			if (archivo
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarArchivoDescarga.getValue().toLowerCase())
					&& archivo
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArchivoDescarga.getValue()
											.toLowerCase())
						&& archivo
							.getPrograma().getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarArchivoDescarga.getValue()
											.toLowerCase()))
			{
				archivo2.add(archivo);
			}
		}

		ltbArchivoDescarga.setModel(new ListModelList<Archivo>(archivo2));

	}

	
	//si encontrado = true permite descargar un archivo en infromacion de interes
	//si no mapea los datos a lhacia las vista VArchivoDescarga
	@Listen("onClick = #ltbArchivoDescarga")
	public void descargarArchivo(){
		try {
		if(ltbArchivoDescarga.getItemCount()!=0){
		if (encontrado==true) {
			Listitem listItem = ltbArchivoDescarga.getSelectedItem();
			Archivo archivo3= (Archivo) listItem.getValue();		
			Archivo archivo4 = servicioArchivo.buscarArchivo(archivo3.getId());
			Filedownload.save(archivo4.getContenidoDocumento(), archivo4.getTipoDocumento(), archivo4.getNombre());
			
		}else{
			Listitem listItem = ltbArchivoDescarga.getSelectedItem();
			Archivo archivo = (Archivo) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", archivo.getId());
			String vista = "portal-web/VSubirArchivoDescarga";
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoArchivoDescarga.onClose();
			
			
		}
		}
		} catch (NullPointerException e) {

			System.out.println("NullPointerException");
		}
	}
	

}
