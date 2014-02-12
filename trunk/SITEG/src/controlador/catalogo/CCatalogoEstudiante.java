package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import modelo.Estudiante;
import modelo.Programa;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

import servicio.SEstudiante;
import servicio.SPrograma;

public class CCatalogoEstudiante extends CGeneral {

	List<Estudiante> estudiantes = new ArrayList();
	private static String vistaRecibida;
	private static boolean variable = false;
	public static List<Estudiante> estudiantes1;

	@Wire
	private Listbox ltbEstudiante;
	@Wire
	private Window wdwCatalogoEstudiante;
	@Wire
	private Textbox txtCedulaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarEstudiante;
	@Wire
	private Textbox txtApellidoMostrarEstudiante;
	@Wire
	private Textbox txtCorreoMostrarEstudiante;
	@Wire
	private Textbox txtProgramaMostrarEstudiante;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el map
	 * recibido del catalogo exista y se llenan las listas correspondientes de
	 * la vista dado una condicional, que si se cumple se mostrara los
	 * estudiantes sin usuarios sino todos los estudiantes activos.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = servicioEstudiante.buscarActivos();
		ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes));

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("usuario") != null) {
				variable = true;
				List<Estudiante> estudiantes1 = servicioEstudiante
						.buscarEstudianteSinUsuario();
				ltbEstudiante.setModel(new ListModelList<Estudiante>(
						estudiantes1));

			} else {
				List<Estudiante> estudiantes1 = servicioEstudiante
						.buscarActivos();
				ltbEstudiante.setModel(new ListModelList<Estudiante>(
						estudiantes1));

			}

			Selectors.wireComponents(comp, this, false);
		}

	}

	/*
	 * Metodo que permite filtrar los estudiantes disponibles, dado a la
	 * condicional de la variable booleana si es igual a "true" se mostraran los
	 * estudiantes sin usuario sino si es "false" seran todos los estudiantes
	 * activos, mediante el componente de la lista, donde se podra visualizar la
	 * cedula, nombre, apellido, correo y programa
	 */
	@Listen("onChange = #txtCedulaMostrarEstudiante,#txtNombreMostrarEstudiante,#txtApellidoMostrarEstudiante,#txtCorreoMostrarEstudiante,#txtProgramaMostrarEstudiante")
	public void filtrarDatosCatalogo() {

		if (variable == true) {
			estudiantes1 = servicioEstudiante.buscarEstudianteSinUsuario();

		} else {
			estudiantes1 = servicioEstudiante.buscarActivos();
		}
		List<Estudiante> estudiantes2 = new ArrayList<Estudiante>();

		for (Estudiante estudiante : estudiantes1) {
			if (estudiante
					.getCedula()
					.toLowerCase()
					.contains(
							txtCedulaMostrarEstudiante.getValue().toLowerCase())
					&& estudiante
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getPrograma()
							.getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarEstudiante.getValue()
											.toLowerCase())) {
				estudiantes2.add(estudiante);
			}

		}

		ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes2));

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/*
	 * Metodo que permite obtener el objeto Estudiante al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su cedula,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbEstudiante")
	public void mostrarDatosCatalogo() {

		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VEstudiante";

		} else {
			if (ltbEstudiante.getItemCount() != 0) {
				Listitem listItem = ltbEstudiante.getSelectedItem();
				Estudiante estudianteDatosCatalogo = (Estudiante) listItem
						.getValue();
				HashMap<String, Object> map = new HashMap<String, Object>();
				HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
						.getCurrent().getAttribute("itemsCatalogo");

				if (map2 != null)
					map = map2;

				map.put("cedula", estudianteDatosCatalogo.getCedula());
				String vista = vistaRecibida;
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoEstudiante.onClose();
			}
		}
	}

}
