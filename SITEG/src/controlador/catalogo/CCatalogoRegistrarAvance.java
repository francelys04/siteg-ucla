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
	private static String estatus1 ="Proyecto Factible";
	 private static String estatus2 ="Proyecto en Desarrollo";
	


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
		
		List<Teg> tegs = servicioTeg.buscarTegPorProfesor(estatus1, estatus2, ObtenerUsuarioProfesor());
		System.out.println(tegs.size());
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
	
	/*
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar la fecha, el nombre y apellido del estudiante, la fecha, la
	 * tematica, el area y el titulo de estos.
	 */
	@Listen("onChange = #txtEstudianteAvance, #txtFechaRegistrarAvance, #txtTematicaRegistrarAvance,#txtAreaRegistrarAvance,#txtTituloRegistrarAvance")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = servicioTeg.buscarTegPorProfesor(estatus1, estatus2, ObtenerUsuarioProfesor());
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
