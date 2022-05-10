package com.eastnets.watchdog.config;

import org.apache.commons.lang.StringUtils;

public class MQConfiguration {

	private String serverName;
	private String channelName;
	private Integer port;

	public String getServerName() {
		return StringUtils.defaultString(serverName, "");
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getChannelName() {
		return StringUtils.defaultString(channelName, "");
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getPort() {
		if (port == null) {
			return 0;
		}
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
