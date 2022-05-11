package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Status {
	CurrentStatus CurrentStatusObject;
	@XmlElement(name = "StructuredHistory")
	StructuredHistory StructuredHistory;
	@XmlElement(name = "TextHistory")
	private String TextHistory;

	public StructuredHistory getStructuredHistory() {
		return StructuredHistory;
	}

	public void setStructuredHistory(StructuredHistory structuredHistory) {
		StructuredHistory = structuredHistory;
	}

	@XmlElement(name = "CurrentStatus")
	public CurrentStatus getCurrentStatus() {
		return CurrentStatusObject;
	}

	public void setCurrentStatus(CurrentStatus CurrentStatusObject) {
		this.CurrentStatusObject = CurrentStatusObject;
	}

	public String getTextHistory() {
		return TextHistory;
	}

	public void setTextHistory(String textHistory) {
		TextHistory = textHistory;
	}

}