package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Requisito;

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
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SRequisito;

@Controller
public class CRequisito extends CGeneral {
	SRequisito servicioRequisito = GeneradorBeans
			.getServicioRequisito();
	
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
	private long id = 0;
	
	void inicializar(Component comp) {

		List<Requisito> requisitos = servicioRequisito.buscarActivos();

		if(txtNombreRequisito==null)
			ltbRequisito.setModel(new ListModelList<Requisito>(requisitos));
		
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				Requisito requisito = servicioRequisito.buscarRequisito(codigo);
				txtNombreRequisito.setValue(requisito.getNombre());
				txtDescripcionRequisito.setValue(requisito.getDescripcion());
				id = codigo;
				map.clear();
				map = null;
			}
		}
		
}

	@Listen("onClick = #btnBuscarRequisito")
	public void buscarRequisito() {

			
		
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoRequisito.zul", null, null);
		 catalogo.recibir("maestros/VRequisito");
		window.doModal();

	}

	@Listen("onClick = #btnGuardarRequisito")
	public void guardarEstudiante() {

		String nombre = txtNombreRequisito.getValue();
		String descripcion = txtDescripcionRequisito.getValue();
		Boolean estatus = true;
		
		
		Requisito requisito = new Requisito(id, nombre, descripcion,
				estatus);
		servicioRequisito.guardar(requisito);

		cancelarRequisito();
		System.out.println("Requisito Guardado");
	}

	@Listen("onClick = #btnEliminarRequisito")
	public void eliminarEstudiante() {
		Requisito requisito = servicioRequisito.buscarRequisito(id);
		requisito.setEstatus(false);
		servicioRequisito.guardar(requisito);
		cancelarRequisito();
		System.out.println("Requisito Eliminado");
	}

	@Listen("onClick = #btnCancelarRequisito")
	public void cancelarRequisito() {
		id = 0;
		txtNombreRequisito.setValue("");
		txtDescripcionRequisito.setValue("");
	}

	@Listen("onChange = #txtNombreMostrarRequisito,#txtDescripcionMostrarRequisito")
	public void filtrarDatosCatalogo() {
		List<Requisito> requisitos1 = servicioRequisito.buscarActivos();
		List<Requisito> requisitos2 = new ArrayList<Requisito>();

		for (Requisito requisito : requisitos1) {
			if (requisito
					.getNombre()
					.toLowerCase()
					.contains( 
							txtNombreMostrarRequisito.getValue().toLowerCase())
					&& requisito
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarRequisito.getValue()
											.toLowerCase())) {
				requisitos2.add(requisito);
			}

		}

		ltbRequisito.setModel(new ListModelList<Requisito>(requisitos2));

	}

	@Listen("onDoubleClick = #ltbRequisito")
	public void mostrarDatosCatalogo() {
		Listitem listItem = ltbRequisito.getSelectedItem();
		Requisito requisitoDatosCatalogo = (Requisito) listItem.getValue();
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", requisitoDatosCatalogo.getId());
		String vista = "maestros/VRequisito";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoRequisito.onClose();

	}

	
	
}
