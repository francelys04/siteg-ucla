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

	SCronograma servicioCronograma = GeneradorBeans.getServicioCronograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SActividad servicioActividad = GeneradorBeans.getServicioActividad();
	@Wire
	private Combobox cmbLapsoCrearCronograma;
	@Wire
	private Combobox cmbProgramaCrearCronograma;
	@Wire
	private Listbox ltbActividadesDisponibles;
	@Wire
	private Listbox ltbActividadesSeleccionadas;

	List<Cronograma> cronogramas = new ArrayList();
	List<Actividad> actividades = new ArrayList();

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Lapso> lapsos = servicioLapso.buscarActivos();
		List<Programa> programas = servicioPrograma.buscarActivas();

		// llenarActividades();
		if (cmbLapsoCrearCronograma != null) {

			cmbLapsoCrearCronograma.setModel(new ListModelList<Lapso>(lapsos));

			cmbProgramaCrearCronograma.setModel(new ListModelList<Programa>(
					programas));

		}

	}

	@Listen("onClick = #btnAgregarActividades")
	public void moverDerechaActividad() {

		List<Listitem> listitemEliminar = new ArrayList();
		List<Listitem> listItem = ltbActividadesDisponibles.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {

				if (listItem.get(i).isSelected()) {

					Actividad actividad = listItem.get(i).getValue();
					actividades.remove(actividad);
					Cronograma cronograma = new Cronograma();
					cronograma.setActividad(actividad);
					cronogramas.add(cronograma);
					ltbActividadesSeleccionadas
							.setModel(new ListModelList<Cronograma>(cronogramas));
					listitemEliminar.add(listItem.get(i));
				}

			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbActividadesDisponibles.removeItemAt(listitemEliminar.get(i).getIndex());
		}

		ltbActividadesSeleccionadas.setMultiple(false);
		ltbActividadesSeleccionadas.setCheckmark(false);
		ltbActividadesSeleccionadas.setMultiple(true);
		ltbActividadesSeleccionadas.setCheckmark(true);

	}

	@Listen("onClick = #btnRemoverActividades")
	public void moverIzquierdaActividad() {

		List<Listitem> listitemEliminar = new ArrayList();
		List<Listitem> listItem2 = ltbActividadesSeleccionadas.getItems();
		if (listItem2.size() != 0) {
			for (int i = 0; i < listItem2.size(); i++) {

				if (listItem2.get(i).isSelected()) {

					Cronograma cronograma = listItem2.get(i).getValue();
					cronogramas.remove(cronograma);
					actividades.add(cronograma.getActividad());
					ltbActividadesDisponibles
							.setModel(new ListModelList<Actividad>(actividades));
					listitemEliminar.add(listItem2.get(i));
				}

			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbActividadesSeleccionadas.removeItemAt(listitemEliminar.get(i)
					.getIndex());
		}
		ltbActividadesDisponibles.setMultiple(false);
		ltbActividadesDisponibles.setCheckmark(false);
		ltbActividadesDisponibles.setMultiple(true);
		ltbActividadesDisponibles.setCheckmark(true);

	}

	@Listen("onClick = #btnCancelarCronograma")
	public void limpiarCampos() {
		cmbLapsoCrearCronograma.setValue("");
		cmbProgramaCrearCronograma.setValue("");
		ltbActividadesSeleccionadas.getItems().clear();
		ltbActividadesDisponibles.getItems().clear();

	}

	@Listen("onClick = #btnCrearCronograma")
	public void crearCronograma() {
		boolean error = false;
		Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbLapsoCrearCronograma.getSelectedItem().getId()));
		Programa programa = servicioPrograma
				.buscar((Long.parseLong(cmbProgramaCrearCronograma
						.getSelectedItem().getId())));

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
		if (!cmbLapsoCrearCronograma.getValue().equals("")
				&& !cmbProgramaCrearCronograma.getValue().equals("")) {
			llenarActividades();
		}
	}

	@Listen("onChange = #cmbProgramaCrearCronograma")
	public void buscarLapso() {
		if (!cmbLapsoCrearCronograma.getValue().equals("")
				&& !cmbProgramaCrearCronograma.getValue().equals("")) {
			llenarActividades();
		}

	}

	private void llenarActividades() {
		if (ltbActividadesDisponibles.isMultiple())
			ltbActividadesDisponibles.setMultiple(false);
		if (ltbActividadesSeleccionadas.isMultiple())
			ltbActividadesSeleccionadas.setMultiple(false);
		Lapso lapso = servicioLapso.buscarLapso(Long
				.parseLong(cmbLapsoCrearCronograma.getSelectedItem().getId()));
		Programa programa = servicioPrograma
				.buscar((Long.parseLong(cmbProgramaCrearCronograma
						.getSelectedItem().getId())));
		cronogramas = servicioCronograma
				.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		actividades = servicioActividad
				.buscarActividadSinCronograma(programa, lapso);
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
