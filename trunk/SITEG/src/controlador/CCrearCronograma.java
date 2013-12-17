package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.CondicionPrograma;
import modelo.Cronograma;
import modelo.Lapso;
import modelo.Programa;
import modelo.ProgramaArea;

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
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Lapso> lapsos = servicioLapso.buscarActivos();
		List<Programa> programas = servicioPrograma.buscarActivas();

		if (cmbLapsoCrearCronograma != null) {

			cmbLapsoCrearCronograma
					.setModel(new ListModelList<Lapso>(lapsos));

			cmbProgramaCrearCronograma.setModel(new ListModelList<Programa>(
					programas));

		}
	}
	
	@Listen("onClick = #btnAgregarActividades")
	public void moverDerechaActividad() {

		Listitem list1 = ltbActividadesDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbActividadesSeleccionadas);
	}

	@Listen("onClick = #btnRemoverActividades")
	public void moverIzquierdaActividad() {
		Listitem list2 = ltbActividadesSeleccionadas.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbActividadesDisponibles);
	}
	
	@Listen("onClick = #btnCancelarCronograma")
	public void limpiarCampos(){
		cmbLapsoCrearCronograma.setValue("");
		cmbProgramaCrearCronograma.setValue("");
		llenarActividades();
	}
	
	@Listen("onClick = #btnCrearCronograma")
	public void crearCronograma(){
		Lapso lapso = servicioLapso.buscarPorNombre(cmbLapsoCrearCronograma
				.getValue());
		Programa programa = servicioPrograma
				.buscarPorNombrePrograma(cmbProgramaCrearCronograma
						.getValue());
		List<Cronograma> cronogramas = new ArrayList<Cronograma>();
		for (int i = 0; i < ltbActividadesSeleccionadas.getItemCount(); i++) {
			System.out.println("imprimo cronograma");
			System.out.println(ltbActividadesSeleccionadas.getItemCount());
			Listitem listItem = ltbActividadesSeleccionadas.getItemAtIndex(i);
			Date fechaInicio = ((Datebox)((listItem.getChildren().get(1))).getFirstChild()).getValue();
			Date fechaFin = ((Datebox)((listItem.getChildren().get(2))).getFirstChild()).getValue();
			int id = ((Spinner) ((listItem.getChildren().get(3))).getFirstChild()).getValue();
//			Actividad actividad = ((Actividad)((listItem.getChildren().get(0))).getFirstChild());
			//System.out.println(ltbActividadesSeleccionadas.getSelectedItems().);
			Actividad actividad = servicioActividad.buscarActividad(id);
//			System.out.println(listItem.getChildren().get(0).getFirstChild());
			System.out.println("imprimo id");
			System.out.println(id);
//			Actividad actividadCronograma = ltbActividadesSeleccionadas.getItems().get(i)
//					.getValue();
//			System.out.println(actividad.toString());
			Boolean estatus = true;
			Cronograma cronograma = new Cronograma(lapso, programa, actividad, fechaInicio, fechaFin, estatus);
			cronogramas.add(cronograma);
		}
		servicioCronograma.guardar(cronogramas);
		alert("Cronograma Guardadas");
		limpiarCampos();
	}
	
	@Listen("onChange = #cmbLapsoCrearCronograma")
	public void buscarPrograma() {
		if (cmbLapsoCrearCronograma.getValue() != null
				&& cmbProgramaCrearCronograma.getValue() != null)
			llenarActividades();
	}

	@Listen("onChange = #cmbProgramaCrearCronograma")
	public void buscarLapso() {
		if (cmbProgramaCrearCronograma.getValue() != null
				&& cmbLapsoCrearCronograma.getValue() != null)
			llenarActividades();

	}

	private void llenarActividades() {
		String programaNombre = cmbProgramaCrearCronograma.getValue();
		String lapsoNombre = cmbLapsoCrearCronograma.getValue();
		Programa programa = servicioPrograma
				.buscarPorNombrePrograma(programaNombre);
		Lapso lapso = servicioLapso.buscarPorNombre(lapsoNombre);
		List<Cronograma> cronogramas = servicioCronograma.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		List<Actividad>	actividades = servicioActividad.buscarActividadSinCronograma(programa, lapso);
		ltbActividadesSeleccionadas.setModel(new ListModelList<Cronograma>(
				cronogramas));
		ltbActividadesDisponibles.setModel(new ListModelList<Actividad>(
				actividades));
	}
}
