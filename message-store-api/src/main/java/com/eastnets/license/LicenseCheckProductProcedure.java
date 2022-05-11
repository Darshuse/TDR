package com.eastnets.license;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.eastnets.util.Constants;

@Configuration
public class LicenseCheckProductProcedure extends StoredProcedure implements Serializable {

	private static final long serialVersionUID = -3703360268988282897L;
	private static final String PROCEDURE_NAME = "sCheckProduct";

	public LicenseCheckProductProcedure(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, PROCEDURE_NAME);
		declareParameter(new SqlParameter("productID", Types.VARCHAR));
		declareParameter(new SqlOutParameter("expirationDate", Types.DATE));
		declareParameter(new SqlOutParameter("licensed", Types.INTEGER));
		compile();
	}

	@Bean
	public ModuleValidationInfo execute() {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("productID", Constants.PRODUCT_ID);

		Map<String, Object> resultMap = execute(parameters);

		ModuleValidationInfo info = new ModuleValidationInfo();

		if (resultMap.get("licensed") != null) {
			info.setExpirationDate((Date) resultMap.get("expirationDate"));
			info.setLicensed(Integer.valueOf(resultMap.get("licensed").toString()));
		} else {
			info.setLicensed(0);
		}
		return info;
	}
}
