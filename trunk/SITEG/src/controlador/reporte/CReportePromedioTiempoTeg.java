package controlador.reporte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
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

import modelo.Teg;
import modelo.TegEstatus;
import modelo.Tematica;
import modelo.reporte.PromedioTiempoTeg;
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
import net.sf.jasperreports.view.JasperViewer;

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
import controlador.CGeneral;

import servicio.SAreaInvestigacion;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STegEstatus;
import servicio.STematica;

@Controller
public class CReportePromedioTiempoTeg extends CGeneral {

	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	Tematica tematica = new Tematica();
	List<Teg> tegs = new ArrayList();
	long idTeg = 0;

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
	private Window wdwReportePromedioTiempoTeg;
	@Wire
	private Jasperreport jstVistaPrevia;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos" y se llena una lista del mismo en el
	 * componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaTodos = new Programa(10000, "Todos", "", "", true,
				null);
		programas.add(programaTodos);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));

		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);

	}

	/*
	 * Metodo que permite generar un reporte, dado a una lista de tegs
	 * previamente cargada al seleccionar un programa, area y tematica, se
	 * generara un pdf donde se muestra una lista de las etapas del teg con la
	 * cantidad de ocurrencia en unos intervalos de tiempo especificado de esta
	 * seleccion, comparando su periodo de tiempo donde ha cambiado, ademas se
	 * carga previamente una lista de las etapas de manera estatica dado si se
	 * aprobo o se reprobo un teg, donde mediante el componente "Jasperreport"
	 * sera mapea una serie de parametros y una lista previamente cargada que
	 * seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReportePromedioTiempoTeg")
	public void generarPromedioTiempoTeg() throws JRException {

		if ((cmbPrograma.getText().compareTo("") == 0)
				|| (cmbArea.getText().compareTo("") == 0)
				|| (cmbTematica.getText().compareTo("") == 0)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			List<String> nombreEstatus = new ArrayList();
			nombreEstatus.add("Solicitando Registro");
			nombreEstatus.add("Proyecto Registrado");
			nombreEstatus.add("Comision Asignada");
			nombreEstatus.add("Factibilidad Evaluada");
			nombreEstatus.add("Proyecto Factible");
			nombreEstatus.add("Proyecto en Desarrollo");
			nombreEstatus.add("Avances Finalizados");
			nombreEstatus.add("TEG Registrado");
			nombreEstatus.add("Trabajo en Desarrollo");
			nombreEstatus.add("Revisiones Finalizadas");
			nombreEstatus.add("Solicitando Defensa");
			nombreEstatus.add("Jurado Asignado");
			nombreEstatus.add("Defensa Asignada");
			nombreEstatus.add("TEG Aprobado");
			nombreEstatus.add("TEG Reprobado");

			long contador13 = 0;
			long contador46 = 0;
			long contador7 = 0;
			long cantidadTotal = 0;
			String estatus1 = "";
			String estatus2 = "";
			List<PromedioTiempoTeg> tegsPromedios = new ArrayList();
			int tamanioListaEstatus = nombreEstatus.size();

			if (tamanioListaEstatus % 2 != 0) {
				tamanioListaEstatus = tamanioListaEstatus - 3;
			}
			filtrarDatosBusqueda();
			if (tegs.size() != 0) {
				for (int i = 0; i < nombreEstatus.size(); i++) {

					List<TegEstatus> tegEstatus1 = servicioTegEstatus
							.buscarEstatusSegunTeg(nombreEstatus.get(i), tegs);
					List<TegEstatus> tegEstatus2 = servicioTegEstatus
							.buscarEstatusSegunTeg(nombreEstatus.get(i + 1),
									tegs);

					if (nombreEstatus.size() % 2 != 0
							&& tamanioListaEstatus == i) {
						List<TegEstatus> tegEstatus3 = servicioTegEstatus
								.buscarEstatusSegunTeg(
										nombreEstatus.get(i + 2), tegs);
						if (tegEstatus3.size() != 0) {
							for (int q = 0; q < tegEstatus3.size(); q++) {
								tegEstatus2.add(tegEstatus3.get(q));
							}

						}
						estatus1 = nombreEstatus.get(i);
						estatus2 = nombreEstatus.get(i + 1) + "/"
								+ nombreEstatus.get(i + 2);

						i = nombreEstatus.size();

					} else {

						estatus1 = nombreEstatus.get(i);
						estatus2 = nombreEstatus.get(i + 1);
					}

					for (int j = 0; j < tegEstatus1.size(); j++) {
						for (int v = 0; v < tegEstatus2.size(); v++) {
							if (tegEstatus1.get(j).getTeg().getId() == tegEstatus1
									.get(v).getTeg().getId()) {

								Date fecha1 = tegEstatus1.get(j)
										.getFechaEstatus();
								Date fecha2 = tegEstatus2.get(v)
										.getFechaEstatus();

								Calendar calendarioFecha1 = Calendar
										.getInstance();
								calendarioFecha1.setTime(fecha1);

								Calendar calendarioFecha2 = Calendar
										.getInstance();
								calendarioFecha2.setTime(fecha2);

								long miliSegundoFecha1 = calendarioFecha1
										.getTimeInMillis();
								long miliSegundoFecha2 = calendarioFecha2
										.getTimeInMillis();
								long diferenciaFecha = miliSegundoFecha2
										- miliSegundoFecha1;
								long direfenciaDias = Math.abs(diferenciaFecha
										/ (24 * 60 * 60 * 1000));

								cantidadTotal = cantidadTotal + 1;

								if (direfenciaDias >= 0 && direfenciaDias <= 3) {
									contador13 = contador13 + 1;
								} else if (direfenciaDias >= 4
										&& direfenciaDias <= 6) {
									contador46 = contador46 + 1;
								} else {
									contador7 = contador7 + 1;
								}
							}

						}
					}
					PromedioTiempoTeg promedio = new PromedioTiempoTeg(estatus1
							+ "-" + estatus2, contador13, contador46, contador7);
					tegsPromedios.add(promedio);

					contador13 = 0;
					contador46 = 0;
					contador7 = 0;
					cantidadTotal = 0;

					i = i + 1;
				}
				FileSystemView filesys = FileSystemView.getFileSystemView();
				Map parametro = new HashMap();
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/estadisticos/compilados/RPromedioTiempoTeg.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				parametro.put("titulo",
						"UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
								+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
								+ "DIRECCION DE PROGRAMA");
				parametro.put("programaNombre", cmbPrograma.getValue());
				parametro.put("areaNombre", cmbArea.getValue());
				parametro.put("tematicaNombre", cmbTematica.getValue());
				parametro.put("logoUcla", reporteImage + "logo ucla.png");
				parametro.put("logoCE", reporteImage + "logo CE.png");
				parametro.put("logoSiteg", reporteImage + "logo.png");

				JasperReport jasperReport = (JasperReport) JRLoader
						.loadObject(reporteSrc);

				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, parametro,
						new JRBeanCollectionDataSource(tegsPromedios));

				JasperViewer.viewReport(jasperPrint, false);
			} else {
				Messagebox.show(
						"No hay informacion disponible para esta seleccion",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);
			}

		}

	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		try {
			String idPrograma = cmbPrograma.getSelectedItem().getId();
			String nombrePrograma = cmbPrograma.getValue();
			if (nombrePrograma.equals("Todos")) {

				areas = servicioArea.buscarActivos();
				AreaInvestigacion area = new AreaInvestigacion(100000001,
						"Todos", "", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
				cmbArea.setDisabled(false);

			} else {

				cmbArea.setDisabled(false);
				cmbArea.setValue("");
				cmbTematica.setValue("");
				programa = servicioPrograma.buscar(Long.parseLong(idPrograma));
				areas = servicioProgramaArea.buscarAreasDePrograma(programa);
				AreaInvestigacion area = new AreaInvestigacion(100000001,
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
	 * valor en el campo tematica, ademas se adiciona un nuevo item donde se
	 * puede seleccionar la opcion de "Todos" en el combo de las tematicas.
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() throws JRException {
		String idArea = cmbArea.getSelectedItem().getId();
		String nombreArea = cmbArea.getValue();
		if (nombreArea.equals("Todos")) {
			
			cmbTematica.setValue("Todos");
			cmbTematica.setDisabled(true);
			
			
		} else {
			
			cmbTematica.setDisabled(false);
			cmbTematica.setValue("");
			area = servicioArea.buscarArea(Long.parseLong(idArea));
			List<Tematica> tematicasTodos = servicioTematica
					.buscarTematicasDeArea(area);
			Tematica tematicaTodos = new Tematica(10000000, "Todos", "", true,
					null);
			tematicasTodos.add(tematicaTodos);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicasTodos));
				
	
		}
	}

	/* Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarReportePromedioTiempoTeg")
	public void cancelarPromedioTiempoTeg() throws JRException {
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setValue("");
		cmbTematica.setDisabled(true);
		tegs = null;
		jstVistaPrevia.setSrc("");
		jstVistaPrevia.setDatasource(null);
	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirReportePromedioTiempoTeg")
	public void salirPromedioTiempoTeg() throws JRException {
		cancelarPromedioTiempoTeg();
		wdwReportePromedioTiempoTeg.onClose();
	}

	/*
	 * Metodo que permite cargar una lista de tegs Aprobados/Reprobados al
	 * seleccionar un programa, area y tematica.
	 */
	public void filtrarDatosBusqueda() {

		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		String estatusAprobado = "TEG Aprobado";
		String estatusReprobado = "TEG Reprobado";
		tegs = null;
		if (nombrePrograma.equals("Todos")) {
			tegs = servicioTeg.buscarTegSegunEstatus(estatusAprobado,
					estatusReprobado);
		} else if (!nombrePrograma.equals("Todos")
				&& nombreArea.equals("Todos")) {
			tegs = servicioTeg.buscarTegSegunProgramaEstatus(programa,
					estatusAprobado, estatusReprobado);
		} else if (!nombrePrograma.equals("Todos")
				&& !nombreArea.equals("Todos")
				&& !nombreTematica.equals("Todos")) {
			Tematica tematica = servicioTematica
					.buscarTematicaPorNombre(nombreTematica);
			tegs = servicioTeg.buscarTegSegunTematicaEstatus(tematica,
					estatusAprobado, estatusReprobado);
		} else if (!nombrePrograma.equals("Todos")
				&& !nombreArea.equals("Todos")
				&& nombreTematica.equals("Todos")) {
			tegs = servicioTeg.buscarTegSegunAreaInvestigacionEstatus(area,
					estatusAprobado, estatusReprobado);
		}

	}

}
