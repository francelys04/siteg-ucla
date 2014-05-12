package controlador.seguridad;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.seguridad.Arbol;
import modelo.seguridad.ArbolModelo;
import modelo.seguridad.ArbolNodo;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;

import controlador.CGeneral;

/**
 * Controlador que permite crear dinamicamente un arbol desde la base de
 * datos, la profundidad y cantidad de nodos de dicho arbol se basa segun
 * los permisos que tiene el grupo del usuario que se encuentra en sesion
 */
@Controller
public class CArbol extends CGeneral {

	private static final long serialVersionUID = -4579514009690299069L;
	TreeModel _model;
	@Wire
	private Tree treeMenu;
	@Wire
	private Include formularios;
	@Wire
	private Label etiqueta;
	@Wire
	private Image imagenes;

	/**
	 * Metodo heredado del Controlador CGeneral que permite inicializar los
	 * componentes de la vista, asi como tambien permite la creacion del
	 * arbol-menu del sistema.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		if (u.getImagen().toString() == "[]")
			imagenes.setSrc("/imagenes/buscar.png");
		try {
			BufferedImage imag;
			imag = ImageIO.read(new ByteArrayInputStream(u.getImagen()));
			imagenes.setContent(imag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		treeMenu.setModel(getModel());

		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("vista") != null) {
				formularios.setSrc("/vistas/" + (String) map.get("vista")
						+ ".zul");
				map.clear();
				map = null;
			}
		}

	}

	/**
	 * Metodo que permite retornar el modelo del arbol
	 */
	public TreeModel getModel() {
		if (_model == null) {
			_model = new ArbolModelo(getFooRoot());
		}
		return _model;
	}

	/**
	 * Metodo que se encarga de la creacion de los nodos del arbol, basandose en
	 * los permisos otorgados a los grupos del usuario que se encuentra en
	 * sesion
	 */
	private ArbolNodo getFooRoot() {

		ArbolNodo root = new ArbolNodo(null, 0, "");
		ArbolNodo oneLevelNode = new ArbolNodo(null, 0, "");
		ArbolNodo twoLevelNode = new ArbolNodo(null, 0, "");
		ArbolNodo threeLevelNode = new ArbolNodo(null, 0, "");
		ArbolNodo fourLevelNode = new ArbolNodo(null, 0, "");

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				auth.getAuthorities());

		ArrayList<Arbol> arbole = new ArrayList<Arbol>();
		List<Arbol> arboles = new ArrayList<Arbol>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int k = 0; k < authorities.size(); k++) {

			Arbol arbol;
			String nombre = authorities.get(k).toString();
			arbol = servicioArbol.buscarPorNombreArbol(nombre);
			if (arbol != null)
				ids.add(arbol.getId());
			arbole.add(arbol);

		}
		Collections.sort(ids);
		for (int t = 0; t < ids.size(); t++) {
			Arbol a;
			a = servicioArbol.buscarPorId(ids.get(t));
			arboles.add(a);
		}

		long temp1, temp2, temp3 = 0;

		for (int i = 0; i < arboles.size(); i++) {

			if (arboles.get(i).getHijo() == 0) {
				oneLevelNode = new ArbolNodo(root, i, arboles.get(i)
						.getNombre());
				root.appendChild(oneLevelNode);
				temp1 = arboles.get(i).getId();
				arboles.remove(i);

				for (int j = i; j < arboles.size(); j++) {

					if (temp1 == arboles.get(j).getHijo()) {
						twoLevelNode = new ArbolNodo(oneLevelNode, i, arboles
								.get(j).getNombre());
						oneLevelNode.appendChild(twoLevelNode);
						temp2 = arboles.get(j).getId();
						arboles.remove(j);

						for (int k = j; k < arboles.size(); k++) {

							if (temp2 == arboles.get(k).getHijo()) {
								threeLevelNode = new ArbolNodo(twoLevelNode, i,
										arboles.get(k).getNombre());
								twoLevelNode.appendChild(threeLevelNode);
								temp3 = arboles.get(k).getId();
								arboles.remove(k);

								for (int z = k; z < arboles.size(); z++) {

									if (temp3 == arboles.get(z).getHijo()) {
										fourLevelNode = new ArbolNodo(
												threeLevelNode, i, arboles.get(
														z).getNombre());
										threeLevelNode
												.appendChild(fourLevelNode);
										arboles.remove(z);

										z = z - 1;
									}

								}
								k = k - 1;
							}

						}
						j = j - 1;
					}

				}

				i = i - 1;
			}
		}
		return root;

	}

	/**
	 * Metodo que permite mostrar la ventana asociada al nombre del nodo del
	 * arbol
	 */
	@Listen("onSelect = #treeMenu")
	public void selectedNode() {
		String item = String.valueOf(treeMenu.getSelectedItem().getValue());
		if (treeMenu.getSelectedItem().getLevel() > 0) {
			Arbol arbolItem = servicioArbol.buscarPorNombreArbol(item);
			String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
			formularios.setSrc(null);
			formularios.setSrc(ruta);
		}
	}
}