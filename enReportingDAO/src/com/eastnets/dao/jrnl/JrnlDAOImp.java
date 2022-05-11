package com.eastnets.dao.jrnl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import com.eastnets.dao.DAOBaseImp;

/**
 * Journal DAO Implementation 
 * @author EastNets
 * @since January 31, 2013
 */
public abstract class JrnlDAOImp extends DAOBaseImp implements JrnlDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3635887285137512193L;

	@Override
	public List<String> fillLookupEventClass() throws Exception {

		String queryString = "select JRNL_EVENT_CLASS from LDHELPERJRNLCLASS";
		
		List<String> eventClassList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_EVENT_CLASS");
			}
		});
		return eventClassList;
	}
	
	@Override
	public List<String> fillLookupEventSeverity() throws Exception {

		String queryString = "select JRNL_EVENT_SEVERITY from LDHELPERJRNLEVENTSEVERITY";
		
		List<String> eventSeverityList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_EVENT_SEVERITY");
			}
		});
		return eventSeverityList;
	}
	 
	@Override
	public List<String> fillLookupApplicationService() throws Exception {

		String queryString = "select JRNL_APPL_SERV_NAME from LDHELPERJRNLAPPLICATION" ;
		
		List<String> applicationServiceList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_APPL_SERV_NAME");
			}
		});
		return applicationServiceList;
	}
	
	@Override
	public List<String> fillLookupComponent() throws Exception {

		String queryString = "select JRNL_COMP_NAME from LDHELPERJRNLCOMP" ;
		
		List<String> componentList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_COMP_NAME");
			}
		});
		componentList.add(0,"All");
		return componentList;
	}
	
	@Override
	public List<String> fillLookupEventType() throws Exception {

		String queryString = "select JRNL_EVENT_TYPE from LDHELPERJRNLEVENTTYPE" ;
		
		List<String> eventTypeList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_EVENT_TYPE");
			}
		});
		eventTypeList.add(0,"All");
		return eventTypeList;
	}
	
	@Override
	public List<String> fillLookupHostname() throws Exception {

		String queryString = "select JRNL_HOSTNAME from LDHELPERJRNLHOSTNAME" ;
		
		List<String> hostnameList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_HOSTNAME");
			}
		});
		hostnameList.add(0,"All");
		return hostnameList;
	}
	
	@Override
	public List<String> fillLookupAlarmType() throws Exception {

		String queryString = "select JRNL_ALARM_TYPE from LDHELPERJRNLALARMTYPE" ;
		
		List<String> alarmTypeList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return rs.getString("JRNL_ALARM_TYPE");
			}
		});
		alarmTypeList.add(0,"All");
		return alarmTypeList;
	}
	
}
