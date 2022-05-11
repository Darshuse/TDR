/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.watchdog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.watchdog.WatchDogDAO;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.watchdog.Annotaion;
import com.eastnets.domain.watchdog.EventNotification;
import com.eastnets.domain.watchdog.EventRequest;
import com.eastnets.domain.watchdog.MessageNotification;
import com.eastnets.domain.watchdog.MessageRequest;
import com.eastnets.domain.watchdog.WDNotificationSettings;
import com.eastnets.service.ServiceBaseImp;

/**
 * WatchDog Service Implementation
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public class WatchDogServiceImp extends ServiceBaseImp implements WatchDogService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3974929083969952596L;

	private WatchDogDAO watchDogDAO;
	private CommonDAO commonDAO;

	private Date calculateDateTime(Long wdNbDayHistory) {

		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);

		if (wdNbDayHistory > 0) {
			int neg = -1 * wdNbDayHistory.intValue();
			instance.add(Calendar.DATE, neg);
		}

		Date time = instance.getTime();
		return time;
	}

	@Override
	public List<MessageRequest> getMessageRequests(String loggedInUser, boolean sideAdminUser, Long loggedInUserGroupId) {
		return watchDogDAO.getMessageRequests(sideAdminUser, loggedInUserGroupId);
	}

	@Override
	public List<MessageNotification> getMessageNotifications(String loggedInUser, Long wdNbDayHistory, boolean sideAdminUser, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId) {

		Date calculateDateTime = calculateDateTime(wdNbDayHistory);

		List<MessageNotification> messageNotificationsList = watchDogDAO.getMessageNotifications(calculateDateTime, sideAdminUser, from, to, loggedInUserGroupId);
		getMessageAnnotations(messageNotificationsList, hideAnnotatedMessages);
		return messageNotificationsList;
	}

	@Override
	public List<MessageNotification> getCalculatedDupplicateNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId) {

		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		List<MessageNotification> calculatedDupplicateNotifications = watchDogDAO.getCalculatedDupplicateNotifications(calculateDateTime, from, to, loggedInUserGroupId);
		getMessageAnnotations(calculatedDupplicateNotifications, hideAnnotatedMessages);
		return calculatedDupplicateNotifications;
	}

	@Override
	public List<MessageNotification> getPossibleDupplicateNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId) {

		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		List<MessageNotification> possibleDupplicateNotifications = watchDogDAO.getPossibleDupplicateNotifications(calculateDateTime, from, to, loggedInUserGroupId);
		getMessageAnnotations(possibleDupplicateNotifications, hideAnnotatedMessages);
		return possibleDupplicateNotifications;

	}

	@Override
	public List<MessageNotification> getNakedNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId) {

		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		List<MessageNotification> nakedNotifications = watchDogDAO.getNakedNotifications(calculateDateTime, from, to, loggedInUserGroupId);
		getMessageAnnotations(nakedNotifications, hideAnnotatedMessages);
		return nakedNotifications;
	}

	@Override
	public List<EventNotification> getEventNotifications(String loggedInUser, Long iNbDayHistory, boolean sideAdminUser, Long from, Long to) {

		Date calculateDateTime = calculateDateTime(iNbDayHistory);
		return watchDogDAO.getEventNotifications(calculateDateTime, loggedInUser, sideAdminUser, from, to);
	}

	@Override
	public MessageRequest getMessageRequest(String loggedInUser, Long id, boolean sideAdminUser, Long loggedInUserGroupId) {
		MessageRequest messageRequest = watchDogDAO.getMessageRequest(id, sideAdminUser, loggedInUserGroupId);
		if (messageRequest != null) {
			Long dateValueOp = messageRequest.getDateValueOp();
			if (dateValueOp == null || dateValueOp.equals(-1L)) {
				messageRequest.setDaysValueDate(null);
			}
		}
		return messageRequest;
	}

	@Override
	public List<EventRequest> getEventRequests(String loggedInUser, boolean sideAdminUser) {
		return watchDogDAO.getEventRequests(sideAdminUser, loggedInUser);
	}

	@Override
	public Long addMessageRequest(String loggedInUser, MessageRequest param) {
		Long code = watchDogDAO.addMessageRequest(param, loggedInUser);
		return code;
	}

	protected void getMessageAnnotations(List<MessageNotification> messageNotificationsList, boolean hideAnnotatedMessages) {

		List<MessageNotification> list = new ArrayList<MessageNotification>();

		for (MessageNotification messageNotification : messageNotificationsList) {
			List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(messageNotification.getId(), messageNotification.getNotificationType());

			if (notificationAnnotaions != null && notificationAnnotaions.size() > 0) {
				list.add(messageNotification);
			}
			messageNotification.setAnnotaionsList(notificationAnnotaions);
		}

		if (hideAnnotatedMessages && list.size() > 0) {
			messageNotificationsList.removeAll(list);
			list.clear();
		}
	}

	@Override
	public void deleteMessageRequest(String loggedInUser, List<MessageRequest> messageRequests) {
		for (MessageRequest messageRequest : messageRequests) {
			if (messageRequest.isChecked()) {
				watchDogDAO.deleteMessageRequest(messageRequest.getId());
			}
		}
	}

	@Override
	public void deleteEventRequest(String loggedInUser, List<EventRequest> eventRequests) {
		for (EventRequest eventRequest : eventRequests) {
			if (eventRequest.isChecked()) {
				watchDogDAO.deleteEventRequest(eventRequest.getId());
			}
		}
	}

	@Override
	public Long addEventRequest(String loggedInUser, EventRequest param) {
		return watchDogDAO.addEventRequest(param, loggedInUser);
	}

	@Override
	public void deleteEventNotification(String loggedInUser, List<EventNotification> eventNotifications) {
		for (EventNotification eventNotification : eventNotifications) {
			if (eventNotification.isChecked()) {
				watchDogDAO.deleteEventNotification(eventNotification.getNotificationId());
			}
		}
	}

	@Override
	public void deleteMessageNotification(String loggedInUser, List<MessageNotification> messageNotifications) {
		for (MessageNotification messageNotification : messageNotifications) {
			if (messageNotification.isChecked()) {
				watchDogDAO.deleteMessageNotification(messageNotification.getNotificationType(), messageNotification.getId());
			}
		}

	}

	@Override
	public void addWDSettings(String loggedInUser, Long userId, WDNotificationSettings wdGeneralSettings) {

		Long aid = 0L;
		ApplicationSetting applicationSetting = new ApplicationSetting();
		applicationSetting.setAllianceID(aid);
		applicationSetting.setId(Constants.APPLICATION_WATCHDOG_GENERAL_SETTINGS);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("NotificationCount");
		applicationSetting.setFieldValue(wdGeneralSettings.getMsgNotificationCount().toString());
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("MsgGapsNotificationCount");
		applicationSetting.setFieldValue(wdGeneralSettings.getMsgGapsNotificationCount().toString());
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("EventNotificationCount");
		applicationSetting.setFieldValue(wdGeneralSettings.getEventNotificationCount().toString());
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("NotificationHistory");
		applicationSetting.setFieldValue(wdGeneralSettings.getMsgNotificationHistory().toString());
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("MsgGapsNotificationHistory");
		applicationSetting.setFieldValue(wdGeneralSettings.getMsgGapsNotificationHistory().toString());
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("EventNotificationHistory");
		applicationSetting.setFieldValue(wdGeneralSettings.getEventNotificationHistory().toString());
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("HideAnnotatedNotifications");
		applicationSetting.setFieldValue((wdGeneralSettings.isHideAnnotatedNotifications()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("HideAnnotatedGapsNotifications");
		applicationSetting.setFieldValue((wdGeneralSettings.isHideGapsAnnotatedNotifications()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowMessageNotification");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowMessageNotifications()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowEventNotifications");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowEventNotifications()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowPossibleDuplicates");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowPossibleDuplicates()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowCalculatedDuplicates");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowCalculatedDuplicates()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowNakedMesages");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowNakedMesages()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowIsnGaps");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowIsnGaps()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowOsnGaps");
		applicationSetting.setFieldValue((wdGeneralSettings.isShowOsnGaps()) ? "True" : "False");
		this.commonDAO.addApplicationSetting(applicationSetting);

	}

	@Override
	public void addDefaultWDSettings(String loggedInUser, Long userId, WDNotificationSettings wdSettings) {

		this.deleteWDSettings(loggedInUser, userId);
		this.addWDSettings(loggedInUser, userId, wdSettings);
	}

	public void deleteWDSettings(String loggedInUser, Long userId) {

		Long aid = 0L;
		ApplicationSetting applicationSetting = new ApplicationSetting();
		applicationSetting.setAllianceID(aid);
		applicationSetting.setId(Constants.APPLICATION_WATCHDOG_GENERAL_SETTINGS);
		applicationSetting.setUserID(userId);

		applicationSetting.setFieldName("NotificationCount");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("MsgGapsNotificationCount");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("EventNotificationCount");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("NotificationHistory");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("MsgGapsNotificationHistory");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("EventNotificationHistory");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("HideAnnotatedNotifications");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("HideAnnotatedGapsNotifications");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowMessageNotification");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowEventNotifications");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowPossibleDuplicates");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowCalculatedDuplicates");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowNakedMesages");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowIsnGaps");
		this.commonDAO.deleteApplicationSetting(applicationSetting);

		applicationSetting.setFieldName("ShowOsnGaps");
		this.commonDAO.deleteApplicationSetting(applicationSetting);
		;

	}

	@Override
	public WDNotificationSettings getWDSettings(String loggedInUser, Long userId) {

		Long aid = 0L;
		List<ApplicationSetting> applicationSettings = commonDAO.getApplicationSettings(Constants.APPLICATION_WATCHDOG_GENERAL_SETTINGS, userId, aid);

		if (applicationSettings == null || applicationSettings.isEmpty()) {
			return null;
		}

		WDNotificationSettings wdSettings = new WDNotificationSettings();

		for (ApplicationSetting applicationSetting : applicationSettings) {

			// General Setting
			if (applicationSetting.getFieldName().equalsIgnoreCase("NotificationCount")) {
				wdSettings.setMsgNotificationCount(Long.parseLong(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("MsgGapsNotificationCount")) {
				wdSettings.setMsgGapsNotificationCount(Long.parseLong(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("EventNotificationCount")) {
				wdSettings.setEventNotificationCount(Long.parseLong(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("NotificationHistory")) {
				wdSettings.setMsgNotificationHistory(Long.parseLong(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("MsgGapsNotificationHistory")) {
				wdSettings.setMsgGapsNotificationHistory(Long.parseLong(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("EventNotificationHistory")) {
				wdSettings.setEventNotificationHistory(Long.parseLong(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("HideAnnotatedNotifications")) {
				wdSettings.setHideAnnotatedNotifications(Boolean.parseBoolean(applicationSetting.getFieldValue()));

			} else if (applicationSetting.getFieldName().equalsIgnoreCase("HideAnnotatedGapsNotifications")) {
				wdSettings.setHideGapsAnnotatedNotifications(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			// Notification Setting
			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowMessageNotification")) {
				wdSettings.setShowMessageNotifications(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowEventNotifications")) {
				wdSettings.setShowEventNotifications(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowPossibleDuplicates")) {
				wdSettings.setShowPossibleDuplicates(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowCalculatedDuplicates")) {
				wdSettings.setShowCalculatedDuplicates(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowNakedMesages")) {
				wdSettings.setShowNakedMesages(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowIsnGaps")) {
				wdSettings.setShowIsnGaps(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}

			else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowOsnGaps")) {
				wdSettings.setShowOsnGaps(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}
		}
		return wdSettings;
	}

	@Override
	public WDNotificationSettings getNotificationSettings(String loggedInUser, Long userId) {
		Long aid = 0L;
		List<ApplicationSetting> applicationSettings = commonDAO.getApplicationSettings(Constants.APPLICATION_WATCHDOG_NOTIFICATION_SETTINGS, userId, aid);

		if (applicationSettings == null || applicationSettings.isEmpty()) {
			return null;
		}

		WDNotificationSettings wdNotificationSettings = new WDNotificationSettings();

		for (ApplicationSetting applicationSetting : applicationSettings) {
			if (applicationSetting.getFieldName().equalsIgnoreCase("ShowMessageNotification")) {
				wdNotificationSettings.setShowMessageNotifications(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			} else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowEventNotifications")) {
				wdNotificationSettings.setShowEventNotifications(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			} else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowPossibleDuplicates")) {
				wdNotificationSettings.setShowPossibleDuplicates(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			} else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowCalculatedDuplicates")) {
				wdNotificationSettings.setShowCalculatedDuplicates(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			} else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowNakedMesages")) {
				wdNotificationSettings.setShowNakedMesages(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			} else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowIsnGaps")) {
				wdNotificationSettings.setShowIsnGaps(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			} else if (applicationSetting.getFieldName().equalsIgnoreCase("ShowOsnGaps")) {
				wdNotificationSettings.setShowOsnGaps(Boolean.parseBoolean(applicationSetting.getFieldValue()));
			}
		}
		return wdNotificationSettings;
	}

	@Override
	public void updateWDSettings(String loggedInUser, Long userId, WDNotificationSettings wdSettings) {
		Long aid = 0L;
		ApplicationSetting applicationSetting = new ApplicationSetting();
		applicationSetting.setAllianceID(aid);
		applicationSetting.setId(Constants.APPLICATION_WATCHDOG_GENERAL_SETTINGS);
		applicationSetting.setUserID(userId);

		// message notification setting

		// Message Notification check box
		applicationSetting.setFieldName("ShowMessageNotification");
		applicationSetting.setFieldValue((wdSettings.isShowMessageNotifications()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// Possible Duplicates check box
		applicationSetting.setFieldName("ShowPossibleDuplicates");
		applicationSetting.setFieldValue((wdSettings.isShowPossibleDuplicates()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// Nacked Messages check box
		applicationSetting.setFieldName("ShowNakedMesages");
		applicationSetting.setFieldValue((wdSettings.isShowNakedMesages()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// Hide Annotation check box
		applicationSetting.setFieldName("HideAnnotatedNotifications");
		applicationSetting.setFieldValue((wdSettings.isHideAnnotatedNotifications()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// Notification History
		if (wdSettings.getMsgNotificationHistory() != null) {
			applicationSetting.setFieldName("NotificationHistory");
			applicationSetting.setFieldValue(wdSettings.getMsgNotificationHistory().toString());
			this.commonDAO.updateApplicationSetting(applicationSetting);
		}

		// Notification per page
		if (wdSettings.getMsgNotificationCount() != null) {
			applicationSetting.setFieldName("NotificationCount");
			applicationSetting.setFieldValue(wdSettings.getMsgNotificationCount().toString());
			this.commonDAO.updateApplicationSetting(applicationSetting);
		}

		// message gap notification setting

		// ISN GAPS check box
		applicationSetting.setFieldName("ShowIsnGaps");
		applicationSetting.setFieldValue((wdSettings.isShowIsnGaps()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// OSN GAPS check box
		applicationSetting.setFieldName("ShowOsnGaps");
		applicationSetting.setFieldValue((wdSettings.isShowOsnGaps()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// Hide annotated Notifications check box
		applicationSetting.setFieldName("HideAnnotatedGapsNotifications");
		applicationSetting.setFieldValue((wdSettings.isHideGapsAnnotatedNotifications()) ? "True" : "False");
		this.commonDAO.updateApplicationSetting(applicationSetting);

		// Notification History
		if (wdSettings.getMsgGapsNotificationHistory() != null) {
			applicationSetting.setFieldName("MsgGapsNotificationHistory");
			applicationSetting.setFieldValue(wdSettings.getMsgGapsNotificationHistory().toString());
			this.commonDAO.updateApplicationSetting(applicationSetting);
		}

		// Notification per Page
		if (wdSettings.getMsgGapsNotificationCount() != null) {
			applicationSetting.setFieldName("MsgGapsNotificationCount");
			applicationSetting.setFieldValue(wdSettings.getMsgGapsNotificationCount().toString());
			this.commonDAO.updateApplicationSetting(applicationSetting);
		}

		// event notification setting

		// notification history
		if (wdSettings.getEventNotificationHistory() != null) {
			applicationSetting.setFieldName("EventNotificationHistory");
			applicationSetting.setFieldValue(wdSettings.getEventNotificationHistory().toString());
			this.commonDAO.updateApplicationSetting(applicationSetting);
		}

		// notification per page
		if (wdSettings.getEventNotificationCount() != null) {
			applicationSetting.setFieldName("EventNotificationCount");
			applicationSetting.setFieldValue(wdSettings.getEventNotificationCount().toString());
			this.commonDAO.updateApplicationSetting(applicationSetting);
		}

	}

	@Override
	public MessageNotification getNotification(String loggedInUser, Long sysId, Integer notificationType, boolean sideAdminUser, Long loggedInUserGroupId) {

		MessageNotification messageNotification = null;

		switch (notificationType) {

		case Constants.WATCHDOGE_FILTER_MESSAGE_NOTIFICATIONS:

			messageNotification = this.getMessageNotification(loggedInUser, sysId, sideAdminUser, loggedInUserGroupId);
			return messageNotification;
		case Constants.WATCHDOGE_FILTER_CALCULATE_DUPLICATES:

			messageNotification = this.getCalculatedDupplicateNotification(loggedInUser, sysId, loggedInUserGroupId);
			return messageNotification;
		case Constants.WATCHDOGE_FILTER_POSSIBLE_DUPLICATES:

			messageNotification = this.getPossibleDupplicateNotification(loggedInUser, sysId, loggedInUserGroupId);
			return messageNotification;
		case Constants.WATCHDOGE_FILTER_ISN_GAPS:

			messageNotification = this.getIsnGapNotification(loggedInUser, sysId, loggedInUserGroupId);
			return messageNotification;
		case Constants.WATCHDOGE_FILTER_OSN_GAPS:
			messageNotification = this.getOsnGapNotification(loggedInUser, sysId, loggedInUserGroupId);
			return messageNotification;
		case Constants.WATCHDOGE_FILTER_NAKED_MESSAGES:

			messageNotification = this.getNakedNotification(loggedInUser, sysId, loggedInUserGroupId);
			return messageNotification;
		default:
			messageNotification = null;
		}

		return messageNotification;
	}

	@Override
	public MessageNotification getMessageNotification(String loggedInUser, Long sysId, boolean sideAdminUser, Long loggedInUserGroupId) {

		MessageNotification messageNotification = watchDogDAO.getMessageNotification(sysId, sideAdminUser, loggedInUserGroupId);
		if (messageNotification == null) {
			return null;
		}
		List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(messageNotification.getId(), messageNotification.getNotificationType());
		messageNotification.setAnnotaionsList(notificationAnnotaions);
		return messageNotification;
	}

	@Override
	public MessageNotification getCalculatedDupplicateNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId) {

		MessageNotification calculatedDupplicateNotification = watchDogDAO.getCalculatedDupplicateNotification(sysId, loggedInUserGroupId);
		if (calculatedDupplicateNotification == null) {
			return null;
		}
		List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(calculatedDupplicateNotification.getId(), calculatedDupplicateNotification.getNotificationType());
		calculatedDupplicateNotification.setAnnotaionsList(notificationAnnotaions);
		return calculatedDupplicateNotification;
	}

	@Override
	public MessageNotification getPossibleDupplicateNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId) {

		MessageNotification possibleDupplicateNotification = watchDogDAO.getPossibleDupplicateNotification(sysId, loggedInUserGroupId);
		if (possibleDupplicateNotification == null) {
			return null;
		}
		List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(possibleDupplicateNotification.getId(), possibleDupplicateNotification.getNotificationType());
		possibleDupplicateNotification.setAnnotaionsList(notificationAnnotaions);
		return possibleDupplicateNotification;
	}

	@Override
	public MessageNotification getIsnGapNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId) {

		MessageNotification isnGapNotification = watchDogDAO.getIsnGapNotification(sysId, loggedInUserGroupId);
		if (isnGapNotification == null) {
			return null;
		}
		List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(isnGapNotification.getId(), isnGapNotification.getNotificationType());
		isnGapNotification.setAnnotaionsList(notificationAnnotaions);
		return isnGapNotification;
	}

	@Override
	public MessageNotification getOsnGapNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId) {

		MessageNotification osnGapNotification = watchDogDAO.getOsnGapNotification(sysId, loggedInUserGroupId);
		if (osnGapNotification == null) {
			return null;
		}
		List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(osnGapNotification.getId(), osnGapNotification.getNotificationType());
		osnGapNotification.setAnnotaionsList(notificationAnnotaions);
		return osnGapNotification;
	}

	@Override
	public MessageNotification getNakedNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId) {
		MessageNotification nakedNotification = watchDogDAO.getNakedNotification(sysId, loggedInUserGroupId);
		if (nakedNotification == null) {
			return null;
		}
		List<Annotaion> notificationAnnotaions = watchDogDAO.getNotificationAnnotaions(nakedNotification.getId(), nakedNotification.getNotificationType());
		nakedNotification.setAnnotaionsList(notificationAnnotaions);
		return nakedNotification;
	}

	public void addNotificationAnnotaion(String loggedInUser, Annotaion annotaion) {
		watchDogDAO.addNotificationAnnotaion(loggedInUser, annotaion);
	}

	@Override
	public Long getMessageNotificationsCount(String loggedInUser, Long wdNbDayHistory, boolean sideAdminUser, Long loggedInUserGroupId) {

		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		return watchDogDAO.getMessageNotificationsCount(calculateDateTime, sideAdminUser, loggedInUserGroupId);
	}

	@Override
	public Long getCalculatedDupplicateNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserGroupId) {
		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		return watchDogDAO.getCalculatedDupplicateNotificationsCount(calculateDateTime, loggedInUserGroupId);
	}

	@Override
	public Long getPossibleDupplicateNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserGroupId) {
		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		return watchDogDAO.getPossibleDupplicateNotificationsCount(calculateDateTime, loggedInUserGroupId);
	}

	@Override
	public Long getIsnGapNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserGroupId) {
		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		return watchDogDAO.getIsnGapNotificationsCount(calculateDateTime, loggedInUserGroupId);
	}

	@Override
	public Long getOsnGapNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserGroupId) {
		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		return watchDogDAO.getOsnGapNotificationsCount(calculateDateTime, loggedInUserGroupId);
	}

	@Override
	public Long getNakedNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserGroupId) {
		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		return watchDogDAO.getNakedNotificationsCount(calculateDateTime, loggedInUserGroupId);
	}

	// Bug 30277:WatchDog - event notification created showing incorrect numbers of Rows for searches (Defect #157)
	@Override
	public Long getEventNotificationsCount(String loggedInUser, boolean sideAdminUser, Long wdNbDayHistory) {
		// calculate date based on notification history
		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		// get count of notification based on notification history
		return watchDogDAO.getEventNotificationsCount(calculateDateTime, loggedInUser, sideAdminUser);
	}

	// SETTER & GETTER

	public WatchDogDAO getWatchDogDAO() {
		return watchDogDAO;
	}

	public void setWatchDogDAO(WatchDogDAO watchDogDAO) {
		this.watchDogDAO = watchDogDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	@Override
	public List<MessageNotification> getGapNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, boolean isnGap, boolean osnGap, Long from, Long to, Long loggedInUserGroupId) {

		Date calculateDateTime = calculateDateTime(wdNbDayHistory);
		List<MessageNotification> gapNotifications = watchDogDAO.getGapNotifications(calculateDateTime, from, to, loggedInUserGroupId, isnGap, osnGap);
		getMessageAnnotations(gapNotifications, hideAnnotatedMessages);
		return gapNotifications;
	}

	@Override
	public List<MessageRequest> serachMessageRequests(String loggedInUser, boolean sideAdminUser, Long loggedInUserGroupId, String email, String accountValue, String description) {
		// TODO Auto-generated method stub
		return watchDogDAO.serachMessageRequests(loggedInUser, sideAdminUser, loggedInUserGroupId, email, accountValue, description);
	}

	@Override
	public Long updateMessageRequest(String loggedInUser, MessageRequest param, MessageRequest oldMessageRequest) {
		// TODO Auto-generated method stub
		return watchDogDAO.updateMessageRequest(param, loggedInUser);
	}
}
