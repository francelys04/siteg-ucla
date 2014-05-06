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
	private static String estatus1 = "TEG Registrado";
	private static String estatus2 = "Trabajo en Desarrollo";

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

		
		List<Teg> teg = buscar();
		for (int i = 0; i < teg.size(); i++) {
			List<Estudiante> estudiante = servicioEstudiante
					.buscarEstudiantePorTeg(teg.get(i));
			String nombre = estudiante.get(0).getNombre();
			String apellido = estudiante.get(0).getApellido();
			teg.get(i).setEstatus(nombre + " " + apellido);
		}

		ltbTrabajosRegistrados.setModel(new ListModelList<Teg>(teg));
		
	}
	
	/*
	 * Metodo que permite retornar una lista de tegs dado a un profesor, donde
	 * el estatus sea "Trabajo Registrado o Trabajo en Desarrollo"
	 */
	public List<Teg> buscar() {
		List<Teg> tegProfesor = servicioTeg.
				buscarTutoriaProfesor(ObtenerUsuarioProfesor());

		List<Teg> tegs = new ArrayList<Teg>();
		for (int i = 0; i < tegProfesor.size(); i++) {
		
			if (tegProfesor.get(i).getEstatus().equals(estatus1) || tegProfesor.get(i).getEstatus().equals(estatus2)) {

				tegs.add(tegProfesor.get(i));
			}
		}
		return tegs;
	}
	
	

	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante, la fecha, la tematica, el
	 * area y el titulo, el nombre y apellido del tutor de estos.
	 */
	@Listen("onChange = #txtEstudianteRevision, #txtFechaRegistrarRevision, #txtTematicaRegistrarRevision,#txtAreaRegistrarRevision,#txtTituloRegistrarRevision")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = servicioTeg.buscarTegPorProfesor(estatus1, estatus2,
				ObtenerUsuarioProfesor());
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
