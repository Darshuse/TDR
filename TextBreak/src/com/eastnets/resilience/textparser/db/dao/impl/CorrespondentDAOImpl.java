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

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.db.bean.Correspondent;
import com.eastnets.resilience.textparser.db.dao.CorrespondentDAO;
import com.eastnets.resilience.textparser.exception.CorrespondentNotFound;
import com.eastnets.resilience.textparser.util.GlobalSettings;

public class CorrespondentDAOImpl implements CorrespondentDAO {

	@Override
	public Correspondent getCorrespondent(String bicCode, Timestamp mesgCreaDateTime) throws SQLException, CorrespondentNotFound {
		CallableStatement cs = Syntax.getDBSession().prepareCall("{ call " + GlobalSettings.getSchemaNameWithDot() + "vwGetCorrInfo_2 (?,?,?,?,?,?,?,?,?,?,?,?,?) }");

		try {

			cs.setString(1, "CORR_TYPE_INSTITUTION");
			cs.setString(2, fixCorrespondent(bicCode));
			cs.setString(3, "");
			cs.setString(4, "");
			cs.setString(5, "");
			cs.setTimestamp(6, mesgCreaDateTime);

			cs.registerOutParameter(7, java.sql.Types.CHAR);
			cs.registerOutParameter(8, java.sql.Types.CHAR);
			cs.registerOutParameter(9, java.sql.Types.CHAR);
			cs.registerOutParameter(10, java.sql.Types.CHAR);
			cs.registerOutParameter(11, java.sql.Types.CHAR);
			cs.registerOutParameter(12, java.sql.Types.CHAR);
			cs.registerOutParameter(13, java.sql.Types.INTEGER);

			cs.execute();

			Correspondent correspondent = new Correspondent();

			correspondent.setInstitutionName(cs.getString(7));
			correspondent.setBranchInfo(cs.getString(8));
			correspondent.setCityName(cs.getString(9));
			correspondent.setCountryCode(cs.getString(10));
			correspondent.setCountryName(cs.getString(11));
			correspondent.setLocation(cs.getString(12));
			correspondent.setReturnStatus(cs.getInt(13));

			cs.close();

			return correspondent;

		} catch (Exception ex) {
			throw new CorrespondentNotFound(bicCode);
		} finally {
			cs.close();
		}

	}

	private String fixCorrespondent(String bicCode) {
		if (bicCode.length() == 8) {
			return bicCode + "XXX";
		}
		return bicCode;
	}

}
