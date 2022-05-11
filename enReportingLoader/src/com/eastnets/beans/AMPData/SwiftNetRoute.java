package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class SwiftNetRoute {
	private String Requestor;
	private String Responder;
	private String Service;
	private String RequestType;

	// Getter Methods

	public String getRequestor() {
		return Requestor;
	}

	public String getResponder() {
		return Responder;
	}

	public String getService() {
		return Service;
	}

	public String getRequestType() {
		return RequestType;
	}

	// Setter Methods

	public void setRequestor(String Requestor) {
		this.Requestor = Requestor;
	}

	public void setResponder(String Responder) {
		this.Responder = Responder;
	}

	public void setService(String Service) {
		this.Service = Service;
	}

	public void setRequestType(String RequestType) {
		this.RequestType = RequestType;
	}
}
