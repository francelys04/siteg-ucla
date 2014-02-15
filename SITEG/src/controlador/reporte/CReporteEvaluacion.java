package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.Proyecto;
import net.sf.jasperreports.engine.JRException;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteEvaluacion extends CGeneral {
	@Wire
	private Window wdwReporteSolicitud;
	@Wire
	private Radiogroup rdgEvaluaciones;
	@Wire
	private Radio rdoFactibilidad;
	@Wire
	private Radio rdoDefensa;
	
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Combobox cmbEstatus;

	@Wire
	private Listbox ltbElegiPrograma;
	@Wire
	private Listbox ltbElegiProgramaArea;
	@Wire
	private Listbox ltbTodo;
	@Wire
	private Listbox ltbElegiArea;

	@Wire
	private Listbox ltbElegiProgramaAreaTematica;

	@Wire
	private Listbox ltbElegiAreaTematica1;
	@Wire
	private Listbox ltbElegiPrograma1;
	@Wire
	private Listbox ltbElegiProgramaArea1;
	@Wire
	private Listbox ltbTodo1;
	@Wire
	private Listbox ltbElegiArea1;

	@Wire
	private Listbox ltbElegiAreaTematica;
	@Wire
	private Listbox ltbElegiProgramaAreaTematica1;

	static Programa programa1 = new Programa();
	static AreaInvestigacion area1 = new AreaInvestigacion();

	
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	private String[] estatusproyecto = { "Proyecto Factible","Proyecto No Factible" ,"Todos" };
	private String[] estatusdefensa = { "TEG Aprobado","TEG Reprobado" ,"Todos" };
	long idArea = 0;
	private static int chequeoEstatus=0;
	static List<Proyecto> elementos1 = new ArrayList<Proyecto>();
	@Override
	public void inicializar(Component comp) {
		
		ltbElegiArea.setVisible(false);

		ltbElegiPrograma.setVisible(false);
		ltbElegiProgramaArea.setVisible(false);
		ltbElegiProgramaAreaTematica.setVisible(false);
		ltbElegiProgramaAreaTematica.setVisible(false);
	
		ltbElegiAreaTematica.setVisible(false);
		ltbTodo.setVisible(false);
		ltbElegiArea.setVisible(false);
		ltbElegiPrograma1.setVisible(false);
		ltbElegiProgramaArea1.setVisible(false);
		ltbElegiProgramaAreaTematica1.setVisible(false);
		ltbElegiProgramaAreaTematica1.setVisible(false);
		ltbElegiAreaTematica1.setVisible(false);
		ltbTodo1.setVisible(false);
		ltbElegiArea1.setVisible(false);
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas = servicioPrograma.buscarActivas();
		programas.add(programaa);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		
	}
	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() {
		 if (cmbPrograma.getValue().equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
			areas = servicioArea.buscarActivos();
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		} else {
			cmbArea.setValue("");
			cmbTematica.setValue("");
			programa1 = (Programa) cmbPrograma.getSelectedItem().getValue();
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(programa1.getId()));
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

		}
	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		if (cmbArea.getValue().equals("Todos")) {

			cmbTematica.setValue("Todos");
			tematicas = servicioTematica.buscarActivos();
			Tematica tema = new Tematica(10000, "Todos", "", true, null);
			tematicas.add(tema);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		} else {
			cmbTematica.setValue("");
			area1 = (AreaInvestigacion) cmbArea.getSelectedItem().getValue();
			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(area1.getId()));
			Tematica tema = new Tematica(10000, "Todos", "", true, null);
			tematicas.add(tema);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		}
	}

	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		if (cmbArea.getValue().equals("Todos")) {
			Tematica tematica = (Tematica) cmbTematica.getSelectedItem()
					.getValue();
			cmbArea.setValue(tematica.getareaInvestigacion().getNombre());
			System.out.println("pase por el todo");

		}
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		idTematica = tematica.getId();
	}
	 @Listen("onCheck = #rdgEvaluaciones")
	    public void llenarCombo() {
	        if (rdoFactibilidad.isChecked() == true) {
	            try {
	                cmbEstatus.setModel(new ListModelList<String>(estatusproyecto));
	            } catch (Exception e) {
	                System.out.println(e);
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        } else if (rdoDefensa.isChecked() == true) {
	            try {
	                cmbEstatus.setModel(new ListModelList<String>(estatusdefensa));
	            } catch (Exception e) {
	                System.out.println(e);
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }

	    }
	@Listen("onClick = #btnGenerar")
	public void generarReporteDefensa() throws JRException {
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		Date fechaInicio = dtbDesde.getValue();
		Date fechaFin = dtbHasta.getValue();
		String nombreestatus = cmbEstatus.getValue();
		ltbElegiArea.setVisible(false);

		ltbElegiPrograma.setVisible(false);
		ltbElegiProgramaArea.setVisible(false);
		ltbElegiProgramaAreaTematica.setVisible(false);
		ltbElegiProgramaAreaTematica.setVisible(false);
	
		ltbElegiAreaTematica.setVisible(false);
		ltbTodo.setVisible(false);
		ltbElegiArea.setVisible(false);
		ltbElegiPrograma1.setVisible(false);
		ltbElegiProgramaArea1.setVisible(false);
		ltbElegiProgramaAreaTematica1.setVisible(false);
		ltbElegiProgramaAreaTematica1.setVisible(false);
		ltbElegiAreaTematica1.setVisible(false);
		ltbTodo1.setVisible(false);
		ltbElegiArea1.setVisible(false);

		Tematica tematica1 = servicioTematica.buscarTematica(idTematica);

		List<Teg> teg = null;
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox
					.show("La fecha de inicio debe ser primero que la fecha de fin",
							"Error", Messagebox.OK, Messagebox.ERROR);
		} else
		if (!nombreestatus.equals("Todos")){
			/*
			 * SELECIONO TODO TODO
			 */
			if (nombrePrograma.equals("Todos")
					&& nombreArea.equals("Todos")
					&& nombreTematica.equals("Todos")
					&& !nombreestatus.equals("Todos")) {
				

				
						teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
								nombreestatus, fechaInicio, fechaFin);
					
					
				
				if (teg.size() != 0) {
					elementos1.clear();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);
						String programa = "";
						String descripcionPrograma = "";
						String correoPrograma = "";
						String cedulaEstudiante = "";
						String nombreEstudiante = "";
						String apellidoEstudiante = "";
						String correoEstudiante = "";
						String direccionEstudiante = "";
						for (Estudiante e : estudiantes) {
							cedulaEstudiante = e.getCedula();
							nombreEstudiante = e.getNombre();
							apellidoEstudiante = e.getApellido();
							correoEstudiante = e.getCorreoElectronico();
							direccionEstudiante = e.getDireccion();
							programa = e.getPrograma().getNombre();
							descripcionPrograma = e.getPrograma()
									.getDescripcion();
							correoPrograma = e.getPrograma().getCorreo();
						}

						String titulo = t.getTitulo();
						String descripcionTeg = t.getDescripcion();
						Date fecha = t.getFecha();
						String area = t.getTematica()
								.getareaInvestigacion().getNombre();
						String descripcionArea = t.getTematica()
								.getareaInvestigacion().getDescripcion();
						String tematica = t.getTematica().getNombre();
						String descripcionTematica = t.getTematica()
								.getDescripcion();
						String cedulaTutor = t.getTutor().getCedula();
						String nombreTutor = t.getTutor().getNombre();
						String apellidoTutor = t.getTutor().getApellido();
						String correoTutor = t.getTutor()
								.getCorreoElectronico();
						String direccionTutor = t.getTutor().getDireccion();
                     if (rdoDefensa.isChecked() == true)
                     {
                    	 System.out.println("pase por defensa");
                    	 System.out.println(teg.size());
                    	 String lugar = t.getDefensa().getLugar();
                    	 Date fecha1 = t.getDefensa().getFecha();
                    	 Date hora = t.getDefensa().getHora();
                    	 String nombreprofesor= t.getDefensa().getProfesor().getNombre() +""+t.getDefensa().getProfesor().getApellido();
                    	 elementos1.add(new Proyecto(titulo, descripcionTeg,
 								fecha, cedulaTutor, nombreTutor,
 								apellidoTutor, correoTutor, direccionTutor,
 								cedulaEstudiante, nombreEstudiante,
 								apellidoEstudiante, correoEstudiante,
 								direccionEstudiante, programa,
 								correoPrograma, descripcionPrograma,
 								tematica, descripcionTematica, area,
 								descripcionArea,fecha1,hora,lugar,nombreprofesor));
                    	 
                     }
                     else
                     {
                    	 Date fecha2 = t.getFactibilidad().getFecha();
                    	 String observacion = t.getFactibilidad().getObservacion();
                    	 String nombreprofesor1 = t.getFactibilidad().getProfesor().getNombre() +""+t.getFactibilidad().getProfesor().getApellido();
						elementos1.add(new Proyecto(titulo, descripcionTeg,
								fecha, cedulaTutor, nombreTutor,
								apellidoTutor, correoTutor, direccionTutor,
								cedulaEstudiante, nombreEstudiante,
								apellidoEstudiante, correoEstudiante,
								direccionEstudiante, programa,
								correoPrograma, descripcionPrograma,
								tematica, descripcionTematica, area,
								descripcionArea,fecha2,observacion , nombreprofesor1));

					}
					}
					 if (rdoDefensa.isChecked() == true)
                     {
					ltbTodo.setModel(new ListModelList<Proyecto>(elementos1));
					ltbTodo.setVisible(true);
                     }
					 else
					 {
						ltbTodo1.setModel(new ListModelList<Proyecto>(elementos1));
						ltbTodo1.setVisible(true);
						 
					 }

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}
			}
			/*
			 * seleccione un programa
			 */
			else if (!nombrePrograma.equals("Todos")
					&& nombreArea.equals("Todos")
					&& nombreTematica.equals("Todos")
					&& !nombreestatus.equals("Todos")) {
				
				
					teg = servicioTeg.buscarTegPorProgramaUnEstatus(
							nombreestatus, programa1, fechaInicio, fechaFin);
					
				
				if (teg.size() != 0) {
					elementos1.clear();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);
						String programa = "";
						String descripcionPrograma = "";
						String correoPrograma = "";
						String cedulaEstudiante = "";
						String nombreEstudiante = "";
						String apellidoEstudiante = "";
						String correoEstudiante = "";
						String direccionEstudiante = "";
						for (Estudiante e : estudiantes) {
							cedulaEstudiante = e.getCedula();
							nombreEstudiante = e.getNombre();
							apellidoEstudiante = e.getApellido();
							correoEstudiante = e.getCorreoElectronico();
							direccionEstudiante = e.getDireccion();
							programa = e.getPrograma().getNombre();
							descripcionPrograma = e.getPrograma()
									.getDescripcion();
							correoPrograma = e.getPrograma().getCorreo();
						}

						String titulo = t.getTitulo();
						String descripcionTeg = t.getDescripcion();
						Date fecha = t.getFecha();
						String area = t.getTematica()
								.getareaInvestigacion().getNombre();
						String descripcionArea = t.getTematica()
								.getareaInvestigacion().getDescripcion();
						String tematica = t.getTematica().getNombre();
						String descripcionTematica = t.getTematica()
								.getDescripcion();
						String cedulaTutor = t.getTutor().getCedula();
						String nombreTutor = t.getTutor().getNombre();
						String apellidoTutor = t.getTutor().getApellido();
						String correoTutor = t.getTutor()
								.getCorreoElectronico();
						String direccionTutor = t.getTutor().getDireccion();
                     if (rdoDefensa.isChecked() == true)
                     {
                    	 String lugar = t.getDefensa().getLugar();
                    	 Date fecha1 = t.getDefensa().getFecha();
                    	 Date hora = t.getDefensa().getHora();
                    	 String nombreprofesor= t.getDefensa().getProfesor().getNombre() +""+t.getDefensa().getProfesor().getApellido();
                    	 elementos1.add(new Proyecto(titulo, descripcionTeg,
 								fecha, cedulaTutor, nombreTutor,
 								apellidoTutor, correoTutor, direccionTutor,
 								cedulaEstudiante, nombreEstudiante,
 								apellidoEstudiante, correoEstudiante,
 								direccionEstudiante, programa,
 								correoPrograma, descripcionPrograma,
 								tematica, descripcionTematica, area,
 								descripcionArea,fecha1,hora,lugar,nombreprofesor));
                    	 
                     }
                     else
                     {
                    	 Date fecha2 = t.getFactibilidad().getFecha();
                    	 String observacion = t.getFactibilidad().getObservacion();
                    	 String nombreprofesor1 = t.getFactibilidad().getProfesor().getNombre() +""+t.getFactibilidad().getProfesor().getApellido();
						elementos1.add(new Proyecto(titulo, descripcionTeg,
								fecha, cedulaTutor, nombreTutor,
								apellidoTutor, correoTutor, direccionTutor,
								cedulaEstudiante, nombreEstudiante,
								apellidoEstudiante, correoEstudiante,
								direccionEstudiante, programa,
								correoPrograma, descripcionPrograma,
								tematica, descripcionTematica, area,
								descripcionArea,fecha2,observacion , nombreprofesor1));

					}
					}
					if (rdoDefensa.isChecked() == true)
                    {
					ltbElegiPrograma.setModel(new ListModelList<Proyecto>(elementos1));
					ltbElegiPrograma.setVisible(true);
                    }
					 else
					 {
						 ltbElegiPrograma1.setModel(new ListModelList<Proyecto>(elementos1));
						 ltbElegiPrograma1.setVisible(true);
						 
					 }
					

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}
			}
			/*
			 * Elijo uno de cada cosa
			 */
			else if (!nombrePrograma.equals("Todos")
					&& !nombreArea.equals("Todos")
					&& !nombreTematica.equals("Todos")
					&& !nombreestatus.equals("Todos")) {
				
				tematica1 = servicioTematica
						.buscarTematicaPorNombre(nombreTematica);
			
					teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									nombreestatus,tematica1, fechaInicio,
									fechaFin);
				

				
				
				if (teg.size() != 0) {
					elementos1.clear();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);
						String programa = "";
						String descripcionPrograma = "";
						String correoPrograma = "";
						String cedulaEstudiante = "";
						String nombreEstudiante = "";
						String apellidoEstudiante = "";
						String correoEstudiante = "";
						String direccionEstudiante = "";
						for (Estudiante e : estudiantes) {
							cedulaEstudiante = e.getCedula();
							nombreEstudiante = e.getNombre();
							apellidoEstudiante = e.getApellido();
							correoEstudiante = e.getCorreoElectronico();
							direccionEstudiante = e.getDireccion();
							programa = e.getPrograma().getNombre();
							descripcionPrograma = e.getPrograma()
									.getDescripcion();
							correoPrograma = e.getPrograma().getCorreo();
						}

						String titulo = t.getTitulo();
						String descripcionTeg = t.getDescripcion();
						Date fecha = t.getFecha();
						String area = t.getTematica()
								.getareaInvestigacion().getNombre();
						String descripcionArea = t.getTematica()
								.getareaInvestigacion().getDescripcion();
						String tematica = t.getTematica().getNombre();
						String descripcionTematica = t.getTematica()
								.getDescripcion();
						String cedulaTutor = t.getTutor().getCedula();
						String nombreTutor = t.getTutor().getNombre();
						String apellidoTutor = t.getTutor().getApellido();
						String correoTutor = t.getTutor()
								.getCorreoElectronico();
						String direccionTutor = t.getTutor().getDireccion();
                     if (rdoDefensa.isChecked() == true)
                     {
                    	 String lugar = t.getDefensa().getLugar();
                    	 Date fecha1 = t.getDefensa().getFecha();
                    	 Date hora = t.getDefensa().getHora();
                    	 String nombreprofesor= t.getDefensa().getProfesor().getNombre() +""+t.getDefensa().getProfesor().getApellido();
                    	 elementos1.add(new Proyecto(titulo, descripcionTeg,
 								fecha, cedulaTutor, nombreTutor,
 								apellidoTutor, correoTutor, direccionTutor,
 								cedulaEstudiante, nombreEstudiante,
 								apellidoEstudiante, correoEstudiante,
 								direccionEstudiante, programa,
 								correoPrograma, descripcionPrograma,
 								tematica, descripcionTematica, area,
 								descripcionArea,fecha1,hora,lugar,nombreprofesor));
                    	 
                     }
                     else
                     {
                    	 Date fecha2 = t.getFactibilidad().getFecha();
                    	 String observacion = t.getFactibilidad().getObservacion();
                    	 String nombreprofesor1 = t.getFactibilidad().getProfesor().getNombre() +""+t.getFactibilidad().getProfesor().getApellido();
						elementos1.add(new Proyecto(titulo, descripcionTeg,
								fecha, cedulaTutor, nombreTutor,
								apellidoTutor, correoTutor, direccionTutor,
								cedulaEstudiante, nombreEstudiante,
								apellidoEstudiante, correoEstudiante,
								direccionEstudiante, programa,
								correoPrograma, descripcionPrograma,
								tematica, descripcionTematica, area,
								descripcionArea,fecha2,observacion , nombreprofesor1));

					}
					}
					if (rdoDefensa.isChecked() == true)
                    {
					ltbElegiProgramaAreaTematica.setModel(new ListModelList<Proyecto>(elementos1));
					ltbElegiProgramaAreaTematica.setVisible(true);
                    }
					 else
					 {
						 ltbElegiProgramaAreaTematica1.setModel(new ListModelList<Proyecto>(elementos1));
						 ltbElegiProgramaAreaTematica1.setVisible(true);
						 
					 }
					

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}
			}
			/*
			 * Eligió programa area pero todos las tematicas
			 */
			else if (!nombrePrograma.equals("Todos")
					&& !nombreArea.equals("Todos")
					&& nombreTematica.equals("Todos")
					&& !nombreestatus.equals("Todos")) {
				
					teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, nombreestatus);
					
				
				if (teg.size() != 0) {
					elementos1.clear();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);
						String programa = "";
						String descripcionPrograma = "";
						String correoPrograma = "";
						String cedulaEstudiante = "";
						String nombreEstudiante = "";
						String apellidoEstudiante = "";
						String correoEstudiante = "";
						String direccionEstudiante = "";
						for (Estudiante e : estudiantes) {
							cedulaEstudiante = e.getCedula();
							nombreEstudiante = e.getNombre();
							apellidoEstudiante = e.getApellido();
							correoEstudiante = e.getCorreoElectronico();
							direccionEstudiante = e.getDireccion();
							programa = e.getPrograma().getNombre();
							descripcionPrograma = e.getPrograma()
									.getDescripcion();
							correoPrograma = e.getPrograma().getCorreo();
						}

						String titulo = t.getTitulo();
						String descripcionTeg = t.getDescripcion();
						Date fecha = t.getFecha();
						String area = t.getTematica()
								.getareaInvestigacion().getNombre();
						String descripcionArea = t.getTematica()
								.getareaInvestigacion().getDescripcion();
						String tematica = t.getTematica().getNombre();
						String descripcionTematica = t.getTematica()
								.getDescripcion();
						String cedulaTutor = t.getTutor().getCedula();
						String nombreTutor = t.getTutor().getNombre();
						String apellidoTutor = t.getTutor().getApellido();
						String correoTutor = t.getTutor()
								.getCorreoElectronico();
						String direccionTutor = t.getTutor().getDireccion();
                     if (rdoDefensa.isChecked() == true)
                     {
                    	 String lugar = t.getDefensa().getLugar();
                    	 Date fecha1 = t.getDefensa().getFecha();
                    	 Date hora = t.getDefensa().getHora();
                    	 String nombreprofesor= t.getDefensa().getProfesor().getNombre() +""+t.getDefensa().getProfesor().getApellido();
                    	 elementos1.add(new Proyecto(titulo, descripcionTeg,
 								fecha, cedulaTutor, nombreTutor,
 								apellidoTutor, correoTutor, direccionTutor,
 								cedulaEstudiante, nombreEstudiante,
 								apellidoEstudiante, correoEstudiante,
 								direccionEstudiante, programa,
 								correoPrograma, descripcionPrograma,
 								tematica, descripcionTematica, area,
 								descripcionArea,fecha1,hora,lugar,nombreprofesor));
                    	 
                     }
                     else
                     {
                    	 Date fecha2 = t.getFactibilidad().getFecha();
                    	 String observacion = t.getFactibilidad().getObservacion();
                    	 String nombreprofesor1 = t.getFactibilidad().getProfesor().getNombre() +""+t.getFactibilidad().getProfesor().getApellido();
						elementos1.add(new Proyecto(titulo, descripcionTeg,
								fecha, cedulaTutor, nombreTutor,
								apellidoTutor, correoTutor, direccionTutor,
								cedulaEstudiante, nombreEstudiante,
								apellidoEstudiante, correoEstudiante,
								direccionEstudiante, programa,
								correoPrograma, descripcionPrograma,
								tematica, descripcionTematica, area,
								descripcionArea,fecha2,observacion , nombreprofesor1));

					}
					}
					if (rdoDefensa.isChecked() == true)
                    {
					ltbElegiProgramaArea.setModel(new ListModelList<Proyecto>(elementos1));
					ltbElegiProgramaArea.setVisible(true);
                    }
					 else
					 {
						 ltbElegiProgramaArea1.setModel(new ListModelList<Proyecto>(elementos1));
						 ltbElegiProgramaArea1.setVisible(true);
						 
					 }
					

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}

			}
			/*
			 * Eligió todo los programas una area y todos los tematicos
			 */

			else if (nombrePrograma.equals("Todos")
					&& !nombreArea.equals("Todos")
					&& nombreTematica.equals("Todos")
					&& !nombreestatus.equals("Todos")) {
				
					teg = servicioTeg.buscarTegsSegunAreaUnEstatus(area1,
							fechaInicio, fechaFin, nombreestatus);
					
				
				if (teg.size() != 0) {
					elementos1.clear();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);
						String programa = "";
						String descripcionPrograma = "";
						String correoPrograma = "";
						String cedulaEstudiante = "";
						String nombreEstudiante = "";
						String apellidoEstudiante = "";
						String correoEstudiante = "";
						String direccionEstudiante = "";
						for (Estudiante e : estudiantes) {
							cedulaEstudiante = e.getCedula();
							nombreEstudiante = e.getNombre();
							apellidoEstudiante = e.getApellido();
							correoEstudiante = e.getCorreoElectronico();
							direccionEstudiante = e.getDireccion();
							programa = e.getPrograma().getNombre();
							descripcionPrograma = e.getPrograma()
									.getDescripcion();
							correoPrograma = e.getPrograma().getCorreo();
						}

						String titulo = t.getTitulo();
						String descripcionTeg = t.getDescripcion();
						Date fecha = t.getFecha();
						String area = t.getTematica()
								.getareaInvestigacion().getNombre();
						String descripcionArea = t.getTematica()
								.getareaInvestigacion().getDescripcion();
						String tematica = t.getTematica().getNombre();
						String descripcionTematica = t.getTematica()
								.getDescripcion();
						String cedulaTutor = t.getTutor().getCedula();
						String nombreTutor = t.getTutor().getNombre();
						String apellidoTutor = t.getTutor().getApellido();
						String correoTutor = t.getTutor()
								.getCorreoElectronico();
						String direccionTutor = t.getTutor().getDireccion();
                     if (rdoDefensa.isChecked() == true)
                     {
                    	 String lugar = t.getDefensa().getLugar();
                    	 Date fecha1 = t.getDefensa().getFecha();
                    	 Date hora = t.getDefensa().getHora();
                    	 String nombreprofesor= t.getDefensa().getProfesor().getNombre() +""+t.getDefensa().getProfesor().getApellido();
                    	 elementos1.add(new Proyecto(titulo, descripcionTeg,
 								fecha, cedulaTutor, nombreTutor,
 								apellidoTutor, correoTutor, direccionTutor,
 								cedulaEstudiante, nombreEstudiante,
 								apellidoEstudiante, correoEstudiante,
 								direccionEstudiante, programa,
 								correoPrograma, descripcionPrograma,
 								tematica, descripcionTematica, area,
 								descripcionArea,fecha1,hora,lugar,nombreprofesor));
                    	 
                     }
                     else
                     {
                    	 Date fecha2 = t.getFactibilidad().getFecha();
                    	 String observacion = t.getFactibilidad().getObservacion();
                    	 String nombreprofesor1 = t.getFactibilidad().getProfesor().getNombre() +""+t.getFactibilidad().getProfesor().getApellido();
						elementos1.add(new Proyecto(titulo, descripcionTeg,
								fecha, cedulaTutor, nombreTutor,
								apellidoTutor, correoTutor, direccionTutor,
								cedulaEstudiante, nombreEstudiante,
								apellidoEstudiante, correoEstudiante,
								direccionEstudiante, programa,
								correoPrograma, descripcionPrograma,
								tematica, descripcionTematica, area,
								descripcionArea,fecha2,observacion , nombreprofesor1));

					}
					}
					if (rdoDefensa.isChecked() == true)
                    {
					ltbElegiProgramaArea.setModel(new ListModelList<Proyecto>(elementos1));
					ltbElegiProgramaArea.setVisible(true);
                    }
					 else
					 {
						 ltbElegiProgramaArea1.setModel(new ListModelList<Proyecto>(elementos1));
						 ltbElegiProgramaArea1.setVisible(true);
						 
					 }
					
					

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}

			
			}
			/*
			 * Eligio todo los programas una area y una tematica
			 */
			else if (nombrePrograma.equals("Todos")
					&& !nombreArea.equals("Todos")
					&& !nombreTematica.equals("Todos")
					&& !nombreestatus.equals("Todos")) {
				
				tematica1 = servicioTematica
						.buscarTematicaPorNombre(nombreTematica);
				
					teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									nombreestatus, tematica1, fechaInicio,
									fechaFin);
					
			
				if (teg.size() != 0) {
					elementos1.clear();
					for (Teg t : teg) {
						List<Estudiante> estudiantes = servicioEstudiante
								.buscarEstudiantePorTeg(t);
						String programa = "";
						String descripcionPrograma = "";
						String correoPrograma = "";
						String cedulaEstudiante = "";
						String nombreEstudiante = "";
						String apellidoEstudiante = "";
						String correoEstudiante = "";
						String direccionEstudiante = "";
						for (Estudiante e : estudiantes) {
							cedulaEstudiante = e.getCedula();
							nombreEstudiante = e.getNombre();
							apellidoEstudiante = e.getApellido();
							correoEstudiante = e.getCorreoElectronico();
							direccionEstudiante = e.getDireccion();
							programa = e.getPrograma().getNombre();
							descripcionPrograma = e.getPrograma()
									.getDescripcion();
							correoPrograma = e.getPrograma().getCorreo();
						}

						String titulo = t.getTitulo();
						String descripcionTeg = t.getDescripcion();
						Date fecha = t.getFecha();
						String area = t.getTematica()
								.getareaInvestigacion().getNombre();
						String descripcionArea = t.getTematica()
								.getareaInvestigacion().getDescripcion();
						String tematica = t.getTematica().getNombre();
						String descripcionTematica = t.getTematica()
								.getDescripcion();
						String cedulaTutor = t.getTutor().getCedula();
						String nombreTutor = t.getTutor().getNombre();
						String apellidoTutor = t.getTutor().getApellido();
						String correoTutor = t.getTutor()
								.getCorreoElectronico();
						String direccionTutor = t.getTutor().getDireccion();
                     if (rdoDefensa.isChecked() == true)
                     {
                    	 String lugar = t.getDefensa().getLugar();
                    	 Date fecha1 = t.getDefensa().getFecha();
                    	 Date hora = t.getDefensa().getHora();
                    	 String nombreprofesor= t.getDefensa().getProfesor().getNombre() +""+t.getDefensa().getProfesor().getApellido();
                    	 elementos1.add(new Proyecto(titulo, descripcionTeg,
 								fecha, cedulaTutor, nombreTutor,
 								apellidoTutor, correoTutor, direccionTutor,
 								cedulaEstudiante, nombreEstudiante,
 								apellidoEstudiante, correoEstudiante,
 								direccionEstudiante, programa,
 								correoPrograma, descripcionPrograma,
 								tematica, descripcionTematica, area,
 								descripcionArea,fecha1,hora,lugar,nombreprofesor));
                    	 
                     }
                     else
                     {
                    	 Date fecha2 = t.getFactibilidad().getFecha();
                    	 String observacion = t.getFactibilidad().getObservacion();
                    	 String nombreprofesor1 = t.getFactibilidad().getProfesor().getNombre() +""+t.getFactibilidad().getProfesor().getApellido();
						elementos1.add(new Proyecto(titulo, descripcionTeg,
								fecha, cedulaTutor, nombreTutor,
								apellidoTutor, correoTutor, direccionTutor,
								cedulaEstudiante, nombreEstudiante,
								apellidoEstudiante, correoEstudiante,
								direccionEstudiante, programa,
								correoPrograma, descripcionPrograma,
								tematica, descripcionTematica, area,
								descripcionArea,fecha2,observacion , nombreprofesor1));

					}
					}
					if (rdoDefensa.isChecked() == true)
                    {
					ltbElegiAreaTematica.setModel(new ListModelList<Proyecto>(elementos1));
					ltbElegiAreaTematica.setVisible(true);
                    }
					 else
					 {
						 ltbElegiAreaTematica1.setModel(new ListModelList<Proyecto>(elementos1));
						 ltbElegiAreaTematica1.setVisible(true);
						 
					 }
					
					

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}
			}

		}
		

	

	}
		
		
	
	
	

}
