package com.eastnets.textbreak.bean;

import java.io.Serializable;

public class TextBreakMQBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5853486849156677415L;
	private String mesgText;

	
	public TextBreakMQBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	public TextBreakMQBean(String mesgText) {
		super();
		this.mesgText = mesgText;
	}

	public String getMesgText() {
		return mesgText;
	}

	public void setMesgText(String mesgText) {
		this.mesgText = mesgText;
	}


}
