package com.eastnets.message.summary.enumDesc;

public enum GeoRouteOption {
	Domestic("Domestic"), International("International"), SWIFT("SWIFT");

	private final String option;
	
	GeoRouteOption(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}



}
