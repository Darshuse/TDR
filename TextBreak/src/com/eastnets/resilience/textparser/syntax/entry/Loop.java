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
package com.eastnets.resilience.textparser.syntax.entry;

import java.util.LinkedList;
import java.util.List;

import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedLoop;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredLoopNotFound;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;

/**
 * Loop entry
 * 
 * @author EHakawati
 * 
 */
public class Loop extends Entry {

	String id;
	int minOcc;
	int maxOcc;
	List<Entry> entries;

	/**
	 * Default constructor
	 */
	public Loop() {
		entries = new LinkedList<Entry>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id.trim();
	}

	public int getMinOcc() {
		return minOcc;
	}

	public void setMinOcc(int minOcc) {
		this.minOcc = minOcc;
	}

	public int getMaxOcc() {
		return maxOcc;
	}

	public void setMaxOcc(int maxOcc) {
		this.maxOcc = maxOcc;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries.addAll(entries);
	}

	/**
	 * Add sun entry to the loop
	 * 
	 * @param entry
	 */
	public void addEntry(Entry entry) {
		getEntries().add(entry);
	}

	@Override
	public boolean isOptional() {
		return getMinOcc() == 0;
	}

	@Override
	public void throwException() throws RequiredNotFound {
		throw new RequiredLoopNotFound(this);
	}

	@Override
	public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound {

		int loop = 0;

		int startNextIndex = parsedMessage.getNextIndex();

		for (; loop < getMaxOcc(); loop++) {

			// Start index (roll back use)
			int nextIndex = parsedMessage.getNextIndex();
			int groupIdx = parsedMessage.getNextGroupIdx();

			boolean isPassed = true;
			int totalCount = 0;

			// check loop entries
			for (Entry entry : getEntries()) {

				try {
					if (!entry.isValid(parsedMessage)) {
						if (!entry.isOptional()) {
							isPassed = false;
							break;
						}
					} else {
						totalCount++;
					}
				} catch (RequiredNotFound ex) {
					// if required field break (it may pass if a previous loop
					// match)
					if (!entry.isOptional()) {
						isPassed = false;
						break;
					}
				}
			}

			// current index
			int toIndex = parsedMessage.getNextIndex();

			// RollBack
			parsedMessage.pointTo(nextIndex);

			// Not passed then break
			if (!isPassed || totalCount == 0) {
				break;
			} else if (isPassed) {

				ParsedLoop parsedLoop = getParsedLoop(groupIdx, loop);
				parsedMessage.addParsedLoop(parsedLoop);

				// attach loop id
				while (parsedMessage.getNextIndex() != toIndex) {

					ParsedField parsedField = parsedMessage.getNextTextEntry();

					// Check sub loops
					if (parsedField.getLoopId() == null || parsedField.getLoopId().indexOf(getId()) == -1) {
						parsedField.setLoopId(getId());

						if (parsedField.getGroupIdx() > 0) {
							parsedMessage.getParsedLoops().remove(parsedField.getGroupIdx());
						}

						parsedField.setGroupIdx(groupIdx);
					}
				}
			}
		}

		// does not match the minimum loop
		if (loop > 0 && loop < getMinOcc()) {
			// RollBack
			parsedMessage.pointTo(startNextIndex);
			loop = 0;
		}

		// if no loop matched and this entry is required
		if (loop == 0 && !isOptional()) {
			if (!parsedMessage.isExcelExport())
				this.throwException();
		}

		return loop > 0;
	}

	private ParsedLoop getParsedLoop(int groupIdx, int loopIndex) {

		// String G3.G2.G1 -> 123
		// String G7.G1.SE -> 17

		String groupId = "";
		String[] looping = this.getId().split("\\.");

		for (int index = looping.length - 1; index >= 0; index--) {
			groupId = groupId + looping[index].replaceAll("[^\\d]", "");
		}
		// not working with G10 +
		// String groupId = new StringBuffer(this.getId().replaceAll("[^\\d]",
		// "")).reverse().toString();

		ParsedLoop parsedLoop = new ParsedLoop();

		parsedLoop.setGroupIdx(groupIdx);
		parsedLoop.setGroupId(groupId);
		parsedLoop.setGroupCnt(loopIndex + 1);

		return parsedLoop;

	}

}
