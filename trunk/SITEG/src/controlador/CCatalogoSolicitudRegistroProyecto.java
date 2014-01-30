package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Cronograma;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.STeg;
import servicio.SUsuario;


import configuracion.GeneradorBeans;


@Controller
public class CCatalogoSolicitudRegistroProyecto extends CGeneral {
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	@Wire
	private Textbox txtEstudianteProyecto;
	@Wire
	private Listbox ltbSolcitudRegistroProyecto;
	@Wire
	private Window wdwCatalogoSolicitudRegistroProyecto;
	@Wire
	private Textbox txtMostrarFecha;
	@Wire
	private Textbox txtMostrarTematica;
	@Wire
	private Textbox txtMostrarArea;
	@Wire
	private Textbox txtMostrarTitulo;
	@Wire
	private Textbox txtMostrarNombreTutor;
	@Wire
	private Textbox txtMostrarApellidoTutor;
	
	CVerificarSolicitudProyecto vistaVerificar = new CVerificarSolicitudProyecto();

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		/*
		 * Listado de todos los teg cuyo
		 * estatus=SolicitandoRegistro
		 */
	
		List<Teg>  t = buscarDatos();
		for (int i = 0; i < t.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(t.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			t.get(i).setEstatus(nombre+" "+apellido);
		}
		ltbSolcitudRegistroProyecto.setModel(new ListModelList<Teg> (t));
		Selectors.wireComponents(comp, this, false) ;

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido	 */
		
	}
	
	@Listen("onChange = #txtMostrarFecha,#txtMostrarTematica,#txtMostrarArea,#txtMostrarTitulo,#txtMostrarNombreTutor,# txtMostrarApellidoTutor")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscarDatos();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			teg1.get(i).setEstatus(nombre+" "+apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg)
					.get(0)
					.getNombre()
					.toLowerCase()
					.contains(
							txtEstudianteProyecto.getValue()
									.toLowerCase())
									&&teg
					.getFecha().toString()
					.toLowerCase()
					.contains(
					txtMostrarFecha.getValue().toLowerCase())
					
				&&	teg
					.getTematica().getNombre()
					.toLowerCase()
					.contains(
							txtMostrarTematica.getValue().toLowerCase())					
							
					&& teg
							.getTematica().getareaInvestigacion().getNombre()
							.toLowerCase()
							.contains(
									txtMostrarArea.getValue()
											.toLowerCase())
											
					&& teg
							.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTitulo.getValue()
											.toLowerCase())
					&& teg
							.getTutor().getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutor.getValue()
											.toLowerCase())
					&& teg
							.getTutor().getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutor.getValue()
											.toLowerCase()))
	
			
			{
				teg2.add(teg);
			}
		}

		ltbSolcitudRegistroProyecto.setModel(new ListModelList<Teg>(teg2));

	}
	
	//permite llevar los datos del teg a las vista verificar solicitud de proyecto
	@Listen("onDoubleClick = #ltbSolcitudRegistroProyecto")
	public void mostrarDatosCatalogo() {	
		if(ltbSolcitudRegistroProyecto.getItemCount()!=0){
			Listitem listItem = ltbSolcitudRegistroProyecto.getSelectedItem();
			if(listItem!=null){
			Teg tegDatosCatalogo = (Teg) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", tegDatosCatalogo.getId());			
			String vista = "transacciones/VVerificarSolicitudProyecto";
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("tegCatalogo", map);
			Window window = (Window) Executions.createComponents(
					"/vistas/transacciones/VVerificarSolicitudProyecto.zul", null, null);	 				
			window.doModal();
			vistaVerificar.recibir("catalogos/VCatalogoSolicitudRegistroProyecto");
		}
		}
	}
	public List<Teg> buscarDatos()
	{
			List<Profesor>  profesores = servicioProfesor.buscarActivos();
			List<Teg>  tegs = servicioTeg.BuscarTegSolicitandoRegistro();
			Profesor profesor1 = new Profesor();		
			List<Teg> t = new ArrayList<Teg>();			
			for (int i = 0; i < tegs.size(); i++) {
				profesor1 = tegs.get(i).getTutor();
				boolean encontre = false;
				for (int j = 0; j < profesores.size(); j++) {
					if (profesores.get(j).getCedula().equals(profesor1.getCedula())) {
						System.out.print(profesores.get(j).getCedula());
						encontre = true;
					}
				}
				if (encontre == true) {
					t.add(tegs.get(i));

				}
			}
			return t;
	}	
}
