package com.eastnets.resilience.toolmonitor;

import org.w3c.dom.Element;

import com.eastnets.domain.csm.ClientServerConnection;
import com.eastnets.service.ServiceLocator;

public interface NotificationHandler {
	
	/**
	 * handle the passed connection configuration
	 * @param config
	 * @param serviceLocator 
	 */
	public void handle( ClientServerConnection config, ServiceLocator serviceLocator ) ;
	
	
	/**
	 * loads the settings from the passed file
	 * @param config
	 * @return false if loading failed, true otherwise
	 */
	public boolean loadSettings( Element config ) ;
}
