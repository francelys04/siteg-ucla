package controlador;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import configuracion.UtilidadSiteg;

@Controller
public class CRespaldoInformacion extends CGeneral {

	@Wire
	private Button btnRespaldar;
	@Wire
	private Button btnSalir;
	@Wire
	private Window wdwRespaldoInformacion;
	
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
	}
	@Listen("onClick = #btnRespaldar")
	public void respaldarInformacion(){
		respaldarBD();
		
	}
	@Listen("onClick = #btnSalir")
	public void salir(){
		wdwRespaldoInformacion.onClose();
	
		
	}
	/**Metodo que respalda la informacion contenida en el
	 *  Sistema
	 * 
	 */
	private void respaldarBD() {
	       try {
	    	   String fecha= new Date().toLocaleString();
	    	   String fecha1= "("+fecha.substring(0, 2)+"-"+fecha.substring(3, 5)+"-"+fecha.substring(6, 10)+")";
	    	   String nombre = "SITEG"+fecha1+".sql";
	    	   String ruta = UtilidadSiteg.obtenerDirectorio()+"SITEG/respaldos/";
	    	   Runtime.getRuntime ().exec ("pg_dump -i -h localhost -p 5432 -U postgres -F c -v -f "+ruta+nombre+" siteg");
	    	   Executions.getCurrent().sendRedirect("/respaldos/"+nombre, "_blank");
	       } catch (IOException e) {
	     
	       }  
	}
	

}
