package com.eastnets.dbconsistencycheck.app;

import java.util.Calendar;
import java.util.Date;

import com.eastnets.config.ConfigBean;

public class DBConsistencyCheckConfig extends ConfigBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//logging 
	private boolean verbose;

	//Application Settings 
	private int			mode = 2;
	private	String		saaServer;
	private	int 		saaPort;
	private	String		reportFile ;
	private String 		reportPath;

	private int			dayNum;
	private int			frequency;
	private int			aid = 0;
	
	//private String		activeTime;
	private int			sleepPeriod;
	private Calendar	sleepTime = Calendar.getInstance();
	private Calendar	reportTime = Calendar.getInstance() ;
	private boolean		forceUpdate = false;
	private int			reportFrequency;
	
	// Getter & setter
	
	public int getSleepPeriod() {
		return sleepPeriod;
	}
	public void setSleepPeriod(int sleepPeriod) {
		this.sleepPeriod = sleepPeriod;
	}
	
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	
	public int getReportFrequency() {
		return reportFrequency;
	}
	public void setReportFrequency(int reportFrequency) {
		this.reportFrequency = reportFrequency;
	}
	
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	public String getSAAServer() {
		return saaServer;
	}
	public void setSAAServer(String saaServer) {
		this.saaServer = saaServer;
	}
	public int getSAAPort() {
		return saaPort;
	}
	public void setSAAPort(int saaPort) {
		this.saaPort = saaPort;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public int getDayNum() {
		return dayNum;
	}
	public void setDayNum(int dayNum) {
		this.dayNum = dayNum;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}	
	public String getSaaServer() {
		return saaServer;
	}
	public void setSaaServer(String saaServer) {
		this.saaServer = saaServer;
	}
	public int getSaaPort() {
		return saaPort;
	}
	public void setSaaPort(int saaPort) {
		this.saaPort = saaPort;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	/*public String getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}*/

	public Calendar getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(Date sleepTime) {
		this.sleepTime.setTime(sleepTime);
	}

	public Calendar getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime.setTime(reportTime);
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public boolean isForceUpdate() {
		return forceUpdate;
	}
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	
	
}
