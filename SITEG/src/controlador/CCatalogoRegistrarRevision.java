package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
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

import servicio.STeg;
import servicio.SUsuario;
import servicio.SProfesor;

import configuracion.GeneradorBeans;

@Controller
public class CCatalogoRegistrarRevision extends CGeneral {
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	
	CRegistrarRevision vistaRegistrarRevision = new CRegistrarRevision();
	@Wire
	private Textbox txtEstudianteRevision;
	@Wire
	private Listbox ltbTrabajosRegistrados;
	@Wire
	private Window wdwCatalogoRegistrarRevisiones;
	@Wire
	private Textbox txtFechaRegistrarRevision;
	@Wire
	private Textbox txtTematicaRegistrarRevision;
	@Wire
	private Textbox txtAreaRegistrarRevision;
	@Wire
	private Textbox txtTituloRegistrarRevision;
	@Wire
	private Textbox txtNombreTutorRegistrarRevision;
	@Wire
	private Textbox txtApellidoTutorRegistrarRevision;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Teg> tegs = buscarDatos();
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			tegs.get(i).setEstatus(nombre+" "+apellido);
		}
		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(tegs));

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
	
	}

	// Metodo que permite obtener una lista de los teg de acuerdo al
	//profesor que se encuentra loggeado
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Teg> tegs = servicioTeg.buscarTegRegistrado();

		Profesor profesor1 = new Profesor();
		List<Teg> t = new ArrayList<Teg>();

		for (int i = 0; i < tegs.size(); i++) {

			profesor1 = tegs.get(i).getTutor();

			boolean encontre = false;

			for (int j = 0; j < profesores.size(); j++) {

				if (profesores.get(j).getCedula().equals(profesor1.getCedula())) {
					encontre = true;
				}
			}
			if (encontre == true) {
				t.add(tegs.get(i));

			}

		}

		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(t));
		return t;
	}

	// Metodo que permite filtrar un teg de acuerdo a la fecha, tematica, area,
	// titulo, nombre y apellido del tutor
	@Listen("onChange = #txtFechaRegistrarRevision, #txtTematicaRegistrarRevision,#txtAreaRegistrarRevision,#txtTituloRegistrarRevision,#txtNombreTutorRegistrarRevision,#txtApellidoTutorRegistrarRevision")
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
							txtEstudianteRevision.getValue()
									.toLowerCase())
					&& teg.getFecha().toString().toLowerCase()
					.contains(txtFechaRegistrarRevision.getValue().toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaRegistrarRevision.getValue().toLowerCase())

					&& teg.getTematica().getareaInvestigacion().getNombre()
							.toLowerCase()
							.contains(txtAreaRegistrarRevision.getValue().toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(txtTituloRegistrarRevision.getValue().toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreTutorRegistrarRevision.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoTutorRegistrarRevision.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(teg2));

	}
	
	
	//Metodo que permite mostrar los datos del catalogo
	@Listen("onDoubleClick = #ltbTrabajosRegistrados")
	public void mostrarDatosCatalogo() {
		if(ltbTrabajosRegistrados.getItemCount()!=0){
		Listitem listItem = ltbTrabajosRegistrados.getSelectedItem();
		if(listItem!=null){
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "transacciones/VRegistrarRevision";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("tegCatalogo", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VRegistrarRevision.zul", null, null);
		window.doModal();
		vistaRegistrarRevision.recibir("catalogos/VCatalogoRegistrarRevision");
		}
		}
	}

}

