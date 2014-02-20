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
import controlador.CRegistrarAvance;

@Controller
public class CCatalogoRegistrarAvance extends CGeneral {

	CRegistrarAvance vistaRegistrarAvance = new CRegistrarAvance();
	@Wire
	private Textbox txtEstudianteAvance;
	@Wire
	private Listbox ltbProyectosFactibles;
	@Wire
	private Window wdwCatalogoRegistrarAvances;
	@Wire
	private Textbox txtFechaRegistrarAvance;
	@Wire
	private Textbox txtTematicaRegistrarAvance;
	@Wire
	private Textbox txtAreaRegistrarAvance;
	@Wire
	private Textbox txtTituloRegistrarAvance;

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
		ltbProyectosFactibles.setModel(new ListModelList<Teg>(tegs));
	}

	/*
	 * Metodo que permite retornar un lista de los tegs, donde se recorre tanto
	 * la lista del teg como los profesores activos, donde se compara si coincide las
	 * cedulas de cada uno de los profesores para cargar la lista de tegs.
	 */
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Teg> tegs = servicioTeg.buscarProyectoFactible();

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

		ltbProyectosFactibles.setModel(new ListModelList<Teg>(tegs1));
		return tegs1;
	}

	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar la fecha, el nombre y apellido del estudiante, la fecha, la
	 * tematica, el area y el titulo de estos.
	 */
	@Listen("onChange = #txtEstudianteAvance, #txtFechaRegistrarAvance, #txtTematicaRegistrarAvance,#txtAreaRegistrarAvance,#txtTituloRegistrarAvance")
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
					.contains(txtEstudianteAvance.getValue().toLowerCase())
					&& teg.getFecha()
							.toString()
							.toLowerCase()
							.contains(
									txtFechaRegistrarAvance.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaRegistrarAvance.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtAreaRegistrarAvance.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtTituloRegistrarAvance.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbProyectosFactibles.setModel(new ListModelList<Teg>(teg2));

	}

	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbProyectosFactibles")
	public void mostrarDatosCatalogo() {
		if (ltbProyectosFactibles.getItemCount() != 0) {
			Listitem listItem = ltbProyectosFactibles.getSelectedItem();
			if (listItem != null) {
				Teg tegDatosCatalogo = (Teg) listItem.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", tegDatosCatalogo.getId());
				String vista = "transacciones/VRegistrarAvance";
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("tegCatalogo", map);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VRegistrarAvance.zul", null,
						null);
				window.doModal();
				vistaRegistrarAvance
						.recibir("catalogos/VCatalogoRegistrarAvance");
			}
		}
	}
}
