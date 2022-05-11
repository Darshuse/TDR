package com.eastnets.extraction.bean;

import java.sql.Clob;

public class TextFieldData {

	private Integer aid;
	private Integer mesgUmidl;
	private Integer mesgUmidh;
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

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getMesgUmidl() {
		return mesgUmidl;
	}

	public void setMesgUmidl(Integer mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}

	public Integer getMesgUmidh() {
		return mesgUmidh;
	}

	public void setMesgUmidh(Integer mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}

}
