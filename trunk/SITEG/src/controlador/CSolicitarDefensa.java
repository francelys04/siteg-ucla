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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.STeg;

@Controller
public class CSolicitarDefensa extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();

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
	private Teg teg;
	private String[] estatus = { "Revisiones Finalizadas", "Solicitando Defensa" };

	@Override
	public
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		try {
			teg = servicioTeg.buscarTegPorEstudiante(ObtenerUsuarioEstudiante())
					.get(0);
			List<Estudiante> estudiantes = servicioEstudiante
					.buscarEstudiantePorTeg(teg);
			
			if (teg.getEstatus().equals(estatus[0])) {
				//se guia es por el programa del estudiante
				txtProgramaSolicitarDefensa.setValue(estudiantes.get(0).getPrograma().getNombre());
				txtAreaSolicitarDefensa.setValue(teg.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaSolicitarDefensa.setValue(teg.getTematica().getNombre());
				txtTituloSolicitarDefensa.setValue(teg.getTitulo());
				ltbEstudiantesSolicitarDefensa
						.setModel(new ListModelList<Estudiante>(estudiantes));
			} else {
				if (teg.getEstatus().equals(estatus[1])) {
					Messagebox.show(
							"Ya posee una solicitud de defensa",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
					cancelar();
				} else {
					Messagebox.show(
							"No puede solicitar defensa",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
					cancelar();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("No puede solicitar defensa",
			"Advertencia", Messagebox.OK,
			Messagebox.EXCLAMATION);
			cancelar();
		}
		
	}

	/**
     * Metodo que cierra la ventana solicitar defensa 
     */
	public void cancelar() {
		wdwSolicitarDefensa.onClose();
	}
    /**
     * Metodo que solicita la defensa de un trabajo especial de grado 
     * ademas cambia el estatus del Teg a estatus solicitando defensa 
     */
	@Listen("onClick = #btnSolicitarDefensa")
	public void solicitarDefensa() {
		Messagebox.show("¿Desea solicitar la defensa de su trabajo especial de grado?",
				"Dialogo de confirmacion", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt)
							throws InterruptedException {
						if (evt.getName().equals("onOK")) {
		java.util.Date fechaEstatus = new Date();					
		TegEstatus tegEstatus = new TegEstatus(0, teg, estatus[1], fechaEstatus);
		servicioTegEstatus.guardar(tegEstatus);
		teg.setEstatus(estatus[1]);
		servicioTeg.guardar(teg);
		Messagebox.show(
				"Su Solicitud de defensa fue enviada con exito",
				"Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
		cancelar();
						}
					}
				});
	}
						
}
