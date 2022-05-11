package com.eastnets.encdec;

import java.io.Serializable;



public class ConnectionSettings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 377859962389766310L;
	private String userName ;
	private String password ;
	private String serviceName;
	private Integer portNumber;
	private String serverName ;
	private String dbServiceName;
	
	public String getDbServiceName() {
		return dbServiceName;
	}
	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
