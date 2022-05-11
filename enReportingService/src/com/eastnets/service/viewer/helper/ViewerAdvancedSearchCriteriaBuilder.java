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

import org.apache.commons.lang.StringUtils;

import com.eastnets.domain.viewer.AdvancedSearchCriteria;
import com.eastnets.domain.viewer.FieldSearchInfo;

public class ViewerAdvancedSearchCriteriaBuilder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5160958029884032114L;

	public static String addBlock3Search(Integer filedTagParsed, FieldSearchInfo.Condition searchCondition, String conditionValue1, boolean isCaseSensitive, AdvancedSearchCriteria advancedSearchHoldables) {

		String tmpStr = "";
		String like = " LIKE " + ViewerServiceUtils.checkUpperSQL("'%" + conditionValue1 + "%'", isCaseSensitive);

		if (searchCondition == FieldSearchInfo.Condition.DOES_NOT_CONTAIN) {
			like = " NOT LIKE " + ViewerServiceUtils.checkUpperSQL("'%" + conditionValue1 + "%'", isCaseSensitive);
		}
		if (isCaseSensitive && ViewerServiceUtils.isSqlServerDB()) {
			like = " COLLATE Latin1_General_CS_AS " + like;
		}

		switch (filedTagParsed) {
		case 103: // Fin Copy
		{
			tmpStr = " AND (" + ViewerServiceUtils.checkUpperSQL("m.mesg_copy_service_id", isCaseSensitive) + like + " )";
			break;
		}
		case 113: // Priority Code
		{
			tmpStr += " AND (" + ViewerServiceUtils.checkUpperSQL("m.mesg_user_priority_code", isCaseSensitive) + like + " )";
			break;
		}
		case 108: // MUR
		{
			tmpStr += " AND (" + ViewerServiceUtils.checkUpperSQL("m.mesg_user_reference_text", isCaseSensitive) + like + " )";
			break;
		}
		case 115: // Release info
		{
			tmpStr += " AND (" + ViewerServiceUtils.checkUpperSQL("m.mesg_release_info", isCaseSensitive) + like + " )";
			break;
		}
		case 119: // mesg_mesg_user_group
		{
			tmpStr += " AND (" + ViewerServiceUtils.checkUpperSQL("m.mesg_mesg_user_group", isCaseSensitive) + like + " )";
			break;
		}
		}
		return tmpStr;
	}

	public static String getDisplay(String fieldTag, boolean isSubField, String selectedFormat, String subFieldText, String searchCondition, String conditionValue1, String conditionValue2) {
		String display = "";
		if (!isSubField) {
			display = StringUtils.trim(fieldTag) + " " + StringUtils.trim(searchCondition) + " " + conditionValue1;
		} else {
			display = StringUtils.trim(fieldTag) + " subfield " + StringUtils.defaultString(subFieldText) + " " + StringUtils.trim(searchCondition) + " " + conditionValue1;
		}
		if (conditionValue2 != null) {
			display = display + " and " + conditionValue2;
		}

		if (isSubField && selectedFormat != null) {
			display += " [ format : " + selectedFormat + " ]";
		}
		return display;
	}

}
