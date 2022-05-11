package com.eastnets.dao.dbconsistencycheck;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BICCodeMapping implements RowMapper<String>{

	@Override
	public String mapRow(ResultSet arg0, int arg1) throws SQLException {
		 String bicCode = arg0.getString("BICCode");
		 return bicCode;

	}

}
