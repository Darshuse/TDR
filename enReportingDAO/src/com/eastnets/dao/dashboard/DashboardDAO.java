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

package com.eastnets.dao.dashboard;

import java.util.Date;
import java.util.List;

import com.eastnets.domain.dashboard.ChartRecord;
import com.eastnets.domain.dashboard.MultiSeriesChartRecord;

/**
 * Dashboard DAO Interface 
 * @author EastNets
 * @since July 22, 2012
 */
public interface DashboardDAO {
	
	/**
	 * Get the data for Messages Count Per Country chart
	 * @param dateFrom
	 * @param dateTo
	 * @return List<ChartRecord> 
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<ChartRecord> getMessagesCountPerCountry(Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the data for Messages Count Per Currency chart
	 * @param dateFrom
	 * @param dateTo
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<ChartRecord> getMessagesCountPerCurrency(Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the data for Amount Per Category chart
	 * @param dateFrom
	 * @param dateTo
	 * @param currency
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<ChartRecord> getAmountPerCategory(Date dateFrom, Date dateTo, String currency, long loggedUserGroupID);
	
	/**
	 * Get the data for Messages Count Per Category chart
	 * @param dateFrom
	 * @param dateTo
	 * @return List<MultiSeriesChartRecord>
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<MultiSeriesChartRecord> getMessageCountPerCategory(Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the data for Sent Received Messages Count Per Logical terminal chart
	 * @param dateFrom
	 * @param dateTo
	 * @return List<MultiSeriesChartRecord> 
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<MultiSeriesChartRecord> getSentReceivedMessagesCountPerLogicalTerminal(Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the data for Event Notifications chart
	 * @param dateFrom
	 * @param dateTo
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<ChartRecord> getEventNotifications(Date dateFrom, Date dateTo);
	
	/**
	 * Get the data for Message Notifications chart
	 * @param dateFrom
	 * @param dateTo
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public List<ChartRecord> getMessageNotifications(Date dateFrom, Date dateTo);
	
	/**
	 * Get the data for Loader New Events chart
	 * @param insertionDate
	 * @param aid
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 14, 2012
	 */
	public List<ChartRecord> getLoaderNewEvents(Date insertionDate, String aid);

	/**
	 * Get the data for Loader New Messages chart
	 * @param insertionDate
	 * @param aid
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 14, 2012
	 */
	public List<ChartRecord> getLoaderNewMessages(Date insertionDate, String aid);
	
	/**
	 * Get the data for Loader Updated Messages chart
	 * @param insertionDate
	 * @param aid
	 * @return List<ChartRecord>
	 * @author EastNets
	 * @since: Aug 14, 2012
	 */
	public List<ChartRecord> getLoaderUpdatedMessages(Date insertionDate, String aid);
	
}