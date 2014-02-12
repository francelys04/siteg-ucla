package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.AreaInvestigacion;

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

import servicio.SActividad;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CCatalogoActividad extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Listbox ltbActividad;
	@Wire
	private Textbox txtNombreMostrarActividad;
	@Wire
	private Textbox txtDescripcionMostrarActividad;
	@Wire
	private Window wdwCatalogoActividad;
	@Wire
	private Window wdwActividad;

	/*
	 * Metodo heredado del Controlador CGeneral donde se se buscan todas las
	 * actividades disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Actividad> actividad = servicioActividad.buscarActivos();
		ltbActividad.setModel(new ListModelList<Actividad>(actividad));
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
	 * Metodo que permite filtrar las actividades disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarActividad,#txtDescripcionMostrarActividad")
	public void filtrarDatosCatalogo() {
		List<Actividad> actividad1 = servicioActividad.buscarActivos();
		List<Actividad> actividad2 = new ArrayList<Actividad>();

		for (Actividad actividad : actividad1) {
			if (actividad
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarActividad.getValue().toLowerCase())
					&& actividad
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarActividad.getValue()
											.toLowerCase())) {
				actividad2.add(actividad);
			}
		}

		ltbActividad.setModel(new ListModelList<Actividad>(actividad2));

	}

	/*
	 * Metodo que permite obtener el objeto Actividad al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser enviada mediante un map a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbActividad")
	public void mostrarDatosCatalogo() {
		if (ltbActividad.getItemCount() != 0) {
			Listitem listItem = ltbActividad.getSelectedItem();
			Actividad actividadDatosCatalogo = (Actividad) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", actividadDatosCatalogo.getId());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");

			wdwCatalogoActividad.onClose();
		}
	}

}
