package modelo.seguridad;


import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

public class ArbolModelo extends AbstractTreeModel<Object> {
	private ArbolNodo _root;
    public ArbolModelo(Object root) {
    	// set the root
        super(root);
        _root = (ArbolNodo)root;
    }
    public boolean isLeaf(Object node) {
        return ((ArbolNodo)node).getChildren().size() == 0; //at most 4 levels
    }
    public Object getChild(Object parent, int index) {
        return ((ArbolNodo)parent).getChildren().get(index);
    }
    public int getChildCount(Object parent) {
        return ((ArbolNodo)parent).getChildren().size(); //each node has 5 children
    }
    public int getIndexOfChild(Object parent, Object child) {
    	List<ArbolNodo> children = ((ArbolNodo)parent).getChildren();
    	for (int i = 0; i < children.size(); i++) {
    		if (children.get(i).equals(children))
    			return i;
    	}
        return -1;
    }
};