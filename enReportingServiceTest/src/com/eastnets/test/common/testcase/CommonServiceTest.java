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

package com.eastnets.test.common.testcase;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.UserSettings;
import com.eastnets.test.BaseTest;
import com.eastnets.test.mock.MockFactory;


public class CommonServiceTest extends BaseTest{
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 2001280709993993880L;

	@Test
    public void testGetUserSettings(){
    	UserSettings settings = this.getServiceLocater().getCommonService().getUserSettings(getLoggedInUser());
    	System.out.println(settings.getEmail());
    	System.out.println(settings.getRpDirectory());
    	System.out.println(settings.getVwListDepth());
    	System.out.println(settings.getWdNbDayHistory());
    }
    
  
    
    @Test
    public void testCanUserCreateGroupRequest(){
    	this.getServiceLocater().getCommonService().canCreateGroupRequest(getLoggedInUser());
    }    

    @Test
    public void testGetCorrInfo() throws InstantiationException, IllegalAccessException{
    	CorrInfo corrInfo = (CorrInfo)MockFactory.getIntance(CorrInfo.class);

    	corrInfo.setCorrType("CORR_TYPE_INSTITUTION");
    	corrInfo.setCorrX1("PTSABEM0XXX");
    	corrInfo.setCorrX3("Ben Laden");
    	corrInfo.setCorrX4("Osama");
    	Calendar c = Calendar.getInstance();
    	c.set(2012, 10, 9, 13, 42, 57);
    	corrInfo.setMesgDate(c.getTime());
    	
    	this.getServiceLocater().getCommonService().getCorrInfo(getLoggedInUser(), corrInfo);
    	Assert.isTrue(corrInfo.getRetstatus() == 1);
    }
    
    @Test
    public void testGetCorrInfoString() throws InstantiationException, IllegalAccessException{
    	CorrInfo corrInfo = (CorrInfo)MockFactory.getIntance(CorrInfo.class);
    	
    	corrInfo.setCorrType("CORR_TYPE_INSTITUTION");
    	corrInfo.setCorrX1("PTSABEM0XXX");
    	corrInfo.setCorrX3("Ben Laden");
    	corrInfo.setCorrX4("Osama");
    	Calendar c = Calendar.getInstance();
    	c.set(2012, 10, 9, 13, 42, 57);
    	corrInfo.setMesgDate(c.getTime());
    	
    	String val= this.getServiceLocater().getCommonService().getCorrInfoString(getLoggedInUser(), corrInfo);
    	Assert.notNull(val);
    	Assert.hasText(val);
    }   
}
