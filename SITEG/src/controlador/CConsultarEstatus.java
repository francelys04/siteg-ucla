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
	private static Estudiante estudiante;
	private static SolicitudTutoria solicitudTutoria;
	private static Teg teg;
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
	private Image imgSolicitandoTutoriaVisible;
	@Wire
	private Image imgTutoriaAceptada;
	@Wire
	private Image imgTutoriaAceptadaVisible;
	@Wire
	private Image imgSolicitandoRegistro;
	@Wire
	private Image imgSolicitandoRegistroVisible;
	@Wire
	private Image imgProyectoRegistrado;
	@Wire
	private Image imgProyectoRegistradoVisible;
	@Wire
	private Image imgComisionEvaluadoraAsignada;
	@Wire
	private Image imgComisionEvaluadoraAsignadaVisible;
	@Wire
	private Image imgProyectoFactible;
	@Wire
	private Image imgProyectoFactibleVisible;
	@Wire
	private Image imgRevisandoAvances;
	@Wire
	private Image imgRevisandoAvancesVisible;
	@Wire
	private Image imgRevisionesFinalizadasProyecto;
	@Wire
	private Image imgRevisionesFinalizadasProyectoVisible;
	@Wire
	private Image imgRegistrarTrabajo;
	@Wire
	private Image imgRegistrarTrabajoVisible;
	@Wire
	private Image imgRevisionesAvances;
	@Wire
	private Image imgRevisionesAvancesVisible;
	@Wire
	private Image imgRevisionesFinalizadasTrabajo;
	@Wire
	private Image imgRevisionesFinalizadasTrabajoVisible;
	@Wire
	private Image imgSolicitandoDefensa;
	@Wire
	private Image imgSolicitandoDefensaVisible;
	@Wire
	private Image imgJuradoAsignado;
	@Wire
	private Image imgJuradoAsignadoVisible;
	@Wire
	private Image imgDefensaAsignada;
	@Wire
	private Image imgDefensaAsignadaVisible;
	@Wire
	private Image imgTrabajoEspecial;
	@Wire
	private Image imgTrabajoEspecialVisible;

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

		solicitudTutoria = servicioSolicitudTutoria.ultimaSolicitud(estudiante);
		teg = servicioTeg.ultimoTeg(estudiante);

		if (solicitudTutoria != null) {

			if (solicitudTutoria.getEstatus().equals("Por Revisar")) {

				imgSolicitandoTutoriaVisible.setVisible(false);
				imgSolicitandoTutoria.setVisible(true);

			} else {

				if (solicitudTutoria.getEstatus().equals("Aceptada")) {

					imgSolicitandoTutoriaVisible.setVisible(false);
					imgTutoriaAceptadaVisible.setVisible(false);
					imgSolicitandoTutoria.setVisible(true);
					imgTutoriaAceptada.setVisible(true);

					if (teg != null) {

						if (teg.getEstatus().equals("Solicitando Registro")) {

							imgSolicitandoRegistroVisible.setVisible(false);
							imgSolicitandoRegistro.setVisible(true);

						} else {

							if (teg.getEstatus().equals("Proyecto Registrado")) {

								imgSolicitandoRegistroVisible.setVisible(false);
								imgProyectoRegistradoVisible.setVisible(false);
								imgSolicitandoRegistro.setVisible(true);
								imgProyectoRegistrado.setVisible(true);

							} else {

								if (teg.getEstatus()
										.equals("Comision Asignada")) {

									imgSolicitandoRegistroVisible
											.setVisible(false);
									imgProyectoRegistradoVisible
											.setVisible(false);
									imgComisionEvaluadoraAsignadaVisible
											.setVisible(false);
									imgSolicitandoRegistro.setVisible(true);
									imgProyectoRegistrado.setVisible(true);
									imgComisionEvaluadoraAsignada
											.setVisible(true);

								} else {

									if (teg.getEstatus().equals(
											"Factibilidad Evaluada")) {

										imgSolicitandoRegistroVisible
												.setVisible(false);
										imgProyectoRegistradoVisible
												.setVisible(false);
										imgComisionEvaluadoraAsignadaVisible
												.setVisible(false);
										imgSolicitandoRegistro.setVisible(true);
										imgProyectoRegistrado.setVisible(true);
										imgComisionEvaluadoraAsignada
												.setVisible(true);

									} else {

										if (teg.getEstatus().equals(
												"Proyecto Factible")) {

											imgSolicitandoRegistroVisible
													.setVisible(false);
											imgProyectoRegistradoVisible
													.setVisible(false);
											imgComisionEvaluadoraAsignadaVisible
													.setVisible(false);
											imgProyectoFactibleVisible
													.setVisible(false);
											imgSolicitandoRegistro
													.setVisible(true);
											imgProyectoRegistrado
													.setVisible(true);
											imgComisionEvaluadoraAsignada
													.setVisible(true);
											imgProyectoFactible
													.setVisible(true);

										} else {

											if (teg.getEstatus().equals(
													"Proyecto en Desarrollo")) {

												imgSolicitandoRegistroVisible
														.setVisible(false);
												imgProyectoRegistradoVisible
														.setVisible(false);
												imgComisionEvaluadoraAsignadaVisible
														.setVisible(false);
												imgProyectoFactibleVisible
														.setVisible(false);
												imgRevisandoAvancesVisible
														.setVisible(false);
												imgSolicitandoRegistro
														.setVisible(true);
												imgProyectoRegistrado
														.setVisible(true);
												imgComisionEvaluadoraAsignada
														.setVisible(true);
												imgProyectoFactible
														.setVisible(true);
												imgRevisandoAvances
														.setVisible(true);

											} else {

												if (teg.getEstatus().equals(
														"Avances Finalizados")) {

													imgSolicitandoRegistroVisible
															.setVisible(false);
													imgProyectoRegistradoVisible
															.setVisible(false);
													imgComisionEvaluadoraAsignadaVisible
															.setVisible(false);
													imgProyectoFactibleVisible
															.setVisible(false);
													imgRevisandoAvancesVisible
															.setVisible(false);
													imgRevisionesFinalizadasProyectoVisible
															.setVisible(false);
													imgSolicitandoRegistro
															.setVisible(true);
													imgProyectoRegistrado
															.setVisible(true);
													imgComisionEvaluadoraAsignada
															.setVisible(true);
													imgProyectoFactible
															.setVisible(true);
													imgRevisandoAvances
															.setVisible(true);
													imgRevisionesFinalizadasProyecto
															.setVisible(true);

												} else {

													if (teg.getEstatus()
															.equals("TEG Registrado")) {

														imgSolicitandoRegistroVisible
																.setVisible(false);
														imgProyectoRegistradoVisible
																.setVisible(false);
														imgComisionEvaluadoraAsignadaVisible
																.setVisible(false);
														imgProyectoFactibleVisible
																.setVisible(false);
														imgRevisandoAvancesVisible
																.setVisible(false);
														imgRevisionesFinalizadasProyectoVisible
																.setVisible(false);
														imgRegistrarTrabajoVisible
																.setVisible(false);
														imgSolicitandoRegistro
																.setVisible(true);
														imgProyectoRegistrado
																.setVisible(true);
														imgComisionEvaluadoraAsignada
																.setVisible(true);
														imgProyectoFactible
																.setVisible(true);
														imgRevisandoAvances
																.setVisible(true);
														imgRevisionesFinalizadasProyecto
																.setVisible(true);
														imgRegistrarTrabajo
																.setVisible(true);

													} else {

														if (teg.getEstatus()
																.equals("Trabajo en Desarrollo")) {

															imgSolicitandoRegistroVisible
																	.setVisible(false);
															imgProyectoRegistradoVisible
																	.setVisible(false);
															imgComisionEvaluadoraAsignadaVisible
																	.setVisible(false);
															imgProyectoFactibleVisible
																	.setVisible(false);
															imgRevisandoAvancesVisible
																	.setVisible(false);
															imgRevisionesFinalizadasProyectoVisible
																	.setVisible(false);
															imgRegistrarTrabajoVisible
																	.setVisible(false);
															imgRevisionesAvancesVisible
																	.setVisible(false);
															imgSolicitandoRegistro
																	.setVisible(true);
															imgProyectoRegistrado
																	.setVisible(true);
															imgComisionEvaluadoraAsignada
																	.setVisible(true);
															imgProyectoFactible
																	.setVisible(true);
															imgRevisandoAvances
																	.setVisible(true);
															imgRevisionesFinalizadasProyecto
																	.setVisible(true);
															imgRegistrarTrabajo
																	.setVisible(true);
															imgRevisionesAvances
																	.setVisible(true);

														} else {

															if (teg.getEstatus()
																	.equals("Revisiones Finalizadas")) {

																imgSolicitandoRegistroVisible
																		.setVisible(false);
																imgProyectoRegistradoVisible
																		.setVisible(false);
																imgComisionEvaluadoraAsignadaVisible
																		.setVisible(false);
																imgProyectoFactibleVisible
																		.setVisible(false);
																imgRevisandoAvancesVisible
																		.setVisible(false);
																imgRevisionesFinalizadasProyectoVisible
																		.setVisible(false);
																imgRegistrarTrabajoVisible
																		.setVisible(false);
																imgRevisionesAvancesVisible
																		.setVisible(false);
																imgRevisionesFinalizadasTrabajoVisible
																		.setVisible(false);
																imgSolicitandoRegistro
																		.setVisible(true);
																imgProyectoRegistrado
																		.setVisible(true);
																imgComisionEvaluadoraAsignada
																		.setVisible(true);
																imgProyectoFactible
																		.setVisible(true);
																imgRevisandoAvances
																		.setVisible(true);
																imgRevisionesFinalizadasProyecto
																		.setVisible(true);
																imgRegistrarTrabajo
																		.setVisible(true);
																imgRevisionesAvances
																		.setVisible(true);
																imgRevisionesFinalizadasTrabajo
																		.setVisible(true);

															} else {

																if (teg.getEstatus()
																		.equals("Solicitando Defensa")) {

																	imgSolicitandoRegistroVisible
																			.setVisible(false);
																	imgProyectoRegistradoVisible
																			.setVisible(false);
																	imgComisionEvaluadoraAsignadaVisible
																			.setVisible(false);
																	imgProyectoFactibleVisible
																			.setVisible(false);
																	imgRevisandoAvancesVisible
																			.setVisible(false);
																	imgRevisionesFinalizadasProyectoVisible
																			.setVisible(false);
																	imgRegistrarTrabajoVisible
																			.setVisible(false);
																	imgRevisionesAvancesVisible
																			.setVisible(false);
																	imgRevisionesFinalizadasTrabajoVisible
																			.setVisible(false);
																	imgSolicitandoDefensaVisible
																			.setVisible(false);
																	imgSolicitandoRegistro
																			.setVisible(true);
																	imgProyectoRegistrado
																			.setVisible(true);
																	imgComisionEvaluadoraAsignada
																			.setVisible(true);
																	imgProyectoFactible
																			.setVisible(true);
																	imgRevisandoAvances
																			.setVisible(true);
																	imgRevisionesFinalizadasProyecto
																			.setVisible(true);
																	imgRegistrarTrabajo
																			.setVisible(true);
																	imgRevisionesAvances
																			.setVisible(true);
																	imgRevisionesFinalizadasTrabajo
																			.setVisible(true);
																	imgSolicitandoDefensa
																			.setVisible(true);

																} else {

																	if (teg.getEstatus()
																			.equals("Jurado Asignado")) {

																		imgSolicitandoRegistroVisible
																				.setVisible(false);
																		imgProyectoRegistradoVisible
																				.setVisible(false);
																		imgComisionEvaluadoraAsignadaVisible
																				.setVisible(false);
																		imgProyectoFactibleVisible
																				.setVisible(false);
																		imgRevisandoAvancesVisible
																				.setVisible(false);
																		imgRevisionesFinalizadasProyectoVisible
																				.setVisible(false);
																		imgRegistrarTrabajoVisible
																				.setVisible(false);
																		imgRevisionesAvancesVisible
																				.setVisible(false);
																		imgRevisionesFinalizadasTrabajoVisible
																				.setVisible(false);
																		imgSolicitandoDefensaVisible
																				.setVisible(false);
																		imgJuradoAsignadoVisible
																				.setVisible(false);
																		imgSolicitandoRegistro
																				.setVisible(true);
																		imgProyectoRegistrado
																				.setVisible(true);
																		imgComisionEvaluadoraAsignada
																				.setVisible(true);
																		imgProyectoFactible
																				.setVisible(true);
																		imgRevisandoAvances
																				.setVisible(true);
																		imgRevisionesFinalizadasProyecto
																				.setVisible(true);
																		imgRegistrarTrabajo
																				.setVisible(true);
																		imgRevisionesAvances
																				.setVisible(true);
																		imgRevisionesFinalizadasTrabajo
																				.setVisible(true);
																		imgSolicitandoDefensa
																				.setVisible(true);
																		imgJuradoAsignado
																				.setVisible(true);

																	} else {

																		if (teg.getEstatus()
																				.equals("Defensa Asignada")) {

																			imgSolicitandoRegistroVisible
																					.setVisible(false);
																			imgProyectoRegistradoVisible
																					.setVisible(false);
																			imgComisionEvaluadoraAsignadaVisible
																					.setVisible(false);
																			imgProyectoFactibleVisible
																					.setVisible(false);
																			imgRevisandoAvancesVisible
																					.setVisible(false);
																			imgRevisionesFinalizadasProyectoVisible
																					.setVisible(false);
																			imgRegistrarTrabajoVisible
																					.setVisible(false);
																			imgRevisionesAvancesVisible
																					.setVisible(false);
																			imgRevisionesFinalizadasTrabajoVisible
																					.setVisible(false);
																			imgSolicitandoDefensaVisible
																					.setVisible(false);
																			imgJuradoAsignadoVisible
																					.setVisible(false);
																			imgDefensaAsignadaVisible
																					.setVisible(false);
																			imgSolicitandoRegistro
																					.setVisible(true);
																			imgProyectoRegistrado
																					.setVisible(true);
																			imgComisionEvaluadoraAsignada
																					.setVisible(true);
																			imgProyectoFactible
																					.setVisible(true);
																			imgRevisandoAvances
																					.setVisible(true);
																			imgRevisionesFinalizadasProyecto
																					.setVisible(true);
																			imgRegistrarTrabajo
																					.setVisible(true);
																			imgRevisionesAvances
																					.setVisible(true);
																			imgRevisionesFinalizadasTrabajo
																					.setVisible(true);
																			imgSolicitandoDefensa
																					.setVisible(true);
																			imgJuradoAsignado
																					.setVisible(true);
																			imgDefensaAsignada
																					.setVisible(true);

																		}

																	}

																}

															}

														}

													}

												}

											}

										}

									}

								}

							}

						}

					}

				} else {

					if (solicitudTutoria.getEstatus().equals("Rechazada")) {

						imgSolicitandoTutoriaVisible.setVisible(false);
						imgSolicitandoTutoria.setVisible(true);

					} else {

						if (solicitudTutoria.getEstatus().equals("Finalizada")) {

							imgSolicitandoTutoriaVisible.setVisible(false);
							imgTutoriaAceptadaVisible.setVisible(false);
							imgSolicitandoTutoria.setVisible(true);
							imgTutoriaAceptada.setVisible(true);

							if (teg != null) {

								if (teg.getEstatus().equals(
										"Proyecto No Factible")) {

									imgSolicitandoRegistroVisible
											.setVisible(false);
									imgProyectoRegistradoVisible
											.setVisible(false);
									imgComisionEvaluadoraAsignadaVisible
											.setVisible(false);
									imgSolicitandoRegistro.setVisible(true);
									imgProyectoRegistrado.setVisible(true);
									imgComisionEvaluadoraAsignada
											.setVisible(true);

								} else {

									if (teg.getEstatus().equals("TEG Aprobado")
											|| teg.getEstatus().equals(
													"TEG Reprobado")) {

										imgSolicitandoRegistroVisible
												.setVisible(false);
										imgProyectoRegistradoVisible
												.setVisible(false);
										imgComisionEvaluadoraAsignadaVisible
												.setVisible(false);
										imgProyectoFactibleVisible
												.setVisible(false);
										imgRevisandoAvancesVisible
												.setVisible(false);
										imgRevisionesFinalizadasProyectoVisible
												.setVisible(false);
										imgRegistrarTrabajoVisible
												.setVisible(false);
										imgRevisionesAvancesVisible
												.setVisible(false);
										imgRevisionesFinalizadasTrabajoVisible
												.setVisible(false);
										imgSolicitandoDefensaVisible
												.setVisible(false);
										imgJuradoAsignadoVisible
												.setVisible(false);
										imgDefensaAsignadaVisible
												.setVisible(false);
										imgTrabajoEspecialVisible
												.setVisible(false);
										imgSolicitandoRegistro.setVisible(true);
										imgProyectoRegistrado.setVisible(true);
										imgComisionEvaluadoraAsignada
												.setVisible(true);
										imgProyectoFactible.setVisible(true);
										imgRevisandoAvances.setVisible(true);
										imgRevisionesFinalizadasProyecto
												.setVisible(true);
										imgRegistrarTrabajo.setVisible(true);
										imgRevisionesAvances.setVisible(true);
										imgRevisionesFinalizadasTrabajo
												.setVisible(true);
										imgSolicitandoDefensa.setVisible(true);
										imgJuradoAsignado.setVisible(true);
										imgDefensaAsignada
												.setVisible(true);
										imgTrabajoEspecial
												.setVisible(true);

									}
								}

							}

						}

					}

				}

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
