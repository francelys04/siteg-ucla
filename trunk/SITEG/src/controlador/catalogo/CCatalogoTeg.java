package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

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

import servicio.STeg;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CCatalogoTeg extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();

	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	Tematica tematica = new Tematica();
	List<Teg> tegs = new ArrayList();
	@Wire
	private Listbox ltbTeg;
	@Wire
	private Textbox txtTituloMostrarTeg;
	@Wire
	private Textbox txtDescripcionMostrarTeg;
	@Wire
	private Textbox txtTutorMostrarTeg;

	@Wire
	private Window wdwCatalogoTeg;
	private long id = 0;

	private static String vistaRecibida;

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VTeg como VCatalogoTeg
	 * 
	 * @date 09-12-2013
	 */

	public void inicializar(Component comp) {

		// busca todas las areas y llena un listado

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsReporte");

		String estatus = "TEG Aprobado";

		if (map != null) {
			if (map.get("tegs") != null) {

				List<Teg> tegsReporte = (List<Teg>) map.get("tegs");

				if (tegsReporte.size() != 0) {
					tegs = servicioTeg.buscarSegunTegs(tegsReporte);
				} else {
					tegs = servicioTeg.buscarTegs(estatus);
				}
			} else {
				tegs = servicioTeg.buscarTegs(estatus);
			}

		}
		ltbTeg.setModel(new ListModelList<Teg>(tegs));

		Selectors.wireComponents(comp, this, false);

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	@Listen("onChange = #txtTituloMostrarTeg,#txtDescripcionMostrarTeg,#txtTutorMostrarTeg")
	public void filtrarDatosCatalogo() {
		List<Teg> Teg1 = servicioTeg.buscarActivos();
		List<Teg> Teg2 = new ArrayList<Teg>();

		for (Teg Teg : Teg1) {
			if (Teg.getTitulo().toLowerCase()
					.contains(txtTituloMostrarTeg.getValue().toLowerCase())
					&& Teg.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTeg.getValue()
											.toLowerCase())
					&& Teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTutorMostrarTeg.getValue().toLowerCase())) {
				Teg2.add(Teg);
			}
		}

		ltbTeg.setModel(new ListModelList<Teg>(Teg2));

	}

	@Listen("onDoubleClick = #ltbTeg")
	public void mostrarDatosCatalogo() {
		if (ltbTeg.getItemCount() != 0) {
			Listitem listItem = ltbTeg.getSelectedItem();
			Teg TegDatosCatalogo = (Teg) listItem.getValue();
			HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
					.getCurrent().getAttribute("itemsReporte");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map = map2;
			map.put("id", TegDatosCatalogo.getId());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");

			wdwCatalogoTeg.onClose();
		}
	}

}
