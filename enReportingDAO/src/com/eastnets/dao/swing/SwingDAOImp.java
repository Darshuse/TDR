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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.swing.procedure.VWSearchEAIProcedure;
import com.eastnets.domain.swing.SwingEntity;

/**
 * Viewer Swing DAO Implementation
 * @author EastNets
 * @since July 11, 2012
 */
public class SwingDAOImp extends DAOBaseImp implements SwingDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3302730201302423324L;
	private VWSearchEAIProcedure vwSearchEAIProcedure;
	
	@Override
	public SwingEntity viewMessageDetails(String statusId, String uniqueIdent){
		Collection<SwingEntity> resultCollection = jdbcTemplate.query(
			    "select eai_unique_identifier,eai_message_id,eai_time_stamp,eai_status,eai_operator,eai_error_code,eai_comment,eai_message_source, fin_mesg_crea_date_time,fin_mesg_status,standard,fin_trn_ref,fin_mesg_type,data,fin_corr,fin_own_dest,fin_swift_IO, eai_time_stamp from rUserFin a,rUserStatus b where b.eai_unique_identifier = '"+uniqueIdent+"' and a.ruserstatus_id = b.id and a.id="+statusId,
			    new RowMapper<SwingEntity>() {
			        @Override
					public SwingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			        	SwingEntity swing = new SwingEntity();
			        	swing.getUserFin().setFinMesgCreaDateTime(rs.getDate("fin_mesg_crea_date_time"));
			        	swing.getUserFin().setFinMesgStatus(rs.getString("fin_mesg_status"));
			        	swing.getUserFin().setFinTrnRef(rs.getString("fin_trn_ref"));
			        	swing.getUserFin().setFinMsgType(rs.getString("fin_mesg_type"));
			        	swing.getUserFin().setFinCorr(rs.getString("fin_corr"));
			        	swing.getUserFin().setFinOwnDist(rs.getString("fin_own_dest"));
			        	swing.getUserFin().setData(rs.getClob("data"));
			        	
			        	swing.getUserStatus().setEaiUniqueIdentifier(rs.getString("eai_unique_identifier"));
			        	swing.getUserStatus().setEaiMessageId(rs.getString("eai_message_id"));
			        	swing.getUserStatus().setEaiTimeStamp(rs.getTimestamp("eai_time_stamp"));
			        	swing.getUserStatus().setEaiStatus(rs.getString("eai_status"));
			        	swing.getUserStatus().setEaiOperator(rs.getString("eai_operator"));
			        	swing.getUserStatus().setEaiErrorCode(rs.getString("eai_error_code"));
			        	swing.getUserStatus().setEaiComment(rs.getString("eai_comment"));
			        	swing.getUserStatus().setEaiMessageSource(rs.getString("eai_message_source"));
			        	
			            return swing;
			        }
			    });
		ArrayList<SwingEntity> resultList = new ArrayList<SwingEntity>();
		resultList.addAll(resultCollection);
		return resultList.get(0);
	}
	
	@Override
	public ArrayList<SwingEntity> doSearch(String subSQLF, String subSQLE) {
		ArrayList<SwingEntity> result = vwSearchEAIProcedure.execute(subSQLF, subSQLE);
		for(SwingEntity r:result){
			System.out.println(r.getUserLink().getUmidl());
			System.out.println(r.getUserFin().getFinMsgType() + " $$");
		}
		return result;
	} 
	


	public VWSearchEAIProcedure getVwSearchEAIProcedure() {
		return vwSearchEAIProcedure;
	}



	public void setVwSearchEAIProcedure(VWSearchEAIProcedure vwSearchEAIProcedure) {
		this.vwSearchEAIProcedure = vwSearchEAIProcedure;
	}
	
}
