package controlador;

import java.util.HashMap;
import modelo.AreaInvestigacion;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import controlador.catalogo.CCatalogoAreaInvestigacion;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad AreaInvestigacion*/
@Controller
public class CAreaInvestigacion extends CGeneral {

	@Wire
	private Textbox txtNombreArea;
	@Wire
	private Textbox txtDescripcionArea;
	@Wire
	private Window wdwCatalogoArea;
	@Wire
	private Textbox txtNombreMostrarArea;
	@Wire
	private Textbox txtDescripcionMostrarArea;
	@Wire
	private Button btnEliminarArea;
	@Wire
	private Window wdwArea;
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
				AreaInvestigacion area = servicioArea.buscarArea(codigo);
				txtNombreArea.setValue(area.getNombre());
				txtDescripcionArea.setValue(area.getDescripcion());
				id = area.getId();
				btnEliminarArea.setDisabled(false);
				map.clear();
				map = null;

			}
		}

	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnBuscarArea")
	public void buscarArea() {
		CCatalogoAreaInvestigacion areas = new CCatalogoAreaInvestigacion();
		areas.metodoPrender();
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArea.zul", null, null);
		window.doModal();
		CCatalogoAreaInvestigacion catalogo = new CCatalogoAreaInvestigacion();
		catalogo.recibir("maestros/VAreaInvestigacion");

	}

	/*
	 * Metodo que permite el guardado o modificacion de una entidad
	 * AreaInvestigacion
	 */
	@Listen("onClick = #btnGuardarArea")
	public void guardarArea() {
		if (txtNombreArea.getText().compareTo("") == 0
				|| txtDescripcionArea.getText().compareTo("") == 0) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show("¿Desea guardar el area de investigacion?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreArea.getValue();
								String descripcion = txtDescripcionArea
										.getValue();
								Boolean estado = true;
								AreaInvestigacion area = new AreaInvestigacion(
										id, nombre, descripcion, estado);
								servicioArea.guardar(area);
								Messagebox
										.show("Area de investigacion registrada exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
								cancelarArea();
								id = 0;

							}
						}
					});
		}
	}

	/* Metodo que permite la eliminacion logica de una entidad AreaInvestigacion */
	@Listen("onClick = #btnEliminarArea")
	public void eliminarArea() {
		Messagebox.show(
				"¿Desea eliminar los datos del de area de investigacion?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							AreaInvestigacion area = servicioArea
									.buscarArea(id);
							area.setEstatus(false);
							servicioArea.guardar(area);
							Messagebox
									.show("Area de investigacion eliminada exitosamente",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
							cancelarArea();

						}
					}
				});

	}

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * variable global id
	 */
	@Listen("onClick = #btnCancelarArea")
	public void cancelarArea() {

		txtNombreArea.setValue("");
		txtDescripcionArea.setValue("");
		btnEliminarArea.setDisabled(true);
		id = 0;
	}

	/*
	 * Metodo que permite cerrar la ventana correspondiente a las areas de
	 * investigacion
	 */
	@Listen("onClick = #btnSalirArea")
	public void salirArea() {
		wdwArea.onClose();
	}
}
