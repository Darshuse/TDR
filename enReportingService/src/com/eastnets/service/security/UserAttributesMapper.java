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

package com.eastnets.service.security;

import java.io.Serializable;

import javax.naming.directory.Attributes;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;

/**
 * User Attributes Mapper
 * @author EastNets
 * @since September 3, 2012
 */
public class UserAttributesMapper implements Serializable, AttributesMapper {
	 
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2984878739798700986L;

	
	protected static final Logger LOGGER = LogManager.getLogger(UserAttributesMapper.class);

	@Override
	public String mapFromAttributes(Attributes attributes) throws NamingException {
 
//		User userObject = new User();
 
		String commonName = "";
		try {
			commonName = (String)attributes.get("cn").get();
			LOGGER.info(commonName);
			
		} catch (javax.naming.NamingException e) {
			e.printStackTrace();
		}

		return commonName;
	}
 
}