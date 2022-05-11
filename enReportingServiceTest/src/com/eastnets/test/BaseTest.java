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

package com.eastnets.test;

import com.eastnets.application.BaseApp;
import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.config.PortNumberRangeException;

public class BaseTest extends BaseApp  {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7946324728185355230L;
	public static ConfigBean configBean;
    
    static{
    	configBean = new ConfigBean();
//    	configBean.setDatabaseName("SideDB");
    	configBean.setDbServiceName("REP32PART");
    	configBean.setServerName("192.168.100.152");
    	try{
    		configBean.setPortNumber("1522");
    	} catch ( Exception exception){
    		
    	}
    	configBean.setUsername("side");
    	configBean.setPassword("edis");
    	configBean.setSchemaName("side");
    	configBean.setTableSpace("sidedb");
    	configBean.setDatabaseType(DBType.ORACLE);
    	configBean.setReportsDirectoryPath("C:\\EnReporting\\branches\\3.0.1\\WebClient3.0\\Reports");
    }
    
    
    
    public BaseTest(){
    	try {
			this.init(BaseTest.configBean);
		} catch (PortNumberRangeException e) {
			e.printStackTrace();
		}
    }
    
    protected String getLoggedInUser(){
    	return "side";
    }
    protected Long getLoggedInUserId(){
    	return 174L;
    }
    protected Long getLoggedGroupId(){
    	return 1L;
    }
}
