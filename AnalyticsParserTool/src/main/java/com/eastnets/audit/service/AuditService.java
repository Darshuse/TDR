package com.eastnets.audit.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.audit.dao.AuditDAO;

@Service
public class AuditService {

	@Autowired
	AuditDAO auditDAO;

	private static final Logger LOGGER = Logger.getLogger(AuditService.class);

	public void insertIntoErrorld(String errorLevel, String errorMessage) {

		try {
			auditDAO.insertIntoErrorld(errorLevel, errorMessage);
		} catch (Exception e) {
			LOGGER.error("Error inserting audit logs in LDERRORS Table");
			// LOGGER.error(e);
		}
	}
}
