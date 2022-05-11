package com.eastnets.entities;

public class Test {

	
	private String mesgType;

	private String textDataBlock;
	
	
	public Test(String mesgType,String textDataBlock) {
		super();
		this.mesgType = mesgType;
		this.textDataBlock=textDataBlock;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getTextDataBlock() {
		return textDataBlock;
	}

	public void setTextDataBlock(String textDataBlock) {
		this.textDataBlock = textDataBlock;
	}
}
