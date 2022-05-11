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

package com.eastnets.dao.watchdog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eastnets.dao.DAO;
import com.eastnets.domain.watchdog.Annotaion;
import com.eastnets.domain.watchdog.EventNotification;
import com.eastnets.domain.watchdog.EventRequest;
import com.eastnets.domain.watchdog.LastIDs;
import com.eastnets.domain.watchdog.MessageNotification;
import com.eastnets.domain.watchdog.MessageRequest;

/**
 * WatchDog DAO Interface
 * 
 * @author EastNets
 * @since July 11, 2012
 */
public interface WatchDogDAO extends DAO {

	/**
	 * Insert message request
	 * 
	 * @param param
	 * @return
	 */
	public Long addMessageRequest(MessageRequest param, String loggedInUser);

	/**
	 * Insert event request
	 * 
	 * @param param
	 * @return
	 */
	public Long addEventRequest(EventRequest param, String loggedInUser);

	/**
	 * Delete message request
	 * 
	 * @param id
	 */
	public void deleteMessageRequest(Long id);

	/**
	 * Delete event request
	 * 
	 * @param id
	 */
	public void deleteEventRequest(Long id);

	/**
	 * Delete event notification
	 * 
	 * @param id
	 */
	public void deleteEventNotification(Long id);

	/**
	 * Delete message notification
	 * 
	 * @param id
	 */
	public void deleteMessageNotification(int notificationType, Long id);

	/**
	 * List message notifications
	 * 
	 * @param fromDate
	 * @param sideAdminUser
	 * @return List<MessageNotification>
	 */
	public List<MessageNotification> getMessageNotifications(Date fromDate, boolean sideAdminUser, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @param sideAdminUser
	 * @return
	 */
	public Long getMessageNotificationsCount(Date fromDate, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * @param fromDate
	 * @return
	 */
	public List<MessageNotification> getCalculatedDupplicateNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @return
	 */
	public Long getCalculatedDupplicateNotificationsCount(Date fromDate, Long loggedInUserGroupId);

	/**
	 * @param fromDate
	 * @return
	 */
	public List<MessageNotification> getPossibleDupplicateNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @return
	 */
	public Long getPossibleDupplicateNotificationsCount(Date fromDate, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @param from
	 * @param to
	 * @return
	 */
	public List<MessageNotification> getGapNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId, boolean isnGap, boolean osnGap);

	/**
	 * 
	 * @param fromDate
	 * @return
	 */
	public Long getIsnGapNotificationsCount(Date fromDate, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @return
	 */
	public Long getOsnGapNotificationsCount(Date fromDate, Long loggedInUserGroupId);

	/**
	 * @param fromDate
	 * @return
	 */
	public List<MessageNotification> getNakedNotifications(Date fromDate, Long from, Long to, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @return
	 */
	public Long getNakedNotificationsCount(Date fromDate, Long loggedInUserGroupId);

	/**
	 * @param sysId
	 * @return
	 */
	public MessageNotification getMessageNotification(Long sysId, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * @param sysId
	 * @return
	 */
	public MessageNotification getCalculatedDupplicateNotification(Long sysId, Long loggedInUserGroupId);

	/**
	 * @param sysId
	 * @return
	 */
	public MessageNotification getPossibleDupplicateNotification(Long sysId, Long loggedInUserGroupId);

	/**
	 * @param sysId
	 * @return
	 */
	public MessageNotification getIsnGapNotification(Long sysId, Long loggedInUserGroupId);

	/**
	 * @param sysId
	 * @return
	 */
	public MessageNotification getOsnGapNotification(Long sysId, Long loggedInUserGroupId);

	/**
	 * @param sysId
	 * @return
	 */
	public MessageNotification getNakedNotification(Long sysId, Long loggedInUserGroupId);

	/**
	 * List message requests
	 * 
	 * @param sideAdminUser
	 * @return List<MessageRequest>
	 */
	public List<MessageRequest> getMessageRequests(boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * get message request by Id
	 * 
	 * @param id
	 * @param sideAdminUser
	 * @return MessageRequest
	 */
	public MessageRequest getMessageRequest(Long id, boolean sideAdminUser, Long loggedInUserGroupId);

	/**
	 * 
	 * @param fromDate
	 * @param loggedInUser
	 * @param sideAdminUser
	 * @param from
	 * @param to
	 * @return
	 */
	public List<EventNotification> getEventNotifications(Date fromDate, String loggedInUser, boolean sideAdminUser, Long from, Long to);

	/**
	 * 
	 * @param sideAdminUser
	 * @return Long
	 */
	public Long getEventNotificationsCount(Date fromDate, String loggedInUsername, boolean sideAdminUser);

	/**
	 * List event requests
	 * 
	 * @param sideAdminUser
	 * @return List<EventRequest>
	 */
	public List<EventRequest> getEventRequests(boolean sideAdminUser, String loggedInUser);

	/**
	 * Get last Id, it is needed when retrieving event or message notification as a starting point
	 * 
	 * @param iNbDayHistory
	 * @return LastIDs
	 */
	public LastIDs getLastIDs(Long iNbDayHistory);

	/**
	 * @param annotaion
	 */
	public void addNotificationAnnotaion(String loggedInUser, Annotaion annotaion);

	/**
	 * @param sysId
	 * @return
	 */
	public List<Annotaion> getNotificationAnnotaions(Long sysId, int requstType);

	public BigDecimal formatAmount(BigDecimal amount, String currency);

	Map<String, Integer> getCurrenciesISO();

	public Long updateMessageRequest(MessageRequest messageType, String loggedInUser);

	public List<MessageRequest> serachMessageRequests(String loggedInUser, boolean sideAdminUser, Long loggedInUserGroupId, String email, String accountValue, String description);

}
