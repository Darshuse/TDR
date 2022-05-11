package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author MKassab
 * 
 */

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentStatus {

	private String Status;
	StatusGranularity StatusGranularity;

	private String StatusDate;

	private String StatusChangeSource;

	private String Message;

	private String Decision;

	// Getter Methods

	@XmlElement
	public String getStatus() {
		return Status;
	}

	@XmlElement
	public String getStatusDate() {
		return StatusDate;
	}

	@XmlElement
	public String getStatusChangeSource() {
		return StatusChangeSource;
	}

	@XmlElement(name = "Message")
	public String getMessage() {
		return Message;
	}

	@XmlElement
	public String getDecision() {
		return Decision;
	}

	// Setter Methods

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public void setStatusDate(String StatusDate) {
		this.StatusDate = StatusDate;
	}

	public void setStatusChangeSource(String StatusChangeSource) {
		this.StatusChangeSource = StatusChangeSource;
	}

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public void setDecision(String Decision) {
	}

	@XmlElement
	public StatusGranularity getStatusGranularity() {
		return StatusGranularity;
	}

	public void setStatusGranularity(StatusGranularity statusGranularity) {
		StatusGranularity = statusGranularity;
	}
}