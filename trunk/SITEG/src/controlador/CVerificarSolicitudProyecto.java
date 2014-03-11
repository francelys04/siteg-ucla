package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import modelo.Estudiante;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.TegRequisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite verificar si un proyecto de trabajo de grado
 * cumple con los requisitos establecidos por el programa
 */
@Controller
public class CVerificarSolicitudProyecto extends CGeneral {

	private static final long serialVersionUID = 8102725322638413636L;
	private Programa programa = new Programa();
	private static long auxId = 0;
	private static String vistaRecibida;
	private static int numero;
	ArrayList<Boolean> valor = new ArrayList<Boolean>();
	@Wire
	private Textbox txtProgramaRegistrarAvances;
	@Wire
	private Textbox txtAreaVerificar;
	@Wire
	private Textbox txtTematicaVerificar;
	@Wire
	private Textbox txtTituloVerificar;
	@Wire
	private Textbox txtNombreTutorVerificar;
	@Wire
	private Textbox txtCedulaTutorVerificar;
	@Wire
	private Textbox txtApellidoTutorVerificar;
	@Wire
	private Listbox ltbSolcitudRegistroProyecto;
	@Wire
	private Textbox txtObservacion;
	@Wire
	private Listbox ltbEstudiantesTeg;
	@Wire
	private Listbox ltbRequisito;
	@Wire
	private Listbox ltbRequisitosDisponibles;
	@Wire
	private Listbox ltbRequisitosSeleccionadas;
	@Wire
	private Radiogroup rdgRequisitos;
	@Wire
	private Radio rdoCompleto;
	@Wire
	private Radio rdoIncompleto;
	@Wire
	private Window wdwVerificarSolicitudProyecto;
	@Wire
	private Datebox db1;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(codigo);
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg2);
				txtProgramaRegistrarAvances.setValue(est.get(0).getPrograma()
						.getNombre());
				txtNombreTutorVerificar.setValue(teg2.getTutor().getNombre());
				txtApellidoTutorVerificar.setValue(teg2.getTutor()
						.getApellido());
				txtTituloVerificar.setValue(teg2.getTitulo());
				txtAreaVerificar.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaVerificar.setValue(teg2.getTematica().getNombre());

				ltbEstudiantesTeg.setModel(new ListModelList<Estudiante>(est));
				programa = est.get(0).getPrograma();
				llenarRequisitos(programa, teg2);
				map.clear();
				map = null;
			}
		}

	}

	/*
	 * Metodo que permite mover uno o varios requisitos hacia la lista de
	 * requisitos entregados
	 */
	@Listen("onClick = #btnAgregarRequisitos")
	public void moverDerechaRequisitos() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbRequisitosDisponibles
				.getModel()).getSelection();
		((ListModelList) ltbRequisitosSeleccionadas.getModel())
				.addAll(selectedItems);
		((ListModelList) ltbRequisitosDisponibles.getModel())
				.removeAll(selectedItems);
	}

	/*
	 * Metodo que permite mover uno o varios requisitos de la lista de
	 * requisitos entregados a la lista de la izquierda (requisitos
	 * disponibles).
	 */
	@Listen("onClick = #btnRemoverRequisitos")
	public void moverIzquierdaRequisitos() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbRequisitosSeleccionadas
				.getModel()).getSelection();
		((ListModelList) ltbRequisitosDisponibles.getModel())
				.addAll(selectedItems);
		((ListModelList) ltbRequisitosSeleccionadas.getModel())
				.removeAll(selectedItems);
	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado origianl
	 */
	@Listen("onClick = #btnCancelar")
	public void cancelarVerificacion() {
		long auxId3 = auxId;
		Teg teg1 = servicioTeg.buscarTeg(auxId3);
		llenarRequisitos(programa, teg1);
		rdoCompleto.setChecked(false);
		rdoIncompleto.setChecked(false);
		txtObservacion.setValue("");
	}

	/*
	 * Metodo que permite guardar los requisitos que han sido entregados por el
	 * estudiante, cambiando el estatus de TEG asi como tambien actualizando los
	 * cambios en la respectiva tabla de historial. Ademas se envia un correo al
	 * estudiante informandole sobre el estatus de su teg
	 */
	@Listen("onClick = #btnGuardar")
	public void GuardarVerificacion() {
		if (ltbRequisitosSeleccionadas.getItems().size() == 0) {
			Messagebox.show("Debe seleccionar al menos un requisito",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			if ((rdoCompleto.isChecked() == false)
					&& (rdoIncompleto.isChecked() == false)) {
				Messagebox
						.show("Debe indicar si los requisitos estan completos o incompletos",
								"Advertencia", Messagebox.OK,
								Messagebox.EXCLAMATION);
			} else {
				Messagebox.show(
						"¿Desea guardar la verificacion de los requisitos?",
						"Dialogo de confirmacion", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {

									long auxId2;
									auxId2 = auxId;
									Teg teg1 = servicioTeg.buscarTeg(auxId2);
									List<TegRequisito> tegRequisitos = new ArrayList<TegRequisito>();
									for (int i = 0; i < ltbRequisitosSeleccionadas
											.getItemCount(); i++) {
										Requisito requisitos = ltbRequisitosSeleccionadas
												.getItems().get(i).getValue();
										TegRequisito teg = new TegRequisito(
												requisitos, teg1, db1
														.getValue());
										tegRequisitos.add(teg);
									}
									servicioTegRequisito.guardar(tegRequisitos);

									if (rdoCompleto.isChecked() == false) {
										if (txtObservacion.getValue()
												.compareTo("") == 0) {
											txtObservacion
													.setValue("Sus requisitos estan incorrectos o incompletos");
										}
										for (int i = 0; i < ltbEstudiantesTeg
												.getItemCount(); i++) {
											Estudiante estudiante = ltbEstudiantesTeg
													.getItems().get(i)
													.getValue();
											valor.add(enviarEmailNotificacion(
													estudiante
															.getCorreoElectronico(),
													txtObservacion.getValue()));
											Messagebox
													.show("Datos guardados exitosamente",
															"Informacion",
															Messagebox.OK,
															Messagebox.INFORMATION);
											salir();

										}
									}

									else if (rdoCompleto.isChecked() == true) {
										Teg teg = servicioTeg.buscarTeg(auxId2);
										String estatus = "Proyecto Registrado";
										teg.setEstatus(estatus);

										/* Guardar datos en la tabla teg_estatus */
										java.util.Date fechaEstatus = new Date();
										TegEstatus tegEstatus = new TegEstatus(
												0, teg, "Proyecto Registrado",
												fechaEstatus);
										servicioTegEstatus.guardar(tegEstatus);

										servicioTeg.guardar(teg);
										Messagebox.show(
												"Datos guardados exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
										salir();

									}
								}
							}
						});
			}
		}
	}

	/*
	 * Metodo que permite llenar las listas de requisitos consignados y no
	 * consignados para el teg
	 */
	public void llenarRequisitos(Programa programa, Teg teg) {

		Lapso lapso = servicioLapso.buscarLapsoVigente();
		List<Requisito> requisitosDerecha = servicioTegRequisito
				.buscarTegRequisitoSeleccionados(teg);
		List<Requisito> requisitoIzquierda = new ArrayList<Requisito>();
		requisitoIzquierda = servicioTegRequisito.buscarTegRequisitoDisponible(
				programa, lapso, teg);
		ltbRequisitosDisponibles.setModel(new ListModelList<Requisito>(
				requisitoIzquierda));

		ltbRequisitosSeleccionadas.setModel(new ListModelList<Requisito>(
				requisitosDerecha));
		numero = requisitosDerecha.size();
		ltbRequisitosDisponibles.setMultiple(true);
		ltbRequisitosSeleccionadas.setMultiple(true);

	}

	/* Metodo que permite cerrar la pantalla actualizando los cambios realizados */
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwVerificarSolicitudProyecto.onClose();
	}

	/*
	 * Metodo que permite recibir el nombre del catalogo a la cual esta asociada
	 * esta vista para asi poder realizar las operaciones sobre dicha vista
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirVerificarSolicitud")
	public void salirVerificarSolicitud() {

		wdwVerificarSolicitudProyecto.onClose();

	}

}
