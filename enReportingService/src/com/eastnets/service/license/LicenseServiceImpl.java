/**
 * 
 */
package com.eastnets.service.license;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.license.LicenseDAO;
import com.eastnets.reporting.licensing.beans.BICLicenseInfo;
import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.License;
import com.eastnets.reporting.licensing.beans.LicenseMisc;
import com.eastnets.reporting.licensing.beans.LicenseValidationInfo;
import com.eastnets.reporting.licensing.beans.ModuleValidationInfo;
import com.eastnets.reporting.licensing.beans.Product;
import com.eastnets.reporting.licensing.util.BeanFactory;
import com.eastnets.reporting.licensing.util.LicenseUtils;
import com.eastnets.service.ServiceBaseImp;

/**
 * 
 * @author EastNets
 * @since dNov 26, 2012
 * 
 */
/**
 * @author MAlTaweel
 *
 */
public class LicenseServiceImpl extends ServiceBaseImp implements LicenseService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1581523488067377312L;
	private LicenseDAO licenseDAO;
	private String schemaName;
	private String databaseName;

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public int addLicenseToDB(String name, License license, boolean forceAddLicenseFlag) {
		try {
			// for TFS23352 we dont support uploading same license twice noce, and we check the license creating time so that we will not upload same license or older license so this validation has no
			// meaning now
			forceAddLicenseFlag = true;
			//
			if (!forceAddLicenseFlag && compareLicensesKeys(license))
				return 2;// the key is same for the old and new license.

			getLicenseDAO().deleteLicenseInfo();
			getLicenseDAO().deleteLicensedBICExceptTnT();
			getLicenseDAO().deleteLicensedProduct();
			getLicenseDAO().resetConnections();

			getLicenseDAO().insertLicenseInfo(license.getLicenseMisc().getLicenseID(), license.getLicenseMisc().getMaxUsers(), license.getLicenseMisc().getMaxConnections(), license.getLicenseKeys()[0], license.getLicenseKeys()[1],
					license.getLicenseDateEncrypted(true));

			for (Product product : license.getProducts()) {
				getLicenseDAO().insertProductInfo(product.getID(), product.getDescription(), product.getExpirationDate(), (product.isLicensed() ? 1 : 0));
			}

			for (Product traffic : license.getTraffics()) {
				getLicenseDAO().insertProductInfo(traffic.getID(), traffic.getDescription(), traffic.getExpirationDate(), (traffic.isLicensed() ? 1 : 0));
			}

			for (BicCode bicCode : license.getBicCodes()) {
				getLicenseDAO().insertOrUpdateBICInfo( // insertBICInfo(
						bicCode.getBicCode(), LicenseUtils.GenerateLicenseInfoString(new BICLicenseInfo()), bicCode.getBandVolume());
			}

			if (!((getSchemaName() != null && getSchemaName().trim().equalsIgnoreCase("DDA")) || (getDatabaseName() != null && getDatabaseName().trim().equalsIgnoreCase("DDA")))) {
				getLicenseDAO().deleteNonParentTnTLicensedBIC();
				getLicenseDAO().deleteNONLicensedBICFromUserGroup();
			}

			return 1;
		} catch (Exception e) {
			throw new WebClientException(e);

		}

	}

	protected boolean compareLicensesKeys(License license) {

		String newLicenseKey = license.getLicenseKeys()[0].concat(license.getLicenseKeys()[1]);
		String currentLicenseKey = getLicenseDAO().getLicenseKey();

		if (currentLicenseKey.trim().equals(newLicenseKey.trim())) {
			return true;
		}

		return false;
	}

	@Override
	public License getLicenseFromDB() {
		LicenseMisc licenseMisc = getLicenseDAO().getLicenseMisc();
		if (licenseMisc == null) {
			return null;
		}

		License licenseBean = BeanFactory.getInstance().getNewLicense(licenseMisc);

		List<Product> products = getLicenseDAO().getProducts();
		List<Product> traffics = getLicenseDAO().getTraffics();
		List<BicCode> licensedBICCodes = getLicenseDAO().getLicensedBICCodes();

		licenseBean.getProducts().clear();
		licenseBean.getProducts().addAll(products);
		licenseBean.getTraffics().clear();
		licenseBean.getTraffics().addAll(traffics);
		licenseBean.getBicCodes().clear();
		licenseBean.getBicCodes().addAll(licensedBICCodes);

		String licenseKey = getLicenseDAO().getLicenseKey();

		String[] keys = new String[2];
		keys[0] = licenseKey.substring(0, 8);
		keys[1] = licenseKey.substring(8);

		licenseBean.setLicenseKeys(keys);

		String licenseDateEncrypted = licenseMisc.getLicenseValidationEncrypted();

		try {
			licenseBean.setLicenseDate(License.getDateFromEncryptedString(licenseDateEncrypted, true));
		} catch (Exception e) {
			throw new WebClientException(e);

		}

		return licenseBean;
	}

	@Override
	public boolean hasGenerateReportLicense(String description) throws Exception {
		License license = getLicenseFromDB();
		for (Product product : license.getProducts()) {
			if (product.getDescription().equals(description) && product.isLicensed()) {
				return true;

			}
		}

		return false;
	}

	@Override
	public boolean checkLicense() {
		LicenseValidationInfo validationInfo = this.getLicenseDAO().checkLicense();

		String strData = validationInfo.getData();
		if (strData == null)
			return false;
		int offset = validationInfo.getOffset();
		String strKey = validationInfo.getKey();
		int retStatus = validationInfo.getStatus();

		return retStatus == 1 && LicenseUtils.check(strData.toCharArray(), offset, strKey.toCharArray());
	}

	@Override
	public boolean checkProduct(String productID) {
		return isLicensedProd(productID);
	}

	/**
	 * @return the licenseDAO
	 */
	public LicenseDAO getLicenseDAO() {
		return licenseDAO;
	}

	/**
	 * @param licenseDAO
	 *            the licenseDAO to set
	 */
	public void setLicenseDAO(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}

	@Override
	public License uploadLicense(InputStream inputStream) {
		return LicenseUtils.readLicenseFromFile(inputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eastnets.service.license.LicenseService#getLicenseProducts()
	 */
	@Override
	public ArrayList<String> getLicenseProducts() {
		ArrayList<String> licProd = new ArrayList<String>();
		// TODO what the hell is this !!!
		String[] products = { "01", "02", "03", "04", "05", "12", "16", "17", "18", "19", "20", "21", "22", "28", "29" };

		for (int i = 0; i < products.length; i++) {
			if (isLicensedProd(products[i]) == true)
				licProd.add(products[i]);
		}

		return licProd;
	}

	/**
	 * return the license status of the given product ID, the status also depends on expiration date.
	 * 
	 * @param productID
	 * @return
	 */
	private boolean isLicensedProd(String productID) {
		ModuleValidationInfo moduleInfo = getLicenseDAO().checkProduct(productID);

		java.util.Date expirationDate = moduleInfo.getExpirationDate();
		Integer licensed = moduleInfo.getLicensed();

		return licensed != null && licensed.toString().equals("1") && (expirationDate == null || expirationDate.after(new java.util.Date()));
	}

	@Override
	public int checkLicenseViolation() throws Exception {

		if (licenseDAO.checkLicenseViolationError())
			return Constants.LICENSE_ERROR;

		if (licenseDAO.checkLicenseViolationWarning())
			return Constants.LICENSE_WARNING;

		return Constants.LICENSE_OK;
	}

	@Override
	public int getViolationCount() throws Exception {

		return licenseDAO.getViolationCount();
	}

	public LicenseMisc getLicenseMiscFromDB() throws Exception {

		LicenseMisc licenseMisc = getLicenseDAO().getLicenseMisc();
		if (licenseMisc == null) {
			return null;
		}

		return licenseMisc;
	}

	public String getOSArchitectureForWindows() throws IOException {
		// Execute command for OS Architecture
		String command = "wmic OS get OSArchitecture";
		Process child = Runtime.getRuntime().exec(command);

		BufferedReader br = new BufferedReader(new InputStreamReader(child.getInputStream()));
		String line = null, result = "";

		while ((line = br.readLine()) != null) {
			if (line.length() > 0)
				result = line.trim();
		}

		br.close();
		return result;
	}
}
