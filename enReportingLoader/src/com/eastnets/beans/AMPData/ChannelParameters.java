package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author MKassab
 * 
 */

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelParameters {

	private String Code;
	private String Protocol;
	private String CommunicationType;
	BackendChannel BackendChannel;

	// Getter Methods

	public String getCode() {
		return Code;
	}

	public String getProtocol() {
		return Protocol;
	}

	public String getCommunicationType() {
		return CommunicationType;
	}

	// Setter Methods

	public void setCode(String Code) {
		this.Code = Code;
	}

	public void setProtocol(String Protocol) {
		this.Protocol = Protocol;
	}

	public void setCommunicationType(String CommunicationType) {
		this.CommunicationType = CommunicationType;
	}

	public BackendChannel getBackendChannel() {
		return BackendChannel;
	}

	public void setBackendChannel(BackendChannel backendChannel) {
		BackendChannel = backendChannel;
	}

}
