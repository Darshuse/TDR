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
package com.eastnets.resilience.xmldump.db.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ldRestoreSet DAO
 * 
 * @author EHakawati
 * 
 */
public interface RestoreSetDAO {

	public final int MODE_NEW = 0;
	public final int MODE_UPDATE = 1;

	public int callRestoreSet(int mode, String fileName, int messageCount, int totalCount, Connection connection) throws SQLException;

}
