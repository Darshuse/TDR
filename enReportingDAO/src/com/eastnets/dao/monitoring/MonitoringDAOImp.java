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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.monitoring.procedure.LoaderGetCountersProcedure;
import com.eastnets.domain.monitoring.Alliance;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.MonitoringEventInfo;
import com.eastnets.domain.monitoring.MonitoringMessageInfo;
import com.eastnets.domain.monitoring.ReportingDBInfo;
import com.eastnets.domain.monitoring.Statistics;

/**
 * Monitoring DAO Implementation
 * 
 * @author EastNets
 * @since July 19, 2012
 */
public abstract class MonitoringDAOImp extends DAOBaseImp implements MonitoringDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7199327659938565493L;
	private LoaderGetCountersProcedure ldGetCountersProcedure;

	@Override
	public int getLastFileKeyID(String date) {
		StringBuilder query = new StringBuilder();
		query.append("select max(keyid) key_id from ldUpdateStatistics where insert_time < ");
		query.append(getDbPortabilityHandler().getDateWithPatternNoBinding(date, Constants.ORACLE_DATE_TIME_PATTERN));
		List<Integer> records = jdbcTemplate.query(query.toString(), new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("key_id");
			}
		});

		return ((records == null || records.isEmpty()) ? 0 : records.get(0));
	}

	@Override
	public Statistics getLoaderStatistics() {
		return ldGetCountersProcedure.execute(new Statistics());
	}

	@Override
	public long getJournalsCount() {
		StringBuilder query = new StringBuilder();
		query.append("select count(*) as jrnlcount from rJrnl");
		List<Long> records = jdbcTemplate.query(query.toString(), new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("jrnlcount");
			}
		});

		return ((records == null || records.isEmpty()) ? 0 : new Long(records.get(0).toString()));
	}

	public LoaderGetCountersProcedure getLdGetCountersProcedure() {
		return ldGetCountersProcedure;
	}

	public void setLdGetCountersProcedure(LoaderGetCountersProcedure ldGetCountersProcedure) {
		this.ldGetCountersProcedure = ldGetCountersProcedure;
	}

	@Override
	public List<ErrorMessage> getErrorMessages(int lastErrorKeyID, boolean displayWarnings) {
		StringBuilder query = new StringBuilder();
		query.append("select ErrExeName, ErrId, Errtime, Errlevel, Errmodule, ErrMsg1, ErrMsg2 from ldErrors where ErrId > ");
		query.append(lastErrorKeyID);
		query.append(" and errExeName IN ('LOADER', 'Archive', 'Restore', 'JrnlArchive' , 'PS')");

		if (!displayWarnings) {
			query.append(" and ErrLevel <> 'Warn' ");
		}

		query.append(" order by ERRID asc");

		List<ErrorMessage> records = jdbcTemplate.query(query.toString(), new RowMapper<ErrorMessage>() {

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

	public Long getErrorMessagesCount(int lastErrorKeyID, boolean displayWarnings, String moduleFilter, String levelFilter) {
		StringBuilder query = new StringBuilder();
		query.append("select count(1) as errorsCount from ldErrors where ErrId > ");
		query.append(lastErrorKeyID);
		if (moduleFilter != null && !moduleFilter.trim().equals("")) {
			if (moduleFilter.equals(Constants.NULL_VALUE)) {
				query.append("and errExeName is null");
			} else {
				if (moduleFilter.equalsIgnoreCase("LOADER")) {
					moduleFilter = "LOADER";
				}
				query.append("and errExeName ='");
				query.append(moduleFilter);
				query.append("' ");
			}
		} else {
			query.append(" and errExeName IN ('LOADER', 'Archive', 'Restore', 'JrnlArchive' , 'PS')");
		}

		if (levelFilter != null && !levelFilter.trim().equals("")) {
			query.append(" and Errlevel ='");
			query.append(levelFilter);
			query.append("'");
		}

		if (!displayWarnings) {
			query.append(" and ErrLevel <> 'Warn' ");
		}

		List<Long> records = jdbcTemplate.query(query.toString(), new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getLong("errorsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));
	}

	@Override
	public List<String> getApplicationsList() {
		StringBuilder query = new StringBuilder();
		query.append("select distinct(ErrExeName) from ldErrors order by ErrExeName");

		List<String> records = jdbcTemplate.query(query.toString(), new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				String string = rs.getString("ErrExeName");
				if (string == null) {
					string = Constants.NULL_VALUE;
				}
				return string;

			}
		});

		return records;
	}

	public String getApplicationListString() {

		try {

			List<String> applications = getApplicationsList();
			if (applications == null || applications.isEmpty()) {
				return "('LOADER', 'Archive', 'Restore')";
			} else {
				StringBuilder applicationsString = new StringBuilder("(");
				for (int i = 0; i < applications.size(); i++) {
					if (i > 0 && i < applications.size()) {
						applicationsString.append(",");
					}
					applicationsString.append("'");
					applicationsString.append(applications.get(i));
					applicationsString.append("'");
				}

				applicationsString.append(")");

				return applicationsString.toString();
			}
		} catch (Exception e) {
			return "('LOADER', 'Archive', 'Restore')";
		}
	}

	@Override
	public List<String> getModulesList(int lastErrorKeyID, boolean displayWarnings, String levelFilter) {
		StringBuilder query = new StringBuilder();
		query.append("select distinct(Errmodule) from ldErrors where ErrId > ");
		query.append(lastErrorKeyID);
		query.append(" and errExeName IN ('LOADER', 'Archive', 'Restore', 'JrnlArchive' , 'MessaageStoreAPI')");

		if (levelFilter != null && !levelFilter.trim().equals("")) {
			query.append("and Errlevel ='");
			query.append(levelFilter);
			query.append("'");
		}

		if (!displayWarnings) {
			query.append(" and ErrLevel <> 'Warn' ");
		}

		query.append(" order by Errmodule asc");

		List<String> records = jdbcTemplate.query(query.toString(), new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				String string = rs.getString("Errmodule");
				if (string == null) {
					string = Constants.NULL_VALUE;
				}
				return string;

			}
		});

		return records;
	}

	@Override
	public List<String> getLevelsList(int lastErrorKeyID, boolean displayWarnings, String moduleFilter) {
		StringBuilder query = new StringBuilder();
		query.append("select distinct(Errlevel) from ldErrors where ErrId > ");
		query.append(lastErrorKeyID);

		if (moduleFilter != null && !moduleFilter.trim().equals("")) {
			if (moduleFilter.equals(Constants.NULL_VALUE)) {
				query.append("and errExeName is null");
			} else {
				if (moduleFilter.equalsIgnoreCase("LOADER")) {
					moduleFilter = "LOADER";
				}
				query.append("and errExeName ='");
				query.append(moduleFilter);
				query.append("'");
			}
		} else {
			query.append(" and errExeName IN ('LOADER', 'Archive', 'Restore', 'JrnlArchive' , 'Messages Migration','MessageStoreAPI')");
		}

		if (!displayWarnings) {
			query.append(" and ErrLevel <> 'Warn' ");
		}

		query.append(" order by Errlevel  asc");

		List<String> records = jdbcTemplate.query(query.toString(), new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getString("Errlevel");

			}
		});

		return records;
	}

	@Override
	public int getLastErrorKeyID(String date) {

		StringBuilder query = new StringBuilder();
		query.append("select max(ErrId) error_id from ldErrors where ErrTime < ");
		query.append(getDbPortabilityHandler().getDateWithPatternNoBinding(date, Constants.ORACLE_DATE_TIME_PATTERN));
		List<Integer> records = jdbcTemplate.query(query.toString(), new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("error_id");
			}
		});

		return ((records == null || records.isEmpty()) ? 0 : records.get(0));

	}

	@Override
	public ReportingDBInfo getReportingDBInfo() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT InstallationDate, InstallationUser, Major, Minor, Revision FROM sDBVersion ORDER BY InstallationDate DESC");
		List<ReportingDBInfo> records = jdbcTemplate.query(query.toString(), new RowMapper<ReportingDBInfo>() {

			@Override
			public ReportingDBInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				ReportingDBInfo reportingDBInfo = new ReportingDBInfo();
				reportingDBInfo.setInstallationDate((rs.getString("InstallationDate")));
				reportingDBInfo.setInstallationUser((rs.getString("InstallationUser")));
				reportingDBInfo.setMajor((rs.getString("Major")));
				reportingDBInfo.setMinor((rs.getString("Minor")));
				reportingDBInfo.setRevision((rs.getString("Revision")));

				return reportingDBInfo;
			}
		});

		return (ReportingDBInfo) ((records == null || records.isEmpty()) ? null : records.get(0));
	}

	@Override
	public Alliance getAlliance(String ID) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(ID);

		String query = "SELECT aid,jrnl_seq_nbr,jrnl_rev_date_time,last_umidl,last_umidh,alliance_instance FROM ldSettings WHERE aid = ?";
		List<Alliance> records = jdbcTemplate.query(query, parameters.toArray(), new RowMapper<Alliance>() {

			@Override
			public Alliance mapRow(ResultSet rs, int rowNum) throws SQLException {

				Alliance alliance = new Alliance();
				alliance.setAid(rs.getString("aid"));
				alliance.setJrnlSeqNbr(rs.getString("jrnl_seq_nbr"));
				alliance.setJrnlRevDateTime(rs.getString("jrnl_rev_date_time"));
				alliance.setLastUmidl(rs.getString("last_umidl"));
				alliance.setLastUmidh(rs.getString("last_umidh"));
				alliance.setInstanceName(rs.getString("alliance_instance"));

				return alliance;
			}
		});

		return (Alliance) ((records == null || records.isEmpty()) ? null : records.get(0));
	}

	@Override
	public MonitoringMessageInfo getMessage(String aid, String umidl, String umidh) {
		StringBuilder query = new StringBuilder("SELECT mesg_crea_date_time, mesg_UUMID, mesg_UUMID_Suffix FROM rMesg WHERE aid = ");
		query.append(aid);
		query.append(" and mesg_s_umidl = ");
		query.append(umidl);
		query.append(" AND mesg_s_umidh = ");
		query.append(umidh);

		List<MonitoringMessageInfo> records = jdbcTemplate.query(query.toString(), new RowMapper<MonitoringMessageInfo>() {

			@Override
			public MonitoringMessageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				MonitoringMessageInfo message = new MonitoringMessageInfo();
				message.setMesgCreaDateTime(rs.getString("mesg_crea_date_time"));
				message.setMesgUUMID(rs.getString("mesg_UUMID"));
				message.setMesgUUMIDSuffix(rs.getString("mesg_UUMID_Suffix"));

				return message;
			}
		});

		return (MonitoringMessageInfo) ((records == null || records.isEmpty()) ? null : records.get(0));
	}

	@Override
	public MonitoringEventInfo getEvent(String aid, String jrnlRevDate, String jrnlSeqNumber) {
		StringBuilder query = new StringBuilder("SELECT jrnl_date_time, jrnl_display_text FROM rJrnl WHERE aid = ");
		query.append(aid);
		query.append(" and jrnl_rev_date_time = ");
		query.append(jrnlRevDate);
		query.append(" AND jrnl_seq_nbr = ");
		query.append(jrnlSeqNumber);

		List<MonitoringEventInfo> records = jdbcTemplate.query(query.toString(), new RowMapper<MonitoringEventInfo>() {

			@Override
			public MonitoringEventInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				MonitoringEventInfo event = new MonitoringEventInfo();
				event.setDateTime(rs.getString("jrnl_date_time"));
				event.setDescription(rs.getString("jrnl_display_text"));

				return event;
			}
		});

		return (MonitoringEventInfo) ((records == null || records.isEmpty()) ? null : records.get(0));
	}

	@Override
	public List<String> getLoginNamesList(String date, String programNameFilter, String eventFilter) {

		final String selectStatememnt = "SELECT distinct(loginame) FROM rAuditLog order by loginame";

		List<String> records = jdbcTemplate.query(selectStatememnt, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("loginame");
			}
		});

		return records;
	}

	@Override
	public List<String> getProgramsNamesList(String date, String loginNameFilter, String eventFilter) {

		final String selectStatememnt = "SELECT distinct(program_name) FROM rAuditLog where program_name not in ('SQL Developer','Microsoft SQL Server') order by program_name";

		List<String> records = jdbcTemplate.query(selectStatememnt, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getString("program_name");
			}
		});

		return records;
	}

	@Override
	public List<String> getEventsList(String date, String loginNameFilter, String programNameFilter) {

		final String selectStatememnt = "SELECT distinct(event) FROM rAuditLog order by event";

		List<String> records = jdbcTemplate.query(selectStatememnt, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getString("event");
			}
		});
		return records;
	}

	public Long getUpdatedMessagesCount(int lastFileKeyID, boolean displayJournals) {

		StringBuilder query = new StringBuilder();
		query.append("select count(1) as logsCount");
		query.append(" from ldUpdateStatistics");
		query.append(" where keyid > ");
		query.append(lastFileKeyID);

		if (displayJournals) {
			query.append(" and not((jrnl_msg_count = 0) and (new_msg_count = 0 and update_msg_count = 0 and notified_msg_count = 0)) ");
		}

		List<Long> records = jdbcTemplate.query(query.toString(), new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getLong("logsCount");
			}
		});
		return ((records == null || records.isEmpty()) ? 0L : new Long(records.get(0).toString()));

	}

}
