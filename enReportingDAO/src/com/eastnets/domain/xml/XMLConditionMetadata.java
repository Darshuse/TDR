package com.eastnets.domain.xml;

import java.io.Serializable;

public class XMLConditionMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7077547845110771376L;
	
	private String identifier;
	private String identiferValue;
	private String xpath;
	private Operator operator;
	private String value;
	private boolean header;
	private String fullConditionMeaning;

	public XMLConditionMetadata() {

	}

	public XMLConditionMetadata(String xpath, String value,boolean header) {
		this.xpath = xpath;
		this.value = value;
		this.header = header;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getFullConditionMeaning() {
		return fullConditionMeaning;
	}

	public void setFullConditionMeaning(String fullConditionMeaning) {
		this.fullConditionMeaning = fullConditionMeaning;
	}
	
	
	
	public String getIdentiferValue() {
		return identiferValue;
	}

	public void setIdentiferValue(String identiferValue) {
		this.identiferValue = identiferValue;
	}

	@Override
	public String toString() {
		return identifier + "#@" + xpath + "#@" + operator + "#@" + value + "#@" + header;
	}
}
