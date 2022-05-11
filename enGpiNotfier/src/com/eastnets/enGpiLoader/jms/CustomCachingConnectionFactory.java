package com.eastnets.enGpiLoader.jms;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.springframework.jms.connection.CachingConnectionFactory;

public class CustomCachingConnectionFactory extends CachingConnectionFactory {

	public Connection createConnection(String username, String password)
			throws JMSException {
		return getTargetConnectionFactory().createConnection(username, password);
	}
}
