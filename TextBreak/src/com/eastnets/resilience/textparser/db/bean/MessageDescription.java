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

package com.eastnets.resilience.textparser.db.bean;

import java.io.Serializable;

/**
 * message type version and type idx holder
 * 
 * @author EHakawati
 * 
 */
public class MessageDescription implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2614554652512882474L;
	private String description;
	private int versionIdx;
	private int messageIdx;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVersionIdx() {
		return versionIdx;
	}

	public void setVersionIdx(int versionIdx) {
		this.versionIdx = versionIdx;
	}

	public int getMessageIdx() {
		return messageIdx;
	}

	public void setMessageIdx(int messageIdx) {
		this.messageIdx = messageIdx;
	}

}
