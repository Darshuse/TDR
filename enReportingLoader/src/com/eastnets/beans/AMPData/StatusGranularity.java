package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusGranularity {
	@XmlElement
	private String Phase;
	@XmlElement
	private String Action;
	@XmlElement
	private String Reason;

	// Getter Methods

	@XmlElement
	public String getPhase() {
		return Phase;
	}

	@XmlElement
	public String getAction() {
		return Action;
	}

	@XmlElement
	public String getReason() {
		return Reason;
	}

	// Setter Methods

	public void setPhase(String Phase) {
		this.Phase = Phase;
	}

	public void setAction(String Action) {
		this.Action = Action;
	}

	public void setReason(String Reason) {
		this.Reason = Reason;
	}
}