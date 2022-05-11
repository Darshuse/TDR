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

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;

import com.eastnets.dao.DAO;
import com.eastnets.domain.Alliance;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.DatabaseInfo;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.domain.viewer.Country;
import com.eastnets.domain.viewer.StatusCode;
import com.eastnets.domain.viewer.StatusReason;
import com.eastnets.domain.watchdog.ActiveSyntax;
import com.eastnets.domain.watchdog.SyntaxEntryField;

/**
 * Common DAO Interface
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public interface CommonDAO extends DAO {

	/**
	 * Call "SetProfile" procedure
	 * 
	 * @param profile
	 * @return String
	 */
	public String activateProfile(User profile);

	/**
	 * Get user settings
	 * 
	 * @param username
	 * @param paramName
	 * @return
	 */
	public String getUserSettings(String username, String paramName);

	/**
	 * Check authorization for a certain module
	 * 
	 * @param programId
	 * @param objectId
	 * @return
	 */
	public boolean checkAdmit(int programId, int objectId);// TODO move it to security module

	/**
	 * Get correspondent information
	 * 
	 * @param corr
	 * @return CorrInfo
	 */
	public CorrInfo getCorrInfo(CorrInfo corr);

	// used for oracle only
	/**
	 * Used to alter session to certian shema
	 * 
	 * @param schema
	 */
	public void alterSession(String schema);

	/**
	 * Get all currencies
	 * 
	 * @return HashSet<String>
	 */
	public HashSet<String> getCurrencies();

	/**
	 * Get available AIDs
	 * 
	 * @return Map<String, String>
	 */
	public SortedMap<String, String> getAvailableAIDs();

	/**
	 * 
	 * @return Max AID number
	 */
	public Long getMaxAID();

	/**
	 * get Application Settings for specific module and alliance
	 * 
	 * @param id
	 *            module ID
	 * @param aid
	 *            alliance ID
	 * @return List<ArchiveSetting>
	 */
	public List<ApplicationSetting> getApplicationSettings(Long id, Long userId, Long aid);

	/**
	 * update Application Setting record
	 * 
	 * @param applicationSetting
	 */
	public void updateApplicationSetting(ApplicationSetting applicationSetting);

	/**
	 * add new Application Setting record
	 * 
	 * @param applicationSetting
	 */
	public void addApplicationSetting(ApplicationSetting applicationSetting);

	/**
	 * 
	 * @param applicationSetting
	 */
	public void deleteApplicationSetting(ApplicationSetting applicationSetting);

	/**
	 * get a specific setting that is identified by the aid and the userID and
	 * 
	 * @param id
	 * @param userId
	 * @param aid
	 * @param settingName
	 * @return
	 */
	public ApplicationSetting getApplicationSetting(Long id, Long userId, Long aid, String settingName);

	/**
	 * @return ActiveSyntax
	 */
	public ActiveSyntax getActiveSyntax();

	/**
	 * @param messgaeType
	 * @param versionIdx
	 * @return
	 */
	public List<SyntaxEntryField> getFiledsValue(Long messgaeType, Long versionIdx);

	/**
	 * Get list of components names
	 * 
	 * @return List<String>
	 */
	public List<String> getJournalComponents();

	/**
	 * 
	 * used for audit
	 */
	public void auditDAO(String loginName, String programName, String event, String action, List<AuditLogDetails> auditLogDetailsList, String ipAddress);

	/**
	 * get alliances list
	 */
	public List<Alliance> getAlliances();

	public String getDataBaseInfo() throws Exception;

	public String getProductVersion() throws Exception;

	/**
	 * get database installation and version information.
	 * 
	 * @return database informations ( installation and upgrades )
	 */
	public List<DatabaseInfo> getDatabaseInfo();

	public boolean is4EyeFeatureEnabled();

	/**
	 * call the setProfile procedure for the passed user in the passed connection
	 * 
	 * @param connection
	 * @param user
	 * @return
	 */
	String activateProfile(Connection connection, User user);

	public int getTextDecompositionType();

	public List<String> getAllCurancy();

	public List<Country> getAllCountry();

	public List<String> getAllMessageType();

	public List<String> getChangeList();

	public List<String> getQualifierList();

	public List<StatusCode> getStatusCodeList();

	public List<String> getSattlmentMethodList();

	public List<String> getStatusClearingSystemList();

	public List<String> getServiceType();

	public List<StatusReason> getReasonCodeList();

	public List<StatusReason> getCovReasonCodeList(String coveStatusCode);

	public List<StatusReason> getGSRPCodeList();

	public List<StatusCode> getStatusList();

	public boolean isPartitionedDatabase();

	public List<Action> getAvailableActions(Integer moduleId);

	public List<StatusReason> getRejectReasonCodeList();

}
