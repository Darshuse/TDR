package com.eastnets.domain.reporting;

import java.io.Serializable;
import java.util.Date;

import com.eastnets.utils.ApplicationUtils;
import com.eastnets.domain.AdvancedDate;

public abstract class ReportSetParameterDateBase extends ReportSetParameter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -491370386127274305L;

	public abstract boolean getWithDate();

	
	private AdvancedDate firstValue= new AdvancedDate();
	private Date secondValue;
	
	public String getFirstValue() {		
		return getValueAdvancedDatetString( firstValue );
	}
	public void setFirstValue(String firstValue) {
		fillValueObject( this.firstValue, firstValue );
	}
	private void fillValueObject(AdvancedDate valueDate, String firstValueString) {
		ApplicationUtils.parseAdvancedDate(valueDate, firstValueString, getWithDate());
	}

	private Date fillValueObject( String firstValueString) {
		return ApplicationUtils.parseAdvancedDate(firstValueString, getWithDate());
		
	}
	public String getSecondValue() {
		return getValueObjectString( secondValue );
	}
	public void setSecondValue(String secondValue) {
		this.secondValue= fillValueObject( secondValue );
	}	

	private String getValueAdvancedDatetString(Object value) {
		if(value == null){
			return null;
		}
		return ApplicationUtils.formatReportAdvancedDate((AdvancedDate)value , getWithDate());
	}
	
	private String getValueObjectString(Object value) {
		if(value == null){
			return null;
		}
		return ApplicationUtils.formatReportDate((Date)value , getWithDate());
	}
	

	public Object getFirstValueObject() {
		return this.firstValue;
	}	
	
	public void setFirstValueObject(Object firstValue) {
		this.firstValue = (AdvancedDate) firstValue;
	}
	
	public Object getSecondValueObject() {
		return this.secondValue;
	}
	
	public void setSecondValueObject(Object secondValue) {
		this.secondValue = (Date) secondValue;
	}	
}
