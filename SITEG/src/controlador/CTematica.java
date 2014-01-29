package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Requisito;
import modelo.Tematica;
import modelo.AreaInvestigacion;

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

import servicio.SPrograma;
import servicio.STematica;
import servicio.SAreaInvestigacion;
import configuracion.GeneradorBeans;

//es un controlador de tematica y su catalogo
@Controller
public class CTematica extends CGeneral {

	// servicios para los dos modelos implicados
	STematica servicioTematica = GeneradorBeans.getSTematica();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	CCatalogoTematica catalogo = new CCatalogoTematica();

	// atributos de la vista tematica
	@Wire
	private Combobox cmbAreaTematica;
	@Wire
	private Textbox txtNombreTematica;
	@Wire
	private Textbox txtDescripcionTematica;

	// atributos de la pantalla del catalogo
	@Wire
	private Listbox ltbTematica;
	@Wire
	private Window wdwCatalogoTematica;
	@Wire
	private Textbox txtCodigoMostrarTematica;
	@Wire
	private Textbox txtNombreMostrarTematica;
	@Wire
	private Textbox txtAreaMostrarTematica;
	@Wire
	private Textbox txtDescripcionMostrarTematica;
	@Wire
	private Button btnEliminarTematica;
	@Wire
	private Window wdwTematica;
	private long id = 0;

	// metodo para llenar combo y mapear
	void inicializar(Component comp) {

		List<AreaInvestigacion> areas = servicioArea.buscarActivos();
		List<Tematica> tematicas = servicioTematica.buscarActivos();

		if (cmbAreaTematica == null) {
			ltbTematica.setModel(new ListModelList<Tematica>(tematicas));
		} else {
			cmbAreaTematica
					.setModel(new ListModelList<AreaInvestigacion>(areas));

		}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				Tematica tematica = servicioTematica.buscarTematica(codigo);
				txtNombreTematica.setValue(tematica.getNombre());
				txtDescripcionTematica.setValue(tematica.getDescripcion());
				cmbAreaTematica.setValue(tematica.getareaInvestigacion()
						.getNombre());
				btnEliminarTematica.setDisabled(false);
				id = tematica.getId();
				map.clear();
				map = null;
			}
		}

	}

	// al darle al boton me muestra el catalogo
	@Listen("onClick = #btnBuscarTematica")
	public void buscarTematica() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoTematica.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VTematica");

	}

	// guarda al darle clic
	@Listen("onClick = #btnGuardarTematica")
	public void guardarTematica() {

		if ((cmbAreaTematica.getText().compareTo("") == 0)
				|| (txtNombreTematica.getText().compareTo("") == 0)
				|| (txtDescripcionTematica.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("¿Desea guardar los datos de la tematica?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreTematica.getValue();
								String descripcion = txtDescripcionTematica
										.getValue();
								String areas = cmbAreaTematica.getValue();

								Boolean estatus = true;
								AreaInvestigacion areainvestigacion = servicioArea
										.buscarAreaPorNombre(areas);

								Tematica tematica = new Tematica(id, nombre,
										descripcion, estatus, areainvestigacion);

								servicioTematica.guardar(tematica);
								cancelarTematica();
								Messagebox.show(
										"Tematica registrada exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
								id = 0;
								
							}
						}
					});

		}
	}

	// elimina al darle clic
	@Listen("onClick = #btnEliminarTematica")
	public void eliminarTematica() {
		Messagebox.show("Â¿Desea eliminar los datos de la tematica?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							Tematica tematica = servicioTematica.buscarTematica(id);
							tematica.setEstatus(false);
							servicioTematica.guardar(tematica);
							cancelarTematica();
							Messagebox.show(
									"Tematica eliminada exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
							
						}
					}
				});
		
		
	}

	// coloca los campos de la vista en blanco
	@Listen("onClick = #btnCancelarTematica")
	public void cancelarTematica() {
		id = 0;
		txtNombreTematica.setValue("");
		txtDescripcionTematica.setValue("");
		cmbAreaTematica.setValue("");
		btnEliminarTematica.setDisabled(true);

	}
	
	@Listen("onClick = #btnSalirTematica")
	public void salirTematica() {
		
		wdwTematica.onClose();
		

	}

}
