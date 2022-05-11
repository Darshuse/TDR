package com.eastnets.domain.viewer;

import java.io.Serializable;

public class XMLType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5982993439129521318L;
	private String xsdName;
	private String xsltName;
	public String getXsdName() {
		return xsdName;
	}
	public void setXsdName(String xsdName) {
		this.xsdName = xsdName;
	}
	public String getXsltName() {
		return xsltName;
	}
	public void setXsltName(String xsltName) {
		this.xsltName = xsltName;
	}
	
}
