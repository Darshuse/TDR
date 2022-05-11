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

package com.eastnets.domain.swing;

import java.io.Serializable;

/**
 * UserLink POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class UserLink implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8766797032054764084L;
	private Integer aid;
	private Integer umidh;
	private Integer umidl;
	private String eaiMessageId;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getUmidh() {
		return umidh;
	}

	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}

	public Integer getUmidl() {
		return umidl;
	}

	public void setUmidl(Integer umidl) {
		this.umidl = umidl;
	}

	public String getEaiMessageId() {
		return eaiMessageId;
	}

	public void setEaiMessageId(String eaiMessageId) {
		this.eaiMessageId = eaiMessageId;
	}

}
