package com.eastnets.message.summary.enumDesc;

public enum Role {
	
	ORIGINATOR("originator"), BENEFICIARY("beneficiary"), INTERMEDIARY("intermediary");

	private final String option;

	Role(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}
	
}
