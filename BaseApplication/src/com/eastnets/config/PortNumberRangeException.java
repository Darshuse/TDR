package com.eastnets.config;

public class PortNumberRangeException extends Exception {
	private static final long serialVersionUID = 6649714356543206163L;

	String message;

	public PortNumberRangeException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "EnumInvalidException: " + message;
	}
}
