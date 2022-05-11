package com.eastnets.textbreak.enums;

public enum DecomposeStatus {

	NEW_MESSAGE(0), DECOMPOSED(1), PARSING_FAILED(2), INSERTION_FAILED(3);

	private Integer status;

	private DecomposeStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
