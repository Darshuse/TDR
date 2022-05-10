package com.eastnets.watchdog.mail;

public class MailResponse {

	private String message;
	private Integer status;

	public MailResponse() {
	}

	public MailResponse(String message, Integer status) {
		super();
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
