package controlador;

import java.util.HashMap;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Tematica;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.catalogo.CCatalogoTematica;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Tematica*/
@Controller
public class CTematica extends CGeneral {

	private static final long serialVersionUID = 6592791224493932846L;
	CCatalogoTematica catalogo = new CCatalogoTematica();
	@Wire
	private Combobox cmbAreaTematica;
	@Wire
	private Textbox txtNombreTematica;
	@Wire
	private Textbox txtDescripcionTematica;
	@Wire
	private Window wdwCatalogoTematica;
	@Wire
	private Button btnEliminarTematica;
	@Wire
	private Window wdwTematica;
	private long id = 0;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	public void inicializar(Component comp) {

		List<AreaInvestigacion> areas = servicioArea.buscarActivos();
		cmbAreaTematica.setModel(new ListModelList<AreaInvestigacion>(areas));

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

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnBuscarTematica")
	public void buscarTematica() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoTematica.zul", null, null);
		window.doModal();
		catalogo.recibir("maestros/VTematica");

	}

	/* Metodo que permite el guardado o modificacion de una entidad Tematica */
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
					new org.zkoss.zk.ui.event.EventListener<Event>() {
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

	/* Metodo que permite la eliminacion logica de una entidad Tematica */
	@Listen("onClick = #btnEliminarTematica")
	public void eliminarTematica() {
		Messagebox.show("Â¿Desea eliminar los datos de la tematica?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							Tematica tematica = servicioTematica
									.buscarTematica(id);
							tematica.setEstatus(false);
							servicioTematica.guardar(tematica);
							cancelarTematica();
							Messagebox.show("Tematica eliminada exitosamente",
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
	@Listen("onClick = #btnCancelarTematica")
	public void cancelarTematica() {
		id = 0;
		txtNombreTematica.setValue("");
		txtDescripcionTematica.setValue("");
		cmbAreaTematica.setValue("");
		btnEliminarTematica.setDisabled(true);

	}

	/* Metodo que permite cerrar la ventana correspondiente a las tematicas */
	@Listen("onClick = #btnSalirTematica")
	public void salirTematica() {
		wdwTematica.onClose();
	}

}
