package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Profesor;
import modelo.Tematica;

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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SProfesor;
import servicio.STematica;
import configuracion.GeneradorBeans;

@Controller
public class CProfesorTematicas extends CGeneral {
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	STematica serviciotematica = GeneradorBeans.getSTematica();
	CCatalogoProfesor catalogo = new CCatalogoProfesor();

	@Wire
	private Textbox txtCedulaProfesorTematica;
	@Wire
	private Listbox ltbProfesor;

	@Wire
	private Textbox txtApellidoProfesorTematica;

	@Wire
	private Textbox txtNombreProfesorTematica;

	@Wire
	private Listbox lsbTematicasProfesorDisponibles;
	@Wire
	private Listbox lsbTematicasProfesorSeleccionadas;
	@Wire
	private Window wdwProfesorTematica;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		// permite llenar los datos del catlago a la vista si el map es
		// disferente de null
		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesorTematica.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(txtCedulaProfesorTematica
								.getValue());
				txtNombreProfesorTematica.setValue(profesor.getNombre());
				txtApellidoProfesorTematica.setValue(profesor.getApellido());
				llenaListas();
				map.clear();
				map = null;
			}
		}

	}

	// permite ver la lista de profesores activos
	@Listen("onClick = #btnCatalogoProfesorTematica")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
		window.doModal();

		catalogo.recibir("maestros/VProfesorTematica");

	}

	// permite mover una tematica de disponible a seleccionada
	@Listen("onClick = #btnAgergarProfesorTematicas")
	public void moverDerechaTematica() {

		Set selectedItems = ((org.zkoss.zul.ext.Selectable) lsbTematicasProfesorDisponibles
				.getModel()).getSelection();
            System.out.println(selectedItems.size());
		((ListModelList) lsbTematicasProfesorSeleccionadas.getModel())
				.addAll(selectedItems);
		((ListModelList) lsbTematicasProfesorDisponibles.getModel())
				.removeAll(selectedItems);
		
	}

	// permite mover una tematica de seleccionada a disponible
	@Listen("onClick = #btnRemoverProfesorTematicas")
	public void moverIzquierdaArea() {
		
		Set selectedItems = ((org.zkoss.zul.ext.Selectable)lsbTematicasProfesorSeleccionadas.getModel()).getSelection();
		((ListModelList)lsbTematicasProfesorDisponibles.getModel()).addAll(selectedItems);
		((ListModelList)lsbTematicasProfesorSeleccionadas.getModel()).removeAll(selectedItems);
	}

	// permite guardar la asignacion de tematicas a un profesor
	@Listen("onClick = #btnGuardarProfesorTematicas")
	public void guardar() {
		if (lsbTematicasProfesorSeleccionadas.getItemCount() != 0) {
			Messagebox.show("�Desea guardar la configuracion realizada?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								Profesor profesor = servicioProfesor
										.buscarProfesorPorCedula(txtCedulaProfesorTematica
												.getValue());

								Set<Tematica> tematicasProfesor = new HashSet<Tematica>();
								for (int i = 0; i < lsbTematicasProfesorSeleccionadas
										.getItemCount(); i++) {
									Tematica tema = lsbTematicasProfesorSeleccionadas
											.getItems().get(i).getValue();
									tematicasProfesor.add(tema);
								}

								profesor.setTematicas(tematicasProfesor);
								servicioProfesor.guardarProfesor(profesor);
								limpiarCampos();
								Messagebox.show(
										"Configuracion guardada con exito",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});
		} else {
			Messagebox.show("Debe seleccionar al menos una tematica",
					"Informacion", Messagebox.OK, Messagebox.INFORMATION);

		}
	}

	// Limpia los campos
	@Listen("onClick = #btnCancelarProfesorTematicas")
	public void limpiarCampos() {
		txtCedulaProfesorTematica.setValue("");
		txtApellidoProfesorTematica.setValue("");
		txtNombreProfesorTematica.setValue("");
		lsbTematicasProfesorDisponibles.getItems().clear();
		lsbTematicasProfesorSeleccionadas.getItems().clear();
	}

	// llena la lista de tematicas disponibles y seleccionadas
	private void llenaListas() {

		String ProfesorCedula = txtCedulaProfesorTematica.getValue();
		Profesor profesor = servicioProfesor
				.buscarProfesorPorCedula(ProfesorCedula);
		List<Tematica> tematicasDerecha = serviciotematica
				.buscarTematicasDelProfesor(profesor);

		if (tematicasDerecha.size() == 0) {

			List<Tematica> tema = serviciotematica.buscarActivos();
			lsbTematicasProfesorDisponibles
					.setModel(new ListModelList<Tematica>(tema));
			lsbTematicasProfesorSeleccionadas
			.setModel(new ListModelList<Tematica>());

		} else {

			lsbTematicasProfesorSeleccionadas
					.setModel(new ListModelList<Tematica>(tematicasDerecha));

			List<Long> ids = new ArrayList<Long>();
			for (int i = 0; i < tematicasDerecha.size(); i++) {
				long id = tematicasDerecha.get(i).getId();
				ids.add(id);
			}
			if (ids.toString() != "[]") {
				List<Tematica> tematicasIzquierda = serviciotematica
						.buscarTematicasSinProfesor(ids);
				lsbTematicasProfesorDisponibles
						.setModel(new ListModelList<Tematica>(
								tematicasIzquierda));
			}

		}

		lsbTematicasProfesorSeleccionadas.setMultiple(true);
		lsbTematicasProfesorDisponibles.setMultiple(true);

	}

	// Limpia los campos
	@Listen("onClick = #btnSalirProfesorTematicas")
	public void salirProfesorTematicas() {

		wdwProfesorTematica.onClose();

	}

}