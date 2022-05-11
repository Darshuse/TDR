package com.eastnets.resilience.filepayloadcommunication.commands;

import java.util.ArrayList;
import java.util.List;

import com.eastnets.domain.filepayload.FilePayload;

public abstract class PushPeekReqCmd extends Command {

	private List<FilePayload> filesPayload;
	private String serverPath;
	private String localPath;

	public PushPeekReqCmd() {
		this.filesPayload = new ArrayList<FilePayload>();
		this.serverPath = "";
		this.localPath = "";
	}

	public PushPeekReqCmd(List<FilePayload> filesPayload, String server_path, String local_path) {
		this.filesPayload = filesPayload;
		this.serverPath = server_path;
		this.localPath = local_path;
	}

	public List<FilePayload> getFilesPayload() {
		return filesPayload;
	}

	public String getLocalPath() {
		return localPath;
	}

	public String getServerPath() {
		return serverPath;
	}

	@Override
	public void parse(String buffer) throws Exception {
		String[] splitData = buffer.split(";;");
		if (splitData.length < 2 || splitData.length % 4 != 2) {
			throw new Exception("Invalid recived data " + splitData.toString());
		}
		serverPath = showSpecialChar(splitData[0]);
		localPath = showSpecialChar(splitData[1]);
		int index = 2;
		filesPayload.clear();
		while (index < splitData.length) {
			FilePayload payload = new FilePayload();
			payload.setFileName(splitData[index++]);
			payload.setFileDigest(splitData[index++]);
			payload.setFileDigestAlgo(splitData[index++]);
			payload.setFileSize(Integer.parseInt(splitData[index++]));
			filesPayload.add(payload);
		}
	}

	@Override
	public String prepare() throws Exception {
		if (filesPayload == null || serverPath == null || localPath == null || filesPayload.isEmpty()) {
			throw new Exception("Invalid data for command \"" + getType().toString() + "\"");
		}
		// data is arranged as
		// server_path;;local_path;;FileName;;FileDigest;;FileDigestAlgo;;FileSize;;FileName;;FileDigest;;FileDigestAlgo;;FileSize....
		// we don't need the but those info about the payload for the server
		// application as it has no database connection
		String data = hideSpecialChar(serverPath) + ";;" + hideSpecialChar(localPath) + "";
		for (FilePayload payload : getFilesPayload()) {
			data += String.format(";;%s;;%s;;%s;;%d", hideSpecialChar(payload.getFileName()), hideSpecialChar(payload.getFileDigest()),
					hideSpecialChar(payload.getFileDigestAlgo()), payload.getFileSize());
		}
		return data;
	}

	public void setFilesPayload(List<FilePayload> filesPayload) {
		this.filesPayload = filesPayload;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
}
