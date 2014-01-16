package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.Archivo;
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


import configuracion.GeneradorBeans;

@Controller
public class CCatalogoNoticia extends CGeneral {

	SNoticia servicioNoticia = GeneradorBeans.getServicioNoticia();

	@Wire
	private Textbox txtNombreMostrarNoticia;
	@Wire
	private Textbox txtDescripcionMostrarNoticia;
	@Wire
	private Listbox ltbNoticia;
	@Wire
	private Window wdwCatalogoNoticia;
	@Wire
	private Image imagen;
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
	
		List<Noticia> noticia = servicioNoticia.buscarActivos();	
		ltbNoticia.setModel(new ListModelList<Noticia>(noticia));
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("noticiaCatalogo");
	
		
	}
	
    
	//Aca se filtran las busqueda en el catalogo, ya sea por nombre o por descripcion
		@Listen("onChange = #txtNombreMostrarNoticia, #txtDescripcionMostrarNoticia")
		public void filtrarDatosCatalogo() {
			List<Noticia> noticia1 = servicioNoticia.buscarActivos();
			List<Noticia> noticia2 = new ArrayList<Noticia>();

			for (Noticia noticia : noticia1) {
				if (noticia
						.getNombre()
						.toLowerCase()
						.contains(
								txtNombreMostrarNoticia.getValue().toLowerCase())
					&& noticia
					.getDescripcion()
					.toLowerCase()
					.contains(txtDescripcionMostrarNoticia.getValue().toLowerCase())
						)
						
					noticia2.add(noticia);
				}
			ltbNoticia.setModel(new ListModelList<Noticia>(noticia2));
			}
		
		//Aca se selecciona una Descarga del catalogo
		@Listen("onDoubleClick = #ltbNoticia")
		public void mostrarDatosCatalogo() {
			List<Noticia> noticia = servicioNoticia.buscarActivos();
			if (noticia.size() ==2){
				Messagebox.show("No puede eliminarse deben haber minimo dos imagenes, registre una y elimine", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
			}
			else{
			Listitem listItem = ltbNoticia.getSelectedItem();
			Noticia descargaDatosCatalogo = (Noticia) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", descargaDatosCatalogo.getId());
			String vista = "portal-web/VNoticia.zul";
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("noticiaCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoNoticia.onClose();
			}
		}

	

}
