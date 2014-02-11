package controlador.reporte;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;
import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Teg;

import modelo.Programa;

import modelo.Tematica;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Controller;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

import org.zkoss.zul.Window;
import configuracion.GeneradorBeans;
import controlador.CGeneral;
import servicio.SAreaInvestigacion;
import servicio.STeg;
import servicio.STematica;

import servicio.SPrograma;

import servicio.SDefensa;

import modelo.reporte.DefensaTeg;
import modelo.reporte.Proyecto;

@Controller
public class CReporteSolicitud extends CGeneral {

	public CReporteSolicitud() {
		super();
		// TODO Auto-generated constructor stub
	}

	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();

	@Wire
	private Window wdwReporteSolicitud;
	@Wire
	private Radiogroup rdgTutoria;
	@Wire
	private Radio rdoTutoria;
	@Wire
	private Radio rdoTEG;
	@Wire
	private Radio rdoProyecto;
	@Wire
	private Radio rdoDefensa;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;

	@Wire
	private Jasperreport jstVistaPrevia;
	@Wire
	private Listbox ltbElegíPrograma;
	@Wire
	private Listbox ltbElegíProgramaArea;
	@Wire
	private Listbox ltbTodo;
	@Wire
	private Listbox ltbElegíArea;

	@Wire
	private Listbox ltbElegíProgramaAreaTematica;

	@Wire
	private Listbox ltbElegíAreaTematica;

	static Programa programa1 = new Programa();
	static AreaInvestigacion area1 = new AreaInvestigacion();
	private static String estatusDefensa = "Solicitando Defensa";
	private static String estatusProyecto = "Solicitando Registro";
	private static String estatusSolicitud = "Por Revisar";
	private static String estatusTeg = "TEG Registrado";
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	private static int chequeoEstatus=0;
	static List<Proyecto> elementos1 = new ArrayList<Proyecto>();
	//static List<DefensaTeg> elementos1 = new ArrayList<DefensaTeg>();

	@Override
	public void inicializar(Component comp) {
		ltbElegíArea.setVisible(false);

		ltbElegíPrograma.setVisible(false);
		ltbElegíProgramaArea.setVisible(false);
		ltbElegíProgramaAreaTematica.setVisible(false);
		ltbElegíProgramaAreaTematica.setVisible(false);
		ltbElegíAreaTematica.setVisible(false);
		ltbTodo.setVisible(false);
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas = servicioPrograma.buscarActivas();
		programas.add(programaa);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));

	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() {
		if ((rdoDefensa.isChecked() == false)
				&& (rdoTutoria.isChecked() == false)
				&& (rdoProyecto.isChecked() == false)
				&& (rdoTEG.isChecked() == false)) {
			Messagebox
					.show("Debe Seleccionar el tipo de solicitud que quiere consultar",
							"Error", Messagebox.OK, Messagebox.INFORMATION);
		} else if (cmbPrograma.getValue().equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
			areas = servicioArea.buscarActivos();
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		} else {
			cmbArea.setValue("");
			cmbTematica.setValue("");
			programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(programa1.getId()));
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

		}
	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		if (cmbArea.getValue().equals("Todos")) {

			cmbTematica.setValue("Todos");
			tematicas = servicioTematica.buscarActivos();
			Tematica tema = new Tematica(10000, "Todos", "", true);
			tematicas.add(tema);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		} else {
			cmbTematica.setValue("");
			area1 = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(area1.getId()));
			Tematica tema = new Tematica(10000, "Todos", "", true);
			tematicas.add(tema);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		}
	}

	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		if (cmbArea.getValue().equals("Todos")) {
			Tematica tematica = (Tematica) cmbTematica.getSelectedItem()
					.getValue();
			cmbArea.setValue(tematica.getareaInvestigacion().getNombre());
			System.out.println("pase por el todo");

		}
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
	}

	@Listen("onClick = #btnGenerar")
	public void generarReporteDefensa() throws JRException {
		ltbElegíArea.setVisible(false);

		ltbElegíPrograma.setVisible(false);
		ltbElegíProgramaArea.setVisible(false);
		ltbElegíProgramaAreaTematica.setVisible(false);
		ltbElegíProgramaAreaTematica.setVisible(false);
		ltbElegíAreaTematica.setVisible(false);
		ltbTodo.setVisible(false);

		if ((rdoDefensa.isChecked() == false)
				&& (rdoTutoria.isChecked() == false)
				&& (rdoProyecto.isChecked() == false)
				&& (rdoTEG.isChecked() == false)) {
			Messagebox
					.show("Debe Seleccionar el tipo de solicitud que quiere consultar",
							"Error", Messagebox.OK, Messagebox.INFORMATION);
		}
		if (rdoTutoria.isChecked() == true) {
			ltbElegíArea.setVisible(false);
			ltbElegíPrograma.setVisible(false);
			ltbElegíProgramaArea.setVisible(false);
			ltbElegíProgramaAreaTematica.setVisible(false);

			ltbTodo.setVisible(false);

		}

		/*
		 * Solicitudes de Proyecto o teg
		 */
		else if ((rdoProyecto.isChecked() == true) || (rdoTEG.isChecked() == true)|| (rdoDefensa.isChecked()== true)) {
			String nombreArea = cmbArea.getValue();
			String nombrePrograma = cmbPrograma.getValue();
			String nombreTematica = cmbTematica.getValue();
			Date fechaInicio = dtbDesde.getValue();
			Date fechaFin = dtbHasta.getValue();

			Tematica tematica1 = servicioTematica.buscarTematica(idTematica);

			List<Teg> teg = null;
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				Messagebox
						.show("La fecha de inicio debe ser primero que la fecha de fin",
								"Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				/*
				 * SELECIONO TODO TODO
				 */
				if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {
					chequeoEstatus=6;

					if(rdoProyecto.isChecked() == true)
					{
						teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
								estatusProyecto, fechaInicio, fechaFin);
					}
					else 
						if(rdoTEG.isChecked() == true)
					{
						teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
								estatusTeg, fechaInicio, fechaFin);
					}
					else if(rdoDefensa.isChecked() == true)
						{
							teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
									estatusDefensa, fechaInicio, fechaFin);
						}
						
					
					if (teg.size() != 0) {
						elementos1.clear();
						for (Teg t : teg) {
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarEstudiantePorTeg(t);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String titulo = t.getTitulo();
							String area = t.getTematica()
									.getareaInvestigacion().getNombre();
							String tematica = t.getTematica().getNombre();
							String nombreTutor = t.getTutor().getNombre() + " "
									+ t.getTutor().getApellido() + " ";
							elementos1.add(new Proyecto(titulo,
									nombreEstudiante, nombreTutor, programa,
									tematica, area));

						}
						ltbTodo.setModel(new ListModelList<Proyecto>(elementos1));
						ltbTodo.setVisible(true);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * seleccione un programa
				 */
				else if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {
					chequeoEstatus=1;
					if(rdoProyecto.isChecked() == true)
					{
						teg = servicioTeg.buscarTegPorProgramaUnEstatus(
								estatusProyecto, programa1, fechaInicio, fechaFin);
					}
					else
					if(rdoTEG.isChecked() == true)
					{
						teg = servicioTeg.buscarTegPorProgramaUnEstatus(
								estatusTeg, programa1, fechaInicio, fechaFin);
					}
					else if(rdoDefensa.isChecked() == true)
						{
						teg = servicioTeg.buscarTegPorProgramaUnEstatus(
								estatusDefensa, programa1, fechaInicio, fechaFin);
						}

					
					if (teg.size() != 0) {
						elementos1.clear();
						for (Teg t : teg) {
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarEstudiantePorTeg(t);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String titulo = t.getTitulo();
							String area = t.getTematica()
									.getareaInvestigacion().getNombre();
							String tematica = t.getTematica().getNombre();
							String nombreTutor = t.getTutor().getNombre() + " "
									+ t.getTutor().getApellido() + " ";
							elementos1.add(new Proyecto(titulo,
									nombreEstudiante, nombreTutor, programa,
									tematica, area));

						}
						ltbElegíPrograma.setModel(new ListModelList<Proyecto>(
								elementos1));
						ltbElegíPrograma.setVisible(true);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * Elijo uno de cada cosa
				 */
				else if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")) {
					chequeoEstatus=3;
					tematica1 = servicioTematica
							.buscarTematicaPorNombre(nombreTematica);
					if(rdoProyecto.isChecked() == true)
					{
						teg = servicioTeg
								.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
										estatusProyecto, tematica1, fechaInicio,
										fechaFin);
					}
					else
					if(rdoTEG.isChecked() == true)
					{
						teg = servicioTeg
								.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
										estatusTeg, tematica1, fechaInicio,
										fechaFin);
					}
					else if(rdoDefensa.isChecked() == true)
						{
						teg = servicioTeg
								.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
										estatusDefensa, tematica1, fechaInicio,
										fechaFin);
						}

					
					
					if (teg.size() != 0) {
						elementos1.clear();
						for (Teg t : teg) {
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarEstudiantePorTeg(t);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String titulo = t.getTitulo();
							String area = t.getTematica()
									.getareaInvestigacion().getNombre();
							String tematica = t.getTematica().getNombre();
							String nombreTutor = t.getTutor().getNombre() + " "
									+ t.getTutor().getApellido() + " ";
							elementos1.add(new Proyecto(titulo,
									nombreEstudiante, nombreTutor, programa,
									tematica, area));

						}
						ltbElegíProgramaAreaTematica
								.setModel(new ListModelList<Proyecto>(elementos1));
						ltbElegíProgramaAreaTematica.setVisible(true);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}
				/*
				 * Eligió programa area pero todos las tematicas
				 */
				else if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {
					chequeoEstatus=2;
					if(rdoProyecto.isChecked() == true)
					{
						teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
								fechaInicio, fechaFin, estatusProyecto);
					}
					else
					
					if(rdoTEG.isChecked() == true)
					{
						teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
								fechaInicio, fechaFin, estatusTeg);
					}
					else if(rdoDefensa.isChecked() == true)
						{
						teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
								fechaInicio, fechaFin, estatusDefensa);
						}
					
					if (teg.size() != 0) {
						elementos1.clear();
						for (Teg t : teg) {
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarEstudiantePorTeg(t);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String titulo = t.getTitulo();
							String area = t.getTematica()
									.getareaInvestigacion().getNombre();
							String tematica = t.getTematica().getNombre();
							String nombreTutor = t.getTutor().getNombre() + " "
									+ t.getTutor().getApellido() + " ";
							elementos1.add(new Proyecto(titulo,
									nombreEstudiante, nombreTutor, programa,
									tematica, area));

						}
						ltbElegíProgramaArea
								.setModel(new ListModelList<Proyecto>(elementos1));
						ltbElegíProgramaArea.setVisible(true);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}

				}
				/*
				 * Eligió todo los programas una area y todos los tematicos
				 */

				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {
					chequeoEstatus=4;
					if(rdoProyecto.isChecked() == true)
					{
						teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
								fechaInicio, fechaFin, estatusProyecto);
					}
					else
					
					if(rdoTEG.isChecked() == true)
					{

						teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
								fechaInicio, fechaFin, estatusTeg);
					}
					else if(rdoDefensa.isChecked() == true)
						{

						teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
								fechaInicio, fechaFin, estatusProyecto);
						}
					
					if (teg.size() != 0) {
						elementos1.clear();
						for (Teg t : teg) {
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarEstudiantePorTeg(t);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String titulo = t.getTitulo();
							String area = t.getTematica()
									.getareaInvestigacion().getNombre();
							String tematica = t.getTematica().getNombre();
							String nombreTutor = t.getTutor().getNombre() + " "
									+ t.getTutor().getApellido() + " ";
							elementos1.add(new Proyecto(titulo,
									nombreEstudiante, nombreTutor, programa,
									tematica, area));

						}
						ltbElegíProgramaArea
								.setModel(new ListModelList<Proyecto>(elementos1));
						ltbElegíProgramaArea.setVisible(true);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}

				
				}
				/*
				 * Eligio todo los programas una area y una tematica
				 */
				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")) {
					chequeoEstatus=5;
					tematica1 = servicioTematica
							.buscarTematicaPorNombre(nombreTematica);
					if(rdoProyecto.isChecked() == true)
					{
						teg = servicioTeg
								.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
										estatusProyecto, tematica1, fechaInicio,
										fechaFin);
					}
					else
					
					if(rdoTEG.isChecked() == true)
					{

						teg = servicioTeg
								.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
										estatusTeg, tematica1, fechaInicio,
										fechaFin);
					}
					else if(rdoDefensa.isChecked() == true)
						{

						teg = servicioTeg
								.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
										estatusDefensa, tematica1, fechaInicio,
										fechaFin);
						}
					
					if (teg.size() != 0) {
						elementos1.clear();
						for (Teg t : teg) {
							List<Estudiante> estudiantes = servicioEstudiante
									.buscarEstudiantePorTeg(t);

							String programa = "";
							String nombreEstudiante = "";
							for (Estudiante e : estudiantes) {
								nombreEstudiante = e.getNombre() + " "
										+ e.getApellido() + " ";
								programa = e.getPrograma().getNombre();
							}

							String titulo = t.getTitulo();
							String area = t.getTematica()
									.getareaInvestigacion().getNombre();
							String tematica = t.getTematica().getNombre();
							String nombreTutor = t.getTutor().getNombre() + " "
									+ t.getTutor().getApellido() + " ";
							elementos1.add(new Proyecto(titulo,
									nombreEstudiante, nombreTutor, programa,
									tematica, area));

						}
						ltbElegíAreaTematica
								.setModel(new ListModelList<Proyecto>(elementos1));
						ltbElegíAreaTematica.setVisible(true);

					} else {
						Messagebox
								.show("No ha informacion disponible para este intervalo");
					}
				}

			}

		

		}

	}

	@Listen("onClick = #btnSalir")
	public void cancelarTematicasSolicitadas() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbDesde.setValue(new Date());
		dtbHasta.setValue(new Date());
	}
	

	// *************************
	// EXPORTAR DOCUMENTO
	// *************************
	void exportarDocumento() throws IOException {
		System.out.println(elementos1.size());
		if (elementos1.size() != 0) {
			System.out.println("psooo22");
			String rutaUrl = obtenerDirectorio();
			System.out.println(rutaUrl);
			String ruta = "/C:/Users/JOSE/workspace/SITEG/WebContent/vistas/reportes/no-estructurados/";
			String sFichero = "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" + "_"
					+ "hhmmss");
			Calendar cal = Calendar.getInstance(); // hoy
			String valor = sdf.format(cal.getTime());

			// *****************************************
			// EXPORTAR RESULTADOS DE SOLICITUDES DE DEFENSA
			// *****************************************
		
				sFichero = ruta + "SolicitudesDefensa.txt";
				File fichero = new File(sFichero);
				if (chequeoEstatus == 1) {
					// Elegi Programa
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));
						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getArea());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", "SolicitudesDefensa_"
									+ valor + ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 2) {
					// Elegi Programa y area
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", "SolicitudesDefensa_"
									+ valor + ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 3) {
					// Elegi Programa, area y tematica
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TUTOR");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", "SolicitudesDefensa_"
									+ valor + ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 4) {
					// ELEGI AREA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", "SolicitudesDefensa_"
									+ valor + ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 5) {
					// ELEGI AREA Y TEMATICA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getPrograma());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", "SolicitudesDefensa_"
									+ valor + ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (chequeoEstatus == 6) {
					// ELEGI NADA

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								sFichero));

						StringBuilder linea2 = new StringBuilder();
						linea2.append("[");
						linea2.append("TITULO");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("ESTUDIANTE");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TUTOR");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("PROGRAMA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("AREA");
						linea2.append("\t");
						linea2.append("\t");
						linea2.append("TEMATICA");
						linea2.append("]");
						bw.write(linea2.toString());
						bw.newLine();
						bw.newLine();

						for (int x = 0; x < elementos1.size(); x++) {
							StringBuilder linea = new StringBuilder();
							linea.append("[");
							linea.append(elementos1.get(x).getTituloTeg());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreEstudiante());
							linea.append("\t");
							linea.append(elementos1.get(x).getNombreTutor());
							linea.append("\t");
							linea.append(elementos1.get(x).getPrograma());
							linea.append("\t");
							linea.append(elementos1.get(x).getArea());
							linea.append("\t");
							linea.append(elementos1.get(x).getTematica());
							linea.append("]");
							linea.append("\t");

							bw.write(linea.toString());
							bw.newLine();
						}
						bw.close();

						java.io.InputStream is = new FileInputStream(ruta
								+ "SolicitudesDefensa.txt");
						if (is != null)
							Filedownload.save(is, "text", "SolicitudesDefensa_"
									+ valor + ".txt");

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}
			} else if (rdoProyecto.isChecked() == true) {

			}

		}


	@Listen("onClick= #btnExportarPlano")
	public void exportarReporte() {
		try {
			
			exportarDocumento();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	

}
