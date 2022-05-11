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

package com.eastnets.domain.viewer;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * AdvancedSearchCriteria POJO
 * 
 * @author EastNets
 * @since September 30, 2012
 */
public class AdvancedSearchCriteria implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1725708953610630803L;
	private String subSQLH;
	private String subSQLT;
	private String subSQLHUnion;

	public void clearAll() {
		setSubSQLH(null);
		setSubSQLT(null);
		setSubSQLHUnion(null);
	}

	public boolean isEmpty() {
		return (StringUtils.isEmpty(subSQLH) && StringUtils.isEmpty(subSQLT) && StringUtils.isEmpty(subSQLHUnion));
	}

	public void setSubSQLH(String subSQLH) {
		this.subSQLH = subSQLH;
	}

	public String getSubSQLH() {
		return subSQLH;
	}

	public void setSubSQLT(String subSQLT) {
		this.subSQLT = subSQLT;
	}

	public String getSubSQLT() {
		return subSQLT;
	}

	public void setSubSQLHUnion(String subSQLHUnion) {
		this.subSQLHUnion = subSQLHUnion;
	}

	public String getSubSQLHUnion() {
		return subSQLHUnion;
	}

	public void appendSubSQLT(String string) {
		if (subSQLT == null)
			subSQLT = "";
		subSQLT += string;
	}

	public void appendSubSQLHUnion(String string) {
		if (subSQLHUnion == null)
			subSQLHUnion = "";
		subSQLHUnion += string;

	}

	public void appendSubSQLH(String string) {
		if (subSQLH == null)
			subSQLH = "";
		subSQLH += string;
	}

}
