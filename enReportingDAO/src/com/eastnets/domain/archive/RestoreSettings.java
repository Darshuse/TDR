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
 * RestoreSettings POJO
 * @author EastNets
 * @since September 19, 2012
 */
public class RestoreSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3408586814732257672L;
	private Long allianceId;
	private String restorePath;
	private String allianceName;
	private List<String>selectedArchives;
	
	public Long getAllianceId() {
		return allianceId;
	}
	public void setAllianceId(Long allianceId) {
		this.allianceId = allianceId;
	}
	public String getRestorePath() {
		return restorePath;
	}
	public void setRestorePath(String restorePath) {
		this.restorePath = restorePath;
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

	
	
}
