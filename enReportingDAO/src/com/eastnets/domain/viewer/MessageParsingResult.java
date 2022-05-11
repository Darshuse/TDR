package com.eastnets.domain.viewer;

public enum MessageParsingResult {

	PARSED(1), NOTDECOMPOSED(2), WRONGFIELDS(3), NOTCONFIRMATION(4), NOTCOVE(5);

	private final int messageNumber;

	MessageParsingResult(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public int getMessageNumber() {
		return messageNumber;
	}
}
