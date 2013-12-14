package fiddle;


import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

public class FooModel extends AbstractTreeModel<Object> {
	private FooNode _root;
    public FooModel(Object root) {
    	// set the root
        super(root);
        _root = (FooNode)root;
    }
    public boolean isLeaf(Object node) {
        return ((FooNode)node).getChildren().size() == 0; //at most 4 levels
    }
    public Object getChild(Object parent, int index) {
        return ((FooNode)parent).getChildren().get(index);
    }
    public int getChildCount(Object parent) {
        return ((FooNode)parent).getChildren().size(); //each node has 5 children
    }
    public int getIndexOfChild(Object parent, Object child) {
    	List<FooNode> children = ((FooNode)parent).getChildren();
    	for (int i = 0; i < children.size(); i++) {
    		if (children.get(i).equals(children))
    			return i;
    	}
        return -1;
    }
};