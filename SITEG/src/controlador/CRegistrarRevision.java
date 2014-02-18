package controlador;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Avance;
import modelo.Estudiante;
import modelo.Teg;
import modelo.TegEstatus;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite guardar las revisiones realizadas por el tutor y
 * el estudiante durante el teg
 */
@Controller
public class CRegistrarRevision extends CGeneral {

	private static final long serialVersionUID = 6771530573761957926L;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static boolean estatusTeg;
	@Wire
	private Datebox dtbRegistrarRevision;
	@Wire
	private Textbox txtProgramaRegistrarRevision;
	@Wire
	private Textbox txtAreaRegistrarRevision;
	@Wire
	private Textbox txtTematicaRegistrarRevision;
	@Wire
	private Textbox txtTituloRegistrarRevision;
	@Wire
	private Textbox txtObservacionRegistrarRevision;
	@Wire
	private Listbox lsbEstudiantesTeg;
	@Wire
	private Listbox ltbRevisionesRegistradas;
	@Wire
	private Window wdwRegistrarRevision;
	@Wire
	private Window wdwCatalogoRegistrarRevision;
	@Wire
	private Button btnGuardarRegistrarRevision;
	@Wire
	private Button btnFinalizarRegistrarRevision;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		estatusTeg = false;
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg2);
				txtProgramaRegistrarRevision.setValue(est.get(0).getPrograma()
						.getNombre());
				txtAreaRegistrarRevision.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaRegistrarRevision.setValue(teg2.getTematica()
						.getNombre());
				txtTituloRegistrarRevision.setValue(teg2.getTitulo());
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

	/*
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/*
	 * Metodo que permite cerrar la ventana, actualizando los cambios realizados
	 * en el resto del sistema
	 */
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwRegistrarRevision.onClose();
	}

	/*
	 * Metodo que permite guardar una revision realizada al teg, pudiendo
	 * agregar mas revisiones en el futuro, cambia el estatus del teg, y
	 * almacena el estatus en la tabla de historial
	 */
	@Listen("onClick = #btnAgregarRevisiones")
	public void guardarRevision() {

		if ((txtObservacionRegistrarRevision.getText().compareTo("") == 0)) {
			Messagebox
					.show("Debe agregar las observaciones respectivas a la revision del Trabajo Especial de Grado",
							"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox
					.show("¿Desea guardar la revision del trabajo Especial de Grado?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {

										Teg tegRevision = servicioTeg
												.buscarTeg(auxiliarId);

										if (tegRevision.getEstatus().equals(
												"Trabajo en Desarrollo")) {
											estatusTeg = true;
										}
										if (estatusTeg == false) {
											tegRevision
													.setEstatus("Trabajo en Desarrollo");
											servicioTeg.guardar(tegRevision);
											java.util.Date fechaEstatus = new Date();
											TegEstatus tegEstatus = new TegEstatus(
													0, tegRevision,
													"Trabajo en Desarrollo",
													fechaEstatus);
											servicioTegEstatus
													.guardar(tegEstatus);
										}
										Date fecha = dtbRegistrarRevision
												.getValue();
										String observacion = txtObservacionRegistrarRevision
												.getValue();
										String estatus = "Revision TEG";
										Avance revision = new Avance(id, fecha,
												observacion, estatus,
												tegRevision);
										servicioAvance.guardar(revision);
										cancelarRegistrarRevision();
										id = 0;
										llenarListas();
										Messagebox
												.show("Revision del Trabajo Especial de Grado registrada exitosamente",
														"Informacion",
														Messagebox.OK,
														Messagebox.INFORMATION);

									}
								}
							});

		}

	}

	/*
	 * Metodo que permite finalizar las revisiones del proyecto, al accionar
	 * este evento se cambia el estatus del teg, ademas en este metodo se
	 * actualiza la tabla de historial del teg
	 */
	@Listen("onClick = #btnFinalizarRegistrarRevision")
	public void finalizarRegistrarRevision() {

		Messagebox
				.show("¿Desea finalizar las revisiones del Trabajo Especial de Grado?",
						"Dialogo de confirmacion", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
									String estatus = "Revisiones Finalizadas";
									Teg tegAvance = servicioTeg
											.buscarTeg(auxiliarId);
									java.util.Date fechaEstatus = new Date();
									TegEstatus tegEstatus = new TegEstatus(0,
											tegAvance, estatus, fechaEstatus);
									servicioTegEstatus.guardar(tegEstatus);
									tegAvance.setEstatus(estatus);
									servicioTeg.guardar(tegAvance);
									Messagebox
											.show("Revisiones del Trabajo Especial de Grado finalizadas exitosamente",
													"Informacion",
													Messagebox.OK,
													Messagebox.INFORMATION);
									salir();
								}
							}
						});

	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarRegistrarRevision")
	public void cancelarRegistrarRevision() {
		txtObservacionRegistrarRevision.setValue("");
	}

	/*
	 * Metodo que permite llenar la lista con las revisiones ya realizados en el
	 * teg
	 */
	public void llenarListas() {
		Teg tegAvance = servicioTeg.buscarTeg(auxiliarId);
		List<Avance> avancesTeg = servicioAvance
				.buscarRevisionPorTeg(tegAvance);
		try {
			if (avancesTeg.size() == 0) {
				btnFinalizarRegistrarRevision.setDisabled(true);
			} else {
				ltbRevisionesRegistradas.setModel(new ListModelList<Avance>(
						avancesTeg));
				btnFinalizarRegistrarRevision.setDisabled(false);
			}
		} catch (NullPointerException e) {
			System.out.println("NullPointerException");
		}
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirRegistrarRevision")
	public void salirRegistrarRevision() {
		wdwRegistrarRevision.onClose();
	}

}
