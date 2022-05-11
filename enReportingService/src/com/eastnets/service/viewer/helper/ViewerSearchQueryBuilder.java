package com.eastnets.service.viewer.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.Config;
import com.eastnets.domain.viewer.AdvancedSearchCriteria;
import com.eastnets.domain.viewer.FieldSearchInfo;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.service.security.data.SecurityDataService;

public class ViewerSearchQueryBuilder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3076336208017097770L;

	public enum QueryType {
		Search, Count;
	}

	private final static String SEPERATOR = " ";

	// data members injected by spring
	protected DBPortabilityHandler dbPortabilityHandler;
	private ApplicationFeatures applicationFeatures;
	private Config config;

	protected String fromTables;
	protected String joinsTables;
	protected String fromTablesUnion;
	protected String whereCondition;
	protected String whereConditionUnion;
	String unicodeCharacter = "";

	private boolean textFieldConditionWithOr = false;

	private SecurityDataService securityDataService;

	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	private void clearData() {
		fromTables = "";
		joinsTables = "";
		fromTablesUnion = "";
		whereCondition = "";
		whereConditionUnion = "";
	}

	/**
	 * generates a search query based on the passed data
	 * 
	 * @param params
	 * @param startFrom
	 * @param listMax
	 * @param timeZoneOffset
	 * @param fieldSearch
	 * @return generated query to be executed
	 */
	public synchronized String generateQuery(QueryType queryTupe, ViewerSearchParam params, String listFilter, int listMax, int timeZoneOffset, List<FieldSearchInfo> fieldSearch, String userName, boolean addColums, boolean showInternalMessages,
			int textDecompostionType, boolean caseSensitive, boolean includeSysMessages, boolean enableUnicodeSearch, boolean enableGpiSearch, Long groupId, int pageNumber, boolean webService) {
		userName = userName.replace("'", "''");
		// clear internal data members to generate new query
		clearData();
		params.setQueryVariablesBinding(new ArrayList<Object>());
		boolean xmlSearchUsed = (params.getXmlConditionsMetadata() != null && !params.getXmlConditionsMetadata().isEmpty()) ? true : false;
		// params.getQueryVariablesBinding(); // fill the internal data members
		prepareQuery(params, fieldSearch, timeZoneOffset, userName, addColums, showInternalMessages, textDecompostionType, caseSensitive, includeSysMessages, xmlSearchUsed, enableUnicodeSearch, enableGpiSearch, groupId);

		if (queryTupe == QueryType.Count) {
			return countQuery(listFilter, listMax, timeZoneOffset);
		}
		// generate the actual query
		return getQuery(listFilter, listMax, timeZoneOffset, pageNumber, webService);
	}

	private String countQuery(String listFilter, int listMax, int timeZoneOffset) {
		// non decomposed message
		String v_SQL = "SELECT COUNT(*) from (( select distinct m.aid, m.MESG_S_UMIDH, m.MESG_S_UMIDL, m.mesg_crea_date_time FROM rMesg m " + joinsTables + whereCondition + ")";
		// decomposed message
		if (StringUtils.isNotEmpty(fromTablesUnion)) {
			v_SQL += " UNION ( select distinct m.aid, m.MESG_S_UMIDH, m.MESG_S_UMIDL, m.mesg_crea_date_time FROM rMesg m " + fromTablesUnion + whereConditionUnion + ")";
		}
		v_SQL += ") AA";
		return v_SQL;
	}

	private String getQuery(String listFilter, int listMax, int timeZoneOffset, int pageNumber, boolean webService) {
		String sqlQuery = "";
		boolean oracleDB = dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE;

		String tableLock = "";
		if (!oracleDB) {
			tableLock = " WITH (NOLOCK) ";
		}

		String joinsWithoutOptimization = "";
		if (config.getSearchOptimizer() == 0) {
			joinsWithoutOptimization = " left outer join (rInst x1 left outer join rAppe y1 on" + SEPERATOR + " x1.Aid = y1.Aid And" + SEPERATOR + " x1.inst_s_umidl = y1.appe_s_umidl And" + SEPERATOR + "	x1.inst_s_umidh = y1.appe_s_umidh And"
					+ SEPERATOR + addPartitioned(" x1.X_CREA_DATE_TIME_MESG = y1.X_CREA_DATE_TIME_MESG And ") + SEPERATOR + "	x1.inst_num = y1.appe_inst_num And" + SEPERATOR + "	x1.x_last_emi_appe_date_time = y1.appe_date_time And" + SEPERATOR
					+ "	x1.x_last_emi_appe_seq_nbr = y1.appe_seq_nbr" + SEPERATOR + " left outer join rAppe y2 on " + SEPERATOR + "	x1.Aid = y2.Aid And" + SEPERATOR + " x1.inst_s_umidl = y2.appe_s_umidl And" + SEPERATOR
					+ "	x1.inst_s_umidh = y2.appe_s_umidh And" + SEPERATOR + addPartitioned(" x1.X_CREA_DATE_TIME_MESG  = y2.X_CREA_DATE_TIME_MESG And ") + SEPERATOR + " x1.inst_num = y2.appe_inst_num And" + SEPERATOR
					+ "	x1.x_last_rec_appe_date_time = y2.appe_date_time And" + SEPERATOR + " x1.x_last_rec_appe_seq_nbr = y2.appe_seq_nbr) on" + SEPERATOR + "	m.aid = x1.aid and" + SEPERATOR + "	m.mesg_s_umidl = x1.inst_s_umidl and" + SEPERATOR
					+ "	m.mesg_s_umidh = x1.inst_s_umidh and" + SEPERATOR + addPartitioned(" m.mesg_crea_date_time=x1.X_CREA_DATE_TIME_MESG and ") + SEPERATOR + " x1.inst_num = 0" + SEPERATOR;
		}
		joinsTables += joinsWithoutOptimization;

		// header
		if (oracleDB) {
			sqlQuery += " select * from ( ";
		}

		sqlQuery += " select DISTINCT " + SEPERATOR;

		// TODO remove the webServie flag
		if (!webService) {
			if (!oracleDB) {
				sqlQuery += " TOP(" + listMax + ") ";
			}
		}

		sqlQuery += getFields() + " FROM rMesg m " + tableLock + SEPERATOR;
		sqlQuery += joinsTables + whereCondition + SEPERATOR;

		if (!StringUtils.isEmpty(listFilter)) {
			if (!oracleDB) {
				sqlQuery += " and convert(nvarchar,mesg_crea_date_time,112) + replace(convert(nvarchar,mesg_crea_date_time,8),':','') + right('000'+convert(nvarchar,m.aid),3) +right('0000000000'+convert(nvarchar,m.mesg_s_umidl),10) + right('0000000000'+convert(nvarchar,abs(m.mesg_s_umidh)),10) < '";
				sqlQuery += listFilter;
				sqlQuery += "'" + SEPERATOR;
			}
		}

		String orderBy = "";
		if (StringUtils.isNotEmpty(fromTablesUnion)) {
			sqlQuery += " UNION SELECT ";
			if (!webService) {
				if (!oracleDB) {
					sqlQuery += " TOP(" + listMax + ") ";
				}
			}
			sqlQuery += getFields() + " FROM rMesg m " + tableLock + SEPERATOR;
			sqlQuery += joinsWithoutOptimization + fromTablesUnion + whereConditionUnion + SEPERATOR;

			if (!StringUtils.isEmpty(listFilter)) {
				if (oracleDB) {
					sqlQuery += " and to_char(mesg_crea_date_time, 'YYYYMMDDHH24MISS') || to_char(m.aid) || to_char(m.mesg_s_umidl) || to_char(abs(m.mesg_s_umidh)) < '";
				} else {
					sqlQuery += " and convert(nvarchar,mesg_crea_date_time,112) + replace(convert(nvarchar,mesg_crea_date_time,8),':','') + convert(nvarchar,abs(m.aid)) + convert(nvarchar,abs(m.mesg_s_umidl)) + convert(nvarchar,abs(m.mesg_s_umidh)) < '";
				}
				sqlQuery += listFilter;
				sqlQuery += "'" + SEPERATOR;
			}
		} else {
			if (oracleDB) {
				orderBy = " ORDER BY mesg_crea_date_time desc " + SEPERATOR;
			} else {
				orderBy = " ORDER BY m.mesg_crea_date_time desc,m.aid desc,m.mesg_s_umidl desc,abs(m.mesg_s_umidh) desc " + SEPERATOR;
			}

		}
		String fetchLimitOffset = "";

		if (webService || oracleDB) {
			fetchLimitOffset = " OFFSET " + (pageNumber * listMax) + " ROWS FETCH NEXT " + listMax + " ROWS ONLY ";
		}

		String outerOrderBy = "";
		if (oracleDB) {
			outerOrderBy = " ) g ORDER BY g.mesg_crea_date_time desc,g.aid desc,g.mesg_s_umidl desc,abs(g.mesg_s_umidh) desc " + SEPERATOR;
		}

		sqlQuery = sqlQuery + orderBy + fetchLimitOffset + outerOrderBy;
		return sqlQuery;
	}

	private String getFields() {
		String absField = "";

		if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			absField = "abs(m.mesg_s_umidh) ,";
		}

		String fields = "m.aid, " + SEPERATOR + "m.mesg_s_umidl, " + SEPERATOR + absField + SEPERATOR + "m.mesg_s_umidh, " + SEPERATOR + "m.mesg_sub_format, " + SEPERATOR + "m.mesg_type, " + SEPERATOR + "m.mesg_uumid, " + SEPERATOR
				+ "m.mesg_sender_X1, " + SEPERATOR + "m.mesg_trn_ref, " + SEPERATOR + "m.mesg_user_reference_text, " + SEPERATOR + "m.mesg_crea_date_time, " + SEPERATOR + "m.x_fin_value_date, " + SEPERATOR + "m.x_fin_amount, " + SEPERATOR
				+ "m.x_fin_ccy, " + SEPERATOR + "m.mesg_frmt_name, " + SEPERATOR + "m.mesg_identifier, " + SEPERATOR + "m.mesg_uumid_suffix, " + SEPERATOR + "m.mesg_status, " + SEPERATOR + "m.mesg_mesg_user_group, " + SEPERATOR
				+ "m.mesg_syntax_table_ver, " + SEPERATOR +
				// GPI
				"m.MESG_ORDER_CUS, " + SEPERATOR + "m.MESG_ORDERING_INST, " + SEPERATOR + "m.MESG_BEN_CUST, " + SEPERATOR + "m.MESG_ACCOUNT_INST, " + SEPERATOR + "m.MESG_CHARGES, " + SEPERATOR + "m.MESG_RCV_CHARGES_AMOUNT, " + SEPERATOR
				+ "m.MESG_SND_CHARGES_AMOUNT, " + SEPERATOR + "m.MESG_SND_CHARGES_CURR, " + SEPERATOR + "m.MESG_Reason_code, " + SEPERATOR + "m.MESG_Status_code, " + SEPERATOR + "m.NOTIF_DATE_TIME, " + SEPERATOR + "m.MESG_EXCHANGE_RATE, " + SEPERATOR
				+ "m.MESG_status_originator, " + SEPERATOR + "m.MESG_forwarded_to, " + SEPERATOR + "m.MESG_NAK_code, " + SEPERATOR + "m.MESG_INSTR_AMOUNT, " + SEPERATOR + "m.MESG_INSTR_CCY, " + SEPERATOR + "m.MESG_SND_CORR, " + SEPERATOR
				+ "m.MESG_RCVR_CORR, " + SEPERATOR + "m.MESG_REIMBURS_INST, " + SEPERATOR + "m.MESG_Settlement_Method, " + SEPERATOR + "m.MESG_Clearing_System, " + SEPERATOR + "m.mesg_rel_trn_ref, " + SEPERATOR + "m.mesg_service, " + SEPERATOR
				+ "m.mesg_e2e_transaction_reference, " + SEPERATOR + "m.MESG_SLA, " + SEPERATOR + "m.mesg_xml_query_ref1, " + SEPERATOR + "m.mesg_xml_query_ref2, " + SEPERATOR + "m.mesg_xml_query_ref3, " + SEPERATOR
				+ "m.X_LAST_EMI_APPE_DELV_STS as emi_network_delivery_status, " + SEPERATOR + "m.X_INST0_RP_NAME," + SEPERATOR;

		fields += "m.mesg_class, " + SEPERATOR + "m.mesg_related_s_umid, " + SEPERATOR + "m.mesg_mod_date_time, " + SEPERATOR + "m.mesg_is_live, " + SEPERATOR + "m.mesg_nature, " + SEPERATOR + "m.mesg_sender_X2, " + SEPERATOR + "m.mesg_sender_X3, "
				+ SEPERATOR + "m.mesg_sender_X4, " + SEPERATOR + "m.mesg_sender_swift_address, " + SEPERATOR + "m.mesg_release_info, " + SEPERATOR + "m.mesg_user_issued_as_pde, " + SEPERATOR + "m.mesg_user_priority_code, " + SEPERATOR
				+ "m.mesg_copy_service_id, " + SEPERATOR + "m.x_inst0_unit_name, " + SEPERATOR + "m.x_category, " + SEPERATOR + "m.x_receiver_X1, " + SEPERATOR + "m.x_receiver_X2, " + SEPERATOR + "m.x_receiver_X3, " + SEPERATOR + "m.x_receiver_X4, "
				+ SEPERATOR + "m.archived, " + SEPERATOR + "m.last_update, " + SEPERATOR + "m.restored, " + SEPERATOR + "m.set_id, " + SEPERATOR + "m.MESG_IS_COPY_REQUIRED, " + SEPERATOR + "m.mesg_requestor_dn, " + SEPERATOR
				+ "m.MESG_FIN_INFORM_RELEASE_INFO, ";

		/* End of new fields */
		String appFields = "m.X_RECEIVER_X1 inst_receiver_X1, " + SEPERATOR;
		if (config.getSearchOptimizer() == 0) {
			appFields = appFields.concat(" y1.appe_iapp_name as emi_iapp_name, " + SEPERATOR + "y1.appe_session_nbr as emi_session_nbr, " + SEPERATOR + "y1.appe_sequence_nbr as emi_sequence_nbr, " + SEPERATOR + "y2.appe_iapp_name as rec_iapp_name, "
					+ SEPERATOR + "y2.appe_session_nbr as rec_session_nbr, " + SEPERATOR + "y2.appe_sequence_nbr as rec_sequence_nbr" + SEPERATOR);
		} else if (config.getSearchOptimizer() == 1) {
			appFields = appFields.concat("m.X_INST0_RP_NAME as inst_rp_name, " + SEPERATOR + "m.X_LAST_EMI_APPE_IAPP_NAME as emi_iapp_name, " + SEPERATOR + "m.X_LAST_EMI_APPE_SESSION_NBR as emi_session_nbr, " + SEPERATOR
					+ "m.X_LAST_EMI_APPE_SEQUENCE_NBR as emi_sequence_nbr, " + SEPERATOR + "m.X_LAST_REC_APPE_IAPP_NAME as rec_iapp_name, " + SEPERATOR + "m.X_LAST_REC_APPE_SESSION_NBR as rec_session_nbr, " + SEPERATOR
					+ "m.X_LAST_REC_APPE_SEQUENCE_NBR as rec_sequence_nbr" + SEPERATOR);
		}

		return fields.concat(appFields);

	}

	private void prepareQuery(ViewerSearchParam params, List<FieldSearchInfo> fieldSearch, int timeZoneOffset, String userName, boolean addColums, boolean showInternalMessages, int textDecompostionType, boolean caseSensitive,
			boolean includeSysMessages, boolean xmlSearch, boolean enableUnicodeSearch, boolean enableGpiSearch, Long groupId) {
		// textDecompostionType 0: no decompose, 1: decompose all ,2: some messages decomposed
		preperUnicodeChar(enableUnicodeSearch);
		textFieldConditionWithOr = false;
		whereCondition = getMessageSecurityData(userName, addColums, showInternalMessages, xmlSearch, groupId, params.getQueryVariablesBinding());
		boolean isMX = StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "ANYXML");

		// Header
		String subSQLH = ViewerSearchParametersBuilder.createSearchParameterSubSQLH(params, timeZoneOffset, enableGpiSearch);

		AdvancedSearchCriteria advancedSearchHoldables = getAdvancedSearchCriteria(params, timeZoneOffset, fieldSearch, textDecompostionType);
		// Text
		String subSQLT = ViewerSearchParametersBuilder.createSearchParameterSubSQLT(params, advancedSearchHoldables, includeSysMessages, textDecompostionType);
		// Appendix
		String subSQLA = ViewerSearchParametersBuilder.createSearchParameterSubSQLA(params);
		// Instance
		String subSQLI = ViewerSearchParametersBuilder.createSearchParameterSubSQLI(params);
		// History
		String subSQLHist = ViewerSearchParametersBuilder.createSearchParameterSubSQLHist(params);
		// File
		String subSQLTFile = ViewerSearchParametersBuilder.createSearchParameterSubSQLFile(params);
		// XML
		String subsQLXML = ViewerSearchParametersBuilder.createXMLSearchParameterSQL(params);
		// searchText
		String subSQLSearchText = subSQLT;

		if (advancedSearchHoldables != null && !advancedSearchHoldables.isEmpty()) {
			subSQLH = subSQLH + StringUtils.defaultString(advancedSearchHoldables.getSubSQLH());
			subSQLT = StringUtils.defaultString(advancedSearchHoldables.getSubSQLT());
		}

		String tableLock = "";
		if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			tableLock = " WITH (NOLOCK) ";
		}

		if (xmlSearch) {
			joinsTables += " inner join rxmltext xmlText" + tableLock + " ON (xmlText.aid = m.aid and xmlText.xmltext_s_umidl = m.mesg_s_umidl and xmlText.xmltext_s_umidh = m.mesg_s_umidh "
					+ addPartitioned(" and xmlText.MESG_CREA_DATE_TIME = m.mesg_crea_date_time ") + ")";
			whereCondition += subsQLXML + SEPERATOR;
		}
		if (whereCondition.length() < 1) {
			whereCondition += "where " + subSQLH + SEPERATOR;
		} else if (subSQLH.length() < 1) {
			whereCondition += SEPERATOR;
		} else {
			whereCondition += " AND " + subSQLH + SEPERATOR;
		}

		if (!StringUtils.isEmpty(subSQLA)) {
			joinsTables += " inner join rAppe a" + tableLock + " on (" + "a.aid = m.aid and a.appe_s_umidl = m.mesg_s_umidl and a.appe_s_umidh = m.mesg_s_umidh" + addPartitioned(" and a.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ") + SEPERATOR
					+ ")";
			whereCondition += " AND " + subSQLA + SEPERATOR;

		}
		if (!StringUtils.isEmpty(subSQLI)) {
			if (!StringUtils.isEmpty(params.getResponserDN())
					&& (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "ANYXML"))) {
				joinsTables += " inner join rInst i" + tableLock + " on (" + "i.aid = m.aid and i.inst_s_umidl = m.mesg_s_umidl and i.inst_s_umidh = m.mesg_s_umidh" + addPartitioned(" and i.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ") + SEPERATOR
						+ ")";
			}
			whereCondition += " AND " + subSQLI + SEPERATOR;
		}

		if (!StringUtils.isEmpty(subSQLTFile)) {
			joinsTables += " inner join rFile f " + tableLock + " ON (f.aid = m.aid and f.file_s_umidh = m.mesg_s_umidh and f.file_s_umidl = m.mesg_s_umidl" + addPartitioned(" and f.mesg_crea_date_time = m.mesg_crea_date_time ") + ") ";
			whereCondition += " AND " + subSQLTFile + SEPERATOR;
		}

		if (!StringUtils.isEmpty(subSQLHist)) {
			joinsTables += " inner join rIntv hist" + tableLock + " on (" + " hist.aid = m.aid and hist.intv_s_umidl = m.mesg_s_umidl and hist.intv_s_umidh = m.mesg_s_umidh" + addPartitioned(" and hist.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ")
					+ SEPERATOR + ")";
			whereCondition += " AND " + subSQLHist + SEPERATOR;
		}

		boolean emptySubSQLT = StringUtils.isEmpty(subSQLT);

		// file messages has no text, but they have payload_text so this can be considered as text,
		// note that in the case of 'any' we don't want to search in files, as it may take very long time
		if (!StringUtils.isEmpty(subSQLSearchText) && params.getUmidFormat().trim().equalsIgnoreCase("file")) {

			if (StringUtils.isEmpty(subSQLTFile)) {
				joinsTables += " inner join rFile f" + tableLock + " on(" + " f.aid = m.aid and f.file_s_umidl = m.mesg_s_umidl and f.file_s_umidh = m.mesg_s_umidh" + SEPERATOR + ")";
			}
			if (applicationFeatures.isFilePayloadSupported()) {

				if (caseSensitive && dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
					whereCondition += " AND ( f.payload_text COLLATE Latin1_General_CS_AS like " + subSQLSearchText;// subSQLSearchText starts and ends with %

					if (subSQLSearchText.contains(dbPortabilityHandler.getNewLineCharsWithConc())) {
						whereCondition += " OR f.payload_text COLLATE Latin1_General_CS_AS like " + subSQLSearchText.replace(dbPortabilityHandler.getNewLineCharsWithConc(), dbPortabilityHandler.getNewLineChar10WithConc());
					}

					whereCondition += " ) " + SEPERATOR;
				} else if (!caseSensitive && dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
					whereCondition += " AND ( upper(f.payload_text) like upper(" + subSQLSearchText + ")";// subSQLSearchText starts and ends with %

					if (subSQLSearchText.contains(dbPortabilityHandler.getNewLineCharsWithConc())) {
						whereCondition += " OR upper(f.payload_text) like upper(" + subSQLSearchText.replace(dbPortabilityHandler.getNewLineCharsWithConc(), dbPortabilityHandler.getNewLineChar10WithConc()) + ")";
					}

					whereCondition += " ) " + SEPERATOR;
				} else {
					whereCondition += " AND ( f.payload_text like " + subSQLSearchText + " ";// subSQLSearchText starts and ends with %

					if (subSQLSearchText.contains(dbPortabilityHandler.getNewLineCharsWithConc())) {
						whereCondition += " OR f.payload_text like " + subSQLSearchText.replace(dbPortabilityHandler.getNewLineCharsWithConc(), dbPortabilityHandler.getNewLineChar10WithConc()) + " ";
					}

					whereCondition += " ) " + SEPERATOR;
				}

			}
			// file messages has no text, so don't process text
			emptySubSQLT = true;
		}

		// for search in archived or in live messages or all of them
		if (params.getSearchInValue() != 2) { // 1 mean all so no need to add the criteria
			whereCondition = whereCondition.concat(" and m.ARCHIVED = ? ");
			params.getQueryVariablesBinding().add(params.getSearchInValue());
		}
		// Search by primary key for the web service API's
		if (params.getPrimaryKey() != null && !params.getPrimaryKey().isEmpty()) {
			// get all the numeric values
			String numeric = extractInt("[^-\\d]", " ", params.getPrimaryKey());

			String[] paramValue = numeric.split(" ");
			String[] paramName = "m.aid, m.mesg_s_umidl, m.mesg_s_umidh".split(",");
			String clauseString = "";
			for (int i = 3; i <= paramValue.length; i += 3) {
				if (paramValue.length == 3) {
					clauseString = clauseString.concat(" ".concat(paramName[0] + "= ? and").concat(paramName[1] + "= ? and").concat(paramName[2] + "= ?"));

				} else if (paramValue.length > 3) {
					if (i == 3) {
						clauseString = clauseString.concat(" ( ".concat(paramName[0] + "= ? and").concat(paramName[1] + "= ? and").concat(paramName[2] + "= ? )"));
					} else {
						clauseString = clauseString.concat(" or ( ".concat(paramName[0] + "= ? and").concat(paramName[1] + "= ? and").concat(paramName[2] + "= ? )"));

					}

				}
			}
			whereCondition = whereCondition.concat(" and ( ".concat(clauseString).concat(" )"));
			for (int i = 0; i < paramValue.length; i++) {
				params.getQueryVariablesBinding().add(paramValue[i]);
			}
		}

		if (!emptySubSQLT) {

			boolean isAdvancsearch = advancedSearchHoldables != null && !StringUtils.isEmpty(advancedSearchHoldables.getSubSQLHUnion());
			if (isMX) {
				joinsTables += " inner join  rXMLTEXT t" + tableLock + " on( t.aid = m.aid " + "and t.xmltext_s_umidl = m.mesg_s_umidl and t.xmltext_s_umidh = m.mesg_s_umidh" + addPartitioned(" and t.mesg_crea_date_time=m.mesg_crea_date_time ")
						+ ") ";

			} else {
				fromTablesUnion = joinsTables + " left outer join rTextField t" + tableLock + " on( t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh"
						+ addPartitioned(" and t.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ") + ") ";
				if (includeSysMessages && textDecompostionType == 1 && !isAdvancsearch) { // currently sys messages does not supported in advanced search
					fromTablesUnion += "left outer join RSYSTEMTEXTFIELD s" + tableLock + " on ( s.aid = m.aid AND s.text_s_umidl = m.mesg_s_umidl AND s.text_s_umidh = m.mesg_s_umidh "
							+ addPartitioned("AND s.X_CREA_DATE_TIME_MESG = m.mesg_crea_date_time ") + " ) ";
				}
				joinsTables += " inner join rText t" + tableLock + " on( t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh" + addPartitioned(" and t.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ") + ") ";

			}

			whereConditionUnion = whereCondition;

			// if fully or partially decomposed, we need to include rTextField search
			if (isAdvancsearch) {
				if (!isMX) {
					// advanced search
					whereCondition += subSQLT;
					String subSQLHUnion = advancedSearchHoldables.getSubSQLHUnion();
					if (subSQLHUnion.contains("OR")) {
						textFieldConditionWithOr = true;
					}
					whereConditionUnion += addChar(subSQLHUnion, '(', subSQLHUnion.indexOf("AND") + 3) + ")";
				}
			} else {
				if (isMX) {

					if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
						if (caseSensitive) {
							whereCondition += " AND ( xmltext_data.value('(/*)[1]', 'varchar(max)') COLLATE Latin1_General_CS_AS LIKE " + unicodeCharacter + "" + subSQLT
									+ " or XMLTEXT_HEADER.value('(/*)[1]', 'varchar(max)') COLLATE Latin1_General_CS_AS LIKE " + subSQLT + ")";
						} else {
							whereCondition += " AND ( xmltext_data.value('(/*)[1]', 'varchar(max)') LIKE " + unicodeCharacter + "" + subSQLT + " or XMLTEXT_HEADER.value('(/*)[1]', 'varchar(max)')  LIKE " + subSQLT + ")";
						}

					} else {
						if (caseSensitive) {
							whereCondition += " AND ( XMLCast(XMLQuery('/*' PASSING XMLTEXT_DATA  RETURNING CONTENT) AS Clob) LIKE " + unicodeCharacter + "" + subSQLT
									+ " or  XMLCast(XMLQuery('/*' PASSING XMLTEXT_HEADER  RETURNING CONTENT)  AS Clob) LIKE " + subSQLT + ")";
						} else {
							whereCondition += " AND ( upper(XMLCast(XMLQuery('/*' PASSING XMLTEXT_DATA  RETURNING CONTENT) AS Clob)) LIKE " + unicodeCharacter + "upper(" + subSQLT + ")"
									+ " or  upper(XMLCast(XMLQuery('/*' PASSING XMLTEXT_HEADER RETURNING CONTENT) AS Clob)) LIKE " + "upper(" + subSQLT + ")" + ")";
						}
					}
				} else {

					// for text search we will search for an exact match in the field value in the textField Table
					if (caseSensitive && dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
						// With SQLServer UPPER take only parameter of char type and not of text type !
						// by default sql server is case insensitive, when we want to use it as caseInsensitive we should use ' COLLATE Latin1_General_CS_AS ' after the column name

						whereCondition += " AND (t.text_data_block COLLATE Latin1_General_CS_AS LIKE " + unicodeCharacter + "" + subSQLT + ")";
						whereConditionUnion += " AND (t.value COLLATE Latin1_General_CS_AS LIKE " + unicodeCharacter + "" + subSQLT + "" + " OR t.value_memo COLLATE Latin1_General_CS_AS like" + unicodeCharacter + "" + subSQLT + "";
						if (includeSysMessages && textDecompostionType == 1) {
							whereConditionUnion += " Or s.value COLLATE Latin1_General_CS_AS LIKE " + subSQLT;
						}
						whereConditionUnion += ")";
					} else if (!caseSensitive && dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
						whereCondition += " AND contains(t.text_data_block, " + subSQLT + ") > 0";
						// whereCondition += " AND (upper(t.text_data_block) LIKE " + unicodeCharacter + "upper(" + subSQLT + "))";

						whereConditionUnion += " AND (upper(t.value) LIKE " + unicodeCharacter + "upper(" + subSQLT + ")" + " OR upper(t.value_memo) LIKE upper(" + subSQLT + ")";
						if (includeSysMessages && textDecompostionType == 1) {
							whereConditionUnion += " Or upper(s.value) LIKE upper(" + subSQLT + ")";
						}
						whereConditionUnion += ")";
					} else {
						// by default sql server is case insensitive
						if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
							whereCondition += " AND contains(t.text_data_block, " + subSQLT + ") > 0";
						} else {
							whereCondition += " AND (t.text_data_block LIKE " + unicodeCharacter + "" + subSQLT + ")";
						}
						whereConditionUnion += " AND (t.value LIKE " + unicodeCharacter + "" + subSQLT + "" + " OR t.value_memo LIKE" + unicodeCharacter + "" + subSQLT + "";
						if (includeSysMessages && textDecompostionType == 1) {
							whereConditionUnion += " Or s.value LIKE " + subSQLT + "";
						}
						whereConditionUnion += ")";
					}
				}
			}
			/*
			 * Handling textDecompostionType if textDecompostionType=0 then only rtext should be included in query. if textDecompostionType=1 then only rtextfield should be included in query. other
			 * wise same as old behavior.
			 */
			if (!isMX) {
				if (textDecompostionType == 0) {
					fromTablesUnion = "";
					whereConditionUnion = "";

				} else if (textDecompostionType == 1) {
					whereCondition = whereConditionUnion;
					joinsTables = fromTablesUnion;
					fromTablesUnion = "";
					whereConditionUnion = "";
				}
			}

		} else {
			fromTablesUnion = "";
			whereConditionUnion = "";
		}

	}

	/**
	 * This method is used in the Message viewer Web service to extract the numeric values from a composite primary Key (aid, umidl, umidh)
	 * 
	 * @author MKhaldon
	 * @param regx
	 * @param replaceWith
	 * @param str
	 * @return
	 */
	private String extractInt(String regx, String replaceWith, String str) {
		str = str.replaceAll(regx, replaceWith);

		// Remove extra spaces from the beginning
		// and the ending of the string
		str = str.trim();

		// Replace all the consecutive white
		// spaces with a single space
		str = str.replaceAll(" +", " ");

		if (str.equals(""))
			return "-1";

		return str;
	}

	private void preperUnicodeChar(boolean enableUnicodeSearch) {
		if ((dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) && enableUnicodeSearch) {
			unicodeCharacter = " N";
		}
	}

	private String addChar(String str, char ch, int position) {
		StringBuilder sb = new StringBuilder(str);
		sb.insert(position, ch);
		return sb.toString();
	}

	private String addPartitioned(String str) {
		if (dbPortabilityHandler.isPartitionedDB()) {
			return str;
		}
		return "";
	}

	private String getMessageSecurityData(String userName, boolean addColums, boolean showInternalMessages, boolean xmlSearch, Long groupId, List<Object> queryBindingVar) {
		String bicsInParam = "";
		String value = "";
		if ((getSecurityDataService().getAllSecurityDataByGroupId(groupId).getAllLicenseBics() != null && !getSecurityDataService().getAllSecurityDataByGroupId(groupId).getAllLicenseBics().isEmpty())
				|| (queryBindingVar != null && !queryBindingVar.isEmpty())) {

			bicsInParam = "X_OWN_LT IN (" + prepareInClauseValuesFromCache(getSecurityDataService().getAllSecurityDataByGroupId(groupId).getAllLicenseBics(), queryBindingVar) + ")";
		}
		String bicCodeCondition = bicsInParam;
		if (showInternalMessages) {
			bicCodeCondition = " (" + bicsInParam + "  or X_OWN_LT = ? )";
			queryBindingVar.add("XXXXXXXX");
		}

		if (bicCodeCondition.length() > 1) {
			value = "WHERE " + bicCodeCondition + " AND " + "X_INST0_UNIT_NAME IN (" + prepareInClauseValuesFromCache(getSecurityDataService().getAllSecurityDataByGroupId(groupId).getAllUnitsList(), queryBindingVar) + ")" + "	AND X_CATEGORY IN ("
					+ prepareInClauseValuesFromCache(getSecurityDataService().getAllSecurityDataByGroupId(groupId).getAllCategoriesList(), queryBindingVar) + ") ";
		}
		return value;
	}

	private static String prepareInClauseValuesFromCache(List<String> values, List<Object> queryBindingVar) {
		List<String> varList = new ArrayList<String>();
		for (String str : values) {
			varList.add("?");
			queryBindingVar.add(str);
		}
		return StringUtils.join(varList, ",");
	}

	private AdvancedSearchCriteria getAdvancedSearchCriteria(ViewerSearchParam params, int timeZoneOffset, List<FieldSearchInfo> fieldSearch, int textDecompostionType) {
		if (fieldSearch == null || fieldSearch.isEmpty()) {
			return null;
		}

		String dateFromStr = "";
		String dateToStr = "";
		if (params.getCreationDate().getDateFrom() != null) {
			Date date = params.getCreationDate().getDateFrom();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, -1 * timeZoneOffset);
			dateFromStr = ViewerServiceUtils.getFormattedDate(calendar.getTime(), true);
		}
		if (params.getCreationDate().getDateTo() != null) {
			Date date = params.getCreationDate().getDateTo();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, -1 * timeZoneOffset);

			dateToStr = ViewerServiceUtils.getFormattedDate(calendar.getTime(), true);
		}

		AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
		for (FieldSearchInfo info : fieldSearch) {
			buildAdvancedSearch(info, criteria, dateFromStr, dateToStr);
		}

		if (textDecompostionType == 2) {
			List<Object> cloneQueryVariablesBinding = params.getQueryVariablesBinding().stream().collect(Collectors.toList());
			params.getQueryVariablesBinding().addAll(0, cloneQueryVariablesBinding);
		}
		return criteria;
	}

	private void buildAdvancedSearch(FieldSearchInfo info, AdvancedSearchCriteria criteria, String valueFromDate, String valueToDate) {
		String tmpStr = "";
		Integer filedTagParsed = 0;
		String subSQLH_Union = "";
		try {
			String tagNum = info.getFieldTag();
			if (tagNum.length() == 3 && !NumberUtils.isDigits(tagNum)) {
				tagNum = tagNum.substring(0, 2);
			}
			filedTagParsed = NumberUtils.createInteger(tagNum);
		} catch (Exception e) {
		}
		if (filedTagParsed == 103 || filedTagParsed == 113 || filedTagParsed == 108 || filedTagParsed == 115 || filedTagParsed == 119) {
			tmpStr = ViewerAdvancedSearchCriteriaBuilder.addBlock3Search(filedTagParsed, info.getCondition(), info.getConditionValue(), false, criteria);

			if (!StringUtils.contains(criteria.getSubSQLH(), tmpStr)) {
				criteria.appendSubSQLH(tmpStr);
			}
		} else {
			subSQLH_Union = addAdvancedCriteriaCondition(info.getFieldTag(), filedTagParsed, info.getCondition(), info.getConditionValue(), false, criteria, valueFromDate, valueToDate, info.getLogicalOpretor());
		}

		if (!StringUtils.isEmpty(criteria.getSubSQLHUnion())) {

			if (info.getLogicalOpretor().equalsIgnoreCase("OR")) {
				criteria.appendSubSQLHUnion(subSQLH_Union);
			} else {
				if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
					criteria.appendSubSQLHUnion(info.getLogicalOpretor() + " (m.aid,m.mesg_s_umidl,m.mesg_s_umidh) in (SELECT * " + SEPERATOR);
					criteria.appendSubSQLHUnion("FROM (SELECT m.aid,m.mesg_s_umidl,m.mesg_s_umidh FROM rMesg m, ");
					criteria.appendSubSQLHUnion(" rTextfield t " + SEPERATOR);
					criteria.appendSubSQLHUnion("WHERE t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh " + addPartitioned(" and t.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ") + SEPERATOR);
					criteria.appendSubSQLHUnion(subSQLH_Union + "))");
				} else {
					// With SQLServer You cannot use IN with multiple col.
					criteria.appendSubSQLHUnion(info.getLogicalOpretor() + " (m.aid + m.mesg_s_umidl + m.mesg_s_umidh) in " + SEPERATOR);
					criteria.appendSubSQLHUnion("(SELECT (m.aid + m.mesg_s_umidl + m.mesg_s_umidh) as MesgID FROM ");
					criteria.appendSubSQLHUnion(" rMesg m WITH (NOLOCK), rTextfield t  WITH (NOLOCK) " + SEPERATOR);
					criteria.appendSubSQLHUnion("WHERE t.aid = m.aid and t.text_s_umidl = m.mesg_s_umidl and t.text_s_umidh = m.mesg_s_umidh " + addPartitioned(" and t.X_CREA_DATE_TIME_MESG=m.mesg_crea_date_time ") + SEPERATOR);
					criteria.appendSubSQLHUnion(subSQLH_Union + ")");
				}
			}

		} else {
			criteria.setSubSQLHUnion(subSQLH_Union);
		}

	}

	private String addAdvancedCriteriaCondition(String fieldTag, Integer filedTagParsed, FieldSearchInfo.Condition searchCondition, String conditionValue, boolean isCaseSensitive, AdvancedSearchCriteria advancedSearchHoldables, String dateFromStr,
			String dateToStr, String logicalOparetor) {
		String tmpStr = "";
		String subSQLHUnion = "";

		String uppderSql = "";
		if (isCaseSensitive && dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			uppderSql = " COLLATE Latin1_General_CS_AS ";
		}
		String nullStr = "nvl";
		if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			nullStr = "isnull";
		}
		String likeStr = " like ";
		if (searchCondition == FieldSearchInfo.Condition.DOES_NOT_CONTAIN) {
			likeStr = " not like ";
		}
		advancedSearchHoldables.appendSubSQLT(" and " + ViewerServiceUtils.checkEmptyClobSQL("t.text_data_block") + " and " + ViewerServiceUtils.checkUpperSQL("t.text_data_block", isCaseSensitive) + uppderSql + likeStr + unicodeCharacter);
		subSQLHUnion = subSQLHUnion + " ";

		conditionValue = conditionValue.replace("'", "''");
		tmpStr = ViewerServiceUtils.checkUpperSQL("'%:" + fieldTag + ":%" + ViewerServiceUtils.trimWildcard(conditionValue) + "%'", isCaseSensitive);

		subSQLHUnion = subSQLHUnion + ((logicalOparetor == null || logicalOparetor.isEmpty() ? "AND" : logicalOparetor) + "  (t.field_code = " + filedTagParsed);
		if (("" + filedTagParsed).length() < StringUtils.defaultString(fieldTag).length()) {
			subSQLHUnion += " AND (t.field_option ='" + StringUtils.substring(fieldTag, ("" + filedTagParsed).length(), StringUtils.defaultString(fieldTag).length()) + "')";
		}
		subSQLHUnion += " AND (";
		String daynamicCondition = "";
		String daynamicConditionValue = "";

		if (searchCondition == FieldSearchInfo.Condition.DOES_NOT_CONTAIN || searchCondition == FieldSearchInfo.Condition.CONTAINS) {
			daynamicCondition = nullStr + "(" + ViewerServiceUtils.checkUpperSQL("t.value", isCaseSensitive) + uppderSql + "," + ViewerServiceUtils.checkUpperSQL("t.value_memo", isCaseSensitive) + uppderSql + ")" + likeStr + unicodeCharacter;
			daynamicConditionValue = ViewerServiceUtils.checkUpperSQL("'%" + ViewerServiceUtils.trimWildcard(conditionValue) + "%'", isCaseSensitive) + ")) ";
			daynamicCondition += daynamicConditionValue;

		} else if (searchCondition == FieldSearchInfo.Condition.EQUAL) {
			daynamicCondition = nullStr + "(" + ViewerServiceUtils.checkUpperSQL("t.value", isCaseSensitive) + uppderSql + "," + ViewerServiceUtils.checkUpperSQL("t.value_memo", isCaseSensitive) + uppderSql + ")" + likeStr + unicodeCharacter;
			daynamicConditionValue = ViewerServiceUtils.checkUpperSQL("'" + ViewerServiceUtils.trimWildcard(conditionValue) + "'", isCaseSensitive) + ")) ";
			daynamicCondition += daynamicConditionValue;

		} else if (searchCondition == FieldSearchInfo.Condition.SOUNDEX) {
			if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
				daynamicCondition = "SOUNDEX (  " + nullStr + "(" + ViewerServiceUtils.checkUpperSQL("t.value", isCaseSensitive) + uppderSql + "," + ViewerServiceUtils.checkUpperSQL("t.value_memo", isCaseSensitive) + uppderSql + ")  )" + likeStr
						+ unicodeCharacter;
				daynamicConditionValue = "SOUNDEX ( " + ViewerServiceUtils.checkUpperSQL("'" + ViewerServiceUtils.trimWildcard(conditionValue) + "'", isCaseSensitive) + "))) ";
			} else {
				daynamicCondition = "SOUNDEX ( " + nullStr + "(" + ViewerServiceUtils.checkUpperSQL("t.value", isCaseSensitive) + uppderSql + "," + ViewerServiceUtils.checkUpperSQL("t.value_memo", isCaseSensitive) + uppderSql + ") )" + likeStr
						+ unicodeCharacter;
				daynamicConditionValue = "SOUNDEX ( " + ViewerServiceUtils.checkUpperSQL("'%" + ViewerServiceUtils.trimWildcard(conditionValue) + "%'", isCaseSensitive) + "))) ";
			}

			daynamicCondition += daynamicConditionValue;
		}

		String conditionWithCreationDtae = "";

		if (!StringUtils.isEmpty(dateFromStr) && !StringUtils.isEmpty(dateToStr)) {
			conditionWithCreationDtae = ViewerServiceUtils.checkAnd(daynamicCondition, "m.mesg_crea_date_time between " + dateFromStr + " and " + dateToStr);
		} else if (!StringUtils.isEmpty(dateFromStr)) {
			conditionWithCreationDtae = ViewerServiceUtils.checkAnd(daynamicCondition, "m.mesg_crea_date_time  >= " + dateFromStr);
		} else if (!StringUtils.isEmpty(dateToStr)) {
			conditionWithCreationDtae = ViewerServiceUtils.checkAnd(daynamicCondition, "m.mesg_crea_date_time  <= " + dateToStr);
		}

		if (logicalOparetor != null && !logicalOparetor.isEmpty() && logicalOparetor.equalsIgnoreCase("and")) {
			subSQLHUnion += conditionWithCreationDtae;
		} else {
			subSQLHUnion += daynamicCondition;
		}

		if (!StringUtils.contains(advancedSearchHoldables.getSubSQLT(), tmpStr)) {
			advancedSearchHoldables.appendSubSQLT(tmpStr);
		}
		return subSQLHUnion;
	}

	public SecurityDataService getSecurityDataService() {
		return securityDataService;
	}

	public void setSecurityDataService(SecurityDataService securityDataService) {
		this.securityDataService = securityDataService;
	}
}
