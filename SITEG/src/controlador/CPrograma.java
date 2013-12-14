package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.Grupo;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

@Controller
public class CPrograma extends CGeneral {

	/*
	 * Inicializacion del servicioPrograma que permitira realizar las
	 * operaciones con las entidades relacionadas con base de datos
	 */
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	/*
	 * Declaracion de componentes dado a sus id, implementados en las vistas
	 * VPrograma y VCatalago
	 */
	@Wire
	private Textbox txtNombrePrograma;
	@Wire
	private Textbox txtDescripcionPrograma;
	@Wire
	private Listbox ltbPrograma;
	@Wire
	private Window wdwCatalogoPrograma;
	@Wire
	private Textbox txtNombreMostrarPrograma;
	@Wire
	private Textbox txtDescripcionMostrarPrograma;

	/*
	 * Inicializacion de la varible global id que sera asociado con el id del
	 * programa
	 */
	long id = 0;

	/*
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VPrograma como VCatalogoPrograma
	 */
	void inicializar(Component comp) {

		/*
		 * Listado de todos los programas que se encuentran activos, cuyo
		 * estatus=true con el servicioPrograma mediante el metodo buscarActivas
		 */
		List<Programa> programas = servicioPrograma.buscarActivas();
		List<AreaInvestigacion> areas = servicioArea.buscarActivos();
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
		/*
		 * Validacion para vaciar la informacion del VCatalogo a la vista
		 * VPrograma.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if ((Long) map.get("id") != null) {
				id = ((Long) map.get("id"));
				System.out.println(id);
				/*
				 * Creacion de objeto cargado con el servicioPrograma mediante
				 * el metodo buscar dado a su id y asi llenar los textbox de la
				 * vista VPrograma
				 */
				Programa programa = servicioPrograma.buscar(id);
				txtNombrePrograma.setValue(programa.getNombre());
				txtDescripcionPrograma.setValue(programa.getDescripcion());
				map.clear();
				map = null;
			}
		}

	}

	// Metodo que permite abrir la vista VCatalago en forma modal //
	@Listen("onClick = #btnBuscarPrograma")
	public void buscarPrograma() {

		Window window = (Window) Executions.createComponents(
				"/vistas/VCatalogoPrograma.zul", null, null);
		window.doModal();

	}

	// Metodo para guardar un programa
	@Listen("onClick = #btnGuardarPrograma")
	public void guardarPrograma() {

		String nombre = txtNombrePrograma.getValue();
		String descripcion = txtDescripcionPrograma.getValue();
		Boolean estatus = true;
		Programa programa = new Programa(id, nombre, descripcion, estatus);
		/*
		 * Permite guardar el objeto programa con el servicioPrograma mediante
		 * el metodo guardar dado el programa anteriormente cargado;
		 */
		servicioPrograma.guardar(programa);
		cancelarPrograma();
		System.out.println("Programa Guardado");
	}

	// Metodo para eliminar un programa dado su id
	@Listen("onClick = #btnEliminarPrograma")
	public void eliminarPrograma() {
		/*
		 * Creacion de objeto cargado con el servicioPrograma mediante el metodo
		 * buscar dado a su id
		 */
		Programa programa = servicioPrograma.buscar(id);
		programa.setEstatus(false);
		/*
		 * Permite eliminar logicamente (Cambio de estatus) el programa con el
		 * servicioPrograma mediante el metodo guardad dado el programa
		 * anteriormente cargado
		 */
		servicioPrograma.guardar(programa);
		cancelarPrograma();
		System.out.println("Programa Eliminado");
	}

	// Metodo para limpiar los componentes textbox y variable id
	@Listen("onClick = #btnCancelarPrograma")
	public void cancelarPrograma() {
		id = 0;
		txtNombrePrograma.setValue("");
		txtDescripcionPrograma.setValue("");

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
		String vista = "VPrograma";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		// Permite abrir la vista VPrograma
		Executions.sendRedirect("/vistas/arbol.zul");
		// Permite cerrar la vista VCatalogo
		wdwCatalogoPrograma.onClose();

	}
}
