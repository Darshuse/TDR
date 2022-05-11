package com.eastnets.dao.loader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.eastnets.domain.loader.LoaderMessage;

import com.eastnets.domain.loader.LoaderMessage;


public class LoaderSqlDAOImp extends LoaderDAOImp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2382423053261516741L;

	@Override
	public void addProcessingMQRows(List<LoaderMessage> messagesList, String aid){
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		LOGGGER.debug("Preparing for batch insert"); 
		jdbcTemplate.update(new PreparedStatementCreator() {
		    @Override
		    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		        PreparedStatement statement = con.prepareStatement("INSERT INTO LDPROCESSINGMQROWS(CREATION_DATE,AID,PROCESSING_DATA,MESSAGE_HISTORY) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		        statement.setDate(1,new java.sql.Date(new Date().getTime())); 
		        statement.setString(2,aid );
		        statement.setString(3,messagesList.get(0).getRowData() );
		        statement.setString(4, messagesList.get(0).getRowHistory());
		        return statement;
		    }
		}, holder);
		long primaryKey = holder.getKey().longValue();
		messagesList.get(0).setMessageSequenceNo(new BigDecimal(primaryKey));
		
		LOGGGER.debug("Batch update Finished");
	} 
	
	
 
	@Override
	public void insertIntoErrorld(String errExeName,String errlevel,String errmodule,String errMsg1,String errMsg2) { 
		String insertQuery="INSERT INTO ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg2) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, new Object[] { errExeName,new Date(),errlevel, errmodule,errMsg1} );   
	}
	
	
 
	
	
	
}