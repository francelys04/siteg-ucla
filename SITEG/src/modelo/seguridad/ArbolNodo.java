package modelo.seguridad;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ArbolNodo {
	private ArbolNodo _parent;
	private List<ArbolNodo> _children;
	private int _index;
	private String _label = "";
	public ArbolNodo (ArbolNodo parent, int index, String label) {
		_parent = parent;
		_index = index;
		_label = label;
	}
	public void setParent (ArbolNodo parent) {
		_parent = parent;
	}
	public ArbolNodo getParent () {
		return _parent;
	}

	public void appendChild (ArbolNodo child) {
		if (_children == null)
			_children = new ArrayList();
		_children.add(child);
	}
	public List<ArbolNodo> getChildren () {
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
