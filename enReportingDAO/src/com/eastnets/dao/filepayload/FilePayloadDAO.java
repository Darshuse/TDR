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

package com.eastnets.dao.filepayload;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;

import com.eastnets.dao.DAO;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.filepayload.FilePayloadGlobalSettings;
import com.eastnets.domain.filepayload.FilePayloadSettings;

public interface FilePayloadDAO extends DAO {

	/**
	 * get list of all File Payloads that needs to be retrieved form the SAA server
	 * @param aid the aid to use
	 * @param maxTryCount maximum number for tries that is allowed for the same file before giving up on finding its payload on the SAA server
	 * @param delayMinutes delay in minutes to wait between each try and the next 
	 * @param chunkCount maximum number of messages that we want to get the payload for 
	 * @return file payload list
	 */
	List<FilePayload> getFilesToRetrieve( int aid, int maxTryCount, int delayMinutes, int chunkCount );
	
	/**
	 * update file payload on the database
	 * @param filesPayload : file record to be updated
	 * @return
	 */
	void updateFilePayload( FilePayload filePayload,final InputStream inputStream )  throws Exception;

	/**
	 * update the clob payload data ( payload_text ) on the database
	 * @param filePayload
	 * @throws Exception
	 */
	void updateFilePayloadText(final FilePayload filePayload,final InputStream inputStream)  throws Exception;
	
	
	int updateSEPAText( final FilePayload filePayload, String mesgText , String mesgId, int mesgOrder, int mesgSize, int blkFlag, int numMesgs);
	
	/**
	 * updates the trial time and count by adding one trial to the count and by setting the trial time to the current time on the database server 
	 * @param filePayloads file records to be updated
	 */
	void updateTrials( List<FilePayload> filePayloads )  throws DataAccessException  ;

	/**
	 * gets the global settings used by the client for file retrieval
	 * @return
	 */
	FilePayloadGlobalSettings getGlobalSettings();
	
	/**
	 * @return a comma( , ) separated list of file extensions that should be treated as ASCII, List<String> because each line has max length of 500 characters 
	 */
	List<String> getTextFileExtensions();
	
	/**
	 * gets the settings for the connection, used to connect to SAA server and get and store the file payloads
	 * @param aid : the aid to use
	 * @return
	 */
	FilePayloadSettings getConnectionSettings( int aid );
	
	/**
	 * resets the requested flag back to 0, this should be called at the startup of the application so that we will try to retrieve files that was marked as requested in previous session of the application,
	 * this is needed to preventing files getting stuck if the application crashed or terminated
	 * @param aid : the aid to reset data for 
	 */
	void resetRequestedFlag( int aid );

	/**
	 * update the payload request flag
	 * @param payloads
	 * @param status
	 * @param requested
	 */
	void updateStatus( FilePayload payload, String status, int requested );

	/**
	 * reset the requested flag form messages that was last requested before requestedResetTimeHours
	 * @param aid
	 * @param requestedResetTimeHours
	 */
	void resetRequested(int aid, double requestedResetTimeHours);

	/**
	 * @param propertyName 
	 * @return a property value stored in the reportingProperties table
	 */
	 String getReportingProperty( String propertyName ) ;
	 
	 
	 public ArrayList <String> getXSDIdentifier();

}
