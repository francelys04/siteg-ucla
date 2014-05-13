package controlador.reporte;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.Cronograma;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/**
 * Controlador que permite generar a traves de un listado el cronograma de las
 * actividades de un programa de acuerdo a un lapso academico determinado
 */
@Controller
public class CReporteCronograma extends CGeneral {

	@Wire
	private Combobox cmbCronogramaPrograma;
	@Wire
	private Combobox cmbCronogramaLapso;
	@Wire
	private Window wdwReporteCronograma;

	/**
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas y lapsos disponibles y se llena una lista del mismo en el
	 * componente de la vista
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		List<Lapso> lapsos = servicioLapso.buscarActivos();

		cmbCronogramaLapso.setModel(new ListModelList<Lapso>(lapsos));
		cmbCronogramaPrograma.setModel(new ListModelList<Programa>(programas));

	}

	/**
	 * Metodo que permite generar un reporte, dado un programa y un lapso se
	 * generara un pdf donde se muestra una lista de las actividades de esta
	 * seleccion, mediante el componente "Jasperreport" donde se mapea una serie
	 * de parametros y una lista previamente cargada que seran los datos que se
	 * muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteCronograma")
	public void GenerarCronograma() throws JRException, IOException {
		if ((cmbCronogramaPrograma.getValue() == "")
				|| (cmbCronogramaLapso.getValue() == "")) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {
			Lapso lapso = servicioLapso.buscarLapso(Long
					.parseLong(cmbCronogramaLapso.getSelectedItem().getId()));
			Programa programa = servicioPrograma
					.buscar((Long.parseLong(cmbCronogramaPrograma
							.getSelectedItem().getId())));
			FileSystemView filesys = FileSystemView.getFileSystemView();
			List<Cronograma> cronograma = servicioCronograma
					.buscarCronogramaPorLapsoYPrograma(programa, lapso);

			if (cronograma.size() != 0) {
				Map<String, Object> p = new HashMap<String, Object>();
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/ReporteCronograma.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";

				p.put("programa", cmbCronogramaPrograma.getValue());
				p.put("lapso", cmbCronogramaLapso.getValue());
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				JasperReport jasperReport = (JasperReport) JRLoader
						.loadObject(reporteSrc);

				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								cronograma));
				JasperViewer.viewReport(jasperPrint, false);

			} else {
				Messagebox.show(
						"No hay informacion disponible para esta seleccion",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);
				Cancelar();
			}

		}

	}

	/** Metodo que permite limpiar los campos de los filtros de busqueda */
	@Listen("onClick = #btnCancelarReporteCronograma")
	public void Cancelar() {
		cmbCronogramaPrograma.setValue("");
		cmbCronogramaLapso.setValue("");
	}

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirReporteCronograma")
	public void Salir() {
		wdwReporteCronograma.onClose();

	}

}
