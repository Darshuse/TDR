/**
 * 
 */
package com.eastnets.extraction.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.extraction.service.helper.EnEcfParser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Validated
@Configuration
public class DataSourceConfig {

	@Autowired
	private YAMLConfig myConfig;
	//

	@Value("${serverName}")
	private String serverName;
	//

	@Value("${portNumber}")
	private Integer port;
	//

	@Value("${dbType}")
	private String dbType;
	@Value("${dbServiceName:}")
	private String serviceName;
	@Value("${instanceName:}")
	private String instanceName;
	@Value("${databaseName}")
	private String dbName;
	//

	@Value("${dbUsername}")
	private String dbUsername;
	//

	@Value("${dbPassword}")
	private String password;
	@Value("${ecfPath}")
	private String ecf;
	@Value("${enableTnsName:false}")
	private boolean enableTnsName;
	@Value("${tnsPath}")
	private String tnsPath;

	@Value("${partitioned:false}")
	private boolean partitioned;

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

	// @Autowired
	// private SearchParam searchParam;

	@Bean
	public DataSource getDataSource() {

		String decryptedPassword = null;

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(getDatabaseUrl());
		hikariConfig.setUsername(getDbUsername());

		try {
			if (password.length() != 0) {
				decryptedPassword = AESEncryptDecrypt.decrypt(password);
			}
		} catch (Exception e) {
			LOGGER.info("Database password already decrypted");
		}

		hikariConfig.setPassword(decryptedPassword == null ? password : decryptedPassword);

		if (getDbType().equalsIgnoreCase("Oracle")) {
			hikariConfig.setSchema("side");
		}
		hikariConfig.setAutoCommit(true);
		hikariConfig.setConnectionTimeout(3000);
		hikariConfig.setIdleTimeout(600000);
		hikariConfig.setLeakDetectionThreshold(0);
		hikariConfig.setMaxLifetime(1800000);
		hikariConfig.setMaximumPoolSize(20);
		hikariConfig.setValidationTimeout(15000);
		hikariConfig.setMinimumIdle(2);
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}

	public YAMLConfig getMyConfig() {
		return myConfig;
	}

	public void setMyConfig(YAMLConfig myConfig) {
		this.myConfig = myConfig;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEcf() {
		return ecf;
	}

	public void setEcf(String ecf) {
		this.ecf = ecf;
	}

	public boolean isEnableTnsName() {
		return enableTnsName;
	}

	public void setEnableTnsName(boolean enableTnsName) {
		this.enableTnsName = enableTnsName;
	}

	public String getTnsPath() {
		return tnsPath;
	}

	public void setTnsPath(String tnsPath) {
		this.tnsPath = tnsPath;
	}

	public boolean isPartitioned() {
		return partitioned;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	public String getDatabaseUrl() {

		if (ecf != null && !ecf.isEmpty()) {
			fillECFInfo();
		}

		if (ecf == null || ecf.isEmpty()) {
			try {

				if ((serverName == null || serverName.isEmpty()) || (port == null) || (dbType == null || dbType.isEmpty()) || (dbUsername == null || dbUsername.isEmpty()) || (password == null || password.isEmpty())) {
					throw new Exception();
				}
			} catch (Exception e) {
				LOGGER.error("Failed to bind properties under '' to com.eastnets.extraction.config.YAMLConfig$$EnhancerBySpringCGLIB$$82e55a62 failed:\r\n" + "\r\n" + "    Property: serverName\r\n" + " ,   portNumber  \r\n" + "  , dbType  \r\n"
						+ "  dbUsername  \r\n" + "\r\n" + " and  password  \r\n" + "is Requird, so Check their values in application.yml . ");
				e.printStackTrace();
			}

		}
		if (dbType.equalsIgnoreCase("mssql")) {
			return "jdbc:sqlserver://" + serverName + ":" + port + ";databaseName=" + dbName + ";instanceName=" + instanceName + ";sslProtocol=TLSv1.2";

		}
		if (enableTnsName) {
			System.setProperty("oracle.net.tns_admin", tnsPath);
			return "jdbc:oracle:thin:@" + serverName;
		}

		if (serviceName == null || serviceName.isEmpty()) {
			return "jdbc:oracle:thin:@" + serverName + ":" + port + ":" + dbName;
		} else {
			return "jdbc:oracle:thin:@" + serverName + ":" + port + "/" + serviceName;
		}

	}

	private void fillECFInfo() {
		// TODO Auto-generated method stub
		ConnectionSettings cs;
		try {
			cs = EnEcfParser.parseECF(ecf);
			if (cs.getServerName() != null) {
				setServerName(cs.getServerName());
			}
			if (cs.getUserName() != null) {
				setDbUsername(cs.getUserName());
			}
			if (cs.getPassword() != null) {
				setPassword(cs.getPassword());
			}
			if (cs.getPortNumber() != null) {
				setPort(cs.getPortNumber());
			}
			if (cs.getServiceName() != null) {
				setServiceName(cs.getServiceName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

	}

	public String getJdbcDriver() {
		if (dbType.equalsIgnoreCase("mssql")) {
			return DatabaseDriver.MSSQL_DRIVER.getDriver();
		}
		return DatabaseDriver.ORACLE_DRIVER.getDriver();
	}

}