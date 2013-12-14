package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.GeneratedValue;




import modelo.AreaInvestigacion;
import modelo.Lapso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
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
				map.clear();
				map = null;
			}
		}

	}
//abre la ventana del catalogo
	@Listen("onClick = #btnBuscarArea")
	public void buscarArea() {

		Window window = (Window) Executions.createComponents(
				"/vistas/VCatalogoArea.zul", null, null);
		window.doModal();

	}
//guarda un area
	@Listen("onClick = #btnGuardarArea")
	public void guardarArea() {

		
		
		String nombre = txtNombreArea.getValue();
		String descripcion = txtDescripcionArea.getValue();
		Boolean estado = true;
		AreaInvestigacion area = new AreaInvestigacion(id, nombre,descripcion, estado);
		servicioArea.guardar(area);
		cancelarArea();
		id = 0;
		Messagebox.show("Area de Investigacion Registrada");
	}
//elimina un area
	@Listen("onClick = #btnEliminarArea")
	public void eliminarArea() {
		
		AreaInvestigacion area = servicioArea.buscarArea(id);
		area.setEstatus(false);
		servicioArea.guardar(area);
		cancelarArea();
		Messagebox.show("Area de Investigacion Eliminada");
	}
//limpia los campos de la ventana
	@Listen("onClick = #btnCancelarArea")
	public void cancelarArea() {

		txtNombreArea.setValue("");
		txtDescripcionArea.setValue("");
		id = 0;

	}
//filtra el catalogo en este caso solo por nombre y descripcion
	@Listen("onChange = #txtNombreMostrarArea")
	public void filtrarDatosCatalogo() {
		List<AreaInvestigacion> area1 = servicioArea.buscarActivos();
		List<AreaInvestigacion> area2 = new ArrayList<AreaInvestigacion>();

		for (AreaInvestigacion area : area1) {
			if (area
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarArea.getValue().toLowerCase())
					
					
					
					&& area
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArea.getValue()
											.toLowerCase())) {
				area2.add(area);
			}

		}

		ltbArea.setModel(new ListModelList<AreaInvestigacion>(area2));

	}
//lleva el item que selecciono en el catalogo a la ventana y cierra el catalogo
	@Listen("onDoubleClick = #ltbArea")
	public void mostrarDatosCatalogo() {

		Listitem listItem = ltbArea.getSelectedItem();
		AreaInvestigacion areaDatosCatalogo = (AreaInvestigacion) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", areaDatosCatalogo.getId());
		String vista = "VAreaInvestigacion";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoArea.onClose();

	}
}

