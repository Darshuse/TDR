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
@XmlType(name = "Instance", propOrder = { "identifier", "type", "notificationType", "status", "relatedNumber",
		"appendixDateTime", "appendixSequenceNumber", "unitName", "routingPoint", "originalRoutingPoint", "mpfnName",
		"mpfnHandle", "processState", "lastMpfnResult", "relativeReference", "priority", "deferredTime",
		"creationApplication", "creationMpfnName", "creationRoutingPoint", "creationDate", "initialTargetRoutingPoint",
		"authoriserOperator", "lastOperator", "inQueueSince", "applicationRoutingPoint", "operatorComment",
		"receiverCorrespondentType", "receiverCorrespondentInstitutionName", "receiverCorrespondentDepartment",
		"receiverCorrespondentLastName", "receiverCorrespondentFirstName", "receiverInstitutionName",
		"receiverBranchInfo", "receiverLocation", "receiverCityName", "receiverCountryCode", "receiverCountryName",
		"routingCode", "dispositionAddressCode", "receiverDeliveryStatus", "receiverNetworkIntegratedApplicationName",
		"receiverSecurityIntegratedApplicationName", "interventionDateTime", "interventionSequenceNumber",
		"crestGatewayId", "crestComServerId", "responderDn", "nonRepudiationIndicator", "cbtReference", "deliveryMode",
		"intervention", "appendix" })
public class Instance extends BaseObject {

	@XmlElement(name = "Identifier")
	protected InstanceIdentifier identifier;

	@XmlElement(name = "Type", required = true)
	protected InstanceType type;

	@XmlElement(name = "NotificationType")
	protected NotificationType notificationType;

	@XmlElement(name = "Status")
	protected Status status;

	@XmlElement(name = "RelatedNumber")
	protected int relatedNumber;

	@XmlElement(name = "AppendixDateTime")
	protected XMLGregorianCalendar appendixDateTime;

	@XmlElement(name = "AppendixSequenceNumber")
	protected int appendixSequenceNumber;

	@XmlElement(name = "UnitName", required = true)
	protected String unitName;

	@XmlElement(name = "RoutingPoint")
	protected String routingPoint;

	@XmlElement(name = "OriginalRoutingPoint")
	protected String originalRoutingPoint;

	@XmlElement(name = "MpfnName")
	protected String mpfnName;

	@XmlElement(name = "MpfnHandle")
	protected String mpfnHandle;

	@XmlElement(name = "ProcessState")
	protected int processState;

	@XmlElement(name = "LastMpfnResult", required = true)
	protected /*MpfnResult*/String lastMpfnResult;

	@XmlElement(name = "RelativeReference")
	protected int relativeReference;

	@XmlElement(name = "Priority")
	protected int priority;

	@XmlElement(name = "DeferredTime")
	protected XMLGregorianCalendar deferredTime;

	@XmlElement(name = "CreationApplication")
	protected String creationApplication;

	@XmlElement(name = "CreationMpfnName")
	protected String creationMpfnName;

	@XmlElement(name = "CreationRoutingPoint")
	protected String creationRoutingPoint;

	@XmlElement(name = "CreationDate")
	protected XMLGregorianCalendar creationDate;

	@XmlElement(name = "InitialTargetRoutingPoint")
	protected String initialTargetRoutingPoint;

	@XmlElement(name = "AuthoriserOperator")
	protected String authoriserOperator;

	@XmlElement(name = "LastOperator")
	protected String lastOperator;

	@XmlElement(name = "InQueueSince")
	protected XMLGregorianCalendar inQueueSince;

	@XmlElement(name = "ApplicationRoutingPoint")
	protected String applicationRoutingPoint;

	@XmlElement(name = "OperatorComment")
	protected String operatorComment;

	@XmlElement(name = "ReceiverCorrespondentType")
	protected CorrespondentType receiverCorrespondentType;

	@XmlElement(name = "ReceiverCorrespondentInstitutionName")
	protected String receiverCorrespondentInstitutionName;

	@XmlElement(name = "ReceiverCorrespondentDepartment")
	protected String receiverCorrespondentDepartment;

	@XmlElement(name = "ReceiverCorrespondentLastName")
	protected String receiverCorrespondentLastName;

	@XmlElement(name = "ReceiverCorrespondentFirstName")
	protected String receiverCorrespondentFirstName;

	@XmlElement(name = "ReceiverInstitutionName")
	protected String receiverInstitutionName;

	@XmlElement(name = "ReceiverBranchInfo")
	protected String receiverBranchInfo;

	@XmlElement(name = "ReceiverLocation")
	protected String receiverLocation;

	@XmlElement(name = "ReceiverCityName")
	protected String receiverCityName;

	@XmlElement(name = "ReceiverCountryCode")
	protected String receiverCountryCode;

	@XmlElement(name = "ReceiverCountryName")
	protected String receiverCountryName;

	@XmlElement(name = "RoutingCode")
	protected String routingCode;

	@XmlElement(name = "DispositionAddressCode")
	protected String dispositionAddressCode;

	@XmlElement(name = "ReceiverDeliveryStatus")
	protected DeliveryStatus receiverDeliveryStatus;

	@XmlElement(name = "ReceiverNetworkIntegratedApplicationName")
	protected String receiverNetworkIntegratedApplicationName;

	@XmlElement(name = "ReceiverSecurityIntegratedApplicationName")
	protected String receiverSecurityIntegratedApplicationName;

	@XmlElement(name = "InterventionDateTime")
	protected XMLGregorianCalendar interventionDateTime;

	@XmlElement(name = "InterventionSequenceNumber")
	protected Integer interventionSequenceNumber;

	@XmlElement(name = "CrestGatewayId")
	protected String crestGatewayId;

	@XmlElement(name = "CrestComServerId")
	protected String crestComServerId;

	@XmlElement(name = "ResponderDn")
	protected String responderDn;

	@XmlElement(name = "NonRepudiationIndicator")
	protected Boolean nonRepudiationIndicator;

	@XmlElement(name = "CbtReference")
	protected String cbtReference;

	@XmlElement(name = "DeliveryMode")
	protected DeliveryMode deliveryMode;

	@XmlElement(name = "Intervention")
	protected List<Intervention> intervention;

	@XmlElement(name = "Appendix")
	protected List<Appendix> appendix;

	public InstanceIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(InstanceIdentifier paramInstanceIdentifier) {
		this.identifier = paramInstanceIdentifier;
	}

	public InstanceType getType() {
		return this.type;
	}

	public void setType(InstanceType paramInstanceType) {
		this.type = paramInstanceType;
	}

	public NotificationType getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(NotificationType paramNotificationType) {
		this.notificationType = paramNotificationType;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status paramStatus) {
		this.status = paramStatus;
	}

	public int getRelatedNumber() {
		return this.relatedNumber;
	}

	public void setRelatedNumber(int paramInt) {
		this.relatedNumber = paramInt;
	}

	public XMLGregorianCalendar getAppendixDateTime() {
		return this.appendixDateTime;
	}

	public void setAppendixDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.appendixDateTime = paramXMLGregorianCalendar;
	}

	public int getAppendixSequenceNumber() {
		return this.appendixSequenceNumber;
	}

	public void setAppendixSequenceNumber(int paramInt) {
		this.appendixSequenceNumber = paramInt;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String paramString) {
		this.unitName = paramString;
	}

	public String getRoutingPoint() {
		return this.routingPoint;
	}

	public void setRoutingPoint(String paramString) {
		this.routingPoint = paramString;
	}

	public String getOriginalRoutingPoint() {
		return this.originalRoutingPoint;
	}

	public void setOriginalRoutingPoint(String paramString) {
		this.originalRoutingPoint = paramString;
	}

	public String getMpfnName() {
		return this.mpfnName;
	}

	public void setMpfnName(String paramString) {
		this.mpfnName = paramString;
	}

	public String getMpfnHandle() {
		return this.mpfnHandle;
	}

	public void setMpfnHandle(String paramString) {
		this.mpfnHandle = paramString;
	}

	public int getProcessState() {
		return this.processState;
	}

	public void setProcessState(int paramInt) {
		this.processState = paramInt;
	}

	public /*MpfnResult*/String getLastMpfnResult() {
		return this.lastMpfnResult;
	}

	public void setLastMpfnResult(/*MpfnResult*/String paramMpfnResult) {
		this.lastMpfnResult = paramMpfnResult;
	}

	public int getRelativeReference() {
		return this.relativeReference;
	}

	public void setRelativeReference(int paramInt) {
		this.relativeReference = paramInt;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int paramInt) {
		this.priority = paramInt;
	}

	public XMLGregorianCalendar getDeferredTime() {
		return this.deferredTime;
	}

	public void setDeferredTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.deferredTime = paramXMLGregorianCalendar;
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

	public XMLGregorianCalendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.creationDate = paramXMLGregorianCalendar;
	}

	public String getInitialTargetRoutingPoint() {
		return this.initialTargetRoutingPoint;
	}

	public void setInitialTargetRoutingPoint(String paramString) {
		this.initialTargetRoutingPoint = paramString;
	}

	public String getAuthoriserOperator() {
		return this.authoriserOperator;
	}

	public void setAuthoriserOperator(String paramString) {
		this.authoriserOperator = paramString;
	}

	public String getLastOperator() {
		return this.lastOperator;
	}

	public void setLastOperator(String paramString) {
		this.lastOperator = paramString;
	}

	public XMLGregorianCalendar getInQueueSince() {
		return this.inQueueSince;
	}

	public void setInQueueSince(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.inQueueSince = paramXMLGregorianCalendar;
	}

	public String getApplicationRoutingPoint() {
		return this.applicationRoutingPoint;
	}

	public void setApplicationRoutingPoint(String paramString) {
		this.applicationRoutingPoint = paramString;
	}

	public String getOperatorComment() {
		return this.operatorComment;
	}

	public void setOperatorComment(String paramString) {
		this.operatorComment = paramString;
	}

	public CorrespondentType getReceiverCorrespondentType() {
		return this.receiverCorrespondentType;
	}

	public void setReceiverCorrespondentType(CorrespondentType paramCorrespondentType) {
		this.receiverCorrespondentType = paramCorrespondentType;
	}

	public String getReceiverCorrespondentInstitutionName() {
		return this.receiverCorrespondentInstitutionName;
	}

	public void setReceiverCorrespondentInstitutionName(String paramString) {
		this.receiverCorrespondentInstitutionName = paramString;
	}

	public String getReceiverCorrespondentDepartment() {
		return this.receiverCorrespondentDepartment;
	}

	public void setReceiverCorrespondentDepartment(String paramString) {
		this.receiverCorrespondentDepartment = paramString;
	}

	public String getReceiverCorrespondentLastName() {
		return this.receiverCorrespondentLastName;
	}

	public void setReceiverCorrespondentLastName(String paramString) {
		this.receiverCorrespondentLastName = paramString;
	}

	public String getReceiverCorrespondentFirstName() {
		return this.receiverCorrespondentFirstName;
	}

	public void setReceiverCorrespondentFirstName(String paramString) {
		this.receiverCorrespondentFirstName = paramString;
	}

	public String getReceiverInstitutionName() {
		return this.receiverInstitutionName;
	}

	public void setReceiverInstitutionName(String paramString) {
		this.receiverInstitutionName = paramString;
	}

	public String getReceiverBranchInfo() {
		return this.receiverBranchInfo;
	}

	public void setReceiverBranchInfo(String paramString) {
		this.receiverBranchInfo = paramString;
	}

	public String getReceiverLocation() {
		return this.receiverLocation;
	}

	public void setReceiverLocation(String paramString) {
		this.receiverLocation = paramString;
	}

	public String getReceiverCityName() {
		return this.receiverCityName;
	}

	public void setReceiverCityName(String paramString) {
		this.receiverCityName = paramString;
	}

	public String getReceiverCountryCode() {
		return this.receiverCountryCode;
	}

	public void setReceiverCountryCode(String paramString) {
		this.receiverCountryCode = paramString;
	}

	public String getReceiverCountryName() {
		return this.receiverCountryName;
	}

	public void setReceiverCountryName(String paramString) {
		this.receiverCountryName = paramString;
	}

	public String getRoutingCode() {
		return this.routingCode;
	}

	public void setRoutingCode(String paramString) {
		this.routingCode = paramString;
	}

	public String getDispositionAddressCode() {
		return this.dispositionAddressCode;
	}

	public void setDispositionAddressCode(String paramString) {
		this.dispositionAddressCode = paramString;
	}

	public DeliveryStatus getReceiverDeliveryStatus() {
		return this.receiverDeliveryStatus;
	}

	public void setReceiverDeliveryStatus(DeliveryStatus paramDeliveryStatus) {
		this.receiverDeliveryStatus = paramDeliveryStatus;
	}

	public String getReceiverNetworkIntegratedApplicationName() {
		return this.receiverNetworkIntegratedApplicationName;
	}

	public void setReceiverNetworkIntegratedApplicationName(String paramString) {
		this.receiverNetworkIntegratedApplicationName = paramString;
	}

	public String getReceiverSecurityIntegratedApplicationName() {
		return this.receiverSecurityIntegratedApplicationName;
	}

	public void setReceiverSecurityIntegratedApplicationName(String paramString) {
		this.receiverSecurityIntegratedApplicationName = paramString;
	}

	public XMLGregorianCalendar getInterventionDateTime() {
		return this.interventionDateTime;
	}

	public void setInterventionDateTime(XMLGregorianCalendar paramXMLGregorianCalendar) {
		this.interventionDateTime = paramXMLGregorianCalendar;
	}

	public Integer getInterventionSequenceNumber() {
		return this.interventionSequenceNumber;
	}

	public void setInterventionSequenceNumber(Integer paramInteger) {
		this.interventionSequenceNumber = paramInteger;
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

	public String getResponderDn() {
		return this.responderDn;
	}

	public void setResponderDn(String paramString) {
		this.responderDn = paramString;
	}

	public Boolean isNonRepudiationIndicator() {
		return this.nonRepudiationIndicator;
	}

	public void setNonRepudiationIndicator(Boolean paramBoolean) {
		this.nonRepudiationIndicator = paramBoolean;
	}

	public String getCbtReference() {
		return this.cbtReference;
	}

	public void setCbtReference(String paramString) {
		this.cbtReference = paramString;
	}

	public DeliveryMode getDeliveryMode() {
		return this.deliveryMode;
	}

	public void setDeliveryMode(DeliveryMode paramDeliveryMode) {
		this.deliveryMode = paramDeliveryMode;
	}

	public List<Intervention> getIntervention() {
		if (this.intervention == null)
			this.intervention = new ArrayList<Intervention>();
		return this.intervention;
	}

	public List<Appendix> getAppendix() {
		if (this.appendix == null)
			this.appendix = new ArrayList<Appendix>();
		return this.appendix;
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
		builder.append("Applicaion  " + this.getCreationApplication() + "\n");
		builder.append("Type  " + this.getType() + "\n");
		builder.append("Status  " + this.getStatus() + "\n");
		builder.append("Unit  " + this.getUnitName() + "\n");
		builder.append("Created  " + this.getCreationDate().toXMLFormat() + "\n");
		return builder.toString();
	}
}
