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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.dao.monitoring.MonitoringDAO;
import com.eastnets.domain.DatabaseInfo;
import com.eastnets.domain.SortOrder;
import com.eastnets.domain.monitoring.Alliance;
import com.eastnets.domain.monitoring.AuditLog;
import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.MonitoringConfig;
import com.eastnets.domain.monitoring.MonitoringLogInfo;
import com.eastnets.domain.monitoring.ReportingDBInfo;
import com.eastnets.domain.monitoring.Statistics;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.utils.ApplicationUtils;

/**
 * Monitoring Service Implementation
 * 
 * @author EastNets
 * @since July 22, 2012
 */
public class MonitoringServiceImp extends ServiceBaseImp implements MonitoringService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3228588177025325458L;
	private static final String EMPTY_CHAR = "-";
	private MonitoringDAO monitoringDAO;
	private CommonDAO commonDAO;
	private MonitoringConfig monitoringConfig;
	private DBPortabilityHandler dbPortabilityHandler;

	public MonitoringDAO getMonitoringDAO() {
		return monitoringDAO;
	}

	public void setMonitoringDAO(MonitoringDAO monitoringDAO) {
		this.monitoringDAO = monitoringDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public MonitoringConfig getMonitoringConfig() {
		return monitoringConfig;
	}

	public void setMonitoringConfig(MonitoringConfig monitoringConfig) {
		this.monitoringConfig = monitoringConfig;
	}

	@Override
	public List<UpdatedMessage> getUpdatedMessages(String userName, SortOrder order, Long from, Long to) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastFileKeyID = monitoringDAO.getLastFileKeyID(formatDateTime);
		List<UpdatedMessage> updatedMessages = monitoringDAO.getUpdatedMessages(lastFileKeyID, monitoringConfig.isDisplayJournals(), order, from, to);
		for (UpdatedMessage message : updatedMessages) {
			if (message.getOverrun() != null && !message.getOverrun().equals("") && message.getOverrun().equalsIgnoreCase("1")) {
				message.setOverrun("Yes");
			} else {
				message.setOverrun("No");
			}

			String updateStatus = message.getStatus();
			if (updateStatus.equalsIgnoreCase("0")) {
				message.setStatus("No Updates");
			} else if (updateStatus.equalsIgnoreCase("2")) {
				message.setStatus("Failed");
			} else {
				message.setStatus("Succeeded");
			}

			message.setNewMsgCount(getEmptyIfZeroOrNull(message.getNewMsgCount()));
			message.setUpdateMsgCount(getEmptyIfZeroOrNull(message.getUpdateMsgCount()));
			message.setNotifiedMsgCount(getEmptyIfZeroOrNull(message.getNotifiedMsgCount()));
			message.setOnTimeCount(getEmptyIfZeroOrNull(message.getOnTimeCount()));
			message.setFailedCount(getEmptyIfZeroOrNull(message.getFailedCount()));
			message.setErrorCount(getEmptyIfZeroOrNull(message.getErrorCount()));
			message.setWarningCount(getEmptyIfZeroOrNull(message.getWarningCount()));
			message.setJrnlMsgCount(getEmptyIfZeroOrNull(message.getJrnlMsgCount()));
			message.setRefreshCount(getEmptyIfZeroOrNull(message.getRefreshCount()));

			message.setOrigin(getbinarySimplification((message.getOrigin())));

			if (!message.getOnTimeCount().equals("") && !message.getUpdateMsgCount().equals("")) {
				Integer onTimeCount = new Integer(message.getOnTimeCount());
				Integer updatedMessagesCount = new Integer(message.getUpdateMsgCount());
				Integer onTimeCountFinal = (onTimeCount / updatedMessagesCount) * 100;
				message.setOnTimeCount(onTimeCountFinal.toString() + "%");

			}
		}

		return updatedMessages;
	}

	@Override
	public Long getUpdatedMessagesCount(String userName) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastFileKeyID = monitoringDAO.getLastFileKeyID(formatDateTime);
		Long count = monitoringDAO.getUpdatedMessagesCount(lastFileKeyID, monitoringConfig.isDisplayJournals());

		return count;
	}

	private String getbinarySimplification(String string) {

		String stringAsBinary = new StringBuilder(Integer.toBinaryString(new Integer(string))).reverse().toString();
		StringBuilder simplifiedBinary = new StringBuilder();

		if (stringAsBinary.equals("0")) {
			return "0";
		}

		for (int index = 0; index < stringAsBinary.length(); index++) {
			if (stringAsBinary.substring(index, index + 1).equals("0")) {
				continue;
			}

			switch (index) {
			case 0:
				simplifiedBinary.append("1");
				break;

			case 1:
				simplifiedBinary.append("2");
				break;

			case 2:
				simplifiedBinary.append("4");
				break;

			case 3:
				simplifiedBinary.append("8");
				break;

			case 4:
				simplifiedBinary.append("16");
				break;

			default:
				simplifiedBinary.append("Out Of Range");
				break;
			}
		}

		return simplifiedBinary.toString();
	}

	private String getEmptyIfZeroOrNull(String value) {
		if (value == null || value.equalsIgnoreCase("0")) {
			return "";
		}

		return value;
	}

	private Date getDatePeriod(int numberOfDays) {
		Calendar currentCalendar = Calendar.getInstance();
		int i = -numberOfDays;
		currentCalendar.add(Calendar.DAY_OF_YEAR, i);
		Date time = currentCalendar.getTime();
		return time;
	}

	@Override
	public Statistics getStatistics(String userName) {
		Statistics statistics = monitoringDAO.getLoaderStatistics();

		if (statistics.getTotalCount() > 0) {
			double totalCount = statistics.getTotalCount();
			statistics.setLiveCountPercentage(Math.round(statistics.getLiveCount() * 100 / totalCount) + "%");
			statistics.setCompleteCountPercentage(Math.round(statistics.getCompleteCount() * 100 / totalCount) + "%");
			statistics.setArchiveCountPercentage(Math.round(statistics.getArchiveCount() * 100 / totalCount) + "%");
			statistics.setIncompleteCountPercentage(Math.round(statistics.getIncompleteCount() * 100 / totalCount) + "%");
		}

		statistics.setJournalCount(monitoringDAO.getJournalsCount());

		return statistics;
	}

	@Override
	public List<ErrorMessage> getTraces(String userName) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastErrorKeyID = monitoringDAO.getLastErrorKeyID(formatDateTime);
		List<ErrorMessage> errorMessages = monitoringDAO.getErrorMessages(lastErrorKeyID, monitoringConfig.isDisplayWarnings());

		for (ErrorMessage message : errorMessages) {

			if (message.getErrMsg2() != null && !message.getErrMsg2().equals("")) {
				message.setDescription(message.getErrMsg2());
				if (message.getErrModule() != null && !message.getErrModule().equals("")) {
					message.setDescription(message.getErrModule() + ":" + message.getErrMsg2());
				}
			}

			if (message.getErrMsg1() != null && !message.getErrMsg1().equals("")) {
				message.setTimeStamp(null);
				message.setErrModule("");
				message.setErrLevel("");
				message.setDescription(message.getErrMsg1());
			}
		}

		return errorMessages;
	}

	@Override
	public List<ErrorMessage> getTraces(String userName, String moduleFilter, String levelFilter, SortOrder order, Long from, Long to) {

		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastErrorKeyID = monitoringDAO.getLastErrorKeyID(formatDateTime);
		List<ErrorMessage> errorMessages = monitoringDAO.getErrorMessages(lastErrorKeyID, monitoringConfig.isDisplayWarnings(), moduleFilter, levelFilter, order, from, to);

		for (ErrorMessage message : errorMessages) {

			String errorMessage2 = message.getErrMsg2();
			if (errorMessage2 != null && !errorMessage2.equals("")) {
				errorMessage2 = errorMessage2.replace("\\r\\n", "<br />");
				message.setDescription(errorMessage2);
				if (message.getErrModule() != null && !message.getErrModule().equals("")) {
					message.setDescription(message.getErrModule() + ":" + errorMessage2);
				}
			}

			String errorMessage1 = message.getErrMsg1();
			if (errorMessage1 != null && !errorMessage1.equals("")) {
				errorMessage1 = errorMessage1.replace("\\r\\n", "<br />");
				message.setTimeStamp(null);
				message.setErrModule("");
				message.setErrLevel("");
				message.setDescription(errorMessage1);
			}
		}

		return errorMessages;
	}

	@Override
	public Long getTracesCount(String userName, String moduleFilter, String levelFilter) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastErrorKeyID = monitoringDAO.getLastErrorKeyID(formatDateTime);

		return monitoringDAO.getErrorMessagesCount(lastErrorKeyID, monitoringConfig.isDisplayWarnings(), moduleFilter, levelFilter);
	}

	@Override
	public List<AuditLog> getAuditLogs(String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, Date fromDate, Date toDate, String ipAddress, SortOrder order, Long from, Long to) {

		String fromDateString = ApplicationUtils.formatDateTime(fromDate);
		String toDateString = ApplicationUtils.formatDateTime(toDate);
		// loginNameFilter=loginNameFilter.replace("'", "''");
		List<AuditLog> auditLogs = monitoringDAO.getAuditLogs(loginNameFilter, programNameFilter, eventFilter, actionFilter, fromDateString, toDateString, ipAddress, order, from, to);

		for (AuditLog auditLog : auditLogs) {
			auditLog.setProgramName(auditLog.getProgramName().replace(".exe", ""));
		}

		return auditLogs;
	}

	@Override
	public List<AuditLog> getAuditLogFile(boolean showDetails, String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, Date fromDate, Date toDate, String ipAddress, SortOrder order) {

		String fromDateString = ApplicationUtils.formatDateTime(fromDate);
		String toDateString = ApplicationUtils.formatDateTime(toDate);

		List<AuditLog> auditLogs = monitoringDAO.getAuditLogFile(showDetails, loginNameFilter, programNameFilter, eventFilter, actionFilter, fromDateString, toDateString, ipAddress, order);
		for (AuditLog auditLog : auditLogs) {
			auditLog.setProgramName(auditLog.getProgramName().replace(".exe", ""));
		}

		return auditLogs;
	}

	@Override
	public Long getAuditLogsCount(String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, Date fromDate, Date toDate, String ipAddress) {

		String fromDateString = ApplicationUtils.formatDateTime(fromDate);
		String toDateString = ApplicationUtils.formatDateTime(toDate);

		return monitoringDAO.getAuditLogsCount(loginNameFilter, programNameFilter, eventFilter, actionFilter, fromDateString, toDateString, ipAddress);

	}

	@Override
	public ReportingDBInfo getReportingDBInfo(String userName) {
		ReportingDBInfo reportingDBInfo = monitoringDAO.getReportingDBInfo();
		if (reportingDBInfo != null) {
			if (reportingDBInfo.getInstallationUser().trim().equalsIgnoreCase("INSTALL")) {
				reportingDBInfo.setLastDatabaseUpdate("Build date " + reportingDBInfo.getInstallationDate());
			} else {
				reportingDBInfo.setLastDatabaseUpdate(reportingDBInfo.getInstallationDate() + " By " + reportingDBInfo.getInstallationUser().trim());
			}

			reportingDBInfo.setDatabaseVersion(reportingDBInfo.getMajor() + "." + reportingDBInfo.getMinor() + "." + reportingDBInfo.getRevision());
		} else {
			reportingDBInfo = new ReportingDBInfo();
			reportingDBInfo.setLastDatabaseUpdate(EMPTY_CHAR);
		}

		return reportingDBInfo;
	}

	@Override
	public Alliance getAlliance(String userName, String ID) {
		Alliance alliance = monitoringDAO.getAlliance(ID);

		if (alliance != null) {
			alliance.setInstanceName(getEmptyCharIfEmptyOrNull(alliance.getInstanceName()));
			if (isEmptyOrNull(alliance.getLastUmidl())) {
				alliance.getLastProcessedMessage().setMesgCreaDateTime(EMPTY_CHAR);
				alliance.getLastProcessedMessage().setMesgUUMID(EMPTY_CHAR);
				alliance.getLastProcessedMessage().setMesgUUMIDSuffix(EMPTY_CHAR);
			} else {
				alliance.setLastProcessedMessage(monitoringDAO.getMessage(alliance.getAid(), alliance.getLastUmidl(), alliance.getLastUmidh()));
			}

			if (isEmptyOrNull(alliance.getJrnlSeqNbr())) {
				alliance.getLastProcessedEvent().setDateTime(EMPTY_CHAR);
				alliance.getLastProcessedEvent().setDescription(EMPTY_CHAR);
			} else {
				alliance.setLastProcessedEvent(monitoringDAO.getEvent(alliance.getAid(), alliance.getJrnlRevDateTime(), alliance.getJrnlSeqNbr()));
			}

			alliance.setInstanceName(getEmptyCharIfEmptyOrNull(alliance.getInstanceName()));

		} else {
			alliance = new Alliance();
		}

		return alliance;
	}

	private String getEmptyCharIfEmptyOrNull(String value) {
		if (isEmptyOrNull(value)) {
			return EMPTY_CHAR;
		}

		return value;
	}

	private boolean isEmptyOrNull(String value) {
		return value == null || value.trim().equals("");
	}

	public List<String> getLoginNamesList(String programNameFilter, String eventFilter) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		return monitoringDAO.getLoginNamesList(formatDateTime, programNameFilter, eventFilter);
	}

	public List<String> getProgramsNamesList(String loginNameFilter, String eventFilter) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		return monitoringDAO.getProgramsNamesList(formatDateTime, loginNameFilter, eventFilter);
	}

	public List<String> getEventsList(String loginNameFilter, String programNameFilter) {
		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		return monitoringDAO.getEventsList(formatDateTime, loginNameFilter, programNameFilter);
	}

	@Override
	public List<String> getModulesList(String userName, String levelFilter) {

		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastErrorKeyID = monitoringDAO.getLastErrorKeyID(formatDateTime);

		return monitoringDAO.getModulesList(lastErrorKeyID, monitoringConfig.isDisplayWarnings(), levelFilter);
	}

	@Override
	public List<String> getApplicationsList(String userName) {
		return monitoringDAO.getApplicationsList();
	}

	@Override
	public List<String> getLevelsList(String userName, String moduleFilter) {

		Date datePeriod = getDatePeriod(monitoringConfig.getDayHistory());
		String formatDateTime = ApplicationUtils.formatDateTime(datePeriod);
		int lastErrorKeyID = monitoringDAO.getLastErrorKeyID(formatDateTime);

		return monitoringDAO.getLevelsList(lastErrorKeyID, monitoringConfig.isDisplayWarnings(), moduleFilter);
	}

	@Override
	public StringBuilder exportAuditLogsCSV(List<AuditLog> auditLogs, String csvSeperator, boolean showDetails) {

		StringBuilder csvBuilder = new StringBuilder();
		// Generate Header
		csvBuilder.append("\"" + "TimeStamp" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Login" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Program Name" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Event" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "IP Address" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Action" + "\"" + csvSeperator);

		if (showDetails) {
			csvBuilder.append("\"" + "Field Name" + "\"" + csvSeperator);
			csvBuilder.append("\"" + "Old Value" + "\"" + csvSeperator);
			csvBuilder.append("\"" + "New Value" + "\"" + csvSeperator);
		}

		csvBuilder.append("\r\n");

		for (AuditLog auditLog : auditLogs) {
			// we written the code as below because the check inside the append affect the design
			if (auditLog.getIpAddress() == null) {
				auditLog.setIpAddress(" ");
			}

			csvBuilder.append("\"" + ApplicationUtils.formatDate(auditLog.getTimeStamp(), Constants.MSSQL_DATE_TIME_PATTERN) + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLog.getLoginName() + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLog.getProgramName() + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLog.getEvent() + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLog.getIpAddress() + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLog.getAction() + "\"" + csvSeperator);

			if (showDetails) {
				int count = 0;
				for (AuditLogDetails auditLogDetailBean : auditLog.getAuditLogDetailsList()) {
					if (count != 0) {
						csvBuilder.append("\r\n");
						csvBuilder.append("\"" + " " + "\"" + csvSeperator);
						csvBuilder.append("\"" + " " + "\"" + csvSeperator);
						csvBuilder.append("\"" + " " + "\"" + csvSeperator);
						csvBuilder.append("\"" + " " + "\"" + csvSeperator);
						csvBuilder.append("\"" + " " + "\"" + csvSeperator);
						csvBuilder.append("\"" + " " + "\"" + csvSeperator);

					}
					count++;

					// we written the code as below because the check inside the append affect the design
					if (auditLogDetailBean.getOldValue() == null) {
						auditLogDetailBean.setOldValue(" ");
					}
					if (auditLogDetailBean.getNewValue() == null) {
						auditLogDetailBean.setNewValue(" ");
					}

					csvBuilder.append("\"" + auditLogDetailBean.getFieldName() + "\"" + csvSeperator);
					csvBuilder.append("\"" + auditLogDetailBean.getOldValue() + "\"" + csvSeperator);
					csvBuilder.append("\"" + auditLogDetailBean.getNewValue() + "\"" + csvSeperator);

				}
			}

			csvBuilder.append("\r\n");
		}

		return csvBuilder;

	}

	@Override
	public StringBuilder exportAuditLogsDetailsCSV(AuditLog auditLog, String csvSeperator) {
		StringBuilder csvBuilder = new StringBuilder();

		// we written the code as below because the check inside the append affect the design
		if (auditLog.getIpAddress() == null) {
			auditLog.setIpAddress(" ");
		}

		// Generate Header
		csvBuilder.append("\"" + "TimeStamp" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Login" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Program Name" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Event" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "IP Address" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Action" + "\"" + csvSeperator);

		csvBuilder.append("\"" + "Field Name" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "Old Value" + "\"" + csvSeperator);
		csvBuilder.append("\"" + "New Value" + "\"" + csvSeperator);

		csvBuilder.append("\r\n");
		csvBuilder.append("\"" + ApplicationUtils.formatDate(auditLog.getTimeStamp(), Constants.MSSQL_DATE_TIME_PATTERN) + "\"" + csvSeperator);
		csvBuilder.append("\"" + auditLog.getLoginName() + "\"" + csvSeperator);
		csvBuilder.append("\"" + auditLog.getProgramName() + "\"" + csvSeperator);
		csvBuilder.append("\"" + auditLog.getEvent() + "\"" + csvSeperator);
		csvBuilder.append("\"" + auditLog.getIpAddress() + "\"" + csvSeperator);
		csvBuilder.append("\"" + auditLog.getAction() + "\"" + csvSeperator);

		int count = 0;
		for (AuditLogDetails auditLogDetailBean : auditLog.getAuditLogDetailsList()) {
			if (count != 0) {
				csvBuilder.append("\r\n");
				csvBuilder.append("\"" + " " + "\"" + csvSeperator);
				csvBuilder.append("\"" + " " + "\"" + csvSeperator);
				csvBuilder.append("\"" + " " + "\"" + csvSeperator);
				csvBuilder.append("\"" + " " + "\"" + csvSeperator);
				csvBuilder.append("\"" + " " + "\"" + csvSeperator);
				csvBuilder.append("\"" + " " + "\"" + csvSeperator);

			}
			count++;

			// we written the code as below because the check inside the append affect the design
			if (auditLogDetailBean.getOldValue() == null) {
				auditLogDetailBean.setOldValue(" ");
			}
			if (auditLogDetailBean.getNewValue() == null) {
				auditLogDetailBean.setNewValue(" ");
			}

			csvBuilder.append("\"" + auditLogDetailBean.getFieldName() + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLogDetailBean.getOldValue() + "\"" + csvSeperator);
			csvBuilder.append("\"" + auditLogDetailBean.getNewValue() + "\"" + csvSeperator);
		}

		return csvBuilder;
	}

	@Override
	public String getLogsDetails(AuditLog auditLog) {
		// NOT USED
		StringBuilder print = new StringBuilder();
		// below code written to make sure that each column and its value reserve the same space in whole file
		int dateStringLength = ApplicationUtils.formatDateTime(auditLog.getTimeStamp()).length();
		int longestLogin = 6;
		int longestProgramName = 13;
		int longestEvent = 5;
		int longestAction = 0;
		if (longestProgramName < auditLog.getProgramName().length()) {
			longestProgramName = auditLog.getProgramName().length();
		}
		if (longestLogin < auditLog.getLoginName().length()) {
			longestLogin = auditLog.getLoginName().length();
		}
		if (longestEvent < auditLog.getEvent().length()) {
			longestEvent = auditLog.getEvent().length();
		}
		if (longestAction < auditLog.getAction().length()) {
			longestAction = auditLog.getAction().length();
		}

		print.append(StringUtils.left("TimeStamp" + space(dateStringLength), dateStringLength)).append("   ").append(StringUtils.left("Login" + space(longestLogin), longestLogin)).append("   ")
				.append(StringUtils.left("Program Name" + space(longestProgramName), longestProgramName)).append("   ").append(StringUtils.left("Event" + space(longestEvent), longestEvent)).append("   ")
				.append(StringUtils.left("IP Address" + space(15), 15)).append("   ").append(StringUtils.left("Action" + space(longestAction), longestAction));

		print.append("-------------------------------------------------------------------------------------" + "-------------------------------------------------------------------------------");

		print.append("\r\n").append(StringUtils.left(ApplicationUtils.formatDateTime(auditLog.getTimeStamp()) + space(dateStringLength), dateStringLength)).append("   ")
				.append(StringUtils.left(StringUtils.defaultString(auditLog.getLoginName()) + space(longestLogin), longestLogin)).append("   ");

		String programName = StringUtils.defaultString(auditLog.getProgramName()).replace(".exe", "");

		print.append(StringUtils.left(programName + space(longestProgramName), longestProgramName)).append("   ").append(StringUtils.left(StringUtils.defaultString(auditLog.getEvent()) + space(longestEvent), longestEvent)).append("   ")
				.append(StringUtils.left(StringUtils.defaultString(auditLog.getIpAddress()) + space(15), 15)).append("   ").append(StringUtils.left(StringUtils.defaultString(auditLog.getAction()) + space(longestAction), longestAction));

		if (!auditLog.getAuditLogDetailsList().isEmpty()) {
			print.append("\r\n");

			for (AuditLogDetails auditLogDetailBean : auditLog.getAuditLogDetailsList()) {
				print.append("\r\n").append(" - Field Name: ").append(auditLogDetailBean.getFieldName()).append("\r\n").append(" - Old Value: ").append(auditLogDetailBean.getOldValue()).append("\r\n").append(" - New Value: ")
						.append(auditLogDetailBean.getNewValue()).append("\r\n");
			}
		}

		return print.toString();
	}

	@Override
	public String getLogData(String userName, MonitoringLogInfo logInfo, List<AuditLog> auditLogs, List<ErrorMessage> traces) {

		StringBuilder print = new StringBuilder();
		if (!logInfo.isIncludeAuditLogs()) {
			print.append("enReporting ").append(logInfo.getAppVersion()).append("\r\n");
			print.append("Current date-time : ").append(ApplicationUtils.formatDateTime(new Date())).append("\r\n");

			String msg = "Current DB : " + StringUtils.defaultString(logInfo.getDatabaseName(), "Unknown") + ", Server name : [" + StringUtils.defaultString(logInfo.getDatabaseServer(), "Unknown") + "]," + " Type : ";
			if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
				msg = msg + "SQL Server";
			} else {
				msg = msg + "ORACLE";
			}
			print.append(msg).append("\r\n");
			print.append("-----------------------------------------------------------------------------------").append("\r\n").append("\r\n");

			// -------------------------------------------------------------------------------------------
			// DB Info
			// -------------------------------------------------------------------------------------------

			List<DatabaseInfo> databaseInfo = commonDAO.getDatabaseInfo();
			print.append("DB Informations :").append("\r\n");
			print.append("=================").append("\r\n");
			if (!databaseInfo.isEmpty()) {
				for (DatabaseInfo df : databaseInfo) {
					if ("INSTALL".equalsIgnoreCase(df.getInstallationUser().trim())) {
						print.append("Build date ").append(ApplicationUtils.formatDateTime(df.getInstallationDate())).append(" ").append(df.getMajor()).append(".").append(df.getMinor()).append(".").append(df.getRevision()).append("\r\n");
					} else {
						print.append("Update     ").append(ApplicationUtils.formatDateTime(df.getInstallationDate())).append(" by ").append(df.getInstallationUser().trim()).append(" ").append(df.getMajor()).append(".").append(df.getMinor())
								.append(".").append(df.getRevision()).append("\r\n");
					}
				}

			} else {
				print.append("No information found.").append("\r\n");
			}
			print.append("\r\n");
		}

		// we need to know the max length a date string can reach so that we can align the log data
		Calendar c = Calendar.getInstance();
		c.set(2013, 12, 22, 12, 55, 55);
		int dateStringLength = ApplicationUtils.formatDateTime(c.getTime()).length();

		// -------------------------------------------------------------------------------------------
		// Traces
		// -------------------------------------------------------------------------------------------
		if (logInfo.isIncludeTraces()) {
			print.append("Traces :").append("\r\n");
			print.append("========").append("\r\n");

			int longestModule = 6;
			int longestLevel = 6;
			int longestDescription = 13;

			for (ErrorMessage trace : traces) {
				int moduleLength = trace.getApplicationName().length();
				int levelLength = trace.getErrLevel().length();

				if (longestModule < moduleLength) {
					longestModule = moduleLength;
				}
				if (longestLevel < levelLength) {
					longestLevel = levelLength;
				}

			}

			print.append(StringUtils.left("TimeStamp" + space(dateStringLength), dateStringLength)).append("   ").append(StringUtils.left("Module" + space(longestModule), longestModule)).append("   ")
					.append(StringUtils.left("Level" + space(longestLevel), longestLevel)).append("   ").append(StringUtils.left("Description" + space(15), 15));

			print.append("\r\n");
			print.append("-------------------------------------------------------------------------------------" + "-------------------------------------------------------------------------------");
			String msg255 = "";
			for (ErrorMessage trace : traces) {
				if (!StringUtils.isEmpty(trace.getErrMsg2())) {
					msg255 = splitLine(trace.getErrMsg2(), longestDescription);

					if (!StringUtils.isEmpty(trace.getErrModule())) {
						msg255 = trace.getErrModule() + " : " + msg255;
					}
					String exeName = trace.getApplicationName();

					// loader is often saved as LOADER, just to make it more readable
					if ("Loader".equalsIgnoreCase(trace.getApplicationName())) {
						exeName = "Loader";
					}

					print.append("\r\n").append(StringUtils.left(ApplicationUtils.formatDateTime(trace.getTimeStamp()) + space(dateStringLength), dateStringLength)).append("   ")
							.append(StringUtils.left(StringUtils.defaultString(exeName) + space(longestModule), longestModule)).append("   ").append(StringUtils.left(StringUtils.defaultString(trace.getErrLevel()) + space(longestLevel), longestLevel))
							.append("   ").append(StringUtils.left(StringUtils.defaultString(msg255), msg255.length()));

				}

				if (!StringUtils.isEmpty(trace.getErrMsg1())) {
					print.append(space(msg255.length()) + splitLine(trace.getErrMsg1(), msg255.length()));
				}
			}
			print.append("\r\n");
		}
		// -------------------------------------------------------------------------------------------
		// Statistics
		// -------------------------------------------------------------------------------------------
		Long max_int = 2147483647L;
		if (logInfo.isIncludeStatistics()) {
			print.append("Statistics :").append("\r\n");
			print.append("============").append("\r\n");
			List<UpdatedMessage> messages = getUpdatedMessages(userName, SortOrder.descending, 0L, max_int);
			// print the header
			print.append(StringUtils.left("TimeStamp" + space(dateStringLength), dateStringLength)).append("   ").append(StringUtils.left("Id" + space(6), 6)).append("   ")
					.append(StringUtils.left("Overrun" + space("Overrun".length()), "Overrun".length())).append("   ").append(StringUtils.left("Status" + space(9), 9)).append("   ")
					.append(StringUtils.left("New Msg" + space("New Msg".length()), "New Msg".length())).append("   ").append(StringUtils.left("Upd Msg" + space("Upd Msg".length()), "Upd Msg".length())).append("   ")
					.append(StringUtils.left("Notif Msg" + space("Notif Msg".length()), "Notif Msg".length())).append("   ").append(StringUtils.left("Refresh" + space("Refresh".length()), "Refresh".length())).append("   ")
					.append(StringUtils.left("%OnTime" + space("%OnTime".length()), "%OnTime".length())).append("   ").append(StringUtils.left("Fail Count" + space("Fail Count".length()), "Fail Count".length())).append("   ")
					.append(StringUtils.left("Error Count" + space("Error Count".length()), "Error Count".length())).append("   ").append(StringUtils.left("Warning Count" + space("Warning Count".length()), "Warning Count".length())).append("   ")
					.append(StringUtils.left("Jrnl Count" + space("Jrnl Count".length()), "Jrnl Count".length())).append("   ").append(StringUtils.left("Origin" + space("Origin".length()), "Origin".length())).append("   ")
					.append(StringUtils.left("Elapsed Time" + space("Elapsed Time".length()), "Elapsed Time".length())).append("\r\n");

			for (UpdatedMessage mesg : messages) {
				print.append(StringUtils.left(ApplicationUtils.formatDateTime(mesg.getTimeStamp()) + space(dateStringLength), dateStringLength)).append("   ").append(StringUtils.left(StringUtils.defaultString(mesg.getID()) + space(6), 6))
						.append("   ").append(StringUtils.left(StringUtils.defaultString(mesg.getOverrun()) + space("Overrun".length()), "Overrun".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getStatus()) + space(9), 9)).append("   ").append(StringUtils.left(StringUtils.defaultString(mesg.getNewMsgCount()) + space("New Msg".length()), "New Msg".length()))
						.append("   ").append(StringUtils.left(StringUtils.defaultString(mesg.getUpdateMsgCount()) + space("Upd Msg".length()), "Upd Msg".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getNotifiedMsgCount()) + space("Notif Msg".length()), "Notif Msg".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getRefreshCount()) + space("Refresh".length()), "Refresh".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getOnTimeCount()) + space("%OnTime".length()), "%OnTime".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getFailedCount()) + space("Fail Count".length()), "Fail Count".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getErrorCount()) + space("Error Count".length()), "Error Count".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getWarningCount()) + space("Warning Count".length()), "Warning Count".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getJrnlMsgCount()) + space("Jrnl Count".length()), "Jrnl Count".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getOrigin()) + space("Origin".length()), "Origin".length())).append("   ")
						.append(StringUtils.left(StringUtils.defaultString(mesg.getElapsedTime()) + space("Elapsed Time".length()), "Elapsed Time".length())).append("\r\n");
			}
			print.append("\r\n");
		}
		// -------------------------------------------------------------------------------------------
		// Audit Logs
		// -------------------------------------------------------------------------------------------
		if (logInfo.isIncludeAuditLogs()) {

			if (auditLogs != null) {
				// get longest programName
				int longestLogin = 6;
				int longestProgramName = 13;
				int longestEvent = 5;
				int longestAction = 0;

				/*
				 * TODO: List<String> programNameList = auditLogs.stream().map(AuditLog::getProgramName).collect(Collectors.toList());
				 * programNameList.stream().max(Comparator.comparingInt(String::length)); -Below code written to make sure that each column and its value reserve the same space in whole file
				 */
				for (AuditLog auditLog : auditLogs) {
					int loginLength = auditLog.getLoginName().length();
					int proframNameLength = auditLog.getProgramName().length();
					int eventLength = auditLog.getEvent().length();
					int actionLength = auditLog.getAction().length();
					if (longestLogin < loginLength) {
						longestLogin = loginLength;
					}
					if (longestProgramName < proframNameLength) {
						longestProgramName = proframNameLength;
					}
					if (longestLogin < loginLength) {
						longestLogin = loginLength;
					}
					if (longestEvent < eventLength) {
						longestEvent = eventLength;
					}
					if (longestAction < actionLength) {
						longestAction = actionLength;
					}

				}
				print.append(StringUtils.left("TimeStamp" + space(dateStringLength), dateStringLength)).append("   ").append(StringUtils.left("Login" + space(longestLogin), longestLogin)).append("   ")
						.append(StringUtils.left("Program Name" + space(longestProgramName), longestProgramName)).append("   ").append(StringUtils.left("Event" + space(longestEvent), longestEvent)).append("   ")
						.append(StringUtils.left("IP Address" + space(15), 15)).append("   ").append(StringUtils.left("Action" + space(longestAction), longestAction));

				print.append("-------------------------------------------------------------------------------------" + "-------------------------------------------------------------------------------");
				for (AuditLog auditLog : auditLogs) {

					print.append("\r\n").append(StringUtils.left(ApplicationUtils.formatDateTime(auditLog.getTimeStamp()) + space(dateStringLength), dateStringLength)).append("   ")
							.append(StringUtils.left(StringUtils.defaultString(auditLog.getLoginName()) + space(longestLogin), longestLogin)).append("   ");

					String programName = StringUtils.defaultString(auditLog.getProgramName()).replace(".exe", "");

					print.append(StringUtils.left(programName + space(longestProgramName), longestProgramName)).append("   ").append(StringUtils.left(StringUtils.defaultString(auditLog.getEvent()) + space(longestEvent), longestEvent)).append("   ")
							.append(StringUtils.left(StringUtils.defaultString(auditLog.getIpAddress()) + space(15), 15)).append("   ").append(StringUtils.left(StringUtils.defaultString(auditLog.getAction()) + space(longestAction), longestAction));

					if (auditLog.isShowDetails()) {
						if (!auditLog.getAuditLogDetailsList().isEmpty()) {
							print.append("\r\n");

							for (AuditLogDetails auditLogDetailBean : auditLog.getAuditLogDetailsList()) {
								print.append("\r\n").append(" - Field Name: ").append(auditLogDetailBean.getFieldName()).append("\r\n").append(" - Old Value: ").append(auditLogDetailBean.getOldValue()).append("\r\n").append(" - New Value: ")
										.append(auditLogDetailBean.getNewValue()).append("\r\n");
							}
						}
					}

				}
			}

		}

		return print.toString();
	}

	private String splitLine(String errMsg, int spaces) {
		String outLine = errMsg;

		outLine = outLine.replace("\r\n", "\n");
		outLine = outLine.replace("\r", "");
		outLine = outLine.replace("\n", "\r\n");
		// remove the \r\n at the end of the string
		if (outLine.endsWith("\r\n")) {
			outLine = outLine.substring(0, outLine.length() - 2);
		}
		// make sure to align
		outLine = outLine.replace("\r\n", "\r\n" + space(spaces));

		return outLine;
	}

	private String space(int count) {
		String spaces = "";
		for (int i = 0; i < count; ++i) {
			spaces += " ";
		}
		return spaces;
	}

	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
	}

}
