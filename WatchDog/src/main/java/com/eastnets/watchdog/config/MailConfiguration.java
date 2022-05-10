package com.eastnets.watchdog.config;

import org.apache.commons.lang.StringUtils;

public class MailConfiguration {

	private String mailServer;
	private Integer port;
	private String mailFrom;
	private String mailTo;
	private String password;
	private String mailSubject = "Watchdog Notification";

	public String getMailServer() {
		return StringUtils.defaultString(mailServer, "");
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public Integer getPort() {
		if (port == null)
			return 0;
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getMailFrom() {
		return StringUtils.defaultString(mailFrom, "");

	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return StringUtils.defaultString(mailTo, "");

	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getPassword() {
		return StringUtils.defaultString(password, "");

	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

}
