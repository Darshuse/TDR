package com.eastnets.message.summary.configuration;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.eastnets.encdec.AESEncryptDecrypt;

@Configuration
public class DataBaseConfiguration {

	private static final Logger LOGGER = LogManager.getLogger(DataBaseConfiguration.class);

	@Autowired
	GlobalConfiguration globalConfiguration;

	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
		if (globalConfiguration.getDbType().trim().equalsIgnoreCase("Oracle") || globalConfiguration.getDbType().trim().equalsIgnoreCase("ORCL")) {
			managerDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			if (globalConfiguration.isEnableTnsName()) {
				System.setProperty("oracle.net.tns_admin", globalConfiguration.getTnsPath());
				managerDataSource.setUrl("jdbc:oracle:thin:@" + globalConfiguration.getDbServiceName());
			} else {
				managerDataSource.setUrl("jdbc:oracle:thin:@" + globalConfiguration.getServerName() + ':' + globalConfiguration.getPortNumber() + '/' + globalConfiguration.getDbServiceName());
			}

		} else if (globalConfiguration.getDbType().trim().equalsIgnoreCase("Sql") || globalConfiguration.getDbType().trim().equalsIgnoreCase("MSSQL")) {
			managerDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			managerDataSource.setUrl(
					"jdbc:sqlserver://" + globalConfiguration.getServerName() + ':' + globalConfiguration.getPortNumber() + ";databaseName=" + globalConfiguration.getDatabaseName() + ";instanceName=" + globalConfiguration.getDbInstanceName());
		}
		String decryptedPassword = null;
		try {
			decryptedPassword = AESEncryptDecrypt.decrypt(globalConfiguration.getPassword());
			LOGGER.debug("Database password decrypted successfully");
		} catch (Exception e) {
			// Nothing to do
			LOGGER.debug("Database password already decrypted");
		}

		managerDataSource.setUsername(globalConfiguration.getDbUserName());
		managerDataSource.setPassword(decryptedPassword == null ? globalConfiguration.getPassword() : decryptedPassword);
		return managerDataSource;
	}
}
