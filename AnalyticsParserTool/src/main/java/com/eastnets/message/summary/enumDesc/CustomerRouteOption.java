package com.eastnets.message.summary.enumDesc;

public enum CustomerRouteOption {

	
	INTRA("Intra-customer-group"), INTER("Inter-customer-group"), SWIFT("SWIFT");

	private final String option;

	CustomerRouteOption(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}
	
}
