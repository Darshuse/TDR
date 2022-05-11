package com.eastnets.reporting.licensing.beans;

// determine if involved in the license key generation
public interface Licensable<T> extends Comparable<T> {
	public boolean includeInLicense();
	
	public String toLicenseDataString();
}
