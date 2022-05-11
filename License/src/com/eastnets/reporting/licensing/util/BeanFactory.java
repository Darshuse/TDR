/**
 * 
 */
package com.eastnets.reporting.licensing.util;

import java.io.Serializable;
import java.util.Date;

import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.License;
import com.eastnets.reporting.licensing.beans.LicenseMisc;
import com.eastnets.reporting.licensing.beans.Product;
import com.eastnets.reporting.licensing.beans.TrafficBand;
import com.eastnets.reporting.licensing.exception.LicenseException;

/**
 * 
 * @author EastNets
 * @since dDec 9, 2012
 * 
 */
public class BeanFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6334719781874334932L;
	private static BeanFactory factory;

	private BeanFactory() {
	}

	public static BeanFactory getInstance() {

		if (factory == null) {
			factory = new BeanFactory();
		}

		return factory;
	}

	public LicenseMisc getNewLicenseMisc(int maxConnections, int maxUsers, String licenseID, String licenseValidationEncrypted) {
		if (maxConnections <= 0 || maxConnections > 999) {
			throw new LicenseException("Connections Should Be Between 1 and 1000");
		}

		if (maxUsers <= 0 || maxUsers > 999) {
			throw new LicenseException("Users Should Be Between 1 and 1000");
		}

		if (licenseID != null && licenseID.length() > 16) {
			throw new LicenseException("Long License ID; max 16 characters");
		}

		return this.new LicenseInfoImpl(maxConnections, maxUsers, licenseID, licenseValidationEncrypted);
	}

	public BicCode getNewBICCode(String bicCodeName, TrafficBand bicCodeBand, int bandVolume) {
		if (!this.IsValidBIC(bicCodeName)) {
			throw new LicenseException("could not instantiate a bic code with invalid bic code name");
		}

		if (bicCodeBand == null) {
			throw new LicenseException("could not instantiate a bic code with null band");
		}

		return this.new BicCodeImpl(bicCodeName.toUpperCase(), bicCodeBand, bandVolume);
	}

	private boolean IsValidBIC(String bicCode) {
		boolean isValidBic = bicCode != null && bicCode.length() == 8;
		int index = 0;
		while (isValidBic && index < 8) {
			isValidBic = Character.isLetterOrDigit(bicCode.charAt(index++));
		}
		return isValidBic;
	}

	public ProductImpl getNewProduct(String ID, String description, Date expirationDate, boolean licensed) {
		if (ID == null || ID.length() != 2 || !Character.isDigit(ID.charAt(0)) || !Character.isDigit(ID.charAt(1))) {
			throw new LicenseException("could not initiate a Product with invalid ID");
		}

		if (description == null || description.trim().isEmpty()) {
			throw new LicenseException("could not initiate a Product with invalid description");
		}

		return new ProductImpl(ID, description, expirationDate, licensed);
	}

	public License getNewLicense(LicenseMisc licenseInfo) {

		if (licenseInfo == null) {
			throw new LicenseException("License miscellaneous Info should not be null.");
		}
		return new LicenseImpl(licenseInfo);
	}

	private class BicCodeImpl extends BicCode {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8062922380021549833L;

		/**
		 * @param bicCode
		 * @param band
		 */
		public BicCodeImpl(String bicCode, TrafficBand band, int bandVolume) {
			super(bicCode, band, bandVolume);
		}

	}

	private class ProductImpl extends Product {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4241641511383102779L;

		/**
		 * @param ID
		 * @param description
		 * @param expirationDate
		 * @param licensed
		 */
		public ProductImpl(String ID, String description, Date expirationDate, boolean licensed) {
			super(ID, description, expirationDate, licensed);
		}

	}

	private class LicenseImpl extends License {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4077540031321546950L;

		/**
		 * @param connections
		 * @param users
		 * @param licenseID
		 */
		public LicenseImpl(LicenseMisc licenseInfo) {
			super(licenseInfo);
		}

	}

	private class LicenseInfoImpl extends LicenseMisc {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7265026015067619055L;

		/**
		 * @param maxConnections
		 * @param maxUsers
		 * @param licenseID
		 * @param licenseValidationEncrypted
		 */
		public LicenseInfoImpl(int maxConnections, int maxUsers, String licenseID, String licenseValidationEncrypted) {
			super(maxConnections, maxUsers, licenseID, licenseValidationEncrypted);
		}

	}

}
