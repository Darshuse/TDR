/**
 * 
 */
package com.eastnets.dao.license.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.reporting.licensing.beans.LicenseMisc;
import com.eastnets.reporting.licensing.util.BeanFactory;

/**
 * 
 * @author EastNetsb
 * @since dDec 10, 2012
 * 
 */
public class LicensedInfoMapper implements Serializable, RowMapper<LicenseMisc> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3672399179612642538L;

	@Override
	public LicenseMisc mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		String licenseID = rs.getString("LICENSEID");
		int maxConnections = rs.getInt("MAXCONNECTIONS");
		int maxUsers = rs.getInt("MAXUSERS");
		String licenseValidationEncrypted = rs.getString("LicenseValidation");
		return BeanFactory.getInstance().getNewLicenseMisc(maxConnections, maxUsers, licenseID, licenseValidationEncrypted);
	}
}
