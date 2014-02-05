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

	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	@Wire
	private Combobox cmbProgramaEstudiante;
	@Wire
	private Textbox txtCedulaEstudiante;
	@Wire
	private Textbox txtNombreEstudiante;
	@Wire
	private Textbox txtApellidoEstudiante;
	@Wire
	private Radiogroup rdgSexoEstudiante;
	@Wire
	private Radio rdoSexoFEstudiante;
	@Wire
	private Radio rdoSexoMEstudiante;
	@Wire
	private Textbox txtDireccionEstudiante;
	@Wire
	private Textbox txtTelefonoMovilEstudiante;
	@Wire
	private Textbox txtTelefonoFijoEstudiante;
	@Wire
	private Textbox txtCorreoEstudiante;

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
	private static String vistaRecibida;
	private static boolean variable = false;
	public static  List<Estudiante> estudiantes1;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		//llena el catalogo con los datos del estudiante
		List<Programa> programas = servicioPrograma.buscarActivas();

		
		if (cmbProgramaEstudiante == null) {
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarActivos();
			ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes));
		} else {
			cmbProgramaEstudiante.setModel(new ListModelList<Programa>(
					programas));
		}
		 
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("usuario") != null) {
				variable= true;
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudianteSinUsuario();
				ltbEstudiante.setModel(new ListModelList<Estudiante>(
						estudiantes));

			} else {
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarActivos();
				ltbEstudiante.setModel(new ListModelList<Estudiante>(
						estudiantes));

			}

			Selectors.wireComponents(comp, this, false);
		}

	}

	// Metodo que permite el filtrado en el catalogo, por medio de los
	// diferentes campos que este posee
	@Listen("onChange = #txtCedulaMostrarEstudiante,#txtNombreMostrarEstudiante,#txtApellidoMostrarEstudiante,#txtCorreoMostrarEstudiante,#txtProgramaMostrarEstudiante")
	public void filtrarDatosCatalogo() {
		
		if (variable == true)
		{
			 estudiantes1 = servicioEstudiante
			.buscarEstudianteSinUsuario();
			
		}
		else
		{
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

	// Metodo que luego de presionar doble click sobre una fila del catalogo
	// almacena los datos en un mapa, para luego colocarlos en la vista
	// correspondiente
	@Listen("onDoubleClick = #ltbEstudiante")
	public void mostrarDatosCatalogo() {
		
		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VEstudiante";

		} else {
		if(ltbEstudiante.getItemCount()!=0){
		Listitem listItem = ltbEstudiante.getSelectedItem();
		Estudiante estudianteDatosCatalogo = (Estudiante) listItem.getValue();
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

	/**
	 * Metodo para recibir la vista a la que va dirigida el catalogo
	 * 
	 * @date 15-12-2013
	 */

}
