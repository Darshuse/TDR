package com.eastnets.license;

import java.io.Serializable;
import java.util.Date;

public class ModuleValidationInfo implements Serializable {

	private static final long serialVersionUID = 7866551620886431979L;
	private String productID;
	private Date expirationDate;
	private Integer licensed;

	/**
	 * @return the productID
	 */
	public String getProductID() {
		return productID;
	}

	/**
	 * @param productID
	 *            the productID to set
	 */
	public void setProductID(String productID) {
		this.productID = productID;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the licensed
	 */
	public Integer getLicensed() {
		return licensed;
	}

	/**
	 * @param licensed
	 *            the licensed to set
	 */
	public void setLicensed(Integer licensed) {
		this.licensed = licensed;
	}

}
