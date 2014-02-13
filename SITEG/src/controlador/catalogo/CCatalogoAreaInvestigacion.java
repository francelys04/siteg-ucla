package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SAreaInvestigacion;
import servicio.SLapso;
import servicio.SPrograma;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CCatalogoAreaInvestigacion extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;
	private static boolean encontrado;
	private static long idPrograma1;
	public static List<AreaInvestigacion> area1;

	private Label lblPrograma;
	@Wire
	private Listbox ltbArea;
	@Wire
	private Window wdwCatalogoArea;
	@Wire
	private Textbox txtNombreMostrarArea;
	@Wire
	private Textbox txtDescripcionMostrarArea;
	@Wire
	private Combobox cmbPrograma;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todas las areas
	 * disponibles, condicionado por la variable "encontrado" donde si es igual
	 * a "true" se llena el listado del mismo en el componente lista de la
	 * vista.
	 */
	public void inicializar(Component comp) {
		List<Programa> programa = servicioPrograma.buscarActivas();
		cmbPrograma.setModel(new ListModelList<Programa>(programa));
		List<AreaInvestigacion> area = servicioArea.buscarActivos();

		if (encontrado == true) {
			ltbArea.setEmptyMessage("No hay areas de investigacion registradas");
			ltbArea.setTooltiptext("Doble clic para seleccionar el area");
			ltbArea.setModel(new ListModelList<AreaInvestigacion>(area));
			cmbPrograma.setVisible(false);
			lblPrograma.setVisible(false);
			System.out.println("paso if");
		} else {
			ltbArea.setTooltiptext("Doble clic para ver las tematicas del area");
			ltbArea.setEmptyMessage("Seleccione un programa para ver las areas");
			cmbPrograma.setVisible(true);

		}
	}

	public void metodoPrender() {
		encontrado = true;

	}

	public void metodoApagar() {
		encontrado = false;

	}

	/* Metodo que permite cargar una lista de areas dado un programa y un lapso */
	@Listen("onSelect = #cmbPrograma")
	public void llenarLista() {

		idPrograma1 = Long.parseLong(cmbPrograma.getSelectedItem().getId());
		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa = servicioPrograma.buscar(idPrograma1);
		List<AreaInvestigacion> areas = servicioArea.areasPrograma(programa,
				lapso);
		ltbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

	}

	/*
	 * Metodo que permite filtrar las areas disponibles, mediante el componente
	 * de la lista, donde se podra visualizar el nombre y la descripcion de
	 * estas.
	 */
	@Listen("onChange = #txtNombreMostrarArea, #txtDescripcionMostrarArea")
	public void filtrarDatosCatalogo() {
		if (encontrado == true) {
			area1 = servicioArea.buscarActivos();
		} else {
			idPrograma1 = Long.parseLong(cmbPrograma.getSelectedItem().getId());
			Lapso lapso = servicioLapso.buscarLapsoVigente();
			Programa programa = servicioPrograma.buscar(idPrograma1);
			area1 = servicioArea.areasPrograma(programa, lapso);

		}
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

	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/*
	 * Metodo que permite obtener el objeto Area al realizar el evento doble
	 * clic sobre un item en especifico en la lista, dado al condicional, donde
	 * la variable "encontrado" es igual a "false" el metodo recibirId, recibe dicho
	 * objeto por parametro y es redireccionado a la vista
	 * VCatalogoTematicaArea, en caso contrario, se extrae su id, para luego
	 * poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbArea")
	public void mostrarDatosCatalogo() {
		if (ltbArea.getItemCount() != 0) {
			if (encontrado == false) {
				Listitem listItem = ltbArea.getSelectedItem();
				AreaInvestigacion areaDatosCatalogo = (AreaInvestigacion) listItem
						.getValue();
				CCatalogoTematicaArea area = new CCatalogoTematicaArea();
				area.recibirId(areaDatosCatalogo);
				Window window = (Window) Executions.createComponents(
						"/vistas/catalogos/VCatalogoTematicaArea.zul", null,
						null);
				window.doModal();

			} else {

				Listitem listItem = ltbArea.getSelectedItem();
				AreaInvestigacion areaDatosCatalogo = (AreaInvestigacion) listItem
						.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", areaDatosCatalogo.getId());
				String vista = vistaRecibida;
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoArea.onClose();
			}
		}
	}

}
