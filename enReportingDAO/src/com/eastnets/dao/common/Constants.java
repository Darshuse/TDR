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

import java.io.Serializable;

/**
 * Constants
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class Constants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7289689144505985750L;
	// Application Utils class dates formats
	public static String DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static String DATE_FORMAT = "MM/dd/yyyy";
	public static String TIME_FORMAT = "HH:mm:ss";

	public static final String MSSQL_DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";
	public static final String ARCHIVE_DATE_PATTERN = "dd/MM/yyyy";

	public static String REPORT_DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm:ss a";
	public static String REPORT_DATE_FORMAT = "MM/dd/yyyy";
	public static String REPORT_TIME_FORMAT = "hh:mm:ss a";

	// Approval status
	public static final long UNAPPROVED_WAITING_RSA_AND_LSA_APPROVAL = 3;
	public static final long RSA_APPROVED_WAITING_LSA_APPROVAL = 2;
	public static final long LSA_APPROVED_WAITING_RSA_APPROVAL = 1;
	public static final long APPROVED = 0;

	// date pattern DAO format
	public static final String ORACLE_DATE_PATTERN = "MM/dd/yyyy";
	public static final String ORACLE_DATE_TIME_PATTERN = "MM/dd/yyyy HH24:mi:ss";

	public static final String ORACLE_DATE_TIME_PATTERN2 = "yyyy/MM/dd HH24:mi:ss";
	public static final String ORACLE_TIME_PATTERN = "HH24:mi:ss";

	public static final String SQL_DATE_PATTERN = "yyyy/MM/dd";
	public static final String SQL_DATE_TIME_PATTERN = "yyyy/MM/dd HH24:mi:ss";

	// MODULE IDs (PROGRAMM IDs) (SELECT Id FROM sPrograms)
	public static final int MODULE_ID_WATCHDOG = 0;
	public static final int MODULE_ID_REPORTING = 1;
	public static final int MODULE_ID_VIEWER = 2;
	public static final int MODULE_ID_EVENTS = 4;
	public static final int MODULE_ID_DASHBOARD = 5;
	public static final int MODULE_ID_Business_Integellence = 7;

	public static final int MESSAGE_ARCHIVE_ID = 1;
	public static final int MESSAGE_RESTORE_ID = 2;
	public static final int MESSAGE_SWIFT_RESTORE_ID = 3;
	public static final int EVENT_ARCHIVE_ID = 4;
	public static final int EVENT_RESTORE_ID = 5;
	public static final int EVENT_SWIFT_RESTORE_ID = 6;

	// ID of Viewer Security Objects
	public static final int SOBJ_VIEWER_MODIFY_SETTINGS = 1;
	public static final int SOBJ_VIEWER_PRINT_MESSAGES = 2;
	public static final int SOBJ_VIEWER_EXPORT_MESSAGES = 3;
	public static final int SOBJ_VIEWER_MAIL_TO = 4;
	public static final int SOBJ_VIEWER_COUNT = 5;

	// ID of WatchDog Security Objects
	public static final int SOBJ_WD_CREATE_RECEIVE_USER_NOTIFICATION = 2;
	public static final int SOBJ_WD_RECEIVE_NACK_NOTIFICATION = 3;
	public static final int SOBJ_WD_RECEIVE_POSSIBLE_DUPLICATE_NOTIFICATION = 4;
	public static final int SOBJ_WD_RECEIVE_ISN_GAPS_NOTIFICATION = 5;
	public static final int SOBJ_WD_RECEIVE_OSN_GAPS_NOTIFICATION = 6;
	public static final int SOBJ_WD_RECEIVE_CALCULATED_DUPLICATE_NOTIFICATION = 7;
	public static final int SOBJ_WD_PROCESS_SYSTEM_NOTIFICATION = 8;
	public static final int SOBJ_WD_DELETE_SYSTEM_NOTIFICATION = 9;
	public static final int SOBJ_WD_CREATE_PERMANENT_USER_REQUEST = 10;
	public static final int SOBJ_WD_CREATE_GROUP_REQUEST = 11;
	public static final int SOBJ_WD_CREATE_RECEIVE_EVENT_NOTIFICATION = 12;
	public static final int SOBJ_WD_CREATE_PERMANENT_EVENT_NOTIFICATION = 13;

	// ID of DASHBOARD Security Objects
	public static final int SOBJ_DASHBOARD_OPEN_TRAFFIC_ANALIZER = 51;
	public static final int SOBJ_DASHBOARD_OPEN_PERFORMANCE_ANALIZER = 52;
	public static final int SOBJ_DASHBOARD_OPEN_WATCHDOG_ANALIZER = 53;

	public static final String ARCHIVE_BINRAY_NAME = "Archive";
	public static final String RESTORE_BINRAY_NAME = "Restore";

	public static final String ARCHIVE_JRNL_BINRAY_NAME = "JrnlArchive";
	public static final String RSA_USER_NAME = "RSA";
	public static final String LSA_USER__NAME = "LSA";

	public static final long APPLICATION_ARCHICVE_OPTIONS = 0;
	public static final long APPLICATION_MESG_ARCHICVE_SETTING = 1;
	public static final long APPLICATION_JRNL_ARCHICVE_SETTING = 2;
	public static final long APPLICATION_WATCHDOG_GENERAL_SETTINGS = 3;
	public static final long APPLICATION_WATCHDOG_NOTIFICATION_SETTINGS = 4;
	public static final long APPLICATION_REPORTING_OPTIONS = 4;
	public static final long APPLICATION_Viewer_OPTIONS = 5;
	public static final long APPLICATION_JOURNAL_OPTIONS = 6;

	public static final long ARCHIVE_STATUS_RUNNING = 0;
	public static final long ARCHIVE_STATUS_SUCCESS = 1;
	public static final long ARCHIVE_STATUS_FAILED = 2;

	public static final int WATCHDOGE_FILTER_NO_FILTER = 0;
	public static final int WATCHDOGE_FILTER_MESSAGE_NOTIFICATIONS = 1;
	public static final int WATCHDOGE_FILTER_CALCULATE_DUPLICATES = 2;
	public static final int WATCHDOGE_FILTER_POSSIBLE_DUPLICATES = 3;
	public static final int WATCHDOGE_FILTER_ISN_GAPS = 4;
	public static final int WATCHDOGE_FILTER_OSN_GAPS = 5;
	public static final int WATCHDOGE_FILTER_NAKED_MESSAGES = 6;
	public static final int WATCHDOGE_FILTER_EVENT = 7;

	public static final String STORED_PROCEDURE_CALL = "STORED_PROC_CALL";
	public static final String EXPORT_FORMAT_PARAM_NAME = "P_EXPORTFORMAT";
	public static final String CURRENCIES_MAP = "P_CURRENCIESMAP";

	public static final String JASPER_ORACLE_ROW_LIMIT_PARAM_NAME = "P_ORACLEROWNUMCONDITION";
	public static final String JASPER_SQLSERVER_ROW_LIMIT_PARAM_NAME = "P_SQLSERVERROWNUMCONDITION";
	public static final String AMOUNT_FORMAT = "AmountPattern";
	public static final String JASPER_DB_PREFIX_PARAM_NAME = "P_DBPREFIX";
	public static final String JASPER_PREFIX_LOGO_NAME = "P_LOGO_PATH";
	public static final String JASPER_REPORT_DIR = "P_REPORTDIR";
	public static final String JASPER_REPORT_DATE_PATTERN = "P_DATEPATTERN";
	public static final String JASPER_REPORT_DATE_TIME_PATTERN = "P_DATETIMEPATTERN";
	public static final String JASPER_REPORT_TIME_PATTERN = "P_TIMEPATTERN";
	public static final String JASPER_REPORT_RESOURCE_BUNDLE = "REPORT_RESOURCE_BUNDLE";

	public static final int REPORT_PARAMTER_TYPE_STRING = 1;
	public static final int REPORT_PARAMTER_TYPE_DATE = 2;
	public static final int REPORT_PARAMTER_TYPE_DATE_TIME = 3;
	public static final int REPORT_PARAMTER_TYPE_CURRENCY_AMOUNT = 4;
	public static final int REPORT_PARAMTER_TYPE_NUMBER = 5;
	public static final int REPORT_PARAMTER_TYPE_RESOURCE_BUNDLE = 6;
	public static final int REPORT_PARAMTER_TYPE_MESSAGE_NAME = 7;
	public static final int REPORT_PARAMTER_TYPE_CURRENCIES_MAP = 8;

	public static final String REPORTING_ACTION_MODIFY_PARAMETETRS = "3";
	public static final String REPORTING_ACTION_SAVE_PARAMETETRS = "4";
	public static final String REPORTING_ACTION_EXPORT_REPORT = "6";
	public static final String REPORTING_ACTION_DELETE_PARAMETETRS = "7";
	public static final String REPORTING_ACTION_TOGOLE_REPORT_DETAILS = "8";
	public static final String REPORTING_ACTION_VIEW_CAHANGE_OPTIONS = "9";
	public static final String REPORTING_ACTION_TOGGLE_REPORT_TREE_VIEW = "10";
	public static final String REPORTING_ACTION_EDIT_REPORT_DATE_FILE = "11";

	public static final String WATCHDOG_ACTION_CREATE_RECEIVE_USER_NOTIFICATION = "2";
	public static final String WATCHDOG_ACTION_RICIEVE_NACKED_NOTIFICATION = "3";
	public static final String WATCHDOG_ACTION_RECEIVE_POSSIBLE_DUPLICATE_NOTIFICATION = "4";
	public static final String WATCHDOG_ACTION_RECEIVE_ISN_GAPS_NOTIFICATION = "5";
	public static final String WATCHDOG_ACTION_RECEIVE_OSN_NOTIFICATION = "6";
	public static final String WATCHDOG_ACTION_RECEIVE_CALCULATED_DUPLICATE_NOTIFICATION = "7";
	public static final String WATCHDOG_ACTION_PROCESS_SYSTEM_NOTIFICATION = "8";
	public static final String WATCHDOG_ACTION_DELETE_SYSTEM_NOTIFICATION = "9";
	public static final String WATCHDOG_ACTION_CREATE_PERMANENT_USER_REQUEST = "10";
	public static final String WATCHDOG_ACTION_CREATE_GROUP_REQUEST = "11";
	public static final String WATCHDOG_ACTION_CREATE__RECIEVE_EVENT_NOTIFICATION = "12";
	public static final String WATCHDOG_ACTION_CREATE_PAREMANENT_EVENT_NOTIFICATION = "13";

	// public static final String VIEWER_ACTION_MODIFY_SETTINGS = "1";//this was commented because we don't need it any more
	public static final String VIEWER_ACTION_PRINT_MESSAGES = "2";
	public static final String VIEWER_ACTION_EXPORT_MESSAGES = "3";
	public static final String VIEWER_ACTION_MAIL_TO = "4";
	public static final String VIEWER_ACTION_COUNT_MESSAGES = "5";
	public static final String VIEWER_ACTION_SEARCH_BY_SUMID = "6";
	public static final String VIEWER_ACTION_GENERATE_REPORTS = "7";
	public static final String VIEWER_MESSAGE_NOTES = "8";
	public static final String VIEWER_MESSAGE_GPI = "9";
	public static final String VIEWER_MESSAGE_TEXT = "10";
	public static final String VIEWER_MASK_INFORMATION = "11";

	public static final String BI_CREATE_DASHBORD = "0";
	public static final String BI_VIEW_DASHBORD = "1";

	public static final String BI_CREATE_CONNECTIVITY = "2";
	public static final String BI_CREATE_DOMAIN = "3";

	public static final String BI_CREATE_ADHOC = "4";
	public static final String BI_VIEW_ADHOC = "5";

	public static final String BI_EDIT_RESOURCE = "6";

	public static final int LICENSE_OK = 0;
	public static final int LICENSE_WARNING = 1;
	public static final int LICENSE_ERROR = 2;
	// comma separated
	public static final String LICENSE_WARNING_MONTH_COUNT = "3,4";
	// only 1 digit
	public static final String LICENSE_ERROR_MONTH_COUNT = "5";

	public static final String NULL_VALUE = "N/V";

	public static final String FORCE_SESSION_INVALIDATE = "force";
	public static final String LOGOUT_SESSION_INVALIDATE = "logout";
	public static final String TIMEOUT_SESSION_INVALIDATE = "timeout";

}
