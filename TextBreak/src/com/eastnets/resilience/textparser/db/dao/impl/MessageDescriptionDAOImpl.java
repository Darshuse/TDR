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
package com.eastnets.resilience.textparser.db.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.db.bean.MessageDescription;
import com.eastnets.resilience.textparser.db.dao.MessageDescriptionDAO;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.util.GlobalSettings;

/**
 * Get message type basic idx and description Generic implementation valid for both oracle and mssql
 * 
 * @author EHakawati
 * 
 */
public class MessageDescriptionDAOImpl implements MessageDescriptionDAO {

	/**
	 * getMessageDescription implementation
	 * 
	 * @param version
	 * @param mesgType
	 * @return MessageDescription
	 * @throws SQLException
	 * @throws SyntaxNotFound
	 */
	@Override
	public MessageDescription getMessageDescription(String version, String mesgType) throws SQLException, SyntaxNotFound {

		PreparedStatement st = Syntax.getDBSession().prepareStatement("SELECT stxMessage.version_idx, stxMessage.idx, stxMessage.description " + "FROM " + GlobalSettings.getSchemaNameWithDot() + "stxMessage, " + GlobalSettings.getSchemaNameWithDot()
				+ "stxVersion " + "WHERE stxVersion.idx = stxMessage.version_idx " + "AND (stxVersion.Version = ? and stxMessage.Type = ? )");

		// bind variables
		st.setString(1, version);
		st.setString(2, mesgType);

		// execute
		ResultSet rs = st.executeQuery();

		// get entry
		if (rs.next()) {

			MessageDescription msgDesc = new MessageDescription();

			// fill object
			msgDesc.setVersionIdx(rs.getInt("version_idx"));
			msgDesc.setMessageIdx(rs.getInt("idx"));
			msgDesc.setDescription(rs.getString("description"));

			st.close();
			rs.close();
			// return MessageDescription
			return msgDesc;
		}
		st.close();
		rs.close();

		// if not found
		throw new SyntaxNotFound(version, mesgType);

	}

}
