package com.eastnets.enGpiNotifer.JMS;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;


public class CustomCachingConnectionFactory extends CachingConnectionFactory {

	 
	public Connection createConnection(String username, String password)
			throws JMSException {
		return getTargetConnectionFactory().createConnection(username, password);
	}
}
