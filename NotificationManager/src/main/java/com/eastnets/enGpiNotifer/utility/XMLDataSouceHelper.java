package com.eastnets.enGpiNotifer.utility;

import java.io.Serializable;

public class XMLDataSouceHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 104623687705382105L;

	private DataSource dataSource;
	private String ip;
	private String dbType;
	private String dbName;
	private String serviceName;
	private String port;
	private String username;
	private String password;
	private String instansName;
	private String aid;
	private boolean partitioned = false;
	private String ibmMqIp;
	private String ibmMqUsername;
	private String ibmMqPassword;
	private String ibmMqPort;
	private String ibmChannel;
	private String ibmQueueManager;
	private String ibmInputQueueName;
	private String ibmErrorQueueName;
	private String ipView;
	private String dbTypeView;
	private String dbNameView;
	private String serviceNameView;
	private String portView;
	private String usernameView;
	private String passwordView;
	private String instansNameView;
	private boolean partitionedView;
	private String tabelName;
	private String msgTextColumn;
	private String msgIDCloumn;
	private String msgCreationDataColumn;
	private String msgQueueNameColumn;
	private Boolean tnsEnabled = false;
	private String tnsPath = "";

	public void initDBViewConfig(String ip, String dbType, String dbName, String serviceName, String port, String username, String password, String instansName, boolean partitioned, String tabelName, String msgTextColumn, String msgIDCloumn,
			String msgCreationDataColumn, String msgQueueNameColumn, DataSource dataSource, Boolean tnsEnabled, String tnsPath) {
		this.ipView = ip;
		this.dbTypeView = dbType;
		this.dbNameView = dbName;
		this.serviceNameView = serviceName;
		this.portView = port;
		this.usernameView = username;
		this.passwordView = password;
		this.instansNameView = instansName;
		this.partitionedView = partitioned;
		this.dataSource = dataSource;
		this.setTabelName(tabelName);
		this.setMsgTextColumn(msgTextColumn);
		this.setMsgIDCloumn(msgIDCloumn);
		this.setMsgCreationDataColumn(msgCreationDataColumn);
		this.msgQueueNameColumn = msgQueueNameColumn;
		this.tnsEnabled = tnsEnabled;
		this.tnsPath = tnsPath;

	}

	public XMLDataSouceHelper() {
	}

	public void initDBConfig(String ip, String dbType, String dbName, String serviceName, String port, String username, String password, String instansName, String aid, boolean partitioned, DataSource dataSource, Boolean tnsEnabled, String tnsPath) {
		this.ip = ip;
		this.dbType = dbType;
		this.dbName = dbName;
		this.serviceName = serviceName;
		this.port = port;
		this.username = username;
		this.password = password;
		this.instansName = instansName;
		this.partitioned = partitioned;
		this.setAid(aid);
		this.dataSource = dataSource;
		this.tnsEnabled = tnsEnabled;
		this.tnsPath = tnsPath;
	}

	public void initIbmMqConfig(String ibmMqIp, String ibmMqUsername, String ibmMqPassword, String ibmMqPort, String ibmChannel, String ibmQueueManager, String ibmInputQueueName, String ibmErrorQueueName, DataSource dataSource, Boolean tnsEnabled,
			String tnsPath) {
		this.ibmMqIp = ibmMqIp;
		this.ibmMqUsername = ibmMqUsername;
		this.ibmMqPassword = ibmMqPassword;
		this.ibmMqPort = ibmMqPort;
		this.ibmChannel = ibmChannel;
		this.ibmQueueManager = ibmQueueManager;
		this.ibmInputQueueName = ibmInputQueueName;
		this.ibmErrorQueueName = ibmErrorQueueName;
		this.dataSource = dataSource;
		this.tnsEnabled = tnsEnabled;
		this.tnsPath = tnsPath;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "XMLDataSouce [ip=" + ip + ", dbType=" + dbType + ", dbName=" + dbName + ", serviceName=" + serviceName + ", port=" + port + ", username=" + username + ", password=" + password + ", Bulk Size = " + "" + "]";
	}

	public String getInstansName() {
		return instansName;
	}

	public void setInstansName(String instansName) {
		this.instansName = instansName;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public boolean isPartitioned() {
		return partitioned;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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

	public String getIpView() {
		return ipView;
	}

	public void setIpView(String ipView) {
		this.ipView = ipView;
	}

	public String getDbTypeView() {
		return dbTypeView;
	}

	public void setDbTypeView(String dbTypeView) {
		this.dbTypeView = dbTypeView;
	}

	public String getDbNameView() {
		return dbNameView;
	}

	public void setDbNameView(String dbNameView) {
		this.dbNameView = dbNameView;
	}

	public String getServiceNameView() {
		return serviceNameView;
	}

	public void setServiceNameView(String serviceNameView) {
		this.serviceNameView = serviceNameView;
	}

	public String getPortView() {
		return portView;
	}

	public void setPortView(String portView) {
		this.portView = portView;
	}

	public String getUsernameView() {
		return usernameView;
	}

	public void setUsernameView(String usernameView) {
		this.usernameView = usernameView;
	}

	public String getPasswordView() {
		return passwordView;
	}

	public void setPasswordView(String passwordView) {
		this.passwordView = passwordView;
	}

	public String getInstansNameView() {
		return instansNameView;
	}

	public void setInstansNameView(String instansNameView) {
		this.instansNameView = instansNameView;
	}

	public boolean isPartitionedView() {
		return partitionedView;
	}

	public void setPartitionedView(boolean partitionedView) {
		this.partitionedView = partitionedView;
	}

	public String getTabelName() {
		return tabelName;
	}

	public void setTabelName(String tabelName) {
		this.tabelName = tabelName;
	}

	public String getMsgTextColumn() {
		return msgTextColumn;
	}

	public void setMsgTextColumn(String msgTextColumn) {
		this.msgTextColumn = msgTextColumn;
	}

	public String getMsgIDCloumn() {
		return msgIDCloumn;
	}

	public void setMsgIDCloumn(String msgIDCloumn) {
		this.msgIDCloumn = msgIDCloumn;
	}

	public String getMsgCreationDataColumn() {
		return msgCreationDataColumn;
	}

	public void setMsgCreationDataColumn(String msgCreationDataColumn) {
		this.msgCreationDataColumn = msgCreationDataColumn;
	}

	public String getMsgQueueNameColumn() {
		return msgQueueNameColumn;
	}

	public void setMsgQueueNameColumn(String msgQueueNameColumn) {
		this.msgQueueNameColumn = msgQueueNameColumn;
	}

	public Boolean getTnsEnabled() {
		return tnsEnabled;
	}

	public void setTnsEnabled(Boolean tnsEnabled) {
		this.tnsEnabled = tnsEnabled;
	}

	public String getTnsPath() {
		return tnsPath;
	}

	public void setTnsPath(String tnsPath) {
		this.tnsPath = tnsPath;
	}

}