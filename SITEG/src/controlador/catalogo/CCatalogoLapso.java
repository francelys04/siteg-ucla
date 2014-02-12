package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Lapso;

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

import servicio.SLapso;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CCatalogoLapso extends CGeneral {

	private static String vistaRecibida;

	@Wire
	private Listbox ltbLapso;
	@Wire
	private Window wdwCatalogoLapso;
	@Wire
	private Textbox txtCedulaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarLapso;
	@Wire
	private Textbox txtFechaInicialMostrarLapso;
	@Wire
	private Textbox txtFechaFinalMostrarLapso;


	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * disponibles disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		ltbLapso.setModel(new ListModelList<Lapso>(lapsos));
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
	 * Metodo que permite filtrar los lapsos disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre de estos.
	 */
	@Listen("onChange = #txtNombreMostrarLapso")
	public void filtrarDatosCatalogo() {
		List<Lapso> lapsos1 = servicioLapso.buscarActivos();
		List<Lapso> lapsos2 = new ArrayList<Lapso>();

		for (Lapso lapso : lapsos1) {
			if (lapso.getNombre().toLowerCase()
					.contains(txtNombreMostrarLapso.getValue().toLowerCase())) {
				lapsos2.add(lapso);
			}

		}

		ltbLapso.setModel(new ListModelList<Lapso>(lapsos2));

	}

	/*
	 * Metodo que permite obtener el objeto Lapso al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbLapso")
	public void mostrarDatosCatalogo() {

		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VLapsoAcademico";

		} else {
			if( ltbLapso.getSelectedCount()!=0){
			Listitem listItem = ltbLapso.getSelectedItem();
			Lapso lapsoDatosCatalogo = (Lapso) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", lapsoDatosCatalogo.getId());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");
			wdwCatalogoLapso.onClose();
		}
		}
	}

}
