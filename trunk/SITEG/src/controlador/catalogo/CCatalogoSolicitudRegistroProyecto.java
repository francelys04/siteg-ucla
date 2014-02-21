package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;


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
import servicio.seguridad.SUsuario;



import configuracion.GeneradorBeans;
import controlador.CGeneral;
import controlador.CVerificarSolicitudProyecto;


@Controller
public class CCatalogoSolicitudRegistroProyecto extends CGeneral {
	
	CVerificarSolicitudProyecto vistaVerificar = new CVerificarSolicitudProyecto();

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
	
	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los tegs
	 * disponibles mediante el metodo "buscarDatos()", recorriendolo uno a uno
	 * para luego cargar una lista de estudiantes por teg donde mediante la
	 * implementacion del servicio de busqueda se va obteniendo su nombre y su
	 * apellido y se va seteando temporalmente en la variable estatus del teg
	 * para poder visualizarlo en el componente lista de teg de la vista.
	 */
	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		//hizo kairin nuevo
		List<Teg>tegs= servicioTeg
		.buscarTegPorProgramaParaRegistrarTeg(servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor()));
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
			tegs.get(i).setEstatus(nombre+" "+apellido);
		}
		ltbSolcitudRegistroProyecto.setModel(new ListModelList<Teg> (tegs));
		Selectors.wireComponents(comp, this, false) ;
	}
	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante, la fecha, la
	 * tematica, el area y el titulo, el nombre y apellido del tutor de estos.
	 */
	@Listen("onChange = #txtEstudianteProyecto, #txtMostrarFecha,#txtMostrarTematica,#txtMostrarArea,#txtMostrarTitulo,#txtMostrarNombreTutor,#txtMostrarApellidoTutor")
	public void filtrarDatosCatalogo() {
		//hizo kairin nuevo
		List<Teg> teg1 = servicioTeg
		.buscarTegPorProgramaParaRegistrarTeg(servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor()));
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
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
	
	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista a la vista asociada.
	 */
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
	
}
