package com.eastnets.extraction.bean;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

public class InstanceDetails {

	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private Integer instNum;
	private String instStatus;
	private String instType;
	private String instCreaApplServName;
	private Date instCreaDateTime;
	private String instCreaMpfnName;
	private String instMpfnName;
	private String instCreaRpName;
	private String instRpName;

	private Integer instRelatedNbr;
	private String instUnitName;
	private String instResponderDn;
	private String instNrIndicator;

	private Integer timeZoneOffset;

	public Integer getInstNum() {
		return instNum;
	}

	public void setInstNum(Integer instNum) {
		this.instNum = instNum;
	}

	public String getInstStatus() {
		return instStatus;
	}

	public void setInstStatusFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getInstStatusFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase(instStatus));
	}

	public void setInstStatus(String instStatus) {
		this.instStatus = instStatus;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstTypeFormatted(String val) {
		throw new NotImplementedException("this should not be called as it should be readonly");
	}

	public String getInstTypeFormatted() {
		return StringUtils.capitalize(StringUtils.lowerCase(StringUtils.substring(instType, 10)));
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}

	public String getInstCreaApplServName() {
		return instCreaApplServName;
	}

	public void setInstCreaApplServName(String instCreaApplServName) {
		this.instCreaApplServName = instCreaApplServName;
	}

	public Date getInstCreaDateTimeOnDB() {
		return instCreaDateTime;
	}

	public Date getInstCreaDateTime() {
		if (instCreaDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(instCreaDateTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	public void setInstCreaDateTime(Date instCreaDateTime) {
		this.instCreaDateTime = instCreaDateTime;
	}

	public String getInstCreaMpfnName() {
		return instCreaMpfnName;
	}

	public void setInstCreaMpfnName(String instCreaMpfnName) {
		this.instCreaMpfnName = instCreaMpfnName;
	}

	public String getInstCreaRpName() {
		return instCreaRpName;
	}

	public void setInstCreaRpName(String instCreaRpName) {
		this.instCreaRpName = instCreaRpName;
	}

	public String getInstRpName() {
		return instRpName;
	}

	public void setInstRpName(String instRpName) {
		this.instRpName = instRpName;
	}

	public Integer getInstRelatedNbr() {
		return instRelatedNbr;
	}

	public void setInstRelatedNbr(Integer instRelatedNbr) {
		this.instRelatedNbr = instRelatedNbr;
	}

	public String getInstUnitName() {
		return instUnitName;
	}

	public void setInstUnitName(String instUnitName) {
		this.instUnitName = instUnitName;
	}

	public String getInstResponderDn() {
		return instResponderDn;
	}

	public void setInstResponderDn(String instResponderDn) {
		this.instResponderDn = instResponderDn;
	}

	public String getInstNrIndicator() {
		return instNrIndicator;
	}

	public void setInstNrIndicator(String instNrIndicator) {
		this.instNrIndicator = instNrIndicator;
	}

	public void setInstMpfnName(String instMpfnName) {
		this.instMpfnName = instMpfnName;
	}

	public String getInstMpfnName() {
		return instMpfnName;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getRelatedInstance() {
		if (instRelatedNbr == null)
			return "";
		if (StringUtils.equalsIgnoreCase(StringUtils.trim(instType), "INST_TYPE_NOTIFICATION")) {
			return instRelatedNbr.toString();
		}
		return "";
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

}
