package com.eastnets.config;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.eastnets.encdec.AESEncryptDecrypt;

@Service("configBean")
public class ConfigBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1057037807381088905L;

	public static enum DestenationTypes {
		printer, file, email, custom
	};

	public static enum ReportsServerCommand {
		start, stop, pause, resume, exit
	};

	// Database settings
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
	private String sAAAid = "";
	private String dbType = "";
	private Boolean tnsEnabled = false;
	private String tnsPath = "";
	private String reportLogoPath;

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
	private String ecf;
	private Boolean mailAuthenticationRequired = true;
	private String showLog = "ON";

	// LDAP settings
	private AuthMethod authMethod = AuthMethod.DB;// the method of
													// authentication used to
													// authenticate the user DB
													// or LDAP
	private String ldapURL;
	private String ldapBase;
	private String ldapUserDn;
	private String ldapPassword;
	private Integer ldapNumberOfTries;
	private String ldapATTR_USERNAME;

	// Other
	private Boolean viewerProcedureDebug = true;// true or false
	private String reportsDirectoryPath;

	private Integer reportMaxFetchSize = 1000;
	private Integer monitoringDayHistory = 100;
	private Boolean monitoringDisplayJournals = true;
	private Boolean monitoringDisplayWarnings = true;
	private String dbServiceName;// oracle db service used with RAC
	private int maxAttachSize = 2;

	private Boolean clusteringEnabled;
	private String clustername;

	public AuthMethod getAuthMethod() {
		return authMethod;
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

	public String getLdapATTR_USERNAME() {
		return StringUtils.defaultString(ldapATTR_USERNAME, "");
	}

	public String getLdapBase() {
		return StringUtils.defaultString(ldapBase, "");
	}

	public String getLdapPassword() {
		return StringUtils.defaultString(ldapPassword, "");
	}

	public String getLdapURL() {
		return StringUtils.defaultString(ldapURL, "");
	}

	public String getLdapUserDn() {
		return StringUtils.defaultString(ldapUserDn, "");
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

	public Integer getMonitoringDayHistory() {
		if (monitoringDayHistory == null)
			return 100;
		return monitoringDayHistory;
	}

	public Boolean getMonitoringDisplayJournals() {
		if (monitoringDayHistory == null)
			return true;
		return monitoringDisplayJournals;
	}

	public Boolean getMonitoringDisplayWarnings() {
		if (monitoringDisplayWarnings == null)
			return true;
		return monitoringDisplayWarnings;
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

	public Integer getReportMaxFetchSize() {
		if (reportMaxFetchSize == null)
			return 1000;
		return reportMaxFetchSize;
	}

	public String getReportsDirectoryPath() {
		return StringUtils.defaultString(reportsDirectoryPath, "");
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

	public Boolean getViewerProcedureDebug() {
		if (viewerProcedureDebug == null) {
			return true;
		}
		return viewerProcedureDebug;
	}

	public void setAuthMethod(AuthMethod authMethod) {
		this.authMethod = authMethod;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setDatabaseType(DBType databaseType) {
		this.databaseType = databaseType;
	}

	public void setLdapATTR_USERNAME(String ldapATTR_USERNAME) {
		this.ldapATTR_USERNAME = ldapATTR_USERNAME;
	}

	public void setLdapBase(String ldapBase) {
		this.ldapBase = ldapBase;
	}

	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	public void setLdapURL(String ldapURL) {
		this.ldapURL = ldapURL;
	}

	public void setLdapUserDn(String ldapUserDn) {
		this.ldapUserDn = ldapUserDn;
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

	public Boolean getMailAuthenticationRequired() {
		return mailAuthenticationRequired;
	}

	public void setMailAuthenticationRequired(Boolean mailAuthenticationRequired) {
		this.mailAuthenticationRequired = mailAuthenticationRequired;
	}

	public void setMonitoringDayHistory(Integer monitoringDayHistory) {
		this.monitoringDayHistory = monitoringDayHistory;
	}

	public void setMonitoringDisplayJournals(Boolean monitoringDisplayJournals) {
		this.monitoringDisplayJournals = monitoringDisplayJournals;
	}

	public void setMonitoringDisplayWarnings(Boolean monitoringDisplayWarnings) {
		this.monitoringDisplayWarnings = monitoringDisplayWarnings;
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

	public void setReportMaxFetchSize(Integer reportMaxFetchSize) {
		this.reportMaxFetchSize = reportMaxFetchSize;
	}

	public void setReportsDirectoryPath(String reportsDirectoryPath) {
		this.reportsDirectoryPath = reportsDirectoryPath;
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

	public void setViewerProcedureDebug(Boolean viewerProcedureDebug) {
		this.viewerProcedureDebug = viewerProcedureDebug;
	}

	public String getInstanceName() {
		return StringUtils.defaultString(instanceName, "");
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getDbServiceName() {
		return StringUtils.defaultString(dbServiceName, "");
	}

	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	public int getMaxAttachSize() {
		return maxAttachSize;
	}

	public void setMaxAttachSize(int maxAttachSize) {
		this.maxAttachSize = maxAttachSize;
	}

	public Boolean getTnsEnabled() {
		return tnsEnabled;
	}

	public void setTnsEnabled(Boolean tnsEnabled) {
		this.tnsEnabled = tnsEnabled;
	}

	public String getTnsPath() {
		return StringUtils.defaultString(tnsPath, "");
	}

	public void setTnsPath(String tnsPath) {
		this.tnsPath = tnsPath;
	}

	public String getReportLogoPath() {
		return StringUtils.defaultString(reportLogoPath, "logo.png");
	}

	public void setReportLogoPath(String reportLogoPath) {
		this.reportLogoPath = reportLogoPath;
	}

	public Boolean isClusteringEnabled() {
		return clusteringEnabled;
	}

	public void setClusteringEnabled(Boolean clusteringEnabled) {
		this.clusteringEnabled = clusteringEnabled;
	}

	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public String getEcf() {
		return ecf;
	}

	public void setEcf(String ecf) {
		this.ecf = ecf;
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

	public String getShowLog() {
		return showLog;
	}

	public void setShowLog(String showLog) {
		this.showLog = showLog;
	}

	public Integer getLdapNumberOfTries() {
		return ldapNumberOfTries;
	}

	public void setLdapNumberOfTries(Integer ldapNumberOfTries) {
		this.ldapNumberOfTries = ldapNumberOfTries;
	}

}
