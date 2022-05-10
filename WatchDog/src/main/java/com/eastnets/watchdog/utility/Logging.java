package com.eastnets.watchdog.utility;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.eastnets.watchdog.config.WatchdogConfiguration;

/**
 * JUL wrapper (it could be .property file)
 * 
 * @author EHakawati
 * 
 */
public class Logging {

	private static Level logLevel = Level.OFF;
	static WatchdogConfiguration watchdogConfiguration;
	/**
	 * Static initialize block
	 */
	static {

		if (watchdogConfiguration.getDebug()) {
			logLevel = Level.ALL;
		} else {
			logLevel = Level.INFO;
		}
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
		logLevel = Level.toLevel(level);
		Logger.getRootLogger().setLevel(logLevel);
	}

	/**
	 * Create a new logger object
	 * 
	 * @param clazzName
	 * @return
	 */
	public static Logger getLogger(String className) {
		Logger logger = Logger.getLogger(className);
		logger.setLevel(logLevel);
		return logger;
	}
}
