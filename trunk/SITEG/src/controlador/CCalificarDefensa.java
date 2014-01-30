package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.CondicionPrograma;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.ItemDefensa;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegRequisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SDefensa;
import servicio.SEstudiante;
import servicio.SItemDefensa;
import servicio.SLapso;
import servicio.SProgramaItem;
import servicio.STeg;
import configuracion.GeneradorBeans;

@Controller
public class CCalificarDefensa extends CGeneral {

	@Wire
	private Datebox dtbfecha;
	@Wire
	private Textbox txtProgramaCalificar;
	@Wire
	private Textbox txtAreaCalificar;
	@Wire
	private Textbox txtTematicaCalificar;
	@Wire
	private Textbox txtTituloCalificar;
	@Wire
	private Textbox txtNombreTutor;
	@Wire
	private Textbox txtCedulaTutor;
	@Wire
	private Textbox txtApellidoTutor;
	@Wire
	private Listbox ltbEstudiantesCalificar;
	
	
	@Wire
	private Radiogroup rdgCalificacion;
	@Wire
	private Radio rdoAprobado;
	@Wire
	private Radio rdoReprobado;
	
	@Wire
	private Window wdwCalificarDefensa;
	
	
	@Wire
	private Listbox ltbitem;

	private static String vistaRecibida;
	private static long auxId;
	private static  boolean dejeenblanco = false;
	
	private static Programa p;
	ArrayList<Boolean> valor2 = new ArrayList<Boolean>();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstu = GeneradorBeans.getServicioEstudiante();
	SProgramaItem servicioProgratem = GeneradorBeans.getServicioProgramaItem();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SDefensa serviciodefensa = GeneradorBeans.getServicioDefensa();
	SItemDefensa servicioItem = GeneradorBeans.getServicioItemDefensa();
	@Override
	void inicializar(Component comp) {
		
		Selectors.wireComponents(comp, this, false);	
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {				
				long codigo =  (Long) map.get("id");
				auxId = codigo;
				
				Teg teg2 = servicioTeg.buscarTeg(codigo);
				List<Estudiante> estudiante = servicioEstu.buscarEstudiantePorTeg(teg2);
				if (estudiante.size() != 0) {
					 p = estudiante.get(0).getPrograma();
					
					
				}
				txtProgramaCalificar.setValue(p.getNombre());
				txtNombreTutor.setValue(teg2.getTutor().getNombre());
				txtApellidoTutor.setValue(teg2.getTutor().getApellido());
				txtTituloCalificar.setValue(teg2.getTitulo());
				txtCedulaTutor.setValue(teg2.getTutor().getCedula());
				txtAreaCalificar.setValue(teg2.getTematica().getareaInvestigacion().getNombre());
				txtTematicaCalificar.setValue(teg2.getTematica().getNombre());				
				List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg2);				
				ltbEstudiantesCalificar.setModel(new ListModelList<Estudiante>(est));
				Lapso lapso = servicioLapso.buscarLapsoVigente();
				List<ItemEvaluacion> item = servicioProgratem.buscarItemsEnPrograma(p, lapso);
				List<ItemEvaluacion> item2 = new  ArrayList <ItemEvaluacion>();
				for(int i= 0; i<item.size(); i++){
					if (item.get(i).getTipo().equals("Defensa"))
					{
						item2.add(item.get(i));
					}
					
				}
				ltbitem.setModel(new ListModelList<ItemEvaluacion>(item2));
				
				map.clear();
				map = null;
			}
		}
		
	}
	
	public void recibir (String vista)
	{
		vistaRecibida = vista;

	}
	
	@Listen("onClick = #btnGuardar")
	public void guardar() {
		 dejeenblanco = false;
		if ((rdoAprobado.isChecked()==false) && (rdoReprobado.isChecked()==false) ){
			Messagebox.show("Debe indicar si el TEG se encuentra Aprobado o Reproado", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		else{
		long auxId2;
		auxId2=auxId;
		Teg teg1 = servicioTeg.buscarTeg(auxId2);
		
		
		
		
		for (int i = 0; i < ltbitem.getItemCount(); i++) {

			Defensa defensa = serviciodefensa.buscarDefensaDadoTeg(teg1);
			
			Listitem listItem = ltbitem.getItemAtIndex(i);
			String valor = ((Textbox)((listItem.getChildren().get(1))).getFirstChild()).getValue();
			if (valor.equals("")){
				 Messagebox.show("Debe Colocar su Apreciacion en todos los item","Informacion", Messagebox.OK,Messagebox.INFORMATION);	
				 i = ltbitem.getItemCount();
				 dejeenblanco = true;
			}
			else
			{
			ItemEvaluacion item = ltbitem.getItems().get(i).getValue();
			ItemDefensa itemdefensa = new ItemDefensa(item,defensa, valor);
			servicioItem.guardar(itemdefensa);
			}
			
		}
		
		
	
	if(dejeenblanco==false){	
		
		if (rdoAprobado.isChecked()==true){
			
			String estatus1 = "TEG Aprobado";		
		    Messagebox.show("datos guardados exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
		     salir(); 	  
		     teg1.setEstatus(estatus1);
			servicioTeg.guardar(teg1);
				
			}
		
			
		else if (rdoReprobado.isChecked()==true) {
			
		String estatus2 =  "TEG Reprobado";
		teg1.setEstatus(estatus2);
		servicioTeg.guardar(teg1);
		 Messagebox.show("datos guardados exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
         salir();
		}
		}	
		}

		
	}
	private void salir(){
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCalificarDefensa.onClose();
	}
	
	@Listen("onClick = #btnCancelar")
	public void cancelarVerificacion() {
		
		rdoAprobado.setChecked(false);
		rdoReprobado.setChecked(false);
for (int i = 0; i < ltbitem.getItemCount(); i++) {

			
			Listitem listItem = ltbitem.getItemAtIndex(i);
			 ((Textbox)((listItem.getChildren().get(1))).getFirstChild()).setValue("");
		}
		
	}
	


}
