package com.eastnets.enGpiLoader.utility;

public class XMLMailConfigHelper {

	
	private String mailHost;
	private String mailPort;
	private String mailUsername;
	private String mailPassword;
	private String mailTo;
	private String mailFrom;
	private String mailSubjec="EastNets gpi Notifiers";
	
	
	
	public XMLMailConfigHelper(String mailHost, String mailPort, String mailUsername, String mailPassword,
			String mailTo, String mailSubjec,String mailFrom) { 
		this.mailHost = mailHost;
		this.mailPort = mailPort;
		this.mailUsername = mailUsername;
		this.mailPassword = mailPassword;
		this.setMailTo(mailTo);
		this.mailSubjec = mailSubjec;
		this.mailFrom=mailFrom;
		this.mailTo=mailTo;
	}
	public String getMailHost() {
		return mailHost;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public String getMailPort() {
		return mailPort;
	}
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	public String getMailUsername() {
		return mailUsername;
	}
	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
 
	public String getMailSubjec() {
		return mailSubjec;
	}
	public void setMailSubjec(String mailSubjec) {
		this.mailSubjec = mailSubjec;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	} 
	
	
}
