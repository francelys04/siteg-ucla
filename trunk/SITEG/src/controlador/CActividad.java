package controlador;

import java.util.HashMap;

import modelo.Actividad;
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

import controlador.catalogo.CCatalogoActividad;

/*Controlador que permite realizar las operaciones basicas (CRUD)
 * sobre la entidad Actividad*/
@Controller
public class CActividad extends CGeneral {

	private static final long serialVersionUID = -7144250834458342918L;
	CCatalogoActividad catalogo = new CCatalogoActividad();
	private long id = 0;
	@Wire
	private Textbox txtNombreActividad;
	@Wire
	private Textbox txtDescripcionActividad;
	@Wire
	private Textbox txtNombreMostrarActividad;
	@Wire
	private Textbox txtDescripcionMostrarActividad;
	@Wire
	private Listbox ltbActividad;
	@Wire
	private Window wdwCatalogoActividad;
	@Wire
	private Button btnEliminarActividad;
	@Wire
	private Button btnGuardarActividad;
	@Wire
	private Window wdwActividad;

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
				Actividad actividad = servicioActividad.buscarActividad(codigo);
				txtNombreActividad.setValue(actividad.getNombre());
				txtDescripcionActividad.setValue(actividad.getDescripcion());
				id = actividad.getId();
				btnEliminarActividad.setDisabled(false);
				map.clear();
				map = null;
			}
		}
	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoActividad")
	public void buscarActividad() {
		Window window = (Window) Executions.createComponents(

		"/vistas/catalogos/VCatalogoActividad.zul", null, null);

		window.doModal();
		catalogo.recibir("maestros/VActividad");
	}

	/* Metodo que permite el guardado o modificacion de una entidad Actividad */
	@Listen("onClick = #btnGuardarActividad")
	public void guardarActividad() {
		if ((txtNombreActividad.getText().compareTo("") == 0)
				|| (txtDescripcionActividad.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show("¿Desea guardar los datos de la actividad?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								String nombre = txtNombreActividad.getValue();
								String descripcion = txtDescripcionActividad
										.getValue();
								Boolean estatus = true;
								Actividad actividad = new Actividad(id, nombre,
										descripcion, estatus);
								servicioActividad.guardar(actividad);
								cancelarActividad();
								id = 0;
								Messagebox.show(
										"Actividad registrada exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});

		}
	}

	/* Metodo que permite la eliminacion logica de una entidad Actividad */
	@Listen("onClick = #btnEliminarActividad")
	public void eliminarActividad() {
		Messagebox.show("¿Desea eliminar los datos de la actividad?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							Actividad actividad = servicioActividad
									.buscarActividad(id);
							actividad.setEstatus(false);
							servicioActividad.guardar(actividad);
							cancelarActividad();
							Messagebox.show("Actividad eliminada exitosamente",
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
	@Listen("onClick = #btnCancelarActividad")
	public void cancelarActividad() {
		btnEliminarActividad.setDisabled(true);
		id = 0;
		txtNombreActividad.setValue("");
		txtDescripcionActividad.setValue("");
		btnEliminarActividad.setDisabled(true);
	}

	/* Metodo que permite cerrar la ventana correspondiente a las actividades */
	@Listen("onClick = #btnSalirActividad")
	public void salirActividad() {
		wdwActividad.onClose();
	}
	
	/*
	 * Metodo que permite buscar si una actividad existe, de acuerdo al nombre de la
	 * actividad
	 */
	@Listen("onChange = #txtNombreActividad")
	public void buscarNombreActividad() {
		Actividad actividad = servicioActividad.buscarActividadPorNombre(txtNombreActividad.getValue());
		if (actividad != null) {
			txtNombreActividad.setValue(actividad.getNombre());
			txtDescripcionActividad.setValue(actividad.getDescripcion());
			id = actividad.getId();
			btnEliminarActividad.setDisabled(false);
				
		}

	}
	
	
}