package controlador;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Tematica;
import modelo.TipoJurado;
import modelo.compuesta.Jurado;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/**
 * Controlador asociado a la vista restaurar datos que permite restaurar los
 * datos basicos inactivos del sistema
 */
@Controller
public class CRestaurarDatos extends CGeneral {

	@Wire
	private Listbox ltbActividad;
	@Wire
	private Textbox txtNombreMostrarActividad;
	@Wire
	private Textbox txtDescripcionMostrarActividad;
	@Wire
	private Window wdwCatalogoActividad;
	@Wire
	private Listbox ltbPrograma;
	@Wire
	private Window wdwCatalogoPrograma;
	@Wire
	private Textbox txtNombreMostrarPrograma;
	@Wire
	private Textbox txtDescripcionMostrarPrograma;
	@Wire
	private Listbox ltbArea;
	@Wire
	private Textbox txtNombreMostrarArea;
	@Wire
	private Textbox txtDescripcionMostrarArea;
	@Wire
	private Listbox ltbTematica;
	@Wire
	private Textbox txtNombreMostrarTematica;
	@Wire
	private Textbox txtAreaMostrarTematica;
	@Wire
	private Textbox txtDescripcionMostrarTematica;
	@Wire
	private Listbox ltbRequisito;
	@Wire
	private Textbox txtNombreMostrarRequisito;
	@Wire
	private Textbox txtDescripcionMostrarRequisito;
	@Wire
	private Textbox txtNombreMostrarItem;
	@Wire
	private Textbox txtTipoMostrarItem;
	@Wire
	private Textbox txtDescripcionMostrarItem;
	@Wire
	private Listbox ltbItem;
	@Wire
	private Listbox ltbProfesor;
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;
	@Wire
	private Listbox ltbEstudiante;
	@Wire
	private Textbox txtCedulaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarEstudiante;
	@Wire
	private Textbox txtApellidoMostrarEstudiante;
	@Wire
	private Textbox txtCorreoMostrarEstudiante;
	@Wire
	private Textbox txtProgramaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarTipoJurado;
	@Wire
	private Textbox txtDescripcionMostrarTipoJurado;
	@Wire
	private Listbox ltbTipoJurado;
	@Wire
	private Listbox ltbLapso;
	@Wire
	private Textbox txtNombreMostrarLapso;
	@Wire
	private Textbox txtFechaInicialMostrarLapso;
	@Wire
	private Textbox txtFechaFinalMostrarLapso;
	@Wire
	private Window wdwRestauracionDatos;

	/**
	 * Metodo heredado del Controlador CGeneral donde se se buscan todas las
	 * actividades disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		llenarListas();

	}

	/** Metodo que permite llenar todos los listados */
	public void llenarListas() {

		// Lista de programas inactivos
		List<Programa> programas = servicioPrograma.buscarInactivos();
		ltbPrograma.setModel(new ListModelList<Programa>(programas));
		ltbPrograma.setMultiple(false);
		ltbPrograma.setCheckmark(false);
		ltbPrograma.setMultiple(true);
		ltbPrograma.setCheckmark(true);

		// Lista de areas de investigacion
		List<AreaInvestigacion> area = servicioArea.buscarInactivos();
		ltbArea.setModel(new ListModelList<AreaInvestigacion>(area));
		ltbArea.setMultiple(false);
		ltbArea.setCheckmark(false);
		ltbArea.setMultiple(true);
		ltbArea.setCheckmark(true);

		// Lista de tematicas inactivas
		List<Tematica> tematica = servicioTematica.buscarInactivos();
		ltbTematica.setModel(new ListModelList<Tematica>(tematica));
		ltbTematica.setMultiple(false);
		ltbTematica.setCheckmark(false);
		ltbTematica.setMultiple(true);
		ltbTematica.setCheckmark(true);

		// Lista de actividades inactivos
		List<Actividad> actividad = servicioActividad.buscarInactivos();
		ltbActividad.setModel(new ListModelList<Actividad>(actividad));
		ltbActividad.setMultiple(false);
		ltbActividad.setCheckmark(false);
		ltbActividad.setMultiple(true);
		ltbActividad.setCheckmark(true);

		// Lista de items de evaluacion inactivos
		List<ItemEvaluacion> items = servicioItem.buscarItemsInactivos();
		ltbItem.setModel(new ListModelList<ItemEvaluacion>(items));
		ltbItem.setMultiple(false);
		ltbItem.setCheckmark(false);
		ltbItem.setMultiple(true);
		ltbItem.setCheckmark(true);

		// Lista de requisitos inactivos
		List<Requisito> requisito = servicioRequisito.buscarInactivos();
		ltbRequisito.setModel(new ListModelList<Requisito>(requisito));
		ltbRequisito.setMultiple(false);
		ltbRequisito.setCheckmark(false);
		ltbRequisito.setMultiple(true);
		ltbRequisito.setCheckmark(true);

		// Lista de profesores inactivos
		List<Profesor> profesores = servicioProfesor.buscarInactivos();
		ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
		ltbProfesor.setMultiple(false);
		ltbProfesor.setCheckmark(false);
		ltbProfesor.setMultiple(true);
		ltbProfesor.setCheckmark(true);

		// Lista de estudiantes inactivos
		List<Estudiante> estudiantes = servicioEstudiante.buscarInactivos();
		ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes));
		ltbEstudiante.setMultiple(false);
		ltbEstudiante.setCheckmark(false);
		ltbEstudiante.setMultiple(true);
		ltbEstudiante.setCheckmark(true);

		// Lista de tipos de jurado inactivos
		List<TipoJurado> tipoJurado = servicioTipoJurado.buscarInactivos();
		ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado));
		ltbTipoJurado.setMultiple(false);
		ltbTipoJurado.setCheckmark(false);
		ltbTipoJurado.setMultiple(true);
		ltbTipoJurado.setCheckmark(true);

		// Lista de lapsos academicos inactivos
		List<Lapso> lapsos = servicioLapso.buscarInactivos();
		ltbLapso.setModel(new ListModelList<Lapso>(lapsos));
		ltbLapso.setMultiple(false);
		ltbLapso.setCheckmark(false);
		ltbLapso.setMultiple(true);
		ltbLapso.setCheckmark(true);

	}

	/**
	 * Metodo que permite filtrar los lapsos disponibles, mediante el componente
	 * de la lista, donde se podra visualizar el nombre de estos.
	 */
	@Listen("onChange = #txtNombreMostrarLapso,  #txtFechaInicialMostrarLapso, #txtFechaFinalMostrarLapso")
	public void filtrarDatosLapso() {
		List<Lapso> lapsos1 = servicioLapso.buscarInactivos();
		List<Lapso> lapsos2 = new ArrayList<Lapso>();

		for (Lapso lapso : lapsos1) {
			if (lapso.getNombre().toLowerCase()
					.contains(txtNombreMostrarLapso.getValue().toLowerCase())

					&& lapso.getFechaInicial()
							.toString()
							.toLowerCase()
							.contains(
									txtFechaInicialMostrarLapso.getValue()
											.toLowerCase())

					&& lapso.getFechaInicial()
							.toString()
							.toLowerCase()
							.contains(
									txtFechaFinalMostrarLapso.getValue()
											.toLowerCase())) {
				lapsos2.add(lapso);
			}

		}

		ltbLapso.setModel(new ListModelList<Lapso>(lapsos2));

	}

	/**
	 * Metodo que permite filtrar los tipos de jurados disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarTipoJurado,#txtDescripcionMostrarTipoJurado")
	public void filtrarDatosTipoJurado() {
		List<TipoJurado> tipoJurado1 = servicioTipoJurado.buscarInactivos();
		List<TipoJurado> tipoJurado2 = new ArrayList<TipoJurado>();

		for (TipoJurado tipoJurado : tipoJurado1) {
			if (tipoJurado
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarTipoJurado.getValue().toLowerCase())
					&& tipoJurado
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTipoJurado.getValue()
											.toLowerCase())) {
				tipoJurado2.add(tipoJurado);
			}
		}

		ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado2));

	}

	/**
	 * Metodo que permite filtrar los estudiantes disponibles, dado a la
	 * condicional de la variable booleana si es igual a "true" se mostraran los
	 * estudiantes sin usuario sino si es "false" seran todos los estudiantes
	 * activos, mediante el componente de la lista, donde se podra visualizar la
	 * cedula, nombre, apellido, correo y programa
	 */
	@Listen("onChange = #txtCedulaMostrarEstudiante,#txtNombreMostrarEstudiante,#txtApellidoMostrarEstudiante,#txtCorreoMostrarEstudiante,#txtProgramaMostrarEstudiante")
	public void filtrarDatosEstudiante() {

		List<Estudiante> estudiantes1 = servicioEstudiante.buscarInactivos();
		List<Estudiante> estudiantes2 = new ArrayList<Estudiante>();

		for (Estudiante estudiante : estudiantes1) {
			if (estudiante
					.getCedula()
					.toLowerCase()
					.contains(
							txtCedulaMostrarEstudiante.getValue().toLowerCase())
					&& estudiante
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getPrograma()
							.getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarEstudiante.getValue()
											.toLowerCase())) {
				estudiantes2.add(estudiante);
			}

		}

		ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes2));

	}

	/**
	 * Metodo que permite filtrar los programas disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estos.
	 */
	@Listen("onChange = #txtNombreMostrarPrograma,#txtDescripcionMostrarPrograma")
	public void filtrarDatosProgramas() {
		List<Programa> programas1 = servicioPrograma.buscarInactivos();
		List<Programa> programas2 = new ArrayList<Programa>();
		for (Programa programa : programas1) {
			if (programa
					.getNombre()
					.toLowerCase()
					.contains(txtNombreMostrarPrograma.getValue().toLowerCase())
					&& programa
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarPrograma.getValue()
											.toLowerCase())) {
				programas2.add(programa);
			}

		}

		ltbPrograma.setModel(new ListModelList<Programa>(programas2));

	}

	/**
	 * Metodo que permite filtrar las areas disponibles, mediante el componente
	 * de la lista, donde se podra visualizar el nombre y la descripcion de
	 * estas.
	 */
	@Listen("onChange = #txtNombreMostrarArea, #txtDescripcionMostrarArea")
	public void filtrarDatosArea() {

		List<AreaInvestigacion> area1 = servicioArea.buscarInactivos();
		List<AreaInvestigacion> area2 = new ArrayList<AreaInvestigacion>();

		for (AreaInvestigacion area : area1) {
			if (area.getNombre().toLowerCase()
					.contains(txtNombreMostrarArea.getValue().toLowerCase())

					&& area.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarArea.getValue()
											.toLowerCase())) {
				area2.add(area);
			}

		}

		ltbArea.setModel(new ListModelList<AreaInvestigacion>(area2));

	}

	/**
	 * Metodo que permite filtrar las tematicas disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre, area y *
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarTematica,#txtAreaMostrarTematica,#txtDescripcionMostrarTematica")
	public void filtrarDatosTematicas() {
		List<Tematica> tematicas1 = servicioTematica.buscarInactivos();
		List<Tematica> tematicas2 = new ArrayList<Tematica>();

		for (Tematica tematica : tematicas1) {
			if (tematica
					.getNombre()
					.toLowerCase()
					.contains(txtNombreMostrarTematica.getValue().toLowerCase())
					&& tematica
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtAreaMostrarTematica.getValue()
											.toLowerCase())
					&& tematica
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTematica.getValue()
											.toLowerCase())) {
				tematicas2.add(tematica);
			}

		}

		ltbTematica.setModel(new ListModelList<Tematica>(tematicas2));

	}

	/**
	 * Metodo que permite filtrar los requisitos disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estos.
	 */
	@Listen("onChange = #txtNombreMostrarRequisito,#txtDescripcionMostrarRequisito")
	public void filtrarDatosRequisitos() {
		List<Requisito> requisito1 = servicioRequisito.buscarInactivos();
		List<Requisito> requisito2 = new ArrayList<Requisito>();

		for (Requisito requisito : requisito1) {
			if (requisito
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarRequisito.getValue().toLowerCase())
					&& requisito
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarRequisito.getValue()
											.toLowerCase())) {
				requisito2.add(requisito);
			}
		}

		ltbRequisito.setModel(new ListModelList<Requisito>(requisito2));

	}

	/**
	 * Metodo que permite filtrar las actividades disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarActividad,#txtDescripcionMostrarActividad")
	public void filtrarDatosActividades() {
		List<Actividad> actividad1 = servicioActividad.buscarInactivos();
		List<Actividad> actividad2 = new ArrayList<Actividad>();

		for (Actividad actividad : actividad1) {
			if (actividad
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarActividad.getValue().toLowerCase())
					&& actividad
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarActividad.getValue()
											.toLowerCase())) {
				actividad2.add(actividad);
			}
		}

		ltbActividad.setModel(new ListModelList<Actividad>(actividad2));

	}

	/**
	 * Metodo que permite filtrar los items disponibles, mediante el componente
	 * de la lista, donde se podra visualizar el nombre, descripcion y tipo de
	 * estos.
	 */
	@Listen("onChange = #txtNombreMostrarItem, #txtTipoMostrarItem, #txtDescripcionMostrarItem")
	public void filtrarDatosItems() {
		List<ItemEvaluacion> item = servicioItem.buscarItemsActivos();
		List<ItemEvaluacion> item2 = new ArrayList<ItemEvaluacion>();

		for (ItemEvaluacion item1 : item) {
			if (item1.getNombre().toLowerCase()
					.contains(txtNombreMostrarItem.getValue().toLowerCase())
					&& item1.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarItem.getValue()
											.toLowerCase())
					&& item1.getTipo()
							.toLowerCase()
							.contains(
									txtTipoMostrarItem.getValue().toLowerCase())) {
				item2.add(item1);
			}

		}

		ltbItem.setModel(new ListModelList<ItemEvaluacion>(item2));

	}

	/**
	 * Metodo que permite filtrar los profesores disponibles, dado a la
	 * condicional de la variable booleana "variable" si es igual a "true" se
	 * mostraran los profesores sin usuario, si la variable booleana
	 * "esDirector" es igual a "true" se mostrara los profesores sin programa,
	 * sino si es "false" seran todos los profesores activos, mediante el
	 * componente de la lista, donde se podra visualizar la cedula, nombre,
	 * apellido, correo y categoria
	 */
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCategoriaMostrarProfesor,#txtCorreoMostrarProfesor")
	public void filtrarDatosCatalogo() {

		List<Profesor> profesores = servicioProfesor.buscarInactivos();
		List<Profesor> profesores2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesores) {
			if (profesor
					.getCedula()
					.toLowerCase()
					.contains(txtCedulaMostrarProfesor.getValue().toLowerCase())
					&& profesor
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCategoria()
							.getNombre()
							.toLowerCase()
							.contains(
									txtCategoriaMostrarProfesor.getValue()
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	/** Metodo que permite cerrar la ventana correspondiente al Catalogo */
	@Listen("onClick = #btnSalir")
	public void salirRestaurarDatos() {
		wdwRestauracionDatos.onClose();
	}
	
	
	/** Metodo que permite limpiar los campos */
	@Listen("onClick = #btnCancelar")
	public void cancelarRestaurarDatos() {
		
		llenarListas();
		
	}

	

	/** Metodo que permite restaurar los items seleccionados */
	@Listen("onClick = #btnRestaurar")
	public void restaurarDatos() {

		boolean estatus = true;
		
		if (ltbPrograma.getSelectedItem() == null
				&& ltbArea.getSelectedItem() == null
				&& ltbTematica.getSelectedItem() == null
				&& ltbRequisito.getSelectedItem() == null
				&& ltbActividad.getSelectedItem() == null
				&& ltbItem.getSelectedItem() == null
				&& ltbProfesor.getSelectedItem() == null
				&& ltbEstudiante.getSelectedItem() == null
				&& ltbTipoJurado.getSelectedItem() == null
				&& ltbLapso.getSelectedItem() == null) {

			Messagebox.show(
					"Debe seleccionar los datos basicos que desea restaurar",
					"Error", Messagebox.OK, Messagebox.ERROR);

		} else {

			List<Programa> programaSeleccionados = new ArrayList<Programa>();
			if (ltbPrograma.getItemCount() != 0) {
				final List<Listitem> listPrograma = ltbPrograma.getItems();
				for (int i = 0; i < listPrograma.size(); i++) {
					if (listPrograma.get(i).isSelected()) {
						Programa programa = listPrograma.get(i).getValue();
						programa.setEstatus(estatus);
						programaSeleccionados.add(programa);
						servicioPrograma.guardar(programa);
						llenarListas();
					}
				}

			}

			if (ltbArea.getItemCount() != 0) {
				final List<Listitem> listArea = ltbArea.getItems();
				for (int i = 0; i < listArea.size(); i++) {
					if (listArea.get(i).isSelected()) {
						AreaInvestigacion area = listArea.get(i).getValue();
						area.setEstatus(estatus);
						servicioArea.guardar(area);
						llenarListas();
					}
				}

			}

			if (ltbTematica.getItemCount() != 0) {
				final List<Listitem> listTematica = ltbTematica.getItems();
				for (int i = 0; i < listTematica.size(); i++) {
					if (listTematica.get(i).isSelected()) {
						Tematica tematica = listTematica.get(i).getValue();
						tematica.setEstatus(estatus);
						servicioTematica.guardar(tematica);
						llenarListas();
					}
				}

			}

			if (ltbRequisito.getItemCount() != 0) {
				final List<Listitem> listRequisito = ltbRequisito.getItems();
				for (int i = 0; i < listRequisito.size(); i++) {
					if (listRequisito.get(i).isSelected()) {
						Requisito requisito = listRequisito.get(i).getValue();
						requisito.setEstatus(estatus);
						servicioRequisito.guardar(requisito);
						llenarListas();
					}
				}
			}

			if (ltbActividad.getItemCount() != 0) {
				final List<Listitem> listActividad = ltbActividad.getItems();
				for (int i = 0; i < listActividad.size(); i++) {
					if (listActividad.get(i).isSelected()) {
						Actividad actividad = listActividad.get(i).getValue();
						actividad.setEstatus(estatus);
						servicioActividad.guardar(actividad);
						llenarListas();
					}
				}
			}

			if (ltbItem.getItemCount() != 0) {
				final List<Listitem> listItemEvaluacion = ltbItem.getItems();
				for (int i = 0; i < listItemEvaluacion.size(); i++) {
					if (listItemEvaluacion.get(i).isSelected()) {
						ItemEvaluacion item = listItemEvaluacion.get(i)
								.getValue();
						item.setEstatus(estatus);
						servicioItem.guardar(item);
						llenarListas();
					}
				}
			}

			if (ltbProfesor.getItemCount() != 0) {
				final List<Listitem> listProfesor = ltbProfesor.getItems();
				for (int i = 0; i < listProfesor.size(); i++) {
					if (listProfesor.get(i).isSelected()) {
						Profesor profesor = listProfesor.get(i).getValue();
						profesor.setEstatus(estatus);
						servicioProfesor.guardarProfesor(profesor);
						llenarListas();
					}
				}
			}

			if (ltbEstudiante.getItemCount() != 0) {
				final List<Listitem> listEstudiante = ltbProfesor.getItems();
				for (int i = 0; i < listEstudiante.size(); i++) {
					if (listEstudiante.get(i).isSelected()) {
						Estudiante estudiante = listEstudiante.get(i)
								.getValue();
						estudiante.setEstatus(estatus);
						servicioEstudiante.guardar(estudiante);
						llenarListas();
					}
				}
			}

			if (ltbTipoJurado.getItemCount() != 0) {
				final List<Listitem> listTipoJurado = ltbTipoJurado.getItems();
				for (int i = 0; i < listTipoJurado.size(); i++) {
					if (listTipoJurado.get(i).isSelected()) {
						TipoJurado tipoJurado = listTipoJurado.get(i)
								.getValue();
						tipoJurado.setEstatus(estatus);
						servicioTipoJurado.guardar(tipoJurado);
						llenarListas();
					}
				}
			}

			if (ltbLapso.getItemCount() != 0) {
				final List<Listitem> listLapso = ltbLapso.getItems();
				for (int i = 0; i < listLapso.size(); i++) {
					if (listLapso.get(i).isSelected()) {
						Lapso lapso = listLapso.get(i).getValue();
						lapso.setEstatus(estatus);
						servicioLapso.guardar(lapso);
						llenarListas();
					}
				}
			}

			Messagebox.show("Datos restaurados exitosamente", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);

		}

	}
}
