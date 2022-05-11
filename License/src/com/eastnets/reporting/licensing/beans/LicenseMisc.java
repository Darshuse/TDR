/**
 * 
 */
package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;

/**
 * 
 * @author EastNets
 * @since dDec 10, 2012
 * 
 */
public abstract class LicenseMisc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7415706219805146173L;
	private String licenseID;
	private Integer maxUsers;
	private Integer maxConnections;
	private String customerName;
	private String contactName;
	private String concerns;

	private String licenseValidationEncrypted;

	/*
	 * public LicenseMisc(int maxConnections, int maxUsers, String licenseID, String licenseValidationEncrypted) { this.licenseID = licenseID; this.maxConnections = maxConnections; this.maxUsers =
	 * maxUsers; this.licenseValidationEncrypted = licenseValidationEncrypted; }
	 */

	public LicenseMisc(int maxConnections, int maxUsers, String licenseID, String licenseValidationEncrypted) {
		this.licenseID = licenseID;
		this.maxConnections = maxConnections;
		this.maxUsers = maxUsers;
		this.licenseValidationEncrypted = licenseValidationEncrypted;
	}

	/**
	 * @return the licenseID
	 * @throws Exception
	 */
	public void setLicenseID(String val) throws Exception {
		throw new Exception("this should not be called as it should be readonly");
	}

	public String getLicenseID() {
		return licenseID;
	}

	/**
	 * @return the maxUsers
	 */
	public Integer getMaxUsers() {
		return maxUsers;
	}

	/**
	 * @return the maxConnections
	 */
	public Integer getMaxConnections() {
		return maxConnections;
	}

	public String getLicenseValidationEncrypted() {
		return licenseValidationEncrypted;
	}

	public void setLicenseValidationEncrypted(String licenseValidationEncrypted) {
		this.licenseValidationEncrypted = licenseValidationEncrypted;
	}

}
