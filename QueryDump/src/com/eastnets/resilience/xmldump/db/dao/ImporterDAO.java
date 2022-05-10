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
package com.eastnets.resilience.xmldump.db.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Observable;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Base database imported class
 * 
 * @author EHakawati
 * 
 */
public abstract class ImporterDAO extends Observable {

	protected static final int REQUEST_TYPE = 1;

	private int restoreSet;

	protected void setInt(CallableStatement statement, int index, Integer value) throws SQLException {
		if (value == null) {
			statement.setNull(index, java.sql.Types.INTEGER);
		} else {
			statement.setInt(index, value);
		}
	}

	protected void setBoolean(CallableStatement statement, int index, Boolean value) throws SQLException {
		if (value == null) {
			statement.setNull(index, java.sql.Types.INTEGER);
		} else {
			statement.setInt(index, value ? 1 : 0);
		}
	}

	protected void setEnumString(CallableStatement statement, int index, Enum<?> value) throws SQLException {
		if (value == null) {
			statement.setNull(index, java.sql.Types.VARCHAR);
		} else {
			statement.setString(index, value.toString());
		}
	}

	protected void setTimestamp(CallableStatement statement, int index, XMLGregorianCalendar calender)
			throws SQLException {
		if (calender == null) {
			statement.setNull(index, java.sql.Types.DATE);
		} else {
			statement.setTimestamp(index, new Timestamp(calender.toGregorianCalendar().getTime().getTime()),
					calender.toGregorianCalendar());
		}
	}

	public int getRestoreSet() {
		return restoreSet;
	}

	public void setRestoreSet(int restoreSet) {
		this.restoreSet = restoreSet;
	}
}
