package com.eastnets.enReportingLoader.config;

import com.eastnets.config.ConfigBean;

public class AppConfigBean extends ConfigBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1832940635910968590L;
	private String aid;
	private String dbConfigFilePath;
	private String clusterTcpConfigFilePath;

	/*
	 * The following are IBM mq connection details
	 */
	private String queueManager;
	private String channelName;
	private String hostName;
	private int ibmPort;
	private String mqUserName = " ";

	private String mqPassword = " ";

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

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	private Boolean enableDebugMode = false;

	public AppConfigBean() {

	}

	public String getSAAAid() {
		return aid;
	}

	public void setSAAAid(String aid) {
		this.aid = aid;
	}

	public String getDbConfigFilePath() {
		return dbConfigFilePath;
	}

	public void setDbConfigFilePath(String dbConfigFilePath) {
		this.dbConfigFilePath = dbConfigFilePath;
	}

	public Boolean getEnableDebugMode() {
		return enableDebugMode;
	}

	public void setEnableDebugMode(Boolean enableDebugMode) {
		this.enableDebugMode = enableDebugMode;
	}

	public String getClusterTcpConfigFilePath() {
		return clusterTcpConfigFilePath;
	}

	public void setClusterTcpConfigFilePath(String clusterTcpConfigFilePath) {
		this.clusterTcpConfigFilePath = clusterTcpConfigFilePath;
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
}
