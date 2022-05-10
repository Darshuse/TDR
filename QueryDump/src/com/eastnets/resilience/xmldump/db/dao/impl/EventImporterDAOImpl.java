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
package com.eastnets.resilience.xmldump.db.dao.impl;

import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.DatabaseType;
import com.eastnets.resilience.xmldump.db.dao.EventImporterDAO;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;
import com.eastnets.resilience.xsd.eventjournal.EventReturn;

/**
 * Event (Jrnl) Importer
 * 
 * @author EHakawati
 * 
 */
public class EventImporterDAOImpl extends EventImporterDAO {

	private static long DATE_TIME_MAX = 0x7fffff00;

	// Logger Object
	private static final Logger logger = Logging.getLogger(EventImporterDAOImpl.class.getSimpleName());

	/**
	 * Constructor
	 */
	public EventImporterDAOImpl() {
		// Register new observer
		addObserver(new StatisticsObserver());
	}

	/**
	 * Add new Event (Jrnl) record
	 * 
	 * @param Connection
	 * @param EventReturn
	 * @return
	 * @throws SQLException
	 */
	public boolean addEvent(Connection connection, EventReturn event) throws SQLException {

		boolean returnValue = true;

		connection.setAutoCommit(false);
		CallableStatement processJrnl;

		// Prepared statement from resource bundled
		processJrnl = connection.prepareCall("{ call " + ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix).getString("LD_PROCESS_JRNL") + " }");

		try {

			int index = 1;

			// TODO: get the right formula from SWIFT ( case already opened )
			long jrnlRevDateTime = DATE_TIME_MAX - event.getDateTime().toGregorianCalendar().getTimeInMillis() / 1000;

			// V_AID param
			int aid = GlobalConfiguration.getInstance().getAllianceId();
			processJrnl.setInt(index++, aid);

			// V_JRNL_REV_DATE_TIME param
			processJrnl.setLong(index++, jrnlRevDateTime);

			// V_JRNL_SEQ_NBR param
			processJrnl.setLong(index++, event.getEvent().getIdentifier().getSequenceNumber());

			// V_JRNL_COMP_NAME param **
			processJrnl.setString(index++, "SAA_Q");

			// V_JRNL_EVENT_IS_SECURITY param
			this.setBoolean(processJrnl, index++, event.isSecurity());

			// V_JRNL_EVENT_NUM param
			processJrnl.setInt(index++, event.getNumber());

			// V_JRNL_EVENT_NAME param
			processJrnl.setString(index++, event.getName());

			// V_JRNL_EVENT_CLASS param
			processJrnl.setString(index++, event.getClazz());

			// V_JRNL_EVENT_SEVERITY param
			this.setEnumString(processJrnl, index++, event.getSeverity());

			// V_JRNL_EVENT_IS_ALARM param
			processJrnl.setInt(index++, "Alarm Event".equals(event.getType()) ? 1 : 0);

			// V_JRNL_APPL_SERV_NAME param
			processJrnl.setString(index++, event.getApplication());

			// V_JRNL_FUNC_NAME param
			processJrnl.setString(index++, event.getFunction());

			// V_JRNL_HOSTNAME param
			processJrnl.setString(index++, event.getHostname());

			// V_JRNL_OPER_NICKNAME param
			processJrnl.setString(index++, event.getOperator().getName());

			// V_JRNL_DATE_TIME param
			this.setTimestamp(processJrnl, index++, event.getDateTime());

			// V_JRNL_ALARM_STATUS param
			if (event.getAlarmHistory() == null) {
				processJrnl.setString(index++, "NOT_ALARM");
			} else {
				this.setEnumString(processJrnl, index++, event.getAlarmHistory());
			}

			// V_JRNL_ALARM_DIST_STATUS param **
			processJrnl.setString(index++, "ALARM_DIST_N_A");

			// V_JRNL_ALARM_DATE_TIME param
			this.setTimestamp(processJrnl, index++, event.getAlarmDateTime());

			// V_JRNL_ALARM_OPER_NICKNAME param
			processJrnl.setString(index++, event.getAlarmOperator() == null ? null : event.getAlarmOperator().getName());

			// V_JRNL_IS_CONFIG_MGMT
			this.setBoolean(processJrnl, index++, event.isConfig_mgmt());

			// V_JRNL_BUSINESS_ENTITY_NAME
			processJrnl.setString(index++, event.getJrnl_business_entity_name());

			// V_JRNL_DISPLAY_TEXT param
			processJrnl.setString(index++, event.getDescription().length() > 81 ? event.getDescription().substring(0, 81) : event.getDescription());

			// V_JRNL_MERGED_TEXT param
			// TODO : check If applicable in mssql
			int textIndex = index++;
			if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.MSSQL) {
				processJrnl.setString(textIndex, event.getDescription());
			} else {
				processJrnl.registerOutParameter(textIndex, java.sql.Types.CLOB);
			}

			// V_JRNL_LENGTH param
			processJrnl.setInt(index++, event.getDescription().length());

			// V_JRNL_TOKEN param
			processJrnl.setInt(index++, event.getObjectCRC());

			// V_REQUEST_TYPE param
			processJrnl.setInt(index++, 1);

			// V_TEXT param
			processJrnl.setString(index++, event.getDescription().length() > 3999 ? event.getDescription().substring(0, 3999) : event.getDescription());

			// V_JRNL_BROWSER_HOSTNAME
			processJrnl.setString(index++, event.getBrowserHostname());

			// V_JRNL_BROWSER_IP_ADDR
			processJrnl.setString(index++, event.getBrowserIpAddress());

			// V_RETSTATUS
			processJrnl.registerOutParameter(index, java.sql.Types.INTEGER);

			processJrnl.executeUpdate();

			int updateCount = processJrnl.getInt(index);

			if (updateCount == 1) {
				// ORACLE CLOB handling
				if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.ORACLE && event.getDescription() != null) {
					// write clob stream
					Clob clob = processJrnl.getClob(textIndex);
					Writer writer = clob.setCharacterStream(0);
					try {
						writer.write(event.getDescription());
						writer.flush();
						writer.close();
					} catch (Exception ex) {
						logger.warning("Event: " + ex.getMessage());
					}

				}
			}

			processJrnl.close();

			setChanged();
			notifyObservers(StatisticsObserver.Type.JRNL);

		} catch (Exception ex) {
			logger.severe(ex.getMessage() + " Event : sequence " + event.getEvent().getIdentifier().getSequenceNumber());
			connection.rollback();
			returnValue = false;
		} finally {
			logger.info("Commit Event : sequence " + event.getEvent().getIdentifier().getSequenceNumber());
			connection.commit();
		}

		return returnValue;
	}
}
