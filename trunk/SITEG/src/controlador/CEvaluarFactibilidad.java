package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.ItemFactibilidad;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SFactibilidad;
import servicio.SProgramaItem;
import servicio.STeg;
import servicio.SItemFactibilidad;
import servicio.SLapso;
import servicio.SProgramaItem;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CEvaluarFactibilidad extends CGeneral {

	@Wire
	private Datebox db1;
	@Wire
	private Listbox ltbListaFactibilidad;

	@Wire
	private Listbox ltbItemsFactibilidad;
	@Wire
	private Textbox txtProgramaEvaluarFactibilidad;
	@Wire
	private Textbox txtAreaEvaluarFactibilidad;
	@Wire
	private Textbox txtTematicaEvaluarFactibilidad;
	@Wire
	private Textbox txtTituloEvaluarFactibilidad;
	@Wire
	private Textbox txtObservacionEvaluarFactibilidad;
	@Wire
	private Textbox txtNombreTutorEvaluarFactibilidad;
	@Wire
	private Textbox txtApellidoTutorEvaluarFactibilidad;
	@Wire
	private Listbox ltbEstudianteEvaluarFactibilidad;
	@Wire
	private Listbox ltbComisionEvaluarFactibilidad;
	@Wire
	private Window wdwEvaluarFactibilidad;
	@Wire
	private Window wdwCatalogoEvaluarFactibilidad;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;
	private static Programa programa;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);

				// se toma es el programa del estudiante asociado a el teg.
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(teg2);

				if (estudiantes.size() != 0) {
					programa = estudiantes.get(0).getPrograma();
				}

				ltbEstudianteEvaluarFactibilidad
						.setModel(new ListModelList<Estudiante>(estudiantes));

				txtProgramaEvaluarFactibilidad.setValue(programa.getNombre());
				txtAreaEvaluarFactibilidad.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaEvaluarFactibilidad.setValue(teg2.getTematica()
						.getNombre());

				txtNombreTutorEvaluarFactibilidad.setValue(teg2.getTutor()
						.getNombre());
				txtApellidoTutorEvaluarFactibilidad.setValue(teg2.getTutor()
						.getApellido());
				txtTituloEvaluarFactibilidad.setValue(teg2.getTitulo());

				Lapso lapso = servicioLapso.buscarLapsoVigente();
				List<ItemEvaluacion> item = servicioProgramaItem
						.buscarItemsEnPrograma(programa, lapso);
				List<ItemEvaluacion> item2 = new ArrayList<ItemEvaluacion>();
				for (int i = 0; i < item.size(); i++) {
					if (item.get(i).getTipo().equals("Factibilidad")) {
						item2.add(item.get(i));
					}

				}
				ltbItemsFactibilidad
						.setModel(new ListModelList<ItemEvaluacion>(item2));

			}
		}

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// Permite salir y refrescar las vistas (Evaluar factibilidad y catalogo)
	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwEvaluarFactibilidad.onClose();
	}

	// Permite guardar los datos de la factibilidad
	@Listen("onClick = #btnGuardarEvaluacionFactibilidad")
	public void guardar() {

		long id = 0;
		Teg teg = servicioTeg.buscarTeg(auxiliarId);
		Profesor profesor = ObtenerUsuarioProfesor();
		Date fecha = db1.getValue();
		String Observacion = txtObservacionEvaluarFactibilidad.getValue();
		String estatus = "Evaluando Factibilidad";
		Factibilidad factibilidad3 = servicioFactibilidad
				.buscarFactibilidadPorTeg(teg);

		boolean registrefactibilidad = false;
		Factibilidad factibilidad = new Factibilidad(id, teg, profesor, fecha,
				Observacion, estatus);
		if (txtObservacionEvaluarFactibilidad.getValue().compareTo("") == 0) {
			Messagebox
					.show("Debe ingresar una Observacion sobre la evaluacion realizada",
							"Error", Messagebox.OK, Messagebox.ERROR);

		} else {
			if (factibilidad3 == null) {

				servicioFactibilidad.guardar(factibilidad);
				registrefactibilidad = true;
			} else {
				registrefactibilidad = true;
			}
		}

		if (registrefactibilidad == true) {

			boolean dejeenblanco = false;

			Factibilidad factibilidad2 = servicioFactibilidad
					.buscarFactibilidadPorTeg(teg);

			for (int i = 0; i < ltbItemsFactibilidad.getItemCount(); i++) {

				Listitem listItem = ltbItemsFactibilidad.getItemAtIndex(i);
				String valor = ((Textbox) ((listItem.getChildren().get(1)))
						.getFirstChild()).getValue();
				System.out.print(valor);
				if (valor.equals("")) {
					Messagebox
							.show("Debe ingresar una Apreciacion en cada uno de los item de evaluacion",
									"Error", Messagebox.OK, Messagebox.ERROR);
					i = ltbItemsFactibilidad.getItemCount();
					dejeenblanco = true;

				} else {
					ItemEvaluacion item = ltbItemsFactibilidad.getItems()
							.get(i).getValue();
					ItemFactibilidad itemfactibilidad = new ItemFactibilidad(
							item, factibilidad2, valor);
					servicioItemFactibilidad.guardar(itemfactibilidad);
				}
			}

			if (dejeenblanco == false) {

				Messagebox
						.show("¿Desea guardar los datos de la evaluacion de factibilidad?",
								"Dialogo de confirmacion", Messagebox.OK
										| Messagebox.CANCEL,
								Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {
									public void onEvent(Event evt)
											throws InterruptedException {
										if (evt.getName().equals("onOK")) {
											
											Teg tegFactibilidad = servicioTeg.buscarTeg(auxiliarId);

											String estatus1 = "Factibilidad Evaluada";
											tegFactibilidad.setEstatus(estatus1);

											/*
											 * Guardar datos en la tabla
											 * teg_estatus
											 */
											java.util.Date fechaEstatus = new Date();
											TegEstatus tegEstatus = new TegEstatus(
													0, tegFactibilidad,
													"Factibilidad Evaluada",
													fechaEstatus);
											servicioTegEstatus
													.guardar(tegEstatus);

											servicioTeg.guardar(tegFactibilidad);
											Messagebox
													.show("Datos de la evaluacion registrados exitosamente",
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

	@Listen("onClick = #btnCancelarEvaluacionFactibilidad")
	public void cancelar() {
		txtObservacionEvaluarFactibilidad.setValue("");
		for (int i = 0; i < ltbItemsFactibilidad.getItemCount(); i++) {

			Listitem listItem = ltbItemsFactibilidad.getItemAtIndex(i);
			((Textbox) ((listItem.getChildren().get(1))).getFirstChild())
					.setValue("");
		}

	}

	@Listen("onClick = #btnSalirEvaluacionFactibilidad")
	public void salirEvaluarFactibilidad() {

		wdwEvaluarFactibilidad.onClose();

	}

}
