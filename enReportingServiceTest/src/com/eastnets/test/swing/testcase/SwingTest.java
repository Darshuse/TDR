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

package com.eastnets.test.swing.testcase;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

import com.eastnets.domain.swing.SwingEntity;
import com.eastnets.domain.swing.UserFin;
import com.eastnets.domain.swing.UserStatus;
import com.eastnets.test.BaseTest;

public class SwingTest extends BaseTest{

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2340834450003147897L;

	@Test
    public void testDoSearch() throws InstantiationException, IllegalAccessException{
    	SwingEntity swing = new SwingEntity();
    	swing.setFromFinAmount(0);
    	swing.setToFinAmount(100000);
    	swing.setFromFinValueDate(new Date());
    	swing.setToFinValueDate(new Date());
    	swing.setFromEAITimeStamp(new Date());
    	swing.setToEAITimeStamp(new Date());
    	
    	UserFin fin = new UserFin();
    	fin.setFinCorr("corr");
    	fin.setFinCurr("finCurr");
    	fin.setFinIsnOsn("ISN/OSN");
    	fin.setFinMesgCreaDateTime(new Date());
    	fin.setFinMesgNetwStatus("MesgNetwStatus");
    	fin.setFinMesgStatus("finMesgStatus");
    	fin.setFinMsgType("MsgType");
    	fin.setFinOwnDist("finOwnDist");
    	fin.setFinSwiftIO("finSwiftIO");
    	fin.setFinTrnRef("finTrnRef");
    	fin.setFinValueDate(new Date());
    	fin.setInternalTimeStamp(new Date());
    	
    	UserStatus status = new UserStatus();
    	status.setBic("bic");
    	status.setCategory("category");
    	status.setEaiComment("eaiComment");
    	status.setEaiErrorCode("eaiErrorCode");
    	status.setEaiMessageId("eaiMessageId");
    	status.setEaiMessageSource("eaiMessageSource");
    	status.setEaiOperator("eaiOperator");
    	status.setEaiStatus("eaiStatus");
    	status.setEaiTimeStamp(new Timestamp(new Date().getTime()));
    	status.setEaiUniqueIdentifier("eaiUniqueIdentifier");
    	status.setExternIdentifier("externIdentifier");
    	status.setId(10);
    	status.setInternalTimeStamp(new Date());
    	status.setQualifier("qualifier");
    	status.setSourceName("sourceName");
    	status.setStandard("standard");
    	status.setUnit("unit");
    	
    	swing.setUserFin(fin);
    	swing.setUserStatus(status);
    	this.getServiceLocater().getSwingService().doSearch(getLoggedInUser(),swing);
    }
 
    
}
