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

import com.eastnets.resilience.textparser.db.bean.Currency;
import com.eastnets.resilience.textparser.exception.CurrencyNotFound;

/**
 * 
 * @author EHakawati
 * 
 */
public interface CurrencyDAO {

	public Currency getCurrency(String shortCode) throws SQLException, CurrencyNotFound;
}
