package com.eastnets.watchdog.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.entities.Appendix;
import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.GlobalSettings;
import com.eastnets.entities.Jrnl;
import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.LiveMessage;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDJrnlSearchRequest;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.entities.WDSettings;
import com.eastnets.repositories.AppendixRepository;
import com.eastnets.repositories.GlobalSettingsRepository;
import com.eastnets.repositories.JrnlRepository;
import com.eastnets.repositories.LiveMessageRepository;
import com.eastnets.repositories.MesgRepository;
import com.eastnets.repositories.WDAppeKeyRepository;
import com.eastnets.repositories.WDEmailNotificationRepository;
import com.eastnets.repositories.WDEventRequestResultRepository;
import com.eastnets.repositories.WDJrnlKeyRepository;
import com.eastnets.repositories.WDJrnlSearchRequestRepository;
import com.eastnets.repositories.WDMessageRequestResultRepository;
import com.eastnets.repositories.WDMessageSearchRequestRepository;
import com.eastnets.repositories.WDNackResultRepository;
import com.eastnets.repositories.WDPossibleDuplicateResultRepository;
import com.eastnets.repositories.WDSettingsRepository;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.criteria.builders.AbstractOperator;
import com.eastnets.watchdog.criteria.builders.SimpleAndOperatorChecker;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;
import com.eastnets.watchdog.resultbeans.PossibleDuplicateCustomMessageInfo;
import com.eastnets.watchdog.resultbeans.WDAppeKeyBean;
import com.eastnets.watchdog.resultbeans.WDJrnlKeyBean;

/**
 * 
 * @author malbashiti
 *
 */

@Service
@Transactional(readOnly = true)
public class WatchDogRepositoryService {

	private static final Logger LOGGER = Logger.getLogger(WatchDogRepositoryService.class);

	@Autowired
	private GlobalSettingsRepository globalSettingsRepository;

	@Autowired
	private WDSettingsRepository wdSettingsRepository;

	@Autowired
	private AppendixRepository appendixRepository;

	@Autowired
	private MesgRepository mesgRepository;

	@Autowired
	private JrnlRepository jrnlRepository;

	@Autowired
	private WDJrnlKeyRepository wdJrnlKeyRepository;

	@Autowired
	private WDAppeKeyRepository wdAppeKeyRepository;

	@Autowired
	private WDNackResultRepository wdNackResultRepository;

	@Autowired
	private WDPossibleDuplicateResultRepository wdPossibleDuplicateResultRepository;

	@Autowired
	private WDMessageSearchRequestRepository wdMessageSearchRequestRepository;

	@Autowired
	private WDMessageRequestResultRepository wdMessageRequestResultRepository;

	@Autowired
	private WDJrnlSearchRequestRepository wdJrnlSearchRequestRepository;

	@Autowired
	private LiveMessageRepository liveMessageRepository;

	@Autowired
	private WDEmailNotificationRepository wdEmailNotificationRepository;

	@Autowired
	private WDEventRequestResultRepository wdEventRequestResultRepository;

	@Autowired
	@Qualifier("MessagesEmailsNotificationsToBeDumpedQueue")
	BlockingQueue<WDEmailNotification> messagesEmailsToBeDumpedQueue;

	@Autowired
	@Qualifier("messagesRequstToBeDeletedQueue")
	BlockingQueue<WDMessageSearchRequest> messageRequstTobeDeleted;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	private AbstractOperator searchOperator = new SimpleAndOperatorChecker();

	/**
	 * functions that call repositories methods
	 */

	@Transactional(readOnly = true)
	public List<GlobalSettings> getGlobalSettings() {
		return globalSettingsRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<WDSettings> getWDSettings() {
		return wdSettingsRepository.findAll();
	}

	public List<WDMessageSearchRequest> getMessageSearchRequests() {
		return wdMessageSearchRequestRepository.findAll();
	}

	public List<WDJrnlSearchRequest> getJrnlSearchRequests() {
		return wdJrnlSearchRequestRepository.findAll();
	}

	public List<Jrnl> getJrnls() {
		return jrnlRepository.findAll();
	}

	public List<LiveMessage> getLiveMessages() {
		return liveMessageRepository.getLiveMessages();
	}

	public List<MessageEmailNotification> getAvailableMessagesEmailNotifications() {
		if (!watchdogConfiguration.isPartitioned()) {
			return wdEmailNotificationRepository.getAvailableMessagesEmailNotifications();
		} else {

			return wdEmailNotificationRepository.getAvailableMessagesEmailNotificationsPart();
		}
	}

	public List<EventEmailNotification> getAvailableEventsEmailNotifications() {
		if (!watchdogConfiguration.isPartitioned()) {

			return wdEmailNotificationRepository.getAvailableEventsEmailNotifications();
		} else {

			return wdEmailNotificationRepository.getAvailableEventsEmailNotificationsPart();
		}
	}

	@SuppressWarnings("deprecation")
	public List<WDMessageSearchRequest> getWDMessagesSearchRequests(int bulkSize) {
		return wdMessageSearchRequestRepository.getMessagesNotificationRequests(PageRequest.of(0, bulkSize), new Date()).getContent();
	}

	public List<WDJrnlSearchRequest> getWdJrnlSearchRequests(String jrnlCompName, Integer jrnlEventNumber) {
		return wdJrnlSearchRequestRepository.getWdJrnlSearchRequest(jrnlCompName, jrnlEventNumber);
	}

	public List<WDAppeKeyBean> getWdKeys(int bulkSize) {
		return wdAppeKeyRepository.getWDAppeKeys(PageRequest.of(0, bulkSize), watchdogConfiguration.isMultiAlliance(), watchdogConfiguration.getAid());
	}

	public List<WDAppeKeyBean> getWdKeysPart(int bulkSize) {
		return wdAppeKeyRepository.getWDAppeKeysPart(PageRequest.of(0, bulkSize), watchdogConfiguration.isMultiAlliance(), watchdogConfiguration.getAid());
	}

	public List<WDAppeKey> restoreWDKeys() {
		return wdAppeKeyRepository.restoreWDKeys();
	}

	public List<WDJrnlKeyBean> getWdJrnlKeys(int bulkSize) {
		return wdJrnlKeyRepository.getWDJrnlKeys(PageRequest.of(0, bulkSize), watchdogConfiguration.isMultiAlliance(), watchdogConfiguration.getAid());
	}

	public List<WDJrnlKeyBean> getWdJrnlKeysPart(int bulkSize) {
		return wdJrnlKeyRepository.getWDJrnlKeysPart(PageRequest.of(0, bulkSize), watchdogConfiguration.isMultiAlliance(), watchdogConfiguration.getAid());
	}

	public List<WDMessageRequestResult> processWDAppe(WDAppeKey wdKey) {
		return null;
	}

	public List<WDMessageRequestResult> getMatchedMessagesResults(Appendix appe, Date xCreaDate, Mesg mesg) {
		List<WDMessageRequestResult> messagesRequestsResults = new ArrayList<>();
		List<WDMessageSearchRequest> messagesRequests = findMessageMatchedMessagesRequests(appe);

		boolean isMessageMatch = false;
		for (WDMessageSearchRequest searchRequest : messagesRequests) {
			if (searchRequest.getExpirationDate() != null) {
				LocalDate expirationDate = convertToLocalDate(searchRequest.getExpirationDate());
				LocalDate curuntDate = convertToLocalDate(new Date());
				if (curuntDate.isAfter(expirationDate)) {
					LOGGER.info("the Requst with ID" + searchRequest.getRequestID() + " is expiered ");
					continue;
				}
			}
			if (searchRequest.getFieldCode() == null) {
				isMessageMatch = searchOperator.isMatchedMessage(mesg, searchRequest, appe, false, false, watchdogConfiguration.isPartitioned(), xCreaDate);
			} else if (searchRequest.getFieldCode() == 255) {
				isMessageMatch = searchOperator.isMatchedMessage(mesg, searchRequest, appe, true, false, watchdogConfiguration.isPartitioned(), xCreaDate);
			} else {
				isMessageMatch = searchOperator.isMatchedMessage(mesg, searchRequest, appe, false, true, watchdogConfiguration.isPartitioned(), xCreaDate);
			}

			if (isMessageMatch) {
				LOGGER.debug("Message with " + "aid: " + appe.getId().getAid() + " umidl: " + appe.getId().getAppeSUmidl() + " umidh: " + appe.getId().getAppeSUmidh() + " Matched With Request " + searchRequest.getDescription());
				messagesRequestsResults.add(prepareMessageRequestResult(mesg, appe, searchRequest));
				if (searchRequest.getEmail() != null && !searchRequest.getEmail().isEmpty()) {
					messagesEmailsToBeDumpedQueue.add(prepareMessageEmailNotification(searchRequest, appe, mesg));
				}
				if (searchRequest.getAutoDelete() == 1) {
					messageRequstTobeDeleted.add(searchRequest);
				}
			}
			isMessageMatch = false;
		}
		return messagesRequestsResults;

	}

	public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private WDEmailNotification prepareMessageEmailNotification(WDMessageSearchRequest searchRequest, Appendix appe, Mesg mesg) {
		WDEmailNotification emailNotification = new WDEmailNotification();
		emailNotification.setAid(appe.getId().getAid());
		emailNotification.setDescription(searchRequest.getDescription());
		emailNotification.setUsername(searchRequest.getEmail());
		emailNotification.setUmidh(appe.getId().getAppeSUmidh());
		emailNotification.setUmidl(appe.getId().getAppeSUmidl());
		emailNotification.setMessageType(searchRequest.getMessageType());
		emailNotification.setWdID(searchRequest.getRequestID());
		emailNotification.setProcessStatus(0);
		emailNotification.setMesgCreaDateTime(mesg.getMesgCreaDateTime());
		return emailNotification;
	}

	private WDMessageRequestResult prepareMessageRequestResult(Mesg mesg, Appendix appe, WDMessageSearchRequest searchRequest) {
		WDMessageRequestResult requestResult = new WDMessageRequestResult();
		requestResult.setAppeID(appe.getId());
		requestResult.setDescription(searchRequest.getDescription());
		requestResult.setIdentifier(mesg.getMesgIdentifier());
		requestResult.setRequestId(searchRequest.getRequestID());
		requestResult.setMesgCreateOperNickname(mesg.getMesgCreaOperNickname());
		requestResult.setMesgCreateDateTime(mesg.getMesgCreaDateTime());
		requestResult.setMesgReceiverSwiftAddress(mesg.getxReceiverX1().substring(0, 8));
		requestResult.setMesgSenderSwiftAddress(mesg.getMesgSenderX1().substring(0, 8));
		requestResult.setMesgSubFormat(mesg.getMesgSubFormat());
		requestResult.setMesgType(mesg.getMesgType());
		requestResult.setMesgTrnRef(mesg.getMesgTrnRef());
		requestResult.setxFinCcy(mesg.getxFinCcy());
		requestResult.setxFinAmount(mesg.getxFinAmount());
		requestResult.setxFinValueDate(mesg.getxFinValueDate());
		requestResult.setAppeSessionHolder(appe.getAppeSessionHolder());
		requestResult.setAppeCreaMPFNName(appe.getAppeCreaMpfnName());
		requestResult.setAppeSessionNumber(appe.getAppeSessionNbr());
		requestResult.setAppeSequenceNumber(appe.getAppeSequenceNbr());
		requestResult.setInsertTime(new Date());
		requestResult.setUsername(searchRequest.getUsername());
		requestResult.setUserGroup(searchRequest.getGroupRequest());
		requestResult.setResponderDN(searchRequest.getResponderDN());
		requestResult.setRequestorDN(searchRequest.getRequestorDN());
		if (searchRequest.getEmail() != null && !searchRequest.getEmail().isEmpty()) {
			requestResult.setAddToEmailQueue(true);
		}
		return requestResult;
	}

	private List<WDMessageSearchRequest> findMessageMatchedMessagesRequests(Appendix appe) {
		int networkDeliveryStatus = getNetworkDeliveryStatus(appe.getAppeNetworkDeliveryStatus(), appe.getAppeRcvDeliveryStatus());

		return wdMessageSearchRequestRepository.findAllRequestsWithMatchingStatus(networkDeliveryStatus);
	}

	private int getNetworkDeliveryStatus(String appeNetworkDeliveryStatus, String appeRcvDeliveryStatus) {

		if (appeNetworkDeliveryStatus == null || appeRcvDeliveryStatus == null)
			return 0;

		if (appeNetworkDeliveryStatus.equalsIgnoreCase("DLV_ACKED") || (appeRcvDeliveryStatus.equalsIgnoreCase("RE_UACKED") || appeRcvDeliveryStatus.equalsIgnoreCase("EM_DELIVERED"))) {
			return 1;

		} else if (appeNetworkDeliveryStatus.equalsIgnoreCase("DLV_NACKED") || appeRcvDeliveryStatus.equalsIgnoreCase("RE_UNACKED")) {
			return 2;
		} else {
			return 0;
		}
	}

	@Transactional
	public void saveEventRequestsResults(List<WDEventRequestResult> eventRequestsResults) {
		wdEventRequestResultRepository.saveAll(eventRequestsResults);
	}

	@Transactional
	public void saveMessageRequestsResults(List<WDMessageRequestResult> messageRequestsResults) {
		wdMessageRequestResultRepository.saveAll(messageRequestsResults);
	}

	@Transactional
	public void saveNackResults(List<WDNackResult> nackResults) {
		wdNackResultRepository.saveAll(nackResults);
	}

	@Transactional
	public void savePossibleDuplicates(List<WDPossibleDuplicateResult> possibleDuplicates) {
		wdPossibleDuplicateResultRepository.saveAll(possibleDuplicates);
	}

	public void updateStatusOfEmailNotificationToReader(Integer id) {
		wdEmailNotificationRepository.updateEmailReaderStatus(id);
	}

	public void saveEmailNotificaionts(List<WDEmailNotification> emailNotifications) {
		wdEmailNotificationRepository.saveAll(emailNotifications);
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

	private boolean isNotEmpty(String field) {
		return field != null && !field.isEmpty();
	}

	public List<Appendix> getAppendixInfo(AppendixPK id) {
		return appendixRepository.getAppendixInfo(id.getAppeSUmidl(), id.getAppeSUmidh(), id.getAid(), id.getAppeInstNum(), id.getAppeDateTime(), id.getAppeSeqNbr());
	}

	public int checkIfMessagesIsArchived(AppendixPK id, Date mesgCreaDateTime) {
		if (!watchdogConfiguration.isPartitioned()) {
			return mesgRepository.isMessageArchived(id.getAid(), id.getAppeSUmidl(), id.getAppeSUmidh());
		} else {
			return mesgRepository.isMessageArchivedPart(id.getAid(), id.getAppeSUmidl(), id.getAppeSUmidh(), mesgCreaDateTime);
		}
	}

	public List<Mesg> getMessageInformation(AppendixPK id, Date mesgCreaDateTime) {
		return mesgRepository.findAll(new Specification<Mesg>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Mesg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.equal(root.get("id").get("aid"), id.getAid()));
				predicates.add(cb.equal(root.get("id").get("umidl"), id.getAppeSUmidl()));
				predicates.add(cb.equal(root.get("id").get("umidh"), id.getAppeSUmidh()));
				if (watchdogConfiguration.isPartitioned()) {
					predicates.add(cb.equal(root.get("mesgCreaDateTime"), mesgCreaDateTime));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
	}

	public List<Appendix> getAppInformation(AppendixPK id, Date mesgCreaDateTime) {
		return appendixRepository.findAll(new Specification<Appendix>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Appendix> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.equal(root.get("id").get("aid"), id.getAid()));
				predicates.add(cb.equal(root.get("id").get("umidl"), id.getAppeSUmidl()));
				predicates.add(cb.equal(root.get("id").get("umidh"), id.getAppeSUmidh()));
				if (watchdogConfiguration.isPartitioned()) {
					predicates.add(cb.equal(root.get("mesgCreaDateTime"), mesgCreaDateTime));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
	}

	public PossibleDuplicateCustomMessageInfo getWDPossibleDuplicateResult(AppendixPK id) {
		List<PossibleDuplicateCustomMessageInfo> mesgInfoForPossibleDuplicates = mesgRepository.getMesgInfoForPossibleDuplicate(id.getAppeSUmidh(), id.getAppeSUmidl(), id.getAid());
		if (mesgInfoForPossibleDuplicates != null && !mesgInfoForPossibleDuplicates.isEmpty()) {
			return mesgInfoForPossibleDuplicates.get(0);
		} else {
			return null;
		}

	}

	public void deleteWDJrnlKeys(List<JrnlPK> jrnlPKs) {
		try {
			for (JrnlPK pk : jrnlPKs) {
				wdJrnlKeyRepository.deleteById(pk);
			}
		} catch (Exception e) {
			System.out.println("HOLA");
			e.printStackTrace();
		}

	}

	public void deletewdSerachParaRequst(Integer requestID) {
		wdMessageSearchRequestRepository.deleteMessageRequest(requestID);
	}

}
