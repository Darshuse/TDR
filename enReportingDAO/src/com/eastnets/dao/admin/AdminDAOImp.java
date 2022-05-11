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

package com.eastnets.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.admin.procedure.AdminInsertConnectionProcedure;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.Config;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.AdminSettings;
import com.eastnets.domain.admin.ApprovalStatus;
import com.eastnets.domain.admin.CsmConnection;
import com.eastnets.domain.admin.JIResourceFolder;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.LoaderSettings;
import com.eastnets.domain.admin.MsgCatPerProfile;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.admin.UserThemePreferences;
import com.eastnets.utils.Utils;

/**
 * Administration DAO Implementation
 * 
 * @author EastNets
 * @since July 19, 2012
 */
public abstract class AdminDAOImp extends DAOBaseImp implements AdminDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8914969275195496419L;
	private AdminInsertConnectionProcedure adminInsertConnectionProcedure;
	private Config config;
	private ApplicationFeatures applicationFeatures;

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	private List<LoaderConnection> getUmids(final LoaderConnection loaderConnection, List<Date> maxMesgCreationDate) {

		String getUMIDsQuery = null;
		String selectQuery = "SELECT";
		String block = "mesg_s_umidl, mesg_s_umidh from rMesg";
		String condition = "WHERE aid = ? and %s";

		Date maxDate = maxMesgCreationDate.get(0);
		if (maxDate == null) {
			return null;
		}

		String dateFormat = getDbPortabilityHandler().getOneDayEqualCondition(maxDate, "mesg_crea_date_time");
		condition = String.format(condition, dateFormat);

		getUMIDsQuery = getDbPortabilityHandler().buildRownumQuery(selectQuery, block, condition, 1);

		List<LoaderConnection> umids = jdbcTemplate.query(getUMIDsQuery, new Object[] { loaderConnection.getAid() }, new RowMapper<LoaderConnection>() {
			public LoaderConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoaderConnection temploaderConnection = new LoaderConnection();
				temploaderConnection.setAid(loaderConnection.getAid());
				temploaderConnection.setLastUmidl((rs.getLong("mesg_s_umidl")));
				temploaderConnection.setLastUmidh(rs.getLong("mesg_s_umidh"));
				return temploaderConnection;
			}
		});
		return umids;
	}

	public void setAdminInsertConnectionProcedure(AdminInsertConnectionProcedure adminInsertConnectionProcedure) {
		this.adminInsertConnectionProcedure = adminInsertConnectionProcedure;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public AdminInsertConnectionProcedure getAdminInsertConnectionProcedure() {
		return adminInsertConnectionProcedure;
	}

	@Override
	public LoaderSettings getLoaderSettings() {
		String queryString = "SELECT Cycle_active_time,  Cycle_sleep_time,  Live_update,  Live_source" + ",  Live_delay,  Live_Update_window,  Confidence_scan_time,  Disable_update,  Disable_start_time, "
				+ "Disable_duration,  Active_days_monday,  Active_days_tuesday,  Active_days_wednesday,  Active_days_thursday, " + "Active_days_friday, Active_days_saturday,  Active_days_sunday,  Skip_Interventions,  Store_Original_Only, "
				+ "offlinewatchdog,  offlinedecomposition, parse_textblock , load_event ";

		if (applicationFeatures.isFilePayloadSupported())
			queryString += ", payload_try_count_max, payload_try_delay_min, payload_get_chunk_cnt ";

		queryString += " FROM ldGlobalSettings";

		LoaderSettings loaderSettings = (LoaderSettings) jdbcTemplate.queryForObject(queryString, new RowMapper<LoaderSettings>() {
			public LoaderSettings mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoaderSettings loaderSettings = new LoaderSettings();
				loaderSettings.setActiveTime(rs.getInt("Cycle_active_time"));
				loaderSettings.setSleepTime(rs.getInt("Cycle_sleep_time"));
				loaderSettings.setLiveMessageUpdate(rs.getInt("Live_update"));
				loaderSettings.setLiveSource(rs.getString("Live_source"));
				loaderSettings.setUpdateDelayMax(rs.getInt("Live_Update_window"));
				loaderSettings.setUpdateDelayMin(rs.getInt("Live_delay"));
				loaderSettings.setDailyScanTime(rs.getInt("Confidence_scan_time"));

				loaderSettings.setDisableUpdate(rs.getInt("Disable_update"));
				loaderSettings.setDisableStartTime(rs.getInt("Disable_start_time"));
				// loaderSettings.setDisableEndTime(rs.getInt(""));
				loaderSettings.setDisableDuration(rs.getInt("Disable_duration"));
				loaderSettings.setActiveDaysMon(rs.getInt("Active_days_monday"));
				loaderSettings.setActiveDaysTue(rs.getInt("Active_days_tuesday"));
				loaderSettings.setActiveDaysWed(rs.getInt("Active_days_wednesday"));
				loaderSettings.setActiveDaysThu(rs.getInt("Active_days_thursday"));
				loaderSettings.setActiveDaysFri(rs.getInt("Active_days_friday"));
				loaderSettings.setActiveDaysSat(rs.getInt("Active_days_saturday"));
				loaderSettings.setActiveDaysSun(rs.getInt("Active_days_sunday"));

				loaderSettings.setEnableOfflineDecomposition(rs.getInt("offlinedecomposition"));
				loaderSettings.setParseTextBlock(rs.getInt("parse_textblock"));

				loaderSettings.setEnableOfflineWatchdog(rs.getInt("offlinewatchdog"));

				loaderSettings.setSkipRoutingInterventions(rs.getInt("Skip_Interventions"));
				loaderSettings.setLoadEvents(rs.getInt("load_event"));
				loaderSettings.setStoreOriginalInstanceOnly(rs.getInt("Store_Original_Only"));

				if (applicationFeatures.isFilePayloadSupported()) {

					loaderSettings.setPayloadTryCountMax(rs.getInt("payload_try_count_max"));
					loaderSettings.setPayloadTryDelayMin(rs.getInt("payload_try_delay_min"));
					loaderSettings.setPayloadGetChunkCount(rs.getInt("payload_get_chunk_cnt"));

				}

				return loaderSettings;
			}
		});
		return loaderSettings;

	}

	@Override
	public List<String> getAvailableMessageTypes() {
		String queryString = "select distinct(type) from stxMessage  where type not in (select mesg_type from ldParseMsgType where enabled = 1) order by type";
		List<String> types = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String type;
				type = rs.getString("type");
				return type;
			}
		});
		return types;
	}

	@Override
	public List<String> getSelectedMessageTypes() {
		String queryString = "select mesg_type from ldParseMsgType where enabled = 1";
		List<String> types = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String type;
				type = rs.getString("mesg_type");
				return type;
			}
		});
		return types;
	}

	@Override
	public void updateLoaderSettings(LoaderSettings loaderSettings) {
		StringBuffer updateStatement = new StringBuffer();
		updateStatement.append("UPDATE ldGlobalSettings SET ");
		updateStatement.append("Cycle_active_time = ? , ");
		updateStatement.append("Cycle_sleep_time  = ? , ");
		updateStatement.append("Live_update = ? , ");
		updateStatement.append("Live_source = ? , ");
		updateStatement.append("Live_delay = ? , ");
		updateStatement.append("Live_Update_window = ? , ");
		updateStatement.append("Confidence_scan_time = ? , ");

		updateStatement.append("Disable_update = ? , ");
		updateStatement.append("Disable_start_time = ? , ");
		updateStatement.append("Disable_duration = ? , ");
		updateStatement.append("Active_days_monday = ? , ");
		updateStatement.append("Active_days_tuesday = ? , ");
		updateStatement.append("Active_days_wednesday = ? , ");
		updateStatement.append("Active_days_thursday = ? , ");
		updateStatement.append("Active_days_friday = ? , ");
		updateStatement.append("Active_days_saturday = ? , ");
		updateStatement.append("Active_days_sunday = ? , ");

		updateStatement.append("Offlinedecomposition  = ? , ");
		updateStatement.append("PARSE_TEXTBLOCK  = ? , ");

		updateStatement.append("Offlinewatchdog  = ? , ");

		updateStatement.append("Skip_interventions  = ? , ");
		updateStatement.append("Load_event  = ? , ");
		updateStatement.append("Store_Original_Only  = ?  ");

		if (applicationFeatures.isFilePayloadSupported()) {
			updateStatement.append(", payload_try_count_max  = ? , ");
			updateStatement.append("payload_try_delay_min  = ? , ");
			updateStatement.append("payload_get_chunk_cnt  = ?  ");

		}

		if (applicationFeatures.isFilePayloadSupported()) {
			jdbcTemplate.update(updateStatement.toString(),
					new Object[] { loaderSettings.getActiveTime(), loaderSettings.getSleepTime(), loaderSettings.getLiveMessageUpdate(), loaderSettings.getLiveSource(), loaderSettings.getUpdateDelayMin(), loaderSettings.getUpdateDelayMax(),
							loaderSettings.getDailyScanTime(),

							loaderSettings.getDisableUpdate(), loaderSettings.getDisableStartTime(), loaderSettings.getDisableDuration(), loaderSettings.getActiveDaysMon(), loaderSettings.getActiveDaysTue(), loaderSettings.getActiveDaysWed(),
							loaderSettings.getActiveDaysThu(), loaderSettings.getActiveDaysFri(), loaderSettings.getActiveDaysSat(), loaderSettings.getActiveDaysSun(),

							loaderSettings.getEnableOfflineDecomposition(), loaderSettings.getParseTextBlock(), loaderSettings.getEnableOfflineWatchdog(),

							loaderSettings.getSkipRoutingInterventions(), loaderSettings.getLoadEvents(), loaderSettings.getStoreOriginalInstanceOnly(),

							loaderSettings.getPayloadTryCountMax(), loaderSettings.getPayloadTryDelayMin(), loaderSettings.getPayloadGetChunkCount()

					});
		} else {

			jdbcTemplate.update(updateStatement.toString(),
					new Object[] { loaderSettings.getActiveTime(), loaderSettings.getSleepTime(), loaderSettings.getLiveMessageUpdate(), loaderSettings.getLiveSource(), loaderSettings.getUpdateDelayMin(), loaderSettings.getUpdateDelayMax(),
							loaderSettings.getDailyScanTime(),

							loaderSettings.getDisableUpdate(), loaderSettings.getDisableStartTime(), loaderSettings.getDisableDuration(), loaderSettings.getActiveDaysMon(), loaderSettings.getActiveDaysTue(), loaderSettings.getActiveDaysWed(),
							loaderSettings.getActiveDaysThu(), loaderSettings.getActiveDaysFri(), loaderSettings.getActiveDaysSat(), loaderSettings.getActiveDaysSun(),

							loaderSettings.getEnableOfflineDecomposition(), loaderSettings.getParseTextBlock(), loaderSettings.getEnableOfflineWatchdog(),

							loaderSettings.getSkipRoutingInterventions(), loaderSettings.getLoadEvents(), loaderSettings.getStoreOriginalInstanceOnly()

					});
		}

	}

	@Override
	public void deleteLdParseMsgeTypes() {
		jdbcTemplate.execute("DELETE ldParseMsgType");
	}

	@Override
	public void addLdParseMsgeType(String type) {
		String insertQuery = String.format("INSERT INTO ldParseMsgType (mesg_type, enabled) VALUES ('%s', %d)", type, 1);
		jdbcTemplate.execute(insertQuery);
	};

	@Override
	public List<String> getLicensedBICs() {
		String queryString = "SELECT BICCode FROM sLicensedBIC ORDER BY BICCode";
		List<String> bics = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String bic;
				bic = rs.getString("BICCode");
				return bic;
			}
		});
		return bics;
	}

	@Override
	public void addBIC(String bic) {
		jdbcTemplate.execute("INSERT INTO sLicensedBIC (BICCode, LicenseInfo, volume ) VALUES ('" + bic + "', '0,0,0,0,1456060A5E154908', 249)");
	}

	@Override
	public void deleteBIC(String bic) {
		jdbcTemplate.execute("DELETE sLicensedBIC WHERE upper(BICCode) =  upper('" + bic + "')");
	}

	@Override
	public void addUnit(String unit) {
		jdbcTemplate.execute("INSERT INTO sUnit (Unit) VALUES ('" + unit + "')");

	}

	@Override
	public void deleteUnit(String unit) {
		jdbcTemplate.execute("DELETE sUnit WHERE Unit =  ('" + unit + "')");
	}

	@Override
	public List<String> getAllUnits() {

		String queryString = "SELECT INST_UNIT_NAME FROM ldHelperUnitName";
		List<String> units = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String unit = rs.getString("INST_UNIT_NAME");
				return unit;
			}
		});
		return units;
	}

	@Override
	public List<Profile> getProfiles() {
		List<Profile> actualProfiles = new ArrayList<Profile>();
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();
		String getProfilesQuery = "";
		if (isAnalyticsSupported) {
			getProfilesQuery = "SELECT Name,Description,GroupID,biDirectory,sUserGroup.ApprovalStatus AS ApprovalStatus, sApprovalStatuses.StatusDescription AS StatusDescription, sUserGroup.RPDIRECTORY as RPDIRECTORY FROM sUserGroup INNER JOIN sApprovalStatuses ON sUserGroup.APPROVALSTATUS = sApprovalStatuses.Status ORDER BY UPPER(Name)";
		} else {
			getProfilesQuery = "SELECT Name,Description,GroupID,sUserGroup.ApprovalStatus AS ApprovalStatus, sApprovalStatuses.StatusDescription AS StatusDescription, sUserGroup.RPDIRECTORY as RPDIRECTORY FROM sUserGroup INNER JOIN sApprovalStatuses ON sUserGroup.APPROVALSTATUS = sApprovalStatuses.Status ORDER BY UPPER(Name)";

		}
		List<Profile> profiles = jdbcTemplate.query(getProfilesQuery, new RowMapper<Profile>() {
			public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
				Profile profile = new Profile();
				profile.setName(rs.getString("Name"));
				profile.setDescription(rs.getString("Description"));
				ApprovalStatus approvalStatus = new ApprovalStatus();
				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				approvalStatus.setDescription(rs.getString("StatusDescription"));
				profile.setApprovalStatus(approvalStatus);
				profile.setGroupId(rs.getLong("GroupID"));
				profile.setRpDirectory(rs.getString("RPDIRECTORY"));
				if (isAnalyticsSupported) {
					profile.setBiDirectory(rs.getString("biDirectory"));
				}
				return profile;
			}
		});

		// Remove Dummay Profile
		for (Profile profile : profiles) {
			if (profile.getName().equals("LSA&RSADummayProfile")) {

				// doNothing
			} else if (profile.getName().equals("LSARSADummayProfile")) {
				// doNothing
			} else {
				actualProfiles.add(profile);
			}

		}

		// LSA&RSADummayProfile
		return actualProfiles;
	}

	@Override
	public Profile getProfileGeneralInfo(String groupId) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();
		try {
			String queryString = String.format("SELECT * FROM sUserGroup WHERE GroupId = %s", groupId);
			Profile profile = (Profile) jdbcTemplate.queryForObject(queryString, new RowMapper<Profile>() {
				public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
					Profile profile = new Profile();
					profile.setName(rs.getString("Name"));
					profile.setGroupId(rs.getLong("GroupID"));
					profile.setDescription(rs.getString("Description"));
					profile.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
					profile.setVwListDepth(rs.getLong("vwListDepth"));
					profile.setRpDirectory(rs.getString("rpDirectory"));
					if (isAnalyticsSupported) {
						profile.setBiDirectory(rs.getString("biDirectory"));
					}
					profile.setSenderEmail(rs.getString("SenderEMail"));
					profile.setConnectionTimeout(rs.getLong("connection_timeout"));
					return profile;
				}
			});
			return profile;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public List<Action> getAvailableActions(Integer moduleId) {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT sObjects.ID, ");
		selectQuery.append(" sObjects.ProgramID, ");
		selectQuery.append(" sObjects.Name ");
		selectQuery.append(" FROM sObjects ");
		selectQuery.append(" WHERE sObjects.ProgramId =? ");
		selectQuery.append(" ORDER BY sObjects.ID");

		Object[] param = { moduleId };
		List<Action> actions = jdbcTemplate.query(selectQuery.toString(), param, new RowMapper<Action>() {
			public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
				Action action = new Action();
				action.setActionId(rs.getLong("ID"));
				action.setActionName(rs.getString("NAME"));
				action.setModuleId(rs.getInt("PROGRAMID"));
				return action;
			}
		});
		return actions;
	}

	@Override
	public List<LoaderConnection> getLoaderConnections() {

		String getConnectionsQuery = "SELECT aid,alliance_info,Port,Time_Out,SRV_Address,alliance_instance ";

		if (applicationFeatures.isFilePayloadSupported())
			getConnectionsQuery += ", files_local_path ";

		getConnectionsQuery += " FROM ldSettings ORDER BY aid";

		List<LoaderConnection> connections = jdbcTemplate.query(getConnectionsQuery, new RowMapper<LoaderConnection>() {
			public LoaderConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoaderConnection loaderConnection = new LoaderConnection();
				loaderConnection.setAid((rs.getLong("AID")));
				loaderConnection.setAllianceInfo(rs.getString("ALLIANCE_INFO"));
				loaderConnection.setAllianceInstance(rs.getString("ALLIANCE_INSTANCE"));
				loaderConnection.setPort(rs.getLong("PORT"));
				loaderConnection.setServerAddress(rs.getString("SRV_ADDRESS"));
				loaderConnection.setTimeOut(rs.getLong("TIME_OUT"));

				if (applicationFeatures.isFilePayloadSupported()) {

					loaderConnection.setFilesLocalPath(rs.getString("files_local_path"));
				}

				return loaderConnection;
			}
		});

		return connections;
	}

	@Override
	public LoaderConnection getLoaderConnection(Long aid) {

		String queryStr = "SELECT aid,alliance_info,Port,Time_Out,SRV_Address,alliance_instance ";

		if (applicationFeatures.isFilePayloadSupported())
			queryStr += ", files_local_path ";

		queryStr += " FROM ldSettings where aid = %d ORDER BY aid ";

		String getConnectionQuery = String.format(queryStr, aid);

		List<LoaderConnection> connections = (List<LoaderConnection>) jdbcTemplate.query(getConnectionQuery, new RowMapper<LoaderConnection>() {
			public LoaderConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoaderConnection loaderConnection = new LoaderConnection();
				loaderConnection.setAid((rs.getLong("AID")));
				loaderConnection.setAllianceInfo(rs.getString("ALLIANCE_INFO"));
				loaderConnection.setAllianceInstance(rs.getString("ALLIANCE_INSTANCE"));
				loaderConnection.setPort(rs.getLong("PORT"));
				loaderConnection.setServerAddress(rs.getString("SRV_ADDRESS"));
				loaderConnection.setTimeOut(rs.getLong("TIME_OUT"));

				if (applicationFeatures.isFilePayloadSupported()) {
					loaderConnection.setFilesLocalPath(rs.getString("files_local_path"));
				}

				return loaderConnection;
			}
		});

		return connections.get(0);

	}

	@Override
	public void updateLoaderConnection(LoaderConnection loaderConnection) {

		StringBuffer updateStatement = new StringBuffer();

		updateStatement.append("UPDATE ldSettings SET ");
		updateStatement.append("alliance_info = ? ,");
		updateStatement.append("port = ? , ");
		updateStatement.append("time_out = ? , ");
		updateStatement.append("srv_address = ? ");

		if (applicationFeatures.isFilePayloadSupported()) {
			updateStatement.append(", files_local_path = ?  ");
		}

		updateStatement.append(" WHERE aid = ? ");

		if (applicationFeatures.isFilePayloadSupported()) {
			jdbcTemplate.update(updateStatement.toString(), new Object[] {

					loaderConnection.getAllianceInfo(), loaderConnection.getPort(), loaderConnection.getTimeOut(), loaderConnection.getServerAddress(), loaderConnection.getFilesLocalPath(), loaderConnection.getAid() });
		} else {
			jdbcTemplate.update(updateStatement.toString(), new Object[] {

					loaderConnection.getAllianceInfo(), loaderConnection.getPort(), loaderConnection.getTimeOut(), loaderConnection.getServerAddress(), loaderConnection.getAid() });
		}
	}

	@Override
	public Integer getMaxLoaderConnections() {

		String getConnectionsCountQuery = "SELECT MaxConnections FROM sLicenseInfo ";
		Integer connectionsCount = jdbcTemplate.queryForInt(getConnectionsCountQuery, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Integer connectionsCount = rs.getInt(1);
				return connectionsCount;
			}
		});

		return connectionsCount;
	}

	@Override
	public void syncLoaderConenction(LoaderConnection loaderConnection, Date date) {

		String updateConnectionQuery = "UPDATE ldSettings SET LAST_UMIDL = ?, LAST_UMIDH = ? WHERE aid = ? ";

		List<Date> maxMesgCreationDate = getMaxMesgCreationDatebteween(loaderConnection, date);

		if (maxMesgCreationDate == null || maxMesgCreationDate.size() <= 0 || maxMesgCreationDate.get(0) == null) {
			maxMesgCreationDate = getMaxMesgCreationDate(loaderConnection, date);

		}

		List<LoaderConnection> umids = getUmids(loaderConnection, maxMesgCreationDate);

		if (umids != null && umids.size() > 0) {
			LoaderConnection loaderconnection1 = (LoaderConnection) umids.get(0);

			jdbcTemplate.update(updateConnectionQuery.toString(), new Object[] { loaderconnection1.getLastUmidl(), loaderconnection1.getLastUmidh(), loaderConnection.getAid()

			});

		}

	}

	@Override
	public List<Date> getMaxMesgCreationDate(LoaderConnection loaderConnection, Date date) {
		String maxMesgCreaTimeQuery = "SELECT max(mesg_crea_date_time) from rmesg where aid = ? and %s";
		String dateFormat = getDbPortabilityHandler().getOneDayLessthanOrEqualCondition(date, "mesg_crea_date_time");

		maxMesgCreaTimeQuery = String.format(maxMesgCreaTimeQuery, dateFormat);

		List<Date> maxDate = jdbcTemplate.query(maxMesgCreaTimeQuery, new Object[] { loaderConnection.getAid() }, new RowMapper<Date>() {
			public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
				if (rs.getTimestamp(1) != null) {
					return new Date(rs.getTimestamp(1).getTime());
				}
				return null;
			}
		});

		return maxDate;
	}

	@Override
	public List<Date> getMaxMesgCreationDatebteween(LoaderConnection loaderConnection, Date date) {

		String maxMesgCreaTimeQuery = "SELECT max(mesg_crea_date_time) from rmesg where aid = ? and %s";
		String dateFormat = getDbPortabilityHandler().getOneDayCondition(date, "mesg_crea_date_time");

		maxMesgCreaTimeQuery = String.format(maxMesgCreaTimeQuery, dateFormat);

		List<Date> maxDate = jdbcTemplate.query(maxMesgCreaTimeQuery, new Object[] { loaderConnection.getAid() }, new RowMapper<Date>() {
			public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
				if (rs.getTimestamp(1) != null) {
					return new Date(rs.getTimestamp(1).getTime());
				}
				return null;
			}
		});

		return maxDate;
	}

	@Override
	public List<User> getUsers() {
		return executeUsersQuery(this.getUsersQuery(), null, null);
	}

	@Override
	public List<User> getUsers(String userName, String profileName) {
		return executeUsersQuery(this.getUsersQuery(userName, profileName), userName, profileName);
	}

	@Override
	public User getUser(Long userId) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();

		StringBuilder selectUsersQuery = new StringBuilder();

		selectUsersQuery.append("SELECT");
		selectUsersQuery.append(" sUser.UserName AS UserName");
		selectUsersQuery.append(" , sUser.FullUserName AS FullUserName");
		selectUsersQuery.append(" , sUser.GroupID AS GroupID");
		selectUsersQuery.append(" , sUser.UserID AS UserID");
		selectUsersQuery.append(" , sUser.vwListDepth as vwListDepth");
		selectUsersQuery.append(" , sUser.wdNbDayHistory as wdNbDayHistory");
		selectUsersQuery.append(" , sUser.rpDirectory as rpDirectory");
		selectUsersQuery.append(" , sUser.Email as Email");
		selectUsersQuery.append(" , sUser.connection_timeout as connection_timeout");
		selectUsersQuery.append(" , sUser.canRunLoader as canRunLoader");
		selectUsersQuery.append(" , sUserGroup.Name AS GroupName");
		if (isAnalyticsSupported) {
			selectUsersQuery.append(" , sUserGroup.biDirectory AS biDirectory");
		}
		selectUsersQuery.append(" , sUserGroup.connection_timeout AS Groupconnection_timeout");
		selectUsersQuery.append(" , sUser.ApprovalStatus AS ApprovalStatus");
		selectUsersQuery.append(" , sApprovalStatuses.statusDescription AS statusDescription");
		selectUsersQuery.append(" , sUserGroup.RPDIRECTORY AS GroupDirectory");

		if (getApplicationFeatures().isNewSecurity()) {
			selectUsersQuery.append(" , sUser.AuthenticationMethod AS AuthenticationMethod");
			selectUsersQuery.append(" , sUser.Disabled AS Disabled");
			// selectUsersQuery.append(" , " + getDbPortabilityHandler().getDateDifferenceInDays( getDbPortabilityHandler().getSysDate(), "sUser.LastLogin") +" AS LastLoginDays");
			selectUsersQuery.append(" , sUser.LastLogin AS LastLoginDays");
			selectUsersQuery.append(" , sUser.ChangePassword AS ChangePassword");
			selectUsersQuery.append(" , " + getDbPortabilityHandler().getDateDifferenceInDays(getDbPortabilityHandler().getSysDate(), "sUser.PasswordResetDate") + " AS PasswordResetDays");
			selectUsersQuery.append(" , ExpirationDate");
			selectUsersQuery.append(" , " + getDbPortabilityHandler().getDateDifferenceInDays("sUser.ExpirationDate", getDbPortabilityHandler().getSysDate()) + " AS ExpirationDays");
			selectUsersQuery.append(" , sUser.AUTHENTICATOR");
		}

		selectUsersQuery.append(" FROM sUser ");
		selectUsersQuery.append(" INNER JOIN sUserGroup ON sUser.GroupID = sUserGroup.GroupID");
		selectUsersQuery.append(" INNER JOIN sApprovalStatuses ON sApprovalStatuses.Status = sUser.ApprovalStatus");

		selectUsersQuery.append(" WHERE ");

		selectUsersQuery.append(" sUser.UserName NOT IN ('LSA_USER' , 'RSA_USER' ) ");
		selectUsersQuery.append("AND sUser.UserID = ?");
		selectUsersQuery.append("  ORDER BY UserName");

		User user = (User) jdbcTemplate.queryForObject(selectUsersQuery.toString(), new Object[] { userId }, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				User user = new User();
				ApprovalStatus approvalStatus = new ApprovalStatus();
				Profile profile = new Profile();

				user.setApprovalStatus(approvalStatus);
				user.setProfile(profile);

				user.setUserName(rs.getString("UserName"));
				user.setFullUserName(rs.getString("FullUserName"));
				user.setUserId(rs.getLong("UserID"));
				user.setVwListDepth(rs.getLong("vwListDepth"));
				user.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
				user.setRpDirectory(rs.getString("rpDirectory"));
				user.setEmail(rs.getString("Email"));
				user.setConnectionTimeOut(rs.getLong("connection_timeout"));

				int canRunLoaderFlag = rs.getInt("canRunLoader");

				if (canRunLoaderFlag == 1) {
					user.setCanRunLoader(true);
				} else {
					user.setCanRunLoader(false);
				}

				if (getApplicationFeatures().isNewSecurity()) {
					user.setAuthenticationMethod(getNullableValue(rs, rs.getInt("AuthenticationMethod")));
					user.setDisabled(getNullableValue(rs, rs.getBoolean("Disabled")));
					// user.setLastLoginDays( getNullableValue(rs, rs.getDouble("LastLoginDays")));
					user.setLastLoginDays(rs.getTimestamp("LastLoginDays"));
					user.setChangePassword(getNullableValue(rs, rs.getBoolean("ChangePassword")));
					user.setPasswordResetDays(getNullableValue(rs, rs.getDouble("PasswordResetDays")));
					user.setExpirationDate(getNullableValue(rs, rs.getTimestamp("ExpirationDate")));
					user.setExpirationDays(getNullableValue(rs, rs.getDouble("ExpirationDays")));
					user.setAuthenticator(rs.getInt("AUTHENTICATOR"));
				}

				profile.setGroupId(rs.getLong("GroupID"));
				profile.setName(rs.getString("GroupName"));
				profile.setConnectionTimeout(rs.getLong("Groupconnection_timeout"));
				if (isAnalyticsSupported) {
					profile.setBiDirectory(rs.getString("biDirectory"));
				}
				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				profile.setRpDirectory(rs.getString("GroupDirectory"));
				approvalStatus.setDescription(rs.getString("statusDescription"));

				return user;
			}
		});

		return user;
	}

	@Override
	public User getUser(String userName) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();

		User user = new User();
		List<Object> parameters = new ArrayList<>();

		userName = userName.replace("'", "''");
		parameters.add(userName);

		StringBuilder selectUsersQuery = new StringBuilder();

		selectUsersQuery.append("SELECT");
		selectUsersQuery.append(" sUser.UserName AS UserName");
		selectUsersQuery.append(" , sUser.FullUserName AS FullUserName");
		selectUsersQuery.append(" , sUser.GroupID AS GroupID");
		selectUsersQuery.append(" , sUser.UserID AS UserID");
		selectUsersQuery.append(" , sUser.vwListDepth as vwListDepth");
		selectUsersQuery.append(" , sUser.wdNbDayHistory as wdNbDayHistory");
		selectUsersQuery.append(" , sUserGroup.rpDirectory as rpDirectory");
		selectUsersQuery.append(" , sUser.canRunLoader as canRunLoader");
		if (isAnalyticsSupported) {
			selectUsersQuery.append(" , sUserGroup.biDirectory AS biDirectory");
		}
		selectUsersQuery.append(" , sUser.rpDirectory as userRpDirectory");
		selectUsersQuery.append(" , sUser.Email as Email");
		selectUsersQuery.append(" , sUser.connection_timeout as connection_timeout");
		selectUsersQuery.append(" , sUser.numberPasswordAttempts as numberPasswordAttempts");
		if (getApplicationFeatures().isNewSecurity()) {
			selectUsersQuery.append(" , sUser.AUTHENTICATOR as Authenticator");
			selectUsersQuery.append(" , sUser.AuthenticationMethod AS AuthenticationMethod");
			selectUsersQuery.append(" , sUser.Disabled AS Disabled");
			selectUsersQuery.append(" , sUser.LastLogin AS LastLoginDays");
			selectUsersQuery.append(" , sUser.ChangePassword AS ChangePassword");
			selectUsersQuery.append(" , " + getDbPortabilityHandler().getDateDifferenceInDays(getDbPortabilityHandler().getSysDate(), "sUser.PasswordResetDate") + " AS PasswordResetDays");
			selectUsersQuery.append(" , sUser.ExpirationDate");
			selectUsersQuery.append(" , " + getDbPortabilityHandler().getDateDifferenceInDays(getDbPortabilityHandler().getSysDate(), "sUser.ExpirationDate") + " AS ExpirationDays");
			// selectUsersQuery.append(" , sUser.ACTIVATIONDATE AS ACTIVATIONDATE");
			// selectUsersQuery.append(" , sUser.DORMANCYSTATUS AS DORMANCYSTATUS");
		}

		selectUsersQuery.append(" , sUserGroup.Name AS GroupName");
		selectUsersQuery.append(" , sUserGroup.connection_timeout AS Groupconnection_timeout");
		selectUsersQuery.append(" , sUser.ApprovalStatus AS ApprovalStatus");
		selectUsersQuery.append(" , sApprovalStatuses.statusDescription AS statusDescription");
		selectUsersQuery.append(" FROM sUser ");
		selectUsersQuery.append(" INNER JOIN sUserGroup ON sUser.GroupID = sUserGroup.GroupID");
		selectUsersQuery.append(" INNER JOIN sApprovalStatuses ON sApprovalStatuses.Status = sUser.ApprovalStatus");

		selectUsersQuery.append(" WHERE ");

		selectUsersQuery.append(" sUser.UserName NOT IN ('LSA_USER' , 'RSA_USER' ) ");

		selectUsersQuery.append("AND UPPER(sUser.UserName) = upper(?) ");
		selectUsersQuery.append("  ORDER BY UserName");
		List<User> query = jdbcTemplate.query(selectUsersQuery.toString(), parameters.toArray(), new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rownum) throws SQLException {
				User user = new User();
				ApprovalStatus approvalStatus = new ApprovalStatus();
				Profile profile = new Profile();

				user.setApprovalStatus(approvalStatus);
				user.setProfile(profile);

				user.setUserName(rs.getString("UserName"));
				user.setFullUserName(rs.getString("FullUserName"));
				user.setUserId(rs.getLong("UserID"));
				user.setVwListDepth(rs.getLong("vwListDepth"));
				user.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
				profile.setRpDirectory(rs.getString("rpDirectory"));
				user.setRpDirectory(rs.getString("userRpDirectory"));
				user.setEmail(rs.getString("Email"));
				user.setConnectionTimeOut(rs.getLong("connection_timeout"));
				user.setNumberPasswordAttempts(rs.getInt("numberPasswordAttempts"));
				int canRunLoaderFlag = rs.getInt("canRunLoader");

				if (canRunLoaderFlag == 1) {
					user.setCanRunLoader(true);
				} else {
					user.setCanRunLoader(false);
				}
				if (getApplicationFeatures().isNewSecurity()) {
					user.setAuthenticator(rs.getInt("Authenticator"));
					user.setAuthenticationMethod(getNullableValue(rs, rs.getInt("AuthenticationMethod")));
					user.setDisabled(getNullableValue(rs, rs.getBoolean("Disabled")));
					user.setLastLoginDays(rs.getTimestamp("LastLoginDays"));
					user.setChangePassword(getNullableValue(rs, rs.getBoolean("ChangePassword")));
					user.setPasswordResetDays(getNullableValue(rs, rs.getDouble("PasswordResetDays")));
					user.setExpirationDate(getNullableValue(rs, rs.getTimestamp("ExpirationDate")));
					user.setExpirationDays(getNullableValue(rs, rs.getDouble("ExpirationDays")));
					// user.setActivationDate(getNullableValue(rs, rs.getTimestamp("ACTIVATIONDATE")));
					// user.setDormancyStatus(User.DormancyStatus.statusByValue(getNullableValue(rs, rs.getInt("DORMANCYSTATUS"))));
				}

				profile.setGroupId(rs.getLong("GroupID"));
				profile.setName(rs.getString("GroupName"));
				profile.setConnectionTimeout(rs.getLong("Groupconnection_timeout"));
				if (isAnalyticsSupported) {

					profile.setBiDirectory(rs.getString("biDirectory"));
				}
				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				approvalStatus.setDescription(rs.getString("statusDescription"));

				return user;
			}
		});
		return (query != null && !query.isEmpty()) ? query.get(0) : null;

	}

	@Override
	public void updateUser(User user) {

		StringBuilder updateStatement = new StringBuilder();

		updateStatement.append("UPDATE sUser SET");
		updateStatement.append(" UserName = ?");
		updateStatement.append(", GroupID = ? ");
		updateStatement.append(", vwListDepth = ? ");
		updateStatement.append(", wdNbDayHistory = ? ");
		updateStatement.append(", rpDirectory = ? ");
		updateStatement.append(", Email = ? ");
		updateStatement.append(", FullUserName = ? ");
		updateStatement.append(", ApprovalStatus = ? ");

		if (getApplicationFeatures().isNewSecurity()) {

			/*
			 * added by mohammad alzarai 2016-03-17 change user type user story
			 */
			updateStatement.append(", AUTHENTICATIONMETHOD = ?");

			updateStatement.append(", Disabled = ? ");
			updateStatement.append(", ChangePassword = ? ");
			updateStatement.append(", ExpirationDate = ? ");
			updateStatement.append(", Authenticator = ? ");
			updateStatement.append(", numberPasswordAttempts = ? ");
			updateStatement.append(", canRunLoader = ? ");

		}

		updateStatement.append(" WHERE ");

		updateStatement.append(" UserID = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(user.getUserName());
		args.add(user.getProfile().getGroupId());
		args.add(user.getVwListDepth());
		args.add(user.getWdNbDayHistory());
		args.add(defaultString(user.getRpDirectory()));
		args.add(defaultString(user.getEmail()));
		args.add(defaultString(user.getFullUserName()));
		args.add(user.getApprovalStatus().getId());

		/*
		 * added by mohammad alzarai 2016-03-17 change user type user story
		 */

		if (getApplicationFeatures().isNewSecurity()) {
			args.add(user.getAuthenticationMethod());
			args.add(user.isDisabled());
			args.add(user.isChangePassword());
			args.add(user.getExpirationDate());
			args.add(user.getAuthenticator());
			args.add(user.getNumberPasswordAttempts());
			args.add(user.isCanRunLoader());
		}

		args.add(user.getUserId());
		jdbcTemplate.update(updateStatement.toString(), args.toArray());

	}

	@Override
	public void deleteReportingUser(User user) {

		// to delete database user you need to use AdminOralceDAOImp or
		// AdminSqlDAOImp.
		// remove from Reporting database
		StringBuffer deleteReportingQuery = new StringBuffer();
		deleteReportingQuery.append("DELETE FROM SUSER ");
		deleteReportingQuery.append(" WHERE USERID = ");
		deleteReportingQuery.append(user.getUserId());
		deleteReportingQuery.append(" ");

		jdbcTemplate.execute(deleteReportingQuery.toString());

	}

	@Override
	public void deleteApplicationSettingsForUser(User user) {

		StringBuffer deleteReportingQuery = new StringBuffer();
		deleteReportingQuery.append("DELETE FROM wcApplicationSettings ");
		deleteReportingQuery.append(" WHERE USERID = ");
		deleteReportingQuery.append(user.getUserId());
		deleteReportingQuery.append(" ");

		jdbcTemplate.execute(deleteReportingQuery.toString());
	}

	@Override
	public void deleteReportingUser(Profile profile) {

		// to delete database user you need to use AdminOralceDAOImp or
		// AdminSqlDAOImp.
		// remove from Reporting database
		StringBuffer deleteReportingQuery = new StringBuffer();
		deleteReportingQuery.append("DELETE FROM SUSER ");
		deleteReportingQuery.append(" WHERE GroupID = ");
		deleteReportingQuery.append(profile.getGroupId());
		deleteReportingQuery.append(" ");

		jdbcTemplate.execute(deleteReportingQuery.toString());

	}

	@Override
	public void deleteProfileAccessRights(Long profileId, Integer moduleId) {
		String selectQuery = String.format(" DELETE FROM sAdmit WHERE ProgramID = %d and GroupID = %d", moduleId, profileId);
		jdbcTemplate.execute(selectQuery);

	}

	@Override
	public void deleteProfileUnits(Long profileId) {

		jdbcTemplate.execute(" DELETE FROM sUnitUserGroup WHERE GroupId= " + profileId);

	}

	@Override
	public void deleteProfileBICs(Long profileId) {
		String deleteQuery = String.format("DELETE FROM sBICUserGroup WHERE GroupId= %d", profileId);
		jdbcTemplate.execute(deleteQuery);

	}

	@Override
	public void deleteProfileGroupSets(Long profileId) {
		try {
			String deleteQuery = String.format("DELETE FROM group_sets  WHERE group_id= %d", profileId);
			jdbcTemplate.execute(deleteQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteProfileMsgCats(Long profileId) {
		String deleteQuery = String.format("DELETE FROM sMsgUserGroup WHERE GroupId= %d", profileId);
		jdbcTemplate.execute(deleteQuery);

	}

	private void assignUnitToProfile(Profile profile, String unit) {
		String selectQuery = String.format("INSERT INTO sUnitUserGroup (Unit, GroupID) VALUES ('%s', %d)", unit, profile.getGroupId());
		jdbcTemplate.execute(selectQuery);

	}

	private void assignBICToProfile(Profile profile, String bic) {

		String insertQuery = String.format("INSERT INTO sBICUserGroup (BICCode, GroupID) VALUES ('%s', %d)", bic, profile.getGroupId());

		jdbcTemplate.execute(insertQuery);
	}

	private void assignMsgCatToProfile(Profile profile, String msgCat) {

		String insertQuery = String.format("INSERT INTO sMsgUserGroup (Category, GroupID) VALUES ('%s', %d )", msgCat, profile.getGroupId());

		jdbcTemplate.execute(insertQuery);

	}

	@Override
	public void assignAccessRightsToProfile(Profile profile, Integer programID, Long ObjectID) {

		String insertQuery = String.format("INSERT INTO sAdmit (ProgramID, GroupID, ObjectID) VALUES (%d, %d, %d)", programID, profile.getGroupId(), ObjectID);

		jdbcTemplate.execute(insertQuery);
	}

	@Override
	public void updateApprovalStatus(User user) {

		StringBuilder updateQuery = new StringBuilder();

		updateQuery.append("UPDATE sUser set");
		updateQuery.append(" ApprovalStatus = ? ");
		updateQuery.append(" where ");
		updateQuery.append(" UserID = ? ");

		jdbcTemplate.update(updateQuery.toString(), new Object[] { user.getApprovalStatus().getId(), user.getUserId() });
	}

	@Override
	public void updateApprovalStatus(Profile profile) {

		StringBuilder updateQuery = new StringBuilder();

		updateQuery.append("UPDATE SUSERGROUP set");
		updateQuery.append(" ApprovalStatus = ? ");
		updateQuery.append(" where ");
		updateQuery.append(" GroupID = ? ");

		jdbcTemplate.update(updateQuery.toString(), new Object[] { profile.getApprovalStatus().getId(), profile.getGroupId() });
	}

	@Override
	public Profile getProfile(String profileName) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();
		String selectQuery = "";
		if (isAnalyticsSupported) {
			selectQuery = String.format(
					"SELECT GroupID, Name, Description, wdNbDayHistory, vwListDepth, rpDirectory, biDirectory , SenderEMail, connection_timeout,sUserGroup.ApprovalStatus AS ApprovalStatus, sApprovalStatuses.StatusDescription  FROM sUserGroup INNER JOIN sApprovalStatuses ON sUserGroup.APPROVALSTATUS = sApprovalStatuses.Status WHERE upper(Name) = '%s'",
					profileName.toUpperCase());
		} else {
			selectQuery = String.format(
					"SELECT GroupID, Name, Description, wdNbDayHistory, vwListDepth, rpDirectory , SenderEMail, connection_timeout,sUserGroup.ApprovalStatus AS ApprovalStatus, sApprovalStatuses.StatusDescription  FROM sUserGroup INNER JOIN sApprovalStatuses ON sUserGroup.APPROVALSTATUS = sApprovalStatuses.Status WHERE upper(Name) = '%s'",
					profileName.toUpperCase());
		}

		List<Profile> profile = jdbcTemplate.query(selectQuery, new RowMapper<Profile>() {
			public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {

				Profile profile = new Profile();

				profile.setGroupId(rs.getLong("GroupID"));
				profile.setName(rs.getString("Name"));
				profile.setDescription(rs.getString("Description"));
				profile.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
				profile.setVwListDepth(rs.getLong("vwListDepth"));
				profile.setRpDirectory(rs.getString("rpDirectory"));
				if (isAnalyticsSupported) {
					profile.setBiDirectory(rs.getString("biDirectory"));
				}
				profile.setSenderEmail(rs.getString("SenderEMail"));
				profile.setConnectionTimeout(rs.getLong("connection_timeout"));
				ApprovalStatus approvalStatus = new ApprovalStatus();
				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				approvalStatus.setDescription(rs.getString("StatusDescription"));

				profile.setApprovalStatus(approvalStatus);

				return profile;
			}
		});

		if (profile != null && profile.size() > 0)
			return profile.get(0);
		else
			return null;
	}

	@Override
	public List<Profile> getProfiles(String profileName) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();
		List<Profile> actualProfiles = new ArrayList<Profile>();
		String selectQuery = "";
		profileName = profileName != null && !profileName.isEmpty() ? profileName.toUpperCase() : "%";
		if (isAnalyticsSupported) {
			selectQuery = "SELECT GroupID, Name, Description, wdNbDayHistory, vwListDepth, rpDirectory,biDirectory, SenderEMail, " + "connection_timeout,sUserGroup.ApprovalStatus AS ApprovalStatus , sApprovalStatuses.StatusDescription "
					+ "FROM sUserGroup Left JOIN sApprovalStatuses ON sUserGroup.APPROVALSTATUS = sApprovalStatuses.Status ";

		} else {
			selectQuery = "SELECT GroupID, Name, Description, wdNbDayHistory, vwListDepth, rpDirectory, SenderEMail, " + "connection_timeout,sUserGroup.ApprovalStatus AS ApprovalStatus , sApprovalStatuses.StatusDescription "
					+ "FROM sUserGroup Left JOIN sApprovalStatuses ON sUserGroup.APPROVALSTATUS = sApprovalStatuses.Status ";
		}

		// if(profileName != null && !profileName.isEmpty())
		selectQuery += "WHERE upper(Name) like '" + profileName + "'";
		selectQuery += " ORDER BY UPPER(Name)";

		// selectQuery = String.format(selectQuery, profileName.toUpperCase());

		List<Profile> profiles = (List<Profile>) jdbcTemplate.query(selectQuery, new RowMapper<Profile>() {
			public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {

				Profile profile = new Profile();

				profile.setGroupId(rs.getLong("GroupID"));
				profile.setName(rs.getString("Name"));
				profile.setDescription(rs.getString("Description"));
				profile.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
				profile.setVwListDepth(rs.getLong("vwListDepth"));
				profile.setRpDirectory(rs.getString("rpDirectory"));
				profile.setSenderEmail(rs.getString("SenderEMail"));
				profile.setConnectionTimeout(rs.getLong("connection_timeout"));
				if (isAnalyticsSupported) {
					profile.setBiDirectory(rs.getString("biDirectory"));
				}

				ApprovalStatus approvalStatus = new ApprovalStatus();
				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				approvalStatus.setDescription(rs.getString("StatusDescription"));

				profile.setApprovalStatus(approvalStatus);

				return profile;
			}
		});

		// Remove Dummay Profile
		for (Profile profile : profiles) {
			if (profile.getName().equals("LSA&RSADummayProfile")) {

				// doNothing
			} else if (profile.getName().equals("LSARSADummayProfile")) {
				// doNothing
			} else {
				actualProfiles.add(profile);
			}

		}

		return actualProfiles;
	}

	@Override
	public void deleteProfile(String loggedInUser, Profile profile) {

		String deleteQuery = String.format("DELETE FROM sUserGroup WHERE GroupID= %d", profile.getGroupId());
		jdbcTemplate.execute(deleteQuery);
	}

	@Override
	public List<MsgCatPerProfile> getAllMessageCategories() {
		String selectQuery = "SELECT Category,Description FROM sMsgCategory ORDER BY Category";
		List<MsgCatPerProfile> list = jdbcTemplate.query(selectQuery, new RowMapper<MsgCatPerProfile>() {
			public MsgCatPerProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgCatPerProfile msgCatPerProfile = new MsgCatPerProfile();
				msgCatPerProfile.setMsgCat(rs.getString("Category"));
				msgCatPerProfile.setDesc(rs.getString("Description"));
				return msgCatPerProfile;
			}
		});

		return new ArrayList<MsgCatPerProfile>(list);
	}

	@Override
	public void assignBICsToProfile(Profile profile, List<String> bics) {

		for (String bic : bics) {
			this.assignBICToProfile(profile, bic);
		}

	}

	@Override
	public void assignMsgCatsToProfile(Profile profile, List<String> msgCategories) {

		for (String msgCat : msgCategories) {
			this.assignMsgCatToProfile(profile, msgCat);
		}
	}

	@Override
	public void assignUnitsToProfile(Profile profile, List<String> units) {
		for (String unit : units) {
			this.assignUnitToProfile(profile, unit);
		}
	}

	@Override
	public List<String> getUsedUnits() {
		String queryString = "SELECT LTRIM(RTRIM(Unit)) AS Unit FROM sUnit ORDER BY Unit";
		List<String> units = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String unit = rs.getString("Unit");
				return unit;
			}
		});

		return units;
	}

	@Override
	public void deleteUsedUnits() {
		jdbcTemplate.execute("DELETE FROM sUnit");
	}

	@Override
	public List<String> getAssignedUnits() {
		String queryString = "SELECT DISTINCT LTRIM(RTRIM(Unit)) as Unit FROM  sUnitUserGroup ORDER BY Unit";
		List<String> units = jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String unit = rs.getString("Unit");
				return unit;
			}
		});

		return units;

	}

	@Override
	public boolean isProfileHaveUsers(Profile profile) {

		String queryString = "SELECT count(*) as count FROM SUSER WHERE GroupID = " + profile.getGroupId();
		List<Long> usersCount = jdbcTemplate.query(queryString, new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("count");
			}
		});

		if (usersCount != null && !usersCount.isEmpty() && usersCount.get(0) > 0)
			return true;

		return false;
	}

	@Override
	public void deleteSearchTemplatesForUser(User user) {

		/**
		 * Added by Mohammad Alkhateeb by referring to bug No : 30436; so we well delete the parent table firstly then well delete rmsgsearchtemplates detail table we prefer to set the tow delete
		 * statements in one method because the relationship between the tow tables is One to One
		 */
		StringBuffer deleteMasterReportingQuery = new StringBuffer();
		// preparing the query
		deleteMasterReportingQuery.append("DELETE FROM RMSGSEARCHPROFILETEMPLATE WHERE ID IN (SELECT ID FROM rmsgsearchtemplates WHERE CREATED_BY = ");
		deleteMasterReportingQuery.append(user.getUserId());
		deleteMasterReportingQuery.append(" ) ");
		// deleting
		jdbcTemplate.execute(deleteMasterReportingQuery.toString());

		StringBuffer deleteReportingQuery = new StringBuffer();
		deleteReportingQuery.append("DELETE FROM rmsgsearchtemplates ");
		deleteReportingQuery.append(" WHERE CREATED_BY = ");
		deleteReportingQuery.append(user.getUserId());
		deleteReportingQuery.append(" ");

		jdbcTemplate.execute(deleteReportingQuery.toString());
	}

	@Override
	public List<CsmConnection> getCsmConnection() {

		List<CsmConnection> csmConnectionList = null;
		String selectQuery = "SELECT srvrport, srvrname, clntname, hbperiodsec, thresholdpercent, alarmidlecycles " + " FROM csm_connection ";

		csmConnectionList = jdbcTemplate.query(selectQuery, new RowMapper<CsmConnection>() {
			@Override
			public CsmConnection mapRow(ResultSet rs, int arg1) throws SQLException {

				CsmConnection csmConn = new CsmConnection();

				csmConn.setSeverPort(rs.getInt("srvrport"));
				csmConn.setServerName(rs.getString("srvrname"));
				csmConn.setClientName(rs.getString("clntname"));
				csmConn.setHeartBeatPeriodSecond(rs.getInt("hbperiodsec"));
				csmConn.setThresholdPercent(rs.getInt("thresholdpercent"));
				csmConn.setAlarmIdleCycles(rs.getInt("alarmidlecycles"));

				return csmConn;
			}
		});

		return csmConnectionList;
	}

	@Override
	public boolean checkCsmConnection(CsmConnection currentCsmConnection) {

		List<CsmConnection> csmConnectionList = null;
		String selectQuery = "SELECT srvrport, srvrname, clntname, hbperiodsec, thresholdpercent, alarmidlecycles " + " FROM csm_connection WHERE srvrname = '" + currentCsmConnection.getServerName() + "' and srvrport ="
				+ currentCsmConnection.getSeverPort();

		csmConnectionList = jdbcTemplate.query(selectQuery, new RowMapper<CsmConnection>() {
			@Override
			public CsmConnection mapRow(ResultSet rs, int arg1) throws SQLException {

				CsmConnection csmConn = new CsmConnection();

				csmConn.setSeverPort(rs.getInt("srvrport"));
				csmConn.setServerName(rs.getString("srvrname"));
				csmConn.setClientName(rs.getString("clntname"));
				csmConn.setHeartBeatPeriodSecond(rs.getInt("hbperiodsec"));
				csmConn.setThresholdPercent(rs.getInt("thresholdpercent"));
				csmConn.setAlarmIdleCycles(rs.getInt("alarmidlecycles"));

				return csmConn;
			}
		});

		if (csmConnectionList != null && csmConnectionList.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public void addCsmConnection(CsmConnection csmConnection) {

		String insertQuery = "INSERT INTO csm_connection ( srvrport, srvrname, clntname, hbperiodsec, thresholdpercent, alarmidlecycles, lastsrvr, lastclnt) " + "VALUES ( ?,?,?,?,?,?,"
				+ (getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? " SYSDATE , SYSDATE " : " GETDATE(), GETDATE()") + " )";

		jdbcTemplate.update(insertQuery,
				new Object[] { csmConnection.getSeverPort(), csmConnection.getServerName(), csmConnection.getClientName(), csmConnection.getHeartBeatPeriodSecond(), csmConnection.getThresholdPercent(), csmConnection.getAlarmIdleCycles() });
	}

	@Override
	public void updateCsmConnection(String serverName, Integer serverPort, CsmConnection csmConnection) {
		StringBuilder updateQuery = new StringBuilder();
		updateQuery.append("UPDATE csm_connection SET ");
		updateQuery.append(" srvrport = ?");
		updateQuery.append(", srvrname = ?");
		updateQuery.append(", clntname = ?");
		updateQuery.append(", hbperiodsec = ?");
		updateQuery.append(", thresholdpercent = ?");
		updateQuery.append(", alarmidlecycles = ?");
		updateQuery.append(" WHERE");
		updateQuery.append(" srvrname = ? ");
		updateQuery.append(" and srvrport = ? ");

		jdbcTemplate.update(updateQuery.toString(), new Object[] { csmConnection.getSeverPort(), csmConnection.getServerName(), csmConnection.getClientName(), csmConnection.getHeartBeatPeriodSecond(), csmConnection.getThresholdPercent(),
				csmConnection.getAlarmIdleCycles(), serverName, serverPort });

	}

	@Override
	public void deleteCsmConnection(CsmConnection csmConnection) {
		jdbcTemplate.update("DELETE FROM csm_connection WHERE srvrname = ? and srvrport = ? and clntname = ?", new Object[] { csmConnection.getServerName(), csmConnection.getSeverPort(), csmConnection.getClientName() });
	}

	@Override
	public void addReportingUser(User user) {

		StringBuilder insertUsersQuery = new StringBuilder();

		insertUsersQuery.append("INSERT INTO sUser(");
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertUsersQuery.append(" UserID,");
		}

		insertUsersQuery.append("UserName");
		insertUsersQuery.append(",GroupID");
		insertUsersQuery.append(",vwListDepth");
		insertUsersQuery.append(",wdNbDayHistory");
		insertUsersQuery.append(",rpDirectory");
		insertUsersQuery.append(",Email");
		insertUsersQuery.append(",FullUserName");
		insertUsersQuery.append(",ApprovalStatus");

		if (getApplicationFeatures().isNewSecurity()) {
			insertUsersQuery.append(",AuthenticationMethod");
			insertUsersQuery.append(",ChangePassword");
			insertUsersQuery.append(",PasswordResetDate");
			if (user.getExpirationDate() != null) {
				insertUsersQuery.append(",ExpirationDate");
			}

			insertUsersQuery.append(",Authenticator");
		}

		if (user.isCanRunLoader()) {
			insertUsersQuery.append(",canRunLoader");
		}

		insertUsersQuery.append(")");
		insertUsersQuery.append("VALUES (");
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertUsersQuery.append("sUser_ID.nextval, ");
		}
		insertUsersQuery.append("?, ?, ?, ?, ?, ?, ?, ?");

		if (getApplicationFeatures().isNewSecurity()) {
			if (user.getExpirationDate() != null) {

				insertUsersQuery.append(", ?, ?, ?, ? ");
			} else {

				insertUsersQuery.append(", ?, ?, ? ");
			}

		}

		if (getApplicationFeatures().isNewSecurity()) {
			insertUsersQuery.append(", ? ");
		}

		if (user.isCanRunLoader()) {
			insertUsersQuery.append(", ? ");
		}
		insertUsersQuery.append(")");

		List<Object> args = new ArrayList<Object>();
		args.add(user.getUserName());
		args.add(user.getProfile().getGroupId());
		args.add(user.getVwListDepth());
		args.add(user.getWdNbDayHistory());
		args.add(defaultString(user.getRpDirectory()));
		args.add(defaultString(user.getEmail()));
		args.add(defaultString(user.getFullUserName()));
		args.add(user.getApprovalStatus().getId());

		if (getApplicationFeatures().isNewSecurity()) {
			args.add(user.getAuthenticationMethod());
			args.add(user.isChangePassword());

			/*
			 * Make user password expired after two days for auto generated password
			 */
			Date date = new Date();
			if (user.isAutoGeneratedPassword()) {
				date = Utils.afterCurrentDayIgnoreTime(2);
			}

			if (user.getAuthenticationMethod() == 1 || user.getAuthenticationMethod() == 3) {
				args.add(null);
			} else {
				args.add(date);
			}

			if (user.getExpirationDate() != null) {
				args.add(user.getExpirationDate());
			}

			args.add(user.getAuthenticator());
		}
		if (user.isCanRunLoader()) {
			args.add(user.isCanRunLoader() ? 1 : 0);

		}

		jdbcTemplate.update(insertUsersQuery.toString(), args.toArray());
	}

	@Override
	public String getUsersQuery() {
		return getUsersQuery(null, null);
	}

	@Override
	public List<User> filterDormantUsers(int dormancyPeriod, String userName, String profileName) {
		String query = this.getDormantUsersQuery(dormancyPeriod, userName, profileName);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, dormancyPeriod * -1);

		List<User> usersList = (List<User>) jdbcTemplate.query(query.toString(), new Object[] { calendar.getTime() }, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				User user = new User();
				ApprovalStatus approvalStatus = new ApprovalStatus();
				Profile profile = new Profile();

				user.setApprovalStatus(approvalStatus);
				user.setProfile(profile);

				user.setUserName(rs.getString("UserName"));
				user.setFullUserName(rs.getString("FullUserName"));
				user.setUserId(rs.getLong("UserID"));
				user.setVwListDepth(rs.getLong("vwListDepth"));
				user.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
				user.setRpDirectory(rs.getString("rpDirectory"));
				user.setEmail(rs.getString("Email"));
				user.setConnectionTimeOut(rs.getLong("connection_timeout"));

				if (getApplicationFeatures().isNewSecurity()) {
					user.setAuthenticator(rs.getInt("Authenticator"));
					user.setAuthenticationMethod(getNullableValue(rs, rs.getInt("AuthenticationMethod")));
					user.setDisabled(getNullableValue(rs, rs.getBoolean("Disabled")));
					user.setLastLoginDays(rs.getTimestamp("LastLoginDays"));
					user.setChangePassword(getNullableValue(rs, rs.getBoolean("ChangePassword")));
					user.setPasswordResetDays(getNullableValue(rs, rs.getDouble("PasswordResetDays")));
					user.setExpirationDate(getNullableValue(rs, rs.getTimestamp("ExpirationDate")));
					user.setExpirationDays(getNullableValue(rs, rs.getDouble("ExpirationDays")));
				}

				profile.setGroupId(rs.getLong("GroupID"));
				profile.setName(rs.getString("GroupName"));
				profile.setConnectionTimeout(rs.getLong("Groupconnection_timeout"));

				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				approvalStatus.setDescription(rs.getString("statusDescription"));

				if (!getApplicationFeatures().isNewSecurity() && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
					String databaseUserID = rs.getString("database_user_id");
					if (databaseUserID == null || databaseUserID.trim().equals("")) {
						user.setDatabaseUserOld(false);
					} else {
						user.setDatabaseUserOld(true);
					}
				}

				return user;
			}
		});

		return usersList;

	}

	private String getDormantUsersQuery(int dormancyPeriod, String userName, String profileName) {
		StringBuilder selectUsersQuery = new StringBuilder();

		profileName = profileName.isEmpty() ? "%" : profileName.toUpperCase();

		selectUsersQuery.append("SELECT sUser.UserName AS UserName,");
		selectUsersQuery.append(" sUser.FullUserName AS FullUserName,");
		selectUsersQuery.append(" sUser.GroupID AS GroupID,");
		selectUsersQuery.append(" sUser.UserID AS UserID,");
		selectUsersQuery.append(" sUser.vwListDepth AS vwListDepth,");
		selectUsersQuery.append(" sUser.wdNbDayHistory AS wdNbDayHistory,");
		selectUsersQuery.append(" sUser.rpDirectory AS rpDirectory,");
		selectUsersQuery.append(" sUser.Email AS Email,");
		selectUsersQuery.append(" sUser.canRunLoader AS canRunLoader,");
		selectUsersQuery.append(" sUser.connection_timeout AS connection_timeout,");
		boolean isNewSecurity = getApplicationFeatures().isNewSecurity();
		if (isNewSecurity) {
			selectUsersQuery.append(" sUser.AuthenticationMethod AS AuthenticationMethod,");
			selectUsersQuery.append(" sUser.AUTHENTICATOR as Authenticator ,");
			selectUsersQuery.append(" sUser.Disabled AS Disabled,");
			// selectUsersQuery.append(" " + getDbPortabilityHandler().getDateDifferenceInDays( getDbPortabilityHandler().getSysDate() , "sUser.LastLogin") +" AS LastLoginDays,");
			selectUsersQuery.append("sUser.LastLogin AS LastLoginDays,");
			selectUsersQuery.append(" sUser.ChangePassword AS ChangePassword,");
			selectUsersQuery.append(" " + getDbPortabilityHandler().getDateDifferenceInDays(getDbPortabilityHandler().getSysDate(), "sUser.PasswordResetDate") + " AS PasswordResetDays,");
			selectUsersQuery.append(" ExpirationDate,");
			selectUsersQuery.append(" " + getDbPortabilityHandler().getDateDifferenceInDays("sUser.ExpirationDate", getDbPortabilityHandler().getSysDate()) + " AS ExpirationDays,");
		}

		selectUsersQuery.append(" sUserGroup.Name AS GroupName,");
		selectUsersQuery.append(" sUserGroup.connection_timeout AS Groupconnection_timeout,");
		selectUsersQuery.append(" sUser.ApprovalStatus AS ApprovalStatus,");
		selectUsersQuery.append(" sApprovalStatuses.statusDescription AS statusDescription");

		if (!isNewSecurity && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			selectUsersQuery.append(", d.USER_ID AS database_user_id");
		}

		selectUsersQuery.append(" FROM sUser ");
		selectUsersQuery.append(" INNER JOIN sUserGroup ON sUser.GroupID = sUserGroup.GroupID");
		selectUsersQuery.append(" INNER JOIN sApprovalStatuses ON sApprovalStatuses.Status = sUser.ApprovalStatus");

		if (!isNewSecurity && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			selectUsersQuery.append(" LEFT OUTER JOIN all_users d ON UPPER (sUser.UserName) = UPPER (d.USERNAME)");
		}

		selectUsersQuery.append(" WHERE sUser.UserName NOT IN ('LSA_USER' , 'RSA_USER' ) and suser.username not like 'AUDSID_%' ");
		if (userName != null && !userName.isEmpty()) {
			selectUsersQuery.append(" AND ( sUser.UserName like '" + userName + "' )");
		}
		selectUsersQuery.append(" and upper(sUserGroup.name) like '" + profileName + "'");
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			selectUsersQuery.append(" AND ( NVL(LASTLOGIN,CREATIONDATE) < ? or CREATIONDATE IS NULL )");
		} else {
			selectUsersQuery.append(" AND ( ISNULL(LASTLOGIN,CREATIONDATE) < ? or CREATIONDATE IS NULL )");
		}

		selectUsersQuery.append(" ORDER BY UserName");

		return selectUsersQuery.toString();
	}

	@Override
	public String getUsersQuery(String userName, String profileName) {
		StringBuilder selectUsersQuery = new StringBuilder();
		userName = userName != null && !userName.isEmpty() ? userName.toUpperCase() : "%";
		selectUsersQuery.append("SELECT sUser.UserName AS UserName,");
		selectUsersQuery.append(" sUser.FullUserName AS FullUserName,");
		selectUsersQuery.append(" sUser.GroupID AS GroupID,");
		selectUsersQuery.append(" sUser.UserID AS UserID,");
		selectUsersQuery.append(" sUser.vwListDepth AS vwListDepth,");
		selectUsersQuery.append(" sUser.wdNbDayHistory AS wdNbDayHistory,");
		selectUsersQuery.append(" sUser.rpDirectory AS rpDirectory,");
		selectUsersQuery.append(" sUser.Email AS Email,");
		selectUsersQuery.append(" sUser.canRunLoader AS canRunLoader,");
		selectUsersQuery.append(" sUser.connection_timeout AS connection_timeout,");

		boolean isNewSecurity = getApplicationFeatures().isNewSecurity();
		if (isNewSecurity) {
			selectUsersQuery.append(" sUser.AuthenticationMethod AS AuthenticationMethod,");
			selectUsersQuery.append(" sUser.AUTHENTICATOR as Authenticator ,");
			selectUsersQuery.append(" sUser.Disabled AS Disabled,");
			selectUsersQuery.append("sUser.LastLogin AS LastLoginDays,");
			selectUsersQuery.append(" sUser.ChangePassword AS ChangePassword,");
			selectUsersQuery.append(" " + getDbPortabilityHandler().getDateDifferenceInDays(getDbPortabilityHandler().getSysDate(), "sUser.PasswordResetDate") + " AS PasswordResetDays,");
			selectUsersQuery.append(" ExpirationDate,");
			selectUsersQuery.append(" " + getDbPortabilityHandler().getDateDifferenceInDays("sUser.ExpirationDate", getDbPortabilityHandler().getSysDate()) + " AS ExpirationDays,");
		}

		selectUsersQuery.append(" sUserGroup.Name AS GroupName,");
		selectUsersQuery.append(" sUserGroup.connection_timeout AS Groupconnection_timeout,");
		selectUsersQuery.append(" sUser.ApprovalStatus AS ApprovalStatus,");
		selectUsersQuery.append(" sApprovalStatuses.statusDescription AS statusDescription");

		if (!isNewSecurity && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			selectUsersQuery.append(", d.USER_ID AS database_user_id");
		}

		selectUsersQuery.append(" FROM sUser ");
		selectUsersQuery.append(" INNER JOIN sUserGroup ON sUser.GroupID = sUserGroup.GroupID");
		selectUsersQuery.append(" INNER JOIN sApprovalStatuses ON sApprovalStatuses.Status = sUser.ApprovalStatus");

		if (!isNewSecurity && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			selectUsersQuery.append(" LEFT OUTER JOIN all_users d ON UPPER (sUser.UserName) = UPPER (d.USERNAME)");
		}

		selectUsersQuery.append(" WHERE sUser.UserName NOT IN ('LSA' , 'RSA' ) and suser.username not like 'AUDSID_%' ");

		// if(userName != null && userName.trim().length() > 0)

		selectUsersQuery.append(" and upper(sUser.UserName) like '" + userName.toUpperCase() + "'");

		// if(profileName != null && profileName.trim().length() > 0)
		selectUsersQuery.append(" and upper(sUserGroup.name) like ?");

		selectUsersQuery.append(" ORDER BY UserName");

		return selectUsersQuery.toString();
	}

	@Override
	public List<User> executeUsersQuery(String query, String userName, String profileName) {

		profileName = profileName != null && !profileName.isEmpty() ? profileName.toUpperCase() : "%";

		Object[] param = { profileName };
		List<User> usersList = (List<User>) jdbcTemplate.query(query.toString(), param, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				User user = new User();
				ApprovalStatus approvalStatus = new ApprovalStatus();
				Profile profile = new Profile();

				user.setApprovalStatus(approvalStatus);
				user.setProfile(profile);

				user.setUserName(rs.getString("UserName"));
				user.setFullUserName(rs.getString("FullUserName"));
				user.setUserId(rs.getLong("UserID"));
				user.setVwListDepth(rs.getLong("vwListDepth"));
				user.setWdNbDayHistory(rs.getLong("wdNbDayHistory"));
				user.setRpDirectory(rs.getString("rpDirectory"));
				user.setEmail(rs.getString("Email"));
				user.setConnectionTimeOut(rs.getLong("connection_timeout"));
				if (rs.getLong("canRunLoader") == 1) {

					user.setCanRunLoader(true);
				} else {

					user.setCanRunLoader(false);
				}
				if (getApplicationFeatures().isNewSecurity()) {
					user.setAuthenticator(rs.getInt("Authenticator"));
					user.setAuthenticationMethod(getNullableValue(rs, rs.getInt("AuthenticationMethod")));
					user.setDisabled(getNullableValue(rs, rs.getBoolean("Disabled")));
					user.setLastLoginDays(rs.getTimestamp("LastLoginDays"));
					user.setChangePassword(getNullableValue(rs, rs.getBoolean("ChangePassword")));
					user.setPasswordResetDays(getNullableValue(rs, rs.getDouble("PasswordResetDays")));
					user.setExpirationDate(getNullableValue(rs, rs.getTimestamp("ExpirationDate")));
					user.setExpirationDays(getNullableValue(rs, rs.getDouble("ExpirationDays")));
				}

				profile.setGroupId(rs.getLong("GroupID"));
				profile.setName(rs.getString("GroupName"));
				profile.setConnectionTimeout(rs.getLong("Groupconnection_timeout"));

				approvalStatus.setId(rs.getLong("ApprovalStatus"));
				approvalStatus.setDescription(rs.getString("statusDescription"));

				if (!getApplicationFeatures().isNewSecurity() && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
					String databaseUserID = rs.getString("database_user_id");
					if (databaseUserID == null || databaseUserID.trim().equals("")) {
						user.setDatabaseUserOld(false);
					} else {
						user.setDatabaseUserOld(true);
					}
				}

				return user;
			}
		});

		return usersList;
	}

	@Override
	public void addProfile(Profile profile) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();

		String insertQuery = "INSERT INTO sUserGroup ( ";
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertQuery += "GroupID,";
		}
		if (isAnalyticsSupported) {
			insertQuery += "Name, Description, vwListDepth, wdNbDayHistory, rpDirectory, SenderEMail, connection_timeout, ApprovalStatus,biDirectory ) VALUES ( ";

		} else {
			insertQuery += "Name, Description, vwListDepth, wdNbDayHistory, rpDirectory, SenderEMail, connection_timeout, ApprovalStatus ) VALUES ( ";

		}

		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertQuery += "side.sUserGroup_ID.nextval,";
		}
		if (isAnalyticsSupported) {
			insertQuery += "?, ?, ?, ?, ?, ?, ?, ?,?)";
		} else {
			insertQuery += "?, ?, ?, ?, ?, ?, ?, ?)";
		}

		if (isAnalyticsSupported) {
			jdbcTemplate.update(insertQuery,
					new Object[] { profile.getName(), defaultString(profile.getDescription()), defaultLong(profile.getVwListDepth(), 100L), defaultLong(profile.getWdNbDayHistory(), 7L), defaultString(profile.getRpDirectory()),
							defaultString(profile.getSenderEmail()), defaultLong(profile.getConnectionTimeout()), (profile.getApprovalStatus() == null) ? 3L : profile.getApprovalStatus().getId(), defaultString(profile.getBiDirectory()) });
		} else {
			jdbcTemplate.update(insertQuery, new Object[] { profile.getName(), defaultString(profile.getDescription()), defaultLong(profile.getVwListDepth(), 100L), defaultLong(profile.getWdNbDayHistory(), 7L),
					defaultString(profile.getRpDirectory()), defaultString(profile.getSenderEmail()), defaultLong(profile.getConnectionTimeout()), (profile.getApprovalStatus() == null) ? 3L : profile.getApprovalStatus().getId() });
		}

	}

	@Override
	public void updateProfile(Profile profile) {
		Boolean isAnalyticsSupported = applicationFeatures.isAnalyticsSupported();

		StringBuilder updateQuery = new StringBuilder();

		updateQuery.append("UPDATE sUserGroup SET ");
		updateQuery.append(" Name = ?");
		updateQuery.append(", Description = ?");
		updateQuery.append(", vwListDepth = ?");
		updateQuery.append(", wdNbDayHistory = ?");
		updateQuery.append(", rpDirectory = ?");
		updateQuery.append(", SenderEMail = ?");
		updateQuery.append(", connection_timeout = ?");
		updateQuery.append(", ApprovalStatus =  ?");
		if (isAnalyticsSupported) {
			updateQuery.append(", biDirectory =  ?");
		}
		updateQuery.append(" WHERE");
		updateQuery.append("  GroupID = ?");

		if (isAnalyticsSupported) {
			jdbcTemplate.update(updateQuery.toString(), new Object[] { profile.getName(), defaultString(profile.getDescription()), profile.getVwListDepth(), profile.getWdNbDayHistory(), defaultString(profile.getRpDirectory()),
					defaultString(profile.getSenderEmail()), profile.getConnectionTimeout().intValue(), profile.getApprovalStatus().getId(), profile.getBiDirectory(), profile.getGroupId() });
		} else {
			jdbcTemplate.update(updateQuery.toString(), new Object[] { profile.getName(), defaultString(profile.getDescription()), profile.getVwListDepth(), profile.getWdNbDayHistory(), defaultString(profile.getRpDirectory()),
					defaultString(profile.getSenderEmail()), profile.getConnectionTimeout().intValue(), profile.getApprovalStatus().getId(), profile.getGroupId() });
		}

	}

	@Override
	public void syncLoaderConenction(LoaderConnection loaderConnection) {
		String updateConnectionQuery = "UPDATE ldSettings SET LAST_UMIDL = ?, LAST_UMIDH = ? WHERE aid = ? ";
		jdbcTemplate.update(updateConnectionQuery.toString(), new Object[] { "", "", loaderConnection.getAid() });
	}

	@Override
	public void addLoaderConnection(LoaderConnection loaderConnection) {

		StringBuffer insertStatement = new StringBuffer();

		insertStatement.append("INSERT into ldSettings ( aid, alliance_info, port, time_out, srv_address, ");

		if (getApplicationFeatures().isFilePayloadSupported()) {
			insertStatement.append("files_local_path,");
		}

		insertStatement.append(" licenseinfo ) VALUES ");
		if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
			insertStatement.append(" ( ldSettings_aid.NEXTVAL , ");
		} else {
			insertStatement.append(" ( (select max(aid) + 1 from ldSettings) , ");
		}
		insertStatement.append("'" + getDBSafeString(loaderConnection.getAllianceInfo()) + "' , ");
		insertStatement.append(loaderConnection.getPort() + ", ");
		insertStatement.append(loaderConnection.getTimeOut() + ", ");
		insertStatement.append("'" + getDBSafeString(loaderConnection.getServerAddress()) + "' , ");

		if (getApplicationFeatures().isFilePayloadSupported()) {

			insertStatement.append("'" + getDBSafeString(loaderConnection.getFilesLocalPath()) + "' , ");
		}

		insertStatement.append(" '14,70,116,1604,100402181D180140' ) ");

		jdbcTemplate.execute(insertStatement.toString());

	}

	@Override
	public void createSettings(AdminSettings settimgs) {
		String query = "insert into ldAdminSettings( archive_restore_user, archive_restore_password ) values (?, ?)";
		jdbcTemplate.update(query, new Object[] { settimgs.getArchiveRestoreUser(), settimgs.getArchiveRestorePasswordEncrypted() });
	}

	@Override
	public void updateSettings(AdminSettings settimgs) throws Exception {
		if (settimgs == null) {
			throw new Exception("Invalid admin settings.");
		}

		String query = "Update ldAdminSettings set archive_restore_user= ?, archive_restore_password= ? ";
		jdbcTemplate.update(query, new Object[] { settimgs.getArchiveRestoreUser(), settimgs.getArchiveRestorePasswordEncrypted() });
	}

	@Override
	public void restSettings() {
		String query = "DELETE FROM LDADMINSETTINGS";
		jdbcTemplate.update(query);
	}

	@Override
	public AdminSettings getSettings() {
		String query = "Select archive_restore_user, archive_restore_password from ldAdminSettings";
		List<AdminSettings> settings = jdbcTemplate.query(query, new RowMapper<AdminSettings>() {

			@Override
			public AdminSettings mapRow(ResultSet rs, int arg1) throws SQLException {
				AdminSettings settings = new AdminSettings();
				settings.setArchiveRestoreUser(rs.getString("archive_restore_user"));
				settings.setArchiveRestorePasswordEncrypted(rs.getString("archive_restore_password"));

				return settings;
			}
		});
		if (settings == null || settings.isEmpty()) {
			return null;
		}
		return settings.get(0);
	}

	@Override
	public List<String> getUserRoles(User user) {
		return getUserRoles(user.getUserName());
	}

	@Override
	public boolean checkRole(String db_roleName) {
		return checkRole(db_roleName);
	}

	@Override
	public String getUserApprovalStatusDiscription(Long userStatus) {

		String query = " SELECT StatusDescription " + " FROM sApprovalStatuses " + " where Status =  " + userStatus;

		List<String> userStatusDiscriptions = jdbcTemplate.query(query, new RowMapper<String>() {

			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String userStatusDiscriptions;
				userStatusDiscriptions = rs.getString("StatusDescription");
				return userStatusDiscriptions;
			}
		});
		return userStatusDiscriptions.get(0);
	}

	@Override
	public void setUserGroupId(Long userId, Long newGroupId) {
		String query = "Update sUser set GROUPID= ? where USERID=?";
		jdbcTemplate.update(query, new Object[] { newGroupId, userId });
	}

	// @Override
	// public void changeDoramncyStatus(User user) {
	// String updateStatement = "Update SUSER SET DORMANCYSTATUS = ? WHERE USERID = ?";
	// jdbcTemplate.update(updateStatement, new Object[] {
	// user.getDormancyStatus().getValue(), user.getUserId() });
	// }

	public String getUserAuthenticationMethod(String userName) {
		String query = " select AUTHENTICATIONMETHOD from sUser where USERNAME = '" + userName + "'";

		List<String> authenticationMethods = jdbcTemplate.query(query, new RowMapper<String>() {

			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String authenticationMethod;
				authenticationMethod = rs.getString("AUTHENTICATIONMETHOD");
				return authenticationMethod;
			}
		});

		if (authenticationMethods != null && authenticationMethods.size() > 0) {
			return authenticationMethods.get(0);
		} else {
			return null;
		}

	}

	@Override
	public List<Integer> getGroupAccessProgramIds(Long groupId) {

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(groupId);

		String queryString = "select distinct programid from SADMIT where groupid= ? order by programid";

		List<Integer> groupPrograms = (List<Integer>) jdbcTemplate.query(queryString, parameters.toArray(), new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("programid");
			}
		});
		return groupPrograms;

	}

	@Override
	public void saveBaseCur(String loginUser, String baseCur) {

		if (baseCur.equalsIgnoreCase("Message Amount Currency")) {
			baseCur = "";
		}
		StringBuffer updateStatement = new StringBuffer();
		updateStatement.append("UPDATE ldGlobalSettings SET ");
		updateStatement.append("BASE_CUR = ?  ");
		jdbcTemplate.update(updateStatement.toString(), new Object[] { baseCur });

	}

	@Override
	public List<String> getAvailableBaseCurrencies() {

		String queryString = "select distinct Base_Currency from sExchangeRates";

		return (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("Base_Currency");
			}
		});
	}

	@Override
	public String getBaseCurrencyFromDB() {
		String queryString = "select BASE_CUR from ldGlobalSettings";

		List<String> baseCurrencies = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("BASE_CUR");
			}
		});

		if (baseCurrencies == null || baseCurrencies.isEmpty()) {
			return "";
		} else {
			return baseCurrencies.get(0);
		}
	}

	@Override
	public List<JIResourceFolder> getJIResourceFolder() {
		String getjIResourceFoldersQuery = "select * from JIResourceFolder  where URI like '%public%' and MARK_AS_DELETED IS NULL";
		List<JIResourceFolder> jIResourceFolders = jdbcTemplate.query(getjIResourceFoldersQuery, new RowMapper<JIResourceFolder>() {
			public JIResourceFolder mapRow(ResultSet rs, int rowNum) throws SQLException {
				JIResourceFolder jiResourceFolder = new JIResourceFolder();
				jiResourceFolder.setName(rs.getString("Name"));
				jiResourceFolder.setUri(rs.getString("uri"));
				return jiResourceFolder;
			}
		});
		return jIResourceFolders;
	}

	@Override
	public void updateBiDefaultPath(String biDefPath) {

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("UPDATE LDGLOBALSETTINGS SET BI_DEFAULT_PATH = ?");
				statement.setString(1, biDefPath);

				return statement;
			}
		});

	}

	@Override
	public String getDefaultBiPathFromDB() {
		String queryString = "select BI_DEFAULT_PATH from ldGlobalSettings";

		List<String> defaultPath = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("BI_DEFAULT_PATH");
			}
		});

		if (defaultPath == null || defaultPath.isEmpty()) {
			return "";
		} else {
			return defaultPath.get(0);
		}
	}

	@Override
	public void deleteJasperFolder(String folderUri) {
		jdbcTemplate.update("delete from JIResourceFolder where uri = ?", new Object[] { folderUri });
	}

	@Override
	public void markJasperFolderAsDeleted(String folderUri) {
		jdbcTemplate.update("update    JIResourceFolder set  MARK_AS_DELETED = '1' ,HIDDEN=1  where  uri = ?", new Object[] { folderUri });
	}

	@Override
	public Integer getWDEnablePDFTemplates() {

		String queryString = "SELECT WD_PDF_TEMPLATES from WDSETTINGS";

		List<Integer> flags = (List<Integer>) jdbcTemplate.query(queryString, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("WD_PDF_TEMPLATES");
			}
		});
		if (flags == null || flags.isEmpty()) {
			return 0;
		}

		return flags.get(0);
	}

	@Override
	public void updateWDSettings(boolean enablePDFTemplates) {

		StringBuffer updateStatement = new StringBuffer();

		updateStatement.append("UPDATE WDSETTINGS SET ");
		updateStatement.append("WD_PDF_TEMPLATES = ?");

		Integer flag = 0;
		if (enablePDFTemplates)
			flag = 1;
		jdbcTemplate.update(updateStatement.toString(), new Object[] { flag });

	}

	@Override
	public UserThemePreferences getUserThemePreferences(Long userId) {
		String userThemePreferencesQuery = "select * from USERTHEMEPREFERENCES  where USERID = ?";
		List<UserThemePreferences> userThemePreferences = jdbcTemplate.query(userThemePreferencesQuery, new Object[] { userId }, new RowMapper<UserThemePreferences>() {
			public UserThemePreferences mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserThemePreferences userThemePreferences = new UserThemePreferences();
				userThemePreferences.setComponentTheme(rs.getString("componentTheme"));
				userThemePreferences.setInputStyle(rs.getString("inputStyle"));
				userThemePreferences.setMenuMode(rs.getString("menuMode"));
				return userThemePreferences;
			}
		});
		if (userThemePreferences == null || userThemePreferences.isEmpty()) {
			return null;
		}

		return userThemePreferences.get(0);

	}

	@Override
	public void updateUserThemePreferences(UserThemePreferences userThemePreferences, boolean update) {

		if (update) {
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement("UPDATE USERTHEMEPREFERENCES SET menuMode = ? , inputStyle= ? , componentTheme= ? WHERE USERID= ? ");
					statement.setString(1, userThemePreferences.getMenuMode());
					statement.setString(2, userThemePreferences.getInputStyle());
					statement.setString(3, userThemePreferences.getComponentTheme());
					statement.setLong(4, userThemePreferences.getUSERID());
					return statement;
				}
			});

		} else {
			String query = "insert into USERTHEMEPREFERENCES( USERID,menuMode, inputStyle,componentTheme ) values (?,?, ?,?)";
			jdbcTemplate.update(query, new Object[] { userThemePreferences.getUSERID(), userThemePreferences.getMenuMode(), userThemePreferences.getInputStyle(), userThemePreferences.getComponentTheme() });
		}

	}

}
