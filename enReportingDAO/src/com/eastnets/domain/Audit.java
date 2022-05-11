package com.eastnets.domain;

import java.io.Serializable;

/**
 * Used for auditing 
 * @author EastNets
 * @since Nov 26, 2012
 */
public class Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1095317177814335391L;
	private String programName;
	private String loginname;
	private String event;
	private String action;
	private String ipAddress;

	public int retriesNo;

	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getRetriesNo() {
		return retriesNo;
	}
	public void setRetriesNo(int retriesNo) {
		this.retriesNo = retriesNo;
	}
}
