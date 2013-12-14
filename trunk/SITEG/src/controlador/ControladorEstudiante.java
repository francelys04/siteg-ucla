package controlador;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Arbol;
import modelo.Estudiante;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import servicio.SArbol;
import configuracion.GeneradorBeans;

@Controller
public class ControladorEstudiante extends SelectorComposer<Component> {

	SArbol servicioArbol = GeneradorBeans.getServicioArbol();

	@Wire
	private Tree treeMenu;

	List<Arbol> listaArbol = servicioArbol.listarArbol();

	@Listen("onSelect = #treeMenu")
	public void seleccionarArbol() {
		
		
		
		Treechildren tc = treeMenu.getTreechildren();
		Collection<Treeitem> lista = tc.getItems();
		Treeitem treeItemNombre = treeMenu.getSelectedItem();
		Treeitem treeitem;

		String nombreItem = String.valueOf(treeItemNombre.getLabel());
		System.out.println(nombreItem);
		Arbol arbolItem = servicioArbol.buscarPorNombreArbol(nombreItem);
		listaArbol.remove(arbolItem.getId());
		long temp=arbolItem.getHijo();
		System.out.println(arbolItem);
		for (int i = 0; i < listaArbol.size(); i++) {
			if (temp == listaArbol.get(i).getId()) {
				for (Iterator<?> iterator = lista.iterator(); iterator
						.hasNext();) {
					System.out.println("For arbol");
					treeitem = (Treeitem) iterator.next();
					if (listaArbol.get(i).getNombre()
							.equals(treeitem.getLabel())) {
						System.out.println("Item Arbol:" + treeitem.getLabel());
						System.out.println("Item ListaArbol:"
								+ listaArbol.get(i).getNombre());
						
					//	i=0;

						if (treeItemNombre.isSelected()) {
							System.out.println("true");
							treeitem.setSelected(true);
							
						} else {
							if(treeitem.isSelected()){
								treeitem.setSelected(false);
								System.out.println("seleccionado");
								}

						}

					}
				}
				temp=listaArbol.get(i).getHijo();
				i=0;
				
			}
			
		}
	}

}


