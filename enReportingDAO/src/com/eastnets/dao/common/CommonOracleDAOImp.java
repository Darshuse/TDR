package com.eastnets.dao.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.utils.ApplicationUtils;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.monitoring.AuditLogDetails;
import com.eastnets.domain.viewer.Country;
import com.eastnets.domain.viewer.MessageTypeAvi;
import com.eastnets.domain.watchdog.SyntaxEntryField;

public class CommonOracleDAOImp extends CommonDAOImp {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 95293485000250762L;

	public List<SyntaxEntryField> getFiledsValue(Long messgaeType, Long versionIdx)
	{
		
		String selectQuery = String.format("select distinct rtrim(substr(tag, 2)) || decode(ENTRY_OPTION, '0', '', ENTRY_OPTION) as fieldValue, code, code_id	from stxentryfieldview where type = %d and version_idx =%d",messgaeType,versionIdx);
		
		List<SyntaxEntryField> syntaxEntryFieldsList =  jdbcTemplate.query(selectQuery,new RowMapper<SyntaxEntryField> () {
			@Override
			public SyntaxEntryField mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				SyntaxEntryField  syntaxEntryField = new SyntaxEntryField();
				
				String value = rs.getString("fieldValue");
				
				value = (value == null) ? "":value;
				syntaxEntryField.setFieldValue( value);
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
		Long id = jdbcTemplate.queryForLong("select side.rAuditLog_ID.nextval from dual");
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO side.rAuditLog (ID, LOGINAME, PROGRAM_NAME, EVENT, ACTION,IPADDRESS, TIMESTAMP ) VALUES ( ");
		builder.append("?,");
		builder.append(" ?  , ? ,? , ? , ? ,");
		builder.append("TO_DATE(?,'");
		builder.append(Constants.ORACLE_DATE_TIME_PATTERN);
		builder.append("')");
		builder.append(")");
		

		jdbcTemplate.update(builder.toString(), new Object[] { 
				id,
				loginName,
				programName,
				event,
				action,
				ipAddress,
				formatedDate});
		
		if(auditLogDetailsList != null) { 
			auditDetailsDAO(loginName, programName, event, action, formatedDate,auditLogDetailsList,ipAddress,id);
		}
		
	}
		
	
	public void auditDetailsDAO(String loginName,String programName,String event,String action ,String formatedDate, List<AuditLogDetails> auditLogDetailsList,String ipAddress,Long id)
	{		
		
		String insertStatemnt = "INSERT INTO side.rAuditLogDetails (LOGINAME, PROGRAM_NAME, EVENT, ACTION,FIELD_NAME,OLD_VALUE,NEW_VALUE,IPADDRESS,FIELD_TYPE,TIMESTAMP,id) VALUES (? ,? ,?, ? ,? ,?,?,?,?,TO_DATE(?,'"+
				Constants.ORACLE_DATE_TIME_PATTERN+"'),?)";
				
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
	
	@Override
	public String getDataBaseInfo() throws Exception {

		String queryString = "select BANNER from v$version where rownum < 2";
		List<String> bannerList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("BANNER");
			}
		});
		return bannerList.get(0);
	}
	
	@Override
	public String getProductVersion() throws Exception {

		String queryString = "select Major , minor, revision from (select Major , minor, revision from SDBVERSION  order by INSTALLATIONDATE desc) where  rownum  = 1";
		List<String> majorsList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("MAJOR") + "." + rs.getLong("minor") + "." + rs.getLong("revision") ;
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
