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
import com.eastnets.resilience.textparser.exception.RequiredOptionNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;

/**
 * Option entry
 * 
 * @author EHakawati
 * 
 */
public class Option extends Entry {

	String tag;
	String patternId;
	boolean isOptional;
	List<OptionEntry> options;

	public Option() {
		options = new LinkedList<OptionEntry>();
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag.trim();
	}

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public List<OptionEntry> getOptions() {
		return options;
	}

	/**
	 * add a new option entry (subclass)
	 * 
	 * @param field
	 * @param optionChoice
	 * @param expansion
	 */
	public void addOption(Field field, String optionChoice, String expansion) {

		getOptions().add(new OptionEntry(optionChoice, expansion, field));
	}

	/**
	 * OptionEntry Inner class
	 * 
	 * @author EHakawati
	 * 
	 */
	public class OptionEntry extends Entry {
		public OptionEntry(String optionChoice, String expansion, Field field) {
			setOptionChoice(optionChoice);
			setExpansion(expansion);
			setField(field);
		}

		String optionChoice; // a, b, c, etc
		String expansion;
		Field field;

		public String getOptionChoice() {
			return optionChoice;
		}

		public void setOptionChoice(String optionChoice) {
			this.optionChoice = optionChoice;
		}

		public String getExpansion() {
			return expansion;
		}

		public void setExpansion(String expansion) {
			this.expansion = expansion;
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
		throw new RequiredOptionNotFound(this);
	}

	@Override
	public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound {

		// next field must match one element of the option
		for (OptionEntry optionEntry : getOptions()) {
			if (optionEntry.isValid(parsedMessage)) {
				ParsedField parsedField = parsedMessage.getCurrentTextEntry();
				parsedField.setFieldOption(optionEntry.getOptionChoice());
				return true;
			}
		}

		// required option
		if (!isOptional()) {
			if (!parsedMessage.isExcelExport())
				this.throwException();
		}

		return false;

	}
}
