package com.eastnets.notifier.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.eastnets.encdec.AESEncryptDecrypt;

@Configuration
@ComponentScan("com.eastnets.notifier.domain")
public class DataSourceConfiguration {

	@Autowired
	private ProjectProperties projectProperties;

	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
		if (projectProperties.getDbType().equals("Oracle")) {
			managerDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			managerDataSource.setUrl("jdbc:oracle:thin:@" + projectProperties.getServerName() + ':' + projectProperties.getPortNumber() + '/' + projectProperties.getDbServiceName());
		} else if (projectProperties.getDbType().equals("Sql")) {
			managerDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			managerDataSource
					.setUrl("jdbc:sqlserver://" + projectProperties.getServerName() + ':' + projectProperties.getPortNumber() + ";+databaseName=" + projectProperties.getDatabaseName() + ";instanceName=" + projectProperties.getInstanceName());
		}
		System.out.println(projectProperties.getServerName());
		System.out.println(projectProperties.getPortNumber());
		System.out.println(projectProperties.getDbServiceName());
		System.out.println("jdbc:oracle:thin:@" + projectProperties.getServerName() + ':' + projectProperties.getPortNumber() + '/' + projectProperties.getDbServiceName());
		String decryptedPassword = null;
		try {
			decryptedPassword = AESEncryptDecrypt.decrypt(projectProperties.getPassword());
		} catch (Exception e) {
			// Nothing to do
			System.out.println("password already decrypted ");
		}
		managerDataSource.setUsername(projectProperties.getDbUserName());
		managerDataSource.setPassword(decryptedPassword == null ? projectProperties.getPassword() : decryptedPassword);
		return managerDataSource;
	}

	@Bean
	public Map<String, List<String>> notifiedEventsMap() {
		return new HashMap<String, List<String>>();
	}

}
