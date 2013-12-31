package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.AreaInvestigacion;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SCategoria;
import servicio.SProfesor;
import configuracion.GeneradorBeans;
import servicio.SEstudiante;
import servicio.SSolicitudTutoria;
import servicio.STeg;


public class CConsultarEstatus extends CGeneral {

	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SSolicitudTutoria servicioTutoria = GeneradorBeans.getServicioTutoria();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	
	
	@Wire
	private Textbox txtCedulaEstudiante;
	@Wire
	private Textbox txtNombreEstudiante;
	@Wire
	private Textbox txtApellidoEstudiante;
	@Wire
	private Image imgSolicitandoTutoria;
	@Wire
	private Image imgTutoriaAceptada;
	@Wire
	private Image imgSolicitandoRegistro;
	@Wire
	private Image imgProyectoRegistrado;
	@Wire
	private Image imgProyectoFactible;
	@Wire
	private Image imgRevisandoAvances;
	@Wire
	private Image imgRevisionesFinalizadasProyecto;
	@Wire
	private Image imgRegistrarTrabajo;
	@Wire
	private Image imgRevisionesAvances;
	@Wire
	private Image imgRevisionesFinalizadasTrabajo;
	@Wire
	private Image imgSolicitandoDefensa;
	@Wire
	private Image imgDefensaAsignada;
	@Wire
	private Image imgTrabajoEspecial;
	
	private static String cedulaRecibida;
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
		txtCedulaEstudiante.setText(cedulaRecibida);
		Estudiante estudiante = servicioEstudiante.buscarEstudiante(cedulaRecibida);
		txtNombreEstudiante.setText(estudiante.getNombre());
		txtApellidoEstudiante.setText(estudiante.getApellido());
		
		List<SolicitudTutoria> solicitudTutoria = null;
		List<Teg> teg = null;
		
		if (servicioTutoria.buscarSolicitud(estudiante)!=null){
			solicitudTutoria = servicioTutoria.buscarSolicitud(estudiante);		
		}
		if (servicioTeg.buscarTeg(estudiante)!=null){
			teg = servicioTeg.buscarTeg(estudiante);		
		}
		
		
		if(solicitudTutoria.isEmpty()==false){
			
			if (solicitudTutoria.get(0).getEstatus().equals("Por Revisar")==true){
				
				imgTutoriaAceptada.detach();
				imgSolicitandoRegistro.detach();
				imgProyectoRegistrado.detach();
				imgProyectoFactible.detach();
				imgRevisandoAvances.detach();
				imgRevisionesFinalizadasProyecto.detach();
				imgRegistrarTrabajo.detach();
				imgRevisionesAvances.detach();
				imgRevisionesFinalizadasTrabajo.detach();
				imgSolicitandoDefensa.detach();
				imgDefensaAsignada.detach();
				imgTrabajoEspecial.detach();
			}
			else{
				if (solicitudTutoria.get(0).getEstatus().equals("Aceptada")==true){
					
					if(teg.isEmpty()==false){
									
						if (teg.get(0).getEstatus().equals("Solicitando Proyecto")!=true){
							
							if (teg.get(0).getEstatus().equals("Proyecto Registrado")!=true){
								
								if (teg.get(0).getEstatus().equals("Proyecto Factible")!=true){
									
									if (teg.get(0).getEstatus().equals("Proyecto No Factible")==true){
										
										imgSolicitandoTutoria.detach();
										imgTutoriaAceptada.detach();
										imgSolicitandoRegistro.detach();
										imgProyectoRegistrado.detach();
										imgProyectoFactible.detach();
										imgRevisandoAvances.detach();
										imgRevisionesFinalizadasProyecto.detach();
										imgRegistrarTrabajo.detach();
										imgRevisionesAvances.detach();
										imgRevisionesFinalizadasTrabajo.detach();
										imgSolicitandoDefensa.detach();
										imgDefensaAsignada.detach();
										imgTrabajoEspecial.detach();
										Messagebox.show("El Proyecto de Trabajo Especial de Grado NO es" +
												" factible");
										
									}
									else{
										
										if (teg.get(0).getEstatus().equals("Avances En Proceso")!=true){
											
											if (teg.get(0).getEstatus().equals("Avances Finalizados")!=true){
												
												if (teg.get(0).getEstatus().equals("TEG Registrado")!=true){
													
													if (teg.get(0).getEstatus().equals("Revisiones En Proceso")!=true){
														
														if (teg.get(0).getEstatus().equals("Revisiones Finalizadas")!=true){
															
															if (teg.get(0).getEstatus().equals("Solicitando Defensa")!=true){
																
																if (teg.get(0).getEstatus().equals("Defensa Asignada")!=true){
																
																	if (teg.get(0).getEstatus().equals("TEG Aprobado")==true){
																		
																		Messagebox.show("Felicitaciones! Aprobaste el Trabajo" +
																				" Especial de Grado");
																	}
																	else{
																		imgSolicitandoDefensa.detach();
																		imgDefensaAsignada.detach();
																		imgTrabajoEspecial.detach();
																		Messagebox.show("Trabajo Especial de Grado Reprobado");
																	}
																}
																else{
																	imgTrabajoEspecial.detach();
																}
															}
															else{
																imgDefensaAsignada.detach();
																imgTrabajoEspecial.detach();
															}
														}
														else{
															imgSolicitandoDefensa.detach();
															imgDefensaAsignada.detach();
															imgTrabajoEspecial.detach();
														}
													}
													else{
														imgRevisionesFinalizadasTrabajo.detach();
														imgSolicitandoDefensa.detach();
														imgDefensaAsignada.detach();
														imgTrabajoEspecial.detach();
													}
												}
												else{
													imgRevisionesAvances.detach();
													imgRevisionesFinalizadasTrabajo.detach();
													imgSolicitandoDefensa.detach();
													imgDefensaAsignada.detach();
													imgTrabajoEspecial.detach();
												}
											}
											else{
												imgRegistrarTrabajo.detach();
												imgRevisionesAvances.detach();
												imgRevisionesFinalizadasTrabajo.detach();
												imgSolicitandoDefensa.detach();
												imgDefensaAsignada.detach();
												imgTrabajoEspecial.detach();
											}
										}
										else{
											imgRevisionesFinalizadasProyecto.detach();
											imgRegistrarTrabajo.detach();
											imgRevisionesAvances.detach();
											imgRevisionesFinalizadasTrabajo.detach();
											imgSolicitandoDefensa.detach();
											imgDefensaAsignada.detach();
											imgTrabajoEspecial.detach();
										}
									}
								}
								else{
									imgRevisandoAvances.detach();
									imgRevisionesFinalizadasProyecto.detach();
									imgRegistrarTrabajo.detach();
									imgRevisionesAvances.detach();
									imgRevisionesFinalizadasTrabajo.detach();
									imgSolicitandoDefensa.detach();
									imgDefensaAsignada.detach();
									imgTrabajoEspecial.detach();
								}
								
							}
							else{
								imgProyectoFactible.detach();
								imgRevisandoAvances.detach();
								imgRevisionesFinalizadasProyecto.detach();
								imgRegistrarTrabajo.detach();
								imgRevisionesAvances.detach();
								imgRevisionesFinalizadasTrabajo.detach();
								imgSolicitandoDefensa.detach();
								imgDefensaAsignada.detach();
								imgTrabajoEspecial.detach();
							}
						}
						else{
							imgProyectoRegistrado.detach();
							imgProyectoFactible.detach();
							imgRevisandoAvances.detach();
							imgRevisionesFinalizadasProyecto.detach();
							imgRegistrarTrabajo.detach();
							imgRevisionesAvances.detach();
							imgRevisionesFinalizadasTrabajo.detach();
							imgSolicitandoDefensa.detach();
							imgDefensaAsignada.detach();
							imgTrabajoEspecial.detach();
						}
					
					}
					else{
						imgSolicitandoRegistro.detach();
						imgProyectoRegistrado.detach();
						imgProyectoFactible.detach();
						imgRevisandoAvances.detach();
						imgRevisionesFinalizadasProyecto.detach();
						imgRegistrarTrabajo.detach();
						imgRevisionesAvances.detach();
						imgRevisionesFinalizadasTrabajo.detach();
						imgSolicitandoDefensa.detach();
						imgDefensaAsignada.detach();
						imgTrabajoEspecial.detach();
					}
					
				}
				else{
					imgSolicitandoTutoria.detach();
					imgTutoriaAceptada.detach();
					imgSolicitandoRegistro.detach();
					imgProyectoRegistrado.detach();
					imgProyectoFactible.detach();
					imgRevisandoAvances.detach();
					imgRevisionesFinalizadasProyecto.detach();
					imgRegistrarTrabajo.detach();
					imgRevisionesAvances.detach();
					imgRevisionesFinalizadasTrabajo.detach();
					imgSolicitandoDefensa.detach();
					imgDefensaAsignada.detach();
					imgTrabajoEspecial.detach();
					Messagebox.show("Tutoria del Proyecto Rechazada");
				}
				
			}
		
		}
		else{
			imgSolicitandoTutoria.detach();
			imgTutoriaAceptada.detach();
			imgSolicitandoRegistro.detach();
			imgProyectoRegistrado.detach();
			imgProyectoFactible.detach();
			imgRevisandoAvances.detach();
			imgRevisionesFinalizadasProyecto.detach();
			imgRegistrarTrabajo.detach();
			imgRevisionesAvances.detach();
			imgRevisionesFinalizadasTrabajo.detach();
			imgSolicitandoDefensa.detach();
			imgDefensaAsignada.detach();
			imgTrabajoEspecial.detach();
		}
		
	}
	
	public void recibirCedula (String cedula)
		{
			cedulaRecibida = cedula;
			Window VConsultarEstatusProyecto = (Window) Executions.createComponents(
					"/vistas/reportes/VConsultarEstatusProyecto.zul", null, null);
			VConsultarEstatusProyecto.doModal();
			
			
		}
	
	

	
	
	
	
	
}
