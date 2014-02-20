package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
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
import servicio.STeg;
import servicio.SProfesor;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;
import controlador.CGeneral;
import controlador.CRegistrarRevision;

@Controller
public class CCatalogoRegistrarRevision extends CGeneral {

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

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los tegs
	 * disponibles mediante el metodo "buscarDatos()", recorriendolo uno a uno
	 * para luego cargar una lista de estudiantes por teg donde mediante la
	 * implementacion del servicio de busqueda se va obteniendo su nombre y su
	 * apellido y se va seteando temporalmente en la variable estatus del teg
	 * para poder visualizarlo en el componente lista de teg de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Teg> tegs = buscarDatos();
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
			tegs.get(i).setEstatus(nombre + " " + apellido);
		}
		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(tegs));
	}

	/*
	 * Metodo que permite retornar un lista de los tegs, donde se recorre tanto
	 * la lista del teg como los profesores activos, donde se compara si coincide las
	 * cedulas de cada uno de los profesores para cargar la lista de tegs.
	 */
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Teg> tegs = servicioTeg.buscarTegRegistrado();

		Profesor profesor1 = new Profesor();
		List<Teg> tegs1 = new ArrayList<Teg>();

		for (int i = 0; i < tegs.size(); i++) {

			profesor1 = tegs.get(i).getTutor();

			boolean encontre = false;

			for (int j = 0; j < profesores.size(); j++) {

				if (profesores.get(j).getCedula().equals(profesor1.getCedula())) {
					encontre = true;
				}
			}
			if (encontre == true) {
				tegs1.add(tegs.get(i));

			}

		}

		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(tegs1));
		return tegs1;
	}

	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante, la fecha, la
	 * tematica, el area y el titulo, el nombre y apellido del tutor de estos.
	 */
	@Listen("onChange = #txtEstudianteRevision, #txtFechaRegistrarRevision, #txtTematicaRegistrarRevision,#txtAreaRegistrarRevision,#txtTituloRegistrarRevision")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscarDatos();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
			teg1.get(i).setEstatus(nombre + " " + apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg).get(0)
					.getNombre().toLowerCase()
					.contains(txtEstudianteRevision.getValue().toLowerCase())
					&& teg.getFecha()
							.toString()
							.toLowerCase()
							.contains(
									txtFechaRegistrarRevision.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaRegistrarRevision.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtAreaRegistrarRevision.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtTituloRegistrarRevision.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(teg2));

	}

	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbTrabajosRegistrados")
	public void mostrarDatosCatalogo() {
		if (ltbTrabajosRegistrados.getItemCount() != 0) {
			Listitem listItem = ltbTrabajosRegistrados.getSelectedItem();
			if (listItem != null) {
				Teg tegDatosCatalogo = (Teg) listItem.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", tegDatosCatalogo.getId());
				String vista = "transacciones/VRegistrarRevision";
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("tegCatalogo", map);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VRegistrarRevision.zul", null,
						null);
				window.doModal();
				vistaRegistrarRevision
						.recibir("catalogos/VCatalogoRegistrarRevision");
			}
		}
	}

}
