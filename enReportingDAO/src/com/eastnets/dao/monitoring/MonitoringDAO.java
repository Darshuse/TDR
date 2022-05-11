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

package com.eastnets.dao.monitoring;

import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.SortOrder;
import com.eastnets.domain.monitoring.Alliance;
import com.eastnets.domain.monitoring.AuditLog;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.MonitoringEventInfo;
import com.eastnets.domain.monitoring.MonitoringMessageInfo;
import com.eastnets.domain.monitoring.ReportingDBInfo;
import com.eastnets.domain.monitoring.Statistics;
import com.eastnets.domain.monitoring.UpdatedMessage;

/**
 * Monitoring DAO Interface
 * @author EastNets
 * @since July 19, 2012
 */
public interface MonitoringDAO extends DAO {
	
	/**
	 * Get updated messages based on last file key id, with option to display journals or not
	 * @param lastFileKeyID
	 * @param displayJournals
	 * @return List<UpdatedMessage>
	 */
	public List<UpdatedMessage> getUpdatedMessages(int lastFileKeyID, boolean displayJournals, SortOrder order,Long from, Long to);
	
	/**
	 * Get last file key id
	 * @param date
	 * @return int
	 */
	public int getLastFileKeyID(String date);
	
	/**
	 * Get loader statistics
	 * @return Statistics
	 */
	public Statistics getLoaderStatistics();
	
	/**
	 * Get journals count
	 * @return long
	 */
	public long getJournalsCount();
	
	/**
	 * Get error messages based on last error key id with option to display warnings or not
	 * @param lastErrorKeyID
	 * @param displayWarnings
	 * @return List<ErrorMessage>
	 */
	public List<ErrorMessage> getErrorMessages(int lastErrorKeyID, boolean displayWarnings);

	/**
	 * 
	 * @param lastErrorKeyID
	 * @param displayWarnings
	 * @param moduleFilter
	 * @param levelFilter
	 * @param order
	 * @param from
	 * @param to
	 * @return
	 */
	public List<ErrorMessage> getErrorMessages(int lastErrorKeyID, boolean displayWarnings, String moduleFilter, String levelFilter, SortOrder order, Long from, Long to);
	
	/**
	 * 
	 * @param lastErrorKeyID
	 * @param displayWarnings
	 * @param moduleFilter
	 * @param levelFilter
	 * @param order
	 * @param from
	 * @param to
	 * @return Long
	 */
	public Long getErrorMessagesCount(int lastErrorKeyID, boolean displayWarnings, String moduleFilter, String levelFilter);
	
	/**
	 * Get last error key id
	 * @param date
	 * @return int
	 */
	public int getLastErrorKeyID(String date);
	
	/**
	 * 
	 * @param formatDateTime
	 * @param loginNameFilter
	 * @param programNameFilter
	 * @param eventFilter
	 * @param order
	 * @param from
	 * @param to
	 * @return
	 */
	public List<AuditLog> getAuditLogs(String loginNameFilter, String programNameFilter,String eventFilter,String actionFilter,String fromDate , String toDate ,String ipAddress, SortOrder order, Long from, Long to);
	
	/**
	 * 
	 * @param formatDateTime
	 * @param loginNameFilter
	 * @param programNameFilter
	 * @param eventFilter
	 * @return
	 */
	public Long getAuditLogsCount(String loginNameFilter, String programNameFilter,String eventFilter,String actionFilter,String fromDate ,String toDate ,String ipAddress);
	
	/**
	 * Get reporting database info
	 * @return ReportingDBInfo
	 */
	public ReportingDBInfo getReportingDBInfo();
	
	/**
	 * Get alliance
	 * @param ID
	 * @return Alliance
	 */
	public Alliance getAlliance(String ID);
	
	/**
	 * Get monitoring message information
	 * @param aid
	 * @param umidl
	 * @param umidh
	 * @return MonitoringMessageInfo
	 */
	public MonitoringMessageInfo getMessage(String aid, String umidl, String umidh);
	
	/**
	 * Get monitoring event information
	 * @param aid
	 * @param jrnlRevDate
	 * @param jrnlSeqNumber
	 * @return MonitoringEventInfo
	 */
	public MonitoringEventInfo getEvent(String aid, String jrnlRevDate, String jrnlSeqNumber);
	
	/**
	 * 
	 * @return
	 */
	public List<String> getLoginNamesList(String date, String programNameFilter, String eventFilter);
	
	/**
	 * 
	 * @return
	 */
	public List<String> getProgramsNamesList(String date,String loginNameFilter, String eventFilter);
	
	/**
	 * 
	 * @return
	 */
	public List<String> getEventsList(String date, String loginNameFilter, String programNameFilter);

	

	/**
	 * 
	 * @param date
	 * @param levelFilter
	 * @return List<String> 
	 */
	public List<String> getModulesList(int lastErrorKeyID, boolean displayWarnings, String levelFilter);
	
	
	/**
	 * 
	 *
	 * @param applicationsList
	 * @return List<String> 
	 */
	public List<String> getApplicationsList();
	
	
	/**
	 * 
	 * @param date
	 * @param moduleFilter
	 * @return List<String> 
	 */
	public List<String> getLevelsList(int lastErrorKeyID, boolean displayWarnings, String moduleFilter);

	/**
	 * 
	 * @param lastFileKeyID
	 * @param displayJournals
	 * @return
	 */
	public Long getUpdatedMessagesCount(int lastFileKeyID, boolean displayJournals);
	public List<AuditLog> getAuditLogFile(boolean showDetails,String loginNameFilter, String programNameFilter,String eventFilter,String actionFilter,String fromDate , String toDate ,String ipAddress, SortOrder order);


}
