package com.eastnets.dao.jrnl.mappers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.eastnets.domain.jrnl.DetailsEntity;
import com.eastnets.utils.ApplicationUtils;


public class JournalDetailsMapperOracle implements Serializable, RowMapper<DetailsEntity>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -587300710642408497L;
	final String separator = System.getProperty("line.separator");
	
	@Override
	public DetailsEntity mapRow(final ResultSet rs, final int rowNum)
			throws SQLException {
	
	    	String descStr = ApplicationUtils.convertClob2String(rs.getClob("jrnl_merged_text_lob"));
	
			DetailsEntity detailsEntity = new DetailsEntity();
			StringBuffer descStr1 = new StringBuffer(descStr != null ? descStr.toString().replaceAll("\\\\n", separator ).replace("\\r", "") : " ") ;
			
			detailsEntity.setDescription(descStr1);
			detailsEntity.setDateTime(rs.getTimestamp("jrnl_date_time"));
			detailsEntity.setOperator(rs.getString("jrnl_oper_nickname"));
			detailsEntity.setFunction(rs.getString("jrnl_func_name"));
						
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
			
			detailsEntity.setType( eventTyp );
			
			detailsEntity.setSeverity(rs.getString("jrnl_event_severity"));
			detailsEntity.setApplication(rs.getString("jrnl_appl_serv_name"));
			detailsEntity.setCompName(rs.getString("jrnl_comp_name"));
			detailsEntity.setEventName(rs.getString("jrnl_event_name"));
			detailsEntity.setEventClass(rs.getString("jrnl_event_class"));
			detailsEntity.setEventNumber(new Long(rs.getLong("jrnl_event_num")).toString());
			detailsEntity.setHostname(rs.getString("jrnl_hostname"));
			detailsEntity.setSequenceNumber(new Long(rs.getLong("jrnl_seq_nbr")).toString());
			detailsEntity.setBrowserAddress(rs.getString("jrnl_browser_ip_addr") != null ?rs.getString("jrnl_browser_ip_addr") : "" );
			detailsEntity.setBrowserHostname(rs.getString("jrnl_browser_hostname") != null ? rs.getString("jrnl_browser_hostname") : "");
			
			return detailsEntity;
	}
	
}
