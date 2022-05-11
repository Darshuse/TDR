package com.eastnets.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class ServerConfig {

	private int port;
	private String sslkeyPassword;
	private String sslkeyStore;
	private String sslkeyAlias;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSslkeyPassword() {
		return sslkeyPassword;
	}

	public void setSslkeyPassword(String sslkeyPassword) {
		this.sslkeyPassword = sslkeyPassword;
	}

	public String getSslkeyStore() {
		return sslkeyStore;
	}

	public void setSslkeyStore(String sslkeyStore) {
		this.sslkeyStore = sslkeyStore;
	}

	public String getSslkeyAlias() {
		return sslkeyAlias;
	}

	public void setSslkeyAlias(String sslkeyAlias) {
		this.sslkeyAlias = sslkeyAlias;
	}

}
