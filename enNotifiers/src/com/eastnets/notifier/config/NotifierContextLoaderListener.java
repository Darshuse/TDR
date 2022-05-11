package com.eastnets.notifier.config;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class NotifierContextLoaderListener extends ContextLoaderListener implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2739275205020674241L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		LoggingConfig.init();
		LoggingConfig.getLogger().info("Notfier Logger Initialized");
	}
}
