package com.eastnets.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.eastnets.domain.admin.Action;
import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.domain.watchdog.SyntaxEntryField;
import com.eastnets.utils.ApplicationUtils;

public class CommonSqlDAOImp extends CommonDAOImp{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8768621667645670075L;

	public List<SyntaxEntryField> getFiledsValue(Long messgaeType, Long versionIdx)
	{

		String selectQuery = String.format("select distinct rtrim(substring(tag, 2, 3)) + case entry_option when '0' then '' else entry_option end as fieldValue, code, code_id from stxentryfieldview where type = %d and version_idx = %d",messgaeType,versionIdx);



		List<SyntaxEntryField> syntaxEntryFieldsList =  jdbcTemplate.query(selectQuery,new RowMapper<SyntaxEntryField> () {
			@Override
			public SyntaxEntryField mapRow(ResultSet rs, int rowNum) throws SQLException {

				SyntaxEntryField  syntaxEntryField = new SyntaxEntryField();

				String value = rs.getString("fieldValue");

				value = (value == null) ? "":value;

				syntaxEntryField.setFieldValue(value);
				syntaxEntryField.setCode( rs.getLong("code"));
				syntaxEntryField.setCodeId( rs.getLong("code_id"));
				if(value.length() == 3) {
					syntaxEntryField.setFieldOption(value.substring(2));
				}

				return syntaxEntryField;
			}
		});

		return syntaxEntryFieldsList;

	}

	@Override
	public void auditDAO(String loginName,String programName,String event,String action, List<AuditLogDetails> auditLogDetailsList,String ipAddress)
	{			
		String formatedDate = ApplicationUtils.formatDateTime(new Date());
		GeneratedKeyHolder keyHolder  = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
		    @Override
		    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		        PreparedStatement statement = con.prepareStatement("INSERT INTO dbo.rAuditLog ( LOGINAME, PROGRAM_NAME, EVENT, ACTION,ipAddress,TIMESTAMP)  VALUES ( ?, ?, ? ,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		        statement.setString(1,loginName); 
		        statement.setString(2,programName);
		        statement.setString(3,event);
		        statement.setString(4,action);
		        statement.setString(5,ipAddress);
		        statement.setString(6,formatedDate);
		        return statement;
		    }
		}, keyHolder);
		long id= keyHolder .getKey().longValue();

		if(auditLogDetailsList != null) { 
			auditDetailsDAO(loginName, programName, event, action, formatedDate,auditLogDetailsList,ipAddress,id);
		}
	}

	public void auditDetailsDAO(String loginName,String programName,String event,String action ,String formatedDate, List<AuditLogDetails> auditLogDetailsList,String ipAddress,Long id )
	{		

		String insertStatemnt = "INSERT INTO dbo.rAuditLogDetails (LOGINAME, PROGRAM_NAME, EVENT, ACTION,FIELD_NAME,OLD_VALUE,NEW_VALUE,IPADDRESS,FIELD_TYPE,TIMESTAMP,id) VALUES (? ,? , ? ,? ,?,?,?,?,?, ?,?)";

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				AuditLogDetails	auditLogDetailsBean = auditLogDetailsList.get(index);
				preparedStatement.setString(1, loginName);
				preparedStatement.setString(2, programName);
				preparedStatement.setString(3, event);
				preparedStatement.setString(4, action);
				preparedStatement.setString(5, auditLogDetailsBean.getFieldName());
				preparedStatement.setString(6, auditLogDetailsBean.getOldValue());
				preparedStatement.setString(7, auditLogDetailsBean.getNewValue());
				preparedStatement.setString(8,ipAddress);
				preparedStatement.setString(9,auditLogDetailsBean.getFieldType());
				preparedStatement.setString(10,formatedDate);
				preparedStatement.setLong(11,id);
			}	
			@Override
			public int getBatchSize() {
				return auditLogDetailsList.size();
			}
		});
	}
public static void main(String[] args) {
	 
	
	
	java.util.Calendar cal = Calendar.getInstance();  // your util date
	cal.setTime(new java.util.Date());
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);    
	java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
	System.out.println(sqlDate);
}

	@Override
	public String getDataBaseInfo() throws Exception {

		String queryString = "Select @@version";
		List<String> bannerList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		return bannerList.get(0);
	}

	@Override
	public String getProductVersion() throws Exception {

		String queryString = "select top 1 Major , minor, revision from SDBVERSION order by INSTALLATIONDATE desc";
		List<String> majorsList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("MAJOR") + "." + rs.getLong("minor") + "." + rs.getLong("revision");
			}
		});
		return majorsList.get(0);
	}


	@Override
	public List<Action> getAvailableActions(Integer moduleId) {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT sObjects.ID, ");
		selectQuery.append(" sObjects.ProgramID, ");
		selectQuery.append(" sObjects.Name ");
		selectQuery.append(" FROM sObjects ");
		selectQuery.append(" WHERE sObjects.ProgramId =? ");
		selectQuery.append(" ORDER BY sObjects.ID");

		Object [] param ={moduleId};
		List<Action>  actions = jdbcTemplate.query(selectQuery.toString(),param, new RowMapper<Action> () {
			public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
				Action action = new Action();
				action.setActionId(rs.getLong("ID"));
				action.setActionName(rs.getString("NAME"));
				action.setModuleId(rs.getInt("PROGRAMID"));
				return action;
			}
		});
		return actions;
	}

}
