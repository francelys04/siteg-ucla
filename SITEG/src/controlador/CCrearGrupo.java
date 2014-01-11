package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import modelo.Arbol;
import modelo.Grupo;
import modelo.Usuario;

import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import servicio.SArbol;
import servicio.SGrupo;
import servicio.SUsuario;
import configuracion.GeneradorBeans;

import fiddle.FooModel;
import fiddle.FooNode;

public class CCrearGrupo extends SelectorComposer<Component> {

	long id = 0;
	TreeModel _model;
	SArbol servicioArbol = GeneradorBeans.getServicioArbol();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SGrupo servicioGrupo = GeneradorBeans.getServicioGrupo();

	CCatalogoGrupo catalogoGrupo = new CCatalogoGrupo();

	@Wire
	private Tree treeGrupo;
	@Wire
	private Textbox txtNombreGrupo;
	@Wire
	private Button btnEliminarGrupo;
	@Wire
	private Button btnVisualizarFuncionalidades;
	@Wire
	private Button btnCancelarGrupo;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		treeGrupo.setModel(getModel());
		treeGrupo.setCheckmark(true);
		treeGrupo.setMultiple(true);
		btnVisualizarFuncionalidades.setVisible(false);
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if (map != null) {

			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				Grupo grupo = servicioGrupo.buscarGrupo(codigo);
				txtNombreGrupo.setValue(grupo.getNombre());
				txtNombreGrupo.setDisabled(true);
				btnEliminarGrupo.setDisabled(false);
				btnVisualizarFuncionalidades.setVisible(true);
				treeGrupo.setVisible(false);
				id = grupo.getId();
			}
		}
	}

	public TreeModel getModel() {
		if (_model == null) {
			System.out.println("1");
			_model = new FooModel(getFooRoot());
		}
		return _model;

	}

	// create a FooNodes tree structure and return the root
	private FooNode getFooRoot() {

		FooNode root = new FooNode(null, 0, "");
		FooNode oneLevelNode = new FooNode(null, 0, "");
		FooNode twoLevelNode = new FooNode(null, 0, "");
		FooNode threeLevelNode = new FooNode(null, 0, "");
		FooNode fourLevelNode = new FooNode(null, 0, "");
//		Authentication auth = SecurityContextHolder.getContext()
//				.getAuthentication();
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
//				auth.getAuthorities());
		List<Arbol> listaArbol=servicioArbol.listarArbol();
		ArrayList<Arbol> arbole = new ArrayList<Arbol>();
		List<Arbol> arboles = new ArrayList<Arbol>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int k = 0; k < listaArbol.size(); k++) {
			Arbol arbol;
			String nombre = listaArbol.get(k).getNombre();
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
				oneLevelNode = new FooNode(root, i, arboles.get(i).getNombre());
				root.appendChild(oneLevelNode);
				temp1 = arboles.get(i).getId();
				arboles.remove(i);
				for (int j = i; j < arboles.size(); j++) {
					if (temp1 == arboles.get(j).getHijo()) {
						twoLevelNode = new FooNode(oneLevelNode, i, arboles
								.get(j).getNombre());
						oneLevelNode.appendChild(twoLevelNode);
						temp2 = arboles.get(j).getId();
						arboles.remove(j);
						for (int k = j; k < arboles.size(); k++) {
							if (temp2 == arboles.get(k).getHijo()) {
								threeLevelNode = new FooNode(twoLevelNode, i,
										arboles.get(k).getNombre());
								twoLevelNode.appendChild(threeLevelNode);
								temp3 = arboles.get(k).getId();
								arboles.remove(k);
								for (int z = k; z < arboles.size(); z++) {
									if (temp3 == arboles.get(z).getHijo()) {
										fourLevelNode = new FooNode(
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

	public boolean validarNodoHijo(SelectEvent<Treeitem, String> event) {

		Treeitem itemSeleccionado = event.getReference();
		Arbol arbol = servicioArbol.buscarPorNombreArbol(itemSeleccionado
				.getLabel());
		long padre = arbol.getId();
		boolean encontrado = false;
		List<Arbol> listaArbol = servicioArbol.listarArbol();
		for (int i = 0; i < listaArbol.size(); i++) {
			if (padre == listaArbol.get(i).getHijo()) {
				if (itemSeleccionado.isSelected()) {
					Messagebox
							.show("Debe seleccionar/deseleccionar la(s) funcionalidad(es) espeficifica para este grupo",
									"Error", Messagebox.OK,
									Messagebox.INFORMATION);
					itemSeleccionado.setSelected(false);
					i = listaArbol.size();
					encontrado = true;
				} else {
					Messagebox
							.show("Debe seleccionar/deseleccionar la(s) funcionalidad(es) espeficifica para este grupo",
									"Error", Messagebox.OK,
									Messagebox.INFORMATION);
					itemSeleccionado.setSelected(true);
					i = listaArbol.size();
					encontrado = true;
				}
				return encontrado;
			}
		}
		return encontrado;
	}

	@Listen("onSelect = #treeGrupo")
	public void selectedNode(SelectEvent<Treeitem, String> event) {
		if (!validarNodoHijo(event)) {
			List<Arbol> listaArbol2 = servicioArbol.listarArbol();
			Treechildren treeChildren = treeGrupo.getTreechildren();
			Collection<Treeitem> listaItems = treeChildren.getItems();
			Treeitem itemSeleccionado = event.getReference();
			List<Long> ids = new ArrayList<Long>();
			for (int o = 0; o < listaArbol2.size(); o++) {
				long id = listaArbol2.get(o).getId();
				ids.add(id);
			}
			Collections.sort(ids);
			List<Arbol> listaArbol = new ArrayList<Arbol>();
			for (int y = 0; y < ids.size(); y++) {
				listaArbol.add(servicioArbol.buscar(ids.get(y)));
			}
			String nombreItem = String.valueOf(itemSeleccionado.getLabel());
			Arbol arbolItem = servicioArbol.buscarPorNombreArbol(nombreItem);
			listaArbol.remove((int) (long) arbolItem.getId() - 1);
			long temp = arbolItem.getHijo();
			long temp2 = 0;
			long temp3 = temp;
			boolean encontradoHermanoHijo = false;
			boolean encontradoHermanoPadre = false;
			for (int i = 0; i < listaArbol.size(); i++) {
				if (temp == listaArbol.get(i).getId()) {
					for (Iterator<?> iterator = listaItems.iterator(); iterator
							.hasNext();) {
						Treeitem item = (Treeitem) iterator.next();
						if (listaArbol.get(i).getNombre()
								.equals(item.getLabel())) {
							temp2 = listaArbol.get(i).getHijo();
							for (int j = 0; j < listaArbol.size(); j++) {
								if (temp3 == listaArbol.get(j).getHijo()) {
									for (Iterator<?> iterator2 = listaItems
											.iterator(); iterator2.hasNext();) {
										Treeitem item2 = (Treeitem) iterator2
												.next();
										if (listaArbol.get(j).getNombre()
												.equals(item2.getLabel())) {
											if (item2.isSelected()) {
												encontradoHermanoHijo = true;
											}
										}
									}
								}
								if (temp2 == listaArbol.get(j).getHijo()
										&& listaArbol.get(i).getId() != listaArbol
												.get(j).getId()) {
									for (Iterator<?> iterator2 = listaItems
											.iterator(); iterator2.hasNext();) {
										Treeitem item2 = (Treeitem) iterator2
												.next();
										if (listaArbol.get(j).getNombre()
												.equals(item2.getLabel())) {
											if (item2.isSelected()) {
												encontradoHermanoPadre = true;
											}
										}
									}
								}
							}
							if (!item.isSelected()) {
								item.setSelected(true);
							} else {
								if (!encontradoHermanoHijo
										&& !encontradoHermanoPadre) {
									item.setSelected(false);
								}
								if (!encontradoHermanoHijo
										&& encontradoHermanoPadre
										&& !itemSeleccionado.isSelected()) {
									item.setSelected(false);
									encontradoHermanoHijo = true;
									encontradoHermanoPadre = false;
								}
							}
						}
					}
					temp = listaArbol.get(i).getHijo();
					i = -1;
				}
			}
		}
	}

	@Listen("onClick = #btnGuardarGrupo")
	public void guardarGrupo() {
		List<Arbol> listaArbol = servicioArbol.listarArbol();
		Set<Arbol> arboles = new HashSet<Arbol>();
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		String nombreGrupo = txtNombreGrupo.getValue();
		Grupo grupo = servicioGrupo.BuscarPorNombre(nombreGrupo);
		if (id == 0 && grupo == null || id != 0) {
			for (int i = 0; i < listaArbol.size(); i++) {
				for (Iterator<?> iterator = lista.iterator(); iterator
						.hasNext();) {
					Treeitem treeitem = (Treeitem) iterator.next();
					if (listaArbol.get(i).getNombre()
							.equals(treeitem.getLabel())) {
						if (treeitem.isSelected()) {

							Arbol arbol = listaArbol.get(i);
							arboles.add(arbol);
						}
					}
				}
			}
			Boolean estatus = true;
			String nombre = txtNombreGrupo.getValue();
			Grupo grupo1 = new Grupo(id, nombre, estatus, arboles);
			servicioGrupo.guardarGrupo(grupo1);
			Messagebox.show("Grupo registrado exitosamente", "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
			cancelarGrupo();
		} else {
			Messagebox.show("Grupo no disponible", "Error", Messagebox.OK,
					Messagebox.ERROR);
			cancelarGrupo();
		}
	}
	@Listen("onClick = #btnCancelarGrupo")
	public void cancelarGrupo() {
		txtNombreGrupo.setValue("");
		txtNombreGrupo.setDisabled(false);
		btnEliminarGrupo.setDisabled(false);
		btnVisualizarFuncionalidades.setVisible(false);
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		for (int i = 0; i < treeChildren.getItemCount(); i++) {
			for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
				Treeitem treeitem = (Treeitem) iterator.next();
				treeitem.setSelected(false);
			}
		}
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (treeitem.isOpen()) {
				treeitem.setOpen(false);
				lista = treeGrupo.getTreechildren().getItems();
				iterator = lista.iterator();
			}
		}
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if (map != null) {
			map.clear();
			map = null;
			Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		}
		id = 0;
		treeGrupo.setVisible(true);
	}
	@Listen("onClick = #btnVisualizarFuncionalidades")
	public void visualizarFuncionalidades() {
		
		treeGrupo.setVisible(true);
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (!treeitem.isOpen()) {
				treeitem.setOpen(true);
				lista = treeChildren.getItems();
				iterator = lista.iterator();
			}
		}
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		List<Arbol> listaArbol = servicioArbol.buscarporGrupo(grupo);
		for (int i = 0; i < listaArbol.size(); i++) {
			for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
				Treeitem treeitem = (Treeitem) iterator.next();
				if (listaArbol.get(i).getNombre().equals(treeitem.getLabel())) {
					treeitem.setSelected(true);
				}
			}
		}
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (treeitem.isOpen()) {
				treeitem.setOpen(false);
				lista = treeGrupo.getTreechildren().getItems();
				iterator = lista.iterator();
			}
		}
	}
	@Listen("onClick = #btnEliminarGrupo")
	public void eliminarGrupo() {
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		grupo.setEstatus(false);
		servicioGrupo.guardarGrupo(grupo);
		cancelarGrupo();
	}
	@Listen("onClick = #btnCatalogoGrupo")
	public void buscarItem() {
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoGrupo.zul", null, null);
		window.doModal();
		catalogoGrupo.recibir("maestros/VCrearGrupo");
	}
}