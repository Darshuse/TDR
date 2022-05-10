package com.eastnets.watchdog.checker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.entities.WDJrnlSearchRequest;
import com.eastnets.watchdog.config.DAOFactory;
import com.eastnets.watchdog.utility.Constants;

@Service
public class EventChecker extends Checker {

	@Autowired
	@Qualifier("ReaderEventsRequestsOutQueue")
	BlockingQueue<WDJrnlKey> eventsRequestsKeysPreProcessingQueue;

	@Autowired
	@Qualifier("CheckerEventsKeysOutQueue")
	BlockingQueue<WDEventRequestResult> eventsRequestsPostProcessingQueue;

	@Autowired
	@Qualifier("EventsEmailsNotificationsToBeDumpedQueue")
	BlockingQueue<WDEmailNotification> eventsEmailsToBeDumpedQueue;

	@Autowired
	@Qualifier("CheckerWdJrnlKeysToBeDeletedQueue")
	BlockingQueue<WDJrnlKey> wdJrnlkyesToBeDeleted;

	List<WDEmailNotification> emailNotifications = null;
	private static final Logger LOGGER = Logger.getLogger(EventChecker.class);

	@Autowired
	DAOFactory daoFactory;

	@PostConstruct
	public void init() {
		LOGGER.trace("init - Event Checker");
	}

	@Override
	public void run() {

		try {

			while (true) {
				// take method is a blocking method which means the service will
				// hold until al element becomes available.
				check(eventsRequestsKeysPreProcessingQueue.take());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void check(WDJrnlKey wdJrnlKey) {
		LOGGER.debug("Start Checking Event With " + "aid: " + wdJrnlKey.getJrnlPK().getAid() + " JRNL_REV_DATE_TIME : " + wdJrnlKey.getJrnlPK().getJrnlRevDateTime() + " JRNL_SEQ_NBR: " + wdJrnlKey.getJrnlPK().getJrnlSeqNumber());

		try {
			List<WDEventRequestResult> matchedEventResults = getMatchedEventResults(wdJrnlKey);
			if (matchedEventResults != null && !matchedEventResults.isEmpty()) {
				LOGGER.debug("Queue the matched Event to Event Dumber");

				eventsRequestsPostProcessingQueue.addAll(matchedEventResults);
				eventsEmailsToBeDumpedQueue.addAll(emailNotifications);
			} else {
				wdJrnlkyesToBeDeleted.add(wdJrnlKey);
			}

		} catch (Exception e) {
			daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
					"Error when Check Event  " + "aid: " + wdJrnlKey.getJrnlPK().getAid() + " JRNL_REV_DATE_TIME : " + wdJrnlKey.getJrnlPK().getJrnlRevDateTime() + " JRNL_SEQ_NBR: " + wdJrnlKey.getJrnlPK().getJrnlSeqNumber());
			e.printStackTrace();
		}

	}

	public List<WDEventRequestResult> getMatchedEventResults(WDJrnlKey wdJrnlKey) {
		List<WDEventRequestResult> eventRequestResults = new ArrayList<>();
		emailNotifications = new ArrayList<>();
		List<WDJrnlSearchRequest> jrnlSearchRequests = getMatchedSearchRequst(wdJrnlKey);
		for (WDJrnlSearchRequest searchRequest : jrnlSearchRequests) {
			LOGGER.debug("Event with " + "aid: " + wdJrnlKey.getJrnlPK().getAid() + " JRNL_REV_DATE_TIME : " + wdJrnlKey.getJrnlPK().getJrnlRevDateTime() + " JRNL_SEQ_NBR: " + wdJrnlKey.getJrnlPK().getJrnlSeqNumber() + " Matched With Request "
					+ searchRequest.getDescription());
			// here i want to chek on expir date
			if (searchRequest.getExpirationDate() != null) {
				LocalDate expirationDate = convertToLocalDate(searchRequest.getExpirationDate());
				LocalDate curuntDate = convertToLocalDate(new Date());
				if (curuntDate.isAfter(expirationDate)) {
					LOGGER.info("the Requst with ID" + searchRequest.getRequestID() + " is expiered ");
					continue;
				}
			}
			eventRequestResults.add(prepareEventRequestResult(searchRequest, wdJrnlKey));
			if (searchRequest.getEmail() != null) {
				emailNotifications.add(prepareEventEmailNotification(searchRequest, wdJrnlKey));
			}
		}

		return eventRequestResults;
	}

	public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public List<WDJrnlSearchRequest> getMatchedSearchRequst(WDJrnlKey wdJrnlKey) {

		LOGGER.debug("Matching jrnl keys to search request");
		return watchDogRepositoryService.getWdJrnlSearchRequests(wdJrnlKey.getJrnlCompName(), wdJrnlKey.getJrnlEventNumber());

	}

	private WDEmailNotification prepareEventEmailNotification(WDJrnlSearchRequest searchRequest, WDJrnlKey wdJrnlKey) {
		LOGGER.debug("Preparing event email notification ");
		WDEmailNotification emailNotification = new WDEmailNotification();

		emailNotification.setAid(Long.valueOf(wdJrnlKey.getJrnlPK().getAid()));
		emailNotification.setDescription(searchRequest.getDescription());
		emailNotification.setUsername(searchRequest.getEmail());
		emailNotification.setUmidh(wdJrnlKey.getJrnlPK().getJrnlSeqNumber());
		emailNotification.setUmidl(Long.valueOf(wdJrnlKey.getJrnlPK().getJrnlRevDateTime()));
		emailNotification.setMessageType("EVT");
		emailNotification.setWdID(searchRequest.getRequestID());
		emailNotification.setProcessStatus(0);
		emailNotification.setMesgCreaDateTime(wdJrnlKey.getJrnlDateTime());
		return emailNotification;
	}

	private WDEventRequestResult prepareEventRequestResult(WDJrnlSearchRequest searchRequest, WDJrnlKey wdJrnlKey) {
		LOGGER.debug("Preparing event request result ");
		WDEventRequestResult eventRequestResult = new WDEventRequestResult();
		eventRequestResult.setAid(wdJrnlKey.getJrnlPK().getAid());
		eventRequestResult.setDescription(searchRequest.getDescription());
		eventRequestResult.setJrnlRevDateTime(wdJrnlKey.getJrnlPK().getJrnlRevDateTime());
		eventRequestResult.setJrnlSeqNumber(wdJrnlKey.getJrnlPK().getJrnlSeqNumber());
		eventRequestResult.setUsername(searchRequest.getUsername());
		eventRequestResult.setProcessed(0);
		eventRequestResult.setRequestId(searchRequest.getRequestID());
		eventRequestResult.setInsertTime(new Date());
		eventRequestResult.setTempCreaDateTime(wdJrnlKey.getJrnlDateTime());
		return eventRequestResult;
	}

	public BlockingQueue<WDJrnlKey> getEventsRequestsKeysPreProcessingQueue() {
		return eventsRequestsKeysPreProcessingQueue;
	}

	public void setEventsRequestsKeysPreProcessingQueue(BlockingQueue<WDJrnlKey> eventsRequestsKeysPreProcessingQueue) {
		this.eventsRequestsKeysPreProcessingQueue = eventsRequestsKeysPreProcessingQueue;
	}

	public BlockingQueue<WDEventRequestResult> getEventsRequestsPostProcessingQueue() {
		return eventsRequestsPostProcessingQueue;
	}

	public void setEventsRequestsPostProcessingQueue(BlockingQueue<WDEventRequestResult> eventsRequestsPostProcessingQueue) {
		this.eventsRequestsPostProcessingQueue = eventsRequestsPostProcessingQueue;
	}

	public BlockingQueue<WDEmailNotification> getEventsEmailsToBeDumpedQueue() {
		return eventsEmailsToBeDumpedQueue;
	}

	public void setEventsEmailsToBeDumpedQueue(BlockingQueue<WDEmailNotification> eventsEmailsToBeDumpedQueue) {
		this.eventsEmailsToBeDumpedQueue = eventsEmailsToBeDumpedQueue;
	}

}
