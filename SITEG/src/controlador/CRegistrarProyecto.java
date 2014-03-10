package controlador;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.Tematica;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@Controller
public class CRegistrarProyecto extends CGeneral {

	private static final long serialVersionUID = 4360418886488972705L;
	private static long idTem;
	private String idProf;
	private static long id;

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

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		
			Estudiante estudiante = ObtenerUsuarioEstudiante();
			if(estudiante !=null){
			Programa programa = estudiante.getPrograma();

			SolicitudTutoria solicitudAceptada = servicioSolicitudTutoria
					.buscarSolicitudAceptadaEstudiante(estudiante);
//			SolicitudTutoria solicitudFinalizada = servicioSolicitudTutoria.buscarFinalizada(estudiante);
//			List<Teg> tegEstudiante = servicioTeg
//					.buscarTegPorEstudiante(estudiante);
			if (solicitudAceptada != null ) {
//				if(solicitudFinalizada != null)
//					solicitudAceptada = solicitudFinalizada;
				idTem = solicitudAceptada.getTematica().getId();
				idProf = solicitudAceptada.getProfesor().getCedula();
				Teg ultimoTeg = servicioTeg.ultimoTeg(estudiante);
				if (ultimoTeg!=null) {
//					for (int i = 0; i < tegEstudiante.size(); i++) {
//						String teg = tegEstudiante.get(i).getEstatus();
					System.out.println(ultimoTeg.getEstatus()+ultimoTeg.getId());
						if (!ultimoTeg.getEstatus().equals("Proyecto No Factible") && !ultimoTeg.getEstatus().equals("TEG Reprobado")) {

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
//					}
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
			}else{
			Messagebox.show("No tiene permisos para registrar un proyecto",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
			salirRegistroProyecto();
			}

	}

	/*
	 * Metodo que permite registrar un proyecto especial de grado por
	 * determinado estudiante, ademas de que se registra un cambio en el
	 * historal del teg
	 */
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
							new org.zkoss.zk.ui.event.EventListener<Event>() {
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

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarRegistrarProyecto")
	public void cancelarRegistroProyecto() {
		txtTituloRegistrarProyecto.setValue("");
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirRegistrarProyecto")
	public void salirRegistroProyecto() {
		wdwRegistrarProyecto.onClose();
	}
}
