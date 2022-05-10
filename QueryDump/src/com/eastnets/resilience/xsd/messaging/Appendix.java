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
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.eastnets.resilience.xmldump.utils.StringUtils;
import com.eastnets.resilience.xsd.BaseObject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Appendix", propOrder = { "identifier", "integratedApplication", "type", "sessionHolder",
		"sessionNumber", "sequenceNumber", "transmissionNumber", "creatingApplication", "creatingMpfn",
		"creatingRoutingPoint", "connectionResponseCode", "connectionResponseText", "clientReference",
		"checksumResult", "checksumValue", "authenticationResult", "authenticationValue",
		"proprietaryAuthenticationResult", "proprietaryAuthenticationValue", "lauResult", "ackNackLauResult",
		"receiverDeliveryStatus", "networkDeliveryStatus", "ackNackText", "nackReason", "senderCancelationStatus",
		"remoteInputReference", "remoteInputTime", "localOutputTime", "pkiAuthenticationValue",
		"pkiCombinedAuthenticationResult", "pkiProprietaryAuthenticationValue", "pkiProprietaryAuthenticationResult",
		"pkiAuthorisationResult", "pkiAuthenticationResult", "combinedAuthenticationResult",
		"combinedProprietaryAuthenticationResult", "rmaCheckResult", "crestGatewayId", "crestComServerId",
		"senderSwiftAddress", "swiftReference", "swiftRequestReference", "nonRepudiation", "nonRepudiationType",
		"nonRepudiationWarning", "authoriserDn", "snlEndpoint", "snfQueueName", "signerDn", "snfInputTime",
		"storeAndForwardInputTime", "swiftResponseReference", "responseReference", "responseNonRepudiationType",
		"responseNonRepudiationWarning", "responseCbtReference", "responsePossibleDuplicateCreation",
		"responseResponderDn", "mvalResult", "responseMvalResult", "responseAuthenticationValue",
		"responseAuthenticationResult", "pdmHistory", "responseSignerDn", "responsePayloadAttributeName",
		"responsePayloadAttributeValue", "snfDeliveryNotificationRequested", "usePkiSignature", "copyInfo",
		"transferReference", "storedTransferReference", "originalSnfTransferReference",
		"authorisationDeliveryNotificationRequested", "authorisationDeliveryNotificationReceiverDn",
		"authorisationDeliveryNotificationRequestType", "deliveryTime", "creationTime", "fileTransferStartTime",
		"fileTransferEndTime", "thirdPartySignerDn", "copiedThirdPartyList", "skippedThirdPartyList", "senderBic8" })
public class Appendix extends BaseObject {

	@XmlElement(name = "Identifier")
	protected AppendixIdentifier identifier;

	@XmlElement(name = "IntegratedApplication", required = true)
	protected String integratedApplication;

	@XmlElement(name = "Type", required = true)
	protected AppendixType type;

	@XmlElement(name = "SessionHolder", required = true)
	protected String sessionHolder;

	@XmlElement(name = "SessionNumber", required = true)
	protected String sessionNumber;

	@XmlElement(name = "SequenceNumber", required = true)
	protected String sequenceNumber;

	@XmlElement(name = "TransmissionNumber")
	protected int transmissionNumber;

	@XmlElement(name = "CreatingApplication")
	protected String creatingApplication;

	@XmlElement(name = "CreatingMpfn")
	protected String creatingMpfn;

	@XmlElement(name = "CreatingRoutingPoint")
	protected String creatingRoutingPoint;

	@XmlElement(name = "ConnectionResponseCode")
	protected ConnectionResponseCode connectionResponseCode;

	@XmlElement(name = "ConnectionResponseText")
	protected String connectionResponseText;

	@XmlElement(name = "ClientReference")
	protected String clientReference;

	@XmlElement(name = "ChecksumResult")
	protected ChecksumResult checksumResult;

	@XmlElement(name = "ChecksumValue")
	protected String checksumValue;

	@XmlElement(name = "AuthenticationResult")
	protected AuthenticationResult authenticationResult;

	@XmlElement(name = "AuthenticationValue")
	protected String authenticationValue;

	@XmlElement(name = "ProprietaryAuthenticationResult")
	protected AuthenticationResult proprietaryAuthenticationResult;

	@XmlElement(name = "ProprietaryAuthenticationValue")
	protected String proprietaryAuthenticationValue;

	@XmlElement(name = "LauResult")
	protected LauResult lauResult;

	@XmlElement(name = "AckNackLauResult")
	protected LauResult ackNackLauResult;

	@XmlElement(name = "ReceiverDeliveryStatus")
	protected DeliveryStatus receiverDeliveryStatus;

	@XmlElement(name = "NetworkDeliveryStatus")
	protected NetworkDeliveryStatus networkDeliveryStatus;

	@XmlElement(name = "AckNackText")
	protected String ackNackText;

	@XmlElement(name = "NackReason")
	protected String nackReason;

	@XmlElement(name = "SenderCancelationStatus")
	protected SenderCancelationStatus senderCancelationStatus;

	@XmlElement(name = "RemoteInputReference")
	protected String remoteInputReference;

	@XmlElement(name = "RemoteInputTime")
	protected XMLGregorianCalendar remoteInputTime;

	@XmlElement(name = "LocalOutputTime")
	protected XMLGregorianCalendar localOutputTime;

	@XmlElement(name = "PkiAuthenticationValue")
	protected String pkiAuthenticationValue;

	@XmlElement(name = "PkiCombinedAuthenticationResult")
	protected AuthenticationResult pkiCombinedAuthenticationResult;

	@XmlElement(name = "PkiProprietaryAuthenticationValue")
	protected String pkiProprietaryAuthenticationValue;

	@XmlElement(name = "PkiProprietaryAuthenticationResult")
	protected AuthenticationResult pkiProprietaryAuthenticationResult;

	@XmlElement(name = "PkiAuthorisationResult")
	protected AuthenticationResult pkiAuthorisationResult;

	@XmlElement(name = "PkiAuthenticationResult")
	protected AuthenticationResult pkiAuthenticationResult;

	@XmlElement(name = "CombinedAuthenticationResult")
	protected AuthenticationResult combinedAuthenticationResult;

	@XmlElement(name = "CombinedProprietaryAuthenticationResult")
	protected AuthenticationResult combinedProprietaryAuthenticationResult;

	@XmlElement(name = "RmaCheckResult")
	protected RmaCheckResult rmaCheckResult;

	@XmlElement(name = "CrestGatewayId")
	protected String crestGatewayId;

	@XmlElement(name = "CrestComServerId")
	protected String crestComServerId;

	@XmlElement(name = "SenderSwiftAddress")
	protected String senderSwiftAddress;

	@XmlElement(name = "SwiftReference")
	protected String swiftReference;

	@XmlElement(name = "SwiftRequestReference")
	protected String swiftRequestReference;

	@XmlElement(name = "NonRepudiation")
	protected Boolean nonRepudiation;

	@XmlElement(name = "NonRepudiationType")
	protected NonRepudiationType nonRepudiationType;

	@XmlElement(name = "NonRepudiationWarning")
	protected String nonRepudiationWarning;

	@XmlElement(name = "AuthoriserDn")
	protected String authoriserDn;

	@XmlElement(name = "SnlEndpoint")
	protected String snlEndpoint;

	@XmlElement(name = "SnfQueueName")
	protected String snfQueueName;

	@XmlElement(name = "SignerDn")
	protected String signerDn;

	@XmlElement(name = "SnfInputTime")
	protected XMLGregorianCalendar snfInputTime;

	@XmlElement(name = "StoreAndForwardInputTime")
	protected String storeAndForwardInputTime;

	@XmlElement(name = "SwiftResponseReference")
	protected String swiftResponseReference;

	@XmlElement(name = "ResponseReference")
	protected String responseReference;

	@XmlElement(name = "ResponseNonRepudiationType")
	protected NonRepudiationType responseNonRepudiationType;

	@XmlElement(name = "ResponseNonRepudiationWarning")
	protected String responseNonRepudiationWarning;

	@XmlElement(name = "ResponseCbtReference")
	protected String responseCbtReference;

	@XmlElement(name = "ResponsePossibleDuplicateCreation")
	protected PossibleDuplicateCreation responsePossibleDuplicateCreation;

	@XmlElement(name = "ResponseResponderDn")
	protected String responseResponderDn;

	@XmlElement(name = "MvalResult")
	protected String mvalResult;

	@XmlElement(name = "ResponseMvalResult")
	protected String responseMvalResult;

	@XmlElement(name = "ResponseAuthenticationValue")
	protected String responseAuthenticationValue;

	@XmlElement(name = "ResponseAuthenticationResult")
	protected AuthenticationResult responseAuthenticationResult;

	@XmlElement(name = "PdmHistory")
	protected String pdmHistory;

	@XmlElement(name = "ResponseSignerDn")
	protected String responseSignerDn;

	@XmlElement(name = "ResponsePayloadAttributeName")
	protected List<String> responsePayloadAttributeName;

	@XmlElement(name = "ResponsePayloadAttributeValue")
	protected List<String> responsePayloadAttributeValue;

	@XmlElement(name = "SnfDeliveryNotificationRequested")
	protected Boolean snfDeliveryNotificationRequested;

	@XmlElement(name = "UsePkiSignature")
	protected Boolean usePkiSignature;

	@XmlElement(name = "CopyInfo")
	protected String copyInfo;

	@XmlElement(name = "TransferReference")
	protected String transferReference;

	@XmlElement(name = "StoredTransferReference")
	protected String storedTransferReference;

	@XmlElement(name = "OriginalSnfTransferReference")
	protected String originalSnfTransferReference;

	@XmlElement(name = "AuthorisationDeliveryNotificationRequested")
	protected Boolean authorisationDeliveryNotificationRequested;

	@XmlElement(name = "AuthorisationDeliveryNotificationReceiverDn")
	protected String authorisationDeliveryNotificationReceiverDn;

	@XmlElement(name = "AuthorisationDeliveryNotificationRequestType")
	protected String authorisationDeliveryNotificationRequestType;

	@XmlElement(name = "DeliveryTime")
	protected String deliveryTime;

	@XmlElement(name = "CreationTime")
	protected String creationTime;

	@XmlElement(name = "FileTransferStartTime")
	protected XMLGregorianCalendar fileTransferStartTime;

	@XmlElement(name = "FileTransferEndTime")
	protected XMLGregorianCalendar fileTransferEndTime;

	@XmlElement(name = "ThirdPartySignerDn")
	protected String thirdPartySignerDn;

	@XmlElement(name = "CopiedThirdPartyList")
	protected List<String> copiedThirdPartyList;

	@XmlElement(name = "SkippedThirdPartyList")
	protected List<String> skippedThirdPartyList;

	@XmlElement(name = "SenderBic8")
	protected String senderBic8;

	public AppendixIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(AppendixIdentifier paramAppendixIdentifier) {
		this.identifier = paramAppendixIdentifier;
	}

	public String getIntegratedApplication() {
		return this.integratedApplication;
	}

	public void setIntegratedApplication(String paramString) {
		this.integratedApplication = paramString;
	}

	public AppendixType getType() {
		return this.type;
	}

	public void setType(AppendixType paramAppendixType) {
		this.type = paramAppendixType;
	}

	public String getSessionHolder() {
		return this.sessionHolder;
	}

	public void setSessionHolder(String paramString) {
		this.sessionHolder = paramString;
	}

	public String getSessionNumber() {
		return this.sessionNumber;
	}

	public void setSessionNumber(String paramString) {
		this.sessionNumber = paramString;
	}

	public String getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(String paramString) {
		this.sequenceNumber = paramString;
	}

	public int getTransmissionNumber() {
		return this.transmissionNumber;
	}

	public void setTransmissionNumber(int paramInt) {
		this.transmissionNumber = paramInt;
	}

	public String getCreatingApplication() {
		return this.creatingApplication;
	}

	public void setCreatingApplication(String paramString) {
		this.creatingApplication = paramString;
	}

	public String getCreatingMpfn() {
		return this.creatingMpfn;
	}

	public void setCreatingMpfn(String paramString) {
		this.creatingMpfn = paramString;
	}

	public String getCreatingRoutingPoint() {
		return this.creatingRoutingPoint;
	}

	public void setCreatingRoutingPoint(String paramString) {
		this.creatingRoutingPoint = paramString;
	}

	public ConnectionResponseCode getConnectionResponseCode() {
		return this.connectionResponseCode;
	}

	public void setConnectionResponseCode(ConnectionResponseCode paramConnectionResponseCode) {
		this.connectionResponseCode = paramConnectionResponseCode;
	}

	public String getConnectionResponseText() {
		return this.connectionResponseText;
	}

	public void setConnectionResponseText(String paramString) {
		this.connectionResponseText = paramString;
	}

	public String getClientReference() {
		return this.clientReference;
	}

	public void setClientReference(String paramString) {
		this.clientReference = paramString;
	}

	public ChecksumResult getChecksumResult() {
		return this.checksumResult;
	}

	public void setChecksumResult(ChecksumResult paramChecksumResult) {
		this.checksumResult = paramChecksumResult;
	}

	public String getChecksumValue() {
		return this.checksumValue;
	}

	public void setChecksumValue(String paramString) {
		this.checksumValue = paramString;
	}

	public AuthenticationResult getAuthenticationResult() {
		return this.authenticationResult;
	}

	public void setAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.authenticationResult = paramAuthenticationResult;
	}

	public String getAuthenticationValue() {
		return this.authenticationValue;
	}

	public void setAuthenticationValue(String paramString) {
		this.authenticationValue = paramString;
	}

	public AuthenticationResult getProprietaryAuthenticationResult() {
		return this.proprietaryAuthenticationResult;
	}

	public void setProprietaryAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.proprietaryAuthenticationResult = paramAuthenticationResult;
	}

	public String getProprietaryAuthenticationValue() {
		return this.proprietaryAuthenticationValue;
	}

	public void setProprietaryAuthenticationValue(String paramString) {
		this.proprietaryAuthenticationValue = paramString;
	}

	public LauResult getLauResult() {
		return this.lauResult;
	}

	public void setLauResult(LauResult paramLauResult) {
		this.lauResult = paramLauResult;
	}

	public LauResult getAckNackLauResult() {
		return this.ackNackLauResult;
	}

	public void setAckNackLauResult(LauResult paramLauResult) {
		this.ackNackLauResult = paramLauResult;
	}

	public DeliveryStatus getReceiverDeliveryStatus() {
		return this.receiverDeliveryStatus;
	}

	public void setReceiverDeliveryStatus(DeliveryStatus paramDeliveryStatus) {
		this.receiverDeliveryStatus = paramDeliveryStatus;
	}

	public NetworkDeliveryStatus getNetworkDeliveryStatus() {
		return this.networkDeliveryStatus;
	}

	public void setNetworkDeliveryStatus(NetworkDeliveryStatus paramNetworkDeliveryStatus) {
		this.networkDeliveryStatus = paramNetworkDeliveryStatus;
	}

	public String getAckNackText() {
		return this.ackNackText;
	}

	public void setAckNackText(String paramString) {
		this.ackNackText = paramString;
	}

	public String getNackReason() {
		return this.nackReason;
	}

	public void setNackReason(String paramString) {
		this.nackReason = paramString;
	}

	public SenderCancelationStatus getSenderCancelationStatus() {
		return this.senderCancelationStatus;
	}

	public void setSenderCancelationStatus(SenderCancelationStatus paramSenderCancelationStatus) {
		this.senderCancelationStatus = paramSenderCancelationStatus;
	}

	public String getRemoteInputReference() {
		return this.remoteInputReference;
	}

	public void setRemoteInputReference(String paramString) {
		this.remoteInputReference = paramString;
	}

	public XMLGregorianCalendar getRemoteInputTime() {
		return this.remoteInputTime;
	}

	public void setRemoteInputTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.remoteInputTime = paramXMLGregorianCalendar;
	}

	public XMLGregorianCalendar getLocalOutputTime() {
		return this.localOutputTime;
	}

	public void setLocalOutputTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.localOutputTime = paramXMLGregorianCalendar;
	}

	public String getPkiAuthenticationValue() {
		return this.pkiAuthenticationValue;
	}

	public void setPkiAuthenticationValue(String paramString) {
		this.pkiAuthenticationValue = paramString;
	}

	public AuthenticationResult getPkiCombinedAuthenticationResult() {
		return this.pkiCombinedAuthenticationResult;
	}

	public void setPkiCombinedAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.pkiCombinedAuthenticationResult = paramAuthenticationResult;
	}

	public String getPkiProprietaryAuthenticationValue() {
		return this.pkiProprietaryAuthenticationValue;
	}

	public void setPkiProprietaryAuthenticationValue(String paramString) {
		this.pkiProprietaryAuthenticationValue = paramString;
	}

	public AuthenticationResult getPkiProprietaryAuthenticationResult() {
		return this.pkiProprietaryAuthenticationResult;
	}

	public void setPkiProprietaryAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.pkiProprietaryAuthenticationResult = paramAuthenticationResult;
	}

	public AuthenticationResult getPkiAuthorisationResult() {
		return this.pkiAuthorisationResult;
	}

	public void setPkiAuthorisationResult(AuthenticationResult paramAuthenticationResult) {
		this.pkiAuthorisationResult = paramAuthenticationResult;
	}

	public AuthenticationResult getPkiAuthenticationResult() {
		return this.pkiAuthenticationResult;
	}

	public void setPkiAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.pkiAuthenticationResult = paramAuthenticationResult;
	}

	public AuthenticationResult getCombinedAuthenticationResult() {
		return this.combinedAuthenticationResult;
	}

	public void setCombinedAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.combinedAuthenticationResult = paramAuthenticationResult;
	}

	public AuthenticationResult getCombinedProprietaryAuthenticationResult() {
		return this.combinedProprietaryAuthenticationResult;
	}

	public void setCombinedProprietaryAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.combinedProprietaryAuthenticationResult = paramAuthenticationResult;
	}

	public RmaCheckResult getRmaCheckResult() {
		return this.rmaCheckResult;
	}

	public void setRmaCheckResult(RmaCheckResult paramRmaCheckResult) {
		this.rmaCheckResult = paramRmaCheckResult;
	}

	public String getCrestGatewayId() {
		return this.crestGatewayId;
	}

	public void setCrestGatewayId(String paramString) {
		this.crestGatewayId = paramString;
	}

	public String getCrestComServerId() {
		return this.crestComServerId;
	}

	public void setCrestComServerId(String paramString) {
		this.crestComServerId = paramString;
	}

	public String getSenderSwiftAddress() {
		return this.senderSwiftAddress;
	}

	public void setSenderSwiftAddress(String paramString) {
		this.senderSwiftAddress = paramString;
	}

	public String getSwiftReference() {
		return this.swiftReference;
	}

	public void setSwiftReference(String paramString) {
		this.swiftReference = paramString;
	}

	public String getSwiftRequestReference() {
		return this.swiftRequestReference;
	}

	public void setSwiftRequestReference(String paramString) {
		this.swiftRequestReference = paramString;
	}

	public Boolean isNonRepudiation() {
		return this.nonRepudiation;
	}

	public void setNonRepudiation(Boolean paramBoolean) {
		this.nonRepudiation = paramBoolean;
	}

	public NonRepudiationType getNonRepudiationType() {
		return this.nonRepudiationType;
	}

	public void setNonRepudiationType(NonRepudiationType paramNonRepudiationType) {
		this.nonRepudiationType = paramNonRepudiationType;
	}

	public String getNonRepudiationWarning() {
		return this.nonRepudiationWarning;
	}

	public void setNonRepudiationWarning(String paramString) {
		this.nonRepudiationWarning = paramString;
	}

	public String getAuthoriserDn() {
		return this.authoriserDn;
	}

	public void setAuthoriserDn(String paramString) {
		this.authoriserDn = paramString;
	}

	public String getSnlEndpoint() {
		return this.snlEndpoint;
	}

	public void setSnlEndpoint(String paramString) {
		this.snlEndpoint = paramString;
	}

	public String getSnfQueueName() {
		return this.snfQueueName;
	}

	public void setSnfQueueName(String paramString) {
		this.snfQueueName = paramString;
	}

	public String getSignerDn() {
		return this.signerDn;
	}

	public void setSignerDn(String paramString) {
		this.signerDn = paramString;
	}

	public XMLGregorianCalendar getSnfInputTime() {
		return this.snfInputTime;
	}

	public void setSnfInputTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.snfInputTime = paramXMLGregorianCalendar;
	}

	public String getStoreAndForwardInputTime() {
		return this.storeAndForwardInputTime;
	}

	public void setStoreAndForwardInputTime(String paramString) {
		this.storeAndForwardInputTime = paramString;
	}

	public String getSwiftResponseReference() {
		return this.swiftResponseReference;
	}

	public void setSwiftResponseReference(String paramString) {
		this.swiftResponseReference = paramString;
	}

	public String getResponseReference() {
		return this.responseReference;
	}

	public void setResponseReference(String paramString) {
		this.responseReference = paramString;
	}

	public NonRepudiationType getResponseNonRepudiationType() {
		return this.responseNonRepudiationType;
	}

	public void setResponseNonRepudiationType(NonRepudiationType paramNonRepudiationType) {
		this.responseNonRepudiationType = paramNonRepudiationType;
	}

	public String getResponseNonRepudiationWarning() {
		return this.responseNonRepudiationWarning;
	}

	public void setResponseNonRepudiationWarning(String paramString) {
		this.responseNonRepudiationWarning = paramString;
	}

	public String getResponseCbtReference() {
		return this.responseCbtReference;
	}

	public void setResponseCbtReference(String paramString) {
		this.responseCbtReference = paramString;
	}

	public PossibleDuplicateCreation getResponsePossibleDuplicateCreation() {
		return this.responsePossibleDuplicateCreation;
	}

	public void setResponsePossibleDuplicateCreation(PossibleDuplicateCreation paramPossibleDuplicateCreation) {
		this.responsePossibleDuplicateCreation = paramPossibleDuplicateCreation;
	}

	public String getResponseResponderDn() {
		return this.responseResponderDn;
	}

	public void setResponseResponderDn(String paramString) {
		this.responseResponderDn = paramString;
	}

	public String getMvalResult() {
		return this.mvalResult;
	}

	public void setMvalResult(String paramString) {
		this.mvalResult = paramString;
	}

	public String getResponseMvalResult() {
		return this.responseMvalResult;
	}

	public void setResponseMvalResult(String paramString) {
		this.responseMvalResult = paramString;
	}

	public String getResponseAuthenticationValue() {
		return this.responseAuthenticationValue;
	}

	public void setResponseAuthenticationValue(String paramString) {
		this.responseAuthenticationValue = paramString;
	}

	public AuthenticationResult getResponseAuthenticationResult() {
		return this.responseAuthenticationResult;
	}

	public void setResponseAuthenticationResult(AuthenticationResult paramAuthenticationResult) {
		this.responseAuthenticationResult = paramAuthenticationResult;
	}

	public String getPdmHistory() {
		return this.pdmHistory;
	}

	public void setPdmHistory(String paramString) {
		this.pdmHistory = paramString;
	}

	public String getResponseSignerDn() {
		return this.responseSignerDn;
	}

	public void setResponseSignerDn(String paramString) {
		this.responseSignerDn = paramString;
	}

	public List<String> getResponsePayloadAttributeName() {
		if (this.responsePayloadAttributeName == null)
			this.responsePayloadAttributeName = new ArrayList<String>();
		return this.responsePayloadAttributeName;
	}

	public List<String> getResponsePayloadAttributeValue() {
		if (this.responsePayloadAttributeValue == null)
			this.responsePayloadAttributeValue = new ArrayList<String>();
		return this.responsePayloadAttributeValue;
	}

	public Boolean isSnfDeliveryNotificationRequested() {
		return this.snfDeliveryNotificationRequested;
	}

	public void setSnfDeliveryNotificationRequested(Boolean paramBoolean) {
		this.snfDeliveryNotificationRequested = paramBoolean;
	}

	public Boolean isUsePkiSignature() {
		return this.usePkiSignature;
	}

	public void setUsePkiSignature(Boolean paramBoolean) {
		this.usePkiSignature = paramBoolean;
	}

	public String getCopyInfo() {
		return this.copyInfo;
	}

	public void setCopyInfo(String paramString) {
		this.copyInfo = paramString;
	}

	public String getTransferReference() {
		return this.transferReference;
	}

	public void setTransferReference(String paramString) {
		this.transferReference = paramString;
	}

	public String getStoredTransferReference() {
		return this.storedTransferReference;
	}

	public void setStoredTransferReference(String paramString) {
		this.storedTransferReference = paramString;
	}

	public String getOriginalSnfTransferReference() {
		return this.originalSnfTransferReference;
	}

	public void setOriginalSnfTransferReference(String paramString) {
		this.originalSnfTransferReference = paramString;
	}

	public Boolean isAuthorisationDeliveryNotificationRequested() {
		return this.authorisationDeliveryNotificationRequested;
	}

	public void setAuthorisationDeliveryNotificationRequested(Boolean paramBoolean) {
		this.authorisationDeliveryNotificationRequested = paramBoolean;
	}

	public String getAuthorisationDeliveryNotificationReceiverDn() {
		return this.authorisationDeliveryNotificationReceiverDn;
	}

	public void setAuthorisationDeliveryNotificationReceiverDn(String paramString) {
		this.authorisationDeliveryNotificationReceiverDn = paramString;
	}

	public String getAuthorisationDeliveryNotificationRequestType() {
		return this.authorisationDeliveryNotificationRequestType;
	}

	public void setAuthorisationDeliveryNotificationRequestType(String paramString) {
		this.authorisationDeliveryNotificationRequestType = paramString;
	}

	public String getDeliveryTime() {
		return this.deliveryTime;
	}

	public void setDeliveryTime(String paramString) {
		this.deliveryTime = paramString;
	}

	public String getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(String paramString) {
		this.creationTime = paramString;
	}

	public XMLGregorianCalendar getFileTransferStartTime() {
		return this.fileTransferStartTime;
	}

	public void setFileTransferStartTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.fileTransferStartTime = paramXMLGregorianCalendar;
	}

	public XMLGregorianCalendar getFileTransferEndTime() {
		return this.fileTransferEndTime;
	}

	public void setFileTransferEndTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.fileTransferEndTime = paramXMLGregorianCalendar;
	}

	public String getThirdPartySignerDn() {
		return this.thirdPartySignerDn;
	}

	public void setThirdPartySignerDn(String paramString) {
		this.thirdPartySignerDn = paramString;
	}

	public List<String> getCopiedThirdPartyList() {
		if (this.copiedThirdPartyList == null)
			this.copiedThirdPartyList = new ArrayList<String>();
		return this.copiedThirdPartyList;
	}

	public List<String> getSkippedThirdPartyList() {
		if (this.skippedThirdPartyList == null)
			this.skippedThirdPartyList = new ArrayList<String>();
		return this.skippedThirdPartyList;
	}

	public String getSenderBic8() {
		return this.senderBic8;
	}

	public void setSenderBic8(String paramString) {
		this.senderBic8 = paramString;
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
		builder.append("UmidL " + this.getUmidL() + "\n");
		builder.append("UmidH " + this.getUmidH() + "\n");
		builder.append("Created  " + this.getCreationTime() + "\n");
		builder.append("Inst Num  " + this.getIdentifier().getInstanceNumber() + "\n");
		builder.append("Int Seq  " + this.getIdentifier().getInternalSequenceNumber() + "\n");
		builder.append("Seq  " + this.getSequenceNumber() + "\n");
		builder.append("IAPP  " + this.getIntegratedApplication() + "\n");
		builder.append("type  " + this.getType() + "\n");
		return builder.toString();
	}
}
