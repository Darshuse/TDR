package com.eastnets.domain.viewer;

import com.eastnets.domain.viewer.nodes.AlternativeNode;
import com.eastnets.domain.viewer.nodes.FieldNode;
import com.eastnets.domain.viewer.nodes.LoopNode;
import com.eastnets.domain.viewer.nodes.OptionNode;
import com.eastnets.domain.viewer.nodes.SequenceNode;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.entry.Alternative;
import com.eastnets.resilience.textparser.syntax.entry.Field;
import com.eastnets.resilience.textparser.syntax.entry.Loop;
import com.eastnets.resilience.textparser.syntax.entry.Option;
import com.eastnets.resilience.textparser.syntax.entry.Sequence;

public class EntryFactory {	

	public static EntryNode createField(Entry entry, EntryNode parent) {
		if ( entry == null ){
			return null;
		}
		if ( entry.getClass() == Field.class){
			return new FieldNode((Field)entry, parent);
		}
		if ( entry.getClass() == Alternative.class){
			return new AlternativeNode((Alternative)entry, parent);
		}
		if ( entry.getClass() == Loop.class){
			return new LoopNode((Loop)entry, parent);
		}
		if ( entry.getClass() == Option.class){
			return new OptionNode((Option)entry, parent);
		}
		if ( entry.getClass() == Sequence.class){
			return new SequenceNode((Sequence)entry, parent);
		}
		System.out.println("Unsupported syntax node class : " + entry.getClass().getName());
		return null;
	}
	
}
