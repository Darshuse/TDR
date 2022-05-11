package com.eastnets.domain.reporting;

import java.io.Serializable;
import com.eastnets.dao.common.Constants;

public abstract class ReportSetParameter extends Parameter implements Serializable{
	
	//abstract methods 

	public abstract Object getFirstValueObject();	
	public abstract void   setFirstValueObject(Object firstValue);

	public abstract Object getSecondValueObject();
	public abstract void   setSecondValueObject(Object secondValue) ;
	
	public abstract String getFirstValue() ;
	public abstract void   setFirstValue(String firstValue);
	
	public abstract String getSecondValue() ;
	public abstract void   setSecondValue(String secondValue);

	/**
	 * 
	 */
	private static final long serialVersionUID = -7344981620125799298L;
	private Long id;
	private Long type;
	private int maxLengthValue;
	private int minLengthValue;
	
	private boolean mandatory;
	
	private String firstName;
	private String secondName;
	private String description;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public int getMaxLengthValue() {
		return maxLengthValue;
	}
	public void setMaxLengthValue(int maxLengthValue) {
		this.maxLengthValue = maxLengthValue;
	}
	public int getMinLengthValue() {
		return minLengthValue;
	}
	public void setMinLengthValue(int minLengthValue) {
		this.minLengthValue = minLengthValue;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}	
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	
	
	public boolean isDateType(){
		boolean b = this.type == Constants.REPORT_PARAMTER_TYPE_DATE;
		return b;
	} 
	public boolean isDateTimeType(){
		boolean equals = this.type == Constants.REPORT_PARAMTER_TYPE_DATE_TIME;
		return equals;
	} 
	public boolean isCurrencyAmmountType(){
		boolean equals = this.type == Constants.REPORT_PARAMTER_TYPE_CURRENCY_AMOUNT;
		return equals;
	}	
	
	public boolean isMessageNametType(){
		boolean equals = this.type == Constants.REPORT_PARAMTER_TYPE_MESSAGE_NAME;
		return equals;
	}
	
}
