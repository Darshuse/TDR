package com.eastnets.service.gpi;

public enum StatusDescEnum {
	COMPLETEACCC("Bank received the payment and credited it to the beneficiary bank"),
	REJECTPAYMENTACST("Payment rejected"),
	WAITINGFORCREDIT("Waiting for crediting it"),
	NONTRACEABLEBENFICIARYBANK001("Non traceable beneficiary bank"),
	NOTCONFIRMEDINTHESAMEDAY002("Credit to creditors account may not confirmed same day"),
	REQUIRESDOCUMENT003("Credit to creditors account pending receipt of required document"),
	FUNDSPROVIDED004("Credit to creditors account pending ,status originator waiting for funds provided");
	
	
	private String desc;
	private StatusDescEnum(String desc) {
		
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
