package controlador.reporte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.compuesta.Jurado;
import modelo.reporte.ListaTeg;
import modelo.reporte.ProfesorTeg;
import modelo.reporte.Proyecto;
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
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SEstudiante;

@Controller
public class CReporteTrabajos extends CGeneral {
	
	private String[] estatusProyecto = {"Solicitando Registro", "Proyecto Registrado",
			"Comision Asignada", "Factibilidad Evaluada", "Proyecto Factible", "Proyecto No Factible",
			"Proyecto en Desarrollo", "Avances Finalizados", "Todos" };

	private String[] estatusTeg = {"TEG Registrado", "Trabajo en Desarrollo",
			"Revisiones Finalizadas", "Solicitando Defensa", "Jurado Asignado", "Defensa Asignada",
			"TEG Aprobado", "TEG Reprobado", "Todos" };
	
	private String[] estatusAmbos = {"Todos"};
	
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;
	
	@Wire
	private Window wdwReporteTrabajos;
	@Wire
	private Radiogroup rdgEtapa;
	@Wire
	private Radio rdoProyecto;
	@Wire
	private Radio rdoTeg;
	@Wire
	private Radio rdoAmbos;
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
	private static Programa programa1;
	private static AreaInvestigacion area1;
	private static long idarea;
	

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista, tambien se buscan todos los programas
	 * disponibles, adicionando un nuevo item donde se puede seleccionar la
	 * opcion de "Todos", junto a esto se tiene una lista previamente cargada de
	 * manera estatica los estatus de proyecto y se llenan los campos
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
		cmbPrograma.setDisabled(true);
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);

	}

	@Listen("onCheck = #rdgEtapa")
	public void llenarCombo() {
		if (rdoProyecto.isChecked() == true) {
			try {
				cmbEstatus.setModel(new ListModelList<String>(estatusProyecto));
				cmbPrograma.setDisabled(false);
			} catch (Exception e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (rdoTeg.isChecked() == true) {
			try {
				cmbEstatus.setModel(new ListModelList<String>(estatusTeg));
				cmbPrograma.setDisabled(false);
			} catch (Exception e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (rdoAmbos.isChecked() == true) {
			try {
				cmbEstatus.setModel(new ListModelList<String>(estatusAmbos));
				cmbPrograma.setDisabled(false);
			} catch (Exception e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	/*
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
				AreaInvestigacion area = new AreaInvestigacion(989,
						"Todos", "", true);
				areas.add(area);
				cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
			} else {
				cmbArea.setDisabled(false);
				cmbArea.setValue("");
				cmbTematica.setValue("");
				programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
				areas = servicioProgramaArea
						.buscarAreasDePrograma(servicioPrograma
								.buscar(programa1.getId()));
				AreaInvestigacion area = new AreaInvestigacion(1001,
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

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
		cmbEstatus.setDisabled(false);
	}
	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y tipo de cargo, se generara un pdf donde se muestra una lista de
	 * profesores especificando tanto datos basicos como su rol en el teg de
	 * esta seleccion, la cual esta condicionado, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteTrabajos")
	public void generarReporteTrabajos() throws JRException {
		boolean datosVacios = false;
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		Date fechaInicio = dtbFechaInicio.getValue();
		Date fechaFin = dtbFechaFin.getValue();
		String estatus = cmbEstatus.getValue();
		List<Teg> teg = null;
		/*Mensaje para dar cuando falta un dato*/
		if ((cmbPrograma.getValue() == "") || (cmbArea.getValue() == "")
				|| (cmbTematica.getValue() == "")
				|| (cmbEstatus.getValue() == "")
				|| ((rdoProyecto.isChecked() == false)
				&& (rdoTeg.isChecked() == false)
				&& (rdoAmbos.isChecked() == false))) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		}

		else {
			/*Si las fechas estan malas*/
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				Messagebox
						.show("La fecha de inicio debe ser primero que la fecha de fin",
								"Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				/*buscar por una carrera, un area, una tematica y un estatus*/
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {

					String idTematica = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
					
					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					
					teg = servicioTeg
							.buscarTegPorTematicaEstatusPrograma(programa1,
									estatus, tematica1, fechaInicio, fechaFin);
					if (teg.size() == 0) {
						datosVacios = true;
					} 
				}
				/*buscar por una carrera, un area, Todos las tematica y un estatus*/
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
					
					teg = servicioTeg
							.buscarTegPorAreaEstatusPrograma(programa1, area1, estatus, fechaInicio, fechaFin);
					if (teg.size() == 0) {
						datosVacios = true;
					} 
				}
				/*buscar por una carrera, un area, Todos las  tematica y todos los estatus*/
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
					
					if (rdoProyecto.isChecked() == true) {
						try {
							String estatusTeg1 = "Solicitando Registro";
							String estatusTeg2 = "Proyecto Registrado";
							String estatusTeg3 = "Comision Asignada";
							String estatusTeg4 = "Factibilidad Evaluada";
							String estatusTeg5 = "Proyecto Factible";
							String estatusTeg6 = "Proyecto No Factible";
							String estatusTeg7 = "Proyecto en Desarrollo";
							String estatusTeg8 = "Avances Finalizados";
							
							teg = servicioTeg.buscarTegPorVariosEstatusAreaPrograma(programa1, area1, 
									estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, estatusTeg8, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							String estatusTeg1 = "TEG Registrado";
							String estatusTeg2 = "Trabajo en Desarrollo";
							String estatusTeg3 = "Revisiones Finalizadas";
							String estatusTeg4 = "Solicitando Defensa";
							String estatusTeg5 = "Jurado Asignado";
							String estatusTeg6 = "Defensa Asignada";
							String estatusTeg7 = "TEG Aprobado";
							String estatusTeg8 = "TEG Reprobado";
							
							teg = servicioTeg.buscarTegPorVariosEstatusAreaPrograma(programa1, area1, 
									estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, estatusTeg8, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							teg = servicioTeg
									.buscarTegPorAreaPrograma(programa1, area1, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (teg.size() == 0) {
						datosVacios = true;
					} 
				}
				/*buscar por una carrera, un area, una  tematica y todos los estatus*/
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
					
					if (rdoProyecto.isChecked() == true) {
						try {
							String estatusTeg1 = "Solicitando Registro";
							String estatusTeg2 = "Proyecto Registrado";
							String estatusTeg3 = "Comision Asignada";
							String estatusTeg4 = "Factibilidad Evaluada";
							String estatusTeg5 = "Proyecto Factible";
							String estatusTeg6 = "Proyecto No Factible";
							String estatusTeg7 = "Proyecto en Desarrollo";
							String estatusTeg8 = "Avances Finalizados";
							
							teg = servicioTeg
									.buscarTegPorVariosEstatusTematicaPrograma(programa1, tematica1, 
											estatusTeg1, estatusTeg2, estatusTeg3,
											estatusTeg4, estatusTeg5, estatusTeg6,
											estatusTeg7, estatusTeg8,
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							String estatusTeg1 = "TEG Registrado";
							String estatusTeg2 = "Trabajo en Desarrollo";
							String estatusTeg3 = "Revisiones Finalizadas";
							String estatusTeg4 = "Solicitando Defensa";
							String estatusTeg5 = "Jurado Asignado";
							String estatusTeg6 = "Defensa Asignada";
							String estatusTeg7 = "TEG Aprobado";
							String estatusTeg8 = "TEG Reprobado";
							
							teg = servicioTeg
									.buscarTegPorVariosEstatusTematicaPrograma(programa1, tematica1, 
											estatusTeg1, estatusTeg2, estatusTeg3,
											estatusTeg4, estatusTeg5, estatusTeg6,
											estatusTeg7, estatusTeg8,
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							teg = servicioTeg
									.buscarTegPorTematicaPrograma(programa1, tematica1, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (teg.size() == 0) {
						datosVacios = true;
					} 
				}
				/*buscar por una carrera, Todos las area y un estatus*/
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));

					teg = servicioTeg.buscarTegPorProgramaEstatus(
							programa1, estatus, fechaInicio, fechaFin);
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
				/*buscar por una carrera, Todos las area y todos los estatus*/
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {

					String idPrograma = String.valueOf(((Programa) cmbPrograma
							.getSelectedItem().getValue()).getId());
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					
					if (rdoProyecto.isChecked() == true) {
						try {
							String estatusTeg1 = "Solicitando Registro";
							String estatusTeg2 = "Proyecto Registrado";
							String estatusTeg3 = "Comision Asignada";
							String estatusTeg4 = "Factibilidad Evaluada";
							String estatusTeg5 = "Proyecto Factible";
							String estatusTeg6 = "Proyecto No Factible";
							String estatusTeg7 = "Proyecto en Desarrollo";
							String estatusTeg8 = "Avances Finalizados";
							
							teg = servicioTeg
									.buscarTegPorProgramaVariosEstatus(programa1,
											estatusTeg1, estatusTeg2, estatusTeg3,
											estatusTeg4, estatusTeg5, estatusTeg6,
											estatusTeg7, estatusTeg8, 
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							String estatusTeg1 = "TEG Registrado";
							String estatusTeg2 = "Trabajo en Desarrollo";
							String estatusTeg3 = "Revisiones Finalizadas";
							String estatusTeg4 = "Solicitando Defensa";
							String estatusTeg5 = "Jurado Asignado";
							String estatusTeg6 = "Defensa Asignada";
							String estatusTeg7 = "TEG Aprobado";
							String estatusTeg8 = "TEG Reprobado";
							
							teg = servicioTeg
									.buscarTegPorProgramaVariosEstatus(programa1,
											estatusTeg1, estatusTeg2, estatusTeg3,
											estatusTeg4, estatusTeg5, estatusTeg6,
											estatusTeg7, estatusTeg8, 
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							teg = servicioTeg
									.buscarTegPorPrograma(programa1, 
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
				
				/*buscar por Todos las carrera, y un estatus*/
				if (nombrePrograma.equals("Todos") 
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {
					teg = servicioTeg.buscarTegPorEstatus(
							estatus, fechaInicio, fechaFin);
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
				/*buscar por Todos las carrera, y todos los estatus*/
				if (nombrePrograma.equals("Todos") 
						&& nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {

					if (rdoProyecto.isChecked() == true) {
						try {
							String estatusTeg1 = "Solicitando Registro";
							String estatusTeg2 = "Proyecto Registrado";
							String estatusTeg3 = "Comision Asignada";
							String estatusTeg4 = "Factibilidad Evaluada";
							String estatusTeg5 = "Proyecto Factible";
							String estatusTeg6 = "Proyecto No Factible";
							String estatusTeg7 = "Proyecto en Desarrollo";
							String estatusTeg8 = "Avances Finalizados";
							
							teg = servicioTeg
									.buscarTegPorVariosEstatus(
											estatusTeg1, estatusTeg2, estatusTeg3,
											estatusTeg4, estatusTeg5, estatusTeg6,
											estatusTeg7, estatusTeg8, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							String estatusTeg1 = "TEG Registrado";
							String estatusTeg2 = "Trabajo en Desarrollo";
							String estatusTeg3 = "Revisiones Finalizadas";
							String estatusTeg4 = "Solicitando Defensa";
							String estatusTeg5 = "Jurado Asignado";
							String estatusTeg6 = "Defensa Asignada";
							String estatusTeg7 = "TEG Aprobado";
							String estatusTeg8 = "TEG Reprobado";
							
							teg = servicioTeg
									.buscarTegPorVariosEstatus(
											estatusTeg1, estatusTeg2, estatusTeg3,
											estatusTeg4, estatusTeg5, estatusTeg6,
											estatusTeg7, estatusTeg8, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							teg = servicioTeg
									.buscarTegPorFecha(fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
				
				
				/*Todos los programas, una area, una tematica, un estatus*/
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {

					String idTematica1 = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica1));

					teg = servicioTeg
							.buscarTegPorTematicaEstatus(tematica1, estatus,
									fechaInicio, fechaFin);

					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
                
				/*Todos los programas, una area, una tematica, todos los estatus*/
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {
				
					String idTematica = String.valueOf(((Tematica) cmbTematica
							.getSelectedItem().getValue()).getId());
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
						
					if (rdoProyecto.isChecked() == true) {
						try {
							String estatusTeg1 = "Solicitando Registro";
							String estatusTeg2 = "Proyecto Registrado";
							String estatusTeg3 = "Comision Asignada";
							String estatusTeg4 = "Factibilidad Evaluada";
							String estatusTeg5 = "Proyecto Factible";
							String estatusTeg6 = "Proyecto No Factible";
							String estatusTeg7 = "Proyecto en Desarrollo";
							String estatusTeg8 = "Avances Finalizados";
							
							teg = servicioTeg.buscarTegPorTematicaVariosEstatus(estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, estatusTeg8, tematica1, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							String estatusTeg1 = "TEG Registrado";
							String estatusTeg2 = "Trabajo en Desarrollo";
							String estatusTeg3 = "Revisiones Finalizadas";
							String estatusTeg4 = "Solicitando Defensa";
							String estatusTeg5 = "Jurado Asignado";
							String estatusTeg6 = "Defensa Asignada";
							String estatusTeg7 = "TEG Aprobado";
							String estatusTeg8 = "TEG Reprobado";
							
							teg = servicioTeg.buscarTegPorTematicaVariosEstatus(estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, estatusTeg8, tematica1, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							teg = servicioTeg
									.buscarTegUnaTematicaPorDosFechas(tematica1,
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
				
				/*Todos los programas, una area, todas las tematicas, un estatus*/
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));
	
					teg = servicioTeg
							.buscarTegPorAreaEstatus(area1, estatus,
									fechaInicio, fechaFin);
					
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
				
				/*Todos los programas, un area, todas las tematicas, todos los estatus*/
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& nombreTematica.equals("Todos")
						&& estatus.equals("Todos")) {
					String idArea = String.valueOf(((AreaInvestigacion) cmbArea
							.getSelectedItem().getValue()).getId());
					AreaInvestigacion area1 = servicioArea.buscarArea(Long
							.parseLong(idArea));

					if (rdoProyecto.isChecked() == true) {
						try {
							String estatusTeg1 = "Solicitando Registro";
							String estatusTeg2 = "Proyecto Registrado";
							String estatusTeg3 = "Comision Asignada";
							String estatusTeg4 = "Factibilidad Evaluada";
							String estatusTeg5 = "Proyecto Factible";
							String estatusTeg6 = "Proyecto No Factible";
							String estatusTeg7 = "Proyecto en Desarrollo";
							String estatusTeg8 = "Avances Finalizados";
							
							teg = servicioTeg.buscarTegPorAreaVariosEstatus(estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, estatusTeg8, area1, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							String estatusTeg1 = "TEG Registrado";
							String estatusTeg2 = "Trabajo en Desarrollo";
							String estatusTeg3 = "Revisiones Finalizadas";
							String estatusTeg4 = "Solicitando Defensa";
							String estatusTeg5 = "Jurado Asignado";
							String estatusTeg6 = "Defensa Asignada";
							String estatusTeg7 = "TEG Aprobado";
							String estatusTeg8 = "TEG Reprobado";
							
							teg = servicioTeg.buscarTegPorAreaVariosEstatus(estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, estatusTeg8, area1, fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							teg = servicioTeg
									.buscarTegUnAreaPorDosFechas(area1,
											fechaInicio, fechaFin);
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
					if (teg.size() == 0) {
						datosVacios = true;
					}
				}
						
				if (!datosVacios) {
					List<ListaTeg> elementos = new ArrayList<ListaTeg>();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);

						String nombreEstudiantes = "";
						String programa = "";
						for (Estudiante e : estudiantes) {
							nombreEstudiantes += e.getNombre() + " "
									+ e.getApellido() + ". ";
							programa += e.getPrograma().getNombre();
						}

						elementos.add(new ListaTeg(t, nombreEstudiantes, programa));
					}
				
					Collections.sort(elementos, new Comparator<ListaTeg>() {
						public int compare(ListaTeg a, ListaTeg b) {
							return a.getPrograma().compareTo(b.getPrograma());
						}
					});
					
					Map<String, Object> mapa = new HashMap<String, Object>();
					FileSystemView filesys = FileSystemView.getFileSystemView();
					
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estructurados/compilados/RReporteTrabajos1.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
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
					mapa.put("Cantidad", elementos.size());	
					
					if (rdoProyecto.isChecked() == true) {
						try {
							mapa.put("Etapa", "Proyecto");
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoTeg.isChecked() == true) {
						try {
							mapa.put("Etapa", "Trabajo Especial de Grado");
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (rdoAmbos.isChecked() == true) {
						try {
							mapa.put("Etapa", "Proyecto-Trabajo Especial de Grado");
						} catch (Exception e) {
							System.out.println(e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									elementos));
					JasperViewer.viewReport(jasperPrint, false);
				} else {
					Messagebox
							.show("No hay informacion disponible para esta seleccion");
				}
			}

	/* Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarReporteTrabajos")
	public void cancelarReporteTrabajos() throws JRException {
		cmbEstatus.setValue("");
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		cmbPrograma.setDisabled(true);
		cmbArea.setDisabled(true);
		cmbTematica.setDisabled(true);
		cmbEstatus.setDisabled(true);
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		rdoProyecto.setChecked(false);
		rdoTeg.setChecked(false);
		rdoAmbos.setChecked(false);
	}

	/* Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirReporteTrabajos")
	public void salirReporteProyecto() throws JRException {

		cancelarReporteTrabajos();
		wdwReporteTrabajos.onClose();
	}

}
