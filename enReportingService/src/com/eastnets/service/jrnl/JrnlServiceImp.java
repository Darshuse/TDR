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

package com.eastnets.service.jrnl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.jrnl.JrnlDAO;
import com.eastnets.domain.Alliance;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.jrnl.DetailsEntity;
import com.eastnets.domain.jrnl.JournalSearchProcedureParameters;
import com.eastnets.domain.jrnl.JournalShowDetailsProcedureParameters;
import com.eastnets.domain.jrnl.SearchResultEntity;
import com.eastnets.domain.jrnl.SearchLookups;
import com.eastnets.service.ServiceBaseImp;


/**
 * Journal Service Implementation
 * @author EastNets
 * @since December 26, 2012
 */
public class JrnlServiceImp extends ServiceBaseImp implements JrnlService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2923185275527285113L;
	private JrnlDAO jrnlDAO;
	private CommonDAO commonDAO;

	public JrnlDAO getJrnlDAO() {
		return jrnlDAO;
	}

	public void setJrnlDAO(JrnlDAO jrnlDAO) {
		this.jrnlDAO = jrnlDAO;
	}
	
	public CommonDAO getCommonDAO() {
		return commonDAO;
	}
	
	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SearchResultEntity> search(JournalSearchProcedureParameters newParams) throws Exception {
		
		List<SearchResultEntity> results= null;

		results= (List<SearchResultEntity>)jrnlDAO.search( newParams );
				
		return results;
	}
	
	@Override
	public DetailsEntity showDetails(JournalShowDetailsProcedureParameters newParams) throws Exception {
		
		DetailsEntity detailsEntity = null;
		
		detailsEntity = jrnlDAO.showDetails(newParams);
		
		return detailsEntity;
	}
	
	@Override
	public SearchLookups fillJournalSearchLookups() throws Exception {
		
		List<String> eventClassList = jrnlDAO.fillLookupEventClass();
		List<String> eventSeverityList = jrnlDAO.fillLookupEventSeverity();
		List<String> applicationServiceList = jrnlDAO.fillLookupApplicationService();
		List<String> componentList = jrnlDAO.fillLookupComponent();
		List<String> eventTypeList = jrnlDAO.fillLookupEventType();
		List<String> hostnameList = jrnlDAO.fillLookupHostname();
		List<String> alarmTypeList = jrnlDAO.fillLookupAlarmType();
		List<Alliance> SAAList = commonDAO.getAlliances();
				
		SearchLookups searchLookups = new SearchLookups();
		searchLookups.setEventClassLookup(eventClassList);
		searchLookups.setEventSeverityLookup(eventSeverityList);
		searchLookups.setApplicationServiceLookup(applicationServiceList);
		searchLookups.setComponentLookup(componentList);
		searchLookups.setEventTypeLookup(eventTypeList);
		searchLookups.setHostnameLookup(hostnameList);
		searchLookups.setAlarmTypeLookup(alarmTypeList);
		searchLookups.setSaaListLookup(SAAList);
		
		return searchLookups;
		
	}
	
	public String formatMessage(List<SearchResultEntity> result, String dateTimeFormatString ) throws Exception {
		
		StringBuffer printedFileHeader = new StringBuffer(" (Printed from EN-Reporting Event Journal)");
		StringBuffer line = new StringBuffer("_____________________________________________\r\n");
		StringBuffer banner = new StringBuffer("Event Journal - Journal Entry Report");
		StringBuffer tabSpace = new StringBuffer("       ");
		StringBuffer report = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormatString);
		
		report.append(dateFormat.format(Calendar.getInstance().getTime()) + printedFileHeader + "\r\n\r\n") ;
		for(SearchResultEntity journal : result){
			
			report.append(tabSpace).append(line + "\r\n" );
			report.append(tabSpace).append(banner + "\r\n" );
			report.append(tabSpace).append(line + "\r\n" );
			report.append(tabSpace).append("Date-Time   	 = " + dateFormat.format(journal.getDateTime()) + "\r\n" );
			report.append(tabSpace).append("Name       	     = " + journal.getEventName() + "\r\n" );
			report.append(tabSpace).append("Class       	 = " + journal.getEventClass() + "\r\n" );
			report.append(tabSpace).append("Severity    	 = " + journal.getEventServerity() + "\r\n" );
			report.append(tabSpace).append("Type        	 = " + journal.getEventType() + "\r\n" );
			report.append(tabSpace).append("Number       	 = " + journal.getEventNumber() + "\r\n" );
			report.append(tabSpace).append("Seq No       	 = " + journal.getSequenceNumber()+ "\r\n" );
			report.append(tabSpace).append("Application  	 = " + journal.getApplicationName()+ "\r\n" );
			report.append(tabSpace).append("Function     	 = " + journal.getFunction() + "\r\n" );
			report.append(tabSpace).append("Operator     	 = " + journal.getOperatorNickName() + "\r\n" );
			report.append(tabSpace).append("Hostname     	 = " + journal.getHostName() + "\r\n" );
			report.append(tabSpace).append("Alliance Inst 	 = " + journal.getAllianceInstance() + "\r\n" );
			report.append(tabSpace).append("Journal Text 	 = " ) ;
			StringBuffer extra_spaces = new StringBuffer( tabSpace );
			for ( int i = 0 ; i < "Journal Text = ".length() ; ++i ){
				extra_spaces.append(" ");
			}
			splitDescription( extra_spaces , report, journal); 
			
		}
		
		return report.toString();
	}

	/*
	 * when journal description is large, it is splited in multiple line, 
	 * based on 65 character in each line
	 */
	private void splitDescription(StringBuffer tabSpace, StringBuffer report, SearchResultEntity journal) throws Exception {
		
		StringTokenizer st2 = new StringTokenizer(journal.getDescription(), " ");
		String description = "", nextTkn ;
		boolean firstLine = true;
		while(st2.hasMoreTokens()){
			
			nextTkn = st2.nextToken();
			
			nextTkn = nextTkn.replaceAll("\r\n", "\r\n" + tabSpace);
			
			if( (description.length() + nextTkn.length()) >= 65 ){
				if ( ! firstLine ){
					report.append(tabSpace);
				}
				report.append(description + "\r\n" );
				description = nextTkn + " " ;
				firstLine= false;
			} 
			else{
				description = description.concat(nextTkn + " ");
			}
		}
		
		if(description.length() > 0){
			if ( ! firstLine ){
				report.append(tabSpace);
			}
			report.append(description + "\r\n" );
		}
		report.append("\r\n");
	}
	
	@Override
	public String getSetting(String loggedInUser, Long loggedInUserID, String settingName ) throws Exception {
		Long aid= new Long( 0 );//we don't care about the alliance id so that i always use the 0
		ApplicationSetting applicationSetting= commonDAO.getApplicationSetting(Constants.APPLICATION_JOURNAL_OPTIONS, loggedInUserID, aid, settingName);
		if( applicationSetting != null ){
			return applicationSetting.getFieldValue();
		}
		return null;
	}	

	@Override
	public void setSetting(String loggedInUser, Long loggedInUserID, String settingName, String value ) throws Exception {
		Long aid= new Long( 0 );//we don't care about the alliance id so that i always use the 0
		ApplicationSetting applicationSetting= commonDAO.getApplicationSetting(Constants.APPLICATION_JOURNAL_OPTIONS, loggedInUserID, aid, settingName);
		//if the setting already exist just update it  
		if( applicationSetting != null ){
			applicationSetting.setFieldValue(value);
			commonDAO.updateApplicationSetting(applicationSetting);
			return;
		}
		//els add the setting to the database
		applicationSetting= new ApplicationSetting();
		applicationSetting.setAllianceID(aid);
		applicationSetting.setFieldName(settingName);
		applicationSetting.setFieldValue(value);
		applicationSetting.setId(Constants.APPLICATION_JOURNAL_OPTIONS);
		applicationSetting.setUserID(loggedInUserID);
		commonDAO.addApplicationSetting(applicationSetting);
	}
}