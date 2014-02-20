package controlador;

import java.util.HashMap;

import modelo.ItemEvaluacion;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoItem;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Item de evaluacion*/
@Controller
public class CItem extends CGeneral {

	private static final long serialVersionUID = -7125796211528355060L;
	@Wire
	private Textbox txtNombreItem;
	@Wire
	private Textbox txtDescripcionItem;
	@Wire
	private Textbox txtIdItem;
	@Wire
	private Combobox cmbTipoItem;
	@Wire
	private Button btnEliminarItem;
	@Wire
	private Window wdwItem;
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
				ItemEvaluacion item = servicioItem.buscarItem(codigo);
				txtNombreItem.setValue(item.getNombre());
				txtDescripcionItem.setValue(item.getDescripcion());
				cmbTipoItem.setValue(item.getTipo());
				id = item.getId();
				btnEliminarItem.setDisabled(false);
				map.clear();
				map = null;
			}
		}

	}

	/*
	 * Metodo que permite el guardado o modificacion de una entidad Item de
	 * evaluacion
	 */
	@Listen("onClick = #btnGuardarItem")
	public void guardarEstudiante() {
		if (txtNombreItem.getText().compareTo("") == 0
				|| cmbTipoItem.getText().compareTo("") == 0
				|| txtDescripcionItem.getText().compareTo("") == 0) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			Messagebox.show("¿Desea guardar los datos del Item?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreItem.getValue();
								String descripcion = txtDescripcionItem
										.getValue();
								Boolean estatus = true;
								String tipo = cmbTipoItem.getValue();
								ItemEvaluacion item = new ItemEvaluacion(id,
										nombre, descripcion, estatus, tipo);
								servicioItem.guardar(item);
								cancelarItem();
								id = 0;
								Messagebox.show("Item registrado exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);

							}
						}
					});

		}
	}

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarItem")
	public void cancelarItem() {
		txtNombreItem.setValue("");
		cmbTipoItem.setValue("");
		txtDescripcionItem.setValue("");
		btnEliminarItem.setDisabled(true);
		id = 0;
	}

	/*
	 * Metodo que permite la eliminacion logica de una entidad Item de
	 * evaluacion
	 */
	@Listen("onClick = #btnEliminarItem")
	public void eliminarItem() {
		Messagebox.show("¿Desea eliminar el Item?", "Dialogo de confirmacion",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							ItemEvaluacion item = servicioItem.buscarItem(id);
							item.setEstatus(false);
							servicioItem.guardar(item);
							cancelarItem();
							Messagebox.show("Item eliminado exitosamente",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
							id = 0;

						}
					}
				});

	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoItem")
	public void buscarItem() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoItem.zul", null, null);
		window.doModal();
		CCatalogoItem catalogo = new CCatalogoItem();
		catalogo.recibir("maestros/VItem");
	}

	/*
	 * Metodo que permite cerrar la ventana correspondiente a los items de
	 * evaluacion
	 */
	@Listen("onClick = #btnSalirItem")
	public void salirItem() {
		wdwItem.onClose();

	}

}