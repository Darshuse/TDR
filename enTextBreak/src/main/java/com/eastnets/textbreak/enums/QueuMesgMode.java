package com.eastnets.textbreak.enums;

public enum QueuMesgMode {

	NORMAL("normal"),RESTORE("restore");

	private String mode;

 

	private QueuMesgMode(String mode) {
		this.mode = mode;
	}



	public String getMode() {
		return mode;
	}



	public void setMode(String mode) {
		this.mode = mode;
	}






}
