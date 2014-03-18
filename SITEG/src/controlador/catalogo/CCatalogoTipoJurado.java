package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.TipoJurado;
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

@Controller
public class CCatalogoTipoJurado extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Textbox txtNombreTipoJurado;
	@Wire
	private Textbox txtDescripcionTipoJurado;
	@Wire
	private Textbox txtNombreMostrarTipoJurado;
	@Wire
	private Textbox txtDescripcionMostrarTipoJurado;
	@Wire
	private Listbox ltbTipoJurado;
	@Wire
	private Window wdwCatalogoTipoJurado;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todas los tipos
	 * de jurados disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		// llena la lista con los tipos de jurados activos
		List<TipoJurado> tipoJurado = servicioTipoJurado.buscarActivos();
		ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado));
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
	 * Metodo que permite filtrar los tipos de jurados disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarTipoJurado,#txtDescripcionMostrarTipoJurado")
	public void filtrarDatosCatalogo() {
		List<TipoJurado> tipoJurado1 = servicioTipoJurado.buscarActivos();
		List<TipoJurado> tipoJurado2 = new ArrayList<TipoJurado>();

		for (TipoJurado tipoJurado : tipoJurado1) {
			if (tipoJurado
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarTipoJurado.getValue().toLowerCase())
					&& tipoJurado
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTipoJurado.getValue()
											.toLowerCase())) {
				tipoJurado2.add(tipoJurado);
			}
		}

		ltbTipoJurado.setModel(new ListModelList<TipoJurado>(tipoJurado2));

	}

	/*
	 * Metodo que permite obtener el objeto TipoJurado al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbTipoJurado")
	public void mostrarDatosCatalogo() {
		try {
		if (ltbTipoJurado.getItemCount() != 0) {

			if (vistaRecibida == null) {

				vistaRecibida = "maestros/VTipoJurado";

			} else {

				Listitem listItem = ltbTipoJurado.getSelectedItem();
				TipoJurado tipoJuradoDatosCatalogo = (TipoJurado) listItem
						.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", tipoJuradoDatosCatalogo.getId());
				String vista = vistaRecibida;
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoTipoJurado.onClose();
			}
		}
		} catch (NullPointerException e) {
		}
	}

	/*
	 * Metodo que permite generar una lista de los tipos de jurado que se
	 * encuentran activos en el sistema mediante el componente "Jasperreport"
	 */

	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<TipoJurado> tiposJurado = servicioTipoJurado.buscarActivos();

		if (tiposJurado.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/RTiposJurados.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								tiposJurado));
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
	@Listen("onClick = #btnSalirCatalogoTipoJurado")
	public void salirCatalogoTipoJurado() {
		wdwCatalogoTipoJurado.onClose();
	}
}