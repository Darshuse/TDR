package com.eastnets.extraction.bean;

public class Currency {

	private String code;
	private String name;
	private int numberOfDigits;
	private String countryCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfDigits() {
		return numberOfDigits;
	}

	public void setNumberOfDigits(int numberOfDigits) {
		this.numberOfDigits = numberOfDigits;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
