package com.eastnets.dao.license.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.TrafficBand;
import com.eastnets.reporting.licensing.util.BeanFactory;

// row mapper for spring 
public class LicensedBicMapper implements Serializable, RowMapper<BicCode> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5419912388900906024L;

	@Override
	public BicCode mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return BeanFactory.getInstance().getNewBICCode(rs.getString("BicCode"), TrafficBand.getVolumBand(rs.getInt("Volume")), rs.getInt("Volume"));
	}
}
