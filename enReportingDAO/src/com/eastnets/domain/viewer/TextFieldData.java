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

package com.eastnets.domain.viewer;

import java.io.Serializable;
import java.sql.Clob;

/**
 * TextFieldData POJO
 * 
 * @author EastNets
 * @since September 20, 2012
 */
public class TextFieldData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2505705418211112615L;
	private Integer fieldCode;
	private String fieldOption;
	private String value;
	private Integer fieldCnt;
	private Clob valueMemo;
	private String sequence;

	public TextFieldData() {
	}

	private TextFieldData(TextFieldDataBulder builder) {
		this.fieldCode = builder.fieldCode;
		this.fieldOption = builder.fieldOption;
		this.value = builder.value;
		this.valueMemo = builder.valueMemo;
	}

	public void setFieldCode(Integer field_code) {
		this.fieldCode = field_code;
	}

	public Integer getFieldCode() {
		return fieldCode;
	}

	public void setFieldOption(String field_option) {
		this.fieldOption = field_option;
	}

	public String getFieldOption() {
		return fieldOption;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValueMemo(Clob value_memo) {
		this.valueMemo = value_memo;
	}

	public Clob getValueMemo() {
		return valueMemo;
	}

	public Integer getFieldCnt() {
		return fieldCnt;
	}

	public void setFieldCnt(Integer fieldCnt) {
		this.fieldCnt = fieldCnt;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public class TextFieldDataBulder {
		private Integer fieldCode;
		private String fieldOption;
		private String value;
		private Clob valueMemo;

		public TextFieldDataBulder(Integer fieldCode, String fieldOption, String value, Clob valueMemo) {
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

		public TextFieldData build() {
			return new TextFieldData(this);
		}

	}

}
