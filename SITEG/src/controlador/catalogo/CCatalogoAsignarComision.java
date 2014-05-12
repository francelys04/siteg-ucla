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

import controlador.CAsignarComision;
import controlador.CGeneral;

/**
 * Controlador asociado a la vista catalogo asignar comision  que permite mostrar los
 * trabajos especiales de grado con el estatus "Proyecto Registrado" a
 * traves de un listado
 */
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
		Programa programa = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
		if (programa != null) {
			List<Teg> tegs = servicioTeg
					.buscarTegPorProgramaParaAsignarComision(programa);
			List<Teg> tegs2 = new ArrayList<Teg>();
			long id2 = 0;
			long id = 0;

			for (int i = 0; i < tegs.size(); i++) {
				id = tegs.get(i).getId();
				if (id != id2) {
					List<Estudiante> estudiantes = servicioEstudiante
							.buscarEstudiantePorTeg(tegs.get(i));
					String nombre = estudiantes.get(0).getNombre();
					String apellido = estudiantes.get(0).getApellido();
					tegs.get(i).setEstatus(nombre + " " + apellido);
					tegs2.add(tegs.get(i));

					id2 = id;
				}

			}
			ltbProyectosRegistrados.setModel(new ListModelList<Teg>(tegs2));
		}
	}

	/**
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscarDatos()", mediante el componente de la lista, donde se podra
	 * visualizar el nombre y apellido del estudiante, la tematica, el area, el
	 * titulo y el nombre y apellido del tutor.
	 */
	@Listen("onChange = #txtEstudianteComision, #txtMostrarFecha, #txtMostrarTematica,#txtMostrarArea,#txtMostrarTitulo,#txtMostrarNombreTutor,#txtMostrarApellidoTutor")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = servicioTeg
				.buscarTegPorProgramaParaAsignarComision(servicioPrograma
						.buscarProgramaDeDirector(ObtenerUsuarioProfesor()));
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

	/**
	 * Metodo que permite obtener el objeto Teg al realizar el evento doble clic
	 * sobre un item en especifico en la lista, extrayendo asi su id, para luego
	 * poder ser mapeada y enviada a la vista "VAsignarComision".
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
