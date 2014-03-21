package controlador.catalogo;

import java.util.ArrayList;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Tematica;
import modelo.compuesta.CondicionPrograma;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import controlador.CGeneral;

@Controller
public class CCatalogoTutorDisponible extends CGeneral {

	private static long idPrograma;
	private static long idTematica = 0;
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();

	@Wire
	private Combobox cmbProgramaTutores;
	@Wire
	private Listbox ltbTutores;
	@Wire
	private Textbox txtCedulaTutor;
	@Wire
	private Textbox txtNombreTutor;
	@Wire
	private Textbox txtApellidoTutor;
	@Wire
	private Textbox txtCorreoTutor;
	@Wire
	private Combobox cmbAreaTutores;
	@Wire
	private Combobox cmbTematicaTutores;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		programas = servicioPrograma.buscarActivas();
		cmbProgramaTutores.setModel(new ListModelList<Programa>(programas));
		cmbAreaTutores.setDisabled(true);
		cmbTematicaTutores.setDisabled(true);

	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado
	 */
	@Listen("onSelect = #cmbProgramaTutores")
	public void seleccinarPrograma() {
		try {

			ltbTutores.setModel(new ListModelList<Profesor>());
			cmbAreaTutores.setDisabled(false);
			cmbAreaTutores.setValue("");
			cmbTematicaTutores.setValue("");
			cmbTematicaTutores.setDisabled(true);
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long.parseLong(cmbProgramaTutores.getSelectedItem()
							.getId())));
			cmbAreaTutores
					.setModel(new ListModelList<AreaInvestigacion>(areas));

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
	}

	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado
	 */
	@Listen("onSelect = #cmbAreaTutores")
	public void seleccionarArea() {

		ltbTutores.setModel(new ListModelList<Profesor>());
		cmbTematicaTutores.setDisabled(false);
		cmbTematicaTutores.setValue("");
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(Long.parseLong(cmbAreaTutores.getSelectedItem()
						.getId())));

		cmbTematicaTutores.setModel(new ListModelList<Tematica>(tematicas));

	}

	/*
	 * Metodo que permite cargar la lista de tutores disponibles
	 */
	@Listen("onSelect = #cmbTematicaTutores")
	public void seleccionarTematica() {
		
		llenarLista();

	}

	/*
	 * Metodo que permite retornar una lista de profesores, donde se va
	 * recorriendo los profesores cuyas cedulas coincidan con las que se
	 * encuentran en las solicitudes de tutorias generedas, para asi cargar una
	 * lista de tutores disponibles
	 */
	public List<Profesor> llenarLista() {

		idTematica = Long.parseLong(cmbTematicaTutores.getSelectedItem()
				.getId());
		idPrograma = Long.parseLong(cmbProgramaTutores.getSelectedItem()
				.getId());

		Tematica tema = servicioTematica.buscarTematica(idTematica);
		List<Profesor> profesores = servicioProfesor
				.buscarProfesoresPorTematica(tema);
		String variable = "Numero de tutorias por profesor";
		Programa progra = servicioPrograma.buscarPorId(idPrograma);
		CondicionPrograma cm = buscarCondicionVigenteEspecifica(variable,
				progra);
		List<SolicitudTutoria> st = servicioSolicitudTutoria.buscarAceptadas();
		int valor = cm.getValor();

		List<Profesor> profe = new ArrayList<Profesor>();
		for (int i = 0; i < profesores.size(); i++) {
			Profesor pf = profesores.get(i);
			int contar = 0;

			for (int j = 0; j < st.size(); j++) {

				if (pf.getCedula().compareTo(
						st.get(j).getProfesor().getCedula()) == 0) {

					++contar;

				}
			}
			if (contar < valor) {
				profe.add(pf);
			}

		}

		ltbTutores.setModel(new ListModelList<Profesor>(profe));
		return profe;

	}

	/*
	 * Metodo que permite filtrar los profesores disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la apellido
	 * de estos.
	 */
	@Listen("onChange = #txtCedulaTutor,#txtNombreTutor,#txtApellidoTutor,#txtCorreoTutor")
	public void filtrarDatosCatalogo() {

		List<Profesor> profesor1 = llenarLista();
		List<Profesor> profesor2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesor1) {
			if (profesor.getCedula().toLowerCase()
					.contains(txtCedulaTutor.getValue().toLowerCase())
					&& profesor.getNombre().toLowerCase()
							.contains(txtNombreTutor.getValue().toLowerCase())
					&& profesor
							.getApellido()
							.toLowerCase()
							.contains(txtApellidoTutor.getValue().toLowerCase())
					&& profesor.getCorreoElectronico().toLowerCase()
							.contains(txtCorreoTutor.getValue().toLowerCase())) {
				profesor2.add(profesor);
			}
		}

		ltbTutores.setModel(new ListModelList<Profesor>(profesor2));

	}

}
