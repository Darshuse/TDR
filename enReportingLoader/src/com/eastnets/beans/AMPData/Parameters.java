package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Parameters {
	private String MsgRef;
	private String CreationDate;
	private String ApplicationHeader;

	// Getter Methods

	public String getMsgRef() {
		return MsgRef;
	}

	public String getCreationDate() {
		return CreationDate;
	}

	public String getApplicationHeader() {
		return ApplicationHeader;
	}

	// Setter Methods

	public void setMsgRef(String MsgRef) {
		this.MsgRef = MsgRef;
	}

	public void setCreationDate(String CreationDate) {
		this.CreationDate = CreationDate;
	}

	public void setApplicationHeader(String ApplicationHeader) {
		this.ApplicationHeader = ApplicationHeader;
	}
}