package com.eastnets.notifier.exception;

public class NotificationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5954575318543125740L;

	public NotificationException() {
		super();
	}

	public NotificationException(String message) {
		super(message);
	}

	public NotificationException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
