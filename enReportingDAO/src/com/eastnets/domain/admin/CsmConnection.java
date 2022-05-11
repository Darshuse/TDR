package com.eastnets.domain.admin;


import java.util.Date;


public class CsmConnection {

	private Integer severPort;
	private String serverName;
	private String clientName;
	private Integer heartBeatPeriodSecond = 60;
	private Integer thresholdPercent = 10;
	private Integer alarmIdleCycles = 1;
	private Date lastServer;
	private Date lastClient;
	private Integer status;
	
	

	public CsmConnection createCopy() {
		CsmConnection copy = null;
		try{ 
			copy = (CsmConnection) this.clone();
		}catch( Exception ex ){
		}
		if ( copy == null ){
			copy= new CsmConnection();
			copy.severPort= severPort;
			copy.serverName= serverName;
			copy.clientName= clientName;
			copy.heartBeatPeriodSecond =heartBeatPeriodSecond ;
			copy.thresholdPercent= thresholdPercent;
			copy.alarmIdleCycles = alarmIdleCycles;
			copy.lastServer= lastServer;
			copy.lastClient= lastClient;
			copy.status = status;
		}		
		return copy;
	}
		
		
	public Integer getSeverPort() {
		return severPort;
	}
	public void setSeverPort(Integer severPort) {
		this.severPort = severPort;
	}
	
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
		
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
		
	public Integer getHeartBeatPeriodSecond() {
		return heartBeatPeriodSecond;
	}
	public void setHeartBeatPeriodSecond(Integer heartBeatPeriodSecond) {
		this.heartBeatPeriodSecond = heartBeatPeriodSecond;
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
	
}
