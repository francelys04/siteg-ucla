package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Requisito;

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

import servicio.SRequisito;

@Controller
public class CRequisito extends CGeneral {
	SRequisito servicioRequisito = GeneradorBeans.getServicioRequisito();

	CCatalogoRequisito catalogo = new CCatalogoRequisito();

	@Wire
	private Textbox txtNombreRequisito;
	@Wire
	private Textbox txtDescripcionRequisito;
	@Wire
	private Listbox ltbRequisito;
	@Wire
	private Window wdwCatalogoRequisito;
	@Wire
	private Textbox txtNombreMostrarRequisito;
	@Wire
	private Textbox txtDescripcionMostrarRequisito;
	@Wire
	private Button btnEliminarRequisito;
	private long id = 0;

	void inicializar(Component comp) {
		//Permite llenar los datos del requisito en la vista 
		//si el map es diferente a null
			List<Requisito> requisitos = servicioRequisito.buscarActivos();

		if (txtNombreRequisito == null)
			ltbRequisito.setModel(new ListModelList<Requisito>(requisitos));

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				Requisito requisito = servicioRequisito.buscarRequisito(codigo);
				txtNombreRequisito.setValue(requisito.getNombre());
				txtDescripcionRequisito.setValue(requisito.getDescripcion());
				btnEliminarRequisito.setDisabled(false);
				id = codigo;
				map.clear();
				map = null;
			}
		}

	}
	//Permite ver la lis de requisitos activos
	@Listen("onClick = #btnBuscarRequisito")
	public void buscarRequisito() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoRequisito.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VRequisito");

	}
	//Permite guardar un requisito
	@Listen("onClick = #btnGuardarRequisito")
	public void guardarRequisito() {

		if ((txtNombreRequisito.getText().compareTo("") == 0)
				|| (txtDescripcionRequisito.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("¿Desea guardar los datos del requisito?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreRequisito.getValue();
								String descripcion = txtDescripcionRequisito
										.getValue();
								Boolean estatus = true;
								Requisito requisito = new Requisito(id, nombre,
										descripcion, estatus);
								servicioRequisito.guardar(requisito);
								cancelarRequisito();
								Messagebox.show(
										"Requisito registrado exitosamente",
										"Informaciï¿½n", Messagebox.OK,
										Messagebox.INFORMATION);
								id = 0;
							}
						}
					});

		}
	}

	@Listen("onClick = #btnEliminarRequisito")
	public void eliminarRequisito() {

		Messagebox.show("¿Desea eliminar los datos del requisito?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							Requisito requisito = servicioRequisito
									.buscarRequisito(id);
							requisito.setEstatus(false);
							servicioRequisito.guardar(requisito);
							cancelarRequisito();
							Messagebox.show(
									"Requisito eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
						}
					}
				});

	}

	@Listen("onClick = #btnCancelarRequisito")
	public void cancelarRequisito() {
		id = 0;
		txtNombreRequisito.setValue("");
		txtDescripcionRequisito.setValue("");
		btnEliminarRequisito.setDisabled(true);

	}

	

}
