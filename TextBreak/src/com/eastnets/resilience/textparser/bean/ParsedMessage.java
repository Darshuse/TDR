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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.eastnets.resilience.textparser.syntax.entry.FieldPattern.PatternType;

/**
 * Parsed Entry holder/walker
 * 
 * @author EHakawati
 * 
 */
public class ParsedMessage {

	// parsed fields
	private List<ParsedField> parsedFields;
	// parsed loops
	private Map<Integer, ParsedLoop> parsedLoops;

	// parsed fields walker
	private ListIterator<ParsedField> parsedListIterator;
	// current walker object
	private ParsedField currentParsedEntry;

	private int groupIdx;
	private boolean excelExport;
	private boolean invalidMessages;

	/**
	 * Initialize
	 */
	public ParsedMessage() {
		parsedFields = new LinkedList<ParsedField>();
		parsedLoops = new LinkedHashMap<Integer, ParsedLoop>();
	}

	/**
	 * @return next text field entry index
	 */
	public int getNextIndex() {
		return this.parsedListIterator.nextIndex();
	}

	/**
	 * change the parsedListIterator location
	 * 
	 * @param nextIndex
	 */
	public void pointTo(int nextIndex) {
		if (this.parsedListIterator.nextIndex() > nextIndex) {
			while (this.parsedListIterator.nextIndex() > nextIndex) {
				currentParsedEntry = this.parsedListIterator.previous();
			}
		} else {
			while (this.parsedListIterator.nextIndex() < nextIndex) {
				currentParsedEntry = this.parsedListIterator.next();
			}
		}
	}

	/**
	 * reset parsedListIterator iterator
	 */
	public void initIterator() {
		parsedListIterator = getParsedFields().listIterator();
	}

	/**
	 * get next parsed field entry
	 * 
	 * @return
	 */
	public ParsedField getNextTextEntry() {

		currentParsedEntry = parsedListIterator.next();
		return getCurrentTextEntry();
	}

	/**
	 * get previous parsed field entry
	 * 
	 * @return
	 */
	public ParsedField getPreviousTextEntry() {

		currentParsedEntry = parsedListIterator.previous();
		return getCurrentTextEntry();
	}

	/**
	 * get the current parsed field entry
	 * 
	 * @return
	 */
	public ParsedField getCurrentTextEntry() {

		return currentParsedEntry;
	}

	/**
	 * is there any next field entry
	 * 
	 * @return
	 */
	public boolean hasNextTextEntry() {
		return parsedListIterator.hasNext();
	}

	/**
	 * is there any previous field entry
	 * 
	 * @return
	 */
	public boolean hasPreviousTextEntry() {
		return parsedListIterator.hasPrevious();
	}

	/**
	 * get the final Parsed Fields
	 * 
	 * @return
	 */
	public List<ParsedField> getParsedFields() {
		return parsedFields;
	}

	/**
	 * get the final Parsed Loops
	 * 
	 * @return
	 */
	public Map<Integer, ParsedLoop> getParsedLoops() {
		return parsedLoops;
	}

	/**
	 * get the final Parsed Loops
	 * 
	 * @return
	 */
	public List<ParsedLoop> getParsedLoopsSet() {
		List<ParsedLoop> parsedloopsList = new LinkedList<ParsedLoop>();

		for (Entry<Integer, ParsedLoop> entry : getParsedLoops().entrySet()) {
			parsedloopsList.add(entry.getValue());
		}

		return parsedloopsList;
	}

	/**
	 * add entry to parsed fields list
	 * 
	 * @param parsedField
	 */
	public void addParsedField(ParsedField parsedField) {
		getParsedFields().add(parsedField);

	}

	/**
	 * add entry to parsed loop list
	 * 
	 * @param parsedLoop
	 */
	public void addParsedLoop(ParsedLoop parsedLoop) {
		getParsedLoops().put(parsedLoop.getGroupIdx(), parsedLoop);

	}

	public String getExpandedMessage(Timestamp mesgCreaDateTime, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies) {

		StringBuilder message = new StringBuilder();

		for (ParsedField field : this.parsedFields) {
			message.append(field.getExpandedValue(mesgCreaDateTime, thousandAmountFormat, decimalAmountFormat, currencies));
			if (message.charAt(message.length() - 1) != '\n') {
				message.append("\r\n");
			}
		}

		return message.toString();
	}

	public String getExpandedSpecificMessage(Timestamp mesgCreaDateTime, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies, String feilds, boolean expandMessage) {

		StringBuilder message = new StringBuilder();

		for (ParsedField field : this.parsedFields) {
			if (feilds != null) {
				String[] feildsArray = feilds.split(",");
				for (String specificField : feildsArray) {
					if (field.getFieldCode().contains(specificField.trim())) {
						message.append(field.getExpandedValue(mesgCreaDateTime, thousandAmountFormat, decimalAmountFormat, currencies));
						if (message.charAt(message.length() - 1) != '\n') {
							message.append("\r\n");
						}
						message.append(";;");
						break;
					}
				}
			} else {
				message.append(field.getExpandedValue(mesgCreaDateTime, thousandAmountFormat, decimalAmountFormat, currencies));
				if (message.charAt(message.length() - 1) != '\n') {
					message.append("\r\n");
				}
				message.append(";;");
			}
		}

		return message.toString();
	}

	public Map<String, String> getExpandedMessageMap(String amtField, String ccyField, String valueDateField) {

		Map<String, String> keywordsFields = new HashMap<String, String>();// ref, amt,ccy,and valuedate

		String[] amtFields = amtField.trim().split(",");
		String[] ccyFields = ccyField.trim().split(",");
		String[] valueDateFields = valueDateField.trim().split(",");
		boolean amtFilled = false, ccyFilled = false, valueDateFilled = false, refFilled = false;
		if (amtFields[0].equalsIgnoreCase("null"))
			amtFilled = true;
		if (ccyFields[0].equalsIgnoreCase("null"))
			ccyFilled = true;
		if (valueDateFields[0].equalsIgnoreCase("null"))
			valueDateFilled = true;

		try {
			for (ParsedField field : this.parsedFields) {

				// REF(20)
				if (!refFilled && (field.getFieldCode().equals("20") || field.getFieldCode().equals("20C"))) {
					field.getExpandedValueMap(keywordsFields, PatternType.STRING);
					refFilled = true;
				}

				// check if all keywords have been filled
				if (refFilled && amtFilled && ccyFilled && valueDateFilled) {
					return keywordsFields;
				}

				// Amount
				if (!amtFilled && isKeyword(field, amtFields)) {
					field.getExpandedValueMap(keywordsFields, PatternType.AMOUNT);
					amtFilled = true;
				}

				// CCY
				if (!ccyFilled && isKeyword(field, ccyFields)) {
					field.getExpandedValueMap(keywordsFields, PatternType.CURRENCY);
					ccyFilled = true;
				}

				// Value date
				if (!valueDateFilled && isKeyword(field, valueDateFields)) {
					field.getExpandedValueMap(keywordsFields, PatternType.DATE);
					valueDateFilled = true;
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return keywordsFields;
	}

	private boolean isKeyword(ParsedField field, String[] amtFields) {
		for (String strVal : amtFields) {
			String seq = getSeq(strVal);
			String fieldCode = getFieldCode(strVal);
			String constCode = getConstCode(strVal);

			if (!field.getFieldCode().equals(fieldCode))
				continue;
			else if (!seq.isEmpty() && !field.getSequence().equals(seq.substring(0, 1)))
				continue;
			else if (!constCode.isEmpty() && !field.getValue().startsWith(constCode))
				continue;

			return true;
		}

		return false;
	}

	private String getSeq(String strVal) {
		strVal = strVal.toUpperCase().trim();
		if (!strVal.startsWith("SEQ")) {
			return "";
		}

		int dotIndex = strVal.indexOf(".");
		if (dotIndex < 0)
			return "";

		return strVal.substring(3, dotIndex);
	}

	private String getFieldCode(String strVal) {
		strVal = strVal.toUpperCase().trim();

		int dotIndex = strVal.indexOf(".F");
		int colonIndex = strVal.indexOf(":");
		if (dotIndex < 0 && colonIndex < 0) {
			return strVal.substring(1);
		} else if (dotIndex > 0 && colonIndex < 0) {
			return strVal.substring(dotIndex + 2);// +2 to start after .F
		} else if (dotIndex < 0 && colonIndex > 0) {
			return strVal.substring(1, colonIndex);// start from1 to remove F
		}

		return strVal.substring(dotIndex + 2, colonIndex);
	}

	private String getConstCode(String strVal) {
		strVal = strVal.trim();

		int colonIndex = strVal.indexOf(":");
		if (colonIndex < 0)
			return "";

		return strVal.substring(colonIndex);
	}

	public void removeTextField(ParsedField parsedField) {
		this.getParsedFields().remove(parsedField);
	}

	public int getGroupIdx() {
		return groupIdx;
	}

	public int getNextGroupIdx() {
		return ++groupIdx;
	}

	public void setGroupIdx(int groupIdx) {
		this.groupIdx = groupIdx;
	}

	public boolean isExcelExport() {
		return excelExport;
	}

	public void setExcelExport(boolean excelExport) {
		this.excelExport = excelExport;
	}

	public boolean isInvalidMessages() {
		return invalidMessages;
	}

	public void setInvalidMessages(boolean invalidMessages) {
		this.invalidMessages = invalidMessages;
	}

}
