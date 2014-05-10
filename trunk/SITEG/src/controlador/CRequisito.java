package controlador;

import java.util.HashMap;

import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoRequisito;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Requisito*/
@Controller
public class CRequisito extends CGeneral {

	private static final long serialVersionUID = -995393394522719964L;
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
	@Wire
	private Window wdwRequisito;
	private long id = 0;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	public void inicializar(Component comp) {
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

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnBuscarRequisito")
	public void buscarRequisito() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoRequisito.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VRequisito");

	}

	/* Metodo que permite el guardado o modificacion de una entidad Requisito */
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
					new org.zkoss.zk.ui.event.EventListener<Event>() {
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
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
								id = 0;
							}
						}
					});

		}
	}

	/* Metodo que permite la eliminacion logica de una entidad Requisito */
	@Listen("onClick = #btnEliminarRequisito")
	public void eliminarRequisito() {

		Messagebox.show("¿Desea eliminar los datos del requisito?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							Requisito requisito = servicioRequisito
									.buscarRequisito(id);
							requisito.setEstatus(false);
							servicioRequisito.guardar(requisito);
							cancelarRequisito();
							Messagebox.show("Requisito eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
						}
					}
				});

	}

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarRequisito")
	public void cancelarRequisito() {
		id = 0;
		txtNombreRequisito.setValue("");
		txtDescripcionRequisito.setValue("");
		btnEliminarRequisito.setDisabled(true);

	}

	/* Metodo que permite cerrar la ventana correspondiente a los requisitos */
	@Listen("onClick = #btnSalirRequisito")
	public void salirRequisito() {

		wdwRequisito.onClose();

	}
	
	/*
	 * Metodo que permite buscar si un requisito existe, de acuerdo al nombre del
	 * requisito
	 */
	@Listen("onChange = #txtNombreRequisito")
	public void buscarNombreRequisito() {
		Requisito requisito = servicioRequisito.buscarPorNombreRequisito(txtNombreRequisito.getValue());
		if (requisito != null) {

			txtNombreRequisito.setValue(requisito.getNombre());
			txtDescripcionRequisito.setValue(requisito.getDescripcion());
			id = requisito.getId();
			btnEliminarRequisito.setDisabled(false);
				
		}

	}
	
	
	
}
