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

package com.eastnets.domain.dashboard;

import java.io.Serializable;

/**
 * MultiSeriesChartRecord POJO
 * @author EastNets
 * @since August 1, 2012
 */
public class MultiSeriesChartRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6046856334514854881L;
	private String chartCategoryName;
	private ChartRecord chartRecord = new ChartRecord();

	public String getChartCategoryName() {
		return chartCategoryName;
	}

	public void setChartCategoryName(String categoryName) {
		this.chartCategoryName = categoryName;
	}

	public ChartRecord getChartRecord() {
		return chartRecord;
	}

	public void setChartRecord(ChartRecord chartRecord) {
		this.chartRecord = chartRecord;
	}
}
