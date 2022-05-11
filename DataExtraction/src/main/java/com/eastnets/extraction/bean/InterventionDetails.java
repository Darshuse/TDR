package com.eastnets.extraction.bean;

import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;

public class InterventionDetails {

	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private Long intvSeqNbr;
	private Date intvDateTime;
	private Clob intvText;
	private String intvOperNickname;
	private String intvIntyCategory;
	private String intvIntyName;
	private Integer timeZoneOffset;
	private Integer intvInstNum;

	public Long getIntvSeqNbr() {
		return intvSeqNbr;
	}

	public void setIntvSeqNbr(Long intvSeqNbr) {
		this.intvSeqNbr = intvSeqNbr;
	}

	public Date getIntvDateTimeOnDB() {
		return intvDateTime;
	}

	public Date getIntvDateTime() {
		if (intvDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(intvDateTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setIntvDateTime(Date intvDateTime) {
		this.intvDateTime = intvDateTime;
	}

	public Clob getIntvText() {
		return intvText;
	}

	public void setIntvText(Clob intvText) {
		this.intvText = intvText;
	}

	public String getIntvOperNickname() {
		return intvOperNickname;
	}

	public void setIntvOperNickname(String intvOperNickname) {
		this.intvOperNickname = intvOperNickname;
	}

	public String getIntvIntyCategory() {
		return intvIntyCategory;
	}

	public void setIntvIntyCategory(String intvIntyCategory) {
		this.intvIntyCategory = intvIntyCategory;
	}

	public String getIntvIntyName() {
		return intvIntyName;
	}

	public void setIntvIntyName(String intvIntyName) {
		this.intvIntyName = intvIntyName;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

	public Integer getIntvInstNum() {
		return intvInstNum;
	}

	public void setIntvInstNum(Integer intvInstNum) {
		this.intvInstNum = intvInstNum;
	}

}
