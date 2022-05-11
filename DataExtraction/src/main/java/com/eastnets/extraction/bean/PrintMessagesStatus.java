package com.eastnets.extraction.bean;

public enum PrintMessagesStatus {

	EMPTY_MESSAGE("EMPTY_MESSAGE"), CONTAINS_FILE_MESSAGE("CONTAINS_FILE_MESSAGE");

	private String status;

	private PrintMessagesStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
