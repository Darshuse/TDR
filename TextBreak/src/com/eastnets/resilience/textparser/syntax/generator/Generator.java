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
package com.eastnets.resilience.textparser.syntax.generator;

import java.sql.SQLException;
import java.util.List;

import com.eastnets.resilience.textparser.db.bean.FullField;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;

/**
 * 
 * @author EHakawati
 * 
 */
public interface Generator {

	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws SyntaxNotFound
	 */
	public List<Entry> getMessageParsingList() throws SQLException, SyntaxNotFound;

	/**
	 * 
	 * @return message description
	 */
	public String getMessageDescription();

	public List<FullField> getFullFields();

}
