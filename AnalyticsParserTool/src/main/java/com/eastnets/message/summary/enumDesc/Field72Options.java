package com.eastnets.message.summary.enumDesc;

public enum Field72Options {

	ACC("Account with institution"), INS("Instructing institution"), INT("Intermediary institution");

	String fieldValue;

	public String getFieldValue() {
		return fieldValue;
	}

	private Field72Options(String fieldValue) {
		this.fieldValue = fieldValue;
	}

}
