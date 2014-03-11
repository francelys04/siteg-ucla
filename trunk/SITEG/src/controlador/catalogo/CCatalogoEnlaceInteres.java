package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.EnlaceInteres;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CCatalogoEnlaceInteres extends CGeneral {

	private static String vistaRecibida;

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

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todas los
	 * enlaces de interes disponibles y se llena el listado del mismo en el
	 * componente lista de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<EnlaceInteres> enlace = servicioEnlace.buscarActivos();
		ltbEnlace.setModel(new ListModelList<EnlaceInteres>(enlace));

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
	 * Metodo que permite filtrar los enlaces de interes disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * url de estas.
	 */
	@Listen("onChange = #txtNombreMostrarEnlace, #txtUrlMostrarEnlace")
	public void filtrarDatosCatalogo() {
		List<EnlaceInteres> enlace1 = servicioEnlace.buscarActivos();
		List<EnlaceInteres> enlace2 = new ArrayList<EnlaceInteres>();

		for (EnlaceInteres enlace : enlace1) {
			if (enlace.getNombre().toLowerCase()
					.contains(txtNombreMostrarEnlace.getValue().toLowerCase())
					&& enlace
							.getUrl()
							.toLowerCase()
							.contains(
									txtUrlMostrarEnlace.getValue()
											.toLowerCase()))

				enlace2.add(enlace);
		}
		ltbEnlace.setModel(new ListModelList<EnlaceInteres>(enlace2));
	}

	/*
	 * Metodo que permite obtener el objeto EnlaceInteres al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbEnlace")
	public void mostrarDatosCatalogo() {

		if (ltbEnlace.getSelectedCount() != 0) {
			Listitem listItem = ltbEnlace.getSelectedItem();
			EnlaceInteres enlaceDatosCatalogo = (EnlaceInteres) listItem
					.getValue();
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
