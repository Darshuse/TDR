package com.eastnets.domain.filepayload;

import java.io.Serializable;

public class FilePayloadSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8711192776246725443L;
	private String serverAddress;
	private String remotePath;
	private String localPath;
	private int serverPort;
	private boolean standalone;
	
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public boolean isStandalone() {
		return standalone;
	}
	public void setStandalone(boolean standalone) {
		this.standalone = standalone;
	}
}
