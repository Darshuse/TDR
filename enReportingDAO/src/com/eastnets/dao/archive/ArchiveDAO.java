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

package com.eastnets.dao.archive;

import java.util.Date;
import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.archive.ArchiveLog;
import com.eastnets.domain.archive.RestoreSet;

/**
 * Archive DAO Interface
 * @author EastNets
 * @since July 19, 2012
 */
public interface ArchiveDAO extends DAO {

	public List<RestoreSet> getRestoreSet(Long aid,String dateFrom, String dateTo);
	public List<ArchiveLog> getArchiveLogs();
	public List<ArchiveLog> getArchiveLogs(Long aid);
	public List<ArchiveLog> getArchiveLogs(Long aid,Long moduleId);
	public void addArchiveLog(ArchiveLog archiveLog);
	public ArchiveLog getArchiveLog(Long id);
	public ArchiveLog getArchiveLog(Long moduleId,Date creationTime, Long aid);
	public void updateArchiveLog( ArchiveLog archiveLog);
	public List<ArchiveLog> getArchiveLogsByModuleId(Long moduleId);

	
		
}
