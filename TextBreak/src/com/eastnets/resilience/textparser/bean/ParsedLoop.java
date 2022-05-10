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

package com.eastnets.resilience.textparser.bean;

/**
 * Parsed Loop Entry
 * 
 * @author EHakawati
 */
public class ParsedLoop implements ParsedEntry {

	private String sequence;
	private String groupId;
	private int groupCnt;
	private int groupIdx;
	private int parentGroupIdx;

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getGroupCnt() {
		return groupCnt;
	}

	public void setGroupCnt(int groupCnt) {
		this.groupCnt = groupCnt;
	}

	public int getGroupIdx() {
		return groupIdx;
	}

	public void setGroupIdx(int groupIdx) {
		this.groupIdx = groupIdx;
	}

	public int getParentGroupIdx() {
		return parentGroupIdx;
	}

	public void setParentGroupIdx(int parentGroupIdx) {
		this.parentGroupIdx = parentGroupIdx;
	}
}
