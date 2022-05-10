package com.eastnets.watchdog.reader;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.resultbeans.EmailNotification;
import com.eastnets.watchdog.service.WatchDogRepositoryService;

public abstract class Reader implements Runnable {

	@Autowired
	WatchDogRepositoryService watchDogService;

	@Autowired
	@Qualifier("ReaderMessagesRequestsOutQueue")
	BlockingQueue<WDMessageSearchRequest> messagesRequestsKeysPreProcessingQueue;
	@Autowired
	@Qualifier("ReaderEventsRequestsOutQueue")
	BlockingQueue<WDJrnlKey> eventsRequestsKeysPreProcessingQueue;
	@Autowired
	@Qualifier("EmailsNotificationsToBeSentQueue")
	BlockingQueue<EmailNotification> emailsToBeSentQueue;
	@Autowired
	@Qualifier("ReaderWDKeysOutQueue")
	BlockingQueue<WDAppeKey> wdKeysPreProcessingQueue;
	@Autowired
	WatchdogConfiguration configBean;

	public BlockingQueue<WDJrnlKey> getEventsRequestsKeysPreProcessingQueue() {
		return eventsRequestsKeysPreProcessingQueue;
	}

	public void setEventsRequestsKeysPreProcessingQueue(BlockingQueue<WDJrnlKey> eventsRequestsKeysPreProcessingQueue) {
		this.eventsRequestsKeysPreProcessingQueue = eventsRequestsKeysPreProcessingQueue;
	}

	abstract List<WDMessageSearchRequest> getWDMessagesSearchRequests();

	public BlockingQueue<WDMessageSearchRequest> getMessagesRequestsKeysPreProcessingQueue() {
		return messagesRequestsKeysPreProcessingQueue;
	}

	public void setMessagesRequestsKeysPreProcessingQueue(BlockingQueue<WDMessageSearchRequest> messagesRequestsKeysPreProcessingQueue) {
		this.messagesRequestsKeysPreProcessingQueue = messagesRequestsKeysPreProcessingQueue;
	}

	public BlockingQueue<EmailNotification> getEmailsToBeSentQueue() {
		return emailsToBeSentQueue;
	}

	public void setEmailsToBeSentQueue(BlockingQueue<EmailNotification> emailsToBeSentQueue) {
		this.emailsToBeSentQueue = emailsToBeSentQueue;
	}

	public BlockingQueue<WDAppeKey> getWdKeysPreProcessingQueue() {
		return wdKeysPreProcessingQueue;
	}

	public void setWdKeysPreProcessingQueue(BlockingQueue<WDAppeKey> wdKeysPreProcessingQueue) {
		this.wdKeysPreProcessingQueue = wdKeysPreProcessingQueue;
	}

	public WatchdogConfiguration getConfigBean() {
		return configBean;
	}

	public void setConfigBean(WatchdogConfiguration configBean) {
		this.configBean = configBean;
	}

}
