package controlador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.CondicionPrograma;
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
import org.zkoss.zul.Spinner;

import servicio.SAreaInvestigacion;
import servicio.SCondicionPrograma;
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
	SItem servicioItem = GeneradorBeans.getServicioItem();
	SProgramaItem servicioProgramaItem = GeneradorBeans
			.getServicioProgramaItem();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans
			.getServicioCondicionPrograma();
	SProgramaRequisito servicioProgramaRequisito = GeneradorBeans
			.getServicioProgramaRequisito();

	@Wire
	private Combobox cmbLapsoConfigurarPrograma;
	@Wire
	private Combobox cmbProgramaConfigurarPrograma;
	@Wire
	private Listbox ltbAreasDisponibles;
	@Wire
	private Listbox ltbAreasSeleccionadas;
	@Wire
	private Listbox ltbItemsDisponibles;
	@Wire
	private Listbox ltbItemsSeleccionados;
	@Wire
	private Listbox ltbCondiciones;
	@Wire
	private Listbox ltbRequisitosDisponibles;

	@Wire
	private Listbox ltbRequisitosSeleccionadas;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		List<Programa> programas = servicioPrograma.buscarActivas();
		// llenarListas();
		List<Requisito> requisito = servicioRequisito.buscarActivos();
		ltbRequisitosDisponibles.setModel(new ListModelList<Requisito>(
				requisito));

		if (cmbLapsoConfigurarPrograma != null) {

			cmbLapsoConfigurarPrograma
					.setModel(new ListModelList<Lapso>(lapsos));

			cmbProgramaConfigurarPrograma.setModel(new ListModelList<Programa>(
					programas));

		}
	}

	@Listen("onClick = #btnAgregarRequisitos")
	public void moverDerechaRequisitos() {

		Listitem list1 = ltbRequisitosDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbRequisitosSeleccionadas);
	}

	@Listen("onClick = #btnRemoverRequisitos")
	public void moverIzquierdaRequisitos() {
		Listitem list2 = ltbRequisitosSeleccionadas.getSelectedItem();
		System.out.println(list2.getValue().toString());
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbRequisitosDisponibles);
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
		Lapso lapso = servicioLapso
				.buscarLapso(Long.parseLong(cmbLapsoConfigurarPrograma
						.getSelectedItem().getId()));
		Programa programa = servicioPrograma.buscar((Long
				.parseLong(cmbProgramaConfigurarPrograma.getSelectedItem()
						.getId())));
		List<ProgramaArea> programasAreas = new ArrayList<ProgramaArea>();
		List<AreaInvestigacion> areas = servicioProgramaArea
				.buscarAreasDePrograma(programa, lapso);
		if (!areas.isEmpty()) {
			for (int i = 0; i < areas.size(); i++) {
				ProgramaArea programaArea = new ProgramaArea(programa,
						areas.get(i), lapso);
				programasAreas.add(programaArea);
			}
			servicioProgramaArea.limpiar(programasAreas);
		}
		programasAreas = new ArrayList<ProgramaArea>();
		for (int i = 0; i < ltbAreasSeleccionadas.getItemCount(); i++) {
			AreaInvestigacion area = ltbAreasSeleccionadas.getItems().get(i)
					.getValue();
			ProgramaArea programaArea = new ProgramaArea(programa, area, lapso);
			programasAreas.add(programaArea);
		}
		servicioProgramaArea.guardar(programasAreas);

		List<ProgramaRequisito> programasRequisito = new ArrayList<ProgramaRequisito>();
		List<Requisito> requisit = servicioProgramaRequisito.buscarRequisitos(
				programa, lapso);
		if (!requisit.isEmpty()) {
			for (int i = 0; i < requisit.size(); i++) {
				ProgramaRequisito programaRequisito = new ProgramaRequisito(
						programa, requisit.get(i), lapso);
				programasRequisito.add(programaRequisito);
			}
			servicioProgramaRequisito.limpiar(programasRequisito);
		}
		programasRequisito = new ArrayList<ProgramaRequisito>();
		for (int i = 0; i < ltbRequisitosSeleccionadas.getItemCount(); i++) {
			Requisito requisito = ltbRequisitosSeleccionadas.getItems().get(i)
					.getValue();
			ProgramaRequisito programaRequisito = new ProgramaRequisito(
					programa, requisito, lapso);
			programasRequisito.add(programaRequisito);
		}
		servicioProgramaRequisito.guardar(programasRequisito);

		List<ProgramaItem> programasItems = new ArrayList<ProgramaItem>();
		List<ItemEvaluacion> itemsE = servicioProgramaItem
				.buscarItemsEnPrograma(programa, lapso);
		if (!itemsE.isEmpty()) {
			for (int i = 0; i < itemsE.size(); i++) {
				ProgramaItem programaItem = new ProgramaItem(programa,
						itemsE.get(i), lapso);
				programasItems.add(programaItem);
			}
			servicioProgramaItem.limpiar(programasItems);
		}
		programasItems = new ArrayList<ProgramaItem>();
		System.out.print(ltbItemsSeleccionados.getItemCount());
		for (int i = 0; i < ltbItemsSeleccionados.getItemCount(); i++) {
			System.out.print(ltbItemsSeleccionados.getItems().get(i)
					.getValue());
			ItemEvaluacion item = ltbItemsSeleccionados.getItems().get(i)
					.getValue();
			ProgramaItem programaItem = new ProgramaItem(programa, item, lapso);
			programasItems.add(programaItem);
		}
		servicioProgramaItem.guardar(programasItems);

		List<CondicionPrograma> condicionesProgramas = new ArrayList<CondicionPrograma>();
		System.out.println("numero" + ltbCondiciones.getItemCount());
		for (int i = 0; i < ltbCondiciones.getItemCount(); i++) {
			Listitem listItem = ltbCondiciones.getItemAtIndex(i);
			int valor = ((Spinner) ((listItem.getChildren().get(1)))
					.getFirstChild()).getValue();
			System.out.println(valor);
			CondicionPrograma condicionPrograma = ltbCondiciones.getItems()
					.get(i).getValue();
			CondicionPrograma condicionProgramaReal = new CondicionPrograma(
					condicionPrograma.getCondicion(),
					condicionPrograma.getPrograma(), lapso, valor);
			condicionesProgramas.add(condicionProgramaReal);
		}
		servicioCondicionPrograma.guardar(condicionesProgramas);
		alert("Configuraciones Guardadas");
		limpiarCampos();
	}

	@Listen("onClick = #btnCancelarConfigurarPrograma")
	public void limpiarCampos() {
		cmbLapsoConfigurarPrograma.setValue("");
		cmbProgramaConfigurarPrograma.setValue("");
		ltbAreasDisponibles.getItems().clear();
		
		 ltbAreasSeleccionadas.getItems().clear();
	
		 ltbItemsDisponibles.getItems().clear();
		
		 ltbItemsSeleccionados.getItems().clear();
		
		 ltbCondiciones.getItems().clear();
		
		 ltbRequisitosDisponibles.getItems().clear();

		
		 ltbRequisitosSeleccionadas.getItems().clear();
	     
	}

	@Listen("onChange = #cmbProgramaConfigurarPrograma")
	public void buscarPrograma() {
		if (!cmbLapsoConfigurarPrograma.getValue().equals("")
				&& !cmbProgramaConfigurarPrograma.getValue().equals("")) {
			llenarListas();
		}
	}

	@Listen("onChange = #cmbLapsoConfigurarPrograma")
	public void buscarLapso() {
		if (!cmbLapsoConfigurarPrograma.getValue().equals("")
				&& !cmbProgramaConfigurarPrograma.getValue().equals("")) {
			llenarListas();
		}

	}

	public void llenarListas() {
		
		long idLapso = Long
				.parseLong( cmbLapsoConfigurarPrograma.getSelectedItem().getId());
		System.out.println("idlapso");
		System.out.println(idLapso);
		Lapso lapso = servicioLapso
				.buscarLapso(idLapso);
		Programa programa = servicioPrograma.buscar((Long
				.parseLong(cmbProgramaConfigurarPrograma.getSelectedItem()
						.getId())));
		List<AreaInvestigacion> areasIzquierda = servicioArea
				.buscarAreasSinPrograma(programa, lapso);
		List<AreaInvestigacion> areasDerecha = servicioProgramaArea
				.buscarAreasDePrograma(programa, lapso);
		List<ItemEvaluacion> itemsIzquierda = servicioItem
				.buscarItemsDisponibles(programa, lapso);
		List<ItemEvaluacion> itemsDerecha = servicioProgramaItem
				.buscarItemsEnPrograma(programa, lapso);
		List<CondicionPrograma> condicionesDePrograma = servicioCondicionPrograma
				.buscarCondicionesPrograma(programa, lapso);
		List<Requisito> requisitosIzquierda = servicioRequisito
				.buscarRequisitosDisponibles(programa, lapso);
		List<Requisito> requisitosDerecha = servicioProgramaRequisito
				.buscarRequisitosEnPrograma(programa, lapso);
		ltbAreasDisponibles.setModel(new ListModelList<AreaInvestigacion>(
				areasIzquierda));
		ltbAreasSeleccionadas.setModel(new ListModelList<AreaInvestigacion>(
				areasDerecha));
		ltbItemsDisponibles.setModel(new ListModelList<ItemEvaluacion>(
				itemsIzquierda));
		ltbItemsSeleccionados.setModel(new ListModelList<ItemEvaluacion>(
				itemsDerecha));
		ltbCondiciones.setModel(new ListModelList<CondicionPrograma>(
				condicionesDePrograma));
		ltbRequisitosDisponibles.setModel(new ListModelList<Requisito>(
				requisitosIzquierda));
		ltbRequisitosSeleccionadas.setModel(new ListModelList<Requisito>(
				requisitosDerecha));
		}
	

}
