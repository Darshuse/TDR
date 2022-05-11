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

package com.eastnets.service.archive;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.archive.ArchiveLog;
import com.eastnets.domain.archive.ArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveOptions;
import com.eastnets.domain.archive.JrnlRestoreOptions;
import com.eastnets.domain.archive.MessageArchiveOptions;
import com.eastnets.domain.archive.JrnlArchiveSettings;
import com.eastnets.domain.archive.MessageArchiveSettings;
import com.eastnets.domain.archive.MessageRestoreOptions;
import com.eastnets.domain.archive.RestoreOptions;
import com.eastnets.domain.archive.RestoreSet;
import com.eastnets.service.Service;

/**
 * Archive Service Interface
 * @author EastNets
 * @since July 22, 2012
 */
@Transactional (propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public interface ArchiveService extends Service {

	
	/**
	 * create Process for Archive Messages Tool
	 * @param messageArchiveOptions
	 * @return Process
	 */
	public Process createMessageArchiveProcess(MessageArchiveOptions messageArchiveOptions );
	
	/** 
	 * create Process for Restore Messages Tool
	 * @param messageRestoreOptions
	 * @return Process
	 */
	public Process createMessageRestoreProcess(MessageRestoreOptions messageRestoreOptions );
	
	/**
	 * 
	 * @param messageRestoreOptions
	 * @return
	 */
	public Process createSwiftMessageRestoreProcess(MessageRestoreOptions messageRestoreOptions );
	/**
	 * create Process for Archive Journals Tool
	 * @param jrnlArchiveOptions
	 * @return Process
	 */
	public Process createJrnlArchiveProcess(JrnlArchiveOptions jrnlArchiveOptions );
	
	/**
	 * create Process for Restore Journals Tool
	 * @param jrnlRestoreOptions
	 * @return Process
	 */
	public Map <Process , Integer> createJrnlRestoreProcess(JrnlRestoreOptions jrnlRestoreOptions , int countOfArch);
	/**
	 * create Process for Restore Journals Tool
	 * @param jrnlRestoreOptions
	 * @return Process
	 */
	public Map <Process , Integer> createSwiftJrnlRestoreProcess (JrnlRestoreOptions jrnlRestoreOptions , int countOfArch);
	
	/**
	 * Add Archive Settings On The Database
	 * @param loggedInUser
	 * @param applicationSetting
	 */
	public void addArchiveSettings(String loggedInUser,ApplicationSetting applicationSetting);
	
	/**
	 * Get Message Archive Settings
	 * @param loggedInUser
	 * @param allianceId
	 * @return MessageArchiveSettings
	 */
	public MessageArchiveSettings getMessageArchiveSettings(String loggedInUser,Long userId,Long allianceId);
	
	/**
	 * Get Journal Archive Settings
	 * @param loggedInUser
	 * @param allianceId
	 * @return JrnlArchiveSettings
	 */
	public JrnlArchiveSettings getJrnlArchiveSettings(String loggedInUser,Long userId,Long allianceId);
	
	
	/**
	 * Get Message Archive Options (General Options for all Archive options)
	 * @param loggedInUser
	 * @return ArchiveOptions
	 */ 
	public ArchiveOptions getMessageArchiveOptions(String loggedInUser,Long userId);
	
	/**
	 * Get Journal Archive Options (General Options for all Archive options)
	 * @param loggedInUser
	 * @return ArchiveOptions
	 */
	public ArchiveOptions getJrnlArchiveOptions(String loggedInUser,Long userId);

	/**
	 * Get Message Restore Options (General Options for all Restore options)
	 * @param loggedInUser
	 * @return RestoreOptions
	 */
	public RestoreOptions getMessageRestoreOptions(String loggedInUser,Long userId);
	
	/**
	 * Get Journal Restore Options (General Options for all Restore options)
	 * @param loggedInUser
	 * @return RestoreOptions
	 */
	public RestoreOptions getJrnlRestoreOptions(String loggedInUser,Long userId);
	
	/**
	 * Update Message Archive Settings
	 * @param loggedInUser
	 * @param messageArchiveSettings
	 */
	public void updateMessageArchiveSettings(String loggedInUser,Long userId, MessageArchiveSettings messageArchiveSettings);
	
	/**
	 * Update Message Archive Options
	 * @param loggedInUser
	 * @param archiveOptions
	 */
	public void updateMessageArchiveOptions(String loggedInUser,Long userId, ArchiveOptions archiveOptions);
	
	/**
	 * Update Message Restore Options
	 * @param loggedInUser
	 * @param restoreOptions
	 */
	public void updateMessageRestoreOptions(String loggedInUser,Long userId, RestoreOptions restoreOptions);
	
	/**
	 * Update Journal Archive Settings
	 * @param loggedInUser
	 * @param jrnlArchiveSettings
	 */
	public void updateJrnlArchiveSettings(String loggedInUser,Long userId, JrnlArchiveSettings jrnlArchiveSettings);
	
	/**
	 * Update Journal Archive Options
	 * @param loggedInUser
	 * @param archiveOptions
	 */
	public void updateJrnlArchiveOptions(String loggedInUser,Long userId, ArchiveOptions archiveOptions);
	
	/**
	 * Get Restored Archives Set From database
	 * @param loggedInUser
	 * @param aid
	 * @param dateFrom
	 * @param dateTo
	 * @return List<RestoreSet>
	 */
	public List<RestoreSet> getRestoreSet(String loggedInUser,Long aid,Date dateFrom, Date dateTo);
	
	/**
	 * Get Archive Log Using Archive ID
	 * @param loggedInUser
	 * @param id
	 * @return ArchiveLog
	 */
	public ArchiveLog getArchiveLog(String loggedInUser,Long id);
	
	/**
	 * Get All Archive Logs 
	 * @param loggedInUser
	 * @return List<ArchiveLog> 
	 */
	public List<ArchiveLog> getArchiveLogs(String loggedInUser);
	
	/**
	 * Get All Archive Logs for specific AID
	 * @param loggedInUser
	 * @param aid
	 * @return List<ArchiveLog> 
	 */
	public List<ArchiveLog> getArchiveLogs(String loggedInUser,Long aid);
	
	/**
	 * Get All Archive Logs for specific AID and module ID (Mesg Archive, Jrnl Archive, Mesg Restore, Jrnl Restore)
	 * @param loggedInUser
	 * @param id
	 * @param moduleId
	 * @return List<ArchiveLog>
	 */
	public List<ArchiveLog>  getArchiveLogs(String loggedInUser,Long id,Long moduleId);
	
	/**
	 * Add New Archive Log For The Database
	 * @param loggedInUser
	 * @param archiveLog
	 */
	public void addArchiveLog(String loggedInUser,ArchiveLog archiveLog);
	
	/**
	 * Get Archive Log Using Module ID, Creation Time, and AID 
	 * @param loggedInUser
	 * @param moduleId
	 * @param creationTime
	 * @param aid
	 * @return ArchiveLog
	 */
	public ArchiveLog getArchiveLog(String loggedInUser,Long moduleId,Date creationTime, Long aid);
	
	/**
	 * Update Archive Log
	 * @param loggedInUser
	 * @param archiveLog
	 */
	public void updateArchiveLog(String loggedInUser, ArchiveLog archiveLog);
		
	/**
	 * 
	 * @param loggedInUser
	 * @param userId
	 * @param aid
	 */	
	public void addDefaultArchiveOptions(String loggedInUser,Long userId);
	
	
	/**
	 * Add New Default Message Archive Settings for new Alliance Connection  
	 * @param loggedInUser
	 * @param userId
	 * @param aid
	 */
	public void addDefaultMessageArchiveSettings(String loggedInUser,Long userId,Long aid);	
	
	/**
	 * Add New Default Journal Archive Settings for new Alliance Connection
	 * @param loggedInUser
	 * @param user
	 * @param aid
	 */
	public void addDefaultJrnlArchiveSettings(String loggedInUser,Long userId,Long aid);
	
	/**
	 * Get List of Supported SAA Versions by Archive Restore Tools
	 * @return List<String>
	 */
	public List<String> getSaaVersionsList();
	
	/**
	 * Set SAA version List
	 * @param saaVersionsList
	 */
	public void setSaaVersionsList(List<String> saaVersionsList);
}
