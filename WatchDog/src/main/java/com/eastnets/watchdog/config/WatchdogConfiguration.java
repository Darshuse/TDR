package com.eastnets.watchdog.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.eastnets.encdec.ConnectionSettings;
import com.eastnets.encdec.EnEcfParser;
import com.eastnets.watchdogEnum.WatchDogMode;

@Component
@ConfigurationProperties
@PropertySource(value = { "file:${EN_REPORTING_CONFIG_HOME}/WatchDog.properties" })
public class WatchdogConfiguration {

	@Value("${aid:}")
	private Integer aid;
	@Value("${mode:}")
	private char mode;
	@Value("${includeText:false}")
	private Boolean includeText = false;
	@Value("${delay:5}")
	private Integer delay = 5;
	@Value("${maxThreadCount:1}")
	private Integer maxThreadCount = 1;
	@Value("${debug:false}")
	private Boolean debug = false;
	@Value("${readerImpl:DBReader}")
	private String readerImpl = "DBReader";
	@Value("${bulkSize:200}")
	private Integer bulkSize = 200;
	// DB CONFIG
	@Value("${serverName:}")
	private String serverName;
	@Value("${port:}")
	private Integer port;
	@Value("${dbType:}")
	private String dbType;
	@Value("${serviceName:}")
	private String serviceName;
	@Value("${instanceName:}")
	private String instanceName;
	@Value("${dbName:}")
	private String dbName;
	@Value("${dbUsername:}")
	private String dbUsername;
	@Value("${password:}")
	private String password;
	@Value("${ecf:}")
	private String ecf;

	// MAIL CONFIG
	@Value("${mailServer:}")
	private String mailServer;
	@Value("${mailPort:0}")
	private Integer mailPort;
	@Value("${mailFrom:0}")
	private String mailFrom;
	@Value("${mailTo:0}")
	private String mailTo;

	@Value("${mailUsername:}")
	private String mailUsername;
	@Value("${mailPassword:}")
	private String mailPassword;
	@Value("${mailSubject:Watchdog Notification}")
	private String mailSubject = "Watchdog Notification";

	private String dbUrl;
	private DBProvider dbProvider;
	@Value("${enableTnsName:false}")
	private boolean enableTnsName;
	@Value("${tnsPath:}")
	private String tnsPath;
	@Value("${mailAuthenticationRequired:true}")
	private boolean mailAuthenticationRequired;

	@Value("${includeSwiftText:false}")
	private boolean includeSwiftText;

	@Value("${enableExpandText:false}")
	private boolean enableExpandText;

	@Value("${partitioned:false}")
	private boolean partitioned;

	@Value("${multiAlliance:false}")
	private boolean multiAlliance;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public boolean isMultiAlliance() {
		return multiAlliance;
	}

	public void setMultiAlliance(boolean multiAlliance) {
		this.multiAlliance = multiAlliance;
	}

	public boolean isPartitioned() {
		return partitioned;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	public boolean isEnableExpandText() {
		return enableExpandText;
	}

	public void setEnableExpandText(boolean enableExpandText) {
		this.enableExpandText = enableExpandText;
	}

	public String getMailUsername() {
		return mailUsername;
	}

	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}

	public boolean isEnableTnsName() {
		return enableTnsName;
	}

	public boolean isMailAuthenticationRequired() {
		return mailAuthenticationRequired;
	}

	public void setMailAuthenticationRequired(boolean mailAuthenticationRequired) {
		this.mailAuthenticationRequired = mailAuthenticationRequired;
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

	public char getMode() {
		return mode;
	}

	public void setMode(char mode) {
		this.mode = mode;
	}

	public Boolean getIncludeText() {
		return includeText;
	}

	public void setIncludeText(Boolean includeText) {
		this.includeText = includeText;
	}

	public Integer getDelay() {
		if (delay == null)
			return 10;
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getMaxThreadCount() {
		if (maxThreadCount == null)
			return 1;
		return maxThreadCount;
	}

	public void setMaxThreadCount(Integer maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public String getReaderImpl() {
		return readerImpl;
	}

	public void setReaderImpl(String readerImpl) {
		this.readerImpl = readerImpl;
	}

	public Integer getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(Integer bulkSize) {
		this.bulkSize = bulkSize;
	}

	public String getServerName() {
		return StringUtils.defaultString(serverName, "");
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getMailPort() {
		if (mailPort == null) {
			return 0;
		}

		return mailPort;
	}

	public void setMailPort(Integer mailPort) {
		this.mailPort = mailPort;
	}

	public String getDbType() {
		return StringUtils.defaultString(dbType, "");
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getServiceName() {
		return StringUtils.defaultString(serviceName, "");
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInstanceName() {
		return StringUtils.defaultString(instanceName, "");
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public WatchDogMode getWatchDogMode() {
		if (mode == 'M') {
			return WatchDogMode.MESSAGE_REQUEST;
		} else if (mode == 'N') {
			return WatchDogMode.EMAIL_REQUEST;
		} else if (mode == 'E') {
			return WatchDogMode.EVENT_REQUEST;
		}

		return WatchDogMode.ALL_MODE;
	}

	public String getMailPassword() {
		return StringUtils.defaultString(mailPassword, "");
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getDbName() {
		return StringUtils.defaultString(dbName, "");
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public boolean isIncludeSwiftText() {
		return includeSwiftText;
	}

	public void setIncludeSwiftText(boolean includeSwiftText) {
		this.includeSwiftText = includeSwiftText;
	}

	public String getEcf() {
		return StringUtils.defaultString(ecf, "");
	}

	public void setEcf(String ecf) {
		this.ecf = ecf;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public DBProvider getDbProvider() {
		return dbProvider;
	}

	public void setDbProvider(DBProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

	public String getMailServer() {
		return StringUtils.defaultString(mailServer, "");
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public Integer getPort() {
		if (port == null)
			return 0;
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getMailFrom() {
		return StringUtils.defaultString(mailFrom, "");

	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return StringUtils.defaultString(mailTo, "");

	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getPassword() {
		return StringUtils.defaultString(password, "");

	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getJdbcDriver() {
		if (dbType.equalsIgnoreCase("mssql")) {
			return DatabaseDriver.MSSQL_DRIVER.getDriver();
		}
		return DatabaseDriver.ORACLE_DRIVER.getDriver();
	}

	public String getDatabasePlatform() {
		if (dbType.equalsIgnoreCase("mssql")) {
			return DatabaseDriver.MSSQL_DRIVER.getPlatform();
		}
		return DatabaseDriver.ORACLE_DRIVER.getPlatform();
	}

	public String getDatabaseUrl() {

		if (ecf != null && !ecf.isEmpty()) {
			fillECFInfo();
		}

		if (dbType.equalsIgnoreCase("mssql")) {
			return "jdbc:sqlserver://" + serverName + ":" + port + ";databaseName=" + dbName + ";instanceName=" + instanceName;

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
				setServerName(cs.getServiceName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
