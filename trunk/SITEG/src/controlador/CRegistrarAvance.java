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

import modelo.Estudiante;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegEstatus;

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

import servicio.SLapso;
import servicio.SCondicionPrograma;
import configuracion.GeneradorBeans;
import servicio.SAvance;
import servicio.seguridad.SUsuario;

public class CRegistrarAvance extends CGeneral {
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
	private static boolean estatusTeg;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
		estatusTeg = false;

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");
		// permite traer los datos del teg a la vista si el map es diferente de
		// nulo
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

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// permite salir y refrescar las vistas
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwRegistrarAvance.onClose();
	}

	// guarda las observacion en la base de datos
	@Listen("onClick = #btnAgregarObservacionrAvance")
	public void guardarAvance() {

		if ((txtObservacionRegistrarAvance.getText().compareTo("") == 0)) {
			Messagebox
					.show("Debe agregar las observaciones respectivas del avance del Trabajo Especial de Grado",
							"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show(
					"¿Desea guardar el avance del Trabajo Especial de Grado?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Teg tegAvance = servicioTeg
										.buscarTeg(auxiliarId);

								if (tegAvance.getEstatus().equals("Proyecto en Desarrollo")){
									
									estatusTeg = true;
								}
									
									
								
								if(estatusTeg == false){
									
									/*
									 * Guardar estatus de Proyecto en
									 * Desarrrollo en el TEG
									 */
									tegAvance
											.setEstatus("Proyecto en Desarrollo");
									servicioTeg.guardar(tegAvance);

									/* Guardar datos en la tabla teg_estatus */
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

	// Permite finalizar los avance cambia el estatus del teg
	@Listen("onClick = #btnFinalizarRegistrarAvance")
	public void finalizarRegistrarAvance() {

		Messagebox.show("¿Desea finalizar los avances del proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							Teg tegAvance = servicioTeg.buscarTeg(auxiliarId);
							tegAvance.setEstatus("Avances Finalizados");

							/* Guardar datos en la tabla teg_estatus */
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

	// limpia los campos modificables de la vista
	@Listen("onClick = #btnCancelarRegistrarAvance")
	public void cancelarRegistrarAvance() {

		txtObservacionRegistrarAvance.setValue("");

	}

	// permite llenar la lista de avances para que estas se puedan observar
	// para que se tome la desicion de finalizar
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

	@Listen("onClick = #btnSalirRegistrarAvance")
	public void salirRegistrarAvance() {

		wdwRegistrarAvance.onClose();

	}

}
