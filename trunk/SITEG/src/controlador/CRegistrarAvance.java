package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Avance;
import modelo.CondicionPrograma;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SActividad;
import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.STeg;
import servicio.SUsuario;
import servicio.SLapso;
import servicio.SCondicionPrograma;
import configuracion.GeneradorBeans;
import servicio.SAvance;

public class CRegistrarAvance extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAvance servicioAvance = GeneradorBeans.getServicioAvance();

	@Wire
	private Datebox dtbRegistrarAvance;
	@Wire
	private Textbox txtProgramaRegistrarAvance;
	@Wire
	private Textbox txtAreaRegistrarAvance;
	@Wire
	private Textbox txtTematicaRegistrarAvance;
	@Wire
	private Textbox txtTituloRegistrarAvance;
	@Wire
	private Textbox txtObservacionRegistrarAvance;
	@Wire
	private Listbox lsbEstudiantesTeg;
	@Wire
	private Listbox ltbAvancesRegistrados;
	@Wire
	private Window wdwRegistrarAvance;
	@Wire
	private Window wdwCatalogoRegistrarAvance;
	@Wire
	private Button btnGuardarRegistrarAvance;
	@Wire
	private Button btnFinalizarRegistrarAvance;

	private static String vistaRecibida;

	private List<Profesor> profesores;

	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg2);
				txtProgramaRegistrarAvance.setValue(est.get(0).getPrograma().getNombre());
				txtAreaRegistrarAvance.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaRegistrarAvance.setValue(teg2.getTematica()
						.getNombre());
				txtTituloRegistrarAvance.setValue(teg2.getTitulo());
				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegPorCodigo);
				lsbEstudiantesTeg.setModel(new ListModelList<Estudiante>(
						estudiantes));
				llenarListas();
				id = teg2.getId();

				map.clear();
				map = null;
			}
		}

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwRegistrarAvance.onClose();
	}

	@Listen("onClick = #btnGuardarRegistrarAvance")
	public void guardarAvance() {

		if ((txtObservacionRegistrarAvance.getText().compareTo("") == 0)) {
			Messagebox
					.show("Debe agregar las observaciones respectivas sobre el avance del Trabajo Especial de Grado",
							"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show(
					"¿Desea guardar el avance del trabajo Especial de Grado?",
					"Dialogo de confirmación", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Teg tegAvance = servicioTeg
										.buscarTeg(auxiliarId);
								Date fecha = dtbRegistrarAvance.getValue();
								String observacion = txtObservacionRegistrarAvance
										.getValue();
								String estatus = "Avance Proyecto";
								Avance avance = new Avance(id, fecha,
										observacion, estatus, tegAvance);
								servicioAvance.guardar(avance);
								cancelarRegistrarAvance();
								id = 0;
								llenarListas();

								Messagebox
										.show("Avance del Trabajo Especial de Grado registrado exitosamente",
												"Información", Messagebox.OK,
												Messagebox.INFORMATION);

							}
						}
					});

		}

	}

	@Listen("onClick = #btnFinalizarRegistrarAvance")
	public void finalizarRegistrarAvance() {

		Messagebox.show("¿Desea finalizar los avances del proyecto?",
				"Dialogo de confirmación", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							
							Teg tegAvance = servicioTeg.buscarTeg(auxiliarId);
							tegAvance.setEstatus("Avances Finalizados");
							servicioTeg.guardar(tegAvance);
							Messagebox
									.show("Avances del Trabajo Especial de Grado finalizados exitosamente",
											"Información", Messagebox.OK, Messagebox.INFORMATION);
							salir();
						}
					}
				});
	}

	@Listen("onClick = #btnCancelarRegistrarAvance")
	public void cancelarRegistrarAvance() {

		txtObservacionRegistrarAvance.setValue("");

	}

	public void llenarListas() {

		Teg tegAvance = servicioTeg.buscarTeg(auxiliarId);
		List<Avance> avancesTeg = servicioAvance.buscarAvancePorTeg(tegAvance);

		try {
			if (avancesTeg.size() == 0) {

				btnFinalizarRegistrarAvance.setDisabled(true);

			} else {

				ltbAvancesRegistrados.setModel(new ListModelList<Avance>(
						avancesTeg));
				btnFinalizarRegistrarAvance.setDisabled(false);

			}
		} catch (NullPointerException e) {

			System.out.println("NullPointerException");
		}

	}

}
