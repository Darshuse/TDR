
package com.eastnets.notifier.service;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.eastnets.notifier.config.ProjectProperties;
import com.eastnets.notifier.domain.EventData;
import com.eastnets.notifier.domain.Message;
import com.eastnets.notifier.domain.NotifierEventObserver;
import com.eastnets.notifier.repository.ENEventsDAO;

@Service
public class EventsObserver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308412817620884156L;

	@Autowired
	private ENEventsDAO eventsDAO;

	@Autowired
	private NotifiedEventObserver notifiedEventObserver;

	@Autowired
	private BlankMessagesServiceHandler blankMessagesServiceHandler;

	@Autowired
	private ProjectProperties projectProperties;

	@Value("${bulkSize}")
	private int bulkSize;

	@Autowired
	private JMSNotifier messageNotifier;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventsObserver.class);

	public EventsObserver() {

	}

	@PostConstruct
	public void init() {
		eventsDAO.getAllRoutingPointByMsgPrimaryKey();
		eventsDAO.upadteNotifierStatusBeforePullMessages();
		LOGGER.info("Event observer initialized");

	}

	public void startService() {
		boolean stopservice = false;
		while (!stopservice) {
			List<EventData> observeMessages = observeMessages();
			if (observeMessages == null || observeMessages.isEmpty() || observeMessages.size() == 0) {
				try {
					LOGGER.debug("Notifer  will sleep for " + projectProperties.getFetchEvery() + " Seconds");
					Thread.sleep((projectProperties.getFetchEvery()) * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public List<EventData> observeMessages() {
		if (projectProperties.isUpdateNotifierStatus()) {
			eventsDAO.upadteNotifierStatusBeforePullMessages();
		}
		LOGGER.info("start to check for events " + LocalTime.now());
		List<EventData> events = eventsDAO.fetchNewEvents(bulkSize);

		if (events == null) {
			LOGGER.debug("Exception Happened when try to get the messages");
			return null;
		}

		if (!events.isEmpty()) {
			LOGGER.debug("New events fetched ");
		} else {
			LOGGER.debug("No new events found");
		}
		try {
			if (events != null && !events.isEmpty()) {
				// TFS # 52299 handle the blank messages (internal messages) inserted it in RUPDATENOTIFIERHISTORY table , with MALFORMED data
				events = blankMessagesServiceHandler.handleBlankMessagesEvent(events);
				// before we insert to history we want to check for duplicate sent event (send the list of event)
				// check for instance number and queue name remove duplicate event from table(rUpdateNotifier) and from list
				// if message complete remove it from observer table
				deleteFromObserverTable(events, projectProperties.isForceNotifierObserverDelete());
				events = notifiedEventObserver.removeAlreadySentEvent(events);
			}
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				LOGGER.error("Dublication error occured :: please see the RUPDATENOTIFER table with status 4");
				eventsDAO.updateEvents(events, MessageStatus.DUPLICATE.getStatus());
				events = new ArrayList<EventData>();
			} else {
				e.printStackTrace();
			}
		}
		if (!events.isEmpty()) {
			// delete the record from observer table when its Completed
			deleteFromObserverTable(events);
			deleteFromObserverTable(events, projectProperties.isDeleteFromIntvMergedText());

			LOGGER.debug("Events inserted in history table");
			List<EventData> unSentMessages = messageNotifier.notify(this, events);
			if (!unSentMessages.isEmpty()) {
				eventsDAO.updateEvents(unSentMessages, MessageStatus.FAIL.getStatus());
			}
		}

		return events;
	}

	private void deleteFromObserverTable(List<EventData> events) {
		LOGGER.debug("Start deleteFromObserverTable() ");
		for (EventData eventData : events) {
			Message message = eventData.getMessage();
			if (message != null) {
				String messageStatus = message.getMessageStatus();
				LOGGER.debug("Message Key " + eventData.getPrimaryKey() + "Message Status = " + messageStatus);
				if (messageStatus != null && messageStatus.equalsIgnoreCase("COMPLETED")) {
					LOGGER.debug("Start Delete Message from Notifier Event Observer  Foe message Key  " + eventData.getPrimaryKey());
					removetNotifierEventObserver(eventData.getPrimaryKey().getAid(), Integer.parseInt(eventData.getPrimaryKey().getUmidl()), Integer.parseInt(eventData.getPrimaryKey().getUmidh()));
					LOGGER.debug("End Delete Message from Notifier Event Observer  Foe message Key  " + eventData.getPrimaryKey());

				}
			}
		}
	}

	private void deleteFromObserverTable(List<EventData> events, boolean deleteFromIntvMergedText) {
		if (!deleteFromIntvMergedText) {
			return;
		}
		LOGGER.debug("Start deleteFromObserverTable() ");
		for (EventData eventData : events) {
			Message message = eventData.getMessage();
			if (message != null) {
				String mergedText = message.getMergedText();
				LOGGER.debug("Message Key " + eventData.getPrimaryKey() + " Merged Text = " + mergedText);
				if (mergedText != null) {
					if (mergedText.startsWith("Completed")) {
						LOGGER.debug("Start Delete Message from Notifier Event Observer  Foe message Key  " + eventData.getPrimaryKey());
						removetNotifierEventObserver(eventData.getPrimaryKey().getAid(), Integer.parseInt(eventData.getPrimaryKey().getUmidl()), Integer.parseInt(eventData.getPrimaryKey().getUmidh()));
						LOGGER.debug("End Delete Message from Notifier Event Observer  Foe message Key  " + eventData.getPrimaryKey());
					}
				}
			}
		}
	}

	public void changeEventsToNew() {
		LOGGER.debug("Start update processing Events to new  ");
		eventsDAO.changeProcessingToNewEvents();
	}

	public void updateDuplicateEvent(List<EventData> events) {
		eventsDAO.updateEvents(events, MessageStatus.DUPLICATE.getStatus());
	}

	public void updateFailedEventMsg(List<EventData> events) {
		LOGGER.debug("Start update Failed Events ");
		if (!events.isEmpty()) {
			eventsDAO.updateEvents(events, MessageStatus.FAIL.getStatus());
			LOGGER.debug("Failed Events updated");
		}
	}

	public void updateNotifiedMessages(List<EventData> events) {
		LOGGER.debug("Start update Events ");
		if (!events.isEmpty()) {
			eventsDAO.updateEvents(events, MessageStatus.SENT.getStatus());
			LOGGER.debug("Events updated");
		}
	}

	public void removeSentMessages() {
		eventsDAO.removeSentMessages();
	}

	public void insertSentEventInHistory(List<EventData> events) {
		LOGGER.debug("Start insert Events in history table ");
		if (!events.isEmpty()) {
			eventsDAO.bulkInsertEventHistory(events);
			LOGGER.debug("Events inserted");
		}
	}

	public void insertSentEventInEventObserver(List<NotifierEventObserver> allNotifiedEvent) {

		if (!CollectionUtils.isEmpty(allNotifiedEvent)) {
			LOGGER.debug("prepare Notified Event to be insert");
			eventsDAO.bulkInsertNotifierEventObserver(allNotifiedEvent);
			LOGGER.debug(" Notified Events Inserted ");
		}

	}

	public boolean removetNotifierEventObserver(int aid, int umidl, int umidh) {
		return eventsDAO.removetNotifierEventObserver(aid, umidl, umidh);
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

	public NotifiedEventObserver getNotifiedEventObserver() {
		return notifiedEventObserver;
	}

	public void setNotifiedEventObserver(NotifiedEventObserver notifiedEventObserver) {
		this.notifiedEventObserver = notifiedEventObserver;
	}

	public ProjectProperties getProjectProperties() {
		return projectProperties;
	}

	public void setProjectProperties(ProjectProperties projectProperties) {
		this.projectProperties = projectProperties;
	}

}