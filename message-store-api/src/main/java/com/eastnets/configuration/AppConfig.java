package com.eastnets.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource("file:${MESSAGESTORE_CONFIG_PATH}\\messageStoreAPI.properties")
public class AppConfig {

	public String debugLevel;
	
	@Autowired
	private LdSettingConfig ldSettingConfig;

	@Autowired
	private DBConfig dbConfig;

	@Autowired
	private ServerConfig serverConfig;

	public DBConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(DBConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public LdSettingConfig getLdSettingConfig() {
		return ldSettingConfig;
	}

	public void setLdSettingConfig(LdSettingConfig ldSettingConfig) {
		this.ldSettingConfig = ldSettingConfig;
	}

	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public String getDebugLevel() {
		return debugLevel;
	}

	public void setDebugLevel(String debugLevel) {
		this.debugLevel = debugLevel;
	}

}
