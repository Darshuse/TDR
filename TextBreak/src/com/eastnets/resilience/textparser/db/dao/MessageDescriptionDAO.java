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

package com.eastnets.resilience.textparser.db.dao;

import java.sql.SQLException;

import com.eastnets.resilience.textparser.db.bean.MessageDescription;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;

/**
 * MessageDescription DAO
 * 
 * @author EHakawati
 * 
 */
public interface MessageDescriptionDAO {

	/**
	 * 
	 * @param version
	 * @param mesgType
	 * @return
	 * @throws SQLException
	 * @throws SyntaxNotFound
	 */
	public MessageDescription getMessageDescription(String version, String mesgType) throws SQLException,
			SyntaxNotFound;

}
