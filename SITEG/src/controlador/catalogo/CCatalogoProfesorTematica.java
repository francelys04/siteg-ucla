package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Tematica;
import modelo.compuesta.CondicionPrograma;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CCatalogoProfesorTematica extends CGeneral {

	private static String vistaRecibida;

	private static long programaRecibido;
	private static long tematicaRecibida;
	private static long areaRecibida;

	@Wire
	private Listbox ltbProfesor;

	@Wire
	private Combobox cmbProgramaSolicitud;
	@Wire
	private Window wdwCatalogoProfesorArea;

	@Wire
	private Textbox txtCedulaMostrarProfesor;
	@Wire
	private Textbox txtNombreMostrarProfesor;
	@Wire
	private Textbox txtApellidoMostrarProfesor;
	@Wire
	private Textbox txtCorreoMostrarProfesor;

	/*
	 * Metodo heredado del Controlador CGeneral donde se se buscan todos los
	 * profesores disponibles y se llena la lista del mismo en el componente de
	 * la vista.
	 */
	public void inicializar(Component comp) {

		List<Profesor> profe = llenarprofesores();
		ltbProfesor.setModel(new ListModelList<Profesor>(profe));

	}

	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista, long programa, long tematica, long area) {
		vistaRecibida = vista;
		areaRecibida = area;
		programaRecibido = programa;
		tematicaRecibida = tematica;

	}

	/*
	 * Metodo que permite filtrar los profesores disponibles, mediante el
	 * componente de la lista, donde se podra visualizar la
	 * cedula,nombre,apellido y correo de estas.
	 */
	@Listen("onChange = #txtCedulaMostrarProfesor,#txtNombreMostrarProfesor,#txtApellidoMostrarProfesor,#txtCorreoMostrarProfesor")
	public void filtrarDatosCatalogo() {
		List<Profesor> profesores = llenarprofesores();
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
											.toLowerCase())) {
				profesores2.add(profesor);
			}

		}

		ltbProfesor.setModel(new ListModelList<Profesor>(profesores2));

	}

	/*
	 * Metodo que permite obtener el objeto Profesor al realizar el evento doble
	 * clic sobre un item en especifico en la lista, extrayendo asi su cedula,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbProfesor")
	public void mostrarDatosCatalogo() {

		HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (vistaRecibida == null) {

			vistaRecibida = "/vistas/transacciones/VSolicitarTutor";

		} else {
			if (ltbProfesor.getSelectedCount() != 0) {
				Listitem listItem = ltbProfesor.getSelectedItem();
				Profesor profesorDatosCatalogo = (Profesor) listItem.getValue();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cedula", profesorDatosCatalogo.getCedula());
				String vista = vistaRecibida;
				map.put("vista", vista);
				// if (map2 != null && map2.toString() != "{}") {
				System.out.println(areaRecibida);
				if (areaRecibida != 0) {
					map = map2;
					map.put("cedula", profesorDatosCatalogo.getCedula());
					map.put("vista", vista);
					// System.out.println(map2.toString());
					// System.out.println(map2.get("area"));
					// if (!map2.get("area").equals("Todos"))
					// map.put("area", (Long) map2.get("area"));
					// else
					// map.put("area", (String) map2.get("area"));
					// map.put("tematica", tematicaRecibida);
					// map.put("programa", programaRecibido);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					Executions.sendRedirect("/vistas/arbol.zul");
					wdwCatalogoProfesorArea.onClose();
				} else {

					Sessions.getCurrent().setAttribute("itemsCatalogo", map);

					Window window = (Window) Executions.createComponents(
							"/vistas/transacciones/VSolicitarTutor.zul", null,
							null);
					window.doModal();
					wdwCatalogoProfesorArea.onClose();
				}
			}
		}
	}

	/*
	 * Metodo que permite cargar una lista de profesores dado a una lista de
	 * solicitudes de tutorias donde su estatus es aceptada
	 */
	public List<Profesor> llenarprofesores() {
		List<Profesor> profesores = new ArrayList<Profesor>();
		if (tematicaRecibida != -1) {
			Tematica tema = servicioTematica.buscarTematica(tematicaRecibida);
			profesores = servicioProfesor
					.buscarProfesoresPorTematica(tema);
		} else {
			profesores = servicioProfesor
					.buscarProfesoresPorPrograma(programaRecibido);
			return profesores;
		}

		String variable = "Numero de tutorias por profesor";
		Programa progra = servicioPrograma.buscarPorId(programaRecibido);
		CondicionPrograma cm = buscarCondicionVigenteEspecifica(variable,
				progra);
		List<SolicitudTutoria> st = servicioSolicitudTutoria.buscarAceptadas();
		int valor = cm.getValor();

		List<Profesor> profe = new ArrayList<Profesor>();
		for (int i = 0; i < profesores.size(); i++) {
			Profesor pf = profesores.get(i);
			int contar = 0;

			for (int j = 0; j < st.size(); j++) {

				if (pf.getCedula().compareTo(
						st.get(j).getProfesor().getCedula()) == 0) {

					++contar;

				}
			}
			if (contar < valor) {
				profe.add(pf);
			}

		}
		return profe;

	}

	/* Metodo que permite cerrar la vista y abrir la vista SolicitarTutor */
	@Listen("onClick = #btnSalir")
	public void salir() {
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VSolicitarTutor.zul", null, null);
		window.doModal();

	}

	/* Metodo que permite cerrar la vista y abrir la vista del catalogo */
	@Listen("onClick = #btnSalirReporte")
	public void salirReporte() {

		wdwCatalogoProfesorArea.onClose();

	}

}
