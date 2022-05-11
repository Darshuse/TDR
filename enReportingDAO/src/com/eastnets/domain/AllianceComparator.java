package com.eastnets.domain;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.math.NumberUtils;


public class AllianceComparator implements Serializable, Comparator<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1851811453710124293L;

	@Override
	public int compare(String o1, String o2) {
		if( (o1 == null || o1.equalsIgnoreCase("All")) && o2 != null) {
			return -1;
		} else if(o1 != null && (o2 == null || o2.equalsIgnoreCase("All"))){
			return 1;
		}
		Long long1 = null;
		Long long2 = null;
		
		if(NumberUtils.isNumber(o1)){
			long1 = Long.parseLong(o1);
		}else{
			int indexOf1 = o1.indexOf("-");
			String substring1 = o1.substring(0, indexOf1);
			long1 = Long.parseLong(substring1);
			
		}
		
		if(NumberUtils.isNumber(o2)){
			long2 = Long.parseLong(o2);
		}else{
			int indexOf2 = o2.indexOf("-");
			String substring2 = o2.substring(0, indexOf2);
			long2 = Long.parseLong(substring2);
		}
		
		
		int compareTo = long1.compareTo(long2);
		return compareTo;
		
	}
}
