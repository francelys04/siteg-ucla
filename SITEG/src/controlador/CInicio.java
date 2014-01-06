package controlador;

import modelo.Estudiante;
import modelo.Programa;
import configuracion.GeneradorBeans;
import controlador.CConsultarEstatus;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.zkoss.spring.security.SecurityUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import servicio.SEstudiante;


@Controller
public class CInicio extends CGeneral {
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
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
	@Wire
	private Intbox cedulaEstatus;
	@Wire
	private Window wdwConsultarEstatusProyecto;
	@Wire
	private Button btnSolicitarTutor;
	
	
	@Listen("onClick = #btnSolicitarTutor")
	public void SolicitarTutor() {
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VSolicitarTutor.zul", null, null);
		window.doModal();
	}
	

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
	

	
	@Listen("onClick = #btnConsultarEstatus")
	public void ventanaEmergente(Event e) {
		String cedula = Integer.toString(cedulaEstatus.getValue());
		
		if (cedula!= "") {
			if (servicioEstudiante.buscarEstudiante(cedula)!=null){
				
				cedulaEstatus.setValue(null);
				CConsultarEstatus consultarestatus = new CConsultarEstatus();
				consultarestatus.recibirCedula(cedula);
			}
			else{
				Messagebox.show("El estudiante no se encuentra registrado");
				cedulaEstatus.setValue(null);
			}
			
			
		}
		
		
	}
	
	
	


}
