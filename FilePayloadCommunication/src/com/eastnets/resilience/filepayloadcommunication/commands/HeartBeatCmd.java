package com.eastnets.resilience.filepayloadcommunication.commands;

public final class HeartBeatCmd extends Command {

	@Override
	public CommandType getType() {
		return CommandType.COMMAND_HEART_BEAT;
	}

	@Override
	public void parse(String buffer) throws Exception {
	}

	@Override
	public String prepare() throws Exception {
		return "";
	}
}
