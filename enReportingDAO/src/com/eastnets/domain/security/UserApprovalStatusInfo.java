package com.eastnets.domain.security;

import java.io.Serializable;


/**
 * 
 * @author HAlfoqahaa
 *
 */
public class UserApprovalStatusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8043432226276329727L;
	private int status;
	private String description;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
