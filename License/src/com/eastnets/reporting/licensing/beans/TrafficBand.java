/**
 * 
 */
package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;

/**
 * 
 * @author EastNets
 * @since dNov 27, 2012
 * 
 */
public enum TrafficBand implements Serializable {
	Band__1("-1", 249), Band_0("0", 499), Band_1("1", 999), Band_2("2", 1999), Band_3("3", 4999), Band_4("4", 19999), Band_5("5", 49999), Band_6("6", 99999), Band_7("7", 249999), Band_8("8", 499999), Band_9("x", 0);

	private int bandVolume;
	private String band;

	private TrafficBand(String band, int bandVolume) {
		this.bandVolume = bandVolume;
		this.band = band;
	}

	public static TrafficBand getVolumBand(int volume) {
		for (TrafficBand band : TrafficBand.values()) {
			if (band.getBandVolume() == volume) {
				return band;
			}
		}
		if (volume > 0) {
			TrafficBand custom = TrafficBand.Band_9;
			custom.setBandVolume(volume);
			return custom;
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.band + ": < " + (this.bandVolume + 1);
	}

	/**
	 * @return the bandVolume
	 */
	public int getBandVolume() {
		return bandVolume;
	}

	/**
	 * @param bandVolume
	 *            the bandVolume to set
	 */
	public void setBandVolume(int bandVolume) {
		this.bandVolume = bandVolume;
	}

	/**
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * @param band
	 *            the band to set
	 */
	public void setBand(String band) {
		this.band = band;
	}
}
