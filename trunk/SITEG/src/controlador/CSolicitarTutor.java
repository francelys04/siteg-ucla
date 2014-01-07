package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.Categoria;
import modelo.CondicionPrograma;
import modelo.Estudiante;
import modelo.ProgramaArea;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.TipoJurado;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SAreaInvestigacion;
import servicio.SProfesor;
import servicio.SPrograma;
import configuracion.GeneradorBeans;

//es un controlador de Solicitar Tutor
@Controller
public class CSolicitarTutor extends CGeneral {
	
	private static final int Estudiante = 0;
	//servicios para los dos modelos implicados
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SProgramaArea servicioProgramaArea = GeneradorBeans.getServicioProgramaArea();
	SAreaInvestigacion servicioAreaInvestigacion = GeneradorBeans.getServicioArea();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SSolicitudTutoria servicioSolicitarTutor = GeneradorBeans.getServicioTutoria();
	CCatalogoProfesorArea catalogo = new CCatalogoProfesorArea();

	@Wire
	private Datebox db1;
	@Wire
	private Button btnAgregarEstudiante;
	@Wire
	private Listbox ltbEstudiantes;

	//atributos del Proyecto
	@Wire
	private Combobox cmbProgramaSolicitud;
	@Wire
	private Combobox cmbAreaSolicitud;
	@Wire
	private Combobox cmbTematicaSolicitud;
	@Wire
	private Textbox txtTituloSolicitud;

	//atributos del Estudiante
	@Wire
	private Textbox txtCedulaEstudiante;
	
	
	//atributos del Tutor
	@Wire
	private Textbox txtCedulaProfesor;
	@Wire
	private Textbox txtNombreProfesor;
	@Wire
	private Textbox txtApellidoProfesor;
	@Wire
	private Textbox txtCorreoProfesor;

	//atributos de la pantalla del catalogo
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;
	@Wire
	private Window wdwSolicitarTutoria;
	@Wire
	private Window wdwCatalogoProfesorArea;
	
	private long id;
	private int valor;
	
	List<Estudiante> gridEstudiante = new ArrayList<Estudiante>();
	
	//metodo para mapear los datos del Tutor
	void inicializar(Component comp) {
		
		
		List<Programa> programas = servicioPrograma.buscarActivas();
		cmbProgramaSolicitud.setModel(new ListModelList<Programa>(
				programas));
		
		cmbAreaSolicitud.setDisabled(true);
		cmbTematicaSolicitud.setDisabled(true);
		
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesor.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor.buscarProfesorPorCedula(txtCedulaProfesor.getValue());
				txtNombreProfesor.setValue(profesor.getNombre());
				txtApellidoProfesor.setValue(profesor.getApellido());
				txtCorreoProfesor.setValue(profesor.getCorreoElectronico());
				map.clear();
				map = null;
			}
		}
	}
	
	public void limpiarDatosEstudiante(){
		
		txtCedulaEstudiante.setValue("");
		
	}
	@Listen("onSelect = #cmbProgramaSolicitud")
	public void areaSolicitud(){
		
		cmbAreaSolicitud.setDisabled(false);
		
		String programa = cmbProgramaSolicitud.getValue();
		Programa programa2 = servicioPrograma.buscarPorNombrePrograma(programa);
		
		List<AreaInvestigacion> a = servicioProgramaArea.buscarAreasDePrograma(programa2);
		cmbAreaSolicitud.setModel(new ListModelList<AreaInvestigacion>(
				a));
		
	}
	
	@Listen("onSelect = #cmbAreaSolicitud")
	public void tematicaSolicitud(){
		
		cmbTematicaSolicitud.setDisabled(false);
		
		String area = cmbAreaSolicitud.getValue();
		AreaInvestigacion area2 = servicioAreaInvestigacion.buscarAreaPorNombre(area);
		
		
		List<Tematica> tematicas = servicioTematica.buscarTematicasDeArea(area2);
		cmbTematicaSolicitud.setModel(new ListModelList<Tematica>(
				tematicas));
	}
	
	
	@Listen("onClick = #btnCatalogoProfesorArea")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesorArea.zul", null, null);
		window.doModal();
		catalogo.recibir("transacciones/VSolicitarTutor");
		 wdwSolicitarTutoria.onClose();
		

	}
	
	@Listen("onClick = #btnEnviarSolicitudtutoria")
	public void enviarSolicitud(){
		
		Date fecha = db1.getValue();
		String estado = "Por Revisar";
		String programa = cmbProgramaSolicitud.getValue();
		String area = cmbAreaSolicitud.getValue();
		String tematica = cmbTematicaSolicitud.getValue();
		String titulo = txtTituloSolicitud.getValue();
		
		String cedulaEstudiante = txtCedulaEstudiante.getValue();
		
		
		String cedulaProfesor = txtCedulaProfesor.getValue();
		String nombreProfesor = txtNombreProfesor.getValue();
		String apellidoProfesor = txtApellidoProfesor.getValue();
		String correoProfesor = txtCorreoProfesor.getValue();
		
		
		if(gridEstudiante.size() == 0){
			
			if(programa=="Seleccione una Opci�n" || area=="Seleccione una Opci�n" || tematica=="Seleccione una Opci�n" || titulo==""
					   || cedulaEstudiante=="" 
					    || cedulaProfesor=="" || nombreProfesor=="" || apellidoProfesor==""
					   || correoProfesor=="" ){
						
						Messagebox.show("Debe llenar todos los campos", "Campos Vac�os", Messagebox.OK, Messagebox.INFORMATION);
					}
			else{
				
				Estudiante estudiante = servicioEstudiante.buscarEstudiante(cedulaEstudiante);
				
				if(estudiante == null){
					Messagebox.show("Debes estar Previamente Registrado", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);	
				}
				else{
					 
					SolicitudTutoria solicitud = servicioSolicitarTutor.buscarSolicitudEstudiante(estudiante);
					 
					 if(solicitud != null){
						 Messagebox.show("Ya tienes una Solicitud Pendiente", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
					 }
					 else{
						 
						 List<Teg> teg = servicioTeg.buscarTegPorEstudiante(estudiante);
							if (teg.size() > 0) {
								Messagebox.show("Ya Tienes Un TEG en Proceso",
										"Informaci�n", Messagebox.OK,
										Messagebox.INFORMATION);
							} else {
						 Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedulaProfesor);

						 if(profesor == null){
							 Messagebox.show("El Profesor no Existe", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION); 
						 }
						 else{
							 
							 List<Teg> teg2 = servicioTeg.buscarTutoriaProfesor(profesor);
							 String nombre2 = "Numero de tutorias por profesor";
							 buscarCondicionVigenteEspecifica(nombre2);
							 
							 if(teg2.size() >= valor){
								 Messagebox.show("El Profesor ya tiene un maximo de proyectos asignados", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
								 }
							 else{
								   Tematica tematica2 = servicioTematica.buscarTematicaPorNombre(tematica);
									
								   Set<Estudiante> estudiante2 = new HashSet<Estudiante>();
								   Estudiante estudiante3 = servicioEstudiante.buscarEstudiante(cedulaEstudiante);
								   estudiante2.add(estudiante3);

								   SolicitudTutoria solicitud2 = new SolicitudTutoria(id, fecha, titulo, estado, profesor, tematica2, estudiante2);
								   
								   servicioSolicitarTutor.guardarSolicitud(solicitud2);
								   enviarEmailNotificacion();
								   Messagebox.show("Su Solicitud ha sido enviada", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
							 }
						 }
							}
					 }
				}
			}
		}
		else{
			if(programa=="Seleccione una Opci�n" || area=="Seleccione una Opci�n" || tematica=="Seleccione una Opci�n" || titulo==""
					   || cedulaProfesor=="" || nombreProfesor=="" || apellidoProfesor==""
					   || correoProfesor=="" ){
						
						Messagebox.show("Debe llenar todos los campos", "Campos Vac�os", Messagebox.OK, Messagebox.INFORMATION);
					}
			else{
				
				 Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedulaProfesor);
				 
				 if(profesor == null){
					 Messagebox.show("El Profesor no Existe", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION); 
				 }
				 else{
					 List<Teg> teg2 = servicioTeg.buscarTutoriaProfesor(profesor);
					 String nombre2 = "Numero de tutorias por profesor";
					 buscarCondicionVigenteEspecifica(nombre2);
					 
					 if(teg2.size() >= valor){
							 Messagebox.show("El Profesor ya tiene un maximo de proyectos asignados", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);	
						 }
					 else{
						   Tematica tematica2 = servicioTematica.buscarTematicaPorNombre(tematica);
							
						   Set<Estudiante> estudiante2 = new HashSet<Estudiante>();
							for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
								Estudiante estudiante3 = ltbEstudiantes.getItems().get(i)
										.getValue();
								estudiante2.add(estudiante3);
							}
							
						   SolicitudTutoria solicitud2 = new SolicitudTutoria(id, fecha, titulo, estado, profesor, tematica2, estudiante2);
						   
						   servicioSolicitarTutor.guardarSolicitud(solicitud2);
						   enviarEmailNotificacion();
						  
						  Messagebox.show("Su Solicitud ha sido enviada", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
						  cancelarSolicitud();
					 }
				 }
			}
		}

		}
	

	
	@Listen("onClick = #btnCancelarSolicitudTutoria")
	public void cancelarSolicitud(){
		id = 0;
		
		cmbProgramaSolicitud.setValue("");
		cmbAreaSolicitud.setValue("");
		cmbTematicaSolicitud.setValue("");
		cmbAreaSolicitud.setDisabled(true);
		cmbTematicaSolicitud.setDisabled(true);
		txtTituloSolicitud.setValue("");
		
		txtCedulaEstudiante.setValue("");
		ltbEstudiantes.getItems().clear();
		
		txtCedulaProfesor.setValue("");
		txtNombreProfesor.setValue("");
		txtApellidoProfesor.setValue("");
		txtCorreoProfesor.setValue("");
		
	}
	

	
	@Listen("onClick = #btnAgregarEstudiante")
	public void AgregarEstudiante(){
		 String cedulaEstudiante = txtCedulaEstudiante.getValue();
		 
		 int tamano = gridEstudiante.size();
		 

		if(cedulaEstudiante == ""){
			Messagebox.show("Debes colocar Tus Datos", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
		}
		else{
			if(tamano == 0){			
				Estudiante estudiante = servicioEstudiante.buscarEstudiante(cedulaEstudiante);
				if(estudiante == null){
					Messagebox.show("Debes estar Previamente Registrado", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
				}
				else{
					
					SolicitudTutoria solicitud = servicioSolicitarTutor.buscarSolicitudEstudiante(estudiante);
					 
					 if(solicitud != null){
						 Messagebox.show("Ya tienes una Solicitud Pendiente", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
					 }
					 else{
						 List<Teg> teg = servicioTeg.buscarTegPorEstudiante(estudiante);
							if (teg.size() > 0) {
								Messagebox.show("Ya Tienes Un TEG en Proceso",
										"Informaci�n", Messagebox.OK,
										Messagebox.INFORMATION);
							} else {
							 	gridEstudiante.add(estudiante);
								ltbEstudiantes.setModel(new ListModelList<Estudiante>(gridEstudiante));
								limpiarDatosEstudiante();
							}
					 }

				}
			}
			else{
				Estudiante estudiante2 = servicioEstudiante.buscarEstudiante(cedulaEstudiante);
				if(estudiante2 == null){
					Messagebox.show("Debes estar Previamente Registrado", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
				}
				else{
					SolicitudTutoria solicitud = servicioSolicitarTutor.buscarSolicitudEstudiante(estudiante2);
					 
					 if(solicitud != null){
						 Messagebox.show("Ya tienes una Solicitud Pendiente", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
					 }
					 else{
						 List<Teg> teg = servicioTeg.buscarTegPorEstudiante(estudiante2);
							if (teg.size() > 0) {
								Messagebox.show("Ya Tienes Un TEG en Proceso",
										"Informaci�n", Messagebox.OK,
										Messagebox.INFORMATION);
							} else {
						 String nombre = "Numero de estudiantes por trabajo";
						 buscarCondicionVigenteEspecifica(nombre);
						 if(tamano < valor){
						 	gridEstudiante.add(estudiante2);
							ltbEstudiantes.setModel(new ListModelList<Estudiante>(gridEstudiante));
							limpiarDatosEstudiante();
						 }
						 else
						 {
							 Messagebox.show("Solo se permiten "+valor+" Estudiantes por proyecto", "Informaci�n", Messagebox.OK, Messagebox.INFORMATION);
						 }
							}
					 }
				}
			}
		}

	}
	
	public CondicionPrograma buscarCondicionVigenteEspecifica(String nombre){
		String programa = cmbProgramaSolicitud.getValue();
		Programa programa2 = servicioPrograma.buscarPorNombrePrograma(programa);
		List<CondicionPrograma> condicionesActuales = servicioCondicionPrograma.buscarUltimasCondiciones(programa2);
		CondicionPrograma condicionBuscada = new CondicionPrograma();
		
		for(int i=0; i<condicionesActuales.size(); i++){
			if(condicionesActuales.get(i).getCondicion().getNombre().equals(nombre)){
				condicionBuscada = condicionesActuales.get(i);
				valor = condicionBuscada.getValor();
			}
		}
		return condicionBuscada;
	}
	
	private boolean enviarEmailNotificacion(){
	    try
	    {
	        
	        for (int i = 0; i < ltbEstudiantes.getItemCount(); i++) {
	            Estudiante estudiante = ltbEstudiantes.getItems().get(i).getValue();
	            Profesor profesor = servicioProfesor.buscarProfesorPorCedula(txtCedulaProfesor.getValue());            
	                       
	         // Propiedades de la conexion
	        Properties props = new Properties();
	        props.setProperty("mail.smtp.host",  "smtp.gmail.com");
	        props.setProperty("mail.smtp.starttls.enable", "true");
	        props.setProperty("mail.smtp.port", "587");
	        props.setProperty("mail.smtp.auth", "true");
	       
	        Session session = Session.getDefaultInstance(props);
	        //Recoger los datos
	        String asunto = "Notificacion de SITEG";
	        String remitente = "siteg.ucla@gmail.com"; 
	        String contrasena = "Equipo.2";
	        String destino = profesor.getCorreoElectronico();
	        String mensaje = " Solicitud de tutoria \n\n " +
	        		"Estudiante: "+estudiante.getNombre()+" , "+estudiante.getApellido()+"\n\n con Titulo de Proyecto: "+txtTituloSolicitud.getValue();
	        
	       
	        //Obtenemos los destinatarios
	        String destinos[] = destino.split(",");
	                
	        // Construimos el mensaje
	        MimeMessage message = new MimeMessage(session);
	         
	        message.setFrom(new InternetAddress( remitente ));
	 
	        //Forma 3
	        Address [] receptores = new Address [ destinos.length ];
	        int j = 0;
	        while(j<destinos.length){                   
	        receptores[j] = new InternetAddress ( destinos[j] ) ;                  
	        j++;               
	        }
	 
	        
	        //receptores.
	        message.addRecipients(Message.RecipientType.TO, receptores);       
	        message.setSubject(asunto);       
	        message.setText(mensaje);
	             
	        // Lo enviamos.
	        Transport t = session.getTransport("smtp");
	        t.connect(remitente,contrasena);
	        t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
	                 
	        // Cierre de la conexion.
	        t.close();
	        }
	        return true;
	    }
	   
	    catch (Exception e)
	    {
	        e.printStackTrace();
	        return false;
	    } 
	    
	}

}