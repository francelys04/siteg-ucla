package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Programa;
import modelo.ProgramaArea;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegRequisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
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
import servicio.SLapso;
import servicio.SPrograma;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.STeg;
import servicio.STegRequisito;
import configuracion.GeneradorBeans;

@Controller
public class CVerificarSolicitudProyecto extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SRequisito servicioRequisito = GeneradorBeans.getServicioRequisito();
	STegRequisito servicioTegRequisito = GeneradorBeans
			.getServicioTegRequisito();
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
	private Textbox txtTutorVerificarSolicitud;
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

	private long id = 0;
	private static long auxId = 0;
	private static long auxIdP = 0;
	private static String vistaRecibida;
	private static int numero;
	ArrayList<Boolean> valor = new ArrayList<Boolean>();
	SProgramaRequisito servicioRequisitoPrograma = GeneradorBeans
			.getServicioProgramaRequisito();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		// permite obteber los datos del teg seleccionado en el catalogo
		// si el map es diferente de null
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
				// programa por el estudiante del primer estudiante del teg2
				txtProgramaRegistrarAvances.setValue(est.get(0).getPrograma()
						.getNombre());
				txtTutorVerificarSolicitud.setValue(teg2.getTutor().getNombre() + " " + teg2.getTutor().getApellido());
				txtTituloVerificar.setValue(teg2.getTitulo());
				txtAreaVerificar.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaVerificar.setValue(teg2.getTematica().getNombre());

				ltbEstudiantesTeg.setModel(new ListModelList<Estudiante>(est));

				id = est.get(0).getPrograma().getId();
				llenarRequisitos(id, teg2);

				map.clear();
				map = null;
			}
		}

	}

	// permite mover un requisito disponible a seleccionado
	@Listen("onClick = #btnAgregarRequisitos")
	public void moverDerechaRequisitos() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbRequisitosDisponibles
				.getModel()).getSelection();
		((ListModelList) ltbRequisitosSeleccionadas.getModel())
				.addAll(selectedItems);
		((ListModelList) ltbRequisitosDisponibles.getModel())
				.removeAll(selectedItems);
	}

	// permite mover un requisito seleccionado a disponible si este no
	// esta guarda en base de datos
	@Listen("onClick = #btnRemoverRequisitos")
	public void moverIzquierdaRequisitos() {
		Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbRequisitosSeleccionadas
				.getModel()).getSelection();
		((ListModelList) ltbRequisitosDisponibles.getModel())
				.addAll(selectedItems);
		((ListModelList) ltbRequisitosSeleccionadas.getModel())
				.removeAll(selectedItems);
	}

	// limpia los campos
	@Listen("onClick = #btnCancelar")
	public void cancelarVerificacion() {
		long auxId3 = auxId;
		Teg teg1 = servicioTeg.buscarTeg(auxId3);

		// para guiarse por el programa del estudiante
		List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg1);
		long id = est.get(0).getPrograma().getId();
		llenarRequisitos(id, teg1);
		rdoCompleto.setChecked(false);
		rdoIncompleto.setChecked(false);
		txtObservacion.setValue("");
	}

	// permite guardar la verificacion de requisitos
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
						new org.zkoss.zk.ui.event.EventListener() {
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
										servicioTeg.guardar(teg);
										Messagebox
										.show("Datos guardados exitosamente",
												"Informacion",
												Messagebox.OK,
												Messagebox.INFORMATION);
								salir();

									}
								}
							}
						});
			}
		}
	}

	// llena la lista de requisitps disponibles y seleccionados
	public void llenarRequisitos(long id, Teg teg) {

		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa = servicioPrograma.buscar(id);

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

	// permite salir y refrescar las vistas
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwVerificarSolicitudProyecto.onClose();
	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	@Listen("onClick = #btnSalirVerificarSolicitud")
	public void salirVerificarSolicitud() {

		wdwVerificarSolicitudProyecto.onClose();

	}

}
