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

package com.eastnets.domain.admin;

import java.io.Serializable;

/**
 * ApprovalStatus POJO
 * @author EastNets
 * @since September 2, 2012
 */
public class ApprovalStatus implements Serializable,Comparable<ApprovalStatus> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3366061064482059336L;
	Long id;
	String description;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int compareTo(ApprovalStatus o) {

		if( o == null){
			return 1;
		}
		
		if(this.id == o.id 
			&& this.description == o.description ){
			return 0;
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(o != null && o instanceof ApprovalStatus){
			ApprovalStatus approvalStatus = (ApprovalStatus) o;
			
			if(this.id == approvalStatus.getId() || this.description == approvalStatus.getDescription()){
				return true;
			}
		}
		return false;
	}

}
