package controlador.reporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Avance;
import modelo.Estudiante;
import modelo.Teg;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CCalificarDefensa;
import controlador.CGeneral;

/**
 * Controlador que permite generar a traves de un listado el reporte de los
 * avances realizados a un proyecto o revisiones realizas a un trabajo especial
 * de grado
 */
@Controller
public class CReporteAvances extends CGeneral {

	CCalificarDefensa ventanarecibida = new CCalificarDefensa();
	private static String titulo;
	String estatusTeg[] = { "Trabajo en Desarrollo", "Revisiones Finalizadas" };
	String estatusProyecto[] = { "Proyecto en Desarrollo",
			"Avances Finalizados" };

	@Wire
	private Window wdwReporteAvances;
	@Wire
	private Listbox ltbReporteItemTeg;
	@Wire
	private Textbox txtEstudianteCalificarDefensa;
	@Wire
	private Textbox txtMostrarFechaCalificar;
	@Wire
	private Textbox txtMostrarTematicaCalificar;
	@Wire
	private Textbox txtMostrarAreaCalificar;
	@Wire
	private Textbox txtMostrarTituloCalificar;
	@Wire
	private Textbox txtMostrarNombreTutorCalificar;
	@Wire
	private Textbox txtMostrarApellidoTutorCalificar;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Radiogroup rdgEtapa;
	@Wire
	private Radio rdoTEG;
	@Wire
	private Radio rdoProyecto;

	/** Metodo heredado del Controlador General */
	@Override
	public void inicializar(Component comp) {

		cmbEstatus.setDisabled(true);

	}

	/**
	 * Metodo que permite dado a esta seleccion, cargar los estatus del teg en
	 * el componente de la vista, con respecto a la etapa previamente
	 * especificado.
	 */
	@Listen("onCheck = #rdgEtapa")
	public void llenarCombo() {

		cmbEstatus.setValue("");
		ltbReporteItemTeg.getItems().clear();

		if (rdoProyecto.isChecked() == true) {
			try {
				cmbEstatus.setDisabled(false);
				cmbEstatus.setModel(new ListModelList<String>(estatusProyecto));
			} catch (Exception e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (rdoTEG.isChecked() == true) {
			try {
				cmbEstatus.setDisabled(false);
				cmbEstatus.setModel(new ListModelList<String>(estatusTeg));
			} catch (Exception e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Metodo que permite dado a el estatus seleccionado, se buscan los tegs
	 * disponibles recorriendolo uno a uno para luego cargar una lista de
	 * estudiantes por teg donde mediante la implementacion del servicio de
	 * busqueda se va obteniendo su nombre y su apellido y se va seteando
	 * temporalmente en la variable estatus del teg para poder visualizarlo en
	 * el componente lista de teg de la vista
	 */
	@Listen("onSelect= #cmbEstatus")
	public List<Teg> buscar() {

		ltbReporteItemTeg.getItems().clear();

		List<Teg> tegs = servicioTeg.buscarTegs(cmbEstatus.getValue());
		for (int i = 0; i < tegs.size(); i++) {
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarEstudiantePorTeg(tegs.get(i));
			String nombre = estudiantes.get(0).getNombre();
			String apellido = estudiantes.get(0).getApellido();
			tegs.get(i).setEstatus(nombre + " " + apellido);
		}
		if (!tegs.isEmpty()) {
			ltbReporteItemTeg.setModel(new ListModelList<Teg>(tegs));
		}
		return tegs;
	}

	/**
	 * Metodo que permite filtrar los tegs disponibles dado el metodo
	 * "buscar()", mediante el componente de la lista, donde se podra visualizar
	 * el nombre y apellido del estudiante, la fecha, la tematica, el area, el
	 * titulo, el nombre y apellido del tutor de estos.
	 */
	@Listen("onChange = #txtEstudianteCalificarDefensa,#txtMostrarFechaCalificar,#txtMostrarTematicaCalificar,#txtMostrarAreaCalificar,#txtMostrarTituloCalificar,#txtMostrarNombreTutorCalificar,#txtMostrarApellidoTutorCalificar")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscar();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante
					.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			teg1.get(i).setEstatus(nombre + " " + apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (servicioEstudiante
					.buscarEstudiantePorTeg(teg)
					.get(0)
					.getNombre()
					.toLowerCase()
					.contains(
							txtEstudianteCalificarDefensa.getValue()
									.toLowerCase())
					&& teg.getFecha()
							.toString()
							.toLowerCase()
							.contains(
									txtMostrarFechaCalificar.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematicaCalificar.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarAreaCalificar.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTituloCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutorCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutorCalificar.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbReporteItemTeg.setModel(new ListModelList<Teg>(teg2));

	}

	/**
	 * Metodo que permite generar un reporte, dado a si es Proyecto o Teg, se
	 * generara un pdf donde se muestra una lista de avances de esta seleccion,
	 * mediante el componente "Jasperreport" donde se mapea una serie de
	 * parametros y una lista previamente cargada que seran los datos que se
	 * muestra en el documento.
	 */
	@Listen("onDoubleClick = #ltbReporteItemTeg")
	public void mostrarDatosCatalogo() {
		if (ltbReporteItemTeg.getItemCount() != 0) {
			Listitem listItem = ltbReporteItemTeg.getSelectedItem();
			if (listItem != null) {
				Teg teg = (Teg) listItem.getValue();
				if (rdoProyecto.isChecked() == true) {
					titulo = "Avances";
					List<Avance> avances = servicioAvance
							.buscarAvancePorTeg(teg);
					FileSystemView filesys = FileSystemView.getFileSystemView();
					JasperReport jasperReport;

					Map p = new HashMap();
					List<Estudiante> estudiantes = servicioEstudiante
							.buscarEstudiantePorTeg(teg);
					List<String> estu = new ArrayList<String>();
					for (int i = 0; i < estudiantes.size(); i++) {
						String nombre = estudiantes.get(i).getNombre();
						String apellido = estudiantes.get(i).getApellido();
						estu.add(nombre + " " + apellido);

					}
					String tutor = teg.getTutor().getNombre() + " "
							+ teg.getTutor().getApellido();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/RAvances.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");
					p.put("tituloPrincipal", titulo);
					p.put("estudiantes", estu);
					p.put("tutor", tutor);
					p.put("tematica", teg.getTematica().getNombre());
					p.put("programa", estudiantes.get(0).getPrograma()
							.getNombre());
					p.put("area", teg.getTematica().getareaInvestigacion()
							.getNombre());

					try {
						jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);
						JasperPrint jasperPrint;
						jasperPrint = JasperFillManager.fillReport(
								jasperReport, p,
								new JRBeanCollectionDataSource(avances));
						JasperViewer.viewReport(jasperPrint, false);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (rdoTEG.isChecked() == true) {
					titulo = "Revisiones";
					List<Avance> avancesTeg = servicioAvance
							.buscarRevisionPorTeg(teg);
					FileSystemView filesys = FileSystemView.getFileSystemView();
					JasperReport jasperReport;

					Map p = new HashMap();
					List<Estudiante> estudiantes = servicioEstudiante
							.buscarEstudiantePorTeg(teg);
					List<String> estu = new ArrayList<String>();
					for (int i = 0; i < estudiantes.size(); i++) {
						String nombre = estudiantes.get(i).getNombre();
						String apellido = estudiantes.get(i).getApellido();
						estu.add(nombre + " " + apellido);

					}
					String tutor = teg.getTutor().getNombre() + " "
							+ teg.getTutor().getApellido();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/RAvances.jasper";

					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");
					p.put("tituloPrincipal", titulo);
					p.put("estudiantes", estu);
					p.put("tutor", tutor);
					p.put("tematica", teg.getTematica().getNombre());
					p.put("programa", estudiantes.get(0).getPrograma()
							.getNombre());
					p.put("area", teg.getTematica().getareaInvestigacion()
							.getNombre());

					try {
						jasperReport = (JasperReport) JRLoader
								.loadObject(reporteSrc);
						JasperPrint jasperPrint;
						jasperPrint = JasperFillManager.fillReport(
								jasperReport, p,
								new JRBeanCollectionDataSource(avancesTeg));
						JasperViewer.viewReport(jasperPrint, false);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
	}

	@Listen("onClick = #btnCancelarReporteAvances")
	public void limpiarCampos() {

		cmbEstatus.setValue("");
		ltbReporteItemTeg.getItems().clear();
		rdgEtapa.setSelectedItem(null);
		cmbEstatus.setDisabled(false);

	}

	/** Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirReporteAvances")
	public void salirActividad() {
		wdwReporteAvances.onClose();
	}

}
