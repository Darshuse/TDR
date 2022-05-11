package com.eastnets.domain.viewer;

public enum EndPointStatus {
	    ReceivedAndSend(),
	    NotReceiveAndSend(),
	    IntermediateReceiveWithOutSend(),
	    IntermediateRejectPayment(),
	    NonTracabelBank(),
	    ReceivedPaymentNotCreditedIt(),
	    NonTracabelBeneficiaryBank(),
	    ReceivedAndSendCoverPayment,
	    SECONED_AGENT(),
	    COVE_AGENT(),
		CANCELLED(),
		WHITE_STATUS();
	    
}
