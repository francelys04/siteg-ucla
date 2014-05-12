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

/**
 * Controlador que permite guardar los avances realizados entre el tutor y el
 * estudiante durante el proyecto
 */
@Controller
public class CRegistrarAvance extends CGeneral {

	private static final long serialVersionUID = 5336063690880490786L;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static boolean estatusTeg;

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

	/**
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
				txtProgramaRegistrarAvance.setValue(est.get(0).getPrograma()
						.getNombre());
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

	/**
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 *  @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;
	}

	/**
	 * Metodo que permite cerrar la ventana, actualizando los cambios realizados
	 * en el resto del sistema
	 */
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwRegistrarAvance.onClose();
	}

	/**
	 * Metodo que permite guardar un avance al proyecto, pudiendo agregar mas
	 * avances en el futuro, cambia el estatus del teg, y almacena el estatus en
	 * la tabla de historial
	 */
	@Listen("onClick = #btnAgregarObservacionrAvance")
	public void guardarAvance() {

		if ((txtObservacionRegistrarAvance.getText().compareTo("") == 0)) {
			Messagebox
					.show("Debe agregar las observaciones respectivas del avance del Proyecto",
							"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show("¿Desea guardar el avance del Proyecto?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Teg tegAvance = servicioTeg
										.buscarTeg(auxiliarId);

								if (tegAvance.getEstatus().equals(
										"Proyecto en Desarrollo")) {

									estatusTeg = true;
								}

								if (estatusTeg == false) {
									tegAvance
											.setEstatus("Proyecto en Desarrollo");
									servicioTeg.guardar(tegAvance);
									java.util.Date fechaEstatus = new Date();
									TegEstatus tegEstatus = new TegEstatus(0,
											tegAvance,
											"Proyecto en Desarrollo",
											fechaEstatus);
									servicioTegEstatus.guardar(tegEstatus);

								}

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
										.show("Avance del proyecto registrado exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);

							}
						}
					});

		}

	}

	/**
	 * Metodo que permite finalizar los avances del proyecto, al accionar este
	 * evento se cambia el estatus del teg y el estudiante ya puede registrar su
	 * trabajo especial de grado. Tambien en este metodo se actualiza el cambio
	 * en la tabla de historial
	 */
	@Listen("onClick = #btnFinalizarRegistrarAvance")
	public void finalizarRegistrarAvance() {

		Messagebox.show("¿Desea finalizar los avances del proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							Teg tegAvance = servicioTeg.buscarTeg(auxiliarId);
							tegAvance.setEstatus("Avances Finalizados");
							java.util.Date fechaEstatus = new Date();
							TegEstatus tegEstatus = new TegEstatus(0,
									tegAvance, "Avances Finalizados",
									fechaEstatus);
							servicioTegEstatus.guardar(tegEstatus);

							servicioTeg.guardar(tegAvance);
							Messagebox
									.show("Avances del proyecto finalizados exitosamente",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
							salir();
						}
					}
				});
	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarRegistrarAvance")
	public void cancelarRegistrarAvance() {

		txtObservacionRegistrarAvance.setValue("");

	}

	/**
	 * Metodo que permite llenar la lista con los avances ya realizados en el
	 * proyecto
	 */
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

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirRegistrarAvance")
	public void salirRegistrarAvance() {
		wdwRegistrarAvance.onClose();
	}

}
