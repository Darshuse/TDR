package com.eastnets.domain.reporting;

import com.eastnets.dao.common.Constants;

public class ReportSetParameterTypes {

	public static void register() {
		ReportSetParameterTypeFactory.register((long) Constants.REPORT_PARAMTER_TYPE_DATE, ReportSetParameterDate.class);
		ReportSetParameterTypeFactory.register((long) Constants.REPORT_PARAMTER_TYPE_DATE_TIME, ReportSetParameterDateTime.class);
		ReportSetParameterTypeFactory.register((long) Constants.REPORT_PARAMTER_TYPE_STRING, ReportSetParameterString.class);
		ReportSetParameterTypeFactory.register((long) Constants.REPORT_PARAMTER_TYPE_CURRENCY_AMOUNT, ReportSetParameterString.class);
		ReportSetParameterTypeFactory.register((long) Constants.REPORT_PARAMTER_TYPE_RESOURCE_BUNDLE, ReportSetParameterResourceBundle.class);
		ReportSetParameterTypeFactory.register((long) Constants.REPORT_PARAMTER_TYPE_MESSAGE_NAME, ReportSetParameterResourceBundle.class);
		ReportSetParameterTypeFactory.register(ReportSetParameterString.class);
		
	}

}
