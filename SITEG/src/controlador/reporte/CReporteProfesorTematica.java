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
import servicio.SProfesor;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STematica;

@Controller
public class CReporteProfesorTematica extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	@Wire
	private Window wdwReporteProfesorTematica;
	@Wire
	private Textbox txtCedulaProfesor;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	private String[] estatusProfesor = { "Todos", "true", "false" };
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
		cmbPrograma.setModel(new ListModelList<Programa>(
				programas));
		cmbEstatus.setModel(new ListModelList<String>(
				estatusProfesor));

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula((String) map.get("cedula"));
				txtCedulaProfesor.setValue(profesor.getCedula());
				if (map.get("area").equals(estatusProfesor[0])) {
					cmbPrograma.setValue((String) map
							.get("programa"));
					cmbArea
							.setValue((String) map.get("area"));
					cmbTematica.setValue((String) map
							.get("tematica"));
				} else {
					idTematica = (Long) map.get("tematica");

					Programa programa = servicioPrograma.buscar((Long) map
							.get("programa"));
					Tematica tematica = servicioTematica
							.buscarTematica((Long) map.get("tematica"));
					cmbPrograma
							.setValue(programa.getNombre());
					cmbArea
							.setValue((String) map.get("area"));
					cmbTematica
							.setValue(tematica.getNombre());
				}
			}
			map.clear();
			map = null;
		}
	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		cmbArea.setValue("");
		cmbTematica.setValue("");
		if (cmbPrograma.getValue().equals(estatusProfesor[0])) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
		} else {
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long.parseLong(cmbPrograma
							.getSelectedItem().getId())));
			cmbArea
					.setModel(new ListModelList<AreaInvestigacion>(areas));
		}
	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		cmbTematica.setValue("");
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(Long.parseLong(cmbArea
						.getSelectedItem().getId())));
		cmbTematica.setModel(new ListModelList<Tematica>(
				tematicas));
	}

	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		idTematica = Long.parseLong(cmbTematica
				.getSelectedItem().getId());
	}

	@Listen("onClick = #btnProfesor")
	public void buscarProfesor() {

		final HashMap<String, Object> map2 = new HashMap<String, Object>();
		if (cmbPrograma.getValue().equals(estatusProfesor[0])) {
			map2.put("area", cmbArea.getValue());
			map2.put("programa", cmbPrograma.getValue());
			map2.put("tematica", cmbTematica.getValue());
			Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
			window.doModal();

			catalogoProfesor.recibir("reportes/no estructurados/VReporteProfesorTeg");
		} else {
			map2.put("area", cmbArea.getValue());
			Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
			catalogo.recibir("reportes/no estructurados/VReporteProfesorTeg", Long
					.parseLong(cmbPrograma.getSelectedItem()
							.getId()), Long
					.parseLong(cmbTematica.getSelectedItem()
							.getId()));

			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesorTematica.zul", null,
					null);
			window.doModal();
		}

	}

	@Listen("onClick = #btnGenerarReporte")
	public void generarReporte() throws JRException {
		String cedula = txtCedulaProfesor.getValue();
		String estatus = cmbEstatus.getValue();
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedula);
		List<Profesor> profesores = new ArrayList<Profesor>();
	
			if (estatus.equals(estatusProfesor[0])) {
				System.out.println(cmbTematica.getValue());
				if (cmbTematica.getValue().equals(estatusProfesor[0])) {
					
					profesores= servicioProfesor.buscarTodos();
					System.out.println("Todos"+profesores.size());
				} else {
					profesores = servicioProfesor.buscarProfesoresPorTematica(tematica);
					System.out.println("Todos estatus una tematica"+profesores.size());
				}
			} else {
				if (cmbTematica.getValue().equals(estatusProfesor[0])) {
					profesores= servicioProfesor.buscarActivos();
					System.out.println("Todas tematicas un estatus"+profesores.size());
				} else {
				//	profesores = servicioProfesor.buscarProfesoresPorTematicayEstatus(tematica,estatus);
					System.out.println("Una tematica un estatus"+profesores.size());
				}
			}
			FileSystemView filesys = FileSystemView.getFileSystemView();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("titulo", "UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
					+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
					+ "DIRECCION DE PROGRAMA");
			p.put("tematica", tematica.getNombre());
			p.put("area", tematica.getareaInvestigacion().getNombre());
			p.put("programa", tematica.getareaInvestigacion().getProgramasAreas());
			
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(getClass().getResource(
							"/reporte/RProyectosProfesor.jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, p, new JRBeanCollectionDataSource(profesores));
			JasperExportManager.exportReportToPdfFile(jasperPrint, filesys
					.getHomeDirectory().toString() + "/reporte.pdf");

		}
		
	

}

