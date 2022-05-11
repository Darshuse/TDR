package com.eastnets.domain.viewer.nodes;

import com.eastnets.domain.viewer.EntryFactory;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.EntryNodeParent;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.entry.Sequence;

public class SequenceNode extends EntryNodeParent {
	Sequence sequence;
	
	public SequenceNode(Sequence node, EntryNode parent) {
		super(node, parent);
		setType("Sequence");
		
		for ( Entry entry : node.getEntries() ){
			children.add( EntryFactory.createField(entry, this) );
		}
	}
	
	@Override
	public String getTag() {
		return ((Sequence)node).getId();
	}

	@Override
	public String getExpansion() {
		return ((Sequence)node).getExpansion();
	}
}
