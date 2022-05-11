package com.eastnets.dao.jrnl;

import java.util.List;

import com.eastnets.dao.common.Constants;
import com.eastnets.dao.jrnl.mappers.JournalDetailsMapperOracle;
import com.eastnets.dao.jrnl.mappers.JournalMapperOracle;
import com.eastnets.domain.jrnl.DetailsEntity;
import com.eastnets.domain.jrnl.JournalSearchProcedureParameters;
import com.eastnets.domain.jrnl.JournalShowDetailsProcedureParameters;
import com.eastnets.domain.jrnl.SearchResultEntity;

/**
 * Journal DAO Implementation
 * 
 * @author EastNets
 * @since December 24, 2012
 */
public class JrnlOracleDAOImp extends JrnlDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2592991426027987492L;

	@Override
	public Object search(JournalSearchProcedureParameters inParams) throws Exception {

		String whereString = "";
		String orderString = " order by j.jrnl_date_time desc";
		String queryString = "select j.aid,j.jrnl_rev_date_time, j.jrnl_seq_nbr, j.jrnl_date_time, " + " j.jrnl_event_severity, j.jrnl_appl_serv_name, j.jrnl_event_name, j.jrnl_display_text, "
				+ " j.jrnl_event_class, j.jrnl_event_is_security,j.jrnl_comp_name,j.jrnl_oper_nickname, " + " j.jrnl_event_is_alarm, jrnl_event_is_security, j.jrnl_event_num, j.jrnl_func_name, j.jrnl_hostname, j.jrnl_is_config_mgmt, "
				+ " j.jrnl_merged_text_lob , j.jrnl_browser_hostname, j.jrnl_browser_ip_addr, l.alliance_instance from rsJrnl j inner join ldSettings l on l.aid = j.aid";

		if (!inParams.getOperator().trim().equalsIgnoreCase("all")) {
			whereString = whereString.concat(" and j.JRNL_OPER_NICKNAME like '" + inParams.getOperator().trim().replace("'", "''") + "'");
		}

		if (!inParams.getComponent().trim().equalsIgnoreCase("all")) {
			whereString = whereString.concat(" and j.JRNL_COMP_NAME = '" + inParams.getComponent().trim().replace("'", "''") + "'");
		}

		String searchText = inParams.getSearchText().trim();
		if (!searchText.equalsIgnoreCase("all")) {
			searchText = searchText.replace("'", "''");
			searchText = searchText.replace("\r\n", "\n");
			searchText = searchText.replace("\r", "\n");
			searchText = searchText.replace("\n", "\\n");

			whereString = whereString.concat(" and jrnl_merged_text_lob like '%" + searchText + "%' ");
		}

		if (!inParams.getFunction().trim().equalsIgnoreCase("all")) {
			whereString = whereString.concat(" and j.JRNL_FUNC_NAME like '" + inParams.getFunction().trim().replace("'", "''") + "'");
		}

		if (!inParams.getEventType().trim().equalsIgnoreCase("all")) {

			if (inParams.getEventType().trim().equalsIgnoreCase("Alarm Events"))
				whereString = whereString.concat(" and j.jrnl_event_is_alarm = 1 ");
			else if (inParams.getEventType().trim().equalsIgnoreCase("Security Events"))
				whereString = whereString.concat(" and j.jrnl_event_is_security = 1 ");
			else if (inParams.getEventType().trim().equalsIgnoreCase("Security Alarm Events"))
				whereString = whereString.concat(" and j.jrnl_event_is_security = 1 and j.jrnl_event_is_alarm = 1 ");
			else if (inParams.getEventType().trim().equalsIgnoreCase("Config Mgmt Events"))
				whereString = whereString.concat(" and j.jrnl_is_config_mgmt = 1 ");

		}

		if (!inParams.getHostname().trim().equalsIgnoreCase("all")) {
			whereString = whereString.concat(" and j.JRNL_HOSTNAME = '" + inParams.getHostname().trim().replace("'", "''") + "'");
		}

		if (!inParams.getAlarmType().trim().equalsIgnoreCase("all")) {
			whereString = whereString.concat(" and upper(j.JRNL_ALARM_STATUS) in (" + inParams.getAlarmType().trim() + ")");
		}

		if (inParams.getEventNumber() != -1) {
			whereString = whereString.concat(" and j.JRNL_EVENT_NUM = " + inParams.getEventNumber());
		}

		if (!inParams.getEventClasses().isEmpty()) {
			whereString = whereString.concat(" and j.JRNL_EVENT_CLASS in (" + convertListToString(inParams.getEventClasses()) + ")");
		}

		if (!inParams.getEventSeverities().isEmpty()) {
			whereString = whereString.concat(" and REPLACE(j.JRNL_EVENT_SEVERITY,'ADK_','') in (" + convertListToString(inParams.getEventSeverities()).toUpperCase() + ")");
		}

		if (!inParams.getAppsServices().isEmpty()) {
			whereString = whereString.concat(" and j.JRNL_APPL_SERV_NAME in (" + convertListToString(inParams.getAppsServices()) + ")");
		}

		whereString = whereString
				.concat(" and j.jrnl_date_time between TO_DATE('" + inParams.getFromDate() + "','" + Constants.ORACLE_DATE_TIME_PATTERN2 + "') and TO_DATE('" + inParams.getToDate() + "','" + Constants.ORACLE_DATE_TIME_PATTERN2 + "')");

		if (inParams.getListMax() > 0) {
			whereString = whereString.concat(" and rownum <= " + inParams.getListMax());
		}

		if (!whereString.equals(""))
			whereString = whereString.replaceFirst("and", "where");

		List<SearchResultEntity> searchResultList = ((List<SearchResultEntity>) jdbcTemplate.query(queryString + whereString + orderString, new JournalMapperOracle()));

		return searchResultList;
	}

	@Override
	public DetailsEntity showDetails(JournalShowDetailsProcedureParameters inParams) throws Exception {

		String whereString = " where";
		String queryString = "select jrnl_merged_text_lob, jrnl_date_time, jrnl_oper_nickname, " + " jrnl_func_name, jrnl_event_is_alarm, jrnl_event_is_security, jrnl_is_config_mgmt, jrnl_event_severity, jrnl_appl_serv_name, "
				+ " jrnl_event_name, jrnl_event_num, jrnl_event_class, jrnl_hostname, jrnl_seq_nbr, " + " jrnl_comp_name, jrnl_browser_hostname,jrnl_browser_ip_addr from rsJrnl";

		whereString += " AID = " + inParams.getAid();
		whereString += " and JRNL_REV_DATE_TIME = " + inParams.getRevisionDateTime();
		whereString += " and JRNL_SEQ_NBR = " + inParams.getSequenceNumber();

		whereString += " and JRNL_DATE_TIME = " + getDbPortabilityHandler().getFormattedDateWithNoBinding(inParams.getDateTime(), true);

		List<DetailsEntity> detailsEntity = ((List<DetailsEntity>) jdbcTemplate.query(queryString + whereString, new JournalDetailsMapperOracle()));

		return detailsEntity.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String convertListToString(List list) {

		String listAsString = "";
		Object values[] = list.toArray(new String[list.size()]);

		for (Object curValue : values) {
			listAsString += "'" + curValue.toString().replace("'", "''") + "',";
		}

		return listAsString.substring(0, listAsString.length() - 1);
	}

}
