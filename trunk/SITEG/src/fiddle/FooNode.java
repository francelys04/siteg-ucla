package fiddle;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.zkoss.zul.Treeitem;
public class FooNode {
	private FooNode _parent;
	private List<FooNode> _children;
	private int _index;
	private String _label = "";
	public FooNode (FooNode parent, int index, String label) {
		_parent = parent;
		_index = index;
		_label = label;
	}
	public void setParent (FooNode parent) {
		_parent = parent;
	}
	public FooNode getParent () {
		return _parent;
	}

	public void appendChild (FooNode child) {
		if (_children == null)
			_children = new ArrayList();
		_children.add(child);
	}
	public List<FooNode> getChildren () {
		if (_children == null)
			return Collections.EMPTY_LIST;
		return _children;
	}
	public void setIndex (int index) {
		_index = index;
	}
	public int getIndex () {
		return _index;
	}
	public String getLabel () {
		return _label;
	}
	public String toString () {
		return getLabel();
	}
}
