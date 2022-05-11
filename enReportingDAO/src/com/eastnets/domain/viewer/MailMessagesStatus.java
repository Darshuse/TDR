package com.eastnets.domain.viewer;

public enum MailMessagesStatus {
	EMPTY_MESSAGE("EMPTY_MESSAGE"),
	CONTAINS_FILE_MESSAGE("CONTAINS_FILE_MESSAGE"),
	EMPTY_FIELD_MESSAGE("EMPTY_FIELD_MESSAGE"),
	INVALID_ADDRESSBOOK_EMAIL("INVALID_ADDRESSBOOK_EMAIL"),
	INVALID_EMAIL("INVALID_EMAIL"),
	EMPTY_SUBJECT("EMPTY_SUBJECT"),
	EMPTY_EMAIL("EMPTY_EMAIL"),
	EMPTY_SUBJECT_AND_MAIL("EMPTY_SUBJECT_AND_MAIL");
	private   String status;
	
	private MailMessagesStatus(String status) {
		 this.status = status;
	}

	public String getStatus() {
		return status;
	} 
 
}
