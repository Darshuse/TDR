package com.eastnets.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.Alliance;
import com.eastnets.domain.xml.Operator;
import com.eastnets.domain.xml.XMLConditionMetadata;

/**
 * Application Utils contains common logic that can be used at the applicaiton level
 * 
 * @author EastNets
 * @since July 12, 2012
 */
public class ApplicationUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3562675607776268863L;

	public static String REPORT_MONTH = "MONTH";
	public static String REPORT_WEEK = "WEEK";
	public static String REPORT_DAY = "DAY";
	final String separator = System.getProperty("line.separator");

	private Map<Integer, String> saaNames = new HashMap<Integer, String>();

	/**
	 * Convert clob to string
	 * 
	 * @param clobInData
	 * @return String
	 */
	public static String convertClob2String(java.sql.Clob clobInData) {
		if (clobInData == null)
			return null;
		String stringClob = null;
		try {
			long i = 1;
			int clobLength = (int) clobInData.length();
			stringClob = clobInData.getSubString(i, clobLength);

			if (stringClob != null)
				stringClob = stringClob.replaceAll("\\\\n", new ApplicationUtils().separator).replace("\\r", "");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stringClob;
	}

	public static String formatDate(Date date) {

		if (date == null) {
			return "";
		}

		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

		return dateFormat.format(date);

	}

	public static String formatReportAdvancedDate(AdvancedDate advancedDate, String datePattern) {
		if (advancedDate == null || (advancedDate.getType() == AdvancedDate.TYPE_DATE && advancedDate.getDate() == null) || (advancedDate.getType() == AdvancedDate.TYPE_DAYS && advancedDate.getDays() == null)
				|| (advancedDate.getType() == AdvancedDate.TYPE_WEEKS && advancedDate.getWeeks() == null) || (advancedDate.getType() == AdvancedDate.TYPE_MONTHS && advancedDate.getMonths() == null)) {
			return "";
		}

		if (advancedDate.getType() == AdvancedDate.TYPE_DATE) {

			DateFormat dateFormat = new SimpleDateFormat(datePattern);
			return dateFormat.format(advancedDate.getDate());
		}

		String retValue = ApplicationUtils.REPORT_DAY;
		if (advancedDate.getType() == AdvancedDate.TYPE_WEEKS) {
			retValue = ApplicationUtils.REPORT_WEEK;
		} else if (advancedDate.getType() == AdvancedDate.TYPE_MONTHS) {
			retValue = ApplicationUtils.REPORT_MONTH;
		}
		retValue += "-" + advancedDate.getValue() + "_" + (advancedDate.isIncludeCurrent() ? "1" : "0");

		return retValue;
	}

	public static String formatReportDate(Date date, String datePattern) {
		if (date == null) {
			return "";
		}

		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		return dateFormat.format(date);
	}

	public static String formatReportAdvancedDate(AdvancedDate advancedDate, boolean withTime) {
		if (!withTime) {
			return formatReportAdvancedDate(advancedDate, Constants.REPORT_DATE_FORMAT);
		}
		return formatReportAdvancedDate(advancedDate, Constants.REPORT_DATE_TIME_FORMAT);
	}

	public static String formatReportDate(Date date, boolean withTime) {
		if (!withTime) {
			return formatReportDate(date, Constants.REPORT_DATE_FORMAT);
		}
		return formatReportDate(date, Constants.REPORT_DATE_TIME_FORMAT);
	}

	public static String formatDate(Date date, String format) {

		if (date == null) {
			return "";
		}

		DateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);

	}

	public static String formatDateTime(Date date) {

		if (date == null) {
			return "";
		}

		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

		return dateFormat.format(date);

	}

	public static void parseAdvancedDate(AdvancedDate advancedDate, String date, String datePattern) {
		advancedDate.clear();
		if (date == null || date.trim().isEmpty()) {
			return;
		}

		if (isReportDateDuration(date)) {

			// parse the date from formats MONTH- or WEEK- or DAY-
			String[] split = date.split("-");

			advancedDate.setType(AdvancedDate.TYPE_DAYS);

			if (split[0].equals(ApplicationUtils.REPORT_MONTH)) {
				advancedDate.setType(AdvancedDate.TYPE_MONTHS);
			} else if (split[0].equals(ApplicationUtils.REPORT_WEEK)) {
				advancedDate.setType(AdvancedDate.TYPE_WEEKS);
			} else if (split[0].equals(ApplicationUtils.REPORT_DAY)) {
				advancedDate.setType(AdvancedDate.TYPE_DAYS);
			}
			String[] split2 = split[1].split("_");

			advancedDate.setIncludeCurrent(false);
			if (split2.length > 1) {
				advancedDate.setIncludeCurrent(split2[1].trim().equals("1"));
			}

			advancedDate.setValue(Integer.parseInt(split2[0]));
			return;
		}

		if (date.length() < datePattern.length()) {
			return;
		}

		Date dateValue = null;
		DateFormat dateTimeFormat = new SimpleDateFormat(datePattern);
		try {
			dateValue = dateTimeFormat.parse(date);
		} catch (ParseException e) {

		}
		advancedDate.setDate(dateValue);
		advancedDate.setType(AdvancedDate.TYPE_DATE);
	}

	public static Date parseAdvancedDate(String dateStr, String datePattern) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		DateFormat dateTimeFormat = new SimpleDateFormat(datePattern);
		try {
			return dateTimeFormat.parse(dateStr);
		} catch (ParseException e) {
		}
		return null;
	}

	public static void parseAdvancedDate(AdvancedDate advancedDate, String date, boolean withTime) {
		if (withTime) {
			parseAdvancedDate(advancedDate, date, Constants.DATE_TIME_FORMAT);
		} else {
			parseAdvancedDate(advancedDate, date, Constants.DATE_FORMAT);
		}
	}

	public static Date parseAdvancedDate(String dateStr, boolean withTime) {
		if (withTime) {
			return parseAdvancedDate(dateStr, Constants.DATE_TIME_FORMAT);
		}
		return parseAdvancedDate(dateStr, Constants.DATE_FORMAT);
	}

	public static BigDecimal parseDoubleNumber(String number) {

		BigDecimal retValue = null;

		if (NumberUtils.isNumber(number)) {

			retValue = new BigDecimal(number);
		}
		return retValue;
	}

	public void setSAAMap(List<Alliance> saaList) {

		for (Alliance a : saaList) {
			int aid = -1;
			try {
				aid = Integer.parseInt(a.getId());
			} catch (Exception e) {
			}
			saaNames.put(aid, a.getDisplayName());
		}
	}

	public Map<Integer, String> getSAAMap() {
		return saaNames;
	}

	public static boolean isReportDateDuration(String parameterValue) {

		if (parameterValue == null || parameterValue.trim().isEmpty()) {
			return false;
		}
		String[] split = parameterValue.split("-");
		if (split.length == 2) {
			if (REPORT_MONTH.equalsIgnoreCase(split[0]) || REPORT_WEEK.equalsIgnoreCase(split[0]) || REPORT_DAY.equalsIgnoreCase(split[0])) {
				return true;
			}
		}
		return false;
	}

	public static String convertXMLMetadataToString(List<XMLConditionMetadata> metadataList) {
		StringBuilder builder = new StringBuilder();
		if (metadataList != null) {
			for (XMLConditionMetadata conditionMetadata : metadataList) {
				builder.append(conditionMetadata.toString()).append(",");
			}
			if (builder.length() > 0) {
				return builder.substring(0, builder.length() - 1);
			} else {
				return "";
			}
		}
		return "";
	}

	public static String convertListToString(List<?> list) {

		String listAsString = "";
		Object values[] = list.toArray(new String[list.size()]);

		for (Object curValue : values) {
			listAsString += curValue.toString() + ",";
		}

		if (listAsString.length() >= 1)
			return listAsString.substring(0, listAsString.length() - 1);
		else
			return listAsString;

	}

	public static List<XMLConditionMetadata> convertStringToXMLMetadata(String str) {
		List<XMLConditionMetadata> resultList = new ArrayList<XMLConditionMetadata>();
		if (str == null || str.isEmpty()) {
			return resultList;
		}

		String[] token = str.split(",");

		String[] metaData;
		XMLConditionMetadata conditionMetadata;
		for (int i = 0; i < token.length; i++) {
			metaData = token[i].split("#@");
			conditionMetadata = new XMLConditionMetadata();
			conditionMetadata.setIdentifier(metaData[0]);
			conditionMetadata.setXpath(metaData[1]);
			conditionMetadata.setOperator(Operator.valueOf(metaData[2]));
			conditionMetadata.setValue(metaData[3]);
			conditionMetadata.setHeader(Boolean.parseBoolean(metaData[4]));
			conditionMetadata.setFullConditionMeaning(conditionMetadata.getXpath() + conditionMetadata.getOperator().getPrefixMeaning() + conditionMetadata.getValue() + conditionMetadata.getOperator().getPostfixMeaning());
			resultList.add(conditionMetadata);
		}

		return resultList;
	}

	public static List<String> convertStringToList(String str) {
		List<String> resultList = new ArrayList<String>();
		if (str == null)
			return resultList;
		String[] token = str.split(",");

		for (int i = 0; i < token.length; i++) {
			resultList.add(token[i]);
		}

		return resultList;
	}
}
