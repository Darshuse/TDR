package com.eastnets.domain.viewer;

public enum ParamWebSearviceStatus {
	CORECT_PARAM("CORRECT"), MANDATORY_UETR("transactionID"), TRN_REF("trnRef"), AMOUNT_FROM("amountFrom"), AMOUNT_TO(
			"amountTo"), AMOUNT_CCY("amountCcy"), VALUE_DATE_FROM("valueDateFrom"), ORDERING_CUSTOMER(
					"orderingCustomer"), BENEFICIARY_CUSTOMER("beneficiaryCustomer"), TRANSACTION_STATUS(
							"transactionStatus"), DIRECTION("direction"), DATE_FROM("dateFrom"), MESG_TYPE("msgType"), MESSAGE_PK("messagePK");

	private ParamWebSearviceStatus(String desc) {
		this.setDesc(desc);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String desc;

}
