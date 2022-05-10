package com.eastnets.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;

public class WatchDogSettings {

	@EmbeddedId
	private WatchDogSettingsKey watchDogSettingsKey;

	@Column(name = "OSNDELAY")
	private Integer osnDelay;

	@Column(name = "ISNDELAY")
	private Integer isnDelay;

	@Column(name = "DUPDELAY")
	private Integer dupDelay;

	@Column(name = "CHECKUSERPDE")
	private Integer checkUserPDE;

	@Column(name = "CHECKPDEPDR")
	private Integer checkPdePDR;

	@Column(name = "USERREQUEST")
	private Integer userRequest;

	@Column(name = "OSN")
	private Integer osn;

	@Column(name = "ISN")
	private Integer isn;

	@Column(name = "NACK")
	private Integer nack;

	@Column(name = "POSSIBLEDUP")
	private Integer possibleDup;

	@Column(name = "CALCULATEDDUP")
	private Integer calculatedDup;

	@Column(name = "LASTPURGE")
	private Integer lastPurge;

	@Column(name = "LASTISNCHECK")
	private Integer lastISNCheck;

	@Column(name = "LASTOSNCHECK")
	private Integer lastOSNCheck;

	@Column(name = "LASTISNDATE")
	private Integer lastISNDate;

	@Column(name = "LASTOSNDATE")
	private Integer lastOSNDate;

	public Integer getOsnDelay() {
		return osnDelay;
	}

	public void setOsnDelay(Integer osnDelay) {
		this.osnDelay = osnDelay;
	}

	public Integer getIsnDelay() {
		return isnDelay;
	}

	public void setIsnDelay(Integer isnDelay) {
		this.isnDelay = isnDelay;
	}

	public Integer getDupDelay() {
		return dupDelay;
	}

	public void setDupDelay(Integer dupDelay) {
		this.dupDelay = dupDelay;
	}

	public Integer getCheckUserPDE() {
		return checkUserPDE;
	}

	public void setCheckUserPDE(Integer checkUserPDE) {
		this.checkUserPDE = checkUserPDE;
	}

	public Integer getCheckPdePDR() {
		return checkPdePDR;
	}

	public void setCheckPdePDR(Integer checkPdePDR) {
		this.checkPdePDR = checkPdePDR;
	}

	public Integer getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(Integer userRequest) {
		this.userRequest = userRequest;
	}

	public Integer getOsn() {
		return osn;
	}

	public void setOsn(Integer osn) {
		this.osn = osn;
	}

	public Integer getIsn() {
		return isn;
	}

	public void setIsn(Integer isn) {
		this.isn = isn;
	}

	public Integer getNack() {
		return nack;
	}

	public void setNack(Integer nack) {
		this.nack = nack;
	}

	public Integer getPossibleDup() {
		return possibleDup;
	}

	public void setPossibleDup(Integer possibleDup) {
		this.possibleDup = possibleDup;
	}

	public Integer getCalculatedDup() {
		return calculatedDup;
	}

	public void setCalculatedDup(Integer calculatedDup) {
		this.calculatedDup = calculatedDup;
	}

	public Integer getLastPurge() {
		return lastPurge;
	}

	public void setLastPurge(Integer lastPurge) {
		this.lastPurge = lastPurge;
	}

	public Integer getLastISNCheck() {
		return lastISNCheck;
	}

	public void setLastISNCheck(Integer lastISNCheck) {
		this.lastISNCheck = lastISNCheck;
	}

	public Integer getLastOSNCheck() {
		return lastOSNCheck;
	}

	public void setLastOSNCheck(Integer lastOSNCheck) {
		this.lastOSNCheck = lastOSNCheck;
	}

	public Integer getLastISNDate() {
		return lastISNDate;
	}

	public void setLastISNDate(Integer lastISNDate) {
		this.lastISNDate = lastISNDate;
	}

	public Integer getLastOSNDate() {
		return lastOSNDate;
	}

	public void setLastOSNDate(Integer lastOSNDate) {
		this.lastOSNDate = lastOSNDate;
	}

}
