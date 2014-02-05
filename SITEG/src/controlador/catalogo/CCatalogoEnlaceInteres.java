package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Archivo;
import modelo.EnlaceInteres;
import modelo.Lapso;
import modelo.Noticia;

import org.springframework.stereotype.Controller;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SArchivo;
import servicio.SNoticia;
import servicio.SEnlaceInteres;


import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CCatalogoEnlaceInteres extends CGeneral {

	SEnlaceInteres servicioEnlace = GeneradorBeans.getServicioEnlace();

	@Wire
	private Textbox txtNombreMostrarEnlace;
	@Wire
	private Textbox txtUrlMostrarEnlace;
	@Wire
	private Listbox ltbEnlace;
	@Wire
	private Window wdwCatalogoEnlace;
	@Wire
	private Image imagen;
	private static String vistaRecibida;
	
	
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
	//Se llena el catalogo con las noticias
		List<EnlaceInteres> enlace = servicioEnlace.buscarActivos();	
		ltbEnlace.setModel(new ListModelList<EnlaceInteres>(enlace));
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("enlaceCatalogo");
	
		
	}
	
	public void recibir(String vista) {
		vistaRecibida = vista;

	}
	
    
	//Aca se filtran las busqueda en el catalogo, ya sea por nombre o por descripcion
		@Listen("onChange = #txtNombreMostrarEnlace, #txtUrlMostrarEnlace")
		public void filtrarDatosCatalogo() {
			List<EnlaceInteres> enlace1 = servicioEnlace.buscarActivos();
			List<EnlaceInteres> enlace2 = new ArrayList<EnlaceInteres>();

			for (EnlaceInteres enlace : enlace1) {
				if (enlace
						.getNombre()
						.toLowerCase()
						.contains(
								txtNombreMostrarEnlace.getValue().toLowerCase())
					&& enlace
					.getUrl()
					.toLowerCase()
					.contains(txtUrlMostrarEnlace.getValue().toLowerCase())
						)
						
					enlace2.add(enlace);
				}
			ltbEnlace.setModel(new ListModelList<EnlaceInteres>(enlace2));
			}
		
		//Aca se selecciona una Descarga del catalogo
		@Listen("onDoubleClick = #ltbEnlace")
		public void mostrarDatosCatalogo() {
			
			if( ltbEnlace.getSelectedCount()!=0){
			Listitem listItem = ltbEnlace.getSelectedItem();
			EnlaceInteres enlaceDatosCatalogo = (EnlaceInteres) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", enlaceDatosCatalogo.getId());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoEnlace.onClose();
								
			}
		
		}

	

}

