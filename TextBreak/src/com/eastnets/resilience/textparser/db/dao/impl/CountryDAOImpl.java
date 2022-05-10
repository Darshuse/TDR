package com.eastnets.resilience.textparser.db.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.db.bean.Country;
import com.eastnets.resilience.textparser.db.dao.CountryDAO;
import com.eastnets.resilience.textparser.exception.CountryNotFound;
import com.eastnets.resilience.textparser.util.GlobalSettings;

public class CountryDAOImpl implements CountryDAO {

	private static final Map<String, Country> countries = new HashMap<String, Country>();

	@Override
	public Country getCountry(String shortCode) throws SQLException, CountryNotFound {

		if (countries.containsKey(shortCode)) {
			return countries.get(shortCode);
		}

		PreparedStatement st = Syntax.getDBSession().prepareStatement("SELECT COUNTRYCODE, COUNTRYNAME FROM " + GlobalSettings.getSchemaNameWithDot() + "CT WHERE COUNTRYCODE = ?");

		// bind variables
		st.setString(1, shortCode);

		// execute
		ResultSet rs = st.executeQuery();

		if (rs.next()) {

			Country country = new Country();

			country.setCountryCode(rs.getString("COUNTRYCODE"));
			country.setCountryName(rs.getString("COUNTRYNAME"));

			countries.put(shortCode, country);

			st.close();
			rs.close();

			return country;

		}

		st.close();
		rs.close();

		throw new CountryNotFound(shortCode);
	}

}
