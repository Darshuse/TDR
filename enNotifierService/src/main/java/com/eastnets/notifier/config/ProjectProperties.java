package com.eastnets.notifier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@PropertySource(value = { "classpath:application.properties", "file:${EN_REPORTING_CONFIG_HOME}/notifier.properties" })
public class ProjectProperties {

	private String dbType;
	private String serverName;
	private String portNumber;
	private String dbServiceName;
	@Value("${dbUserName}")
	private String dbUserName;
	private String password;
	private String databaseName;
	private String instanceName;
	private Integer bulkSize;
	private String schemaName;

	/*
	 * The following are IBM mq connection details
	 */
	@Value("${ibmQueueManager}")
	private String queueManager;
	@Value("${ibmChannel}")
	private String channelName;
	@Value("${ibmHostName}")
	private String hostName;

	@Value("${ibmPort}")
	private int ibmPort;
	@Value("${ibmUserName: }")
	private String mqUserName;

	@Value("${fetchEvery:5}")
	private Integer fetchEvery = 5;

	@Value("${ibmUserPassword: }")
	private String mqPassword;

	@Value("${SSLKeyStoreType:}")
	private String sslKeyStoreType;

	@Value("${SSLKeyStore:}")
	private String sslKeyStore;

	@Value("${SSLKeyStorePassword:}")
	private String sslKeyStorePassword;

	@Value("${SSLTrustStoreType:}")
	private String sslTrustStoreType;

	@Value("${SSLTrustStore:}")
	private String sslTrustStore;

	@Value("${SSLTrustStorePassword:}")
	private String sslTrustStorePassword;

	@Value("${SSLCipherSuite:}")
	private String sslCipherSuite;
	@Value("${sslPeerName:}")
	private String sslPeerName;
	@Value("${SSLCipherSpec:}")
	private String SSLCipherSpec;

	@Value("${useIBMCipherMappings:false}")
	private String useIBMCipherMappings;

	@Value("${addtrustStore:false}")
	private String addtrustStore;

	@Value("${noAuthentication:false}")
	private boolean noAuthentication;

	@Value("${SSLEnableFIPS:false}")
	private boolean enableFIPS;

	@Value("${deleteFromIntvMergedText:true}")
	private boolean deleteFromIntvMergedText;

	@Value("${updateNotifierStatus:false}")
	private boolean updateNotifierStatus;

	@Value("${forceNotifierObserverDelete:false}")
	private boolean forceNotifierObserverDelete;

	public boolean isUpdateNotifierStatus() {
		return updateNotifierStatus;
	}

	public boolean isForceNotifierObserverDelete() {
		return forceNotifierObserverDelete;
	}

	public void setForceNotifierObserverDelete(boolean forceNotifierObserverDelete) {
		this.forceNotifierObserverDelete = forceNotifierObserverDelete;
	}

	public void setUpdateNotifierStatus(boolean updateNotifierStatus) {
		this.updateNotifierStatus = updateNotifierStatus;
	}

	public boolean isDeleteFromIntvMergedText() {
		return deleteFromIntvMergedText;
	}

	public void setDeleteFromIntvMergedText(boolean deleteFromIntvMergedText) {
		this.deleteFromIntvMergedText = deleteFromIntvMergedText;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getServerName() {
		return serverName;
	}

	public Integer getFetchEvery() {
		return fetchEvery;
	}

	public void setFetchEvery(Integer fetchEvery) {
		this.fetchEvery = fetchEvery;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getDbServiceName() {
		return dbServiceName;
	}

	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Integer getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(Integer bulkSize) {
		this.bulkSize = bulkSize;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getQueueManager() {
		return queueManager;
	}

	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getIbmPort() {
		return ibmPort;
	}

	public void setIbmPort(int ibmPort) {
		this.ibmPort = ibmPort;
	}

	public String getMqUserName() {
		return mqUserName;
	}

	public void setMqUserName(String mqUserName) {
		this.mqUserName = mqUserName;
	}

	public String getMqPassword() {
		return mqPassword;
	}

	public void setMqPassword(String mqPassword) {
		this.mqPassword = mqPassword;
	}

	public String getSslKeyStoreType() {
		return sslKeyStoreType;
	}

	public void setSslKeyStoreType(String sslKeyStoreType) {
		this.sslKeyStoreType = sslKeyStoreType;
	}

	public String getSslKeyStore() {
		return sslKeyStore;
	}

	public void setSslKeyStore(String sslKeyStore) {
		this.sslKeyStore = sslKeyStore;
	}

	public String getSslKeyStorePassword() {
		return sslKeyStorePassword;
	}

	public void setSslKeyStorePassword(String sslKeyStorePassword) {
		this.sslKeyStorePassword = sslKeyStorePassword;
	}

	public String getSslTrustStoreType() {
		return sslTrustStoreType;
	}

	public void setSslTrustStoreType(String sslTrustStoreType) {
		this.sslTrustStoreType = sslTrustStoreType;
	}

	public String getSslTrustStore() {
		return sslTrustStore;
	}

	public void setSslTrustStore(String sslTrustStore) {
		this.sslTrustStore = sslTrustStore;
	}

	public String getSslTrustStorePassword() {
		return sslTrustStorePassword;
	}

	public void setSslTrustStorePassword(String sslTrustStorePassword) {
		this.sslTrustStorePassword = sslTrustStorePassword;
	}

	public String getSslCipherSuite() {
		return sslCipherSuite;
	}

	public void setSslCipherSuite(String sslCipherSuite) {
		this.sslCipherSuite = sslCipherSuite;
	}

	public String getUseIBMCipherMappings() {
		return useIBMCipherMappings;
	}

	public void setUseIBMCipherMappings(String useIBMCipherMappings) {
		this.useIBMCipherMappings = useIBMCipherMappings;
	}

	public String getAddtrustStore() {
		return addtrustStore;
	}

	public void setAddtrustStore(String addtrustStore) {
		this.addtrustStore = addtrustStore;
	}

	public boolean isNoAuthentication() {
		return noAuthentication;
	}

	public void setNoAuthentication(boolean noAuthentication) {
		this.noAuthentication = noAuthentication;
	}

	public String getSSLCipherSpec() {
		return SSLCipherSpec;
	}

	public void setSSLCipherSpec(String sSLCipherSpec) {
		SSLCipherSpec = sSLCipherSpec;
	}

	public String getSslPeerName() {
		return sslPeerName;
	}

	public void setSslPeerName(String sslPeerName) {
		this.sslPeerName = sslPeerName;
	}

	public boolean isEnableFIPS() {
		return enableFIPS;
	}

	public void setEnableFIPS(boolean enableFIPS) {
		this.enableFIPS = enableFIPS;
	}

}
