package controlador;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.zkoss.zul.Button;
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

import servicio.SActividad;

@Controller
public class CActividad extends CGeneral {

	SActividad servicioActividad = GeneradorBeans.getServicioActividad();
  CCatalogoActividad catalogo = new CCatalogoActividad();
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
	
	
	private long id = 0;

	/** Metodo heredado del controlador CGeneral que permite inicializar los
	 componentes de zk
	 asi como tambien permite settear los atributos a los campos luego de
	 seleccionar desde el catalogo
	 
	 *@author Edgar
	 *@param comp 
	 */
	void inicializar(Component comp) {

		/**
		 * Listado de todos las actividades que se encuentran activos, cuyo
		 * estatus=true con el servicioActividad mediante el metodo
		 * buscarActivos
		 **/

		List<Actividad> actividad = servicioActividad.buscarActivos();

		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		if (txtNombreActividad == null) {
			ltbActividad.setModel(new ListModelList<Actividad>(actividad));
		}

		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if (map.get("id") != null) {

				long codigo =  (Long) map.get("id");
				Actividad actividad2 = servicioActividad
						.buscarActividad(codigo);
				txtNombreActividad.setValue(actividad2.getNombre());
				txtDescripcionActividad.setValue(actividad2.getDescripcion());
				id = actividad2.getId();
				btnEliminarActividad.setDisabled(false);
				map.clear();
				map = null;
			}
		}
	}

	// Aca se muestra el catalogo de las actividades Registradas
	@Listen("onClick = #btnCatalogoActividad")
	public void buscarActividad() {

		Window window = (Window) Executions.createComponents(

				"/vistas/catalogos/VCatalogoActividad.zul", null, null);
		 
				
		window.doModal();
		catalogo.recibir("maestros/VActividad");
	}

	// Aca se guardan las actividades
	@Listen("onClick = #btnGuardarActividad")
	public void guardarActividad() {
		String nombre = txtNombreActividad.getValue();
		String descripcion = txtDescripcionActividad.getValue();
		Boolean estatus = true;
		Actividad actividad = new Actividad(id, nombre, descripcion, estatus);
		servicioActividad.guardar(actividad);
		cancelarActividad();
		alert("Actividades Guardadas");
		
		id = 0;
	}

	// Aca se eliminan logicamente las actividades
	@Listen("onClick = #btnEliminarActividad")
	public void eliminarActividad() {
		btnEliminarActividad.setDisabled(false);
		System.out.println("Tipo de Jurado Eliminado");
		Actividad actividad = servicioActividad.buscarActividad(id);
		actividad.setEstatus(false);
		servicioActividad.guardar(actividad);
		cancelarActividad();
		alert("Actividad Eliminada");
		
	}

	// Aca se mandan a limpiar los campos de textos de la vista
	@Listen("onClick = #btnCancelarActividad")
	public void cancelarActividad() {
		btnEliminarActividad.setDisabled(true);
		id = 0;
		txtNombreActividad.setValue("");
		txtDescripcionActividad.setValue("");
		
	}



}