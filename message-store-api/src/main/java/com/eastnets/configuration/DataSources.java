package com.eastnets.configuration;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.util.DBUtil;

@Configuration
public class DataSources {

	private static final Logger LOGGER = LogManager.getLogger(DataSources.class);

	@Autowired
	private AppConfig appConfig;

	@Bean
	public DataSource getDbDatasource() {
		String password = appConfig.getDbConfig().getPassword();
		try {
			password = AESEncryptDecrypt.decrypt(password);
		} catch (Exception e) {
			LOGGER.debug("password already decrypted ");
		}

		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(DBProvider.valueOf(appConfig.getDbConfig().getDbType()).getDriver());
		dataSourceBuilder.url(DBUtil.getDatabaseURL(appConfig.getDbConfig()));
		dataSourceBuilder.username(appConfig.getDbConfig().getDbUserName());
		dataSourceBuilder.password(password);

		return dataSourceBuilder.build();
	}
}
