package controlador;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;

import modelo.Programa;
import modelo.ProgramaArea;

import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.PromedioTegFechaEntrega;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.stereotype.Controller;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SAreaInvestigacion;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STematica;

@Controller
public class CReportePromedioTegFechaEntrega extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();

	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	Tematica tematica = new Tematica();

	/********** Promedio Teg Fecha Entrega **************/

	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Jasperreport jstVistaPrevia;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaTodos = new Programa(10000, "Todos", "", "", true,
				null);
		programas.add(programaTodos);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));

	}

	@Listen("onClick = #btnGenerarReportePromedioTegFechaEntrega")
	public void generarPromedioTegFechaEntrega() throws JRException {

		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();

		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		fechaInicio = dtbFechaInicio.getValue();
		fechaFin = dtbFechaFin.getValue();

		List<Teg> tegsSeleccionados = new ArrayList();
		List<PromedioTegFechaEntrega> tegsFechaEntrega = new ArrayList();
		long contadorMenores = 0;
		long contadorMayores = 0;
		long contadorIguales = 0;

		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			
			if (nombrePrograma.equals("Todos")) {
				tegsSeleccionados= servicioTeg.buscarTegTodos(fechaInicio, fechaFin);
			}
			else
			if (!nombrePrograma.equals("Todos") && nombreArea.equals("Todos")) {
				tegsSeleccionados=servicioTeg.buscarTegsSegunPrograma(programa, fechaInicio, fechaFin);
			}
			else
			if (!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && !nombreTematica.equals("Todos")) {
			    Tematica tematica= servicioTematica.buscarTematicaPorNombre(nombreTematica);
				tegsSeleccionados= servicioTeg.buscarTegsDeTematicaPorDosFechas(tematica, fechaInicio, fechaFin);
			}else
			if(!nombrePrograma.equals("Todos") && !nombreArea.equals("Todos") && nombreTematica.equals("Todos")){
				tegsSeleccionados=servicioTeg.buscarTegsSegunArea(area, fechaInicio, fechaFin);
			}
			
			if (tegsSeleccionados.size() != 0) {
				for (int i = 0; i < tegsSeleccionados.size(); i++) {
					if (tegsSeleccionados.get(i).getFecha()
							.before(tegsSeleccionados.get(i).getFechaEntrega())) {
						contadorMenores = contadorMenores + 1;

						PromedioTegFechaEntrega tegFechaEntrega = new PromedioTegFechaEntrega(
								tegsSeleccionados.get(i), contadorMenores,
								contadorIguales, contadorMayores);
						tegsFechaEntrega.add(tegFechaEntrega);
					} else {
						if (tegsSeleccionados.get(i).getFechaEntrega()
								.before(tegsSeleccionados.get(i).getFecha())) {
							contadorMayores = contadorMayores + 1;

							PromedioTegFechaEntrega tegFechaEntrega = new PromedioTegFechaEntrega(
									tegsSeleccionados.get(i), contadorMenores,
									contadorIguales, contadorMayores);
							tegsFechaEntrega.add(tegFechaEntrega);
						} else {
							contadorIguales = contadorIguales + 1;

							PromedioTegFechaEntrega tegFechaEntrega = new PromedioTegFechaEntrega(
									tegsSeleccionados.get(i), contadorMenores,
									contadorIguales, contadorMayores);
							tegsFechaEntrega.add(tegFechaEntrega);
						}
					}
					
				}
				for(int i=0;i<tegsFechaEntrega.size();i++){
					System.out.println("datos:"+tegsFechaEntrega.get(i).getTeg().getTematica().getNombre());
					System.out.println("menores:"+tegsFechaEntrega.get(i).getContadorMenores());
					System.out.println("iguales:"+tegsFechaEntrega.get(i).getContadorIguales());
					System.out.println("Mayores:"+tegsFechaEntrega.get(i).getContadorMayores());
					
				}
				 FileSystemView filesys = FileSystemView.getFileSystemView();
				 Map parametro = new HashMap();
				 String rutaUrl = obtenerDirectorio();
				 String reporteSrc = rutaUrl
				 +
				 "SITEG/vistas/reportes/estadisticos/compilados/RPromedioTegFechaEntrega.jasper";
				 String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
				
				 
				 parametro.put("titulo",
				 "UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
				 + "DECANATO DE CIENCIAS Y TECNOLOGIA"
				 + "DIRECCION DE PROGRAMA");
				 parametro.put("programaNombre", cmbPrograma.getValue());
				 parametro.put("areaNombre", cmbArea.getValue());
				 parametro.put("tematicaNombre", cmbTematica.getValue());
				 parametro.put("fechaInicio", fechaInicio);
				 parametro.put("fechaFin", fechaFin);
				 parametro.put("logoUcla", reporteImage + "logo ucla.png");
				 parametro.put("logoCE", reporteImage + "logo CE.png");
				 jstVistaPrevia.setSrc(reporteSrc);
				 jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
				 tegsFechaEntrega));
				 jstVistaPrevia.setType("pdf");
				 jstVistaPrevia.setParameters(parametro);
				
			} 
		 
		 else {
		 Messagebox.show("No ha informacion disponible para este intervalo");
		 }
		}

	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		String idPrograma = cmbPrograma.getSelectedItem().getId();
		String nombrePrograma = cmbPrograma.getValue();
		if (nombrePrograma.equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
			cmbArea.setDisabled(true);
			cmbTematica.setDisabled(true);

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

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() throws JRException {
		String idArea = cmbArea.getSelectedItem().getId();
		String nombreArea = cmbArea.getValue();
		if (nombreArea.equals("Todos")) {
			cmbTematica.setValue("Todos");
			cmbTematica.setDisabled(true);
		} else {
			cmbTematica.setDisabled(false);
			area = servicioArea.buscarArea(Long.parseLong(idArea));
			List<Tematica> tematicasTodos = servicioTematica
					.buscarTematicasDeArea(area);
			Tematica tematicaTodos = new Tematica(10000000, "Todos", "", true,
					null);
			tematicasTodos.add(tematicaTodos);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicasTodos));
		}
	}

	@Listen("onClick = #btnSalirReportePromedioTegFechaEntrega")
	public void cancelarPromedioTegFechaEntrega() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setValue("");
		cmbTematica.setDisabled(true);
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());

		jstVistaPrevia.setSrc("");
		jstVistaPrevia.setDatasource(null);
	}

}