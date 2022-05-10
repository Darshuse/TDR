package com.eastnets.watchdog.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.Jrnl;
import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.JrnlSearchRequest;
import com.eastnets.entities.LiveMessage;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.MessageSearchRequest;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.repositories.JrnlRepository;
import com.eastnets.repositories.JrnlSearchRequestRepository;
import com.eastnets.repositories.LiveMessageRepository;
import com.eastnets.repositories.MessageRepository;
import com.eastnets.repositories.MessageSearchRequestRepository;
import com.eastnets.repositories.WDAppeKeyRepository;
import com.eastnets.repositories.WDEmailNotificationRepository;
import com.eastnets.repositories.WDEventRequestResultRepository;
import com.eastnets.repositories.WDJrnlKeyRepository;
import com.eastnets.watchdog.custom.results.EventEmailNotification;
import com.eastnets.watchdog.custom.results.MessageEmailNotification;

/**
 * 
 * @author malbashiti
 *
 */

@Service
@Transactional(readOnly = true)
public class WatchDogService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private JrnlRepository jrnlRepository;

	@Autowired
	private MessageSearchRequestRepository messageSearchRequestRepository;

	@Autowired
	private JrnlSearchRequestRepository jrnlSearchRequestRepository;

	@Autowired
	private LiveMessageRepository liveMessageRepository;

	@Autowired
	private WDEmailNotificationRepository wdEmailNotificationRepository;

	@Autowired
	private WDJrnlKeyRepository wdJrnlKeyRepository;

	@Autowired
	private WDAppeKeyRepository wdKeyRepository;

	@Autowired
	private WDEventRequestResultRepository wdEventRequestResultRepository;

	/**
	 * services that call repositories methods
	 */

	public List<MessageSearchRequest> getMessageSearchRequests() {
		return messageSearchRequestRepository.findAll();
	}

	public List<JrnlSearchRequest> getJrnlSearchRequests() {
		return jrnlSearchRequestRepository.findAll();
	}

	public List<Jrnl> getJrnls() {
		return jrnlRepository.findAll();
	}

	public List<Mesg> getMessages() {
		return messageRepository.findAll();
	}

	public List<LiveMessage> getLiveMessages() {
		return liveMessageRepository.getLiveMessages();
	}

	public List<MessageEmailNotification> getAvailableMessagesEmailNotifications() {
		return wdEmailNotificationRepository.getAvailableMessagesEmailNotifications();
	}

	public List<EventEmailNotification> getAvailableEventsEmailNotifications() {
		return wdEmailNotificationRepository.getAvailableEventsEmailNotifications();
	}

	@SuppressWarnings("deprecation")
	public List<WDAppeKey> getWdAppeKeys(int bulkSize) {
		return wdKeyRepository.getWdKeys(new PageRequest(0, bulkSize)).getContent();
	}

	@SuppressWarnings("deprecation")
	public List<WDJrnlKey> getWdJrnlKeys(int bulkSize) {
		return wdJrnlKeyRepository.getWdJrnlKeys(new PageRequest(0, bulkSize)).getContent();
	}

	public List<JrnlSearchRequest> checkWDJrnlKey(String jrnlCompName, Integer jrnlEventNumber) {
		return wdJrnlKeyRepository.matchJrnlRequestsWithKeys(jrnlCompName, jrnlEventNumber, new Date());
	}

	public List<MessageSearchRequest> checkWDMessageKey(String jrnlCompName, Integer jrnlEventNumber) {
		// Need to be updated
		return null;
	}

	@Transactional
	public void saveEventRequestsResults(List<WDEventRequestResult> eventRequestsResults) {
		wdEventRequestResultRepository.saveAll(eventRequestsResults);
	}

	@Transactional
	public void saveMessageRequestsResults(List<WDMessageRequestResult> messageRequestsResults) {
	}

	@Transactional
	public void updateStatusOfJrnlKeysToReader(JrnlPK pk) {
		wdJrnlKeyRepository.updateJrnlReaderStatus(pk.getAid(), pk.getJrnlRevDateTime(), pk.getJrnlSeqNumber());
	}

	public void updateStatusOfAppeKeysToReader(AppendixPK pk) {
		wdKeyRepository.updateAppeReaderStatus(pk.getAid(), pk.getAppeSUmidh(), pk.getAppeSUmidl(), pk.getAppeInstNum(),
				pk.getAppeDateTime(), pk.getAppeSeqNbr(), new Date());
	}

	public void updateStatusOfEmailNotificationToReader(Integer id) {
		wdEmailNotificationRepository.updateEmailReaderStatus(id);
	}

	public void saveEmailNotificaionts(List<WDEmailNotification> emailNotifications) {
		wdEmailNotificationRepository.saveAll(emailNotifications);
	}

	public List<WDJrnlKey> restoreWdJrnlKeys() {
		return wdJrnlKeyRepository.restoreJrnlKeys();
	}

	public List<WDAppeKey> restoreWdMessageKeys() {
		return wdKeyRepository.restoreKeys();
	}

	public List<MessageEmailNotification> restoreMessageEmailNotifications() {
		return wdEmailNotificationRepository.restoreMessageEmailNotifications();
	}

	public List<EventEmailNotification> restoreEventsEmailNotificaions() {
		return wdEmailNotificationRepository.restoreEventEmailNotitifications();
	}

	public void removeEmailNotification(Integer id) {
		wdEmailNotificationRepository.deleteById(id);
	}

	public void updateStatusOfEmailNotificationToFailed(Integer id) {
		wdEmailNotificationRepository.updateEmailFailedStatus(id);
	}

}
