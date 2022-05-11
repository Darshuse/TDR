package com.eastnets.dao;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eastnets.entities.AuditLog;
import com.eastnets.entities.LdErrors;

@Repository
public class CommonSqlDAOImp implements CommonDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void insertAuditLog(AuditLog auditLog) {

		String statment = "INSERT INTO dbo.rAuditLog (LOGINAME, PROGRAM_NAME, EVENT, ACTION,ipAddress,TIMESTAMP)  VALUES ( ?, ?, ? ,?,?,?)";

		jdbcTemplate.update(statment, auditLog.getLoginame(), auditLog.getProgramName(), auditLog.getEvent(),
				auditLog.getAction(), auditLog.getIpAddress(), auditLog.getTimestamp());

	}

	@Override
	public void insertLdErrors(LdErrors ldErrors) {

		String insertStatment = "INSERT INTO dbo.ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(insertStatment, ldErrors.getErrName(), LocalDateTime.now(), ldErrors.getErrorEvel(),
				ldErrors.getErrorModule(), ldErrors.getErrorMesage1(), ldErrors.getErrorMessage2());

	}

}
