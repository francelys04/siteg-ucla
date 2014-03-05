package controlador.reporte;

import java.util.*;

import javax.swing.filechooser.FileSystemView;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Categoria;
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
import net.sf.jasperreports.view.JasperViewer;

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

	CCatalogoProfesor catalogo = new CCatalogoProfesor();

	@Wire
	private Window wdwReporteProfesorTematica;
	@Wire
	private Textbox txtCedulaProfesorTematica;
	@Wire
	private Textbox txtNombreProfesorTematica;
	@Wire
	private Textbox txtApellidoProfesorTematica;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {

				txtCedulaProfesorTematica.setValue((String) map.get("cedula"));
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula(txtCedulaProfesorTematica
								.getValue());
				txtNombreProfesorTematica.setValue(profesor.getNombre());
				txtApellidoProfesorTematica.setValue(profesor.getApellido());

			}
			map.clear();
			map = null;
		}

	}

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoProfesorTematica")
	public void buscarProfesor() {

		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
		window.doModal();

		catalogo.recibir("reportes/salidas/VReporteProfesorTematica");

	}

	/*
	 * Metodo que permite generar un reporte, seleccionando un profesor, se
	 * generara un pdf donde se muestra una lista de tematicas asociadas al
	 * profesor de esta seleccion, mediante el componente "Jasperreport" donde
	 * se mapea una serie de parametros y una lista previamente cargada que
	 * seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenrerarReporteProfesorTematica")
	public void generarReporteProfesorTematica() throws JRException {

		if (txtCedulaProfesorTematica.getText().compareTo("") == 0
				|| txtNombreProfesorTematica.getText().compareTo("") == 0
				|| txtApellidoProfesorTematica.getText().compareTo("") == 0) {
			Messagebox.show("Debe seleccionar un profesor", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			Profesor profesor = servicioProfesor
					.buscarProfesorPorCedula(txtCedulaProfesorTematica
							.getValue());
			List<Tematica> tematicasProfesor = servicioTematica
					.buscarTematicasDelProfesor(profesor);

			if (tematicasProfesor.size() != 0) {

				FileSystemView filesys = FileSystemView.getFileSystemView();
				JasperReport jasperReport;
				try {
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/RProfesorTematica.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
					Map p = new HashMap();
					p.put("logoUcla", reporteImage + "logo ucla.png");
					p.put("logoCE", reporteImage + "logo CE.png");
					p.put("logoSiteg", reporteImage + "logo.png");
					
					p.put("cedulaprofesor", txtCedulaProfesorTematica.getValue() );
					p.put("profesor", txtNombreProfesorTematica.getValue() + " " + txtApellidoProfesorTematica.getValue());

					jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);
					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, p, new JRBeanCollectionDataSource(
									tematicasProfesor));
					JasperViewer.viewReport(jasperPrint, false);

				} catch (JRException e) {
					System.out.println(e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				Messagebox.show(
						"No hay informacion disponible para esta seleccion",
						"Informacion", Messagebox.OK, Messagebox.INFORMATION);

			}

		}

	}
	
	
	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 */
	@Listen("onClick = #btnCancelarReporteProfesorTematica")
	public void cancelar() {
		
		txtCedulaProfesorTematica.setValue("");
		txtNombreProfesorTematica.setValue("");
		txtApellidoProfesorTematica.setValue("");
		
		
	}
	
	
	
	
	/* Metodo que permite cerrar la ventana correspondiente al reporte */
	@Listen("onClick = #btnSalirReporteProfesorTematica")
	public void salirReporte() {
		wdwReporteProfesorTematica.onClose();
	}
	
	

}
