package com.eastnets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.configuration.DaoFactoryConfig;
import com.eastnets.entities.AuditLog;

@Service
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	private DaoFactoryConfig daoFactoryConfig;

	@Override
	public void save(AuditLog auditLog) {

		daoFactoryConfig.getCommonDAOImpl().insertAuditLog(auditLog);

	}

}
