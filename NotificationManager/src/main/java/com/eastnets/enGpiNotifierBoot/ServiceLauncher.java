package com.eastnets.enGpiNotifierBoot;


import org.apache.log4j.Logger;

import com.eastnets.enGpiNotifer.source.MessageSource;
import com.eastnets.enGpiNotifer.utility.DataSourceParser; 

/**
 * @author MKassab
 * 
 */
public class ServiceLauncher {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 5275049801457064994L;
	private static final Logger LOGGER = Logger.getLogger(ServiceLauncher.class); 
	
	public void startGpiUpdateStatusJob(DataSourceParser dataSourceParser ,MessageSource messageSource) throws Exception {
		messageSource.startSourceMonotoring(dataSourceParser);
	}
}