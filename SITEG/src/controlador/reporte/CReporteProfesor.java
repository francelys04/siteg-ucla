package controlador.reporte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;
import modelo.compuesta.Jurado;
import modelo.reporte.ProfesorTeg;
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
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;
import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoProfesorTematica;

@Controller
public class CReporteProfesor extends CGeneral {

	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	private String[] estatusProfesor = { "Tutor", "Comision Evaluadora",
			"Jurado", "Todos" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;

	@Wire
	private Window wdwReporteProfesor;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Jasperreport jstVistaPrevia;
	private static Programa programa1;
	private static AreaInvestigacion area1;
	private static long idarea;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista, tambien se buscan todos los programas
	 * disponibles, adicionando un nuevo item donde se puede seleccionar la
	 * opcion de "Todos", junto a esto se tiene una lista previamente cargada de
	 * manera estatica los estatus o roles del profesor y se llenan los campos
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);

		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusProfesor));

		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);

	}

	/**
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas.
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		try {
			if (cmbPrograma.getValue().equals("Todos")) {
				cmbArea.setDisabled(false);
				cmbArea.setValue("");
				areas = servicioArea.buscarActivos();
				AreaInvestigacion area = new AreaInvestigacion(989, "Todos",
						"", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
				cmbTematica.setValue("");
			} else {
				cmbArea.setDisabled(false);
				cmbArea.setValue("");
				cmbTematica.setValue("");
				programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
				areas = servicioProgramaArea
						.buscarAreasDePrograma(servicioPrograma
								.buscar(programa1.getId()));
				AreaInvestigacion area = new AreaInvestigacion(1001, "Todos",
						"", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
	}

	/**
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		try {
			if (cmbArea.getValue().equals("Todos")) {

				cmbTematica.setValue("Todos");
				cmbTematica.setDisabled(true);
				cmbEstatus.setDisabled(false);

			} else {

				cmbTematica.setDisabled(false);
				cmbTematica.setValue("");
				area1 = (AreaInvestigacion) cmbArea.getSelectedItem()
						.getValue();
				tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
						.buscarArea(area1.getId()));
				Tematica tema = new Tematica(1000, "Todos", "", true, null);
				tematicas.add(tema);
				cmbTematica.setModel(new ListModelList<Tematica>(tematicas));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle.e exception
		}
		idarea = Long.parseLong(cmbArea.getSelectedItem().getId());
	}

	/**
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
		cmbEstatus.setDisabled(false);
	}

	/**
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y tipo de cargo, se generara un pdf donde se muestra una lista de
	 * profesores especificando tanto datos basicos como su rol en el teg de
	 * esta seleccion, la cual esta condicionado, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteProfesor")
	public void generarReporteTEG() throws JRException {

		boolean datosVacios = false;
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		Date fechaInicio = dtbFechaInicio.getValue();
		Date fechaFin = dtbFechaFin.getValue();
		String estatus = cmbEstatus.getValue();
		List<ProfesorTeg> elementos = new ArrayList<ProfesorTeg>();

		/** Mensaje para dar cuando falta un dato */
		if (nombrePrograma.equals("") || nombreArea.equals("")
				|| nombreTematica.equals("") || estatus.equals("")) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		}

		else {
			/** Si las fechas estan malas */
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				Messagebox
						.show("La fecha de fin debe ser posterior a la fecha de inicio",
								"Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				/** buscar por una carrera, un area, una tematica y un rol */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {

					String idTematica1 = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica1));

					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechas(programa1,
									tematica1, fechaInicio, fechaFin);

					for (Teg tegs : teg) {

						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Tutor")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
						}

						if (estatus.equals("Jurado")) {
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
						}

						if (estatus.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}

					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/** buscar por una carrera, un area, Todos las tematica y un rol */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg = servicioTeg.buscarTegDeUnAreaPorDosFechas(
							programa1, area1, fechaInicio, fechaFin);
					for (Teg tegs : teg) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Tutor")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
						}

						if (estatus.equals("Jurado")) {
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
						}

						if (estatus.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}

					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/**
				 * buscar por una carrera, un area, Todos las tematica y todos
				 * los estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg = servicioTeg.buscarTegDeUnAreaPorDosFechas(
							programa1, area1, fechaInicio, fechaFin);

					for (Teg tegs : teg) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Todos")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/**
				 * buscar por una carrera, un area, una tematica y todos los
				 * estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {
					String idTematica = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));

					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg1 = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechas(programa1,
									tematica1, fechaInicio, fechaFin);

					for (Teg tegs : teg1) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Todos")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/**
				 * buscar por una carrera, Todos las area, todas las tematicas y
				 * un estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreArea.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg2 = servicioTeg
							.buscarTegDeUnProgramaPorDosFechas(programa1,
									fechaInicio, fechaFin);

					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Tutor")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
						}

						if (estatus.equals("Jurado")) {
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
						}

						if (estatus.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}

					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/**
				 * buscar por una carrera, Todos las area, Todas las tematicas y
				 * todos los estatus
				 */
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreArea.equals("Todos")
						&& estatus.equals("Todos")) {
					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					List<Teg> teg2 = servicioTeg
							.buscarTegDeUnProgramaPorDosFechas(programa1,
									fechaInicio, fechaFin);

					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Todos")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/**
				 * buscar por Todos las carrera, Todos las Areas, Todas las
				 * tematicas y un estatus
				 */
				if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {
					List<Teg> teg2 = servicioTeg.buscarTodosTegPorDosFechas(
							fechaInicio, fechaFin);

					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Tutor")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
						}

						if (estatus.equals("Jurado")) {
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
						}

						if (estatus.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}

					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}
				/**
				 * buscar por Todos las carrera, Todos las Areas, Todas las
				 * tematicas y todos los estatus
				 */
				if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {
					List<Teg> teg2 = servicioTeg.buscarTodosTegPorDosFechas(
							fechaInicio, fechaFin);

					for (Teg tegs : teg2) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Todos")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}

				/** Todos los programas, una area, una tematica, un estatus */
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {

					String idTematica1 = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica1));

					List<Teg> teg = servicioTeg
							.buscarTegUnaTematicaPorDosFechas(tematica1,
									fechaInicio, fechaFin);

					for (Teg tegs : teg) {

						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Tutor")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
						}

						if (estatus.equals("Jurado")) {
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
						}

						if (estatus.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}

					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}

				/**
				 * Todos los programas, una area, una tematica, todos los
				 * estatus
				 */
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {

					String idTematica = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));

					List<Teg> teg1 = servicioTeg
							.buscarTegUnaTematicaPorDosFechas(tematica1,
									fechaInicio, fechaFin);

					for (Teg tegs : teg1) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Todos")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}

				/**
				 * Todos los programas, una area, todas las tematicas, un
				 * estatus
				 */
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					List<Teg> teg = servicioTeg.buscarTegUnAreaPorDosFechas(
							area1, fechaInicio, fechaFin);
					for (Teg tegs : teg) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Tutor")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
						}

						if (estatus.equals("Jurado")) {
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
						}

						if (estatus.equals("Comision Evaluadora")) {
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}

					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}

				/**
				 * Todos los programas, un area, todas las tematicas, todos los
				 * estatus
				 */
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					List<Teg> teg = servicioTeg.buscarTegUnAreaPorDosFechas(
							area1, fechaInicio, fechaFin);

					for (Teg tegs : teg) {
						Profesor profesorTutor = tegs.getTutor();
						if (estatus.equals("Todos")) {
							elementos.add(new ProfesorTeg(profesorTutor
									.getNombre()
									+ " "
									+ profesorTutor.getApellido(), tegs
									.getTitulo(), "Tutor", tegs.getEstatus()));
							for (Jurado jurado : servicioJurado
									.buscarJuradoDeTeg(tegs)) {
								Profesor profesorJurado = jurado.getProfesor();
								elementos.add(new ProfesorTeg(profesorJurado
										.getNombre()
										+ " "
										+ profesorJurado.getApellido(), tegs
										.getTitulo(), "Jurado - "
										+ jurado.getTipoJurado().getNombre(),
										tegs.getEstatus()));
							}
							for (Profesor profesorComision : servicioProfesor
									.buscarComisionDelTeg(tegs)) {
								elementos.add(new ProfesorTeg(profesorComision
										.getNombre()
										+ " "
										+ profesorComision.getApellido(), tegs
										.getTitulo(), "Comision", tegs
										.getEstatus()));
							}
						}
					}
					if (elementos.size() == 0) {
						datosVacios = true;
					}
				}

				if (!datosVacios) {
					Collections.sort(elementos, new Comparator<ProfesorTeg>() {
						public int compare(ProfesorTeg a, ProfesorTeg b) {
							return a.getNombre().compareTo(b.getNombre());
						}
					});

					Map<String, Object> mapa = new HashMap<String, Object>();
					FileSystemView filesys = FileSystemView.getFileSystemView();

					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estructurados/compilados/RReporteProfesor.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
					mapa.put("programa", cmbPrograma.getValue());
					mapa.put("Fecha", new Date());
					mapa.put("FechaInicio", dtbFechaInicio.getValue());
					mapa.put("FechaFin", dtbFechaFin.getValue());
					mapa.put("Area", cmbArea.getValue());
					mapa.put("Programa", cmbPrograma.getValue());
					mapa.put("Tematica", cmbTematica.getValue());
					mapa.put("Estatus", cmbEstatus.getValue());
					mapa.put("logoUcla", reporteImage + "logo ucla.png");
					mapa.put("logoCE", reporteImage + "logo CE.png");
					mapa.put("logoSiteg", reporteImage + "logo.png");

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									elementos));
					JasperViewer.viewReport(jasperPrint, false);

				} else {

					Messagebox
							.show("No hay informacion disponible para esta seleccion",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);

				}
			}
		}
	}

	/** Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarReporteProfesor")
	public void cancelarReporteProfesor() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		cmbEstatus.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);
	}

	/** Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirReporteProfesor")
	public void salirReporteProfesor() throws JRException {

		cancelarReporteProfesor();
		wdwReporteProfesor.onClose();
	}

}
