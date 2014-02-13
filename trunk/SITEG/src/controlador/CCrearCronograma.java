package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Actividad;
import modelo.AreaInvestigacion;
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

import configuracion.GeneradorBeans;

import servicio.SActividad;
import servicio.SCronograma;
import servicio.SLapso;
import servicio.SPrograma;

@Controller
public class CCrearCronograma extends CGeneral {

	@Wire
	private Combobox cmbLapsoCrearCronograma;
	@Wire
	private Textbox txtProgramaCrearCronograma;
	@Wire
	private Listbox ltbActividadesDisponibles;
	@Wire
	private Listbox ltbActividadesSeleccionadas;

	private static boolean actividadesCargadas;
	private static boolean actividadesSeleccionadas;

	List<Cronograma> cronogramas = new ArrayList();
	List<Actividad> actividades = new ArrayList();

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		actividadesCargadas = false;
		actividadesSeleccionadas = false;

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		if (txtProgramaCrearCronograma.getValue().compareTo("") == 0){
			Programa programa1 = servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
			txtProgramaCrearCronograma.setValue(programa1.getNombre());
		}

		// llenarActividades();
		if (cmbLapsoCrearCronograma != null) {
			cmbLapsoCrearCronograma.setModel(new ListModelList<Lapso>(lapsos));
			
		}
		

	}

	@Listen("onClick = #btnAgregarActividades")
	public void moverDerechaActividad() {

		if (actividadesCargadas == true) {

			List<Listitem> listitemEliminar = new ArrayList();
			List<Listitem> listItem = ltbActividadesDisponibles.getItems();
		
			if (listItem.size() != 0) {
				
				System.out.println("lista distinta de cero");
				
				for (int i = 0; i < listItem.size(); i++) {

					if (listItem.get(i).isSelected()) {
						
						System.out.println("item seleccionado");

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
			
			
			if(actividadesSeleccionadas == false){
				
				System.out.println("no seleccione actividades");

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

	@Listen("onClick = #btnRemoverActividades")
	public void moverIzquierdaActividad() {

		if (actividadesCargadas == true) {

			List<Listitem> listitemEliminar = new ArrayList();
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
			
			if(actividadesSeleccionadas == false) {

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

	@Listen("onClick = #btnCancelarCronograma")
	public void limpiarCampos() {
		cmbLapsoCrearCronograma.setValue("");
		ltbActividadesSeleccionadas.getItems().clear();
		ltbActividadesDisponibles.getItems().clear();
		actividadesCargadas = false;
		actividadesSeleccionadas = false;

	}

	@Listen("onClick = #btnCrearCronograma")
	public void crearCronograma() {
		boolean error = false;
		Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbLapsoCrearCronograma.getSelectedItem().getId()));
		Programa programa = servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor());

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
			System.out.println(fechaFin);
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				error = true;
			}
			// fechaFin.equals(null) || fechaInicio.equals(null) ||
			// fechaInicio.after(fechaFin)
			int id = ((Spinner) ((listItem.getChildren().get(3)))
					.getFirstChild()).getValue();
			Actividad actividad = servicioActividad.buscarActividad(id);
			Boolean estatus = true;
			Cronograma cronograma = new Cronograma(lapso, programa, actividad,
					fechaInicio, fechaFin, estatus);
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

	@Listen("onChange = #cmbLapsoCrearCronograma")
	public void buscarPrograma() {
		if (!cmbLapsoCrearCronograma.getValue().equals(""))
				 {
			llenarActividades();
		}
	}


	private void llenarActividades() {

		actividadesCargadas = true;

		if (ltbActividadesDisponibles.isMultiple())
			ltbActividadesDisponibles.setMultiple(false);
		if (ltbActividadesSeleccionadas.isMultiple())
			ltbActividadesSeleccionadas.setMultiple(false);
		Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbLapsoCrearCronograma.getSelectedItem().getId()));
		Programa programa = servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
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
