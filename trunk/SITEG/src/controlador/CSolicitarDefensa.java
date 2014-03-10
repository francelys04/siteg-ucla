package controlador;

import java.util.Date;
import java.util.List;

import modelo.Estudiante;
import modelo.Teg;
import modelo.TegEstatus;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/*
 * Controlador que permite solicitar la defensa del trabajo especial de
 * grado una vez que el teg tiene el estatus Solicitando Defensa
 */
@Controller
public class CSolicitarDefensa extends CGeneral {

	private static final long serialVersionUID = -4879121260803326486L;
	private Teg teg;
	private String[] estatus = { "Revisiones Finalizadas",
			"Solicitando Defensa" };
	@Wire
	private Datebox dtbFechaSolicitarDefensa;
	@Wire
	private Textbox txtProgramaSolicitarDefensa;
	@Wire
	private Textbox txtAreaSolicitarDefensa;
	@Wire
	private Textbox txtTematicaSolicitarDefensa;
	@Wire
	private Textbox txtTituloSolicitarDefensa;
	@Wire
	private Listbox ltbEstudiantesSolicitarDefensa;
	@Wire
	private Window wdwSolicitarDefensa;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista y se llenan los campos correspondientes de
	 * la vista, asi como los objetos empleados dentro de este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		Estudiante estudiante = ObtenerUsuarioEstudiante();
		if(estudiante != null){
			teg = servicioTeg.ultimoTeg(estudiante);
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarEstudiantePorTeg(teg);
			if (teg.getEstatus().equals(estatus[0])) {
				txtProgramaSolicitarDefensa.setValue(estudiantes.get(0)
						.getPrograma().getNombre());
				txtAreaSolicitarDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaSolicitarDefensa.setValue(teg.getTematica()
						.getNombre());
				txtTituloSolicitarDefensa.setValue(teg.getTitulo());
				ltbEstudiantesSolicitarDefensa
						.setModel(new ListModelList<Estudiante>(estudiantes));
			} else {
				if (teg.getEstatus().equals(estatus[1])) {
					Messagebox.show("Ya posee una solicitud de defensa",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
					salir();
				} else {
					Messagebox.show("No puede solicitar defensa",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
					salir();
				}
			}
		} else {
			Messagebox.show("No puede solicitar defensa", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
			salir();
		}

	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirSolicitarDefensa")
	public void salir() {
		wdwSolicitarDefensa.onClose();
	}

	/*
	 * Metodo que permite solicitar la defensa por determinado estudiante,
	 * cambiando asi el estatus del teg, asi como se actualiza el historial de
	 * cambios del teg
	 */
	@Listen("onClick = #btnSolicitarDefensa")
	public void solicitarDefensa() {
		Messagebox.show(
				"¿Desea solicitar la defensa de su trabajo especial de grado?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							java.util.Date fechaEstatus = new Date();
							TegEstatus tegEstatus = new TegEstatus(0, teg,
									estatus[1], fechaEstatus);
							servicioTegEstatus.guardar(tegEstatus);
							teg.setEstatus(estatus[1]);
							servicioTeg.guardar(teg);
							Messagebox
									.show("Su Solicitud de defensa fue enviada con exito",
											"Informacion", Messagebox.OK,
											Messagebox.INFORMATION);
							salir();
						}
					}
				});
	}

}
