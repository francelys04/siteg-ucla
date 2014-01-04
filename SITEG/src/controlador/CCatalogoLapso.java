package controlador;

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

public class CCatalogoLapso extends CGeneral {
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();

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
	@Wire
	private Textbox txtNombreLapso;

	private static String vistaRecibida;

	void inicializar(Component comp) {

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		// System.out.println(lapsos.get(0).getNombre());
		if (txtNombreLapso == null) {
			ltbLapso.setModel(new ListModelList<Lapso>(lapsos));
		}

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

	}

	public void recibir(String vista) {
		vistaRecibida = vista;
		System.out.println("imprimo");
		System.out.println(vistaRecibida);
	}

	// Filtra por nombre
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

	// Carga los lapsos desde el catalogo hacia el formulario lapso academico
	@Listen("onDoubleClick = #ltbLapso")
	public void mostrarDatosCatalogo() {

		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VLapsoAcademico";

		} else {

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
