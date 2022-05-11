package com.eastnets.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class sUsersPasswordId implements Serializable {

	private static final long serialVersionUID = -6284275466006224285L;

	@Column(name = "PASSWORD_DATE")
	private LocalDateTime passwordDate;

	@Column(name = "USERID")
	private Long userID;

	public LocalDateTime getPasswordDate() {
		return passwordDate;
	}

	public void setPasswordDate(LocalDateTime passwordDate) {
		this.passwordDate = passwordDate;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

}
