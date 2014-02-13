package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Profesor;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SProfesor;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CCatalogoProfesor extends CGeneral {

	private static String vistaRecibida;
	public static List<Profesor> profesores;
	public static boolean variable2 = false;
	public static boolean esDirector = false;

	@Wire
	private Listbox ltbProfesor;
	@Wire
	private Window wdwCatalogoProfesor;
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

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el map
	 * recibido del catalogo exista y se llenan las listas correspondientes de
	 * la vista dado una condicional, que si se cumple se mostrara los
	 * profesores sin usuarios sino todos los profesores activos y sino todos los
	 * profesores activos.
	 */
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			System.out.println("mapa");
			if (map.get("usuario") != null) {
				System.out.println("usuario" + map.toString());
				List<Profesor> profesores1 = servicioProfesor
						.buscarProfesorSinUsuario();
				ltbProfesor.setModel(new ListModelList<Profesor>(profesores1));
				variable2 = true;

			}

			if (map.get("director") != null) {
				System.out.println("director" + map.toString());
				esDirector = true;
				List<Profesor> profesores1 = servicioProfesor
						.buscarProfesoresSinPrograma();
				ltbProfesor.setModel(new ListModelList<Profesor>(profesores1));
			}

		} else {
			List<Profesor> profesores1 = servicioProfesor.buscarActivos();
			ltbProfesor.setModel(new ListModelList<Profesor>(profesores1));

		}
		Selectors.wireComponents(comp, this, false);
	}
	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;
	}

	/*
	 * Metodo que permite filtrar los profesores disponibles, dado a la
	 * condicional de la variable booleana "variable" si es igual a "true" se
	 * mostraran los profesores sin usuario, si la variable booleana
	 * "esDirector" es igual a "true" se mostrara los profesores sin programa,
	 * sino si es "false" seran todos los profesores activos, mediante el
	 * componente de la lista, donde se podra visualizar la cedula, nombre,
	 * apellido, correo y categoria
	 */
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCategoriaMostrarProfesor,#txtCorreoMostrarProfesor")
	public void filtrarDatosCatalogo() {
		if (variable2 == true) {
			profesores = servicioProfesor.buscarProfesorSinUsuario();
		} else {
			if (esDirector) {
				profesores = servicioProfesor.buscarProfesoresSinPrograma();
			} else {
				profesores = servicioProfesor.buscarActivos();
			}
		}
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

	/*
	 * Metodo que permite obtener el objeto Profesor al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su cedula,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		try {
			if (vistaRecibida == null) {

				vistaRecibida = "maestros/VProfesor";

			} else {
				if (ltbProfesor.getSelectedCount() != 0) {
					Listitem listItem = ltbProfesor.getSelectedItem();
					Profesor profesorDatosCatalogo = (Profesor) listItem
							.getValue();

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
		} catch (NullPointerException e) {
		}

	}

}
