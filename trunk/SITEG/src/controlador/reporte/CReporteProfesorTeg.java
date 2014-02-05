package controlador.reporte;

import java.util.*;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;
import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoProfesorTematica;

import servicio.SAreaInvestigacion;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STematica;

@Controller
public class CReporteProfesorTeg extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	@Wire
	private Window wdwReporteProfesorTeg;
	@Wire
	private Datebox dtbFechaReporteProfesorTeg;
	@Wire
	private Datebox dtbInicioReporteProfesorTeg;
	@Wire
	private Datebox dtbFinReporteProfesorTeg;
	@Wire
	private Textbox txtCedulaReporteProfesorTeg;
	@Wire
	private Combobox cmbEstatusReporteProfesorTeg;
	@Wire
	private Combobox cmbProgramaReporteProfesorTeg;
	@Wire
	private Combobox cmbAreaReporteProfesorTeg;
	@Wire
	private Combobox cmbTematicaReporteProfesorTeg;
	private String[] estatusProyecto = { "Todos", "Solicitando Registro",
			"Proyecto Registrado", "Comision Asignada",
			"Factibilidad Evaluada", "Proyecto Factible",
			"Proyecto No Factible", "Avances Finalizados", "TEG Registrado",
			"Revisiones Finalizadas", "Solicitando Defensa",
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbProgramaReporteProfesorTeg.setModel(new ListModelList<Programa>(
				programas));
		cmbEstatusReporteProfesorTeg.setModel(new ListModelList<String>(
				estatusProyecto));

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula((String) map.get("cedula"));
				txtCedulaReporteProfesorTeg.setValue(profesor.getCedula());
				if (map.get("area").equals(estatusProyecto[0])) {
					cmbProgramaReporteProfesorTeg.setValue((String) map
							.get("programa"));
					cmbAreaReporteProfesorTeg
							.setValue((String) map.get("area"));
					cmbTematicaReporteProfesorTeg.setValue((String) map
							.get("tematica"));
				} else {
					idTematica = (Long) map.get("tematica");

					Programa programa = servicioPrograma.buscar((Long) map
							.get("programa"));
					Tematica tematica = servicioTematica
							.buscarTematica((Long) map.get("tematica"));
					cmbProgramaReporteProfesorTeg
							.setValue(programa.getNombre());
					cmbAreaReporteProfesorTeg
							.setValue((String) map.get("area"));
					cmbTematicaReporteProfesorTeg
							.setValue(tematica.getNombre());
				}
			}
			map.clear();
			map = null;
		}
	}

	@Listen("onSelect = #cmbProgramaReporteProfesorTeg")
	public void seleccinarPrograma() {
		cmbAreaReporteProfesorTeg.setValue("");
		cmbTematicaReporteProfesorTeg.setValue("");
		if (cmbProgramaReporteProfesorTeg.getValue().equals(estatusProyecto[0])) {
			cmbAreaReporteProfesorTeg.setValue("Todos");
			cmbTematicaReporteProfesorTeg.setValue("Todos");
		} else {
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long.parseLong(cmbProgramaReporteProfesorTeg
							.getSelectedItem().getId())));
			cmbAreaReporteProfesorTeg
					.setModel(new ListModelList<AreaInvestigacion>(areas));
		}
	}

	@Listen("onSelect = #cmbAreaReporteProfesorTeg")
	public void seleccionarArea() {
		cmbTematicaReporteProfesorTeg.setValue("");
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(Long.parseLong(cmbAreaReporteProfesorTeg
						.getSelectedItem().getId())));
		cmbTematicaReporteProfesorTeg.setModel(new ListModelList<Tematica>(
				tematicas));
	}

	@Listen("onSelect = #cmbTematicaReporteProfesorTeg")
	public void seleccionarTematica() {
		idTematica = Long.parseLong(cmbTematicaReporteProfesorTeg
				.getSelectedItem().getId());
	}

	@Listen("onClick = #btnProfesorReporteProfesorTeg")
	public void buscarProfesor() {

		final HashMap<String, Object> map2 = new HashMap<String, Object>();
		if (cmbProgramaReporteProfesorTeg.getValue().equals(estatusProyecto[0])) {
			map2.put("area", cmbAreaReporteProfesorTeg.getValue());
			map2.put("programa", cmbProgramaReporteProfesorTeg.getValue());
			map2.put("tematica", cmbTematicaReporteProfesorTeg.getValue());
			Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
			window.doModal();

			catalogoProfesor.recibir("reportes/no estructurados/VReporteProfesorTeg");
		} else {
			map2.put("area", cmbAreaReporteProfesorTeg.getValue());
			Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
			catalogo.recibir("reportes/no estructurados/VReporteProfesorTeg", Long
					.parseLong(cmbProgramaReporteProfesorTeg.getSelectedItem()
							.getId()), Long
					.parseLong(cmbTematicaReporteProfesorTeg.getSelectedItem()
							.getId()));

			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesorTematica.zul", null,
					null);
			window.doModal();
		}

	}

	@Listen("onClick = #btnGenerarReporteProfesorTeg")
	public void generarReporteProfesorTeg() throws JRException {
		String cedula = txtCedulaReporteProfesorTeg.getValue();
		Date fechaInicio = dtbInicioReporteProfesorTeg.getValue();
		Date fechaFin = dtbFinReporteProfesorTeg.getValue();
		String estatus = cmbEstatusReporteProfesorTeg.getValue();
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedula);
		List<Teg> tegs = new ArrayList<Teg>();
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			if (estatus.equals(estatusProyecto[0])) {
				System.out.println(cmbTematicaReporteProfesorTeg.getValue());
				if (cmbTematicaReporteProfesorTeg.getValue().equals(estatusProyecto[0])) {
					tegs= servicioTeg.buscarTodosTegsDeTutorPorDosFechas(profesor,fechaInicio, fechaFin);
					System.out.println("Todos"+tegs.size());
				} else {
					tegs = servicioTeg.buscarTegsDeTutorPorDosFechasYTematica(profesor,
							tematica, fechaInicio, fechaFin);
					System.out.println("Todos estatus una tematica"+tegs.size());
				}
			} else {
				if (cmbTematicaReporteProfesorTeg.getValue().equals(estatusProyecto[0])) {
					tegs= servicioTeg.buscarTegsDeTutorPorDosFechasYEstatus(profesor, estatus, fechaInicio, fechaFin);
					System.out.println("Todas tematicas un estatus"+tegs.size());
				} else {
					tegs = servicioTeg.buscarTegsDeTutorPorTematicaPorDosFechasYEstatus(
							profesor, tematica, estatus, fechaInicio, fechaFin);
					System.out.println("Una tematica un estatus"+tegs.size());
				}
			}
			FileSystemView filesys = FileSystemView.getFileSystemView();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("titulo", "UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
					+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
					+ "DIRECCION DE PROGRAMA");
			p.put("cedula", profesor.getCedula());
			p.put("nombre", profesor.getNombre());
			p.put("apellido", profesor.getApellido());
			p.put("fecha1", fechaInicio);
			p.put("fecha2", fechaFin);
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(getClass().getResource(
							"/reporte/RProyectosProfesor.jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, p, new JRBeanCollectionDataSource(tegs));
			JasperExportManager.exportReportToPdfFile(jasperPrint, filesys
					.getHomeDirectory().toString() + "/reporte.pdf");

		}
		
	}

}

