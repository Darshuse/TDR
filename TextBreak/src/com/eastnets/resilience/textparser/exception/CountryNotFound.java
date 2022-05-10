package com.eastnets.resilience.textparser.exception;

public class CountryNotFound  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -837281999884049460L;
	private String countryCode;
	
	
	public CountryNotFound(String code) {
		super();
		setCountryCode(code);
	}
	
	@Override
	public String getMessage() {
		return toString();
	}

	@Override
	public String toString() {
		return "CountryNotFound : Version ( " + getCountryCode() + " )";
	}

	// Getter & Setter 
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
