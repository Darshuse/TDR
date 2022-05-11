package com.eastnets.domain.csm;

import java.util.Date;

public class ClientServerConnection {
	
	public static final int STATUS_SERVER_DOWN 		= 0x00000001;
	public static final int STATUS_CLIENT_DOWN		= 0x00000002;
	public static final int STATUS_CONNECTION_DOWN 	= 0x00000004;
	public static final int STATUS_UNKNOWN 			= 0x40000000;
	public static final int STATUS_DIRTY 			= 0x80000000;
	
	
	private Integer serverPort;
	private String serverName;
	private Boolean connectionDownAlarm;
	private String clientName;
	private Boolean clientDownAlarm;
	private Integer hpPeriodSeconds;
	private Integer thresholdPercent;
	private Integer alarmIdleCycles;
	private Date lastServer;
	private Date lastClient;
	private Integer status;
	
	//calculated automatically by the database query
	private Integer secondsFromLastServerRequest;
	private Integer secondsFromLastClientRequest;
	
	public Integer getServerPort() {
		return serverPort;
	}
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public Boolean getConnectionDownAlarm() {
		return connectionDownAlarm;
	}
	public void setConnectionDownAlarm(Boolean connectionDownAlarm) {
		this.connectionDownAlarm = connectionDownAlarm;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Boolean getClientDownAlarm() {
		return clientDownAlarm;
	}
	public void setClientDownAlarm(Boolean clientDownAlarm) {
		this.clientDownAlarm = clientDownAlarm;
	}
	public Integer getHPPeriodSeconds() {
		return hpPeriodSeconds;
	}
	public void setHPPeriodSeconds(Integer hpPeriodSeconds) {
		this.hpPeriodSeconds = hpPeriodSeconds;
	}
	public Integer getThresholdPercent() {
		return thresholdPercent;
	}
	public void setThresholdPercent(Integer thresholdPercent) {
		this.thresholdPercent = thresholdPercent;
	}
	public Integer getAlarmIdleCycles() {
		return alarmIdleCycles;
	}
	public void setAlarmIdleCycles(Integer alarmIdleCycles) {
		this.alarmIdleCycles = alarmIdleCycles;
	}
	public Date getLastServer() {
		return lastServer;
	}
	public void setLastServer(Date lastServer) {
		this.lastServer = lastServer;
	}
	public Date getLastClient() {
		return lastClient;
	}
	public void setLastClient(Date lastClient) {
		this.lastClient = lastClient;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSecondsFromLastServerRequest() {
		if ( secondsFromLastServerRequest  == null ) return Integer.MAX_VALUE ;//if it is null then treat it as  if it is not working 
		return secondsFromLastServerRequest;
	}
	public void setSecondsFromLastServerRequest(Integer secondsFromLastServerRequest) {
		this.secondsFromLastServerRequest = secondsFromLastServerRequest;
	}
	public Integer getSecondsFromLastClientRequest() {
		if ( secondsFromLastClientRequest  == null ) return Integer.MAX_VALUE ;//if it is null then treat it as  if it is not working 
		return secondsFromLastClientRequest;
	}
	public void setSecondsFromLastClientRequest(Integer secondsFromLastClientRequest) {
		this.secondsFromLastClientRequest = secondsFromLastClientRequest;
	}
		
	public boolean isServerDown() {
		if ( status == null ){
			return true;
		}
		return (status & STATUS_SERVER_DOWN) != 0 ;
	}
	
	public boolean isClientDown() {
		if ( status == null ){
			return true;
		}
		return (status & STATUS_CLIENT_DOWN) != 0 ;
	}

	public boolean isUnknown() {
		if ( status == null ){
			return true;
		}		
		return (status & STATUS_UNKNOWN) != 0 ;
	}
		
	public boolean isConnectionDown() {
		if ( status == null ){
			return true;
		}
		return isServerDown() || isClientDown() || (status & STATUS_CONNECTION_DOWN) != 0  ;
	}
}
