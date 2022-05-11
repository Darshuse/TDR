package com.eastnets.notifier.messaging;

public enum MessageStatus {

	NEW(0), SENT(1), FAIL(2), DUPLICATE(4);

	private int status;

	private MessageStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
