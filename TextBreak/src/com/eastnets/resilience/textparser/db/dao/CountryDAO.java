package com.eastnets.resilience.textparser.db.dao;

import java.sql.SQLException;

import com.eastnets.resilience.textparser.db.bean.Country;
import com.eastnets.resilience.textparser.exception.CountryNotFound;

public interface CountryDAO {

	public Country getCountry(String shortCode) throws SQLException, CountryNotFound;
}
