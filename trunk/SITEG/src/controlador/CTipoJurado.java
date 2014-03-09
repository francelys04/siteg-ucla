package controlador;

import java.util.HashMap;

import modelo.TipoJurado;

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

import controlador.catalogo.CCatalogoTipoJurado;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad TipoJurado*/
@Controller
public class CTipoJurado extends CGeneral {

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
	@Wire
	private Window wdwTipoJurado;
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

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoTipoJurado")
	public void buscarTipoJurado() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoTipoJurado.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VTipoJurado");

	}

	/* Metodo que permite el guardado o modificacion de una entidad TipoJurado */
	@Listen("onClick = #btnGuardarTipoJurado")
	public void guardarTipoJurado() {

		if ((txtNombreTipoJurado.getText().compareTo("") == 0)
				|| (txtDescripcionTipoJurado.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("¿Desea guardar los datos del tipo de jurado?",
					"Dialogo de confirmacion", Messagebox.OK
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
								Messagebox
										.show("Tipo de jurado registrado exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
								id = 0;

							}
						}
					});

		}

	}

	/* Metodo que permite la eliminacion logica de una entidad TipoJurado */
	@Listen("onClick = #btnEliminarTipoJurado")
	public void eliminarTipoJurado() {

		Messagebox.show("¿Desea eliminar los datos del tipo de jurado?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							TipoJurado tipoJurado = servicioTipoJurado
									.buscarTipoJurado(id);
							tipoJurado.setEstatus(false);
							servicioTipoJurado.guardar(tipoJurado);
							cancelarTipoJurado();
							Messagebox.show(
									"Tipo de jurado eliminado exitosamente",
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
	@Listen("onClick = #btnCancelarTipoJurado")
	public void cancelarTipoJurado() {
		id = 0;
		txtNombreTipoJurado.setValue("");
		txtDescripcionTipoJurado.setValue("");
		btnEliminarTipoJurado.setDisabled(true);
	}

	/* Metodo que permite cerrar la ventana correspondiente a los tipos de jurado */
	@Listen("onClick = #btnSalirTipoJurado")
	public void salirTipoJurado() {
		wdwTipoJurado.onClose();
	}

}
