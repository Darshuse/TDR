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

package com.eastnets.domain;

import java.io.Serializable;

/**
 * Alliance POJO
 * 
 * @author EastNets
 * @since September 20, 2012
 */
public class Alliance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7037857902675864990L;
	private String id;// alliance identifier .. a.k.a AID
	private String name;
	private String displayName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setDisplayName(name);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String name) {
		displayName = name != null ? name : "N.A.";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alliance other = (Alliance) obj;

		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s[id,%s]", getClass().getSimpleName(), getId());
	}

}
