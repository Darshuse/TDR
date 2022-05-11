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

package com.eastnets.dao.archive;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.archive.ArchiveLog;
import com.eastnets.domain.archive.RestoreSet;
import com.eastnets.utils.ApplicationUtils;

/**
 * Archive Oracle SQL Implementation
 * 
 * @author EastNets
 * @since October 4, 2012
 */
public class ArchiveSqlDAOImp extends ArchiveDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8042032195700330912L;

	public List<RestoreSet> getRestoreSet(Long aid, String dateFrom, String dateTo) {

		StringBuilder selectQuery = new StringBuilder();

		selectQuery.append("select aid, name, restored_at, min_date, max_date From ldRestoreSet ");
		selectQuery.append(" WHERE aid = ");
		selectQuery.append(aid);
		selectQuery.append(" AND set_type = 2 AND (");
		selectQuery.append("(( Min_Date >= '");
		selectQuery.append(dateFrom);
		selectQuery.append("' AND Min_Date <= '");
		selectQuery.append(dateTo);
		selectQuery.append("' ) OR ");
		selectQuery.append("( Max_Date >= '");
		selectQuery.append(dateFrom);
		selectQuery.append("' AND Max_Date <= '");
		selectQuery.append(dateTo);
		selectQuery.append("')))");

		List<RestoreSet> restoreSets = (List<RestoreSet>) jdbcTemplate.query(selectQuery.toString(), new RowMapper<RestoreSet>() {

			@Override
			public RestoreSet mapRow(ResultSet rs, int rowNum) throws SQLException {
				RestoreSet restoreSet = new RestoreSet();

				restoreSet.setAid(rs.getLong("aid"));
				restoreSet.setName(rs.getString("name"));
				restoreSet.setRestoredAt(rs.getDate("restored_at"));
				restoreSet.setMinDate(rs.getDate("min_date"));
				restoreSet.setMaxDate(rs.getDate("max_date"));

				return restoreSet;
			}

		});

		return restoreSets;
	}

	@Override
	public void addArchiveLog(ArchiveLog archiveLog) {

		Date dateFrom = archiveLog.getDateFrom();
		Date dateTo = archiveLog.getDateTo();
		Date creationTime = archiveLog.getCreationTime();

		String formatedDateFrom = null;
		String formatedDateTo = null;
		String formatDate = null;

		formatedDateFrom = ApplicationUtils.formatDate(dateFrom, Constants.MSSQL_DATE_TIME_PATTERN);
		formatedDateTo = ApplicationUtils.formatDate(dateTo, Constants.MSSQL_DATE_TIME_PATTERN);
		formatDate = ApplicationUtils.formatDate(creationTime, Constants.MSSQL_DATE_TIME_PATTERN);

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO LDARCHIVELOG VALUES(?,?,?,?,?");

		if (dateFrom == null) {
			builder.append(",");
			builder.append("NULL");
		} else {
			builder.append(",'");
			builder.append(formatedDateFrom);
			builder.append("'");
		}
		if (dateTo == null) {
			builder.append(",");
			builder.append("NULL");
		} else {
			builder.append(",'");
			builder.append(formatedDateTo);
			builder.append("'");
		}
		builder.append(", ' ')");

		String insertQuery = builder.toString();

		jdbcTemplate.update(insertQuery, new Object[] { archiveLog.getModuleId(), formatDate, archiveLog.getAid(), archiveLog.getStatus(), archiveLog.getLogName() });

	}

	@Override
	public ArchiveLog getArchiveLog(Long moduleId, Date creationTime, Long aid) {
		String formatDate = ApplicationUtils.formatDate(creationTime, Constants.MSSQL_DATE_TIME_PATTERN);

		List<Object> parameters = new ArrayList<>();
		parameters.add(moduleId);
		parameters.add(formatDate);
		parameters.add(aid);

		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where MODUL_ID = ? and CREATION_TIME = ? and AID = ? order by id";

		List<ArchiveLog> archiveLogsList = (List<ArchiveLog>) jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ArchiveLog>() {
			@Override
			public ArchiveLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArchiveLog archiveLog = new ArchiveLog();
				archiveLog.setId(rs.getLong("ID"));
				archiveLog.setAid(rs.getLong("AID"));
				archiveLog.setStatus(rs.getLong("STATUS"));
				archiveLog.setLogName(rs.getString("LOG_PATH"));
				archiveLog.setDateFrom(rs.getDate("DATE_FROM"));
				archiveLog.setDateTo(rs.getDate("DATE_TO"));
				archiveLog.setCreationTime(rs.getDate("CREATION_TIME"));
				archiveLog.setLogText(rs.getString("LOG_TEXT"));

				return archiveLog;
			}
		});
		return archiveLogsList.get(0);
	}

	@Override
	public List<ArchiveLog> getArchiveLogs() {

		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog order by id";
		List<ArchiveLog> archiveLogsList = (List<ArchiveLog>) jdbcTemplate.query(selectQuery, new RowMapper<ArchiveLog>() {
			@Override
			public ArchiveLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArchiveLog archiveLog = new ArchiveLog();
				archiveLog.setId(rs.getLong("ID"));
				archiveLog.setAid(rs.getLong("AID"));
				archiveLog.setStatus(rs.getLong("STATUS"));
				archiveLog.setLogName(rs.getString("LOG_PATH"));
				archiveLog.setDateFrom(rs.getDate("DATE_FROM"));
				archiveLog.setDateTo(rs.getDate("DATE_TO"));
				archiveLog.setCreationTime(rs.getDate("CREATION_TIME"));
				archiveLog.setLogText(rs.getString("LOG_TEXT"));

				return archiveLog;
			}
		});
		return archiveLogsList;
	}

	@Override
	public List<ArchiveLog> getArchiveLogs(Long aid) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(aid);
		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where aid = ? order by id";

		List<ArchiveLog> archiveLogsList = (List<ArchiveLog>) jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ArchiveLog>() {
			@Override
			public ArchiveLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArchiveLog archiveLog = new ArchiveLog();
				archiveLog.setId(rs.getLong("ID"));
				archiveLog.setAid(rs.getLong("AID"));
				archiveLog.setStatus(rs.getLong("STATUS"));
				archiveLog.setLogName(rs.getString("LOG_PATH"));
				archiveLog.setDateFrom(rs.getDate("DATE_FROM"));
				archiveLog.setDateTo(rs.getDate("DATE_TO"));
				archiveLog.setCreationTime(rs.getDate("CREATION_TIME"));
				archiveLog.setLogText(rs.getString("LOG_TEXT"));

				return archiveLog;
			}
		});
		return archiveLogsList;
	}

	@Override
	public List<ArchiveLog> getArchiveLogs(Long aid, Long moduleId) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(aid);
		parameters.add(moduleId);
		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where aid = ? and MODUL_ID =? order by id";

		List<ArchiveLog> archiveLogsList = (List<ArchiveLog>) jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ArchiveLog>() {
			@Override
			public ArchiveLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArchiveLog archiveLog = new ArchiveLog();
				archiveLog.setId(rs.getLong("ID"));
				archiveLog.setAid(rs.getLong("AID"));
				archiveLog.setStatus(rs.getLong("STATUS"));
				archiveLog.setLogName(rs.getString("LOG_PATH"));
				archiveLog.setDateFrom(rs.getDate("DATE_FROM"));
				archiveLog.setDateTo(rs.getDate("DATE_TO"));
				archiveLog.setCreationTime(rs.getDate("CREATION_TIME"));
				archiveLog.setLogText(rs.getString("LOG_TEXT"));

				return archiveLog;
			}
		});
		return archiveLogsList;
	}

	@Override
	public List<ArchiveLog> getArchiveLogsByModuleId(Long moduleId) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(moduleId);
		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where MODUL_ID = ? order by id";

		List<ArchiveLog> archiveLogsList = (List<ArchiveLog>) jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ArchiveLog>() {
			@Override
			public ArchiveLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArchiveLog archiveLog = new ArchiveLog();
				archiveLog.setId(rs.getLong("ID"));
				archiveLog.setAid(rs.getLong("AID"));
				archiveLog.setStatus(rs.getLong("STATUS"));
				archiveLog.setLogName(rs.getString("LOG_PATH"));
				archiveLog.setDateFrom(rs.getDate("DATE_FROM"));
				archiveLog.setDateTo(rs.getDate("DATE_TO"));
				archiveLog.setCreationTime(rs.getDate("CREATION_TIME"));
				archiveLog.setLogText(rs.getString("LOG_TEXT"));

				return archiveLog;
			}
		});
		return archiveLogsList;
	}

	@Override
	public ArchiveLog getArchiveLog(Long id) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(id);
		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where id = ? order by id";

		List<ArchiveLog> archiveLogsList = (List<ArchiveLog>) jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ArchiveLog>() {
			@Override
			public ArchiveLog mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArchiveLog archiveLog = new ArchiveLog();
				archiveLog.setId(rs.getLong("ID"));
				archiveLog.setAid(rs.getLong("AID"));
				archiveLog.setStatus(rs.getLong("STATUS"));
				archiveLog.setLogName(rs.getString("LOG_PATH"));
				archiveLog.setDateFrom(rs.getDate("DATE_FROM"));
				archiveLog.setDateTo(rs.getDate("DATE_TO"));
				archiveLog.setCreationTime(rs.getDate("CREATION_TIME"));
				archiveLog.setLogText(rs.getString("LOG_TEXT"));

				return archiveLog;
			}
		});
		return archiveLogsList.get(0);
	}

	public void updateArchiveLog(ArchiveLog archiveLog) {

		String selectQuery = "update LDARCHIVELOG set LOG_TEXT =?, STATUS = ? WHERE ID = ?";

		jdbcTemplate.update(selectQuery, new Object[] { archiveLog.getLogText(), archiveLog.getStatus(), archiveLog.getId() });
	}
}
