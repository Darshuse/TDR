/**
 * 
 */
package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author EastNets
 * @since dNov 28, 2012
 * 
 */
public class ModuleValidationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8906069161585219851L;
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
