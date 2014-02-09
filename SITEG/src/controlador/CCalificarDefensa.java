package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Mencion;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.compuesta.ItemDefensa;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SDefensa;
import servicio.SEstudiante;
import servicio.SItemDefensa;
import servicio.SLapso;
import servicio.SMencion;
import servicio.SProgramaItem;
import servicio.STeg;
import configuracion.GeneradorBeans;

@Controller
public class CCalificarDefensa extends CGeneral {

	@Wire
	private Datebox dtbfecha;
	@Wire
	private Textbox txtProgramaCalificar;
	@Wire
	private Textbox txtAreaCalificar;
	@Wire
	private Textbox txtTematicaCalificar;
	@Wire
	private Textbox txtTituloCalificar;
	@Wire
	private Listbox ltbEstudiantesCalificar;
	@Wire
	private Textbox txtNombreTutorCalificarDefensa;
	@Wire
	private Textbox txtApellidoTutorCalificarDefensa;
	@Wire
	private Radiogroup rdgCalificacion;
	@Wire
	private Radio rdoAprobado;
	@Wire
	private Radio rdoReprobado;
	@Wire
	private Combobox cmbMencionTeg;
	@Wire
	private Window wdwCalificarDefensa;

	@Wire
	private Listbox ltbitem;

	private static String vistaRecibida;
	private static long auxId;
	private static boolean dejeenblanco = false;
	private static Teg teg1;

	private static Programa p;
	ArrayList<Boolean> valor2 = new ArrayList<Boolean>();
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstu = GeneradorBeans.getServicioEstudiante();
	SProgramaItem servicioProgratem = GeneradorBeans.getServicioProgramaItem();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SDefensa serviciodefensa = GeneradorBeans.getServicioDefensa();
	SItemDefensa servicioItem = GeneradorBeans.getServicioItemDefensa();
	SMencion servicioMencion = GeneradorBeans.getServicioMencion();

	@Override
	public void inicializar(Component comp) {

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");
		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(codigo);
				List<Estudiante> estudiante = servicioEstu
						.buscarEstudiantePorTeg(teg2);
				if (estudiante.size() != 0) {
					p = estudiante.get(0).getPrograma();

				}
				txtProgramaCalificar.setValue(p.getNombre());
				txtTituloCalificar.setValue(teg2.getTitulo());
				txtAreaCalificar.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaCalificar.setValue(teg2.getTematica().getNombre());
				txtNombreTutorCalificarDefensa.setValue(teg2.getTutor()
						.getNombre());
				txtApellidoTutorCalificarDefensa.setValue(teg2.getTutor()
						.getApellido());
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg2);
				ltbEstudiantesCalificar.setModel(new ListModelList<Estudiante>(
						est));
				Lapso lapso = servicioLapso.buscarLapsoVigente();
				List<ItemEvaluacion> item = servicioProgratem
						.buscarItemsEnPrograma(p, lapso);
				List<ItemEvaluacion> item2 = new ArrayList<ItemEvaluacion>();
				List<Mencion>menciones = servicioMencion.buscarActivos();
				cmbMencionTeg
				.setModel(new ListModelList<Mencion>(menciones));
				for (int i = 0; i < item.size(); i++) {
					if (item.get(i).getTipo().equals("Defensa")) {
						item2.add(item.get(i));
					}

				}
				ltbitem.setModel(new ListModelList<ItemEvaluacion>(item2));

				map.clear();
				map = null;
			}
		}

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	@Listen("onClick = #btnGuardar")
	public void guardar() {
		dejeenblanco = false;
		if(cmbMencionTeg.getValue().equals("")){
			Messagebox.show(
					"Debe Seleccionar una mencion para el TEG",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		}else{
			if ((rdoAprobado.isChecked() == false)
				&& (rdoReprobado.isChecked() == false)) {
			Messagebox.show(
					"Debe indicar si el TEG se encuentra Aprobado o Reprobado",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			long auxId2;
			auxId2 = auxId;
			teg1 = servicioTeg.buscarTeg(auxId2);

			for (int i = 0; i < ltbitem.getItemCount(); i++) {

				Defensa defensa = serviciodefensa.buscarDefensaDadoTeg(teg1);

				Listitem listItem = ltbitem.getItemAtIndex(i);
				String valor = ((Textbox) ((listItem.getChildren().get(1)))
						.getFirstChild()).getValue();
				if (valor.equals("")) {
					Messagebox.show(
							"Debe Colocar su Apreciacion en todos los item",
							"Informacion", Messagebox.OK,
							Messagebox.INFORMATION);
					i = ltbitem.getItemCount();
					dejeenblanco = true;
				} else {
					ItemEvaluacion item = ltbitem.getItems().get(i).getValue();
					ItemDefensa itemdefensa = new ItemDefensa(item, defensa,
							valor);
					servicioItem.guardar(itemdefensa);
				}

			}

			Messagebox.show("¿Desea guardar la calificacion del Trabajo Especial de Grado?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								if (dejeenblanco == false) {
									Mencion mencion = servicioMencion.buscar(Long.parseLong(cmbMencionTeg.getSelectedItem().getId()));
									TegEstatus tegEstatus = new TegEstatus();
									java.util.Date fechaEstatus = new Date();
									if (rdoAprobado.isChecked() == true) {

										String estatus1 = "TEG Aprobado";
										Messagebox.show(
												"Calificacion guardada exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
										tegEstatus = new TegEstatus(0, teg1, estatus1, fechaEstatus);
										servicioTegEstatus.guardar(tegEstatus);
										teg1.setMencion(mencion);
										teg1.setEstatus(estatus1);
										servicioTeg.guardar(teg1);
										salir();
									}

									else if (rdoReprobado.isChecked() == true) {

										String estatus2 = "TEG Reprobado";
										tegEstatus = new TegEstatus(0, teg1, estatus2, fechaEstatus);
										servicioTegEstatus.guardar(tegEstatus);
										teg1.setEstatus(estatus2);
										teg1.setMencion(mencion);
										servicioTeg.guardar(teg1);
										Messagebox.show(
												"Calificacion guardada exitosamente",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
										salir();
									}
								}

							}
						}
					});
		}
		}
	}

	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCalificarDefensa.onClose();
	}

	@Listen("onClick = #btnCancelar")
	public void cancelarVerificacion() {

		rdoAprobado.setChecked(false);
		rdoReprobado.setChecked(false);
		for (int i = 0; i < ltbitem.getItemCount(); i++) {

			Listitem listItem = ltbitem.getItemAtIndex(i);
			((Textbox) ((listItem.getChildren().get(1))).getFirstChild())
					.setValue("");
		}

	}

	@Listen("onClick = #btnSalirCalificarDefensa")
	public void salirCalificarDefensa() {

		wdwCalificarDefensa.onClose();

	}

}
