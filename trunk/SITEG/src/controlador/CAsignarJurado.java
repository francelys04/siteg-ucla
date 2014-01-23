package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Condicion;
import modelo.CondicionPrograma;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Jurado;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TipoJurado;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SCondicionPrograma;
import servicio.SDefensa;
import servicio.SJurado;
import servicio.STeg;
import servicio.STipoJurado;

@Controller
public class CAsignarJurado extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();
	SJurado servicioJurado = GeneradorBeans.getServicioJurado();
	STipoJurado servicioTipoJurado = GeneradorBeans.getServicioTipoJurado();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans.getServicioCondicionPrograma();
	@Wire
	private Textbox txtProgramaAtenderDefensa;
	@Wire
	private Textbox txtAreaAtenderDefensa;
	@Wire
	private Textbox txtTematicaAtenderDefensa;
	@Wire
	private Textbox txtTituloAtenderDefensa;
	@Wire
	private Textbox txtCedulaTutorAtenderDefensa;
	@Wire
	private Textbox txtNombreTutorAtenderDefensa;
	@Wire
	private Textbox txtApellidoTutorAtenderDefensa;
	@Wire
	private Label lblCondicionAtenderDefensa;
	@Wire
	private Listbox ltbJuradoDisponible;
	@Wire
	private Listbox ltbJuradoSeleccionado;
	@Wire
	private Listbox ltbEstudiantesAtenderDefensa;
	@Wire
	private Datebox dtbFechaAtenderDefensa;
	@Wire
	private Datebox dtbFechaDefensa;
	@Wire
	private Textbox txtLugarDefensa;
	@Wire
	private Timebox tmbHoraDefensa;
	@Wire
	private Window wdwAsignarJurado;
	// @Wire
	// private Combobox cmbDefensaTipoJurado;
	private static String vistaRecibida;
	private static String cedula;
	private static String tipoJurado;
	long idTeg = 0;
	long idDefensa = 0;
    private static Programa programa;
    
    Jurado jurado = new Jurado(); 
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);
				
				txtCedulaTutorAtenderDefensa.setValue(teg.getTutor()
						.getCedula());
				txtNombreTutorAtenderDefensa.setValue(teg.getTutor()
						.getNombre());
				txtApellidoTutorAtenderDefensa.setValue(teg.getTutor()
						.getApellido());
				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				//para que se guie por el programa del estudiante
				List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg);
				programa = est.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(est.get(0).getPrograma().getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());
				lblCondicionAtenderDefensa
						.setValue("Recuerde que la cantidad de jurados es de:"
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes del jurado", programa)
										.getValor());
		
			}
		}
	}
	//metodo que permite mover un profesor de disponible a seleccionado
	@Listen("onClick = #btnAgregarJurado")
	public void moverDerechaJurado() {

		Listitem list1 = ltbJuradoDisponible.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbJuradoSeleccionado);
	}
	//metodo que permite mover un profesor de seleccionado a disponible
	@Listen("onClick = #btnRemoverJurado")
	public void moverIzquierdaJurado() {
		Listitem list2 = ltbJuradoSeleccionado.getSelectedItem();
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else{
			list2.setParent(ltbJuradoDisponible);
			}
		}	
	

	public void recibir(String vista) {
		vistaRecibida = vista;

	}
	//Metodo para llenar las listas de jurados tanto disponibles (para ser seleccionado)
	//como los que ya han sido seleccionado en un teg (se los trae de la bd)
	public void llenarListas(Teg teg) {
		List<Estudiante> estudiantesTeg = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		ltbEstudiantesAtenderDefensa.setModel(new ListModelList<Estudiante>(
				estudiantesTeg));
		List<Profesor> juradoDisponible = servicioProfesor
				.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
		List<Jurado> juradoOcupado = servicioJurado.buscarJuradoDeTeg(teg);
		ltbJuradoSeleccionado.setModel(new ListModelList<Jurado>(juradoOcupado));
		ltbJuradoSeleccionado.setDisabled(true);

		
	}


	@Listen("onClick = #btnAceptarDefensaMientrasTanto")
	public void aceptarDefensa() {
		Guardar();
	}
	
	//Metodo que permite finalizar la asignacion del jurado
	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensaDefinit1iva() {
		int valorcondicion = buscarCondicionVigenteEspecifica("Numero de integrantes del jurado", programa).getValor();
		int valorItem = ltbJuradoSeleccionado.getItemCount();
		if (valorItem != valorcondicion) {

			Messagebox.show("El numero de profesores no es correcto",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

		} else {
			for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
				Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
				tipoJurado = ((Combobox) ((listItem.getChildren().get(3)))
						.getFirstChild()).getValue();
				cedula = ((Label) ((listItem.getChildren().get(0))).getFirstChild()).getValue();
				if (tipoJurado == "") {
					Messagebox.show("Debe seleccionar un tipo para cada jurado",
							"Error", Messagebox.OK, Messagebox.ERROR);
				}
				else{
					Messagebox.show("Desea finalizar la asignacion de jurado?",
							"Dialogo de confirmacion", Messagebox.OK
									| Messagebox.CANCEL, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {
										List<Jurado> jurados = new ArrayList<Jurado>();
										Teg teg1 = servicioTeg.buscarTeg(idTeg);
										long cedula1 =  Long.parseLong (cedula);
										Profesor profesorJurado = servicioProfesor.buscarProfesorPorCedula(String.valueOf(cedula1));
										TipoJurado tipo = servicioTipoJurado.buscarPorNombre(tipoJurado);
										Jurado jurado = new Jurado(teg1, profesorJurado, tipo);
										jurados.add(jurado);
										servicioJurado.guardar(jurados);
										Teg teg = servicioTeg.buscarTeg(idTeg);
										teg.setEstatus("Jurado Asignado");
										servicioTeg.guardar(teg);
										Messagebox.show("Los datos del jurado fueron guardados exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
										
										
										
										salir();
									
										
									}
								}
							});
					
					
			
				}
			}	
		
			
		}

	}
	
		
		
	

	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		Teg teg = servicioTeg.buscarTeg(idTeg);
	List<Profesor> juradoDisponible = servicioProfesor
				.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
	ltbJuradoSeleccionado.getItems().clear();
		
	}

	public void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAsignarJurado.onClose();
	}

	//Metodo que permite guardar un jurado asi este no este completamente asigando
	//es decir no este completo segun las condiciones del programa 
	public void Guardar() {
		
		int valorcondicion = buscarCondicionVigenteEspecifica("Numero de integrantes del jurado", programa).getValor();

		Listitem listJurado = ltbJuradoSeleccionado.getSelectedItem();
		if (listJurado == null)
			Messagebox
					.show("Debe Seleccionar los profesores que conformaran el jurado",
							"Error", Messagebox.OK, Messagebox.ERROR);
		else {

			int valorItem = ltbJuradoSeleccionado.getItemCount();

			if (valorItem > valorcondicion) {

				Messagebox
						.show("El numero de profesores seleccionados excede al numero de integrantes del jurado para este programa",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				if (valorItem == valorcondicion) {

					Messagebox
							.show("El numero de profesores que conforman la comision evaluadora esta completo, por favor seleccione el boton de finalizar",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);

				} else {
					for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
						Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
					tipoJurado = ((Combobox) ((listItem.getChildren().get(3))).getFirstChild()).getValue();
					cedula = ((Label) ((listItem.getChildren().get(0))).getFirstChild()).getValue();
						if (tipoJurado == "") {
							Messagebox.show("Debe seleccionar un tipo para cada jurado",
									"Error", Messagebox.OK, Messagebox.ERROR);
						}
						else{
							Messagebox.show("Desea guardar los datos del jurado?",
									"Dialogo de confirmacion", Messagebox.OK
											| Messagebox.CANCEL, Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener() {
										public void onEvent(Event evt)
												throws InterruptedException {
											if (evt.getName().equals("onOK")) {
												List<Jurado> jurados = new ArrayList<Jurado>();
												Teg teg1 = servicioTeg.buscarTeg(idTeg);
												long cedula1 =  Long.parseLong (cedula);
												Profesor profesorJurado = servicioProfesor.buscarProfesorPorCedula(String.valueOf(cedula1));
												TipoJurado tipo = servicioTipoJurado.buscarPorNombre(tipoJurado);
												Jurado jurado = new Jurado(teg1, profesorJurado, tipo);
												jurados.add(jurado);
												servicioJurado.guardar(jurados);
												Messagebox.show("Los datos del jurado fueron guardados exitosamente",
														"Informacion", Messagebox.OK,
														Messagebox.INFORMATION);
												
												
												
												salir();
											
												
											}
										}
									});

						}
					}	
				
					}	
				
					
				}

			}

		}

	}	
