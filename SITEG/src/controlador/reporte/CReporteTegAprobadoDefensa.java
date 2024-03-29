package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Programa;
import modelo.Tematica;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import controlador.CGeneral;

@Controller
public class CReporteTegAprobadoDefensa extends CGeneral {

	private static final long serialVersionUID = 7740151952778333602L;
	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	Tematica tematica = new Tematica();

	/*********** Promedio Teg Fecha Entrega **************/

	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Jasperreport jstVistaPrevia;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaTodos = new Programa(10000, "Todos", "", "", true,
				null);
		programas.add(programaTodos);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));

	}

	@Listen("onClick = #btnGenerarReporteTegAprobadoDefensa")
	public void generarTegsAprobadosDefensa() throws JRException {

		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();

		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		fechaInicio = dtbFechaInicio.getValue();
		fechaFin = dtbFechaFin.getValue();

		List<Defensa> defensasSeleccionadas = new ArrayList();
		String estatus = "Aprobado";

		System.out.println("das:" + defensasSeleccionadas.size());
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			if (nombrePrograma.equals("Todos")) {

				defensasSeleccionadas = servicioDefensa
						.buscarDefensaTegSegunEstatus(estatus, fechaInicio,
								fechaFin);
			} else if (!nombrePrograma.equals("Todos")
					&& nombreArea.equals("Todos")) {
				defensasSeleccionadas = servicioDefensa
						.buscarDefensaTegSegunEstatusPrograma(estatus,
								programa, fechaInicio, fechaFin);
			} else if (!nombrePrograma.equals("Todos")
					&& !nombreArea.equals("Todos")
					&& !nombreTematica.equals("Todos")) {
				Tematica tematica = servicioTematica
						.buscarTematicaPorNombre(nombreTematica);
				defensasSeleccionadas = servicioDefensa
						.buscarDefensaTegSegunEstatusTematica(estatus,
								tematica, fechaInicio, fechaFin);
			} else if (!nombrePrograma.equals("Todos")
					&& !nombreArea.equals("Todos")
					&& nombreTematica.equals("Todos")) {
				defensasSeleccionadas = servicioDefensa
						.buscarDefensaTegSegunEstatusArea(estatus, area,
								fechaInicio, fechaFin);
			}

		}
		if (defensasSeleccionadas.size() != 0) {

			for (int i = 0; i < defensasSeleccionadas.size(); i++) {
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantePorTeg(defensasSeleccionadas.get(i)
								.getTeg());

				if (estudiantes.size() != 0) {

					String nombre = estudiantes.get(0).getNombre();
					String apellido = estudiantes.get(0).getApellido();
					defensasSeleccionadas.get(i).getTeg()
							.setEstatus(nombre + " " + apellido);
				}
			}

			FileSystemView filesys = FileSystemView.getFileSystemView();
			Map parametro = new HashMap();
			String rutaUrl = obtenerDirectorio();
			String reporteSrc = rutaUrl
					+ "SITEG/vistas/reportes/estructurados/compilados/RTegAprobadoDefensa.jasper";
			String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";

			parametro.put("titulo",
					"UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
							+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
							+ "DIRECCION DE PROGRAMA");
			parametro.put("programaNombre", cmbPrograma.getValue());
			parametro.put("areaNombre", cmbArea.getValue());
			parametro.put("tematicaNombre", cmbTematica.getValue());
			parametro.put("fechaInicio", fechaInicio);
			parametro.put("fechaFin", fechaFin);
			parametro.put("logoUcla", reporteImage + "logo ucla.png");
			parametro.put("logoCE", reporteImage + "logo CE.png");
			jstVistaPrevia.setSrc(reporteSrc);
			jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
					defensasSeleccionadas));
			jstVistaPrevia.setType("pdf");
			jstVistaPrevia.setParameters(parametro);

		} else {
			Messagebox.show("No ha informacion disponible para este intervalo");
		}
	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		String idPrograma = cmbPrograma.getSelectedItem().getId();
		String nombrePrograma = cmbPrograma.getValue();
		if (nombrePrograma.equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
			cmbArea.setDisabled(true);
			cmbTematica.setDisabled(true);

		} else {
			cmbArea.setDisabled(false);
			programa = servicioPrograma.buscar(Long.parseLong(idPrograma));
			List<AreaInvestigacion> programaAreas = servicioProgramaArea
					.buscarAreasDePrograma(programa);
			AreaInvestigacion areaInvestigacion = new AreaInvestigacion(
					1000000, "Todos", "", true);
			programaAreas.add(areaInvestigacion);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(programaAreas));
		}
	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() throws JRException {
		String idArea = cmbArea.getSelectedItem().getId();
		String nombreArea = cmbArea.getValue();
		if (nombreArea.equals("Todos")) {
			cmbTematica.setValue("Todos");
			cmbTematica.setDisabled(true);
		} else {
			cmbTematica.setDisabled(false);
			area = servicioArea.buscarArea(Long.parseLong(idArea));
			List<Tematica> tematicasTodos = servicioTematica
					.buscarTematicasDeArea(area);
			Tematica tematicaTodos = new Tematica(10000000, "Todos", "", true,
					null);
			tematicasTodos.add(tematicaTodos);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicasTodos));
		}
	}

	@Listen("onClick = #btnSalirReporteTegAprobadoDefensa")
	public void cancelarTegsAprobadosDefensa() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setValue("");
		cmbTematica.setDisabled(true);
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());

		jstVistaPrevia.setSrc("");
		jstVistaPrevia.setDatasource(null);
	}

}
