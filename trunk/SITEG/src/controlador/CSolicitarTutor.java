package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Categoria;
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

//es un controlador de tematica y su catalogo
@Controller
public class CSolicitarTutor extends CGeneral {

	// servicios para los dos modelos implicados
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

	@Wire
	private Datebox db1;
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
	@Wire
	private Textbox txtNombreEstudiante;
	@Wire
	private Textbox txtApellidoEstudiante;
	@Wire
	private Textbox txtCorreoEstudiante;

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

	private long id;

	// metodo para mapear los datos del Tutor
	void inicializar(Component comp) {

		List<Programa> programas = servicioPrograma.buscarActivas();
		cmbProgramaSolicitud.setModel(new ListModelList<Programa>(programas));

		cmbAreaSolicitud.setDisabled(true);
		cmbTematicaSolicitud.setDisabled(true);

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
				map.clear();
				map = null;
			}
		}
	}

	@Listen("onClick = #btnCatalogoProfesorArea")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
		window.doModal();

	}

	@Listen("onClick = #btnEnviarSolicitudtutoria")
	public void enviarSolicitud() {

		Date fecha = db1.getValue();
		String estado = "Por Revisar";
		String programa = cmbProgramaSolicitud.getValue();
		String area = cmbAreaSolicitud.getValue();
		String tematica = cmbTematicaSolicitud.getValue();
		String titulo = txtTituloSolicitud.getValue();

		String cedulaEstudiante = txtCedulaEstudiante.getValue();
		String nombreEstudiante = txtNombreEstudiante.getValue();
		String apellidoEstudiante = txtApellidoEstudiante.getValue();
		String correoEstudiante = txtCorreoEstudiante.getValue();

		String cedulaProfesor = txtCedulaProfesor.getValue();
		String nombreProfesor = txtNombreProfesor.getValue();
		String apellidoProfesor = txtApellidoProfesor.getValue();
		String correoProfesor = txtCorreoProfesor.getValue();

		if (programa == "" || area == "" || tematica == "" || titulo == ""
				|| cedulaEstudiante == "" || nombreEstudiante == ""
				|| apellidoEstudiante == "" || correoEstudiante == ""
				|| cedulaProfesor == "" || nombreProfesor == ""
				|| apellidoProfesor == "" || correoProfesor == "") {

			Messagebox.show("Debe llenar todos los campos", "Campos Vacíos",
					Messagebox.OK, Messagebox.INFORMATION);
		} else {
			Estudiante estudiante = servicioEstudiante
					.buscarEstudiante(cedulaEstudiante);

			if (estudiante == null) {
				Messagebox.show("Debes estar Previamente Registrado",
						"Información", Messagebox.OK, Messagebox.INFORMATION);
			} else {
				List<Teg> teg = servicioTeg.buscarTegPorEstudiante(estudiante);
				if (teg.size() > 0) {
					Messagebox.show("Ya Tienes Un TEG en Proceso",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
				} else {
					Profesor profesor = servicioProfesor
							.buscarProfesorPorCedula(cedulaProfesor);
					List<Teg> teg2 = servicioTeg
							.buscarTutoriaProfesor(profesor);
					if (teg2.size() >= 3) { //No creo que deban ser 3 especificamente, debe ser lo que diga esa condicion del programa....
						Messagebox.show(
								"El Profesor ya tiene más de 3 Proyectos",
								"Información", Messagebox.OK,
								Messagebox.INFORMATION);
					} else {
						SolicitudTutoria solicitud = servicioSolicitarTutor
								.buscarSolicitudEstudiante(estudiante);
						try {
							if (solicitud == null) {
								Messagebox.show(
										"Ya tienes una Solicitud Pendiente",
										"Información", Messagebox.OK,
										Messagebox.INFORMATION);
							} else {

							}
						} catch (NullPointerException e) {

							System.out.println("NullPointerException");
						}

					}
				}
			}
		}
	}

	@Listen("onClick = #btnCancelarSolicitudTutoria")
	public void cancelarSolicitud() {
		id = 0;

		cmbProgramaSolicitud.setValue("");
		cmbAreaSolicitud.setValue("");
		cmbTematicaSolicitud.setValue("");
		txtTituloSolicitud.setValue("");

		txtCedulaEstudiante.setValue("");
		txtNombreEstudiante.setValue("");
		txtApellidoEstudiante.setValue("");
		txtCorreoEstudiante.setValue("");

		txtCedulaProfesor.setValue("");
		txtNombreProfesor.setValue("");
		txtApellidoProfesor.setValue("");
		txtCorreoProfesor.setValue("");

	}

	@Listen("onSelect = #cmbProgramaSolicitud")
	public void areaSolicitud() {

		cmbAreaSolicitud.setDisabled(false);

		String programa = cmbProgramaSolicitud.getValue();
		Programa programa2 = servicioPrograma.buscarPorNombrePrograma(programa);

		List<AreaInvestigacion> a = servicioProgramaArea
				.buscarAreasDePrograma(programa2);
		cmbAreaSolicitud.setModel(new ListModelList<AreaInvestigacion>(a));

	}

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

}