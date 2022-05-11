package com.eastnets.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = -2906592677081000797L;
	@XmlElement(name = "Username")
	private String username;
	@XmlElement(name = "Password")
	private String password;

	// need default constructor for JSON Parsing
	public AuthenticationRequest() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}