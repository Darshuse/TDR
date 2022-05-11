package com.eastnets.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.http.HttpStatus;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageResponse {

	@XmlElement(name = "StatusCode")
	private HttpStatus statusCode = HttpStatus.OK;

	@XmlElement(name = "StatusMseg")
	private String statusMsg = "";

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

}
