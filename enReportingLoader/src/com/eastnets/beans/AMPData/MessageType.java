package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageType {
	private String Code;
	private String Name;
	private String Description;

	// Getter Methods

	public String getCode() {
		return Code;
	}

	public String getName() {
		return Name;
	}

	public String getDescription() {
		return Description;
	}

	// Setter Methods

	public void setCode(String Code) {
		this.Code = Code;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}
}