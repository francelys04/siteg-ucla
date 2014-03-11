package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Teg;
import modelo.compuesta.Jurado;

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

import controlador.CCalificarDefensa;
import controlador.CGeneral;

@Controller
public class CCatalogoCalificarDefensa extends CGeneral {

	CCalificarDefensa ventanarecibida = new CCalificarDefensa();

	@Wire
	private Listbox ltbCalificarDefensa;
	@Wire
	private Textbox txtEstudianteCalificarDefensa;
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

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los tegs
	 * disponibles mediante el metodo "buscar()", recorriendolo uno a uno
	 * para luego cargar una lista de estudiantes por teg donde mediante la
	 * implementacion del servicio de busqueda se va obteniendo su nombre y su
	 * apellido y se va seteando temporalmente en la variable estatus del teg
	 * para poder visualizarlo en el componente lista de teg de la vista.
	 */
	@Override
	public void inicializar(Component comp) {

		List<Teg> teg = buscar();
		for (int i = 0; i < teg.size(); i++) {
			List<Estudiante> estudiante = servicioEstudiante
					.buscarEstudiantePorTeg(teg.get(i));
			String nombre = estudiante.get(0).getNombre();
			String apellido = estudiante.get(0).getApellido();
			teg.get(i).setEstatus(nombre + " " + apellido);
		}

		ltbCalificarDefensa.setModel(new ListModelList<Teg>(teg));
	}

	/*
	 * Metodo que permite retornar una lista de tegs dado a un profesor, donde
	 * el estatus sea "Defensa Asignada"
	 */
	public List<Teg> buscar() {
		List<Jurado> jurado = servicioJurado
				.buscarTegDeProfesor(ObtenerUsuarioProfesor());

		List<Teg> tegs = new ArrayList<Teg>();
		for (int i = 0; i < jurado.size(); i++) {
			Teg teg = jurado.get(i).getTeg();

			if (teg.getEstatus().equals("Defensa Asignada")) {

				tegs.add(teg);
			}
		}
		return tegs;
	}

	/*
	 * Metodo que permite filtrar los tegs disponibles, donde dado a una lista
	 * de estudiantes que se carga con la implementacion del servicio de
	 * busqueda, se obtiene a su vez el nombre y apellido del estudiante y se
	 * setea en la variable estatus por cada teg,, donde se podra visualizar
	 * mediante el componente lista de la vista el nombre y apellido del
	 * estudiante, la fecha, la tematica, el area, el titulo y el nombre y
	 * apellido del tutor.
	 */
	@Listen("onChange = #txtEstudianteCalificarDefensa, #txtMostrarFechaCalificar,#txtMostrarTematicaCalificar,#txtMostrarAreaCalificar,#txtMostrarTituloCalificar,#txtMostrarNombreTutorCalificar,#txtMostrarApellidoTutorCalificar")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscar();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante
					.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
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
							txtEstudianteCalificarDefensa.getValue()
									.toLowerCase())
					&& teg.getFecha()
							.toString()
							.toLowerCase()
							.contains(
									txtMostrarFechaCalificar.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematicaCalificar.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarAreaCalificar.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTituloCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutorCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutorCalificar.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbCalificarDefensa.setModel(new ListModelList<Teg>(teg2));

	}

	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeado dado a su id y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbCalificarDefensa")
	public void mostrarDatosCatalogo() {
		if (ltbCalificarDefensa.getItemCount() != 0) {
			Listitem listItem = ltbCalificarDefensa.getSelectedItem();
			if (listItem != null) {
				Teg tegDatosCatalogo = (Teg) listItem.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", tegDatosCatalogo.getId());
				String vista = "transacciones/VCalificarDefensa";
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("tegCatalogo", map);

				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VCalificarDefensa.zul", null,
						null);
				window.doModal();
				ventanarecibida.recibir("catalogos/VCatalogoCalificarDefensa");
			}
		}
	}
}
