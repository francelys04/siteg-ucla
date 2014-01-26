package controlador;

import java.util.ArrayList;
import java.util.Collections;
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
import org.zkoss.zk.ui.select.SelectorComposer;
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
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;

@Controller
public class CReporteTematicasMasSolicitadas extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();

	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	/********** Proyectos Solicitados **************/

	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbTipo;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaFicticio = new Programa(10000, "Todos", "", "", true,
				null);
		programas.add(programaFicticio);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));

	}

	@Listen("onClick = #btnGenerarProyectosSolicitados")
	public void generarProyectosSolicitados() throws JRException {

		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String tipo = cmbTipo.getValue();
		String estatus = cmbEstatus.getValue();
		System.out.println("es" + estatus);
		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		fechaInicio = dtbFechaInicio.getValue();
		fechaFin = dtbFechaFin.getValue();
		List<Long> tematicasSeleccionadas = new ArrayList();
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			if (nombrePrograma != "Todos" && nombreArea != "Todos") {
				String idArea = cmbArea.getSelectedItem().getId();
				AreaInvestigacion area1 = servicioArea
						.buscarArea(Long.parseLong(idArea));
				tematicasSeleccionadas = servicioTeg
						.buscarUltimasTematicasProgramaAreaEstatus(estatus,
								area1, fechaInicio, fechaFin);
			}
			else
			if (nombrePrograma.equals("Todos")) {
				tematicasSeleccionadas = servicioTeg.buscarUltimasEstatus(
						estatus, fechaInicio, fechaFin);
			}
			else
			if(nombrePrograma!="Todos"  && nombreArea.equals("Todos")) {
				String idPrograma=cmbPrograma.getSelectedItem().getId();
				Programa programa1=servicioPrograma.buscar(Long.parseLong(idPrograma));
				System.out.println("programa"+programa1.getNombre());
				tematicasSeleccionadas = servicioTeg
						.buscarUltimasTematicasProgramaEstatus(estatus,
								programa1, fechaInicio, fechaFin);
			
			}
			
		}
		/*************************** Contador de Lista ************* ******************/
		List<Long> tematicas = new ArrayList();
		List<Integer> contadores = new ArrayList();
		long tematica = tematicasSeleccionadas.get(0);
		int contadorTematicas = 0;
		for (int i = 0; i < tematicasSeleccionadas.size(); i++) {
			if (tematica == tematicasSeleccionadas.get(i)) {
				contadorTematicas = contadorTematicas + 1;
			} else {
				tematicas.add(tematica);
				contadores.add(contadorTematicas);
				tematica = tematicasSeleccionadas.get(i);
				contadorTematicas = 1;
			}
		}
		tematicas.add(tematica);
		contadores.add(contadorTematicas);
		/*************************** Ordenado de Lista ************* ******************/
		List<Long> tematicasOrdenados = new ArrayList();
		List<Integer> contadoresOrdenados = new ArrayList();
		List<Tematica> tematicasFinales = new ArrayList();
		int valor = 0;
		int valor2 = 0;
		long valorTematica = 0;
		for (int i = 0; i < contadores.size(); i++) {
			valor = contadores.get(i);
			for (int j = 0; j < contadores.size(); j++) {
				valor2 = contadores.get(j);
				valorTematica = tematicas.get(j);
				if (valor2 > valor) {
					contadores.remove(j);
					tematicas.remove(j);
					contadoresOrdenados.add(valor2);
					tematicasOrdenados.add(valorTematica);
					j = contadores.size();
				}
			}
		}
		if (contadoresOrdenados.size() < 5) {
			int cantidadFaltante = 4 - contadoresOrdenados.size();
			System.out.println("tamaño lista1:" + contadoresOrdenados.size());
			int i;
			for (i = 0; i < 4 - contadoresOrdenados.size(); i++) {
				for (int z = 0; z < contadores.size(); z++) {
					System.out.println("itemsz-:" + contadores.get(z));
					contadoresOrdenados.add(contadores.get(z));
					tematicasOrdenados.add(tematicas.get(z));
				}
			}
			System.out.println("tamaño lista2:" + contadoresOrdenados.size());
		}
		if (contadoresOrdenados.size() > 5) {
			for (int i = 0; i < 4; i++) {
				contadoresOrdenados.add(contadores.get(i));
				tematicasOrdenados.add(tematicas.get(i));
			}
		}
		List<Long> tematicasFinales2 = new ArrayList();
		for (int i = 0; i < tematicasOrdenados.size(); i++) {
			Tematica tematicaFinal = servicioTematica
					.buscarTematica(tematicasOrdenados.get(i));
			tematicasFinales.add(tematicaFinal);
			System.out.println("items3tematicas-:"
					+ tematicasFinales.get(i).getId());
		}
		List<Teg> tegsSegunUltimasTematicas = servicioTeg
				.buscarUltimasOrdenadasEstatus(estatus, tematicasFinales,
						fechaInicio, fechaFin);

		FileSystemView filesys = FileSystemView.getFileSystemView();
		Map p = new HashMap();
		p.put("titulo", "UNIVERSIDAD CENTROCCIDENTA LISANDRO ALVARADO");
		p.put("programaNombre", cmbPrograma.getValue());
		p.put("areaNombre", cmbArea.getValue());
		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(getClass().getResource(
						"/reporte/RProyectosSolicitados.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p,
				new JRBeanCollectionDataSource(tegsSegunUltimasTematicas));
		JasperExportManager.exportReportToPdfFile(jasperPrint, filesys
				.getHomeDirectory().toString()
				+ "/ReporteTematicasMasSolicitadas.pdf");
		Messagebox.show("Se ha generado exitosamente",
				"Informacion", Messagebox.OK, Messagebox.INFORMATION);


	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		String idPrograma = cmbPrograma.getSelectedItem().getId();
		String nombrePrograma = cmbPrograma.getValue();
		if (nombrePrograma.equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbArea.setDisabled(true);
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

	@Listen("onSelect = #cmbTipo")
	public void seleccionarTipo() throws JRException {
		List<String> listaComboEstatus = new ArrayList();
		String tipoNombre = cmbTipo.getValue();
		if (tipoNombre.equals("Proyecto")) {
			// listaComboEstatus.add("Todos");
			listaComboEstatus.add("Factible");
			listaComboEstatus.add("No Factible");
		}
		if (tipoNombre.equals("Teg")) {
			// listaComboEstatus.add("Todos");
			listaComboEstatus.add("Aprobado");
			listaComboEstatus.add("Reprobado");
		}
		cmbEstatus.setModel(new ListModelList<String>(listaComboEstatus));
	}
	}
