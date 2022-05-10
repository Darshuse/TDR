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
package com.eastnets.resilience.xmldump.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.eastnets.resilience.xmldump.GlobalConfiguration;

/**
 * JUL wrapper (it could be .property file)
 * 
 * @author EHakawati
 * 
 */
public class Logging {

	private final static QuickFormatter formatter = new QuickFormatter();
	private final static Handler handler = new ConsoleHandler();
	private static Level logLevel = Level.OFF;

	/**
	 * Static initialize block
	 */
	static {

		LogManager.getLogManager().reset();
		handler.setFormatter(Logging.formatter);

		GlobalConfiguration configuration = GlobalConfiguration.getInstance();

		if (configuration.getLogLevel() != null) {
			logLevel = Level.parse(configuration.getLogLevel());
		} else {
			if (GlobalConfiguration.getInstance().isDebug()) {
				logLevel = Level.ALL;
			} else {
				logLevel = Level.INFO;
			}
		}
		handler.setLevel(logLevel);
	}

	/**
	 * Private constructor
	 */
	private Logging() {
	}

	/**
	 * change project log level
	 * 
	 * @param level
	 */
	public static void changeLogLevel(String level) {
		logLevel = Level.parse(level);
		handler.setLevel(logLevel);
	}

	/**
	 * Create a new logger object
	 * 
	 * @param clazzName
	 * @return
	 */
	public static Logger getLogger(String clazzName) {

		Logger logger = Logger.getLogger(clazzName);
		logger.addHandler(handler);
		logger.setLevel(logLevel);
		return logger;
	}
}
