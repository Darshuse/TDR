package com.eastnets.domain.viewer;

import java.sql.Date;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DetailsHistory {
	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
	private String senderName;
	private String senderBic;
	private String receiverName;
	private String receiverBic;
	private String detailsReason;
	private transient Date mesgCreaDateTime;
	private Integer timeZoneOffset;
	private String mesgCreationDateTime;
	private String msgType;
	private String statusCode;
	private String statusDescription;
	private String instructedAmount;
	private String reasonCode;
	private String senderDeducts;
	private String senderDeductsCur;
	private String settlementMethod;
	private String clearingSystem;
	private String notifDateTime;
	private String mesgExchangeRate;
	@JsonIgnore
	private String sbInstructedCurr;
	private String mesgSubFormat;
	private String mesgTrnRef;

	private String mesgCopyServiceId;

	private String ordringInstution;
	private boolean comesFrom199Updated;
	private String mesgNakedCode;

	private String exChangeRateFromCcy;
	private String exChangeRateToCcy;
	private String ConfirmationAmount;
	private String ConfirmationCCY;
	private String fromCurrencyWithExchangeRate;
	private String toCurrencyWithExchangeRate;
	private String timeZone;
	@JsonIgnore
	private String allDeducts;
	private String mesgCharges;
	private String mesgHistory;

	private String orginlaSender;

	@JsonIgnore
	public String getAllDeducts() {
		return allDeducts;
	}

	public void setAllDeducts(String allDeducts) {
		this.allDeducts = allDeducts;
	}

	@JsonIgnore
	public Date getMesgCreaDateTime() {
		if (mesgCreaDateTime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(mesgCreaDateTime);
		if (timeZoneOffset != null) {
			cal.add(Calendar.HOUR_OF_DAY, timeZoneOffset);
		}
		return new java.sql.Date(cal.getTime().getTime());
	}

	@JsonIgnore
	public String getSbInstructedCurr() {
		return sbInstructedCurr;
	}

	public void setSbInstructedCurr(String sbInstructedCurr) {
		this.sbInstructedCurr = sbInstructedCurr;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getDetailsReason() {
		return detailsReason;
	}

	public void setDetailsReason(String detailsReason) {
		this.detailsReason = detailsReason;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMesgCreaDateTimeStr() {
		return mesgCreationDateTime;
	}

	public void setMesgCreaDateTimeStr(String mesgCreaDateTimeStr) {
		this.mesgCreationDateTime = mesgCreaDateTimeStr;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;

	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(String instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getSenderDeducts() {
		return senderDeducts;
	}

	public void setSenderDeducts(String senderDeducts) {
		this.senderDeducts = senderDeducts;
	}

	public String getSenderDeductsCur() {
		if (senderDeducts == null || senderDeducts.isEmpty())
			return "";
		return senderDeductsCur;
	}

	public void setSenderDeductsCur(String senderDeductsCur) {
		this.senderDeductsCur = senderDeductsCur;
	}

	public String getstatusDesc(String statusCode, String resonCode, String mesgType) {
		String statusDesc = "";

		if (statusCode == null && reasonCode != null) {

			if (reasonCode.equalsIgnoreCase("DUPL")) {
				statusDesc = "Duplicate Payment";
			} else if (reasonCode.equalsIgnoreCase("AGNT")) {
				statusDesc = "Incorrect Agent";
			} else if (reasonCode.equalsIgnoreCase("CURR")) {
				statusDesc = "Incorrect Currency";
			} else if (reasonCode.equalsIgnoreCase("CUST")) {
				statusDesc = "Requested By Customer";
			} else if (reasonCode.equalsIgnoreCase("UPAY")) {
				statusDesc = "Undue Payment";
			} else if (reasonCode.equalsIgnoreCase("CUTA")) {
				statusDesc = "Cancel Upon Unable To Apply";
			} else if (reasonCode.equalsIgnoreCase("TECH")) {
				statusDesc = "Technical Problem";
			} else if (reasonCode.equalsIgnoreCase("FRAD")) {
				statusDesc = "Fraudulent Origin";
			} else if (reasonCode.equalsIgnoreCase("COVR")) {
				statusDesc = "Cover Cancelled or Returned";
			} else if (reasonCode.equalsIgnoreCase("AMNT") || reasonCode.equalsIgnoreCase("AM09")) {
				statusDesc = "Incorrect Amount";
			}

			if (reasonCode.contains("INDM")) {

				statusDesc = statusDesc.concat(", with a willingness to provide an indemnity subject to a bilateral agreement");
			}

		} else if (statusCode != null && statusCode.contains("ACSP")) {

			if (resonCode.contains("000")) {
				statusDesc = (mesgType.equalsIgnoreCase("199") ? "Payment transferred to next gpi agent or gpi-compatible market infrastructure (MI)"
						: "Account booking done and gCOV transaction transferred to next gpi reimbursement agent or gCOV-compatible market infrastructure (MI)");

			} else if (resonCode.contains("001")) {
				statusDesc = "Non traceable bank";

			} else if (resonCode.contains("002")) {
				statusDesc = (mesgType.equalsIgnoreCase("199") ? "Credit to creditors account may not confirmed same day" : "Debit/Credit to nostro account may not be confirmed or gCOV transaction may not be transferred same day");

			} else if (resonCode.contains("003")) {
				statusDesc = (mesgType.equalsIgnoreCase("199") ? "Credit to creditors account pending receipt of required document" : "Reimbursement agent has requested a previous gpi agent to provide additional information/correct information");
			} else if (resonCode.contains("004")) {
				statusDesc = "Credit to creditors account pending ,status originator waiting for funds provided";
			}

		} else if (statusCode != null && (statusCode.contains("ACSC") || statusCode.contains("ACCC"))) {
			statusDesc = "Bank received the payment and credited it to the beneficiary bank";

		} else if (statusCode != null && statusCode.contains("RJCT")) {
			if (resonCode != null) {
				if (resonCode.contains("AC01")) {
					statusDesc = "Account number is invalid or missing";
				} else if (resonCode.contains("AC04")) {
					statusDesc = "Account number specified has been closed on the bank of account's books.";
				} else if (resonCode.contains("AC06")) {
					statusDesc = "Account specified is blocked,prohibiting posting of transactions against it.";
				} else if (resonCode.contains("BE01")) {
					statusDesc = "Identification of end customer is not consistent with associated account number.(formerly CreditorConsistency).";
				} else if (resonCode.contains("NOAS")) {
					statusDesc = "Failed to contact beneficiary.";
				} else if (resonCode.contains("RR03")) {
					statusDesc = "Specification of the creditor’s name and/or address needed for regulatory requirements is insufficient or missing.";
				} else if (resonCode.contains("FF07")) {
					statusDesc = "Purpose is missing or invalid.";
				} else if (resonCode.contains("RC01")) {
					statusDesc = "Bank identifier code specified in the message has an incorrect format (formerly IncorrectFormatForRoutingCode).";
				} else if (resonCode.contains("G004")) {
					statusDesc = "Missing cover.";
				} else if (resonCode.contains("RC08")) {
					statusDesc = "Routing code not valid for local clearing.";
				} else if (resonCode.contains("FOCR")) {
					statusDesc = "Return following a cancellation request.";
				} else if (resonCode.contains("DUPL")) {
					statusDesc = "Payment is a duplicate of another payment.";
				} else if (resonCode.contains("RR05")) {
					statusDesc = "Regulatory or central bank reporting information missing, incomplete or invalid.";
				} else if (resonCode.contains("AM06")) {
					statusDesc = "Below limit.";
				} else if (resonCode.contains("CUST")) {
					statusDesc = "At request of creditor.";
				} else if (resonCode.contains("MS03")) {
					statusDesc = "Reason has not been specified by agent.";
				}
			} else {
				statusDesc = "Payment rejected";
			}

		} else if (statusCode != null && statusCode.contains("PDCR")) {
			if (resonCode.contains("000")) {
				statusDesc = "valid gSRP request received by Tracker";
			} else if (resonCode.contains("001")) {
				statusDesc = "gCCT UETR registered in network cancellation list";
			} else if (resonCode.contains("002")) {
				statusDesc = "gSRP network stop occurred on related UETR";
			} else if (resonCode.contains("003")) {
				statusDesc = "gSRP Tracker forwarded request to processing/last gpi agent";
			} else if (resonCode.contains("004")) {
				statusDesc = "Tracker received network delivery acknowledgement (UACK) of gSRP request forwarded to processing/last gpi agent, response pending";
			} else if (resonCode.contains("PTNA")) {
				statusDesc = "Past To Next Agent";
			} else if (resonCode.contains("RQDA")) {
				statusDesc = "Requested Debit Authority";
			} else if (resonCode.contains("INDM")) {
				statusDesc = "Cancellation Indemnity Required";
			}

		} else if (statusCode != null && statusCode.contains("CNCL")) {
			statusDesc = "Cancelled payment";
		} else if (statusCode != null && statusCode.contains("RJCR")) {

			if (resonCode.contains("LEGL")) {
				statusDesc = "Cancellation cannot be accepted for regulatory reasons";
			} else if (resonCode.contains("AGNT")) {
				statusDesc = "Cancellation cannot be accepted because an agent refuses to cancel";
			} else if (resonCode.contains("CUST")) {
				statusDesc = "Cancellation cannot be accepted because of a customer's decision";
			} else if (resonCode.contains("NOAS")) {
				statusDesc = "No response from beneficiary";
			} else if (resonCode.contains("NOOR")) {
				statusDesc = "Original transaction never received";
			} else if (resonCode.contains("AC04")) {
				statusDesc = "Account number specified has been closed on the receiver's books";
			} else if (resonCode.contains("AM04")) {
				statusDesc = "Amount of funds available to cover specified message amount is insufficient";
			} else if (resonCode.contains("INDM")) {
				statusDesc = "Cancellation Indemnity Required";
			} else if (resonCode.contains("ARDT")) {
				statusDesc = "Cancellation not accepted, the transaction has been returned";
			} else if (resonCode.contains("FRNA")) {
				statusDesc = "Failed to Forward gSRP Request";
			}
		} else if (statusCode != null && statusCode.contains("RETN")) {
			statusDesc = "The Payment was returned";
		}

		return statusDesc;

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

	public String getNotifDateTime() {
		return notifDateTime;
	}

	public void setNotifDateTime(String notifDateTime) {
		this.notifDateTime = notifDateTime;
	}

	public String getMesgSubFormat() {
		return mesgSubFormat;
	}

	public void setMesgSubFormat(String mesgSubFormat) {
		this.mesgSubFormat = mesgSubFormat;
	}

	public String getMesgTrnRef() {
		return mesgTrnRef;
	}

	public void setMesgTrnRef(String mesgTrnRef) {
		this.mesgTrnRef = mesgTrnRef;
	}

	public String getMesgCopyServiceId() {
		return mesgCopyServiceId;
	}

	public void setMesgCopyServiceId(String mesgCopyServiceId) {
		this.mesgCopyServiceId = mesgCopyServiceId;
	}

	public String getOrdringInstution() {
		return ordringInstution;
	}

	public void setOrdringInstution(String ordringInstution) {
		this.ordringInstution = ordringInstution;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverBic() {
		return receiverBic;
	}

	public void setReceiverBic(String receiverBic) {
		this.receiverBic = receiverBic;
	}

	public boolean isComesFrom199Updated() {
		return comesFrom199Updated;
	}

	public void setComesFrom199Updated(boolean comesFrom199Updated) {
		this.comesFrom199Updated = comesFrom199Updated;
	}

	public String getMesgExchangeRate() {
		return mesgExchangeRate;
	}

	public void setMesgExchangeRate(String mesgExchangeRate) {
		this.mesgExchangeRate = mesgExchangeRate;
	}

	public String getMesgNakedCode() {
		return mesgNakedCode;
	}

	public void setMesgNakedCode(String mesgNakedCode) {
		this.mesgNakedCode = mesgNakedCode;
	}

	public String getExChangeRateFromCcy() {
		return exChangeRateFromCcy;
	}

	public void setExChangeRateFromCcy(String exChangeRateFromCcy) {
		this.exChangeRateFromCcy = exChangeRateFromCcy;
	}

	public String getExChangeRateToCcy() {
		return exChangeRateToCcy;
	}

	public void setExChangeRateToCcy(String exChangeRateToCcy) {
		this.exChangeRateToCcy = exChangeRateToCcy;
	}

	public String getConfirmationAmount() {
		return ConfirmationAmount;
	}

	public void setConfirmationAmount(String confirmationAmount) {
		ConfirmationAmount = confirmationAmount;
	}

	public String getConfirmationCCY() {
		return ConfirmationCCY;
	}

	public void setConfirmationCCY(String confirmationCCY) {
		ConfirmationCCY = confirmationCCY;
	}

	public String getFromCurrencyWithExchangeRate() {
		return fromCurrencyWithExchangeRate;
	}

	public void setFromCurrencyWithExchangeRate(String fromCurrencyWithExchangeRate) {
		this.fromCurrencyWithExchangeRate = fromCurrencyWithExchangeRate;
	}

	public String getToCurrencyWithExchangeRate() {
		return toCurrencyWithExchangeRate;
	}

	public void setToCurrencyWithExchangeRate(String toCurrencyWithExchangeRate) {
		this.toCurrencyWithExchangeRate = toCurrencyWithExchangeRate;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getMesgHistory() {
		return mesgHistory;
	}

	public void setMesgHistory(String mesgHistory) {
		this.mesgHistory = mesgHistory;
	}

	public String getMesgCharges() {
		return mesgCharges;
	}

	public void setMesgCharges(String mesgCharges) {
		this.mesgCharges = mesgCharges;
	}

	public String getOrginlaSender() {
		return orginlaSender;
	}

	public void setOrginlaSender(String orginlaSender) {
		this.orginlaSender = orginlaSender;
	}

}
