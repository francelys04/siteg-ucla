package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.MasSolicitados;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteEstadisticoDefensa extends CGeneral {

	@Wire
	private Window wdwReporteDefensa;
	@Wire
	private Datebox dtbInicio;
	@Wire
	private Datebox dtbFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;

	long idTematica = 0;
	long idArea = 0;
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	static Programa programa1 = new Programa();
	static AreaInvestigacion area1 = new AreaInvestigacion();
	private static long valor1 = 0;
	private static long valor2 = 0;
	private static long valor3 = 0;
	private static List<Teg> teg1 = new ArrayList<Teg>();
	private static List<Teg> teg2 = new ArrayList<Teg>();
	private static List<Teg> teg3 = new ArrayList<Teg>();
	private static String estatus1 = "Solicitando Defensa";
	private static String estatus2 = "Jurado Asignado";
	private static String estatus3 = "Defensa Asignada";

	@Override
	public void inicializar(Component comp) {

		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		

	}

	@Listen("onSelect = #cmbPrograma")
	
	public void seleccionarPrograma() {
		
		try{
			if (cmbPrograma.getValue().equals("Todos")) {
		

			areas = servicioArea.buscarActivos();
			AreaInvestigacion area = new AreaInvestigacion(1000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
			cmbArea.setDisabled(false);
			}
		
		else {
			cmbArea.setValue("");
			cmbArea.setDisabled(false);
			cmbTematica.setValue("");
			programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(programa1.getId()));
			AreaInvestigacion area = new AreaInvestigacion(1000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		}}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
		}


	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica, ademas se adiciona un nuevo item donde se
	 * puede seleccionar la opcion de "Todos" en el combo de las tematicas
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {

		try{
		if (cmbArea.getValue().equals("Todos")) {

			cmbTematica.setValue("Todos");
			cmbTematica.setDisabled(true);
		} else {
			cmbTematica.setValue("");
			cmbTematica.setDisabled(false);
			area1 = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(area1.getId()));
			Tematica tema = new Tematica(10000, "Todos", "", true, null);
			tematicas.add(tema);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
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

	@Listen("onClick = #btnGenerar")
	public void generarReporteDefensa() throws JRException {
		if (cmbPrograma.getText().compareTo("") == 0
				|| cmbArea.getText().compareTo("") == 0
				|| cmbTematica.getText().compareTo("") == 0) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			Date fechaInicio = dtbInicio.getValue();
			Date fechaFin = dtbFin.getValue();

			if (fechaInicio.after(fechaFin)) {

				Messagebox
						.show("La fecha de fin debe ser posterior a la fecha de inicio",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {
				String nombreArea = cmbArea.getValue();
				String nombrePrograma = cmbPrograma.getValue();
				String nombreTematica = cmbTematica.getValue();
				fechaInicio = dtbInicio.getValue();
				fechaFin = dtbFin.getValue();
				if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {
					teg1 = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
							estatus1, fechaInicio, fechaFin);
					teg2 = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
							estatus2, fechaInicio, fechaFin);
					teg3 = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
							estatus3, fechaInicio, fechaFin);
					valor1 = teg1.size();
					valor2 = teg2.size();
					valor3 = teg3.size();
				}
				/*
				 * seleccione un programa
				 */
				else if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {

					teg1 = servicioTeg.buscarTegPorProgramaUnEstatus(estatus1,
							programa1, fechaInicio, fechaFin);
					teg2 = servicioTeg.buscarTegPorProgramaUnEstatus(estatus2,
							programa1, fechaInicio, fechaFin);
					teg3 = servicioTeg.buscarTegPorProgramaUnEstatus(estatus3,
							programa1, fechaInicio, fechaFin);
					valor1 = teg1.size();
					valor2 = teg2.size();
					valor3 = teg3.size();

				}
				/*
				 * Elijo uno de cada cosa
				 */
				else if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					teg1 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus1, tematica1, fechaInicio, fechaFin);
					teg2 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus2, tematica1, fechaInicio, fechaFin);
					teg3 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus3, tematica1, fechaInicio, fechaFin);
					valor1 = teg1.size();
					valor2 = teg2.size();
					valor3 = teg3.size();

				}
				/*
				 * Eligi� programa area pero todos las tematicas
				 */
				else if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {

					teg1 = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, estatus1);
					teg2 = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, estatus2);
					teg3 = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, estatus3);
					valor1 = teg1.size();
					valor2 = teg2.size();
					valor3 = teg3.size();

				}
				/*
				 * Eligi� todo los programas una area y todos los tematicos
				 */

				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")) {

					teg1 = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, estatus1);
					teg2 = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, estatus2);
					teg3 = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, estatus3);
					valor1 = teg1.size();
					valor2 = teg2.size();
					valor3 = teg3.size();

				}
				/*
				 * Eligio todo los programas una area y una tematica
				 */
				else if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")) {

					Tematica tematica1 = servicioTematica
							.buscarTematica(idTematica);

					teg1 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus1, tematica1, fechaInicio, fechaFin);
					teg2 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus2, tematica1, fechaInicio, fechaFin);
					teg3 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus3, tematica1, fechaInicio, fechaFin);
					valor1 = teg1.size();
					valor2 = teg2.size();
					valor3 = teg3.size();

				}
				System.out.println(valor1);
				System.out.println(valor2);
				System.out.println(valor3);
				if ((valor1 == 0) && (valor2 == 0) && (valor3 == 0)) {
					Messagebox
					.show("No hay informacion disponible para esta seleccion",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
					
					
				} else {
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
					+ "SITEG/vistas/reportes/estadisticos/compilados/ReporteEstadisticoDefensa.jasper";
			String reporteImage = rutaUrl
					+ "SITEG/public/imagenes/reportes/";
			System.out.println(reporteSrc);
					List<MasSolicitados> masSolicitados = new ArrayList<MasSolicitados>();
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("programa", cmbPrograma.getValue());
					mapa.put("area", cmbArea.getValue());
					mapa.put("tematica", cmbTematica.getValue());
					mapa.put("logoUcla", reporteImage + "logo ucla.png");
					mapa.put("logoCE", reporteImage + "logo CE.png");
					mapa.put("logoSiteg", reporteImage + "logo.png");

					

					

					//JasperReport jasperReport = (JasperReport) JRLoader
						//	.loadObject(getClass().getResource(
							//		"ReporteEstadisticoDefensa.jasper"));
					JasperReport jasperReport = (JasperReport) JRLoader
					 .loadObject(reporteSrc);
					

					MasSolicitados masSolicita = new MasSolicitados(valor1,
							valor2, valor3);
					masSolicitados.add(masSolicita);
					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									masSolicitados));
					JasperViewer.viewReport(jasperPrint, false);
				}

			}

		}
	}
	/* Metodo que permite limpiar los campos de los filtros de busqueda */
	@Listen("onClick = #btnCancelar")
	public void cancelar() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		dtbInicio.setValue(new Date());
		dtbFin.setValue(new Date());

	}

	/* Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalir")
	public void salirReporteSolicitud() throws JRException {

		wdwReporteDefensa.onClose();

	}
}
