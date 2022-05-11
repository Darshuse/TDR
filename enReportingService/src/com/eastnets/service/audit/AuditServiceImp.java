package com.eastnets.service.audit;

import java.util.List;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.service.ServiceBaseImp;

public class AuditServiceImp extends ServiceBaseImp implements AuditService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3269974852944235649L;
	private CommonDAO commonDAO;

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	@Override
	public void logAudit(String loggedInUser, String programName, String event, String action, String ipAddress) {
		commonDAO.auditDAO(loggedInUser, programName, event, action, null, ipAddress);

	}

	@Override
	public void logMaskingInformationAudit(String loginName, String programName, String event, String action, List<AuditLogDetails> auditLogDetailsList, String ipAddress) {
		commonDAO.auditDAO(loginName, programName, event, action, auditLogDetailsList, ipAddress);

	}

}
