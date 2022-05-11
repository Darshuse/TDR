package com.eastnets.config;

public class EnumInvalidException extends Exception {
	private static final long serialVersionUID = -3795302149511916348L;

	String message;

	public EnumInvalidException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "EnumInvalidException: " + message;
	}
}
