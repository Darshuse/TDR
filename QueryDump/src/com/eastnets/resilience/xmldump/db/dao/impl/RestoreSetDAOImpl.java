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
package com.eastnets.resilience.xmldump.db.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.DBConnection;
import com.eastnets.resilience.xmldump.db.dao.RestoreSetDAO;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;

/**
 * ldUpdateSetInfo executor
 * 
 * @author EHakawati
 * 
 */
public class RestoreSetDAOImpl implements RestoreSetDAO {

	// archive type
	private static final int TYPE_SAA_QUERY = 3;

	/**
	 * execute ldUpdateSetInfo
	 * 
	 * @param mode
	 * @param fileName
	 * @param messageCount
	 * @param totalCount
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int callRestoreSet(int mode, String fileName, int messageCount, int totalCount, Connection connection) throws SQLException {

		CallableStatement cs = connection.prepareCall("{ call "
				+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_UPDATE_SET_INFO") + " }");

		// mode
		cs.setInt(1, mode);
		cs.setInt(2, GlobalConfiguration.getInstance().getAllianceId());
		cs.setString(3, fileName);
		cs.setInt(4, TYPE_SAA_QUERY);
		cs.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
		cs.setInt(6, messageCount);
		cs.setInt(7, totalCount);
		cs.registerOutParameter(8, java.sql.Types.INTEGER);

		cs.executeUpdate();

		return cs.getInt(8);
	}

}
