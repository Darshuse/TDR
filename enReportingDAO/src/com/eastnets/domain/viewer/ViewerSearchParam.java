/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.domain.viewer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.xml.XMLConditionMetadata;

/**
 * ViewerSearchParam POJO, This POJO filled from UI and sent to service .
 * 
 * @author EastNets
 * @since September 20, 2012
 */
public class ViewerSearchParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3772725716172905304L;
	// Source & Creation
	private String sourceSearchFile;
	private List<String> sourceSelectedSAA = new ArrayList<String>();// first
																		// String
																		// is
																		// the
																		// alliance
																		// name,
																		// the
																		// second
																		// is
																		// the
																		// aid

	private List<Object> queryVariablesBinding = new ArrayList<Object>();
	private AdvancedDate creationDate = new AdvancedDate();
	private Date databaseInfoMinCreationDate;
	private Date databaseInfoMaxCreationDate;

	// ID & Content

	private String umidFormat;
	private String umidIO;
	private String umidCorrespondent;
	private String umidType;
	private String umidQual;
	private String umidReference;
	private String contentSender;
	private String contentSenderInstitution;
	private String contentSenderDepartment;
	private String contentSenderFirstName;
	private String contentSenderLastName;
	private String contentReceiver;
	private String contentReceiverInstitution;
	private String contentReceiverDepartment;
	private String contentReceiverFirstName;
	private String contentReceiverLastName;
	private String contentNature;
	private String contentTransactionReference;

	// Add by mohmmad kassab
	private List<String> sourceSelectedCountry = new ArrayList<String>();
	private List<String> sourceSelectedCurancy = new ArrayList<String>();
	private List<String> sourceSelectedMT = new ArrayList<String>();
	private List<String> statusList = new ArrayList<String>();

	private String contentRelatedRefference;
	private String contentAmountFrom;// could be null if not specified in the
										// GUI
	private String contentAmountTo;// could be null if not specified in the GUI
	private String contentAmountCurrency;
	private Date contentValueDateFrom;// could be null if not specified in the
										// GUI
	private Date contentValueDateTo;// could be null if not specified in the GUI
	private String contentSearchText;
	private String serviceName;
	private String serviceNameExt;
	private String identifier;

	// Transmission
	private String interventionsNetworkName;
	private String interventionsFromToNetwork;
	private String interventionsSessionHolder;
	private String interventionsSessionNumber;
	private String interventionsSequenceNumberFrom;
	private String interventionsSequenceNumberTo;
	private String emiNetworkDeliveryStatus;

	// Instance location & units
	private String instanceStatus;
	private String historyQueue;

	/*
	 * Added By Mohammad Alzarai to be aligned with Alliance Message Management
	 */
	private String bankingPriority;
	private String finCopy;
	private String finInform;
	private String mur;
	private String uetr;
	private String slaId;

	private String requestorDN;
	private String responserDN;
	private String messageProperty;
	private boolean copy;
	private boolean includeSysMsg;
	private Identifier messageName;

	private String mxKeyword1;
	private String mxKeyword2;
	private String mxKeyword3;
	private List<XMLConditionMetadata> xmlConditionsMetadata;
	private int searchInValue = 2;
	private String logicalName;
	private String transferDescription;

	// GPI
	private String accountWithInstitution;
	private String orderingInstitution;
	private String orderingCustomer;
	private String beneficiaryCustomer;
	private String detailOfCharge;
	private String deductsFrom;
	private String deductsTo;
	private String statusCode;
	private String serviceType;
	private String transactionStatus;
	private String reasonCodes;
	private String statusOriginatorBic;
	private String forwordBic;
	private String insAmountFrom;
	private String insAmountTo;
	private String gpiCur;
	private String senderCorr;
	private String receiverCorr;
	private String reimbursementInst;
	private String gSRPReasonCode;
	private boolean stopRecal;
	private boolean atMyBank;
	private boolean enabelGpiSeacrh;
	private String sattlmentMethod;
	private String clearingSystemList;

	// For WebSearvice
	private ParamWebSearviceStatus paramWebSearviceStatus;
	private List<String> mandatoryParams;
	private List<String> queuesSelected = new ArrayList<String>();

	// primary key is (aid,umidl,umidh) it's need to be String because the request send String value
	private String primaryKey;

	private List<String> unitsSelected = new ArrayList<String>();

	public ViewerSearchParam() {
		searchInValue = 2;
		xmlConditionsMetadata = new ArrayList<XMLConditionMetadata>();
	}

	public ViewerSearchParam createCopy() {
		return (ViewerSearchParam) SerializationUtils.clone(this);
	}

	public void setSourceSelectedSAA(List<String> sourceSelectedSAA) {
		this.sourceSelectedSAA = sourceSelectedSAA;
	}

	public List<String> getSourceSelectedSAA() {
		return sourceSelectedSAA;
	}

	public void setCreationDate(AdvancedDate creationDate) {
		this.creationDate = creationDate;
	}

	public AdvancedDate getCreationDate() {
		return creationDate;
	}

	public void setDatabaseInfoMinCreationDate(Date databaseInfoMinCreationDate) {
		this.databaseInfoMinCreationDate = databaseInfoMinCreationDate;
	}

	public Date getDatabaseInfoMinCreationDate() {
		return databaseInfoMinCreationDate;
	}

	public void setDatabaseInfoMaxCreationDate(Date databaseInfoMaxCreationDate) {
		this.databaseInfoMaxCreationDate = databaseInfoMaxCreationDate;
	}

	public Date getDatabaseInfoMaxCreationDate() {
		return databaseInfoMaxCreationDate;
	}

	public void setUmidIO(String umidIO) {
		this.umidIO = umidIO;
	}

	public String getUmidIO() {
		return umidIO;
	}

	public void setUmidCorrespondent(String umidCorrespondent) {
		if (umidCorrespondent != null)
			this.umidCorrespondent = umidCorrespondent.toUpperCase();
	}

	public String getUmidCorrespondent() {
		return umidCorrespondent;
	}

	public void setUmidType(String umidType) {
		this.umidType = umidType;
	}

	public String getUmidType() {
		return umidType;
	}

	public void setUmidQual(String umidQual) {
		this.umidQual = umidQual;
	}

	public String getUmidQual() {
		return umidQual;
	}

	public void setUmidReference(String umidReference) {
		this.umidReference = umidReference;
	}

	public String getUmidReference() {
		return umidReference;
	}

	public void setContentSender(String contentSender) {
		this.contentSender = contentSender;
	}

	public String getContentSender() {
		return contentSender;
	}

	public void setContentSenderInstitution(String contentSenderInstitution) {
		this.contentSenderInstitution = contentSenderInstitution;
	}

	public String getContentSenderInstitution() {
		return contentSenderInstitution;
	}

	public void setContentSenderDepartment(String contentSenderDepartment) {
		this.contentSenderDepartment = contentSenderDepartment;
	}

	public String getContentSenderDepartment() {
		return contentSenderDepartment;
	}

	public void setContentSenderFirstName(String contentSenderFirstName) {
		this.contentSenderFirstName = contentSenderFirstName;
	}

	public String getContentSenderFirstName() {
		return contentSenderFirstName;
	}

	public void setContentSenderLastName(String contentSenderLastName) {
		this.contentSenderLastName = contentSenderLastName;
	}

	public String getContentSenderLastName() {
		return contentSenderLastName;
	}

	public void setContentReceiver(String contentReceiver) {
		this.contentReceiver = contentReceiver;
	}

	public String getContentReceiver() {
		return contentReceiver;
	}

	public void setContentReceiverInstitution(String contentReceiverInstitution) {
		this.contentReceiverInstitution = contentReceiverInstitution;
	}

	public String getContentReceiverInstitution() {
		return contentReceiverInstitution;
	}

	public void setContentReceiverDepartment(String contentReceiverDepartment) {
		this.contentReceiverDepartment = contentReceiverDepartment;
	}

	public String getContentReceiverDepartment() {
		return contentReceiverDepartment;
	}

	public void setContentReceiverFirstName(String contentReceiverFirstName) {
		this.contentReceiverFirstName = contentReceiverFirstName;
	}

	public String getContentReceiverFirstName() {
		return contentReceiverFirstName;
	}

	public void setContentReceiverLastName(String contentReceiverLastName) {
		this.contentReceiverLastName = contentReceiverLastName;
	}

	public String getContentReceiverLastName() {
		return contentReceiverLastName;
	}

	public void setContentTransactionReference(String contentTransactionReference) {
		this.contentTransactionReference = contentTransactionReference;
	}

	public String getContentTransactionReference() {
		return contentTransactionReference;
	}

	public void setContentRelatedRefference(String contentRelatedRefference) {
		this.contentRelatedRefference = contentRelatedRefference;
	}

	public String getContentRelatedRefference() {
		return contentRelatedRefference;
	}

	public String getContentAmountFrom() {
		return contentAmountFrom;
	}

	public void setContentAmountFrom(String contentAmountFrom) {
		this.contentAmountFrom = contentAmountFrom;
	}

	public String getContentAmountTo() {
		return contentAmountTo;
	}

	public void setContentAmountTo(String contentAmountTo) {
		this.contentAmountTo = contentAmountTo;
	}

	public void setInterventionsSessionHolder(String interventionsSessionHolder) {
		this.interventionsSessionHolder = interventionsSessionHolder;
	}

	public void setContentAmountCurrency(String contentAmountCurrency) {
		this.contentAmountCurrency = contentAmountCurrency;
	}

	public String getContentAmountCurrency() {
		return contentAmountCurrency;
	}

	public void setContentValueDateFrom(Date contentValueDateFrom) {
		this.contentValueDateFrom = contentValueDateFrom;
	}

	public Date getContentValueDateFrom() {
		return contentValueDateFrom;
	}

	public void setContentValueDateTo(Date contentValueDateTo) {
		this.contentValueDateTo = contentValueDateTo;
	}

	public Date getContentValueDateTo() {
		return contentValueDateTo;
	}

	public void setContentSearchText(String contentSearchText) {
		this.contentSearchText = contentSearchText;
	}

	public String getContentSearchText() {
		return contentSearchText;
	}

	public void setInterventionsNetworkName(String interventionsNetworkName) {
		this.interventionsNetworkName = interventionsNetworkName;
	}

	public String getInterventionsNetworkName() {
		return interventionsNetworkName;
	}

	public void setInterventionsFromToNetwork(String interventionsFromToNetwork) {
		this.interventionsFromToNetwork = interventionsFromToNetwork;
	}

	public String getInterventionsFromToNetwork() {
		return interventionsFromToNetwork;
	}

	public String getInterventionsSessionHolder() {
		return interventionsSessionHolder;
	}

	public String getInterventionsSessionNumber() {
		return interventionsSessionNumber;
	}

	public void setInterventionsSessionNumber(String interventionsSessionNumber) {
		this.interventionsSessionNumber = interventionsSessionNumber;
	}

	public String getInterventionsSequenceNumberFrom() {
		return interventionsSequenceNumberFrom;
	}

	public void setInterventionsSequenceNumberFrom(String interventionsSequenceNumberFrom) {
		this.interventionsSequenceNumberFrom = interventionsSequenceNumberFrom;
	}

	public String getInterventionsSequenceNumberTo() {
		return interventionsSequenceNumberTo;
	}

	public void setInterventionsSequenceNumberTo(String interventionsSequenceNumberTo) {
		this.interventionsSequenceNumberTo = interventionsSequenceNumberTo;
	}

	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}

	public String getInstanceStatus() {
		return instanceStatus;
	}

	public void setQueuesSelected(List<String> queuesSelected) {
		this.queuesSelected = queuesSelected;
	}

	public List<String> getQueuesSelected() {
		return queuesSelected;
	}

	public void setUnitsSelected(List<String> unitsSelected) {
		this.unitsSelected = unitsSelected;
	}

	public List<String> getUnitsSelected() {
		return unitsSelected;
	}

	public void setSourceSearchFile(String sourceSearchFile) {
		this.sourceSearchFile = sourceSearchFile;
	}

	public String getSourceSearchFile() {
		return sourceSearchFile;
	}

	public void setUmidFormat(String umidFormat) {
		this.umidFormat = umidFormat;
	}

	public String getUmidFormat() {
		return umidFormat;
	}

	public void setContentNature(String contentNature) {
		this.contentNature = contentNature;
	}

	public String getContentNature() {
		return contentNature;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceNameExt() {
		return serviceNameExt;
	}

	public void setServiceNameExt(String serviceNameExt) {
		this.serviceNameExt = serviceNameExt;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEmiNetworkDeliveryStatus() {
		return emiNetworkDeliveryStatus;
	}

	public void setEmiNetworkDeliveryStatus(String emiNetworkDeliveryStatus) {
		this.emiNetworkDeliveryStatus = emiNetworkDeliveryStatus;
	}

	public String getHistoryQueue() {
		return historyQueue;
	}

	public void setHistoryQueue(String history) {
		this.historyQueue = history;
	}

	public String getBankingPriority() {
		return bankingPriority;
	}

	public void setBankingPriority(String bankingPriority) {
		this.bankingPriority = bankingPriority;
	}

	public String getFinCopy() {
		return finCopy;
	}

	public void setFinCopy(String finCopy) {
		this.finCopy = finCopy;
	}

	public String getFinInform() {
		return finInform;
	}

	public void setFinInform(String finInform) {
		this.finInform = finInform;
	}

	public String getMur() {
		return mur;
	}

	public void setMur(String mur) {
		this.mur = mur;
	}

	public String getUetr() {
		return uetr;
	}

	public void setUetr(String uetr) {
		this.uetr = uetr;
	}

	public String getSlaId() {
		return slaId;
	}

	public void setSlaId(String slaId) {
		this.slaId = slaId;
	}

	public String getRequestorDN() {
		return requestorDN;
	}

	public void setRequestorDN(String requestorDN) {
		this.requestorDN = requestorDN;
	}

	public String getResponserDN() {
		return responserDN;
	}

	public void setResponserDN(String responserDN) {
		this.responserDN = responserDN;
	}

	public String getMessageProperty() {
		return messageProperty;
	}

	public void setMessageProperty(String messageProperty) {
		this.messageProperty = messageProperty;
	}

	public boolean isCopy() {
		return copy;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public Identifier getMessageName() {
		return messageName;
	}

	public void setMessageName(Identifier messageName) {
		this.messageName = messageName;
	}

	public String getMxKeyword1() {
		return mxKeyword1;
	}

	public void setMxKeyword1(String mxKeyword1) {
		this.mxKeyword1 = mxKeyword1;
	}

	public String getMxKeyword2() {
		return mxKeyword2;
	}

	public void setMxKeyword2(String mxKeyword2) {
		this.mxKeyword2 = mxKeyword2;
	}

	public String getMxKeyword3() {
		return mxKeyword3;
	}

	public void setMxKeyword3(String mxKeyword3) {
		this.mxKeyword3 = mxKeyword3;
	}

	public List<XMLConditionMetadata> getXmlConditionsMetadata() {
		return xmlConditionsMetadata;
	}

	public void setXmlConditionsMetadata(List<XMLConditionMetadata> xmlConditionsMetadata) {
		this.xmlConditionsMetadata = xmlConditionsMetadata;
	}

	public int getSearchInValue() {
		return searchInValue;
	}

	public void setSearchInValue(int searchInValue) {
		this.searchInValue = searchInValue;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public String getTransferDescription() {
		return transferDescription;
	}

	public void setTransferDescription(String transferDescription) {
		this.transferDescription = transferDescription;
	}

	public List<String> getSourceSelectedCountry() {
		return sourceSelectedCountry;
	}

	public void setSourceSelectedCountry(List<String> sourceSelectedCountry) {
		this.sourceSelectedCountry = sourceSelectedCountry;
	}

	public List<String> getSourceSelectedCurancy() {
		return sourceSelectedCurancy;
	}

	public void setSourceSelectedCurancy(List<String> sourceSelectedCurancy) {
		this.sourceSelectedCurancy = sourceSelectedCurancy;
	}

	public List<String> getSourceSelectedMT() {
		return sourceSelectedMT;
	}

	public void setSourceSelectedMT(List<String> sourceSelectedMT) {
		this.sourceSelectedMT = sourceSelectedMT;
	}

	public boolean isIncludeSysMsg() {
		return includeSysMsg;
	}

	public void setIncludeSysMsg(boolean includeSysMsg) {
		this.includeSysMsg = includeSysMsg;
	}

	public String getAccountWithInstitution() {
		return accountWithInstitution;
	}

	public void setAccountWithInstitution(String accountWithInstitution) {
		this.accountWithInstitution = accountWithInstitution;
	}

	public String getOrderingInstitution() {
		return orderingInstitution;
	}

	public void setOrderingInstitution(String orderingInstitution) {
		this.orderingInstitution = orderingInstitution;
	}

	public String getOrderingCustomer() {
		return orderingCustomer;
	}

	public void setOrderingCustomer(String orderingCustomer) {
		this.orderingCustomer = orderingCustomer;
	}

	public String getBeneficiaryCustomer() {
		return beneficiaryCustomer;
	}

	public void setBeneficiaryCustomer(String beneficiaryCustomer) {
		this.beneficiaryCustomer = beneficiaryCustomer;
	}

	public String getDetailOfCharge() {
		return detailOfCharge;
	}

	public void setDetailOfCharge(String detailOfCharge) {
		this.detailOfCharge = detailOfCharge;
	}

	public String getDeductsFrom() {
		return deductsFrom;
	}

	public void setDeductsFrom(String deductsFrom) {
		this.deductsFrom = deductsFrom;
	}

	public String getDeductsTo() {
		return deductsTo;
	}

	public void setDeductsTo(String deductsTo) {
		this.deductsTo = deductsTo;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonCodes() {
		return reasonCodes;
	}

	public void setReasonCodes(String reasonCodes) {
		this.reasonCodes = reasonCodes;
	}

	public String getStatusOriginatorBic() {
		return statusOriginatorBic;
	}

	public void setStatusOriginatorBic(String statusOriginatorBic) {
		this.statusOriginatorBic = statusOriginatorBic;
	}

	public String getForwordBic() {
		return forwordBic;
	}

	public void setForwordBic(String forwordBic) {
		this.forwordBic = forwordBic;
	}

	public boolean isStopRecal() {
		return stopRecal;
	}

	public void setStopRecal(boolean stopRecal) {
		this.stopRecal = stopRecal;
	}

	public boolean isAtMyBank() {
		return atMyBank;
	}

	public void setAtMyBank(boolean atMyBank) {
		this.atMyBank = atMyBank;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public String getInsAmountFrom() {
		return insAmountFrom;
	}

	public void setInsAmountFrom(String insAmountFrom) {
		this.insAmountFrom = insAmountFrom;
	}

	public String getInsAmountTo() {
		return insAmountTo;
	}

	public void setInsAmountTo(String insAmountTo) {
		this.insAmountTo = insAmountTo;
	}

	public String getGpiCur() {
		return gpiCur;
	}

	public void setGpiCur(String gpiCur) {
		this.gpiCur = gpiCur;
	}

	public ParamWebSearviceStatus getParamWebSearviceStatus() {
		return paramWebSearviceStatus;
	}

	public void setParamWebSearviceStatus(ParamWebSearviceStatus paramWebSearviceStatus) {
		this.paramWebSearviceStatus = paramWebSearviceStatus;
	}

	public List<String> getMandatoryParams() {
		return mandatoryParams;
	}

	public void setMandatoryParams(List<String> mandatoryParams) {
		this.mandatoryParams = mandatoryParams;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getSenderCorr() {
		return senderCorr;
	}

	public void setSenderCorr(String senderCorr) {
		this.senderCorr = senderCorr;
	}

	public String getReceiverCorr() {
		return receiverCorr;
	}

	public void setReceiverCorr(String receiverCorr) {
		this.receiverCorr = receiverCorr;
	}

	public String getReimbursementInst() {
		return reimbursementInst;
	}

	public void setReimbursementInst(String reimbursementInst) {
		this.reimbursementInst = reimbursementInst;
	}

	public boolean isEnabelGpiSeacrh() {
		return enabelGpiSeacrh;
	}

	public void setEnabelGpiSeacrh(boolean enabelGpiSeacrh) {
		this.enabelGpiSeacrh = enabelGpiSeacrh;
	}

	public String getgSRPReasonCode() {
		return gSRPReasonCode;
	}

	public void setgSRPReasonCode(String gSRPReasonCode) {
		this.gSRPReasonCode = gSRPReasonCode;
	}

	public String getSattlmentMethod() {
		return sattlmentMethod;
	}

	public void setSattlmentMethod(String sattlmentMethod) {
		this.sattlmentMethod = sattlmentMethod;
	}

	public String getClearingSystemList() {
		return clearingSystemList;
	}

	public void setClearingSystemList(String clearingSystemList) {
		this.clearingSystemList = clearingSystemList;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<Object> getQueryVariablesBinding() {
		return queryVariablesBinding;
	}

	public void setQueryVariablesBinding(List<Object> queryVariablesBinding) {
		this.queryVariablesBinding = queryVariablesBinding;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

}
