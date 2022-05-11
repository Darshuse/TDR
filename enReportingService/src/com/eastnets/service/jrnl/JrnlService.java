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

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.eastnets.service.Service;
import com.eastnets.domain.jrnl.DetailsEntity;
import com.eastnets.domain.jrnl.JournalSearchProcedureParameters;
import com.eastnets.domain.jrnl.JournalShowDetailsProcedureParameters;
import com.eastnets.domain.jrnl.SearchLookups;
import com.eastnets.domain.jrnl.SearchResultEntity;

/**
 * Journal Service Interface
 * @author EastNets
 * @since December 26, 2012
 */
@Transactional (propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public interface JrnlService extends Service {
	
	/**
	 * Search for journals on db based on certain search criteria
	 * @return List<SearchResultEntity>
	 */
	public List<SearchResultEntity> search(JournalSearchProcedureParameters newParams) throws Exception ;	
	
	public DetailsEntity showDetails(JournalShowDetailsProcedureParameters newParams) throws Exception ;	
	
	public SearchLookups fillJournalSearchLookups() throws Exception ;
	
	public String formatMessage(List<SearchResultEntity> result, String dateTimeFormatString) throws Exception ;

	public String getSetting(String loggedInUser, Long loggedInUserID, String settingName) throws Exception ;
	
	public void setSetting(String loggedInUser, Long loggedInUserID, String settingName, String value) throws Exception ;
}
