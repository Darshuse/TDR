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
import java.util.List;

import com.eastnets.resilience.textparser.db.bean.FullField;

/**
 * SyntaxAllFieldsView DAO
 * 
 * @author EHakawati
 */
public interface FullFieldsDAO {

	/**
	 * get the a list of SyntaxAllFieldsView
	 * 
	 * @param vesrion
	 * @param mesgType
	 * @return
	 * @throws SQLException
	 */
	public List<FullField> getSyntaxAllFieldsViews(int i, int j) throws SQLException;

	public List<FullField> getAllFields(int versionIdx, int typeIdx) throws SQLException;

}
