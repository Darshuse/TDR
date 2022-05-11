package com.eastnets.domain.watchdog;

import java.io.Serializable;


public class WDGeneralSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3419013292518832536L;
	
	private Long msgNotificationHistory;
	private Long msgGapsNotificationHistory;
	private Long eventNotificationHistory;
	private Long msgNotificationCount;
	private Long msgGapsNotificationCount;
	private Long eventNotificationCount;
	
	private Boolean hideAnnotatedNotifications;
	private Boolean hideGapsAnnotatedNotifications;
	
	// GETTER & SETTER
	public Long getMsgNotificationHistory() {
		return msgNotificationHistory;
	}
	
	public void setMsgNotificationHistory(Long msgNotificationHistory) {
		this.msgNotificationHistory = msgNotificationHistory;
	}
	
	public Long getMsgGapsNotificationHistory() {
		return msgGapsNotificationHistory;
	}
	
	public void setMsgGapsNotificationHistory(Long msgGapsNotificationHistory) {
		this.msgGapsNotificationHistory = msgGapsNotificationHistory;
	}
	
	public Long getEventNotificationHistory() {
		return eventNotificationHistory;
	}
	
	public void setEventNotificationHistory(Long eventNotificationHistory) {
		this.eventNotificationHistory = eventNotificationHistory;
	}
	
	public Long getMsgNotificationCount() {
		return msgNotificationCount;
	}
	
	public void setMsgNotificationCount(Long msgNotificationCount) {
		this.msgNotificationCount = msgNotificationCount;
	}
	
	public Long getMsgGapsNotificationCount() {
		return msgGapsNotificationCount;
	}
	
	public void setMsgGapsNotificationCount(Long msgGapsNotificationCount) {
		this.msgGapsNotificationCount = msgGapsNotificationCount;
	}
	
	public Long getEventNotificationCount() {
		return eventNotificationCount;
	}
	
	public void setEventNotificationCount(Long eventNotificationCount) {
		this.eventNotificationCount = eventNotificationCount;
	}
	
	public Boolean isHideAnnotatedNotifications() {
		return hideAnnotatedNotifications;
	}
	
	public void setHideAnnotatedNotifications(Boolean hideAnnotatedNotifications) {
		this.hideAnnotatedNotifications = hideAnnotatedNotifications;
	}
	
	public Boolean isHideGapsAnnotatedNotifications() {
		return hideGapsAnnotatedNotifications;
	}
	
	public void setHideGapsAnnotatedNotifications(
			Boolean hideGapsAnnotatedNotifications) {
		this.hideGapsAnnotatedNotifications = hideGapsAnnotatedNotifications;
	}
	
}
