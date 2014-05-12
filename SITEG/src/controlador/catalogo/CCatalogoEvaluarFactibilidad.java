package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CEvaluarFactibilidad;
import controlador.CGeneral;

/**
 * Controlador asociado a la vista catalogo evaluar factibilidad que permite
 * mostrar los trabajos especiales de grado con el estatus "Comision Asignada" a
 * traves de un listado
 */
@Controller
public class CCatalogoEvaluarFactibilidad extends CGeneral {

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

	/**
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

	/**
	 * Metodo que permite obtener una lista de los tegs, de acuerdo al programa
	 * del profesor que se encuentra loggeado y a su vez verificando que su
	 * estatus sea "Comision Asignada"
	 */
	public List<Teg> buscarDatos() {

		List<Teg> tegs = servicioTeg
				.buscarTegDeComision(ObtenerUsuarioProfesor());

		List<Teg> tegComisionAsignada = new ArrayList<Teg>();
		for (int i = 0; i < tegs.size(); i++) {

			if (tegs.get(i).getEstatus().equals("Comision Asignada")) {

				tegComisionAsignada.add(tegs.get(i));
			}
		}
		return tegComisionAsignada;

	}

	/**
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar la fecha, el nombre y apellido del estudiante, la tematica, el
	 * area, el titulo y el nombre y apellido del tutor.
	 */
	@Listen("onChange = #txtEstudianteFactibilidad, #txtMostrarFechaFactibilidad, #txtMostrarTematicaFactibilidad,#txtMostrarAreaFactibilidad,#txtMostrarTituloFactibilidad,#txtMostrarNombreTutorFactibilidad,#txtMostrarApellidoTutorFactibilidad")
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
			if (teg.getFecha()
					.toString()
					.toLowerCase()
					.contains(
							txtMostrarFechaFactibilidad.getValue()
									.toLowerCase())
					&& servicioEstudiante
							.buscarEstudiantePorTeg(teg)
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

	/**
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
				String vista = "transacciones/VEvaluarFactibilidad";
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("tegCatalogo", map);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VEvaluarFactibilidad.zul", null,
						null);
				window.doModal();
				vistaFactibilidad
						.recibir("catalogos/VCatalogoEvaluarFactibilidad");
			}
		}
	}

}