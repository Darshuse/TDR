package com.eastnets.dao.license.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.reporting.licensing.beans.Product;
import com.eastnets.reporting.licensing.util.BeanFactory;



// row mapper for spring 
public class LicensedProductMapper implements Serializable, RowMapper<Product> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6170761800470404047L;

	@Override
	public Product mapRow(final ResultSet rs, final int rowNum)
			throws SQLException {
		return BeanFactory.getInstance().getNewProduct(rs.getString("ID"), rs.getString("Description"),
				rs.getDate("ExpirationDate"), rs.getBoolean("Licensed"));
	}
}
