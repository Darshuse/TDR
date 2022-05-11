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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.BaseEntity;

/**
 * Profile POJO
 * 
 * @author EastNets
 * @since August 28, 2012
 */
public class Profile extends BaseEntity implements Comparable<Profile> {

	// test

	/**
	 * 
	 */
	private static final long serialVersionUID = -6618966432346039900L;
	protected String name;
	protected String description;

	protected Long groupId=1L;
	protected ApprovalStatus approvalStatus;
	protected Long wdNbDayHistory = 7L;
	protected Long vwListDepth = 100L;
	protected String rpDirectory = null;
	protected String biDirectory = null;
	protected String senderEmail;
	protected Long connectionTimeout = 0L;

	private List<String> profileRoles = new ArrayList<String>();

	public Profile() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().equals("")) {
			this.name = null;
		} else {
			this.name = name;

		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null || description.trim().equals("")) {
			this.description = null;
		} else {
			this.description = description;

		}
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public ApprovalStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Long getWdNbDayHistory() {
		return wdNbDayHistory;
	}

	public void setWdNbDayHistory(Long wdNbDayHistory) {
		this.wdNbDayHistory = wdNbDayHistory;
	}

	public Long getVwListDepth() {
		return vwListDepth;
	}

	public void setVwListDepth(Long vwListDepth) {
		this.vwListDepth = vwListDepth;
	}

	public String getRpDirectory() {
		return rpDirectory;
	}

	public void setRpDirectory(String rpDirectory) {
		this.rpDirectory = rpDirectory;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public Long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	@Override
	public boolean equals(Object obj) {

		boolean isEqual = false;

		if (obj != null) {
			Profile currentProfile = (Profile) obj;

			if (name.trim().equalsIgnoreCase(currentProfile.getName().trim()) && this.compareTo(currentProfile) == 0)
				isEqual = true;
		}

		return isEqual;
	}

	public boolean isLsaApproved() {

		return this.approvalStatus.id == Constants.LSA_APPROVED_WAITING_RSA_APPROVAL;
	}

	public boolean isRsaApproved() {

		return this.approvalStatus.id == Constants.RSA_APPROVED_WAITING_LSA_APPROVAL;
	}

	@Override
	public int compareTo(Profile o) {

		if (o == null) {
			return 1;
		}

		if (!this.name.trim().equalsIgnoreCase(o.getName().trim())) {
			return -1;
		}

		if (this.description != o.description) {
			if (this.description != null && this.description.length() > 0 && !this.description.equals(o.description)) {
				return -1;
			} else if (o.description != null && o.description.length() > 0 && !o.description.equals(this.description)) {
				return -1;
			}
		}

		if (this.groupId != o.groupId) {
			if (this.groupId != null && !this.groupId.equals(o.groupId)) {
				return -1;
			} else if (o.groupId != null && !o.groupId.equals(this.groupId)) {
				return -1;
			}
		}

		if (this.approvalStatus != o.approvalStatus) {
			if (this.approvalStatus != null && !this.approvalStatus.equals(o.approvalStatus)) {
				return -1;
			} else if (o.approvalStatus != null && !o.approvalStatus.equals(this.approvalStatus)) {
				return -1;
			}
		}

		if (this.wdNbDayHistory != o.wdNbDayHistory) {
			if (this.wdNbDayHistory != null && !this.wdNbDayHistory.equals(o.wdNbDayHistory)) {
				return -1;
			} else if (o.wdNbDayHistory != null && !o.wdNbDayHistory.equals(this.wdNbDayHistory)) {
				return -1;
			}
		}

		if (this.vwListDepth != o.vwListDepth) {
			if (this.vwListDepth != null && !this.vwListDepth.equals(o.vwListDepth)) {
				return -1;
			} else if (o.vwListDepth != null && !o.vwListDepth.equals(this.vwListDepth)) {
				return -1;
			}
		}

		if (this.rpDirectory != o.rpDirectory) {
			if (this.rpDirectory != null && this.rpDirectory.length() > 0 && !this.rpDirectory.equals(o.rpDirectory)) {
				return -1;
			} else if (o.rpDirectory != null && o.rpDirectory.length() > 0 && !o.rpDirectory.equals(this.rpDirectory)) {
				return -1;
			}
		}

		if (this.biDirectory != o.biDirectory) {
			if (this.biDirectory != null && this.biDirectory.length() > 0 && !this.biDirectory.equals(o.biDirectory)) {
				return -1;
			} else if (o.biDirectory != null && o.biDirectory.length() > 0 && !o.biDirectory.equals(this.biDirectory)) {
				return -1;
			}
		}

		if (this.senderEmail != o.senderEmail) {
			if (this.senderEmail != null && this.senderEmail.length() > 0 && !this.senderEmail.equals(o.senderEmail)) {
				return -1;
			} else if (o.senderEmail != null && o.senderEmail.length() > 0 && !o.senderEmail.equals(this.senderEmail)) {
				return -1;
			}
		}
		if (this.connectionTimeout != o.connectionTimeout) {
			if (this.connectionTimeout != null && !this.connectionTimeout.equals(o.connectionTimeout)) {
				return -1;
			} else if (o.connectionTimeout != null && !o.connectionTimeout.equals(this.connectionTimeout)) {
				return -1;
			}
		}
		if (this.profileRoles != o.profileRoles) {
			if (this.profileRoles != null && !this.profileRoles.equals(o.profileRoles)) {
				return -1;
			} else if (o.profileRoles != null && !o.profileRoles.equals(this.profileRoles)) {
				return 1;
			}
		}

		return 0;
	}

	public String getBiDirectory() {
		return biDirectory;
	}

	public void setBiDirectory(String biDirectory) {
		this.biDirectory = biDirectory;
	}

	public List<String> getProfileRoles() {
		return profileRoles;
	}

	public void setProfileRoles(List<String> profileRoles) {
		this.profileRoles.clear();
		if (profileRoles == null || profileRoles.isEmpty()) {
			return;
		}
		this.profileRoles.addAll(profileRoles);
		Collections.sort(this.profileRoles);
	}
}
