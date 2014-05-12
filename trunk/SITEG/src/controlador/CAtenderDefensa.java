package controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Defensa;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.Jurado;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

/**
 * Controlador que permite atender las solicitudes de defensa asignadole un
 * lugar una fecha y una hora
 */
@Controller
public class CAtenderDefensa extends CGeneral {

	private static final long serialVersionUID = -4953804421310031410L;
	private static String vistaRecibida;
	private static long idTeg = 0;
	private static Estudiante estudianteTeg;
	long idDefensa = 0;
	private static Programa programa;
	@Wire
	private Textbox txtProgramaAtenderDefensa;
	@Wire
	private Textbox txtAreaAtenderDefensa;
	@Wire
	private Textbox txtTematicaAtenderDefensa;
	@Wire
	private Textbox txtTituloAtenderDefensa;
	@Wire
	private Label lblCondicionAtenderDefensa;
	@Wire
	private Listbox ltbEstudiantesAtenderDefensa;
	@Wire
	private Datebox dtbFechaAtenderDefensa;
	@Wire
	private Datebox dtbFechaDefensa;
	@Wire
	private Textbox txtLugarDefensa;
	@Wire
	private Timebox tmbHoraDefensa;
	@Wire
	private Window wdwAtenderDefensa;
	@Wire
	private Textbox txtNombreTutorProgramarDefensa;
	@Wire
	private Textbox txtApellidoTutorProgramarDefensa;
	@Wire
	private Listbox ltbJuradoAtenderDefensa;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private static SimpleDateFormat formatoHora = new SimpleDateFormat(
			"hh:mm:ss a");

	/**
	 * Metodo heredado del Controlador CGeneral dondese verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);
				txtNombreTutorProgramarDefensa.setValue(teg.getTutor()
						.getNombre());
				txtApellidoTutorProgramarDefensa.setValue(teg.getTutor()
						.getApellido());

				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg);

				List<Jurado> jurados = servicioJurado.buscarJuradoDeTeg(teg);
				List<Profesor> profesoresJurado = new ArrayList<Profesor>();
				for (int i = 0; i < jurados.size(); i++) {

					Profesor profesor = jurados.get(i).getProfesor();
					profesoresJurado.add(profesor);
				}

				ltbJuradoAtenderDefensa.setModel(new ListModelList<Profesor>(
						profesoresJurado));

				programa = est.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(est.get(0).getPrograma()
						.getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());

			}
		}
	}

	/**
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 * 
	 * @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/**
	 * Metodo que permite llenar la lista de los estudiantes pertenecientes al
	 * teg
	 * 
	 * @param teg
	 *            trabajo especial de grado seleccionado para realizar la
	 *            programacion de la defensa
	 */
	public void llenarListas(Teg teg) {
		List<Estudiante> estudiantesTeg = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		ltbEstudiantesAtenderDefensa.setModel(new ListModelList<Estudiante>(
				estudiantesTeg));
	}

	/**
	 * Metodo que permite aceptar la solicitud de defensa, cambiando el estatus
	 * del teg. Ademas se logra guardar en la respectiva tabla de historial de
	 * cambios de estatus y se almacena en la base de datos la defensa referente
	 * al teg actual
	 */
	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensa() {

		String fechaActual = String.valueOf(formatoFecha.format(new Date()));
		String fechaDefensa = String.valueOf(formatoFecha
				.format(dtbFechaDefensa.getValue()));

		if (tmbHoraDefensa.getValue() == null
				|| txtLugarDefensa.getText().compareTo("") == 0
				|| dtbFechaDefensa.getValue() == null) {

			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			if (dtbFechaAtenderDefensa.getValue().after(
					dtbFechaDefensa.getValue())
					|| fechaActual.equals(fechaDefensa)) {

				Messagebox
						.show("La fecha de defensa debe ser posterior a la fecha actual",
								"Error", Messagebox.OK, Messagebox.ERROR);
			} else {

				Messagebox.show("¿Desea guardar los datos de la defensa?",
						"Dialogo de confirmacion", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {

									Teg teg = servicioTeg.buscarTeg(idTeg);
									Date fecha = dtbFechaDefensa.getValue();
									Date hora = tmbHoraDefensa.getValue();
									String lugar = txtLugarDefensa.getValue();
									Profesor profesor = ObtenerUsuarioProfesor();

									for (int i = 0; i < ltbEstudiantesAtenderDefensa
											.getItemCount(); i++) {
										estudianteTeg = ltbEstudiantesAtenderDefensa
												.getItems().get(i).getValue();

										enviarEmailNotificacion(
												estudianteTeg
														.getCorreoElectronico(),
												"La defensa de su Trabajo Especial de Grado fue programada para el: "
														+ formatoFecha
																.format(fecha)
														+ " "
														+ "a las: "
														+ formatoHora
																.format(hora)
														+ " " + "en la: "
														+ lugar);
									}

									String estatus = "Defensa Programada";
									Defensa defensa = new Defensa(idDefensa,
											teg, fecha, hora, lugar, estatus,
											profesor);
									servicioDefensa.guardarDefensa(defensa);
									String estatus1 = "Defensa Asignada";
									Teg teg1 = servicioTeg.buscarTeg(idTeg);
									java.util.Date fechaEstatus = new Date();
									TegEstatus tegEstatus = new TegEstatus(0,
											teg1, estatus1, fechaEstatus);
									servicioTegEstatus.guardar(tegEstatus);
									teg1.setEstatus(estatus1);
									servicioTeg.guardar(teg1);
									Messagebox
											.show("Datos de la defensa guardados con exito",
													"Informacion",
													Messagebox.OK,
													Messagebox.INFORMATION);
									salir();

								}
							}
						});
			}

		}

	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		txtLugarDefensa.setValue("");
		dtbFechaDefensa.setValue(new Date());
		tmbHoraDefensa.setValue(new Date());

	}

	/**
	 * Metodo que permite cerrar la ventana, actualizando los cambios realizados
	 * en el resto del sistema
	 */
	public void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAtenderDefensa.onClose();
	}

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirDefensa")
	public void SalirDefensa() {

		wdwAtenderDefensa.onClose();

	}

}
