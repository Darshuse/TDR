package com.eastnets.reporting.license.generator.pdfLicenseGenerator.codeBean;

import java.util.Date;

import com.eastnets.reporting.licensing.beans.Product;

public class MyProduct extends Product {
	private static final long serialVersionUID = 1L;
	String ID;
	String description;
	Date expirationDate;
	boolean licensed;

	public MyProduct(String ID, String description, Date expirationDate, boolean licensed) {
		super(ID, description, expirationDate, licensed);
		this.ID = ID;
		this.description = description;
		this.expirationDate = expirationDate;
		this.licensed = licensed;
		// TODO Auto-generated constructor stub
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isLicensed() {
		return licensed;
	}

	public void setLicensed(boolean licensed) {
		this.licensed = licensed;
	}

	/**
	 * 
	 */

}
