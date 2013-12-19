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
	SProgramaRequisito servicioProgramaRequisito = GeneradorBeans.getServicioProgramaRequisito();
	
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
	private Listbox  ltbRequisitosDisponibles;
	
	@Wire
	private Listbox ltbRequisitosSeleccionadas;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		List<Programa> programas = servicioPrograma.buscarActivas();
		llenarListas();
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
		
		List<ProgramaRequisito> programasRequisito = new ArrayList<ProgramaRequisito>();
		for (int i = 0; i < ltbRequisitosSeleccionadas.getItemCount(); i++) {
			Requisito requisito = ltbRequisitosSeleccionadas.getItems().get(i)
					.getValue();
			ProgramaRequisito programaRequisito = new ProgramaRequisito(programa, requisito, lapso);
			programasRequisito.add(programaRequisito);
		}
		servicioProgramaRequisito.guardar(programasRequisito);
		
		
		
		List<ProgramaItem> programasItems = new ArrayList<ProgramaItem>();
		for (int i = 0; i < ltbItemsSeleccionados.getItemCount(); i++) {
			ItemEvaluacion item = ltbItemsSeleccionados.getItems().get(i)
					.getValue();
			ProgramaItem programaItem = new ProgramaItem(programa, item, lapso);
			programasItems.add(programaItem);
		}
		servicioProgramaItem.guardar(programasItems);
		List<CondicionPrograma> condicionesProgramas = new ArrayList<CondicionPrograma>();
		for (int i = 0; i < ltbCondiciones.getItemCount(); i++) {
			Listitem listItem = ltbCondiciones.getItemAtIndex(i);
			int valor = ((Spinner)((listItem.getChildren().get(1))).getFirstChild()).getValue();
			System.out.println(valor);
			CondicionPrograma condicionPrograma = ltbCondiciones.getItems().get(i)
					.getValue();
			CondicionPrograma condicionProgramaReal = new CondicionPrograma(condicionPrograma.getCondicion(), condicionPrograma.getPrograma(), lapso, valor);
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
		List<ItemEvaluacion> itemsIzquierda = servicioItem
				.buscarItemsDisponibles(programa, lapso);
		List<ItemEvaluacion> itemsDerecha = servicioProgramaItem
				.buscarItemsEnPrograma(programa, lapso);
		List<CondicionPrograma> condicionesDePrograma = servicioCondicionPrograma
				.buscarCondicionesPrograma(programa, lapso);
		List<Requisito> requisitosIzquierda = servicioRequisito.buscarRequisitosDisponibles(programa, lapso);
		List<Requisito> requisitosDerecha = servicioProgramaRequisito.buscarRequisitosEnPrograma(programa, lapso);
		
		if(condicionesDePrograma.toString() == "[]"){
			condicionesDePrograma = servicioCondicionPrograma
					.buscarCondicionesPrograma(programa, null);
		}			
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
