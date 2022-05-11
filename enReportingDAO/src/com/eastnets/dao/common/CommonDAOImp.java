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

package com.eastnets.dao.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.procedure.GetActiveSyntaxVersionProcedure;
import com.eastnets.dao.common.procedure.GetUserSettingsProcedure;
import com.eastnets.dao.common.procedure.SCheckAdmitProcedure;
import com.eastnets.dao.common.procedure.SetProfileProcedure;
import com.eastnets.dao.common.procedure.VWGetCorrInfoProcedure;
import com.eastnets.domain.Alliance;
import com.eastnets.domain.AllianceComparator;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.Config;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.DatabaseInfo;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.viewer.Country;
import com.eastnets.domain.viewer.StatusCode;
import com.eastnets.domain.viewer.StatusReason;
import com.eastnets.domain.watchdog.ActiveSyntax;

/**
 * Common DAO Implementation
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public abstract class CommonDAOImp extends DAOBaseImp implements CommonDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8565167860150436689L;
	private SetProfileProcedure setProfileProcedure;
	private GetUserSettingsProcedure getUserSettingsProcedure;
	private SCheckAdmitProcedure sCheckAdmitProcedure;
	private VWGetCorrInfoProcedure vwGetCorrInfoProcedure;
	private GetActiveSyntaxVersionProcedure getActiveSyntaxVersionProcedure;
	private Config config;

	@Override
	public String activateProfile(User profile) {
		if (profile == null) {
			return "";
		}
		setProfileProcedure.execute(profile);
		return profile.getError();
	}

	@Override
	public String activateProfile(Connection connection, User user) {
		if (user == null) {
			return "";
		}

		CallableStatement cs = null;
		try {
			cs = connection.prepareCall("{call SetProfile(?,?,?,?,?,?,?)}");

			cs.setString(1, user.getProfile().getName());
			cs.setString(2, user.getUserName());
			cs.setString(3, user.getVwListDepth().toString());
			cs.setString(4, user.getWdNbDayHistory().toString());
			cs.setString(5, user.getRpDirectory());
			cs.setString(6, user.getEmail());
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.execute();
			user.setError(cs.getString(7));
		} catch (SQLException e) {
			System.err.println("Error calling SetProfile : " + e.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					System.err.println("Error closing statement for SetProfile : " + e.getMessage());
				}
			}
		}
		return user.getError();
	}

	public SetProfileProcedure getSetProfileProcedure() {
		return setProfileProcedure;
	}

	public void setSetProfileProcedure(SetProfileProcedure setProfileProcedure) {
		this.setProfileProcedure = setProfileProcedure;
	}

	@Override
	public String getUserSettings(String username, String paramName) {
		String result = getUserSettingsProcedure.execute(username, paramName);
		return result;
	}

	// used for oracle only
	@Override
	public void alterSession(String schema) {
		jdbcTemplate.execute("alter session set current_schema = " + schema);

		// in some locale( like Hungarian ), the order by statement orders the numbers after the letters, when checking license we order the BICs
		// and generate the sign based on it, so we fail to verify the license due to the change of this string.
		// The following line force the NLS_LANGUAGE for the current database session to AMERICAN which is what we have already tested and we are sure that it works fine.
		jdbcTemplate.execute("ALTER SESSION SET NLS_LANGUAGE= 'AMERICAN'");
	}

	public GetUserSettingsProcedure getGetUserSettingsProcedure() {
		return getUserSettingsProcedure;
	}

	public void setGetUserSettingsProcedure(GetUserSettingsProcedure getUserSettingsProcedure) {
		this.getUserSettingsProcedure = getUserSettingsProcedure;
	}

	public SCheckAdmitProcedure getsCheckAdmitProcedure() {
		return sCheckAdmitProcedure;
	}

	public void setsCheckAdmitProcedure(SCheckAdmitProcedure sCheckAdmitProcedure) {
		this.sCheckAdmitProcedure = sCheckAdmitProcedure;
	}

	@Override
	public boolean checkAdmit(int programId, int objectId) {
		Integer isAllowed = sCheckAdmitProcedure.execute(programId, objectId);
		return (isAllowed == 1);
	}

	@Override
	public CorrInfo getCorrInfo(CorrInfo corr) {
		vwGetCorrInfoProcedure.execute(corr);
		return corr;
	}

	public VWGetCorrInfoProcedure getVwGetCorrInfoProcedure() {
		return vwGetCorrInfoProcedure;
	}

	public void setVwGetCorrInfoProcedure(VWGetCorrInfoProcedure vwGetCorrInfoProcedure) {
		this.vwGetCorrInfoProcedure = vwGetCorrInfoProcedure;
	}

	public GetActiveSyntaxVersionProcedure getGetActiveSyntaxVersionProcedure() {
		return getActiveSyntaxVersionProcedure;
	}

	public void setGetActiveSyntaxVersionProcedure(GetActiveSyntaxVersionProcedure getActiveSyntaxVersionProcedure) {
		this.getActiveSyntaxVersionProcedure = getActiveSyntaxVersionProcedure;
	}

	@Override
	public HashSet<String> getCurrencies() {
		List<String> records = jdbcTemplate.query("select CURRENCYCODE from cu", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("CURRENCYCODE");
			}
		});

		return new HashSet<String>(records);
	}

	@Override
	public SortedMap<String, String> getAvailableAIDs() {
		List<Map<String, String>> availableAIDs = jdbcTemplate.query("select aid, alliance_instance from LDSETTINGS order by aid asc", new RowMapper<Map<String, String>>() {
			@Override
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				SortedMap<String, String> aid = new TreeMap<String, String>(new AllianceComparator());
				aid.put(rs.getString("aid"), (rs.getString("alliance_instance") == null || rs.getString("alliance_instance").isEmpty() || rs.getString("alliance_instance").equals("") ? "N/A" : rs.getString("alliance_instance")));
				return aid;
			}
		});

		SortedMap<String, String> aids = new TreeMap<String, String>(new AllianceComparator());
		for (Map<String, String> aidMap : availableAIDs) {
			aids.putAll(aidMap);
		}

		return aids;
	}

	public Long getMaxAID() {

		List<Long> maxID = jdbcTemplate.query("select max(aid) as max_id from LDSETTINGS", new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {

				return rs.getLong("max_id");

			}
		});

		if (maxID == null || maxID.isEmpty()) {
			return null;
		}
		return maxID.get(0);
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public List<ApplicationSetting> getApplicationSettings(Long archiveID, Long userId, Long allianceId) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(archiveID);
		parameters.add(userId);
		parameters.add(allianceId);

		String selectQuery = "SELECT ID, USERID, AID, FIELDNAME, FIELDVALUE FROM WCAPPLICATIONSETTINGS WHERE ID = ? AND USERID = ? AND AID = ? order by AID";
		List<ApplicationSetting> applicationSetting = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ApplicationSetting>() {
			@Override
			public ApplicationSetting mapRow(ResultSet rs, int rowNum) throws SQLException {

				ApplicationSetting applicationSetting = new ApplicationSetting();

				applicationSetting.setId(rs.getLong("ID"));
				applicationSetting.setUserID(rs.getLong("USERID"));
				applicationSetting.setAllianceID(rs.getLong("AID"));
				applicationSetting.setFieldName(rs.getString("FIELDNAME"));
				applicationSetting.setFieldValue(rs.getString("FIELDVALUE"));
				return applicationSetting;
			}
		});

		return applicationSetting;
	}

	@Override
	public void updateApplicationSetting(ApplicationSetting applicationSetting) {

		String selectQuery = "update WCAPPLICATIONSETTINGS set FIELDVALUE =? WHERE ID = ? AND USERID = ? AND AID = ? AND FIELDNAME = ?";

		String fieldValue = (applicationSetting.getFieldValue() == null) ? "" : applicationSetting.getFieldValue();
		Long id = applicationSetting.getId();
		Long userID = applicationSetting.getUserID();
		Long allianceID = applicationSetting.getAllianceID();
		String fieldName = applicationSetting.getFieldName();
		jdbcTemplate.update(selectQuery, new Object[] { fieldValue, id, userID, allianceID, fieldName });

	}

	@Override
	public void addApplicationSetting(ApplicationSetting applicationSetting) {

		String insertQuery = "INSERT INTO WCAPPLICATIONSETTINGS ( ID, USERID, AID, FIELDNAME, FIELDVALUE ) VALUES ( ?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, new Object[] { applicationSetting.getId(), applicationSetting.getUserID(), applicationSetting.getAllianceID(), applicationSetting.getFieldName(), applicationSetting.getFieldValue() });
	}

	@Override
	public void deleteApplicationSetting(ApplicationSetting applicationSetting) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(applicationSetting.getId());
		parameters.add(applicationSetting.getUserID());
		parameters.add(applicationSetting.getAllianceID());
		parameters.add(applicationSetting.getFieldName());

		String insertQuery = "delete WCAPPLICATIONSETTINGS where ID = ? and USERID = ? and AID = ? and FIELDNAME = ?";

		jdbcTemplate.update(insertQuery, parameters.toArray());
	}

	@Override
	public ActiveSyntax getActiveSyntax() {

		return getActiveSyntaxVersionProcedure.execute();
	}

	@Override
	public List<String> getJournalComponents() {

		String selectQuery = String.format("select jrnl_comp_name from ldHelperJrnlComp order by jrnl_comp_name");

		List<String> journalsNamesList = jdbcTemplate.query(selectQuery, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				String componentName = rs.getString("jrnl_comp_name");
				return componentName;
			}
		});

		return journalsNamesList;
	}

	@Override
	public List<Alliance> getAlliances() {

		String queryString = "select alliance_instance, aid  from ldsettings order by aid";
		List<Alliance> saaList = (List<Alliance>) jdbcTemplate.query(queryString, new RowMapper<Alliance>() {
			public Alliance mapRow(ResultSet rs, int rowNum) throws SQLException {
				String aid = rs.getString("aid");
				String allianceInstance = rs.getString("alliance_instance");

				Alliance alliance = new Alliance();
				alliance.setName(allianceInstance);
				alliance.setId(aid);
				return alliance;
			}
		});
		return saaList;
	}

	@Override
	public ApplicationSetting getApplicationSetting(Long id, Long userId, Long aid, String settingName) {
		String selectQuery = String.format("SELECT ID, USERID, AID, FIELDNAME, FIELDVALUE FROM WCAPPLICATIONSETTINGS WHERE ID = %d AND USERID = %d AND AID = %d AND FIELDNAME = '%s' order by AID", id, userId, aid, settingName);

		List<ApplicationSetting> applicationSetting = jdbcTemplate.query(selectQuery, new RowMapper<ApplicationSetting>() {
			@Override
			public ApplicationSetting mapRow(ResultSet rs, int rowNum) throws SQLException {

				ApplicationSetting applicationSetting = new ApplicationSetting();

				applicationSetting.setId(rs.getLong("ID"));
				applicationSetting.setUserID(rs.getLong("USERID"));
				applicationSetting.setAllianceID(rs.getLong("AID"));
				applicationSetting.setFieldName(rs.getString("FIELDNAME"));
				applicationSetting.setFieldValue(rs.getString("FIELDVALUE"));
				return applicationSetting;
			}
		});
		if (applicationSetting.size() == 0) {
			return null;
		}

		return applicationSetting.get(0);
	}

	@Override
	public List<DatabaseInfo> getDatabaseInfo() {
		String selectQuery = "SELECT InstallationDate, InstallationUser, Major, Minor, Revision FROM sDBVersion  ORDER BY InstallationDate ASC";

		List<DatabaseInfo> databaseInfo = jdbcTemplate.query(selectQuery, new RowMapper<DatabaseInfo>() {
			@Override
			public DatabaseInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				DatabaseInfo databaseInfo = new DatabaseInfo();
				Date dt = null;
				if (rs.getTimestamp("InstallationDate") != null) {
					dt = new Date(rs.getTimestamp("InstallationDate").getTime());
				}
				databaseInfo.setInstallationDate(dt);
				databaseInfo.setInstallationUser(rs.getString("InstallationUser"));
				databaseInfo.setMajor(rs.getInt("Major"));
				databaseInfo.setMinor(rs.getInt("Minor"));
				databaseInfo.setRevision(rs.getInt("Revision"));

				return databaseInfo;
			}
		});

		return databaseInfo;
	}

	@Override
	public boolean is4EyeFeatureEnabled() {

		String queryString = "select propvalue from REPORTINGPROPERTIES where propname = 'IsFourEyeEnabled' ";

		List<String> eventClassList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("propvalue");
			}
		});
		return (eventClassList.get(0) != null && eventClassList.get(0).trim().equals("1") ? true : false);
	}

	public void setLoadDbVersion(boolean val) {
		String dbVersionStr;
		try {
			if (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
				alterSession("SIDE");
			}
			dbVersionStr = this.getProductVersion();
			dbVersionStr = dbVersionStr.substring(0, dbVersionStr.indexOf('.') + 1) + dbVersionStr.substring(dbVersionStr.indexOf('.') + 1).replace(".", "");

			getDbPortabilityHandler().setDbVersion(Double.parseDouble(dbVersionStr));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getAllCurancy() {
		List<String> curancy = jdbcTemplate.query("select DISTINCT CURRENCYCODE from cu", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("CURRENCYCODE");
			}
		});
		return curancy;
	}

	@Override
	public List<Country> getAllCountry() {

		List<Country> countrisList = jdbcTemplate.query("select * from ct order by CountryName asc", new RowMapper<Country>() {
			@Override
			public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
				Country country = new Country();
				country.setDisplayName(rs.getString("CountryName"));
				country.setName(rs.getString("CountryCode"));
				return country;
			}
		});

		return countrisList;
	}

	@Override
	public List<String> getAllMessageType() {
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
	public int getTextDecompositionType() {
		String queryString = "select PARSE_TEXTBLOCK from LDGLOBALSETTINGS";
		List<Integer> decompositionTypes = (List<Integer>) jdbcTemplate.query(queryString, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int decompositionType = rs.getInt("PARSE_TEXTBLOCK");

				return decompositionType;
			}
		});
		return decompositionTypes.get(0);
	}

	@Override
	public List<String> getQualifierList() {
		ArrayList<String> qualifierList = new ArrayList<String>();
		qualifierList.add("");
		qualifierList.add("STP");
		qualifierList.add("REMIT");
		qualifierList.add("COV");
		qualifierList.add("IRSLST");
		return qualifierList;
	}

	@Override
	public List<StatusCode> getStatusCodeList() {
		List<StatusCode> statusCodeList = new ArrayList<StatusCode>();

		StatusCode anyCode = new StatusCode();
		anyCode.setCodeValue("Any");
		anyCode.setLabel("Any");

		StatusCode pCode = new StatusCode();
		pCode.setCodeValue("ACSP");
		pCode.setLabel("In Progress");

		////////////////////

		String label = "Completed";

		// ACSC was named to ACCC
		StatusCode acccCode = new StatusCode();
		acccCode.setCodeValue("ACCC");
		acccCode.setLabel(label);
		///////////////////

		StatusCode rCode = new StatusCode();
		rCode.setCodeValue("RJCT");
		rCode.setLabel("Rejected");

		//////////////////

		StatusCode returnCode = new StatusCode();
		returnCode.setCodeValue("RETN");
		returnCode.setLabel("Returned");

		StatusCode gSRPCodeCR = new StatusCode();
		gSRPCodeCR.setCodeValue("PDCR");
		gSRPCodeCR.setLabel("PDCR");

		StatusCode gSRPCodeJR = new StatusCode();
		gSRPCodeJR.setCodeValue("RJCR");
		gSRPCodeJR.setLabel("RJCR");

		StatusCode gSRPCodeCL = new StatusCode();
		gSRPCodeCL.setCodeValue("CNCL");
		gSRPCodeCL.setLabel("CNCL");

		statusCodeList.add(anyCode);
		statusCodeList.add(pCode);
		statusCodeList.add(acccCode);
		statusCodeList.add(rCode);
		statusCodeList.add(returnCode);

		statusCodeList.add(gSRPCodeCR);
		statusCodeList.add(gSRPCodeJR);
		statusCodeList.add(gSRPCodeCL);

		return statusCodeList;

	}

	@Override
	public List<StatusReason> getGSRPCodeList() {
		List<StatusReason> gSRPReasonCodes = new ArrayList<StatusReason>();

		StatusReason statusReason = new StatusReason();
		statusReason.setLabel("Any");
		statusReason.setResonValue("Any");
		gSRPReasonCodes.add(statusReason);

		StatusReason dupl = new StatusReason();
		dupl.setLabel("DUPL");
		dupl.setResonValue("DUPL");
		gSRPReasonCodes.add(dupl);

		StatusReason agnt = new StatusReason();
		agnt.setLabel("AGNT");
		agnt.setResonValue("AGNT");
		gSRPReasonCodes.add(agnt);

		StatusReason curr = new StatusReason();
		curr.setLabel("CURR");
		curr.setResonValue("CURR");
		gSRPReasonCodes.add(curr);

		StatusReason cust = new StatusReason();
		cust.setLabel("CUST");
		cust.setResonValue("CUST");
		gSRPReasonCodes.add(cust);

		StatusReason upay = new StatusReason();
		upay.setLabel("UPAY");
		upay.setResonValue("UPAY");
		gSRPReasonCodes.add(upay);

		StatusReason cuta = new StatusReason();
		cuta.setLabel("CUTA");
		cuta.setResonValue("CUTA");
		gSRPReasonCodes.add(cuta);

		StatusReason tech = new StatusReason();
		tech.setLabel("TECH");
		tech.setResonValue("TECH");
		gSRPReasonCodes.add(tech);

		StatusReason frad = new StatusReason();
		frad.setLabel("FRAD");
		frad.setResonValue("FRAD");
		gSRPReasonCodes.add(frad);

		StatusReason covr = new StatusReason();
		covr.setLabel("COVR");
		covr.setResonValue("COVR");
		gSRPReasonCodes.add(covr);

		StatusReason AM09 = new StatusReason();
		AM09.setLabel("AM09");
		AM09.setResonValue("Am09");
		gSRPReasonCodes.add(AM09);

		return gSRPReasonCodes;
	}

	@Override
	public List<StatusReason> getReasonCodeList() {
		List<StatusReason> reasonCodeList = new ArrayList<>();

		StatusReason statusReason = new StatusReason();
		statusReason.setLabel("Any");
		statusReason.setResonValue("Any");

		StatusReason statusReason1 = new StatusReason();
		statusReason1.setLabel("G000");
		statusReason1.setResonValue("G000");

		StatusReason statusReason2 = new StatusReason();
		statusReason2.setLabel("G001");
		statusReason2.setResonValue("G001");

		StatusReason statusReason3 = new StatusReason();
		statusReason3.setLabel("G002");
		statusReason3.setResonValue("G002");

		StatusReason statusReason4 = new StatusReason();
		statusReason4.setLabel("G003");
		statusReason4.setResonValue("G003");

		StatusReason statusReason5 = new StatusReason();
		statusReason5.setLabel("G004");
		statusReason5.setResonValue("G004");
		reasonCodeList.add(statusReason);
		reasonCodeList.add(statusReason1);
		reasonCodeList.add(statusReason2);
		reasonCodeList.add(statusReason3);
		reasonCodeList.add(statusReason4);
		reasonCodeList.add(statusReason5);

		return reasonCodeList;
	}

	@Override
	public List<StatusReason> getCovReasonCodeList(String statusCode) {
		List<StatusReason> reasonCodeListRJCR = new ArrayList<>();
		List<StatusReason> reasonCodeListPDCR = new ArrayList<>();
		List<StatusReason> reasonCodeListGSRPReq = new ArrayList<>();
		if (statusCode.equalsIgnoreCase("RJCR")) {
			StatusReason statusReason = new StatusReason();
			statusReason.setLabel("Any");
			statusReason.setResonValue("Any");

			StatusReason LEGL = new StatusReason();
			LEGL.setLabel("LEGL");
			LEGL.setResonValue("LEGL");

			StatusReason AGNT = new StatusReason();
			AGNT.setLabel("AGNT");
			AGNT.setResonValue("AGNT");

			StatusReason CUST = new StatusReason();
			CUST.setLabel("CUST");
			CUST.setResonValue("CUST");

			StatusReason ARDT = new StatusReason();
			ARDT.setLabel("ARDT");
			ARDT.setResonValue("ARDT");

			StatusReason NOAS = new StatusReason();
			NOAS.setLabel("NOAS");
			NOAS.setResonValue("NOAS");

			StatusReason NOOR = new StatusReason();
			NOOR.setLabel("NOOR");
			NOOR.setResonValue("NOOR");

			StatusReason AC04 = new StatusReason();
			AC04.setLabel("AC04");
			AC04.setResonValue("AC04");

			StatusReason AM04 = new StatusReason();
			AM04.setLabel("AM04");
			AM04.setResonValue("AM04");

			StatusReason INDM = new StatusReason();
			INDM.setLabel("INDM");
			INDM.setResonValue("INDM");

			StatusReason FRNA = new StatusReason();
			FRNA.setLabel("FRNA");
			FRNA.setResonValue("FRNA");

			reasonCodeListRJCR.add(statusReason);
			reasonCodeListRJCR.add(LEGL);
			reasonCodeListRJCR.add(AGNT);
			reasonCodeListRJCR.add(CUST);
			reasonCodeListRJCR.add(ARDT);
			reasonCodeListRJCR.add(NOAS);
			reasonCodeListRJCR.add(NOOR);
			reasonCodeListRJCR.add(AC04);
			reasonCodeListRJCR.add(AM04);
			reasonCodeListRJCR.add(INDM);
			reasonCodeListRJCR.add(FRNA);

			return reasonCodeListRJCR;

		} else if (statusCode.equalsIgnoreCase("PDCR")) {
			StatusReason statusReason = new StatusReason();
			statusReason.setLabel("Any");
			statusReason.setResonValue("Any");

			StatusReason PDCR000 = new StatusReason();
			PDCR000.setLabel("S000");
			PDCR000.setResonValue("S000");

			StatusReason PDCR001 = new StatusReason();
			PDCR001.setLabel("S001");
			PDCR001.setResonValue("S001");

			StatusReason PDCR002 = new StatusReason();
			PDCR002.setLabel("S002");
			PDCR002.setResonValue("S002");

			StatusReason PDCR003 = new StatusReason();
			PDCR003.setLabel("S003");
			PDCR003.setResonValue("S003");

			StatusReason PTNA = new StatusReason();
			PTNA.setLabel("PTNA");
			PTNA.setResonValue("PTNA");

			StatusReason RQDA = new StatusReason();
			RQDA.setLabel("RQDA");
			RQDA.setResonValue("RQDA");

			StatusReason INDM = new StatusReason();
			INDM.setLabel("INDM");
			INDM.setResonValue("INDM");

			reasonCodeListPDCR.add(statusReason);
			reasonCodeListPDCR.add(PDCR000);
			reasonCodeListPDCR.add(PDCR001);
			reasonCodeListPDCR.add(PDCR002);
			reasonCodeListPDCR.add(PDCR003);

			reasonCodeListPDCR.add(PTNA);
			reasonCodeListPDCR.add(RQDA);
			reasonCodeListPDCR.add(INDM);
			return reasonCodeListPDCR;

		}
		return null;
	}

	@Override
	public List<StatusCode> getStatusList() {
		List<StatusCode> statusCodeList = new ArrayList<StatusCode>();
		StatusCode anyCode = new StatusCode();
		anyCode.setCodeValue("Any");
		anyCode.setLabel("Any");

		StatusCode mesgNotUpdatedCode = new StatusCode();
		mesgNotUpdatedCode.setCodeValue("New");
		mesgNotUpdatedCode.setLabel("New");

		StatusCode pCode = new StatusCode();
		pCode.setCodeValue("ACSP");
		pCode.setLabel("In Progress");

		////////////////////

		String label = "Completed";

		// ACSC was named to ACCC
		StatusCode acccCode = new StatusCode();
		acccCode.setCodeValue("ACCC");
		acccCode.setLabel(label);

		///////////////////

		StatusCode rCode = new StatusCode();
		rCode.setCodeValue("RJCT");
		rCode.setLabel("Rejected");

		//////////////////

		StatusCode returnCode = new StatusCode();
		returnCode.setCodeValue("RETN");
		returnCode.setLabel("Returned");

		//////////////////

		statusCodeList.add(anyCode);
		statusCodeList.add(mesgNotUpdatedCode);
		statusCodeList.add(pCode);
		statusCodeList.add(acccCode);
		statusCodeList.add(rCode);
		statusCodeList.add(returnCode);

		return statusCodeList;
	}

	@Override
	public List<String> getChangeList() {
		ArrayList<String> detailOfChangeList = new ArrayList<String>();
		detailOfChangeList.add("");
		detailOfChangeList.add("BEN");
		detailOfChangeList.add("OUR");
		detailOfChangeList.add("SHA");
		return detailOfChangeList;
	}

	@Override
	public boolean isPartitionedDatabase() {
		String queryString = "select count(*) cnt from all_tab_columns where  COLUMN_NAME='X_CREA_DATE_TIME_MESG' and table_name='RINST'";

		Integer count = jdbcTemplate.query(queryString, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getInt("cnt");
				}
				return null;
			}
		});

		boolean isPartitioned = count == 1;
		return isPartitioned;
	}

	@Override
	public List<String> getSattlmentMethodList() {
		ArrayList<String> sattlmentMethodList = new ArrayList<String>();
		sattlmentMethodList.add("Any");
		sattlmentMethodList.add("CLRG");
		sattlmentMethodList.add("COVE");
		sattlmentMethodList.add("INDA");
		sattlmentMethodList.add("INGA");
		return sattlmentMethodList;
	}

	@Override
	public List<String> getStatusClearingSystemList() {
		// FOR NEW CODES
		List<String> records = jdbcTemplate.query("select code from CLEARING_SYSTEM", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("code");
			}
		});
		records.add(0, "Any");

		return records;
	}

	@Override
	public List<StatusReason> getRejectReasonCodeList() {

		List<StatusReason> reasonCodeList = new ArrayList<StatusReason>();
		StatusReason statusReason = new StatusReason();
		statusReason.setLabel("Any");
		statusReason.setResonValue("Any");

		StatusReason statusReason1 = new StatusReason();
		statusReason1.setLabel("AC01");
		statusReason1.setResonValue("AC01");

		StatusReason statusReason2 = new StatusReason();
		statusReason2.setLabel("AC04");
		statusReason2.setResonValue("AC04");

		StatusReason statusReason3 = new StatusReason();
		statusReason3.setLabel("AC06");
		statusReason3.setResonValue("AC06");

		StatusReason statusReason4 = new StatusReason();
		statusReason4.setLabel("BE01");
		statusReason4.setResonValue("BE01");

		StatusReason statusReason5 = new StatusReason();
		statusReason5.setLabel("NOAS");
		statusReason5.setResonValue("NOAS");

		StatusReason statusReason6 = new StatusReason();
		statusReason6.setLabel("RR03");
		statusReason6.setResonValue("RR03");

		StatusReason statusReason7 = new StatusReason();
		statusReason7.setLabel("FF07");
		statusReason7.setResonValue("FF07");

		StatusReason statusReason8 = new StatusReason();
		statusReason8.setLabel("RC01");
		statusReason8.setResonValue("RC01");

		StatusReason statusReason9 = new StatusReason();
		statusReason9.setLabel("G004");
		statusReason9.setResonValue("G004");

		StatusReason statusReason10 = new StatusReason();
		statusReason10.setLabel("RC08");
		statusReason10.setResonValue("RC08");

		StatusReason statusReason11 = new StatusReason();
		statusReason11.setLabel("FOCR");
		statusReason11.setResonValue("FOCR");

		StatusReason statusReason12 = new StatusReason();
		statusReason12.setLabel("DUPL");
		statusReason12.setResonValue("DUPL");

		StatusReason statusReason13 = new StatusReason();
		statusReason13.setLabel("RR05");
		statusReason13.setResonValue("RR05");

		StatusReason statusReason14 = new StatusReason();
		statusReason14.setLabel("AM06");
		statusReason14.setResonValue("AM06");

		StatusReason statusReason15 = new StatusReason();
		statusReason15.setLabel("CUST");
		statusReason15.setResonValue("CUST");

		StatusReason statusReason16 = new StatusReason();
		statusReason16.setLabel("MS03");
		statusReason16.setResonValue("MS03");

		reasonCodeList.add(statusReason);
		reasonCodeList.add(statusReason1);
		reasonCodeList.add(statusReason2);
		reasonCodeList.add(statusReason3);
		reasonCodeList.add(statusReason4);
		reasonCodeList.add(statusReason5);
		reasonCodeList.add(statusReason6);
		reasonCodeList.add(statusReason7);
		reasonCodeList.add(statusReason8);
		reasonCodeList.add(statusReason9);
		reasonCodeList.add(statusReason10);
		reasonCodeList.add(statusReason11);
		reasonCodeList.add(statusReason12);
		reasonCodeList.add(statusReason13);
		reasonCodeList.add(statusReason14);
		reasonCodeList.add(statusReason15);
		reasonCodeList.add(statusReason16);

		return reasonCodeList;

	}

	@Override
	public List<String> getServiceType() {
		ArrayList<String> serviceTypeList = new ArrayList<String>();
		serviceTypeList.add("Any");
		serviceTypeList.add("gpi");
		serviceTypeList.add("UC");
		return serviceTypeList;
	}

}
