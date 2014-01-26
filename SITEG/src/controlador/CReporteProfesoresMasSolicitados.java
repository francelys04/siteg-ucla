package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Programa;
import modelo.SolicitudTutoria;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SProfesor;
import servicio.SPrograma;

@Controller
public class CReporteProfesoresMasSolicitados extends CGeneral {
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	@Wire
	private Window wdwReporteProfesorMasSolicitados;
	@Wire
	private Datebox dtbInicioReporteProfesoresSolicitados;
	@Wire
	private Datebox dtbFinReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbProgramaReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbAreaReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbTematicaReporteProfesoresSolicitados;
	@Wire
	private Combobox cmbSolicitudesReporteProfesoresSolicitados;
	private String[] estatusSolicitud = {"Aprobadas", "Rechazadas","Por Revisar","Todos"};
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(10000000, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbSolicitudesReporteProfesoresSolicitados.setModel(new ListModelList<String>(
				estatusSolicitud));
		cmbProgramaReporteProfesoresSolicitados.setModel(new ListModelList<Programa>(
				programas));
		Selectors.wireComponents(comp, this, false);
	}
	
	@Listen("onSelect = #cmbProgramaReporteProfesoresSolicitados")
	public void buscarArea(){
		cmbAreaReporteProfesoresSolicitados.setValue("");
		cmbTematicaReporteProfesoresSolicitados.setValue("");
		if (cmbProgramaReporteProfesoresSolicitados.getValue().equals(estatusSolicitud[3])) {
			cmbAreaReporteProfesoresSolicitados.setValue("Todos");
			cmbTematicaReporteProfesoresSolicitados.setValue("Todos");
		} else {
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long.parseLong(cmbProgramaReporteProfesoresSolicitados
							.getSelectedItem().getId())));
			System.out.println(areas.toString());
			cmbAreaReporteProfesoresSolicitados
					.setModel(new ListModelList<AreaInvestigacion>(areas));
		}
	}
	
	@Listen("onSelect = #cmbAreaReporteProfesoresSolicitados")
	public void seleccionarTematica() {
		cmbTematicaReporteProfesoresSolicitados.setValue("");
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(Long.parseLong(cmbAreaReporteProfesoresSolicitados
						.getSelectedItem().getId())));
		cmbTematicaReporteProfesoresSolicitados.setModel(new ListModelList<Tematica>(
				tematicas));
	}
	
	@Listen("onSelect = #cmbTematicaReporteProfesoresSolicitados")
	public void tomarIdTematica(){
		idTematica =Long.parseLong(cmbTematicaReporteProfesoresSolicitados.getSelectedItem().getId());
	}
	
	@Listen("onClick = #btnGenerarReporteProfesoresSolicitados")
	public void generarReporte() throws JRException{
		Date fechaInicio = dtbInicioReporteProfesoresSolicitados.getValue();
		Date fechaFin = dtbFinReporteProfesoresSolicitados.getValue();
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		String estatus = cmbSolicitudesReporteProfesoresSolicitados.getValue();
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			if (estatus.equals(estatusSolicitud[3])) {
				if (cmbTematicaReporteProfesoresSolicitados.getValue().equals(estatusSolicitud[3])) {
//					todos todos
					solicitudes = servicioSolicitudTutoria.buscarTodasSolicitudesEntreFechas(fechaInicio, fechaFin);
					System.out.println("Todos"+solicitudes.size());
				} else {
//					todos estatus una tematica
					solicitudes = servicioSolicitudTutoria.buscarSolicitudesPorTematicaEntreFechas(tematica,fechaInicio, fechaFin);
					System.out.println("Todos estatus una tematica"+solicitudes.size());
				}
			} else {
				if (cmbTematicaReporteProfesoresSolicitados.getValue().equals(estatusSolicitud[3]))  {
//					todos tematicas un estatus
					solicitudes = servicioSolicitudTutoria.buscarSolicitudesPorEstatusEntreFechas(estatus, fechaInicio, fechaFin);
					System.out.println("Todas tematicas un estatus"+solicitudes.size());
				} else {
//					un estatus una tematica
					solicitudes = servicioSolicitudTutoria.buscarSolicitudesPorTematicaEstatusEntreFechas(tematica, estatus, fechaInicio, fechaFin);
					System.out.println("Una tematica un estatus"+solicitudes.size());
				}
			}
			
			FileSystemView filesys = FileSystemView.getFileSystemView();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("titulo", "UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
					+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
					+ "DIRECCION DE PROGRAMA");
			p.put("fecha1", fechaInicio);
			p.put("fecha2", fechaFin);
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(getClass().getResource(
							"/reporte/RProfesoresMasSolicitados.jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, p, new JRBeanCollectionDataSource(solicitudes));
			JasperExportManager.exportReportToPdfFile(jasperPrint, filesys
					.getHomeDirectory().toString() + "/reporteGrafica.pdf");
			
		}
	}

}
