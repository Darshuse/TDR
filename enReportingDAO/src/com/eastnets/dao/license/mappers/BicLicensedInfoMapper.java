package com.eastnets.dao.license.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.domain.license.BicLicenseInfo;
import com.eastnets.reporting.licensing.beans.BICLicenseInfo;
import com.eastnets.reporting.licensing.beans.TrafficBand;
import com.eastnets.reporting.licensing.util.BeanFactory;
import com.eastnets.reporting.licensing.util.LicenseUtils;

// row mapper for spring 
public class BicLicensedInfoMapper implements Serializable, RowMapper<BicLicenseInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5785628037388367280L;

	@Override
	public BicLicenseInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		BicLicenseInfo bicLicenseInfo = new BicLicenseInfo();

		bicLicenseInfo.setBicCode(BeanFactory.getInstance().getNewBICCode(rs.getString("BicCode"), TrafficBand.getVolumBand(rs.getInt("Volume")), rs.getInt("Volume")));

		if (rs.getString("licenseInfo") != null && rs.getString("licenseInfo").length() > 0) {
			BICLicenseInfo biclnsInfo = new BICLicenseInfo();
			LicenseUtils.ParseCheckLicenseInfoString(biclnsInfo, rs.getString("licenseInfo"));
			bicLicenseInfo.setBicLicenseInfo(biclnsInfo);

			// current average equal to messages per month / date of month
			Calendar cal = Calendar.getInstance();
			int currentAverage = Math.round((float) biclnsInfo.getCurrentMonthCount() / cal.get(Calendar.DAY_OF_MONTH));

			bicLicenseInfo.setCurrentAverage(String.valueOf(currentAverage));
		}

		return bicLicenseInfo;
	}
}
