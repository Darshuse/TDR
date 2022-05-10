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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastnets.resilience.textparser.util.SwiftToRegexConverter;

/**
 * Pattern entry
 * 
 * @author EHakawati
 * 
 */
public class FieldPattern {

	String id;
	String prompt;
	int minChar;
	int maxChar;
	int nbRows;
	String rowSeparator;
	String type;
	String defaultValue;
	String existenceCondition;
	boolean isVisible;
	boolean isOptional;
	boolean isEditable;
	boolean isVerifiable;
	Pattern parser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id.trim();
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public int getMinChar() {
		return minChar;
	}

	public void setMinChar(int minChar) {
		this.minChar = minChar;
	}

	public int getMaxChar() {
		return maxChar;
	}

	public void setMaxChar(int maxChar) {
		this.maxChar = maxChar;
	}

	public int getNbRows() {
		return nbRows;
	}

	public void setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}

	public String getRowSeparator() {
		return rowSeparator;
	}

	public void setRowSeparator(String rowSeparator) {
		this.rowSeparator = rowSeparator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExistenceCondition() {
		return existenceCondition;
	}

	public void setExistenceCondition(String existenceCondition) {
		this.existenceCondition = existenceCondition;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isVerifiable() {
		return isVerifiable;
	}

	public void setVerifiable(boolean isVerifiable) {
		this.isVerifiable = isVerifiable;
	}

	public Pattern getParser() {
		if (parser == null) {
			String regex = SwiftToRegexConverter.getFinToRegexFormat(this.getPrompt(), false);
			parser = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);
		}
		return parser;
	}

	public void setParser(Pattern parser) {
		this.parser = parser;
	}

	/**
	 * check pattern
	 * 
	 * @param text
	 * @return
	 */
	public boolean isPatternAvailable(String text) {
		return getParser().matcher(text).find();
	}

	/**
	 * 
	 * @param text
	 * @param outputText
	 */
	public String parsePattern(String text) {
		Matcher matcher = getParser().matcher(text);
		if (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * 
	 * @author EHakawati
	 */
	public enum PatternType {

		STRING, AMOUNT, AMOUN, BIC_ADDRESS, CURRENCY, COUNTRY, SIGN, DATE, TIME, DEBIT_CREDI, DEBIT_CREDIT, FIELD_HEADER, NEW_LINE;

		public static PatternType valueOfType(String type) {
			type = type.replaceAll(" |/", "_");
			return PatternType.valueOf(type.toUpperCase());
		}
	}
}
