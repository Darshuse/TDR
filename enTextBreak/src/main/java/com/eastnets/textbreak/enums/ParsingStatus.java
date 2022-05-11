package com.eastnets.textbreak.enums;

public enum ParsingStatus {

	UNDER_PARSING("0"),FAIELD("1"),EXTRACTED("2");

	private String status;

 

	private ParsingStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}




}
