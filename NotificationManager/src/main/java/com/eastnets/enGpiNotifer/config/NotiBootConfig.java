/**
 * 
 */
package com.eastnets.enGpiNotifer.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Configuration;

import com.eastnets.config.DBType;
import com.eastnets.config.PortNumberRangeException;
import com.eastnets.encdec.AESEncryptDecrypt;

 
public class NotiBootConfig { 
	private String dbConfigFilePath; 
	private long sleepPeriod = 30; 
	private Boolean enableDebugMode=false; 
	public static enum DestenationTypes {
		printer, file, email
	};

	public static enum ReportsServerCommand {
		start, stop, pause, resume, exit
	};

	
	
	private DBType databaseType = DBType.ORACLE;// Oracle, Sql
	private String serverName = "localhost";// localhost

	private String databaseName;// Oracle->ORCL, Sql -> SIDEDB
	private String portNumber;// Oracle->1521, Sql -> 1433
	private String schemaName = "SIDE";// SIDE
	private String tableSpace;// Oracle->ORCL, Sql -> SIDEDB
	private String instanceName;// Sql specific
	private String username;
	private String password;
	private Boolean partitioned = false;
	private String sAAAid="";
	private String dbType="";
	// mail config
	private String mailHost;
	private String mailPort = "25";// 25
	private String mailUsername;
	private String mailPassword;
	private String mailFrom;
	private String mailTo;
	private String mailCc;
	private String mailSubject;
	private String mailBody;
 

	@PostConstruct
	public void initConfig() {
		
	}
	
 
	public String getDatabaseName() {
		if (DBType.MSSQL == databaseType) {
			return StringUtils.defaultString(databaseName, "SIDEDB");
		}
		return StringUtils.defaultString(databaseName, "ORCL");
	}

	public DBType getDatabaseType() {
		return databaseType;
	}

 

	public String getMailBody() {
		return StringUtils.defaultString(mailBody, "");
	}

	public String getMailCc() {
		return StringUtils.defaultString(mailCc, "");
	}

	public String getMailFrom() {
		return StringUtils.defaultString(mailFrom, "enReporting3");
	}

	public String getMailHost() {
		return StringUtils.defaultString(mailHost, "");
	}

	public String getMailPassword() {
		return StringUtils.defaultString(mailPassword, "");
	}

	public String getMailPort() {
		return StringUtils.defaultString(mailPort, "25");
	}

	public String getMailSubject() {
		return StringUtils.defaultString(mailSubject, "");
	}

	public String getMailTo() {
		return StringUtils.defaultString(mailTo, "");
	}

	public String getMailUsername() {
		return StringUtils.defaultString(mailUsername, "");
	}

	 
	public Boolean getPartitioned() {
		if (partitioned == null) {
			return false;
		}
		return partitioned;
	}

	public String getPassword() {
		return StringUtils.defaultString(password, "");
	}

	public String getPortNumber() {
		if (DBType.MSSQL == databaseType) {
			return StringUtils.defaultString(portNumber, "1433");
		}
		return StringUtils.defaultString(portNumber, "1521");
	}

 

 
	public String getSchemaName() {
		return StringUtils.defaultString(schemaName, "SIDE");
	}

	public String getServerName() {
		return StringUtils.defaultString(serverName, "localhost");
	}

	public String getTableSpace() {
		if (DBType.MSSQL == databaseType) {
			return StringUtils.defaultString(tableSpace, "SIDEDB");
		}
		return StringUtils.defaultString(tableSpace, "ORCL");
	}

	public String getUsername() {
		return StringUtils.defaultString(username, "");
	}

 

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setDatabaseType(DBType databaseType) {
		this.databaseType = databaseType;
	}

 
	public void setMailBody(String body) {
		this.mailBody = body;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public void setMailPort(String mailPort) throws PortNumberRangeException {
		int portNumberParsed = NumberUtils.toInt(mailPort, -1);
		if (portNumberParsed <= 0 || portNumberParsed > 65535) {
			throw new PortNumberRangeException("Mail server port should be between 0 and 65535");
		}
		this.mailPort = mailPort;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}

 

	public void setPartitioned(Boolean partitioned) {
		this.partitioned = partitioned;
	}

	public void setPassword(String passwordEncrypted) {
		this.password = passwordEncrypted;
	}

	public void setPasswordText(String password) throws Exception {
		this.password = AESEncryptDecrypt.encrypt(password);
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = String.format("%d", portNumber);
	}

	public void setPortNumber(String portNumber) throws PortNumberRangeException {
		int portNumberParsed = NumberUtils.toInt(portNumber, -1);
		if (portNumberParsed <= 0 || portNumberParsed > 65535) {
			throw new PortNumberRangeException("Database port should be between 0 and 65535");
		}
		this.portNumber = portNumber;
	}

 
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public void setUsername(String username) {
		this.username = username;
	}

 
	public String getInstanceName() {
		return StringUtils.defaultString(instanceName, "");
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

 
	public String getsAAAid() {
		return sAAAid;
	}

	public void setsAAAid(String sAAAid) {
		this.sAAAid = sAAAid;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

 
	public String getDbConfigFilePath() {
		return dbConfigFilePath;
	}
	public void setDbConfigFilePath(String dbConfigFilePath) {
		this.dbConfigFilePath = dbConfigFilePath;
	}
	public long getSleepPeriod() {
		return sleepPeriod;
	}
	public void setSleepPeriod(long sleepPeriod) {
		this.sleepPeriod = sleepPeriod;
	}
	public Boolean getEnableDebugMode() {
		return enableDebugMode;
	}
	public void setEnableDebugMode(Boolean enableDebugMode) {
		this.enableDebugMode = enableDebugMode;
	}



}
