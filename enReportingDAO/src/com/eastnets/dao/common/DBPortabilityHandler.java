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

package com.eastnets.dao.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Data Base Portability Handler, Used as adapter to handle differences between Oracle and MsSql Database
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class DBPortabilityHandler {

	public static final int DB_TYPE_ORACLE = 0;
	public static final int DB_TYPE_MSSQL = 1;
	private int dbType;
	private String dbTypeString;
	private boolean partitioned;
	private boolean viewerAmountZerosPadding;

	private double dbVersion;

	public double getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(double dbVersion) {
		this.dbVersion = dbVersion;
	}

	public DBPortabilityHandler() {
	}

	public String functionPrefix() {
		String pref = "";
		if (dbType == DB_TYPE_MSSQL) {
			pref = "dbo.";
		}
		return pref;
	}

	public String getSysDate() {
		String sysdate = "sysdate";
		if (dbType == DB_TYPE_MSSQL) {
			sysdate = "GETDATE()";
		}
		return sysdate;
	}

	/*
	 * If you need to set a field in the DB with a future or past day you can use this method that will return to you the portable SQL function with proper day, you can user minus value to get past
	 * date
	 */
	public String addDaysForCurrentDate(int noOfDays) {
		String sysdate = "trunc(sysdate + " + noOfDays + ")";
		if (dbType == DB_TYPE_MSSQL) {
			sysdate = "DATEADD(day, DATEDIFF(day, 0, GETDATE())," + noOfDays + ")";
		}
		return sysdate;
	}

	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public String getDbTypeString() {
		return dbTypeString;
	}

	public void setDbTypeString(String dbTypeString) {
		this.dbTypeString = dbTypeString;
		if (this.dbTypeString.equalsIgnoreCase("SQL")) {
			this.dbType = DB_TYPE_MSSQL;
		} else {
			this.dbType = DB_TYPE_ORACLE;
		}
	}

	public boolean isPartitioned() {
		return partitioned && dbType == DB_TYPE_ORACLE;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	public String getDayFromDate(String columnName) {

		String function = "to_char(" + columnName + ", 'dd/mm/yyyy')";
		if (dbType == DB_TYPE_MSSQL) {
			function = "CONVERT(char, " + columnName + ",103)"; // 103 means dd/mm/yyyy
		}

		return function;
	}

	public String getDayMonthFromDate(String columnName) {

		String function = "to_char(" + columnName + ", 'dd-mm')";
		if (dbType == DB_TYPE_MSSQL) {
			function = "CONVERT(varchar(5), " + columnName + ",105)"; // 105 dd-mm-yyyy
		}

		return function;
	}

	/**
	 * @param columnName
	 * @return yymmdd
	 */
	public String getDate6(String columnName) {

		String function = "to_char(" + columnName + ", 'yymmdd')";
		if (dbType == DB_TYPE_MSSQL) {
			function = "CONVERT(NVARCHAR, " + columnName + ",12)"; // 12 yymmdd
		}

		return function;
	}

	public String getDayMonthYearFromDate(String columnName) {

		String function = "to_char(" + columnName + ", 'dd-mm-yyyy')";
		if (dbType == DB_TYPE_MSSQL) {
			function = "CONVERT(varchar(10), " + columnName + ",102)"; // 105 dd-mm-yyyy
		}

		return function;
	}

	public String getDate(String date, String pattern) {
		if (dbType == DB_TYPE_MSSQL) {
			return " ? ";
		}

		return "To_DATE( ? , '" + pattern + "')";
	}

	public String getDateWithPatternNoBinding(String date, String pattern) {
		if (dbType == DB_TYPE_MSSQL) {
			return "'" + date + "'";
		}

		return "To_DATE('" + date + "', '" + pattern + "')";
	}

	public String getHourFromDate(String columnName) {

		String function = "substr(to_char(trunc(" + columnName + ",'HH24'),'" + Constants.ORACLE_DATE_TIME_PATTERN + "'),12,2)";

		if (dbType == DB_TYPE_MSSQL) {
			function = "DATEPART(hh, " + columnName + ")";
		}

		return function;
	}

	public String getSubStringFunctionName() {
		String function = "SUBSTR";
		if (dbType == DB_TYPE_MSSQL) {
			function = "SUBSTRING";
		}

		return function;
	}

	public String getFromDateToDateCondition(Date fromDate, Date toDate, String columnName) {
		if (dbType == DB_TYPE_MSSQL) {
			return getFromDateToDateCondition(formatDateTime(fromDate, true), formatDateTime(toDate, false), columnName, "");
		}

		return getFromDateToDateCondition(formatDate(fromDate), formatDate(toDate), columnName);
	}

	public String getOneDayCondition(String date, String columnName) {
		return getFromDateToDateCondition(date, date, columnName, Constants.ORACLE_DATE_TIME_PATTERN);
	}

	public String getOneDayCondition(Date date, String columnName) {
		if (dbType == DB_TYPE_MSSQL) {
			return getFromDateToDateCondition(formatDateTime(date, true), formatDateTime(date, false), columnName, "");
		}

		return getOneDayCondition(formatDate(date), columnName);
	}

	public String getOneDayEqualCondition(Date date, String columnName) {

		String formatedDate = formatDateTime(date);
		return getDateEqualCondition(formatedDate, columnName, Constants.ORACLE_DATE_TIME_PATTERN);
	}

	public String getOneDayLessthanOrEqualCondition(Date date, String columnName) {

		String formatedDate = formatDateTime(date);
		return getDateLessthanOrEqualCondition(formatedDate, columnName, Constants.ORACLE_DATE_TIME_PATTERN);
	}

	public String getOneDayGreatthanOrEqualCondition(Date date, String columnName) {

		String formatedDate = formatDateTime(date);
		return getDateGreaterthanOrEqualCondition(formatedDate, columnName, Constants.ORACLE_DATE_TIME_PATTERN);
	}

	public String getFromDateToDateCondition(String fromDate, String toDate, String columnName, String pattern) {
		StringBuilder function = new StringBuilder();
		function.append(columnName);
		function.append(" between ");

		if (dbType == DB_TYPE_MSSQL) {
			function.append("'");
			function.append(fromDate);
			function.append("'");
			function.append(" AND ");
			function.append("'");
			function.append(toDate);
			function.append("'");
		} else {
			function.append("to_date('");
			function.append(fromDate);
			function.append(" 00:00:00', '");
			function.append(pattern);
			function.append("')");
			function.append(" AND ");
			function.append("to_date('");
			function.append(toDate);
			function.append(" 23:59:59', '");
			function.append(pattern);
			function.append("'");
			function.append(")");
		}

		return function.toString();
	}

	public String getDateEqualCondition(String date, String columnName, String pattern) {
		StringBuilder function = new StringBuilder();
		function.append(columnName);
		function.append(" = ");

		if (dbType == DB_TYPE_MSSQL) {
			function.append("'");
			function.append(date);
			function.append("'");
		} else {
			function.append("to_date('");
			function.append(date);
			function.append("', '");
			function.append(pattern);
			function.append("')");
		}

		return function.toString();
	}

	public String getDateLessthanOrEqualCondition(String date, String columnName, String pattern) {
		StringBuilder function = new StringBuilder();
		function.append(columnName);
		function.append(" <= ");

		if (dbType == DB_TYPE_MSSQL) {
			function.append("'");
			function.append(date);
			function.append("'");
		} else {
			function.append("to_date('");
			function.append(date);
			function.append("', '");
			function.append(pattern);
			function.append("')");
		}

		return function.toString();
	}

	public String getDateGreaterthanOrEqualCondition(String date, String columnName, String pattern) {
		StringBuilder function = new StringBuilder();
		function.append(columnName);
		function.append(" >= ");

		if (dbType == DB_TYPE_MSSQL) {
			function.append("'");
			function.append(date);
			function.append("'");
		} else {
			function.append("to_date('");
			function.append(date);
			function.append("', '");
			function.append(pattern);
			function.append("')");
		}

		return function.toString();
	}

	public String getFromDateToDateCondition(String fromDate, String toDate, String columnName) {
		return getFromDateToDateCondition(fromDate, toDate, columnName, Constants.ORACLE_DATE_TIME_PATTERN);
	}

	private static String formatDate(Date date) {
		DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		return formatter.format(date);
	}

	private String formatDateTime(Date date) {
		DateFormat formatter = null;

		if (dbType == DB_TYPE_MSSQL) {
			formatter = new SimpleDateFormat(Constants.MSSQL_DATE_TIME_PATTERN);
		} else {
			formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		}

		return formatter.format(date);
	}

	public String buildRownumQuery(String query, String block, String condition, int rowNums) {
		String retValue = null;
		if (getDbType() == DB_TYPE_ORACLE) {
			retValue = String.format("%s %s %s AND ROWNUM = %d ", query, block, condition, rowNums);

		} else {
			retValue = String.format("%s Top %d %s %s", query, rowNums, block, condition);
		}

		return retValue;
	}

	public String addRowNumCondition(String query, String condition, String orderBy, int maximumRows) {
		StringBuilder fullQuery = new StringBuilder();
		if (getDbType() == DB_TYPE_ORACLE) {
			condition = condition.replace("where ", " WHERE rownum <= " + maximumRows + " and ");
		} else {
			query = query.replace("select", "select top " + maximumRows + " ");
		}

		fullQuery.append(query).append(condition).append(orderBy);
		return fullQuery.toString();
	}

	public String checkUpperSQL(String str, boolean caseSensitive) {
		if (caseSensitive) {
			return str;
		}
		if (getDbType() == DB_TYPE_MSSQL) {
			// With SQLServer UPPER take only parameter of varchar type and not of text type !
			return str;// sql server is by default is case insensitive, when we want to use it as caseInsensitive we should use ' COLLATE Latin1_General_CS_AS ' after the column name
		}
		return " UPPER(" + str + ")";

	}

	public String checkEmptyClobSQL(String str) {

		if (getDbType() == DB_TYPE_MSSQL) {

			return str + " is not null";
		}

		return " dbms_lob.getlength( " + str + ")  > 0 ";
	}

	/**
	 * @param date
	 *            date without time
	 * @param strart
	 *            if true then return the date with 00:00:00 otherwise 23:59:59
	 * @return date formated with setting the time.
	 */
	public String formatDateTime(Date date, boolean strart) {

		DateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (strart == true) {
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		} else {
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		}

		return formatter.format(cal.getTime());
	}

	public String getFormattedDate(Date date, Boolean withTime) {
		if (date == null) {
			return "";
		}
		String dateTime = "";
		if (withTime) {
			dateTime = " HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + dateTime);
		String dateStr = formatter.format(date);
		String pattern = "YYYYMMDD";
		if (withTime) {
			pattern += " HH24:MI:SS";
		}
		return getDate(dateStr, pattern);
	}

	public String getFormattedDateWithNoBinding(Date date, Boolean withTime) {
		if (date == null) {
			return "";
		}
		String dateTime = "";
		if (withTime) {
			dateTime = " HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + dateTime);
		String dateStr = formatter.format(date);
		String pattern = "YYYYMMDD";
		if (withTime) {
			pattern += " HH24:MI:SS";
		}
		return getDateWithPatternNoBinding(dateStr, pattern);
	}

	public String getCurrentDateFunction() {
		if (getDbType() == DB_TYPE_MSSQL) {

			return "getDate()";
		}
		return "SYSDATE";
	}

	public String getDataLengthFn() {
		if (getDbType() == DB_TYPE_MSSQL) {
			return "DataLength";
		}

		return "dbms_lob.getlength";
	}

	/**
	 * @return '||chr(13) ||chr(10) ||' for oracle and '+ char(13) + char(10) +' for mssql, this replaces the \r\n within a string
	 */
	public String getNewLineCharsWithConc() {
		if (getDbType() == DB_TYPE_ORACLE) {
			return "'||chr(13) ||chr(10) ||'";
		}

		return "'+ char(13) + char(10) +'";
	}

	public CharSequence getNewLineChar10WithConc() {
		if (getDbType() == DB_TYPE_ORACLE) {
			return "'|| chr(10) ||'";
		}

		return "'+ char(10) +'";
	}

	public String getNullValue(String field, String defaultValue) {
		if (getDbType() == DB_TYPE_ORACLE) {
			return "NVL (" + field + ", " + defaultValue + ")";
		}
		return "isnull (" + field + ", " + defaultValue + ")";
	}

	public String getSubStr(String field, int start, int length) {
		if (getDbType() == DB_TYPE_ORACLE) {
			return "substr(" + field + ", " + start + ", " + length + ")";
		}
		return "substring(" + field + ", " + start + ", " + length + ")";
	}

	public String getDateDifferenceInDays(String dateField1, String dateField2) {
		if (getDbType() == DB_TYPE_ORACLE) {
			return "EXTRACT(second FROM ( " + dateField2 + " - " + dateField1 + " ) )  / ( 24.0 * 60.0 * 60.0 ) " + "+ EXTRACT(minute FROM ( " + dateField2 + " - " + dateField1 + " ) )  / ( 24.0 * 60.0 ) " + "+ EXTRACT(hour FROM ( " + dateField2
					+ " - " + dateField1 + " ) )  / ( 24.0 ) " + "+ EXTRACT(day FROM ( " + dateField2 + " - " + dateField1 + " ) ) ";
		}
		return "DATEDIFF( second , " + dateField1 + ", " + dateField2 + " ) / ( 24.0 * 60.0 * 60.0 ) ";// using day instead of second will give rounded int value
	}

	public boolean isPartitionedDB() {
		return partitioned;
	}

	public boolean isViewerAmountZerosPadding() {
		return viewerAmountZerosPadding;
	}

	public void setViewerAmountZerosPadding(boolean viewerAmountZerosPadding) {
		this.viewerAmountZerosPadding = viewerAmountZerosPadding;
	}

}