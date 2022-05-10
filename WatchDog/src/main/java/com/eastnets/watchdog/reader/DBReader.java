package com.eastnets.watchdog.reader;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.watchdog.config.DAOFactory;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;
import com.eastnets.watchdog.resultbeans.WDAppeKeyBean;
import com.eastnets.watchdog.resultbeans.WDJrnlKeyBean;
import com.eastnets.watchdog.utility.Constants;
import com.eastnets.watchdog.utility.Util;

@Service
public class DBReader extends Reader {

	private static final Logger LOGGER = Logger.getLogger(DBReader.class);

	@Autowired
	WatchdogDaoImpl watchdogDaoImpl;

	@Autowired
	Util util;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Autowired
	DAOFactory daoFactory;

	@PostConstruct
	public void init() {
		LOGGER.trace("Reader Type: DB reader");
	}

	@Override
	public void run() {

		LOGGER.info("DB Reader Service Started");
		switch (configBean.getWatchDogMode()) {

		// default case which is both Messages and Events
		case ALL_MODE:
			LOGGER.info("Running Reader in mode A - events, messages and email notifications");
			try {
				while (true) {

					/*
					 * Mode 0 work flow: 1- GET all available event notifications requests from WDEVENTSEARCHPARAMETER 2- Get all available message notifications requests from WDUSERSEARCHPARAMETER
					 * table 3- Get all email notifications from WDEmailNotifications ( we first add notifications related to messages and then those related to events)
					 */
					readEventSearchParameter('A');
					readMessageSearchParametersAndKeys('A');
					readEmailNotiifcations('A');
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		// Only Events
		case EVENT_REQUEST:
			LOGGER.info("Running Reader in mode E - only events notifications");
			try {
				while (true) {
					readEventSearchParameter('E');
				}
			} catch (Exception e) {
				daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
						"Exception when reading Events from wdJrnlKeys  " + " aid : " + watchdogConfiguration.getAid());
				e.printStackTrace();
			}
			break;

		// Only Messages
		case MESSAGE_REQUEST:
			LOGGER.info("Running Reader in mode M -- only messages notifications");
			try {
				while (true) {
					readMessageSearchParametersAndKeys('M');
				}
			} catch (Exception e) {
				daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Exception when reading messages from WDKEYS " + " aid : " + watchdogConfiguration.getAid());
				e.printStackTrace();
			}
			break;

		// Only Email Notifications
		case EMAIL_REQUEST:
			LOGGER.info("Running Reader in mode N -- Only Email notifications");
			try {

				while (true) {
					readEmailNotiifcations('N');
				}

			} catch (Exception e) {
				daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
						"Exception when reading messages from WDEMAILNOTIFICATION " + " aid : " + watchdogConfiguration.getAid());
				e.printStackTrace();
			}
			break;

		// The default which is both Messages and Events
		default:
			LOGGER.error("Wrong Mode -- Reader will stop");
			break;
		}
	}

	private void readMessageSearchParametersAndKeys(char mode) {

		LOGGER.debug("Mode " + mode + " - Fetching WD Appe Keys from WDKEYS Table");
		List<WDAppeKeyBean> wdAppeKeysBean = getWDAppeKeys();
		LOGGER.debug("Get " + wdAppeKeysBean.size() + " From WDKEYS");
		if (wdAppeKeysBean == null || wdAppeKeysBean.isEmpty()) {
			try {
				LOGGER.info("Watchdog  will sleep for " + watchdogConfiguration.getDelay() + " Seconds");
				Thread.sleep((watchdogConfiguration.getDelay()) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			watchdogDaoImpl.updateWdAppeKey(wdAppeKeysBean);
			wdKeysPreProcessingQueue.addAll(util.convertWdAppeBeanToEntity(wdAppeKeysBean));
		}

	}

	private void readEventSearchParameter(char mode) {
		LOGGER.debug("Mode " + mode + " - Fetching event notification");
		List<WDJrnlKeyBean> wdJrnlKeys = getWdJrnlKeys();
		LOGGER.debug("Get " + wdJrnlKeys.size() + " From WDJRNLKEYS");
		if (wdJrnlKeys == null || wdJrnlKeys.isEmpty()) {
			try {
				LOGGER.info("Watchdog  will sleep for " + watchdogConfiguration.getDelay() + " Seconds");
				Thread.sleep((watchdogConfiguration.getDelay()) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			watchdogDaoImpl.updateWdJurnalKey(wdJrnlKeys);
			eventsRequestsKeysPreProcessingQueue.addAll(util.convertWdJrnlBeanToEntity(wdJrnlKeys));
		}
	}

	private void readEmailNotiifcations(char mode) {

		LOGGER.debug("Mode " + mode + " - Fetching messages & events emails");

		// get All messages email notifications available at
		// WDEMAILNOTIFICATIONS
		// Table and add them to email Queue to be sent by Mail
		// service.
		//
		List<MessageEmailNotification> messagesEmailNotifications = getMessagesEmailNotifications();
		List<EventEmailNotification> eventsEmailNotifications = getEventsEmailNotifications();

		if ((messagesEmailNotifications != null && !messagesEmailNotifications.isEmpty()) || (eventsEmailNotifications != null && !eventsEmailNotifications.isEmpty())) {
			watchdogDaoImpl.updateMessageWDEmailNotification(messagesEmailNotifications);
			emailsToBeSentQueue.addAll(messagesEmailNotifications);
			// get All events email notifications available at
			// WDEMAILNOTIFICATIONS
			// Table and add them to email Queue to be sent by Mail
			// service.

			watchdogDaoImpl.updateEventWDEmailNotification(eventsEmailNotifications);
			emailsToBeSentQueue.addAll(eventsEmailNotifications);
		} else {

			LOGGER.debug("Get " + " 0 " + " From WDEMAILNOTIFICATION");
			try {
				LOGGER.info("Watchdog  will sleep for " + watchdogConfiguration.getDelay() + " Seconds");
				Thread.sleep((watchdogConfiguration.getDelay()) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	List<WDJrnlKeyBean> getWdJrnlKeys() {
		if (watchdogConfiguration.isPartitioned()) {
			return watchDogService.getWdJrnlKeysPart(configBean.getBulkSize());
		} else {
			return watchDogService.getWdJrnlKeys(configBean.getBulkSize());
		}
	}

	List<WDMessageSearchRequest> getWDMessagesSearchRequests() {
		return watchDogService.getWDMessagesSearchRequests(configBean.getBulkSize());
	}

	List<WDAppeKeyBean> getWDAppeKeys() {
		if (watchdogConfiguration.isPartitioned()) {
			return watchDogService.getWdKeysPart(configBean.getBulkSize());
		} else {
			return watchDogService.getWdKeys(configBean.getBulkSize());
		}
	}

	List<MessageEmailNotification> getMessagesEmailNotifications() {
		return watchDogService.getAvailableMessagesEmailNotifications();
	}

	List<EventEmailNotification> getEventsEmailNotifications() {
		return watchDogService.getAvailableEventsEmailNotifications();
	}
}
