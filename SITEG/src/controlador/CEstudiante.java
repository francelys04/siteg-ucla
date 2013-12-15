package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Programa;

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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

@Controller
public class CEstudiante extends CGeneral {
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

	// Metodo heredado del controlador CGeneral que permite inicializar los
	// componentes de zk
	// asi como tambien permite settear los atributos a los campos luego de
	// seleccionar desde el catalogo
	@Override
	void inicializar(Component comp) {

		List<Programa> programas = servicioPrograma.buscarActivas();
		List<Estudiante> estudiantes = servicioEstudiante.buscarActivos();

		if (cmbProgramaEstudiante == null) {
			ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes));
		} else {
			cmbProgramaEstudiante.setModel(new ListModelList<Programa>(
					programas));
		}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaEstudiante.setValue((String) map.get("cedula"));
				Estudiante estudiante = servicioEstudiante
						.buscarEstudiante(txtCedulaEstudiante.getValue());
				txtNombreEstudiante.setValue(estudiante.getNombre());
				txtApellidoEstudiante.setValue(estudiante.getApellido());

				if (estudiante.getSexo() == "Masculino") {
					rdoSexoMEstudiante.setChecked(true);
				} else
					rdoSexoFEstudiante.setChecked(true);
				txtDireccionEstudiante.setValue(estudiante.getDireccion());
				txtTelefonoMovilEstudiante.setValue(estudiante.getTelefono());
				txtTelefonoFijoEstudiante.setValue(estudiante
						.getTelefono_fijo());
				txtCorreoEstudiante.setValue(estudiante.getCorreoElectronico());
				cmbProgramaEstudiante.setValue(estudiante.getPrograma()
						.getNombre());
				map.clear();
				map = null;
			}
		}

	}

	// Metodo que permite visualizar el catalogo, activado por el boton de
	// buscar
	@Listen("onClick = #btnCatalogoEstudiante")
	public void buscarEstudiante() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoEstudiante.zul", null, null);
		window.doModal();

	}

	// Metodo que permite tomar los datos de la vista y luego de llamar un
	// servicio almacenarlos en la base de datos
	@Listen("onClick = #btnGuardarEstudiante")
	public void guardarEstudiante() {

		String cedula = txtCedulaEstudiante.getValue();
		String nombre = txtNombreEstudiante.getValue();
		String apellido = txtApellidoEstudiante.getValue();
		String sexo;
		if (rdoSexoFEstudiante.isChecked())
			sexo = "Femenino";
		else
			sexo = "Masculino";
		String direccion = txtDireccionEstudiante.getValue();
		String telefonoMovil = txtTelefonoMovilEstudiante.getValue();
		String telefonoFijo = txtTelefonoFijoEstudiante.getValue();
		String correo = txtCorreoEstudiante.getValue();
		String programas = cmbProgramaEstudiante.getValue();
		Boolean estatus = true;
		Programa programa = servicioPrograma.buscarPorNombrePrograma(programas);

		Estudiante estudiante = new Estudiante(cedula, nombre, apellido,
				correo, sexo, direccion, telefonoMovil, telefonoFijo, estatus,
				programa);
		servicioEstudiante.guardar(estudiante);
		cancelarEstudiante();
		System.out.println("Estudiante Guardado");
	}

	// Metodo que buscar un objeto dado un codigo y lo elimina logicamente de la
	// base de datos
	@Listen("onClick = #btnEliminarEstudiante")
	public void eliminarEstudiante() {
		System.out.println("Estudiante Eliminado");
		String cedula = txtCedulaEstudiante.getValue();
		Estudiante estudiante = servicioEstudiante.buscarEstudiante(cedula);
		estudiante.setEstatus(false);
		servicioEstudiante.guardar(estudiante);
		cancelarEstudiante();
		System.out.println("Estudiante Eliminado");
	}

	// Metodo que limpia los campos de la vista
	@Listen("onClick = #btnCancelarEstudiante")
	public void cancelarEstudiante() {

		txtCedulaEstudiante.setValue("");
		txtNombreEstudiante.setValue("");
		txtApellidoEstudiante.setValue("");
		rdgSexoEstudiante.setSelectedItem(null);
		txtDireccionEstudiante.setValue("");
		txtTelefonoMovilEstudiante.setValue("");
		txtTelefonoFijoEstudiante.setValue("");
		txtCorreoEstudiante.setValue("");
		cmbProgramaEstudiante.setValue("");

	}

	// Metodo que permite el filtrado en el catalogo, por medio de los
	// diferentes campos que este posee
	@Listen("onChange = #txtCedulaMostrarEstudiante,#txtNombreMostrarEstudiante,#txtApellidoMostrarEstudiante,#txtCorreoMostrarEstudiante,#txtProgramaMostrarEstudiante")
	public void filtrarDatosCatalogo() {
		List<Estudiante> estudiantes1 = servicioEstudiante.buscarActivos();
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

	// Metodo que luego de presionar doble click sobre una fila del catalogo
	// almacena los datos en un mapa, para luego colocarlos en la vista
	// correspondiente
	@Listen("onDoubleClick = #ltbEstudiante")
	public void mostrarDatosCatalogo() {

		Listitem listItem = ltbEstudiante.getSelectedItem();
		Estudiante estudianteDatosCatalogo = (Estudiante) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cedula", estudianteDatosCatalogo.getCedula());
		String vista = "maestros/VActividad";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoEstudiante.onClose();

	}
}