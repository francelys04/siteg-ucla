package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Defensa;
import modelo.Estudiante;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Mencion;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.ItemDefensa;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Controlador que permite ponderar un conjunto de items asociados a la defensa
 * del teg, asi como tambien asignarle una mencion e indicar si se aprueba o
 * reprueba dicho teg
 */
@Controller
public class CCalificarDefensa extends CGeneral {

	private static final long serialVersionUID = 5650021246758420789L;
	private static String vistaRecibida;
	private static long auxId;
	private static boolean dejeenblanco = false;
	private static Teg teg1;
	private static Programa programa;

	@Wire
	private Datebox dtbfecha;
	@Wire
	private Textbox txtProgramaCalificar;
	@Wire
	private Textbox txtAreaCalificar;
	@Wire
	private Textbox txtTematicaCalificar;
	@Wire
	private Textbox txtTituloCalificar;
	@Wire
	private Listbox ltbEstudiantesCalificar;
	@Wire
	private Textbox txtNombreTutorCalificarDefensa;
	@Wire
	private Textbox txtApellidoTutorCalificarDefensa;
	@Wire
	private Radiogroup rdgCalificacion;
	@Wire
	private Radio rdoAprobado;
	@Wire
	private Radio rdoReprobado;
	@Wire
	private Combobox cmbMencionTeg;
	@Wire
	private Window wdwCalificarDefensa;
	@Wire
	private Listbox ltbitem;

	/**
	 * Metodo heredado del Controlador CGeneral dondese verifica que el mapa
	 * recibido del catalogo exista yse llenan los campos y listas
	 * correspondientes de la vista, asicomo los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");
		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(codigo);
				List<Estudiante> estudiante = servicioEstudiante
						.buscarEstudiantePorTeg(teg2);
				if (estudiante.size() != 0) {
					programa = estudiante.get(0).getPrograma();

				}
				txtProgramaCalificar.setValue(programa.getNombre());
				txtTituloCalificar.setValue(teg2.getTitulo());
				txtAreaCalificar.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaCalificar.setValue(teg2.getTematica().getNombre());
				txtNombreTutorCalificarDefensa.setValue(teg2.getTutor()
						.getNombre());
				txtApellidoTutorCalificarDefensa.setValue(teg2.getTutor()
						.getApellido());
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg2);
				ltbEstudiantesCalificar.setModel(new ListModelList<Estudiante>(
						est));
				Lapso lapso = servicioLapso.buscarLapsoVigente();
				List<ItemEvaluacion> item = servicioProgramaItem
						.buscarItemsEnPrograma(programa, lapso);
				List<ItemEvaluacion> item2 = new ArrayList<ItemEvaluacion>();
				List<Mencion> menciones = servicioMencion.buscarActivos();
				cmbMencionTeg.setModel(new ListModelList<Mencion>(menciones));
				for (int i = 0; i < item.size(); i++) {
					if (item.get(i).getTipo().equals("Defensa")) {
						item2.add(item.get(i));
					}

				}
				ltbitem.setModel(new ListModelList<ItemEvaluacion>(item2));

				map.clear();
				map = null;
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
	 * Metodo que permite actualizar el estatus de la solicitud del o los
	 * estudiantes asociados al teg, a finalizada, para asi indicar que ha
	 * terminado la tutoria
	 */
	public void actualizarSolicitud() {
		Estudiante estudiante = ltbEstudiantesCalificar.getItems().get(0)
				.getValue();
		SolicitudTutoria solicitud = servicioSolicitudTutoria
				.buscarSolicitudAceptadaEstudiante(estudiante);
		solicitud.setEstatus("Finalizada");
		servicioSolicitudTutoria.guardar(solicitud);
	}

	/**
	 * Metodo que permite guardar la calificacion de la defensa, cambiando el
	 * estatus del teg, almacenando el cambio en la tabla respectivas, ademas se
	 * actualiza el estatus de la defensa
	 */
	@Listen("onClick = #btnGuardar")
	public void guardar() {
		dejeenblanco = false;
		if (cmbMencionTeg.getValue().equals("")) {
			Messagebox.show("Debe Seleccionar una mencion para el TEG",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			if ((rdoAprobado.isChecked() == false)
					&& (rdoReprobado.isChecked() == false)) {
				Messagebox
						.show("Debe indicar si el TEG se encuentra Aprobado o Reprobado",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
			} else {
				long auxId2;
				auxId2 = auxId;
				teg1 = servicioTeg.buscarTeg(auxId2);
				Defensa defensa = servicioDefensa.buscarDefensaDadoTeg(teg1);
				final List<ItemDefensa> items = new ArrayList<ItemDefensa>();
				for (int i = 0; i < ltbitem.getItemCount(); i++) {
					Listitem listItem = ltbitem.getItemAtIndex(i);
					String valor = ((Textbox) ((listItem.getChildren().get(1)))
							.getFirstChild()).getValue();
					if (valor.equals("")) {
						Messagebox
								.show("Debe Colocar su Apreciacion en todos los item",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
						i = ltbitem.getItemCount();
						dejeenblanco = true;
					} else {
						ItemEvaluacion item = ltbitem.getItems().get(i)
								.getValue();
						ItemDefensa itemdefensa = new ItemDefensa(item,
								defensa, valor);
						items.add(itemdefensa);
					}

				}
				if (!dejeenblanco) {
					Messagebox
							.show("¿Desea guardar la calificacion del Trabajo Especial de Grado?",
									"Dialogo de confirmacion",
									Messagebox.OK | Messagebox.CANCEL,
									Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener<Event>() {
										public void onEvent(Event evt)
												throws InterruptedException {
											if (evt.getName().equals("onOK")) {
												servicioItemDefensa
														.guardarVarios(items);
												Mencion mencion = servicioMencion.buscar(Long
														.parseLong(cmbMencionTeg
																.getSelectedItem()
																.getId()));
												TegEstatus tegEstatus = new TegEstatus();
												java.util.Date fechaEstatus = new Date();
												if (rdoAprobado.isChecked() == true) {
													String estatus1 = "TEG Aprobado";
													tegEstatus = new TegEstatus(
															0, teg1, estatus1,
															fechaEstatus);
													teg1.setEstatus(estatus1);
												} else if (rdoReprobado
														.isChecked() == true) {
													String estatus2 = "TEG Reprobado";
													tegEstatus = new TegEstatus(
															0, teg1, estatus2,
															fechaEstatus);
													teg1.setEstatus(estatus2);

												}
												Messagebox
														.show("Calificacion guardada exitosamente",
																"Informacion",
																Messagebox.OK,
																Messagebox.INFORMATION);
												Defensa defensa = servicioDefensa
														.buscarDefensaDadoTeg(teg1);
												defensa.setEstatus("Defensa Evaluada");
												teg1.setMencion(mencion);
												servicioDefensa
														.guardarDefensa(defensa);
												servicioTegEstatus
														.guardar(tegEstatus);
												servicioTeg.guardar(teg1);
												actualizarSolicitud();
												salir();
											}
										}
									});
				}
			}
		}
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
		wdwCalificarDefensa.onClose();
	}

	/**
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelar")
	public void cancelarVerificacion() {

		rdoAprobado.setChecked(false);
		rdoReprobado.setChecked(false);
		cmbMencionTeg.setDisabled(true);
		cmbMencionTeg.setValue("");
		for (int i = 0; i < ltbitem.getItemCount(); i++) {

			Listitem listItem = ltbitem.getItemAtIndex(i);
			((Textbox) ((listItem.getChildren().get(1))).getFirstChild())
					.setValue("");
		}

	}

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirCalificarDefensa")
	public void salirCalificarDefensa() {

		wdwCalificarDefensa.onClose();

	}

	/**
	 * Metodo que permite habilitar el combo para seleccionar la mencion del
	 * trabajo
	 */
	@Listen("onCheck  = #rdoAprobado")
	public void habilitarMencion() {

		cmbMencionTeg.setValue("");
		cmbMencionTeg.setDisabled(false);
		cmbMencionTeg.setStyle("margin-left:55px");

	}

	/**
	 * Metodo que permite deshabilitar el combo para seleccionar la mencion del
	 * trabajo
	 */
	@Listen("onCheck  = #rdoReprobado")
	public void deshabilitarMencion() {

		cmbMencionTeg.setValue("");
		cmbMencionTeg.setStyle("margin-left:55px;color:black !important;");
		cmbMencionTeg.setDisabled(true);
		cmbMencionTeg.setValue("No aplica");

	}

}
