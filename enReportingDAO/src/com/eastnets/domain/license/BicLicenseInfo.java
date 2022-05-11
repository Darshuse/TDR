package com.eastnets.domain.license;

import java.io.Serializable;

import com.eastnets.reporting.licensing.beans.BICLicenseInfo;
import com.eastnets.reporting.licensing.beans.BicCode;

/**
 * @author EastNets
 * @since January 29, 2013
 */
public class BicLicenseInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4757572932627056323L;
	BicCode bicCode ;
	BICLicenseInfo bicLicenseInfo;
	String currentAverage;
	boolean endsWith0 = false;
	
	
	public boolean isEndsWith0() {
		return !bicCode.includeInLicense();
	}
		
	public String getCurrentAverage() {
		return currentAverage;
	}
	public void setCurrentAverage(String currentAverage) {
		this.currentAverage = currentAverage;
	}
	
	public BicCode getBicCode() {
		return bicCode;
	}
	public void setBicCode(BicCode bicCode) {
		this.bicCode = bicCode;
	}
	
	public BICLicenseInfo getBicLicenseInfo() {
		return bicLicenseInfo;
	}
	public void setBicLicenseInfo(BICLicenseInfo bicLicenseInfo) {
		this.bicLicenseInfo = bicLicenseInfo;
	}
	
}
