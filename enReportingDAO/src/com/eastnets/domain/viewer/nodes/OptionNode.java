package com.eastnets.domain.viewer.nodes;

import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.resilience.textparser.syntax.entry.Option;
import com.eastnets.resilience.textparser.syntax.entry.Option.OptionEntry;

public class OptionNode extends EntryNode {	
	String cashedId =null;
	
	
	@Override
	public String getTag(){
		return ((Option)node).getTag() + "a";
	}
	@Override
	public String getId(){
		if (cashedId == null ){
			cashedId= "";
			for (OptionEntry field : ((Option)node).getOptions()  ){
				if ( !cashedId.isEmpty() ){
					cashedId+="|";
				}
				cashedId+=field.getField().getTag() + field.getField().getOptionLetter();
			}
		}
		return cashedId;
	}

	@Override
	public String getExpansion(){
		for (OptionEntry opt : ((Option)node).getOptions() ){
			return FieldExpantionFormatter.formatExpantion(opt.getExpansion());
		}
		return "";
	}

	public OptionNode(Option node, EntryNode parent) {
		super(node, parent);
		setType("Option");
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

}
