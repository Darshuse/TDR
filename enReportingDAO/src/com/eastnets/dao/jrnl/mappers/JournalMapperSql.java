package com.eastnets.dao.jrnl.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.eastnets.domain.jrnl.SearchResultEntity;


/**
 * Row mapper for spring
 * @author EastNets
 * @since December 24, 2012
 */
public class JournalMapperSql implements Serializable, RowMapper<SearchResultEntity> {

		/**
	 * 
	 */
	private static final long serialVersionUID = -5614672752186066981L;
		final String separator = System.getProperty("line.separator");
	
		@Override
		public SearchResultEntity mapRow(final ResultSet rs, final int rowNum)
				throws SQLException {
					
			SearchResultEntity journalEntity = new SearchResultEntity();
			
			journalEntity.setAid(rs.getInt("aid"));
			
			journalEntity.setAllianceInstance(rs.getString("alliance_instance"));
			journalEntity.setRevDateTime(rs.getLong("jrnl_rev_date_time"));
			journalEntity.setSequenceNumber(rs.getLong("jrnl_seq_nbr"));
			journalEntity.setDateTime(rs.getTimestamp("jrnl_date_time"));
			journalEntity.setEventServerity(rs.getString("jrnl_event_severity"));
			journalEntity.setApplicationName(rs.getString("jrnl_appl_serv_name"));
			journalEntity.setEventName(rs.getString("jrnl_event_name"));
			journalEntity.setJournalDisplayName(rs.getString("jrnl_display_text"));
			journalEntity.setEventClass(rs.getString("jrnl_event_class"));
			journalEntity.setEventIsSecurity(rs.getInt("jrnl_event_is_security"));
			journalEntity.setEventIsConfigMgmt(rs.getInt("jrnl_is_config_mgmt"));
			journalEntity.setCompName(rs.getString("jrnl_comp_name") != null ? rs.getString("jrnl_comp_name") : "" );
		    journalEntity.setOperatorNickName(rs.getString("jrnl_oper_nickname") != null ? rs.getString("jrnl_oper_nickname") : "");	

		    String eventTyp = returnEventType(rs);				
			journalEntity.setEventType(eventTyp);
			
		    journalEntity.setEventNumber(rs.getLong("jrnl_event_num"));
		    journalEntity.setHostName(rs.getString("jrnl_hostname") != null ? rs.getString("jrnl_hostname") : "" );
		    journalEntity.setFunction(rs.getString("jrnl_func_name") != null ? rs.getString("jrnl_func_name") : "" );
		    
		    String str = rs.getString("jrnl_merged_text");
		    String descStr1 = str != null ? str.toString().replaceAll("\\\\n", separator ).replace("\\r", "") : " " ;
		    journalEntity.setDescription(descStr1);
		    
		    return journalEntity;
		    
		}
		
		private String returnEventType(final ResultSet rs) throws SQLException {
					
			String eventTyp= "Normal";
			boolean isAlarm= rs.getInt("jrnl_event_is_alarm") == 1;
			boolean isSecurity= rs.getInt("jrnl_event_is_security") == 1;
			boolean isCfgMgmt = rs.getInt("jrnl_is_config_mgmt") == 1;
	
			if (isCfgMgmt && isAlarm)
			{
				eventTyp= "Config Mgmt & Alarm Event";
			}
			else if ( isCfgMgmt &&  isSecurity ){
				eventTyp= "Config Mgmt & Security Event";
			}
			else if ( isAlarm &&  isSecurity ){
				eventTyp= "Security & Alarm Event";
			}
			else if( isAlarm ){
				eventTyp= "Alarm Event";
			}
			else if( isSecurity ){
				eventTyp= "Security Event";
			}
			else if (isCfgMgmt)
			{
				eventTyp= "Config Mgmt Event";
			}
			return eventTyp;
		}
}

