package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Programa;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

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

public class CCatalogoPrograma extends CGeneral {

	private static String vistaRecibida;

	@Wire
	private Listbox ltbPrograma;
	@Wire
	private Window wdwCatalogoPrograma;
	@Wire
	private Textbox txtNombreMostrarPrograma;
	@Wire
	private Textbox txtDescripcionMostrarPrograma;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {
		List<Programa> programas = servicioPrograma.buscarActivas();
		ltbPrograma.setModel(new ListModelList<Programa>(programas));
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
	 * Metodo que permite filtrar los programas disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estos.
	 */
	@Listen("onChange = #txtNombreMostrarPrograma,#txtDescripcionMostrarPrograma")
	public void filtrarDatosCatalogo() {
		List<Programa> programas1 = servicioPrograma.buscarActivas();
		List<Programa> programas2 = new ArrayList<Programa>();
		for (Programa programa : programas1) {
			if (programa
					.getNombre()
					.toLowerCase()
					.contains(txtNombreMostrarPrograma.getValue().toLowerCase())
					&& programa
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarPrograma.getValue()
											.toLowerCase())) {
				programas2.add(programa);
			}

		}

		ltbPrograma.setModel(new ListModelList<Programa>(programas2));

	}

	/*
	 * Metodo que permite obtener el objeto Progrma al realizar el evento doble
	 * clic sobre un item en especifico en la lista, extrayendo asi su id, para
	 * luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbPrograma")
	public void mostrarDatosCatalogo() {

		try {
			if (vistaRecibida == null) {

				vistaRecibida = "maestros/VPrograma";

			} else {

				if (ltbPrograma.getItemCount() != 0) {

					Listitem listItem = ltbPrograma.getSelectedItem();
					Programa programaDatos = servicioPrograma
							.buscarPorNombrePrograma(((Programa) listItem
									.getValue()).getNombre());
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", programaDatos.getId());
					String vista = vistaRecibida;
					map.put("vista", vista);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					Executions.sendRedirect("/vistas/arbol.zul");
					wdwCatalogoPrograma.onClose();

				}
			}
		} catch (NullPointerException e) {

			System.out.println(" ");
		}


	}

	/*
	 * Metodo que permite generar una lista de los programas que se encuentran
	 * activos en el sistema mediante el componente "Jasperreport"
	 */
	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<Programa> programas = servicioPrograma.buscarActivas();

		if (programas.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/RPrograma.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								programas));
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
	@Listen("onClick = #btnSalir")
	public void salirCatalogoPrograma() {
		wdwCatalogoPrograma.onClose();
	}
}
