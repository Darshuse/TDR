package com.eastnets.domain;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;


public class TreeNode<T> implements Iterable<TreeNode<T>> {

    private T data;
    
    private Object value;

	private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }


    @Override
    public String toString() {
            return data != null ? data.toString() : "[data null]";
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
            TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
            return iter;
    }

    public boolean hasChild( T node ){
    	if ( children.contains(node) ){
    		return true;
    	}    	
    	return false;
    }

    @Override
    public boolean equals(Object obj) {
    	return this.data.equals(((TreeNode<?>)obj).data);
    }

    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}

	public TreeNode<T> getChild(T childValue) {
		TreeNode<T> child = new TreeNode<T>(childValue);
		int index= children.indexOf(child);
		if ( index >= 0 ){
			return children.get(index);
		}
		return null;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}



class TreeNodeIter<T> implements Iterator<TreeNode<T>> {

        enum ProcessStages {
                ProcessParent, ProcessChildCurNode, ProcessChildSubNode
        }

        private TreeNode<T> treeNode;

        public TreeNodeIter(TreeNode<T> treeNode) {
                this.treeNode = treeNode;
                this.doNext = ProcessStages.ProcessParent;
                this.childrenCurNodeIter = treeNode.getChildren().iterator();
        }

        private ProcessStages doNext;
        private TreeNode<T> next;
        private Iterator<TreeNode<T>> childrenCurNodeIter;
        private Iterator<TreeNode<T>> childrenSubNodeIter;

        @Override
        public boolean hasNext() {

                if (this.doNext == ProcessStages.ProcessParent) {
                        this.next = this.treeNode;
                        this.doNext = ProcessStages.ProcessChildCurNode;
                        return true;
                }

                if (this.doNext == ProcessStages.ProcessChildCurNode) {
                        if (childrenCurNodeIter.hasNext()) {
                                TreeNode<T> childDirect = childrenCurNodeIter.next();
                                childrenSubNodeIter = childDirect.iterator();
                                this.doNext = ProcessStages.ProcessChildSubNode;
                                return hasNext();
                        }

                        else {
                                this.doNext = null;
                                return false;
                        }
                }
                
                if (this.doNext == ProcessStages.ProcessChildSubNode) {
                        if (childrenSubNodeIter.hasNext()) {
                                this.next = childrenSubNodeIter.next();
                                return true;
                        }
                        else {
                                this.next = null;
                                this.doNext = ProcessStages.ProcessChildCurNode;
                                return hasNext();
                        }
                }

                return false;
        }

        @Override
        public TreeNode<T> next() {
                return this.next;
        }

        @Override
        public void remove() {
                throw new UnsupportedOperationException();
        }

}