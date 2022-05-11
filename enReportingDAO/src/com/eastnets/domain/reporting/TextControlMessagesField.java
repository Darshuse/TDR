package com.eastnets.domain.reporting;

import java.sql.Clob;

public class TextControlMessagesField {
	private String fieldCode;
	private String fieldOption;
	private String value;
	private Integer fieldCnt;
	private Clob valueMemo;
	private String sequence;
	private String fieldTag;
	private int fielRowNum;

	public String getFieldOption() {
		if (fieldOption == null)
			return "";
		return fieldOption;
	}

	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}

	public String getValue() {
		if (value == null)
			return "||";
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getFieldCnt() {
		return fieldCnt;
	}

	public void setFieldCnt(Integer fieldCnt) {
		this.fieldCnt = fieldCnt;
	}

	public Clob getValueMemo() {
		return valueMemo;
	}

	public void setValueMemo(Clob valueMemo) {
		this.valueMemo = valueMemo;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFieldTag() {
		return fieldTag;
	}

	public void setFieldTag(String fieldTag) {
		this.fieldTag = fieldTag;
	}

	public int getFielRowNum() {
		return fielRowNum;
	}

	public void setFielRowNum(int fielRowNum) {
		this.fielRowNum = fielRowNum;
	}
}
