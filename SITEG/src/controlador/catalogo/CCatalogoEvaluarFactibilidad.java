package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Factibilidad;
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

import servicio.STeg;
import servicio.SProfesor;
import servicio.seguridad.SUsuario;

import configuracion.GeneradorBeans;
import controlador.CEvaluarFactibilidad;
import controlador.CGeneral;

@Controller
public class CCatalogoEvaluarFactibilidad extends CGeneral {
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();

	CEvaluarFactibilidad vistaFactibilidad = new CEvaluarFactibilidad();

	@Wire
	private Textbox txtEstudianteFactibilidad;
	@Wire
	private Listbox ltbListaFactibilidad;
	@Wire
	private Window wdwCatalogoEvaluarFactibilidad;
	@Wire
	private Textbox txtMostrarFechaFactibilidad;
	@Wire
	private Textbox txtMostrarTematicaFactibilidad;
	@Wire
	private Textbox txtMostrarAreaFactibilidad;
	@Wire
	private Textbox txtMostrarTituloFactibilidad;
	@Wire
	private Textbox txtMostrarNombreTutorFactibilidad;
	@Wire
	private Textbox txtMostrarApellidoTutorFactibilidad;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Teg> tegs = buscarDatos();
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			tegs.get(i).setEstatus(nombre+" "+apellido);
		}
		ltbListaFactibilidad.setModel(new ListModelList<Teg>(tegs));

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		
		

	}
	
	

	// Metodo que permite obtener una lista de los teg de acuerdo al
	// programa del profesor que se encuentra loggeado
	public List<Teg> buscarDatos() {

		
		List<Teg> tegs = servicioTeg.buscarTegDeComision(ObtenerUsuarioProfesor());
		
		
		List<Teg> tegComisionAsignada = new ArrayList<Teg>();
		for (int i = 0; i < tegs.size(); i++) {

			if (tegs.get(i).getEstatus().equals("Comision Asignada")) {

				tegComisionAsignada.add(tegs.get(i));
			}
		}
		return tegComisionAsignada;

		
	}

	// Metodo que permite filtrar un teg de acuerdo a la fecha, tematica, area,
	// titulo, nombre y apellido del tutor
	@Listen("onChange = #txtMostrarFechaFactibilidad, #txtMostrarTematicaFactibilidad,#txtMostrarAreaFactibilidad,#txtMostrarTituloFactibilidad,#txtMostrarNombreTutorFactibilidad,# txtMostrarApellidoTutorFactibilidad")
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
			if (teg.getFecha()
					.toString()
					.toLowerCase()
					.contains(
							txtMostrarFechaFactibilidad.getValue()
									.toLowerCase())
						&& servicioEstudiante.buscarEstudiantePorTeg(teg)
							.get(0)
							.getNombre()
							.toLowerCase()
							.contains(
									txtEstudianteFactibilidad.getValue()
											.toLowerCase())
					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematicaFactibilidad.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarAreaFactibilidad.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTituloFactibilidad.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutorFactibilidad
											.getValue().toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutorFactibilidad
											.getValue().toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbListaFactibilidad.setModel(new ListModelList<Teg>(teg2));

	}

	//Metodo que permite mostrar los datos del catalogo
	@Listen("onDoubleClick = #ltbListaFactibilidad")
	public void mostrarDatosCatalogo() {
		if(ltbListaFactibilidad.getItemCount()!=0){
		Listitem listItem = ltbListaFactibilidad.getSelectedItem();
		if(listItem!=null){
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "transacciones/VEvaluarFactibilidad";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("tegCatalogo", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VEvaluarFactibilidad.zul", null, null);
		window.doModal();
		vistaFactibilidad.recibir("catalogos/VCatalogoEvaluarFactibilidad");
		}
	}
	}
	

	

}