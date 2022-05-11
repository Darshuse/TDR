package com.eastnets.message.summary.Bean;

import java.sql.Date;

public class CalculatedMeasuresBean {

	private long aid;
	private long umidh;
	private long umidl;
	private String initialOrderingBIC;
	private String initialOrderingCountery;
	private String initialOrderingRegion;
	private String ultimateBeneficiaryBIC;
	private String ultimateBeneficiaryCountry;
	private String ultimateBeneficiaryRegion;
	private Integer coverPaymentFlag;
	private String currencyPair;
	private Integer gpiFlag;
	private String initialOrderingOption;
	private String instructedCurrency;
	private String instructionCode;
	private String intermediaryInstitution;
	private String intermediaryInstitutionOption;
	private String receiverCorrespondentBIC8;
	private String receiverCorrespondentOption;
	private String remittanceInformation;
	private String senderToReceiverInformation;
	private String corridor;
	private String senderCorrBIC;

	private String myRole;
	private String counterPartRole;
	private Date mesgCreateDateTime;

	public String getMyRole() {
		return myRole;
	}

	public void setMyRole(String myRole) {
		this.myRole = myRole;
	}

	public String getCounterPartRole() {
		return counterPartRole;
	}

	public void setCounterPartRole(String counterPartRole) {
		this.counterPartRole = counterPartRole;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getUmidh() {
		return umidh;
	}

	public void setUmidh(long umidh) {
		this.umidh = umidh;
	}

	public long getUmidl() {
		return umidl;
	}

	public void setUmidl(long umidl) {
		this.umidl = umidl;
	}

	public String getInitialOrderingBIC() {
		return initialOrderingBIC;
	}

	public void setInitialOrderingBIC(String initialOrderingBIC) {
		this.initialOrderingBIC = initialOrderingBIC;
	}

	public String getInitialOrderingCountery() {
		return initialOrderingCountery;
	}

	public void setInitialOrderingCountery(String initialOrderingCountery) {
		this.initialOrderingCountery = initialOrderingCountery;
	}

	public String getInitialOrderingRegion() {
		return initialOrderingRegion;
	}

	public void setInitialOrderingRegion(String initialOrderingRegion) {
		this.initialOrderingRegion = initialOrderingRegion;
	}

	public String getUltimateBeneficiaryBIC() {
		return ultimateBeneficiaryBIC;
	}

	public void setUltimateBeneficiaryBIC(String ultimateBeneficiaryBIC) {
		this.ultimateBeneficiaryBIC = ultimateBeneficiaryBIC;
	}

	public String getUltimateBeneficiaryCountry() {
		return ultimateBeneficiaryCountry;
	}

	public void setUltimateBeneficiaryCountry(String ultimateBeneficiaryCountry) {
		this.ultimateBeneficiaryCountry = ultimateBeneficiaryCountry;
	}

	public String getUltimateBeneficiaryRegion() {
		return ultimateBeneficiaryRegion;
	}

	public void setUltimateBeneficiaryRegion(String ultimateBeneficiaryRegion) {
		this.ultimateBeneficiaryRegion = ultimateBeneficiaryRegion;
	}

	public Integer getCoverPaymentFlag() {
		return coverPaymentFlag;
	}

	public void setCoverPaymentFlag(Integer coverPaymentFlag) {
		this.coverPaymentFlag = coverPaymentFlag;
	}

	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}

	public Integer getGpiFlag() {
		return gpiFlag;// KASSAB
	}

	public void setGpiFlag(Integer gpiFlag) {
		this.gpiFlag = gpiFlag;
	}

	public String getInitialOrderingOption() {
		return initialOrderingOption;
	}

	public void setInitialOrderingOption(String initialOrderingOption) {
		this.initialOrderingOption = initialOrderingOption;
	}

	public String getInstructedCurrency() {
		return instructedCurrency;
	}

	public void setInstructedCurrency(String instructedCurrency) {
		this.instructedCurrency = instructedCurrency;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public String getIntermediaryInstitution() {
		return intermediaryInstitution;
	}

	public void setIntermediaryInstitution(String intermediaryInstitution) {
		this.intermediaryInstitution = intermediaryInstitution;
	}

	public String getIntermediaryInstitutionOption() {
		return intermediaryInstitutionOption;
	}

	public void setIntermediaryInstitutionOption(String intermediaryInstitutionOption) {
		this.intermediaryInstitutionOption = intermediaryInstitutionOption;
	}

	public String getReceiverCorrespondentBIC8() {
		return receiverCorrespondentBIC8;
	}

	public void setReceiverCorrespondentBIC8(String receiverCorrespondentBIC8) {
		this.receiverCorrespondentBIC8 = receiverCorrespondentBIC8;
	}

	public String getReceiverCorrespondentOption() {
		return receiverCorrespondentOption;
	}

	public void setReceiverCorrespondentOption(String receiverCorrespondentOption) {
		this.receiverCorrespondentOption = receiverCorrespondentOption;
	}

	public String getRemittanceInformation() {
		return remittanceInformation;
	}

	public void setRemittanceInformation(String remittanceInformation) {
		this.remittanceInformation = remittanceInformation;
	}

	public String getSenderToReceiverInformation() {
		return senderToReceiverInformation;
	}

	public void setSenderToReceiverInformation(String senderToReceiverInformation) {
		this.senderToReceiverInformation = senderToReceiverInformation;
	}

	public Date getMesgCreateDateTime() {
		return mesgCreateDateTime;
	}

	public void setMesgCreateDateTime(Date mesgCreateDateTime) {
		this.mesgCreateDateTime = mesgCreateDateTime;
	}

	public String getCorridor() {
		return corridor;
	}

	public void setCorridor(String corridor) {
		this.corridor = corridor;
	}

	public String getSenderCorrBIC() {
		return senderCorrBIC;
	}

	public void setSenderCorrBIC(String senderCorrBIC) {
		this.senderCorrBIC = senderCorrBIC;
	}

}