package com.eastnets.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SUSER")
public class User implements Serializable {
	private static final long serialVersionUID = 3307522807196321592L;

	@Id
	@Column(name = "USERID")
	private Integer userID;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "authenticationmethod")
	private String authenticationmethod;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserPassword> details = new ArrayList<>();

	@Column(name = "GROUPID")
	private String groupId;

	public Integer getUserID() {
		return userID;
	}

	public String getAuthenticationmethod() {
		return authenticationmethod;
	}

	public void setAuthenticationmethod(String authenticationmethod) {
		this.authenticationmethod = authenticationmethod;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<UserPassword> getDetails() {
		return details;
	}

	public void setDetails(List<UserPassword> details) {
		this.details = details;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
