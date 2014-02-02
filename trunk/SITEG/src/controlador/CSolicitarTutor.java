package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.Categoria;
import modelo.CondicionPrograma;
import modelo.Estudiante;
import modelo.ProgramaArea;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.TipoJurado;

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
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SAreaInvestigacion;
import servicio.SProfesor;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

//es un controlador de Solicitar Tutor
@Controller
public class CSolicitarTutor extends CGeneral {

	private static final int Estudiante = 0;
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	SAreaInvestigacion servicioAreaInvestigacion = GeneradorBeans
			.getServicioArea();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SSolicitudTutoria servicioSolicitarTutor = GeneradorBeans
			.getServicioTutoria();
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();

	@Wire
	private Datebox db1;
	@Wire
	private Button btnAgregarEstudiante;
	@Wire
	private Listbox ltbEstudiantes;

	// atributos del Proyecto
	@Wire
	private Combobox cmbProgramaSolicitud;
	@Wire
	private Combobox cmbAreaSolicitud;
	@Wire
	private Combobox cmbTematicaSolicitud;
	@Wire
	private Textbox txtTituloSolicitud;

	// atributos del Estudiante
	@Wire
	private Textbox txtCedulaEstudiante;

	// atributos del Tutor
	@Wire
	private Textbox txtCedulaProfesor;
	@Wire
	private Textbox txtNombreProfesor;
	@Wire
	private Textbox txtApellidoProfesor;
	@Wire
	private Textbox txtCorreoProfesor;

	// atributos de la pantalla del catalogo
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
	@Wire
	private Window wdwSolicitarTutoria;
	@Wire
	private Window wdwCatalogoProfesorArea;

	private long id;
	private static long valor;
	public static String combo;
	public static String combo1;
	public static String combo2;
	public static String descripcion;
	public static long idcombo;
	public static String estado;
	public static String programa;
	public static String area;
	public static String tematica;
	public static String titulo;
	public static String cedulaProfesor;
	public static Date fecha;

	private static List<Estudiante> EstudiantesColocados = new ArrayList<Estudiante>();

	List<Estudiante> gridEstudiante = new ArrayList<Estudiante>();

	// metodo para mapear los datos del Tutor
	void inicializar(Component comp) {

		List<Programa> programas = servicioPrograma.buscarActivas();

		if (combo == null) {
			cmbProgramaSolicitud
					.setModel(new ListModelList<Programa>(programas));

		} else {
			cmbProgramaSolicitud.setValue(combo);
			combo = null;
		}

		if (combo1 == null) {
			cmbAreaSolicitud.setDisabled(true);
		} else {
			cmbAreaSolicitud.setValue(combo1);
			combo1 = null;
		}

		if (combo2 == null) {
			cmbTematicaSolicitud.setDisabled(true);
		} else {
			cmbTematicaSolicitud.setValue(combo2);
			combo2 = null;

		}

		if (descripcion != null) {

			txtTituloSolicitud.setValue(descripcion);
			descripcion = null;
		}

		if (EstudiantesColocados.size() != 0) {
			for (int i = 0; i < EstudiantesColocados.size(); i++) {

				ltbEstudiantes.setModel(new ListModelList<Estudiante>(
						EstudiantesColocados));
			}
			EstudiantesColocados.clear();

		}

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesor.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
				txtNombreProfesor.setValue(profesor.getNombre());
				txtApellidoProfesor.setValue(profesor.getApellido());
				txtCorreoProfesor.setValue(profesor.getCorreoElectronico());
			}
			map.clear();
			map = null;
		}
	}

	public void limpiarDatosEstudiante() {

		txtCedulaEstudiante.setValue("");

	}

	// se activa el combo areas si selecciona un programa
	// obteniendo el programa seleccionado
	@Listen("onSelect = #cmbProgramaSolicitud")
	public void areaSolicitud() {
		cmbAreaSolicitud.setValue("");
		cmbTematicaSolicitud.setValue("");

		cmbAreaSolicitud.setDisabled(false);

		String programa = cmbProgramaSolicitud.getValue();
		Programa programa2 = servicioPrograma.buscarPorNombrePrograma(programa);

		List<AreaInvestigacion> a = servicioProgramaArea
				.buscarAreasDePrograma(programa2);
		cmbAreaSolicitud.setModel(new ListModelList<AreaInvestigacion>(a));
		idcombo = Long
				.parseLong(cmbProgramaSolicitud.getSelectedItem().getId());

	}

	// se activa el combo tematicas si selecciona un area
	// obteniendo el area seleccionada
	@Listen("onSelect = #cmbAreaSolicitud")
	public void tematicaSolicitud() {

		cmbTematicaSolicitud.setDisabled(false);

		String area = cmbAreaSolicitud.getValue();
		AreaInvestigacion area2 = servicioAreaInvestigacion
				.buscarAreaPorNombre(area);

		List<Tematica> tematicas = servicioTematica
				.buscarTematicasDeArea(area2);
		cmbTematicaSolicitud.setModel(new ListModelList<Tematica>(tematicas));

	}

	@Listen("onSelect = #cmbTematicaSolicitud")
	public void tematica() {
		valor = Long.parseLong(cmbTematicaSolicitud.getSelectedItem().getId());

	}

	public String capturarArea() {
		String a = cmbAreaSolicitud.getValue();
		return a;
	}

	// permite ver la lista de profesores disponibles
	@Listen("onClick = #btnCatalogoProfesorArea")
	public void buscarProfesor() {

		combo = cmbProgramaSolicitud.getValue();
		combo1 = cmbAreaSolicitud.getValue();
		combo2 = cmbTematicaSolicitud.getValue();
		descripcion = txtTituloSolicitud.getValue();
		for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
			Estudiante e = ltbEstudiantes.getItems().get(i).getValue();

			EstudiantesColocados.add(e);

		}

		if (combo.compareTo("") == 0) {

			Messagebox.show("Debe elegir el programa", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

		else if (combo2.compareTo("") == 0) {

			Messagebox.show("Debe elegir la tematica", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

		if ((combo.compareTo("") != 0) && (combo2.compareTo("") != 0)) {

			catalogo.recibir("transacciones/VSolicitarTutor", idcombo, valor);

			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesorTematica.zul", null,
					null);
			window.doModal();

			wdwSolicitarTutoria.onClose();
		}

	}

	// Permite realizar una peticion de tutoria
	@Listen("onClick = #btnEnviarSolicitudtutoria")
	public void enviarSolicitud() {

		fecha = db1.getValue();
		estado = "Por Revisar";
		programa = cmbProgramaSolicitud.getValue();
		area = cmbAreaSolicitud.getValue();
		tematica = cmbTematicaSolicitud.getValue();
		titulo = txtTituloSolicitud.getValue();
		cedulaProfesor = txtCedulaProfesor.getValue();
		String nombreProfesor = txtNombreProfesor.getValue();
		String apellidoProfesor = txtApellidoProfesor.getValue();
		String correoProfesor = txtCorreoProfesor.getValue();
		Estudiante pruebaCondicion = ltbEstudiantes.getItems().get(0)
				.getValue();
		for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
			Estudiante e = ltbEstudiantes.getItems().get(i).getValue();

			gridEstudiante.add(e);

		}

		if (gridEstudiante.size() == 0) {
			Messagebox.show("No hay estudiantes agregados", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			if (programa == "Seleccione una Opcion"
					|| area == "Seleccione una Opcion"
					|| tematica == "Seleccione una Opcion" || titulo == ""
					|| cedulaProfesor == "" || nombreProfesor == ""
					|| apellidoProfesor == "" || correoProfesor == "") {
				Messagebox.show("Debes completar todos los campos", "Error",
						Messagebox.OK, Messagebox.ERROR);
			} else {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(cedulaProfesor);

				if (profesor == null) {
					Messagebox.show("El profesor no existe", "Advertencia",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}  else {
						Messagebox.show(
								"¿Desea enviar la solicitud de tutoria?",
								"Dialogo de confirmacion", Messagebox.OK
										| Messagebox.CANCEL,
								Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {
									public void onEvent(Event evt)
											throws InterruptedException {
										if (evt.getName().equals("onOK")) {
											Tematica tematica2 = servicioTematica
													.buscarTematicaPorNombre(tematica);
											Profesor profesor = servicioProfesor
													.buscarProfesorPorCedula(cedulaProfesor);

											Set<Estudiante> estudiante2 = new HashSet<Estudiante>();
											for (int i = 0; i < ltbEstudiantes
													.getItemCount(); i++) {
												Estudiante estudiante3 = ltbEstudiantes
														.getItems().get(i)
														.getValue();
												estudiante2.add(estudiante3);
											}

											SolicitudTutoria solicitud2 = new SolicitudTutoria(
													id, fecha, titulo, estado,
													profesor, tematica2,
													estudiante2);

											servicioSolicitarTutor
													.guardarSolicitud(solicitud2);
											enviarEmailNotificacion();
											Messagebox
													.show("Su solicitud de tutoria ha sido enviada exitosamente",
															"Informacion",
															Messagebox.OK,
															Messagebox.INFORMATION);
											wdwSolicitarTutoria.onClose();
										}
									}
								});
					}
				}
			}
		}
	

	// limpia todos los campos de la vista
	@Listen("onClick = #btnCancelarSolicitudTutoria")
	public void cancelarSolicitud() {
		id = 0;

		cmbProgramaSolicitud.setValue("");
		cmbAreaSolicitud.setValue("");
		cmbTematicaSolicitud.setValue("");
		cmbAreaSolicitud.setDisabled(true);
		cmbTematicaSolicitud.setDisabled(true);
		txtTituloSolicitud.setValue("");

		txtCedulaEstudiante.setValue("");
		ltbEstudiantes.getItems().clear();

		txtCedulaProfesor.setValue("");
		txtNombreProfesor.setValue("");
		txtApellidoProfesor.setValue("");
		txtCorreoProfesor.setValue("");
		List<Programa> programas = servicioPrograma.buscarActivas();

		cmbProgramaSolicitud.setModel(new ListModelList<Programa>(programas));

	}

	// permite agrgar un estudiante a la vista
	@Listen("onClick = #btnAgregarEstudiante")
	public void AgregarEstudiante() {
		String cedulaEstudiante = txtCedulaEstudiante.getValue();

		int tamano = gridEstudiante.size();

		if (cedulaEstudiante == "") {
			Messagebox.show("Debe ingresar su cedula", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			if (tamano == 0) {
				Estudiante estudiante = servicioEstudiante
						.buscarEstudiante(cedulaEstudiante);
				if (estudiante == null) {
					Messagebox.show("Debe estar previamente Registrado",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {

					SolicitudTutoria solicitud = servicioSolicitarTutor
							.buscarSolicitudEstudiantePorRevisar(estudiante);
					SolicitudTutoria solicitudAceptada = servicioSolicitarTutor
							.buscarSolicitudAceptadaEstudiante(estudiante);
					if (solicitud != null) {
						Messagebox
								.show("Ya tiene una solicitud de tutoria que se encuentra por revisar",
										"Advertencia", Messagebox.OK,
										Messagebox.EXCLAMATION);
					} else {
						if (solicitudAceptada != null) {
							Messagebox
									.show("Ya tiene una solicitud de tutoria aceptada",
											"Advertencia", Messagebox.OK,
											Messagebox.EXCLAMATION);
						} else {
							List<Teg> teg = servicioTeg
									.buscarTegPorEstudiante(estudiante);
							if (teg.size() > 0) {
								Messagebox.show("Ya tiene un TEG en proceso",
										"Advertencia", Messagebox.OK,
										Messagebox.EXCLAMATION);
							} else

							if (cmbProgramaSolicitud.getValue().compareTo(
									estudiante.getPrograma().getNombre()) != 0) {
								Messagebox
										.show("Debe elegir el programa al que pertenece",
												"Advertencia", Messagebox.OK,
												Messagebox.EXCLAMATION);
							} else {
								gridEstudiante.add(estudiante);
								ltbEstudiantes
										.setModel(new ListModelList<Estudiante>(
												gridEstudiante));
								limpiarDatosEstudiante();
							}
						}
					}

				}
			} else {
				Estudiante estudiante2 = servicioEstudiante
						.buscarEstudiante(cedulaEstudiante);
				if (estudiante2 == null) {
					Messagebox.show("Debe estar previamente registrado",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {
					SolicitudTutoria solicitud = servicioSolicitarTutor
							.buscarSolicitudEstudiantePorRevisar(estudiante2);
					SolicitudTutoria solicitudAceptada = servicioSolicitarTutor
							.buscarSolicitudAceptadaEstudiante(estudiante2);
					if (solicitud != null) {
						Messagebox
								.show("Ya tiene una solicitud de tutoria que se encuentra por revisar",
										"Advertencia", Messagebox.OK,
										Messagebox.EXCLAMATION);
					} else {
						if (solicitudAceptada != null) {
							Messagebox
									.show("Ya tiene una solicitud de tutoria aceptada",
											"Advertencia", Messagebox.OK,
											Messagebox.EXCLAMATION);
						} else {
							List<Teg> teg = servicioTeg
									.buscarTegPorEstudiante(estudiante2);
							if (teg.size() > 0) {
								Messagebox.show("Ya tiene un TEG en proceso",
										"Advertencia", Messagebox.OK,
										Messagebox.EXCLAMATION);
							} else {

								String nombre = "Numero de estudiantes por trabajo";

								if (tamano < buscarCondicionVigenteEspecifica(
										nombre, estudiante2.getPrograma())
										.getValor()) {
									gridEstudiante.add(estudiante2);
									ltbEstudiantes
											.setModel(new ListModelList<Estudiante>(
													gridEstudiante));
									limpiarDatosEstudiante();
								} else {
									Messagebox
											.show("Solo se permiten "
													+ buscarCondicionVigenteEspecifica(
															nombre,
															estudiante2
																	.getPrograma())
															.getValor()
													+ " estudiantes por proyecto",
													"Advertencia",
													Messagebox.OK,
													Messagebox.EXCLAMATION);
								}
							}
						}
					}
				}
			}
		}

	}

	// enviar email al profesor
	private void enviarEmailNotificacion() {
		for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
			Estudiante estudiante = ltbEstudiantes.getItems().get(i).getValue();
			Profesor profesor = servicioProfesor
					.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
			String mensaje = " Solicitud de tutoria \n\n " + "Estudiante: "
					+ estudiante.getNombre() + " , " + estudiante.getApellido()
					+ "\n\n con Titulo de Proyecto: "
					+ txtTituloSolicitud.getValue();

			enviarEmailNotificacion(estudiante.getCorreoElectronico(), mensaje);

		}

	}
	
	
	
	@Listen("onClick = #btnSalirSolicitudTutoria")
	public void salirSolicitudTutoria() {
	
		wdwSolicitarTutoria.onClose();
		
		
	}
	
}