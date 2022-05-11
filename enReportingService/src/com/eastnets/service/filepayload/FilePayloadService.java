/**
 * Copyright (c) 2013 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.filepayload;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.filepayload.FilePayloadGlobalSettings;
import com.eastnets.domain.filepayload.FilePayloadSettings;
import com.eastnets.service.Service;

/**
 * FilePayload Service Interface
 * @author EastNets
 * @since September 08, 2013
 */
public interface FilePayloadService extends Service{

	/**
	 * get list of all File Payloads that needs to be retrieved form the SAA server
	 * @param loggedInUser the user logged in , used by the aspect
	 * @param aid the aid to use
	 * @param maxTryCount maximum number for tries that is allowed for the same file before giving up on finding its payload on the SAA server
	 * @param delayMinutes delay in minutes to wait between each try and the next 
	 * @param chunkCount maximum number of messages that we want to get the payload for 
	 * @return file payload list
	 */
	List<FilePayload> getFilesToRetrieve( String loggedInUser, int aid, int maxTryCount, int delayMinutes, int chunkCount );
	
	/**
	 * update the payloads for the passed files 
	 * @param loggedInUser the user logged in , used by the aspect
	 * @param filesPayload : file records to be updated
	 * @param globalSettings 
	 */
	void updateFilesPayload( String loggedInUser, List<FilePayload> filesPayload, FilePayloadGlobalSettings globalSettings )  throws Exception;
	
	/**
	 * updates the trial time and count by adding one trial to the count and by setting the trial time to the current time on the database server 
	 * @param loggedInUser the user logged in , used by the aspect
	 * @param filePayloads file records to be updated
	 */
	void updateTrials( String loggedInUser, List<FilePayload> filePayloads )  throws DataAccessException  ;

	/**
	 * gets the global settings used by the client for file retrieval
	 * @param loggedInUser the user logged in , used by the aspect
	 * @return
	 */
	FilePayloadGlobalSettings getGlobalSettings( String loggedInUser );
	
	/**
	 * gets the settings for the connection, used to connect to SAA server and get and store the file payloads
	 * @param loggedInUser the user logged in , used by the aspect
	 * @param aid : the aid to use
	 * @return
	 */
	FilePayloadSettings getConnectionSettings( String loggedInUser, int aid );	
	

	/**
	 * update the payload status and reset the requested flag for all payloads
	 * @param loggedInUser
	 * @param payloads
	 * @param results
	 */
	void updateStatus(String loggedInUser, List<FilePayload> payloads, List<String> results );
	
	
	/**
	 * reset the requested flag for all flags, this should be called in on the startup of the application
	 * @param loggedInUser
	 * @param aid
	 */
	void resetRequestedFlag( String loggedInUser, int aid );

	/**
	 * reset the requested flag form messages that was last requested before requestedResetTimeHours
	 * @param loggedInUser
	 * @param aid
	 * @param requestedResetTimeHours
	 */
	void resetRequested(String loggedInUser, int aid, double requestedResetTimeHours);
	
	int updateSEPAText( final FilePayload filePayload, String mesgText , String mesgId, int mesgOrder, int mesgSize, int blkFlag, int numMesgs);


}
