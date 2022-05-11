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

package com.eastnets.test.monitoring.testcase;

import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.SortOrder;
import com.eastnets.domain.monitoring.Alliance;
import com.eastnets.domain.monitoring.AuditLog;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.ReportingDBInfo;
import com.eastnets.domain.monitoring.Statistics;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.test.BaseTest;

public class MonitoringTest extends BaseTest {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5377517321711686554L;

	@Test
	public void testGetUpdatedMessages() {
		List<UpdatedMessage> updatedMessages = this.getServiceLocater().getMonitoringService().getUpdatedMessages(getLoggedInUser(),SortOrder.descending, 1L, 25L);
		Assert.notNull(updatedMessages);
		Assert.notEmpty(updatedMessages);
	}

	@Test
	public void testGetStatistics() {
		Statistics statistics = this.getServiceLocater().getMonitoringService().getStatistics(getLoggedInUser());
		Assert.notNull(statistics);
	}
	
	@Test
	public void testGetTraces() {
		List<ErrorMessage> traces = this.getServiceLocater().getMonitoringService().getTraces(getLoggedInUser());
		Assert.notNull(traces);
		Assert.notEmpty(traces);		
	}
	
	@Test
	public void testGetTracesbyFilter() {
		List<ErrorMessage> traces = this.getServiceLocater().getMonitoringService().getTraces(getLoggedInUser(),"","Info",SortOrder.ascending,1L,100L);
		Assert.notNull(traces);
		Assert.notEmpty(traces);		
	}
	
	@Test
	public void testGetAuditLogs() {
		List<AuditLog> auditLogs = this.getServiceLocater().getMonitoringService().getAuditLogs("","","",null,null,null,"",SortOrder.descending,1L,100L);
		Assert.notNull(auditLogs);
		Assert.notEmpty(auditLogs);
	}
	
	@Test
	public void testGetReportingInfo() {
		ReportingDBInfo reportingDBInfo = this.getServiceLocater().getMonitoringService().getReportingDBInfo(getLoggedInUser());
		Assert.notNull(reportingDBInfo);
		Assert.notNull(reportingDBInfo.getDatabaseVersion());
		Assert.notNull(reportingDBInfo.getLastDatabaseUpdate());
	}
	
	@Test
	public void testGetAlliance() {
		Alliance alliance = this.getServiceLocater().getMonitoringService().getAlliance("side", "0");
		Assert.notNull(alliance);
		Assert.notNull(alliance.getInstanceName());
	}
	
}
