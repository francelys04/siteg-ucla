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
import controlador.CGeneral;
import controlador.CRegistrarFactibilidad;

@Controller
public class CCatalogoRegistrarFactibilidad extends CGeneral {

	CRegistrarFactibilidad vistaFactibilidad = new CRegistrarFactibilidad();
	@Wire
	private Textbox txtEstudianteRegistrarFactibilidad;
	@Wire
	private Listbox ltbListaFactibilidad;
	@Wire
	private Window wdwCatalogoRegistrarFactibilidad;
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
		ltbListaFactibilidad.setModel(new ListModelList<Teg>(tegs));
	}

	/*
	 * Metodo que permite retornar un lista de los tegs, donde se recorre tanto
	 * la lista del teg como los profesores activos, donde se compara si coincide las
	 * cedulas de cada uno de los profesores para cargar la lista de tegs.
	 */
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Teg> tegs = servicioTeg.buscarProyectoFactibilidadEvaluada();

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

		ltbListaFactibilidad.setModel(new ListModelList<Teg>(tegs1));
		return tegs1;
	}
	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante, la fecha, la
	 * tematica, el area y el titulo, el nombre y apellido del tutor de estos.
	 */
	@Listen("onChange = #txtMostrarFechaFactibilidad, #txtMostrarTematicaFactibilidad,#txtMostrarAreaFactibilidad,#txtMostrarTituloFactibilidad,#txtMostrarNombreTutorFactibilidad,# txtMostrarApellidoTutorFactibilidad")
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
			if (servicioEstudiante
					.buscarEstudiantePorTeg(teg)
					.get(0)
					.getNombre()
					.toLowerCase()
					.contains(
							txtEstudianteRegistrarFactibilidad.getValue()
									.toLowerCase())
					&& teg.getFecha()
							.toString()
							.toLowerCase()
							.contains(
									txtMostrarFechaFactibilidad.getValue()
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

	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbListaFactibilidad")
	public void mostrarDatosCatalogo() {
		if (ltbListaFactibilidad.getItemCount() != 0) {
			Listitem listItem = ltbListaFactibilidad.getSelectedItem();
			if (listItem != null) {
				Teg tegDatosCatalogo = (Teg) listItem.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", tegDatosCatalogo.getId());
				String vista = "transacciones/VRegistrarFactibilidad";
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("tegCatalogo", map);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VRegistrarFactibilidad.zul",
						null, null);
				window.doModal();
				vistaFactibilidad
						.recibir("catalogos/VCatalogoRegistrarFactibilidad");
			}
		}
	}

}
