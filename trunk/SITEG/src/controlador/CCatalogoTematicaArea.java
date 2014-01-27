package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.AreaInvestigacion;
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

@Controller
public class CCatalogoTematicaArea extends CGeneral {

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

	private static AreaInvestigacion area;

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VActividad como VCatalogoTematicaArea
	 * 
	 * @date 09-12-2013
	 */
	void inicializar(Component comp) {

		/*
		 * Listado de todos las tematicas que se encuentran activos, cuyo
		 * estatus=true
		 */
		
		List<Tematica> tematica = servicioTematica.buscarTematicasDeArea(area);

		/*
		 * Validacion para mostrar el listado de tematicas
		 */
		
			ltbTematica.setModel(new ListModelList<Tematica>(tematica));
	

		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item
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

	/**
	 * Aca se selecciona una actividad del catalogo
	 * 
	 * @date 09-12-2013
	 */
	public void recibirId (AreaInvestigacion a) {
		area = a;
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

	
	

}
