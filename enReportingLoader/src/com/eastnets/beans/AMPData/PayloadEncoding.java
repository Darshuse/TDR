package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class PayloadEncoding {
	private String Code;
	private String IANAName;

	// Getter Methods

	public String getCode() {
		return Code;
	}

	public String getIANAName() {
		return IANAName;
	}

	// Setter Methods

	public void setCode(String Code) {
		this.Code = Code;
	}

	public void setIANAName(String IANAName) {
		this.IANAName = IANAName;
	}
}