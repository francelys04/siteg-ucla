package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Lapso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SLapso;

@Controller
public class CLapso extends CGeneral {
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
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
	private Listbox ltbLapso;
	@Wire
	private Window wdwCatalogoLapso;
	@Wire
	private Textbox txtCedulaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarLapso;
	@Wire
	private Textbox txtFechaInicialMostrarLapso;
	@Wire
	private Textbox txtFechaFinalMostrarLapso;

	@Wire
	private Button btnEliminarLapso;

	@Wire
	private Button btnGuardarLapso;
	private long id = 0;

	// Metodo para cargar el catalogo de lapsos academicos

	void inicializar(Component comp) {

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		// System.out.println(lapsos.get(0).getNombre());
		if (txtNombreLapso == null) {
			ltbLapso.setModel(new ListModelList<Lapso>(lapsos));
		}

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

	// Muestra el catalogo de lapsos
	@Listen("onClick = #btnCatalogoLapsos")
	public void buscarLapso() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoLapso.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VLapsoAcademico");

	}

	// Guarda un lapso
	@Listen("onClick = #btnGuardarLapso")
	public void guardarLapsoAcademico() {

		if ((txtNombreLapso.getText().compareTo("") == 0)
				|| (dtbInicioLapso.getText().compareTo("") == 0)
				|| (dtbFinLapso.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else if(dtbInicioLapso.getValue().after(dtbFinLapso.getValue())){
			Messagebox.show("La fecha de fin de lapso debe ser posterior a la fecha de inicio", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else{
			Messagebox.show("¿Desea guardar los datos del lapso académico?",
					"Dialogo de confirmación", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
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
								cancelarLapso();
								id = 0;
								Messagebox
										.show("Lapso académico registrado exitosamente",
												"Información", Messagebox.OK,
												Messagebox.INFORMATION);
							}
						}
					});

		}
	}

	// Elimina un lapso
	@Listen("onClick = #btnEliminarLapso")
	public void eliminarLapso() {
		Messagebox.show("¿Desea eliminar los datos de la actividad?",
				"Dialogo de confirmación", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Lapso lapso = servicioLapso.buscarLapso(id);
							lapso.setEstatus(false);
							servicioLapso.guardar(lapso);
							cancelarLapso();
							Messagebox.show("Lapso académico eliminado exitosamente",
									"Información", Messagebox.OK,
									Messagebox.INFORMATION);

						}
					}
				});

	}

	// Coloca todos los campos en blanco
	@Listen("onClick = #btnCancelarLapso")
	public void cancelarLapso() {
		btnEliminarLapso.setDisabled(true);
		txtNombreLapso.setValue("");
		dtbInicioLapso.setValue(null);
		dtbFinLapso.setValue(null);
		btnEliminarLapso.setDisabled(true);
	}

}
