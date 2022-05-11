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

package com.eastnets.domain.archive;

import java.util.List;

/**
 * MessageArchiveOptions POJO
 * @author EastNets
 * @since September 20, 2012
 */
public class MessageArchiveOptions  extends ArchiveOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = -363755999118392068L;

	private boolean isDateRange;
	
	private Long swiftVersion;
		
	private List<MessageArchiveSettings> messageArchiveSettings;

	public boolean isDateRange() {
		return isDateRange;
	}

	public void setDateRange(boolean isDateRange) {
		this.isDateRange = isDateRange;
	}

	public Long getSwiftVersion() {
		return swiftVersion;
	}

	public void setSwiftVersion(Long swiftVersion) {
		this.swiftVersion = swiftVersion;
	}

	public List<MessageArchiveSettings> getMessageArchiveSettings() {
		return messageArchiveSettings;
	}

	public void setMessageArchiveSettings(
			List<MessageArchiveSettings> messageArchiveSettings) {
		this.messageArchiveSettings = messageArchiveSettings;
	}
	
	
	
	
}
