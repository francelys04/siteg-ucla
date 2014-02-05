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

	STematica servicioTematica = GeneradorBeans.getSTematica();

	// atributos de la vista tematica
	@Wire
	private Combobox cmbAreaTematica;
	@Wire
	private Textbox txtNombreTematica;
	@Wire
	private Textbox txtDescripcionTematica;

	// atributos de la pantalla del catalogo
	@Wire
	private Listbox ltbTematica;
	@Wire
	private Textbox txtNombreMostrarTematica;
	@Wire
	private Textbox txtAreaMostrarTematica;
	@Wire
	private Textbox txtDescripcionMostrarTematica;
	private long id = 0;

	@Wire
	private Window wdwCatalogoTematica;

	private static String vistaRecibida;

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 *
	 * 
	 * @date 09-12-2013
	 */
	public void inicializar(Component comp) {

		/*
		 * Listado de todos las tematicas que se encuentran activas
		 * 
		 */

		List<Tematica> tematica = servicioTematica.buscarActivos();

		
		if (txtNombreTematica == null) {
			ltbTematica.setModel(new ListModelList<Tematica>(tematica));
		}

		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tematicaCatalogo");
		

	}

	/**
	 * Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	 * descripcion
	 * 
	 * @date 09-12-2013
	 */

	
	public void recibir(String vista) {
		vistaRecibida = vista;
	}

	// filtra en el catalogo

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

	// Aca se selecciona una tematica del catalogo
	@Listen("onDoubleClick = #ltbTematica")
	public void mostrarDatosCatalogo() {
		if(ltbTematica.getItemCount()!=0){

		try {
		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VTematica";

		} else {
			
			Listitem listItem = ltbTematica.getSelectedItem();
			Tematica tematicaDatosCatalogo = (Tematica) listItem.getValue();
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
		
