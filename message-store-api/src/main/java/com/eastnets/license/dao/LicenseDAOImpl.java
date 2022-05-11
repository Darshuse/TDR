package com.eastnets.license.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eastnets.license.LicenseCheckProductProcedure;
import com.eastnets.license.ModuleValidationInfo;

@Repository
public class LicenseDAOImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModuleValidationInfo checkProduct() throws Exception {
		LicenseCheckProductProcedure checkProductSP = new LicenseCheckProductProcedure(jdbcTemplate);
		return checkProductSP.execute();
	}

}
