package com.eastnets.domain.viewer.nodes;

import com.eastnets.domain.viewer.EntryFactory;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.EntryNodeParent;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.entry.Loop;

public class LoopNode extends EntryNodeParent {
	Loop loop;

	public LoopNode(Loop node, EntryNode parent) {
		super(node, parent);
		setType("Loop");
		setName(node.getId());

		for (Entry entry : node.getEntries()) {
			children.add(EntryFactory.createField(entry, this));
		}
	}
}
