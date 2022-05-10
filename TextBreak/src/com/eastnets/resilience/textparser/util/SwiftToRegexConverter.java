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
package com.eastnets.resilience.textparser.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author EHakawati
 * 
 */
public class SwiftToRegexConverter {

	private enum ReplaceType {
		MULTI_LINE, AVAILABLE_CAPITAL_TYPES, AVAILABLE_SMALL_TYPES, OPTIONAL_START, OPTIONAL_END, CURRENCY, ORING, DATE_TIME, SIGN;
	}

	private static final Map<String, String> finToRegex = new HashMap<String, String>();
	private static final Map<ReplaceType, Pattern> replacePatterns = new HashMap<ReplaceType, Pattern>();
	private static final Set<String> staticFields = new HashSet<String>();
	private static final Map<String, String> regexConstVals = new HashMap<String, String>();

	// Static initializer
	static {

		finToRegex.put("X", "[0-9\\\\.a-zA-Z/\\\\-\\\\?:\\\\(\\\\),\\\\'\\\\+ \"]");
		finToRegex.put("Z", "[0-9\\\\.a-zA-Z!&quot;%&amp;\\\\*;&lt;&gt; ,\\\\(\\\\)/=\\\\'\\\\+:\\\\?@#\\\\{\\\\-_]");
		finToRegex.put("N", "([0-9]+[,]?[0-9]*)");
		finToRegex.put("Y", "[0-9\\\\.A-Z,\\\\-\\\\(\\\\)/=\\\\';\\\\+:\\\\?!&quot;%&amp;\\\\*&lt;&gt;; ]");
		finToRegex.put("A", "[A-Za-z]");
		finToRegex.put("B", "[a-zA-Z0-9]");
		finToRegex.put("D", "[0-9]");
		finToRegex.put("H", "[A-F0-9]");
		finToRegex.put("S", " ");

		replacePatterns.put(ReplaceType.ORING, Pattern.compile("(\\w+\\|\\w+)+\\|\\w+", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.MULTI_LINE, Pattern.compile("\\((\\d+)\\-(\\d+|n)\\) (.*)", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.AVAILABLE_CAPITAL_TYPES, Pattern.compile("(.*?)(\\d+)([X|Z|N|Y|A|B|D|H])(.*)", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.AVAILABLE_SMALL_TYPES, Pattern.compile("(.*?)(\\d+)([x|z|n|y|a|b|d|h|s])(.*)", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.OPTIONAL_START, Pattern.compile("(.*)\\[(.+)", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.OPTIONAL_END, Pattern.compile("(.+)\\](.*)", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.CURRENCY, Pattern.compile("(\\d+)c", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.DATE_TIME, Pattern.compile("(YY|MM|DD|HH|MM|SS)+", Pattern.DOTALL | Pattern.MULTILINE));
		replacePatterns.put(ReplaceType.SIGN, Pattern.compile("(\\+/\\-)", Pattern.DOTALL | Pattern.MULTILINE));

		staticFields.add("NOT EXCEEDING");
		staticFields.add("COPY OF MANDATORY FIELDS OF REFERENCED MESSAGE");

		regexConstVals.put("\\", "\\\\");
		regexConstVals.put("?", "\\?");
		regexConstVals.put("*", "\\*");
		regexConstVals.put("+", "\\+");
		regexConstVals.put("-", "\\-");
		regexConstVals.put("'", "\\'");
		regexConstVals.put(".", "\\.");
		regexConstVals.put("\"", "&quot;");
		regexConstVals.put("%", "&amp;");
		regexConstVals.put("<", "&lt;");
		regexConstVals.put(">", "&gt;");
		regexConstVals.put("[", "\\[");
		regexConstVals.put("]", "\\]");
		regexConstVals.put(")", "\\)");
		regexConstVals.put("(", "\\(");
		regexConstVals.put("{", "\\{");
		regexConstVals.put("}", "\\}");
		regexConstVals.put("|", "\\|");
	}

	public static String getFinToRegexFormat(String inputPrompt, boolean followedByNewline) {
		if (inputPrompt == null || inputPrompt.isEmpty()) {
			return "^(.)*";
		}

		StringBuilder regex = new StringBuilder("^(");

		// this was added to fix bugs, it suppose not to affect the detection but it may allow wrong formatted data
		// if thats the case then see the documentation written inside modifyPrompt() and be careful not to make it worse for other messages
		inputPrompt = modifyPrompt(inputPrompt);

		if (staticFields.contains(inputPrompt)) {

			regex.append(inputPrompt);

		} else {

			Matcher matcher = replacePatterns.get(ReplaceType.MULTI_LINE).matcher(inputPrompt);

			if (matcher.find()) {
				if (matcher.replaceAll("$2").equalsIgnoreCase("n")) {
					inputPrompt = matcher.replaceAll("($3(\\\\r\\\\n)?){$1,}");
				} else {
					inputPrompt = matcher.replaceAll("($3(\\\\r\\\\n)?){$1,$2}");
				}

			}

			String[] prompts = inputPrompt.split(" ");
			String optionalNewLine = "";

			if (followedByNewline) {
				optionalNewLine = "(\\\\r\\\\n)";
			}

			for (String prompt : prompts) {

				matcher = replacePatterns.get(ReplaceType.OPTIONAL_START).matcher(prompt);

				while (matcher.find()) {
					prompt = matcher.replaceAll("$1($2");
					matcher = replacePatterns.get(ReplaceType.OPTIONAL_START).matcher(prompt);
				}

				matcher = replacePatterns.get(ReplaceType.OPTIONAL_END).matcher(prompt);

				while (matcher.find()) {
					prompt = matcher.replaceAll("$1){0,1}$2");
					matcher = replacePatterns.get(ReplaceType.OPTIONAL_END).matcher(prompt);
				}

				matcher = replacePatterns.get(ReplaceType.SIGN).matcher(prompt);

				while (matcher.find()) {
					prompt = matcher.replaceAll("(\\\\+|\\\\-)");
					matcher = replacePatterns.get(ReplaceType.SIGN).matcher(prompt);
				}

				matcher = replacePatterns.get(ReplaceType.AVAILABLE_CAPITAL_TYPES).matcher(prompt);
				while (matcher.find()) {
					prompt = matcher.replaceAll("$1(" + finToRegex.get(matcher.group(3)) + "{1,$2}" + optionalNewLine + ")$4");
					matcher = replacePatterns.get(ReplaceType.AVAILABLE_CAPITAL_TYPES).matcher(prompt);
				}

				matcher = replacePatterns.get(ReplaceType.AVAILABLE_SMALL_TYPES).matcher(prompt);
				while (matcher.find()) {
					prompt = matcher.replaceAll("$1(" + finToRegex.get(matcher.group(3).toUpperCase()) + "{$2}" + optionalNewLine + ")$4");
					matcher = replacePatterns.get(ReplaceType.AVAILABLE_SMALL_TYPES).matcher(prompt);
				}

				matcher = replacePatterns.get(ReplaceType.CURRENCY).matcher(prompt);
				while (matcher.find()) {
					prompt = matcher.replaceAll("([a-zA-Z]{$1})");
					matcher = replacePatterns.get(ReplaceType.CURRENCY).matcher(prompt);
				}

				matcher = replacePatterns.get(ReplaceType.ORING).matcher(prompt);
				if (matcher.find()) {
					prompt = matcher.replaceAll("($0)");
				}

				matcher = replacePatterns.get(ReplaceType.DATE_TIME).matcher(prompt);
				while (matcher.find()) {
					prompt = matcher.replaceAll(finToRegex.get("D") + "{" + matcher.group(0).length() + "}");
					matcher = replacePatterns.get(ReplaceType.DATE_TIME).matcher(prompt);
				}

				regex.append(prompt);
			}
		}
		return regex.append(")").toString();

	}

	private static String modifyPrompt(String inputPrompt) {
		/*
		 * we had a little bugs where int some messages ( for example 103 ) we have some fields that has a preceding / that sometimes needs to be handled and some other times it needs not to be
		 * handled , more details for some 103 message : field 54B has the format [/1!a][/34x] which means ( in our understanding and in our regular expression generation that -match the char('/') and
		 * then match one alpha char then match the char('/') again and match up to 34 chars from the "SWIFT Character Set (X Character Set)" * but the case is that the second / char was never there
		 * so the string matching fails (the value was something like /sameer and this seems to be acceptable for SAA same things apply for the field 55b, 51b and even the field 50A which has the
		 * format [/34x]
		 * 
		 * What is going to be done here is that i am going to remove the / before any "SWIFT Character Set (X Character Set)" block, cause i had some theory based on this message and other messages
		 * that i have tested that this / may not appear
		 * 
		 * and what makes this solution good to my eye that the "SWIFT Character Set (X Character Set)" already contains a / so if this / is present we are gonna match it anyway
		 */

		Matcher matcher = Pattern.compile("(.*)\\[/(\\d+)(!?)(x|X)\\](.*)", Pattern.DOTALL | Pattern.MULTILINE).matcher(inputPrompt);

		if (matcher.find()) {
			inputPrompt = matcher.replaceAll("$1[/] \\[$2$3$4\\]$5");
		}

		return inputPrompt;
	}

	public static String toReqExConstString(String defaultValue) {
		String val = defaultValue;

		Iterator<Entry<String, String>> it = regexConstVals.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
			val = val.replace(pairs.getKey(), pairs.getValue());
		}
		return val;
	}

}
