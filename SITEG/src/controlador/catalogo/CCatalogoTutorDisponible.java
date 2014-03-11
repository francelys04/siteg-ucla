package controlador.catalogo;

import java.util.ArrayList;
import java.util.List;

import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
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

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Programa> programa = servicioPrograma.buscarActivas();
		cmbProgramaTutores.setModel(new ListModelList<Programa>(programa));
	}

	/*
	 * Metodo que permite retornar una lista de profesores, donde se va
	 * recorriendo los profesores cuyas cedulas coincidan con las que se
	 * encuentran en las solicitudes de tutorias generedas, para asi cargar una
	 * lista de profesores
	 */
	@Listen("onSelect = #cmbProgramaTutores")
	public List<Profesor> llenarLista() {

		List<Profesor> profesores = servicioProfesor.buscarActivos();
		String variable = "Numero de tutorias por profesor";
		idPrograma = Long.parseLong(cmbProgramaTutores.getSelectedItem()
				.getId());
		Programa progra = servicioPrograma.buscarPorId(idPrograma);
		CondicionPrograma condicion = buscarCondicionVigenteEspecifica(
				variable, progra);
		List<SolicitudTutoria> solicitudTutoria = servicioSolicitudTutoria
				.buscarAceptadas();
		int valor = condicion.getValor();

		List<Profesor> tutorDisponible = new ArrayList<Profesor>();
		for (int i = 0; i < profesores.size(); i++) {
			Profesor profesorSolicitud = profesores.get(i);
			int contar = 0;

			for (int j = 0; j < solicitudTutoria.size(); j++) {

				if (profesorSolicitud.getCedula().compareTo(
						solicitudTutoria.get(j).getProfesor().getCedula()) == 0) {

					++contar;

				}
			}
			if (contar < valor) {
				tutorDisponible.add(profesorSolicitud);
			}

		}

		ltbTutores.setModel(new ListModelList<Profesor>(tutorDisponible));
		return tutorDisponible;

	}

	/*
	 * Metodo que permite filtrar los profesores disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * apellido de estos.
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
