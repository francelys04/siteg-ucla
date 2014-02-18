package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.compuesta.CondicionPrograma;
import modelo.compuesta.ProgramaArea;
import modelo.compuesta.ProgramaItem;
import modelo.compuesta.ProgramaRequisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;

/*
 * Controlador que le permite al director de programa configurar los
 * requisitos, areas, items de evaluacion y condiciones que tendra dicho
 * programa para un determinado lapso academico
 */
@Controller
public class CConfigurarPrograma extends CGeneral {

	private static final long serialVersionUID = 3465137893569110941L;
	private static boolean listasCargadas;
	@Wire
	private Combobox cmbLapsoConfigurarPrograma;
	@Wire
	private Textbox txtProgramaConfigurarPrograma;
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

	/*
	 * Metodo heredado del Controlador CGeneral dondese verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		listasCargadas = false;

		List<Lapso> lapsos = servicioLapso.buscarActivos();
		if (txtProgramaConfigurarPrograma.getValue().compareTo("") == 0) {
			Programa programa1 = servicioPrograma
					.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
			txtProgramaConfigurarPrograma.setValue(programa1.getNombre());
		}

		if (cmbLapsoConfigurarPrograma != null) {

			cmbLapsoConfigurarPrograma
					.setModel(new ListModelList<Lapso>(lapsos));
		}
	}

	/*
	 * Metodo que permite mover uno o varios requisitos hacia la lista de
	 * requisitos del programa a configurar.
	 */
	@Listen("onClick = #btnAgregarRequisitos")
	public void moverDerechaRequisitos() {

		try {
			if (listasCargadas == true) {

				Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbRequisitosDisponibles
						.getModel()).getSelection();

				if (selectedItems.size() == 0) {

					Messagebox.show("Debe seleccionar un requisito ",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else {

					((ListModelList) ltbRequisitosSeleccionadas.getModel())
							.addAll(selectedItems);
					((ListModelList) ltbRequisitosDisponibles.getModel())
							.removeAll(selectedItems);

				}
			} else {

				Messagebox
						.show("Debe seleccionar el lapso academico junto con el programa que se desea configurar ",
								"Error", Messagebox.OK, Messagebox.ERROR);

			}
		} catch (NullPointerException e) {

		}

	}

	/*
	 * Metodo que permite mover uno o varios requisitos asignados al programa a
	 * la lista de la izquierda (requisitos disponibles).
	 */
	@Listen("onClick = #btnRemoverRequisitos")
	public void moverIzquierdaRequisitos() {

		try {

			if (listasCargadas == true) {
				Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbRequisitosSeleccionadas
						.getModel()).getSelection();

				if (selectedItems.size() == 0) {

					Messagebox.show("Debe seleccionar un requisito ",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else {

					((ListModelList) ltbRequisitosDisponibles.getModel())
							.addAll(selectedItems);
					((ListModelList) ltbRequisitosSeleccionadas.getModel())
							.removeAll(selectedItems);

				}
			} else {

				Messagebox
						.show("Debe seleccionar el lapso academico junto con el programa que se desea configurar ",
								"Error", Messagebox.OK, Messagebox.ERROR);

			}
		} catch (NullPointerException e) {

		}

	}

	/*
	 * Metodo que permite mover una o varias areas hacia la lista de areas del
	 * programa a configurar.
	 */
	@Listen("onClick = #btnAgregarAreas")
	public void moverDerechaArea() {

		try {

			if (listasCargadas == true) {

				Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbAreasDisponibles
						.getModel()).getSelection();

				if (selectedItems.size() == 0) {

					Messagebox.show(
							"Debe seleccionar un area de investigacion ",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else {

					((ListModelList) ltbAreasSeleccionadas.getModel())
							.addAll(selectedItems);
					((ListModelList) ltbAreasDisponibles.getModel())
							.removeAll(selectedItems);

				}
			} else {

				Messagebox
						.show("Debe seleccionar el lapso academico junto con el programa que se desea configurar ",
								"Error", Messagebox.OK, Messagebox.ERROR);

			}

		} catch (NullPointerException e) {

		}

	}

	/*
	 * Metodo que permite mover una o varias areas asignadas al programa a la
	 * lista de la izquierda (areas disponibles).
	 */
	@Listen("onClick = #btnRemoverAreas")
	public void moverIzquierdaArea() {

		try {

			if (listasCargadas == true) {

				Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbAreasSeleccionadas
						.getModel()).getSelection();

				if (selectedItems.size() == 0) {

					Messagebox.show(
							"Debe seleccionar un area de investigacion ",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else {

					((ListModelList) ltbAreasDisponibles.getModel())
							.addAll(selectedItems);
					((ListModelList) ltbAreasSeleccionadas.getModel())
							.removeAll(selectedItems);

				}
			} else {

				Messagebox
						.show("Debe seleccionar el lapso academico junto con el programa que se desea configurar ",
								"Error", Messagebox.OK, Messagebox.ERROR);

			}
		} catch (NullPointerException e) {

		}

	}

	/*
	 * Metodo que permite mover uno o varios items de evaluacion hacia la lista
	 * de items de evaluacion del programa a configurar.
	 */
	@Listen("onClick = #btnAgregarItems")
	public void moverDerechaItems() {

		try {

			if (listasCargadas == true) {

				Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbItemsDisponibles
						.getModel()).getSelection();

				if (selectedItems.size() == 0) {

					Messagebox.show("Debe seleccionar un item de evaluacion ",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else {

					((ListModelList) ltbItemsSeleccionados.getModel())
							.addAll(selectedItems);
					((ListModelList) ltbItemsDisponibles.getModel())
							.removeAll(selectedItems);

				}
			} else {

				Messagebox
						.show("Debe seleccionar el lapso academico junto con el programa que se desea configurar ",
								"Error", Messagebox.OK, Messagebox.ERROR);

			}
		} catch (NullPointerException e) {

		}

	}

	/*
	 * Metodo que permite mover uno o varios items de evaluacion asignados al
	 * programa a la lista de la izquierda (items de evaluacion disponibles).
	 */
	@Listen("onClick = #btnRemoverItems")
	public void moverIzquierdaItems() {

		try {

			if (listasCargadas == true) {

				Set selectedItems = ((org.zkoss.zul.ext.Selectable) ltbItemsSeleccionados
						.getModel()).getSelection();

				if (selectedItems.size() == 0) {

					Messagebox.show("Debe seleccionar un item de evaluacion ",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else {

					((ListModelList) ltbItemsDisponibles.getModel())
							.addAll(selectedItems);
					((ListModelList) ltbItemsSeleccionados.getModel())
							.removeAll(selectedItems);

				}
			} else {

				Messagebox
						.show("Debe seleccionar el lapso academico junto con el programa que se desea configurar ",
								"Error", Messagebox.OK, Messagebox.ERROR);
			}
		} catch (NullPointerException e) {

		}

	}

	/*
	 * Metodo que permite guardar las areas, requisitos e items de evaluacion
	 * seleccionados para dicho programa. Asi como tambien los valores de las
	 * condiciones respectivas
	 */
	@Listen("onClick = #btnGuardarConfiguracionPrograma")
	public void guardar() {
		Lapso lapso = servicioLapso
				.buscarLapso(Long.parseLong(cmbLapsoConfigurarPrograma
						.getSelectedItem().getId()));
		Programa programa = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
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
		for (int i = 0; i < ltbItemsSeleccionados.getItemCount(); i++) {
			System.out
					.print(ltbItemsSeleccionados.getItems().get(i).getValue());
			ItemEvaluacion item = ltbItemsSeleccionados.getItems().get(i)
					.getValue();
			ProgramaItem programaItem = new ProgramaItem(programa, item, lapso);
			programasItems.add(programaItem);
		}
		servicioProgramaItem.guardar(programasItems);

		List<CondicionPrograma> condicionesProgramas = new ArrayList<CondicionPrograma>();
		for (int i = 0; i < ltbCondiciones.getItemCount(); i++) {
			Listitem listItem = ltbCondiciones.getItemAtIndex(i);
			int valor = ((Spinner) ((listItem.getChildren().get(1)))
					.getFirstChild()).getValue();
			CondicionPrograma condicionPrograma = ltbCondiciones.getItems()
					.get(i).getValue();
			CondicionPrograma condicionProgramaReal = new CondicionPrograma(
					condicionPrograma.getCondicion(),
					condicionPrograma.getPrograma(), lapso, valor);
			condicionesProgramas.add(condicionProgramaReal);
		}
		servicioCondicionPrograma.guardar(condicionesProgramas);
		Messagebox.show("Configuraciones guardas exitosamente", "Informacion",
				Messagebox.OK, Messagebox.INFORMATION);
		limpiarCampos();
	}

	/*
	 * Metodo que permite reiniciar los campos de la vista a su estado original
	 */
	@Listen("onClick = #btnCancelarConfigurarPrograma")
	public void limpiarCampos() {
		cmbLapsoConfigurarPrograma.setValue("");
		ltbAreasDisponibles.getItems().clear();
		ltbAreasSeleccionadas.getItems().clear();
		ltbItemsDisponibles.getItems().clear();
		ltbItemsSeleccionados.getItems().clear();
		ltbCondiciones.getItems().clear();
		ltbRequisitosDisponibles.getItems().clear();
		ltbRequisitosSeleccionadas.getItems().clear();
		listasCargadas = false;
	}

	/*
	 * Metodo que permite buscar dinamicamente las configuraciones establecidas
	 * para cierto lapso que se seleccione y el programa del director de
	 * programa en sesion
	 */
	@Listen("onChange = #cmbLapsoConfigurarPrograma")
	public void buscarLapso() {
		if (!cmbLapsoConfigurarPrograma.getValue().equals("")) {
			llenarListas();
		}
	}

	/*
	 * Metodo que permite buscar los requisitos, areas, items de evaluacion y
	 * condiciones para cierto lapso, y cierto programa, para asi llenar las
	 * listas respectivas de la vista
	 */
	public void llenarListas() {

		listasCargadas = true;
		long idLapso = Long.parseLong(cmbLapsoConfigurarPrograma
				.getSelectedItem().getId());
		Lapso lapso = servicioLapso.buscarLapso(idLapso);
		Programa programa = servicioPrograma
				.buscarProgramaDeDirector(ObtenerUsuarioProfesor());
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

		ltbAreasDisponibles.setMultiple(true);
		ltbAreasDisponibles.setCheckmark(true);
		ltbAreasSeleccionadas.setMultiple(true);
		ltbAreasSeleccionadas.setCheckmark(true);
		ltbRequisitosDisponibles.setMultiple(true);
		ltbRequisitosDisponibles.setCheckmark(true);
		ltbRequisitosSeleccionadas.setMultiple(true);
		ltbRequisitosSeleccionadas.setCheckmark(true);
		ltbItemsDisponibles.setMultiple(true);
		ltbItemsDisponibles.setCheckmark(true);
		ltbItemsSeleccionados.setMultiple(true);
		ltbItemsSeleccionados.setCheckmark(true);

	}

}
