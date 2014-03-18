package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Requisito;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CCatalogoRequisito extends CGeneral {

	private static String vistaRecibida;

	@Wire
	private Listbox ltbRequisito;
	@Wire
	private Textbox txtNombreMostrarRequisito;
	@Wire
	private Textbox txtDescripcionMostrarRequisito;
	@Wire
	private Textbox txtNombreRequisito;
	@Wire
	private Textbox txtDescripcionRequisito;
	@Wire
	private Button btnEliminarRequisito;
	private long id = 0;
	@Wire
	private Window wdwCatalogoRequisito;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan lista correspondientes de la
	 * vista, asicomo los objetos empleados dentro de este controlador.
	 */
	public void inicializar(Component comp) {

		List<Requisito> requisito = servicioRequisito.buscarActivos();
		ltbRequisito.setModel(new ListModelList<Requisito>(requisito));

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("requisitoCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Requisito requisito2 = servicioRequisito
						.buscarRequisito(codigo);
				txtNombreRequisito.setValue(requisito2.getNombre());
				txtDescripcionRequisito.setValue(requisito2.getDescripcion());
				id = requisito2.getId();
				map.clear();
				map = null;

			}
		}
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
	 * Metodo que permite filtrar los requisitos disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estos.
	 */
	@Listen("onChange = #txtNombreMostrarRequisito,#txtDescripcionMostrarRequisito")
	public void filtrarDatosCatalogo() {
		List<Requisito> requisito1 = servicioRequisito.buscarActivos();
		List<Requisito> requisito2 = new ArrayList<Requisito>();

		for (Requisito requisito : requisito1) {
			if (requisito
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarRequisito.getValue().toLowerCase())
					&& requisito
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarRequisito.getValue()
											.toLowerCase())) {
				requisito2.add(requisito);
			}
		}

		ltbRequisito.setModel(new ListModelList<Requisito>(requisito2));

	}

	/*
	 * Metodo que permite obtener el objeto Requisito al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbRequisito")
	public void mostrarDatosCatalogo() {
		
		try {
		
		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VRequisito";

		} else {
			if (ltbRequisito.getItemCount() != 0) {
				Listitem listItem = ltbRequisito.getSelectedItem();
				Requisito requisitoDatosCatalogo = (Requisito) listItem
						.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", requisitoDatosCatalogo.getId());
				String vista = vistaRecibida;
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoRequisito.onClose();
			}
		}
		} catch (NullPointerException e) {
		}
	}

	/*
	 * Metodo que permite generar una lista de los requisitos que se deben
	 * consignar para realizar un trabjo especial de grado que se encuentran
	 * activos en el sistema mediante el componente "Jasperreport"
	 */

	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<Requisito> requisitos = servicioRequisito.buscarActivos();

		if (requisitos.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/RRequisito.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								requisitos));
				JasperViewer.viewReport(jasperPrint, false);

			} catch (JRException e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Messagebox.show("No hay informacion disponible", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
		}

	}

	/* Metodo que permite cerrar la ventana correspondiente al Catalogo */
	@Listen("onClick = #btnSalirCatalogoRequisito")
	public void salirCatalogoRequisitos() {
		wdwCatalogoRequisito.onClose();
	}

}
