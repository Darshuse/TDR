package com.eastnets.resilience.mtutil;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MT")
public class MTType {
	
	String id ;
	public String getId() {
		return id;
	}
	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	String ccyField ;
	String amtField ;
	String valueDateField ;
	public String getCcyField() {
		return ccyField;
	}
	@XmlElement
	public void setCcyField(String ccyField) {
		this.ccyField = ccyField;
	}
	public String getAmtField() {
		return amtField;
	}
	@XmlElement
	public void setAmtField(String amtField) {
		this.amtField = amtField;
	}
	public String getValueDateField() {
		return valueDateField;
	}
	@XmlElement
	public void setValueDateField(String valueDateField) {
		this.valueDateField = valueDateField;
	}
}
