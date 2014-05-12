package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.ItemEvaluacion;
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
 * Controlador asociado a la vista catalogo item que permite mostrar los items
 * de evaluacion disponibles a traves de un listado
 */
@Controller
public class CCatalogoItem extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Textbox txtNombreItem;
	@Wire
	private Textbox txtDescripcionItem;
	@Wire
	private Textbox txtTipoItem;
	@Wire
	private Textbox txtIdItem;
	@Wire
	private Listbox ltbItem;
	@Wire
	private Window wdwCatalogoItem;
	@Wire
	private Textbox txtNombreMostrarItem;
	@Wire
	private Textbox txtTipoMostrarItem;
	@Wire
	private Textbox txtDescripcionMostrarItem;

	/**
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los items
	 * disponibles y se llena el listado del mismo en el componente lista de la
	 * vista.
	 */
	@Override
	public void inicializar(Component comp) {

		List<ItemEvaluacion> items = servicioItem.buscarItemsActivos();
		ltbItem.setModel(new ListModelList<ItemEvaluacion>(items));
	}

	/**
	 * Metodo que permite filtrar los items disponibles, mediante el componente
	 * de la lista, donde se podra visualizar el nombre, descripcion y tipo de
	 * estos.
	 */
	@Listen("onChange = #txtNombreMostrarItem, #txtTipoMostrarItem, #txtDescripcionMostrarItem")
	public void filtrarDatosCatalogo() {
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
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 *  @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/**
	 * Metodo que permite obtener el objeto Item al realizar el evento doble
	 * clic sobre un item en especifico en la lista, extrayendo asi su id, para
	 * luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbItem")
	public void mostrarDatosCatalogo() {

		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VItem";

		} else {
			if (ltbItem.getSelectedCount() != 0) {
				Listitem listItem = ltbItem.getSelectedItem();
				ItemEvaluacion itemDatosCatalogo = (ItemEvaluacion) listItem
						.getValue();
				String vista = vistaRecibida;
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", itemDatosCatalogo.getId());
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoItem.onClose();
			}
		}
	}

	/**
	 * Metodo que permite generar una lista de los items de envaluacion que se
	 * encuentran activos en el sistema, agrupados por tipo mediante el
	 * componente "Jasperreport"
	 */

	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<ItemEvaluacion> items = servicioItem.buscarItemsActivos();

		if (items.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/RItemsEvaluacion.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(items));
				JasperViewer.viewReport(jasperPrint, false);

			} catch (JRException e) {
				System.out.println(e);
				e.printStackTrace();
			}

		} else {
			Messagebox.show("No hay informacion disponible", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
		}

	}

	/** Metodo que permite cerrar la ventana correspondiente al Catalogo */
	@Listen("onClick = #btnSalirCatalogoItems")
	public void salirCatalogoItems() {
		wdwCatalogoItem.onClose();
	}

}
