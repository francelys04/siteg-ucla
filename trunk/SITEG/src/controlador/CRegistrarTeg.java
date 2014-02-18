package controlador;

import java.util.Date;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.Cronograma;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Metodo que permite formalizar el registro de un TEG por parte de un
 * estudiante
 */
@Controller
public class CRegistrarTeg extends CGeneral {

	private static long auxiliarId = 0;
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
	private Textbox txtDescripcionRegistrarTeg;
	@Wire
	private Textbox txtNombreTutorRegistrarTrabajo;
	@Wire
	private Textbox txtApellidoTutorRegistrarTrabajo;
	@Wire
	private Datebox dtbFechaInicioRegistrarTeg;
	@Wire
	private Datebox dtbFechaEntregaRegistrarTeg;
	@Wire
	private Spinner spnDuracionRegistrarTeg;
	@Wire
	private Listbox lsbEstudiantesRegistrarTeg;
	@Wire
	private Window wdwRegistrarTeg;
	@Wire
	private Button btnGuardarRegistrarTeg;
	@Wire
	private Button btnCancelarRegistrarTeg;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	public void inicializar(Component comp) {

		try {
			Estudiante estudiante = ObtenerUsuarioEstudiante();
			Programa programa = new Programa();
			programa = estudiante.getPrograma();

			Teg tegEstudiante = servicioTeg
					.buscarTegEstudiantePorEstatus(estudiante);
			if (tegEstudiante == null) {
				Messagebox
						.show("Para formalizar la inscripcion del Trabajo Especial de Grado deben estar finalizados los avances del mismo",
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
				txtNombreTutorRegistrarTrabajo.setValue(tegEstudiante
						.getTutor().getNombre());
				txtApellidoTutorRegistrarTrabajo.setValue(tegEstudiante
						.getTutor().getApellido());

				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegEstudiante);
				lsbEstudiantesRegistrarTeg
						.setModel(new ListModelList<Estudiante>(estudiantes));

				Lapso lapsoVigente = servicioLapso.buscarLapsoVigente();
				Actividad actividadFechaTeg = servicioActividad
						.buscarActividadPorNombre("Fecha de entrega del trabajo especial de grado");
				Cronograma cronogramaActividad = servicioCronograma
						.buscarCronogramaPorLapsoProgramaYActividad(programa,
								lapsoVigente, actividadFechaTeg);
				dtbFechaEntregaRegistrarTeg.setValue(cronogramaActividad
						.getFechaFin());
			}

		} catch (Exception e) {
			Messagebox.show("No tiene permisos para registrar un TEG",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
			salirRegistroTeg();
		}

	}

	/*
	 * Metodo que permite registrar un teg, cambiando el estado al teg en la
	 * base de datos y almacenando los datos en el historico de cambios de
	 * estatus del teg
	 */
	@Listen("onClick = #btnGuardarRegistrarTeg")
	public void guardarTeg() {

		if ((txtTituloRegistrarTeg.getText().compareTo("") == 0)
				|| (txtDescripcionRegistrarTeg.getText().compareTo("") == 0)
				|| (dtbFechaInicioRegistrarTeg.getText().compareTo("") == 0)
				|| (dtbFechaEntregaRegistrarTeg.getText().compareTo("") == 0)
				|| (spnDuracionRegistrarTeg.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show(
					"¿Desea guardar los datos del Trabajo Especial de Grado?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Teg tegRegistrado = servicioTeg
										.buscarTeg(auxiliarId);
								tegRegistrado.setTitulo(txtTituloRegistrarTeg
										.getValue());
								tegRegistrado
										.setDescripcion(txtDescripcionRegistrarTeg
												.getValue());
								tegRegistrado
										.setFechaInicio(dtbFechaInicioRegistrarTeg
												.getValue());
								tegRegistrado
										.setFechaEntrega(dtbFechaEntregaRegistrarTeg
												.getValue());
								tegRegistrado
										.setDuracion(spnDuracionRegistrarTeg
												.getValue());
								tegRegistrado.setEstatus("TEG Registrado");

								/* Guardar datos en la tabla teg_estatus */
								java.util.Date fechaEstatus = new Date();
								TegEstatus tegEstatus = new TegEstatus(0,
										tegRegistrado, "TEG Registrado",
										fechaEstatus);
								servicioTegEstatus.guardar(tegEstatus);

								servicioTeg.guardar(tegRegistrado);
								cancelarRegistroTeg();
								Messagebox
										.show("Trabajo Especial de Grado registrado exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
								wdwRegistrarTeg.onClose();

							}
						}
					});

		}

	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarRegistrarTeg")
	public void cancelarRegistroTeg() {
		txtDescripcionRegistrarTeg.setValue("");
		txtTituloRegistrarTeg.setValue("");
		dtbFechaInicioRegistrarTeg.setValue(null);
		dtbFechaEntregaRegistrarTeg.setValue(null);
		spnDuracionRegistrarTeg.setValue(null);
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirRegistrarTeg")
	public void salirRegistroTeg() {

		wdwRegistrarTeg.onClose();

	}

}