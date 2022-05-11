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
 * MsgCatPerProfile POJO
 * @author EastNets
 * @since September 3, 2012
 */
public class MsgCatPerProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6510044959246219954L;
	public static Integer SELECTED_MSG_CAT = 1;
	public static Integer AVAILABLE_MSG_CAT = 0;

	private Long profileId;
	private String msgCat;
	private String desc;
	private Integer isSelectedMsgCat;

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getMsgCat() {
		return msgCat;
	}

	public void setMsgCat(String msgCat) {
		this.msgCat = msgCat;
	}

	public Integer getIsSelectedMsgCat() {
		return isSelectedMsgCat;
	}

	public void setIsSelectedMsgCat(Integer isSelectedMsgCat) {
		this.isSelectedMsgCat = isSelectedMsgCat;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((msgCat == null) ? 0 : msgCat.hashCode());
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
		MsgCatPerProfile other = (MsgCatPerProfile) obj;
		if (msgCat == null) {
			if (other.msgCat != null)
				return false;
		} else if (!msgCat.equals(other.msgCat))
			return false;
		return true;
	}

}
