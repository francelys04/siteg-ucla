package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Tematica;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.STematica;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CCatalogoTematica extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Listbox ltbTematica;
	@Wire
	private Textbox txtNombreMostrarTematica;
	@Wire
	private Textbox txtAreaMostrarTematica;
	@Wire
	private Textbox txtDescripcionMostrarTematica;
	@Wire
	private Window wdwCatalogoTematica;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todas las
	 * tematicas disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Tematica> tematica = servicioTematica.buscarActivos();
		ltbTematica.setModel(new ListModelList<Tematica>(tematica));
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
	 * Metodo que permite filtrar las tematicas disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre, area y *
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarTematica,#txtAreaMostrarTematica,#txtDescripcionMostrarTematica")
	public void filtrarDatosCatalogo() {
		List<Tematica> tematicas1 = servicioTematica.buscarActivos();
		List<Tematica> tematicas2 = new ArrayList<Tematica>();

		for (Tematica tematica : tematicas1) {
			if (tematica
					.getNombre()
					.toLowerCase()
					.contains(txtNombreMostrarTematica.getValue().toLowerCase())
					&& tematica
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtAreaMostrarTematica.getValue()
											.toLowerCase())
					&& tematica
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTematica.getValue()
											.toLowerCase())) {
				tematicas2.add(tematica);
			}

		}

		ltbTematica.setModel(new ListModelList<Tematica>(tematicas2));

	}

	/*
	 * Metodo que permite obtener el objeto Tematica al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbTematica")
	public void mostrarDatosCatalogo() {
		if (ltbTematica.getItemCount() != 0) {

			try {
				if (vistaRecibida == null) {

					vistaRecibida = "maestros/VTematica";

				} else {

					Listitem listItem = ltbTematica.getSelectedItem();
					Tematica tematicaDatosCatalogo = (Tematica) listItem
							.getValue();
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", tematicaDatosCatalogo.getId());
					String vista = vistaRecibida;
					map.put("vista", vista);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					Executions.sendRedirect("/vistas/arbol.zul");
					wdwCatalogoTematica.onClose();

				}
			} catch (NullPointerException e) {

				System.out.println("NullPointerException");
			}

		}
	}
}
