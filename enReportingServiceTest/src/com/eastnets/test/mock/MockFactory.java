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

package com.eastnets.test.mock;

import java.sql.Timestamp;
import java.util.Date;

import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.watchdog.EventRequest;
import com.eastnets.domain.watchdog.MessageRequest;

public class MockFactory {
@SuppressWarnings("rawtypes")
public static Object getIntance(Class  cl) throws InstantiationException, IllegalAccessException{
	if(cl.getName().equals("com.eastnets.domain.watchdog.MessageRequest")){
		MessageRequest request = (MessageRequest)cl.newInstance();
		request.setDescription("from Test Case");
		request.setMsgType("103");
		request.setAutoDelete(false);
		request.setGroupRequst(false);
		request.setAmountOp(-1L);
		request.setDateValueOp(-1L);
		request.setStatus(0L);
		request.setEmail("mjumaa@eastnets.com");
		return request;
	}else if(cl.getName().equals("com.eastnets.domain.UserProfile")){
//		return new UserProfile("side");
	}else if(cl.getName().equals("com.eastnets.domain.admin.Profile")){
		return new User("SIDE");
	}else if(cl.getName().equals("com.eastnets.domain.watchdog.EventRequest")){
		EventRequest request = (EventRequest)cl.newInstance();
		request.setComponentName("BSS");
		request.setDescription("Desc");
		request.setEventNbr("12");
		request.setExpirationDate(new Timestamp(new Date().getTime()));
		return request;
	}else if(cl.getName().equals("com.eastnets.domain.CorrInfo")){
		CorrInfo corrInfo = new CorrInfo();
		corrInfo.setCorrType(CorrInfo.CORR_TYPE_INSTITUTION);
		corrInfo.setCorrX1("BNPAFRPPXXX");
		return corrInfo;
	}
	return null;
}



}
