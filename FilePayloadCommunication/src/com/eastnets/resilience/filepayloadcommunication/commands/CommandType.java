package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.HashMap;
import java.util.Map;

public enum CommandType {
	COMMAND_PUSH_REQ("PUSH_REQ"), COMMAND_PUSH_RES("PUSH_RES"), COMMAND_PEEK_REQ("PEEK_REQ"), COMMAND_PEEK_RES("PEEK_RES"), COMMAND_REGISTER_PEER(
			"REGISTER_PEER"), COMMAND_HEART_BEAT("HEART_BEAT"), COMMAND_UNKNOWN("UNKNOWN");

	private final String text;

	private static Map<String, CommandType> commandTypeMap;

	public static CommandType fromString(String commandText) {
		if (!commandTypeMap.containsKey(commandText)) {
			return COMMAND_UNKNOWN;
		}
		return commandTypeMap.get(commandText);
	}

	private CommandType(final String text) {
		this.text = text;
		registerText(text, this);
	}

	private void registerText(String text, CommandType payloadTransferResult) {
		if (commandTypeMap == null) {
			commandTypeMap = new HashMap<String, CommandType>();
		}
		commandTypeMap.put(text, payloadTransferResult);
	}

	@Override
	public String toString() {
		return text;
	}
}
