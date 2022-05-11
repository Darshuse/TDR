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

package com.eastnets.dao.swing;

import java.util.ArrayList;

import com.eastnets.dao.DAO;
import com.eastnets.domain.swing.SwingEntity;

/**
 * Viewer Swing DAO Interface
 * @author EastNets
 * @since July 11, 2012
 */
public interface SwingDAO extends DAO {
	public ArrayList<SwingEntity> doSearch(String subSQLF, String subSQLE);
	public SwingEntity viewMessageDetails(String statusId, String uniqueIdent);
	
}
