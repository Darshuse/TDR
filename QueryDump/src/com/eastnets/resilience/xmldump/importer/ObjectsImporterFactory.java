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
package com.eastnets.resilience.xmldump.importer;

import java.sql.Connection;
import java.util.List;

import com.eastnets.resilience.xsd.InputStartEntry;
import com.eastnets.resilience.xsd.eventjournal.EventReturn;
import com.eastnets.resilience.xsd.messaging.Message;

/**
 * Factory method that creates importer (Message/Event) objects
 * 
 * @author EHakawati
 * 
 */
public class ObjectsImporterFactory {

	private ObjectsImporterFactory() {

	}

	/**
	 * Factory method
	 * 
	 * @param type
	 * @param list
	 * @param restoreSet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ObjectsImporter getObjectsImporter(Class<? extends InputStartEntry> type,
			List<? extends InputStartEntry> list, int restoreSet, Connection conn, boolean forceUpdate) {

		if (type == Message.class) {
			return new MessageImporter((List<Message>) list, restoreSet,  conn,forceUpdate);
		} else if (type == EventReturn.class) {
			return new EventImporter((List<EventReturn>) list, restoreSet, conn);
		}
		return null;
	}

}
