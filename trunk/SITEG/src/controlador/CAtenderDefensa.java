package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.Condicion;
import modelo.CondicionPrograma;
import modelo.Defensa;
import modelo.Estudiante;
import modelo.Jurado;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.TipoJurado;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SDefensa servicioDefensa = GeneradorBeans.getServicioDefensa();
	SJurado servicioJurado = GeneradorBeans.getServicioJurado();
	STipoJurado servicioTipoJurado = GeneradorBeans.getServicioTipoJurado();
	SCondicionPrograma servicioCondicionPrograma = GeneradorBeans
			.getServicioCondicionPrograma();
	@Wire
	private Textbox txtProgramaAtenderDefensa;
	@Wire
	private Textbox txtAreaAtenderDefensa;
	@Wire
	private Textbox txtTematicaAtenderDefensa;
	@Wire
	private Textbox txtTituloAtenderDefensa;
	@Wire
	private Textbox txtCedulaTutorAtenderDefensa;
	@Wire
	private Textbox txtNombreTutorAtenderDefensa;
	@Wire
	private Textbox txtApellidoTutorAtenderDefensa;
	@Wire
	private Label lblCondicionAtenderDefensa;
	@Wire
	private Listbox ltbJuradoDisponible;
	@Wire
	private Listbox ltbJuradoSeleccionado;
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
	// @Wire
	// private Combobox cmbDefensaTipoJurado;
	private static String vistaRecibida;
	long idTeg = 0;
	long idDefensa = 0;
    private static Programa programa;
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("catalogoSolicitudDefensa");
		if (map != null) {
			if (map.get("id") != null) {
				idTeg = (Long) map.get("id");
				Teg teg = servicioTeg.buscarTeg(idTeg);
				llenarListas(teg);
				
				txtCedulaTutorAtenderDefensa.setValue(teg.getTutor()
						.getCedula());
				txtNombreTutorAtenderDefensa.setValue(teg.getTutor()
						.getNombre());
				txtApellidoTutorAtenderDefensa.setValue(teg.getTutor()
						.getApellido());
				txtAreaAtenderDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaAtenderDefensa.setValue(teg.getTematica()
						.getNombre());
				//para que se guie por el programa del estudiante
				List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg);
				programa = est.get(0).getPrograma();
				txtProgramaAtenderDefensa.setValue(est.get(0).getPrograma().getNombre());
				txtTituloAtenderDefensa.setValue(teg.getTitulo());
				lblCondicionAtenderDefensa
						.setValue("Recuerde que la cantidad de jurados es de:"
								+ buscarCondicionVigenteEspecifica(
										"Numero de integrantes del jurado", programa)
										.getValor());
				Defensa defensa = servicioDefensa.buscarDefensaDadoTeg(teg);
				if (defensa != null) {
					idDefensa = defensa.getId();
					dtbFechaDefensa.setValue(defensa.getFecha());
					tmbHoraDefensa.setValue(defensa.getHora());
					txtLugarDefensa.setValue(defensa.getLugar());
				}
			}
		}
	}

	@Listen("onClick = #btnAgregarJurado")
	public void moverDerechaJurado() {

		Listitem list1 = ltbJuradoDisponible.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un Item");
		else
			list1.setParent(ltbJuradoSeleccionado);
	}

	@Listen("onClick = #btnRemoverJurado")
	public void moverIzquierdaJurado() {
		Listitem list2 = ltbJuradoSeleccionado.getSelectedItem();
		if (list2 == null)
			Messagebox.show("Seleccione un Item");
		else
			list2.setParent(ltbJuradoDisponible);
	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	public void llenarListas(Teg teg) {
		List<Estudiante> estudiantesTeg = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		ltbEstudiantesAtenderDefensa.setModel(new ListModelList<Estudiante>(
				estudiantesTeg));
		List<Profesor> juradoDisponible = servicioProfesor
				.buscarProfesorJuradoDadoTeg(teg);
		ltbJuradoDisponible.setModel(new ListModelList<Profesor>(
				juradoDisponible));
		List<Jurado> juradoOcupado = servicioJurado.buscarJuradoDeTeg(teg);
		ltbJuradoSeleccionado
				.setModel(new ListModelList<Jurado>(juradoOcupado));
		// List<TipoJurado> tipoJurado = servicioTipoJurado.buscarActivos();
		// System.out.println(tipoJurado.toString());
		// cmbDefensaTipoJurado.setModel(new
		// ListModelList<TipoJurado>(tipoJurado));
	}

	public boolean validarJurado() {
		if (ltbJuradoSeleccionado.getItemCount() == buscarCondicionVigenteEspecifica(
				"Numero de integrantes del jurado", programa).getValor()) {
			System.out.println("aqui");
			return true;
		} else
			System.out.println("alla");
		return false;
	}

	@Listen("onClick = #btnAceptarDefensaMientrasTanto")
	public void aceptarDefensa() {
		if (guardardatosDefensa()) {
			Messagebox
					.show("Solicitud de Defensa guardada para futuras modificaciones",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			salir();
		}
	}

	@Listen("onClick = #btnAceptarDefensa")
	public void aceptarDefensaDefinitiva() {
		if (validarJurado() && guardardatosDefensa()) {
			Teg teg = servicioTeg.buscarTeg(idTeg);
			teg.setEstatus("Defensa Asignada");
			servicioTeg.guardar(teg);
			Messagebox.show("Solicitud de Defensa finalizada exitosamente",
					"Información", Messagebox.OK, Messagebox.INFORMATION);
			salir();
		} else {
			Messagebox.show(
					"El numero de profesores en el jurado no es el correcto, debe ser: "
							+ buscarCondicionVigenteEspecifica(
									"Numero de integrantes del jurado", programa)
									.getValor(), "Error", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Listen("onClick = #btnCancelarDefensa")
	public void cancelarDefensa() {
		salir();
	}

	public void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAtenderDefensa.onClose();
	}

	public boolean guardardatosDefensa() {
		Teg teg = servicioTeg.buscarTeg(idTeg);

		List<Jurado> jurados = new ArrayList<Jurado>();
		boolean error = false;
		for (int i = 0; i < ltbJuradoSeleccionado.getItemCount(); i++) {
			Listitem listItem = ltbJuradoSeleccionado.getItemAtIndex(i);
			String tipojurado = ((Combobox) ((listItem.getChildren().get(3)))
					.getFirstChild()).getValue();
			if (tipojurado == "") {
				error = true;
			}
			long cedula = ((Spinner) ((listItem.getChildren().get(0)))
					.getFirstChild()).getValue();
			Profesor profesorJurado = servicioProfesor
					.buscarProfesorPorCedula(String.valueOf(cedula));
			TipoJurado tipo = servicioTipoJurado.buscarPorNombre(tipojurado);
			Jurado jurado = new Jurado(teg, profesorJurado, tipo);
			jurados.add(jurado);
		}
		if (!error) {
			servicioJurado.guardar(jurados);
			Date fecha = dtbFechaDefensa.getValue();
			Date hora = tmbHoraDefensa.getValue();
			String lugar = txtLugarDefensa.getValue();
			
			String estatus = "Por Defender";
			Profesor profesor = servicioProfesor
					.buscarProfesorPorCedula(txtCedulaTutorAtenderDefensa
							.getValue());
			Defensa defensa = new Defensa(idDefensa, teg, fecha, hora, lugar,
					 estatus, profesor);
			servicioDefensa.guardarDefensa(defensa);
			return true;
		} else {
			Messagebox.show("Debe seleccionar un tipo para cada jurado",
					"Error", Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}
}
