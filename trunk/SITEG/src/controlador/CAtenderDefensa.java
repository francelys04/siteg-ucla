package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Condicion;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TegEstatus;
import modelo.TipoJurado;
import modelo.compuesta.Jurado;

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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SCondicionPrograma;
import servicio.SDefensa;
import servicio.SJurado;
import servicio.STeg;
import servicio.STipoJurado;

@Controller
public class CAtenderDefensa extends CGeneral {

	@Wire
	private Textbox txtProgramaAtenderDefensa;
	@Wire
	private Textbox txtAreaAtenderDefensa;
	@Wire
	private Textbox txtTematicaAtenderDefensa;
	@Wire
	private Textbox txtTituloAtenderDefensa;
	@Wire
	private Label lblCondicionAtenderDefensa;
	@Wire
	private Listbox ltbEstudiantesAtenderDefensa;
	@Wire
	private Datebox dtbFechaAtenderDefensa;
	@Wire
	private Datebox dtbFechaDefensa;
	@Wire
	private Textbox txtLugarDefensa;
	@Wire
	private Timebox tmbHoraDefensa;
	@Wire
	private Window wdwAtenderDefensa;
	@Wire
	private Textbox txtNombreTutorProgramarDefensa;
	@Wire
	private Textbox txtApellidoTutorProgramarDefensa;
	@Wire
	private Listbox ltbJuradoAtenderDefensa;
	// @Wire
	// private Combobox cmbDefensaTipoJurado;
	private static String vistaRecibida;
	private static long idTeg = 0;
	long idDefensa = 0;
	private static Programa programa;

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		// Permite mapear los datos del catalogo a la vista Atender Defensa
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);

				txtNombreTutorProgramarDefensa.setValue(teg.getTutor().getNombre());
				txtApellidoTutorProgramarDefensa.setValue(teg.getTutor().getApellido());

				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				// para que se guie por el programa del estudiante
				List<Estudiante> est = servicioEstudiante
						.buscarEstudiantesDelTeg(teg);
				
				List<Jurado> jurados = servicioJurado.buscarJuradoDeTeg(teg);
				List<Profesor> profesoresJurado = new ArrayList<Profesor>();
				for (int i = 0; i < jurados.size(); i++) {

					Profesor profesor = jurados.get(i).getProfesor();
					profesoresJurado.add(profesor);
				}
				
				
				ltbJuradoAtenderDefensa.setModel(new ListModelList<Profesor>(
						profesoresJurado));
			
				programa = est.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(est.get(0).getPrograma()
						.getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());

			}
		}
	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	// Metodo que permite llenar la lista de los estudiantes
	public void llenarListas(Teg teg) {
		List<Estudiante> estudiantesTeg = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		ltbEstudiantesAtenderDefensa.setModel(new ListModelList<Estudiante>(
				estudiantesTeg));
	}

	// Metodo que permite guardar los datos de la defensa
	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensa() {
		if (tmbHoraDefensa.getValue() == null
				|| txtLugarDefensa.getText().compareTo("") == 0
				|| dtbFechaDefensa.getValue() == null) {

			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {
			Messagebox.show("¿Desea guardar los datos de la defensa?",
					"Dialogo de confirmacion", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {

								Teg teg = servicioTeg.buscarTeg(idTeg);
								Date fecha = dtbFechaDefensa.getValue();
								Date hora = tmbHoraDefensa.getValue();
								String lugar = txtLugarDefensa.getValue();
								Profesor profesor = ObtenerUsuarioProfesor();

								String estatus = "Defensa Programada";
								Defensa defensa = new Defensa(idDefensa, teg,
										fecha, hora, lugar, estatus, profesor);
								servicioDefensa.guardarDefensa(defensa);
								String estatus1 = "Defensa Asignada";
								System.out.println(idTeg);
								Teg teg1 = servicioTeg.buscarTeg(idTeg);
								java.util.Date fechaEstatus = new Date();					
								TegEstatus tegEstatus = new TegEstatus(0, teg1, estatus1, fechaEstatus);
								servicioTegEstatus.guardar(tegEstatus);
								teg1.setEstatus(estatus1);
								servicioTeg.guardar(teg1);
								Messagebox
										.show("Datos de la defensa guardados con exito",
												"Informacion", Messagebox.OK,
												Messagebox.INFORMATION);
								salir();

							}
						}
					});
		}

	}

	// metodo para limpiar los campos
	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		txtLugarDefensa.setValue("");
		dtbFechaDefensa.setValue(null);

	}

	// metodo para cerrar y refrescar las vistas
	public void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAtenderDefensa.onClose();
	}

	@Listen("onClick = #btnSalirDefensa")
	public void SalirDefensa() {

		wdwAtenderDefensa.onClose();

	}

}
