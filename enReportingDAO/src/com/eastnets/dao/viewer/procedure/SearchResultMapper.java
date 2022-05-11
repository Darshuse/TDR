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

package com.eastnets.dao.viewer.procedure;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.domain.viewer.SearchResultEntity;

/**
 * Viewer Search Result Mapper
 * @author EastNets
 * @since September 20, 2012
 */
public class SearchResultMapper implements Serializable, RowMapper<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7828107469566677754L;

	@Override
	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		try
		{
			 ResultSetMetaData rsmd = rs.getMetaData();
			 rsmd.getColumnName(1);//just o make it throw the exception to know its not a count operation 			 
			Integer val= rs.getInt("SUM(VALUE)");
			return val;
		}
		catch(Exception e){//its not a count operation
		}
		try
		{
			Integer val= rs.getInt("");//sql server returns namelss colums as a result of the count operation
			return val;
		}
		catch(Exception e){//its not a count operation
		}
		
		SearchResultEntity enity= new SearchResultEntity();
		enity.setAid( (rs.getInt("aid")));
		
		enity.setMesgUmidl(rs.getInt("mesg_s_umidl"));
		enity.setMesgUmidh(rs.getInt("mesg_s_umidh"));
		enity.setMesgSubFormat(rs.getString("mesg_sub_format"));
		enity.setMesgType(rs.getString("mesg_type"));
		enity.setMesgUumid(rs.getString("mesg_uumid"));
		enity.setMesgSenderX1(rs.getString("mesg_sender_X1"));
		enity.setMesgTrnRef(rs.getString("mesg_trn_ref"));
		
		enity.setMesgCreaDateTime( new Date( rs.getTimestamp("mesg_crea_date_time").getTime() ) );

		enity.setxFinValueDate(rs.getDate("x_fin_value_date"));
		enity.setxFinAmount(rs.getBigDecimal("x_fin_amount"));
		enity.setxFinCcy(rs.getString("x_fin_ccy"));
		enity.setMesgFrmtName(rs.getString("mesg_frmt_name"));
		String status= rs.getString("mesg_status");
		if ( status != null ){
			status= status.trim();
		}			
		enity.setMesgStatus(status);
		enity.setMesgMesgUserGroup(rs.getString("mesg_mesg_user_group"));
		enity.setMesgIdentifier(rs.getString("mesg_identifier"));
		enity.setMesgUumidSuffix(rs.getInt("mesg_uumid_suffix"));
		enity.setInstRpName(rs.getString("inst_rp_name"));
		enity.setInstReceiverX1(rs.getString("inst_receiver_X1"));
		enity.setEmiIAppName(rs.getString("emi_iapp_name"));
		enity.setEmiSessionNbr(rs.getInt("emi_session_nbr"));
		enity.setEmiSequenceNbr(rs.getString("emi_sequence_nbr"));
		enity.setEmiNetworkDeliveryStatus(rs.getString("emi_network_delivery_status"));
		enity.setRecIAppName(rs.getString("rec_iapp_name"));
		enity.setRecSessionNbr(rs.getInt("rec_session_nbr"));
		enity.setRecSequenceNbr(rs.getString("rec_sequence_nbr"));
		enity.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));		
		
		return enity;
	}

}
