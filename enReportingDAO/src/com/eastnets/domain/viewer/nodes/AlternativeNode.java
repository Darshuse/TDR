package com.eastnets.domain.viewer.nodes;

import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.resilience.textparser.syntax.entry.Alternative;
import com.eastnets.resilience.textparser.syntax.entry.Alternative.AlternativeEntry;
import com.eastnets.resilience.textparser.syntax.entry.Field;

public class AlternativeNode extends EntryNode {
	String cashedId =null;
	String cashedTag =null;
	
	public AlternativeNode(Alternative node, EntryNode parent) {
		super(node, parent);
		setType("Alternative");
	}
	

	@Override
	public String getTag(){
		if ( cashedTag == null ){
			cashedTag= "";
			for (AlternativeEntry alternate : ((Alternative)node).getAlternates() ){
				Field field= alternate.getField();
				cashedTag = field.getTag() + "a";
				break;
			}
		}
		return cashedTag;
	}

	@Override
	public String getId(){
		if ( cashedId == null  ){
			cashedId= "";
			for (AlternativeEntry field : ((Alternative)node).getAlternates()  ){
				if ( !cashedId.isEmpty() ){
					cashedId+="|";
				}
				cashedId+=field.getField().getTag();
			}
		}
		return cashedId;
	}

	@Override
	public String getExpansion(){
		for (AlternativeEntry alternate : ((Alternative)node).getAlternates() ){
			Field field= alternate.getField();
			return FieldExpantionFormatter.formatExpantion(field.getExpansion());
		}
		return "";
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

}
