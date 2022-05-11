/**
 * 
 */
package com.eastnets.service.license;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.reporting.licensing.beans.License;
import com.eastnets.reporting.licensing.beans.LicenseMisc;
import com.eastnets.service.Service;

/**
 * 
 * @author EastNets
 * @since dNov 26, 2012
 * 
 */
public interface LicenseService extends Service {

	

	public boolean checkLicense();
	
	public boolean checkProduct(String productID);

	@Transactional (propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int addLicenseToDB(String name, License license, boolean forceAddLicenseFlag);
	
	public License getLicenseFromDB();
	
	public License uploadLicense(InputStream inputStream);
	
	/**
	 * return back with list of license products only
	 * @return
	 */
	public ArrayList<String> getLicenseProducts();
	
	/**
	 * check license violation
	 */
	public int checkLicenseViolation() throws Exception;
	
	public int getViolationCount() throws Exception;

	public LicenseMisc getLicenseMiscFromDB() throws Exception;
	
	public String getOSArchitectureForWindows() throws IOException;
	
	public boolean hasGenerateReportLicense(String description) throws Exception;
	
	
	
	
}
