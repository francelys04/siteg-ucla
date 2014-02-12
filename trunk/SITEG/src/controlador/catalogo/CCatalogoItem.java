package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.ItemEvaluacion;

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

import servicio.SItem;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CCatalogoItem extends CGeneral {


	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Textbox txtNombreItem;
	@Wire
	private Textbox txtDescripcionItem;
	@Wire
	private Textbox txtTipoItem;
	@Wire
	private Textbox txtIdItem;
	@Wire
	private Listbox ltbItem;
	@Wire
	private Window wdwCatalogoItem;
	@Wire
	private Textbox txtNombreMostrarItem;
	@Wire
	private Textbox txtTipoMostrarItem;
	@Wire
	private Textbox txtDescripcionMostrarItem;

	
	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * items disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	@Override
	public	void inicializar(Component comp) {

		List<ItemEvaluacion> items = servicioItem.buscarItemsActivos();
		ltbItem.setModel(new ListModelList<ItemEvaluacion>(items));
	}
	/*
	 * Metodo que permite filtrar los items disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre,
	 * descripcion y tipo de estos.
	 */
	@Listen("onChange = #txtNombreMostrarItem, #txtTipoMostrarItem, #txtDescripcionMostrarItem")
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
											.toLowerCase())
				&& item1
				.getTipo()
				.toLowerCase()
				.contains(
						txtTipoMostrarItem.getValue()
								.toLowerCase()))
			{
				item2.add(item1);
			}

		}

		ltbItem.setModel(new ListModelList<ItemEvaluacion>(item2));

	}
	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}
	/*
	 * Metodo que permite obtener el objeto Item al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbItem")
	public void mostrarDatosCatalogo() {
		
		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VItem";

		} else {
		if( ltbItem.getSelectedCount()!=0){
		Listitem listItem = ltbItem.getSelectedItem();
		ItemEvaluacion itemDatosCatalogo = (ItemEvaluacion) listItem.getValue();
		String vista = vistaRecibida;
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", itemDatosCatalogo.getId());
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoItem.onClose();
		}
		}
	}

}
