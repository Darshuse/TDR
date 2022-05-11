package com.eastnets.textbreak.enums;

public enum ReadersBean {
	DB_READER("dbReader"),MQ_READER("mqReader");

 
	private String readerBeanName;
	
	private ReadersBean(String readerBeanName) {
		this.setReaderBeanName(readerBeanName);
		
	}

	public String getReaderBeanName() {
		return readerBeanName;
	}

	public void setReaderBeanName(String readerBeanName) {
		this.readerBeanName = readerBeanName;
	} 
	
	
}
