package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Programa;

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

import servicio.SPrograma;
import configuracion.GeneradorBeans;

public class CCatalogoPrograma  extends CGeneral {
	
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	
	@Wire
	private Listbox ltbPrograma;
	@Wire
	private Window wdwCatalogoPrograma;
	@Wire
	private Textbox txtNombreMostrarPrograma;
	@Wire
	private Textbox txtDescripcionMostrarPrograma;
	 private static String vistaRecibida;
	
	void inicializar(Component comp) {

		/*
		 * Listado de todos los programas que se encuentran activos, cuyo
		 * estatus=true con el servicioPrograma mediante el metodo buscarActivas
		 */
		List<Programa> programas = servicioPrograma.buscarActivas();
		
		/*
		 * Validacion para mostrar el listado de programas mediante el
		 * componente ltbPrograma dependiendo si se encuentra ejecutando la
		 * vista VCatalogoPrograma
		 */
		if (ltbPrograma != null) {
			ltbPrograma.setModel(new ListModelList<Programa>(programas));
		}

		Selectors.wireComponents(comp, this, false);
		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VCatalogo
		 */
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
	

	}
	public void recibir (String vista)
	{
		vistaRecibida = vista;
		//System.out.println("imprimo");
		//System.out.println(vistaRecibida);
	}
	
	

	/*
	 * Metodo para filtrar los programas dado a el nombre y descripcion y del
	 * programa en la vista VCatalogoPrograma
	 */
	@Listen("onChange = #txtNombreMostrarPrograma,#txtDescripcionMostrarPrograma")
	public void filtrarDatosCatalogo() {
		List<Programa> programas1 = servicioPrograma.buscarActivas();
		List<Programa> programas2 = new ArrayList<Programa>();
		/*
		 * Ciclo que permite recorrer cada uno de los programas asociandolo con
		 * el filtreo del nombre y descripcion del programa
		 */
		for (Programa programa : programas1) {
			if (programa
					.getNombre()
					.toLowerCase()
					.contains(txtNombreMostrarPrograma.getValue().toLowerCase())
					&& programa
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarPrograma.getValue()
											.toLowerCase())) {
				programas2.add(programa);
			}

		}

		ltbPrograma.setModel(new ListModelList<Programa>(programas2));

	}

	/*
	 * Metodo que permite pasar los datos del programa a VPrograma mostrado en
	 * la vista VCatalogoPrograma
	 */
	@Listen("onDoubleClick = #ltbPrograma")
	public void mostrarDatosCatalogo() {

		// Programa seleccionado en el catalogo VCatalogoPrograma
		Listitem listItem = ltbPrograma.getSelectedItem();

		/*
		 * Creacion de objecto cargado con el servicioPrograma la cual mediante
		 * el metodo buscarPorNombrePrograma nos retornara los datos del
		 * programa dado al valor del item seleccionado en la vista
		 * VCatalgoPrograma
		 */
		Programa programaDatos = servicioPrograma
				.buscarPorNombrePrograma(((Programa) listItem.getValue())
						.getNombre());
		/*
		 * Permite asignar uno o mas valores para poder ser utilizado en otra
		 * region del proyecto
		 */
		final HashMap<String, Object> map = new HashMap<String, Object>();
		/*
		 * Permite guardar el id del programa seleccionado asociandolo con el
		 * nombre de id en la variable map
		 */
		map.put("id", programaDatos.getId());
		/*
		 * Permite asignar el map asociandolo con el nombre de programasCatalogo
		 * utilizando Sessions, setiandolo para posteriormente retornar su valor
		 * y asi transportar los datos a la vista VPrograma
		 */
		String vista = vistaRecibida;
		map.put("vista", vista);
		
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		
		Executions.sendRedirect("/vistas/arbol.zul");
		// Permite cerrar la vista VCatalogo
		wdwCatalogoPrograma.onClose();

	}

}
