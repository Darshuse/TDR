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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.dao.MessageFileInfoDAO;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;
import com.eastnets.resilience.xsd.messaging.Message;

/**
 * File Act importer
 * 
 * @author EHakawati
 * 
 */
public class MessageFileInfoDAOImpl extends MessageFileInfoDAO {

	// Logger
	private static final Logger logger = Logging.getLogger(MessageFileInfoDAOImpl.class.getSimpleName());
	
	public MessageFileInfoDAOImpl( int restoreSet ){
		addObserver(new StatisticsObserver());
		setRestoreSet(restoreSet);
	}
	/**
	 * Add File Act descriptor
	 * 
	 * @param Connection
	 * @param Message
	 * @return boolean
	 * @throws SQLException
	 */
	@Override
	public boolean addFileInfo(Connection conn, Message message) throws SQLException {

		boolean returnValue = true;
		conn.setAutoCommit(false);
		try {

			// Prepare statement
			CallableStatement cs = conn.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_CHECK_FILE") + " }");

			int index = 1;
			int aid = GlobalConfiguration.getInstance().getAllianceId();

			cs.setInt(index++, aid);
			cs.setInt(index++, message.getUmidL());
			cs.setInt(index++, message.getUmidH());
			this.setTimestamp(cs, index++, message.getCreationDate());
			cs.setString(index++, message.getTransferDescription());
			cs.setString(index++, message.getTransferInfo());
			cs.setString(index++, message.getFileLogicalName());
			cs.setLong(index++, message.getFileSize());
			cs.setString(index++, message.getFileDescription());
			cs.setString(index++, message.getFileInfo());
			cs.setString(index++, message.getFileDigestAlgorithm());
			cs.setString(index++, message.getFileDigestValue());
			cs.setString(index++, message.getFileHeaderInfo());
			cs.setString(index++, message.getDeliveryNotificationReceiverDn());
			cs.setString(index++, message.getDeliveryNotificationRequestType());

			int outputIndex = index;
			// RETSTATUS
			cs.registerOutParameter(outputIndex, java.sql.Types.INTEGER);

			cs.executeUpdate();

			int updateCount = cs.getInt(outputIndex);

			cs.close();
			if (updateCount > 0) {
				setChanged();
				notifyObservers(StatisticsObserver.Type.FILE);
			}

		} catch (Exception ex) {
			logger.severe(ex.getMessage() + " Message File Info umidl ( " + message.getUmidL() + " ), umidh ( "
					+ message.getUmidH() + " )");
			conn.rollback();
			returnValue = false;
		} finally {
			logger.info("Commit Message File Info umidl ( " + message.getUmidL() + " ), umidh ( "
					+ message.getUmidH() + " )");
			conn.commit();
		}
		return returnValue;
	}

}
