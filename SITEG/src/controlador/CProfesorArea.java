package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.AreaInvestigacion;

import modelo.Actividad;
import modelo.CondicionPrograma;
import modelo.Cronograma;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.ProfesorArea;
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
import servicio.SProfesorArea;
import configuracion.GeneradorBeans;

@Controller
public class CProfesorArea extends CGeneral{
	
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	CCatalogoProfesor catalogo = new CCatalogoProfesor();
	SProfesorArea servicioProfesorArea = GeneradorBeans.getProfesorArea();
	
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
		
		
		List<ProfesorArea> profesorArea = new ArrayList<ProfesorArea>();
		for (int i = 0; i < lsbAreasProfesorSeleccionadas.getItemCount(); i++) {
			System.out.println("imprimo cronograma");
			System.out.println(lsbAreasProfesorSeleccionadas.getItemCount());
			AreaInvestigacion area = lsbAreasProfesorSeleccionadas.getItems().get(i)
					.getValue();
			ProfesorArea profesorarea = new ProfesorArea(profesor,area);
			profesorArea.add(profesorarea);
		}
		
		
		servicioProfesorArea.guardar(profesorArea);
		alert("Configuraciones Guardadas");
		limpiarCampos();
	}
	@Listen("onClick = #btnCancelarProfesorAreas")
	public void limpiarCampos() {
		txtCedulaProfesorArea.setValue("");
		txtApellidoProfesorArea.setValue("");
		txtNombreProfesorArea.setValue("");
		//llenarListas();
	}
	

	private void llenaListas() {
		String ProfesorCedula = txtCedulaProfesorArea.getValue();
		
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(ProfesorCedula);
		
		List<AreaInvestigacion> areasIzquierda = servicioArea.buscarAreasSinProfesor(profesor);
		List<AreaInvestigacion> areasDerecha = servicioProfesorArea
				.buscarAreasDeProfesor(profesor);
		
		lsbAreasProfesorDisponibles.setModel(new ListModelList<AreaInvestigacion>(
				areasIzquierda));
		lsbAreasProfesorSeleccionadas.setModel(new ListModelList<AreaInvestigacion>(
				areasDerecha));
		
	}
	
	


}
