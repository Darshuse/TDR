package com.eastnets.dao.dbconsistencycheck;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageIDMapping implements RowMapper<String> {

	@Override
	public String mapRow(ResultSet arg0, int arg1) throws SQLException {
		String message_s_umid = "";
		int umidl = Integer.parseInt(((BigDecimal)arg0.getBigDecimal("mesg_s_umidl")).toString());
		int umidh = Integer.parseInt(((BigDecimal)arg0.getBigDecimal("mesg_s_umidh")).toString());
		message_s_umid = Integer.toHexString(umidl).toUpperCase()+Integer.toHexString(umidh).toUpperCase();
		return message_s_umid;
	}

}
