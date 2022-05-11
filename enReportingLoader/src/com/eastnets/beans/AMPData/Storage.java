package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Storage {
	PayloadEncoding PayloadEncodingObject;
	private String Payload;

	// Getter Methods

	public PayloadEncoding getPayloadEncoding() {
		return PayloadEncodingObject;
	}

	public String getPayload() {
		return Payload;
	}

	// Setter Methods

	public void setPayloadEncoding(PayloadEncoding PayloadEncodingObject) {
		this.PayloadEncodingObject = PayloadEncodingObject;
	}

	public void setPayload(String Payload) {
		this.Payload = Payload;
	}
}