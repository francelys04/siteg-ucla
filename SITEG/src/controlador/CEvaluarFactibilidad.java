package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Factibilidad;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
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
	private Listbox ltbListaFactibilidad;
	@Wire
	private Listbox ltbListaEstudiantes;
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
				
				
				ltbListaEstudiantes.setModel(new ListModelList<Estudiante>(
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
		
		Messagebox.show(
				"Evaluación registrada exitosamente",
				"Información", Messagebox.OK,
				Messagebox.INFORMATION);
		salir();
		
		
	}
	
}
