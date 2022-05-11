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

package com.eastnets.service.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.domain.dashboard.MultiSeriesChartRecord;

/**
 * Dashboard Service Interface
 * @author EastNets
 * @since July 22, 2012
 */
@Transactional (propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public interface DashboardService {
	
	/**
	 * Get the XMl that is representing the Messages Per Country chart
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @return String
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public String buildMessagesPerCountryChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the XMl that is representing the Messages Per Currency chart
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @return String
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public String buildMessagesPerCurrencyChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the XMl that is representing the Amount Per Category chart based on the given currency
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @param currency
	 * @return String
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public String buildAmountPerCategoryChartXml(String loggedInUser, Date dateFrom, Date dateTo, String currency, long loggedUserGroupID);
	
	/**
	 * Get the XMl that is representing the Send Recieved MessagesPerLogicalTerminal chart
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @return String
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public String buildSendRecievedMessagesPerLogicalTerminalChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the nodes that is representing the Message Count Per Category chart
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @return Map<String,MultiSeriesChartRecord>
	 * @author EastNets
	 * @since: Mar 18, 2013
	 */
	public Map<String,List<MultiSeriesChartRecord>> getMessagesCountPerValueDate(String loggedInUser,	Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * 
	 * @param loggedInUser
	 * @param category
	 * @param MultiSeriesChartRecordList
	 * @return
	 */
	public String buildMessageCountPerCategoryChartXml(String loggedInUser, String category, List<MultiSeriesChartRecord> MultiSeriesChartRecordList);
	
	/**
	 * Get the XMl that is representing the Message Count Per Category chart
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @return String
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public String buildMessageCountPerCategoryChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID);
	
	/**
	 * Get the XMl that is representing the Notifications chart based on the given notification type
	 * @param profile
	 * @param dateFrom
	 * @param dateTo
	 * @param notificationType
	 * @return String
	 * @author EastNets
	 * @since: Aug 8, 2012
	 */
	public String buildNotificationsChartXml(String loggedInUser, Date dateFrom, Date dateTo, String notificationType);
	
	/**
	 * Get the XMl that is representing the loader New Messages chart based on the given aid
	 * @param profile
	 * @param insertionDate
	 * @param aid
	 * @return
	 * @author EastNets
	 * @since: Aug 14, 2012
	 */
	public String buildLoaderNewMessagesChartXml(String loggedInUser, Date date, String aid);

	/**
	 * Get the XMl that is representing the loader New Events chart based on the given aid
	 * @param profile
	 * @param date
	 * @param aid
	 * @return String
	 * @author EastNets
	 * @since: Aug 14, 2012
	 */
	public String buildLoaderNewEventsChartXml(String loggedInUser, Date date, String aid);
	
	/**
	 * Get the XMl that is representing the loader Updated Messages chart based on the given aid
	 * @param profile
	 * @param date
	 * @param aid
	 * @return String
	 * @author EastNets
	 * @since: Aug 14, 2012
	 */
	public String buildLoaderUpdateMessagesChartXml(String loggedInUser, Date date, String aid);
	
	/**
	 * 
	 * @author MAlkhateeb
	 * @return return empty xml graph that well be used when the customer decide to load the graph data lazily
	 */
	public String buildEmptyGraph();
}
