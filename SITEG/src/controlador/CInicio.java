package controlador;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.zkoss.spring.security.SecurityUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

@Controller
public class CInicio extends CGeneral {
	@Wire
	private Treeitem fila3;
	@Wire
	private Treecell fila211;
	@Wire
	private Treecell fila2;
	@Wire
	private Treecell fila11;
	@Wire
	private Treecell fila1;
	@Wire
	private Treecell fila12;
	@Wire
	private Treecell fila112;
	@Wire
	private Treecell fila111;

	@Listen("onClick = #btnDatos")
	public void datos() {
		// Agarrar datos del usuario
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.toString());
	}

	@Override
	void inicializar(Component com) {
		// TODO Auto-generated method stub

	}
	
	@Listen("onClick = #evaluarSolicitudes")
	public void ventanaEmergente1(Event e) {
		Window VEvaluarTutorias = (Window) Executions.createComponents(
				"VEvaluarTutorias.zul", null, null);
		VEvaluarTutorias.doModal();
	}
	
	
	@Listen("onClick = #verificarSolicitudes")
	public void ventanaEmergente2(Event e) {
		Window VVerificarSolicitudProyecto = (Window) Executions.createComponents(
				"VVerificarSolicitudProyecto.zul", null, null);
		VVerificarSolicitudProyecto.doModal();
	}
	
	@Listen("onClick = #asignarComisiones")
	public void ventanaEmergente3(Event e) {
		Window VAsignarComision = (Window) Executions.createComponents(
				"VAsignarComision.zul", null, null);
		VAsignarComision.doModal();
	}
	
	@Listen("onClick = #evaluarFactibilidad")
	public void ventanaEmergente4(Event e) {
		Window VEvaluarFactibilidad = (Window) Executions.createComponents(
				"VEvaluarFactibilidad.zul", null, null);
		VEvaluarFactibilidad.doModal();
	}
	

	@Listen("onClick = #registrarFactibilidad")
	public void ventanaEmergente5(Event e) {
		Window VRegistrarFactibilidad = (Window) Executions.createComponents(
				"VRegistrarFactibilidad.zul", null, null);
		VRegistrarFactibilidad.doModal();
	}
	
	@Listen("onClick = #registrarAvances")
	public void ventanaEmergente6(Event e) {
		Window VRegistrarAvances = (Window) Executions.createComponents(
				"VRegistrarAvances.zul", null, null);
		VRegistrarAvances.doModal();
	}
	
	
	@Listen("onClick = #evaluarAvances")
	public void ventanaEmergente7(Event e) {
		Window VEvaluarAvances = (Window) Executions.createComponents(
				"VEvaluarAvances.zul", null, null);
		VEvaluarAvances.doModal();
	}
	
	@Listen("onClick = #registrarRevisiones")
	public void ventanaEmergente8(Event e) {
		Window VRegistrarRevisiones = (Window) Executions.createComponents(
				"VRegistrarRevisiones.zul", null, null);
		VRegistrarRevisiones.doModal();
	}
	
	
	@Listen("onClick = #evaluarRevisiones")
	public void ventanaEmergente9(Event e) {
		Window VEvaluarRevisiones = (Window) Executions.createComponents(
				"VEvaluarRevisiones.zul", null, null);
		VEvaluarRevisiones.doModal();
	}
	
	@Listen("onClick = #atenderDefensas")
	public void ventanaEmergente10(Event e) {
		Window VAtenderDefensa = (Window) Executions.createComponents(
				"VAtenderDefensa.zul", null, null);
		VAtenderDefensa.doModal();
	}
	
	@Listen("onClick = #calificarDefensas")
	public void ventanaEmergente11(Event e) {
		Window VCalificarDefensa = (Window) Executions.createComponents(
				"VCalificarDefensa.zul", null, null);
		VCalificarDefensa.doModal();
	}
	
	
//	 @Listen("onClick = #SolicitarDefensa")
//	    public void cancel() {
//	        // ask confirmation before canceling order
//	        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
//	            public void onEvent(ClickEvent event) throws Exception {
//	                if(Messagebox.Button.YES.equals(event.getButton())) {
//	                    // cancel order
//	                    // ...
//	                    // notify user
//	                    Messagebox.show("Solicitud enviada correctamente");
//	                }
//	            }
//	        };
//	        Messagebox.show("Estás seguro de Solicitar la Defensa de tu Trabajo Especial de Grado?", "Advertencia", new Messagebox.Button[]{
//	                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
//	    }

}
