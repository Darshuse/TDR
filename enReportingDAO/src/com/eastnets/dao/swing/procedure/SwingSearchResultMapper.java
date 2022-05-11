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

package com.eastnets.dao.swing.procedure;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.domain.swing.SwingEntity;

/**
 * Swing Viewer Search Result Mapper
 * @author EastNets
 * @since July 11, 2012
 */
public class SwingSearchResultMapper implements Serializable, RowMapper<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2170482488225641384L;

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SwingEntity result = new SwingEntity();
        result.getUserStatus().setId(rs.getInt("id"));
        result.getUserStatus().setInternalTimeStamp(rs.getDate("internal_time_stamp"));
        result.getUserStatus().setUnit(rs.getString("unit"));
        result.getUserStatus().setBic(rs.getString("bic"));
        result.getUserStatus().setStandard(rs.getString("standard"));
        result.getUserStatus().setCategory( rs.getString("category"));
        result.getUserStatus().setQualifier( rs.getString("qualifier"));
        result.getUserStatus().setEaiUniqueIdentifier( rs.getString("eai_unique_identifier"));
        result.getUserStatus().setEaiTimeStamp( rs.getTimestamp("eai_time_stamp"));
        result.getUserStatus().setEaiStatus( rs.getString("eai_status"));
        result.getUserStatus().setEaiOperator( rs.getString("eai_operator"));
        result.getUserStatus().setEaiErrorCode( rs.getString("eai_error_code"));
        result.getUserStatus().setEaiComment( rs.getString("eai_comment"));
        result.getUserStatus().setEaiMessageSource( rs.getString("eai_message_source"));
        result.getUserStatus().setSourceName( rs.getString("source_name"));
        result.getUserStatus().setExternIdentifier( rs.getString("extern_identifier"));

        result.getUserFin().setInternalTimeStamp(rs.getDate("internal_time_stamp"));
        result.getUserFin().setFinSwiftIO(rs.getString("fin_swift_IO"));
        result.getUserFin().setFinCorr(rs.getString("fin_corr"));
        result.getUserFin().setFinOwnDist(rs.getString("fin_own_dest"));
        result.getUserFin().setFinMsgType(rs.getString("fin_mesg_type"));
        result.getUserFin().setFinMesgStatus(rs.getString("fin_mesg_status"));
        result.getUserFin().setFinValueDate(rs.getDate("fin_value_date"));
        result.getUserFin().setFinTrnRef(rs.getString("fin_trn_ref"));
        result.getUserFin().setFinAmount(rs.getString("fin_amnt"));
        result.getUserFin().setFinCurr(rs.getString("fin_curr"));
        result.getUserFin().setFinMesgCreaDateTime(rs.getDate("fin_mesg_crea_date_time"));
        result.getUserFin().setFinIsnOsn(rs.getString("fin_isn_osn"));
        result.getUserFin().setFinMesgNetwStatus(rs.getString("fin_mesg_netw_status"));
   
        result.getUserLink().setAid(rs.getInt("aid"));
        result.getUserLink().setUmidh(rs.getInt("umidh"));
        result.getUserLink().setUmidl(rs.getInt("umidl"));

        
        return result;
	}

}
