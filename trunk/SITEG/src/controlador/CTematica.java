package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import modelo.Tematica;
import modelo.AreaInvestigacion;

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

import servicio.STematica;
import servicio.SAreaInvestigacion;
import configuracion.GeneradorBeans;

//es un controlador de tematica y su catalogo
@Controller
public class CTematica extends CGeneral {
	
	//servicios para los dos modelos implicados
	STematica servicioTematica = GeneradorBeans
			.getSTematica();
	

	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	
	//atributos de la vista tematica
	@Wire
	private Combobox cmbAreaTematica;
	@Wire
	private Textbox txtNombreTematica;
	@Wire
	private Textbox txtDescripcionTematica;

	
	//atributos de la pantalla del catalogo
	@Wire
	private Listbox ltbTematica;
	@Wire
	private Window wdwCatalogoTematica;
	@Wire
	private Textbox txtCodigoMostrarTematica;
	@Wire
	private Textbox txtNombreMostrarTematica;
	@Wire
	private Textbox txtAreaMostrarTematica;
	@Wire
	private Textbox txtDescripcionMostrarTematica;
	private long id=0;
	

	//metodo para llenar combo y mapear
	void inicializar(Component comp) {

		List<AreaInvestigacion> areas = servicioArea.buscarActivos();
		List<Tematica> tematicas = servicioTematica.buscarActivos();
		
		if (cmbAreaTematica == null) {
			ltbTematica.setModel(new ListModelList<Tematica>(tematicas));
		} else {
			cmbAreaTematica.setModel(new ListModelList<AreaInvestigacion>(
					areas));
			
		}
		
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ( map.get("id") != null) {
				long codigo = (Long) map.get("id");
				Tematica tematica = servicioTematica
						.buscarTematica(codigo);
				txtNombreTematica.setValue(tematica.getNombre());
				txtDescripcionTematica.setValue(tematica.getDescripcion());
				cmbAreaTematica.setValue(tematica.getLineaInvestigacion()
						.getNombre());
				id = tematica.getId();
				map.clear();
				map = null;
			}
		}

	}
//al darle al boton me muestra el catalogo
	@Listen("onClick = #btnBuscarTematica")
	public void buscarTematica() {

		Window window = (Window) Executions.createComponents(
				"/vistas/VCatalogoTematica.zul", null, null);
		window.doModal();

	}
//guarda al darle clic
	@Listen("onClick = #btnGuardarTematica")
	public void guardarTematica() {
		String nombre = txtNombreTematica.getValue();
		String descripcion = txtDescripcionTematica.getValue();
		String areas = cmbAreaTematica.getValue();
		
		Boolean estatus = true;
		AreaInvestigacion areainvestigacion = servicioArea
				.buscarAreaPorNombre(areas);

		Tematica tematica = new Tematica(id,nombre,descripcion,estatus,areainvestigacion);
		
		servicioTematica.guardar(tematica);
		cancelarTematica();
		id = 0;
		Messagebox.show("Tematica Registrada");
	}
//elimina al darle clic
	@Listen("onClick = #btnEliminarTematica")
	public void eliminarTematica() {
		System.out.println("Tematica Eliminada");
		Tematica tematica = servicioTematica.buscarTematica(id);
		tematica.setEstatus(false);
		servicioTematica.guardar(tematica);
		cancelarTematica();
		Messagebox.show("Tematica Eliminada");
	}
//coloca los campos de la vista en blanco
	@Listen("onClick = #btnCancelarTematica")
	public void cancelarTematica() {
		id = 0;
		txtNombreTematica.setValue("");
		txtDescripcionTematica.setValue("");
		cmbAreaTematica.setValue("");

	}
	
	// filtra en el catalogo 
	
	@Listen("onChange = #txtNombreMostrarTematica,#txtAreaMostrarTematica,#txtDescripcionMostrarTematica")
	public void filtrarDatosCatalogo() {
		List<Tematica> tematicas1 = servicioTematica.buscarActivos();
		List<Tematica> tematicas2 = new ArrayList<Tematica>();

		for (Tematica tematica : tematicas1) {
			if (tematica
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarTematica.getValue().toLowerCase())
					&& tematica
							.getLineaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtAreaMostrarTematica.getValue()
											.toLowerCase())
					&& tematica
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTematica.getValue()
											.toLowerCase())) {
				tematicas2.add(tematica);
			}

		}

		ltbTematica.setModel(new ListModelList<Tematica>(tematicas2));

	}

	//al darle clic a un item se va a pantalla y se cierra el catalogo
	@Listen("onDoubleClick = #ltbTematica")
	public void mostrarDatosCatalogo() {

		Listitem listItem = ltbTematica.getSelectedItem();
		Tematica tematicaDatosCatalogo = (Tematica) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tematicaDatosCatalogo.getId());
		String vista = "VTematica";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoTematica.onClose();
		
		//txtNombreTematica.setValue(nombre);
		

	}
}

