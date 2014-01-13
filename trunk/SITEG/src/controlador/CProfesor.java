package controlador;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Categoria;

import modelo.Profesor;
import modelo.Programa;
import modelo.Tematica;
import modelo.Usuario;

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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SCategoria;
import servicio.SProfesor;
import servicio.SUsuario;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

public class CProfesor extends CGeneral {

	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SCategoria servicioCategoria = GeneradorBeans.getServicioCategoria();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	
	CCatalogoProfesor catalogo = new CCatalogoProfesor();
	@Wire
	private Combobox cmbCategoriaProfesor;
	@Wire
	private Combobox cmbProgramaProfesor;
	@Wire
	private Textbox txtCedulaProfesor;
	@Wire
	private Textbox txtNombreProfesor;
	@Wire
	private Textbox txtApellidoProfesor;
	@Wire
	private Textbox txtDireccionProfesor;
	@Wire
	private Textbox txtTelefonoMovilProfesor;
	@Wire
	private Textbox txtTelefonoFijoProfesor;
	@Wire
	private Textbox txtCorreoProfesor;
	@Wire
	private Radiogroup rdgSexoProfesor;
	@Wire
	private Radio rdoSexoFProfesor;
	@Wire
	private Radio rdoSexoMProfesor;
	@Wire
	private Listbox ltbProfesor;
	@Wire
	private Window wdwCatalogoProfesor;
	@Wire
	private Window wdwProfesor;
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
	@Wire Button btnEliminarProfesor;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Categoria> categorias = servicioCategoria.buscarCategoria();
		List<Programa> programas = servicioPrograma.buscarActivas();
		
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		if (cmbCategoriaProfesor == null) {
			ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
		} else {
			cmbCategoriaProfesor.setModel(new ListModelList<Categoria>(
					categorias));
		}
		
		cmbProgramaProfesor.setModel(new ListModelList<Programa>(programas));

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesor.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
				txtNombreProfesor.setValue(profesor.getNombre());
				txtApellidoProfesor.setValue(profesor.getApellido());

				if (profesor.getSexo().equals("Masculino")) {
					rdoSexoMProfesor.setChecked(true);
				} else
					rdoSexoFProfesor.setChecked(true);
			
				txtDireccionProfesor.setValue(profesor.getDireccion());
				txtTelefonoMovilProfesor.setValue(profesor.getTelefono_movil());
				txtTelefonoFijoProfesor.setValue(profesor.getTelefono_fijo());
				txtCorreoProfesor.setValue(profesor.getCorreoElectronico());
				cmbCategoriaProfesor.setValue(profesor.getCategoria()
						.getNombre());
				cmbProgramaProfesor.setValue(profesor.getPrograma().getNombre());
				btnEliminarProfesor.setDisabled(false);
				map.clear();
				map = null;
			}
		}
	}

	@Listen("onClick = #btnCatalogoProfesor")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
		window.doModal();

		catalogo.recibir("maestros/VProfesor");

	}

	@Listen("onClick = #btnGuardarProfesor")
	public void guardarProfesor() {

		if (txtCedulaProfesor.getText().compareTo("") == 0
				|| txtNombreProfesor.getText().compareTo("") == 0
				|| txtApellidoProfesor.getText().compareTo("") == 0
				|| txtCorreoProfesor.getText().compareTo("") == 0
				|| txtDireccionProfesor.getText().compareTo("") == 0
				|| txtTelefonoMovilProfesor.getText().compareTo("") == 0
				|| txtTelefonoFijoProfesor.getText().compareTo("") == 0
				|| cmbCategoriaProfesor.getText().compareTo("") == 0
				|| cmbProgramaProfesor.getText().compareTo("") == 0
				|| (rdoSexoFProfesor.isChecked() == false && rdoSexoMProfesor
						.isChecked() == false)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		}else{
		Messagebox.show("¿Desea guardar los datos del profesor?",
				"Dialogo de confirmación", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							String cedula = txtCedulaProfesor.getValue();
							String nombre = txtNombreProfesor.getValue();
							String apellido = txtApellidoProfesor.getValue();
							String sexo = rdgSexoProfesor.getSelectedItem()
									.getLabel();
							String direccion = txtDireccionProfesor.getValue();
							String telefonoMovil = txtTelefonoMovilProfesor
									.getValue();
							String telefonoFijo = txtTelefonoFijoProfesor
									.getValue();
							String correo = txtCorreoProfesor.getValue();
							String categorias = cmbCategoriaProfesor.getValue();
							String programas = cmbProgramaProfesor.getValue();
							Boolean estatus = true;
							Categoria categoria = servicioCategoria
									.buscarCategoriaPorNombre(categorias);
							Programa programa = servicioPrograma
							.buscarPorNombrePrograma(programas);
							Set<Tematica> tematicasProfesor = new HashSet<Tematica>();
							Usuario usuario = servicioUsuario.buscarUsuarioPorNombre("");
							Profesor profesor = new Profesor(cedula, nombre,
									apellido, correo, sexo, direccion,
									telefonoMovil, telefonoFijo, estatus,
									categoria, programa, tematicasProfesor, usuario);
							servicioProfesor.guardarProfesor(profesor);
							Messagebox.show(
									"Profesor registrado exitosamente",
									"Información", Messagebox.OK,
									Messagebox.INFORMATION);
							cancelarProfesor();

						}

					}
				});
		}
	}
		
		

	@Listen("onClick = #btnCancelarProfesor")
	public void cancelarProfesor() {

		txtCedulaProfesor.setConstraint("");
		txtCedulaProfesor.setValue("");
		txtCedulaProfesor.setConstraint("/.+[0-9]+/: Debe ingresar una cedula valida");
		txtNombreProfesor.setValue("");
		txtApellidoProfesor.setValue("");
		rdgSexoProfesor.setSelectedItem(null);
		txtDireccionProfesor.setValue("");
		txtTelefonoMovilProfesor.setConstraint("");
		txtTelefonoMovilProfesor.setValue("");
		txtTelefonoMovilProfesor.setConstraint("/.+[0-9]+/: Debe ingresar un telefono valido");
		txtTelefonoFijoProfesor.setConstraint("");
		txtTelefonoFijoProfesor.setValue("");
		txtTelefonoFijoProfesor.setConstraint("/.+[0-9]+/: Debe ingresar un telefono valido");
		txtCorreoProfesor.setValue("");
		cmbCategoriaProfesor.setValue("");
		cmbProgramaProfesor.setValue("");
		btnEliminarProfesor.setDisabled(true);
			
	}
	


	@Listen("onClick = #btnEliminarProfesor")
	public void eliminarProfesor() {
		
		Messagebox.show("¿Desea eliminar los datos del profesor?",
				"Dialogo de confirmación", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							String cedula = txtCedulaProfesor.getValue();
							Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedula);
							profesor.setEstatus(false);
							servicioProfesor.guardarProfesor(profesor);
							cancelarProfesor();
							Messagebox.show(
									"Profesor eliminado exitosamente",
									"Información", Messagebox.OK,
									Messagebox.INFORMATION);
						}
					}
				});
		
	}

}
