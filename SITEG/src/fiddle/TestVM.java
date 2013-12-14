package fiddle;

import java.util.*;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import modelo.Arbol;
import modelo.Estudiante;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
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
import configuracion.GeneradorBeans;

public class TestVM extends SelectorComposer<Component> {
	TreeModel _model;
	SArbol servicioArbol = GeneradorBeans.getServicioArbol();

	@Wire
	private Tree treeMenu;
	@Wire
	private Include formularios;

	List<String> listmenu1 = new ArrayList();

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(auth.getAuthorities());
		// List<GrantedAuthority> authorities = new
		// ArrayList<GrantedAuthority>(auth.getAuthorities());
		// System.out.println(authorities.toString());
		// authorities.add(new GrantedAuthorityImpl('ROLE_NEWROLE'));
		// Authentication newAuth = new
		// UsernamePasswordToken(auth.getPrincipal(),auth.getCredentials(),authorities)
		// SecurityContextHolder.getContext().setAuthentication(newAuth);
		
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

	public TreeModel getModel() {
		if (_model == null) {
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
		//System.out.println(arboles.toString());
		//arbolito = servicioArbol.ordenarPorID(ids);
		System.out.println(ids.toString());
		Collections.sort(ids);
		System.out.println(ids.toString());
		for(int t = 0; t<ids.size(); t++){
			Arbol a;
			a = servicioArbol.buscarPorId(ids.get(t));
			arboles.add(a);
		}
		System.out.println(arbole.toString());
		System.out.println(arboles.toString());
		//List<Arbol> arboles = servicioArbol.listarArbol(); 

		
		long temp1,temp2,temp3=0;
		
		for (int i = 0; i < arboles.size(); i++) {

			if(arboles.get(i).getHijo()==0){
				oneLevelNode= new FooNode(root, i, arboles.get(i).getNombre());
				root.appendChild(oneLevelNode);
				temp1=arboles.get(i).getId();
				arboles.remove(i);
				
				
				for(int j=i;j<arboles.size();j++){

					if(temp1==arboles.get(j).getHijo()){
						twoLevelNode= new FooNode(oneLevelNode, i, arboles.get(j).getNombre());
						oneLevelNode.appendChild(twoLevelNode);
						temp2=arboles.get(j).getId();
						arboles.remove(j);
						
						for(int k=j; k<arboles.size();k++){

							if(temp2==arboles.get(k).getHijo()){
								threeLevelNode= new FooNode(twoLevelNode, i, arboles.get(k).getNombre());
								twoLevelNode.appendChild(threeLevelNode);
								temp3=arboles.get(k).getId();
								arboles.remove(k);
								
								for(int z=k; z<arboles.size();z++){

									if(temp3==arboles.get(z).getHijo()){
										fourLevelNode= new FooNode(threeLevelNode, i, arboles.get(z).getNombre());
										threeLevelNode.appendChild(fourLevelNode);
										arboles.remove(z);
										
										z=z-1;
									}
									
										
								}
								k=k-1;
							}
							
						}
						j=j-1 ;	
					}
					
				}
				
					
				
			i=i-1;	
			}
			}
		
		
		System.out.println(arboles.toString());
		return root;
		
	}

	@Listen("onSelect = #treeMenu")
	public void selectedNode() {
		String item = String.valueOf(treeMenu.getSelectedItem().getValue());
		if (treeMenu.getSelectedItem().getLevel() > 0) {
			Arbol arbolItem = servicioArbol.buscarPorNombreArbol(item);
			System.out.println(arbolItem.getUrl());
			String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
			formularios.setSrc(null);
			formularios.setSrc(ruta);

		}

	}
}