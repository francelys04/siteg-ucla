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
import modelo.reporte.Proyecto;
import modelo.compuesta.Jurado;
import modelo.reporte.ProfesorTeg;

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
import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoProfesorTematica;
import controlador.CGeneral;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SEstudiante;

@Controller
public class CReporteProfesor extends CGeneral{

	public CReporteProfesor() {
		super();
		// TODO Auto-generated constructor stub
	}

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	SProgramaArea servicioProgramaArea = GeneradorBeans.getServicioProgramaArea();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();

	@Wire
	private Window wdwReporteProfesor;
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

	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	private String[] estatusProfesor = { "Tutor", "Comision Evaluadora", "Jurado", "Todos" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	
	

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista, tambien se buscan todos los programas
	 * disponibles, adicionando un nuevo item donde se puede seleccionar la
	 * opcion de "Todos", junto a esto se tiene una lista previamente cargada de
	 * manera estatica los estatus o roles del profesor y se llenan los campos
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);

		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusProfesor));
	}
	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas.
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		if (cmbPrograma.getValue().equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todas");
		} else {
			cmbArea.setValue("");
			cmbTematica.setValue("");
			Programa programa = (Programa) cmbPrograma.getSelectedItem()
					.getValue();
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(programa.getId()));
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		}

	}
	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		if (cmbArea.getValue().equals("Todos")) {
			cmbTematica.setValue("Todas");
		} else {
			
			cmbTematica.setValue("");
			AreaInvestigacion area = (AreaInvestigacion) cmbArea
					.getSelectedItem().getValue();
			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(area.getId()));
			Tematica todastematica = new Tematica(10000000, "Todas",
					"", true, area);
			tematicas.add(todastematica);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		}
	}
	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		// idTematica = tematica.getId();
	}
	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y tipo de cargo, se generara un pdf donde se muestra una lista de
	 * profesores especificando tanto datos basicos como su rol en el teg de
	 * esta seleccion, la cual esta condicionado, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteProfesor")
	public void generarReporteTEG() throws JRException {
		
		boolean datosVacios = false;
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		Date fechaInicio = dtbFechaInicio.getValue();
		Date fechaFin = dtbFechaFin.getValue();
		String estatus = cmbEstatus.getValue();
		String tipoCargo = (String) cmbEstatus.getSelectedItem().getValue();
		List<ProfesorTeg> elementos = new ArrayList<ProfesorTeg>();

		/*Mensaje para dar cuando falta un dato*/
		if ((cmbPrograma.getValue() == "") || (cmbArea.getValue() == "")
				|| (cmbTematica.getValue() == "")
				|| (cmbEstatus.getValue() == "")) {
			Messagebox.show("Datos imcompletos", "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
		}

		else {
			/*Si las fechas estan malas*/
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				Messagebox
						.show("La fecha de inicio debe ser primero que la fecha de fin",
								"Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				/*buscar por una carrera, un area, una tematica y un rol*/
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todas")
						&& !estatus.equals("Todos")) {

		/*			String idTematica1 = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica1));
					
					List<Teg> teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechas(tematica1, fechaInicio, fechaFin);
					
					for (Teg tegs : teg) {
				 		
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Tutor")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
						}
						
						if (tipoCargo.equals("Jurado")) {
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
						}
						
						if (tipoCargo.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					} */
				}
				/*buscar por una carrera, un area, todas las tematica y un estatus*/
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todas")
						&& !estatus.equals("Todos")) {
			/*		String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));
					
					List<Teg> teg = servicioTeg
							.buscarTegDeUnAreaPorDosFechas(area1, fechaInicio, fechaFin);
					for (Teg tegs : teg) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Tutor")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
						}
						
						if (tipoCargo.equals("Jurado")) {
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
						}
						
						if (tipoCargo.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					
					}
					if (teg.size() == 0) {
						datosVacios = true;
					} */
				}
				/*buscar por una carrera, un area, todas las  tematica y todos los estatus*/
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todas")
						&& estatus.equals("Todos")) {
		/*			String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));
					
					List<Teg> teg = servicioTeg
							.buscarTegDeUnAreaPorDosFechas(area1, fechaInicio, fechaFin);
					
					for (Teg tegs : teg) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Todos")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					}
					if (teg.size() == 0) {
						datosVacios = true;
					} */
				}
				/*buscar por una carrera, un area, una  tematica y todos los estatus*/
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todas")
						&& estatus.equals("Todos")) {
		/*			String idTematica = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));

					List<Teg> teg1 = servicioTeg.buscarTegDeUnaTematicaPorDosFechas(tematica1, fechaInicio, fechaFin);
					
					for (Teg tegs : teg1) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Todos")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					}
					if (teg1.size() == 0) {
						datosVacios = true;
					} */
				}
				/*buscar por una carrera, todas las area y un estatus*/
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& !estatus.equals("Todos")) {
			/*		String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg2 = servicioTeg.buscarTegDeUnProgramaPorDosFechas(programa1, fechaInicio, fechaFin);
					
					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Tutor")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
						}
						
						if (tipoCargo.equals("Jurado")) {
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
						}
						
						if (tipoCargo.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					
					}
					if (teg2.size() == 0) {
						datosVacios = true;
					}*/
				}
				/*buscar por una carrera, todas las area y todos los estatus*/
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& estatus.equals("Todos")) {
			/*		String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					
					List<Teg> teg2 = servicioTeg.buscarTegDeUnProgramaPorDosFechas(programa1, fechaInicio, fechaFin);
					
					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Todos")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					}
					if (teg2.size() == 0) {
						datosVacios = true;
					}*/
				}
				/*buscar por todas las carrera, y un estatus*/
				if (nombrePrograma.equals("Todos") && !estatus.equals("Todos")) {
				/*	List<Teg> teg2 = servicioTeg.buscarTodosTegPorDosFechas(fechaInicio, fechaFin);
					
					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Tutor")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
						}
						
						if (tipoCargo.equals("Jurado")) {
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
						}
						
						if (tipoCargo.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					
					}
					if (teg2.size() == 0) {
						datosVacios = true;
					}*/
				}
				/*buscar por todas las carrera, y todos los estatus*/
				if (nombrePrograma.equals("Todos") && estatus.equals("Todos")) {
			/*		List<Teg> teg2 = servicioTeg.buscarTodosTegPorDosFechas(fechaInicio, fechaFin);
					
					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (tipoCargo.equals("Todos")) {
							elementos.add(new ProfesorTeg(
									profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
									tegs.getTitulo(),
									"Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(
										profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
										tegs.getTitulo(),
										"Jurado - " + jurado.getTipoJurado().getNombre(), tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(
										profesorComision.getNombre() + " " + profesorComision.getApellido(),
										tegs.getTitulo(),
										"Comision", tegs.getEstatus()));
							}
						}
					}
					if (teg2.size() == 0) {
						datosVacios = true;
					}*/
				}
				
				if (!datosVacios) {
					Collections.sort(elementos, new Comparator<ProfesorTeg>() {
						public int compare(ProfesorTeg a, ProfesorTeg b) {
						return a.getNombre().compareTo(b.getNombre());
						}});
					
					
					FileSystemView filesys = FileSystemView.getFileSystemView();
					Map p = new HashMap();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estructurados/compilados/RReporteProfesor.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
					p.put("programa", cmbPrograma.getValue());
					p.put("Fecha", new Date());
					p.put("FechaInicio", dtbFechaInicio.getValue());
					p.put("FechaFin", dtbFechaFin.getValue());
					p.put("Area", cmbArea.getValue());
					p.put("Programa", cmbPrograma.getValue());
					p.put("Tematica", cmbTematica.getValue());
					p.put("Estatus", cmbEstatus.getValue());
					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");

					jstVistaPrevia.setSrc(reporteSrc);
					jstVistaPrevia
							.setDatasource(new JRBeanCollectionDataSource(
									elementos));
					jstVistaPrevia.setType("pdf");
					jstVistaPrevia.setParameters(p);
				} else {
					Messagebox
							.show("No hay informacion disponible para esta seleccion");
				}
			}
		}		
	}

	/* Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarReporteProfesor")
	public void cancelarTematicasSolicitadas() throws JRException {
		cmbEstatus.setValue("");
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		jstVistaPrevia.setSrc("");
		jstVistaPrevia.setDatasource(null);
	}

	/* Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirReporteProfesor")
	public void salirTematicasSolicitadas() throws JRException {

		cancelarTematicasSolicitadas();
		wdwReporteProfesor.onClose();
	}

}
