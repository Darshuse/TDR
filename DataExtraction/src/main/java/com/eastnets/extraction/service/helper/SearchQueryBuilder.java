package com.eastnets.extraction.service.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.config.YAMLConfig;

@Service
public class SearchQueryBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchQueryBuilder.class);

	private final static String SEPERATOR = " ";

	protected String fromTables;
	protected String joinsTables;
	protected String fromTablesUnion;
	protected String whereCondition;
	protected String whereConditionUnion;

	@Autowired
	private YAMLConfig configuration;

	private void clearData() {
		fromTables = "";
		joinsTables = "";
		fromTablesUnion = "";
		whereCondition = "";
		whereConditionUnion = "";
	}

	public String generateQuery(SearchParam searchParam) {

		LOGGER.info("Query generator started.");

		// clear internal data members to generate new query
		clearData();

		// fill the internal data members
		prepareQuery(searchParam);

		// generate the actual query
		String finalQuerey = getQuery(searchParam);

		LOGGER.info("Query generator finished.");
		return finalQuerey;
	}

	private void prepareQuery(SearchParam searchParam) {

		String subSQLH = createSearchParameterSubSQLH(searchParam);

		String tableLock = "";
		if (DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			tableLock = " WITH (NOLOCK) ";
		}

		whereCondition = getMessageSecurityData();

		whereCondition += subSQLH + SEPERATOR;

		fromTablesUnion = "";
		whereConditionUnion = "";
	}

	private String getQuery(SearchParam searchParam) {

		String sqlQuery = "";
		boolean oracleDB = DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE;

		String tableLock = "";
		if (!oracleDB) {
			tableLock = " WITH (NOLOCK) ";
		}

		joinsTables += " left outer join (rInst x1 left outer join rAppe y1 on" + SEPERATOR + " 	x1.Aid = y1.Aid And" + SEPERATOR + " 	x1.inst_s_umidl = y1.appe_s_umidl And" + SEPERATOR + "	x1.inst_s_umidh = y1.appe_s_umidh And" + SEPERATOR
				+ addPartitioned(" x1.X_CREA_DATE_TIME_MESG = y1.X_CREA_DATE_TIME_MESG And ") + SEPERATOR + "	x1.inst_num = y1.appe_inst_num And" + SEPERATOR + "	x1.x_last_emi_appe_date_time = y1.appe_date_time And" + SEPERATOR
				+ "	x1.x_last_emi_appe_seq_nbr   = y1.appe_seq_nbr" + SEPERATOR + " left outer join rAppe y2 on " + SEPERATOR + "	x1.Aid 							= y2.Aid And" + SEPERATOR + "	x1.inst_s_umidl 				= y2.appe_s_umidl And"
				+ SEPERATOR + "	x1.inst_s_umidh 				= y2.appe_s_umidh And" + SEPERATOR + addPartitioned(" x1.X_CREA_DATE_TIME_MESG     = y2.X_CREA_DATE_TIME_MESG And ") + SEPERATOR
				+ "	x1.inst_num 					= y2.appe_inst_num And" + SEPERATOR + "	x1.x_last_rec_appe_date_time 	= y2.appe_date_time And" + SEPERATOR + "	x1.x_last_rec_appe_seq_nbr   	= y2.appe_seq_nbr) on" + SEPERATOR
				+ "	m.aid 							= x1.aid and" + SEPERATOR + "	m.mesg_s_umidl 					= x1.inst_s_umidl and" + SEPERATOR + "	m.mesg_s_umidh 					= x1.inst_s_umidh and" + SEPERATOR
				+ addPartitioned(" m.mesg_crea_date_time=x1.X_CREA_DATE_TIME_MESG and ") + SEPERATOR + "	x1.inst_num = 0" + SEPERATOR;

		// header
		sqlQuery += "select DISTINCT " + SEPERATOR;
		sqlQuery += getFields() + " FROM   rMesg m " + tableLock + SEPERATOR;
		sqlQuery += joinsTables + whereCondition + SEPERATOR;
		sqlQuery += " ORDER BY mesg_crea_date_time desc,m.aid desc,m.mesg_s_umidl desc,abs(m.mesg_s_umidh) desc " + SEPERATOR;
		sqlQuery += " OFFSET " + searchParam.getTransactionNumber() * searchParam.getMessageNumber() + " rows fetch next " + searchParam.getMessageNumber() + " rows only";

		return sqlQuery;
	}

	private String getFields() {
		String absField = "";

		if (DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
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
		appFields = appFields.concat(" y1.appe_iapp_name as emi_iapp_name, " + SEPERATOR + "y1.appe_session_nbr as emi_session_nbr, " + SEPERATOR + "y1.appe_sequence_nbr as emi_sequence_nbr, " + SEPERATOR + "y2.appe_iapp_name as rec_iapp_name, "
				+ SEPERATOR + "y2.appe_session_nbr as rec_session_nbr, " + SEPERATOR + "y2.appe_sequence_nbr as rec_sequence_nbr" + SEPERATOR);

		return fields.concat(appFields);

	}

	private String addPartitioned(String str) {
		if (configuration.isPartitioned()) {
			return str;
		}
		return "";
	}

	private String getMessageSecurityData() {

		String value = "WHERE m.MESG_FRMT_NAME != 'Internal' AND ";
		return value;
	}

	private static String createSearchParameterSubSQLH(SearchParam searchParam) {

		StringBuilder subSQLH = new StringBuilder("");

		subSQLH.append(getSelectedAIDSubSQL(searchParam));

		if (searchParam.getBICFile().trim().length() > 0) {
			List<String> bicFileContent = FileReaderUtils.getContentAsList(searchParam.getBICFile());
			if (bicFileContent.size() > 0) {
				subSQLH.append(SearchUtils.checkAnd(subSQLH, getSelectedBICsAsSql(bicFileContent)));
			}
		}

		if (!StringUtils.equalsIgnoreCase(searchParam.getMesgFormat(), "any")) { // IF no formate selected, the value
																					// should be filled with "any".
			subSQLH.append(SearchUtils.checkAnd(subSQLH, " m.mesg_frmt_name = '" + searchParam.getMesgFormat() + "'"));
		}

		if (StringUtils.equalsIgnoreCase(searchParam.getDirection(), "input") || StringUtils.equalsIgnoreCase(searchParam.getDirection(), "output")) {
			subSQLH.append(SearchUtils.checkAnd(subSQLH, "m.mesg_sub_format = '" + searchParam.getDirection().toUpperCase() + "'"));
		}

		// UmidType is visible only when the format is swift
		if (!searchParam.getMesgTypeList().isEmpty()) {
			String mesgTypeStr = " ( ";
			for (int i = 0; i < searchParam.getMesgTypeList().size(); i++) {
				if (i != 0) {
					mesgTypeStr += " OR ";
				}
				mesgTypeStr += "m.mesg_type" + SearchUtils.checkForWildCard(SearchUtils.sqlEscapeString(searchParam.getMesgTypeList().get(i)));
			}
			mesgTypeStr += " ) ";

			subSQLH.append(SearchUtils.checkAnd(subSQLH, mesgTypeStr));

		}

		if (!StringUtils.isEmpty(searchParam.getIdentifier()) && !searchParam.getIdentifier().equals(",")) {
			String[] identifierArr = searchParam.getIdentifier().split(",");

			if (identifierArr != null && identifierArr.length > 0) {
				int counter = 0;
				String query = "(";
				for (String identierValue : identifierArr) {
					if (counter == 0) {
						query += " m.mesg_identifier like '" + identierValue.trim() + "' ";
					} else {
						query += " OR m.mesg_identifier like '" + identierValue.trim() + "' ";
					}
					counter++;
				}
				query += ")";
				subSQLH.append(SearchUtils.checkAnd(subSQLH, query));
			} else {

				subSQLH.append(SearchUtils.checkAnd(subSQLH, "m.mesg_identifier " + SearchUtils.checkForWildCard(SearchUtils.sqlEscapeString(searchParam.getIdentifier()))));
			}
		}

		String dateFromStr = SearchUtils.DateToSqlStr(searchParam.getFromDate());
		String dateToStr = SearchUtils.DateToSqlStr(searchParam.getToDate());

		String dateOption = "";
		if (searchParam.getDate() == null || !searchParam.getDate().equalsIgnoreCase("appe_date_time")) {
			dateOption = "m.mesg_crea_date_time";
		} else {
			dateOption = "y1.APPE_DATE_TIME";
		}

		if (!StringUtils.isEmpty(dateFromStr) && !StringUtils.isEmpty(dateToStr)) {
			subSQLH.append(SearchUtils.checkAnd(subSQLH, dateOption + " between " + dateFromStr + " and " + dateToStr));
		} else if (!StringUtils.isEmpty(dateFromStr)) {
			subSQLH.append(SearchUtils.checkAnd(subSQLH, dateOption + "  >= " + dateFromStr));
		} else if (!StringUtils.isEmpty(dateToStr)) {
			subSQLH.append(SearchUtils.checkAnd(subSQLH, dateOption + "  <= " + dateToStr));
		}

		if (searchParam.isMesgIsLive()) {
			subSQLH.append(SearchUtils.checkAnd(subSQLH, "m.mesg_status = 'LIVE'"));
		}

		if (!StringUtils.isEmpty(searchParam.getSenderBIC())) {
			String temp = searchParam.getSenderBIC();

			subSQLH.append(SearchUtils.checkAnd(subSQLH, "( m.mesg_sender_X1 like '" + SearchUtils.sqlEscapeString(temp) + "')"));
		}

		if (!StringUtils.isEmpty(searchParam.getReceiverBIC())) {
			String temp = searchParam.getReceiverBIC();

			subSQLH.append(SearchUtils.checkAnd(subSQLH, "(m.x_receiver_X1 like '" + SearchUtils.sqlEscapeString(temp) + "')"));
		}

		if (!searchParam.isExtractFlagged()) {
			subSQLH.append(SearchUtils.checkAnd(subSQLH, "(m.x_extracted = 0 OR m.x_extracted IS null " + ")"));
		}

		return subSQLH.toString();

	}

	private static String getSelectedAIDSubSQL(SearchParam searchParam) {

		StringBuilder subSQLADI = new StringBuilder("");

		List<String> selectedSAA = searchParam.getAid();
		if (selectedSAA != null && !selectedSAA.isEmpty()) {
			subSQLADI.append("m.AID in ( ");
			for (int i = 0; i < selectedSAA.size(); ++i) {
				String val = (String) selectedSAA.get(i);
				if (val != null) {
					subSQLADI.append(val);
					if (i < selectedSAA.size() - 1) {
						subSQLADI.append(", ");
					}
				}
			}
			subSQLADI.append(" )");

		}
		return subSQLADI.toString();
	}

	private static String getSelectedBICsAsSql(List<String> bicList) {
		StringBuilder subSqlInBICs = new StringBuilder("");
		for (int i = 0; i < bicList.size(); i++) {
			if (i > 0) {
				subSqlInBICs.append(",");
			}
			if (i == 0) {
				subSqlInBICs.append("(");
			}
			subSqlInBICs.append(" '" + bicList.get(i) + "' ");
		}
		if (subSqlInBICs.length() > 0) {
			subSqlInBICs.append(")");
		}

		return "(m.mesg_sender_X1 in " + subSqlInBICs.toString() + " or m.x_receiver_X1 in " + subSqlInBICs.toString() + ")";

	}

	public String generateQueryForXml(SearchParam searchParam) {
		String dateFromStr = SearchUtils.DateToSqlStr(searchParam.getFromDate());
		String dateToStr = SearchUtils.DateToSqlStr(searchParam.getToDate());
		HashMap<String, List<String>> rules = Utils.readRules(Utils.readXML(searchParam.getXmlCriteriaFile()));
		String criterias = "";
		for (String rule : rules.keySet()) {
			List<String> valueList = rules.get(rule);
			criterias += " and " + rule;
			if (valueList.size() > 1) {
				List<String> listWithQute = valueList.stream().map(s -> "'" + s + "'").collect(Collectors.toList());
				criterias += " in (" + String.join(" , ", listWithQute) + ") ";
			} else {
				if (valueList.get(0).contains("%")) {
					criterias += " like '" + valueList.get(0) + "' ";
				} else {
					criterias += " = '" + valueList.get(0) + "' ";
				}
			}

		}
		String query = "select DISTINCT * from " + searchParam.getSource() + " WHERE CREATION_DATE_TIME between " + dateFromStr + " and " + dateToStr + " " + criterias;

		if (!searchParam.isExtractFlagged()) {
			query = query + " and (x_extracted = 0 OR x_extracted IS null ) ";
		}

		query = query + " ORDER BY creation_date_time desc, aid desc, umidl desc, umidh desc ";
		query = query + " OFFSET " + searchParam.getTransactionNumber() * searchParam.getMessageNumber() + " rows fetch next " + searchParam.getMessageNumber() + " rows only";

		return query;
	}

	public String generateUpdateQuery(String query) {
		int index1 = query.indexOf("WHERE");
		int index2 = query.indexOf("ORDER BY");
		query = query.substring(index1, index2);
		query = query.replaceAll("m\\.", "RMESG.");
		query = query.replaceAll("y1\\.", "rAppe.");
		query = "update RMESG SET RMESG.x_extracted =1 " + query;
		return query;
	}

	public String generateXMLUpdateQuery(List<String> allAid, List<String> allUmidl, List<String> allUmidh) {

		String query = "update RMESG  SET x_extracted =1  WHERE AID IN (" + prepareInClauseValuesFromCache(allAid) + ") and MESG_S_UMIDL IN (" + prepareInClauseValuesFromCache(allUmidl) + ") AND MESG_S_UMIDH IN ("
				+ prepareInClauseValuesFromCache(allUmidh) + ") ";
		return query;
	}

	private static String prepareInClauseValuesFromCache(List<String> values) {
		if (values == null || values.isEmpty()) {
			return null;
		}
		List<String> varList = new ArrayList<String>();
		for (String str : values) {
			varList.add(str);
		}
		return StringUtils.join(varList, ",");

	}

}
