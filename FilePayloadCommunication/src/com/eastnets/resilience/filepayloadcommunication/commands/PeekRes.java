package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.Map;

public class PeekRes extends PushPeekResCmd {

	public PeekRes() {
	}

	public PeekRes(Map<String, PayloadTransferResult> payloadTransferResult) {
		super(payloadTransferResult);
	}

	@Override
	public CommandType getType() {
		return CommandType.COMMAND_PEEK_RES;
	}

}
