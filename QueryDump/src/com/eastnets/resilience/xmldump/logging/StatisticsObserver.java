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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Observable;
import java.util.Observer;

import com.eastnets.resilience.xmldump.db.dao.ImporterDAO;
import com.eastnets.resilience.xmldump.importer.ObjectsImporter;

/**
 * Statistics holder
 * 
 * @author EHakawati
 * 
 */
public class StatisticsObserver implements Observer {

	// restore_set -> type/count
	private static final Map<Integer, Map<Type, Integer>> statistics = new HashMap<Integer, Map<Type, Integer>>();
	// logger
	private static final Logger logger = Logging.getLogger(StatisticsObserver.class.getSimpleName());

	private static Integer threadsCounts = 0;
	private static boolean summaryLoop = false;

	/**
	 * Database record insertion types
	 * 
	 * @author EHakawati
	 * 
	 */
	public enum Type {
		MESG, INST, INTV, APPE, TEXT, TEXTFIELD, TEXTLOOP, TEXTSYSTEM, FILE, JRNL
	}

	/**
	 * Inner logging thread creation
	 */
	public static void startSummayLoop() {
		if (summaryLoop == false) {
			summaryLoop = true;
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {

					while (summaryLoop) {

						if (threadsCounts > 0) {
							logger.info("Current workers: " + threadsCounts + getSummary());
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							logger.severe(e.getMessage());
						}
					}
				}
			});

			thread.start();
		}
	}

	/**
	 * Stop the inner logging thread creation
	 */
	public static void stopSummayLoop() {
		summaryLoop = false;
	}

	/**
	 * Get total imported messages
	 * 
	 * @param resoreSet
	 * @return message count
	 */
	public static int getMessageCount(int resoreSet) {
		int mesgCount = 0;
		if (statistics.containsKey(resoreSet)) {
			Map<Type, Integer> statMap = statistics.get(resoreSet);
			if (statMap.containsKey(StatisticsObserver.Type.MESG)) {
				mesgCount = statMap.get(StatisticsObserver.Type.MESG);
			}
		}
		return mesgCount;
	}

	/**
	 * get total messages inserted on the database
	 * 
	 * @param resoreSet
	 * @return
	 */
	public static int getTotalRecordsCount(int resoreSet) {
		int totalCount = 0;
		if (statistics.containsKey(resoreSet)) {
			Map<Type, Integer> statMap = statistics.get(resoreSet);

			for (Type type : Type.values()) {
				if (statMap.containsKey(type)) {
					totalCount += statMap.get(type);
				}
			}

		}
		return totalCount;
	}

	/**
	 * Observer implementation
	 * 
	 * @param Observable
	 * @param Object
	 */
	@Override
	public void update(Observable object, Object obj) {

		if (object instanceof ImporterDAO) {

			ImporterDAO importer = (ImporterDAO) object;
			Type statType = (Type) obj;
			int recordSet = importer.getRestoreSet();

			if (!statistics.containsKey(importer.getRestoreSet())) {
				statistics.put(importer.getRestoreSet(), new HashMap<Type, Integer>());
			}

			Map<Type, Integer> statMap = statistics.get(recordSet);

			synchronized (statMap) {

				if (!statMap.containsKey(statType)) {
					statMap.put(statType, 0);
				}

				statMap.put(statType, statMap.get(statType) + 1);
			}

		} else if (object instanceof ObjectsImporter) {
			threadsCounts += (Integer) obj;
		}
	}

	/**
	 * @return format simple summary string
	 */
	public static String getSummary() {
		StringBuilder statSummary = new StringBuilder();

		StringBuilder statSummaryVal = new StringBuilder();
		statSummaryVal.append(" (");
		int total = 0;
		boolean hasData= false;
		for (Type type : Type.values()) {

			int count = 0;

			for (Entry<Integer, Map<Type, Integer>> stat : statistics.entrySet()) {
				if (stat.getValue().containsKey(type)) {
					count += stat.getValue().get(type);
				}
			}
			if (count > 0) {
				total += count;
				hasData= true;
				statSummaryVal.append(type.name());
				statSummaryVal.append(":");
				statSummaryVal.append(count);
				statSummaryVal.append(" ");
			}
		}

		statSummaryVal.append(")");
		if ( hasData ){
			statSummary.append(statSummaryVal);
		}

		statSummary.append(" Total records: " + total);
		return statSummary.toString();
	}

}
