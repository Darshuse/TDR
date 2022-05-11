package com.eastnets.dao.loader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;

import com.eastnets.domain.loader.LoaderMessage;


public class LoaderOracleDAOImp extends LoaderDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1612153451095606781L;

	public void addProcessingMQRows(List<LoaderMessage> messagesList, String aid){ 
		Long mesgID = jdbcTemplate.queryForLong("select PRCESSINGMQROWS_ID.NEXTVAL from dual ");
		LOGGGER.debug("Preparing for batch insert");  
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("INSERT INTO LDPROCESSINGMQROWS(ID,CREATION_DATE,AID,PROCESSING_DATA,MESSAGE_HISTORY) VALUES(?,?,?,?,?)");
				statement.setLong(1, mesgID);
				statement.setDate(2,new java.sql.Date(new Date().getTime())); 
				statement.setString(3,aid );
				statement.setString(4,messagesList.get(0).getRowData());
				statement.setString(5, messagesList.get(0).getRowHistory());
			//	statement.setString(6, messagesList.get(0).getMesgType());
				return statement;
			}
		}); 
		messagesList.get(0).setMessageSequenceNo(new BigDecimal(mesgID));
		LOGGGER.debug("Batch update Finished");


		/*	
		 * 
		 * */

	}


	@Override
	public void insertIntoErrorld(String errExeName,String errlevel,String errmodule,String errMsg1,String errMsg2) { 	
		Long mesgID = jdbcTemplate.queryForLong("select LDERRORS_ID.NEXTVAL from dual ");
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ldErrors(ERRID,ErrExeName,Errlevel,Errmodule,ErrMsg2,Errtime)"); 
		builder.append(" VALUES (?,? ,? , ? , ? , SYSDATE )");  
		jdbcTemplate.update(builder.toString(), new Object[] {
				mesgID,
				errExeName,
				errlevel,
				errmodule,
				errMsg1});
	}
	
	


}