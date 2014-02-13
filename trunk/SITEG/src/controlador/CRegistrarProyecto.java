package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;

import modelo.SolicitudTutoria;
import modelo.Tematica;

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

import servicio.SEstudiante;

import servicio.SPrograma;
import servicio.SAreaInvestigacion;
import servicio.STematica;
import servicio.STeg;
import servicio.SProfesor;
import servicio.SSolicitudTutoria;
import servicio.seguridad.SUsuario;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CRegistrarProyecto extends CGeneral {

	@Wire
	private Textbox txtProgramaRegistrarProyecto;
	@Wire
	private Datebox dbfechaRegistrarProyecto;
	@Wire
	private Textbox txtAreaRegistrarProyecto;
	@Wire
	private Textbox txtTematicaRegistrarProyecto;
	@Wire
	private Textbox txtTituloRegistrarProyecto;
	@Wire
	private Textbox txtCedulaTutorRegistrarProyecto;
	@Wire
	private Textbox txtNombreTutorRegistrarProyecto;
	@Wire
	private Textbox txtApellidoTutorRegistrarProyecto;
	@Wire
	private Window wdwRegistrarProyecto;
	@Wire
	private Listbox lsbEstudiantesRegistrarProyecto;
	private static long idTem;
	private String idProf;
	private static long id;

	public void inicializar(Component comp) {
		try {
		Estudiante estudiante = ObtenerUsuarioEstudiante();
		Programa programa = estudiante.getPrograma();

		SolicitudTutoria solicitudAceptada = servicioSolicitudTutoria
				.buscarSolicitudAceptadaEstudiante(estudiante);

		List<Teg> tegEstudiante = servicioTeg
				.buscarTegPorEstudiante(estudiante);
		// Permite verificar los estatus del teg para asi
		// validar si este puede registrarlo de ser asi
		// llena los datos en la vista
		
			if (solicitudAceptada != null) {

				idTem = solicitudAceptada.getTematica().getId();
				idProf = solicitudAceptada.getProfesor().getCedula();

				if (tegEstudiante.size() != 0) {

					for (int i = 0; i < tegEstudiante.size(); i++) {
						String teg = tegEstudiante.get(i).getEstatus();
						if (teg != "Proyecto No Factible") {

							Messagebox.show("Ya posee un proyecto registrado",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);
							wdwRegistrarProyecto.onClose();
						} else {

							txtProgramaRegistrarProyecto.setValue(programa
									.getNombre());
							txtAreaRegistrarProyecto.setValue(solicitudAceptada
									.getTematica().getareaInvestigacion()
									.getNombre());
							txtTematicaRegistrarProyecto
									.setValue(solicitudAceptada.getTematica()
											.getNombre());
							txtTituloRegistrarProyecto
									.setValue(solicitudAceptada
											.getDescripcion());
							txtNombreTutorRegistrarProyecto
									.setValue(solicitudAceptada.getProfesor()
											.getNombre());
							txtApellidoTutorRegistrarProyecto
									.setValue(solicitudAceptada.getProfesor()
											.getApellido());

							List<Estudiante> estudiantes = servicioEstudiante
									.buscarSolicitudesEstudiante(solicitudAceptada);
							lsbEstudiantesRegistrarProyecto
									.setModel(new ListModelList<Estudiante>(
											estudiantes));

						}
					}
				} else {

					txtProgramaRegistrarProyecto.setValue(programa.getNombre());
					txtAreaRegistrarProyecto.setValue(solicitudAceptada
							.getTematica().getareaInvestigacion().getNombre());
					txtTematicaRegistrarProyecto.setValue(solicitudAceptada
							.getTematica().getNombre());
					txtTituloRegistrarProyecto.setValue(solicitudAceptada
							.getDescripcion());
					txtNombreTutorRegistrarProyecto.setValue(solicitudAceptada
							.getProfesor().getNombre());
					txtApellidoTutorRegistrarProyecto
							.setValue(solicitudAceptada.getProfesor()
									.getApellido());
					List<Estudiante> estudiantes = servicioEstudiante
							.buscarSolicitudesEstudiante(solicitudAceptada);
					lsbEstudiantesRegistrarProyecto
							.setModel(new ListModelList<Estudiante>(estudiantes));
				}

			} else {

				Messagebox
						.show("Para registrar un proyecto su solicitud de tutoria debe ser aceptada",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
				wdwRegistrarProyecto.onClose();

			}

		} catch (Exception e) {
			// TODO: handle exception
						Messagebox.show("No tiene permisos para registrar un proyecto",
						"Advertencia", Messagebox.OK,
						Messagebox.EXCLAMATION);
						salirRegistroProyecto();
		}

	}

	@Listen("onClick = #btnGuardarRegistrarProyecto")
	public void guardarProyecto() {

		if ((txtTituloRegistrarProyecto.getText().compareTo("") == 0)
				|| (txtProgramaRegistrarProyecto.getText().compareTo("") == 0)
				|| (txtAreaRegistrarProyecto.getText().compareTo("") == 0)
				|| (txtTematicaRegistrarProyecto.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox
					.show("¿Desea registrar el proyecto de trabajo especial de grado?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {

										Date fecha = dbfechaRegistrarProyecto
												.getValue();
										String titulo = txtTituloRegistrarProyecto
												.getValue();
										Tematica tematica = servicioTematica
												.buscarTematica(idTem);
										Profesor tutor = servicioProfesor
												.buscarProfesorPorCedula(idProf);
										String descripcion = null;
										long duracion = 0;
										String estatus = "Solicitando Registro";
										Date fechaEntrega = null;
										Date fechaInicio = null;
										Set<Profesor> profesores = null;
										Estudiante estudiante = ObtenerUsuarioEstudiante();
										SolicitudTutoria solicitudAceptada = servicioSolicitudTutoria
												.buscarSolicitudAceptadaEstudiante(estudiante);
										List<Estudiante> estudiantesSolicitud = servicioEstudiante
												.buscarSolicitudesEstudiante(solicitudAceptada);
										Set<Estudiante> estudiantesPorSolicitud = new HashSet<Estudiante>();
										for (int i = 0; i < estudiantesSolicitud
												.size(); i++) {
											Estudiante estudiantes = estudiantesSolicitud
													.get(i);
											estudiantesPorSolicitud
													.add(estudiantes);
										}

										Teg proyecto = new Teg(id, titulo,
												fecha, fechaInicio,
												fechaEntrega, descripcion,
												duracion, tutor, estatus,
												tematica, profesores,
												estudiantesPorSolicitud);
										servicioTeg.guardar(proyecto);
										id = 0;

										// Guardar datos en la tabla teg_estatus
										java.util.Date fechaEstatus = new Date();
										Teg ultimoTeg = servicioTeg
												.buscarUltimoTeg();
										TegEstatus tegEstatus = new TegEstatus(
												0, ultimoTeg,
												"Solicitando Registro",
												fechaEstatus);
										servicioTegEstatus.guardar(tegEstatus);

										cancelarRegistroProyecto();
										Messagebox
												.show("Proyecto de trabajo especial de grado registrado exitosamente",
														"Informacion",
														Messagebox.OK,
														Messagebox.INFORMATION);
										wdwRegistrarProyecto.onClose();

									}
								}
							});

		}

	}

	// limpia los datos que son modificables en la vista
	@Listen("onClick = #btnCancelarRegistrarProyecto")
	public void cancelarRegistroProyecto() {

		txtTituloRegistrarProyecto.setValue("");

	}

	@Listen("onClick = #btnSalirRegistrarProyecto")
	public void salirRegistroProyecto() {

		wdwRegistrarProyecto.onClose();

	}

}
