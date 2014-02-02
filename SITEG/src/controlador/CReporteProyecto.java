package controlador;

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
import modelo.Jurado;
import modelo.Profesor;
import modelo.Programa;
import modelo.ProgramaArea;
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;

@Controller
public class CReporteProyecto extends CGeneral {
	public static class ElementoReporte {
		private String nombre;
		private String titulo;
		private String cargo;
		private String estatusTeg;


		public ElementoReporte(String nombre, String titulo, String cargo, String estatusTeg) {
			super();
			this.nombre = nombre;
			this.titulo = titulo;
			this.cargo = cargo;
			this.estatusTeg = estatusTeg;
		}

		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public String getTitulo() {
			return titulo;
		}
		
		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}
		
		public String getCargo() {
			return cargo;
		}
		
		public void setCargo(String cargo) {
			this.cargo = cargo;
		}
		
		public String getEstatusTeg() {
			return estatusTeg;
		}

		public void setEstatusTeg(String estatusTeg) {
			this.estatusTeg = estatusTeg;
		}
	}
	
	SJurado servicioJurado = GeneradorBeans.getServicioJurado();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	@Wire
	private Window wdwReporteProfesorCargo;
	@Wire
	private Datebox dtbFechaReporteProfesorTeg;
	@Wire
	private Datebox dtbCronogramaFechaInicio;

	@Wire
	private Datebox dtbCronogramaFechaHasta;
	@Wire
	private Textbox txtCedulaReporteProfesorTeg;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	private String[] estatusProfesor = { "Todos", "Tutor",
			"Comision Evaluadora", "Jurado" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);

		cmbPrograma.setModel(new ListModelList<Programa>(programas));

		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusProfesor));

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula((String) map.get("cedula"));
				txtCedulaReporteProfesorTeg.setValue(profesor.getCedula());

				idTematica = (Long) map.get("tematica");

				Programa programa = servicioPrograma.buscar((Long) map
						.get("programa"));
				Tematica tematica = servicioTematica.buscarTematica((Long) map
						.get("tematica"));
				cmbPrograma.setValue(programa.getNombre());
				cmbArea.setValue((String) map.get("area"));
				cmbTematica.setValue(tematica.getNombre());
			}
		}
	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		cmbArea.setValue("");
		cmbTematica.setValue("");

		Programa programa = (Programa) cmbPrograma.getSelectedItem().getValue();
		areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
				.buscar(programa.getId()));
		cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		cmbTematica.setValue("");
		AreaInvestigacion tematica = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(tematica.getId()));
		cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
	}

	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
	}

	@Listen("onClick = #btnProfesorReporteProfesorTeg")
	public void buscarProfesor() {

		final HashMap<String, Object> map2 = new HashMap<String, Object>();

		map2.put("area", cmbArea.getValue());
		Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
		catalogo.recibir("reportes/no estructurados/VReporteProfesorTeg",
				Long.parseLong(cmbPrograma.getSelectedItem().getId()),
				Long.parseLong(cmbTematica.getSelectedItem().getId()));

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesorTematica.zul", null, null);
		window.doModal();

	}

	@Listen("onClick = #btnGenerarReporteProfesorCargo")
	public void generarReporteProfesorTeg() throws JRException {
		Date fechaInicio = dtbCronogramaFechaInicio.getValue();
		Date fechaFin = dtbCronogramaFechaHasta.getValue();
		String area1 = cmbArea.getValue();
		String tematica1 = cmbTematica.getValue();
		String programa1= cmbPrograma.getValue();
		AreaInvestigacion area = servicioArea.buscarArea(idArea);
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		String tipoCargo = (String) cmbEstatus.getSelectedItem().getValue();
		System.out.println(tipoCargo);
		//Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedula);

		List<ElementoReporte> elementos = new ArrayList<ElementoReporte>();

		if (fechaFin == null || fechaInicio == null || fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			List<Teg> tegs = servicioTeg.buscarTegsDeTematicaPorDosFechas(tematica, fechaInicio, fechaFin);
			for (Teg teg : tegs) {
				Profesor profesorTutor = teg.getTutor();
				if (tipoCargo.equals("Tutor") || tipoCargo.equals("Todos")) {
					elementos.add(new ElementoReporte(
							profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
							teg.getTitulo(),
							"Tutor", teg.getEstatus()));
				}
				
				if (tipoCargo.equals("Jurado") || tipoCargo.equals("Todos")) {
					for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(teg)) {
						Profesor profesorJurado = jurado.getProfesor();
						elementos.add(new ElementoReporte(
								profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
								teg.getTitulo(),
								"Jurado - " + jurado.getTipoJurado().getNombre(), teg.getEstatus()));
					}
				}
				
				if (tipoCargo.equals("Comision Evaluadora") || tipoCargo.equals("Todos")) {
					for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(teg)) {
						elementos.add(new ElementoReporte(
								profesorComision.getNombre() + " " + profesorComision.getApellido(),
								teg.getTitulo(),
								"Comision", teg.getEstatus()));
					}
				}
			}

			Collections.sort(elementos, new Comparator<ElementoReporte>() {
				public int compare(ElementoReporte a, ElementoReporte b) {
				return a.nombre.compareTo(b.nombre);
				}});
		}

		FileSystemView filesys = FileSystemView.getFileSystemView();
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("titulo", "LISTA DE PROFESORES CON EL ROL QUE DESEMPEÑAN EN CADA TRABAJO ESPECIAL DE GRADO");
		p.put("fecha", new Date());
		p.put("fecha1", fechaInicio);
		p.put("fecha2", fechaFin);
		p.put("area", area1);
		p.put("tematica", tematica1);
		p.put("programa", programa1);
		p.put("cargo", tipoCargo);

		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(getClass().getResource(
						"/reporte/RReporteProfesorCargo.jasper"));

		JasperPrint jasperPrint = JasperFillManager.fillReport(
				jasperReport, p, new JRBeanCollectionDataSource(elementos));
		
		String ruta = filesys.getHomeDirectory().toString() + "/reporte.pdf";
		JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
		
		Messagebox.show("Su reporte fue guardado en: " + ruta,
				"Información", Messagebox.OK,
				Messagebox.INFORMATION);

	}
	@Listen("onClick = #btnSalirReporteProfesorCargo")
	public void cancelarItem() {
		cmbEstatus.setValue("");
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");;
		
	}
}
