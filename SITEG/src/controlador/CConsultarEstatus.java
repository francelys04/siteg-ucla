package controlador;

import java.util.List;

import modelo.Estudiante;
import modelo.SolicitudTutoria;
import modelo.Teg;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite verificar el estatus en que se encuentra la
 * solicitud de tutoria, proyectro o TEG de cierto estudiante
 */
public class CConsultarEstatus extends CGeneral {

	private static final long serialVersionUID = 8248202520359105018L;
	private static String cedulaRecibida;
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

	/*
	 * Metodo heredado del Controlador CGeneral dondese verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos y listas
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
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
			solicitudTutoria = servicioSolicitudTutoria
					.buscarSolicitud(estudiante);
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

											if (teg.get(indiceTeg)
													.getEstatus()
													.equals("Proyecto en Desarrollo") != true) {

												if (teg.get(indiceTeg)
														.getEstatus()
														.equals("Avances Finalizados") != true) {

													if (teg.get(indiceTeg)
															.getEstatus()
															.equals("TEG Registrado") != true) {

														if (teg.get(indiceTeg)
																.getEstatus()
																.equals("Trabajo en Desarrollo") != true) {

															if (teg.get(
																	indiceTeg)
																	.getEstatus()
																	.equals("Revisiones Finalizadas") != true) {

																if (teg.get(
																		indiceTeg)
																		.getEstatus()
																		.equals("Solicitando Defensa") != true) {

																	if (teg.get(
																			indiceTeg)
																			.getEstatus()
																			.equals("Jurado Asignado") != true) {

																		if (teg.get(
																				indiceTeg)
																				.getEstatus()
																				.equals("Defensa Asignada") == true) {

																			imgTrabajoEspecial
																					.detach();
																		}

																	} else {

																		imgDefensaAsignada
																				.detach();
																		imgTrabajoEspecial
																				.detach();

																	}

																} else {

																	imgJuradoAsignado
																			.detach();
																	imgDefensaAsignada
																			.detach();
																	imgTrabajoEspecial
																			.detach();
																}
															} else {

																imgSolicitandoDefensa
																		.detach();
																imgJuradoAsignado
																		.detach();
																imgDefensaAsignada
																		.detach();
																imgTrabajoEspecial
																		.detach();
															}

														} else {

															imgRevisionesFinalizadasTrabajo
																	.detach();
															imgSolicitandoDefensa
																	.detach();
															imgJuradoAsignado
																	.detach();
															imgDefensaAsignada
																	.detach();
															imgTrabajoEspecial
																	.detach();
														}

													} else {

														imgRevisionesAvances
																.detach();
														imgRevisionesFinalizadasTrabajo
																.detach();
														imgSolicitandoDefensa
																.detach();
														imgJuradoAsignado
																.detach();
														imgDefensaAsignada
																.detach();
														imgTrabajoEspecial
																.detach();
													}
												} else {

													imgRegistrarTrabajo
															.detach();
													imgRevisionesAvances
															.detach();
													imgRevisionesFinalizadasTrabajo
															.detach();
													imgSolicitandoDefensa
															.detach();
													imgJuradoAsignado.detach();
													imgDefensaAsignada.detach();
													imgTrabajoEspecial.detach();
												}

											} else {

												imgRevisionesFinalizadasProyecto
														.detach();
												imgRegistrarTrabajo.detach();
												imgRevisionesAvances.detach();
												imgRevisionesFinalizadasTrabajo
														.detach();
												imgSolicitandoDefensa.detach();
												imgJuradoAsignado.detach();
												imgDefensaAsignada.detach();
												imgTrabajoEspecial.detach();
											}

										} else {

											imgRevisandoAvances.detach();
											imgRevisionesFinalizadasProyecto
													.detach();
											imgRegistrarTrabajo.detach();
											imgRevisionesAvances.detach();
											imgRevisionesFinalizadasTrabajo
													.detach();
											imgSolicitandoDefensa.detach();
											imgJuradoAsignado.detach();
											imgDefensaAsignada.detach();
											imgTrabajoEspecial.detach();
										}

									} else {

										imgProyectoFactible.detach();
										imgRevisandoAvances.detach();
										imgRevisionesFinalizadasProyecto
												.detach();
										imgRegistrarTrabajo.detach();
										imgRevisionesAvances.detach();
										imgRevisionesFinalizadasTrabajo
												.detach();
										imgSolicitandoDefensa.detach();
										imgJuradoAsignado.detach();
										imgDefensaAsignada.detach();
										imgTrabajoEspecial.detach();
									}

								} else {

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
					

				}

			}

		} else {
			if (teg.isEmpty() == false && indiceSolicitud == -1) {
			
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
				
			}
		}

	}

	/*
	 * Metodo que permite obtener la cedula del estudiante que desea consultar
	 * el estatus y ademas permite abrir la vista
	 */
	public void recibirCedula(String cedula) {
		cedulaRecibida = cedula;
		Window VConsultarEstatusProyecto = (Window) Executions
				.createComponents(
						"/vistas/transacciones/VConsultarEstatusProyecto.zul",
						null, null);
		VConsultarEstatusProyecto.doModal();

	}

}
