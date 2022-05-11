package com.eastnets.dao.monitoring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.SortOrder;
import com.eastnets.domain.monitoring.AuditLog;
import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.utils.ApplicationUtils;

public class MonitoringSqlDAOImp extends MonitoringDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 725964243601487388L;

	@Override
	public Long getAuditLogsCount(String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, String fromDate, String toDate, String ipAddress) {

		String query = "SELECT count(1) as logsCount FROM rAuditLog";
		query += " where timestamp between cast('" + fromDate + "' as datetime ) and cast('" + toDate + "' as datetime )";
		query += "and program_name != 'Microsoft SQL Server'";

		if (loginNameFilter != null && !loginNameFilter.trim().equals("")) {
			query += String.format(" and loginame COLLATE Latin1_General_CS_AS like '%s'", loginNameFilter.trim());
		}
		if (programNameFilter != null && !programNameFilter.trim().equals("")) {
			query += String.format(" and program_name = '%s'", programNameFilter);
		}
		if (eventFilter != null && !eventFilter.trim().equals("")) {
			query += String.format("and event = '%s'", eventFilter);
		}
		if (ipAddress != null && !ipAddress.trim().equals("")) {
			query += String.format("and ipAddress COLLATE Latin1_General_CS_AS like '%s'", ipAddress.trim());
		}
		if (actionFilter != null && !actionFilter.trim().equals("")) {
			query += String.format(" and action COLLATE Latin1_General_CS_AS like '%s'", actionFilter.trim());
		}

		List<Long> records = jdbcTemplate.query(query, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getLong("logsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public List<AuditLog> getAuditLogs(String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, String fromDate, String toDate, String ipAddress, SortOrder order, Long from, Long to) {

		String orderString = "DESC";
		if (SortOrder.ascending == order) {
			orderString = " ASC ";
		}
		String query = "SELECT * FROM (SELECT id,loginame,program_name,event,action, ipAddress,timestamp,ROW_NUMBER() OVER (ORDER BY timestamp " + orderString + ")AS RowNum FROM rAuditLog";
		query += " where timestamp between cast('" + fromDate + "' as datetime ) and cast('" + toDate + "' as datetime )";
		query += "and program_name != 'Microsoft SQL Server'";

		if (loginNameFilter != null && !loginNameFilter.trim().equals("")) {
			query += String.format(" and loginame COLLATE Latin1_General_CS_AS like '%s'", loginNameFilter.trim());
		}
		if (programNameFilter != null && !programNameFilter.trim().equals("")) {
			query += String.format(" and program_name = '%s'", programNameFilter);
		}
		if (eventFilter != null && !eventFilter.trim().equals("")) {
			query += String.format(" and event = '%s'", eventFilter);
		}
		if (ipAddress != null && !ipAddress.trim().equals("")) {
			query += String.format(" and ipAddress COLLATE Latin1_General_CS_AS like '%s'", ipAddress.trim());
		}
		if (actionFilter != null && !actionFilter.trim().equals("")) {
			query += String.format(" and action COLLATE Latin1_General_CS_AS like '%s'", actionFilter.trim());
		}

		query += " )a WHERE RowNum BETWEEN " + from + " AND " + to;

		query += " ORDER BY a.timestamp " + orderString;

		List<AuditLog> records = jdbcTemplate.query(query, new RowMapper<AuditLog>() {

			@Override
			public AuditLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				AuditLog auditLog = new AuditLog();
				auditLog.setId(rs.getString("id"));
				auditLog.setLoginName(rs.getString("loginame"));
				auditLog.setProgramName(rs.getString("program_name"));
				auditLog.setEvent(rs.getString("event"));
				auditLog.setAction(rs.getString("action"));
				auditLog.setIpAddress(rs.getString("ipAddress"));
				auditLog.setTimeStamp(rs.getTimestamp("timestamp"));
				String date = ApplicationUtils.formatDateTime(auditLog.getTimeStamp());
				auditLog.setAuditLogDetailsList(getAuditLogsDetails(auditLog.getId(), auditLog.getLoginName(), auditLog.getProgramName(), auditLog.getEvent(), auditLog.getAction(), date, auditLog.getIpAddress(), order));

				return auditLog;
			}
		});

		return new ArrayList<AuditLog>(records);
	}

	public List<AuditLogDetails> getAuditLogsDetails(String id, String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, String timeStamp, String ipAddress, SortOrder order) {

		String orderString = "DESC";
		if (SortOrder.ascending == order) {
			orderString = " ASC ";
		}

		String query = "SELECT field_name,field_type,old_value,new_value FROM rAuditLogDetails ";
		query += "where timestamp = cast('" + timeStamp + "' as datetime)";

		if (id != null && !id.equals("")) {
			query += String.format(" and id = '%s'", id);
		}
		if (loginNameFilter != null && !loginNameFilter.trim().equals("")) {
			query += String.format(" and loginame = '%s'", loginNameFilter.replace("'", "''"));
		}

		query += " ORDER BY timestamp ";
		query += orderString;

		List<AuditLogDetails> records = jdbcTemplate.query(query, new RowMapper<AuditLogDetails>() {

			@Override
			public AuditLogDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				AuditLogDetails auditLogDetails = new AuditLogDetails();
				auditLogDetails.setFieldName(rs.getString("field_name"));
				auditLogDetails.setFieldType(rs.getString("field_type"));
				auditLogDetails.setOldValue(rs.getString("old_value"));
				auditLogDetails.setNewValue(rs.getString("new_value"));
				return auditLogDetails;
			}
		});

		return new ArrayList<AuditLogDetails>(records);
	}

	@Override
	public List<AuditLog> getAuditLogFile(boolean showDetails, String loginNameFilter, String programNameFilter, String eventFilter, String actionFilter, String fromDate, String toDate, String ipAddress, SortOrder order) {

		String orderString = "DESC";
		if (SortOrder.ascending == order) {
			orderString = " ASC ";
		}

		String query = "SELECT id,loginame,program_name,event,action,timestamp,ipAddress FROM rAuditLog ";
		query += " where timestamp between cast('" + fromDate + "' as datetime ) and cast('" + toDate + "' as datetime )";
		query += "and program_name != 'Microsoft SQL Server'";

		if (loginNameFilter != null && !loginNameFilter.trim().equals("")) {
			query += String.format(" and loginame COLLATE Latin1_General_CS_AS like '%s'", loginNameFilter.trim());
		}
		if (programNameFilter != null && !programNameFilter.trim().equals("")) {
			query += String.format(" and program_name = '%s'", programNameFilter);
		}
		if (eventFilter != null && !eventFilter.trim().equals("")) {
			query += String.format("and event = '%s'", eventFilter);
		}
		if (ipAddress != null && !ipAddress.trim().equals("")) {
			query += String.format("and ipAddress COLLATE Latin1_General_CS_AS like '%s'", ipAddress.trim());
		}
		if (actionFilter != null && !actionFilter.trim().equals("")) {
			query += String.format(" and action COLLATE Latin1_General_CS_AS like '%s'", actionFilter.trim());
		}

		query += " ORDER BY timestamp ";
		query += orderString;
		List<AuditLog> records = jdbcTemplate.query(query, new RowMapper<AuditLog>() {

			@Override
			public AuditLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				AuditLog auditLog = new AuditLog();
				auditLog.setId(rs.getString("id"));
				auditLog.setLoginName(rs.getString("loginame"));
				auditLog.setProgramName(rs.getString("program_name"));
				auditLog.setEvent(rs.getString("event"));
				auditLog.setAction(rs.getString("action"));
				auditLog.setIpAddress(rs.getString("ipAddress"));
				auditLog.setTimeStamp(rs.getTimestamp("timestamp"));
				// export audit logs with details
				if (showDetails) {
					String date = ApplicationUtils.formatDateTime(auditLog.getTimeStamp());
					auditLog.setAuditLogDetailsList(getAuditLogsDetails(auditLog.getId(), auditLog.getLoginName(), auditLog.getProgramName(), auditLog.getEvent(), auditLog.getAction(), date, auditLog.getIpAddress(), order));
				}
				return auditLog;
			}
		});

		return new ArrayList<AuditLog>(records);

	}

	@Override
	public List<ErrorMessage> getErrorMessages(int lastErrorKeyID, boolean displayWarnings, String moduleFilter, String levelFilter, SortOrder order, Long from, Long to) {

		List<Object> parameters = new ArrayList<>();

		String orderString = "DESC";
		if (SortOrder.ascending == order) {
			orderString = " ASC ";
		}

		String query = "SELECT * FROM (select ErrExeName, ErrId, Errtime, Errlevel, Errmodule, ErrMsg1, ErrMsg2,ROW_NUMBER() OVER (ORDER BY ErrId " + orderString + ") AS RowNum from ldErrors ";
		query += " where ErrId > ? ";
		parameters.add(lastErrorKeyID);

		if (moduleFilter != null && !moduleFilter.trim().equals("")) {
			if (moduleFilter.equals(Constants.NULL_VALUE)) {
				query += " and errExeName is null ";
			} else {
				if (moduleFilter.equalsIgnoreCase("LOADER")) {
					moduleFilter = "LOADER";
				}
				query += " and errExeName = ? ";
				parameters.add(moduleFilter);
			}
		} else {
			query += " and errExeName IN (";
			// the code below written to prevent the security injection
			// to use the ? with in clause we should to ignore the single quotes
			String[] errExeNameArray = getApplicationListString().split(",");
			for (int i = 0; i < errExeNameArray.length; i++) {
				if (errExeNameArray.length - 1 != i) {
					parameters.add(i == 0 ? errExeNameArray[i].substring(2, errExeNameArray[i].length() - 1) : errExeNameArray[i].substring(1, errExeNameArray[i].length() - 1));
					query += "?,";
				} else {
					parameters.add(errExeNameArray[i].substring(1, errExeNameArray[i].length() - 2));
					query += "?)";

				}

			}

		}

		if (levelFilter != null && !levelFilter.trim().equals("")) {
			query += " and Errlevel = ?";
			parameters.add(levelFilter);
		}

		if (!displayWarnings) {
			query += " and ErrLevel <> ? ";
			parameters.add("Warn");

		}
		query += " ) a ";
		if (to != 0) {
			query += " WHERE RowNum BETWEEN ? AND ?";
			parameters.add(from);
			parameters.add(to);

		}
		query += " ORDER BY a.ERRID ";
		query += orderString;

		List<ErrorMessage> records = jdbcTemplate.query(query, parameters.toArray(), new RowMapper<ErrorMessage>() {

			@Override
			public ErrorMessage mapRow(ResultSet rs, int rowNum) throws SQLException {

				ErrorMessage mesg = new ErrorMessage();
				mesg.setApplicationName(rs.getString("ErrExeName"));
				mesg.setErrID(rs.getString("ErrId"));
				mesg.setTimeStamp(rs.getTimestamp("Errtime"));
				mesg.setErrLevel(rs.getString("Errlevel"));
				mesg.setErrModule(rs.getString("Errmodule"));
				mesg.setErrMsg1(rs.getString("ErrMsg1"));
				mesg.setErrMsg2(rs.getString("ErrMsg2"));

				return mesg;
			}
		});

		return new ArrayList<ErrorMessage>(records);
	}

	@Override
	public List<UpdatedMessage> getUpdatedMessages(int lastFileKeyID, boolean displayJournals, SortOrder order, Long from, Long to) {

		String orderString = "DESC";
		if (SortOrder.ascending == order) {
			orderString = " ASC ";
		}

		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM (select keyid, aid, insert_time, update_time, total_time, new_msg_count, update_msg_count, on_time_count,");
		query.append(" notified_msg_count, refresh_count, overrun, update_status, error_count, failed_count, warning_count, jrnl_msg_count, origin");
		query.append(" ,ROW_NUMBER() OVER (ORDER BY Keyid ");
		query.append(orderString);
		query.append(") AS RowNum");
		query.append(" from ldUpdateStatistics");
		query.append(" where keyid > ");
		query.append(lastFileKeyID);

		if (displayJournals) {
			query.append(" and not((jrnl_msg_count = 0) and (new_msg_count = 0 and update_msg_count = 0 and notified_msg_count = 0)) ");
		}

		query.append(")a WHERE RowNum BETWEEN ");
		query.append(from);
		query.append(" AND ");
		query.append(to);
		query.append(" order by a.Keyid ");
		query.append(orderString);

		List<UpdatedMessage> records = jdbcTemplate.query(query.toString(), new RowMapper<UpdatedMessage>() {

			@Override
			public UpdatedMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
				UpdatedMessage record = new UpdatedMessage();
				record.setLastFileKeyID(rs.getString("keyid"));
				record.setID(rs.getString("aid"));
				record.setInsertTime(rs.getString("insert_time"));
				record.setTimeStamp(rs.getTimestamp("update_time"));
				record.setElapsedTime(rs.getString("total_time"));
				record.setNewMsgCount(rs.getString("new_msg_count"));
				record.setUpdateMsgCount(rs.getString("update_msg_count"));
				record.setOnTimeCount(rs.getString("on_time_count"));
				record.setNotifiedMsgCount(rs.getString("notified_msg_count"));
				record.setRefreshCount(rs.getString("refresh_count"));
				record.setOverrun(rs.getString("overrun"));
				record.setStatus(rs.getString("update_status"));
				record.setErrorCount(rs.getString("error_count"));
				record.setFailedCount(rs.getString("failed_count"));
				record.setWarningCount(rs.getString("warning_count"));
				record.setJrnlMsgCount(rs.getString("jrnl_msg_count"));
				record.setOrigin(rs.getString("origin"));
				return record;
			}
		});

		return new ArrayList<UpdatedMessage>(records);
	}
}
