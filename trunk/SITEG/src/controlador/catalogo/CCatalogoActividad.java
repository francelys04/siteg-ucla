package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Actividad;
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
public class CCatalogoActividad extends CGeneral {

	private long id = 0;
	private static String vistaRecibida;

	@Wire
	private Listbox ltbActividad;
	@Wire
	private Textbox txtNombreMostrarActividad;
	@Wire
	private Textbox txtDescripcionMostrarActividad;
	@Wire
	private Window wdwCatalogoActividad;
	@Wire
	private Window wdwActividad;

	/*
	 * Metodo heredado del Controlador CGeneral donde se se buscan todas las
	 * actividades disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Actividad> actividad = servicioActividad.buscarActivos();
		ltbActividad.setModel(new ListModelList<Actividad>(actividad));
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
	 * Metodo que permite filtrar las actividades disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre y la
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarActividad,#txtDescripcionMostrarActividad")
	public void filtrarDatosCatalogo() {
		List<Actividad> actividad1 = servicioActividad.buscarActivos();
		List<Actividad> actividad2 = new ArrayList<Actividad>();

		for (Actividad actividad : actividad1) {
			if (actividad
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarActividad.getValue().toLowerCase())
					&& actividad
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarActividad.getValue()
											.toLowerCase())) {
				actividad2.add(actividad);
			}
		}

		ltbActividad.setModel(new ListModelList<Actividad>(actividad2));

	}

	/*
	 * Metodo que permite obtener el objeto Actividad al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser enviada mediante un map a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbActividad")
	public void mostrarDatosCatalogo() {
		if (ltbActividad.getItemCount() != 0) {
			Listitem listItem = ltbActividad.getSelectedItem();
			Actividad actividadDatosCatalogo = (Actividad) listItem.getValue();
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", actividadDatosCatalogo.getId());
			String vista = vistaRecibida;
			map.put("vista", vista);
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
			Executions.sendRedirect("/vistas/arbol.zul");

			wdwCatalogoActividad.onClose();
		}
	}

	/*
	 * Metodo que permite generar una lista de las actividades que se encuentran
	 * activos en el sistema mediante el componente "Jasperreport"
	 */

	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<Actividad> actividades = servicioActividad.buscarActivos();

		if (actividades.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/RActividades.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								actividades));
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
	@Listen("onClick = #btnSalirCatalogoActividad")
	public void salirCatalogoActividad() {
		wdwCatalogoActividad.onClose();
	}
}
