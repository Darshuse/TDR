package com.eastnets.watchdog.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;
import com.eastnets.watchdog.resultbeans.User;
import com.eastnets.watchdog.resultbeans.WDAppeKeyBean;
import com.eastnets.watchdog.resultbeans.WDJrnlKeyBean;

public abstract class WatchdogDao {

	public abstract void deleteWDkeys(List<WDAppeKey> wdAppeKeys, boolean flag);

	public abstract void deleteWDkeysNonPart(List<AppendixPK> appendixPKs);

	public abstract void deleteWDkeys(List<AppendixPK> appendixPKs);

	public abstract void deleteWdJrnlKey(List<JrnlPK> jrnlPKs);

	public abstract void deleteMessageRequest(List<WDMessageSearchRequest> messageSearchRequests);

	public abstract void updateWdJurnalKey(List<WDJrnlKeyBean> matchedJrnlKeys);

	public abstract void updateWdAppeKey(List<WDAppeKeyBean> matchedAppeKeys);

	public abstract void updateMessageWDEmailNotification(List<MessageEmailNotification> emailNotifications);

	public abstract void updateEventWDEmailNotification(List<EventEmailNotification> eventEmailNotifications);

	public abstract void deleteMessageWDEmailNotification(MessageEmailNotification emailNotification);

	public abstract void deleteEventWDEmailNotification(EventEmailNotification eventEmailNotification);

	public abstract List<Long> getTest();

	public abstract String getExpandedMesssageText(String syntaxVersion, String messageType, String unexpandedText, java.util.Date messageDate, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies);

	public abstract Map<String, Integer> getCurrenciesISO();

	public abstract void insertIntoWdEmailNotifaction(List<WDEmailNotification> emailNotifications);

	public abstract void saveWDMessageRequestResult(List<WDMessageRequestResult> messageRequestsResults);

	public abstract void saveWDEmailNotification(List<WDEmailNotification> emailNotifications);

	public abstract void saveNackResults(List<WDNackResult> nackResults);

	public abstract void savePossibleDuplicates(List<WDPossibleDuplicateResult> possibleDuplicates);

	public abstract void saveEventResults(List<WDEventRequestResult> eventRequestsResults);

	public abstract User getUserProfileId(String username);

	public abstract List<String> getProfileRoles(Long profileID);

	public abstract boolean sCheckRoals();

	public abstract void insertLdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2);

}
