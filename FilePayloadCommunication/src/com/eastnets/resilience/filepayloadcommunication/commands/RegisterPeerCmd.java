package com.eastnets.resilience.filepayloadcommunication.commands;

public final class RegisterPeerCmd extends Command {

	private double protocolVersion;

	public RegisterPeerCmd() {
	}

	public RegisterPeerCmd(double protocolVersion) {
		this.setProtocolVersion(protocolVersion);
	}

	public double getProtocolVersion() {
		return protocolVersion;
	}

	@Override
	public CommandType getType() {
		return CommandType.COMMAND_REGISTER_PEER;
	}

	@Override
	public void parse(String buffer) throws Exception {
		setProtocolVersion(Double.parseDouble(buffer));
	}

	@Override
	public String prepare() throws Exception {
		return String.format("%.2f", getProtocolVersion());
	}

	public void setProtocolVersion(double protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

}
