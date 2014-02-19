package controlador.seguridad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import modelo.seguridad.Arbol;
import modelo.seguridad.ArbolModelo;
import modelo.seguridad.ArbolNodo;
import modelo.seguridad.Grupo;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import controlador.CGeneral;
import controlador.catalogo.CCatalogoGrupo;

/*
 * Controlador que permite crear un nuevo grupo, a su vez se asocian a este un
 * subconjunto de las funcionalidades del sistema
 */
@Controller
public class CCrearGrupo extends CGeneral {

	private static final long serialVersionUID = -1689987977281226750L;
	long id = 0;
	TreeModel _model;
	CCatalogoGrupo catalogoGrupo = new CCatalogoGrupo();
	public static List<String> funcionalidades = new ArrayList<String>();

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
	@Wire
	private Listbox ltbFuncionalidadesSeleccionados;
	@Wire
	private Window wdwCrearGrupo;

	/*
	 * Metodo heredado del controlador CGeneral, se hace el llamado al metodo
	 * que crea el arbol de las funcionalidades del sistema
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

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

	/*
	 * Metodo que permite retornar el modelo del arbol
	 */
	public TreeModel getModel() {
		if (_model == null) {
			_model = new ArbolModelo(getFooRoot());
		}
		return _model;

	}

	/*
	 * Metodo que permite crear el arbol-menu del sistema, en el cual cada
	 * funcionalidad esta representada por un nodo hoja de este
	 */
	private ArbolNodo getFooRoot() {

		ArbolNodo root = new ArbolNodo(null, 0, "");
		ArbolNodo oneLevelNode = new ArbolNodo(null, 0, "");
		ArbolNodo twoLevelNode = new ArbolNodo(null, 0, "");
		ArbolNodo threeLevelNode = new ArbolNodo(null, 0, "");
		ArbolNodo fourLevelNode = new ArbolNodo(null, 0, "");
		List<Arbol> listaArbol = servicioArbol.listarArbol();
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

	/*
	 * Metodo que permite verificar que solo sean seleccionados los nodos hoja,
	 * para evitar añadir funcionalidades erroneas
	 */
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

	/*
	 * Metodo que permite mostrar un resumen de las funcionalidades que han sido
	 * seleccionadas para el grupo
	 */
	public void llenarFuncionalidadesSeleccionadas() {
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		List<Arbol> listaArbol = servicioArbol.buscarporGrupo(grupo);
		int ItemEncontrado = 0;
		for (int i = 0; i < listaArbol.size(); i++) {

			long padre = listaArbol.get(i).getId();
			ItemEncontrado = 0;
			for (int j = 0; j < listaArbol.size(); j++) {
				System.out.println("entro2");
				long hijo = listaArbol.get(j).getHijo();

				if (padre == hijo) {
					ItemEncontrado = 1;
					j = listaArbol.size();
				}
			}
			if (ItemEncontrado == 0) {
				funcionalidades.add(listaArbol.get(i).getNombre());
			}
		}
		ltbFuncionalidadesSeleccionados.setModel(new ListModelList<String>(
				funcionalidades));
	}

	/*
	 * Metodo que permite que al seleccionar un nodo hoja del arbol, se marquen
	 * con el check su respectivos antecesores. Se verifica que si existe un
	 * nodo hermano marcado los antecesores no son desmarcados
	 */
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

			if (itemSeleccionado.isSelected()) {

				funcionalidades.add(nombreItem);

				ltbFuncionalidadesSeleccionados
						.setModel(new ListModelList<String>(funcionalidades));
			} else {

				List<Listitem> listaFuncionalidadesSeleccionadas = ltbFuncionalidadesSeleccionados
						.getItems();
				for (int i = 0; i < listaFuncionalidadesSeleccionadas.size(); i++) {
					if (listaFuncionalidadesSeleccionadas.get(i).getLabel()
							.equals(nombreItem)) {
						ltbFuncionalidadesSeleccionados
								.removeItemAt(listaFuncionalidadesSeleccionadas
										.get(i).getIndex());
					}
				}
				funcionalidades.remove(nombreItem);
				ltbFuncionalidadesSeleccionados
						.setModel(new ListModelList<String>(funcionalidades));
			}
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

	/*
	 * Metodo que se encarga de guardar el grupo, asi como tambien todas las
	 * funcionalidades que fueron asociadas a este
	 */
	@Listen("onClick = #btnGuardarGrupo")
	public void guardarGrupo() {
		Messagebox.show("¿Desea guardar los datos del grupo?",
				"Dialogo de confirmacion", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener<Event>() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							List<Arbol> listaArbol = servicioArbol
									.listarArbol();
							Set<Arbol> arboles = new HashSet<Arbol>();
							Treechildren treeChildren = treeGrupo
									.getTreechildren();
							Collection<Treeitem> lista = treeChildren
									.getItems();
							String nombreGrupo = txtNombreGrupo.getValue();
							Grupo grupo = servicioGrupo
									.BuscarPorNombre(nombreGrupo);
							if (id == 0 && grupo == null || id != 0) {
								for (int i = 0; i < listaArbol.size(); i++) {
									for (Iterator<?> iterator = lista
											.iterator(); iterator.hasNext();) {
										Treeitem treeitem = (Treeitem) iterator
												.next();
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
								Grupo grupo1 = new Grupo(id, nombre, estatus,
										arboles);
								servicioGrupo.guardarGrupo(grupo1);
								Messagebox.show(
										"Grupo registrado exitosamente",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
								cancelarGrupo();
							} else {
								Messagebox.show("Grupo no disponible", "Error",
										Messagebox.OK, Messagebox.ERROR);
								cancelarGrupo();
							}
						}
					}
				});
	}

	/*
	 * Metodo que permite limpiar los campos de la vista, asi como tambien la
	 * limpieza del arbol de las funcionalidades
	 */
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
		funcionalidades.clear();
		ltbFuncionalidadesSeleccionados.setModel(new ListModelList<String>(
				funcionalidades));

	}

	/*
	 * Metodo que permite visualizar las funcionalidades asociadas a un
	 * determinado grupo. Este boton aparece una vez que se ha buscado un grupo
	 * desde el catalogo
	 */
	@Listen("onClick = #btnVisualizarFuncionalidades")
	public void visualizarFuncionalidades() {
		llenarFuncionalidadesSeleccionadas();
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

	/* Metodo que permite la eliminacion logica de una entidad Grupo */
	@Listen("onClick = #btnEliminarGrupo")
	public void eliminarGrupo() {
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		grupo.setEstatus(false);
		servicioGrupo.guardarGrupo(grupo);
		cancelarGrupo();
	}
	
	
	/* Metodo que permite cerrar la vista crear grupo*/
	@Listen("onClick = #btnSalirCrearGrupo")
	public void salirGrupo() {
		
		wdwCrearGrupo.onClose();
	
	}

	

	/*
	 * Metodo que permite abrir el catalogo correspondiente y se envia al metodo
	 * del catalogo el nombre de la vista a la que deben regresar los valores
	 */
	@Listen("onClick = #btnCatalogoGrupo")
	public void buscarItem() {
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoGrupo.zul", null, null);
		window.doModal();
		catalogoGrupo.recibir("maestros/VCrearGrupo");
	}

}