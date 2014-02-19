package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.Cronograma;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que le permite al director de programa configurar las
 * actividades, es decir, crear el cronograma por el cual se regira dicho
 * programa para un determinado lapso academico
 */
@Controller
public class CCrearCronograma extends CGeneral {

	private static final long serialVersionUID = -5977701710543879084L;
	private static boolean actividadesCargadas;
	private static boolean actividadesSeleccionadas;
	List<Cronograma> cronogramas = new ArrayList<Cronograma>();
	List<Actividad> actividades = new ArrayList<Actividad>();
	@Wire
	private Combobox cmbLapsoCrearCronograma;
	@Wire
	private Textbox txtProgramaCrearCronograma;
	@Wire
	private Listbox ltbActividadesDisponibles;
	@Wire
	private Listbox ltbActividadesSeleccionadas;
	@Wire
	private Window wdwCrearCronograma;

	/*
	 * Metodo heredado del Controlador CGeneral dondese verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		actividadesCargadas = false;
		actividadesSeleccionadas = false;
		List<Lapso> lapsos = servicioLapso.buscarActivos();
		if (txtProgramaCrearCronograma.getValue().compareTo("") == 0) {
			Programa programa1 = servicioPrograma
					.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
			txtProgramaCrearCronograma.setValue(programa1.getNombre());
		}
		if (cmbLapsoCrearCronograma != null) {
			cmbLapsoCrearCronograma.setModel(new ListModelList<Lapso>(lapsos));
		}

	}

	/*
	 * Metodo que permite mover una o varias actividades hacia la lista de
	 * actividades del cronograma.
	 */
	@Listen("onClick = #btnAgregarActividades")
	public void moverDerechaActividad() {

		if (actividadesCargadas == true) {

			List<Listitem> listitemEliminar = new ArrayList<Listitem>();
			List<Listitem> listItem = ltbActividadesDisponibles.getItems();

			if (listItem.size() != 0) {

				System.out.println("lista distinta de cero");

				for (int i = 0; i < listItem.size(); i++) {

					if (listItem.get(i).isSelected()) {
						actividadesSeleccionadas = true;
						Actividad actividad = listItem.get(i).getValue();
						actividades.remove(actividad);
						Cronograma cronograma = new Cronograma();
						cronograma.setActividad(actividad);
						cronogramas.add(cronograma);
						ltbActividadesSeleccionadas
								.setModel(new ListModelList<Cronograma>(
										cronogramas));
						listitemEliminar.add(listItem.get(i));
					}

				}
			}

			if (actividadesSeleccionadas == false) {
				Messagebox.show("Debe seleccionar una actividad ",
						"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
			}

			for (int i = 0; i < listitemEliminar.size(); i++) {
				ltbActividadesDisponibles.removeItemAt(listitemEliminar.get(i)
						.getIndex());
			}

		} else {

			Messagebox
					.show("Debe seleccionar el lapso academico y el programa al cual se le creara el cronograma",
							"Error", Messagebox.OK, Messagebox.ERROR);

		}

		ltbActividadesSeleccionadas.setMultiple(false);
		ltbActividadesSeleccionadas.setCheckmark(false);
		ltbActividadesSeleccionadas.setMultiple(true);
		ltbActividadesSeleccionadas.setCheckmark(true);

	}

	/*
	 * Metodo que permite mover una o varias actividades asignadas al cronograma
	 * a la lista de la izquierda (actividades disponibles).
	 */
	@Listen("onClick = #btnRemoverActividades")
	public void moverIzquierdaActividad() {

		if (actividadesCargadas == true) {

			List<Listitem> listitemEliminar = new ArrayList<Listitem>();
			List<Listitem> listItem2 = ltbActividadesSeleccionadas.getItems();
			if (listItem2.size() != 0) {
				for (int i = 0; i < listItem2.size(); i++) {
					if (listItem2.get(i).isSelected()) {
						Cronograma cronograma = listItem2.get(i).getValue();
						cronogramas.remove(cronograma);
						actividades.add(cronograma.getActividad());
						ltbActividadesDisponibles
								.setModel(new ListModelList<Actividad>(
										actividades));
						listitemEliminar.add(listItem2.get(i));
					}

				}
			}

			if (actividadesSeleccionadas == false) {
				Messagebox.show("Debe seleccionar una actividad ",
						"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);

			}

			for (int i = 0; i < listitemEliminar.size(); i++) {
				ltbActividadesSeleccionadas.removeItemAt(listitemEliminar
						.get(i).getIndex());
			}

		} else {

			Messagebox
					.show("Debe seleccionar el lapso academico y el programa al cual se le creara el cronograma",
							"Error", Messagebox.OK, Messagebox.ERROR);
		}

		ltbActividadesDisponibles.setMultiple(false);
		ltbActividadesDisponibles.setCheckmark(false);
		ltbActividadesDisponibles.setMultiple(true);
		ltbActividadesDisponibles.setCheckmark(true);

	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarCronograma")
	public void limpiarCampos() {
		cmbLapsoCrearCronograma.setValue("");
		ltbActividadesSeleccionadas.getItems().clear();
		ltbActividadesDisponibles.getItems().clear();
		actividadesCargadas = false;
		actividadesSeleccionadas = false;

	}
	
	
	/*
	 * Metodo que permite cerrar la vista de crear cronograma
	 */
	@Listen("onClick = #btnSalirCronograma")
	public void salirCronograma() {
		
		wdwCrearCronograma.onClose();

	}
	
	
	
	

	/*
	 * Metodo que permite guardar las actividades seleccionadas para el programa
	 * en determinado lapso, creando asi el cronograma para el programa
	 */
	@Listen("onClick = #btnCrearCronograma")
	public void crearCronograma() {
		boolean error = false;
		Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbLapsoCrearCronograma.getSelectedItem().getId()));
		Programa programa = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());

		List<Cronograma> cronogramas = servicioCronograma
				.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		if (!cronogramas.isEmpty()) {
			servicioCronograma.limpiar(cronogramas);
		}
		cronogramas = new ArrayList<Cronograma>();
		for (int i = 0; i < ltbActividadesSeleccionadas.getItemCount(); i++) {
			Listitem listItem = ltbActividadesSeleccionadas.getItemAtIndex(i);
			Date fechaInicio = ((Datebox) ((listItem.getChildren().get(1)))
					.getFirstChild()).getValue();
			Date fechaFin = ((Datebox) ((listItem.getChildren().get(2)))
					.getFirstChild()).getValue();
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				error = true;
			}
			int id = ((Spinner) ((listItem.getChildren().get(3)))
					.getFirstChild()).getValue();
			Actividad actividad = servicioActividad.buscarActividad(id);
			Cronograma cronograma = new Cronograma(lapso, programa, actividad,
					fechaInicio, fechaFin);
			cronogramas.add(cronograma);
		}
		if (!error) {
			servicioCronograma.guardar(cronogramas);
			Messagebox.show("Cronograma Registrado con exito", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
			limpiarCampos();
		} else {
			Messagebox
					.show("La fecha de fin de las actividades debe ser posterior a la fecha de inicio",
							"Error", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/*
	 * Metodo que permite buscar dinamicamente las actividades establecidas para
	 * cierto lapso que se seleccione y el programa del director de programa en
	 * sesion
	 */
	@Listen("onChange = #cmbLapsoCrearCronograma")
	public void buscarLapso() {
		if (!cmbLapsoCrearCronograma.getValue().equals("")) {
			llenarActividades();
		}
	}

	/*
	 * Metodo que permite buscar las actividades para cierto lapso, y cierto
	 * programa, para asi llenar las listas respectivas de la vista
	 */
	private void llenarActividades() {

		actividadesCargadas = true;
		if (ltbActividadesDisponibles.isMultiple())
			ltbActividadesDisponibles.setMultiple(false);
		if (ltbActividadesSeleccionadas.isMultiple())
			ltbActividadesSeleccionadas.setMultiple(false);
		Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbLapsoCrearCronograma.getSelectedItem().getId()));
		Programa programa = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
		cronogramas = servicioCronograma.buscarCronogramaPorLapsoYPrograma(
				programa, lapso);
		actividades = servicioActividad.buscarActividadSinCronograma(programa,
				lapso);
		ltbActividadesSeleccionadas.setModel(new ListModelList<Cronograma>(
				cronogramas));
		ltbActividadesDisponibles.setModel(new ListModelList<Actividad>(
				actividades));
		ltbActividadesSeleccionadas.setMultiple(false);
		ltbActividadesSeleccionadas.setCheckmark(false);
		ltbActividadesSeleccionadas.setMultiple(true);
		ltbActividadesSeleccionadas.setCheckmark(true);
		ltbActividadesDisponibles.setMultiple(false);
		ltbActividadesDisponibles.setCheckmark(false);
		ltbActividadesDisponibles.setMultiple(true);
		ltbActividadesDisponibles.setCheckmark(true);
	}
}
