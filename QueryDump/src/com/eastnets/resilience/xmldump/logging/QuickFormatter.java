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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.eastnets.resilience.xmldump.GlobalConfiguration;

/**
 * Custom formatter
 * 
 * @author EHakawati
 * 
 */
public class QuickFormatter extends Formatter {

	public QuickFormatter() {
		super();
	}

	public String format(LogRecord record) {

		// Create a StringBuffer to contain the formatted record
		// start with the date.
		StringBuilder sb = new StringBuilder();

		sb.append((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).format((new Date(record.getMillis()))));
		sb.append("\t");

		// Get the level name and add it to the buffer
		sb.append(record.getLevel().getName());
		sb.append("\t");

		// Get the class name
		// sb.append(record.getLoggerName());
		// sb.append("\t");
		
		if(GlobalConfiguration.getInstance().isDebug()) {
			
			sb.append(record.getLoggerName());
			sb.append("\t");
			
			sb.append(record.getSourceMethodName());
			sb.append("\t");
		}
		

		// Get the formatted message (includes localization
		// and substitution of parameters) and add it to the buffer
		sb.append(formatMessage(record));
		sb.append("\n");

		return sb.toString();
	}

}
