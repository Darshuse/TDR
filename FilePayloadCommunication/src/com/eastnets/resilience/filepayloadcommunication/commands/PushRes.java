package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.Map;

public class PushRes extends PushPeekResCmd {

	public PushRes() {
	}

	public PushRes(Map<String, PayloadTransferResult> payloadTransferResult) {
		super(payloadTransferResult);
	}

	@Override
	public CommandType getType() {
		return CommandType.COMMAND_PUSH_RES;
	}

}
