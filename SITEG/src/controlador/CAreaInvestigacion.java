package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.GeneratedValue;




import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Programa;

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

import servicio.SAreaInvestigacion;
import configuracion.GeneradorBeans;
//constructor de la clases area de investigacion y de su catalogo
@Controller
public class CAreaInvestigacion extends CGeneral {
	//servicio 
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	

	//atributos de la vista de registrar area
	@Wire
	private Textbox txtNombreArea;
	@Wire
	private Textbox txtDescripcionArea;
	
   //atributos del catalogo de area
	@Wire
	private Listbox ltbArea;
	@Wire
	private Window wdwCatalogoArea;
	@Wire
	private Textbox txtNombreMostrarArea;
	@Wire
	private Textbox txtDescripcionMostrarArea;
	@Wire
	private Button btnEliminarArea;
	private long id = 0;
	

	
	void inicializar(Component comp) {

		//busca todas las areas y llena un listado
		List<AreaInvestigacion> area = servicioArea.buscarActivos();

		if(txtNombreArea==null){
			ltbArea.setModel(new ListModelList<AreaInvestigacion>(area));
			}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				AreaInvestigacion a = servicioArea
						.buscarArea(codigo);
				txtNombreArea.setValue(a.getNombre());
				txtDescripcionArea.setValue(a.getDescripcion());
				id = a.getId();
				btnEliminarArea.setDisabled(false);
				map.clear();
				map = null;
				
			}
		}

	}
//abre la ventana del catalogo
	@Listen("onClick = #btnBuscarArea")
	public void buscarArea() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArea.zul", null, null);
		window.doModal();
		CCatalogoAreaInvestigacion catalogo = new CCatalogoAreaInvestigacion();
		catalogo.recibir("maestros/VAreaInvestigacion");

	}
//guarda un area
	@Listen("onClick = #btnGuardarArea")
	public void guardarArea() {
		if(txtNombreArea.getText().compareTo("")==0 
			|| txtDescripcionArea.getText().compareTo("")==0){
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);			
		}else{
			Messagebox.show("Desea guardar el area de investigacion?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								String nombre = txtNombreArea.getValue();
								String descripcion = txtDescripcionArea.getValue();
								Boolean estado = true;
								AreaInvestigacion area = new AreaInvestigacion(id, nombre,descripcion, estado);
								servicioArea.guardar(area);
								 Messagebox.show("Area de investigacion registrada exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
								cancelarArea();
								id = 0;
								
							}
						}
					});
			
			
			
		
		
		}
	}
//elimina un area
	@Listen("onClick = #btnEliminarArea")
	public void eliminarArea() {
		Messagebox.show("Desea eliminar el area de investigacion?",
				"Dialogo de confirmacion", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt)
							throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							AreaInvestigacion area = servicioArea.buscarArea(id);
							area.setEstatus(false);
							servicioArea.guardar(area);
							 Messagebox.show("Area de investigacion eliminada exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
							cancelarArea();
							
							
						}
					}
				});
		
		
		
		
		
		
	}
//limpia los campos de la ventana
	@Listen("onClick = #btnCancelarArea")
	public void cancelarArea() {

		txtNombreArea.setValue("");
		txtDescripcionArea.setValue("");
		btnEliminarArea.setDisabled(true);
		id = 0;

	}

}

