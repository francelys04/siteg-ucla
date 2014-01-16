package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import servicio.SItem;
import servicio.SUsuario;
import configuracion.GeneradorBeans;

public class CCatalogoUsuario extends CGeneral {
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	@Wire
	private Textbox txtNombreMostrarUsuario;
	@Wire
	private Textbox txtPasswordMostrarUsuario;
	@Wire
	private Listbox ltbUsuario;
	@Wire
	private Window wdwCatalogoUsuario;


	private static String vistaRecibida;

	@Override
	void inicializar(Component comp) {

		List<Usuario> usuarios = servicioUsuario.buscarActivos();
		ltbUsuario.setModel(new ListModelList<Usuario>(usuarios));
	
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		
		System.out.println("Map"+map);
	}

	@Listen("onChange = #txtNombreMostarUsuario")
	public void filtrarDatosCatalogo() {
		List<Usuario> usuarios = servicioUsuario.buscarActivos();
		List<Usuario> usuarios1 = servicioUsuario.buscarActivos();
		
		for (Usuario usuario : usuarios) {
			if (usuario
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarUsuario.getValue().toLowerCase()))
			{
				usuarios1.add(usuario);
			}

		}

		ltbUsuario.setModel(new ListModelList<Usuario>(usuarios1));

	}

	public void recibir(String vista) {
		vistaRecibida = vista;
		System.out.println("Recibir vista"+vista);

	}

	@Listen("onDoubleClick = #ltbUsuario")
	public void mostrarDatosCatalogo() {
		Listitem listItem = ltbUsuario.getSelectedItem();
		Usuario usuario = (Usuario) listItem.getValue();
		Usuario usuario1=servicioUsuario.buscarUsuarioPorNombre(usuario.getNombre());
		String vista = vistaRecibida;
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
	
		if(map2!=null)
			map=map2;
		
		
		map.put("id", usuario1.getId());
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoUsuario.onClose();

	}

}
