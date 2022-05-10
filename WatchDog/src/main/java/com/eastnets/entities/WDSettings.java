package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WDSETTINGS")
public class WDSettings {

	@EmbeddedId
	WDSettingsPK pk;

	@Column(name = "OSNDELAY")
	private int osnDelay;

	@Column(name = "ISNDELAY")
	private int isnDelay;

	@Column(name = "CHECKUSERPDE")
	private int checkUserPDE;

	@Column(name = "CHECKPDEPDR")
	private int checkPDEPDR;

	@Column(name = "USERREQUEST")
	private int userReqest;

	@Column(name = "ISN")
	private int isn;

	@Column(name = "NACK")
	private int nack;

	@Column(name = "POSSIBLEDUP")
	private int possibleDup;

	@Column(name = "CALCULATEDDUP")
	private int calculatedDup;

	@Column(name = "LASTPURGE")
	private Date lastPurge;

	@Column(name = "LASTISNCHECK")
	private Date lastISNCheck;

	@Column(name = "LASTOSNCHECK")
	private Date lastOSNCheck;

	@Column(name = "LASTISNDATE")
	private Date lastISNDate;

	@Column(name = "LASTOSNDATE")
	private Date lastOSNDate;

	@Column(name = "EVENTREQUEST")
	private int eventRequest;

	public WDSettingsPK getPk() {
		return pk;
	}

	public void setPk(WDSettingsPK pk) {
		this.pk = pk;
	}

	public int getOsnDelay() {
		return osnDelay;
	}

	public void setOsnDelay(int osnDelay) {
		this.osnDelay = osnDelay;
	}

	public int getIsnDelay() {
		return isnDelay;
	}

	public void setIsnDelay(int isnDelay) {
		this.isnDelay = isnDelay;
	}

	public int getCheckUserPDE() {
		return checkUserPDE;
	}

	public void setCheckUserPDE(int checkUserPDE) {
		this.checkUserPDE = checkUserPDE;
	}

	public int getCheckPDEPDR() {
		return checkPDEPDR;
	}

	public void setCheckPDEPDR(int checkPDEPDR) {
		this.checkPDEPDR = checkPDEPDR;
	}

	public int getUserReqest() {
		return userReqest;
	}

	public void setUserReqest(int userReqest) {
		this.userReqest = userReqest;
	}

	public int getIsn() {
		return isn;
	}

	public void setIsn(int isn) {
		this.isn = isn;
	}

	public int getNack() {
		return nack;
	}

	public void setNack(int nack) {
		this.nack = nack;
	}

	public int getPossibleDup() {
		return possibleDup;
	}

	public void setPossibleDup(int possibleDup) {
		this.possibleDup = possibleDup;
	}

	public int getCalculatedDup() {
		return calculatedDup;
	}

	public void setCalculatedDup(int calculatedDup) {
		this.calculatedDup = calculatedDup;
	}

	public Date getLastPurge() {
		return lastPurge;
	}

	public void setLastPurge(Date lastPurge) {
		this.lastPurge = lastPurge;
	}

	public Date getLastISNCheck() {
		return lastISNCheck;
	}

	public void setLastISNCheck(Date lastISNCheck) {
		this.lastISNCheck = lastISNCheck;
	}

	public Date getLastOSNCheck() {
		return lastOSNCheck;
	}

	public void setLastOSNCheck(Date lastOSNCheck) {
		this.lastOSNCheck = lastOSNCheck;
	}

	public Date getLastISNDate() {
		return lastISNDate;
	}

	public void setLastISNDate(Date lastISNDate) {
		this.lastISNDate = lastISNDate;
	}

	public Date getLastOSNDate() {
		return lastOSNDate;
	}

	public void setLastOSNDate(Date lastOSNDate) {
		this.lastOSNDate = lastOSNDate;
	}

	public int getEventRequest() {
		return eventRequest;
	}

	public void setEventRequest(int eventRequest) {
		this.eventRequest = eventRequest;
	}

	@Override
	public String toString() {
		return "WDSettings [pk=" + pk + ", isnDelay=" + isnDelay + ", checkUserPDE=" + checkUserPDE + ", checkPDEPDR="
				+ checkPDEPDR + ", userReqest=" + userReqest + ", isn=" + isn + ", nack=" + nack + ", possibleDup="
				+ possibleDup + ", calculatedDup=" + calculatedDup + ", lastPurge=" + lastPurge + ", lastISNCheck="
				+ lastISNCheck + ", lastOSNCheck=" + lastOSNCheck + ", lastISNDate=" + lastISNDate + ", lastOSNDate="
				+ lastOSNDate + ", eventRequest=" + eventRequest + "]";
	}

}