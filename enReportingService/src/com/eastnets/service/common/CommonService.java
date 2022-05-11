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

package com.eastnets.service.common;

import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;

import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.UserSettings;
import com.eastnets.domain.viewer.StatusCode;
import com.eastnets.domain.viewer.StatusReason;
import com.eastnets.domain.watchdog.ActiveSyntax;
import com.eastnets.domain.watchdog.SyntaxEntryField;

/**
 * Common Service Interface
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public interface CommonService {

	/**
	 * Get user settings
	 * 
	 * @param loggedInUser
	 * @return UserSettings
	 */
	public UserSettings getUserSettings(String loggedInUser);

	/**
	 * Get correspondent information
	 * 
	 * @param loggedInUser
	 * @param corr
	 * @return CorrInfo
	 */
	public CorrInfo getCorrInfo(String loggedInUser, CorrInfo corr);

	/**
	 * Get correspondent information formatted as string
	 * 
	 * @param loggedInUser
	 * @param corr
	 * @return String
	 */
	public String getCorrInfoString(String loggedInUser, CorrInfo corr);

	// security
	/**
	 * Check if user has the permission to create a group request
	 * 
	 * @param loggedInUser
	 * @return boolean
	 */
	public boolean canCreateGroupRequest(String loggedInUser);

	/**
	 * Get all used currencies
	 * 
	 * @return HashSet<String>
	 */
	public HashSet<String> getCurrencies();

	/**
	 * Get available AIDs
	 * 
	 * @param loggedInUser
	 * @return Map<String, String>
	 */
	public SortedMap<String, String> getAvailableAIDs(String loggedInUser);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public Long getMaxAID(String loggedInUser);

	/**
	 * get Active Syntax from database
	 * 
	 * @param loggedInUser
	 * @return ActiveSyntax
	 */
	public ActiveSyntax getActiveSyntax(String loggedInUser);

	/**
	 * @param loggedInUser
	 * @param messgaeType
	 * @param versionIdx
	 * @return
	 */
	public List<SyntaxEntryField> getFiledsValue(String loggedInUser, Long messgaeType, Long versionIdx);

	/**
	 * Get list of components names
	 * 
	 * @param loggedInUser
	 * @return List<String>
	 */
	public List<String> getJournalComponents(String loggedInUser);

	public String getDataBaseInfo() throws Exception;

	public String getProductVersion() throws Exception;

	public double getDbVersion() throws Exception;

	public boolean is4EyeFeatureEnabled();

	public boolean moduleInitialize(String loggedinUser, String moduleName);

	public int getTextDecompositionType();

	public boolean isPartitionedDatabase();

	public List<StatusCode> getStatusCodeList();

	public List<StatusCode> getStatusList();

	public List<StatusReason> getReasonCodeList();

	public List<StatusReason> getCovReasonCodeList(String coveStatusCode);

	CorrInfo getCorrInfo(CorrInfo corr);

	public List<String> getSattlmentMethodList();

	public List<String> getStatusClearingSystemList();

	public List<StatusReason> getRejectReasonCodeList();
}
