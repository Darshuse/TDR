package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GrantedRolesId implements Serializable {

	private static final long serialVersionUID = -6711304424097441789L;

	@Column(name = "role_name")
	private String roleName;
	@Column(name = "group_id")
	private String groupId;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
