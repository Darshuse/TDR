package com.eastnets.watchdog.dumper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.watchdog.config.DAOFactory;
import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.utility.Constants;

/**
 * 
 * <h1>MessageDumper</h1> <br/>
 * <p>
 * The MessageDumper responsible for save all notifications and delete data from temp table
 * </p>
 *
 * @author mohmmad kassab
 * @version 1.0
 * 
 */

@Service
public class MessageDumper extends Dumper {

	private static final Logger LOGGER = Logger.getLogger(MessageDumper.class);

	List<WDEmailNotification> messagesEmailsNotifications = new ArrayList<>();
	List<WDMessageRequestResult> messageRequestsResults = new ArrayList<>();
	List<WDNackResult> nackResults = new ArrayList<>();
	List<WDPossibleDuplicateResult> possibleDuplicateResults = new ArrayList<>();
	List<WDMessageSearchRequest> messageRequests = new ArrayList<>();
	List<WDAppeKey> wdAppeKeyToBeDeleted = new ArrayList<>();

	@Autowired
	@Qualifier("CheckerMessagesKeysOutQueue")
	BlockingQueue<WDMessageRequestResult> messagesKeysPostProcessingQueue;

	@Autowired
	@Qualifier("MessagesEmailsNotificationsToBeDumpedQueue")
	BlockingQueue<WDEmailNotification> messagesEmailsNotificationsToBeDumpedQueue;

	@Autowired
	@Qualifier("CheckerWdNackResultOutQueue")
	BlockingQueue<WDNackResult> wdNackResultPostProecssingQueue;

	@Autowired
	@Qualifier("CheckerWdPossibleDuplicateResultOutQueue")
	BlockingQueue<WDPossibleDuplicateResult> wdPossibleDuplicateResultPostProecssingQueue;

	@Autowired
	@Qualifier("messagesRequstToBeDeletedQueue")
	BlockingQueue<WDMessageSearchRequest> messageRequstTobeDeleted;

	@Autowired
	@Qualifier("CheckerWdKeysToBeDeletedQueue")
	BlockingQueue<WDAppeKey> wdkyesToBeDeleted;

	@Autowired
	private WatchdogDaoImpl watchdogDaoImpl;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DAOFactory daoFactory;

	public void init() {
		LOGGER.trace("Init - Message Dumper");
	}

	/**
	 * <p>
	 * To make message dumber work as service using Java Thred
	 * </p>
	 * 
	 */
	public void run() {
		LOGGER.trace("Run - Message Dumper");

		while (true) {
			fillSaveDataNotifaction();
			SaveMessageNotifactionInfo();

		}
	}

	/**
	 * <p>
	 * This method is used get Data from all Shared queue (From MessageChecker)
	 * (messagesKeysPostProcessingQueue,messagesEmailsNotificationsToBeDumpedQueue,wdNackResultPostProecssingQueue,wdPossibleDuplicateResultPostProecssingQueue,messageRequstTobeDeleted) and fill all
	 * data into list take advantage of batch insert.
	 * </p>
	 * 
	 */
	private void fillSaveDataNotifaction() {
		resetDataList();
		long t = System.currentTimeMillis();
		long end = t + 2000;

		// Loop for seconds
		// to dump.
		// This is done to take advantage of batch insert.

		try {
			while ((System.currentTimeMillis() < end)) {
				WDMessageRequestResult messageResult = messagesKeysPostProcessingQueue.poll();

				if (messageResult != null) {
					messageRequestsResults.add(messageResult);
				}
				WDEmailNotification messageEmailNotification = messagesEmailsNotificationsToBeDumpedQueue.poll();
				if (messageEmailNotification != null) {
					messagesEmailsNotifications.add(messageEmailNotification);
				}
				WDNackResult nackResult = wdNackResultPostProecssingQueue.poll();
				if (nackResult != null) {
					nackResults.add(nackResult);
				}

				WDPossibleDuplicateResult possibleDuplicate = wdPossibleDuplicateResultPostProecssingQueue.poll();

				if (possibleDuplicate != null) {
					possibleDuplicateResults.add(possibleDuplicate);
				}

				WDMessageSearchRequest WDMessageSearchRequest = messageRequstTobeDeleted.poll();
				if (WDMessageSearchRequest != null) {
					messageRequests.add(WDMessageSearchRequest);
				}
				WDAppeKey appeKey = wdkyesToBeDeleted.poll();

				if (appeKey != null) {
					wdAppeKeyToBeDeleted.add(appeKey);
				}
			}

		} catch (Exception e) {
			daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when getting notifaction results from queue  ");
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * This method is used to save notification info (WDUSERREQUESTRESULT,WDEMAILNOTIFICATION,WDNACKRESULT,WDPOSSIBLEDUPRESULT)
	 * </p>
	 * <p>
	 * and delete the data from (WDkeys,messageRequest)
	 * </p>
	 */
	private void SaveMessageNotifactionInfo() {

		try {
			if (!messageRequestsResults.isEmpty()) {
				LOGGER.debug("Saving new message requests results into WDUSERREQUESTRESULT");
				daoFactory.getWatchDogDAO().saveWDMessageRequestResult(messageRequestsResults);
				watchdogDaoImpl.deleteWDkeys(messageRequestsResults.stream().map(x -> x.getAppendixPK()).collect(Collectors.toList()));
			}
			if (!messagesEmailsNotifications.isEmpty()) {
				LOGGER.debug("Saving new email notifications into WDEMAILNOTIFICATION");
				daoFactory.getWatchDogDAO().saveWDEmailNotification(messagesEmailsNotifications);
			}
			if (!nackResults.isEmpty()) {
				LOGGER.debug("Saving new Nack Results into WDNACKRESULT");
				daoFactory.getWatchDogDAO().saveNackResults(nackResults);
				watchdogDaoImpl.deleteWDkeysNonPart(nackResults.stream().map(x -> x.getAppendixPK()).collect(Collectors.toList()));
			}

			if (!possibleDuplicateResults.isEmpty()) {
				LOGGER.debug("Saving Possible Duplicates into WDPOSSIBLEDUPRESULT");
				daoFactory.getWatchDogDAO().savePossibleDuplicates(possibleDuplicateResults);
				watchdogDaoImpl.deleteWDkeysNonPart(possibleDuplicateResults.stream().map(x -> x.getAppendixPK()).collect(Collectors.toList()));
			}
			if (!messageRequests.isEmpty()) {
				watchdogDaoImpl.deleteMessageRequest(messageRequests);
			}

			if (!wdAppeKeyToBeDeleted.isEmpty()) {
				watchdogDaoImpl.deleteWDkeys(wdAppeKeyToBeDeleted, true);
			}

		} catch (Exception e) {
			daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "", "Error when saving Notifaction !! ");
			e.printStackTrace();
		}

	}

	private void resetDataList() {
		messagesEmailsNotifications = new ArrayList<>();
		messageRequestsResults = new ArrayList<>();
		nackResults = new ArrayList<>();
		possibleDuplicateResults = new ArrayList<>();
		messageRequests = new ArrayList<>();
		wdAppeKeyToBeDeleted = new ArrayList<>();
	}

	public BlockingQueue<WDMessageRequestResult> getMessagesKeysPostProcessingQueue() {
		return messagesKeysPostProcessingQueue;
	}

	public void setMessagesKeysPostProcessingQueue(BlockingQueue<WDMessageRequestResult> messagesKeysPostProcessingQueue) {
		this.messagesKeysPostProcessingQueue = messagesKeysPostProcessingQueue;
	}

	public BlockingQueue<WDEmailNotification> getMessagesEmailsNotificationsToBeDumpedQueue() {
		return messagesEmailsNotificationsToBeDumpedQueue;
	}

	public void setMessagesEmailsNotificationsToBeDumpedQueue(BlockingQueue<WDEmailNotification> messagesEmailsNotificationsToBeDumpedQueue) {
		this.messagesEmailsNotificationsToBeDumpedQueue = messagesEmailsNotificationsToBeDumpedQueue;
	}

	public BlockingQueue<WDNackResult> getWdNackResultPostProecssingQueue() {
		return wdNackResultPostProecssingQueue;
	}

	public void setWdNackResultPostProecssingQueue(BlockingQueue<WDNackResult> wdNackResultPostProecssingQueue) {
		this.wdNackResultPostProecssingQueue = wdNackResultPostProecssingQueue;
	}

	public BlockingQueue<WDPossibleDuplicateResult> getWdPossibleDuplicateResultPostProecssingQueue() {
		return wdPossibleDuplicateResultPostProecssingQueue;
	}

	public void setWdPossibleDuplicateResultPostProecssingQueue(BlockingQueue<WDPossibleDuplicateResult> wdPossibleDuplicateResultPostProecssingQueue) {
		this.wdPossibleDuplicateResultPostProecssingQueue = wdPossibleDuplicateResultPostProecssingQueue;
	}

}
