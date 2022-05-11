package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.HashMap;
import java.util.Map;

public enum PayloadTransferResult {
	PAYLOAD_DIGEST_NOT_MATCH("DigestNotMatch"), PAYLOAD_SIZE_NOT_MATCH("SizeNotMatch"), PAYLOAD_FILE_NOT_FOUND("FileNotFound"), PAYLOAD_TRANSFER_FAILED(
			"TransferFailed"), PAYLOAD_SUCCESS("Success"), PAYLOAD_UNKNOWN("Unknown");

	private final String text;
	private static Map<String, PayloadTransferResult> resultMap;

	public static PayloadTransferResult fromString(final String text) {
		if (!PayloadTransferResult.resultMap.containsKey(text)) {
			return PAYLOAD_UNKNOWN;
		} else {
			return PayloadTransferResult.resultMap.get(text);
		}
	}

	private PayloadTransferResult(final String text) {
		this.text = text;
		registerText(text, this);
	}

	private void registerText(String text, PayloadTransferResult payloadTransferResult) {
		if (PayloadTransferResult.resultMap == null) {
			resultMap = new HashMap<String, PayloadTransferResult>();
		}
		PayloadTransferResult.resultMap.put(text, payloadTransferResult);
	}

	@Override
	public String toString() {
		return text;
	}

}
