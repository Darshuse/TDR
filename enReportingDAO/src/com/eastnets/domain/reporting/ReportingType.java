/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.domain.reporting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author EastNets
 * @since dNov 8, 2012
 *
 */
public class ReportingType implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4082440662042273791L;
	private Map<String, List<String>> reportsAndCriterias;
	
	/**
	 * 
	 */
	public ReportingType() {
		this.reportsAndCriterias = new LinkedHashMap<String, List<String>>();
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("first saved criteria");
		arrayList.add("second saved criteria");
		
		ArrayList<String> arrayList2 = new ArrayList<String>();
		arrayList2.add("third saved criteria");
		arrayList2.add("fourth saved criteria");
		
		ArrayList<String> arrayList3 = new ArrayList<String>();
		arrayList3.add("fifth saved criteria");
		arrayList3.add("six saved criteria");
		
		ArrayList<String> arrayList4 = new ArrayList<String>();
		arrayList4.add("seventh saved criteria");
		arrayList4.add("8 saved criteria");
		arrayList4.add("9 saved criteria");
		
		this.reportsAndCriterias.put("Fin Messages", arrayList);
		this.reportsAndCriterias.put("Incomming ammount list by LT", arrayList2);
		this.reportsAndCriterias.put("Manual Messages", arrayList3);
		this.reportsAndCriterias.put("Outgoing ammount list by LT", arrayList4);
		
	}

	public Map<String, List<String>> getReportsAndCriterias() {
		return reportsAndCriterias;
	}

	public void setReportsAndCriterias(Map<String, List<String>> reportsAndCriterias) {
		this.reportsAndCriterias = reportsAndCriterias;
	}

}
