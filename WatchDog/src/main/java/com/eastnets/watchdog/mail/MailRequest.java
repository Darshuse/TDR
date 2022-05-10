package com.eastnets.watchdog.mail;

public class MailRequest {

	private String name;
	private String to;
	private String subject;
	private String mailFrom;

	public MailRequest(String name, String to, String subject, String mailFrom) {
		super();
		this.name = name;
		this.to = to;
		this.subject = subject;
		this.mailFrom = mailFrom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

}
