package com.eastnets.service.util;

import com.eastnets.domain.viewer.TableColumnsHeader;

public enum MessageViewerHeaderEnum {


	alliance_instance("Alliance"),
	mesgSubFormat("IO"),
	correspondent("Correspondent"),
	mesgReference("Reference"),
	mesgFrmtName("Format"),
	mesgIdentifier("Identifier"),
	mesgStatus("Status"),
	xFinAmountFormatted("Amount"),
	xFinCcy("Ccy"),
	xFinValueDateFormatted("Value Date"),//xFinValueDateFormatted
	mesgSenderX1("Sender"),
	instReceiverX1("Receiver"),
	mesgCreaDateTimeStr("Creation Date"),
	instRpName("Org.Inst.RP"),
	emiNetworkDeliveryStatusFormatted("Nt.Status"),
	emiIAppNameFormatted("Emi.Info"),
	recIAppNameFormatted("Rec.Info"),
	slaId("SLA ID"),
	uetr("UETR"),
	mesgRelatedReference("Related Reference"),
	mesgUserReferenceText("MUR"),
	mXKeyword1("MX Keyword 1"),
	mXKeyword2("MX Keyword 2"),
	mXKeyword3("MX Keyword 3"),
	serviceName("Service Name"),
	orderingCustomer("Ordering customer"),
	orderingInstitution("Ordering institution"),
	beneficiaryCustomer("Beneficiary customer "),
	accountWithInstitution("Account with institution "),
	detailsOfcharges("Details of charges"),
	deducts("Deducts"),
	deductsFrom("Deducts from"),
	deductsTo("Deducts to"),
	exchangeRate("Exchange rate"),
	statusCode("Status code"), 
	statusDesc("Status Description"), 
	reasonCodes("Reason codes"),
	statusOriginatorBIC("Status originator BIC"),
	forwardedToAgent("Forwarded-to-Agent"),
	deductsFormatted("Deducts"),
	transactionStatus("Transaction status"),
    NAKCode("NAK code"),
	gpiCur("Cur"),
	date_time_suffix("Suffix"),
	senderCorr("Sender's Correspondent"),
	receiverCorr("Receiver's Correspondent"),
	reimbursementInst("Third Reimbursement Inst"),
	sattlmentMethod("Sattlment Method"),
	clearingSystem("Clearing System"),
	notifDateTime("Credit Date Time"),
	note("note");




	String header;

	private MessageViewerHeaderEnum(String header){
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}



}
