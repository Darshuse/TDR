package com.eastnets.extraction.bean;

import java.util.Date;

public class CorrInfo {

	private String corrType;
	private String corrX1;
	private String corrX2;
	private String corrX3;
	private String corrX4;
	private Date mesgDate;
	private String corrInstitutionName;
	private String corrBranchInfo;
	private String corrCityName;
	private String corrCtryCode;
	private String corrCtryName;
	private String corrLocation;
	private Integer retstatus;
	public static String CORR_TYPE_INSTITUTION = "CORR_TYPE_INSTITUTION";
	public static String CORR_TYPE_DEPARTMENT = "CORR_TYPE_DEPARTMENT";
	public static String CORR_TYPE_APPLICATION = "CORR_TYPE_APPLICATION";
	public static String CORR_TYPE_INDIVIDUAL = "CORR_TYPE_INDIVIDUAL";

	public String getCorrType() {
		return corrType;
	}

	public void setCorrType(String corrType) {
		this.corrType = corrType;
	}

	public String getCorrX1() {
		return corrX1;
	}

	public void setCorrX1(String corrX1) {
		this.corrX1 = corrX1;
	}

	public String getCorrX2() {
		return corrX2;
	}

	public void setCorrX2(String corrX2) {
		this.corrX2 = corrX2;
	}

	public String getCorrX3() {
		return corrX3;
	}

	public void setCorrX3(String corrX3) {
		this.corrX3 = corrX3;
	}

	public String getCorrX4() {
		return corrX4;
	}

	public void setCorrX4(String corrX4) {
		this.corrX4 = corrX4;
	}

	public Date getMesgDate() {
		return mesgDate;
	}

	public void setMesgDate(Date mesgDate) {
		this.mesgDate = mesgDate;
	}

	public String getCorrInstitutionName() {
		return corrInstitutionName;
	}

	public void setCorrInstitutionName(String corrInstitutionName) {
		this.corrInstitutionName = corrInstitutionName;
	}

	public String getCorrBranchInfo() {
		return corrBranchInfo;
	}

	public void setCorrBranchInfo(String corrBranchInfo) {
		this.corrBranchInfo = corrBranchInfo;
	}

	public String getCorrCityName() {
		return corrCityName;
	}

	public void setCorrCityName(String corrCityName) {
		this.corrCityName = corrCityName;
	}

	public String getCorrCtryCode() {
		return corrCtryCode;
	}

	public void setCorrCtryCode(String corrCtryCode) {
		this.corrCtryCode = corrCtryCode;
	}

	public String getCorrCtryName() {
		return corrCtryName;
	}

	public void setCorrCtryName(String corrCtryName) {
		this.corrCtryName = corrCtryName;
	}

	public String getCorrLocation() {
		return corrLocation;
	}

	public void setCorrLocation(String corrLocation) {
		this.corrLocation = corrLocation;
	}

	public Integer getRetstatus() {
		return retstatus;
	}

	public void setRetstatus(Integer retstatus) {
		this.retstatus = retstatus;
	}

}
