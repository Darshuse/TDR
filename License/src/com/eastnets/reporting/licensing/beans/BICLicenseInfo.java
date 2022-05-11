package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;

public class BICLicenseInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -584129643800328914L;
	private byte currentMonth;
	private int currentMonthCount;
	private short currentYear;
	private byte violationMonthsCount;

	public BICLicenseInfo() {
		this.currentMonthCount = 0;
		this.violationMonthsCount = 0;
		this.currentMonth = 0;
		this.currentYear = 0;
	}

	public BICLicenseInfo(final int currentMonthCount,
			final byte violationMonthsCount, final byte currentMonth,
			final short currentYear) {
		this.currentMonthCount = currentMonthCount;
		this.violationMonthsCount = violationMonthsCount;
		this.currentMonth = currentMonth;
		this.currentYear = currentYear;
	}

	/**
	 * @return the currentMonth
	 */
	public byte getCurrentMonth() {
		return currentMonth;
	}

	/**
	 * @param currentMonth the currentMonth to set
	 */
	public void setCurrentMonth(byte currentMonth) {
		this.currentMonth = currentMonth;
	}

	/**
	 * @return the currentMonthCount
	 */
	public int getCurrentMonthCount() {
		return currentMonthCount;
	}

	/**
	 * @param currentMonthCount the currentMonthCount to set
	 */
	public void setCurrentMonthCount(int currentMonthCount) {
		this.currentMonthCount = currentMonthCount;
	}

	/**
	 * @return the currentYear
	 */
	public short getCurrentYear() {
		return currentYear;
	}

	/**
	 * @param currentYear the currentYear to set
	 */
	public void setCurrentYear(short currentYear) {
		this.currentYear = currentYear;
	}

	/**
	 * @return the violationMonthsCount
	 */
	public byte getViolationMonthsCount() {
		return violationMonthsCount;
	}

	/**
	 * @param violationMonthsCount the violationMonthsCount to set
	 */
	public void setViolationMonthsCount(byte violationMonthsCount) {
		this.violationMonthsCount = violationMonthsCount;
	}
}
