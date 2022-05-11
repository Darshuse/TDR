/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.monitoring;

import java.util.Date;
import java.util.List;

import com.eastnets.domain.SortOrder;
import com.eastnets.domain.monitoring.Alliance;
import com.eastnets.domain.monitoring.AuditLog;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.MonitoringLogInfo;
import com.eastnets.domain.monitoring.ReportingDBInfo;
import com.eastnets.domain.monitoring.Statistics;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.service.Service;

/**
 * Monitoring Service Interface
 * 
 * @author EastNets
 * @since July 22, 2012
 */
public interface MonitoringService extends Service {

	/**
	 * Get list of updated messages
	 * 
	 * @param userName
	 * @return List<UpdatedMessage>
	 */
	public List<UpdatedMessage> getUpdatedMessages(String userName, SortOrder order, Long from, Long to);

	/**
	 * Get updated messages Count
	 * 
	 * @param userName
	 * @return Long
	 */
	public Long getUpdatedMessagesCount(String userName);

	/**
	 * Get list of traces
	 * 
	 * @param userName
	 * @return List<ErrorMessage>
	 */
	public List<ErrorMessage> getTraces(String userName);

	/**
	 * Get list of traces
	 * 
	 * @param userName
	 * @param moduleFilter
	 * @param levelFilter
	 * @return List<ErrorMessage>
	 */
	public List<ErrorMessage> getTraces(String userName, String moduleFilter, String levelFilter, SortOrder order, Long from, Long to);

	/**
	 * 
	 * @param userName
	 * @param lastErrorKeyID
	 * @param displayWarnings
	 * @param moduleFilter
	 * @param levelFilter
	 * @return
	 */
	public Long getTracesCount(String userName, String moduleFilter, String levelFilter);

	/**
	 * Get statistics
	 * 
	 * @param userName
	 * @return
	 */
	public Statistics getStatistics(String userName);

	/**
	 * Get reporting database information
	 * 
	 * @param userName
	 * @return ReportingDBInfo
	 */
	public ReportingDBInfo getReportingDBInfo(String userName);

	/**
	 * 
	 * @param userName
	 * @return List<String>
	 */
	public List<String> getLoginNamesList(String programNameFilter, String eventFilter);

	/**
	 * 
	 * @param userName
	 * @return List<String>
	 */
	public List<String> getProgramsNamesList(String loginNameFilter, String eventFilter);

	/**
	 * 
	 * @param userName
	 * @return List<String>
	 */
	public List<String> getEventsList(String loginNameFilter, String programNameFilter);

	/**
	 * 
	 * @param userName
	 * @param lastErrorKeyID
	 * @param displayWarnings
	 * @param levelFilter
	 * @return
	 */
	public List<String> getModulesList(String userName, String levelFilter);

	/**
	 * 
	 * @param userName
	 * @param lastErrorKeyID
	 * @param displayWarnings
	 * @param moduleFilter
	 * @return
	 */
	public List<String> getLevelsList(String userName, String moduleFilter);

	/**
	 * Get alliance info by id
	 * 
	 * @param userName
	 * @param ID
	 * @return Alliance
	 */
	public Alliance getAlliance(String userName, String ID);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<String> getApplicationsList(String userName);

	/**
	 * 
	 * @param userName
	 * @param loginNameFilter
	 * @param programNameFilter
	 * @param eventFilter
	 * @return
	 */
	public List<AuditLog> getAuditLogs(String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, Date fromDate, Date toDate, String ipAddress, SortOrder order, Long from, Long to);

	/**
	 * 
	 * @param userName
	 * @param loginNameFilter
	 * @param programNameFilter
	 * @param eventFilter
	 * @return
	 */
	public Long getAuditLogsCount(String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, Date fromDate, Date toDate, String ipAddress);

	/**
	 * @param userName
	 * @param logInfo
	 *            informations needed to generate the log file
	 */
	public String getLogData(String userName, MonitoringLogInfo logInfo, List<AuditLog> auditLogs, List<ErrorMessage> tracesRecords);

	public String getLogsDetails(AuditLog auditLog);

	public List<AuditLog> getAuditLogFile(boolean showDetails, String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, Date fromDate, Date toDate, String ipAddress, SortOrder order);

	public StringBuilder exportAuditLogsCSV(List<AuditLog> auditLogs, String csvSeperator, boolean showDetails);

	public StringBuilder exportAuditLogsDetailsCSV(AuditLog auditLog, String csvSeperator);

}
