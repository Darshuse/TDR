
package com.eastnets.textbreak.bean;

import java.sql.Connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.eastnets.textbreak.encdec.ConnectionSettings;
import com.eastnets.textbreak.encdec.EnEcfParser;
import com.eastnets.textbreak.enums.DatabaseDriver;
import com.eastnets.textbreak.enums.ReadersBean;

@Component
@ConfigurationProperties
@PropertySource(value = { "classpath:TextBreak.properties", "file:${EN_REPORTING_CONFIG_HOME}/TextBreak.properties" }, ignoreResourceNotFound = true)
public class TextBreakConfig {
	@Value("${aid:}")
	private String aid;
	@Value("${serverName:localhost}")
	private String serverName;
	@Value("${databaseName:}")
	private String databaseName;
	@Value("${portNumber:}")
	private String portNumber;
	@Value("${schemaName:SIDE}")
	private String schemaName;
	@Value("${tableSpace:}")
	private String tableSpace;
	@Value("${instanceName:}")
	private String instanceName;
	@Value("${dbUsername:}")
	private String dbUsername;
	@Value("${dbPassword:}")
	private String password;
	@Value("${partitioned:false}")
	private Boolean partitioned;
	@Value("${dbType:}")
	private String dbType;
	@Value("${dbServiceName:}")
	private String dbServiceName;
	@Value("${messageNumber:100}")
	private String messageNumber;
	@Value("${dayNumber:}")
	private String dayNumber;
	@Value("${fromDate:}")
	private String fromDate;
	@Value("${toDate:2099/12/31 23:59:59}")
	private String toDate;
	@Value("${allMessages:false}")
	private String allMessages;
	@Value("${apacheMqServerIp:}")
	private String apacheMqServerIp;
	@Value("${apacheMqServerPort:}")
	private String apacheMqServerPort;
	@Value("${apacheMqQueueName:}")
	private String apacheMqQueueName;
	@Value("${enableDebug:false}")
	private boolean enableDebug;
	@Value("${enableTnsName:false}")
	private boolean enableTnsName;
	@Value("${tnsPath:}")
	private String tnsPath;
	@Value("${recovery:false}")
	private boolean recovery;
	private String username;
	@Value("${ecfPath:}")
	private String ecfPath;
	@Value("${decomposedOnly:false}")
	private boolean decomposedOnly;
	// Hikari config
	@Value("${connection-timeout:300000}")
	private volatile long connectionTimeout;
	@Value("${validation-timeout:15000}")
	private volatile long validationTimeout;
	@Value("${idle-timeout:600000}")
	private volatile long idleTimeout;
	@Value("${leak-detection-threshold:0}")
	private volatile long leakDetectionThreshold;
	@Value("${max-lifetime:1800000}")
	private volatile long maxLifetime;
	@Value("${maximum-pool-size:20}")
	private volatile int maxPoolSize;
	@Value("${minimum-idle:2}")
	private volatile int minIdle;
	@Value("${initializationFailTimeout:0}")
	private long initializationFailTimeout;
	@Value("${auto-commit:true}")
	private boolean isAutoCommit;
	@Value("${onlinedecompos:false}")
	private boolean onlinedecompos;
	@Value("${delay:5}")
	private Integer delay = 5;
	@Value("${removeFromRtext:false}")
	private boolean removeFromRtext;
	@Value("${checkTextIntegrity:false}")
	private boolean checkTextIntegrity;
	@Value("${excludeSystemMessages:false}")
	private boolean excludeSystemMessages;
	private Connection expandConnConfig;

	public Connection getExpandConnConfig() {
		return expandConnConfig;
	}

	public boolean isCheckTextIntegrity() {
		return checkTextIntegrity;
	}

	public void setCheckTextIntegrity(boolean checkTextIntegrity) {
		this.checkTextIntegrity = checkTextIntegrity;
	}

	public void setExpandConnConfig(Connection expandConnConfig) {
		this.expandConnConfig = expandConnConfig;
	}

	public boolean isOnlinedecompos() {
		return onlinedecompos;
	}

	public void setOnlinedecompos(boolean onlinedecompos) {
		this.onlinedecompos = onlinedecompos;
	}

	public boolean isRemoveFromRtext() {
		return removeFromRtext;
	}

	public void setRemoveFromRtext(boolean removeFromRtext) {
		this.removeFromRtext = removeFromRtext;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getDayNumber() {
		return StringUtils.defaultString(dayNumber, "");
	}

	public String getEcfPath() {
		return ecfPath;
	}

	public void setEcfPath(String ecfPath) {
		this.ecfPath = ecfPath;
	}

	public void setDayNumber(String dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getFromDate() {
		return StringUtils.defaultString(fromDate, "");
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return StringUtils.defaultString(toDate, "");
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getAllMessages() {
		return StringUtils.defaultString(allMessages, "");
	}

	public void setAllMessages(String allMessages) {
		this.allMessages = allMessages;
	}

	public String getApacheMqServerIp() {
		return StringUtils.defaultString(apacheMqServerIp, "");
	}

	public void setApacheMqServerIp(String apacheMqServerIp) {
		this.apacheMqServerIp = apacheMqServerIp;
	}

	public String getApacheMqServerPort() {
		return StringUtils.defaultString(apacheMqServerPort, "");
	}

	public void setApacheMqServerPort(String apacheMqServerPort) {
		this.apacheMqServerPort = apacheMqServerPort;
	}

	public String getApacheMqQueueName() {
		return apacheMqQueueName;
	}

	public void setApacheMqQueueName(String apacheMqQueueName) {
		this.apacheMqQueueName = apacheMqQueueName;
	}

	public String getDbServiceName() {
		return StringUtils.defaultString(dbServiceName, "");
	}

	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getSchemaName() {
		return StringUtils.defaultString(schemaName, "SIDE");
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableSpace() {
		if (dbType.equalsIgnoreCase("mssql")) {
			return StringUtils.defaultString(tableSpace, "SIDEDB");
		}
		return StringUtils.defaultString(tableSpace, "ORCL");
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public String getInstanceName() {
		return StringUtils.defaultString(instanceName, "");
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getPartitioned() {
		if (partitioned == null) {
			return false;
		}
		return partitioned;
	}

	public void setPartitioned(Boolean partitioned) {
		this.partitioned = partitioned;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public boolean isEnableDebug() {
		return enableDebug;
	}

	public boolean isDecomposedOnly() {
		return decomposedOnly;
	}

	public void setDecomposedOnly(boolean decomposedOnly) {
		this.decomposedOnly = decomposedOnly;
	}

	public void setEnableDebug(boolean enableDebug) {
		this.enableDebug = enableDebug;
	}
	/*
	 * public String getTextBreakReader() { if(apacheMqServerIp == null || apacheMqServerIp.isEmpty() ){ return "dbReader"; } return "mqReader"; } public void setTextBreakReader(String
	 * textBreakReader) { this.textBreakReader = textBreakReader; }
	 */

	public String getTextBreakReaderBeanName() {
		if (apacheMqServerIp == null || apacheMqServerIp.isEmpty()) {
			return ReadersBean.DB_READER.getReaderBeanName();
		}
		return ReadersBean.MQ_READER.getReaderBeanName();
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

		if (ecfPath != null && !ecfPath.isEmpty()) {
			fillECFInfo();
		}

		if (dbType.equalsIgnoreCase("mssql")) {
			return "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";instanceName=" + instanceName;

		}
		if (enableTnsName) {
			System.setProperty("oracle.net.tns_admin", tnsPath);
			return "jdbc:oracle:thin:@" + dbServiceName;
		}
		if (dbServiceName == null || dbServiceName.isEmpty()) {
			return "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + databaseName;
		} else {
			return "jdbc:oracle:thin:@" + serverName + ":" + portNumber + "/" + dbServiceName;
		}

	}

	private void fillECFInfo() {
		// TODO Auto-generated method stub
		ConnectionSettings cs;
		try {
			cs = EnEcfParser.parseECF(ecfPath);
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
				setPortNumber(cs.getPortNumber().toString());
			}
			if (cs.getServiceName() != null) {
				setDbServiceName(cs.getServiceName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isRecovery() {
		return recovery;
	}

	public void setRecovery(boolean recovery) {
		this.recovery = recovery;
	}

	public void loggerMode() {
		if (isEnableDebug())
			LogManager.getRootLogger().setLevel(Level.DEBUG);
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public long getValidationTimeout() {
		return validationTimeout;
	}

	public void setValidationTimeout(long validationTimeout) {
		this.validationTimeout = validationTimeout;
	}

	public long getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public long getLeakDetectionThreshold() {
		return leakDetectionThreshold;
	}

	public void setLeakDetectionThreshold(long leakDetectionThreshold) {
		this.leakDetectionThreshold = leakDetectionThreshold;
	}

	public long getMaxLifetime() {
		return maxLifetime;
	}

	public void setMaxLifetime(long maxLifetime) {
		this.maxLifetime = maxLifetime;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getInitializationFailTimeout() {
		return initializationFailTimeout;
	}

	public void setInitializationFailTimeout(long initializationFailTimeout) {
		this.initializationFailTimeout = initializationFailTimeout;
	}

	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	public boolean isExcludeSystemMessages() {
		return excludeSystemMessages;
	}

	public void setExcludeSystemMessages(boolean excludeSystemMessages) {
		this.excludeSystemMessages = excludeSystemMessages;
	}

}
