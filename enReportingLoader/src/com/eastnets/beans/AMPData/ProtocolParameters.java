package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class ProtocolParameters {

	@XmlElement(name = "InterAct")
	InterAct InterActObject;

	// Getter Methods

	public InterAct getInterAct() {
		return InterActObject;
	}

	// Setter Methods

	public void setInterAct(InterAct InterActObject) {
		this.InterActObject = InterActObject;
	}
}