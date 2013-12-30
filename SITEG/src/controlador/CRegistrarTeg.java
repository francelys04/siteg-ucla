package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;

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
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SActividad;
import servicio.STeg;

@Controller
public class CRegistrarTeg extends CGeneral {

	SActividad servicioActividad = GeneradorBeans.getServicioActividad();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();

	@Wire
	private Textbox txtProgramaRegistraTeg;
	@Wire
	private Datebox dtbFechaTeg;
	@Wire
	private Textbox txtAreaRegistrarTeg;
	@Wire
	private Textbox txtTematicaRegistrarTeg;
	@Wire
	private Textbox txtTituloRegistrarTeg;
	@Wire
	private Textbox txtDescripciónRegistrarTeg;
	@Wire
	private Datebox dtbFechaInicioRegistrarTeg;
	@Wire
	private Datebox dtbFechaEntregaRegistrarTeg;
	@Wire
	private Spinner spnDuracionRegistrarTeg;
	@Wire
	private Textbox txtCedulaTutorRegistrarTeg;
	@Wire
	private Textbox txtNombreTutorRegistrarTeg;
	@Wire
	private Textbox txtApellidoTutorRegistrarTeg;
	@Wire
	private Listbox lsbEstudiantesRegistrarTeg;
	@Wire
	private Window wdwRegistrarTeg;
	@Wire
	private Button btnGuardarRegistrarTeg;
	@Wire
	private Button btnCancelarRegistrarTeg;
	private static long auxiliarId = 0;

	void inicializar(Component comp) {

		Estudiante estudiante = ObtenerUsuarioEstudiante();
		Programa programa = new Programa();
		programa = estudiante.getPrograma();

		Teg tegEstudiante = servicioTeg
				.buscarTegEstudiantePorEstatus(estudiante);
		

		try {
			if (tegEstudiante == null) {

				Messagebox
						.show("Para formalizar la inscripción del Trabajo Especial de Grado deben estar finalizados los avances del mismo",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
				wdwRegistrarTeg.onClose();

			} else {

				auxiliarId = tegEstudiante.getId();
				txtProgramaRegistraTeg.setValue(programa.getNombre());
				txtAreaRegistrarTeg.setValue(tegEstudiante.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaRegistrarTeg.setValue(tegEstudiante.getTematica()
						.getNombre());
				txtTituloRegistrarTeg.setValue(tegEstudiante.getTitulo());
				txtCedulaTutorRegistrarTeg.setValue(tegEstudiante.getTutor()
						.getCedula());
				txtNombreTutorRegistrarTeg.setValue(tegEstudiante.getTutor()
						.getNombre());
				txtApellidoTutorRegistrarTeg.setValue(tegEstudiante.getTutor()
						.getApellido());
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegEstudiante);
				lsbEstudiantesRegistrarTeg
						.setModel(new ListModelList<Estudiante>(estudiantes));

			}

		} catch (NullPointerException e) {

			System.out.println("NullPointerException");
		}

	}
	
	@Listen("onClick = #btnGuardarRegistrarTeg")
	public void guardarTeg(){
		
		if ((txtTituloRegistrarTeg.getText().compareTo("") == 0)
				|| (txtDescripciónRegistrarTeg.getText().compareTo("") == 0)
				|| (dtbFechaInicioRegistrarTeg.getText().compareTo("") == 0)
				|| (dtbFechaEntregaRegistrarTeg.getText().compareTo("") == 0)
				|| (spnDuracionRegistrarTeg.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		}else {
			Messagebox.show("¿Desea guardar los datos del Trabajo Especial de Grado?",
					"Dialogo de confirmación", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Teg tegRegistrado = servicioTeg.buscarTeg(auxiliarId);
								tegRegistrado.setTitulo(txtTituloRegistrarTeg.getValue());
								tegRegistrado.setDescripcion(txtDescripciónRegistrarTeg.getValue());
								tegRegistrado.setFechaInicio(dtbFechaInicioRegistrarTeg.getValue());
								tegRegistrado.setFechaEntrega(dtbFechaEntregaRegistrarTeg.getValue());
								tegRegistrado.setDuracion(spnDuracionRegistrarTeg.getValue());
								tegRegistrado.setEstatus("Teg Registrado");
								servicioTeg.guardar(tegRegistrado);
								cancelarRegistroTeg();
								Messagebox.show(
										"Trabajo Especial de Grado registrado exitosamente",
										"Información", Messagebox.OK,
										Messagebox.INFORMATION);
								wdwRegistrarTeg.onClose();
								
							}
						}
					});

		}
		
		
		
	}
	
	@Listen("onClick = #btnCancelarRegistrarTeg")
	public void cancelarRegistroTeg(){
		
		txtDescripciónRegistrarTeg.setValue("");
		txtTituloRegistrarTeg.setValue("");
		dtbFechaInicioRegistrarTeg.setValue(null);
		dtbFechaEntregaRegistrarTeg.setValue(null);
		spnDuracionRegistrarTeg.setValue(null);
		
		
		
		
	}
	
	
}