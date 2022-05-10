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

import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredAlternativeNotFound;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;

/**
 * Alternative entry
 * 
 * @author EHakawati
 * 
 */
public class Alternative extends Entry {

	String id;
	boolean isOptional;
	List<AlternativeEntry> alternates;

	public Alternative() {
		alternates = new LinkedList<AlternativeEntry>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public List<AlternativeEntry> getAlternates() {
		return alternates;
	}

	/**
	 * add a new alternative entry (subclass)
	 * 
	 * @param alternative
	 * @param optionChoice
	 */
	public void addAlternative(Field alternative, String optionChoice) {
		getAlternates().add(new AlternativeEntry(alternative, optionChoice));
	}

	/**
	 * AlternativeEntry Inner class
	 * 
	 * @author EHakawati
	 */
	public class AlternativeEntry extends Entry {
		private String optionChoice;
		private Field field;

		public AlternativeEntry(Field field, String optionChoice) {
			setField(field);
			setOptionChoice(optionChoice);
		}

		public String getOptionChoice() {
			return optionChoice;
		}

		public void setOptionChoice(String optionChoice) {
			this.optionChoice = optionChoice;
		}

		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		@Override
		public boolean isOptional() {
			return false;
		}

		@Override
		public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound {
			return this.getField().isValid(parsedMessage);
		}

	}

	@Override
	public void throwException() throws RequiredNotFound {
		throw new RequiredAlternativeNotFound(this);
	}

	@Override
	public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound {

		boolean isFound = false;
		int nextIndex = parsedMessage.getNextIndex();

		// one and only one of the sub-entries must be matched
		for (AlternativeEntry alternateEntry : getAlternates()) {

			if (alternateEntry.isValid(parsedMessage)) {

				if (isFound == true) {
					parsedMessage.pointTo(nextIndex);
					return false;
				}

				isFound = true;
			}
		}

		if (isFound == false && !isOptional()) {
			if (!parsedMessage.isExcelExport())
				this.throwException();
		}

		return isFound;
	}

}
