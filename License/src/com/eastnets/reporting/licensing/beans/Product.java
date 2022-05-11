package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;
import java.util.Date;

import com.eastnets.reporting.licensing.util.ProductUtils;


public abstract class Product implements Serializable, Licensable<Product> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8515454731233382981L;
	private String description;
	private Date expirationDate;
	private String ID;
	private boolean licensed;

	public Product(String ID, String description, Date expirationDate, boolean licensed) {
		this.ID = ID;
		this.description = description;
		this.expirationDate = expirationDate;
		this.licensed = licensed;
	}

	@Override
	public int compareTo(final Product product) {
		return this.ID.compareTo(product.ID);
	}

	@Override
	public boolean equals(final Object product) {
		return this.ID.equals(((Product) product).ID);
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	@Override
	public int hashCode() {
		return this.ID.hashCode();
	}

	@Override
	public boolean includeInLicense() {
		return this.licensed;
	}

	public boolean isLicensed() {
		return this.licensed;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setLicensed(final boolean licensed) {
		this.licensed = licensed;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	@Override
	public String toLicenseDataString() {
		return this.ID + ((this.expirationDate != null) ? String.valueOf(ProductUtils.daysUntill(this.expirationDate)) : "");
	}
}
