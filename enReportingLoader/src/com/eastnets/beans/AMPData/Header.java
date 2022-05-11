package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Header {
	private String Direction;
	private String Protocol;
	private String Service;
	private String ProfileCode;
	private String SenderAddress;
	private String ReceiverAddress;
	private String MessageReference;
	private String TransactionReference;
	private String TransferReference;
	private String Owner;
	private String OriginatorApplication;
	private String MessageFormat;
	@XmlElement(name = "MessageType")
	MessageType MessageTypeObject;
	private String DateCreated;
	private String DateReceived;
	private String DateDelivered;
	private String PDEIndication;
	private String NetworkPriority;
	private String ProcessPriority;
	private String ProcessingType;
	private String Workflow;
	private String WorkflowModel;
	private String BackendChannel;
	private String NetworkChannel;
	private String Environment;

	// Getter Methods

	public String getDirection() {
		return Direction;
	}

	public String getProtocol() {
		return Protocol;
	}

	public String getService() {
		return Service;
	}

	public String getProfileCode() {
		return ProfileCode;
	}

	public String getSenderAddress() {
		return SenderAddress;
	}

	public String getReceiverAddress() {
		return ReceiverAddress;
	}

	public String getMessageReference() {
		return MessageReference;
	}

	public String getTransactionReference() {
		return TransactionReference;
	}

	public String getTransferReference() {
		return TransferReference;
	}

	public String getOwner() {
		return Owner;
	}

	public String getOriginatorApplication() {
		return OriginatorApplication;
	}

	public String getMessageFormat() {
		return MessageFormat;
	}

	public MessageType getMessageType() {
		return MessageTypeObject;
	}

	public String getDateCreated() {
		return DateCreated;
	}

	public String getDateReceived() {
		return DateReceived;
	}

	public String getDateDelivered() {
		return DateDelivered;
	}

	public String getPDEIndication() {
		return PDEIndication;
	}

	public String getNetworkPriority() {
		return NetworkPriority;
	}

	public String getProcessPriority() {
		return ProcessPriority;
	}

	public String getProcessingType() {
		return ProcessingType;
	}

	public String getWorkflow() {
		return Workflow;
	}

	public String getWorkflowModel() {
		return WorkflowModel;
	}

	public String getBackendChannel() {
		return BackendChannel;
	}

	public String getNetworkChannel() {
		return NetworkChannel;
	}

	public String getEnvironment() {
		return Environment;
	}

	// Setter Methods

	public void setDirection(String Direction) {
		this.Direction = Direction;
	}

	public void setProtocol(String Protocol) {
		this.Protocol = Protocol;
	}

	public void setService(String Service) {
		this.Service = Service;
	}

	public void setProfileCode(String ProfileCode) {
		this.ProfileCode = ProfileCode;
	}

	public void setSenderAddress(String SenderAddress) {
		this.SenderAddress = SenderAddress;
	}

	public void setReceiverAddress(String ReceiverAddress) {
		this.ReceiverAddress = ReceiverAddress;
	}

	public void setMessageReference(String MessageReference) {
		this.MessageReference = MessageReference;
	}

	public void setTransactionReference(String TransactionReference) {
		this.TransactionReference = TransactionReference;
	}

	public void setTransferReference(String TransferReference) {
		this.TransferReference = TransferReference;
	}

	public void setOwner(String Owner) {
		this.Owner = Owner;
	}

	public void setOriginatorApplication(String OriginatorApplication) {
		this.OriginatorApplication = OriginatorApplication;
	}

	public void setMessageFormat(String MessageFormat) {
		this.MessageFormat = MessageFormat;
	}

	public void setMessageType(MessageType MessageTypeObject) {
		this.MessageTypeObject = MessageTypeObject;
	}

	public void setDateCreated(String DateCreated) {
		this.DateCreated = DateCreated;
	}

	public void setDateReceived(String DateReceived) {
		this.DateReceived = DateReceived;
	}

	public void setDateDelivered(String DateDelivered) {
		this.DateDelivered = DateDelivered;
	}

	public void setPDEIndication(String PDEIndication) {
		this.PDEIndication = PDEIndication;
	}

	public void setNetworkPriority(String NetworkPriority) {
		this.NetworkPriority = NetworkPriority;
	}

	public void setProcessPriority(String ProcessPriority) {
		this.ProcessPriority = ProcessPriority;
	}

	public void setProcessingType(String ProcessingType) {
		this.ProcessingType = ProcessingType;
	}

	public void setWorkflow(String Workflow) {
		this.Workflow = Workflow;
	}

	public void setWorkflowModel(String WorkflowModel) {
		this.WorkflowModel = WorkflowModel;
	}

	public void setBackendChannel(String BackendChannel) {
		this.BackendChannel = BackendChannel;
	}

	public void setNetworkChannel(String NetworkChannel) {
		this.NetworkChannel = NetworkChannel;
	}

	public void setEnvironment(String Environment) {
		this.Environment = Environment;
	}
}