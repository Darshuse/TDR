package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class PushPeekResCmd extends Command {

	private Map<String, PayloadTransferResult> payloadTransferResult;

	public PushPeekResCmd() {
		this.payloadTransferResult = new HashMap<String, PayloadTransferResult>();
	}

	public PushPeekResCmd(Map<String, PayloadTransferResult> payloadTransferResult) {
		this.payloadTransferResult = payloadTransferResult;
	}

	public Map<String, PayloadTransferResult> getPayloadTransferResult() {
		return payloadTransferResult;
	}

	@Override
	public void parse(String buffer) throws Exception {
		String[] splitData = buffer.split(";;");

		int index = 0;
		payloadTransferResult.clear();
		while (index + 1 < splitData.length) {
			String fileName = showSpecialChar(splitData[index++]);
			String status = splitData[index++];
			payloadTransferResult.put(fileName, PayloadTransferResult.fromString(status));
		}
	}

	@Override
	public String prepare() throws Exception {
		if (payloadTransferResult == null || payloadTransferResult.isEmpty()) {
			throw new Exception("Invalid data for command \"" + getType().toString() + "\"");
		}

		String data = "";
		Iterator<Entry<String, PayloadTransferResult>> itr = payloadTransferResult.entrySet().iterator();

		boolean firstItem = true;

		while (itr.hasNext()) {
			Entry<String, PayloadTransferResult> entry = itr.next();
			if (!firstItem) {
				data += ";;";
			}

			data += String.format("%s;;%s", hideSpecialChar(entry.getKey()), entry.getValue());
			firstItem = false;
		}
		return data;
	}

	public void setPayloadTransferResult(Map<String, PayloadTransferResult> payloadTransferResult) {
		this.payloadTransferResult = payloadTransferResult;
	}

}
