package com.eastnets.textbreak.bean;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataBaseConfig {

	private static final Logger LOGGER = Logger.getLogger(DataBaseConfig.class);

	@Autowired
	TextBreakConfig textBreakConfig;

	@Bean
	public DataSource dataSource() {
		String url = textBreakConfig.getDatabaseUrl();
		String username = textBreakConfig.getDbUsername();
		String password = textBreakConfig.getPassword();
		String decryptedPassword = null;
		try {
			decryptedPassword = AESEncryptDecrypt.decrypt(password);
		} catch (Exception e) {
			LOGGER.info("Database password already decrypted");
		}

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);

		hikariConfig.setPassword(decryptedPassword == null ? password : decryptedPassword);

		if (textBreakConfig.getDbType().equalsIgnoreCase("Oracle")) {
			hikariConfig.setSchema("side");
		}
		hikariConfig.setAutoCommit(textBreakConfig.isAutoCommit());
		hikariConfig.setConnectionTimeout(textBreakConfig.getConnectionTimeout());
		hikariConfig.setIdleTimeout(textBreakConfig.getIdleTimeout());
		hikariConfig.setLeakDetectionThreshold(textBreakConfig.getLeakDetectionThreshold());
		hikariConfig.setMaxLifetime(textBreakConfig.getMaxLifetime());
		hikariConfig.setMaximumPoolSize(textBreakConfig.getMaxPoolSize());
		hikariConfig.setValidationTimeout(textBreakConfig.getValidationTimeout());
		hikariConfig.setMinimumIdle(textBreakConfig.getMinIdle());
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);

		return dataSource;
	}
}
