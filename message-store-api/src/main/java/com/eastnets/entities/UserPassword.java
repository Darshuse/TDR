package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUSERPASSWORDS")
public class UserPassword implements Serializable {

	private static final long serialVersionUID = -5461611849231952302L;

	@EmbeddedId
	private sUsersPasswordId passwordId;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "SALT")
	private String salt;

	@ManyToOne
	@JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false, unique = true)
	private User user;

	public sUsersPasswordId getPasswordId() {
		return passwordId;
	}

	public void setPasswordId(sUsersPasswordId passwordId) {
		this.passwordId = passwordId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
