package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;

// the bicCode bean
public abstract class BicCode implements Serializable, Licensable<BicCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3614897117978858948L;
	private String bicCode;
	private int bandVolume;

	private TrafficBand band;

	public BicCode(String bicCode, TrafficBand band, int bandVolume) {
		this.bicCode = bicCode.toUpperCase();
		this.band = band;
		this.bandVolume = bandVolume;
	}

	@Override
	public int compareTo(BicCode bicCode) {
		return this.getBicCode().compareTo(bicCode.getBicCode());
	}

	@Override
	public boolean equals(final Object bicCode) {
		return this.bicCode.equals(((BicCode) bicCode).bicCode);
	}

	@Override
	public int hashCode() {
		return this.bicCode.hashCode();
	}

	@Override
	public String toString() {
		return "Bic code: " + this.bicCode + " Volume: " + this.getBandVolume();
	}

	/**
	 * @return the bicCode
	 */
	public String getBicCode() {
		return bicCode;
	}

	public int getBandVolume() {
		return bandVolume;
	}

	public void setBandVolume(int bandVolume) {
		this.bandVolume = bandVolume;
	}

	@Override
	public boolean includeInLicense() {
		return !this.bicCode.endsWith("0");
	}

	/**
	 * @return the band
	 */
	public TrafficBand getBand() {
		return band;
	}

	@Override
	public String toLicenseDataString() {
		return this.bicCode + this.getBandVolume();
	}
}
