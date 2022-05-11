package com.eastnets.enGpiLoader.bulder;

import java.sql.Clob;

import com.eastnets.domain.viewer.TextFieldData;

public class TextFieldDataBulder {
	private Integer fieldCode;
	private String fieldOption;
	private String value;
	private Clob valueMemo;
	
	
	
	
	public TextFieldDataBulder(Integer fieldCode, String fieldOption, String value, Clob valueMemo) {
		super();
		this.fieldCode = fieldCode;
		this.fieldOption = fieldOption;
		this.value = value;
		this.valueMemo = valueMemo;
	}
	public Integer getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(Integer fieldCode) {
		this.fieldCode = fieldCode;
	}
	public String getFieldOption() {
		return fieldOption;
	}
	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Clob getValueMemo() {
		return valueMemo;
	}
	public void setValueMemo(Clob valueMemo) {
		this.valueMemo = valueMemo;
	}
	
	
 
}
