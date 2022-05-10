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
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.DBConnection;
import com.eastnets.resilience.xmldump.db.dao.MessageFileInfoDAO;
import com.eastnets.resilience.xmldump.db.dao.MessageImportDAO;
import com.eastnets.resilience.xmldump.db.dao.ParsedTextDAO;
import com.eastnets.resilience.xmldump.db.dao.impl.MessageFileInfoDAOImpl;
import com.eastnets.resilience.xmldump.db.dao.impl.MessageImportDAOImpl;
import com.eastnets.resilience.xmldump.db.dao.impl.ParsedTextDAOImpl;
import com.eastnets.resilience.xmldump.logging.Logging;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xsd.messaging.Appendix;
import com.eastnets.resilience.xsd.messaging.Instance;
import com.eastnets.resilience.xsd.messaging.Intervention;
import com.eastnets.resilience.xsd.messaging.Message;
import com.eastnets.resilience.xsd.messaging.MessageInfo;

/**
 * Dumping an Message list to database
 * 
 * @author EHakawati
 * 
 */
public class MessageImporter extends Observable implements ObjectsImporter {

	// logging
	private static final Logger logger = Logging.getLogger(MessageImporter.class.getSimpleName());

	// System text parser
	private static final Pattern systemTextPattern = Pattern.compile("\\{(.*?):(.*?)\\}", Pattern.DOTALL
			| Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

	private List<Message> messages;
	private int restoreSet;
	private Connection conn;
	private boolean forceUpdate = false;
	/**
	 * Constructor
	 * 
	 * @param messages
	 * @param restoreSet
	 */
	public MessageImporter(List<Message> messages, int restoreSet, Connection conn, boolean forceUpdate) {
		// register new observer
		addObserver(new StatisticsObserver());
		this.messages = messages;
		this.restoreSet = restoreSet;
		this.conn = conn;
		this.setForceUpdate(forceUpdate);
	}

	/**
	 * Thread method implementation
	 */
	@Override
	public void run() {

		// Notify statistics observer about a new worker starting
		setChanged();
		notifyObservers(+1);

		try {

			for (Message mesg : this.messages) {
				this.addMessage(conn, mesg);
			}
		} catch (SQLException ex) {
			logger.severe(ex.getMessage());
		}

		// Notify statistics observer about a new worker ends
		setChanged();
		notifyObservers(-1);

	}

	private boolean addMessage(Connection connection, Message message) throws SQLException {

		boolean retunValue = false;
		try {
			connection.setAutoCommit(false);
			MessageImportDAO dao = new MessageImportDAOImpl(getRestoreSet(),forceUpdate);
			MessageFileInfoDAO fileDAO = new MessageFileInfoDAOImpl(getRestoreSet());
			
			//check if the message has updates or not, we compare the modification date, if it is newer we update, and if older e ignore
			// but when the modification date is the same, we use the count of interventions and appendices, if interventions or appendices was added then we update, other wise we ignore
			int aid = GlobalConfiguration.getInstance().getAllianceId();		
			MessageInfo messageInfo = dao.getMessageInfoFromDB( connection, aid, message.getUmidL(), message.getUmidH() );
			if ( messageInfo != null && !forceUpdate){//messageInfo means the the message is not in the database, so we will add it anyway
				GregorianCalendar  cal = new GregorianCalendar( );
				cal.setTime(messageInfo.getMesgCreationDateTime());
				GregorianCalendar  cal2 = message.getModificationDate().toGregorianCalendar();
				
				
				long dateCompare=  ( cal.getTime().getTime()  + cal.getTimeZone().getRawOffset() ) 
						         - ( cal2.getTime().getTime()  + cal2.getTimeZone().getRawOffset() )  ;
				
				if ( dateCompare > 0 ){ // the message is older than the one in the database, so skip it 
					System.out.println(String.format("Message( %d, %d ) older than the one in the database.", message.getUmidL(), message.getUmidH()));
					return retunValue;//no update
				}
				if ( dateCompare == 0 )	{ //messages have equal modification date 
					int intvAppeCount= 0;
					for ( Instance instance : message.getInstance() ){
						if ( instance != null ){
							if ( instance.getAppendix() != null  ){
								intvAppeCount += instance.getAppendix().size();
							}
							if ( instance.getIntervention() != null  ){
								intvAppeCount += instance.getIntervention().size();
							}
						}
					}
					
					if ( intvAppeCount <=  messageInfo.getIntvAppeCount() ){//message from dump don't have any extra appendices/interventions in it, by that we assume that it is not an update for the message in the database 
						System.out.println(String.format("Message( %d, %d ) already exists in database.", message.getUmidL(), message.getUmidH()));
						return retunValue;//no update
					}
					//reaching here, the message has the same modification date but yet it has more appendices/interventions so we will force its update 
				}
			}
			//reaching here we are sure that the message has been updated 
			
			retunValue = dao.addMessage(connection, message);
			
			//if file message update the rfile record
			if (retunValue && message.getFormatName().trim().equalsIgnoreCase("file") ){
				retunValue = fileDAO.addFileInfo(connection, message);
			}

			if (retunValue) {
				if (message.getInstance() != null) {
					for (Instance instance : message.getInstance()) {
						dao.addInstance(connection, message, instance);

						if (instance.getAppendix() != null) {
							for (Appendix appendix : instance.getAppendix()) {
								dao.addAppendix(connection, message, instance, appendix);
							}
						}

						if (instance.getIntervention() != null) {
							for (Intervention intervention : instance.getIntervention()) {
								dao.addIntervention(connection, message, instance, intervention);
							}
						}
					}
				}

				if (message.getText() != null) {
					this.addText(connection, message, dao);
				}

				logger.info("Commit Message umidl ( " + message.getUmidL() + " ), umidh ( " + message.getUmidH() + " )");
				connection.commit();
			}
		} catch (Exception ex) {
			retunValue = false;
			logger.severe(ex.getMessage() + " Message umidl ( " + message.getUmidL() + " ), umidh ( "
					+ message.getUmidH() + " )");
			connection.rollback();
		} finally {
		}

		return retunValue;
	}

	private void addText(Connection connection, Message message, MessageImportDAO dao) throws SQLException {

		boolean isTextBreakEnabled = GlobalConfiguration.getInstance().isTextBreakEnabled();
		
		if ( isTextBreakEnabled && message.getType() !=null  && "Swift".equals(message.getFormatName()) ) {
			if (Integer.parseInt(message.getType()) > 99) {
				try {
					// Syntax parsing + database dumping
					ParsedMessage parsedMessage = parseText(connection, message);

					// reset data text block
					message.getText().setDataBlock(null);

					dao.addText(connection, message);

					if (parsedMessage != null) {

						// Database importer
						ParsedTextDAO parsedTextDAO = new ParsedTextDAOImpl(connection, restoreSet);

						// add parsed loops
						parsedTextDAO.addTextLoops(message, parsedMessage);

						// add parsed texts
						parsedTextDAO.addTextFields(message, parsedMessage);

					}
				} catch (Exception e) {
					dao.addText(connection, message);
				}
			} else {
				try {
					// System text parsing + database dumping
					List<String[]> systemTexts = parseSystemText(connection, message);

					if (systemTexts != null && systemTexts.size() > 0) {
						// reset data text block
						message.getText().setDataBlock(null);
					}

					dao.addText(connection, message);
					if (systemTexts != null) {
						// Database importer
						ParsedTextDAO parsedTextDAO = new ParsedTextDAOImpl(connection, restoreSet);

						parsedTextDAO.addSystemText(message, systemTexts);
					}
				} catch (Exception e) {
					dao.addText(connection, message);
				}
			}
		} else {
			dao.addText(connection, message);
		}
	}

	/**
	 * System text splitter + database dumping
	 * 
	 * @param connection
	 * @param message
	 * @throws SQLException
	 */
	public List<String[]> parseSystemText(Connection connection, Message message) throws SQLException {

		try {

			if (message.getText().getDataBlock() != null) {
				List<String[]> systemText = new LinkedList<String[]>();

				// Regex parsing
				Matcher matcher = systemTextPattern.matcher(message.getText().getDataBlock());
				while (matcher.find()) {
					String[] array = new String[2];
					array[0] = matcher.group(1);
					array[1] = matcher.group(2);

					systemText.add(array);
				}

				return systemText;
			}

		} catch (Exception e) {

			logger.severe("System Text parsing FAILD: " + e.getMessage() + " Message umidl ( " + message.getUmidL()
					+ " ), umidh ( " + message.getUmidH() + " )");

		}
		return null;
	}

	/**
	 * Syntax text parsing + database dumping
	 * 
	 * @param connection
	 * @param message
	 * @throws SQLException
	 */
	public ParsedMessage parseText(Connection connection, Message message) throws Exception {

		try {

			// initialize syntax text parsing
			com.eastnets.resilience.textparser.syntax.Message messageParser = Syntax.getParser(
					message.getSyntaxTableVersion(), message.getType(), connection);

			// parse text
			return messageParser.parseMessageText(message.getText().getDataBlock());

		} catch (Exception e) {
			logger.severe("Text parsing FAILD: " + e.getMessage() + " Message umidl ( " + message.getUmidL()
					+ " ), umidh ( " + message.getUmidH() + " ) -- Validation " + message.getValidationPassed().name());

			throw e;
		}

	}

	/**
	 * @return
	 */
	public int getRestoreSet() {
		return restoreSet;
	}

	/**
	 * @param restoreSet
	 */
	public void setRestoreSet(int restoreSet) {
		this.restoreSet = restoreSet;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
}
