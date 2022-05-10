package com.eastnets.watchdog.dumper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.watchdog.config.DAOFactory;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.utility.Constants;

/**
 * 
 * <h1>EventDumper</h1> <br/>
 * <p>
 * The EventDumper responsible for save all notifications and delete data from temp table
 * </p>
 *
 * @author mohmmad kassab
 * @version 1.0
 * 
 */

@Service
public class EventDumper extends Dumper {

	private static final Logger LOGGER = Logger.getLogger(EventDumper.class);

	@Autowired
	@Qualifier("CheckerEventsKeysOutQueue")
	BlockingQueue<WDEventRequestResult> eventsRequestsPostProcessingQueue;

	@Autowired
	@Qualifier("EventsEmailsNotificationsToBeDumpedQueue")
	BlockingQueue<WDEmailNotification> eventsEmailsNotificationsToBeDumpedQueue;

	@Autowired
	@Qualifier("CheckerWdJrnlKeysToBeDeletedQueue")
	BlockingQueue<WDJrnlKey> wdJrnlkyesToBeDeleted;

	@Autowired
	WatchdogDaoImpl watchdogDaoImpl;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Autowired
	DAOFactory daoFactory;

	List<WDEventRequestResult> eventRequestsResults = new ArrayList<>();
	List<WDEmailNotification> eventsEmailsNotifications = new ArrayList<>();
	List<JrnlPK> wdJrnlKeysPk = new ArrayList<>();

	public void init() {
		LOGGER.trace("Init - Event Dumper");
	}

	public void run() {

		LOGGER.trace("Run - Event Dumper");
		try {
			while (true) {
				fillDataNotifaction();
				SaveEventNotifactionInfo();
			}
		} catch (Exception e) {
			LOGGER.error("**** ERROR in Event Dumper ****");
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * This method is used to save notification info (WDEVENTREQUESTRESULT,WDEMAILNOTIFICATION)
	 * </p>
	 * <p>
	 * and delete the data from (wdJrnlKeysPk)
	 * </p>
	 */
	private void SaveEventNotifactionInfo() {
		try {
			if (!eventsEmailsNotifications.isEmpty()) {
				LOGGER.debug("Saving new Event requests results into WDEVENTREQUESTRESULT");
				daoFactory.getWatchDogDAO().saveEventResults(eventRequestsResults);
				watchdogDaoImpl.deleteWdJrnlKey(eventRequestsResults.stream().map(x -> x.getJrnlKey()).collect(Collectors.toList()));
			}
			if (!eventsEmailsNotifications.isEmpty()) {
				LOGGER.debug("Saving new Event requests results into WDEMAILNOTIFICATION");
				daoFactory.getWatchDogDAO().saveWDEmailNotification(eventsEmailsNotifications);
			}

			if (!wdJrnlKeysPk.isEmpty()) {
				watchdogDaoImpl.deleteWdJrnlKey(wdJrnlKeysPk);
			}
		} catch (Exception e) {
			daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when saving Notifaction !! ");
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * This method is used get Data from all Shared queue (From EventChecker) (wdJrnlkyesToBeDeleted,eventsEmailsNotificationsToBeDumpedQueue,eventsRequestsPostProcessingQueue) and fill all data into
	 * list take advantage of batch insert.
	 * </p>
	 * 
	 */
	private void fillDataNotifaction() {
		resetDataList();
		long t = System.currentTimeMillis();
		long end = t + 2000;

		// Loop for seconds
		// to dump.
		// This is done to take advantage of batch insert.

		try {
			while ((System.currentTimeMillis() < end)) {
				WDEventRequestResult eventResult = eventsRequestsPostProcessingQueue.poll();
				if (eventResult != null) {
					eventRequestsResults.add(eventResult);
				}

				WDEmailNotification eventEmailNotification = eventsEmailsNotificationsToBeDumpedQueue.poll();

				if (eventEmailNotification != null) {
					eventsEmailsNotifications.add(eventEmailNotification);
				}

				WDJrnlKey wdJrnlKey = wdJrnlkyesToBeDeleted.poll();

				if (wdJrnlKey != null) {
					wdJrnlKeysPk.add(wdJrnlKey.getJrnlPK());
				}
			}

		} catch (Exception e) {
			daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when getting notifaction results from queue  ");
			e.printStackTrace();
		}

	}

	private void resetDataList() {

		eventRequestsResults = new ArrayList<>();
		eventsEmailsNotifications = new ArrayList<>();
		wdJrnlKeysPk = new ArrayList<>();
	}

	public BlockingQueue<WDEventRequestResult> getEventsRequestsPostProcessingQueue() {
		return eventsRequestsPostProcessingQueue;
	}

	public void setEventsRequestsPostProcessingQueue(BlockingQueue<WDEventRequestResult> eventsRequestsPostProcessingQueue) {
		this.eventsRequestsPostProcessingQueue = eventsRequestsPostProcessingQueue;
	}

	public BlockingQueue<WDEmailNotification> getEventsEmailsNotificationsToBeDumpedQueue() {
		return eventsEmailsNotificationsToBeDumpedQueue;
	}

	public void setEventsEmailsNotificationsToBeDumpedQueue(BlockingQueue<WDEmailNotification> eventsEmailsNotificationsToBeDumpedQueue) {
		this.eventsEmailsNotificationsToBeDumpedQueue = eventsEmailsNotificationsToBeDumpedQueue;
	}

}
