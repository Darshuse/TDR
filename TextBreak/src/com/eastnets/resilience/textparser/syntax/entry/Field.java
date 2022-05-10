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
import com.eastnets.resilience.textparser.exception.RequiredFieldNotFound;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;

public class Field extends Entry {

	private String id;
	private String tag;
	private String patternId;
	private String expansion;
	private boolean isOptional;
	private String optionLetter;
	private int codeId;

	private final List<FieldPattern> patterns = new LinkedList<FieldPattern>();

	public Field() {
		setOptionLetter("");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;

		if (id.charAt(id.length() - 2) == '.') {
			char lastChar = id.charAt(id.length() - 1);
			if (lastChar >= '0' && lastChar <= '9') {
				codeId = lastChar - '0';
			}
		} else {
			String[] idSplitings = id.split("\\.");
			String lastIndex = idSplitings[idSplitings.length - 1];
			if (lastIndex.matches("[0-9]+")) {
				codeId = Integer.parseInt(lastIndex);
			}
		}
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		if (tag != null) {
			this.tag = tag.trim();
		}
	}

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public String getExpansion() {
		return expansion;
	}

	public void setExpansion(String expansion) {
		this.expansion = expansion;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public List<FieldPattern> getPatterns() {
		return patterns;
	}

	public void addPattern(FieldPattern pattern) {
		this.getPatterns().add(pattern);
	}

	public String getOptionLetter() {
		return optionLetter;
	}

	public void setOptionLetter(String optionLetter) {
		this.optionLetter = optionLetter;
	}

	@Override
	public void throwException() throws RequiredNotFound {
		throw new RequiredFieldNotFound(this);
	}

	@Override
	public boolean isValid(ParsedMessage parsedMessage) throws RequiredNotFound {

		if (parsedMessage.hasNextTextEntry()) {

			if (this.getTag() == null) {
				fieldsCompaction(parsedMessage);
				return true;
			}

			ParsedField field = parsedMessage.getNextTextEntry();
			String tag = this.getTag() + this.getOptionLetter();

			if (tag.equals("F" + field.getFieldCode())) {

				if ("16S".equalsIgnoreCase(field.getFieldCode()) || "16R".equalsIgnoreCase(field.getFieldCode())) {
					if (field.getValue().equals(this.getPatterns().get(1).getDefaultValue())) {
						field.setExpansion(getExpansion());
						field.setPatterns(getPatterns());
						field.setCodeId(codeId);
						return true;
					}
				} else {
					field.setExpansion(getExpansion());
					field.setPatterns(getPatterns());
					field.setCodeId(codeId);
					return true;
				}
			}

			parsedMessage.getPreviousTextEntry();

		}
		return false;
	}

	/**
	 * handle text message (NOTAG fields)
	 * 
	 * @param parsedMessage
	 */
	private void fieldsCompaction(ParsedMessage parsedMessage) {

		ParsedField field = new ParsedField();
		List<ParsedField> removalFields = new LinkedList<ParsedField>();

		StringBuilder output = new StringBuilder();

		while (parsedMessage.hasNextTextEntry()) {
			ParsedField parsedField = parsedMessage.getNextTextEntry();
			output.append(":");
			output.append(parsedField.getFieldCode());
			output.append(":");
			output.append(parsedField.getValue() + "\r\n");
			removalFields.add(parsedField);
		}

		field.setValue(output.toString());

		parsedMessage.addParsedField(field);

		for (ParsedField parsedField : removalFields) {
			parsedMessage.removeTextField(parsedField);
		}

		parsedMessage.initIterator();
		parsedMessage.pointTo(parsedMessage.getParsedFields().size());

	}

}
