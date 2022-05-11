package com.eastnets.message.summary.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextFieldBean {

	private Integer fieldCode;
	private Integer fieldCodeId;
	private String sequenceId;
	private Integer groupIdx;
	private List<String> listOfvalue = new ArrayList<>();// values with no option
	private Map<String, List<String>> optionsValuesMap = new HashMap<>(); // fieldOption and listOfValues

	public Integer getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(Integer fieldCode) {
		this.fieldCode = fieldCode;
	}

	public Integer getFieldCodeId() {
		return fieldCodeId;
	}

	public void setFieldCodeId(Integer fieldCodeId) {
		this.fieldCodeId = fieldCodeId;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Integer getGroupIdx() {
		return groupIdx;
	}

	public void setGroupIdx(Integer groupIdx) {
		this.groupIdx = groupIdx;
	}

	public List<String> getListOfvalue() {
		return listOfvalue;
	}

	public void setListOfvalue(List<String> listOfvalue) {
		this.listOfvalue = listOfvalue;
	}

	public Map<String, List<String>> getOptionsValuesMap() {
		return optionsValuesMap;
	}

	public void setOptionsValuesMap(Map<String, List<String>> optionsValuesMap) {
		this.optionsValuesMap = optionsValuesMap;
	}

}
