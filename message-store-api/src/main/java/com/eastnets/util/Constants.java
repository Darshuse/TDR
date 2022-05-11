package com.eastnets.util;

import java.io.Serializable;
import java.math.BigDecimal;

public final class Constants implements Serializable {

	private static final long serialVersionUID = -1800236994362014435L;

	//
	public static final String DATABASE_TYPE_ORACLE = "Oracle";
	public static final String DATABASE_TYPE_MSSQL = "MSSQL";

	// Default Values for Message Fields
	public static final String MESG_VALIDATION_REQUESTED = "VAL_NO_VALIDATION";
	public static final String MESG_VALIDATION_PASSED = "VAL_NO_VALIDATION";
	public static final String MESG_CLASS = "MESG_NORMAL";
	public static final BigDecimal MESG_IS_TEXT_READONLY = BigDecimal.ZERO;
	public static final BigDecimal MESG_IS_DELETE_INHIBITED = BigDecimal.ZERO;
	public static final BigDecimal MESG_IS_PARTIAL = BigDecimal.ZERO;
	public static final String MESG_NATURE = "FINANCIAL_MSG";
	public static final String MESG_SENDER_CORR_TYPE = "CORR_TYPE_ INSTITUTION";
	public static final Integer ARCHIVED = 0;
	public static final Integer RESTORED = 0;
	public static final BigDecimal MESG_LIVE = BigDecimal.ONE;
	public static final BigDecimal MESG_COMPLETED = BigDecimal.ZERO;
	public static final BigDecimal MESG_IS_RETRIEVED = BigDecimal.ONE;
	// public static final String MESG_NETWORK_APPL_IND = "PS";
	public static final String MESG_STATUS_COMPELTED = "COMPLETED";
	public static final String MESG_STATUS_LIVE = "LIVE";
	public static final String MESG_NETWORK_PRIORITY_NORMAL = "PRI_NORMAL";
	public static final String MESG_NETWORK_PRIORITY_URGENT = "PRI_URGENT";

	//
	public static final String MESG_SUB_FORMAT_I = "INPUT";
	public static final String MESG_SUB_FORMAT_O = "OUTPUT";

	// Default Values for Instance Fields
	public static final Integer INST_NUM = 0;
	public static final String INST_TYPE = "INST_TYPE_ORIGINAL";
	public static final String INST_NOTIFICATION_TYPE = "INST_NOTIFICATION_NONE";
	public static final BigDecimal INST_RELATED_NBR = BigDecimal.ZERO;
	public static final BigDecimal INST_APPE_SEQ_NBR = BigDecimal.ZERO;
	public static final BigDecimal INST_PROCESS_STATE = BigDecimal.ZERO;
	public static final String INST_LAST_MPFN_RESULT = "R_SUCCESS";
	public static final BigDecimal INST_RELATIVE_REF = BigDecimal.ZERO;
	public static final double INST_SM2000_PRIORITY = 7;
	public static final String INST_STATUS_COMPELTED = "COMPLETED";
	public static final String INST_STATUS_LIVE = "LIVE";

	// Default Values for Text Fields
	public static final BigDecimal TEXT_TOKEN = BigDecimal.ZERO;

	// Intervention Values
	public static final Integer INTV_TOKEN = 0;
	public static final String INTV_INSTANCE_NAME_COMPLETED = "Instance completed";
	public static final Integer INTV_INSTANCE_NO_COMPLETED = 2004;
	public static final String INTV_INSTANCE_NAME_CREATED = "Instance created";
	public static final Integer INTV_INSTANCE_NO_CREATED = 2000;
	public static final String INTV_INSTANCE_NAME_ROUTED = "Instance routed";
	public static final Integer INTV_INSTANCE_NO_ROUTED = 2001;

	public static final String INTV_INSTANCE_STATUS_COMPLETED = "Completed";
	public static final String INTV_INSTANCE_STATUS_CREATED = "created";
	public static final String INTV_INSTANCE_STATUS_ROUTED = "routed";

	public static final String INTV_INSTANCE_CATEGORY_ROUTED = "INTY_ROUTING";

	// User role
	public static final String USER_ROLE = "SIDE_CONNECTOR";

	// Product ID - license
	public static final String PRODUCT_ID = "30";

}
