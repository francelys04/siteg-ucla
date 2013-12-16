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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
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

import servicio.SEstudiante;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

@Controller
public class CEstudiante extends CGeneral {
	
	CCatalogoEstudiante catalogo = new CCatalogoEstudiante();
	
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
	private Intbox itbTelefonoMovilEstudiante;
	@Wire
	private Intbox itbTelefonoFijoEstudiante;
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
				} else{
					rdoSexoFEstudiante.setChecked(true);
					}
				int TelefonoMovil= Integer.parseInt (estudiante.getTelefono());
				int TelefonoFijo = Integer.parseInt (estudiante.getTelefono_fijo());
				txtDireccionEstudiante.setValue(estudiante.getDireccion());
				itbTelefonoMovilEstudiante.setValue(TelefonoMovil);
				itbTelefonoFijoEstudiante.setValue(TelefonoFijo);
				txtCorreoEstudiante.setValue(estudiante.getCorreoElectronico());
				cmbProgramaEstudiante.setValue(estudiante.getPrograma()
						.getNombre());
				btnEliminarEstudiante.setDisabled(false);
				map.clear();
				map = null;
			}
		}

	}

	// Metodo que permite visualizar el catalogo, activado por el boton de
	// buscar
	@Listen("onClick = #btnCatalogoEstudiante")
	public void buscarEstudiante() {	
		
		Window window = (Window) Executions.createComponents("/vistas/catalogos/VCatalogoEstudiante.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VEstudiante");
		
	}
	// Metodo que permite tomar los datos de la vista y luego de llamar un
	// servicio almacenarlos en la base de datos
	@Listen("onClick = #btnGuardarEstudiante")
	public void guardarEstudiante() {
		if (txtCedulaEstudiante.getText().compareTo("") == 0
				|| txtNombreEstudiante.getText().compareTo("") == 0
				|| txtApellidoEstudiante.getText().compareTo("") == 0
				|| txtCorreoEstudiante.getText().compareTo("") == 0
				|| txtDireccionEstudiante.getText().compareTo("") == 0	
				|| itbTelefonoMovilEstudiante.getText().compareTo("")==0
				|| itbTelefonoFijoEstudiante.getText().compareTo("")==0
				|| cmbProgramaEstudiante.getText().compareTo("")==0
				|| rdoSexoFEstudiante.isChecked() == false
				&& rdoSexoMEstudiante.isChecked() == false
				) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("Desea guardar el estudiante?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								 String cedula = txtCedulaEstudiante.getValue();
								 String nombre = txtNombreEstudiante.getValue();
								 String apellido = txtApellidoEstudiante.getValue(); 
								 String correo = txtCorreoEstudiante.getValue();
								 String direccion = txtDireccionEstudiante.getValue();							 						 
								 String telefonoFijo = Integer.toString(itbTelefonoMovilEstudiante.getValue());
								 String telefonoMovil = Integer.toString(itbTelefonoFijoEstudiante.getValue());
								 String programas = cmbProgramaEstudiante.getValue();
								 String sexo;
								 if (rdoSexoFEstudiante.isChecked())
								 	sexo = "Femenino";
								 		else
								 			sexo = "Masculino";
								 Boolean estatus = true; 
								  
								 Programa programa = servicioPrograma.buscarPorNombrePrograma(programas);
								 
								 Estudiante estudiante = new Estudiante(cedula, nombre, apellido, correo, sexo, direccion, telefonoMovil, telefonoFijo, estatus, programa);
								 servicioEstudiante.guardar(estudiante);						
								 Messagebox.show("Estudiante registrado exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
								 cancelarEstudiante();
								 //id = 0;
								}
						}
					});

		}
	}
	// Metodo que buscar un objeto dado un codigo y lo elimina logicamente de la
	// base de datos
	@Listen("onClick = #btnEliminarEstudiante")
	public void eliminarEstudiante() {
	Messagebox.show("Desea eliminar el estudiante?",
			"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
			Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onOK")) {
						String cedula = txtCedulaEstudiante.getValue();
						Estudiante estudiante = servicioEstudiante.buscarEstudiante(cedula);
						estudiante.setEstatus(false);
						servicioEstudiante.guardar(estudiante);
						cancelarEstudiante();						
						 Messagebox.show("Estudiante eliminado exitosamente", "Informacion", Messagebox.OK,Messagebox.INFORMATION);
						 btnEliminarEstudiante.setDisabled(false);
					}
				}
			});
	
	}

	// Metodo que limpia los campos de la vista
	@Listen("onClick = #btnCancelarEstudiante")
	public void cancelarEstudiante() {
		
		txtCedulaEstudiante.setValue("");		
		txtNombreEstudiante.setValue("");		
		txtApellidoEstudiante.setValue("");				
		rdgSexoEstudiante.setSelectedItem(null);	
		txtDireccionEstudiante.setValue("");
		itbTelefonoMovilEstudiante.setValue(null);		
		itbTelefonoFijoEstudiante.setValue(null);
		txtCorreoEstudiante.setValue("");	    
		cmbProgramaEstudiante.setValue("");
		
		btnEliminarEstudiante.setDisabled(true);

	}
}