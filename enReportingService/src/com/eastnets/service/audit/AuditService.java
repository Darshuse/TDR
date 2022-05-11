package com.eastnets.service.audit;

import java.util.List;

import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.service.Service;

public interface AuditService extends Service {

	public void logAudit(String loggedInUser, String programName, String event, String action, String ipAddress);

	public void logMaskingInformationAudit(String loginName, String programName, String event, String action, List<AuditLogDetails> auditLogDetailsList, String ipAddress);
}
