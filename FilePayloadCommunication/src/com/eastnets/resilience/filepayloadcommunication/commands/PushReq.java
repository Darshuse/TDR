package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.List;

import com.eastnets.domain.filepayload.FilePayload;

public class PushReq extends PushPeekReqCmd {

	public PushReq() {
	}

	public PushReq(List<FilePayload> filesPayload, String server_path, String local_path) {
		super(filesPayload, server_path, local_path);
	}

	@Override
	public CommandType getType() {
		return CommandType.COMMAND_PUSH_REQ;
	}

}
