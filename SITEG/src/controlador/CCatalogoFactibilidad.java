package controlador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import servicio.SEstudiante;
import servicio.SFactibilidad;


import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.Tematica;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;
@Controller
public class CCatalogoFactibilidad extends CGeneral{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SFactibilidad serviciofactibilidad = GeneradorBeans.getFactibilidad();
	@Wire
	private Textbox txtIdListaFactibilidad;
	@Wire
	private Textbox txtTituloListaFactibilidad;
	@Wire
	private Textbox txtDescripcionListaFactibilidad;
	@Wire
	private Textbox textEstatusListaFactibilidad;
	@Wire
	private Textbox textTematicaListaFactibilidad;
	@Wire
	private Listbox ltbListaFactibilidad;
	@Wire
	private Window  wdwListaFactibilidad;
	@Wire
	private Window wdwCatalogoFactibilidad;
	private  static String vistaRecibida;
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		//se busca los proyectos con estatus FactibilidadEvaluada
		List<Factibilidad> factibilidad = serviciofactibilidad.buscarActivos();
		//Se llena el listbox con los proyectos con estatus FactibilidadEvaluada
		ltbListaFactibilidad.setModel(new ListModelList<Factibilidad>(factibilidad));
		
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		
		
	
	}
	// Metodo que permite el filtrado de la lista
			@Listen("onChange = #txtIdListaFactibilidad,#txtTituloListaFactibilidad,#txtDescripcionListaFactibilidad,#textTematicaListaFactibilidad,#textEstatusListaFactibilidad")
			public void filtrarDatosCatalogo() {
				List<Factibilidad> factibilidad1 = serviciofactibilidad.buscarActivos();
				List<Factibilidad> factibilidad2 = new ArrayList<Factibilidad>();

				for (Factibilidad factibilidad : factibilidad1) {
					//if (/*factibilidad.getId()
						//	.toLowerCase()
						//	.contains(
							//		txtIdListaFactibilidad.getValue().toLowerCase())
							if( factibilidad.getTeg().getTitulo()
									.toLowerCase()
									.contains(
											txtTituloListaFactibilidad.getValue()
													.toLowerCase())
							&& factibilidad.getTeg().getDescripcion()
									.toLowerCase()
									.contains(
											txtDescripcionListaFactibilidad.getValue()
													.toLowerCase())
							&& factibilidad.getTeg().getTematica().getNombre()
									.toLowerCase()
									.contains(
											textTematicaListaFactibilidad.getValue()
													.toLowerCase())
							&& factibilidad.getEstatus()
									.toLowerCase()
									.contains(
											textEstatusListaFactibilidad.getValue()
													.toLowerCase())){
							
						factibilidad2.add(factibilidad);
					}

				}

				ltbListaFactibilidad.setModel(new ListModelList<Factibilidad>(factibilidad2));

			}

			public void recibir (String vista)
			{
  			  vistaRecibida = vista;
			
			}
			// Metodo que luego de presionar doble click sobre una fila de la lista
			// almacena los datos en un mapa, para luego colocarlos en la vista
			// correspondiente
			
			 @Listen("onDoubleClick = #ltbListaFactibilidad")
             public void mostrarDatosLista() {                         
                     Listitem listItem = ltbListaFactibilidad.getSelectedItem();
                     Factibilidad ListaFactibilidad = (Factibilidad) listItem.getValue();
             //        System.out.println(ListaFactibilidad.getId());
                     final HashMap<String, Object> map = new HashMap<String, Object>();
                     map.put("id", ListaFactibilidad.getId());
                     String vista = vistaRecibida;
                     map.put("vista", vista);      
                     Sessions.getCurrent().setAttribute("itemsCatalogo", map);
                     Executions.sendRedirect("/vistas/arbol.zul");        
                     wdwListaFactibilidad.onClose();

             }
}
