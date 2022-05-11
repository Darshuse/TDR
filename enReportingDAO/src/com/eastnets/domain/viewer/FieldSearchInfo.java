
package com.eastnets.domain.viewer;

import com.eastnets.domain.BaseEntity;

/**
 * SearchResultEntity POJO
 * 
 * @author EastNets
 * @since September 20, 2012
 */
public class FieldSearchInfo extends BaseEntity {
	private static final long serialVersionUID = -8506568781848320128L;

	private String fieldTag;
	private String conditionValue;
	private String logicalOpretor;

	public enum Condition {
		CONTAINS, DOES_NOT_CONTAIN, EQUAL, SOUNDEX
	};

	private Condition condition;

	public FieldSearchInfo(String fieldTag, Condition condition, String conditionValue, String logicalOpretor) {
		this.fieldTag = fieldTag;
		this.condition = condition;
		this.conditionValue = conditionValue;
		this.logicalOpretor = logicalOpretor;
	}

	public String toString() {
		return String.format("%s %s %s %s", ((logicalOpretor == null) ? "" : logicalOpretor), fieldTag, condition, conditionValue);
	}

	public String getFieldTag() {
		return fieldTag;
	}

	public void setFieldTag(String fieldTag) {
		this.fieldTag = fieldTag;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getLogicalOpretor() {
		return logicalOpretor;
	}

	public void setLogicalOpretor(String logicalOpretor) {
		this.logicalOpretor = logicalOpretor;
	}

}