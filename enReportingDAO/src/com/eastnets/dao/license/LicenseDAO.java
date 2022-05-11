/**
 * 
 */
package com.eastnets.dao.license;

import java.util.Date;
import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.license.BicLicenseInfo;
import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.LicenseMisc;
import com.eastnets.reporting.licensing.beans.LicenseValidationInfo;
import com.eastnets.reporting.licensing.beans.ModuleValidationInfo;
import com.eastnets.reporting.licensing.beans.Product;

/**
 * 
 * @author EastNets
 * @since dNov 26, 2012
 * 
 */
public interface LicenseDAO extends DAO {
	
	public LicenseValidationInfo checkLicense();
	
	public ModuleValidationInfo checkProduct(final String productID);
	
	public void deleteLicenseInfo();
	public void deleteBICUserGroup();
	public void deleteNONLicensedBICFromUserGroup();
	public void deleteLicensedBIC();
	public void deleteLicensedBICExceptTnT();
	public void deleteNonParentTnTLicensedBIC();
	public void deleteLicensedProduct();
	public void resetConnections();
	
	public void insertLicenseInfo(String licenseID, int maxUsers, int maxConnections, String key0, String key1, String licenseValidationEncrypted);
	public void insertProductInfo(String productID, String description, Date expirationDate, int isLicensed);
	public void insertBICInfo(String bicCode, String bicLicenseInfo, int volume);
	public void insertOrUpdateBICInfo(String bicCode, String bicLicenseInfo, int volume);
	
	public String getLicenseKey();
	
	public LicenseMisc getLicenseMisc();
	public List<Product> getProducts();
	public List<Product> getTraffics();
	public List<BicCode> getLicensedBICCodes();
	
	/**
	 * check license violation error
	 */
	public boolean checkLicenseViolationError() throws Exception;
	
	/**
	 * check license violation warning
	 */
	public boolean checkLicenseViolationWarning() throws Exception;
	
	
	public int getViolationCount() throws Exception ;
	
	public List<BicLicenseInfo> getBicLicenseInfo();
}
