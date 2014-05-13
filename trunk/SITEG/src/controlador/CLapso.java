package controlador;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Condicion;
import modelo.Lapso;
import modelo.TipoJurado;
import modelo.compuesta.CondicionPrograma;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoLapso;

/**Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Lapso*/
@Controller
public class CLapso extends CGeneral {

	private static final long serialVersionUID = -3737502248287300642L;
	CCatalogoLapso catalogo = new CCatalogoLapso();
	@Wire
	private Textbox txtNombreLapso;
	@Wire
	private Textbox txtNombreEstudiante;
	@Wire
	private Datebox dtbInicioLapso;
	@Wire
	private Datebox dtbFinLapso;
	@Wire
	private Window wdwLapsoAcademico;
	@Wire
	private Button btnEliminarLapso;
	@Wire
	private Button btnGuardarLapso;
	private long id = 0;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	public void inicializar(Component comp) {

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Lapso lapso = servicioLapso.buscarLapso(codigo);
				txtNombreLapso.setValue(lapso.getNombre());
				dtbInicioLapso.setValue(lapso.getFechaInicial());
				dtbFinLapso.setValue(lapso.getFechaFinal());
				id = lapso.getId();
				btnEliminarLapso.setDisabled(false);
				map.clear();
				map = null;
			}
		}

	}

	/**
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoLapsos")
	public void buscarLapso() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoLapso.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VLapsoAcademico");

	}

	/**
	 * Metodo que permite el guardado o modificacion de una entidad Lapso, asi
	 * como la asignacion, de ser necesaria, de las condiciones por programa
	 * para el lapso actual
	 */
	@Listen("onClick = #btnGuardarLapso")
	public void guardarLapsoAcademico() {

		if ((txtNombreLapso.getText().compareTo("") == 0)
				|| (dtbInicioLapso.getText().compareTo("") == 0)
				|| (dtbFinLapso.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else if (dtbInicioLapso.getValue().after(dtbFinLapso.getValue())
				|| (dtbInicioLapso.getValue().equals(dtbFinLapso.getValue()))) {

			Messagebox
					.show("La fecha de fin de lapso debe ser posterior a la fecha de inicio",
							"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show("¿Desea guardar los datos del lapso academico?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								String nombre = txtNombreLapso.getValue();
								Date fechaInicio = dtbInicioLapso.getValue();
								Date fechaFin = dtbFinLapso.getValue();
								Boolean estatus = true;
								Lapso lapso = new Lapso(id, nombre,
										fechaInicio, fechaFin, estatus);
								servicioLapso.guardar(lapso);

								if (servicioPrograma.buscarActivas().size() != 0) {

									for (int i = 0; i < servicioPrograma
											.buscarActivas().size(); i++) {

										if (servicioCondicionPrograma
												.buscarCondicionesPrograma(
														servicioPrograma
																.buscarActivas()
																.get(i),
														servicioLapso
																.BuscarLapsoActual())
												.isEmpty()) {

											List<Condicion> condiciones = servicioCondicion
													.buscarActivos();
											List<CondicionPrograma> condicionesPrograma = new ArrayList<CondicionPrograma>();
											for (int j = 0; j < condiciones
													.size(); j++) {
												Condicion condicion = condiciones
														.get(j);
												CondicionPrograma condicionPrograma = new CondicionPrograma();
												condicionPrograma
														.setPrograma(servicioPrograma
																.buscarActivas()
																.get(i));
												Lapso ultimoLapso = servicioLapso
														.BuscarLapsoActual();

												condicionPrograma
														.setLapso(ultimoLapso);
												condicionPrograma
														.setCondicion(condicion);
												condicionPrograma.setValor(0);
												condicionesPrograma
														.add(condicionPrograma);
											}

											servicioCondicionPrograma
													.guardar(condicionesPrograma);
										}
									}
								}
								cancelarLapso();
								id = 0;
								Messagebox
										.show("Lapso academico registrado exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
							}
						}
					});

		}
	}

	/** Metodo que permite la eliminacion logica de una entidad Lapso */
	@Listen("onClick = #btnEliminarLapso")
	public void eliminarLapso() {
		Lapso lapso = servicioLapso.BuscarLapsoActual();
		Date fechaI = lapso.getFechaInicial();
		Date fechaF = lapso.getFechaFinal();
		Date fecha = new Date();
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
			String fechaSistema = formateador.format(fecha);
			Date fechaActual;
			fechaActual = formateador.parse(fechaSistema);

			if (fechaActual.after(fechaI) && fechaActual.before(fechaF)) {
				Messagebox.show("El Lapso Academico se esta ejecutando",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);
			} else {
				Messagebox.show(
						"¿Desea eliminar los datos del lapso academico?",
						"Dialogo de confirmacion", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
									Lapso lapso = servicioLapso.buscarLapso(id);
									lapso.setEstatus(false);
									servicioLapso.guardar(lapso);
									cancelarLapso();
									Messagebox
											.show("Lapso academico eliminado exitosamente",
													"Informacion",
													Messagebox.OK,
													Messagebox.INFORMATION);

								}
							}
						});
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
	}

	/**
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarLapso")
	public void cancelarLapso() {
		btnEliminarLapso.setDisabled(true);
		txtNombreLapso.setValue("");
		dtbInicioLapso.setValue(null);
		dtbFinLapso.setValue(null);
		btnEliminarLapso.setDisabled(true);
		id = 0;
	}

	/**
	 * Metodo que permite cerrar la ventana correspondiente a los lapsos
	 * academicos
	 */
	@Listen("onClick = #btnSalirLapso")
	public void salirLapso() {
		wdwLapsoAcademico.onClose();
	}

	/**
	 * Metodo que permite buscar si un lapso academico existe, de acuerdo al
	 * nombre del lapso academico
	 */
	@Listen("onChange = #txtNombreLapso")
	public void buscarNombreLapso() {
		Lapso lapso = servicioLapso.buscarPorNombreLapso(txtNombreLapso
				.getValue());
		if (lapso != null) {

			txtNombreLapso.setValue(lapso.getNombre());
			dtbInicioLapso.setValue(lapso.getFechaInicial());
			dtbFinLapso.setValue(lapso.getFechaFinal());
			id = lapso.getId();
			btnEliminarLapso.setDisabled(false);

		}

	}

}
