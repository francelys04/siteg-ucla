package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Requisito;
import modelo.Teg;
import modelo.Profesor;
import modelo.Usuario;
import modelo.Avance;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.STeg;
import servicio.SAvance;
import servicio.SEstudiante;
import servicio.STematica;
import servicio.SAreaInvestigacion;
import servicio.SProfesor;

import configuracion.GeneradorBeans;

@Controller
public class CEvaluarAvances extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	SAvance servicioAvance = GeneradorBeans.getServicioAvance(); // En esta
																	// linea me
																	// da el
																	// error..
																	// del Beans

	// Estas variables las inicialice para que se me pudieran mostrar los datos
	// en el formulario
	// se deben es pasar por parametro para validar
	private long id = 1;
	private String cedula = "1";

	@Wire
	private Window wdwEvaluarAvances;
	@Wire
	private Textbox txtProgramaEvaluarAvances;
	@Wire
	private Textbox txtAreaEvaluarAvances;
	@Wire
	private Textbox txtTematicaEvaluarAvances;
	@Wire
	private Textbox txtTituloEvaluarAvances;
	@Wire
	private Datebox dbEvaluarAvances;
	@Wire
	private Listbox ltbEvaluarAvance;
	@Wire
	private Button btnCerrarEvaluacionAvances;
	@Wire
	private Listbox ltbEstudiantesProyecto;

	@Override
	void inicializar(Component comp) {

		List<Teg> teg = servicioTeg.buscarProyectoFactible();
		List<Avance> avance = servicioAvance.buscarActivos();
        List<Estudiante> estudiantes = servicioEstudiante.buscarEstudiantes();
		
		ltbEstudiantesProyecto.setModel(new ListModelList<Estudiante>(estudiantes));
		ltbEvaluarAvance.setModel(new ListModelList<Avance>(avance));

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Teg tegs = servicioTeg.buscarTeg(id);
				Profesor prof = servicioProfesor
						.buscarProfesorPorCedula(cedula);
				Avance avances = servicioAvance.buscarAvancePorId(id);

				// OJOOOOOOOOO id y cedula estan inicializados al principio
				// Se debe arreglar.. el id del TEG y la Cedula del Profesro de
				// deben pasar por parametro
				// o se deben sacar directamente del item que se selecciona del
				// catalogoEvaluarAvances
				txtProgramaEvaluarAvances.setValue(prof.getPrograma()
						.getNombre());
				txtAreaEvaluarAvances.setValue(tegs.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaEvaluarAvances.setValue(tegs.getTematica()
						.getNombre());
				txtTituloEvaluarAvances.setValue(tegs.getTitulo());

				id = tegs.getId();
				map.clear();
				map = null;
			}
		}
	}

}
