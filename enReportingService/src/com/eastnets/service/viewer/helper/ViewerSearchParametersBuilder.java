/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.viewer.helper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.eastnets.domain.viewer.AdvancedSearchCriteria;
import com.eastnets.domain.viewer.ViewerSearchParam;

/**
 * Viewer Search Parameters Builder
 * 
 * @author EastNets
 * @since September 3, 2012
 */
public class ViewerSearchParametersBuilder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -255750682415743499L;

	static String createSearchParameterSubSQLI(ViewerSearchParam params) {
		String subSQLI = "";
		List<String> selectedQueues = params.getQueuesSelected();
		if (selectedQueues != null && selectedQueues.size() != 0) {
			subSQLI = ViewerServiceUtils.checkAnd(subSQLI, " m.X_INST0_RP_NAME  in (");
			for (int i = 0; i < selectedQueues.size(); ++i) {
				if (selectedQueues.get(i) != null) {
					params.getQueryVariablesBinding().add(ViewerServiceUtils.sqlEscapeString(selectedQueues.get(i)));
					subSQLI += " ? ";
					if (i < selectedQueues.size() - 1) {
						subSQLI += ", ";
					}
				}
			}
			subSQLI += ") ";
		}

		List<String> selectedUnits = params.getUnitsSelected();
		if (selectedUnits != null && selectedUnits.size() != 0) {
			subSQLI = ViewerServiceUtils.checkAnd(subSQLI, " m.x_inst0_unit_name in (");
			for (int i = 0; i < selectedUnits.size(); ++i) {
				if (selectedUnits.get(i) != null) {
					params.getQueryVariablesBinding().add(ViewerServiceUtils.sqlEscapeString(selectedUnits.get(i)));
					subSQLI += " ? ";
					if (i < selectedUnits.size() - 1) {
						subSQLI += ", ";
					}
				}
			}
			subSQLI += ") ";
		}

		if (!StringUtils.isEmpty(params.getResponserDN())
				&& (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "ANYXML"))) {
			subSQLI = ViewerServiceUtils.checkAnd(subSQLI, "i.inst_responder_dn " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getResponserDN()), params.getQueryVariablesBinding()));
		}
		return subSQLI;
	}

	static String createXMLSearchParameterSQL(ViewerSearchParam params) {
		if (params.getXmlConditionsMetadata() != null && !params.getXmlConditionsMetadata().isEmpty()) {
			return ViewerServiceUtils.buildXMLCondition(params.getXmlConditionsMetadata());
		}
		return null;
	}

	static String createSearchParameterSubSQLA(ViewerSearchParam params) {
		String subSQLA = "";

		if (!StringUtils.equalsIgnoreCase(params.getInterventionsNetworkName(), "any")) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_iapp_name =  ? ");
			params.getQueryVariablesBinding().add(ViewerServiceUtils.sqlEscapeString(StringUtils.upperCase(params.getInterventionsNetworkName())));
		}

		if (StringUtils.equalsIgnoreCase(params.getInterventionsFromToNetwork(), "to")) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_type = ? ");
			params.getQueryVariablesBinding().add("APPE_EMISSION");
		}

		if (StringUtils.equalsIgnoreCase(params.getInterventionsFromToNetwork(), "From")) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_type = ? ");
			params.getQueryVariablesBinding().add("APPE_RECEPTION");
		}

		if (!StringUtils.isEmpty(params.getInterventionsSessionHolder())) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_session_holder " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getInterventionsSessionHolder()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getInterventionsSessionNumber())) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_session_nbr = ? ");
			params.getQueryVariablesBinding().add(params.getInterventionsSessionNumber());
		}

		if (!StringUtils.isEmpty(params.getInterventionsSequenceNumberFrom()) && !StringUtils.isEmpty(params.getInterventionsSequenceNumberTo())) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_sequence_nbr between ? and ? ");
			params.getQueryVariablesBinding().add(ViewerServiceUtils.sqlEscapeString(params.getInterventionsSequenceNumberFrom()));
			params.getQueryVariablesBinding().add(ViewerServiceUtils.sqlEscapeString(params.getInterventionsSequenceNumberTo()));
		} else if (!StringUtils.isEmpty(params.getInterventionsSequenceNumberFrom())) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_sequence_nbr >= ? ");
			params.getQueryVariablesBinding().add(params.getInterventionsSequenceNumberFrom());
		} else if (!StringUtils.isEmpty(params.getInterventionsSequenceNumberTo())) {
			subSQLA = ViewerServiceUtils.checkAnd(subSQLA, "a.appe_sequence_nbr <= ? ");
			params.getQueryVariablesBinding().add(params.getInterventionsSequenceNumberTo());
		}
		return subSQLA;
	}

	static String createSearchParameterSubSQLT(ViewerSearchParam params, AdvancedSearchCriteria advancedSearchHoldables, boolean includeSysMessages, int textDecompostionType) {
		if (!StringUtils.isEmpty(params.getContentSearchText())) {
			String searchTextModified = params.getContentSearchText();

			List<Object> cloneQueryVariablesBinding = params.getQueryVariablesBinding().stream().collect(Collectors.toList());
			int startIndex = 0;
			int endIndex = searchTextModified.length();

			if (searchTextModified.startsWith("%")) {
				startIndex++;
			}
			if (searchTextModified.endsWith("%")) {
				endIndex--;
			}
			searchTextModified = StringUtils.substring(searchTextModified, startIndex, endIndex);
			searchTextModified = ViewerServiceUtils.sqlEscapeString(searchTextModified);
			searchTextModified = searchTextModified.replace("\r\n", "\n");
			searchTextModified = searchTextModified.replace("\r", "\n");
			searchTextModified = searchTextModified.replace("\n", "%");

			if (params.getUmidFormat().trim().equalsIgnoreCase("file")) {
				params.getQueryVariablesBinding().add("%" + searchTextModified + "%");
				return " ? ";
			}

			if (advancedSearchHoldables == null) {
				params.getQueryVariablesBinding().add("%" + searchTextModified + "%");
			}
			if (advancedSearchHoldables == null && textDecompostionType != 0) {
				params.getQueryVariablesBinding().add("%" + searchTextModified + "%");
				if (advancedSearchHoldables == null && includeSysMessages) {
					params.getQueryVariablesBinding().add("%" + searchTextModified + "%");
				}

				if (textDecompostionType == 2) {
					cloneQueryVariablesBinding.add("%" + searchTextModified + "%");
					params.getQueryVariablesBinding().addAll(0, cloneQueryVariablesBinding);
				}
			}

			return " ? ";
		}

		return "";
	}

	static String createSearchParameterSubSQLH(ViewerSearchParam params, int timeZone, boolean enableGpiSearch) {
		String subSQLH = "";
		List<String> selectedSAA = params.getSourceSelectedSAA();
		if (selectedSAA != null && !selectedSAA.isEmpty()) {
			subSQLH = "m.AID in ( ";
			for (int i = 0; i < selectedSAA.size(); ++i) {
				String val = selectedSAA.get(i);
				if (val != null) {
					subSQLH += " ? ";
					if (i < selectedSAA.size() - 1) {
						subSQLH += ", ";
					}
					params.getQueryVariablesBinding().add(val);
				}

			}
			subSQLH += " )";
		}

		if (!StringUtils.equalsIgnoreCase(params.getUmidFormat(), "any")) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, " m.mesg_frmt_name =  ? ");
			params.getQueryVariablesBinding().add(params.getUmidFormat());
		}

		if (StringUtils.equalsIgnoreCase(params.getUmidIO(), "i") || StringUtils.equalsIgnoreCase(params.getUmidIO(), "o")) {
			String ioText = "INPUT";
			if (StringUtils.equalsIgnoreCase(params.getUmidIO(), "o")) {
				ioText = "OUTPUT";
			}
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_sub_format = ? ");
			params.getQueryVariablesBinding().add(ioText);
		}

		if (!StringUtils.isEmpty(params.getUmidCorrespondent())) {
			String tmp = params.getUmidCorrespondent();
			if (!(params.getUmidCorrespondent().length() == 11 || params.getUmidCorrespondent().endsWith("%"))) {
				tmp += "%";
			}
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "((m.mesg_sub_format = ? and m.mesg_sender_X1 like ? ) or (m.mesg_sub_format = ? and m.x_receiver_X1 like ? ) ) ");
			params.getQueryVariablesBinding().add("OUTPUT");
			params.getQueryVariablesBinding().add(tmp);
			params.getQueryVariablesBinding().add("INPUT");
			params.getQueryVariablesBinding().add(tmp);
		}

		// UmidType is visible only when the format is swift
		if (!StringUtils.isEmpty(params.getUmidType()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_type" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getUmidType()), params.getQueryVariablesBinding()));

		}

		if (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift")) {
			// mkassab
			if (!params.getSourceSelectedMT().isEmpty()) {

				int itemCount = 1;
				int listMTSize = params.getSourceSelectedMT().size();
				String allSelectedMT = "";
				String queryForMT = "m.mesg_type IN (";
				for (String msgType : params.getSourceSelectedMT()) {
					params.getQueryVariablesBinding().add(msgType);
					if (listMTSize == itemCount) {
						msgType = " ? ";

					} else {
						msgType = " ? " + ",";
					}

					allSelectedMT = allSelectedMT + msgType;
					itemCount++;

				}
				queryForMT = queryForMT + allSelectedMT + ")";
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, queryForMT);

			}
		}

		// UmidType is visible only when the format is swift
		if (!StringUtils.isEmpty(params.getUmidQual())) {
			if (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_mesg_user_group " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(StringUtils.upperCase(params.getUmidQual())), params.getQueryVariablesBinding()));

			}
		}
		if (!StringUtils.isEmpty(params.getUmidReference())) {
			if (!StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift")) {
				if (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "ANYXML")) {
					subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "(m.mesg_user_reference_text" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getUmidReference()), params.getQueryVariablesBinding()) + ")");
				} else {
					subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "(m.mesg_trn_ref " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getUmidReference()), params.getQueryVariablesBinding())
							+ " OR  m.mesg_user_reference_text" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getUmidReference()), params.getQueryVariablesBinding()) + ")");
				}

			} else {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_trn_ref " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getUmidReference()), params.getQueryVariablesBinding()));
			}
		}
		if (!StringUtils.isEmpty(params.getServiceName())
				&& (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXML"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_service " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getServiceName() + "%"), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getServiceNameExt())
				&& (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXML"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_service " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString("%!" + params.getServiceNameExt()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getIdentifier()) && !params.getIdentifier().equals(",")) {
			String[] identifierArr = params.getIdentifier().split(",");

			if (identifierArr != null && identifierArr.length > 0) {
				int counter = 0;
				String query = "(";
				for (String identierValue : identifierArr) {
					if (counter == 0) {
						query += " m.mesg_identifier like ? ";
					} else {
						query += " OR m.mesg_identifier like ? ";
					}
					counter++;
					params.getQueryVariablesBinding().add(identierValue.trim());
				}

				query += ")";
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, query);
			} else {

				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_identifier " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getIdentifier()), params.getQueryVariablesBinding()));
			}
		}

		if (!StringUtils.equalsIgnoreCase(params.getContentSender(), "any")) {
			if (!params.getContentSenderInstitution().isEmpty() || !params.getContentSenderDepartment().isEmpty() || !params.getContentSenderLastName().isEmpty() || !params.getContentSenderFirstName().isEmpty()) {

				subSQLH = ViewerServiceUtils.checkAnd(subSQLH,
						ViewerServiceUtils.getBIC("m.mesg_sender_", ViewerServiceUtils.sqlEscapeString(params.getContentSenderInstitution()), ViewerServiceUtils.sqlEscapeString(params.getContentSenderDepartment()),
								ViewerServiceUtils.sqlEscapeString(params.getContentSenderLastName()), ViewerServiceUtils.sqlEscapeString(params.getContentSenderFirstName()), params.getQueryVariablesBinding()));

			}

		}

		// mkassab
		if (!params.getSourceSelectedCountry().isEmpty()) {
			int itemCount = 1;
			int listSize = params.getSourceSelectedCountry().size();
			String QueryForCountry = "(";
			String countryStrQuery = "";
			for (String countryCode : params.getSourceSelectedCountry()) {
				if (StringUtils.equalsIgnoreCase(params.getUmidIO(), "i")) {

					countryStrQuery = countryStrQuery + "(m.mesg_sub_format = ? and m.x_receiver_X1 like ? )";
					params.getQueryVariablesBinding().add("INPUT");
					params.getQueryVariablesBinding().add("____" + ViewerServiceUtils.sqlEscapeString(countryCode) + "%");
				} else if (StringUtils.equalsIgnoreCase(params.getUmidIO(), "o")) {
					countryStrQuery = countryStrQuery + "(m.mesg_sub_format = ? and m.mesg_sender_X1 like ? )";
					params.getQueryVariablesBinding().add("OUTPUT");
					params.getQueryVariablesBinding().add("____" + ViewerServiceUtils.sqlEscapeString(countryCode) + "%");
				} else {
					countryStrQuery = countryStrQuery + "((m.mesg_sub_format = ? and m.mesg_sender_X1 like ?) or (m.mesg_sub_format = ? and m.x_receiver_X1 like ? ))";
					params.getQueryVariablesBinding().add("OUTPUT");
					params.getQueryVariablesBinding().add("____" + ViewerServiceUtils.sqlEscapeString(countryCode) + "%");
					params.getQueryVariablesBinding().add("INPUT");
					params.getQueryVariablesBinding().add("____" + ViewerServiceUtils.sqlEscapeString(countryCode) + "%");

				}

				if (itemCount == listSize) {
					QueryForCountry = QueryForCountry + countryStrQuery + " )";
				} else {
					QueryForCountry = QueryForCountry + countryStrQuery + " or ";
				}

				countryStrQuery = "";
				itemCount++;
			}

			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, QueryForCountry);

		}

		if (!params.getSourceSelectedCurancy().isEmpty()) {
			int itemCount = 1;
			int listCurancySize = params.getSourceSelectedCurancy().size();
			String allCurancy = "";
			String queryForCurancy = "m.x_fin_ccy IN (";
			for (String curancy : params.getSourceSelectedCurancy()) {
				params.getQueryVariablesBinding().add(curancy);
				if (listCurancySize == itemCount) {
					curancy = " ? ";

				} else {
					curancy = " ? " + ",";
				}

				allCurancy = allCurancy + curancy;
				itemCount++;
			}
			queryForCurancy = queryForCurancy + allCurancy + ")";
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, queryForCurancy);
		}

		if (!StringUtils.equalsIgnoreCase(params.getContentReceiver(), "any")) {

			boolean checkDepValue = params.getContentReceiverDepartment() == null ? false : !params.getContentReceiverDepartment().isEmpty();
			if (!params.getContentReceiverInstitution().isEmpty() || checkDepValue || !params.getContentReceiverLastName().isEmpty() || !params.getContentReceiverFirstName().isEmpty()) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH,
						ViewerServiceUtils.getBIC("m.x_receiver_", ViewerServiceUtils.sqlEscapeString(params.getContentReceiverInstitution()), ViewerServiceUtils.sqlEscapeString(params.getContentReceiverDepartment()),
								ViewerServiceUtils.sqlEscapeString(params.getContentReceiverLastName()), ViewerServiceUtils.sqlEscapeString(params.getContentReceiverFirstName()), params.getQueryVariablesBinding()));

			}

		}

		if (!StringUtils.equalsIgnoreCase(params.getContentNature(), "all") && !StringUtils.isEmpty(params.getContentNature())) {
			String cotentNature = params.getContentNature();
			cotentNature = StringUtils.upperCase(cotentNature);
			params.getQueryVariablesBinding().add(cotentNature + "%");
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, " m.mesg_nature like ? ");
		}

		if (StringUtils.equalsIgnoreCase(params.getContentNature(), "Financial") || StringUtils.equalsIgnoreCase(params.getContentNature(), "Text") || StringUtils.equalsIgnoreCase(params.getContentNature(), "Security")) {
			if (!StringUtils.isEmpty(params.getContentTransactionReference())) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_trn_ref " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getContentTransactionReference()), params.getQueryVariablesBinding()));
			}
			if (!StringUtils.isEmpty(params.getContentRelatedRefference())) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_rel_trn_ref " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getContentRelatedRefference()), params.getQueryVariablesBinding()));
			}
		}

		String amountFrom = params.getContentAmountFrom();
		String amountTo = params.getContentAmountTo();
		if (amountFrom != null) {
			amountFrom = amountFrom.replace(',', '.');
		}
		if (amountTo != null) {
			amountTo = amountTo.replace(',', '.');
		}

		if (!StringUtils.isEmpty(amountFrom) && !StringUtils.isEmpty(amountTo)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_amount between ? and ?");
			params.getQueryVariablesBinding().add(amountFrom);
			params.getQueryVariablesBinding().add(amountTo);
		} else if (!StringUtils.isEmpty(amountFrom)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_amount >= ?");
			params.getQueryVariablesBinding().add(amountFrom);
		} else if (!StringUtils.isEmpty(amountTo)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_amount <= ? ");
			params.getQueryVariablesBinding().add(amountTo);
		}
		if (!StringUtils.isEmpty(params.getContentAmountCurrency())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_ccy = ? ");
			params.getQueryVariablesBinding().add(params.getContentAmountCurrency());
		}

		String insAmountFrom = params.getInsAmountFrom();
		String insAamountTo = params.getInsAmountTo();
		if (insAmountFrom != null) {
			insAmountFrom = insAmountFrom.replace(',', '.');
		}
		if (insAamountTo != null) {
			insAamountTo = insAamountTo.replace(',', '.');
		}
		if (!StringUtils.isEmpty(insAmountFrom) && !StringUtils.isEmpty(insAamountTo)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_INSTR_AMOUNT between ? and ? ");
			params.getQueryVariablesBinding().add(insAmountFrom);
			params.getQueryVariablesBinding().add(insAamountTo);
		} else if (!StringUtils.isEmpty(insAmountFrom)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_INSTR_AMOUNT >= ? ");
			params.getQueryVariablesBinding().add(insAmountFrom);
		} else if (!StringUtils.isEmpty(insAamountTo)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_INSTR_AMOUNT <= ? ");
			params.getQueryVariablesBinding().add(insAamountTo);
		}
		if (!StringUtils.isEmpty(params.getGpiCur())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_INSTR_CCY = ? ");
			params.getQueryVariablesBinding().add(params.getGpiCur());
		}

		String valueFromDate = "";
		String valueToDate = "";
		Calendar calendar1 = Calendar.getInstance();

		if (params.getContentValueDateFrom() != null) {
			calendar1.setTime(params.getContentValueDateFrom());
			calendar1.add(Calendar.HOUR_OF_DAY, -1 * timeZone);// we are
																// searching
																// in the
																// database,
																// so we
																// remove
																// the time
																// zone
																// offset,
																// if we are
																// viewing
																// we will
																// add the
																// offset
			valueFromDate = ViewerServiceUtils.getFormattedDate(calendar1.getTime(), false, params.getQueryVariablesBinding());// yyyymmdd
		}
		if (params.getContentValueDateTo() != null) {
			calendar1.setTime(params.getContentValueDateTo());
			calendar1.add(Calendar.HOUR_OF_DAY, -1 * timeZone);// we are
																// searching
																// in the
																// database,
																// so we
																// remove
																// the time
																// zone
																// offset,
																// if we are
																// viewing
																// we will
																// add the
																// offset
			valueToDate = ViewerServiceUtils.getFormattedDate(calendar1.getTime(), false, params.getQueryVariablesBinding());// yyyymmdd
		}
		if (!StringUtils.isEmpty(valueFromDate) && !StringUtils.isEmpty(valueToDate)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_value_date between " + valueFromDate + " and " + valueToDate);
		} else if (!StringUtils.isEmpty(valueFromDate)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_value_date >= " + valueFromDate);
		} else if (!StringUtils.isEmpty(valueToDate)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.x_fin_value_date <= " + valueToDate);
		}

		String dateFromStr = "";
		String dateToStr = "";
		if (params.getCreationDate().getDateFrom() != null) {
			Date date = params.getCreationDate().getDateFrom();
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(date);
			calendar2.add(Calendar.HOUR_OF_DAY, -1 * timeZone);// we are
																// searching in
																// the database,
																// so we remove
																// the time zone
																// offset, if we
																// are viewing
																// we will add
																// the offset
			dateFromStr = ViewerServiceUtils.getFormattedDate(calendar2.getTime(), true, params.getQueryVariablesBinding());
		}
		if (params.getCreationDate().getDateTo() != null) {
			Date date = params.getCreationDate().getDateTo();
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(date);
			calendar3.add(Calendar.HOUR_OF_DAY, -1 * timeZone);// we are
																// searching in
																// the database,
																// so we remove
																// the time zone
																// offset, if we
																// are viewing
																// we will add
																// the offset
			dateToStr = ViewerServiceUtils.getFormattedDate(calendar3.getTime(), true, params.getQueryVariablesBinding());
		}
		if (!StringUtils.isEmpty(dateFromStr) && !StringUtils.isEmpty(dateToStr)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_crea_date_time between " + dateFromStr + " and " + dateToStr);
		} else if (!StringUtils.isEmpty(dateFromStr)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_crea_date_time  >= " + dateFromStr);
		} else if (!StringUtils.isEmpty(dateToStr)) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_crea_date_time  <= " + dateToStr);
		}

		if (StringUtils.equalsIgnoreCase(params.getInstanceStatus(), "Completed")) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_status = ? ");
			params.getQueryVariablesBinding().add("COMPLETED");
		}
		if (StringUtils.equalsIgnoreCase(params.getInstanceStatus(), "Live")) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_status = ? ");
			params.getQueryVariablesBinding().add("LIVE");
		}

		// This is to apply delivery Status from Transmission tab
		if (!StringUtils.equalsIgnoreCase(params.getEmiNetworkDeliveryStatus(), "any")) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.X_LAST_EMI_APPE_DELV_STS = ? ");
			params.getQueryVariablesBinding().add(StringUtils.upperCase(params.getEmiNetworkDeliveryStatus()));
		}

		/*
		 * ***************************************************************** The below Code was added to reflect adding new criteria fields to message viewer search criteria where the UMID value is
		 * "swift"
		 * 
		 * Added by Mohammad Alzarai
		 * 
		 * *****************************************************************
		 */

		if (!StringUtils.isEmpty(params.getFinCopy()) && StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift")) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_copy_service_id " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getFinCopy()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getBankingPriority()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_user_priority_code " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getBankingPriority()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getMur()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_user_reference_text " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getMur()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getSlaId()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_SLA " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getSlaId()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getUetr()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_e2e_transaction_reference " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getUetr()), params.getQueryVariablesBinding()));
		}
		if (!StringUtils.isEmpty(params.getFinInform()) && StringUtils.equalsIgnoreCase(params.getUmidFormat(), "swift")) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "MESG_FIN_INFORM_RELEASE_INFO " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getFinInform()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getDeductsFrom())) {
			// subSQLH = ViewerServiceUtils.checkAnd(subSQLH,
			// "MESG_FIN_INFORM_RELEASE_INFO " +
			// ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getFinInform())));
		}
		if (!StringUtils.isEmpty(params.getDeductsTo())) {
			// subSQLH = ViewerServiceUtils.checkAnd(subSQLH,
			// "MESG_FIN_INFORM_RELEASE_INFO " +
			// ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getFinInform())));
		}

		if (!StringUtils.isEmpty(params.getSattlmentMethod())) {
			if (!params.getSattlmentMethod().equalsIgnoreCase("Any")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Settlement_Method " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getSattlmentMethod()), params.getQueryVariablesBinding()));

			}
		}

		if (!StringUtils.isEmpty(params.getClearingSystemList())) {
			if (!params.getClearingSystemList().equalsIgnoreCase("Any")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Clearing_System " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getClearingSystemList()), params.getQueryVariablesBinding()));
			}
		}

		if (!StringUtils.isEmpty(params.getStatusCode())) {
			if (params.getStatusCode().equalsIgnoreCase("ACSC") || params.getStatusCode().equalsIgnoreCase("ACCC")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Status_code in (?, ?) ");
				params.getQueryVariablesBinding().add("ACSC");
				params.getQueryVariablesBinding().add("ACCC");

			} else if (!params.getStatusCode().equalsIgnoreCase("Any")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Status_code " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getStatusCode()), params.getQueryVariablesBinding()));
			}
		}

		if (!StringUtils.isEmpty(params.getTransactionStatus())) {

			if (params.getTransactionStatus().equalsIgnoreCase("ACSC") || params.getTransactionStatus().equalsIgnoreCase("ACCC")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Status_code in (?, ?) ");
				params.getQueryVariablesBinding().add("ACSC");
				params.getQueryVariablesBinding().add("ACCC");

			} else if (params.getTransactionStatus().equalsIgnoreCase("New")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Status_code is null");

			} else if (!params.getTransactionStatus().equalsIgnoreCase("Any")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Status_code " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getTransactionStatus()), params.getQueryVariablesBinding()));
			}
		}

		if (!StringUtils.isEmpty(params.getReasonCodes())) {
			if (!params.getReasonCodes().equalsIgnoreCase("Any")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_Reason_code " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getReasonCodes()), params.getQueryVariablesBinding()));

			}
		}

		if (!StringUtils.isEmpty(params.getgSRPReasonCode()) && !params.getgSRPReasonCode().equalsIgnoreCase("Any")) {

			if (params.getgSRPReasonCode().equalsIgnoreCase("AMNT") || params.getgSRPReasonCode().equalsIgnoreCase("AM09")) {
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "( m.MESG_Reason_code in (?, ?) or  m.MESG_Status_code in (?, ?) ");
				params.getQueryVariablesBinding().add("AMNT");
				params.getQueryVariablesBinding().add("AM09");
				params.getQueryVariablesBinding().add("AMNT");
				params.getQueryVariablesBinding().add("AMNT");
			} else {
				String reasonCode = ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getgSRPReasonCode()), params.getQueryVariablesBinding());
				subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "( m.MESG_Reason_code " + reasonCode + " or  m.MESG_Status_code " + reasonCode + " ) ");

			}
		}

		if (!StringUtils.isEmpty(params.getServiceType())) {
			if (!params.getServiceType().equalsIgnoreCase("Any")) {
				String serviceType = params.getServiceType();
				if (serviceType.equalsIgnoreCase("UC")) {
					subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_sla is null ");
				} else if (serviceType.equalsIgnoreCase("gpi")) {
					subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_sla is not null ");
				}
			}
		}

		if (!StringUtils.isEmpty(params.getStatusOriginatorBic())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_status_originator " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getStatusOriginatorBic()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getForwordBic())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_forwarded_to " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getForwordBic()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getAccountWithInstitution())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "MESG_ACCOUNT_INST" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getAccountWithInstitution()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getOrderingCustomer())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_ORDER_CUS" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(ViewerServiceUtils.addWildCard(params.getOrderingCustomer())), params.getQueryVariablesBinding()));
		}
		if (!StringUtils.isEmpty(params.getOrderingInstitution())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_ORDERING_INST" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getOrderingInstitution()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getBeneficiaryCustomer())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH,
					"m.MESG_BEN_CUST" + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(ViewerServiceUtils.addWildCard(params.getBeneficiaryCustomer())), params.getQueryVariablesBinding()));
		}
		if (!StringUtils.isEmpty(params.getDetailOfCharge())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_CHARGES " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getDetailOfCharge()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getSenderCorr())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_SND_CORR " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getSenderCorr()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getReceiverCorr())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_RCVR_CORR " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getReceiverCorr()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getReimbursementInst())) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_REIMBURS_INST " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getReimbursementInst()), params.getQueryVariablesBinding()));
		}

		/*
		 * ***************************************************************** The below Code was added to reflect adding new criteria fields to message viewer search criteria where the UMID value is
		 * "MX"
		 * 
		 * Added by Mohammad Alzarai
		 * 
		 * *****************************************************************
		 */
		//

		if (!StringUtils.isEmpty(params.getMxKeyword1()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXml"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_xml_query_ref1 " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getMxKeyword1()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getMxKeyword2()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXml"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_xml_query_ref2 " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getMxKeyword2()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getMxKeyword3()) && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXml"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_xml_query_ref3 " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getMxKeyword3()), params.getQueryVariablesBinding()));
		}

		if (!StringUtils.isEmpty(params.getRequestorDN())
				&& (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXml"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.mesg_requestor_dn " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(params.getRequestorDN()), params.getQueryVariablesBinding()));
		}

		if (params.isCopy() && (StringUtils.equalsIgnoreCase(params.getUmidFormat(), "MX") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "file") || StringUtils.equalsIgnoreCase(params.getUmidFormat(), "AnyXml"))) {
			subSQLH = ViewerServiceUtils.checkAnd(subSQLH, "m.MESG_IS_COPY_REQUIRED = ? ");
			params.getQueryVariablesBinding().add("1");
		}

		return subSQLH;

	}

	static String createSearchParameterSubSQLHist(ViewerSearchParam params) {
		String subSQLHist = "";
		String historyQueueParam = "";
		if (params.getHistoryQueue() != null && !StringUtils.isEmpty(params.getHistoryQueue())) {
			historyQueueParam = "%" + params.getHistoryQueue() + "%";
			params.getQueryVariablesBinding().add("INTY_ROUTING");
			subSQLHist = ViewerServiceUtils.checkAnd(subSQLHist, "hist.intv_merged_text " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(historyQueueParam), params.getQueryVariablesBinding()));
			subSQLHist = "hist.intv_inty_category= ? and " + subSQLHist + " ";
		}
		return subSQLHist;
	}

	static String createSearchParameterSubSQLFile(ViewerSearchParam param) {
		String subSQLFile = "";

		if (param.getLogicalName() != null && !StringUtils.isEmpty(param.getLogicalName())) {
			subSQLFile = ViewerServiceUtils.checkAnd(subSQLFile, " f.mesg_file_logical_name " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(param.getLogicalName()), param.getQueryVariablesBinding()));
		}

		if (param.getTransferDescription() != null && !StringUtils.isEmpty(param.getTransferDescription())) {
			subSQLFile = ViewerServiceUtils.checkAnd(subSQLFile, " f.mesg_transfer_desc " + ViewerServiceUtils.checkForWildCard(ViewerServiceUtils.sqlEscapeString(param.getTransferDescription()), param.getQueryVariablesBinding()));
		}

		return subSQLFile;
	}

}
