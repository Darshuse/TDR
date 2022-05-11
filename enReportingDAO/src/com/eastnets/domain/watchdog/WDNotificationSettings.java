package com.eastnets.domain.watchdog;

import java.io.Serializable;

public class WDNotificationSettings extends WDGeneralSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044111764430598706L;
	
	private Boolean showMessageNotifications;
	private Boolean showEventNotifications;
	private Boolean showPossibleDuplicates;
	private Boolean showCalculatedDuplicates;
	private Boolean showNakedMesages;
	private Boolean showIsnGaps;
	private Boolean showOsnGaps;
	
	public Boolean isShowMessageNotifications() {
		return showMessageNotifications;
	}
	
	public void setShowMessageNotifications(Boolean showMessageNotifications) {
		this.showMessageNotifications = showMessageNotifications;
	}
	
	public Boolean isShowEventNotifications() {
		return showEventNotifications;
	}
	
	public void setShowEventNotifications(Boolean showEventNotifications) {
		this.showEventNotifications = showEventNotifications;
	}
	
	public Boolean isShowPossibleDuplicates() {
		return showPossibleDuplicates;
	}
	
	public void setShowPossibleDuplicates(Boolean showPossibleDuplicates) {
		this.showPossibleDuplicates = showPossibleDuplicates;
	}
	
	public Boolean isShowCalculatedDuplicates() {
		return showCalculatedDuplicates;
	}
	
	public void setShowCalculatedDuplicates(Boolean showCalculatedDuplicates) {
		this.showCalculatedDuplicates = showCalculatedDuplicates;
	}
	
	public Boolean isShowNakedMesages() {
		return showNakedMesages;
	}
	
	public void setShowNakedMesages(Boolean showNakedMesages) {
		this.showNakedMesages = showNakedMesages;
	}
	
	public Boolean isShowIsnGaps() {
		return showIsnGaps;
	}
	
	public void setShowIsnGaps(Boolean showIsnGaps) {
		this.showIsnGaps = showIsnGaps;
	}
	
	public Boolean isShowOsnGaps() {
		return showOsnGaps;
	}
	
	public void setShowOsnGaps(Boolean showOsnGaps) {
		this.showOsnGaps = showOsnGaps;
	}
	
}
