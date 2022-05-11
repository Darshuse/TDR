package com.eastnets.domain.viewer;

import java.math.BigDecimal;

public class GPIMesgFields {

	private Integer mesgCredebAccountNumber;
	private String mesgDebitAmount;
	private String mesgDebitCcy;
	private String mesgCreditAmount;
	private String mesgCreditCcy;
	private String mesgBeneficiaryName;
	private String mesgTransactionDate;
	private String mesgBeneficiaryAccount;
	private String mesgBeneficiaryBankName;
	private String mesgExchangeRate;
	private String mesgExchangeCurrFrom;
	private String mesgExchangeCurrTo;
	private String mesgContractId;
	private String mesgCharges;
	private String mesgAccountName;
	private String mesgDebitAccountInstructing;
	private String mesgBeneficiaryAddress;
	private String mesgBeneficiaryBankCode;
	private String mesgIntermediaryBankCode;
	private String mesgInstructions;
	private String mesgB2bInstructions;
	private String mesgSndChargesAmount = "";
	private String mesgSndChargesCurr = "";
	private BigDecimal mesgRcvChargesAmount;
	private String mesgRcvChargesCurr;
	private String mesgOriginatorName;
	private String mesgOriginatorAccountId;
	private String mesgOriginatorAddress;
	private String mesgOderingPartyName;
	private String mesgOderingPartyInstitution;
	private String mesgOderingPartyAddress;
	private String mesgRemitInfo;
	private String statusCode;
	private String reasonCode;
	private String forwardedTo;
	private String statusOriginator;
	private String nakCode;
	private String notifyTime;
	private BigDecimal mesgInstrAmount;
	private String mesgInstrCcy;
	private String mesgOrderCus;
	private String mesgSendingInst;
	private String mesgOrderingInst;
	private String mesgBeneficiaryInst;
	private String mesgSnd_Corr;
	private String mesgRcvrCorr;
	private String mesgReimbursInst;
	private String mesgIntermInst;
	private String mesgAccountInst;
	private String mesgBenCust;
	private String mesgNakCode;
	private String sbOrderingCus;
	private String sbOrderingInst;
	private String sbIntermediaryInst;
	private String sbAccountWithInst;
	private String sbBeneficiaryCustomer;
	private String sbRemittanceInfo;
	private BigDecimal sbInstructedAmount;
	private String sbInstructedCurr;
	private String settlementMethod;
	private String clearingSystem;
	private String detailsofCharges;
	private String senderToReceiver;
	private String senderToReceiverFieldCuasing;
	private String senderToReceiverReasonCode;
	private String senderToReceiverReff;
	private String allDeducts;

	public String getAllDeducts() {
		return allDeducts;
	}

	public void setAllDeducts(String allDeducts) {
		this.allDeducts = allDeducts;
	}

	public String getSbOrderingCus() {
		return sbOrderingCus;
	}

	public void setSbOrderingCus(String sbOrderingCus) {
		this.sbOrderingCus = sbOrderingCus;
	}

	public String getSbOrderingInst() {
		return sbOrderingInst;
	}

	public void setSbOrderingInst(String sbOrderingInst) {
		this.sbOrderingInst = sbOrderingInst;
	}

	public String getSbIntermediaryInst() {
		return sbIntermediaryInst;
	}

	public void setSbIntermediaryInst(String sbIntermediaryInst) {
		this.sbIntermediaryInst = sbIntermediaryInst;
	}

	public String getSbAccountWithInst() {
		return sbAccountWithInst;
	}

	public void setSbAccountWithInst(String sbAccountWithInst) {
		this.sbAccountWithInst = sbAccountWithInst;
	}

	public String getSbBeneficiaryCustomer() {
		return sbBeneficiaryCustomer;
	}

	public void setSbBeneficiaryCustomer(String sbBeneficiaryCustomer) {
		this.sbBeneficiaryCustomer = sbBeneficiaryCustomer;
	}

	public String getSbRemittanceInfo() {
		return sbRemittanceInfo;
	}

	public void setSbRemittanceInfo(String sbRemittanceInfo) {
		this.sbRemittanceInfo = sbRemittanceInfo;
	}

	public BigDecimal getSbInstructedAmount() {
		return sbInstructedAmount;
	}

	public void setSbInstructedAmount(BigDecimal sbInstructedAmount) {
		this.sbInstructedAmount = sbInstructedAmount;
	}

	public String getSbInstructedCurr() {
		return sbInstructedCurr;
	}

	public void setSbInstructedCurr(String sbInstructedCurr) {
		this.sbInstructedCurr = sbInstructedCurr;
	}

	private boolean updateFinMessageStatus = true;
	private MessageParsingResult messageParsingResult;

	public BigDecimal getMesgInstrAmount() {
		return mesgInstrAmount;
	}

	public void setMesgInstrAmount(BigDecimal mesgInstrAmount) {
		this.mesgInstrAmount = mesgInstrAmount;
	}

	public String getMesgInstrCcy() {
		return mesgInstrCcy;
	}

	public void setMesgInstrCcy(String mesgInstrCcy) {
		this.mesgInstrCcy = mesgInstrCcy;
	}

	public String getMesgOrderCus() {
		return mesgOrderCus;
	}

	public void setMesgOrderCus(String mesgOrderCus) {
		this.mesgOrderCus = mesgOrderCus;
	}

	public String getMesgSendingInst() {
		return mesgSendingInst;
	}

	public void setMesgSendingInst(String mesgSendingInst) {
		this.mesgSendingInst = mesgSendingInst;
	}

	public String getMesgOrderingInst() {
		return mesgOrderingInst;
	}

	public void setMesgOrderingInst(String mesgOrderingInst) {
		this.mesgOrderingInst = mesgOrderingInst;
	}

	public String getMesgBeneficiaryInst() {
		return mesgBeneficiaryInst;
	}

	public void setMesgBeneficiaryInst(String mesgBeneficiaryInst) {
		this.mesgBeneficiaryInst = mesgBeneficiaryInst;
	}

	public String getMesgSnd_Corr() {
		return mesgSnd_Corr;
	}

	public void setMesgSnd_Corr(String mesgSnd_Corr) {
		this.mesgSnd_Corr = mesgSnd_Corr;
	}

	public String getMesgRcvrCorr() {
		return mesgRcvrCorr;
	}

	public void setMesgRcvrCorr(String mesgRcvrCorr) {
		this.mesgRcvrCorr = mesgRcvrCorr;
	}

	public String getMesgReimbursInst() {
		return mesgReimbursInst;
	}

	public void setMesgReimbursInst(String mesgReimbursInst) {
		this.mesgReimbursInst = mesgReimbursInst;
	}

	public String getMesgIntermInst() {
		return mesgIntermInst;
	}

	public void setMesgIntermInst(String mesgIntermInst) {
		this.mesgIntermInst = mesgIntermInst;
	}

	public String getMesgAccountInst() {
		return mesgAccountInst;
	}

	public void setMesgAccountInst(String mesgAccountInst) {
		this.mesgAccountInst = mesgAccountInst;
	}

	public String getMesgBenCust() {
		return mesgBenCust;
	}

	public void setMesgBenCust(String mesgBenCust) {
		this.mesgBenCust = mesgBenCust;
	}

	public Integer getMesgCredebAccountNumber() {
		return mesgCredebAccountNumber;
	}

	public void setMesgCredebAccountNumber(Integer mesgCredebAccountNumber) {
		this.mesgCredebAccountNumber = mesgCredebAccountNumber;
	}

	public String getMesgDebitAmount() {
		return mesgDebitAmount;
	}

	public void setMesgDebitAmount(String mesgDebitAmount) {
		this.mesgDebitAmount = mesgDebitAmount;
	}

	public String getMesgDebitCcy() {
		return mesgDebitCcy;
	}

	public void setMesgDebitCcy(String mesgDebitCcy) {
		this.mesgDebitCcy = mesgDebitCcy;
	}

	public String getMesgCreditAmount() {
		return mesgCreditAmount;
	}

	public void setMesgCreditAmount(String mesgCreditAmount) {
		this.mesgCreditAmount = mesgCreditAmount;
	}

	public String getMesgCreditCcy() {
		return mesgCreditCcy;
	}

	public void setMesgCreditCcy(String mesgCreditCcy) {
		this.mesgCreditCcy = mesgCreditCcy;
	}

	public String getMesgBeneficiaryName() {
		return mesgBeneficiaryName;
	}

	public void setMesgBeneficiaryName(String mesgBeneficiaryName) {
		this.mesgBeneficiaryName = mesgBeneficiaryName;
	}

	public String getMesgTransactionDate() {
		return mesgTransactionDate;
	}

	public void setMesgTransactionDate(String mesgTransactionDate) {
		this.mesgTransactionDate = mesgTransactionDate;
	}

	public String getMesgBeneficiaryAccount() {
		return mesgBeneficiaryAccount;
	}

	public void setMesgBeneficiaryAccount(String mesgBeneficiaryAccount) {
		this.mesgBeneficiaryAccount = mesgBeneficiaryAccount;
	}

	public String getMesgBeneficiaryBankName() {
		return mesgBeneficiaryBankName;
	}

	public void setMesgBeneficiaryBankName(String mesgBeneficiaryBankName) {
		this.mesgBeneficiaryBankName = mesgBeneficiaryBankName;
	}

	public String getMesgExchangeRate() {
		return mesgExchangeRate;
	}

	public void setMesgExchangeRate(String mesgExchangeRate) {
		this.mesgExchangeRate = mesgExchangeRate;
	}

	public String getMesgExchangeCurrFrom() {
		return mesgExchangeCurrFrom;
	}

	public void setMesgExchangeCurrFrom(String mesgExchangeCurrFrom) {
		this.mesgExchangeCurrFrom = mesgExchangeCurrFrom;
	}

	public String getMesgExchangeCurrTo() {
		return mesgExchangeCurrTo;
	}

	public void setMesgExchangeCurrTo(String mesgExchangeCurrTo) {
		this.mesgExchangeCurrTo = mesgExchangeCurrTo;
	}

	public String getMesgContractId() {
		return mesgContractId;
	}

	public void setMesgContractId(String mesgContractId) {
		this.mesgContractId = mesgContractId;
	}

	public String getMesgCharges() {
		return mesgCharges;
	}

	public void setMesgCharges(String mesgCharges) {
		this.mesgCharges = mesgCharges;
	}

	public String getMesgAccountName() {
		return mesgAccountName;
	}

	public void setMesgAccountName(String mesgAccountName) {
		this.mesgAccountName = mesgAccountName;
	}

	public String getMesgDebitAccountInstructing() {
		return mesgDebitAccountInstructing;
	}

	public void setMesgDebitAccountInstructing(String mesgDebitAccountInstructing) {
		this.mesgDebitAccountInstructing = mesgDebitAccountInstructing;
	}

	public String getMesgBeneficiaryAddress() {
		return mesgBeneficiaryAddress;
	}

	public void setMesgBeneficiaryAddress(String mesgBeneficiaryAddress) {
		this.mesgBeneficiaryAddress = mesgBeneficiaryAddress;
	}

	public String getMesgBeneficiaryBankCode() {
		return mesgBeneficiaryBankCode;
	}

	public void setMesgBeneficiaryBankCode(String mesgBeneficiaryBankCode) {
		this.mesgBeneficiaryBankCode = mesgBeneficiaryBankCode;
	}

	public String getMesgIntermediaryBankCode() {
		return mesgIntermediaryBankCode;
	}

	public void setMesgIntermediaryBankCode(String mesgIntermediaryBankCode) {
		this.mesgIntermediaryBankCode = mesgIntermediaryBankCode;
	}

	public String getMesgInstructions() {
		return mesgInstructions;
	}

	public void setMesgInstructions(String mesgInstructions) {
		this.mesgInstructions = mesgInstructions;
	}

	public String getMesgB2bInstructions() {
		return mesgB2bInstructions;
	}

	public void setMesgB2bInstructions(String mesgB2bInstructions) {
		this.mesgB2bInstructions = mesgB2bInstructions;
	}

	public String getMesgSndChargesAmount() {
		return mesgSndChargesAmount;
	}

	public void setMesgSndChargesAmount(String mesgSndChargesAmount) {
		this.mesgSndChargesAmount = mesgSndChargesAmount;
	}

	public String getMesgSndChargesCurr() {
		return mesgSndChargesCurr;
	}

	public void setMesgSndChargesCurr(String mesgSndChargesCurr) {
		this.mesgSndChargesCurr = mesgSndChargesCurr;
	}

	public BigDecimal getMesgRcvChargesAmount() {
		return mesgRcvChargesAmount;
	}

	public void setMesgRcvChargesAmount(BigDecimal mesgRcvChargesAmount) {
		this.mesgRcvChargesAmount = mesgRcvChargesAmount;
	}

	public String getMesgRcvChargesCurr() {
		return mesgRcvChargesCurr;
	}

	public void setMesgRcvChargesCurr(String mesgRcvChargesCurr) {
		this.mesgRcvChargesCurr = mesgRcvChargesCurr;
	}

	public String getMesgOriginatorName() {
		return mesgOriginatorName;
	}

	public void setMesgOriginatorName(String mesgOriginatorName) {
		this.mesgOriginatorName = mesgOriginatorName;
	}

	public String getMesgOriginatorAccountId() {
		return mesgOriginatorAccountId;
	}

	public void setMesgOriginatorAccountId(String mesgOriginatorAccountId) {
		this.mesgOriginatorAccountId = mesgOriginatorAccountId;
	}

	public String getMesgOriginatorAddress() {
		return mesgOriginatorAddress;
	}

	public void setMesgOriginatorAddress(String mesgOriginatorAddress) {
		this.mesgOriginatorAddress = mesgOriginatorAddress;
	}

	public String getMesgOderingPartyName() {
		return mesgOderingPartyName;
	}

	public void setMesgOderingPartyName(String mesgOderingPartyName) {
		this.mesgOderingPartyName = mesgOderingPartyName;
	}

	public String getMesgOderingPartyInstitution() {
		return mesgOderingPartyInstitution;
	}

	public void setMesgOderingPartyInstitution(String mesgOderingPartyInstitution) {
		this.mesgOderingPartyInstitution = mesgOderingPartyInstitution;
	}

	public String getMesgOderingPartyAddress() {
		return mesgOderingPartyAddress;
	}

	public void setMesgOderingPartyAddress(String mesgOderingPartyAddress) {
		this.mesgOderingPartyAddress = mesgOderingPartyAddress;
	}

	public String getMesgRemitInfo() {
		return mesgRemitInfo;
	}

	public void setMesgRemitInfo(String mesgRemitInfo) {
		this.mesgRemitInfo = mesgRemitInfo;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getForwardedTo() {
		return forwardedTo;
	}

	public void setForwardedTo(String forwardedTo) {
		this.forwardedTo = forwardedTo;
	}

	public String getStatusOriginator() {
		return statusOriginator;
	}

	public void setStatusOriginator(String statusOriginator) {
		this.statusOriginator = statusOriginator;
	}

	public String getNakCode() {
		return nakCode;
	}

	public void setNakCode(String nakCode) {
		this.nakCode = nakCode;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getMesgNakCode() {
		return mesgNakCode;
	}

	public void setMesgNakCode(String mesgNakCode) {
		this.mesgNakCode = mesgNakCode;
	}

	public MessageParsingResult getMessageParsingResult() {
		return messageParsingResult;
	}

	public void setMessageParsingResult(MessageParsingResult messageParsingResult) {
		this.messageParsingResult = messageParsingResult;
	}

	public boolean isUpdateFinMessageStatus() {
		return updateFinMessageStatus;
	}

	public void setUpdateFinMessageStatus(boolean updateFinMessageStatus) {
		this.updateFinMessageStatus = updateFinMessageStatus;
	}

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}

	public String getClearingSystem() {
		return clearingSystem;
	}

	public void setClearingSystem(String clearingSystem) {
		this.clearingSystem = clearingSystem;
	}

	public String getDetailsofCharges() {
		return detailsofCharges;
	}

	public void setDetailsofCharges(String detailsofCharges) {
		this.detailsofCharges = detailsofCharges;
	}

	public String getSenderToReceiver() {
		return senderToReceiver;
	}

	public void setSenderToReceiver(String senderToReceiver) {
		this.senderToReceiver = senderToReceiver;
	}

	public String getSenderToReceiverReasonCode() {
		return senderToReceiverReasonCode;
	}

	public void setSenderToReceiverReasonCode(String senderToReceiverReasonCode) {
		this.senderToReceiverReasonCode = senderToReceiverReasonCode;
	}

	public String getSenderToReceiverReff() {
		return senderToReceiverReff;
	}

	public void setSenderToReceiverReff(String senderToReceiverReff) {
		this.senderToReceiverReff = senderToReceiverReff;
	}

	public String getSenderToReceiverFieldCuasing() {
		return senderToReceiverFieldCuasing;
	}

	public void setSenderToReceiverFieldCuasing(String senderToReceiverFieldCuasing) {
		this.senderToReceiverFieldCuasing = senderToReceiverFieldCuasing;
	}

}
