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

package com.eastnets.resilience.textparser.db.bean;

import java.io.Serializable;

/**
 * a full stxGetAllFieldsView record
 * 
 * @author EHakawati
 */
public class FullField implements Serializable {

	/**
	 * 
	 */

	private String fieldTagCr;
	private int feildCodeCr;
	private String feildOptionCr;
	private String feildSequanceCr;
	private int lineNumber;

	private static final long serialVersionUID = -2321151645947080633L;
	private int fieldIdx;
	private int fieldCnt;
	private String fieldEntryId;
	private String fieldTag;
	private String fieldPatternId;
	private String fieldExpansion;
	private boolean fieldIsOptional;
	private int loopId;
	private int fieldCode;

	private String loopEntryId;
	private int loopMinOcc;
	private int loopMaxOcc;
	private String parentLoopEntryId;
	private int parentLoopMinOcc;
	private int parentLoopMaxOcc;
	private String sequenceEntryId;
	private boolean sequenceIsOptional;
	private String sequenceExpansion;
	private String fieldOptionOptionChoice;
	private String fieldOptionExpansion;
	private String fieldOptionFieldTag;
	private String fieldOptionPatternId;
	private boolean fieldOptionIsOptional;
	private String alternativeEntryId;
	private boolean alternativeIsOptional;
	private String alternativeOptionChoice;
	private String patternPrompt;
	private int patternMinChar;
	private int patternMaxChar;
	private int patternNbNows;
	private String patternRowSeparator;
	private String patternType;
	private String patternDefaultValue;
	private String patternExistenceCondition;
	private boolean patternIsVisible;
	private boolean patternIsOptional;
	private boolean patternIsEditable;
	private boolean patternIsVerifiable;
	private String patternId;

	public FullField() {
		// TODO Auto-generated constructor stub
	}

	public FullField(String fieldTagCr, int feildCodeCr, String feildOptionCr, String feildSequanceCr, int lineNumber) {
		super();
		this.fieldTagCr = fieldTagCr;
		this.setFeildCodeCr(feildCodeCr);
		this.feildOptionCr = feildOptionCr;
		this.feildSequanceCr = feildSequanceCr;
		this.lineNumber = lineNumber;
	}

	public String getFulTag() {
		if (fieldOptionOptionChoice != null && !fieldOptionOptionChoice.equalsIgnoreCase("0")) {
			return fieldTag.trim() + fieldOptionOptionChoice;
		}
		return fieldTag.trim();
	}

	public int getFieldIdx() {
		return fieldIdx;
	}

	public void setFieldIdx(int fieldIdx) {
		this.fieldIdx = fieldIdx;
	}

	public int getFieldCnt() {
		return fieldCnt;
	}

	public void setFieldCnt(int fieldCnt) {
		this.fieldCnt = fieldCnt;
	}

	public String getFieldEntryId() {
		return fieldEntryId;
	}

	public void setFieldEntryId(String fieldEntryId) {
		this.fieldEntryId = fieldEntryId;
	}

	public String getFieldTag() {
		return fieldTag;
	}

	public void setFieldTag(String fieldTag) {
		this.fieldTag = fieldTag;
	}

	public String getFieldPatternId() {
		return fieldPatternId;
	}

	public void setFieldPatternId(String fieldPatternId) {
		this.fieldPatternId = fieldPatternId;
	}

	public String getFieldExpansion() {
		return fieldExpansion;
	}

	public void setFieldExpansion(String fieldExpansion) {
		this.fieldExpansion = fieldExpansion;
	}

	public boolean isFieldIsOptional() {
		return fieldIsOptional;
	}

	public void setFieldIsOptional(boolean fieldIsOptional) {
		this.fieldIsOptional = fieldIsOptional;
	}

	public int getLoopId() {
		return loopId;
	}

	public void setLoopId(int loopId) {
		this.loopId = loopId;
	}

	public String getLoopEntryId() {
		return loopEntryId;
	}

	public void setLoopEntryId(String loopEntryId) {
		this.loopEntryId = loopEntryId;
	}

	public int getLoopMinOcc() {
		return loopMinOcc;
	}

	public void setLoopMinOcc(int loopMinOcc) {
		this.loopMinOcc = loopMinOcc;
	}

	public int getLoopMaxOcc() {
		return loopMaxOcc;
	}

	public void setLoopMaxOcc(int loopMaxOcc) {
		this.loopMaxOcc = loopMaxOcc;
	}

	public String getParentLoopEntryId() {
		return parentLoopEntryId;
	}

	public void setParentLoopEntryId(String parentLoopEntryId) {
		this.parentLoopEntryId = parentLoopEntryId;
	}

	public int getParentLoopMinOcc() {
		return parentLoopMinOcc;
	}

	public void setParentLoopMinOcc(int parentLoopMinOcc) {
		this.parentLoopMinOcc = parentLoopMinOcc;
	}

	public int getParentLoopMaxOcc() {
		return parentLoopMaxOcc;
	}

	public void setParentLoopMaxOcc(int parentLoopMaxOcc) {
		this.parentLoopMaxOcc = parentLoopMaxOcc;
	}

	public String getSequenceEntryId() {
		return sequenceEntryId;
	}

	public void setSequenceEntryId(String sequenceEntryId) {
		this.sequenceEntryId = sequenceEntryId;
	}

	public boolean isSequenceIsOptional() {
		return sequenceIsOptional;
	}

	public void setSequenceIsOptional(boolean sequenceIsOptional) {
		this.sequenceIsOptional = sequenceIsOptional;
	}

	public String getSequenceExpansion() {
		return sequenceExpansion;
	}

	public void setSequenceExpansion(String sequenceExpansion) {
		this.sequenceExpansion = sequenceExpansion;
	}

	public String getFieldOptionOptionChoice() {
		return fieldOptionOptionChoice;
	}

	public void setFieldOptionOptionChoice(String fieldOptionOptionChoice) {
		this.fieldOptionOptionChoice = fieldOptionOptionChoice;
	}

	public String getFieldOptionExpansion() {
		return fieldOptionExpansion;
	}

	public void setFieldOptionExpansion(String fieldOptionExpansion) {
		this.fieldOptionExpansion = fieldOptionExpansion;
	}

	public String getFieldOptionFieldTag() {
		return fieldOptionFieldTag;
	}

	public void setFieldOptionFieldTag(String fieldOptionFieldTag) {
		this.fieldOptionFieldTag = fieldOptionFieldTag;
	}

	public String getFieldOptionPatternId() {
		return fieldOptionPatternId;
	}

	public void setFieldOptionPatternId(String fieldOptionPatternId) {
		this.fieldOptionPatternId = fieldOptionPatternId;
	}

	public boolean isFieldOptionIsOptional() {
		return fieldOptionIsOptional;
	}

	public void setFieldOptionIsOptional(boolean fieldOptionIsOptional) {
		this.fieldOptionIsOptional = fieldOptionIsOptional;
	}

	public String getAlternativeEntryId() {
		return alternativeEntryId;
	}

	public void setAlternativeEntryId(String alternativeEntryId) {
		this.alternativeEntryId = alternativeEntryId;
	}

	public boolean isAlternativeIsOptional() {
		return alternativeIsOptional;
	}

	public void setAlternativeIsOptional(boolean alternativeIsOptional) {
		this.alternativeIsOptional = alternativeIsOptional;
	}

	public String getAlternativeOptionChoice() {
		return alternativeOptionChoice;
	}

	public void setAlternativeOptionChoice(String alternativeOptionChoice) {
		this.alternativeOptionChoice = alternativeOptionChoice;
	}

	public String getPatternPrompt() {
		return patternPrompt;
	}

	public void setPatternPrompt(String patternPrompt) {
		this.patternPrompt = patternPrompt;
	}

	public int getPatternMinChar() {
		return patternMinChar;
	}

	public void setPatternMinChar(int patternMinChar) {
		this.patternMinChar = patternMinChar;
	}

	public int getPatternMaxChar() {
		return patternMaxChar;
	}

	public void setPatternMaxChar(int patternMaxChar) {
		this.patternMaxChar = patternMaxChar;
	}

	public int getPatternNbNows() {
		return patternNbNows;
	}

	public void setPatternNbNows(int patternNbNows) {
		this.patternNbNows = patternNbNows;
	}

	public String getPatternRowSeparator() {
		return patternRowSeparator;
	}

	public void setPatternRowSeparator(String patternRowSeparator) {
		this.patternRowSeparator = patternRowSeparator;
	}

	public String getPatternType() {
		return patternType;
	}

	public void setPatternType(String patternType) {
		this.patternType = patternType;
	}

	public String getPatternDefaultValue() {
		return patternDefaultValue;
	}

	public void setPatternDefaultValue(String patternDefaultValue) {
		this.patternDefaultValue = patternDefaultValue;
	}

	public String getPatternExistenceCondition() {
		return patternExistenceCondition;
	}

	public void setPatternExistenceCondition(String patternExistenceCondition) {
		this.patternExistenceCondition = patternExistenceCondition;
	}

	public boolean isPatternIsVisible() {
		return patternIsVisible;
	}

	public void setPatternIsVisible(boolean patternIsVisible) {
		this.patternIsVisible = patternIsVisible;
	}

	public boolean isPatternIsOptional() {
		return patternIsOptional;
	}

	public void setPatternIsOptional(boolean patternIsOptional) {
		this.patternIsOptional = patternIsOptional;
	}

	public boolean isPatternIsEditable() {
		return patternIsEditable;
	}

	public void setPatternIsEditable(boolean patternIsEditable) {
		this.patternIsEditable = patternIsEditable;
	}

	public boolean isPatternIsVerifiable() {
		return patternIsVerifiable;
	}

	public void setPatternIsVerifiable(boolean patternIsVerifiable) {
		this.patternIsVerifiable = patternIsVerifiable;
	}

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(int fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFieldTagCr() {
		return fieldTagCr;
	}

	public void setFieldTagCr(String fieldTagCr) {
		this.fieldTagCr = fieldTagCr;
	}

	public String getFeildOptionCr() {
		return feildOptionCr;
	}

	public void setFeildOptionCr(String feildOptionCr) {
		this.feildOptionCr = feildOptionCr;
	}

	public String getFeildSequanceCr() {
		return feildSequanceCr;
	}

	public void setFeildSequanceCr(String feildSequanceCr) {
		this.feildSequanceCr = feildSequanceCr;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getFeildCodeCr() {
		return feildCodeCr;
	}

	public void setFeildCodeCr(int feildCodeCr) {
		this.feildCodeCr = feildCodeCr;
	}

}
