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
package com.eastnets.resilience.textparser.syntax.generator.db;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.eastnets.resilience.textparser.db.bean.FullField;
import com.eastnets.resilience.textparser.db.bean.MessageDescription;
import com.eastnets.resilience.textparser.db.dao.FullFieldsDAO;
import com.eastnets.resilience.textparser.db.dao.MessageDescriptionDAO;
import com.eastnets.resilience.textparser.db.dao.impl.FullFieldsDAOImpl;
import com.eastnets.resilience.textparser.db.dao.impl.MessageDescriptionDAOImpl;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.syntax.Entry;
import com.eastnets.resilience.textparser.syntax.entry.Alternative;
import com.eastnets.resilience.textparser.syntax.entry.Field;
import com.eastnets.resilience.textparser.syntax.entry.FieldPattern;
import com.eastnets.resilience.textparser.syntax.entry.Loop;
import com.eastnets.resilience.textparser.syntax.entry.Option;
import com.eastnets.resilience.textparser.syntax.entry.Sequence;
import com.eastnets.resilience.textparser.syntax.generator.Generator;

/**
 * DB based parsing list generator This class is based on the OLD c++ syntax parser.
 * 
 * @author EHakawati
 */
public class DBGenerator implements Generator {

	// syntax table version
	private String version;
	// message table
	private String messageType;
	// message type description (version,type db idx and string description)
	private MessageDescription messageDiscription;

	// for parsing use
	private Set<String> sequencesIds;
	private Set<String> loopIds;
	private Set<String> optionsIds;
	private Set<String> alternativeIds;

	/**
	 * Object construction and MessageDescription loading
	 * 
	 * @param version
	 * @param mesgType
	 * @throws SQLException
	 * @throws SyntaxNotFound
	 */
	public DBGenerator(String version, String mesgType) throws SQLException, SyntaxNotFound {

		setVersion(version);
		setMessageType(mesgType);

		// Load version idx, type idx and description
		MessageDescriptionDAO descDAO = new MessageDescriptionDAOImpl();

		messageDiscription = descDAO.getMessageDescription(version, getMessageType());

		sequencesIds = new HashSet<String>();
		loopIds = new HashSet<String>();
		optionsIds = new HashSet<String>();
		alternativeIds = new HashSet<String>();

	}

	/**
	 * Load and parse DB FullFields into parsing list
	 * 
	 * @throws SyntaxNotFound
	 * @throws SQLException
	 * @return List<Entry> Parsing list
	 */
	@Override
	public List<Entry> getMessageParsingList() throws SQLException, SyntaxNotFound {

		FullFieldsDAO allFieldsView = new FullFieldsDAOImpl();

		// get stxGetAllFieldsView view output
		List<FullField> fullFields = allFieldsView.getSyntaxAllFieldsViews(this.messageDiscription.getVersionIdx(), this.messageDiscription.getMessageIdx());

		// double check
		if (fullFields.size() == 0) {
			throw new SyntaxNotFound(getVersion(), getMessageType());
		}

		// convert stxGetAllFieldsView output into parsing list (based on our
		// old syntax)
		List<Entry> parseList = getParsingList(fullFields.listIterator(), "", "");

		return parseList;
	}

	/**
	 * Parsing entry point (Recursion function)
	 * 
	 * @param fullFields
	 *            stxGetAllFieldsView list
	 * @param loopId
	 *            loop breaker signal
	 * @param sequenceId
	 *            sequence breaker signal
	 * @return parsing list
	 */
	private List<Entry> getParsingList(ListIterator<FullField> fullFields, String loopId, String sequenceId) {

		List<Entry> parseList = new LinkedList<Entry>();

		while (fullFields.hasNext()) {
			FullField fField = fullFields.next();

			// Parent Id value fix (A lot of loops issue on DB)
			if (fField.getParentLoopEntryId() != null) {
				String parentFix = fField.getLoopEntryId().replaceFirst("\\w+?\\.", "");
				if (parentFix.charAt(0) != 'S') {
					fField.setParentLoopEntryId(parentFix);
				}
			}

			// check loop breaker
			if (loopId != null && !"".equals(loopId) && !loopId.equals(fField.getLoopEntryId()) && !loopId.equals(fField.getParentLoopEntryId())) {
				fullFields.previous();
				return parseList;
			}

			// check sequence breaker
			if (sequenceId != null && !"".equals(sequenceId) && !sequenceId.equals(fField.getSequenceEntryId())) {
				fullFields.previous();
				return parseList;
			}

			// if the next FullField is a sequence
			if (fField.getSequenceEntryId() != null && !"".equals(fField.getSequenceEntryId()) && !sequencesIds.contains(fField.getSequenceEntryId())) {

				// add it to set in order to skip same sequence
				sequencesIds.add(fField.getSequenceEntryId());

				// add sequence entry
				parseList.add(getSequence(fullFields, fField));

				// if the next FullField is a loop parent
			} else if (fField.getParentLoopEntryId() != null && !"".equals(fField.getParentLoopEntryId()) && !loopIds.contains(fField.getParentLoopEntryId())) {

				// add it to set in order to skip same loop
				loopIds.add(fField.getParentLoopEntryId());

				// add loop entry
				parseList.add(getParentLoop(fullFields, fField));
				// if the next FullField is a loop
			} else if (fField.getLoopEntryId() != null && !"".equals(fField.getLoopEntryId()) && !loopIds.contains(fField.getLoopEntryId())) {

				// add it to set in order to skip same loop
				loopIds.add(fField.getLoopEntryId());

				// add loop entry
				parseList.add(getLoop(fullFields, fField));
				// if the next FullField is an option
			} else if (fField.getFieldOptionFieldTag() != null && !"".equals(fField.getFieldOptionFieldTag()) && !optionsIds.contains(fField.getFieldEntryId())) {

				// add it to set in order to skip same option
				optionsIds.add(fField.getFieldEntryId());

				// add option entry
				parseList.add(getOption(fullFields, fField));

				// if the next FullField is an alternative
			} else if (fField.getAlternativeEntryId() != null && !"".equals(fField.getAlternativeEntryId()) && !alternativeIds.contains(fField.getAlternativeEntryId())) {

				// add it to set in order to skip same alternative
				alternativeIds.add(fField.getAlternativeEntryId());

				// add an alternative entry
				parseList.add(getAlternative(fullFields, fField));

			} else {
				if (fField.getAlternativeEntryId() == null)
					// add Field
					parseList.add(getField(fullFields, fField));
			}
		}

		return parseList;
	}

	/**
	 * create an alternative entry
	 * 
	 * @param fullFields
	 * @param currentField
	 * @return alternative entry
	 */
	private Alternative getAlternative(ListIterator<FullField> fullFields, FullField currentField) {

		Alternative alternative = new Alternative();

		// fill alternative from FullField
		alternative.setId(currentField.getAlternativeEntryId());
		alternative.setOptional(currentField.isAlternativeIsOptional());

		// breaker
		String alternativeEntryId = currentField.getAlternativeEntryId();
		do {

			// add an alternative field
			alternative.addAlternative(getField(fullFields, currentField), currentField.getAlternativeOptionChoice());

			if (fullFields.hasNext()) {
				// get next full field entry
				currentField = fullFields.next();
			} else {
				break;
			}

		} while (alternativeEntryId.equals(currentField.getAlternativeEntryId()));

		// get one entry back
		fullFields.previous();

		return alternative;
	}

	/**
	 * create an option entry
	 * 
	 * @param fullFields
	 * @param currentField
	 * @return option entry
	 */
	private Option getOption(ListIterator<FullField> fullFields, FullField currentField) {

		Option option = new Option();

		// fill option from FullField
		option.setTag(currentField.getFieldOptionFieldTag());
		option.setPatternId(currentField.getFieldOptionPatternId());
		option.setOptional(currentField.isFieldOptionIsOptional());

		String fieldEntryId = currentField.getFieldEntryId();
		do {

			// add an option field
			option.addOption(getField(fullFields, currentField), currentField.getFieldOptionOptionChoice(), currentField.getFieldOptionExpansion());

			if (fullFields.hasNext()) {
				currentField = fullFields.next();
			} else {
				break;
			}

		} while (fieldEntryId.equals(currentField.getFieldEntryId()));

		// check if last object
		if (fullFields.hasNext()) {
			fullFields.previous();
		}

		return option;

	}

	/**
	 * create a loop entry
	 * 
	 * @param fullFields
	 * @param currentField
	 * @return loop entry
	 */
	private Loop getLoop(ListIterator<FullField> fullFields, FullField currentField) {

		Loop loop = new Loop();

		// fill loop from FullField
		loop.setId(currentField.getLoopEntryId());
		loop.setMaxOcc(currentField.getLoopMaxOcc());
		loop.setMinOcc(currentField.getLoopMinOcc());

		// get one entry back (in order to parse it as an entry (field))
		fullFields.previous();

		// recursive call will loop breaker
		loop.setEntries(getParsingList(fullFields, loop.getId(), ""));

		return loop;

	}

	/**
	 * create a loop parent entry
	 * 
	 * @param fullFields
	 * @param currentField
	 * @return loop entry
	 */
	private Loop getParentLoop(ListIterator<FullField> fullFields, FullField currentField) {

		Loop loop = new Loop();

		// fill loop from FullField
		loop.setId(currentField.getParentLoopEntryId());
		loop.setMaxOcc(currentField.getParentLoopMaxOcc());
		loop.setMinOcc(currentField.getParentLoopMinOcc());

		// get one entry back (in order to parse it as an entry (field))
		fullFields.previous();

		// recursive call will loop breaker
		loop.setEntries(getParsingList(fullFields, loop.getId(), ""));

		return loop;

	}

	/**
	 * create a sequence entry
	 * 
	 * @param fullFields
	 * @param currentField
	 * @return sequence entry
	 */
	private Sequence getSequence(ListIterator<FullField> fullFields, FullField currentField) {

		Sequence sequence = new Sequence();

		// fill sequence from FullField
		sequence.setId(currentField.getSequenceEntryId());
		sequence.setOptional(currentField.isSequenceIsOptional());
		sequence.setExpansion(currentField.getSequenceExpansion());

		// get one entry back (in order to parse it as an entry (field))
		fullFields.previous();

		// recursive call will sequence breaker
		sequence.setEntries(getParsingList(fullFields, "", sequence.getId()));

		return sequence;

	}

	/**
	 * create a field entry
	 * 
	 * @param fullFields
	 * @param currentField
	 * @return field entry
	 */
	private Field getField(ListIterator<FullField> fullFields, FullField currentField) {

		Field field = new Field();

		// fill field from FullField
		field.setId(currentField.getFieldEntryId());
		field.setTag(currentField.getFieldTag());
		field.setPatternId(currentField.getFieldPatternId());
		field.setExpansion(currentField.getFieldExpansion());
		field.setOptional(currentField.isFieldIsOptional());

		if (currentField.getFieldOptionOptionChoice() != null && currentField.getFieldOptionOptionChoice().length() == 1) {
			field.setOptionLetter(currentField.getFieldOptionOptionChoice());
		} else if (currentField.getAlternativeOptionChoice() != null && currentField.getAlternativeOptionChoice().length() == 1) {
			field.setOptionLetter(currentField.getAlternativeOptionChoice());
		}

		int fieldIdx = currentField.getFieldIdx();
		do {

			// add a pattern
			field.addPattern(getPattern(currentField));

			if (fullFields.hasNext()) {
				currentField = fullFields.next();
			} else {
				break;
			}

		} while (fieldIdx == currentField.getFieldIdx());

		// check if last object
		if (fullFields.hasNext()) {
			fullFields.previous();
		}

		return field;
	}

	/**
	 * fill a pattern entry
	 * 
	 * @param fullField
	 * @return pattern entry
	 */
	private FieldPattern getPattern(FullField fullField) {

		FieldPattern pattern = new FieldPattern();

		// fill pattern from FullField
		pattern.setId(fullField.getPatternId());
		pattern.setPrompt(fullField.getPatternPrompt());
		pattern.setMinChar(fullField.getPatternMinChar());
		pattern.setMaxChar(fullField.getPatternMaxChar());
		pattern.setNbRows(fullField.getPatternNbNows());
		pattern.setRowSeparator(fullField.getPatternRowSeparator());
		pattern.setType(fullField.getPatternType());
		pattern.setDefaultValue(fullField.getPatternDefaultValue());
		pattern.setExistenceCondition(fullField.getPatternExistenceCondition());
		pattern.setVisible(fullField.isPatternIsVisible());
		pattern.setOptional(fullField.isPatternIsOptional());
		pattern.setEditable(fullField.isPatternIsEditable());
		pattern.setVerifiable(fullField.isPatternIsVerifiable());

		return pattern;
	}

	@Override
	public List<FullField> getFullFields() {
		FullFieldsDAO allFieldsView = new FullFieldsDAOImpl();

		// get stxGetAllFieldsView view output
		List<FullField> fullFields = null;
		try {
			fullFields = allFieldsView.getAllFields(this.messageDiscription.getVersionIdx(), this.messageDiscription.getMessageIdx());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fullFields;

	}

	/**
	 * get message type description
	 * 
	 * @return description
	 */
	@Override
	public String getMessageDescription() {
		return this.messageDiscription.getDescription();
	}

	/**
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return message type
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

}
