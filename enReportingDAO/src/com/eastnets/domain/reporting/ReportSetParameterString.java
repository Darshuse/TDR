package com.eastnets.domain.reporting;

public class ReportSetParameterString extends ReportSetParameter{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8635192464657276600L;

	public ReportSetParameterString(){
		
	}

	private String firstValue;
	private String secondValue;

	public String getFirstValue() {
		return firstValue;
	}
	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}
	public String getSecondValue() {
		return secondValue;
	}
	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	public Object getValueObject(String value) {
		return value;
	}
	
	public Object getFirstValueObject() {
		if(firstValue == null){
			return null;
		}		
		return firstValue; 
	}	
	
	public void setFirstValueObject(Object firstValue) {
		this.firstValue = (String) firstValue;
	}
	
	public Object getSecondValueObject() {
		return this.secondValue;
	}
	
	public void setSecondValueObject(Object secondValue) {
		this.secondValue = (String) secondValue;
	}
}
