/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.resilience.textparser.db.bean;

/**
 * 
 * @author EHakawati
 * 
 */
public class Correspondent {

	private String institutionName;
	private String branchInfo;
	private String cityName;
	private String countryCode;
	private String countryName;
	private String location;
	private int returnStatus;

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getBranchInfo() {
		return branchInfo;
	}

	public void setBranchInfo(String branchInfo) {
		this.branchInfo = branchInfo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		if (getInstitutionName() != null && this.getInstitutionName().length() > 0) {
			output.append("\r\n" + getInstitutionName());
		}

		if (getBranchInfo() != null && getBranchInfo().length() > 0) {
			output.append("\r\n" + getBranchInfo());
		}

		if (getLocation() != null && getLocation().length() > 0) {
			output.append("\r\n" + getLocation());
		}

		if (getCityName() != null && getCityName().length() > 0) {
			output.append("\r\n" + getCityName());
		}

		if (getCountryName() != null && getCountryName().length() > 0) {
			output.append("\r\n" + getCountryName());
		}

		if (getCountryCode() != null && getCountryCode().length() > 0) {
			output.append(" " + getCountryCode());
		}
		return output.toString();
	}

}
