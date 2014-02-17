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
import controlador.CGeneral;
import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoProfesorTematica;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;

@Controller
public class CReporteProfesor extends CGeneral {
	
	
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	private String[] estatusProfesor = { "Todos", "Tutor",
			"Comision Evaluadora", "Jurado" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	
	@Wire
	private Window wdwReporteProfesor;
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
	@Wire
	private Jasperreport jstVistaPrevia;

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
	public
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
			cmbTematica.setValue("Todos");
		}
		else{
		    cmbArea.setValue("");
		    cmbTematica.setValue("");
		    Programa programa = (Programa) cmbPrograma.getSelectedItem().getValue();
		     areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
				    .buscar(programa.getId()));
		 	AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos", "", true);
		 	areas.add(area);
		    cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		}	}
	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		if (cmbArea.getValue().equals("Todos")) {
			
			cmbTematica.setValue("Todos");
		}
		else{
		   cmbTematica.setValue("");
		    AreaInvestigacion tematica = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
		    tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				        .buscarArea(tematica.getId()));
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
		idTematica = tematica.getId();
	}

	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y tipo de cargo, se generara un pdf donde se muestra una lista de
	 * profesores especificando tanto datos basicos como su rol en el teg de
	 * esta seleccion, la cual esta condicionado, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
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
		List<ProfesorTeg> elementos = new ArrayList<ProfesorTeg>();

		if (fechaFin == null || fechaInicio == null || fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			List<Teg> tegs = servicioTeg.buscarTegsDeTematicaPorDosFechas(tematica, fechaInicio, fechaFin);
			for (Teg teg : tegs) {
				 		
				Profesor profesorTutor = teg.getTutor();
				if (tipoCargo.equals("Tutor") || tipoCargo.equals("Todos")) {
					elementos.add(new ProfesorTeg(
							profesorTutor.getNombre() + " " + profesorTutor.getApellido(),
							teg.getTitulo(),
							"Tutor", teg.getEstatus()));
				}
				
				if (tipoCargo.equals("Jurado") || tipoCargo.equals("Todos")) {
					for (Jurado jurado : servicioJurado.buscarJuradoDeTeg(teg)) {
						Profesor profesorJurado = jurado.getProfesor();
						elementos.add(new ProfesorTeg(
								profesorJurado.getNombre() + " " + profesorJurado.getApellido(),
								teg.getTitulo(),
								"Jurado - " + jurado.getTipoJurado().getNombre(), teg.getEstatus()));
					}
				}
				
				if (tipoCargo.equals("Comision Evaluadora") || tipoCargo.equals("Todos")) {
					for (Profesor profesorComision : servicioProfesor.buscarComisionDelTeg(teg)) {
						elementos.add(new ProfesorTeg(
								profesorComision.getNombre() + " " + profesorComision.getApellido(),
								teg.getTitulo(),
								"Comision", teg.getEstatus()));
					}
				}
			}

			Collections.sort(elementos, new Comparator<ProfesorTeg>() {
				public int compare(ProfesorTeg a, ProfesorTeg b) {
				return a.getNombre().compareTo(b.getNombre());
				}});
		}

		FileSystemView filesys = FileSystemView.getFileSystemView();
		Map<String, Object> p = new HashMap<String, Object>();
		String rutaUrl = obtenerDirectorio();
		 String reporteSrc = rutaUrl
		 +
		 "SITEG/vistas/reportes/estructurados/compilados/RReporteProfesor.jasper";
		 String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
		
		
		 p.put("fecha", new Date());
			p.put("fecha1", fechaInicio);
			p.put("fecha2", fechaFin);
			p.put("area", area1);
			p.put("tematica", tematica1);
			p.put("programa", programa1);
			p.put("cargo", tipoCargo);
			p.put("logoUcla", reporteImage + "logo ucla.png");
			p.put("logoCE", reporteImage + "logo CE.png");
			p.put("logoSiteg", reporteImage + "logo.png");

		 jstVistaPrevia.setSrc(reporteSrc);
		 jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
		 elementos));
		 jstVistaPrevia.setType("pdf");
		 jstVistaPrevia.setParameters(p);
		 
		
	}
		
	/* Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarReporteProfesorCargo")
	public void cancelarTematicasSolicitadas() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setValue("");
		cmbEstatus.setValue("");
		dtbCronogramaFechaInicio.setValue(new Date());
		dtbCronogramaFechaHasta.setValue(new Date());

//		jstVistaPrevia.setSrc("");
//		jstVistaPrevia.setDatasource(null);
	}

	/* Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirReporteProfesorCargo")
	public void salirTematicasSolicitadas() throws JRException {

		cancelarTematicasSolicitadas();
		wdwReporteProfesor.onClose();
	}
	
	
}
