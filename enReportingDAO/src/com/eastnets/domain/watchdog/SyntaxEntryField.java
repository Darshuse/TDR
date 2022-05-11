package com.eastnets.domain.watchdog;

import java.io.Serializable;

public class SyntaxEntryField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6886511568458081649L;
	private String fieldValue;
	private Long code;
	private Long codeId;
	private String fieldOption;
	
	public SyntaxEntryField(){
		fieldValue = "";
		code = -1L;
		codeId = -1L;
		fieldOption="";
	}
	
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public Long getCodeId() {
		return codeId;
	}
	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}
	
	
	public String getFieldOption() {
		return fieldOption;
	}
	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		
		SyntaxEntryField o = (SyntaxEntryField)obj;
		
		if(o.code == this.code && o.codeId == this.codeId && o.getFieldValue().equals(this.fieldValue))
			return true;
		
		return false;
	}
	
}
