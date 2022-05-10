package com.eastnets.watchdog.config;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.eastnets.encdec.AESEncryptDecrypt;

@Configuration
public class DataBaseConfiguration {

	private static final Logger LOGGER = Logger.getLogger(DataBaseConfiguration.class);

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(watchdogConfiguration.getJdbcDriver());
		dataSource.setUrl(watchdogConfiguration.getDatabaseUrl());
		dataSource.setUsername(watchdogConfiguration.getDbUsername());

		String decryptedPassword = null;
		try {
			decryptedPassword = AESEncryptDecrypt.decrypt(watchdogConfiguration.getPassword());
			LOGGER.info("Database password decrypted successfully");
		} catch (Exception e) {
			// Nothing to do
			LOGGER.info("Database password already decrypted");
		}

		if (watchdogConfiguration.getDbType().equalsIgnoreCase("Oracle")) {
			dataSource.setSchema("side");
		}

		dataSource.setPassword(decryptedPassword == null ? watchdogConfiguration.getPassword() : decryptedPassword);
		return dataSource;
	}
}
