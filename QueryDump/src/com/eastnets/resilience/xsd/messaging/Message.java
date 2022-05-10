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
package com.eastnets.resilience.xsd.messaging;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.eastnets.resilience.xmldump.utils.StringUtils;
import com.eastnets.resilience.xsd.BaseObject;
import com.eastnets.resilience.xsd.InputStartEntry;

@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", propOrder = { "identifier", "validationRequested", "validationPassed", "clazz",
		"relatedSUmid", "textReadonly", "deleteInhibited", "textModified", "partial", "status", "creationApplication",
		"creationMpfnName", "creationRoutingPoint", "creationOperator", "creationDate", "modificationOperator",
		"modificationDate", "verificationOperator", "casSenderReference", "casTargetRoutingPoint", "batchReference",
		"recoveryAcceptInfo", "applicationSenderReference", "xmlv2DigestValue", "uumid", "uumidSuffix",
		"senderCorrespondentType", "senderCorrespondentInstitutionName", "senderCorrespondentDepartment",
		"senderCorrespondentLastName", "senderCorrespondentFirstName", "senderInstitutionName", "senderBranchInfo",
		"senderLocation", "senderCityName", "senderCountryCode", "senderCountryName", "receiverAliasName",
		"formatName", "subFormat", "syntaxTableVersion", "nature", "networkApplicationIndication", "type", "live",
		"retrieved", "templateName", "securityRequired", "possibleDuplicateCreation", "networkPriority",
		"deliveryOverdueWarningRequired", "networkObsolescencePeriod", "networkDeliveryNotificationRequired",
		"userPriorityCode", "userReferenceText", "userIssuedAsPde", "finValueDate", "finCurrencyAmount",
		"transactionReference", "relatedTransactionReference", "dataKeyword1", "dataKeyword2", "dataKeyword3",
		"messageUserGroup", "copyServiceId", "releaseInfo", "securityApplicationName", "possibleDuplicate",
		"messageIdentifier", "senderSwiftAddress", "receiverSwiftAddress", "approvalTrailer", "requestorDn", "service",
		"mesg_sla", "mesg_e2e_transaction_ref", "mesg_is_copy_req", "mesg_auth_delv_notif_req", "mesg_copy_recipient_dn",
		"mesg_copy_state", "mesg_copy_type", "mesg_fin_inform_release_info", "mesg_is_copy", "mesg_overdue_warning_delay", 
		"mesg_overdue_warning_time", "mesg_source_template_name",
		"requestType", "xmlQueryReference1", "xmlQueryReference2", "xmlQueryReference3", "payloadType",
		"signatureDigestValue", "signatureDigestReference", "usePkiSignature", "payloadAttributeName",
		"payloadAttributeValue", "overdueWarningTime", "overdueWarningDelay", "copyRequired", "copy",
		"transferDescription", "transferInfo", "fileLogicalName", "fileSize", "fileDescription", "fileInfo",
		"positiveAuthorizationNtificationRequested", "fileDigestAlgorithm", "fileDigestValue", "fileHeaderInfo",
		"deliveryNotificationReceiverDn", "deliveryNotificationRequestType", "copyType", "copyState",
		"copyRecipientDn", "vendorName", "productName", "productVersion", "recipientList", "recipientListPublic",
		"originalSnfReference", "thirdPartyDnList", "instance", "text" })


public class Message extends BaseObject implements InputStartEntry {

	@XmlElement(name = "Identifier")
	protected MessageIdentifier identifier;

	@XmlElement(name = "ValidationRequested", required = true)
	protected ValidationLevel validationRequested;

	@XmlElement(name = "ValidationPassed")
	protected ValidationLevel validationPassed;

	@XmlElement(name = "Class")
	protected Class clazz;

	@XmlElement(name = "RelatedSUmid")
	protected String relatedSUmid;

	@XmlElement(name = "TextReadonly")
	protected boolean textReadonly;

	@XmlElement(name = "DeleteInhibited")
	protected boolean deleteInhibited;

	@XmlElement(name = "TextModified")
	protected boolean textModified;

	@XmlElement(name = "Partial")
	protected boolean partial;

	@XmlElement(name = "Status")
	protected Status status;

	@XmlElement(name = "CreationApplication")
	protected String creationApplication;

	@XmlElement(name = "CreationMpfnName")
	protected String creationMpfnName;

	@XmlElement(name = "CreationRoutingPoint")
	protected String creationRoutingPoint;

	@XmlElement(name = "CreationOperator")
	protected String creationOperator;

	@XmlElement(name = "CreationDate")
	protected XMLGregorianCalendar creationDate;

	@XmlElement(name = "ModificationOperator")
	protected String modificationOperator;

	@XmlElement(name = "ModificationDate")
	protected XMLGregorianCalendar modificationDate;

	@XmlElement(name = "VerificationOperator")
	protected String verificationOperator;

	@XmlElement(name = "CasSenderReference")
	protected String casSenderReference;

	@XmlElement(name = "CasTargetRoutingPoint")
	protected String casTargetRoutingPoint;

	@XmlElement(name = "BatchReference")
	protected String batchReference;

	@XmlElement(name = "RecoveryAcceptInfo")
	protected Boolean recoveryAcceptInfo;

	@XmlElement(name = "ApplicationSenderReference")
	protected String applicationSenderReference;

	@XmlElement(name = "Xmlv2DigestValue")
	protected String xmlv2DigestValue;

	@XmlElement(name = "Uumid")
	protected String uumid;

	@XmlElement(name = "UumidSuffix")
	protected Integer uumidSuffix;

	@XmlElement(name = "SenderCorrespondentType")
	protected CorrespondentType senderCorrespondentType;

	@XmlElement(name = "SenderCorrespondentInstitutionName")
	protected String senderCorrespondentInstitutionName;

	@XmlElement(name = "SenderCorrespondentDepartment")
	protected String senderCorrespondentDepartment;

	@XmlElement(name = "SenderCorrespondentLastName")
	protected String senderCorrespondentLastName;

	@XmlElement(name = "SenderCorrespondentFirstName")
	protected String senderCorrespondentFirstName;

	@XmlElement(name = "SenderInstitutionName")
	protected String senderInstitutionName;

	@XmlElement(name = "SenderBranchInfo")
	protected String senderBranchInfo;

	@XmlElement(name = "SenderLocation")
	protected String senderLocation;

	@XmlElement(name = "SenderCityName")
	protected String senderCityName;

	@XmlElement(name = "SenderCountryCode")
	protected String senderCountryCode;

	@XmlElement(name = "SenderCountryName")
	protected String senderCountryName;

	@XmlElement(name = "ReceiverAliasName")
	protected String receiverAliasName;

	@XmlElement(name = "FormatName")
	protected String formatName;

	@XmlElement(name = "SubFormat")
	protected SubFormat subFormat;

	@XmlElement(name = "SyntaxTableVersion")
	protected String syntaxTableVersion;

	@XmlElement(name = "Nature")
	protected Nature nature;

	@XmlElement(name = "NetworkApplicationIndication")
	protected String networkApplicationIndication;

	@XmlElement(name = "Type")
	protected String type;

	@XmlElement(name = "Live")
	protected Boolean live;

	@XmlElement(name = "Retrieved")
	protected Boolean retrieved;

	@XmlElement(name = "TemplateName")
	protected String templateName;

	@XmlElement(name = "SecurityRequired")
	protected Boolean securityRequired;

	@XmlElement(name = "PossibleDuplicateCreation")
	protected PossibleDuplicateCreation possibleDuplicateCreation;

	@XmlElement(name = "NetworkPriority")
	protected NetworkPriority networkPriority;

	@XmlElement(name = "DeliveryOverdueWarningRequired")
	protected Boolean deliveryOverdueWarningRequired;

	@XmlElement(name = "NetworkObsolescencePeriod")
	protected Integer networkObsolescencePeriod;

	@XmlElement(name = "NetworkDeliveryNotificationRequired")
	protected Boolean networkDeliveryNotificationRequired;

	@XmlElement(name = "UserPriorityCode")
	protected String userPriorityCode;

	@XmlElement(name = "UserReferenceText")
	protected String userReferenceText;

	@XmlElement(name = "UserIssuedAsPde")
	protected Boolean userIssuedAsPde;

	@XmlElement(name = "FinValueDate")
	protected String finValueDate;

	@XmlElement(name = "FinCurrencyAmount")
	protected String finCurrencyAmount;

	@XmlElement(name = "TransactionReference")
	protected String transactionReference;

	@XmlElement(name = "RelatedTransactionReference")
	protected String relatedTransactionReference;

	@XmlElement(name = "DataKeyword1")
	protected String dataKeyword1;

	@XmlElement(name = "DataKeyword2")
	protected String dataKeyword2;

	@XmlElement(name = "DataKeyword3")
	protected String dataKeyword3;

	@XmlElement(name = "MessageUserGroup")
	protected String messageUserGroup;

	@XmlElement(name = "CopyServiceId")
	protected String copyServiceId;

	@XmlElement(name = "ReleaseInfo")
	protected String releaseInfo;

	@XmlElement(name = "SecurityApplicationName")
	protected String securityApplicationName;

	@XmlElement(name = "PossibleDuplicate")
	protected Boolean possibleDuplicate;

	@XmlElement(name = "MessageIdentifier")
	protected String messageIdentifier;

	@XmlElement(name = "SenderSwiftAddress")
	protected String senderSwiftAddress;

	@XmlElement(name = "ReceiverSwiftAddress")
	protected String receiverSwiftAddress;

	@XmlElement(name = "ApprovalTrailer")
	protected String approvalTrailer;

	@XmlElement(name = "RequestorDn")
	protected String requestorDn;

	@XmlElement(name = "Service")
	protected String service;

	@XmlElement(name = "ServiceLevelAgreement")
	protected String mesg_sla;
	
	@XmlElement(name = "E2ETransactionReference")
	protected String mesg_e2e_transaction_ref;
	
	@XmlElement(name = "CopyRequired")
	protected Boolean mesg_is_copy_req;
	
	@XmlElement(name = "AuthorisationDeliveryNotificationRequested")
	protected Boolean mesg_auth_delv_notif_req;
	
	@XmlElement(name = "CopyRecipientDn")
	protected Boolean mesg_copy_recipient_dn;
	
	@XmlElement(name = "CopyState")
	protected CopyState mesg_copy_state; 
	
	@XmlElement(name = "CopyType")
	protected CopyType mesg_copy_type;
	
	@XmlElement(name = "FinInformReleaseInfo")
	protected String mesg_fin_inform_release_info;
	
	@XmlElement(name = "Copy")
	protected Boolean mesg_is_copy ;
	
	@XmlElement(name = "OverdueWarningDelay")
	protected Integer mesg_overdue_warning_delay;
	
	@XmlElement(name = "OverdueWarningTime")
	protected XMLGregorianCalendar mesg_overdue_warning_time;
		
	@XmlElement(name = "SourceTemplateName")
	protected String mesg_source_template_name;
		
	@XmlElement(name = "RequestType")
	protected String requestType;

	@XmlElement(name = "XmlQueryReference1")
	protected String xmlQueryReference1;

	@XmlElement(name = "XmlQueryReference2")
	protected String xmlQueryReference2;

	@XmlElement(name = "XmlQueryReference3")
	protected String xmlQueryReference3;

	@XmlElement(name = "PayloadType")
	protected String payloadType;

	@XmlElement(name = "SignatureDigestValue")
	protected List<String> signatureDigestValue;

	@XmlElement(name = "SignatureDigestReference")
	protected List<String> signatureDigestReference;

	@XmlElement(name = "UsePkiSignature")
	protected Boolean usePkiSignature;

	@XmlElement(name = "PayloadAttributeName")
	protected List<String> payloadAttributeName;

	@XmlElement(name = "PayloadAttributeValue")
	protected List<String> payloadAttributeValue;

	@XmlElement(name = "OverdueWarningTime")
	protected XMLGregorianCalendar overdueWarningTime;

	@XmlElement(name = "OverdueWarningDelay")
	protected Integer overdueWarningDelay;

	@XmlElement(name = "CopyRequired")
	protected Boolean copyRequired;

	@XmlElement(name = "Copy")
	protected Boolean copy;

	@XmlElement(name = "TransferDescription")
	protected String transferDescription;

	@XmlElement(name = "TransferInfo")
	protected String transferInfo;

	@XmlElement(name = "FileLogicalName")
	protected String fileLogicalName;

	@XmlElement(name = "FileSize")
	protected Long fileSize;

	@XmlElement(name = "FileDescription")
	protected String fileDescription;

	@XmlElement(name = "FileInfo")
	protected String fileInfo;

	@XmlElement(name = "PositiveAuthorizationNtificationRequested")
	protected Boolean positiveAuthorizationNtificationRequested;

	@XmlElement(name = "FileDigestAlgorithm")
	protected String fileDigestAlgorithm;

	@XmlElement(name = "FileDigestValue")
	protected String fileDigestValue;

	@XmlElement(name = "FileHeaderInfo")
	protected String fileHeaderInfo;

	@XmlElement(name = "DeliveryNotificationReceiverDn")
	protected String deliveryNotificationReceiverDn;

	@XmlElement(name = "DeliveryNotificationRequestType")
	protected String deliveryNotificationRequestType;

	@XmlElement(name = "CopyType")
	protected CopyType copyType;

	@XmlElement(name = "CopyState")
	protected CopyState copyState;

	@XmlElement(name = "CopyRecipientDn")
	protected String copyRecipientDn;

	@XmlElement(name = "VendorName")
	protected List<String> vendorName;

	@XmlElement(name = "ProductName")
	protected List<String> productName;

	@XmlElement(name = "ProductVersion")
	protected List<String> productVersion;

	@XmlElement(name = "RecipientList")
	protected List<String> recipientList;

	@XmlElement(name = "RecipientListPublic")
	protected Boolean recipientListPublic;

	@XmlElement(name = "OriginalSnfReference")
	protected String originalSnfReference;

	@XmlElement(name = "ThirdPartyDnList")
	protected List<String> thirdPartyDnList;

	@XmlElement(name = "Instance")
	protected List<Instance> instance;

	@XmlElement(name = "Text", required = true)
	protected Text text;

	
	public String getMesg_sla() {
		return mesg_sla;
	}

	public void setMesg_sla(String mesg_sla) {
		this.mesg_sla = mesg_sla;
	}

	public String getMesg_e2e_transaction_ref() {
		return mesg_e2e_transaction_ref;
	}

	public void setMesg_e2e_transaction_ref(String mesg_e2e_transaction_ref) {
		this.mesg_e2e_transaction_ref = mesg_e2e_transaction_ref;
	}

	public Boolean getMesg_is_copy_req() {
		return mesg_is_copy_req;
	}

	public void setMesg_is_copy_req(Boolean mesg_is_copy_req) {
		this.mesg_is_copy_req = mesg_is_copy_req;
	}

	public Boolean getMesg_auth_delv_notif_req() {
		return mesg_auth_delv_notif_req;
	}

	public void setMesg_auth_delv_notif_req(Boolean mesg_auth_delv_notif_req) {
		this.mesg_auth_delv_notif_req = mesg_auth_delv_notif_req;
	}

	public Boolean getMesg_copy_recipient_dn() {
		return mesg_copy_recipient_dn;
	}

	public void setMesg_copy_recipient_dn(Boolean mesg_copy_recipient_dn) {
		this.mesg_copy_recipient_dn = mesg_copy_recipient_dn;
	}

	public CopyState getMesg_copy_state() {
		return mesg_copy_state;
	}

	public void setMesg_copy_state(CopyState mesg_copy_state) {
		this.mesg_copy_state = mesg_copy_state;
	}

	public CopyType getMesg_copy_type() {
		return mesg_copy_type;
	}

	public void setMesg_copy_type(CopyType mesg_copy_type) {
		this.mesg_copy_type = mesg_copy_type;
	}

	public String getMesg_fin_inform_release_info() {
		return mesg_fin_inform_release_info;
	}

	public void setMesg_fin_inform_release_info(String mesg_fin_inform_release_info) {
		this.mesg_fin_inform_release_info = mesg_fin_inform_release_info;
	}

	public Boolean getMesg_is_copy() {
		return mesg_is_copy;
	}

	public void setMesg_is_copy(Boolean mesg_is_copy) {
		this.mesg_is_copy = mesg_is_copy;
	}

	public Integer getMesg_overdue_warning_delay() {
		return mesg_overdue_warning_delay;
	}

	public void setMesg_overdue_warning_delay(Integer mesg_overdue_warning_delay) {
		this.mesg_overdue_warning_delay = mesg_overdue_warning_delay;
	}

	public XMLGregorianCalendar getMesg_overdue_warning_time() {
		return mesg_overdue_warning_time;
	}

	public void setMesg_overdue_warning_time(XMLGregorianCalendar mesg_overdue_warning_time) {
		this.mesg_overdue_warning_time = mesg_overdue_warning_time;
	}

	public String getMesg_source_template_name() {
		return mesg_source_template_name;
	}

	public void setMesg_source_template_name(String mesg_source_template_name) {
		this.mesg_source_template_name = mesg_source_template_name;
	}

	public Boolean getRecoveryAcceptInfo() {
		return recoveryAcceptInfo;
	}

	public Boolean getLive() {
		return live;
	}

	public Boolean getRetrieved() {
		return retrieved;
	}

	public Boolean getSecurityRequired() {
		return securityRequired;
	}

	public Boolean getDeliveryOverdueWarningRequired() {
		return deliveryOverdueWarningRequired;
	}

	public Boolean getNetworkDeliveryNotificationRequired() {
		return networkDeliveryNotificationRequired;
	}

	public Boolean getUserIssuedAsPde() {
		return userIssuedAsPde;
	}

	public Boolean getPossibleDuplicate() {
		return possibleDuplicate;
	}

	public Boolean getUsePkiSignature() {
		return usePkiSignature;
	}

	public Boolean getCopyRequired() {
		return copyRequired;
	}

	public Boolean getCopy() {
		return copy;
	}

	public Boolean getPositiveAuthorizationNtificationRequested() {
		return positiveAuthorizationNtificationRequested;
	}

	public Boolean getRecipientListPublic() {
		return recipientListPublic;
	}

	public void setSignatureDigestValue(List<String> signatureDigestValue) {
		this.signatureDigestValue = signatureDigestValue;
	}

	public void setSignatureDigestReference(List<String> signatureDigestReference) {
		this.signatureDigestReference = signatureDigestReference;
	}

	public void setPayloadAttributeName(List<String> payloadAttributeName) {
		this.payloadAttributeName = payloadAttributeName;
	}

	public void setPayloadAttributeValue(List<String> payloadAttributeValue) {
		this.payloadAttributeValue = payloadAttributeValue;
	}

	public void setVendorName(List<String> vendorName) {
		this.vendorName = vendorName;
	}

	public void setProductName(List<String> productName) {
		this.productName = productName;
	}

	public void setProductVersion(List<String> productVersion) {
		this.productVersion = productVersion;
	}

	public void setRecipientList(List<String> recipientList) {
		this.recipientList = recipientList;
	}

	public void setThirdPartyDnList(List<String> thirdPartyDnList) {
		this.thirdPartyDnList = thirdPartyDnList;
	}

	public void setInstance(List<Instance> instance) {
		this.instance = instance;
	}

	public MessageIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(MessageIdentifier paramMessageIdentifier) {
		this.identifier = paramMessageIdentifier;
	}

	public ValidationLevel getValidationRequested() {
		return this.validationRequested;
	}

	public void setValidationRequested(ValidationLevel paramValidationLevel) {
		this.validationRequested = paramValidationLevel;
	}

	public ValidationLevel getValidationPassed() {
		return this.validationPassed;
	}

	public void setValidationPassed(ValidationLevel paramValidationLevel) {
		this.validationPassed = paramValidationLevel;
	}

	public Class getClazz() {
		return this.clazz;
	}

	public void setClazz(Class paramClass) {
		this.clazz = paramClass;
	}

	public String getRelatedSUmid() {
		return this.relatedSUmid;
	}

	public void setRelatedSUmid(String paramString) {
		this.relatedSUmid = paramString;
	}

	public boolean isTextReadonly() {
		return this.textReadonly;
	}

	public void setTextReadonly(boolean paramBoolean) {
		this.textReadonly = paramBoolean;
	}

	public boolean isDeleteInhibited() {
		return this.deleteInhibited;
	}

	public void setDeleteInhibited(boolean paramBoolean) {
		this.deleteInhibited = paramBoolean;
	}

	public boolean isTextModified() {
		return this.textModified;
	}

	public void setTextModified(boolean paramBoolean) {
		this.textModified = paramBoolean;
	}

	public boolean isPartial() {
		return this.partial;
	}

	public void setPartial(boolean paramBoolean) {
		this.partial = paramBoolean;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status paramStatus) {
		this.status = paramStatus;
	}

	public String getCreationApplication() {
		return this.creationApplication;
	}

	public void setCreationApplication(String paramString) {
		this.creationApplication = paramString;
	}

	public String getCreationMpfnName() {
		return this.creationMpfnName;
	}

	public void setCreationMpfnName(String paramString) {
		this.creationMpfnName = paramString;
	}

	public String getCreationRoutingPoint() {
		return this.creationRoutingPoint;
	}

	public void setCreationRoutingPoint(String paramString) {
		this.creationRoutingPoint = paramString;
	}

	public String getCreationOperator() {
		return this.creationOperator;
	}

	public void setCreationOperator(String paramString) {
		this.creationOperator = paramString;
	}

	public XMLGregorianCalendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.creationDate = paramXMLGregorianCalendar;
	}

	public String getModificationOperator() {
		return this.modificationOperator;
	}

	public void setModificationOperator(String paramString) {
		this.modificationOperator = paramString;
	}

	public XMLGregorianCalendar getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.modificationDate = paramXMLGregorianCalendar;
	}

	public String getVerificationOperator() {
		return this.verificationOperator;
	}

	public void setVerificationOperator(String paramString) {
		this.verificationOperator = paramString;
	}

	public String getCasSenderReference() {
		return this.casSenderReference;
	}

	public void setCasSenderReference(String paramString) {
		this.casSenderReference = paramString;
	}

	public String getCasTargetRoutingPoint() {
		return this.casTargetRoutingPoint;
	}

	public void setCasTargetRoutingPoint(String paramString) {
		this.casTargetRoutingPoint = paramString;
	}

	public String getBatchReference() {
		return this.batchReference;
	}

	public void setBatchReference(String paramString) {
		this.batchReference = paramString;
	}

	public Boolean isRecoveryAcceptInfo() {
		return this.recoveryAcceptInfo;
	}

	public void setRecoveryAcceptInfo(Boolean paramBoolean) {
		this.recoveryAcceptInfo = paramBoolean;
	}

	public String getApplicationSenderReference() {
		return this.applicationSenderReference;
	}

	public void setApplicationSenderReference(String paramString) {
		this.applicationSenderReference = paramString;
	}

	public String getXmlv2DigestValue() {
		return this.xmlv2DigestValue;
	}

	public void setXmlv2DigestValue(String paramString) {
		this.xmlv2DigestValue = paramString;
	}

	public String getUumid() {
		return this.uumid;
	}

	public void setUumid(String paramString) {
		this.uumid = paramString;
	}

	public Integer getUumidSuffix() {
		return this.uumidSuffix;
	}

	public void setUumidSuffix(Integer paramInteger) {
		this.uumidSuffix = paramInteger;
	}

	public CorrespondentType getSenderCorrespondentType() {
		return this.senderCorrespondentType;
	}

	public void setSenderCorrespondentType(CorrespondentType paramCorrespondentType) {
		this.senderCorrespondentType = paramCorrespondentType;
	}

	public String getSenderCorrespondentInstitutionName() {
		return this.senderCorrespondentInstitutionName;
	}

	public void setSenderCorrespondentInstitutionName(String paramString) {
		this.senderCorrespondentInstitutionName = paramString;
	}

	public String getSenderCorrespondentDepartment() {
		return this.senderCorrespondentDepartment;
	}

	public void setSenderCorrespondentDepartment(String paramString) {
		this.senderCorrespondentDepartment = paramString;
	}

	public String getSenderCorrespondentLastName() {
		return this.senderCorrespondentLastName;
	}

	public void setSenderCorrespondentLastName(String paramString) {
		this.senderCorrespondentLastName = paramString;
	}

	public String getSenderCorrespondentFirstName() {
		return this.senderCorrespondentFirstName;
	}

	public void setSenderCorrespondentFirstName(String paramString) {
		this.senderCorrespondentFirstName = paramString;
	}

	public String getSenderInstitutionName() {
		return this.senderInstitutionName;
	}

	public void setSenderInstitutionName(String paramString) {
		this.senderInstitutionName = paramString;
	}

	public String getSenderBranchInfo() {
		return this.senderBranchInfo;
	}

	public void setSenderBranchInfo(String paramString) {
		this.senderBranchInfo = paramString;
	}

	public String getSenderLocation() {
		return this.senderLocation;
	}

	public void setSenderLocation(String paramString) {
		this.senderLocation = paramString;
	}

	public String getSenderCityName() {
		return this.senderCityName;
	}

	public void setSenderCityName(String paramString) {
		this.senderCityName = paramString;
	}

	public String getSenderCountryCode() {
		return this.senderCountryCode;
	}

	public void setSenderCountryCode(String paramString) {
		this.senderCountryCode = paramString;
	}

	public String getSenderCountryName() {
		return this.senderCountryName;
	}

	public void setSenderCountryName(String paramString) {
		this.senderCountryName = paramString;
	}

	public String getReceiverAliasName() {
		return this.receiverAliasName;
	}

	public void setReceiverAliasName(String paramString) {
		this.receiverAliasName = paramString;
	}

	public String getFormatName() {
		return this.formatName;
	}

	public void setFormatName(String paramString) {
		this.formatName = paramString;
	}

	public SubFormat getSubFormat() {
		return this.subFormat;
	}

	public void setSubFormat(SubFormat paramSubFormat) {
		this.subFormat = paramSubFormat;
	}

	public String getSyntaxTableVersion() {
		return this.syntaxTableVersion;
	}

	public void setSyntaxTableVersion(String paramString) {
		this.syntaxTableVersion = paramString;
	}

	public Nature getNature() {
		return this.nature;
	}

	public void setNature(Nature paramNature) {
		this.nature = paramNature;
	}

	public String getNetworkApplicationIndication() {
		return this.networkApplicationIndication;
	}

	public void setNetworkApplicationIndication(String paramString) {
		this.networkApplicationIndication = paramString;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String paramString) {
		this.type = paramString;
	}

	public Boolean isLive() {
		return this.live;
	}

	public void setLive(Boolean paramBoolean) {
		this.live = paramBoolean;
	}

	public Boolean isRetrieved() {
		return this.retrieved;
	}

	public void setRetrieved(Boolean paramBoolean) {
		this.retrieved = paramBoolean;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String paramString) {
		this.templateName = paramString;
	}

	public Boolean isSecurityRequired() {
		return this.securityRequired;
	}

	public void setSecurityRequired(Boolean paramBoolean) {
		this.securityRequired = paramBoolean;
	}

	public PossibleDuplicateCreation getPossibleDuplicateCreation() {
		return this.possibleDuplicateCreation;
	}

	public void setPossibleDuplicateCreation(PossibleDuplicateCreation paramPossibleDuplicateCreation) {
		this.possibleDuplicateCreation = paramPossibleDuplicateCreation;
	}

	public NetworkPriority getNetworkPriority() {
		return this.networkPriority;
	}

	public void setNetworkPriority(NetworkPriority paramNetworkPriority) {
		this.networkPriority = paramNetworkPriority;
	}

	public Boolean isDeliveryOverdueWarningRequired() {
		return this.deliveryOverdueWarningRequired;
	}

	public void setDeliveryOverdueWarningRequired(Boolean paramBoolean) {
		this.deliveryOverdueWarningRequired = paramBoolean;
	}

	public Integer getNetworkObsolescencePeriod() {
		return this.networkObsolescencePeriod;
	}

	public void setNetworkObsolescencePeriod(Integer paramInteger) {
		this.networkObsolescencePeriod = paramInteger;
	}

	public Boolean isNetworkDeliveryNotificationRequired() {
		return this.networkDeliveryNotificationRequired;
	}

	public void setNetworkDeliveryNotificationRequired(Boolean paramBoolean) {
		this.networkDeliveryNotificationRequired = paramBoolean;
	}

	public String getUserPriorityCode() {
		return this.userPriorityCode;
	}

	public void setUserPriorityCode(String paramString) {
		this.userPriorityCode = paramString;
	}

	public String getUserReferenceText() {
		return this.userReferenceText;
	}

	public void setUserReferenceText(String paramString) {
		this.userReferenceText = paramString;
	}

	public Boolean isUserIssuedAsPde() {
		return this.userIssuedAsPde;
	}

	public void setUserIssuedAsPde(Boolean paramBoolean) {
		this.userIssuedAsPde = paramBoolean;
	}

	public String getFinValueDate() {
		return this.finValueDate;
	}

	public void setFinValueDate(String paramString) {
		this.finValueDate = paramString;
	}

	public String getFinCurrencyAmount() {
		return this.finCurrencyAmount;
	}

	public void setFinCurrencyAmount(String paramString) {
		this.finCurrencyAmount = paramString;
	}

	public String getTransactionReference() {
		return this.transactionReference;
	}

	public void setTransactionReference(String paramString) {
		this.transactionReference = paramString;
	}

	public String getRelatedTransactionReference() {
		return this.relatedTransactionReference;
	}

	public void setRelatedTransactionReference(String paramString) {
		this.relatedTransactionReference = paramString;
	}

	public String getDataKeyword1() {
		return this.dataKeyword1;
	}

	public void setDataKeyword1(String paramString) {
		this.dataKeyword1 = paramString;
	}

	public String getDataKeyword2() {
		return this.dataKeyword2;
	}

	public void setDataKeyword2(String paramString) {
		this.dataKeyword2 = paramString;
	}

	public String getDataKeyword3() {
		return this.dataKeyword3;
	}

	public void setDataKeyword3(String paramString) {
		this.dataKeyword3 = paramString;
	}

	public String getMessageUserGroup() {
		return this.messageUserGroup;
	}

	public void setMessageUserGroup(String paramString) {
		this.messageUserGroup = paramString;
	}

	public String getCopyServiceId() {
		return this.copyServiceId;
	}

	public void setCopyServiceId(String paramString) {
		this.copyServiceId = paramString;
	}

	public String getReleaseInfo() {
		return this.releaseInfo;
	}

	public void setReleaseInfo(String paramString) {
		this.releaseInfo = paramString;
	}

	public String getSecurityApplicationName() {
		return this.securityApplicationName;
	}

	public void setSecurityApplicationName(String paramString) {
		this.securityApplicationName = paramString;
	}

	public Boolean isPossibleDuplicate() {
		return this.possibleDuplicate;
	}

	public void setPossibleDuplicate(Boolean paramBoolean) {
		this.possibleDuplicate = paramBoolean;
	}

	public String getMessageIdentifier() {
		return this.messageIdentifier;
	}

	public void setMessageIdentifier(String paramString) {
		this.messageIdentifier = paramString;
	}

	public String getSenderSwiftAddress() {
		return this.senderSwiftAddress;
	}

	public void setSenderSwiftAddress(String paramString) {
		this.senderSwiftAddress = paramString;
	}

	public String getReceiverSwiftAddress() {
		return this.receiverSwiftAddress;
	}

	public void setReceiverSwiftAddress(String paramString) {
		this.receiverSwiftAddress = paramString;
	}

	public String getApprovalTrailer() {
		return this.approvalTrailer;
	}

	public void setApprovalTrailer(String paramString) {
		this.approvalTrailer = paramString;
	}

	public String getRequestorDn() {
		return this.requestorDn;
	}

	public void setRequestorDn(String paramString) {
		this.requestorDn = paramString;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String paramString) {
		this.service = paramString;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String paramString) {
		this.requestType = paramString;
	}

	public String getXmlQueryReference1() {
		return this.xmlQueryReference1;
	}

	public void setXmlQueryReference1(String paramString) {
		this.xmlQueryReference1 = paramString;
	}

	public String getXmlQueryReference2() {
		return this.xmlQueryReference2;
	}

	public void setXmlQueryReference2(String paramString) {
		this.xmlQueryReference2 = paramString;
	}

	public String getXmlQueryReference3() {
		return this.xmlQueryReference3;
	}

	public void setXmlQueryReference3(String paramString) {
		this.xmlQueryReference3 = paramString;
	}

	public String getPayloadType() {
		return this.payloadType;
	}

	public void setPayloadType(String paramString) {
		this.payloadType = paramString;
	}

	public List<String> getSignatureDigestValue() {
		if (this.signatureDigestValue == null)
			this.signatureDigestValue = new ArrayList<String>();
		return this.signatureDigestValue;
	}

	public List<String> getSignatureDigestReference() {
		if (this.signatureDigestReference == null)
			this.signatureDigestReference = new ArrayList<String>();
		return this.signatureDigestReference;
	}

	public Boolean isUsePkiSignature() {
		return this.usePkiSignature;
	}

	public void setUsePkiSignature(Boolean paramBoolean) {
		this.usePkiSignature = paramBoolean;
	}

	public List<String> getPayloadAttributeName() {
		if (this.payloadAttributeName == null)
			this.payloadAttributeName = new ArrayList<String>();
		return this.payloadAttributeName;
	}

	public List<String> getPayloadAttributeValue() {
		if (this.payloadAttributeValue == null)
			this.payloadAttributeValue = new ArrayList<String>();
		return this.payloadAttributeValue;
	}

	public XMLGregorianCalendar getOverdueWarningTime() {
		return this.overdueWarningTime;
	}

	public void setOverdueWarningTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.overdueWarningTime = paramXMLGregorianCalendar;
	}

	public Integer getOverdueWarningDelay() {
		return this.overdueWarningDelay;
	}

	public void setOverdueWarningDelay(Integer paramInteger) {
		this.overdueWarningDelay = paramInteger;
	}

	public Boolean isCopyRequired() {
		return this.copyRequired;
	}

	public void setCopyRequired(Boolean paramBoolean) {
		this.copyRequired = paramBoolean;
	}

	public Boolean isCopy() {
		return this.copy;
	}

	public void setCopy(Boolean paramBoolean) {
		this.copy = paramBoolean;
	}

	public String getTransferDescription() {
		return this.transferDescription;
	}

	public void setTransferDescription(String paramString) {
		this.transferDescription = paramString;
	}

	public String getTransferInfo() {
		return this.transferInfo;
	}

	public void setTransferInfo(String paramString) {
		this.transferInfo = paramString;
	}

	public String getFileLogicalName() {
		return this.fileLogicalName;
	}

	public void setFileLogicalName(String paramString) {
		this.fileLogicalName = paramString;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long paramLong) {
		this.fileSize = paramLong;
	}

	public String getFileDescription() {
		return this.fileDescription;
	}

	public void setFileDescription(String paramString) {
		this.fileDescription = paramString;
	}

	public String getFileInfo() {
		return this.fileInfo;
	}

	public void setFileInfo(String paramString) {
		this.fileInfo = paramString;
	}

	public Boolean isPositiveAuthorizationNtificationRequested() {
		return this.positiveAuthorizationNtificationRequested;
	}

	public void setPositiveAuthorizationNtificationRequested(Boolean paramBoolean) {
		this.positiveAuthorizationNtificationRequested = paramBoolean;
	}

	public String getFileDigestAlgorithm() {
		return this.fileDigestAlgorithm;
	}

	public void setFileDigestAlgorithm(String paramString) {
		this.fileDigestAlgorithm = paramString;
	}

	public String getFileDigestValue() {
		return this.fileDigestValue;
	}

	public void setFileDigestValue(String paramString) {
		this.fileDigestValue = paramString;
	}

	public String getFileHeaderInfo() {
		return this.fileHeaderInfo;
	}

	public void setFileHeaderInfo(String paramString) {
		this.fileHeaderInfo = paramString;
	}

	public String getDeliveryNotificationReceiverDn() {
		return this.deliveryNotificationReceiverDn;
	}

	public void setDeliveryNotificationReceiverDn(String paramString) {
		this.deliveryNotificationReceiverDn = paramString;
	}

	public String getDeliveryNotificationRequestType() {
		return this.deliveryNotificationRequestType;
	}

	public void setDeliveryNotificationRequestType(String paramString) {
		this.deliveryNotificationRequestType = paramString;
	}

	public CopyType getCopyType() {
		return this.copyType;
	}

	public void setCopyType(CopyType paramCopyType) {
		this.copyType = paramCopyType;
	}

	public CopyState getCopyState() {
		return this.copyState;
	}

	public void setCopyState(CopyState paramCopyState) {
		this.copyState = paramCopyState;
	}

	public String getCopyRecipientDn() {
		return this.copyRecipientDn;
	}

	public void setCopyRecipientDn(String paramString) {
		this.copyRecipientDn = paramString;
	}

	public List<String> getVendorName() {
		if (this.vendorName == null)
			this.vendorName = new ArrayList<String>();
		return this.vendorName;
	}

	public List<String> getProductName() {
		if (this.productName == null)
			this.productName = new ArrayList<String>();
		return this.productName;
	}

	public List<String> getProductVersion() {
		if (this.productVersion == null)
			this.productVersion = new ArrayList<String>();
		return this.productVersion;
	}

	public List<String> getRecipientList() {
		if (this.recipientList == null)
			this.recipientList = new ArrayList<String>();
		return this.recipientList;
	}

	public Boolean isRecipientListPublic() {
		return this.recipientListPublic;
	}

	public void setRecipientListPublic(Boolean paramBoolean) {
		this.recipientListPublic = paramBoolean;
	}

	public String getOriginalSnfReference() {
		return this.originalSnfReference;
	}

	public void setOriginalSnfReference(String paramString) {
		this.originalSnfReference = paramString;
	}

	public List<String> getThirdPartyDnList() {
		if (this.thirdPartyDnList == null)
			this.thirdPartyDnList = new ArrayList<String>();
		return this.thirdPartyDnList;
	}

	public List<Instance> getInstance() {
		if (this.instance == null)
			this.instance = new ArrayList<Instance>();
		return this.instance;
	}

	public Text getText() {
		return this.text;
	}

	public void setText(Text paramText) {
		this.text = paramText;
	}

	@Override
	public int getUmidL() {
		return StringUtils.getUmidl(this.getIdentifier().getSUmid());
	}

	@Override
	public int getUmidH() {
		return StringUtils.getUmidh(this.getIdentifier().getSUmid());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Uumid " + this.getUumid() + "\n");
		builder.append("UmidL " + this.getUmidL() + "\n");
		builder.append("UmidH " + this.getUmidH() + "\n");
		builder.append("Type " + this.getType() + "\n");
		builder.append("Status " + this.getStatus() + "\n");
		builder.append("senderX1 " + this.getSenderCorrespondentInstitutionName() + "\n");
		builder.append("Nature " + this.getNature() + "\n");
		builder.append("Syntax " + this.getSyntaxTableVersion() + "\n");
		builder.append("Last Update " + this.getModificationDate().toXMLFormat() + "\n");
		return builder.toString();
	}
}