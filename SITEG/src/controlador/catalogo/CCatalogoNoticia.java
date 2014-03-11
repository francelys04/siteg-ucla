package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Noticia;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CCatalogoNoticia extends CGeneral {

	
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
	
	/*
	 * Metodo heredado del Controlador CGeneral donde se se buscan todas las
	 * noticias disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	@Override
	public	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Noticia> noticia = servicioNoticia.buscarActivos();	
		ltbNoticia.setModel(new ListModelList<Noticia>(noticia));
	}
	
    
	/*
	 * Metodo que permite filtrar las noticias disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estas.
	 */
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
		
		/*
		 * Metodo que permite obtener el objeto Noticia al realizar el evento
		 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
		 * para luego poder ser mapeada y enviada a la vista asociada a ella.
		 */
		@Listen("onDoubleClick = #ltbNoticia")
		public void mostrarDatosCatalogo() {
			List<Noticia> noticia = servicioNoticia.buscarActivos();
			if (noticia.size() ==2){
				Messagebox.show("No puede eliminarse deben haber minimo dos imagenes, registre una y elimine", "Informacion", Messagebox.OK, Messagebox.INFORMATION);
			}
			else{
			if( ltbNoticia.getSelectedCount()!=0){
			Listitem listItem = ltbNoticia.getSelectedItem();
			Noticia descargaDatosCatalogo = (Noticia) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", descargaDatosCatalogo.getId());
			String vista = "portal-web/VNoticia";
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoNoticia.onClose();
			}
		}
		}

	

}
