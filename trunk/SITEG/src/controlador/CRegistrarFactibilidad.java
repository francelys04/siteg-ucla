package controlador;

import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.ItemFactibilidad;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SEstudiante;
import servicio.SFactibilidad;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.STeg;
import servicio.SItemFactibilidad;

import configuracion.GeneradorBeans;
import controlador.CCatalogoRegistrarFactibilidad;
import controlador.CGeneral;

public class CRegistrarFactibilidad extends CGeneral {
	// CCatalogoFactibilidad catalogo = new CCatalogoFactibilidad();
	SFactibilidad servicioFactibilidad = GeneradorBeans
			.getServicioFactibilidad();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SItemFactibilidad servicioItemFactibilidad = GeneradorBeans
			.getServicioItemFactibilidad();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();

	@Wire
	private Listbox ltbListaFactibilidad;
	@Wire
	private Listbox ltbItemsFactibilidad;
	@Wire
	private Textbox txtProgramaRegistrarFactibilidad;
	@Wire
	private Textbox txtAreaRegistrarFactibilidad;
	@Wire
	private Textbox txtTematicaRegistrarFactibilidad;
	@Wire
	private Textbox txtTituloRegistrarFactibilidad;
	@Wire
	private Textbox txtCedulaTutorRegistrarFactibilidad;
	@Wire
	private Textbox txtNombreTutorRegistrarFactibilidad;
	@Wire
	private Textbox txtApellidoTutorRegistrarFactiblidad;
	@Wire
	private Textbox txtCedulaComisionRegistrarFactibilidad;
	@Wire
	private Textbox txtNombreComisionRegistrarFactibilidad;
	@Wire
	private Textbox txtApellidoComisionRegistrarFactibilidad;
	@Wire
	private Textbox txtObservacionRegistrarFactibilidad;
	@Wire
	private Listbox ltbEstudianteRegistrarFactibilidad;
	@Wire
	private Listbox ltbComisiónRegistrarFactibilidad;
	@Wire
	private Window wdwRegistrarFactibilidad;
	@Wire
	private Window wdwCatalogoRegistrarFactibilidad;
	private static String vistaRecibida;
	private long id = 0;
	private static long auxiliarId = 0;
	private static long auxIdPrograma = 0;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Programa programa = servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor());

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				txtProgramaRegistrarFactibilidad.setValue(programa.getNombre());
				txtAreaRegistrarFactibilidad.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaRegistrarFactibilidad.setValue(teg2.getTematica().getNombre());
				txtCedulaTutorRegistrarFactibilidad.setValue(teg2.getTutor().getCedula());
				txtNombreTutorRegistrarFactibilidad.setValue(teg2.getTutor().getNombre());
				txtApellidoTutorRegistrarFactiblidad
						.setValue(teg2.getTutor().getApellido());
				txtTituloRegistrarFactibilidad.setValue(teg2.getTitulo());
				
				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				Factibilidad factibilidadPorTeg = servicioFactibilidad.buscarFactibilidadPorTeg(tegPorCodigo);
				
				txtCedulaComisionRegistrarFactibilidad.setValue(factibilidadPorTeg.getProfesor().getCedula());
				txtNombreComisionRegistrarFactibilidad.setValue(factibilidadPorTeg.getProfesor().getNombre());
				txtApellidoComisionRegistrarFactibilidad.setValue(factibilidadPorTeg.getProfesor().getApellido());
				txtObservacionRegistrarFactibilidad.setValue(factibilidadPorTeg.getObservacion());
				
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegPorCodigo);
				ltbEstudianteRegistrarFactibilidad.setModel(new ListModelList<Estudiante>(
						estudiantes));
				id = teg2.getId();

				map.clear();
				map = null;
			}
		}

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwRegistrarFactibilidad.onClose();
	}



	
}
