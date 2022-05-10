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
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.exception.RequiredSequenceNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;

/**
 * Sequence entry
 * 
 * @author EHakawati
 * 
 */
public class Sequence extends Entry {

	String id;
	boolean isOptional;
	String expansion;
	List<Entry> entries;

	/**
	 * constructor
	 */
	public Sequence() {
		entries = new LinkedList<Entry>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id.trim();
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public String getExpansion() {
		return expansion;
	}

	public void setExpansion(String expansion) {
		this.expansion = expansion;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	/**
	 * Add sun entry to the sequence
	 * 
	 * @param entry
	 */
	public void addEntry(Entry entry) {
		getEntries().add(entry);
	}

	@Override
	public void throwException() throws RequiredNotFound {
		throw new RequiredSequenceNotFound(this);
	}

	@Override
	public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound {

		// Start index (roll back use)
		int nextIndex = parsedMessage.getNextIndex();

		boolean isPassed = true;

		// check sub entries
		for (Entry entry : getEntries()) {

			try {
				if (!entry.isValid(parsedMessage) && !entry.isOptional()) {
					isPassed = false;
					break;
				}
			} catch (RequiredNotFound ex) {
				// if required field break
				if (!entry.isOptional()) {
					isPassed = false;
					break;
				}
			}
		}

		int toIndex = parsedMessage.getNextIndex();

		// RollBack
		parsedMessage.pointTo(nextIndex);

		if (isPassed) {

			// attach sequence letter id
			while (parsedMessage.getNextIndex() != toIndex) {
				ParsedField parsedField = parsedMessage.getNextTextEntry();
				parsedField.setSequence(this.getId().substring(1));

				if (parsedField.getGroupIdx() > 0) {
					parsedMessage.getParsedLoops().get(parsedField.getGroupIdx()).setSequence(parsedField.getSequence());
				}
			}

		} else if (!isPassed && !isOptional()) {
			// Not passed
			if (!parsedMessage.isExcelExport())
				this.throwException();
		}

		return isPassed;
	}
}
