package com.eastnets.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.HttpStatus;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 2413988361554743727L;

	@XmlElement(name = "Token")
	private String token;

	@XmlElement(name = "ExpirationDate")
	private String expirationDate = "";

	@XmlElement(name = "StatusCode")
	private HttpStatus statusCode = HttpStatus.OK;

	public AuthenticationResponse() {

	}

	public AuthenticationResponse(String token, String expirationDate, HttpStatus statusCode) {
		this.token = token;
		this.expirationDate = expirationDate;
		this.statusCode = statusCode;
	}

	public String getToken() {
		return token;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

}
