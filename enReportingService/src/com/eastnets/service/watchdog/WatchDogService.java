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

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.domain.watchdog.Annotaion;
import com.eastnets.domain.watchdog.EventNotification;
import com.eastnets.domain.watchdog.EventRequest;
import com.eastnets.domain.watchdog.MessageNotification;
import com.eastnets.domain.watchdog.MessageRequest;
import com.eastnets.domain.watchdog.WDNotificationSettings;
import com.eastnets.service.Service;

/**
 * WatchDog Service Interface 
 * @author EastNets
 * @since July 12, 2012
 */

/**
 * @author aellabadi
 *
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public interface WatchDogService extends Service {

	/**
	 * Insert new watchdog message request
	 * 
	 * @param loggedInUser
	 * @param param
	 * @return Integer
	 */
	public Long addMessageRequest(String loggedInUser, MessageRequest param);

	/**
	 * Insert new watchdog event request
	 * 
	 * @param loggedInUser
	 * @param param
	 * @return Integer
	 */
	public Long addEventRequest(String loggedInUser, EventRequest param);

	/**
	 * Delete watchdog message request
	 * 
	 * @param loggedInUser
	 * @param messageRequests
	 */
	public void deleteMessageRequest(String loggedInUser, List<MessageRequest> messageRequests);

	/**
	 * Delete watchdog event request
	 * 
	 * @param loggedInUser
	 * @param eventRequests
	 */
	public void deleteEventRequest(String loggedInUser, List<EventRequest> eventRequests);

	/**
	 * Delete watchdog event notification
	 * 
	 * @param loggedInUser
	 * @param eventNotifications
	 */
	public void deleteEventNotification(String loggedInUser, List<EventNotification> eventNotifications);

	/**
	 * Delete watchdog message notification
	 * 
	 * @param loggedInUser
	 * @param messageNotifications
	 */
	public void deleteMessageNotification(String loggedInUser, List<MessageNotification> messageNotifications);

	/**
	 * List messages notifications
	 * 
	 * @param loggedInUser
	 * @param wdNbDayHistory
	 * @param sideAdminUser
	 * @return List<MessageNotification>
	 */
	public List<MessageNotification> getMessageNotifications(String loggedInUser, Long wdNbDayHistory, boolean sideAdminUser, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @return
	 */
	public List<MessageNotification> getCalculatedDupplicateNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @return
	 */
	public List<MessageNotification> getPossibleDupplicateNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId);

	public List<MessageNotification> getGapNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, boolean isnGap, boolean osnGap, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @return
	 */
	public List<MessageNotification> getNakedNotifications(String loggedInUser, Long wdNbDayHistory, boolean hideAnnotatedMessages, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @param notificationType
	 * @param sideAdminUser
	 * @return
	 */
	public MessageNotification getNotification(String loggedInUser, Long sysId, Integer notificationType, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @param sideAdminUser
	 * @return
	 */
	public MessageNotification getMessageNotification(String loggedInUser, Long sysId, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @return
	 */
	public MessageNotification getCalculatedDupplicateNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @return
	 */
	public MessageNotification getPossibleDupplicateNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @return
	 */
	public MessageNotification getIsnGapNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @return
	 */
	public MessageNotification getOsnGapNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param sysId
	 * @return
	 */
	public MessageNotification getNakedNotification(String loggedInUser, Long sysId, Long loggedInUserGroupId);

	/**
	 * List messages request
	 * 
	 * @param loggedInUser
	 * @param sideAdminUser
	 * @return List<MessageRequest>
	 */
	public List<MessageRequest> getMessageRequests(String loggedInUser, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * @param loggedInUser
	 * @param id
	 * @param sideAdminUser
	 * @return
	 */
	public MessageRequest getMessageRequest(String loggedInUser, Long id, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * List event notifications
	 * 
	 * @param loggedInUser
	 * @param iNbDayHistory
	 * @param sideAdminUser
	 * @return List<EventNotification>
	 */
	public List<EventNotification> getEventNotifications(String loggedInUser, Long iNbDayHistory, boolean sideAdminUser, Long from, Long to);

	/**
	 * List event requests
	 * 
	 * @param loggedInUser
	 * @param sideAdminUser
	 * @return List<EventRequest>
	 */
	public List<EventRequest> getEventRequests(String loggedInUser, boolean sideAdminUser);

	/**
	 * Add new watch dog General Settings
	 * 
	 * @param loggedInUser
	 * @param generalSettings
	 */
	public void addWDSettings(String loggedInUser, Long userId, WDNotificationSettings wdGeneralSettings);

	/**
	 * Add new watch dog General Settings
	 * 
	 * @param loggedInUser
	 * @param generalSettings
	 */
	public void addDefaultWDSettings(String loggedInUser, Long userId, WDNotificationSettings wdNewGeneralSettings);

	/**
	 * 
	 * @param loggedInUser
	 * @param userId
	 * @param wdNewGeneralSettings
	 */
	public void deleteWDSettings(String loggedInUser, Long userId);

	/**
	 * Get watch dog General Settings
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public WDNotificationSettings getWDSettings(String loggedInUser, Long userId);

	/**
	 * Get watch dog Notification Settings
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public WDNotificationSettings getNotificationSettings(String loggedInUser, Long userId);

	/**
	 * update Watch dog General Settings
	 * 
	 * @param loggedInUser
	 * @param wdGeneralSettings
	 */
	public void updateWDSettings(String loggedInUser, Long userId, WDNotificationSettings wdSettings);

	/**
	 * @param loggedInUser
	 * @param annotaion
	 */
	public void addNotificationAnnotaion(String loggedInUser, Annotaion annotaion);

	/**
	 * 
	 * @param loggedInUser
	 * @param wdNbDayHistory
	 * @param sideAdminUser
	 * @return
	 */
	public Long getMessageNotificationsCount(String loggedInUser, Long wdNbDayHistory, boolean sideAdminUser, Long loggedInUserId);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public Long getCalculatedDupplicateNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserId);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public Long getPossibleDupplicateNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserId);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public Long getIsnGapNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserId);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public Long getOsnGapNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserId);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public Long getNakedNotificationsCount(String loggedInUser, Long wdNbDayHistory, Long loggedInUserId);

	/**
	 * 
	 * @param loggedInUser
	 * @param sideAdminUser
	 * @param wdNbDayHistory
	 * @return
	 */
	// Bug 30277:WatchDog - event notification created showing incorrect numbers of Rows for searches (Defect #157)
	public Long getEventNotificationsCount(String loggedInUser, boolean sideAdminUser, Long eventNotifHistory);

	public Long updateMessageRequest(String loggedInUser, MessageRequest param, MessageRequest oldMessageRequest);

	/**
	 * List messages request
	 * 
	 * @param loggedInUser
	 * @param sideAdminUser
	 * @return List<MessageRequest>
	 */
	public List<MessageRequest> serachMessageRequests(String loggedInUser, boolean sideAdminUser, Long loggedInUserGroupId, String email, String accountValue, String description);

}
