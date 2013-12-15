package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;

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

@Controller
public class CCatalogoActividad extends SelectorComposer<Component> {

	SActividad servicioActividad = GeneradorBeans
			.getServicioActividad();

	@Wire
	private Listbox ltbActividad;
	@Wire
	private Textbox txtNombreMostrarActividad;
	@Wire
	private Textbox txtDescripcionMostrarActividad;
	@Wire
	private Window wdwCatalogoActividad;
	@Wire
	private Textbox txtNombreActividad;
	@Wire
	private Textbox txtDescripcionActividad;
	private long id = 0;
	@Wire
	private Window wdwActividad;
	
	private String vistaRecibida;

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VActividad como VCatalogoActividad
	 * 
	 * @date 09-12-2013
	 */
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		/*
		 * Listado de todos las actividades que se encuentran activos, cuyo
		 * estatus=true con el servicioActividad mediante el metodo
		 * buscarActivos
		 */

		List<Actividad> actividad = servicioActividad.buscarActivos();

		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		if (txtNombreActividad == null) {
			ltbActividad.setModel(new ListModelList<Actividad>(actividad));
		}

		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("actividadCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (long) map.get("id");
				Actividad actividad2 = servicioActividad
						.buscarActividad(codigo);
				txtNombreActividad.setValue(actividad2.getNombre());
				txtDescripcionActividad.setValue(actividad2.getDescripcion());
				id = actividad2.getId();
				map.clear();
				map = null;
			}
		}
	}

	/**
	 * Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	 * descripcion
	 * 
	 * @date 09-12-2013
	 */
	

	/**
	 * Aca se selecciona una actividad del catalogo
	 * 
	 * @date 09-12-2013
	 */
	public void recibir (String vista)
	{
		vistaRecibida = vista;
		System.out.println("imprimo");
		System.out.println(vistaRecibida);
	}
	
	// Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	// descripcion
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

	
	// Aca se selecciona una actividad del catalogo
	@Listen("onDoubleClick = #ltbActividad")
	public void mostrarDatosCatalogo() {


		Listitem listItem = ltbActividad.getSelectedItem();
		Actividad actividadDatosCatalogo = (Actividad) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", actividadDatosCatalogo.getId());
		
		map.put("vistaRecibida", vistaRecibida);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
       //Executions.sendRedirect("/vistas/maestros/VActividad.zul");
		 Executions.sendRedirect("/vistas/arbol.zul");
		
		wdwCatalogoActividad.onClose();

	}

}
