package controlador;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Factibilidad;
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
import servicio.STeg;
import servicio.SItemFactibilidad;

import configuracion.GeneradorBeans;
import controlador.CCatalogoFactibilidad;
import controlador.CGeneral;
	public class CRegistrarFactibilidad extends CGeneral{
		CCatalogoFactibilidad catalogo = new CCatalogoFactibilidad();
		SFactibilidad ServicioRegistrarFactibilidad = GeneradorBeans.getFactibilidad();
		STeg ServicioTeg = GeneradorBeans.getServicioTeg();
		SEstudiante ServicioEstudiante = GeneradorBeans.getServicioEstudiante();
		SItemFactibilidad ServicioItemFactibilidad = GeneradorBeans.getServicioItemFactibilidad();
		@Wire
		private Listbox ltbListaFactibilidad; 
		@Wire 
		private Listbox ltbItemsFactibilidad;
		//@Wire
		//private Combobox cmbRegistrarFactibilidad;
		@Wire
		private Textbox txtAreaRegistrarFactibilidad;
		@Wire
		private Textbox txtTematicaRegistrarFactibilidad;
		@Wire
		private Textbox txtTituloRegistrarFactibilidad;
		@Wire
		private Textbox txtCedulaTutorRegistrarFactibilidad;
		@Wire
		private Textbox txtNombreTutorRegistrarFactibilidad;
		@Wire
		private Textbox txtApellidoTutorRegistrarFactiblidad;
		@Wire
		private Textbox txtNombreTutorFactibilidad;
		@Wire
		private Textbox txtApellidoTutorFactibilidad;
		@Wire 
		private Textbox txtObservacionRegistrarFactibilidad;
		@Wire
		private Listbox ltbEstudianteRegistrarFactibilidad;
		@Wire 
		private Listbox ltbComisiónRegistrarFactibilidad;
		private long id;
		@Override
		void inicializar(Component comp) {
			// TODO Auto-generated method stub
		//	List<Factibilidad> factibilidad1 = ServicioRegistrarFactibilidad.buscarActivos();
		//	cmbRegistrarFactibilidad.setModel(new ListModelList<Factibilidad>(factibilidad1));
			Selectors.wireComponents(comp, this, false);

			HashMap<String, Object> map = (HashMap<String, Object>) Sessions
					.getCurrent().getAttribute("itemsCatalogo");

					/*
					 * Validacion para vaciar la informacion del Catalogo a la vista
					 * VRegistrarFactibilidad.zul si la varible map tiene algun dato contenido
					 */
					if (map != null) {
						if (map.get("id") != null) {
							String IdFactibilidad;
					          Teg teg;
							  Teg teg1;
							 id =  (Long) map.get("id");
							 Factibilidad factibilidad1 =  ServicioRegistrarFactibilidad.buscarIdTeg(id);
					          teg = factibilidad1.getTeg();
					         // teg1 = ServicioTeg.BuscarDatosTeg(teg.getId());
					          txtAreaRegistrarFactibilidad.setValue(teg.getTematica().getareaInvestigacion().getNombre());
					          txtAreaRegistrarFactibilidad.setDisabled(true);
					          txtTematicaRegistrarFactibilidad.setValue(teg.getTematica().getNombre());
					          txtTematicaRegistrarFactibilidad.setDisabled(true);
					          txtTituloRegistrarFactibilidad.setValue(teg.getTitulo());
					          txtTituloRegistrarFactibilidad.setDisabled(true);
					          List<Estudiante> estudiante = ServicioEstudiante.buscarEstudiantePorTeg(teg);
					          ltbEstudianteRegistrarFactibilidad.setModel(new ListModelList<Estudiante>(estudiante));
					          txtCedulaTutorRegistrarFactibilidad.setValue(teg.getTutor().getCedula());
					          txtCedulaTutorRegistrarFactibilidad.setDisabled(true);
					          txtNombreTutorRegistrarFactibilidad.setValue(teg.getTutor().getNombre());
					          txtNombreTutorRegistrarFactibilidad.setDisabled(true);
					          txtApellidoTutorRegistrarFactiblidad.setValue(teg.getTutor().getApellido());
					          txtApellidoTutorRegistrarFactiblidad.setDisabled(true);
					          List<Factibilidad> factibilidad2 = ServicioRegistrarFactibilidad.buscarProfesores(id);
					       //  ltbComisiónRegistrarFactibilidad.setModel(new ListModelList<Factibilidad>(factibilidad2));
					          txtNombreTutorFactibilidad.setValue(factibilidad1.getProfesor().getNombre());
					          txtNombreTutorFactibilidad.setDisabled(true);
					          txtApellidoTutorFactibilidad.setValue(factibilidad1.getProfesor().getApellido());
					          txtApellidoTutorFactibilidad.setDisabled(true);
					          txtObservacionRegistrarFactibilidad.setValue(factibilidad1.getObservacion());
					          txtObservacionRegistrarFactibilidad.setDisabled(true);
					         List<ItemFactibilidad> ItemFactibilidad1 = ServicioItemFactibilidad.buscarItemFactibilidad(factibilidad1);
					         ltbItemsFactibilidad.setModel(new ListModelList<ItemFactibilidad>(ItemFactibilidad1));
							map.clear();
							map = null;
						}
					}
		}
//Metodo que limpia los campos
	public void limpiar(){
         txtAreaRegistrarFactibilidad.setDisabled(false);
         txtTematicaRegistrarFactibilidad.setDisabled(false);
         txtTituloRegistrarFactibilidad.setDisabled(false);
         txtCedulaTutorRegistrarFactibilidad.setDisabled(false);
         txtNombreTutorRegistrarFactibilidad.setDisabled(false);
         txtApellidoTutorRegistrarFactiblidad.setDisabled(false);
         txtNombreTutorFactibilidad.setDisabled(false);
         txtApellidoTutorFactibilidad.setDisabled(false);
         txtObservacionRegistrarFactibilidad.setDisabled(false);
         txtAreaRegistrarFactibilidad.setValue("");
         txtTematicaRegistrarFactibilidad.setValue("");
         txtTituloRegistrarFactibilidad.setValue("");
         txtCedulaTutorRegistrarFactibilidad.setValue("");
         txtNombreTutorRegistrarFactibilidad.setValue("");
         txtApellidoTutorRegistrarFactiblidad.setValue("");
         txtNombreTutorFactibilidad.setValue("");
         txtApellidoTutorFactibilidad.setValue("");
         txtObservacionRegistrarFactibilidad.setValue("");
	}

		// Metodo que permite visualizar el catalogo de factibilidad.
		@Listen("onClick = #btnRegistrarFactibilidad")
		public void buscarEstudiante() {

			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoFactibilidad.zul", null, null);
			window.doModal();
			catalogo.recibir("transacciones/VRegistrarFactibilidad");

		}
	//Metodo para actualizar el estado del proyecto a ProyectoFactible
	//cuando se acepta un proyecto
		@Listen("onClick = #btnAceptarFactibilidad")
		public void AceptarFactibilidad(){
			
			if ((txtAreaRegistrarFactibilidad.getText().compareTo("") == 0)) {
				Messagebox.show("Debe buscar un proyecto", "Error",
						Messagebox.OK, Messagebox.ERROR);
			}
			else{
			Messagebox.show("¿Desea Aceptar el proyecto?",
					"Dialogo de confirmación", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
							    Factibilidad factibilidad2 = ServicioRegistrarFactibilidad.buscarIdTeg(id);
							    //System.out.println(factibilidad2.getTeg().getId());
							    Teg ObjetoTeg = ServicioTeg.buscarTeg(factibilidad2.getTeg().getId());
                                 ObjetoTeg.setEstatus("ProyectoFactible");
								ServicioTeg.guardar(ObjetoTeg);
								factibilidad2.setEstatus("Aceptado");
								ServicioRegistrarFactibilidad.guardar(factibilidad2);
								limpiar();
								Messagebox.show(
										"Proyecto Aceptado exitosamente",
										"Información", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});
		}
			
		}
		
		//Metodo para actualizar el estado del proyecto a ProyectoNoFactible
		//cuando se rechaza un proyecto
			@Listen("onClick = #btnRechazarFactibilidad")
			public void RechazarFactibilidad(){
				
				if ((txtAreaRegistrarFactibilidad.getText().compareTo("") == 0)) {
					Messagebox.show("Debe buscar un proyecto", "Error",
							Messagebox.OK, Messagebox.ERROR);
				}
				else{
				Messagebox.show("¿Desea rechazar el proyecto?",
						"Dialogo de confirmación", Messagebox.OK
								| Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
								    Factibilidad factibilidad3 = ServicioRegistrarFactibilidad.buscarIdTeg(id);
								    Teg ObjetoTeg1 = ServicioTeg.buscarTeg(factibilidad3.getTeg().getId());
	                                 ObjetoTeg1.setEstatus("ProyectoNoFactible");
									ServicioTeg.guardar(ObjetoTeg1);
									factibilidad3.setEstatus("Rechazado");
									ServicioRegistrarFactibilidad.guardar(factibilidad3);
									limpiar();
									Messagebox.show(
											"Proyecto rechazado exitosamente",
											"Información", Messagebox.OK,
											Messagebox.INFORMATION);
								}
							}
						});
			}
				
			}
}
