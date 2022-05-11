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

package com.eastnets.dao;

/**
 * DAO Interface, contains all common methods in all DAOs
 * @author EastNets
 * @since July 11, 2012
 */
public interface DAO {
	
	/**
	 * Get used database type
	 * @return integer
	 */
	public int getDbType();
}
