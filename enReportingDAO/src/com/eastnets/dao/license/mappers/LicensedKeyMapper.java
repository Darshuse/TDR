package com.eastnets.dao.license.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

// row mapper for spring 
public class LicensedKeyMapper implements Serializable, RowMapper<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7882240665179543638L;

	@Override
	public String mapRow(final ResultSet rs, final int rowNum)
			throws SQLException {
		return rs.getString("LICENSEKEY");
	}
}
