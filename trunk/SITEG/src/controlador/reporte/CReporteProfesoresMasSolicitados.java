package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.MasSolicitados;
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
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

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
	private Jasperreport jstVistaProfesores;
	private String[] estatusSolicitud = { "Aceptada", "Rechazada",
			"Por Revisar", "Finalizada" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(10000000, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbProgramaReporteProfesoresSolicitados
				.setModel(new ListModelList<Programa>(programas));
		Selectors.wireComponents(comp, this, false);
	}

	@Listen("onSelect = #cmbProgramaReporteProfesoresSolicitados")
	public void buscarArea() {
		cmbAreaReporteProfesoresSolicitados.setValue("");
		cmbTematicaReporteProfesoresSolicitados.setValue("");
		if (cmbProgramaReporteProfesoresSolicitados.getValue().equals("Todos")) {
			cmbAreaReporteProfesoresSolicitados.setValue("Todos");
			cmbTematicaReporteProfesoresSolicitados.setValue("Todos");
		} else {
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long
							.parseLong(cmbProgramaReporteProfesoresSolicitados
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
		cmbTematicaReporteProfesoresSolicitados
				.setModel(new ListModelList<Tematica>(tematicas));
	}

	@Listen("onSelect = #cmbTematicaReporteProfesoresSolicitados")
	public void tomarIdTematica() {
		idTematica = Long.parseLong(cmbTematicaReporteProfesoresSolicitados
				.getSelectedItem().getId());
	}

	@Listen("onClick = #btnGenerarReporteProfesoresSolicitados")
	public void generarReporte() throws JRException {
		boolean datosVacios = false;
		Date fechaInicio = dtbInicioReporteProfesoresSolicitados.getValue();
		Date fechaFin = dtbFinReporteProfesoresSolicitados.getValue();
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		List<SolicitudTutoria> solicitudesFinales = new ArrayList<SolicitudTutoria>();
		List<SolicitudTutoria> solicitudTutorias = new ArrayList<SolicitudTutoria>();
		List<Teg> tegs = new ArrayList<Teg>();
		List<String> profesores = new ArrayList<String>();
		List<Integer> contadores = new ArrayList<Integer>();
		List<MasSolicitados> masSolicitados = new ArrayList<MasSolicitados>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
				if (cmbTematicaReporteProfesoresSolicitados.getValue().equals(
						"Todos")) {
					// todos tematicas un estatus
					solicitudes = servicioSolicitudTutoria
							.buscarTodasSolicitudesEntreFechas(fechaInicio,
									fechaFin);
					if (solicitudes.size() == 0) {
						datosVacios = true;
					} else {
						System.out.println("Todas tematicas un estatus"
								+ solicitudes.size());
						map = ordenar(solicitudes);
						profesores = (List<String>) map.get("Profesores");
						contadores = (List<Integer>) map.get("Contadores");
						for (int i = 0; i < profesores.size(); i++) {
							Profesor profesor = servicioProfesor
									.buscarProfesorPorCedula(profesores.get(i));
							long valor = servicioSolicitudTutoria
									.contarSolicitudes(profesor);
							solicitudesFinales = servicioSolicitudTutoria.buscarPorProfesorEntreFechas(profesor, fechaInicio, fechaFin);
							long primerValor = 0; 
							long segundoValor = 0; 
							long tercerValor = 0;
							for(int j =0;j<solicitudesFinales.size();j++){
								if(solicitudesFinales.get(j).getEstatus().equals(estatusSolicitud[1]))
										primerValor ++;
								else{
									if(solicitudesFinales.get(j).getEstatus().equals(estatusSolicitud[2]))
										segundoValor ++;
										else 
											tercerValor ++;
								}
							
							
//							System.out.println("id" + valor);
//							System.out.println("duracion" + contadores.get(i));
//							Teg teg = new Teg(valor, "", null, null, null, "",
//									contadores.get(i), profesor, "", null,
//									null, null);
//							tegs.add(teg);
							}
							MasSolicitados masSolicita = new MasSolicitados(primerValor, segundoValor, tercerValor, profesor, null);
							masSolicitados.add(masSolicita);
						}
						System.out.println(masSolicitados.size());
					}
				} else {
					// un estatus una tematica
					solicitudes = servicioSolicitudTutoria
							.buscarSolicitudesPorTematicaEntreFechas(tematica,
									fechaInicio, fechaFin);
					if (solicitudes.size() == 0) {
						datosVacios = true;
					} else {
						System.out.println("Una tematica un estatus"
								+ solicitudes.size());
						map = ordenar(solicitudes);
						profesores = (List<String>) map.get("Profesores");
						contadores = (List<Integer>) map.get("Contadores");
						for (int i = 0; i < profesores.size(); i++) {
							Profesor profesor = servicioProfesor
									.buscarProfesorPorCedula(profesores.get(i));
							long valor = servicioSolicitudTutoria
									.contarSolicitudesPorTematica(profesor,
											tematica);
							solicitudesFinales = servicioSolicitudTutoria.buscarPorProfesorTematicaEntreFechas(profesor, tematica, fechaInicio, fechaFin);
							long primerValor = 0; 
							long segundoValor = 0; 
							long tercerValor = 0;
							for(int j =0;j<solicitudesFinales.size();j++){
								if(solicitudesFinales.get(j).getEstatus().equals(estatusSolicitud[1]))
										primerValor ++;
								else{
									if(solicitudesFinales.get(j).getEstatus().equals(estatusSolicitud[2]))
										segundoValor ++;
										else 
											tercerValor ++;
								}
							
							
//							System.out.println("id" + valor);
//							System.out.println("duracion" + contadores.get(i));
//							Teg teg = new Teg(valor, "", null, null, null, "",
//									contadores.get(i), profesor, "", null,
//									null, null);
//							tegs.add(teg);
							}
							MasSolicitados masSolicita = new MasSolicitados(primerValor, segundoValor, tercerValor, profesor, null);
							masSolicitados.add(masSolicita);
						}
						System.out.println(masSolicitados.size());

					}
				}
				if (!datosVacios) {
					for(int w=0;w<masSolicitados.size();w++){
						System.out.println("Primer"+masSolicitados.get(w).getPrimerValor());
						System.out.println("segundo"+masSolicitados.get(w).getSegundoValor());
						System.out.println("tercer"+masSolicitados.get(w).getTercerValor());
						System.out.println("tutor"+masSolicitados.get(w).getTutor().getCedula());
					}
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("inicio", fechaInicio);
					mapa.put("fin", fechaFin);
					if (cmbTematicaReporteProfesoresSolicitados.getValue()
							.equals("Todos"))
						mapa.put("tematica", "Todas las tematicas");
					else
						mapa.put("tematica", tematica.getNombre());
					if (cmbProgramaReporteProfesoresSolicitados.getValue()
							.equals("Todos"))
						mapa.put("programa", "Todos los Programas");
					else
						mapa.put("programa",
								cmbProgramaReporteProfesoresSolicitados
										.getValue());
					if (cmbAreaReporteProfesoresSolicitados.getValue().equals(
							"Todos"))
						mapa.put("area", "Todas las areas");
					else
						mapa.put("area",
								cmbAreaReporteProfesoresSolicitados.getValue());
					// Metodo utilizado para los que de error el preview
					 FileSystemView filesys =
					 FileSystemView.getFileSystemView();
					 JasperReport jasperReport = (JasperReport) JRLoader
					 .loadObject(getClass()
					 .getResource(
					 "/reporte/RProfesoresMasSolicitados.jasper"));
					 JasperPrint jasperPrint = JasperFillManager.fillReport(
					 jasperReport, mapa, new JRBeanCollectionDataSource(masSolicitados));
					 JasperExportManager.exportReportToPdfFile(jasperPrint,
					 filesys.getHomeDirectory().toString()
					 + "/reportePr.pdf");

//					String rutaUrl = obtenerDirectorio();
//					String reporteSrc = rutaUrl
//							+ "SITEG/vistas/reportes/estadisticos/compilados/RProfesoresMasSolicitados.jasper";
//					jstVistaProfesores.setSrc(reporteSrc);
//					jstVistaProfesores
//							.setDatasource(new JRBeanCollectionDataSource(masSolicitados));
//					jstVistaProfesores.setType("pdf");
//					jstVistaProfesores.setParameters(mapa);
				} else {
					Messagebox
							.show("No hay informacion disponible para esta selección");
				}
		}
	}

	public Map<String, Object> ordenar(List<SolicitudTutoria> solicitudes) {
		List<String> profesores = new ArrayList();
		List<Integer> contadores = new ArrayList();
		List<SolicitudTutoria> solicitudesTutoria = new ArrayList();
		SolicitudTutoria solicitud = solicitudes.get(0);
		String profesor = solicitudes.get(0).getProfesor().getCedula();

		int contadorProfesores = 0;
		for (int i = 0; i < solicitudes.size(); i++) {
			solicitud = solicitudes.get(i);
			if (profesor == solicitudes.get(i).getProfesor().getCedula()) {
				contadorProfesores = contadorProfesores + 1;
			} else {
				profesores.add(profesor);
				contadores.add(contadorProfesores);
				solicitudesTutoria.add(solicitud);
				solicitud = solicitudes.get(i);
				profesor = solicitudes.get(i).getProfesor().getCedula();
				contadorProfesores = 1;
			}
		}
		profesores.add(profesor);
		contadores.add(contadorProfesores);
		solicitudesTutoria.add(solicitud);
		/*************************** Ordenado de Lista ************* ******************/
		List<String> profesoresOrdenados = new ArrayList();
		List<Integer> contadoresOrdenados = new ArrayList();
		List<SolicitudTutoria> solicitudesFinales = new ArrayList();
		int valor = 0;
		int valor2 = 0;
		SolicitudTutoria solicit = new SolicitudTutoria();
		String valorTematica = "";
		for (int i = 0; i < contadores.size(); i++) {
			valor = contadores.get(i);
			for (int j = 0; j < contadores.size(); j++) {
				valor2 = contadores.get(j);
				valorTematica = profesores.get(j);
				solicit = solicitudesTutoria.get(j);
				if (valor2 > valor) {
					contadores.remove(j);
					profesores.remove(j);
					solicitudesTutoria.remove(j);
					contadoresOrdenados.add(valor2);
					profesoresOrdenados.add(valorTematica);
					solicitudesFinales.add(solicit);
					j = contadores.size();
				}
			}
		}
		if (contadoresOrdenados.size() < 5 && contadores.size() != 0) {
			int cantidadFaltante = 5 - contadoresOrdenados.size();
			for (int i = 0; i < cantidadFaltante; i++) {
				if (i < contadores.size()) {
					contadoresOrdenados.add(contadores.get(i));
					profesoresOrdenados.add(profesores.get(i));
					solicitudesFinales.add(solicitudesTutoria.get(i));
				}
			}
		}
		if (contadoresOrdenados.size() > 5) {
			for (int i = 5; i < contadoresOrdenados.size(); i++) {
				contadoresOrdenados.remove(i);
				profesoresOrdenados.remove(i);
				solicitudesFinales.remove(i);
			}
		}
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Profesores", profesoresOrdenados);
		map.put("Contadores", contadoresOrdenados);
		return map;
	}
}
