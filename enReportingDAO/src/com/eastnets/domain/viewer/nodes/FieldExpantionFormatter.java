package com.eastnets.domain.viewer.nodes;

import java.util.ArrayList;
import java.util.List;

public class FieldExpantionFormatter {
	
	private static List<String> invalidSuffix= new ArrayList<String>();
	
	static{
		invalidSuffix.add("-Name and Addr");
		invalidSuffix.add(" - FI BIC");
		invalidSuffix.add(" - BIC");
	}
	

	public static String formatExpantion(String expantion){
		if ( expantion == null ){
			return "";
		}
		for ( String item : invalidSuffix){
			if (expantion.endsWith(item)){
				return expantion.substring(0, expantion.length() - item.length() );
			}
		}
		return expantion;
	}
	
	
}
