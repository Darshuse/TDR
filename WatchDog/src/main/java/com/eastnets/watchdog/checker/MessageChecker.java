package com.eastnets.watchdog.checker;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.eastnets.entities.Appendix;
import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.entities.WDSettings;
import com.eastnets.watchdog.config.DAOFactory;
import com.eastnets.watchdog.resultbeans.PossibleDuplicateCustomMessageInfo;
import com.eastnets.watchdog.utility.Constants;

@Service
public class MessageChecker extends Checker {
	private static final Logger LOGGER = Logger.getLogger(MessageChecker.class);
	@Autowired
	@Qualifier("ReaderMessagesRequestsOutQueue")
	BlockingQueue<WDMessageSearchRequest> messagesRequestsPreProcessingQueue;
	@Autowired
	@Qualifier("CheckerMessagesKeysOutQueue")
	BlockingQueue<WDMessageRequestResult> messagesKeysPostProcessingQueue;

	@Autowired
	@Qualifier("ReaderWDKeysOutQueue")
	BlockingQueue<WDAppeKey> wdKeysPreProcessingQueue;

	@Autowired
	@Qualifier("CheckerWdNackResultOutQueue")
	BlockingQueue<WDNackResult> wdNackResultPostProecssingQueue;

	@Autowired
	@Qualifier("CheckerWdPossibleDuplicateResultOutQueue")
	BlockingQueue<WDPossibleDuplicateResult> wdPossibleDuplicateResultPostProecssingQueue;

	@Autowired
	@Qualifier("MessagesEmailsNotificationsToBeDumpedQueue")
	BlockingQueue<WDEmailNotification> messagesEmailsToBeDumpedQueue;

	@Autowired
	@Qualifier("CheckerWdKeysToBeDeletedQueue")
	BlockingQueue<WDAppeKey> wdkyesToBeDeleted;

	WDSettings wdSettings;

	@Autowired
	DAOFactory daoFactory;

	@PostConstruct
	public void init() {
		LOGGER.trace("init - Message Checker");
	}

	@Override
	public void run() {
		LOGGER.debug("Message Checker started");
		try {

			wdSettings = watchDogRepositoryService.getWDSettings().get(0);

			while (true) {

				check(wdKeysPreProcessingQueue.take());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void check(WDAppeKey wdKey) {
		Mesg mesg = watchDogRepositoryService.getMessageInformation(wdKey.getAppendixPK(), wdKey.getMesgCreaDateTime()).get(0);
		Appendix appe = watchDogRepositoryService.getAppendixInfo(wdKey.getAppendixPK()).get(0);
		int archived = mesg.getArchived();

		LOGGER.debug("Start Checking Message With " + "aid: " + appe.getId().getAid() + " umidl: " + appe.getId().getAppeSUmidl() + " umidh: " + appe.getId().getAppeSUmidh());

		// If message is archived, Stop and delete the entry from WDKEYS table.
		if (archived != 0) {
			LOGGER.info("Message is archived: " + "aid: " + appe.getId().getAid() + " umidl: " + appe.getId().getAppeSUmidl() + " umidh: " + appe.getId().getAppeSUmidh());
		} else {
			try {
				if (wdSettings.getUserReqest() == 1) {
					checkMessageSearchRequest(appe, wdKey, mesg);
				}
				if (wdSettings.getNack() == 1) {
					checkNacked(appe, wdKey.getMesgCreaDateTime());
				}
				if (wdSettings.getPossibleDup() == 1) {
					checkPossibleDuplicate(appe, wdKey.getMesgCreaDateTime());
				}
			} catch (Exception e) {
				daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
						"Error when Check messages  " + "aid: " + appe.getId().getAid() + " umidl: " + appe.getId().getAppeSUmidl() + " umidh: " + appe.getId().getAppeSUmidh());
				e.printStackTrace();
			}

		}
	}

	private void checkPossibleDuplicate(Appendix appe, Date xCreaDate) {

		boolean forceTest = false;
		String appeRcvDeliveryStatus = appe.getAppeRcvDeliveryStatus();
		if ((appe.getAppeType().equalsIgnoreCase("APPE_EMISSION") && appe.getAppeNetworkDeliveryStatus().equalsIgnoreCase("DLV_ACKED"))
				|| (appe.getAppeType().equalsIgnoreCase("APPE_RECEPTION") && (appeRcvDeliveryStatus != null && appeRcvDeliveryStatus.equalsIgnoreCase("RE_UACKED"))) || forceTest) {
			WDPossibleDuplicateResult prepareWDPossibleDuplicateResult = prepareWDPossibleDuplicateResult(appe.getId(), xCreaDate);
			wdPossibleDuplicateResultPostProecssingQueue.add(prepareWDPossibleDuplicateResult);
		}

	}

	private WDPossibleDuplicateResult prepareWDPossibleDuplicateResult(AppendixPK id, Date xCreaDate) {
		PossibleDuplicateCustomMessageInfo customMesgInfo = watchDogRepositoryService.getWDPossibleDuplicateResult(id);

		WDPossibleDuplicateResult wdPossibleDuplicateResult = new WDPossibleDuplicateResult();
		wdPossibleDuplicateResult.setAppendixKey(id);
		if (customMesgInfo != null) {
			wdPossibleDuplicateResult.setMesgPossibleDupCreation(customMesgInfo.getMesgPossibleDupCreation());
			wdPossibleDuplicateResult.setMesgUserIssuedAsPDE(customMesgInfo.getMesgUserIssuedAsPDE());
		}
		wdPossibleDuplicateResult.setInsertTime(new Date());
		wdPossibleDuplicateResult.setTempCreaDateTime(xCreaDate);

		return wdPossibleDuplicateResult;
	}

	private void checkNacked(Appendix appe, Date xCreaDate) {

		boolean forceTest = false;
		if ((appe.getAppeType().equalsIgnoreCase("APPE_EMISSION") && appe.getAppeNetworkDeliveryStatus().equalsIgnoreCase("DLV_NACKED")) || forceTest) {
			wdNackResultPostProecssingQueue.add(prepareWDNackResult(appe.getId(), xCreaDate));
		}
	}

	private WDNackResult prepareWDNackResult(AppendixPK id, Date xCreaDate) {

		LOGGER.debug("Creating WDNackResult entry");
		LOGGER.trace("for: " + id.toString());
		WDNackResult nackResult = new WDNackResult();
		nackResult.setAppendixKey(id);
		nackResult.setInsertTime(new Date());
		nackResult.setTempCreaDateTime(xCreaDate);
		return nackResult;
	}

	private void checkMessageSearchRequest(Appendix appe, WDAppeKey wdKey, Mesg mesg) {
		List<WDMessageRequestResult> matchedMessagesResults = watchDogRepositoryService.getMatchedMessagesResults(appe, wdKey.getMesgCreaDateTime(), mesg);
		if (matchedMessagesResults != null && !matchedMessagesResults.isEmpty()) {
			LOGGER.debug("Queue the matched messge to Message Dumber");
			messagesKeysPostProcessingQueue.addAll(matchedMessagesResults);
		} else {
			wdkyesToBeDeleted.add(wdKey);
		}
	}

	public BlockingQueue<WDMessageSearchRequest> getMessagesRequestsPreProcessingQueue() {
		return messagesRequestsPreProcessingQueue;
	}

	public void setMessagesRequestsPreProcessingQueue(BlockingQueue<WDMessageSearchRequest> messagesRequestsPreProcessingQueue) {
		this.messagesRequestsPreProcessingQueue = messagesRequestsPreProcessingQueue;
	}

	public BlockingQueue<WDMessageRequestResult> getMessagesKeysPostProcessingQueue() {
		return messagesKeysPostProcessingQueue;
	}

	public void setMessagesKeysPostProcessingQueue(BlockingQueue<WDMessageRequestResult> messagesKeysPostProcessingQueue) {
		this.messagesKeysPostProcessingQueue = messagesKeysPostProcessingQueue;
	}

	public BlockingQueue<WDAppeKey> getWdKeysPreProcessingQueue() {
		return wdKeysPreProcessingQueue;
	}

	public void setWdKeysPreProcessingQueue(BlockingQueue<WDAppeKey> wdKeysPreProcessingQueue) {
		this.wdKeysPreProcessingQueue = wdKeysPreProcessingQueue;
	}

	public BlockingQueue<WDNackResult> getWdNackResultPostProecssingQueue() {
		return wdNackResultPostProecssingQueue;
	}

	public void setWdNackResultPostProecssingQueue(BlockingQueue<WDNackResult> wdNackResultPostProecssingQueue) {
		this.wdNackResultPostProecssingQueue = wdNackResultPostProecssingQueue;
	}

	public WDSettings getWdSettings() {
		return wdSettings;
	}

	public void setWdSettings(WDSettings wdSettings) {
		this.wdSettings = wdSettings;
	}

	public BlockingQueue<WDPossibleDuplicateResult> getWdPossibleDuplicateResultPostProecssingQueue() {
		return wdPossibleDuplicateResultPostProecssingQueue;
	}

	public void setWdPossibleDuplicateResultPostProecssingQueue(BlockingQueue<WDPossibleDuplicateResult> wdPossibleDuplicateResultPostProecssingQueue) {
		this.wdPossibleDuplicateResultPostProecssingQueue = wdPossibleDuplicateResultPostProecssingQueue;
	}

	public BlockingQueue<WDEmailNotification> getMessagesEmailsToBeDumpedQueue() {
		return messagesEmailsToBeDumpedQueue;
	}

	public void setMessagesEmailsToBeDumpedQueue(BlockingQueue<WDEmailNotification> messagesEmailsToBeDumpedQueue) {
		this.messagesEmailsToBeDumpedQueue = messagesEmailsToBeDumpedQueue;
	}

}
