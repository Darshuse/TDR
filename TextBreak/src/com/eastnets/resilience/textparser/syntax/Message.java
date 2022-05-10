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
package com.eastnets.resilience.textparser.syntax;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedLoop;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.db.bean.FullField;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.exception.UnrecognizedBlockException;
import com.eastnets.resilience.textparser.syntax.entry.Field;
import com.eastnets.resilience.textparser.syntax.entry.Loop;
import com.eastnets.resilience.textparser.syntax.entry.Option;
import com.eastnets.resilience.textparser.syntax.entry.Option.OptionEntry;
import com.eastnets.resilience.textparser.syntax.entry.Sequence;
import com.eastnets.resilience.textparser.syntax.generator.Generator;
import com.eastnets.resilience.textparser.syntax.generator.db.DBGenerator;

/**
 * Message parser entry point
 * 
 * @author EHakawati
 */
public class Message {

	// parsing REGEX
	private static final Pattern pattern = Pattern.compile(":(\\w+?):(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern pattern77e = Pattern.compile(":77E:(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

	/**
	 * Message type
	 */
	private String messageType;

	/**
	 * Message Description
	 */
	private String description;

	private List<FullField> fullFields;

	private boolean excelExport;
	/**
	 * Parsing list
	 */
	private List<Entry> parseList;

	public List<Entry> getParseList() {
		return parseList;
	}

	public void setParseList(List<Entry> parseList) {
		this.parseList = parseList;
	}

	/**
	 * Constructor
	 * 
	 * @param messageType
	 */
	public Message(String messageType) {
		setMessageType(messageType);
		parseList = null;
	}

	/**
	 * Load the parser list
	 * 
	 * @param version
	 * @throws SyntaxNotFound
	 * @throws SQLException
	 */
	public void loadMessageType(String version) throws SQLException, SyntaxNotFound {

		// double check
		if (parseList != null) {
			return;
		}

		// TODO: Factories (DB or XSD ), we have only DB for now
		Generator parserGenerator = new DBGenerator(version, getMessageType());

		// Current message type description
		setDescription(parserGenerator.getMessageDescription());

		// get parse list
		parseList = parserGenerator.getMessageParsingList();

		// fullFields = parserGenerator.getFullFields();

	}

	/**
	 * The main entry of parsing
	 * 
	 * @param text
	 * @param expandText
	 * @return
	 * @throws RequiredNotFound
	 * @throws UnrecognizedBlockException
	 */
	public ParsedMessage parseMessageText(String text) throws RequiredNotFound, UnrecognizedBlockException {

		// create unparsed ParsedMessage object
		ParsedMessage parsedMessage = extractParsedMessage(text);

		// parse/validate ParsedMessage object
		parsedMessage.setExcelExport(isExcelExport());
		parseListWalker(parseList.iterator(), parsedMessage);

		// extract all loops
		fillParentLoops(parsedMessage);

		return parsedMessage;

	}

	/**
	 * The main entry of parsing
	 * 
	 * @param text
	 * @param expandText
	 * @return
	 * @throws RequiredNotFound
	 * @throws UnrecognizedBlockException
	 */
	public ParsedMessage parseMessageTextRJE(String text) throws UnrecognizedBlockException {

		// create unparsed ParsedMessage object
		ParsedMessage parsedMessage = extractParsedMessage(text);

		try {
			// parse/validate ParsedMessage object
			parseListWalker(parseList.iterator(), parsedMessage);
		} catch (RequiredNotFound ex) {
			// return if any entry is missing
			return parsedMessage;
		}
		// extract all loops
		fillParentLoops(parsedMessage);

		return parsedMessage;

	}

	/**
	 * 
	 * @param parsedMessage
	 */
	private void fillParentLoops(ParsedMessage parsedMessage) {

		Map<Integer, ParsedLoop> parsedLoops = parsedMessage.getParsedLoops();
		Map<String, Integer> parents = new HashMap<String, Integer>();

		List<Integer> groupIdxs = new LinkedList<Integer>(parsedLoops.keySet());
		Collections.sort(groupIdxs);

		for (int groupIdx : groupIdxs) {

			ParsedLoop loop = parsedLoops.get(groupIdx);
			String groupId = loop.getGroupId();
			parents.put(loop.getSequence() + loop.getGroupId(), groupIdx);

			String parentGroupId = groupId.substring(0, groupId.length() - 1);

			if (parentGroupId.length() > 0 && parents.get(loop.getSequence() + parentGroupId) != null) {
				loop.setParentGroupIdx(parents.get(loop.getSequence() + parentGroupId));
			}
		}
	}

	/**
	 * Parse text into key/value parsed text
	 * 
	 * @param text
	 * @param parsedMessage
	 */
	private ParsedMessage extractParsedMessage(String text) {

		ParsedMessage parsedMessage = new ParsedMessage();

		// check 77E field (sub message)
		String field77E = null;
		Matcher matcher77e = pattern77e.matcher(text);
		if (matcher77e.find()) {
			field77E = matcher77e.group(1);
			text = matcher77e.replaceAll("");
		}

		/*
		 * fixed for the following bugs 32142 Dynamic Report: MT586 Message Expand Failed 32140 Dynamic Report: MT565 Message Expand Failed work arround to ignore last space char in text
		 */
		text = text.replaceAll("\\s+$", "");
		// line splitter
		String[] fields = text.split("\\\\r\\\\n:|\\r\\n:|\n:");

		// create ParsedField and attach it to the ParsedMessage object
		for (String field : fields) {
			Matcher matcher = Message.pattern.matcher(":" + field);
			if (matcher.find()) {

				ParsedField parsedField = new ParsedField();
				String code = matcher.group(1);
				parsedField.setFieldCode(code);
				parsedField.setFieldOption(getFieldCodeOption(code));
				parsedField.setValue(matcher.group(2));
				parsedMessage.addParsedField(parsedField);
			}
		}

		// add sub message (77E)
		if (field77E != null) {
			ParsedField parsedField = new ParsedField();
			parsedField.setFieldCode("77E");
			parsedField.setValue(field77E);
			parsedField.setFieldOption("E");
			parsedMessage.addParsedField(parsedField);
		}
		// parsedMessage.getParsedFields().sort(new Comparator<ParsedField>() {
		// @Override
		// public int compare(ParsedField o1, ParsedField o2) {
		// return o1.getFieldCode().compareTo(o2.getFieldCode());
		// }
		// });
		return parsedMessage;
	}

	private String getFieldCodeOption(String code) {
		if (code != null && code.length() == 3) {
			return code.substring(2);
		}
		return null;
	}

	/**
	 * parsing entry point
	 * 
	 * @param parseList
	 * @param parsedMessage
	 * @throws RequiredNotFound
	 * @throws UnrecognizedBlockException
	 */
	@SuppressWarnings("rawtypes")
	private void parseListWalker(Iterator parseList, ParsedMessage parsedMessage) throws RequiredNotFound, UnrecognizedBlockException {

		// reset/initialize parser
		parsedMessage.initIterator();
		Map<String, String> env = System.getenv();
		// Level 1 parsing (level 2,3,... will be checked recursively)
		while (parseList.hasNext()) {
			Entry entry = (Entry) parseList.next();

			// this was added by Sameer so that he can always detect issues
			if (env.containsKey("MAX_DEBUGGING") && env.get("MAX_DEBUGGING").equalsIgnoreCase("SAMEER")) {
				print2(entry, "");
			}

			if (!entry.isValid(parsedMessage) && !entry.isOptional()) {
				if (!isExcelExport()) {
					entry.throwException();
				} else {
					parsedMessage.setInvalidMessages(true);
				}
			}
		}

		// this was added by Sameer so that he can always detect issues
		if (env.containsKey("MAX_DEBUGGING") && env.get("MAX_DEBUGGING").equalsIgnoreCase("SAMEER")) {
			System.out.println("-------------------------------------------------------------");
		}
		// If not all entries parsed
		if (parsedMessage.hasNextTextEntry()) {
			if (!isExcelExport()) {
				throw new UnrecognizedBlockException(parsedMessage);
			} else {
				parsedMessage.setInvalidMessages(true);
			}

		}

	}

	// added for debugging reasons, this will print the syntax as read from the database
	@SuppressWarnings("unused")
	private void print(Entry entry, String tabs) {
		try {
			Field f = (Field) entry;
			if (f != null) {
				System.out.println(tabs + f.getId() + ":" + f.getTag() + ", " + f.getPatternId());
				return;
			}
		} catch (Exception e) {
		}
		try {
			Sequence s = (Sequence) entry;
			if (s != null) {
				System.out.println(tabs + "Sequence:" + s.getId());
				for (Entry subEntry : s.getEntries()) {
					print(subEntry, tabs + "   ");
				}
			}
		} catch (Exception e) {
		}
		try {
			Loop l = (Loop) entry;
			if (l != null) {
				System.out.println(tabs + "Loop:" + l.getId());
				for (Entry subEntry : l.getEntries()) {
					print(subEntry, tabs + "   ");
				}
			}
		} catch (Exception e) {
		}
		try {
			Option op = (Option) entry;
			if (op != null) {
				System.out.print(tabs + "Option:" + op.getTag() + " ");
				for (OptionEntry oe : op.getOptions()) {
					System.out.print(":" + oe.getOptionChoice());
				}
				System.out.println("");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @return message type
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * Set message type
	 * 
	 * @param messageType
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return message description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * set message description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	private void print2(Entry entry, String tabs) {
		try {
			Field f = (Field) entry;
			if (f != null) {
				if (f.isOptional()) {
					System.out.print(tabs + "(O)");
				} else {

					System.out.print(tabs + "(M)");
				}
				System.out.println(f.getPatternId().replace('.', ':'));
				return;
			}
		} catch (Exception e) {
		}
		try {
			Sequence s = (Sequence) entry;
			if (s != null) {
				// System.out.println(tabs + "Sequence:" + s.getId() );
				for (Entry subEntry : s.getEntries()) {
					print2(subEntry, tabs);
				}
			}
		} catch (Exception e) {
		}
		try {
			Loop l = (Loop) entry;
			if (l != null) {
				if (l.isOptional()) {
					System.out.print(tabs + "(O)");
				} else {

					System.out.print(tabs + "(M)");
				}
				System.out.println("{");
				for (Entry subEntry : l.getEntries()) {
					print2(subEntry, tabs + "  ");
				}
				System.out.println(tabs + "}");
			}
		} catch (Exception e) {
		}
		try {
			Option op = (Option) entry;
			if (op != null) {
				// System.out.print(tabs + "Option:" + op.getTag() + " ");
				if (op.isOptional()) {
					System.out.print(tabs + "(O)");
				} else {

					System.out.print(tabs + "(M)");
				}
				for (OptionEntry oe : op.getOptions()) {
					System.out.print(":" + op.getTag() + ":" + oe.getOptionChoice() + ", ");
				}
				System.out.println("");
			}
		} catch (Exception e) {
		}
	}

	public List<FullField> getFullFields() {
		return fullFields;
	}

	public void setFullFields(List<FullField> fullFields) {
		this.fullFields = fullFields;
	}

	public boolean isExcelExport() {
		return excelExport;
	}

	public void setExcelExport(boolean excelExport) {
		this.excelExport = excelExport;
	}

}
