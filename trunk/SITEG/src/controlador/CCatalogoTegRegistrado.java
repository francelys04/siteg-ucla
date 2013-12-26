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
public class CCatalogoTegRegistrado extends CGeneral {
	
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	private long id = 0;
	@Wire
	private Listbox ltbCatalogoTegRegistrado;
	@Wire
	private Window wdwCatalogoEvaluarAvances;
	@Wire
	private Textbox txtTituloCatalogoTegRegistrado;
	@Wire
	private Textbox txtTematicaCatalogoTegRegistrado;
	@Wire
	private Textbox txtDescripcionCatalogoTegRegistrado;
   

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		/*
		 * Listado de todos los teg cuyo
		 * estatus=ProyectoFactible
		 */
 
		List<Teg> teg = servicioTeg.buscarTegRegistrado();

		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		//if (txtNombreRequisito == null) {
		ltbCatalogoTegRegistrado.setModel(new ListModelList<Teg>(teg));
		//}
		Selectors.wireComponents(comp, this, false);

		
		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido	 */
		
		
		if (map != null) {
			if (map.get("estatus") != null) {

				String estatus = (String) map.get("estatus");
				Teg tegs = (Teg) servicioTeg.buscarTegRegistrado();
			
				txtTituloCatalogoTegRegistrado.setValue(tegs.getTitulo());
				txtTematicaCatalogoTegRegistrado.setValue(tegs.getTematica().getNombre());
				txtDescripcionCatalogoTegRegistrado.setValue(tegs.getDescripcion());
				
				//btnEliminarUsuario.setDisabled(false);
				estatus = tegs.getEstatus();
				map.clear();
				map = null;
			}
		}
	}
	
	@Listen("onChange = #txtTituloCatalogoTegRegistrado,#txtTematicaCatalogoTegRegistrado,#txtDescripcionCatalogoTegRegistrado")
	public void filtrarDatosCatalogo() {
		List<Teg> tegs = servicioTeg.buscarActivos();
		List<Teg> tegs2 = new ArrayList<Teg>();

		
		for (Teg teg : tegs) {
			if (teg
					.getTitulo()
					.toLowerCase()
					.contains(
							txtTituloCatalogoTegRegistrado.getValue().toLowerCase())
					&& teg
							.getTematica().getNombre()
							.toLowerCase()
							.contains(
									txtTematicaCatalogoTegRegistrado.getValue().toLowerCase())
					&& teg
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionCatalogoTegRegistrado.getValue().toLowerCase())) {
				 
				tegs2.add(teg);
			}

		}

		ltbCatalogoTegRegistrado.setModel(new ListModelList<Teg>(tegs2));

	}
		//Le falta
	@Listen("onDoubleClick = #ltbCatalogoTegRegistrado")
	public void mostrarDatosCatalogo() {
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VRegistrarRevisiones.zul", null, null);	 				
		window.doModal();

		Listitem listItem = ltbCatalogoTegRegistrado.getSelectedItem();
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "transacciones/VRegistrarRevisiones.zul";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		List<Teg> teg = servicioTeg.buscarTegRegistrado();

	}
	
}
