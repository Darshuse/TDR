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

package com.eastnets.domain.monitoring;

import java.io.Serializable;

/**
 * Statistics POJO
 * @author EastNets
 * @since August 30, 2012
 */
public class Statistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3083394565745001567L;
	private long totalCount;
	private long liveCount;
	private String liveCountPercentage;
	private long completeCount;
	private String completeCountPercentage;
	private long archiveCount;
	private String archiveCountPercentage;
	private long incompleteCount;
	private String incompleteCountPercentage;
	private long journalCount;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getLiveCount() {
		return liveCount;
	}

	public void setLiveCount(long liveCount) {
		this.liveCount = liveCount;
	}

	public String getLiveCountPercentage() {
		return liveCountPercentage;
	}

	public void setLiveCountPercentage(String liveCountPercentage) {
		this.liveCountPercentage = liveCountPercentage;
	}

	public long getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(long completeCount) {
		this.completeCount = completeCount;
	}

	public String getCompleteCountPercentage() {
		return completeCountPercentage;
	}

	public void setCompleteCountPercentage(String completeCountPercentage) {
		this.completeCountPercentage = completeCountPercentage;
	}

	public long getArchiveCount() {
		return archiveCount;
	}

	public void setArchiveCount(long archiveCount) {
		this.archiveCount = archiveCount;
	}

	public String getArchiveCountPercentage() {
		return archiveCountPercentage;
	}

	public void setArchiveCountPercentage(String archiveCountPercentage) {
		this.archiveCountPercentage = archiveCountPercentage;
	}

	public long getIncompleteCount() {
		return incompleteCount;
	}

	public void setIncompleteCount(long incompleteCount) {
		this.incompleteCount = incompleteCount;
	}

	public String getIncompleteCountPercentage() {
		return incompleteCountPercentage;
	}

	public void setIncompleteCountPercentage(String incompleteCountPercentage) {
		this.incompleteCountPercentage = incompleteCountPercentage;
	}

	public long getJournalCount() {
		return journalCount;
	}

	public void setJournalCount(long journalCount) {
		this.journalCount = journalCount;
	}

}
