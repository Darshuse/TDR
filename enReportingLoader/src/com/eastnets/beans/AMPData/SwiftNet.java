package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class SwiftNet {
	private String UserReference;
	private String DelNotRequest;
	private String RequestCrypto;
	private String NRIndicator;
	private String DeliveryMode;
	private String RequireSignatureList;
	private String CopyIndicator;
	private String AuthNotifIndicator;
	private String SnFOutputSequenceNumber;
	private String SnFOutputSessionId;
	private String SnFDeliveryTime;
	private String SnFInputTime;
	private String E2EMessageID;

	// Getter Methods

	public String getUserReference() {
		return UserReference;
	}

	public String getDelNotRequest() {
		return DelNotRequest;
	}

	public String getRequestCrypto() {
		return RequestCrypto;
	}

	public String getNRIndicator() {
		return NRIndicator;
	}

	public String getDeliveryMode() {
		return DeliveryMode;
	}

	public String getRequireSignatureList() {
		return RequireSignatureList;
	}

	public String getCopyIndicator() {
		return CopyIndicator;
	}

	public String getAuthNotifIndicator() {
		return AuthNotifIndicator;
	}

	public String getSnFOutputSequenceNumber() {
		return SnFOutputSequenceNumber;
	}

	public String getSnFOutputSessionId() {
		return SnFOutputSessionId;
	}

	public String getSnFDeliveryTime() {
		return SnFDeliveryTime;
	}

	public String getSnFInputTime() {
		return SnFInputTime;
	}

	// Setter Methods

	public void setUserReference(String UserReference) {
		this.UserReference = UserReference;
	}

	public void setDelNotRequest(String DelNotRequest) {
		this.DelNotRequest = DelNotRequest;
	}

	public void setRequestCrypto(String RequestCrypto) {
		this.RequestCrypto = RequestCrypto;
	}

	public void setNRIndicator(String NRIndicator) {
		this.NRIndicator = NRIndicator;
	}

	public void setDeliveryMode(String DeliveryMode) {
		this.DeliveryMode = DeliveryMode;
	}

	public void setRequireSignatureList(String RequireSignatureList) {
		this.RequireSignatureList = RequireSignatureList;
	}

	public void setCopyIndicator(String CopyIndicator) {
		this.CopyIndicator = CopyIndicator;
	}

	public void setAuthNotifIndicator(String AuthNotifIndicator) {
		this.AuthNotifIndicator = AuthNotifIndicator;
	}

	public void setSnFOutputSequenceNumber(String SnFOutputSequenceNumber) {
		this.SnFOutputSequenceNumber = SnFOutputSequenceNumber;
	}

	public void setSnFOutputSessionId(String SnFOutputSessionId) {
		this.SnFOutputSessionId = SnFOutputSessionId;
	}

	public void setSnFDeliveryTime(String SnFDeliveryTime) {
		this.SnFDeliveryTime = SnFDeliveryTime;
	}

	public void setSnFInputTime(String SnFInputTime) {
		this.SnFInputTime = SnFInputTime;
	}

	public String getE2EMessageID() {
		return E2EMessageID;
	}

	public void setE2EMessageID(String e2eMessageID) {
		E2EMessageID = e2eMessageID;
	}
}