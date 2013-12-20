package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Requisito;
import modelo.Teg;

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


import configuracion.GeneradorBeans;


@Controller
public class CCatalogoSolicitudRegistroProyecto extends CGeneral {
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	
	@Wire
	private Listbox ltbSolcitudRegistroProyecto;
	@Wire
	private Window wdwCatalogoSolicitudRegistroProyecto;
	@Wire
	private Textbox txtMostrarFecha;
	@Wire
	private Textbox txtMostrarTematica;
	@Wire
	private Textbox txtMostrarArea;
	@Wire
	private Textbox txtMostrarTitulo;
	@Wire
	private Textbox txtMostrarNombreTutor;
	@Wire
	private Textbox txtMostrarApellidoTutor;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		/*
		 * Listado de todos los teg cuyo
		 * estatus=SolicitandoRegistro
		 */

		List<Teg> teg = servicioTeg.buscarSolicitudRegistroTeg();

		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		//if (txtNombreRequisito == null) {
			ltbSolcitudRegistroProyecto.setModel(new ListModelList<Teg>(teg));
		//}

		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido	 */
		
	}
	
	@Listen("onChange = #txtMostrarTematica,#txtMostrarArea,#txtMostrarTitulo,#txtMostrarNombreTutor,# txtMostrarApellidoTutor")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = servicioTeg.buscarSolicitudRegistroTeg();
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (teg
					.getTematica().getNombre()
					.toLowerCase()
					.contains(
							txtMostrarTematica.getValue().toLowerCase())					
							
					&& teg
							.getTematica().getareaInvestigacion().getNombre()
							.toLowerCase()
							.contains(
									txtMostrarArea.getValue()
											.toLowerCase())
											
					&& teg
							.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTitulo.getValue()
											.toLowerCase())
					&& teg
							.getTutor().getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutor.getValue()
											.toLowerCase())
					&& teg
							.getTutor().getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutor.getValue()
											.toLowerCase()))
	
			
			{
				teg2.add(teg);
			}
		}

		ltbSolcitudRegistroProyecto.setModel(new ListModelList<Teg>(teg2));

	}
	
	
	@Listen("onDoubleClick = #ltbSolcitudRegistroProyecto")
	public void mostrarDatosCatalogo() {		
		
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VVerificarSolicitudProyecto.zul", null, null);	 				
		window.doModal();

			Listitem listItem = ltbSolcitudRegistroProyecto.getSelectedItem();
			Teg tegDatosCatalogo = (Teg) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", tegDatosCatalogo.getId());
			String vista = "transacciones/VVerificarSolicitudProyecto";
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			List<Teg> teg = servicioTeg.buscarSolicitudRegistroTeg();
/*
			//if (txtNombreRequisito == null) {
				ltbSolcitudRegistroProyecto.setModel(new ListModelList<Teg>(teg));
			//}

		//	Selectors.wireComponents(comp, this, false);

			HashMap<String, Object> map1 = (HashMap<String, Object>) Sessions
					.getCurrent().getAttribute("tegCatalogo");
			if (map1 != null) {
				if (map1.get("id") != null) {

					long codigo =  (Long) map1.get("id");
					Teg teg3 = servicioTeg.buscarTeg(codigo);
					txtNombreActividad.setValue(actividad2.getNombre());
					txtDescripcionActividad.setValue(actividad2.getDescripcion());
					id = actividad2.getId();
					btnEliminarActividad.setDisabled(false);
					map1.clear();
					map1 = null;
				}
			}*/

	}
}
