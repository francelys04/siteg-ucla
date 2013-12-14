package controlador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.ProgramaArea;
import modelo.ProgramaItem;
import modelo.ProgramaRequisito;
import modelo.Requisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleGroupsModel;

import servicio.SAreaInvestigacion;
import servicio.SItem;
import servicio.SLapso;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SProgramaItem;
import servicio.SProgramaRequisito;
import servicio.SRequisito;

import configuracion.GeneradorBeans;

@Controller
public class CConfigurarPrograma extends CGeneral {

	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SRequisito servicioRequisito = GeneradorBeans.getServicioRequisito();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	SProgramaRequisito servicioProgramaRequisito = GeneradorBeans
			.getServicioProgramaRequisito();
	SItem servicioItem = GeneradorBeans.getServicioItem();
	SProgramaItem servicioProgramaItem = GeneradorBeans
			.getServicioProgramaItem();
	@Wire
	private Combobox cmbLapsoConfigurarPrograma;
	@Wire
	private Combobox cmbProgramaConfigurarPrograma;
	@Wire
	private Listbox ltbAreasDisponibles;
	@Wire
	private Listbox ltbAreasSeleccionadas;
	@Wire
	private Listbox ltbRequisitosDisponibles;
	@Wire
	private Listbox ltbRequisitosSeleccionados;
	@Wire
	private Listbox ltbItemsDisponibles;
	@Wire
	private Listbox ltbItemsSeleccionados;
	@Wire
	private Listbox ltbCondiciones;
	

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		List<Programa> programas = servicioPrograma.buscarActivas();

		if (cmbLapsoConfigurarPrograma != null) {

			cmbLapsoConfigurarPrograma
					.setModel(new ListModelList<Lapso>(lapsos));

			cmbProgramaConfigurarPrograma.setModel(new ListModelList<Programa>(
					programas));

		}
	}

	@Listen("onClick = #btnAgregarAreas")
	public void moverDerechaArea() {

		Listitem list1 = ltbAreasDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbAreasSeleccionadas);
	}

	@Listen("onClick = #btnRemoverAreas")
	public void moverIzquierdaArea() {
		Listitem list2 = ltbAreasSeleccionadas.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbAreasDisponibles);
	}

	@Listen("onClick = #btnAgregarRequisitos")
	public void moverDerechaRequisito() {

		Listitem list1 = ltbRequisitosDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbRequisitosSeleccionados);
	}

	@Listen("onClick = #btnRemoverRequisitos")
	public void moverIzquierdaRequisito() {
		Listitem list2 = ltbRequisitosSeleccionados.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbRequisitosDisponibles);
	}

	@Listen("onClick = #btnAgregarItems")
	public void moverDerechaItems() {

		Listitem list1 = ltbItemsDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbItemsSeleccionados);
	}

	@Listen("onClick = #btnRemoverItems")
	public void moverIzquierdaItems() {
		Listitem list2 = ltbItemsSeleccionados.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbItemsDisponibles);
	}

	@Listen("onClick = #btnGuardarConfiguracionPrograma")
	public void guardar() {
		Lapso lapso = servicioLapso.buscarPorNombre(cmbLapsoConfigurarPrograma
				.getValue());
		Programa programa = servicioPrograma
				.buscarPorNombrePrograma(cmbProgramaConfigurarPrograma
						.getValue());
		List<ProgramaArea> programasAreas = new ArrayList<ProgramaArea>();
		for (int i = 0; i < ltbAreasSeleccionadas.getItemCount(); i++) {
			AreaInvestigacion area = ltbAreasSeleccionadas.getItems().get(i)
					.getValue();
			ProgramaArea programaArea = new ProgramaArea(programa, area, lapso);
			programasAreas.add(programaArea);
		}
		servicioProgramaArea.guardar(programasAreas);
		List<ProgramaRequisito> programasRequisitos = new ArrayList<ProgramaRequisito>();
		for (int i = 0; i < ltbRequisitosSeleccionados.getItemCount(); i++) {
			Requisito requisito = ltbRequisitosSeleccionados.getItems().get(i)
					.getValue();
			ProgramaRequisito programaRequisito = new ProgramaRequisito(
					programa, requisito, lapso);
			programasRequisitos.add(programaRequisito);
		}
		servicioProgramaRequisito.guardar(programasRequisitos);
		List<ProgramaItem> programasItems = new ArrayList<ProgramaItem>();
		for (int i = 0; i < ltbItemsSeleccionados.getItemCount(); i++) {
			ItemEvaluacion item = ltbItemsSeleccionados.getItems().get(i)
					.getValue();
			ProgramaItem programaItem = new ProgramaItem(programa, item, lapso);
			programasItems.add(programaItem);
		}
		servicioProgramaItem.guardar(programasItems);
	}

	@Listen("onClick = #btnCancelarConfigurarPrograma")
	public void limpiarCampos(){
		cmbLapsoConfigurarPrograma.setValue("");
		cmbProgramaConfigurarPrograma.setValue("");
		llenarListas();
	}
	
	@Listen("onChange = #cmbProgramaConfigurarPrograma")
	public void buscarPrograma() {
		if (cmbProgramaConfigurarPrograma.getValue() != null
				&& cmbLapsoConfigurarPrograma.getValue() != null)
			llenarListas();
	}
	
	@Listen("onChange = #cmbLapsoConfigurarPrograma")
	public void buscarLapso() {
		if (cmbProgramaConfigurarPrograma.getValue() != null
				&& cmbLapsoConfigurarPrograma.getValue() != null)
			llenarListas();

	}

	public void llenarListas() {
		String programaNombre = cmbProgramaConfigurarPrograma.getValue();
		String lapsoNombre = cmbLapsoConfigurarPrograma.getValue();
		Programa programa = servicioPrograma
				.buscarPorNombrePrograma(programaNombre);
		Lapso lapso = servicioLapso.buscarPorNombre(lapsoNombre);
		List<AreaInvestigacion> areasIzquierda = servicioArea
				.buscarAreasSinPrograma(programa, lapso);
		List<AreaInvestigacion> areasDerecha = servicioProgramaArea
				.buscarAreasDePrograma(programa, lapso);
		List<Requisito> requisitosIzquierda = servicioRequisito
				.buscarRequisitosDisponibles(programa, lapso);
		List<Requisito> requisitosDerecha = servicioProgramaRequisito
				.buscarRequisitosEnPrograma(programa, lapso);
		List<ItemEvaluacion> itemsIzquierda = servicioItem
				.buscarItemsDisponibles(programa, lapso);
		List<ItemEvaluacion> itemsDerecha = servicioProgramaItem
				.buscarItemsEnPrograma(programa, lapso);
		ltbAreasDisponibles.setModel(new ListModelList<AreaInvestigacion>(
				areasIzquierda));
		ltbAreasSeleccionadas.setModel(new ListModelList<AreaInvestigacion>(
				areasDerecha));
		ltbRequisitosDisponibles.setModel(new ListModelList<Requisito>(
				requisitosIzquierda));
		ltbRequisitosSeleccionados.setModel(new ListModelList<Requisito>(
				requisitosDerecha));
		ltbItemsDisponibles.setModel(new ListModelList<ItemEvaluacion>(
				itemsIzquierda));
		ltbItemsSeleccionados.setModel(new ListModelList<ItemEvaluacion>(
				itemsDerecha));
	}
}
