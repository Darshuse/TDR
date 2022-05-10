package com.eastnets.watchdog.config;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.watchdog.resultbeans.EmailNotification;

@Configuration
public class QueueConfigration {

	@Bean
	public LinkedBlockingQueue<WDMessageSearchRequest> ReaderMessagesRequestsOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDJrnlKey> ReaderEventsRequestsOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDAppeKey> ReaderWDKeysOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDMessageRequestResult> CheckerMessagesKeysOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDEventRequestResult> CheckerEventsKeysOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDNackResult> CheckerWdNackResultOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDPossibleDuplicateResult> CheckerWdPossibleDuplicateResultOutQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDEmailNotification> EventsEmailsNotificationsToBeDumpedQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDEmailNotification> MessagesEmailsNotificationsToBeDumpedQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<EmailNotification> EmailsNotificationsToBeSentQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDMessageSearchRequest> messagesRequstToBeDeletedQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDAppeKey> CheckerWdKeysToBeDeletedQueue() {
		return new LinkedBlockingQueue<>(3000);
	}

	@Bean
	public LinkedBlockingQueue<WDJrnlKey> CheckerWdJrnlKeysToBeDeletedQueue() {
		return new LinkedBlockingQueue<>(3000);
	}
}
