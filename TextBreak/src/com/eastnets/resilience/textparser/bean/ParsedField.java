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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastnets.resilience.textparser.syntax.entry.FieldPattern;
import com.eastnets.resilience.textparser.syntax.entry.FieldPattern.PatternType;
import com.eastnets.resilience.textparser.util.SwiftFormatUtils;
import com.eastnets.resilience.textparser.util.SwiftToRegexConverter;

/**
 * Parsed Field Entry
 * 
 * @author EHakawati
 * 
 */
public class ParsedField implements ParsedEntry {

	private final String PADDING = "    ";
	private final String NEW_EXPAND_LINE = "\r\n";

	private String expansion;
	private String sequence;
	private String fieldOption;
	private String fieldCode;
	private String value;
	private String loopId;
	private int groupIdx;
	private List<FieldPattern> patterns;
	private String expandedValue;
	private int codeId;
	private String currency = "";

	public int getGroupIdx() {
		return groupIdx;
	}

	public void setGroupIdx(int groupIdx) {
		this.groupIdx = groupIdx;
	}

	public String getExpansion() {
		return expansion;
	}

	public void setExpansion(String expansion) {
		this.expansion = expansion;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getFieldOption() {
		return fieldOption;
	}

	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLoopId() {
		return loopId;
	}

	public void setLoopId(String loopId) {
		this.loopId = loopId;
	}

	public List<FieldPattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<FieldPattern> patterns) {
		this.patterns = patterns;
	}

	public void setExpandedValue(String expandedText) {
		this.expandedValue = expandedText;
	}

	public String getExpandedValue(Timestamp mesgCreaDateTime, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies) {

		currency = "";

		if (expandedValue != null) {
			return expandedValue;
		}

		String text = getValue();

		// Compacted Fields
		if (getFieldCode() == null) {
			return NEW_EXPAND_LINE + PADDING + text.replaceAll("\r\n", "\r\n" + PADDING);
		}
		ListIterator<FieldPattern> patterns = getPatterns().listIterator();

		StringBuilder expandedText = new StringBuilder();

		String tag = "F" + this.getFieldCode();

		if (tag.length() == 2) {
			expandedText.append(" ");
		}

		expandedText.append(tag);

		expandedText.append(": " + this.getExpansion() + NEW_EXPAND_LINE);

		synchronized (patterns) { // double checking also
			List<Integer> patternIndices = new ArrayList<Integer>();
			String patternRegEx = formatPatterns(patterns, patternIndices);
			patternRegEx = patternRegEx.replaceAll("a-zA-Z", "a-zA-Z\u0600-\u06FF");

			Matcher matcher = Pattern.compile(patternRegEx, Pattern.DOTALL | Pattern.MULTILINE).matcher(text);

			if (!matcher.find()) {
				// pattern was not matched
				if (text != null && !text.isEmpty()) {
					// this was added by Sameer so that he can always detect issues
					Map<String, String> env = System.getenv();
					if (env.containsKey("MAX_DEBUGGING") && env.get("MAX_DEBUGGING").equalsIgnoreCase("SAMEER")) {
						System.err.println(String.format("Field %s was not matched.", tag));
					}
				}
				if (!(patternRegEx.equalsIgnoreCase("^") && (text == null || text.isEmpty()))) {
					expandedText.append(NEW_EXPAND_LINE + PADDING + " " + text);
				}
				return expandedText.toString();
			}
			patterns = getPatterns().listIterator();
			int patternIndex = -1;

			while (patterns.hasNext()) {
				FieldPattern pattern = patterns.next();
				patternIndex++;
				try {
					PatternType type = PatternType.valueOfType(pattern.getType());
					String patternPrompt = pattern.getPrompt();

					switch (type) {
					case FIELD_HEADER:
						break; // SKIP
					case NEW_LINE:
						if (expandedText.charAt(expandedText.length() - 1) != '\n' && !expandedText.toString().endsWith(NEW_EXPAND_LINE + PADDING + " ")) {
							expandedText.append(NEW_EXPAND_LINE);
						}
						break;
					default:

						if (pattern.getPrompt() == null || !pattern.isVisible()) {
							if (expandedText.charAt(expandedText.length() - 1) == '\n') {
								expandedText.append(PADDING);
								expandedText.append(" ");
							}
							String defaultValue = pattern.getDefaultValue();
							if (defaultValue == null) {
								defaultValue = "";
							}
							expandedText.append(defaultValue);
							text = text.substring(defaultValue.length());

						} else {
							try {
								String matchedText = "";
								// if((pattern.getNbRows()>1 && pattern.getPrompt().endsWith("Z"))||pattern.getPrompt().endsWith("Z")){
								if (pattern.getNbRows() > 1 || pattern.getPrompt().endsWith("Z")) {

									matchedText = text;
								} else {
									matchedText = matcher.group(patternIndices.get(patternIndex));
								}

								if (matchedText != null && !"".equals(matchedText)) {
									if (expandedText.charAt(expandedText.length() - 1) == '\n') {
										expandedText.append(PADDING);
									}
									expandedText.append(" ");
									text = text.substring(matchedText.length());
									if (text.startsWith("\r\n")) {
										text = text.substring(2);
									}
									matchedText = matchedText.replaceAll("\r\n", "\r\n" + PADDING + " ");

									if (matchedText.endsWith(PADDING + " ")) {
										matchedText = matchedText.substring(0, matchedText.length() - (PADDING + " ").length());
									}
									if (type == PatternType.CURRENCY || type == PatternType.AMOUNT || type == PatternType.AMOUN || type == PatternType.DATE || type == PatternType.COUNTRY || type == PatternType.SIGN) {
										if (expandedText.charAt(expandedText.length() - 1) != '\n' && !expandedText.toString().endsWith(NEW_EXPAND_LINE + PADDING + " ")) {
											expandedText.append(NEW_EXPAND_LINE + PADDING + " ");
										}
									}
									expandedText.append(this.formatPattern(type, patternPrompt, matchedText, mesgCreaDateTime, thousandAmountFormat, decimalAmountFormat, currencies));
								}
							} catch (IllegalStateException ex) {
								// Invalid pattern
								if (!pattern.isOptional() && pattern.getMinChar() == 0) {
									throw ex;
								}
							}
						}
						break;
					}
				} catch (Exception ex) {
					// -- Unsupported type - Do nothing (double check)
					// expandedText.append(text);
					System.out.println(ex.getMessage());
				}
			}
		}
		return expandedText.toString();
	}

	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws ParseException
	 */
	public String formatPattern(PatternType type, String patternPrompt, String text, Timestamp mesgCreaDateTime, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies) throws ParseException {

		switch (type) {
		case AMOUN:
		case AMOUNT: {

			text = amountFormat(text, thousandAmountFormat, decimalAmountFormat, currencies);

			return "Amount       : " + text + "\t \t" + "#" + text + "#" + NEW_EXPAND_LINE;
		}

		case CURRENCY:
			currency = text;
			return "Currency     : " + SwiftFormatUtils.getCurrency(text) + NEW_EXPAND_LINE;

		case COUNTRY:
			return SwiftFormatUtils.getCountry(text) + NEW_EXPAND_LINE;

		case SIGN:
			return "Sign         : " + text + NEW_EXPAND_LINE;

		case DEBIT_CREDI:
		case DEBIT_CREDIT:
			return "Debit/Credit : " + (text.equalsIgnoreCase("D") ? "Debit" : "Credit") + NEW_EXPAND_LINE;
		case DATE:
			// Always in yyMMdd format
			return text + "\t \t \t \t" + SwiftFormatUtils.getDate(text, patternPrompt, "yyyy MMM dd") + NEW_EXPAND_LINE;

		case TIME:
			// Always in yyMMdd format
			return text + "\t \t \t \t" + SwiftFormatUtils.getTime(text, patternPrompt, "HH:mm:ss") + NEW_EXPAND_LINE;

		case BIC_ADDRESS:
			return SwiftFormatUtils.getFormatedBic(text, mesgCreaDateTime) + NEW_EXPAND_LINE;
		default:
			return text;
		}
	}

	public String amountFormat(String text, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies) {

		// String decimalFormatPattern = "###,###.000000";
		// String digits = "";
		String decimalFormatPattern = "";
		// if have currency show . digits by config in CU Currency Table for each currency

		// if (currency != null && !currency.isEmpty() && currency.length() == 3) {
		// Integer numberOfDigits = currencies.get(currency);
		//
		// String formatPattern = "0.";
		//
		// for (int i = 0; i < numberOfDigits; i++) {
		// formatPattern += "0";
		// }
		//
		// decimalFormatPattern = formatPattern;
		//
		// }

		DecimalFormat decimalFormat = new DecimalFormat(decimalFormatPattern);

		/*
		 * check amount configured separator from default config file
		 */
		if ((thousandAmountFormat != null && !thousandAmountFormat.isEmpty()) || (decimalAmountFormat != null && !decimalAmountFormat.isEmpty())) {
			int lastIndex = text.lastIndexOf(',');
			String fraction = text.substring(lastIndex + 1);
			text = text.subSequence(0, lastIndex) + "." + fraction;

			Double number = Double.parseDouble(text);

			String foramtedNumberStr = decimalFormat.format(number);

			String[] parts = foramtedNumberStr.split("\\.");
			String result = "";
			lastIndex = foramtedNumberStr.lastIndexOf('.');
			fraction = lastIndex < 0 ? "" : foramtedNumberStr.substring(lastIndex + 1);
			if (parts == null || parts.length == 0) {
				// Do nothing
			} else {
				if (fraction.isEmpty()) {
					result = parts[0] + ".";
					// return part zero only
				} else {
					result = parts[0] + "." + fraction.substring(0, fraction.length());
				}
			}

			if (thousandAmountFormat.equals(",")) {
				text = result;
				return text;
			} else if (decimalAmountFormat.equals(",")) {
				text = result;
				text = text.replace(",", "-"); // , = -
				text = text.replace(".", ",");
				text = text.replace("-", ".");
				return text;
			} else {
				text = text.replace(",", ".");
				number = Double.parseDouble(text);
				return decimalFormat.format(number);
			}

		} else {
			if (text.substring(text.length() - 1).equalsIgnoreCase(",")) {
				text = text.substring(0, text.length() - 1);
				return text;
			}
			if (!text.contains(",")) {
				return text;

			} else {

				text = text.replace(",", ".");
				// if (text.contains(".")) {
				// String[] amountValue = text.split("\\.");
				// digits = amountValue[1];
				// }

				// Double number = Double.parseDouble(text);
				// if (digits.length() > 6) {
				// return decimalFormat.format(number);
				// }
				return text;

			}
		}

	}

	public int getMaxTextSize() {

		int totalSize = 0;

		// Generate pats.
		// sum(cast(max_char as int) * cast(nb_rows as int)) as total_length

		if (getPatterns() != null) {
			for (FieldPattern pattern : getPatterns()) {
				totalSize = pattern.getMaxChar() * pattern.getNbRows();
			}
		}

		// Skip header pat.
		return totalSize - 1;

	}

	private String formatPatterns(ListIterator<FieldPattern> patterns, List<Integer> patternIndex) {
		String fieldPattern = "^";
		int patIndex = 1;
		while (patterns.hasNext()) {
			FieldPattern pattern = patterns.next();
			boolean followedByNewline = false;
			Boolean afterNewlineOptional = null;
			if (patterns.hasNext()) {
				FieldPattern nextPattern = patterns.next();

				followedByNewline = (PatternType.valueOfType(nextPattern.getType()) == PatternType.NEW_LINE);
				if (followedByNewline && patterns.hasNext()) {
					nextPattern = patterns.next();
					afterNewlineOptional = nextPattern.isOptional();
					patterns.previous();
				}

				patterns.previous();
			}
			PatternType type = PatternType.valueOfType(pattern.getType());
			int count = -1;
			if (type != PatternType.FIELD_HEADER && type != PatternType.NEW_LINE) {// ignore new lines and field headers

				if ((pattern.getPrompt() == null || pattern.getPrompt().isEmpty() || !pattern.isVisible())) {
					if (pattern.getDefaultValue() != null && !pattern.getDefaultValue().isEmpty()) {
						String val = "(" + SwiftToRegexConverter.toReqExConstString(pattern.getDefaultValue()) + ")";

						fieldPattern += val;
						count = countOF(val, "(") - countOF(val, "\\(");
						patIndex += count;
					}
					patternIndex.add(-1);

				} else {

					String prompt = pattern.getPrompt();
					String regex = SwiftToRegexConverter.getFinToRegexFormat(prompt, false);
					regex = regex.substring(1);
					if (followedByNewline) {
						if (afterNewlineOptional == null || afterNewlineOptional) {
							if (!pattern.isOptional()) {
								regex += "(\\r\\n){0,1}";
							} else {
								regex += "(\\r\\n){0,1}";
							}
						} else {
							regex = "(" + regex + "(\\r\\n)){0,1}";
						}
					}
					fieldPattern += "(" + regex + ")";
					patternIndex.add(patIndex);
					count = countOF(regex, "(") - countOF(regex, "\\(");
					patIndex += count + 1;
				}
			} else {
				patternIndex.add(-1);
			}
		}
		/*
		 * if ( fieldPattern.length() > 7 ){ //remove the [\\r\\n]* from the end fieldPattern= fieldPattern.substring(0, fieldPattern.length() - 7 ); }
		 */
		// System.out.println("*:" + fieldPattern + ":*" );
		return fieldPattern;
	}

	// count the number of occurrences of one string inside another
	private int countOF(String str, String val) {
		int count = -1;
		int index = -1;
		do {
			index = str.indexOf(val, index + 1);
			count++;

		} while (index != -1);

		return count;
	}

	public String getExpandedValueMap(Map<String, String> mesg, PatternType reqType) {

		if (expandedValue != null) {
			return expandedValue;
		}

		String text = getValue();

		// Compacted Fields
		if (getFieldCode() == null) {
			return NEW_EXPAND_LINE + PADDING + text.replaceAll("\r\n", "\r\n" + PADDING);
		}
		ListIterator<FieldPattern> patterns = getPatterns().listIterator();

		StringBuilder expandedText = new StringBuilder();

		String tag = this.getFieldCode();

		if (tag.length() == 2) {
			expandedText.append(" ");
		}

		expandedText.append(tag);

		expandedText.append(": " + this.getExpansion() + NEW_EXPAND_LINE);

		synchronized (patterns) { // double checking also
			List<Integer> patternIndices = new ArrayList<Integer>();
			String patternRegEx = formatPatterns(patterns, patternIndices);

			Matcher matcher = Pattern.compile(patternRegEx, Pattern.DOTALL | Pattern.MULTILINE).matcher(text);

			if (!matcher.find()) {
				// pattern was not matched
				if (text != null && !text.isEmpty()) {
					// this was added by Sameer so that he can always detect issues
					Map<String, String> env = System.getenv();
					if (env.containsKey("MAX_DEBUGGING") && env.get("MAX_DEBUGGING").equalsIgnoreCase("SAMEER")) {
						System.err.println(String.format("Field %s was not matched.", tag));
					}
				}
				if (!(patternRegEx.equalsIgnoreCase("^") && (text == null || text.isEmpty()))) {
					expandedText.append(NEW_EXPAND_LINE + PADDING + " " + text);
				}
				return expandedText.toString();
			}
			patterns = getPatterns().listIterator();
			int patternIndex = -1;

			while (patterns.hasNext()) {
				FieldPattern pattern = patterns.next();
				patternIndex++;
				try {
					PatternType type = PatternType.valueOfType(pattern.getType());

					if (reqType == PatternType.DATE && pattern.getPrompt() != null && !pattern.getPrompt().contains("YYMMDD")) // to handle when date pattern is STRING
						continue;
					else if (reqType != PatternType.DATE && reqType != type) {
						continue;
					}
					switch (type) {
					case FIELD_HEADER:
						break; // SKIP
					case NEW_LINE:
						if (expandedText.charAt(expandedText.length() - 1) != '\n' && !expandedText.toString().endsWith(NEW_EXPAND_LINE + PADDING + " ")) {
							expandedText.append(NEW_EXPAND_LINE);
						}
						break;
					default:

						if (pattern.getPrompt() == null || !pattern.isVisible()) {
							if (expandedText.charAt(expandedText.length() - 1) == '\n') {
								expandedText.append(PADDING);
								expandedText.append(" ");
							}
							String defaultValue = pattern.getDefaultValue();
							if (defaultValue == null) {
								defaultValue = "";
							}
							expandedText.append(defaultValue);
							text = text.substring(defaultValue.length());

						} else {
							try {
								String matchedText = matcher.group(patternIndices.get(patternIndex));
								if (matchedText != null && !"".equals(matchedText)) {
									if (expandedText.charAt(expandedText.length() - 1) == '\n') {
										expandedText.append(PADDING);
									}
									expandedText.append(" ");
									text = text.substring(matchedText.length());
									if (text.startsWith("\r\n")) {
										text = text.substring(2);
									}
									matchedText = matchedText.replaceAll("\r\n", "\r\n" + PADDING + " ");

									if (matchedText.endsWith(PADDING + " ")) {
										matchedText = matchedText.substring(0, matchedText.length() - (PADDING + " ").length());
									}
									if (type == PatternType.CURRENCY || type == PatternType.AMOUNT || type == PatternType.AMOUN || type == PatternType.DATE) {
										if (expandedText.charAt(expandedText.length() - 1) != '\n' && !expandedText.toString().endsWith(NEW_EXPAND_LINE + PADDING + " ")) {
											expandedText.append(NEW_EXPAND_LINE + PADDING + " ");
										}
									}
									// check if the field is already in the map.
									// we only need to get the first value of field 20
									if (!tag.startsWith("20"))
										mesg.put(tag + ":" + reqType.name(), matchedText);
									// to get only first value( after SEME if presented in case 20C, example 574) of field 20
									else if (mesg.get(tag + ":" + type.name()) == null && matchedText.compareTo("SEME") != 0)
										mesg.put(tag + ":" + reqType.name(), matchedText);

									// expandedText.append(this.formatPattern(type, matchedText, mesgCreaDateTime));
								}
							} catch (IllegalStateException ex) {
								// Invalid pattern
								if (!pattern.isOptional() && pattern.getMinChar() == 0) {
									throw ex;
								}
							}
						}
						break;
					}
				} catch (Exception ex) {
					// -- Unsupported type - Do nothing (double check)
					expandedText.append(text);
				}
			}
		}
		return expandedText.toString();
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
