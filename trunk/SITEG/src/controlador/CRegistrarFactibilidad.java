package controlador;

import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Factibilidad;
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
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.STeg;
import servicio.SItemFactibilidad;

import configuracion.GeneradorBeans;
import controlador.CCatalogoRegistrarFactibilidad;
import controlador.CGeneral;

public class CRegistrarFactibilidad extends CGeneral {
	SFactibilidad servicioFactibilidad = GeneradorBeans
			.getServicioFactibilidad();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SItemFactibilidad servicioItemFactibilidad = GeneradorBeans
			.getServicioItemFactibilidad();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();

	@Wire
	private Listbox ltbListaFactibilidad;
	@Wire
	private Listbox ltbItemsFactibilidad;
	@Wire
	private Textbox txtProgramaRegistrarFactibilidad;
	@Wire
	private Textbox txtAreaRegistrarFactibilidad;
	@Wire
	private Textbox txtTematicaRegistrarFactibilidad;
	@Wire
	private Textbox txtTituloRegistrarFactibilidad;
	@Wire
	private Textbox txtCedulaComisionRegistrarFactibilidad;
	@Wire
	private Textbox txtNombreComisionRegistrarFactibilidad;
	@Wire
	private Textbox txtApellidoComisionRegistrarFactibilidad;
	@Wire
	private Textbox txtObservacionRegistrarFactibilidad;
	@Wire
	private Listbox ltbEstudianteRegistrarFactibilidad;
	@Wire
	private Listbox ltbComisionRegistrarFactibilidad;
	@Wire
	private Window wdwRegistrarFactibilidad;
	@Wire
	private Window wdwCatalogoRegistrarFactibilidad;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;
	private static Programa programa;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		//permite cargar los datos del item seleccionado en el catalogo		
		programa = servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor());

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				txtProgramaRegistrarFactibilidad.setValue(programa.getNombre());
				txtAreaRegistrarFactibilidad.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaRegistrarFactibilidad.setValue(teg2.getTematica().getNombre());
				txtTituloRegistrarFactibilidad.setValue(teg2.getTitulo());
				
				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				Factibilidad factibilidadPorTeg = servicioFactibilidad.buscarFactibilidadPorTeg(tegPorCodigo);
				
				
				List<ItemFactibilidad> itemsFactibilidad = servicioItemFactibilidad.buscarItemFactibilidad(factibilidadPorTeg);
				ltbItemsFactibilidad.setModel(new ListModelList<ItemFactibilidad>(
						itemsFactibilidad));
				
				 txtCedulaComisionRegistrarFactibilidad.setValue(teg2.getTutor().getCedula());
				 txtNombreComisionRegistrarFactibilidad.setValue(teg2.getTutor().getNombre());
				 txtApellidoComisionRegistrarFactibilidad.setValue(teg2.getTutor().getApellido());
				 
				 
				
				txtObservacionRegistrarFactibilidad.setValue(factibilidadPorTeg.getObservacion());
				
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegPorCodigo);
				ltbEstudianteRegistrarFactibilidad.setModel(new ListModelList<Estudiante>(
						estudiantes));
				id = teg2.getId();

				map.clear();
				map = null;
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
		wdwRegistrarFactibilidad.onClose();
	}
	//Permite aceptar la factibilidad de un proyecto
	@Listen("onClick = #btnAceptarRegistrarFactibilidad")
	public void aceptarfactibilidad(){
		Messagebox.show("¿Desea aceptar la factibilidad del proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
		
		String estatus = "Proyecto Factible";
		Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
		teg2.setEstatus(estatus);
		servicioTeg.guardar(teg2);
		for (int i = 0; i < ltbEstudianteRegistrarFactibilidad.getItemCount(); i++) {
	        Estudiante estudiante = ltbEstudianteRegistrarFactibilidad.getItems().get(i).getValue();
	        enviarEmailNotificacion(estudiante.getCorreoElectronico(), "Su Proyecto es Factible");
	        
		}
		
		
		Messagebox.show("Datos guardados exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
		salir();
						}
					}
				});
	
	}
	//Permite rechazar la factibilidad de un proyecto
	@Listen("onClick = #btnRechazarRegistrarFactibilidad")
	public void rechazarfactibilidad()
	{
		Messagebox.show("¿Desea rechazar factibilidad del proyecto?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
		String estatus = "Proyecto No Factible";
		Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
		teg2.setEstatus(estatus);
		servicioTeg.guardar(teg2);
		for (int i = 0; i < ltbEstudianteRegistrarFactibilidad.getItemCount(); i++) {
	        Estudiante estudiante = ltbEstudianteRegistrarFactibilidad.getItems().get(i).getValue();
	        enviarEmailNotificacion(estudiante.getCorreoElectronico(), "Su Proyecto es no Factible");
	        
		}
		Messagebox.show("Datos guardados exitosamente","Informacion", Messagebox.OK,Messagebox.INFORMATION);
		salir();
						}
					}
				});
	}
	}



	

