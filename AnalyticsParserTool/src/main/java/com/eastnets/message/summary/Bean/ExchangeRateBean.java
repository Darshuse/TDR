package com.eastnets.message.summary.Bean;

public class ExchangeRateBean {


	private String baseCur;
	private String curCode;
	private Double curRate;

	
	
	
	
	public String getKey(){
		return baseCur+curCode;
	}

	public String getBaseCur() {
		return baseCur;
	}
	public void setBaseCur(String baseCur) {
		this.baseCur = baseCur;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public Double getCurRate() {
		return curRate;
	}
	public void setCurRate(Double curRate) {
		this.curRate = curRate;
	}




}
