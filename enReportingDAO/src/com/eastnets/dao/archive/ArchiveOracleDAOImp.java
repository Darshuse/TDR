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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.archive.ArchiveLog;
import com.eastnets.domain.archive.RestoreSet;
import com.eastnets.utils.ApplicationUtils;

/**
 * Archive Oracle DAO Implementation
 * 
 * @author EastNets
 * @since October 4, 2012
 */
public class ArchiveOracleDAOImp extends ArchiveDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7664625973306464639L;
	private LobHandler lobHandler;

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public List<RestoreSet> getRestoreSet(Long aid, String dateFrom, String dateTo) {

		StringBuilder selectQuery = new StringBuilder();

		selectQuery.append("select aid, name, restored_at, min_date, max_date ");
		selectQuery.append(" From ldRestoreSet ");
		selectQuery.append(" WHERE ");
		selectQuery.append(" aid = ");
		selectQuery.append(aid);
		selectQuery.append(" AND set_type = 2 ");
		selectQuery.append(" AND");
		selectQuery.append(" (( ");
		selectQuery.append(" Min_Date >= TO_DATE( '");
		selectQuery.append(dateFrom);
		selectQuery.append("', '");
		selectQuery.append(Constants.ORACLE_DATE_TIME_PATTERN);
		selectQuery.append("' ) ");
		selectQuery.append(" AND Min_Date <= TO_DATE( '");
		selectQuery.append(dateTo);
		selectQuery.append("', '");
		selectQuery.append(Constants.ORACLE_DATE_TIME_PATTERN);
		selectQuery.append("' )) ");
		selectQuery.append(" OR ( Max_Date >= TO_DATE( '");
		selectQuery.append(dateFrom);
		selectQuery.append("', '");
		selectQuery.append(Constants.ORACLE_DATE_TIME_PATTERN);
		selectQuery.append("' ) ");
		selectQuery.append(" AND ");
		selectQuery.append(" Max_Date <= TO_DATE( '");
		selectQuery.append(dateTo);
		selectQuery.append("', '");
		selectQuery.append(Constants.ORACLE_DATE_TIME_PATTERN);
		selectQuery.append("' ) ");
		selectQuery.append(" ))");

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

		Date creationTime = archiveLog.getCreationTime();

		String formatDate = null;

		formatDate = ApplicationUtils.formatDateTime(creationTime);
		String dateFrom = ApplicationUtils.formatDateTime(archiveLog.getDateFrom());
		String dateTo = ApplicationUtils.formatDateTime(archiveLog.getDateTo());

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO LDARCHIVELOG(ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM,DATE_TO) VALUES( ");
		builder.append("LDARCHIVELOG_ID.NEXTVAL, ? ,TO_DATE(?, '" + Constants.ORACLE_DATE_TIME_PATTERN + "' ), ? , ? , ?");
		if (dateFrom == null || dateFrom.trim().equals("")) {
			builder.append(" ,");
			builder.append("NULL");
			builder.append(", TO_DATE(");
		} else {
			builder.append(" ,TO_DATE('");
			builder.append(dateFrom);
			builder.append("', '");
			builder.append(Constants.ORACLE_DATE_TIME_PATTERN);
			builder.append("' ), TO_DATE(");
		}

		builder.append("?, '" + Constants.ORACLE_DATE_TIME_PATTERN + "' ))");

		String insertQuery = builder.toString();

		jdbcTemplate.update(insertQuery, new Object[] { archiveLog.getModuleId(), formatDate, archiveLog.getAid(), archiveLog.getStatus(), archiveLog.getLogName(), dateTo });

	}

	@Override
	public ArchiveLog getArchiveLog(Long moduleId, Date creationTime, Long aid) {
		String formatDate = ApplicationUtils.formatDateTime(creationTime);
		List<Object> parameters = new ArrayList<>();
		parameters.add(moduleId);
		parameters.add(formatDate);
		parameters.add(aid);

		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where MODUL_ID = ? and CREATION_TIME = TO_DATE(?,'" + Constants.ORACLE_DATE_TIME_PATTERN + "') and AID = ?";

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
				Clob clob = rs.getClob("LOG_TEXT");

				if (clob == null) {
					archiveLog.setLogText("");
				} else {
					Reader characterStream = clob.getCharacterStream();
					StringBuffer str = new StringBuffer();
					String string;
					BufferedReader bufferRead = new BufferedReader(characterStream);

					try {
						while ((string = bufferRead.readLine()) != null) {
							str.append(string);
							str.append('\n');
						}
						bufferRead.close();
					} catch (IOException e) {
						//
						e.printStackTrace();
					}
					archiveLog.setLogText(str.toString());
				}

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
				Clob clob = rs.getClob("LOG_TEXT");

				if (clob == null) {
					archiveLog.setLogText("");
				} else {
					StringBuffer str = new StringBuffer();
					String string;
					BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

					try {
						while ((string = bufferRead.readLine()) != null) {
							str.append(string);
							str.append('\n');
						}
						bufferRead.close();
					} catch (IOException e) {
						//
						e.printStackTrace();
					}
					archiveLog.setLogText(str.toString());
				}
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
				Clob clob = rs.getClob("LOG_TEXT");

				if (clob == null) {
					archiveLog.setLogText("");
				} else {
					StringBuffer str = new StringBuffer();
					String string;
					BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

					try {
						while ((string = bufferRead.readLine()) != null) {
							str.append(string);
							str.append('\n');
						}
						bufferRead.close();
					} catch (IOException e) {
						//
						e.printStackTrace();
					}
					archiveLog.setLogText(str.toString());
				}
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

		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where aid = ? and MODUL_ID = ? order by id";

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
				Clob clob = rs.getClob("LOG_TEXT");

				if (clob == null) {
					archiveLog.setLogText("");
				} else {
					StringBuffer str = new StringBuffer();
					String string;
					BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

					try {
						while ((string = bufferRead.readLine()) != null) {
							str.append(string);
							str.append('\n');
						}
						bufferRead.close();
					} catch (IOException e) {
						//
						e.printStackTrace();
					}
					archiveLog.setLogText(str.toString());
				}
				return archiveLog;
			}
		});
		return archiveLogsList;
	}

	@Override
	public List<ArchiveLog> getArchiveLogsByModuleId(Long moduleId) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(moduleId);
		String selectQuery = "SELECT ID, MODUL_ID, CREATION_TIME, AID,STATUS,LOG_PATH,DATE_FROM, DATE_TO,LOG_TEXT FROM ldArchiveLog where MODUL_ID =?  order by id";

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
				Clob clob = rs.getClob("LOG_TEXT");

				if (clob == null) {
					archiveLog.setLogText("");
				} else {
					StringBuffer str = new StringBuffer();
					String string;
					BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

					try {
						while ((string = bufferRead.readLine()) != null) {
							str.append(string);
							str.append('\n');
						}
						bufferRead.close();
					} catch (IOException e) {
						//
						e.printStackTrace();
					}
					archiveLog.setLogText(str.toString());
				}
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
				Clob clob = rs.getClob("LOG_TEXT");

				if (clob == null) {
					archiveLog.setLogText("");
				} else {
					StringBuffer str = new StringBuffer();
					String string;
					BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

					try {
						while ((string = bufferRead.readLine()) != null) {
							str.append(string);
							str.append('\n');
						}
						bufferRead.close();
					} catch (IOException e) {
						//
						e.printStackTrace();
					}
					archiveLog.setLogText(str.toString());
				}

				return archiveLog;
			}
		});
		return archiveLogsList.get(0);
	}

	public void updateArchiveLog(final ArchiveLog archiveLog) {

		String updateQuery = "update LDARCHIVELOG set LOG_TEXT = ? , STATUS = ? WHERE ID = ?";

		jdbcTemplate.update(updateQuery, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				String logText = archiveLog.getLogText();
				lobHandler.getLobCreator().setClobAsString(ps, 1, logText);
				Long status = archiveLog.getStatus();
				ps.setLong(2, status);
				Long id = archiveLog.getId();
				ps.setLong(3, id);

			}
		});
	}
}
