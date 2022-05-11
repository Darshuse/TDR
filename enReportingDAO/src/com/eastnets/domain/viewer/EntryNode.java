package com.eastnets.domain.viewer;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.eastnets.resilience.textparser.syntax.Entry;

public abstract class EntryNode implements TreeNode {

	public abstract String getTag();
	public abstract String getExpansion();
	public abstract String getId();
	
	protected String type;
	protected String name;
	protected String value;
	
	protected Entry node;
	protected EntryNode parent;
	
	private boolean checked = true;
	
	public EntryNode(Entry node, EntryNode parent){
		this.node= node;
		this.parent= parent;
		setName( getTag() + " : " + getExpansion());
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Entry getNode() {
		return node;
	}

	public void setNode(Entry node) {
		this.node = node;
	}

	public EntryNode getParent() {
		return parent;
	}

	public void setParent(EntryNode parent) {
		this.parent = parent;
	}	

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}


	@Override
	public int getChildCount() {
		return 0;
	}


	@Override
	public int getIndex(TreeNode node) {
		return 0;
	}


	@Override
	public boolean getAllowsChildren() {
		return false;
	}


	@Override
	public Enumeration<EntryNode> children() {
		return null;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void appendLineToValue(String value) {
		if ( this.value == null )
		{
			this.value= "";
		}
		if ( !this.value.isEmpty() ){
			this.value+= "\n";
		}
		this.value += value;  
		
	}


}
