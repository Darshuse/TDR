package com.eastnets.dao;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.AuditLog;
import com.eastnets.entities.LdErrors;

@Repository
public class CommonOracleDAOImp implements CommonDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void insertAuditLog(AuditLog auditLog) {

		String sql = "select side.rAuditLog_ID.nextval from dual";
		Long id = jdbcTemplate.queryForObject(sql, Long.class);

		String statment = "INSERT INTO side.rAuditLog (ID, LOGINAME, PROGRAM_NAME, EVENT, ACTION,IPADDRESS, TIMESTAMP ) VALUES (?,?,?,?,?,?,?)";

		jdbcTemplate.update(statment, id, auditLog.getLoginame(), auditLog.getProgramName(), auditLog.getEvent(),
				auditLog.getAction(), auditLog.getIpAddress(), auditLog.getTimestamp());

	}

	@Override
	public void insertLdErrors(LdErrors ldErrors) {

		String sql = "select LDERRORS_ID.NEXTVAL from dual";
		Long id = jdbcTemplate.queryForObject(sql, Long.class);

		String insertStatment = "INSERT INTO ldErrors(ErrID,ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertStatment, id, ldErrors.getErrName(), LocalDateTime.now(), ldErrors.getErrorEvel(),
				ldErrors.getErrorModule(), ldErrors.getErrorMesage1(), ldErrors.getErrorMessage2());

	}

}
