package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Profesor;
import net.sf.jasperreports.engine.JRException;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/***
 * Controlador asociado a la vista catalogo profesor que permite mostrar los
 * profesores disponibles a traves de un listado
 */
@Controller
public class CCatalogoProfesor extends CGeneral {

	private static String vistaRecibida;
	private static boolean botonApagado = false;
	public static List<Profesor> profesores;
	public static boolean variable2 = false;
	public static boolean esDirector = false;

	@Wire
	private Listbox ltbProfesor;
	@Wire
	private Window wdwCatalogoProfesor;
	@Wire
	private Window wdwCatalogoDirectorPrograma;
	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;
	@Wire
	private Textbox txtCategoriaMostrarProfesor;
	@Wire
	private Button btnSalir;
	@Wire
	private Button btnImprimir;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el map
	 * recibido del catalogo exista y se llenan las listas correspondientes de
	 * la vista dado una condicional, que si se cumple se mostrara los
	 * profesores sin usuarios sino todos los profesores activos y sino todos
	 * los profesores activos.
	 */
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		ltbProfesor.setModel(new ListModelList<Profesor>(profesores));
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			System.out.println("mapa");
			if (map.get("usuario") != null) {
				System.out.println("usuario" + map.toString());
				List<Profesor> profesores1 = servicioProfesor
						.buscarProfesorSinUsuario();
				ltbProfesor.setModel(new ListModelList<Profesor>(profesores1));
				variable2 = true;

			}

			if (map.get("director") != null) {
				System.out.println("director" + map.toString());
				esDirector = true;
				List<Profesor> profesores1 = servicioProfesor
						.buscarProfesoresSinPrograma();
				ltbProfesor.setModel(new ListModelList<Profesor>(profesores1));
			}

		} else {
			List<Profesor> profesores1 = servicioProfesor.buscarActivos();
			ltbProfesor.setModel(new ListModelList<Profesor>(profesores1));

		}
		Selectors.wireComponents(comp, this, false);
	}

	/**
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 * @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;
	}

	/**
	 * Metodo que permite filtrar los profesores disponibles, dado a la
	 * condicional de la variable booleana "variable" si es igual a "true" se
	 * mostraran los profesores sin usuario, si la variable booleana
	 * "esDirector" es igual a "true" se mostrara los profesores sin programa,
	 * sino si es "false" seran todos los profesores activos, mediante el
	 * componente de la lista, donde se podra visualizar la cedula, nombre,
	 * apellido, correo y categoria
	 */
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCategoriaMostrarProfesor,#txtCorreoMostrarProfesor")
	public void filtrarDatosCatalogo() {
		if (variable2 == true) {
			profesores = servicioProfesor.buscarProfesorSinUsuario();
		} else {
			if (esDirector) {
				profesores = servicioProfesor.buscarProfesoresSinPrograma();
			} else {
				profesores = servicioProfesor.buscarActivos();
			}
		}
		List<Profesor> profesores2 = new ArrayList<Profesor>();

		for (Profesor profesor : profesores) {
			if (profesor
					.getCedula()
					.toLowerCase()
					.contains(txtCedulaMostrarProfesor.getValue().toLowerCase())
					&& profesor
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarProfesor.getValue()
											.toLowerCase())
					&& profesor
							.getCategoria()
							.getNombre()
							.toLowerCase()
							.contains(
									txtCategoriaMostrarProfesor.getValue()
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	/**
	 * Metodo que permite obtener el objeto Profesor al realizar el evento doble
	 * clic sobre un item en especifico en la lista, extrayendo asi su cedula,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		try {
			if (vistaRecibida == null) {

				vistaRecibida = "maestros/VProfesor";

			} else {
				if (ltbProfesor.getSelectedCount() != 0) {
					Listitem listItem = ltbProfesor.getSelectedItem();
					Profesor profesorDatosCatalogo = (Profesor) listItem
							.getValue();

					HashMap<String, Object> map = new HashMap<String, Object>();
					HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
							.getCurrent().getAttribute("itemsCatalogo");

					if (map2 != null)
						map = map2;
					map.put("cedula", profesorDatosCatalogo.getCedula());
					String vista = vistaRecibida;
					map.put("vista", vista);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					Executions.sendRedirect("/vistas/arbol.zul");
					wdwCatalogoProfesor.onClose();
				}
			}
		} catch (NullPointerException e) {
		}

	}

	/**
	 * Metodo que permite generar una lista de los profesores que se encuentran
	 * activos en el sistema mediante el componente "Jasperreport"
	 */
	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<Profesor> profesores = servicioProfesor.buscarActivos();

		if (profesores.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/RProfesores.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								profesores));
				JasperViewer.viewReport(jasperPrint, false);

			} catch (JRException e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Messagebox.show("No hay informacion disponible", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
		}

	}

	/** Metodo que permite cerrar la ventana correspondiente al catalogo */
	@Listen("onClick = #btnSalirCatalogoProfesor")
	public void salirCatalogo() {
		wdwCatalogoProfesor.onClose();
	}

	/** Metodo que permite cerrar la ventana correspondiente al catalogo */
	@Listen("onClick = #btnSalirCatalogoDirector")
	public void salirCatalogoDirector() {
		wdwCatalogoDirectorPrograma.onClose();
	}

}
