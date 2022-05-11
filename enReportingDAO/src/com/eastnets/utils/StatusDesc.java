package com.eastnets.utils;

public enum StatusDesc {

	ACCC("Completed"), ACSC("Completed"), ACSP("In progress"), RJCT("Rejected"), RETN("Returned"), PDCR("Pending"), RJCR("Rejected"), CNCL("Cancelled");

	public String statusDesc;

	public String getStatusDesc() {
		return statusDesc;
	}

	private StatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

}
