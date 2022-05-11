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

package com.eastnets.domain.watchdog;

import java.util.Date;

import com.eastnets.domain.BaseEntity;

/**
 * EventRequest POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class EventRequest extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5500559219017365501L;
	private Long id;
	private String description;
	private String componentName;
	private String eventNbr;
	private String email;
	private Date expirationDate;

	
	public EventRequest(){
		
		description ="";
		componentName ="";
		eventNbr = null;
		expirationDate =null;

	}

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

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	
	public String getEventNbr() {
		return eventNbr;
	}

	public void setEventNbr(String eventNbr) {
		this.eventNbr = eventNbr;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

}
