package com.eastnets.notifier.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;

import com.eastnets.dao.events.ENEventsDAO;
import com.eastnets.domain.events.ENEventMetadata;

public class EventsObserver implements Runnable, Serializable {

	private static final long serialVersionUID = 308412817620884156L;
	private ENEventsDAO eventsDAO;
	private int bulkSize;
	private JMSNotifier messageNotifier;
	private boolean sendingProblem = false;
	private boolean notFinished = true;
	private boolean debugEnabled;
	private static final Logger LOGGER = Logger.getLogger(EventsObserver.class);

	public void init() {
		LOGGER.info("Event observer initialized");
		if (debugEnabled) {
			LogManager.getRootLogger().setLevel(Level.DEBUG);
		}
	}

	@Override
	public void run() {
		while (notFinished) {
			try {
				if (sendingProblem) {
					sendingProblem = false;
					Thread.sleep(60000);
				}

				eventsDAO.removeSentMessages();

				List<ENEventMetadata> events = eventsDAO.fetchNewEvents(bulkSize);

				if (events == null || events.isEmpty()) {
					LOGGER.debug("No events found");
					Thread.sleep(30000);
				} else {
					LOGGER.debug("New events fetched ");

					try {
						eventsDAO.bulkInsertEventHistory(events);
					} catch (Exception e) {
						if (e instanceof DuplicateKeyException) {
							LOGGER.error("Dublication error occured :: please see the RUPDATENOTIFER table with status 4");
							eventsDAO.updateEvents(events, MessageStatus.DUPLICATE.getStatus());
							events = new ArrayList<ENEventMetadata>();
						}
					}
					if (events != null && !events.isEmpty()) {
						LOGGER.debug("Events inserted in history table");
						List<ENEventMetadata> unSentMessages = messageNotifier.notify(events);
						if (!unSentMessages.isEmpty()) {
							// There is a problem in the JMS Server so tell the Observer to
							// sleep for longer period.
							sendingProblem = true;
							eventsDAO.removeHistoryEvents(unSentMessages);
							eventsDAO.updateEvents(unSentMessages, MessageStatus.FAIL.getStatus());
						}
					}
					Thread.sleep(1000);
				}

			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException in run method " + e.getMessage(), e);
			} catch (Throwable e) {
				LOGGER.error("Throwable in run method " + e.getMessage(), e);
			}
		}
	}

	public void destory() {
		notFinished = false;
		eventsDAO.changeProcessingToNewEvents();
		System.out.println("Destroyed call");
	}

	public ENEventsDAO getEventsDAO() {
		return eventsDAO;
	}

	public void setEventsDAO(ENEventsDAO eventsDAO) {
		this.eventsDAO = eventsDAO;
	}

	public int getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(int bulkSize) {
		this.bulkSize = bulkSize;
	}

	public JMSNotifier getMessageNotifier() {
		return messageNotifier;
	}

	public void setMessageNotifier(JMSNotifier messageNotifier) {
		this.messageNotifier = messageNotifier;
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

}
