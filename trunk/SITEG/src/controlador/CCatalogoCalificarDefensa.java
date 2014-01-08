package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SProfesor;
import servicio.STeg;
import configuracion.GeneradorBeans;

@Controller
public class CCatalogoCalificarDefensa extends CGeneral {
	
	@Wire
	private Listbox ltbCalificarDefensa;
	
	@Wire
	private Textbox txtMostrarFechaCalificar;
	@Wire
	private Textbox txtMostrarTematicaCalificar;
	@Wire
	private Textbox txtMostrarAreaCalificar;
	@Wire
	private Textbox txtMostrarTituloCalificar;
	@Wire
	private Textbox txtMostrarNombreTutorCalificar;
	@Wire
	private Textbox txtMostrarApellidoTutorCalificar;
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	
	@Override
	void inicializar(Component comp) {
		
		List<Teg>  t = buscarDatos();
		ltbCalificarDefensa.setModel(new ListModelList<Teg> (t));
		Selectors.wireComponents(comp, this, false) ;

		

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");
		
		
		
	}
	
	public List<Teg> buscarDatos()
	{
			Profesor profesor = ObtenerUsuarioProfesor();		
			Programa programa = new Programa();			
			programa= profesor.getPrograma();			
			List<Profesor>  profesores = servicioProfesor.buscarProfesorDelPrograma(programa);			
			List<Teg>  tegs = servicioTeg.BuscarTegCalificandoDefensa();
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
