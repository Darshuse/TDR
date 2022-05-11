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

package com.eastnets.dao.filepayload;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.filepayload.FilePayloadGlobalSettings;
import com.eastnets.domain.filepayload.FilePayloadSettings;

/**
 * File Payload DAO Implementation
 * 
 * @author EastNets
 * @since September 5, 2013
 */
public abstract class FilePayloadDAOImp extends DAOBaseImp implements FilePayloadDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4477537555281995040L;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

	@Override
	public FilePayloadGlobalSettings getGlobalSettings() {
		String query = "select payload_try_count_max, payload_try_delay_min, payload_get_chunk_cnt, payload_transfer_path " + " from  ldGlobalSettings";

		FilePayloadGlobalSettings globalSettings = jdbcTemplate.query(query, new ResultSetExtractor<FilePayloadGlobalSettings>() {

			@Override
			public FilePayloadGlobalSettings extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {
					FilePayloadGlobalSettings globalSettings = new FilePayloadGlobalSettings();
					globalSettings.setMaxTryCount(rs.getInt("payload_try_count_max"));
					globalSettings.setDelayMinutes(rs.getInt("payload_try_delay_min"));
					globalSettings.setChunkCount(rs.getInt("payload_get_chunk_cnt"));
					globalSettings.setPayloadTransferePath(rs.getString("payload_transfer_path"));

					return globalSettings;
				}
				return null;

			}
		});
		return globalSettings;
	}

	@Override
	public FilePayloadSettings getConnectionSettings(int aid) {
		String query = "select srv_address, files_remote_path, files_local_path, files_server_port" /* + ",  standalone " */ + " from  ldSettings where aid = " + aid;

		FilePayloadSettings payloadSettings = jdbcTemplate.query(query, new ResultSetExtractor<FilePayloadSettings>() {

			@Override
			public FilePayloadSettings extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					FilePayloadSettings payloadSettings = new FilePayloadSettings();
					payloadSettings.setServerAddress(rs.getString("srv_address"));
					payloadSettings.setRemotePath(rs.getString("files_remote_path"));
					payloadSettings.setLocalPath(rs.getString("files_local_path"));
					payloadSettings.setServerPort(rs.getInt("files_server_port"));
					// payloadSettings.setStandalone(rs.getBoolean("standalone"));
					return payloadSettings;
				}
				return null;
			}
		});
		return payloadSettings;
	}

	@Override
	public void resetRequestedFlag(int aid) {
		String query = "Update rFile set requested = 0 where requested <> 0 and aid = " + aid;
		jdbcTemplate.execute(query);
	}

	@Override
	public void updateStatus(FilePayload payload, String status, int requested) {
		String query = "Update rFile set requested = " + requested;
		if (status != null) {
			query += " , transfer_status= '" + status.trim() + "'";
		}
		query += " where " + String.format(" aid = %d and file_s_umidl = %d and file_s_umidh = %d and mesg_crea_date_time = %s ", payload.getAid(), payload.getSumidl(), payload.getSumidh(),
				getDbPortabilityHandler().getFormattedDateWithNoBinding(payload.getCreationDateTime(), true));
		jdbcTemplate.execute(query);
	}

	@Override
	public void resetRequested(int aid, double requestedResetTimeHours) {
		String query = "Update rFile set requested = 0 where requested <> 0 and aid = " + aid + " and (sysdate - last_try) * 24 > " + requestedResetTimeHours;
		jdbcTemplate.execute(query);

	}

	@Override
	public List<String> getTextFileExtensions() {
		String query = "select PROPVALUE from reportingproperties where PROPNAME = 'TextPayloadExtensions' ";

		List<String> payloads = jdbcTemplate.query(query, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String value = rs.getString("PROPVALUE");

				return value;
			}
		});
		return payloads;
	}

	@Override
	public String getReportingProperty(String propertyName) {
		String query = "select PROPVALUE from reportingproperties where PROPNAME = '" + propertyName + "' ";

		List<String> values = jdbcTemplate.query(query, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String value = rs.getString("PROPVALUE");
				return value;
			}
		});
		if (values.size() == 0) {
			return null;
		}
		return values.get(0);
	}

	@Override
	public ArrayList<String> getXSDIdentifier() {
		// TODO Auto-generated method stub
		String sql = "SELECT identifier_value FROM xmlTypes WHERE xml_type='SEPA' and type_status = '1'";
		ArrayList<String> xsdlist = new ArrayList<String>();
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		for (Map row : rows) {
			String xsdName = ((String) row.get("identifier_value"));
			xsdlist.add(xsdName);
		}
		return xsdlist;
	}

	protected static String getDateStr() {
		return dateFormat.format(new Date());
	}

}
