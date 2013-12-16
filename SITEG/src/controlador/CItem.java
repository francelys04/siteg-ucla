package controlador;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;

import modelo.AreaInvestigacion;
import modelo.ItemEvaluacion;

import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import servicio.SItem;
import configuracion.GeneradorBeans;

@Controller
public class CItem extends CGeneral {
	SItem servicioItem = GeneradorBeans.getServicioItem();


	@Wire
	private Textbox txtNombreItem;
	@Wire
	private Textbox txtDescripcionItem;
	@Wire
	private Textbox txtIdItem;
	@Wire
	private Listbox ltbItem;
	@Wire
	private Window wdwCatalogoItem;
	@Wire
	private Textbox txtNombreMostrarItem;
	@Wire
	private Textbox txtDescripcionMostrarItem;
	@Wire
	private Button btnEliminarItem;
	private long id = 0;

	
	void inicializar(Component comp) {

		List<ItemEvaluacion> items = servicioItem.buscarItemsActivos();	
//		System.out.println(items.get(0).getNombre());
//		System.out.println(items.get(1).getNombre());
		
		if(txtNombreItem==null){
		ltbItem.setModel(new ListModelList<ItemEvaluacion>(items));
		}
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

	
		if (map != null) {
			if ( map.get("id") != null) {

				long codigo = (Long) map.get("id");
				ItemEvaluacion item = servicioItem.buscarItem(codigo);
				txtNombreItem.setValue(item.getNombre());
				txtDescripcionItem.setValue(item.getDescripcion());
				id=item.getId();
				btnEliminarItem.setDisabled(false);
				map.clear();
				map = null;
			}
		}

	}

	@Listen("onClick = #btnGuardarItem")
	public void guardarEstudiante() {
		if (txtNombreItem.getText().compareTo("")==0
			|| txtDescripcionItem.getText().compareTo("")==0){
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);			
		}else{
			
			Messagebox.show("Desea guardar Item?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreItem.getValue();
								String descripcion = txtDescripcionItem.getValue();
								Boolean estatus = true;
								String tipo = "valor";
								ItemEvaluacion item= new ItemEvaluacion(id,nombre, descripcion, estatus, tipo);
								servicioItem.guardar(item);
								cancelarItem();
								id = 0;
								Messagebox.show("Item registrado exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
								
								
							}
						}
					});
			
			
		
		
		}
	}
	
	
	@Listen("onClick = #btnCancelarItem")
	public void cancelarItem() {
		txtNombreItem.setValue("");
		txtDescripcionItem.setValue("");
		btnEliminarItem.setDisabled(true);
		id=0;
	}
	
	@Listen("onClick = #btnEliminarItem")
	public void eliminarItem() {
		Messagebox.show("Desea eliminar Item?",
				"Dialogo de confirmacion", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt)
							throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							ItemEvaluacion item = servicioItem.buscarItem(id);
							item.setEstatus(false);
							servicioItem.guardar(item);
							cancelarItem();
							Messagebox.show("Item eliminado exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
							id=0;
							
							
						}
					}
				});
		
		
	}

	@Listen("onClick = #btnCatalogoItem")
	public void buscarItem() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoItem.zul", null, null);
		window.doModal();
		CCatalogoItem catalogo = new CCatalogoItem();
		catalogo.recibir("maestros/VItem");
	}	
	
}