package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Requisito;
import modelo.TipoJurado;

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

import servicio.STipoJurado;
import configuracion.GeneradorBeans;

@Controller
public class CTipoJurado extends CGeneral {

	STipoJurado servicioTipoJurado = GeneradorBeans.getServicioTipoJurado();

	CCatalogoTipoJurado catalogo = new CCatalogoTipoJurado();

	@Wire
	private Textbox txtNombreTipoJurado;
	@Wire
	private Textbox txtDescripcionTipoJurado;
	@Wire
	private Textbox txtNombreMostrarTipoJurado;
	@Wire
	private Textbox txtDescripcionMostrarTipoJurado;
	@Wire
	private Listbox ltbTipoJurado;
	@Wire
	private Window wdwCatalogoTipoJurado;
	@Wire
	private Button btnEliminarTipoJurado;
	private long id = 0;

	void inicializar(Component comp) {

		List<TipoJurado> tipoJurado = servicioTipoJurado.buscarActivos();

		if (txtNombreTipoJurado == null) {
			ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado));
		}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		// Aca se hace un mapeo para asignar los valores a las caja de textos
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				TipoJurado tipoJurado2 = servicioTipoJurado
						.buscarTipoJurado(codigo);
				txtNombreTipoJurado.setValue(tipoJurado2.getNombre());
				txtDescripcionTipoJurado.setValue(tipoJurado2.getDescripcion());
				btnEliminarTipoJurado.setDisabled(false);
				id = tipoJurado2.getId();
				map.clear();
				map = null;
			}
		}
	}

	// Aca se muestra el catalogo de los Tipo de Jurado Registrados
	@Listen("onClick = #btnCatalogoTipoJurado")
	public void buscarTipoJurado() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoTipoJurado.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VTipoJurado");

	}

	// Aca se guardan los Tipo de Jurado
	@Listen("onClick = #btnGuardarTipoJurado")
	public void guardarTipoJurado() {

		if ((txtNombreTipoJurado.getText().compareTo("") == 0)
				|| (txtDescripcionTipoJurado.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("¿Desea guardar los datos del tipo de jurado?",
					"Dialogo de confirmación", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreTipoJurado.getValue();
								String descripcion = txtDescripcionTipoJurado
										.getValue();
								Boolean estatus = true;

								TipoJurado tipoJurado = new TipoJurado(id,
										nombre, descripcion, estatus);
								servicioTipoJurado.guardar(tipoJurado);
								cancelarTipoJurado();
								Messagebox.show(
										"Tipo de jurado registrado exitosamente",
										"Información", Messagebox.OK,
										Messagebox.INFORMATION);
								id = 0;

							}
						}
					});

		}

	}

	// Aca se eliminan logicamente los Tipo de Jurado
	@Listen("onClick = #btnEliminarTipoJurado")
	public void eliminarTipoJurado() {
		
		Messagebox.show("¿Desea eliminar los datos del tipo de jurado?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							TipoJurado tipoJurado = servicioTipoJurado.buscarTipoJurado(id);
							tipoJurado.setEstatus(false);
							servicioTipoJurado.guardar(tipoJurado);
							cancelarTipoJurado();
							Messagebox.show(
									"Tipo de jurado eliminado exitosamente",
									"Información", Messagebox.OK,
									Messagebox.INFORMATION);
						}
					}
				});
		
		
		
	}

	// Aca se mandan a limpiar los campos de textos de la vista
	@Listen("onClick = #btnCancelarTipoJurado")
	public void cancelarTipoJurado() {
		id = 0;
		txtNombreTipoJurado.setValue("");
		txtDescripcionTipoJurado.setValue("");
		btnEliminarTipoJurado.setDisabled(true);
	}
}
