package com.eastnets.service.gpi;

public enum GSRPReasonCode {
	CNCL("Payment Cancelled"),
	LEGL("Cancellation cannot be accepted for regulatory reasons"),
	AGNT("Cancellation cannot be accepted because an agent refuses to cancel"),
	CUST("Cancellation cannot be accepted because of a customer's decision"),
	ARDT("Cancellation not accepted, the transaction has already been returned"),
	NOAS("No response from beneficiary"),
	NOOR("Original transaction never received"),
	AC04("Account number specified has been closed on the receiver's books"),
	AM04("Amount of funds available to cover specified message is insufficient"),
	INDM("Cancellation Indemnity Required"),
	PTNA("Past To Next Agent"),
	RQDA("Requested Debit Authority"),
	DUPL("Duplicate Payment"),
	AGNTREQ("Incorrect Agent"),
	CURR("Incorrect Currency"),
	CUSTREQ("Requested By Customer"),
	UPAY("Undue Payment"),
	CUTA("Cancel Upon Unable To Apply"),
	TECH("Technical Problem"),
	FRAD("Fraudulent Origin"),
	COVR("Cover Cancelled or Returned"),
	AMNT("Incorrect Amount"),
	AM09("Incorrect Amount"),
	PDCRS000("Valid gSRP request received by Tracker"),
	PDCRS001("gCCT UETR registered in network cancellation list"),
	PDCRS002("gSRP network stop occurred on related UETR"),
	PDCRS003("gSRP Tracker forwarded request to processing/last gpi agent"),
	PDCRS004("Tracker received network delivery acknowledgement (UACK), Response pending"),
	RJCRFRNA("Failed to Forward gSRP Request");
	
	private String desc;
	GSRPReasonCode(String desc){
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}
