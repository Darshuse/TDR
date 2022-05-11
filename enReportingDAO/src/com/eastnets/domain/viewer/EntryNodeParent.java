package com.eastnets.domain.viewer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.eastnets.resilience.textparser.syntax.Entry;

public abstract class EntryNodeParent extends EntryNode {
	protected List<EntryNode> children = new ArrayList<EntryNode>();
	
	public EntryNodeParent(Entry node, EntryNode parent) {
		super(node, parent);
	}
	
	@Override
	public String getTag() {
		return "";
	}

	@Override
	public String getId() {
		return "";
	}	
	
	@Override
	public String getExpansion() {
		return "";
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}


	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}


	@Override
	public boolean getAllowsChildren() {
		return true;
	}


	@Override
	public Enumeration<EntryNode> children() {
		return asEnumeration(children.iterator());
	}
	
	//stolen from com.google.common.collect.Iterators form google-collections source ^_^	
	public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
	    if (iterator == null ){
	    	return null;
	    }
	    return new Enumeration<T>() {
	      public boolean hasMoreElements() {
	        return iterator.hasNext();
	      }
	      public T nextElement() {
	        return iterator.next();
	      }
	    };
	  }
	
	public List<EntryNode> getChildNodes(){
		return children;
	}

}
