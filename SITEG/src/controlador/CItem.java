package controlador;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.ItemEvaluacion;

import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import servicio.SItem;
import configuracion.GeneradorBeans;

@Controller
public class CItem extends CGeneral {
	SItem servicioItem = GeneradorBeans.getServicioItem();


	@Wire
	private Textbox txtNombreItem;
	@Wire
	private Textbox txtDescripcionItem;
	@Wire
	private Textbox txtIdItem;
	@Wire
	private Listbox ltbItem;
	@Wire
	private Window wdwCatalogoItem;
	@Wire
	private Textbox txtNombreMostrarItem;
	@Wire
	private Textbox txtDescripcionMostrarItem;
	private long id = 0;

	
	void inicializar(Component comp) {

		List<ItemEvaluacion> items = servicioItem.buscarItemsActivos();	
//		System.out.println(items.get(0).getNombre());
//		System.out.println(items.get(1).getNombre());
		
		if(txtNombreItem==null){
		ltbItem.setModel(new ListModelList<ItemEvaluacion>(items));
		}
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

	
		if (map != null) {
			if ( map.get("id") != null) {

				long codigo = (Long) map.get("id");
				ItemEvaluacion item = servicioItem.buscarItem(codigo);
				txtNombreItem.setValue(item.getNombre());
				txtDescripcionItem.setValue(item.getDescripcion());
				id=item.getId();
				map.clear();
				map = null;
			}
		}

	}

	@Listen("onClick = #btnGuardarItem")
	public void guardarEstudiante() {
		//ACOMODAR LA VISTA METIENDOLE EL VALOR
		String nombre = txtNombreItem.getValue();
		String descripcion = txtDescripcionItem.getValue();
		Boolean estatus = true;
		String tipo = "valor";
		ItemEvaluacion item= new ItemEvaluacion(id,nombre, descripcion, estatus, tipo);
		servicioItem.guardar(item);
		cancelarItem();
		id = 0;
	}
	
	
	@Listen("onClick = #btnCancelarItem")
	public void cancelarItem() {
		txtNombreItem.setValue("");
		txtDescripcionItem.setValue("");
		id=0;
	}
	
	@Listen("onClick = #btnEliminarItem")
	public void eliminarLapso() {
		
		ItemEvaluacion item = servicioItem.buscarItem(id);
		item.setEstatus(false);
		servicioItem.guardar(item);
		cancelarItem();
		System.out.println("Item Eliminado");
		id=0;
	}

	@Listen("onClick = #btnCatalogoItem")
	public void buscarItem() {

		Window window = (Window) Executions.createComponents(
				"/vistas/VCatalogoItem.zul", null, null);
		window.doModal();

	}
	
	@Listen("onDoubleClick = #ltbItem")
	public void mostrarDatosCatalogo() {
		Listitem listItem = ltbItem.getSelectedItem();
		ItemEvaluacion itemDatosCatalogo = (ItemEvaluacion) listItem.getValue();
		String vista = "VItem";
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", itemDatosCatalogo.getId());
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoItem.onClose();

	}
	
	@Listen("onChange = #txtNombreMostrarItem,#txtDescripcionMostrarItem")
	public void filtrarDatosCatalogo() {
		List<ItemEvaluacion> item = servicioItem.buscarItemsActivos();
		List<ItemEvaluacion> item2 = new ArrayList<ItemEvaluacion>();

		for (ItemEvaluacion item1 : item) {
			if (item1
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarItem.getValue().toLowerCase())
					&& item1
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarItem.getValue()
											.toLowerCase()))
			{
				item2.add(item1);
			}

		}

		ltbItem.setModel(new ListModelList<ItemEvaluacion>(item2));

	}
	
}