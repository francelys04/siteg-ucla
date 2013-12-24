package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import modelo.Actividad;
import modelo.Requisito;
import modelo.Teg;
import modelo.Profesor;
import modelo.Usuario;

import org.springframework.stereotype.Controller;
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

import servicio.STeg;
import servicio.SProfesor;

import configuracion.GeneradorBeans;

@Controller
public class CCatalogoEvaluarAvances extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	private long id = 0;
	@Wire
	private Listbox ltbAvanceProyecto;
	@Wire
	private Window wdwCatalogoEvaluarAvances;
	@Wire
	private Textbox txtMostrarTematica;
	@Wire
	private Textbox txtMostrarArea;
	@Wire
	private Textbox txtMostrarTitulo;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		/*
		 * Listado de todos los teg cuyo estatus=ProyectoFactible
		 */

		List<Teg> teg = servicioTeg.buscarProyectoFactible();

		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		// if (txtNombreRequisito == null) {
		ltbAvanceProyecto.setModel(new ListModelList<Teg>(teg));
		// }
		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Teg tegs = servicioTeg.buscarTeg(id);

				txtMostrarArea.setValue(tegs.getTematica()
						.getareaInvestigacion().getNombre());
				txtMostrarTematica.setValue(tegs.getTematica().getNombre());
				txtMostrarTitulo.setValue(tegs.getTitulo());

				id = tegs.getId();
				map.clear();
				map = null;
			}
		}
	}

	@Listen("onChange = #txtMostrarArea,#txtMostrarTematica,txtMostrarTitulo")
	public void filtrarDatosCatalogo() {
		List<Teg> tegs = servicioTeg.buscarActivos();
		List<Teg> tegs2 = new ArrayList<Teg>();

		for (Teg teg : tegs) {
			if (teg.getTematica().getareaInvestigacion().getNombre()
					.toLowerCase()
					.contains(txtMostrarArea.getValue().toLowerCase())
					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematica.getValue().toLowerCase())
					&& teg.getTitulo()
							.toLowerCase()
							.contains(txtMostrarTitulo.getValue().toLowerCase())) {

				tegs2.add(teg);
			}

		}

		ltbAvanceProyecto.setModel(new ListModelList<Teg>(tegs2));

	}

	@Listen("onDoubleClick = #ltbAvanceProyecto")
	public long mostrarDatosCatalogo() {
		/*
		 * Window window = (Window) Executions.createComponents(
		 * "/vistas/transacciones/VEvaluarAvances.zul", null, null);
		 * window.doModal();
		 */

		Listitem listItem = ltbAvanceProyecto.getSelectedItem();
		Teg itemDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", itemDatosCatalogo.getId());
		String vista = "transacciones/VEvaluarAvances";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoEvaluarAvances.onClose();
		return id;

	}

}
