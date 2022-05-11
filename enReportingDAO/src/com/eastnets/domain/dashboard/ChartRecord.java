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

package com.eastnets.domain.dashboard;

import java.io.Serializable;

/**
 * ChartRecord POJO
 * @author EastNets
 * @since July 25, 2012
 */
public class ChartRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -505845693896842253L;
	private String name;
	private String name2;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
