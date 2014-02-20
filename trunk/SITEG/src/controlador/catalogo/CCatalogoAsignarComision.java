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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.STeg;
import servicio.SProfesor;
import servicio.seguridad.SUsuario;
import configuracion.GeneradorBeans;
import controlador.CAsignarComision;
import controlador.CGeneral;

@Controller
public class CCatalogoAsignarComision extends CGeneral {

	CAsignarComision vistaComision = new CAsignarComision();

	@Wire
	private Textbox txtEstudianteComision;
	@Wire
	private Listbox ltbProyectosRegistrados;
	@Wire
	private Window wdwCatalogoAsignarComision;
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
		ltbProyectosRegistrados.setModel(new ListModelList<Teg>(tegs));

	}

	/*
	 * Metodo que permite cargar una lista de teg dado una condicion booleana
	 * donde sera igual a "true" cuando la cedula del profesor por cada teg se
	 * encuentre al recorrer todos los profesores activos y realizar dicha
	 * comparacion.
	 */
	public List<Teg> buscarDatos() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		List<Teg> tegs = servicioTeg.BuscarProyectoRegistrado();

		Profesor profesor1 = new Profesor();
		List<Teg> tegs1 = new ArrayList<Teg>();

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
				tegs1.add(tegs.get(i));

			}

		}

		ltbProyectosRegistrados.setModel(new ListModelList<Teg>(tegs1));
		return tegs1;
	}

	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante, la tematica, el area, el
	 * titulo y el nombre y apellido del tutor.
	 */
	@Listen("onChange = #txtEstudianteComision, #txtMostrarFecha, #txtMostrarTematica,#txtMostrarArea,#txtMostrarTitulo,#txtMostrarNombreTutor,#txtMostrarApellidoTutor")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscarDatos();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante
					.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			teg1.get(i).setEstatus(nombre + " " + apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (teg.getFecha().toString().toLowerCase()
					.contains(txtMostrarFecha.getValue().toLowerCase())
					&& servicioEstudiante
							.buscarEstudiantePorTeg(teg)
							.get(0)
							.getNombre()
							.toLowerCase()
							.contains(
									txtEstudianteComision.getValue()
											.toLowerCase())
					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematica.getValue().toLowerCase())

					&& teg.getTematica().getareaInvestigacion().getNombre()
							.toLowerCase()
							.contains(txtMostrarArea.getValue().toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(txtMostrarTitulo.getValue().toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutor.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutor.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbProyectosRegistrados.setModel(new ListModelList<Teg>(teg2));

	}
	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista "VAsignarComision".
	 */
	@Listen("onDoubleClick = #ltbProyectosRegistrados")
	public void mostrarDatosCatalogo() {
		if (ltbProyectosRegistrados.getItemCount() != 0) {
			Listitem listItem = ltbProyectosRegistrados.getSelectedItem();
			if (listItem != null) {
				Teg tegDatosCatalogo = (Teg) listItem.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", tegDatosCatalogo.getId());
				String vista = "transacciones/VAsignarComision";
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("tegCatalogo", map);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VAsignarComision.zul", null,
						null);
				window.doModal();
				vistaComision.recibir("catalogos/VCatalogoAsignarComision");
			}
		}
	}
}
