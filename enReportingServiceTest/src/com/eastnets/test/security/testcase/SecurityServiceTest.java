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

package com.eastnets.test.security.testcase;

import org.junit.Test;

import com.eastnets.domain.admin.User;
import com.eastnets.test.BaseTest;


public class SecurityServiceTest extends BaseTest {
    
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8689706000963209002L;

	@Test
    public void testLdapAuth() throws Exception{
    	User user = new User();
    	user.setUserName("userName");
    	user.setPassword("password");
    	this.getServiceLocater().getSecurityService().authenticate(user,"ldap",false);
    }
    
}
