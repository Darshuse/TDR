package com.eastnets.domain.viewer.nodes;

import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.resilience.textparser.syntax.entry.Field;

public class FieldNode extends EntryNode{
	public FieldNode( Field field, EntryNode parent) {
		super( field, parent);
		setType("Field");
	}

	@Override
	public String getTag() {
		return ((Field)node).getTag();
	}

	@Override
	public String getId() {
		return ((Field)node).getPatternId();
	}
	
	@Override
	public String getExpansion() {
		return FieldExpantionFormatter.formatExpantion(((Field)node).getExpansion());
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
}
