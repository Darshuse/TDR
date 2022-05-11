package com.eastnets.audit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eastnets.message.summary.configuration.GlobalConfiguration;

@Repository
public class AuditDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	GlobalConfiguration globalConfiguration;

	public void insertIntoErrorld(String errorLevel, String errorMessage) {
		if (globalConfiguration.getDbType().equalsIgnoreCase("ORACLE")) {
			Long mesgID = jdbcTemplate.queryForObject("select LDERRORS_ID.NEXTVAL from dual", Long.class);
			String insertQuery = "INSERT INTO ldErrors(ErrID,ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?,?)";
			jdbcTemplate.update(insertQuery, mesgID, "Messages Migration", new java.util.Date(), errorLevel,
					"Messages Migration", "", errorMessage);

		} else if (globalConfiguration.getDbType().equalsIgnoreCase("SQL")) {
			String insertQuery = "INSERT INTO ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
			jdbcTemplate.update(insertQuery, "Messages Migration", new java.util.Date(), errorLevel,
					"Messages Migration", "", errorMessage);
		}
	}
}
