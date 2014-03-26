package controlador;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.ItemFactibilidad;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite indicar si un proyecto es factible o no,
 * cambiando el estatus del teg
 */
@Controller
public class CRegistrarFactibilidad extends CGeneral {

	private static final long serialVersionUID = -7412081861984890268L;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static Programa programa;
	private static Estudiante estudianteTeg;

	@Wire
	private Listbox ltbListaFactibilidad;
	@Wire
	private Listbox ltbItemsFactibilidad;
	@Wire
	private Textbox txtProgramaRegistrarFactibilidad;
	@Wire
	private Textbox txtAreaRegistrarFactibilidad;
	@Wire
	private Textbox txtTematicaRegistrarFactibilidad;
	@Wire
	private Textbox txtTituloRegistrarFactibilidad;
	@Wire
	private Textbox txtObservacionRegistrarFactibilidad;
	@Wire
	private Textbox txtNombreTutorRegistrarFactibilidad;
	@Wire
	private Textbox txtApellidoTutorRegistrarFactibilidad;
	@Wire
	private Listbox ltbEstudianteRegistrarFactibilidad;
	@Wire
	private Listbox ltbComisionRegistrarFactibilidad;
	@Wire
	private Window wdwRegistrarFactibilidad;
	@Wire
	private Window wdwCatalogoRegistrarFactibilidad;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programa = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				txtProgramaRegistrarFactibilidad.setValue(programa.getNombre());
				txtAreaRegistrarFactibilidad.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaRegistrarFactibilidad.setValue(teg2.getTematica()
						.getNombre());
				txtTituloRegistrarFactibilidad.setValue(teg2.getTitulo());

				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				Factibilidad factibilidadPorTeg = servicioFactibilidad
						.buscarFactibilidadPorTeg(tegPorCodigo);

				List<ItemFactibilidad> itemsFactibilidad = servicioItemFactibilidad
						.buscarItemFactibilidad(factibilidadPorTeg);
				ltbItemsFactibilidad
						.setModel(new ListModelList<ItemFactibilidad>(
								itemsFactibilidad));

				txtNombreTutorRegistrarFactibilidad.setValue(teg2.getTutor()
						.getNombre());
				txtApellidoTutorRegistrarFactibilidad.setValue(teg2.getTutor()
						.getApellido());

				txtObservacionRegistrarFactibilidad.setValue(factibilidadPorTeg
						.getObservacion());

				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegPorCodigo);
				ltbEstudianteRegistrarFactibilidad
						.setModel(new ListModelList<Estudiante>(estudiantes));
				id = teg2.getId();

				map.clear();
				map = null;
			}
		}

	}

	/*
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/* Metodo que permite cerrar la pantalla actualizando los cambios realizados */
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwRegistrarFactibilidad.onClose();
	}

	/*
	 * Metodo que permite aceptar la factibilidad de un determinado proyecto,
	 * cambiando asi el estatus del proyecto a Proyecto Factible. Ademas se
	 * actualiza la tabla de cambios de estatus del teg
	 */
	@Listen("onClick = #btnAceptarRegistrarFactibilidad")
	public void aceptarFactibilidad() {
		Messagebox.show("¿Desea aceptar la factibilidad del proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							String estatus = "Proyecto Factible";
							Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
							Factibilidad factibilidad = servicioFactibilidad
									.buscarFactibilidadPorTeg(teg2);
							factibilidad.setEstatus("Factibilidad Aceptada");
							servicioFactibilidad.guardar(factibilidad);
							teg2.setEstatus(estatus);
							java.util.Date fechaEstatus = new Date();
							TegEstatus tegEstatus = new TegEstatus(0, teg2,
									"Proyecto Factible", fechaEstatus);
							servicioTegEstatus.guardar(tegEstatus);
							servicioTeg.guardar(teg2);
							Messagebox
									.show("Datos de la factibilidad guardados exitosamente",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
							salir();
						}
					}
				});

	}

	/*
	 * Metodo que permite rechazar la factibilidad de un determinado proyecto,
	 * cambiando asi el estatus del proyecto a Proyecto No Factible. Ademas se
	 * actualiza la tabla de cambios de estatus del teg
	 */
	@Listen("onClick = #btnRechazarRegistrarFactibilidad")
	public void rechazarfactibilidad() {
		Messagebox.show("¿Desea rechazar factibilidad del proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							estudianteTeg = ltbEstudianteRegistrarFactibilidad
									.getItems().get(0).getValue();
							
							String estatus = "Proyecto No Factible";
							Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
							Factibilidad factibilidad = servicioFactibilidad
									.buscarFactibilidadPorTeg(teg2);
							factibilidad.setEstatus("Factibilidad Rechazada");
							servicioFactibilidad.guardar(factibilidad);
							teg2.setEstatus(estatus);
							java.util.Date fechaEstatus = new Date();
							TegEstatus tegEstatus = new TegEstatus(0, teg2,
									"Proyecto No Factible", fechaEstatus);
							servicioTegEstatus.guardar(tegEstatus);
							servicioTeg.guardar(teg2);
							actualizarSolicitud();
							
							for (int i = 0; i < ltbEstudianteRegistrarFactibilidad
									.getItemCount(); i++) {
								estudianteTeg = ltbEstudianteRegistrarFactibilidad
										.getItems().get(i).getValue();
								enviarEmailNotificacion(
										estudianteTeg.getCorreoElectronico(),
										"Su Proyecto es no Factible");

							}

							Messagebox
									.show("Datos de la factibilidad guardados exitosamente",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
							salir();
						}
					}
				});
	}
	
	
	/*
	 * Metodo que permite actualizar el estatus de la solicitud del o los
	 * estudiantes asociados al teg, a finalizada, para asi indicar que ha
	 * terminado la tutoria
	 */
	public void actualizarSolicitud() {
		Estudiante estudiante = ltbEstudianteRegistrarFactibilidad.getItems().get(0)
				.getValue();
		SolicitudTutoria solicitud = servicioSolicitudTutoria
				.buscarSolicitudAceptadaEstudiante(estudiante);
		solicitud.setEstatus("Finalizada");
		servicioSolicitudTutoria.guardar(solicitud);
	}
	
	

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirRegistrarFactibilidad")
	public void salirRegistrarFactibilidad() {
		wdwRegistrarFactibilidad.onClose();
	}
}
