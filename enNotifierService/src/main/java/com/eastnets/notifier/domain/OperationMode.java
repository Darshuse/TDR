package com.eastnets.notifier.domain;

public enum OperationMode {

	INSERT(0), UPDATE(1);

	OperationMode(int mode) {
		this.mode = mode;
	}

	private int mode;

	public int getMode() {
		return mode;
	}
	
	
	

}
