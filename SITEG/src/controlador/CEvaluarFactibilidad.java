package controlador;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.ItemDefensa;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.ItemFactibilidad;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SFactibilidad;
import servicio.SProgramaItem;
import servicio.STeg;
import servicio.SItemFactibilidad;
import servicio.SLapso;
import servicio.SProgramaItem;

import configuracion.GeneradorBeans;
import controlador.CCatalogoRegistrarFactibilidad;
import controlador.CGeneral;

public class CEvaluarFactibilidad extends CGeneral {

	SFactibilidad servicioFactibilidad = GeneradorBeans
			.getServicioFactibilidad();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SItemFactibilidad servicioItemFactibilidad = GeneradorBeans
			.getServicioItemFactibilidad();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SProgramaItem servicioProgramaItem = GeneradorBeans.getServicioProgramaItem();
	
	@Wire
	private Datebox db1;
	@Wire
	private Listbox ltbListaFactibilidad;
	
	@Wire
	private Listbox ltbItemsFactibilidad;
	@Wire
	private Textbox txtProgramaEvaluarFactibilidad;
	@Wire
	private Textbox txtAreaEvaluarFactibilidad;
	@Wire
	private Textbox txtTematicaEvaluarFactibilidad;
	@Wire
	private Textbox txtTituloEvaluarFactibilidad;
	@Wire
	private Textbox txtCedulaTutorEvaluarFactibilidad;
	@Wire
	private Textbox txtNombreTutorEvaluarFactibilidad;
	@Wire
	private Textbox txtApellidoTutorEvaluarFactiblidad;
	@Wire
	private Textbox txtCedulaComisionEvaluarFactibilidad;
	@Wire
	private Textbox txtNombreComisionEvaluarFactibilidad;
	@Wire
	private Textbox txtApellidoComisionEvaluarFactibilidad;
	@Wire
	private Textbox txtObservacionEvaluarFactibilidad;
	@Wire
	private Listbox ltbEstudianteEvaluarFactibilidad;
	@Wire
	private Listbox ltbComisiónEvaluarFactibilidad;
	@Wire
	private Window wdwEvaluarFactibilidad;
	@Wire
	private Window wdwCatalogoEvaluarFactibilidad;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;
	private static Programa programa;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				
				// se toma es el programa del estudiante asociado a el teg.
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(teg2);
				
				if (estudiantes.size() != 0) {
					programa = estudiantes.get(0).getPrograma();	
				}
				
				
				ltbEstudianteEvaluarFactibilidad.setModel(new ListModelList<Estudiante>(
						estudiantes));
				
							
				txtProgramaEvaluarFactibilidad.setValue(programa.getNombre());
				txtAreaEvaluarFactibilidad.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaEvaluarFactibilidad.setValue(teg2.getTematica().getNombre());
				txtCedulaTutorEvaluarFactibilidad.setValue(teg2.getTutor().getCedula());
				txtNombreTutorEvaluarFactibilidad.setValue(teg2.getTutor().getNombre());
				txtApellidoTutorEvaluarFactiblidad
						.setValue(teg2.getTutor().getApellido());
				txtTituloEvaluarFactibilidad.setValue(teg2.getTitulo());
				
				Lapso lapso = servicioLapso.buscarLapsoVigente();
				List<ItemEvaluacion> item = servicioProgramaItem.buscarItemsEnPrograma(programa, lapso);
				List<ItemEvaluacion> item2 = new  ArrayList <ItemEvaluacion>();
				for(int i= 0; i<item.size(); i++){
					if (item.get(i).getTipo().equals("Factibilidad"))
					{
						item2.add(item.get(i));
					}
					
				}
				ltbItemsFactibilidad.setModel(new ListModelList<ItemEvaluacion>(item2));
				
				
				
				
			}
		}

	}
	
	
	
	

	public void recibir(String vista) {
		vistaRecibida = vista;

	}
	
	

	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwEvaluarFactibilidad.onClose();
	}


	@Listen("onClick = #btnGuardarEvaluacionFactibilidad")
	public void guardar() {
		
	long id = 0;
	Teg teg = servicioTeg.buscarTeg(auxiliarId);
	Profesor profesor = ObtenerUsuarioProfesor();
	Date fecha = db1.getValue();
	String Observacion = txtObservacionEvaluarFactibilidad.getValue();
	String estatus = "Evaluando Factibilidad";
	Factibilidad factibilidad3 = servicioFactibilidad.buscarFactibilidadPorTeg(teg);
	
boolean registrefactibilidad = false;
	Factibilidad factibilidad = new Factibilidad(id,teg,profesor,fecha,Observacion,estatus);
	if ( txtObservacionEvaluarFactibilidad.getValue().compareTo("")==0){
		Messagebox.show("Debe Colocar una Observacion","Informacion", Messagebox.OK,Messagebox.INFORMATION);
		
		
	}
	else
	{
		if (factibilidad3 == null){
			
		
			
		servicioFactibilidad.guardar(factibilidad);
		registrefactibilidad = true;
		}
		else
		{
			registrefactibilidad = true;
		}
	}
		
	
if (registrefactibilidad == true)
{
	
	boolean dejeenblanco = false;

	Factibilidad factibilidad2 = servicioFactibilidad.buscarFactibilidadPorTeg(teg);
	
	for (int i = 0; i < ltbItemsFactibilidad.getItemCount(); i++) {

	
		Listitem listItem = ltbItemsFactibilidad.getItemAtIndex(i);
		String valor = ((Textbox)((listItem.getChildren().get(1))).getFirstChild()).getValue();
		System.out.print(valor);
		if (valor.equals("")){
			System.out.print("entre al if");
			System.out.print(valor);
			 Messagebox.show("Debe Colocar su Apreciacion en todos los item","Informacion", Messagebox.OK,Messagebox.INFORMATION);	
			 i = ltbItemsFactibilidad.getItemCount();
			 dejeenblanco = true;
			
			
		}
		else
		{
		ItemEvaluacion item = ltbItemsFactibilidad.getItems().get(i).getValue();
		ItemFactibilidad itemfactibilidad= new ItemFactibilidad(item,factibilidad2, valor);
		servicioItemFactibilidad.guardar(itemfactibilidad);
		}
	}
	
	if (dejeenblanco == false)
	{
	for (int i = 0; i < ltbEstudianteEvaluarFactibilidad.getItemCount(); i++) {
        Estudiante estudiante = ltbEstudianteEvaluarFactibilidad.getItems().get(i).getValue();
        enviarEmailNotificacion(estudiante.getCorreoElectronico(), "Observacion de la Evalucioon de Factibilidad:"+ txtObservacionEvaluarFactibilidad.getValue());
        
	}
	String estatus1 = "Factibilidad Evaluada";
	teg.setEstatus(estatus1);
	servicioTeg.guardar(teg); 
	Messagebox.show("datos guardados exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
    salir();
     
     } 

  }
}

	@Listen("onClick = #btnCancelarEvaluacionFactibilidad")
	public void cancelar() {
		txtObservacionEvaluarFactibilidad.setValue("");
		for (int i = 0; i < ltbItemsFactibilidad.getItemCount(); i++) {

			
			Listitem listItem = ltbItemsFactibilidad.getItemAtIndex(i);
			 ((Textbox)((listItem.getChildren().get(1))).getFirstChild()).setValue("");
		}
		
	}

}


	
	
	
	
	
		
	
	

