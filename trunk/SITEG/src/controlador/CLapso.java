package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SLapso;

@Controller
public class CLapso extends CGeneral {
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();			

	
	@Wire
	private Textbox txtNombreLapso;
	@Wire
	private Textbox txtNombreEstudiante;
	@Wire
	private Datebox dtbInicioLapso;
	@Wire
	private Datebox dtbFinLapso;
	@Wire
	private Listbox ltbLapso;
	@Wire
	private Window wdwCatalogoLapso;
	@Wire
	private Textbox txtCedulaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarLapso;
	@Wire
	private Textbox txtFechaInicialMostrarLapso;
	@Wire
	private Textbox txtFechaFinalMostrarLapso;
	private long id = 0;
	//Metodo para cargar el catalogo de lapsos academicos

	
	void inicializar(Component comp) {
		
		List<Lapso> lapsos =servicioLapso.buscarActivos();
		//System.out.println(lapsos.get(0).getNombre());
		if(txtNombreLapso==null){
		ltbLapso.setModel(new ListModelList<Lapso>(lapsos));
		}

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		
		System.out.println("por aqui");
		
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Lapso lapso = servicioLapso.buscarLapso(codigo);
				txtNombreLapso.setValue(lapso.getNombre());
				dtbInicioLapso.setValue(lapso.getFechaInicial());		
				dtbFinLapso.setValue(lapso.getFechaFinal());
				id = lapso.getId();
				map.clear();
				map = null;
			}
		}

	}

	//Muestra el catalogo de lapsos
	@Listen("onClick = #btnCatalogoLapsos")
	public void buscarLapso() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoLapso.zul", null, null);
		window.doModal();

	}
	//Guarda un lapso
	@Listen("onClick = #btnGuardarLapso")
	public void guardarEstudiante() {

		String nombre = txtNombreLapso.getValue();
		Date fechaInicio = dtbInicioLapso.getValue();
		Date fechaFin = dtbFinLapso.getValue();
		Boolean estatus = true;
		Lapso lapso = new Lapso(id, nombre, fechaInicio, fechaFin,estatus);
		servicioLapso.guardar(lapso);
		cancelarLapso();
		id = 0;
		System.out.println("Lapso Guardado");
	}
	//Elimina un lapso
	@Listen("onClick = #btnEliminarLapso")
	public void eliminarLapso() {
		
		Lapso lapso = servicioLapso.buscarLapso(id);
		lapso.setEstatus(false);
		servicioLapso.guardar(lapso);
		cancelarLapso();
		System.out.println("Lapso Eliminado");
	}
	//Coloca todos los campos en blanco
	@Listen("onClick = #btnCancelarLapso")
	public void cancelarLapso() {
		txtNombreLapso.setValue("");
		dtbInicioLapso.setValue(null);
		dtbFinLapso.setValue(null);
		id = 0;
	
	}
	//Filtra por nombre
	@Listen("onChange = #txtNombreMostrarLapso")
	public void filtrarDatosCatalogo() {
		List<Lapso> lapsos1 = servicioLapso.buscarActivos();
		List<Lapso> lapsos2 = new ArrayList<Lapso>();

		for (Lapso lapso : lapsos1) {
			if (lapso.getNombre().toLowerCase().contains(txtNombreMostrarLapso.getValue().toLowerCase()))
					 {
				lapsos2.add(lapso);
			}

		}

		ltbLapso.setModel(new ListModelList<Lapso>(lapsos2));

	}
	//Carga los lapsos desde el catalogo hacia el formulario lapso academico
	@Listen("onDoubleClick = #ltbLapso")
	public void mostrarDatosCatalogo() {

		Listitem listItem = ltbLapso.getSelectedItem();
		Lapso lapsoDatosCatalogo = (Lapso) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", lapsoDatosCatalogo.getId());
		String vista = "maestros/VLapsoAcademico";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoLapso.onClose();

	}
}

