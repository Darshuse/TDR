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

package com.eastnets.test.dashboard.testcase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.eastnets.dao.common.Constants;
import com.eastnets.test.BaseTest;

public class DashboardTest extends BaseTest {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 544998169735278155L;

	@Test
	public void testMsgPerCountryChart() throws InstantiationException,
			IllegalAccessException {
		try {

			String xmlString = this.getServiceLocater().getDashboardService()
					.buildMessagesPerCountryChartXml(
							getLoggedInUser(),
							new SimpleDateFormat(Constants.ORACLE_DATE_PATTERN)
									.parse("24/07/2010"), new Date(), getLoggedGroupId());
			
			if(xmlString.startsWith("<graph") && xmlString.endsWith("</graph>")) {
				assert(true);
			} else {
				assert(false);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMsgPerCurrencyChart() throws InstantiationException,
			IllegalAccessException {
		try {

			String xmlString = this.getServiceLocater().getDashboardService()
					.buildMessagesPerCurrencyChartXml(
							getLoggedInUser(),
							new SimpleDateFormat(Constants.ORACLE_DATE_PATTERN)
									.parse("24/07/2010"), new Date(), getLoggedGroupId());
			
			if(xmlString.startsWith("<graph") && xmlString.endsWith("</graph>")) {
				assert(true);
			} else {
				assert(false);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMessagesCountPerValueDateChart() throws InstantiationException,
			IllegalAccessException {
		try {

			String xmlString = this.getServiceLocater().getDashboardService()
					.buildMessageCountPerCategoryChartXml(
							getLoggedInUser(),
							new SimpleDateFormat(Constants.ORACLE_DATE_PATTERN)
									.parse("24/07/2010"), new Date(), getLoggedGroupId());
			
			if(xmlString.startsWith("<graph") && xmlString.endsWith("</graph>")) {
				assert(true);
			} else {
				assert(false);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMessagesPerLogicalTerminalChart() throws InstantiationException,
			IllegalAccessException {
		try {

			String xmlString = this.getServiceLocater().getDashboardService()
					.buildSendRecievedMessagesPerLogicalTerminalChartXml(
							getLoggedInUser(),
							new SimpleDateFormat(Constants.ORACLE_DATE_PATTERN)
									.parse("24/07/2010"), new Date(), getLoggedGroupId());
			
			if(xmlString.startsWith("<graph") && xmlString.endsWith("</graph>")) {
				assert(true);
			} else {
				assert(false);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAmountPerCategoryChart() throws InstantiationException,
			IllegalAccessException {
		try {

			String xmlString = this.getServiceLocater().getDashboardService()
					.buildAmountPerCategoryChartXml(
							getLoggedInUser(),
							new SimpleDateFormat(Constants.ORACLE_DATE_PATTERN)
									.parse("24/07/2010"), new Date(), "USD", getLoggedGroupId());
			
			if(xmlString.startsWith("<graph") && xmlString.endsWith("</graph>")) {
				assert(true);
			} else {
				assert(false);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNotificationsChart() throws InstantiationException,
			IllegalAccessException {
		try {

			String xmlString = this.getServiceLocater().getDashboardService()
					.buildNotificationsChartXml(
							getLoggedInUser(),
							new SimpleDateFormat(Constants.ORACLE_DATE_PATTERN)
									.parse("24/07/2010"), new Date(), "Message");
			
			if(xmlString.startsWith("<graph") && xmlString.endsWith("</graph>")) {
				assert(true);
			} else {
				assert(false);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
