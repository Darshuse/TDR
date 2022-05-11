package com.eastnets.service.loader.helper;

import java.io.Serializable;

public class XMLDataSouceHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 104623687705382105L;

	private boolean dbIntegrationEnabled;
	private boolean ibmMQIntegrationEnabled;
	private boolean apacheMqIntegrationEnabled;
	private boolean useErrorQueue = true;
	private String ip;
	private String dbType;
	private String dbName;
	private String serviceName;
	private String port;
	private String username;
	private String password;
	private int bulkSize;
	private String formatType;
	private String ibmMqIp;
	private String ibmMqUsername;
	private String ibmMqPassword;
	private String ibmMqPort;
	private String ibmChannel;
	private String ibmQueueManager;
	private String ibmInputQueueName;
	private String ibmErrorQueueName;

	private String apacheMqIp;
	private String apacheMqPort;
	private String apacheInputQueueName;
	private String apacheErrorQueueName;

	private String mqVendor;
	private int mqBulkSize;

	private String sslKeyStoreType;
	private String sslKeyStore;

	private String sslKeyStorePassword;

	private String sslTrustStoreType;

	private String sslTrustStore;

	private String sslTrustStorePassword;

	private String sslCipherSuite;
	private String sslPeerName;
	private String SSLCipherSpec;

	private String useIBMCipherMappings;

	private String addtrustStore;

	private boolean noAuthentication;

	private boolean enableFIPS;
	private boolean appId = false;

	public XMLDataSouceHelper() {
	}

	public void initDBConfig(String ip, String dbType, String dbName, String serviceName, String port, String username, String password, int bulkSize) {
		this.ip = ip;
		this.dbType = dbType;
		this.dbName = dbName;
		this.serviceName = serviceName;
		this.port = port;
		this.username = username;
		this.password = password;
		this.bulkSize = bulkSize;
	}

	public void initIbmMqConfig(String ibmMqIp, String mqVendor, String ibmMqUsername, String ibmMqPassword, String ibmMqPort, String ibmChannel, String ibmQueueManager, String ibmInputQueueName, String ibmErrorQueueName, int mqBulkSize,
			String formatType, String sslKeyStoreType, String sslKeyStore, String sslKeyStorePassword, String sslTrustStoreType, String sslTrustStore, String sslTrustStorePassword, String sslCipherSuite, String sslPeerName, String sSLCipherSpec,
			String useIBMCipherMappings, boolean noAuthentication, boolean enableFIPS, boolean appId) {
		this.ibmMqIp = ibmMqIp;
		this.mqVendor = mqVendor;
		this.ibmMqUsername = ibmMqUsername;
		this.ibmMqPassword = ibmMqPassword;
		this.ibmMqPort = ibmMqPort;
		this.ibmChannel = ibmChannel;
		this.ibmQueueManager = ibmQueueManager;
		this.ibmInputQueueName = ibmInputQueueName;
		this.ibmErrorQueueName = ibmErrorQueueName;
		this.mqBulkSize = mqBulkSize;
		this.formatType = formatType;
		this.sslKeyStoreType = sslKeyStoreType;
		this.sslKeyStore = sslKeyStore;
		this.sslKeyStorePassword = sslKeyStorePassword;
		this.sslTrustStoreType = sslTrustStoreType;
		this.sslTrustStore = sslTrustStore;
		this.sslTrustStorePassword = sslTrustStorePassword;
		this.sslCipherSuite = sslCipherSuite;
		this.sslPeerName = sslPeerName;
		this.SSLCipherSpec = sSLCipherSpec;
		this.useIBMCipherMappings = useIBMCipherMappings;
		this.noAuthentication = noAuthentication;
		this.enableFIPS = enableFIPS;
		this.appId = appId;
	}

	public void initApacheMqConfig(String apacheMqIp, String apacheMqPort, String mqVendor, String apacheInputQueueName, String apacheErrorQueueName, int mqBulkSize, String formatType) {
		this.apacheMqIp = apacheMqIp;
		this.apacheMqPort = apacheMqPort;
		this.mqVendor = mqVendor;
		this.apacheInputQueueName = apacheInputQueueName;
		this.apacheErrorQueueName = apacheErrorQueueName;
		this.mqBulkSize = mqBulkSize;
		this.formatType = formatType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(int bulkSize) {
		this.bulkSize = bulkSize;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getIbmMqIp() {
		return ibmMqIp;
	}

	public void setIbmMqIp(String ibmMqIp) {
		this.ibmMqIp = ibmMqIp;
	}

	public String getIbmMqUsername() {
		return ibmMqUsername;
	}

	public void setIbmMqUsername(String ibmMqUsername) {
		this.ibmMqUsername = ibmMqUsername;
	}

	public String getIbmMqPassword() {
		return ibmMqPassword;
	}

	public void setIbmMqPassword(String ibmMqPassword) {
		this.ibmMqPassword = ibmMqPassword;
	}

	public String getIbmMqPort() {
		return ibmMqPort;
	}

	public void setIbmMqPort(String ibmMqPort) {
		this.ibmMqPort = ibmMqPort;
	}

	public String getIbmChannel() {
		return ibmChannel;
	}

	public void setIbmChannel(String ibmChannel) {
		this.ibmChannel = ibmChannel;
	}

	public String getIbmQueueManager() {
		return ibmQueueManager;
	}

	public void setIbmQueueManager(String ibmQueueManager) {
		this.ibmQueueManager = ibmQueueManager;
	}

	public String getIbmInputQueueName() {
		return ibmInputQueueName;
	}

	public void setIbmInputQueueName(String ibmInputQueueName) {
		this.ibmInputQueueName = ibmInputQueueName;
	}

	public String getIbmErrorQueueName() {
		return ibmErrorQueueName;
	}

	public void setIbmErrorQueueName(String ibmErrorQueueName) {
		this.ibmErrorQueueName = ibmErrorQueueName;
	}

	public String getApacheMqIp() {
		return apacheMqIp;
	}

	public void setApacheMqIp(String apacheMqIp) {
		this.apacheMqIp = apacheMqIp;
	}

	public String getApacheMqPort() {
		return apacheMqPort;
	}

	public void setApacheMqPort(String apacheMqPort) {
		this.apacheMqPort = apacheMqPort;
	}

	public String getApacheInputQueueName() {
		return apacheInputQueueName;
	}

	public void setApacheInputQueueName(String apacheInputQueueName) {
		this.apacheInputQueueName = apacheInputQueueName;
	}

	public String getApacheErrorQueueName() {
		return apacheErrorQueueName;
	}

	public void setApacheErrorQueueName(String apacheErrorQueueName) {
		this.apacheErrorQueueName = apacheErrorQueueName;
	}

	public String getMqVendor() {
		return mqVendor;
	}

	public void setMqVendor(String mqVendor) {
		this.mqVendor = mqVendor;
	}

	public int getMqBulkSize() {
		return mqBulkSize;
	}

	public void setMqBulkSize(int mqBulkSize) {
		this.mqBulkSize = mqBulkSize;
	}

	public boolean isDbIntegrationEnabled() {
		return dbIntegrationEnabled;
	}

	public void setDbIntegrationEnabled(boolean dbIntegrationEnabled) {
		this.dbIntegrationEnabled = dbIntegrationEnabled;
	}

	public boolean isIbmMQIntegrationEnabled() {
		return ibmMQIntegrationEnabled;
	}

	public void setIbmMQIntegrationEnabled(boolean ibmMQIntegrationEnabled) {
		this.ibmMQIntegrationEnabled = ibmMQIntegrationEnabled;
	}

	public boolean isApacheMqIntegrationEnabled() {
		return apacheMqIntegrationEnabled;
	}

	public void setApacheMqIntegrationEnabled(boolean apacheMqIntegrationEnabled) {
		this.apacheMqIntegrationEnabled = apacheMqIntegrationEnabled;
	}

	@Override
	public String toString() {
		return "XMLDataSouce [ip=" + ip + ", dbType=" + dbType + ", dbName=" + dbName + ", serviceName=" + serviceName + ", port=" + port + ", username=" + username + ", password=" + password + ", Bulk Size = " + bulkSize + "]";
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public boolean isUseErrorQueue() {
		return useErrorQueue;
	}

	public void setUseErrorQueue(boolean useErrorQueue) {
		this.useErrorQueue = useErrorQueue;
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

	public String getSslPeerName() {
		return sslPeerName;
	}

	public void setSslPeerName(String sslPeerName) {
		this.sslPeerName = sslPeerName;
	}

	public String getSSLCipherSpec() {
		return SSLCipherSpec;
	}

	public void setSSLCipherSpec(String sSLCipherSpec) {
		SSLCipherSpec = sSLCipherSpec;
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

	public boolean isEnableFIPS() {
		return enableFIPS;
	}

	public void setEnableFIPS(boolean enableFIPS) {
		this.enableFIPS = enableFIPS;
	}

	public boolean isAppId() {
		return appId;
	}

	public void setAppId(boolean appId) {
		this.appId = appId;
	}

}