package com.eastnets.extraction.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eastnets.extraction.config.Logging;

public class SearchParam {

	private List<String> Aid = new ArrayList<String>();
	private Boolean mesgIsLive;
	private String mesgFormat;
	private Date fromDate;
	private Date toDate;
	private List<String> mesgTypeList;
	private String senderBIC;
	private String receiverBIC;
	private String direction;
	private String filePath;
	private int dayNumber;
	private String BICFile;
	private Boolean history;
	private String fileName;
	private Boolean expand;
	private String identifier;
	private String date;
	private int mode;
	private Boolean dryRun;
	private double fileSize;
	private Boolean enableDebug;
	private Boolean enableDebugFull;
	private int skipWeeks;
	private String xmlCriteriaFile;
	private String xmlTemplateFile;
	private String scheduler;
	private int transactionNumber;
	private Integer messageNumber;
	private Boolean flag;
	private Boolean extractFlagged;
	private String dbUsername;
	private String dbPassword;
	private String serverName;
	private String databaseName;
	private Integer portNumber;
	private String dbType;
	private String dbServiceName;
	private String instanceName;
	private Boolean partitioned;
	private String tnsPath;
	private Boolean enableTnsName;
	private String ecfPath;
	private Boolean previous;
	private String source;
	private GeneratedFilesData generatedFilesData = new GeneratedFilesData();
	private Logging logging;

	public List<String> getAid() {
		return Aid;
	}

	public void setAid(List<String> aid) {
		Aid = aid;
	}

	public Boolean isMesgIsLive() {
		return mesgIsLive;
	}

	public void setMesgIsLive(Boolean mesgIsLive) {
		this.mesgIsLive = mesgIsLive;
	}

	public String getMesgFormat() {
		return mesgFormat;
	}

	public void setMesgFormat(String mesgFormat) {
		this.mesgFormat = mesgFormat;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<String> getMesgTypeList() {
		return mesgTypeList;
	}

	public void setMesgTypeList(List<String> mesgTypeList) {
		this.mesgTypeList = mesgTypeList;
	}

	public String getSenderBIC() {
		return senderBIC;
	}

	public void setSenderBIC(String senderBIC) {
		this.senderBIC = senderBIC;
	}

	public String getReceiverBIC() {
		return receiverBIC;
	}

	public void setReceiverBIC(String receiverBIC) {
		this.receiverBIC = receiverBIC;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getBICFile() {
		return BICFile;
	}

	public void setBICFile(String bICFile) {
		BICFile = bICFile;
	}

	public Boolean isHistory() {
		return history;
	}

	public void setHistory(Boolean history) {
		this.history = history;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Boolean isExpand() {
		return expand;
	}

	public void setExpand(Boolean expand) {
		this.expand = expand;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Boolean isDryRun() {
		return dryRun;
	}

	public void setDryRun(Boolean dryRun) {
		this.dryRun = dryRun;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}

	public Boolean isEnableDebug() {
		return enableDebug;
	}

	public void setEnableDebug(Boolean enableDebug) {
		this.enableDebug = enableDebug;
	}

	public Boolean isEnableDebugFull() {
		return enableDebugFull;
	}

	public void setEnableDebugFull(Boolean enableDebugFull) {
		this.enableDebugFull = enableDebugFull;
	}

	public int getSkipWeeks() {
		return skipWeeks;
	}

	public void setSkipWeeks(int skipWeeks) {
		this.skipWeeks = skipWeeks;
	}

	public String getXmlCriteriaFile() {
		return xmlCriteriaFile;
	}

	public void setXmlCriteriaFile(String xmlCriteriaFile) {
		this.xmlCriteriaFile = xmlCriteriaFile;
	}

	public String getXmlTemplateFile() {
		return xmlTemplateFile;
	}

	public void setXmlTemplateFile(String xmlTemplateFile) {
		this.xmlTemplateFile = xmlTemplateFile;
	}

	public String getScheduler() {
		return scheduler;
	}

	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}

	public int getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Integer getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(Integer messageNumber) {
		this.messageNumber = messageNumber;
	}

	public Boolean isFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Boolean isExtractFlagged() {
		return extractFlagged;
	}

	public void setExtractFlagged(Boolean extractFlagged) {
		this.extractFlagged = extractFlagged;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
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

	public Integer getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbServiceName() {
		return dbServiceName;
	}

	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Boolean isPartitioned() {
		return partitioned;
	}

	public void setPartitioned(Boolean partitioned) {
		this.partitioned = partitioned;
	}

	public String getTnsPath() {
		return tnsPath;
	}

	public void setTnsPath(String tnsPath) {
		this.tnsPath = tnsPath;
	}

	public Boolean isEnableTnsName() {
		return enableTnsName;
	}

	public void setEnableTnsName(Boolean enableTnsName) {
		this.enableTnsName = enableTnsName;
	}

	public String getEcfPath() {
		return ecfPath;
	}

	public void setEcfPath(String ecfPath) {
		this.ecfPath = ecfPath;
	}

	public Boolean isPrevious() {
		return previous;
	}

	public void setPrevious(Boolean previous) {
		this.previous = previous;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public GeneratedFilesData getGeneratedFilesData() {
		return generatedFilesData;
	}

	public void setGeneratedFilesData(GeneratedFilesData generatedFilesData) {
		this.generatedFilesData = generatedFilesData;
	}

	public Logging getLogging() {
		return logging;
	}

	public void setLogging(Logging logging) {
		this.logging = logging;
	}

	@Override
	public String toString() {
		return "SearchParam [Aid=" + Aid + ", mesgIsLive=" + mesgIsLive + ", mesgFormat=" + mesgFormat + ", fromDate=" + fromDate + ", toDate=" + toDate + ", mesgType=" + mesgTypeList + ", senderBIC=" + senderBIC + ", receiverBIC=" + receiverBIC
				+ ", direction=" + direction + ", filePath=" + filePath + ",logging=" + logging + "]";
	}

}
