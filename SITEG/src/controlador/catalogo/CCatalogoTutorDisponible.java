package controlador.catalogo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Actividad;
import modelo.Categoria;
import modelo.Lapso;
import modelo.Profesor;
import modelo.AreaInvestigacion;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.compuesta.CondicionPrograma;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

import servicio.SAreaInvestigacion;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SAreaInvestigacion;
import servicio.SSolicitudTutoria;

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
