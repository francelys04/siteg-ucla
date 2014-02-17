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

	@Wire
	private Textbox txtCedulaEstudiante;
	@Wire
	private Textbox txtNombreEstudiante;
	@Wire
	private Textbox txtApellidoEstudiante;
	@Wire
	private Textbox txtProgramaEstudiante;
	@Wire
	private Image imgSolicitandoTutoria;
	@Wire
	private Image imgTutoriaAceptada;
	@Wire
	private Image imgSolicitandoRegistro;
	@Wire
	private Image imgProyectoRegistrado;
	@Wire
	private Image imgComisionEvaluadoraAsignada;
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
	private Image imgJuradoAsignado;
	@Wire
	private Image imgDefensaAsignada;
	@Wire
	private Image imgTrabajoEspecial;

	private static String cedulaRecibida;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		txtCedulaEstudiante.setText(cedulaRecibida);
		Estudiante estudiante = servicioEstudiante
				.buscarEstudiante(cedulaRecibida);
		txtNombreEstudiante.setText(estudiante.getNombre());
		txtApellidoEstudiante.setText(estudiante.getApellido());
		txtProgramaEstudiante.setText(estudiante.getPrograma().getNombre());

		int indiceSolicitud = -1;
		int indiceTeg = -1;
		List<SolicitudTutoria> solicitudTutoria = null;
		List<Teg> teg = null;

		if (servicioSolicitudTutoria.buscarSolicitud(estudiante) != null) {
			solicitudTutoria = servicioSolicitudTutoria.buscarSolicitud(estudiante);
		}
		if (servicioTeg.buscarTegPorEstudiante(estudiante) != null) {
			teg = servicioTeg.buscarTegPorEstudiante(estudiante);
		}

		for (int i = 0; i < solicitudTutoria.size(); ++i) {
			if (solicitudTutoria.get(i).getEstatus().equals("Por Revisar") == true
					|| solicitudTutoria.get(i).getEstatus().equals("Aceptada") == true) {
				indiceSolicitud = i;
			}
		}

		for (int i = 0; i < teg.size(); ++i) {
			if (teg.get(i).getEstatus().equals("Proyecto No Factible") != true
					|| teg.get(i).getEstatus().equals("TEG Reprobado") != true) {
				indiceTeg = i;
			}
		}

		if (solicitudTutoria.isEmpty() == false && indiceSolicitud != -1) {

			if (solicitudTutoria.get(indiceSolicitud).getEstatus()
					.equals("Por Revisar") == true) {

				imgTutoriaAceptada.detach();
				imgSolicitandoRegistro.detach();
				imgProyectoRegistrado.detach();
				imgComisionEvaluadoraAsignada.detach();
				imgProyectoFactible.detach();
				imgRevisandoAvances.detach();
				imgRevisionesFinalizadasProyecto.detach();
				imgRegistrarTrabajo.detach();
				imgRevisionesAvances.detach();
				imgRevisionesFinalizadasTrabajo.detach();
				imgSolicitandoDefensa.detach();
				imgJuradoAsignado.detach();
				imgDefensaAsignada.detach();
				imgTrabajoEspecial.detach();

			} else {
				if (solicitudTutoria.get(indiceSolicitud).getEstatus()
						.equals("Aceptada") == true) {

					if (teg.isEmpty() == false && indiceTeg != -1) {

						if (teg.get(indiceTeg).getEstatus()
								.equals("Solicitando Registro") != true) {

							if (teg.get(indiceTeg).getEstatus()
									.equals("Proyecto Registrado") != true) {

								if (teg.get(indiceTeg).getEstatus()
										.equals("Comision Asignada") != true) {
										
									if (teg.get(indiceTeg).getEstatus()
											.equals("Factibilidad Evaluada") != true) {
	
										if (teg.get(indiceTeg).getEstatus()
												.equals("Proyecto Factible") != true) {
											
											if (teg.get(indiceTeg).getEstatus()
													.equals("Proyecto en Desarrollo") != true) {
	
												if (teg.get(indiceTeg).getEstatus()
														.equals("Avances Finalizados") != true) {
		
													if (teg.get(indiceTeg).getEstatus()
															.equals("TEG Registrado") != true) {
														
														if (teg.get(indiceTeg).getEstatus()
																.equals("Trabajo en Desarrollo") != true) {
		
															if (teg.get(indiceTeg)
																	.getEstatus()
																	.equals("Revisiones Finalizadas") != true) {
			
																if (teg.get(indiceTeg)
																		.getEstatus()
																		.equals("Solicitando Defensa") != true) {
																	
																	if (teg.get(indiceTeg)
																			.getEstatus()
																			.equals("Jurado Asignado") != true) {
			
																		if (teg.get(indiceTeg)
																				.getEstatus()
																				.equals("Defensa Asignada") == true) {
				
																			
																			imgTrabajoEspecial.detach();
																		}
																	
																	}else{
																		
																		imgDefensaAsignada.detach();
																		imgTrabajoEspecial.detach();
																		
																	}
																	
																} else {
																	
																	imgJuradoAsignado.detach();
																	imgDefensaAsignada.detach();
																	imgTrabajoEspecial.detach();
																}
															} else {
																
																imgSolicitandoDefensa.detach();
																imgJuradoAsignado.detach();
																imgDefensaAsignada.detach();
																imgTrabajoEspecial.detach();
															}
														
														}else{
															
															imgRevisionesFinalizadasTrabajo.detach();
															imgSolicitandoDefensa.detach();
															imgJuradoAsignado.detach();
															imgDefensaAsignada.detach();
															imgTrabajoEspecial.detach();
														}
		
													} else {
													
														imgRevisionesAvances.detach();
														imgRevisionesFinalizadasTrabajo.detach();
														imgSolicitandoDefensa.detach();
														imgJuradoAsignado.detach();
														imgDefensaAsignada.detach();
														imgTrabajoEspecial.detach();
													}
												} else {
													
													imgRegistrarTrabajo.detach();
													imgRevisionesAvances.detach();
													imgRevisionesFinalizadasTrabajo.detach();
													imgSolicitandoDefensa.detach();
													imgJuradoAsignado.detach();
													imgDefensaAsignada.detach();
													imgTrabajoEspecial.detach();
												}
											
											}else{
												
												imgRevisionesFinalizadasProyecto.detach();
												imgRegistrarTrabajo.detach();
												imgRevisionesAvances.detach();
												imgRevisionesFinalizadasTrabajo.detach();
												imgSolicitandoDefensa.detach();
												imgJuradoAsignado.detach();
												imgDefensaAsignada.detach();
												imgTrabajoEspecial.detach();
											}
	
										} else {
										
											imgRevisandoAvances.detach();
											imgRevisionesFinalizadasProyecto.detach();
											imgRegistrarTrabajo.detach();
											imgRevisionesAvances.detach();
											imgRevisionesFinalizadasTrabajo.detach();
											imgSolicitandoDefensa.detach();
											imgJuradoAsignado.detach();
											imgDefensaAsignada.detach();
											imgTrabajoEspecial.detach();
										}
	
									}else{

										imgProyectoFactible.detach();
										imgRevisandoAvances.detach();
										imgRevisionesFinalizadasProyecto.detach();
										imgRegistrarTrabajo.detach();
										imgRevisionesAvances.detach();
										imgRevisionesFinalizadasTrabajo.detach();
										imgSolicitandoDefensa.detach();
										imgJuradoAsignado.detach();
										imgDefensaAsignada.detach();
										imgTrabajoEspecial.detach();
									}
								
								}else{
									
									imgProyectoFactible.detach();
									imgRevisandoAvances.detach();
									imgRevisionesFinalizadasProyecto.detach();
									imgRegistrarTrabajo.detach();
									imgRevisionesAvances.detach();
									imgRevisionesFinalizadasTrabajo.detach();
									imgSolicitandoDefensa.detach();
									imgJuradoAsignado.detach();
									imgDefensaAsignada.detach();
									imgTrabajoEspecial.detach();
								}
							} else {

								imgComisionEvaluadoraAsignada.detach();
								imgProyectoFactible.detach();
								imgRevisandoAvances.detach();
								imgRevisionesFinalizadasProyecto.detach();
								imgRegistrarTrabajo.detach();
								imgRevisionesAvances.detach();
								imgRevisionesFinalizadasTrabajo.detach();
								imgSolicitandoDefensa.detach();
								imgJuradoAsignado.detach();
								imgDefensaAsignada.detach();
								imgTrabajoEspecial.detach();
							}
						} else {
							
							imgProyectoRegistrado.detach();
							imgComisionEvaluadoraAsignada.detach();
							imgProyectoFactible.detach();
							imgRevisandoAvances.detach();
							imgRevisionesFinalizadasProyecto.detach();
							imgRegistrarTrabajo.detach();
							imgRevisionesAvances.detach();
							imgRevisionesFinalizadasTrabajo.detach();
							imgSolicitandoDefensa.detach();
							imgJuradoAsignado.detach();
							imgDefensaAsignada.detach();
							imgTrabajoEspecial.detach();
						}

					} else {
						if (teg.isEmpty() == false && indiceTeg != -1) {
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
							imgJuradoAsignado.detach();
							imgComisionEvaluadoraAsignada.detach();
							Messagebox
									.show("Trabajo Especial de Grado No Factible o Reprobado",
											"Información", Messagebox.OK,
											Messagebox.INFORMATION);

						} else {
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
							imgJuradoAsignado.detach();
							imgComisionEvaluadoraAsignada.detach();
						}

					}

				} else {
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
					imgJuradoAsignado.detach();
					imgComisionEvaluadoraAsignada.detach();
					Messagebox
					.show("Tutoria del Proyecto Rechazada",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
					
					
				}

			}

		} else {
			if (teg.isEmpty() == false && indiceSolicitud == -1) {
				if (teg.get(indiceTeg).getEstatus().equals("TEG Aprobado") == true) {
					Messagebox
							.show("Felicidades!! Trabajo Especial de Grado Aprobado!",
									"Información", Messagebox.OK,
									Messagebox.INFORMATION);

				}
			} else {
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
				imgJuradoAsignado.detach();
				Messagebox
				.show("Estudiante apto para realizar un Trabajo Especial de Grado",
						"Información", Messagebox.OK,
						Messagebox.INFORMATION);
			}
		}

	}

	public void recibirCedula(String cedula) {
		cedulaRecibida = cedula;
		Window VConsultarEstatusProyecto = (Window) Executions
				.createComponents(
						"/vistas/transacciones/VConsultarEstatusProyecto.zul", null,
						null);
		VConsultarEstatusProyecto.doModal();

	}

}
