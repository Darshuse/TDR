package com.eastnets.message.summary.enumDesc;

public enum Field70Options {

	INV("Invoice"), IPI("International Payment Instruction"), RFB("Reference for Beneficiary"), ROC(
			"Reference of Customer"), TSU("Trade Services Utility transaction");

	String fieldValue;

	public String getFieldValue() {
		return fieldValue;
	}

	private Field70Options(String fieldValue) {
		this.fieldValue = fieldValue;
	}

}
