package controlador;

import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.seguridad.Usuario;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoEstudiante;

/**Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Estudiante*/
@Controller
public class CEstudiante extends CGeneral {

	private static final long serialVersionUID = 4825869502092751536L;
	CCatalogoEstudiante catalogo = new CCatalogoEstudiante();

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
	@Wire
	private Button btnEliminarEstudiante;
	@Wire
	private Window wdwEstudiante;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {

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

				if (estudiante.getSexo().equals("Masculino")) {
					rdoSexoMEstudiante.setChecked(true);
				} else {
					rdoSexoFEstudiante.setChecked(true);
				}
				txtDireccionEstudiante.setValue(estudiante.getDireccion());
				txtTelefonoMovilEstudiante.setValue(estudiante.getTelefono());
				txtTelefonoFijoEstudiante.setValue(estudiante
						.getTelefono_fijo());
				txtCorreoEstudiante.setValue(estudiante.getCorreoElectronico());
				cmbProgramaEstudiante.setValue(estudiante.getPrograma()
						.getNombre());
				btnEliminarEstudiante.setDisabled(false);
				map.clear();
				map = null;
			}
		}
		txtCorreoEstudiante
				.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un correo como: ejemplo@ejemplo.com");
	}

	/**
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoEstudiante")
	public void buscarEstudiante() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoEstudiante.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VEstudiante");

	}

	/**
	 * Metodo que permite el guardado o modificacion de una entidad Estudiante,
	 * asi como la verificacion y guardado de su respectivo objeto usuario
	 */
	@Listen("onClick = #btnGuardarEstudiante")
	public void guardarEstudiante() {

		if (cmbProgramaEstudiante.getText().compareTo("") == 0
				|| txtCedulaEstudiante.getText().compareTo("") == 0
				|| txtNombreEstudiante.getText().compareTo("") == 0
				|| txtApellidoEstudiante.getText().compareTo("") == 0
				|| txtCorreoEstudiante.getText().compareTo("") == 0
				|| txtDireccionEstudiante.getText().compareTo("") == 0
				|| txtTelefonoMovilEstudiante.getText().compareTo("") == 0
				|| txtTelefonoFijoEstudiante.getText().compareTo("") == 0
				|| (rdoSexoFEstudiante.isChecked() == false && rdoSexoMEstudiante
						.isChecked() == false)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("¿Desea guardar datos del estudiante?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String cedula = txtCedulaEstudiante.getValue();
								String nombre = txtNombreEstudiante.getValue();
								String apellido = txtApellidoEstudiante
										.getValue();
								String correo = txtCorreoEstudiante.getValue();
								String direccion = txtDireccionEstudiante
										.getValue();
								String telefonoFijo = txtTelefonoFijoEstudiante
										.getValue();
								String telefonoMovil = txtTelefonoMovilEstudiante
										.getValue();
								String programas = cmbProgramaEstudiante
										.getValue();
								String sexo = rdgSexoEstudiante
										.getSelectedItem().getLabel();
								Boolean estatus = true;

								Programa programa = servicioPrograma
										.buscarPorNombrePrograma(programas);

								Usuario usuario = servicioUsuario
										.buscarUsuarioPorNombre(cedula);
								Estudiante estudiante = new Estudiante(cedula,
										nombre, apellido, correo, sexo,
										direccion, telefonoMovil, telefonoFijo,
										estatus, programa, usuario);
								servicioEstudiante.guardar(estudiante);
								Messagebox.show(
										"Estudiante registrado exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
								cancelarEstudiante();
							}
						}
					});

		}
	}

	/** Metodo que permite la eliminacion logica de una entidad Estudiante */
	@Listen("onClick = #btnEliminarEstudiante")
	public void eliminarEstudiante() {
		String cedula = txtCedulaEstudiante.getValue();
		final Estudiante estudiante = servicioEstudiante
				.buscarEstudiante(cedula);
		List tegsNoCulminados = servicioTeg
				.buscarTegNoCulminadosEstudiante(estudiante);
		List solNoCulminadas = servicioSolicitudTutoria
				.buscarSolicitudesTutoriaNoCulminadasEstudiante(estudiante);
		if (tegsNoCulminados.size() == 0 && solNoCulminadas.size() == 0) {
			Messagebox.show("¿Desea eliminar los datos del estudiante?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								estudiante.setEstatus(false);
								servicioEstudiante.guardar(estudiante);
								cancelarEstudiante();
								Messagebox.show(
										"Estudiante eliminado exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
								btnEliminarEstudiante.setDisabled(false);
							}
						}
					});
		} else if (tegsNoCulminados.size() != 0) {
			Messagebox
					.show("Este estudiante no puede ser eliminado no ha culminado el TEG",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
		} else if (solNoCulminadas.size() != 0) {
			Messagebox
					.show("Este estudiante no puede ser eliminado ya que posee solicitudes de tutoria no cerradas",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
		}
	}

	/**
	 * Metodo que permite limpiar los campos de la vista
	 */
	@Listen("onClick = #btnCancelarEstudiante")
	public void cancelarEstudiante() {
		cmbProgramaEstudiante.setValue("");
		txtCedulaEstudiante.setConstraint("");
		txtCedulaEstudiante.setValue("");
		txtCedulaEstudiante
				.setConstraint("/.+[0-9]+/: Debe ingresar una cedula como: 19482714");
		txtNombreEstudiante.setValue("");
		txtApellidoEstudiante.setValue("");
		rdgSexoEstudiante.setSelectedItem(null);
		txtDireccionEstudiante.setValue("");
		txtTelefonoMovilEstudiante.setConstraint("");
		txtTelefonoMovilEstudiante.setValue("");
		txtTelefonoMovilEstudiante
				.setConstraint("/.+[0-9]+/: Debe ingresar un telefono como: 04264518973");
		txtTelefonoFijoEstudiante.setConstraint("");
		txtTelefonoFijoEstudiante.setValue("");
		txtTelefonoFijoEstudiante
				.setConstraint("/.+[0-9]+/: Debe ingresar un telefono como: 02512521309" );
		txtCorreoEstudiante.setConstraint("");
		txtCorreoEstudiante.setValue("");
		txtCorreoEstudiante
				.setConstraint("/.+@.+\\.[a-z]+/: Debe ingresar un correo como: ejemplo@ejemplo.com");
		btnEliminarEstudiante.setDisabled(true);

	}

	/** Metodo que permite cerrar la ventana correspondiente a los estudiantes */
	@Listen("onClick = #btnSalirEstudiante")
	public void salirEstudiante() {
		wdwEstudiante.onClose();
	}
	
	
	
	/**
	 * Metodo que permite buscar si un estudiante existe, de acuerdo al numero de
	 * cedula del estudiante
	 */
	@Listen("onChange = #txtCedulaEstudiante")
	public void buscarCedulaEstudiante() {
		Estudiante estudiante = servicioEstudiante.buscarEstudiante(txtCedulaEstudiante.getValue());
		if (estudiante != null) {
			
			cmbProgramaEstudiante.setValue(estudiante.getPrograma().getNombre());
			txtCedulaEstudiante.setValue(estudiante.getCedula());
			txtNombreEstudiante.setValue(estudiante.getNombre());
			txtApellidoEstudiante.setValue(estudiante.getApellido());
			
			if (estudiante.getSexo().equals("Masculino")) {
				rdoSexoMEstudiante.setChecked(true);
			} else
				rdoSexoFEstudiante.setChecked(true);
			
			txtDireccionEstudiante.setValue(estudiante.getDireccion());
			txtTelefonoMovilEstudiante.setValue(estudiante.getTelefono());
			txtTelefonoFijoEstudiante.setValue(estudiante.getTelefono_fijo());
			txtCorreoEstudiante.setValue(estudiante.getCorreoElectronico());
			btnEliminarEstudiante.setDisabled(false);
		}

	}

	
	

}