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

import org.apache.commons.lang.StringUtils;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.UserSettings;
import com.eastnets.domain.viewer.StatusCode;
import com.eastnets.domain.viewer.StatusReason;
import com.eastnets.domain.watchdog.ActiveSyntax;
import com.eastnets.domain.watchdog.SyntaxEntryField;
import com.eastnets.service.ServiceBaseImp;

/**
 * Common Service Implementation
 * @author EastNets
 * @since July 11, 2012
 */
public class CommonServiceImp extends ServiceBaseImp implements CommonService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8073957024423239387L;
	private CommonDAO commonDAO;
	private static HashSet<String> currencies;
	private static SortedMap<String, String> availableAIDs;
	private DBPortabilityHandler dbPortabilityHandler;

	@Override
	public CorrInfo getCorrInfo(String loggedInUser, CorrInfo corr) {
		return commonDAO.getCorrInfo(corr);
	}

	@Override
	public CorrInfo getCorrInfo(CorrInfo corr) {
		return commonDAO.getCorrInfo(corr);
	}

	@Override
	public UserSettings getUserSettings(String loggedInUser) {
		UserSettings userSettings = new UserSettings();
		String vwListDepth = commonDAO.getUserSettings(loggedInUser, "vwListDepth");
		String wdNbDayHistory = commonDAO.getUserSettings(loggedInUser, "wdNbDayHistory");
		String rpDirectory = commonDAO.getUserSettings(loggedInUser, "rpDirectory");
		String email = commonDAO.getUserSettings(loggedInUser, "Email");

		userSettings.setEmail(email);
		userSettings.setRpDirectory(rpDirectory);
		userSettings.setVwListDepth(vwListDepth);
		userSettings.setWdNbDayHistory(wdNbDayHistory);

		return userSettings;

	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	@Override
	public boolean canCreateGroupRequest(String loggedInUser) {
		boolean canCreateGroupRequest = commonDAO.checkAdmit(Constants.MODULE_ID_WATCHDOG,Constants.SOBJ_WD_CREATE_GROUP_REQUEST);
		return canCreateGroupRequest;
	}

	@Override
	public HashSet<String> getCurrencies() {
		if(currencies == null) {
			currencies = commonDAO.getCurrencies();
		}

		return currencies;
	}

	@Override
	public SortedMap<String, String> getAvailableAIDs(String loggedInUser) {
		//if(availableAIDs == null) {
		availableAIDs = commonDAO.getAvailableAIDs();
		//	}

		return availableAIDs;
	}

	/**
	 * 
	 */
	public Long getMaxAID(String loggedInUser){

		return commonDAO.getMaxAID();
	}

	//financial Institution address
	@Override
	public String getCorrInfoString( String loggedInUser, CorrInfo corr ) 
	{
		CorrInfo corrInfo= getCorrInfo( loggedInUser, corr );
		if(corrInfo != null)
		{		
			String tmp= StringUtils.defaultString( corrInfo.getCorrInstitutionName() ) + "\n";
			if ( !StringUtils.isEmpty(corrInfo.getCorrBranchInfo()) ) 
			{
				tmp = tmp + StringUtils.defaultString( corrInfo.getCorrBranchInfo() )  + "\n" ;
			}
			if ( !StringUtils.isEmpty(corrInfo.getCorrLocation()))
			{
				tmp = tmp + StringUtils.defaultString( corrInfo.getCorrLocation() ) + "\n" ;
			}
			if ( !StringUtils.isEmpty(corrInfo.getCorrCityName() ))
			{
				tmp = tmp + StringUtils.defaultString( corrInfo.getCorrCityName() ) + "\n" ;
			}
			tmp = tmp +  StringUtils.defaultString( corrInfo.getCorrCtryCode() ) + "\n";
			tmp = tmp +  StringUtils.defaultString( corrInfo.getCorrCtryName() );
			return tmp;
		}
		return "";
	}

	@Override
	public ActiveSyntax getActiveSyntax(String loggedInUser) {

		return commonDAO.getActiveSyntax();
	}

	@Override
	public List<SyntaxEntryField> getFiledsValue(String loggedInUser, Long messgaeType, Long versionIdx) {

		return commonDAO.getFiledsValue(messgaeType, versionIdx);
	}

	@Override
	public List<String> getJournalComponents(String loggedInUser) {
		return commonDAO.getJournalComponents();
	}

	public String getDataBaseInfo() throws Exception {
		return commonDAO.getDataBaseInfo();
	}

	public String getProductVersion() throws Exception {
		return commonDAO.getProductVersion();
	}

	public double getDbVersion() throws Exception {
		return getDbPortabilityHandler().getDbVersion();
	}

	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
	}

	@Override
	public boolean is4EyeFeatureEnabled() {
		return commonDAO.is4EyeFeatureEnabled();
	}

	public boolean moduleInitialize(String loggedinUser, String moduleName) {
		return true;
	}
	@Override
	public int getTextDecompositionType() {
		return commonDAO.getTextDecompositionType();
	}

	@Override
	public boolean isPartitionedDatabase() {
		return commonDAO.isPartitionedDatabase();
	}

	@Override
	public List<StatusCode> getStatusCodeList() {
		return commonDAO.getStatusCodeList();
	}

	@Override
	public List<StatusReason> getReasonCodeList() {
		return commonDAO.getReasonCodeList();
	}
	@Override
	public List<StatusReason> getCovReasonCodeList(String coveStatusCode) {
		return commonDAO.getCovReasonCodeList(coveStatusCode);
	}

	@Override
	public List<StatusCode> getStatusList() {
		return commonDAO.getStatusList();
	}

	@Override
	public List<String> getSattlmentMethodList() {
		// TODO Auto-generated method stub
		return commonDAO.getSattlmentMethodList();
	}	

	@Override
	public List<String> getStatusClearingSystemList() {
		// TODO Auto-generated method stub
		return commonDAO.getStatusClearingSystemList();
	}
	
	@Override
	public List<StatusReason> getRejectReasonCodeList() {
		// TODO Auto-generated method stub
		return commonDAO.getRejectReasonCodeList();
	}


}