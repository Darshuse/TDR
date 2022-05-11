
package com.eastnets.notifier.domain;

import java.math.BigDecimal;
import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.Date;

public class Message {

	private String messageType;
	private String messageIdentifer;
	private BigDecimal amount;
	private String transactionReference;
	private String relatedReference;
	private String uterReferenceNumber;
	private String serviceTypeIdentifier;
	private LocalDateTime valueDate;
	private LocalDateTime mesgModifiedDateTime;
	private String messageStatus;
	private LocalDateTime receivedDate;
	private String senderBIC;
	private String recieverBIC;
	private String direction;
	private String mesgCreRpName;

	private String routingPointName;

	private String unitName;

	private String valueDateStr;

	private String amountStr;
	private Text text;

	// new fields to build RJE
	private String mesgReceiverSwiftAddress;// m.mesg_receiver_swift_address
	private String mesgSenderSwiftAddress;// m.mesg_sender_swift_address
	private String mesgNetworkPriority; // m.mesg_network_priority
	private String mesgNetworkDelvNotifReq; // m.mesg_network_delv_notif_req
	private String mesgDelvOverdueWarnReq; // m.mesg_delv_overdue_warn_req
	private String mesgNetworkObsoPeriod; // m.mesg_network_obso_period
	private Date mesgCreaDateTime; // m.mesg_crea_date_time
	private String mesgCopyServiceId; // m.mesg_copy_service_id
	private String mesgUserPriorityCode; // m.mesg_user_priority_code
	private String mesgUserReferenceText; // m.mesg_user_reference_text
	private String mesgReleaseInfo; // m.mesg_release_info
	private String mesgUserGroup; // m.mesg_mesg_user_group
	private String textSwiftBlock5; // t.text_swift_block_5
	private String mesgPossibleDupCreation;// m.mesg_possible_dup_creation
	private String mesgUserIssuedAsPde; // m.mesg_user_issued_as_pde

	private Appendix appendix;
	private Integer appeSessionNbr; // appe_session_nbr
	private Integer appeSequenceNbr; // appe_sequence_nbr
	private String appeSessionHolder;// appe_session_holder
	private Date appeLocalOutputTime;// appe_local_output_time
	private Date appeRemoteInputTime; // appe_remote_input_time
	private String appeRemoteInputReference; // appe_remote_input_reference
	private Clob appeAckNackText; // appe_ack_nack_text
	private String appeChecksumValue; // appe_checksum_value
	private String appeChecksumResult; // appe_checksum_result
	private Clob appeAuthValue; // appe_auth_value
	private String appeAuthResult; // appe_auth_result

	// this is for intervention data
	private String operatorNickName;
	private String instanceNumber;
	private String mergedText;
	private LocalDateTime interventionDateTime;
	private String intervintionNetworkStatus;

	private String instanceStatus;

	private Long sequenceNumber;

	// this is for instance data
	private String ququeStatus;

	// this is for appendix
	private String netwrokStatus;
	private String sourceEntity;
	private String appendixType;
	private String appendixIappName;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getRelatedReference() {
		return relatedReference;
	}

	public void setRelatedReference(String relatedReference) {
		this.relatedReference = relatedReference;
	}

	public String getUterReferenceNumber() {
		return uterReferenceNumber;
	}

	public void setUterReferenceNumber(String uterReferenceNumber) {
		this.uterReferenceNumber = uterReferenceNumber;
	}

	public String getServiceTypeIdentifier() {
		return serviceTypeIdentifier;
	}

	public void setServiceTypeIdentifier(String serviceTypeIdentifier) {
		this.serviceTypeIdentifier = serviceTypeIdentifier;
	}

	public LocalDateTime getValueDate() {
		return valueDate;
	}

	public void setValueDate(LocalDateTime valueDate) {
		this.valueDate = valueDate;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getSenderBIC() {
		return senderBIC;
	}

	public void setSenderBIC(String senderBIC) {
		this.senderBIC = senderBIC;
	}

	public String getRecieverBIC() {
		return recieverBIC;
	}

	public void setRecieverBIC(String recieverBIC) {
		this.recieverBIC = recieverBIC;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Text getText() {
		return text;
	}

	public String getMessageIdentifer() {
		return messageIdentifer;
	}

	public void setMessageIdentifer(String messageIdentifer) {
		this.messageIdentifer = messageIdentifer;
	}

	public void setText(Text text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", amount=" + amount + ", transactionReference=" + transactionReference + ", relatedReference=" + relatedReference + ", uterReferenceNumber=" + uterReferenceNumber + ", serviceTypeIdentifier="
				+ serviceTypeIdentifier + ", valueDate=" + valueDate + ", messageStatus=" + messageStatus + ", receivedDate=" + receivedDate + ", senderBIC=" + senderBIC + ", recieverBIC=" + recieverBIC + ", direction=" + direction + ", text=" + text
				+ "]";

	}

	public String getMesgCreRpName() {
		return mesgCreRpName;
	}

	public void setMesgCreRpName(String mesgCreRpName) {
		this.mesgCreRpName = mesgCreRpName;
	}

	public LocalDateTime getMesgModifiedDateTime() {
		return mesgModifiedDateTime;
	}

	public void setMesgModifiedDateTime(LocalDateTime mesgModifiedDateTime) {
		this.mesgModifiedDateTime = mesgModifiedDateTime;
	}

	public String getAmountStr() {
		return amountStr;
	}

	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}

	public String getValueDateStr() {
		return valueDateStr;
	}

	public void setValueDateStr(String valueDateStr) {
		this.valueDateStr = valueDateStr;
	}

	public String getMesgReceiverSwiftAddress() {
		return mesgReceiverSwiftAddress;
	}

	public void setMesgReceiverSwiftAddress(String mesgReceiverSwiftAddress) {
		this.mesgReceiverSwiftAddress = mesgReceiverSwiftAddress;
	}

	public String getMesgSenderSwiftAddress() {
		return mesgSenderSwiftAddress;
	}

	public void setMesgSenderSwiftAddress(String mesgSenderSwiftAddress) {
		this.mesgSenderSwiftAddress = mesgSenderSwiftAddress;
	}

	public String getMesgNetworkPriority() {
		return mesgNetworkPriority;
	}

	public void setMesgNetworkPriority(String mesgNetworkPriority) {
		this.mesgNetworkPriority = mesgNetworkPriority;
	}

	public String getMesgNetworkDelvNotifReq() {
		return mesgNetworkDelvNotifReq;
	}

	public void setMesgNetworkDelvNotifReq(String mesgNetworkDelvNotifReq) {
		this.mesgNetworkDelvNotifReq = mesgNetworkDelvNotifReq;
	}

	public String getMesgDelvOverdueWarnReq() {
		return mesgDelvOverdueWarnReq;
	}

	public void setMesgDelvOverdueWarnReq(String mesgDelvOverdueWarnReq) {
		this.mesgDelvOverdueWarnReq = mesgDelvOverdueWarnReq;
	}

	public String getMesgNetworkObsoPeriod() {
		return mesgNetworkObsoPeriod;
	}

	public void setMesgNetworkObsoPeriod(String mesgNetworkObsoPeriod) {
		this.mesgNetworkObsoPeriod = mesgNetworkObsoPeriod;
	}

	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}

	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}

	public String getMesgCopyServiceId() {
		return mesgCopyServiceId;
	}

	public void setMesgCopyServiceId(String mesgCopyServiceId) {
		this.mesgCopyServiceId = mesgCopyServiceId;
	}

	public String getMesgUserPriorityCode() {
		return mesgUserPriorityCode;
	}

	public void setMesgUserPriorityCode(String mesgUserPriorityCode) {
		this.mesgUserPriorityCode = mesgUserPriorityCode;
	}

	public String getMesgUserReferenceText() {
		return mesgUserReferenceText;
	}

	public void setMesgUserReferenceText(String mesgUserReferenceText) {
		this.mesgUserReferenceText = mesgUserReferenceText;
	}

	public String getMesgReleaseInfo() {
		return mesgReleaseInfo;
	}

	public void setMesgReleaseInfo(String mesgReleaseInfo) {
		this.mesgReleaseInfo = mesgReleaseInfo;
	}

	public String getMesgUserGroup() {
		return mesgUserGroup;
	}

	public void setMesgUserGroup(String mesgUserGroup) {
		this.mesgUserGroup = mesgUserGroup;
	}

	public String getTextSwiftBlock5() {
		return textSwiftBlock5;
	}

	public void setTextSwiftBlock5(String textSwiftBlock5) {
		this.textSwiftBlock5 = textSwiftBlock5;
	}

	public String getMesgPossibleDupCreation() {
		return mesgPossibleDupCreation;
	}

	public void setMesgPossibleDupCreation(String mesgPossibleDupCreation) {
		this.mesgPossibleDupCreation = mesgPossibleDupCreation;
	}

	public String getMesgUserIssuedAsPde() {
		return mesgUserIssuedAsPde;
	}

	public void setMesgUserIssuedAsPde(String mesgUserIssuedAsPde) {
		this.mesgUserIssuedAsPde = mesgUserIssuedAsPde;
	}

	public String getRoutingPointName() {
		return routingPointName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public void setRoutingPointName(String routingPointName) {
		this.routingPointName = routingPointName;
	}

	public Integer getAppeSessionNbr() {
		return appeSessionNbr;
	}

	public void setAppeSessionNbr(Integer appeSessionNbr) {
		this.appeSessionNbr = appeSessionNbr;
	}

	public Integer getAppeSequenceNbr() {
		return appeSequenceNbr;
	}

	public void setAppeSequenceNbr(Integer appeSequenceNbr) {
		this.appeSequenceNbr = appeSequenceNbr;
	}

	public String getAppeSessionHolder() {
		return appeSessionHolder;
	}

	public void setAppeSessionHolder(String appeSessionHolder) {
		this.appeSessionHolder = appeSessionHolder;
	}

	public Date getAppeLocalOutputTime() {
		return appeLocalOutputTime;
	}

	public void setAppeLocalOutputTime(Date appeLocalOutputTime) {
		this.appeLocalOutputTime = appeLocalOutputTime;
	}

	public Date getAppeRemoteInputTime() {
		return appeRemoteInputTime;
	}

	public void setAppeRemoteInputTime(Date appeRemoteInputTime) {
		this.appeRemoteInputTime = appeRemoteInputTime;
	}

	public String getAppeRemoteInputReference() {
		return appeRemoteInputReference;
	}

	public void setAppeRemoteInputReference(String appeRemoteInputReference) {
		this.appeRemoteInputReference = appeRemoteInputReference;
	}

	public Clob getAppeAckNackText() {
		return appeAckNackText;
	}

	public void setAppeAckNackText(Clob appeAckNackText) {
		this.appeAckNackText = appeAckNackText;
	}

	public String getAppeChecksumValue() {
		return appeChecksumValue;
	}

	public void setAppeChecksumValue(String appeChecksumValue) {
		this.appeChecksumValue = appeChecksumValue;
	}

	public String getAppeChecksumResult() {
		return appeChecksumResult;
	}

	public void setAppeChecksumResult(String appeChecksumResult) {
		this.appeChecksumResult = appeChecksumResult;
	}

	public Clob getAppeAuthValue() {
		return appeAuthValue;
	}

	public void setAppeAuthValue(Clob appeAuthValue) {
		this.appeAuthValue = appeAuthValue;
	}

	public String getAppeAuthResult() {
		return appeAuthResult;
	}

	public void setAppeAuthResult(String appeAuthResult) {
		this.appeAuthResult = appeAuthResult;
	}

	public String getOperatorNickName() {
		return operatorNickName;
	}

	public void setOperatorNickName(String operatorNickName) {
		this.operatorNickName = operatorNickName;
	}

	public String getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(String instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String getMergedText() {
		return mergedText;
	}

	public void setMergedText(String mergedText) {
		this.mergedText = mergedText;
	}

	public LocalDateTime getInterventionDateTime() {
		return interventionDateTime;
	}

	public void setInterventionDateTime(LocalDateTime interventionDateTime) {
		this.interventionDateTime = interventionDateTime;
	}

	public String getInstanceStatus() {
		return instanceStatus;
	}

	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getQuqueStatus() {
		return ququeStatus;
	}

	public void setQuqueStatus(String ququeStatus) {
		this.ququeStatus = ququeStatus;
	}

	public String getNetwrokStatus() {
		return netwrokStatus;
	}

	public void setNetwrokStatus(String netwrokStatus) {
		this.netwrokStatus = netwrokStatus;
	}

	public String getSourceEntity() {
		return sourceEntity;
	}

	public void setSourceEntity(String sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	public String getAppendixType() {
		return appendixType;
	}

	public void setAppendixType(String appendixType) {
		this.appendixType = appendixType;
	}

	public String getAppendixIappName() {
		return appendixIappName;
	}

	public void setAppendixIappName(String appendixIappName) {
		this.appendixIappName = appendixIappName;
	}

	public String getIntervintionNetworkStatus() {
		return intervintionNetworkStatus;
	}

	public void setIntervintionNetworkStatus(String intervintionNetworkStatus) {
		this.intervintionNetworkStatus = intervintionNetworkStatus;

	}

}
