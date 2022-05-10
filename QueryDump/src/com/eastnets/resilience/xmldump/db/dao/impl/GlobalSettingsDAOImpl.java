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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.eastnets.resilience.xmldump.db.DBConnection;
import com.eastnets.resilience.xmldump.db.dao.GlobalSettingsDAO;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;

/**
 * 
 * @author EHakawati
 * 
 */
public class GlobalSettingsDAOImpl implements GlobalSettingsDAO {

	/**
	 * Glocal settings loader
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<Setting, String> getGlobalSettings(Connection connection) throws SQLException {

		PreparedStatement cs = connection.prepareStatement("SELECT LIVE_SOURCE,PARSE_TEXTBLOCK FROM " + GlobalSettings.getSchemaNameWithDot() + "LDGLOBALSETTINGS");

		ResultSet rs = cs.executeQuery();

		Map<Setting, String> settings = new HashMap<Setting, String>();

		if (rs.next()) {

			settings.put(Setting.LIVE_SOURCE, rs.getString(Setting.LIVE_SOURCE.name()));
			settings.put(Setting.PARSE_TEXTBLOCK, rs.getString(Setting.PARSE_TEXTBLOCK.name()));

		} else {
			// defaults similar to the old restore
			// throw new SQLException("LDGLOBALSETTINGS is not accessible");
			settings.put(Setting.LIVE_SOURCE, "MESG");
			settings.put(Setting.PARSE_TEXTBLOCK, "0");
		}

		return settings;

	}

}
