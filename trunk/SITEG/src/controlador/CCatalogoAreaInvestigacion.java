package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.AreaInvestigacion;

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

import servicio.SAreaInvestigacion;
import configuracion.GeneradorBeans;

public class CCatalogoAreaInvestigacion extends CGeneral {

	
		//servicio 
		SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
		

		//atributos de la vista de registrar area
		@Wire
		private Textbox txtNombreArea;
		@Wire
		private Textbox txtDescripcionArea;
		
	   //atributos del catalogo de area
		@Wire
		private Listbox ltbArea;
		@Wire
		private Window wdwCatalogoArea;
		@Wire
		private Textbox txtNombreMostrarArea;
		@Wire
		private Textbox txtDescripcionMostrarArea;
		private long id = 0;
		private static String vistaRecibida;

		
		void inicializar(Component comp) {

			//busca todas las areas y llena un listado
			List<AreaInvestigacion> area = servicioArea.buscarActivos();

			if(txtNombreArea==null){
				ltbArea.setModel(new ListModelList<AreaInvestigacion>(area));
				}

			Selectors.wireComponents(comp, this, false);

			HashMap<String, Object> map = (HashMap<String, Object>) Sessions
					.getCurrent().getAttribute("itemsCatalogo");

		}
		//filtra el catalogo en este caso solo por nombre y descripcion
		@Listen("onChange = #txtNombreMostrarArea, #txtDescripcionMostrarArea")
		public void filtrarDatosCatalogo() {
			List<AreaInvestigacion> area1 = servicioArea.buscarActivos();
			List<AreaInvestigacion> area2 = new ArrayList<AreaInvestigacion>();

			for (AreaInvestigacion area : area1) {
				if (area
						.getNombre()
						.toLowerCase()
						.contains(
								txtNombreMostrarArea.getValue().toLowerCase())
						
						
						
						&& area
								.getDescripcion()
								.toLowerCase()
								.contains(
										txtDescripcionMostrarArea.getValue()
												.toLowerCase())) {
					area2.add(area);
				}

			}

			ltbArea.setModel(new ListModelList<AreaInvestigacion>(area2));

		}
		public void recibir (String vista)
		{
			vistaRecibida = vista;
			
		}
			

		//lleva el item que selecciono en el catalogo a la ventana y cierra el catalogo
			@Listen("onDoubleClick = #ltbArea")
			public void mostrarDatosCatalogo() {

				Listitem listItem = ltbArea.getSelectedItem();
				AreaInvestigacion areaDatosCatalogo = (AreaInvestigacion) listItem.getValue();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", areaDatosCatalogo.getId());
				String vista = vistaRecibida;
				map.put("vista", vista);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				Executions.sendRedirect("/vistas/arbol.zul");
				wdwCatalogoArea.onClose();

			}
		
	}


