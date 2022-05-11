/**
 * 
 */
package com.eastnets.dao.license;

import java.util.Date;
import java.util.List;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.license.mappers.BicLicensedInfoMapper;
import com.eastnets.dao.license.mappers.LicensedBicMapper;
import com.eastnets.dao.license.mappers.LicensedInfoMapper;
import com.eastnets.dao.license.mappers.LicensedKeyMapper;
import com.eastnets.dao.license.mappers.LicensedProductMapper;
import com.eastnets.dao.license.procedures.LicenseCheckLicenseProcedure;
import com.eastnets.dao.license.procedures.LicenseCheckProductProcedure;
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
public abstract class LicenseDAOImpl extends DAOBaseImp implements LicenseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1471138958449406534L;
	private LicenseCheckLicenseProcedure checkLicenseSP;
	private LicenseCheckProductProcedure checkProductSP;

	@Override
	public LicenseValidationInfo checkLicense() {
		return checkLicenseSP.execute();
	}

	@Override
	public ModuleValidationInfo checkProduct(final String productID) {
		return checkProductSP.execute(productID);
	}

	@Override
	public void deleteLicenseInfo() {
		jdbcTemplate.execute("delete sLicenseInfo");

	}

	@Override
	public void deleteBICUserGroup() {
		jdbcTemplate.execute("delete sBICUserGroup");

	}

	public void deleteNONLicensedBICFromUserGroup() {
		jdbcTemplate.execute("delete from sBICUserGroup  where biccode not in (select biccode from sLicensedBIC bicLicense )");
	}

	@Override
	public void deleteLicensedBIC() {
		jdbcTemplate.execute("delete sLicensedBIC");

	}

	@Override
	public void deleteLicensedProduct() {
		jdbcTemplate.update("delete sLicensedProduct");

	}

	@Override
	public void resetConnections() {
		jdbcTemplate.update("update ldSettings set LicenseInfo='14,70,116,1604,100402181D180140'");
	}

	@Override
	public void insertLicenseInfo(String licenseID, int maxUsers, int maxConnections, String leftKey, String rightKey, String licenseValidationEncrypted) {
		jdbcTemplate
				.update(String.format("insert into sLicenseInfo(licenseid, maxusers, maxconnections, licensekey, licensevalidation) values('%s',%d,%d,'%s%s','%s')", licenseID, maxUsers, maxConnections, leftKey, rightKey, licenseValidationEncrypted));
	}

	@Override
	public void insertProductInfo(String productID, String description, Date expirationDate, int isLicensed) {
		String strUpdate = String.format("insert into sLicensedProduct values('%s','%s',%s,%d)", productID, description,
				((expirationDate == null) ? "null" : this.getDbPortabilityHandler().getDateWithPatternNoBinding(expirationDate.toString(), "yyyy-mm-dd")), isLicensed);
		jdbcTemplate.update(strUpdate);
	}

	@Override
	public void insertBICInfo(String bicCode, String bicLicenseInfo, int volume) {
		jdbcTemplate.update(String.format("insert into sLicensedBic(BICCode,volume,LicenseInfo) values('%s', %d, '%s')", bicCode, volume, bicLicenseInfo));
	}

	@Override
	public void insertOrUpdateBICInfo(String bicCode, String bicLicenseInfo, int volume) {
		if (jdbcTemplate.queryForObject(String.format("select count(1) from sLicensedBic where BICCODE = '%s'", bicCode), Integer.class) >= 1) {
			jdbcTemplate.update(String.format("update sLicensedBic set volume = %d , LicenseInfo = '%s' where BICCode = '%s'", volume, bicLicenseInfo, bicCode));
		} else {
			jdbcTemplate.update(String.format("insert into sLicensedBic(BICCode,volume,LicenseInfo) values('%s', %d, '%s')", bicCode, volume, bicLicenseInfo));
		}
	}

	@Override
	public LicenseMisc getLicenseMisc() {
		List<LicenseMisc> licenseInfos = jdbcTemplate.query("select LicenseID, MaxUsers, MaxConnections,LicenseValidation from sLicenseInfo", new LicensedInfoMapper());

		return (licenseInfos.isEmpty() ? null : licenseInfos.get(0));
	}

	@Override
	public List<Product> getProducts() {
		return jdbcTemplate.query("select ID, Description, ExpirationDate, Licensed from sLicensedProduct where ID not in ('13', '15') order by ID", new LicensedProductMapper());
	}

	@Override
	public List<Product> getTraffics() {
		return jdbcTemplate.query("select ID, Description, ExpirationDate, Licensed from sLicensedProduct where ID in ('13', '15') order by ID", new LicensedProductMapper());
	}

	@Override
	public List<BicCode> getLicensedBICCodes() {
		return jdbcTemplate.query("select BicCode, Volume from sLicensedBic order by BicCode", new LicensedBicMapper());
	}

	@Override
	public String getLicenseKey() {
		List<String> query = jdbcTemplate.query("select LicenseKey from sLicenseInfo", new LicensedKeyMapper());
		return (query.size() > 0 ? query.get(0) : "");
	}

	/**
	 * @return the checkLicenseSP
	 */
	public LicenseCheckLicenseProcedure getCheckLicenseSP() {
		return checkLicenseSP;
	}

	/**
	 * @param checkLicenseSP
	 *            the checkLicenseSP to set
	 */
	public void setCheckLicenseSP(LicenseCheckLicenseProcedure checkLicenseSP) {
		this.checkLicenseSP = checkLicenseSP;
	}

	/**
	 * @return the checkProductSP
	 */
	public LicenseCheckProductProcedure getCheckProductSP() {
		return checkProductSP;
	}

	/**
	 * @param checkProductSP
	 *            the checkProductSP to set
	 */
	public void setCheckProductSP(LicenseCheckProductProcedure checkProductSP) {
		this.checkProductSP = checkProductSP;
	}

	public List<BicLicenseInfo> getBicLicenseInfo() {
		return jdbcTemplate.query("select BicCode, Volume, licenseInfo from sLicensedBic order by BicCode", new BicLicensedInfoMapper());
	}
}
