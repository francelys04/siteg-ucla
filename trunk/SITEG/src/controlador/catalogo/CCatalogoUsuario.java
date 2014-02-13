package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import modelo.ItemEvaluacion;
import modelo.seguridad.Usuario;

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
import servicio.seguridad.SUsuario;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CCatalogoUsuario extends CGeneral {
	
	private static String vistaRecibida;

	@Wire
	private Textbox txtNombreMostrarUsuario;
	@Wire
	private Textbox txtPasswordMostrarUsuario;
	@Wire
	private Listbox ltbUsuario;
	@Wire
	private Window wdwCatalogoUsuario;


	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * usuarios disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	@Override
	public	void inicializar(Component comp) {
		List<Usuario> usuarios = servicioUsuario.buscarActivos();
		ltbUsuario.setModel(new ListModelList<Usuario>(usuarios));
	
	}
	/*
	 * Metodo que permite filtrar los usuarios disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre de estos.
	 */
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
	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;
		System.out.println("Recibir vista"+vista);

	}
	/*
	 * Metodo que permite obtener el objeto Usuario al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbUsuario")
	public void mostrarDatosCatalogo() {
		if(ltbUsuario.getItemCount()!=0){
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
}
