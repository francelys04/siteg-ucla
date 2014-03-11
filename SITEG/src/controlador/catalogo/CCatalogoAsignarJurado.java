package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Programa;
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

import controlador.CAsignarJurado;
import controlador.CGeneral;

@Controller
public class CCatalogoAsignarJurado extends CGeneral {

	CAsignarJurado vista = new CAsignarJurado();
	private List<Teg> tegsDefensa1 = new ArrayList<Teg>();
	private List<Teg> tegsDefensa = new ArrayList<Teg>();

	@Wire
	private Textbox txtEstudianteJurado;
	@Wire
	private Textbox txtFechaSolicitudDefensa;
	@Wire
	private Textbox txtAreaSolicitudDefensa;
	@Wire
	private Textbox txtTematicaSolicitudDefensa;
	@Wire
	private Textbox txtTituloSolicitudDefensa;
	@Wire
	private Textbox txtNombreTutorSolicitudDefensa;
	@Wire
	private Textbox txtApellidoTutorSolicitudDefensa;
	@Wire
	private Listbox ltbSolicitudesDefensa;
	@Wire
	private Window wdwCatalogoAsignarJurado;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los tegs
	 * disponibles dado el programa segun sea el profesor loggeado,
	 * recorriendolo uno a uno para luego cargar una lista de estudiantes por
	 * teg donde mediante la implementacion del servicio de busqueda se va
	 * obteniendo su nombre y su apellido y se va seteando temporalmente en la
	 * variable estatus del teg para poder visualizarlo en el componente lista
	 * de teg de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Programa programa =servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
		if(programa!=null){
		tegsDefensa1 = servicioTeg
				.buscarTegPorProgramaParaDefensa(programa);
		if (!tegsDefensa1.isEmpty()) {

			tegsDefensa.add(tegsDefensa1.get(0));
			for (int i = 1; i < tegsDefensa1.size(); i++) {
				long temp = tegsDefensa1.get(i - 1).getId();
				if (temp != tegsDefensa1.get(i).getId()) {
					tegsDefensa.add(tegsDefensa1.get(i));
				}

			}
			for (int i = 0; i < tegsDefensa.size(); i++) {
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantePorTeg(tegsDefensa.get(i));
				String nombre = estudiantes.get(0).getNombre();
				String apellido = estudiantes.get(0).getApellido();
				tegsDefensa.get(i).setEstatus(nombre + " " + apellido);
			}
			ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegsDefensa));
		}
		}
	}

	/*
	 * Metodo que permite filtrar los tegs disponibles, mediante el componente
	 * de la lista, donde se podra visualizar la fecha, el nombre y apellido del
	 * estudiante, la tematica, el area, el titulo y el nombre y apellido del
	 * tutor.
	 */
	@Listen("onChange = #txtEstudianteJurado, #txtFechaSolicitudDefensa, #txtAreaSolicitudDefensa,#txtTematicaSolicitudDefensa,#txtTituloSolicitudDefensa,#txtNombreTutorSolicitudDefensa,#txtApellidoTutorSolicitudDefensa")
	public void filtrarCatalogo() {

		List<Teg> tegs = new ArrayList<Teg>();
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
			tegs.get(i).setEstatus(nombre + " " + apellido);
		}
		for (Teg teg : tegsDefensa) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg).get(0)
					.getNombre().toLowerCase()
					.contains(txtEstudianteJurado.getValue().toLowerCase())
					
					&& teg.getFecha()
							.toString()
							.toLowerCase()
							.contains(
									txtFechaSolicitudDefensa.getValue()
											.toLowerCase())
					
					
					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtAreaSolicitudDefensa.getValue()
											.toLowerCase())
					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaSolicitudDefensa.getValue()
											.toLowerCase())
					&& teg.getTitulo().toLowerCase()
							.contains(txtTituloSolicitudDefensa.getValue())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(txtNombreTutorSolicitudDefensa.getValue())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoTutorSolicitudDefensa.getValue())) {
				tegs.add(teg);
			}
		}

		ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegs));
	}

	/*
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbSolicitudesDefensa")
	public void seleccionarLista() {
		if (ltbSolicitudesDefensa.getItemCount() != 0) {
			Listitem listItem = ltbSolicitudesDefensa.getSelectedItem();
			if (listItem != null) {
				Teg tegSeleccionado = (Teg) listItem.getValue();
				long id = tegSeleccionado.getId();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				Sessions.getCurrent().setAttribute("catalogoSolicitudDefensa",
						map);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VAsignarJurado.zul", null, null);
				window.doModal();
				vista.recibir("catalogos/VCatalogoAsignarJurado");
			}
		}
	}
}
