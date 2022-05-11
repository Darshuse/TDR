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

package com.eastnets.domain.archive;

import java.io.Serializable;
import java.util.List;

/**
 * ArchiveSettings POJO
 * @author EastNets
 * @since September 20, 2012
 */
public class ArchiveSettings implements Serializable, Comparable<ArchiveSettings>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1642273481114770977L;
	private Long allianceId;
	private String allianceName;
	List<String> selectedArchives;
	
	public Long getAllianceId() {
		return allianceId;
	}
	public void setAllianceId(Long allianceId) {
		this.allianceId = allianceId;
	}
	public String getAllianceName() {
		return allianceName;
	}
	public void setAllianceName(String allianceName) {
		this.allianceName = allianceName;
	}
	public List<String> getSelectedArchives() {
		return selectedArchives;
	}
	
	public void setSelectedArchives(List<String> selectedArchives) {
		this.selectedArchives = selectedArchives; 
	}
	@Override
	public int compareTo(ArchiveSettings arg0) {
		
		if(arg0 == null || this.allianceId > arg0.getAllianceId()){
			return 1;
		}
		if(this.allianceId < arg0.getAllianceId()){
			return -1;
		}
		return 0;
	}
	
	
}
