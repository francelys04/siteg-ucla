package controlador.reporte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.ListaTeg;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
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
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SEstudiante;

@Controller
public class CReporteTeg extends CGeneral {

	private String[] estatusTeg = { "Todos", "Solicitando Registro",
			"Proyecto Registrado", "Comision Asignada",
			"Factibilidad Evaluada", "Proyecto Factible",
			"Proyecto No Factible", "Proyecto en Desarrollo",
			"Avances Finalizados", "TEG Registrado", "Trabajo en Desarrollo",
			"Revisiones Finalizadas", "Solicitando Defensa", "Jurado Asignado",
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;

	@Wire
	private Window wdwReporteTeg;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Jasperreport jstVistaPrevia;
	private static Programa programa1;
	private static AreaInvestigacion area1;
	private static long idarea;
	private static Date fechaInicio;
	private static Date fechaFin;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos", junto a esto se tiene una lista
	 * previamente cargada de manera estatica, los estatus de teg y se llena una
	 * lista del mismo en el componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);

		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(
				estatusTeg));

		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);

	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		try {
			if (cmbPrograma.getValue().equals("Todos")) {

				areas = servicioArea.buscarActivos();
				AreaInvestigacion area = new AreaInvestigacion(10000000,
						"Todos", "", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
				cmbArea.setDisabled(false);

			} else {

				cmbArea.setDisabled(false);
				cmbArea.setValue("");
				cmbTematica.setValue("");
				programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
				areas = servicioProgramaArea
						.buscarAreasDePrograma(servicioPrograma
								.buscar(programa1.getId()));
				AreaInvestigacion area = new AreaInvestigacion(10000000,
						"Todos", "", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
	}

	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		try {
			if (cmbArea.getValue().equals("Todos")) {

				cmbTematica.setValue("Todos");
				cmbTematica.setDisabled(true);
				cmbEstatus.setDisabled(false);

			} else {

				cmbTematica.setDisabled(false);
				cmbTematica.setValue("");
				area1 = (AreaInvestigacion) cmbArea.getSelectedItem()
						.getValue();
				tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
						.buscarArea(area1.getId()));
				Tematica tema = new Tematica(10000, "Todos", "", true, null);
				tematicas.add(tema);
				cmbTematica.setModel(new ListModelList<Tematica>(tematicas));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
		idarea = Long.parseLong(cmbArea.getSelectedItem().getId());
	}

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
		cmbEstatus.setDisabled(false);
	}

	

	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y unos estatus previamente establecidos, se generara un pdf donde se
	 * muestra una lista de tegs de esta seleccion, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteTeg")
	public void generarReporteTEG() throws JRException {

		if ((cmbPrograma.getText().compareTo("") == 0)
				|| (cmbArea.getText().compareTo("") == 0)
				|| (cmbTematica.getText().compareTo("") == 0)
				|| (cmbEstatus.getText().compareTo("") == 0)
				|| (dtbFechaInicio.getValue() == null)
				|| (dtbFechaFin.getValue() == null)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			fechaInicio = dtbFechaInicio.getValue();
			fechaFin = dtbFechaFin.getValue();

			if (fechaInicio.after(fechaFin)) {

				Messagebox
						.show("La fecha de fin debe ser posterior a la fecha de inicio",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				String nombreArea = cmbArea.getValue();
				String nombrePrograma = cmbPrograma.getValue();
				String nombreTematica = cmbTematica.getValue();
				Date fechaInicio = dtbFechaInicio.getValue();
				Date fechaFin = dtbFechaFin.getValue();
				String estatus = cmbEstatus.getValue();
				List<Teg> teg = null;

				/*
				 * buscar por una carrera, un area, una tematica y un estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todas")
						&& !estatus.equals("Todos")) {
					String idTematica = cmbTematica.getSelectedItem().getId();
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
					teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus, tematica1, fechaInicio, fechaFin);

				}

				/*
				 * buscar por una carrera, un area, todas las tematica y un
				 * estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todas")
						&& !estatus.equals("Todos")) {

					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					teg = servicioTeg.buscarTegPorDosFechasyUnEstatus(area1,
							estatus, fechaInicio, fechaFin);

				}

				/*
				 * buscar por una carrera, un area, todas las tematica y todos
				 * los estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todas")
						&& estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					teg = servicioTeg.buscarTegPorDosFechasyArea(area1,
							fechaInicio, fechaFin);
				}

				/*
				 * buscar por una carrera, un area, una tematica y todos los
				 * estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todas")
						&& estatus.equals("Todos")) {
					String idTematica = cmbTematica.getSelectedItem().getId();
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
					
								
					
					String estatusTeg1 = "Solicitando Registro";
					String estatusTeg2 = "Proyecto Registrado";
					String estatusTeg3 = "Comision Asignada";
					String estatusTeg4 = "Factibilidad Evaluada";
					String estatusTeg5 = "Proyecto Factible";
					String estatusTeg6 = "Proyecto No Factible";
					String estatusTeg7 = "Proyecto en Desarrollo";
					String estatusTeg8 = "Avances Finalizados";
					String estatusTeg9 = "TEG Registrado";
					String estatusTeg10 = "Trabajo en Desarrollo";
					String estatusTeg11 = "Revisiones Finalizadas";
					String estatusTeg12 = "Solicitando Defensa";
					String estatusTeg13 = "Defensa Asignada";
					String estatusTeg14 = "TEG Aprobado";
					String estatusTeg15 = "TEG Reprobado";
					String estatusTeg16 = "Jurado Asignado";
					teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyVariosEstatus(
									estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, tematica1, fechaInicio,
									fechaFin);
				}

				/* buscar por una carrera, todas las area y un estatus */
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idPrograma = cmbPrograma.getSelectedItem().getId();
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					teg = servicioTeg.buscarTegPorProgramaVariasAreasUnEstatus(
							estatus, programa1, fechaInicio, fechaFin);
				}

				/*
				 * buscar por una carrera, todas las area y todos los estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& estatus.equals("Todos")) {
					String idPrograma = cmbPrograma.getSelectedItem().getId();
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					String estatusTeg1 = "Solicitando Registro";
					String estatusTeg2 = "Proyecto Registrado";
					String estatusTeg3 = "Comision Asignada";
					String estatusTeg4 = "Factibilidad Evaluada";
					String estatusTeg5 = "Proyecto Factible";
					String estatusTeg6 = "Proyecto No Factible";
					String estatusTeg7 = "Proyecto en Desarrollo";
					String estatusTeg8 = "Avances Finalizados";
					String estatusTeg9 = "TEG Registrado";
					String estatusTeg10 = "Trabajo en Desarrollo";
					String estatusTeg11 = "Revisiones Finalizadas";
					String estatusTeg12 = "Solicitando Defensa";
					String estatusTeg13 = "Defensa Asignada";
					String estatusTeg14 = "TEG Aprobado";
					String estatusTeg15 = "TEG Reprobado";
					String estatusTeg16 = "Jurado Asignado";
					teg = servicioTeg
							.buscarTegPorProgramaVariasAreasVariosEstatus(
									estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, programa1, fechaInicio,
									fechaFin);
				}

				/* buscar por todas las carrera, y un estatus */
				if (nombrePrograma.equals("Todos") && !estatus.equals("Todos")) {
					teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
							estatus, fechaInicio, fechaFin);
				}

				/* buscar por todas las carrera, y todos los estatus */
				if (nombrePrograma.equals("Todos") && estatus.equals("Todos")) {
					String estatusTeg1 = "Solicitando Registro";
					String estatusTeg2 = "Proyecto Registrado";
					String estatusTeg3 = "Comision Asignada";
					String estatusTeg4 = "Factibilidad Evaluada";
					String estatusTeg5 = "Proyecto Factible";
					String estatusTeg6 = "Proyecto No Factible";
					String estatusTeg7 = "Proyecto en Desarrollo";
					String estatusTeg8 = "Avances Finalizados";
					String estatusTeg9 = "TEG Registrado";
					String estatusTeg10 = "Trabajo en Desarrollo";
					String estatusTeg11 = "Revisiones Finalizadas";
					String estatusTeg12 = "Solicitando Defensa";
					String estatusTeg13 = "Defensa Asignada";
					String estatusTeg14 = "TEG Aprobado";
					String estatusTeg15 = "TEG Reprobado";
					String estatusTeg16 = "Jurado Asignado";
					teg = servicioTeg.buscarTegPorVariosProgramasVariosEstatus(
							estatusTeg1, estatusTeg2, estatusTeg3, estatusTeg4,
							estatusTeg5, estatusTeg6, estatusTeg7, fechaInicio,
							fechaFin);
				}

				List<ListaTeg> elementos = new ArrayList<ListaTeg>();
				for (Teg t : teg) {
					List<Estudiante> estudiantes = servicioEstudiante
							.buscarEstudiantePorTeg(t);

					String nombreEstudiantes = "";
					for (Estudiante e : estudiantes) {
						nombreEstudiantes += e.getNombre() + " "
								+ e.getApellido() + " ";
					}

					elementos.add(new ListaTeg(t, nombreEstudiantes));

				}

				if (elementos.size() != 0) {
					FileSystemView filesys = FileSystemView.getFileSystemView();
					Map p = new HashMap();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl

							+ "SITEG/vistas/reportes/salidas/compilados/ReporteTEG.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
					p.put("programa", cmbPrograma.getValue());
					p.put("FechaInicio", dtbFechaInicio.getValue());
					p.put("FechaFin", dtbFechaFin.getValue());
					p.put("Area", cmbArea.getValue());
					p.put("Tematica", cmbTematica.getValue());
					p.put("Estatus", cmbEstatus.getValue());
					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									elementos));
					JasperViewer.viewReport(jasperPrint, false);

					// jstVistaPrevia.setSrc(reporteSrc);
					// jstVistaPrevia.setDatasource(new
					// JRBeanCollectionDataSource(
					// elementos));
					// jstVistaPrevia.setType("pdf");
					// jstVistaPrevia.setParameters(p);
				} else {
					Messagebox
							.show("No hay informacion disponible para esta seleccion",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);
				}

			}

		}
	}

	/* Metodo que permite limpiar los campos de los filtros de busqueda. */

	@Listen("onClick = #btnCancelarReporteTeg")
	public void Cancelar() {
		cmbEstatus.setValue("");
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);
		
	}

	/* Metodo que permite cerrar la vista del reporte */

	@Listen("onClick = #btnSalirReporteTeg")
	public void Salir() {
		wdwReporteTeg.onClose();

	}
}
