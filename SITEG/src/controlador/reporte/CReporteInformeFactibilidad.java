package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.Tematica;
import modelo.reporte.InformeFactibilidad;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteInformeFactibilidad extends CGeneral {

	@Wire
	private Window wdwReporteInformeFactibilidad;

	private static TegEstatus tegestatus;

	private String[] estatusproyectos = { "Proyecto Factible",
			"Proyecto No Factible" };

	long id = 0;

	private String reporteFact;
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

	}

	@Listen("onClick = #btnGenerarReporteInformeFactibilidad")
	public void GenerarInforme() throws JRException {

		Estudiante estudiante = ObtenerUsuarioEstudiante();
		List<Teg> teg = servicioTeg.buscarTegPorEstudiante(estudiante);
		if (teg.size() != 0) {

			int n = teg.size() - 1;
			System.out.println(n);
			String estatus1 = "Proyecto Factible";

			tegestatus = servicioTegEstatus.buscarTegEstatus(estatus1,
					teg.get(n));
			if (tegestatus != null) {

				Long proyecto = teg.get(n).getId();
				String estatus = teg.get(n).getEstatus();

				String nprofesor = teg.get(n).getTutor().getNombre();
				String aprofesor = teg.get(n).getTutor().getApellido();
				String nestudiante = estudiante.getNombre();
				String aestudiante = estudiante.getApellido();
				String cestudiante = estudiante.getCedula();
				String titulo = teg.get(n).getTitulo();
				String observacion = teg.get(n).getFactibilidad()
						.getObservacion();
				String programateg = estudiante.getPrograma().getNombre();
				String ndirector = estudiante.getPrograma()
						.getDirectorPrograma().getNombre();
				String adirector = estudiante.getPrograma()
						.getDirectorPrograma().getApellido();
				List<InformeFactibilidad> elementos = new ArrayList<InformeFactibilidad>();
				elementos
						.add(new InformeFactibilidad(estatus, nestudiante,
								aestudiante, cestudiante, titulo, programateg,
								nprofesor, aprofesor, observacion, ndirector,
								adirector));

				reporteFact = "SITEG/vistas/reportes/estructurados/compilados/ReporteInformeFactible.jasper";

				FileSystemView filesys = FileSystemView.getFileSystemView();

				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl + reporteFact;
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";

				Map p = new HashMap();
				p.put("fecha", new Date().toLocaleString());

				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");
				JasperReport jasperReport = (JasperReport) JRLoader
						.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								elementos));
				JasperViewer.viewReport(jasperPrint);

			} else {
				String estatus2 = "Proyecto No Factible";
				tegestatus = servicioTegEstatus.buscarTegEstatus(estatus2,
						teg.get(n));
				if (tegestatus != null) {

					Long proyecto = teg.get(n).getId();
					String estatus = teg.get(n).getEstatus();

					String nprofesor = teg.get(n).getTutor().getNombre();
					String aprofesor = teg.get(n).getTutor().getApellido();
					String nestudiante = estudiante.getNombre();
					String aestudiante = estudiante.getApellido();
					String cestudiante = estudiante.getCedula();
					String titulo = teg.get(n).getTitulo();
					String observacion = teg.get(n).getFactibilidad()
							.getObservacion();
					String programateg = estudiante.getPrograma().getNombre();
					String ndirector = estudiante.getPrograma()
							.getDirectorPrograma().getNombre();
					String adirector = estudiante.getPrograma()
							.getDirectorPrograma().getApellido();
					List<InformeFactibilidad> elementos = new ArrayList<InformeFactibilidad>();
					elementos.add(new InformeFactibilidad(estatus, nestudiante,
							aestudiante, cestudiante, titulo, programateg,
							nprofesor, aprofesor, observacion, ndirector,
							adirector));

					reporteFact = "SITEG/vistas/reportes/estructurados/compilados/ReporteInformeNoFactible.jasper";

					FileSystemView filesys = FileSystemView.getFileSystemView();

					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl + reporteFact;
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					Map p = new HashMap();
					p.put("fecha", new Date().toLocaleString());

					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");
					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);
					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									elementos));
					JasperViewer.viewReport(jasperPrint, false);

				}
				else {
					Messagebox.show("No hay informacion disponible",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
				}

			}
			
		}
		else
		{
			Messagebox.show("No Tiene TEG Asociado",
					"Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
		}

	}

	@Listen("onClick = #btnSalirReporteInformeFactibilidad")
	public void salirPromedioTiempoTeg() throws JRException {

		wdwReporteInformeFactibilidad.onClose();
	}

}
