package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import modelo.Grupo;
import modelo.ItemEvaluacion;
import modelo.Usuario;

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

import servicio.SGrupo;
import servicio.SItem;
import servicio.SUsuario;
import configuracion.GeneradorBeans;

public class CCatalogoGrupo extends CGeneral {
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();

	
	@Wire
	private Textbox txtNombreMostrarGrupo;
	@Wire
	private Listbox ltbGrupo;
	@Wire
	private Window wdwCatalogoGrupo;


	private static String vistaRecibida;

	@Override
	void inicializar(Component comp) {
		//Llena el catalogo con los grupos activos
		List<Grupo> grupos = servicioGrupo.buscarActivos();
		ltbGrupo.setModel(new ListModelList<Grupo>(grupos));
	
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		
		System.out.println("Map"+map);
	}
	//permite filtrar los datos del catalogo grupo
	@Listen("onChange = #txtNombreMostarGrupo")
	public void filtrarDatosCatalogo() {
		List<Grupo> grupos = servicioGrupo.buscarActivos();
		List<Grupo> grupos1 = servicioGrupo.buscarActivos();
	
		for (Grupo grupo : grupos) {
			if (grupo
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarGrupo.getValue().toLowerCase())
					)
			{
				grupos1.add(grupo);
			}

		}

		ltbGrupo.setModel(new ListModelList<Grupo>(grupos1));

	}

	public void recibir(String vista) {
		vistaRecibida = vista;
		System.out.println("Recibir vista"+vista);

	}
//permite mapear los datos a la vista recibida
	@Listen("onDoubleClick = #ltbGrupo")
	public void mostrarDatosCatalogo() {
		if(ltbGrupo.getItemCount()!=0){
		Listitem listItem = ltbGrupo.getSelectedItem();
		Grupo grupo = (Grupo) listItem.getValue();
		Grupo grupo1=servicioGrupo.BuscarPorNombre(grupo.getNombre());
		String vista = vistaRecibida;
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
	
		if(map2!=null)
			map=map2;
		
		
		map.put("id", grupo1.getId());
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoGrupo.onClose();

	}
	}
}
