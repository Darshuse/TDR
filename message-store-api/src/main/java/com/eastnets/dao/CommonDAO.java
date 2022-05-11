package com.eastnets.dao;

import com.eastnets.entities.AuditLog;
import com.eastnets.entities.LdErrors;

public interface CommonDAO {

	void insertAuditLog(AuditLog auditLog);

	void insertLdErrors(LdErrors ldErrors);

}
