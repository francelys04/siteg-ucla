package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;

import modelo.Actividad;
import modelo.CondicionPrograma;
import modelo.Cronograma;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;


import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SProfesor;
import configuracion.GeneradorBeans;

@Controller
public class CProfesorArea extends CGeneral{
	
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	CCatalogoProfesor catalogo = new CCatalogoProfesor();
	
	@Wire
	private Textbox txtCedulaProfesorArea;
	@Wire
	private Listbox ltbProfesor;

	@Wire
	private Textbox txtApellidoProfesorArea;
	
	@Wire
	private Textbox txtNombreProfesorArea;
	
	@Wire
	private Listbox lsbAreasProfesorDisponibles;
	@Wire
	private Listbox lsbAreasProfesorSeleccionadas;
	
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<AreaInvestigacion> area = servicioArea.buscarActivos();
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		if (txtCedulaProfesorArea == null) {
			ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
		} else
		{
			lsbAreasProfesorDisponibles.setModel(new ListModelList<AreaInvestigacion>(
					area));
		}

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesorArea.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(txtCedulaProfesorArea.getValue());
				
				txtNombreProfesorArea.setValue(profesor.getNombre());
				txtApellidoProfesorArea.setValue(profesor.getApellido());
				llenaListas();
				map.clear();
				map = null;
			}
		}
		
	}
	
	
	@Listen("onClick = #btnCatalogoProfesorArea")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
		window.doModal();

		catalogo.recibir("maestros/VProfesorArea");

	}
	
	@Listen("onClick = #btnAgergarProfesorAreas")
	public void moverDerechaArea() {

		Listitem list1 = lsbAreasProfesorDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(lsbAreasProfesorSeleccionadas);
	}

	@Listen("onClick = #btnRemoverProfesorAreas")
	public void moverIzquierdaArea() {
		Listitem list2 = lsbAreasProfesorSeleccionadas.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(lsbAreasProfesorDisponibles);
	}
	
	
	
	@Listen("onClick = #btnGuardarProfesorAreas")
	public void guardar() {
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(txtCedulaProfesorArea.getValue());
		

		Set<AreaInvestigacion> areasProfesor = new HashSet<AreaInvestigacion>();
		for (int i = 0; i < lsbAreasProfesorSeleccionadas.getItemCount(); i++) {
			AreaInvestigacion area = lsbAreasProfesorSeleccionadas.getItems().get(i)
					.getValue();
			areasProfesor.add(area);
		}
		
		profesor.setAreas(areasProfesor);
		servicioProfesor.guardarProfesor(profesor);
		alert("Configuraciones Guardadas");
		limpiarCampos();
	}
	@Listen("onClick = #btnCancelarProfesorAreas")
	public void limpiarCampos() {
		txtCedulaProfesorArea.setValue("");
		txtApellidoProfesorArea.setValue("");
		txtNombreProfesorArea.setValue("");
		llenaListas();
	}
	

	private void llenaListas() {
		String ProfesorCedula = txtCedulaProfesorArea.getValue();
		
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(ProfesorCedula);
		
		
		List<AreaInvestigacion> areasDerecha = servicioArea.buscarAreasDelProfesor(profesor);
		
		
		lsbAreasProfesorSeleccionadas.setModel(new ListModelList<AreaInvestigacion>(
				areasDerecha));
		
		
		List<Long> ids = new ArrayList<Long>();
		for(int i=0; i<areasDerecha.size();i++){
			long id = areasDerecha.get(i).getId();
			ids.add(id);
		}
		System.out.println(ids.toString());
		if(ids.toString()!="[]"){
			System.out.println("entro");
		List<AreaInvestigacion> areasIzquierda = servicioArea.buscarAreasSinProfesor(ids);
		lsbAreasProfesorDisponibles.setModel(new ListModelList<AreaInvestigacion>(
						areasIzquierda));
		}
	}
	
	


}
