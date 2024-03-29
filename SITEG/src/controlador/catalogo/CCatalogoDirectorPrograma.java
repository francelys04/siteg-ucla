package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Profesor;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/**
 * Controlador asociado a la vista catalogo director programa que permite
 * mostrar los profesores disponibles que no son directores de programa a traves
 * de un listado
 */
@Controller
public class CCatalogoDirectorPrograma extends CGeneral {

	private static String vistaRecibida;
	List<Profesor> profesores = new ArrayList();
	@Wire
	private Listbox ltbProfesor;
	@Wire
	private Combobox cmbCategoriaProfesor;
	@Wire
	private Window wdwCatalogoProfesor;
	@Wire
	private Window wdwProfesor;
	@Wire
	private Window wdwCatalogoDirectorPrograma;
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el map
	 * recibido del catalogo exista y se llenan las listas correspondientes de
	 * la vista dado una condicional, que si se cumple se mostrara los
	 * profesores sin usuarios sino todos los profesores activos.
	 */
	public void inicializar(Component comp) {

		profesores = servicioProfesor.buscarProfesoresSinPrograma();
		ltbProfesor.setModel(new ListModelList<Profesor>(profesores));

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("usuario") != null) {
				List<Profesor> profesores = servicioProfesor
						.buscarProfesorSinUsuario();
				ltbProfesor.setModel(new ListModelList<Profesor>(profesores));

			} else {
				List<Profesor> profesores = servicioProfesor.buscarActivos();
				ltbProfesor.setModel(new ListModelList<Profesor>(profesores));

			}

			Selectors.wireComponents(comp, this, false);
		}

	}

	/**
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 * 
	 * @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;
	}

	/**
	 * Metodo que permite filtrar los profesores disponibles, mediante el
	 * componente de la lista, donde se podra visualizar la cedula, nombre,
	 * apellido, correo y categoria.
	 */
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCorreoMostrarProfesor,#txtProgramaMostrarProfesor")
	public void filtrarDatosCatalogo() {
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Profesor> profesores2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesores) {
			if (profesor
					.getCedula()
					.toLowerCase()
					.contains(txtCedulaMostrarProfesor.getValue().toLowerCase())
					&& profesor
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCategoria()
							.getNombre()
							.toLowerCase()
							.contains(
									txtCategoriaMostrarProfesor.getValue()
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	/**
	 * Metodo que permite obtener el objeto Profesor al realizar el evento doble
	 * clic sobre un item en especifico en la lista, extrayendo asi su cedula,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VProfesor";

		} else {
			if (ltbProfesor.getItemCount() != 0) {
				Listitem listItem = ltbProfesor.getSelectedItem();
				Profesor profesorDatosCatalogo = (Profesor) listItem.getValue();

				HashMap<String, Object> map = new HashMap<String, Object>();
				HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
						.getCurrent().getAttribute("itemsCatalogo");

				if (map2 != null)
					map = map2;
				map.put("cedula", profesorDatosCatalogo.getCedula());
				String vista = vistaRecibida;
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoProfesor.onClose();
			}
		}
	}

	/** Metodo que permite cerrar la ventana correspondiente a las actividades */
	@Listen("onClick = #btnSalirCatalogoDirector")
	public void salirCatalogo() {
		wdwCatalogoDirectorPrograma.onClose();
	}

}
