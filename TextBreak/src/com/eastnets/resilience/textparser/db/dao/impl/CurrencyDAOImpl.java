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
import java.util.HashMap;
import java.util.Map;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.db.bean.Currency;
import com.eastnets.resilience.textparser.db.dao.CurrencyDAO;
import com.eastnets.resilience.textparser.exception.CurrencyNotFound;
import com.eastnets.resilience.textparser.util.GlobalSettings;

/**
 * We may replace to a property file
 * 
 * @author EHakawati
 * 
 */
public class CurrencyDAOImpl implements CurrencyDAO {

	/**
	 * Small Caching
	 */
	private static final Map<String, Currency> currencies = new HashMap<String, Currency>();

	@Override
	public Currency getCurrency(String code) throws SQLException, CurrencyNotFound {

		if (currencies.containsKey(code)) {
			return currencies.get(code);
		}

		PreparedStatement st = Syntax.getDBSession().prepareStatement("SELECT CURRENCYCODE, CURRENCYNAME, NUMBEROFDIGITS, COUNTRYCODE FROM " + GlobalSettings.getSchemaNameWithDot() + "CU WHERE CURRENCYCODE = ?");

		// bind variables
		st.setString(1, code);

		// execute
		ResultSet rs = st.executeQuery();

		if (rs.next()) {

			Currency currency = new Currency();

			currency.setCode(rs.getString("CURRENCYCODE"));
			currency.setCountryCode(rs.getString("COUNTRYCODE"));
			currency.setName(rs.getString("CURRENCYNAME"));
			currency.setNumberOfDigits(rs.getInt("NUMBEROFDIGITS"));

			currencies.put(code, currency);

			st.close();
			rs.close();

			return currency;

		}

		rs.close();

		throw new CurrencyNotFound(code);

	}

}
