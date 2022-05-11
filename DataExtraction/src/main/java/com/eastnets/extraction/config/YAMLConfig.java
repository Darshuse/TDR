/**
 * 
 */
package com.eastnets.extraction.config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.eastnets.extraction.service.helper.DBPortabilityHandler;

/**
 * @author AAlrbee
 *
 */
@Validated
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

	private String name;
	private String environment;
	private Boolean enabled;
	private List<String> servers = new ArrayList<>();

	private String Aid;

	private String dbUsername;

	private String dbPassword;

	private String serverName;
	private String databaseName;

	private String portNumber;

	private String dbType;
	private String dbServiceName;
	private String instanceName;

	@Value("${partitioned:false}")
	private Boolean partitioned;
	private String tnsPath;
	private Boolean enableTnsName;
	private String ecfPath;
	private String fileName;
	private String filePath;
	private String fileSize;
	@Value("${mode:7}")
	private Integer mode;
	@Value("${expand:false}")
	private Boolean expand;
	@Value("${history:false}")
	private Boolean history;
	@Value("${flag:false}")
	private Boolean flag;
	@Value("${extractFlagged:false}")
	private Boolean extractFlagged;

	@Value("${previous:false}")
	private Boolean previous;

	@Value("${mesgIsLive:false}")
	private Boolean mesgIsLive;
	private String mesgFormat;
	private String BICFile;
	private String dayNumber;
	private String skipWeeks;
	private String fromDate;
	private String toDate;
	private String date;
	private String mesgType;
	private String senderBIC;
	private String receiverBIC;
	private String direction;
	private String messageNumber;
	private String rulesFile;
	private String source;
	private Integer threadLevel;
	@Value("${enableDebug:false}")
	private Boolean enableDebug;
	@Value("${dryRun:false}")
	private Boolean dryRun;
	@Value("${enableDebugFull:false}")
	private Boolean enableDebugFull;
	@NotEmpty
	private String scheduler;
	private String identifier;
	private String templateFile;
	private Logging logging;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getServers() {
		return servers;
	}

	public void setServers(List<String> servers) {
		this.servers = servers;
	}

	public String getAid() {
		return Aid;
	}

	public void setAid(String aid) {
		Aid = aid;
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

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
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
		return partitioned && DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Boolean isExpand() {
		return expand;
	}

	public void setExpand(Boolean expand) {
		this.expand = expand;
	}

	public Boolean isHistory() {
		return history;
	}

	public void setHistory(Boolean history) {
		this.history = history;
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

	public Boolean isPrevious() {
		return previous;
	}

	public void setPrevious(Boolean previous) {
		this.previous = previous;
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

	public String getBICFile() {
		return BICFile;
	}

	public void setBICFile(String bICFile) {
		BICFile = bICFile;
	}

	public String getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(String dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getSkipWeeks() {
		return skipWeeks;
	}

	public void setSkipWeeks(String skipWeeks) {
		this.skipWeeks = skipWeeks;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
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

	public String getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getRulesFile() {
		return rulesFile;
	}

	public void setRulesFile(String rulesFile) {
		this.rulesFile = rulesFile;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getThreadLevel() {
		return threadLevel;
	}

	public void setThreadLevel(Integer threadLevel) {
		this.threadLevel = threadLevel;
	}

	public Boolean isEnableDebug() {
		return enableDebug;
	}

	public void setEnableDebug(Boolean enableDebug) {
		this.enableDebug = enableDebug;
	}

	public Boolean isDryRun() {
		return dryRun;
	}

	public void setDryRun(Boolean dryRun) {
		this.dryRun = dryRun;
	}

	public Boolean isEnableDebugFull() {
		return enableDebugFull;
	}

	public void setEnableDebugFull(Boolean enableDebugFull) {
		this.enableDebugFull = enableDebugFull;
	}

	public String getScheduler() {
		return scheduler;
	}

	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public Logging getLogging() {
		return logging;
	}

	public void setLogging(Logging logging) {
		this.logging = logging;
	}

	@Override
	public String toString() {
		return "YAMLConfig [name=" + name + ", environment=" + environment + ", enabled=" + enabled + ", servers=" + servers + ", Aid=" + Aid + ", dbUsername=" + dbUsername + ", dbPassword=" + dbPassword + ", serverName=" + serverName
				+ ", databaseName=" + databaseName + ", portNumber=" + portNumber + ", dbType=" + dbType + ", dbServiceName=" + dbServiceName + ", instanceName=" + instanceName + ", partitioned=" + partitioned + ", tnsPath=" + tnsPath
				+ ", enableTnsName=" + enableTnsName + ", ecfPath=" + ecfPath + ", fileName=" + fileName + ", filePath=" + filePath + ", fileSize=" + fileSize + ", mode=" + mode + ", expand=" + expand + ", history=" + history + ", flag=" + flag
				+ ", extractFlagged=" + extractFlagged + ", previous=" + previous + ", mesgIsLive=" + mesgIsLive + ", mesgFormat=" + mesgFormat + ", BICFile=" + BICFile + ", dayNumber=" + dayNumber + ", skipWeeks=" + skipWeeks + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", date=" + date + ", mesgType=" + mesgType + ", senderBIC=" + senderBIC + ", receiverBIC=" + receiverBIC + ", direction=" + direction + ", messageNumber=" + messageNumber + ", rulesFile="
				+ rulesFile + ", source=" + source + ", threadLevel=" + threadLevel + ", enableDebug=" + enableDebug + ", dryRun=" + dryRun + ", enableDebugFull=" + enableDebugFull + ", scheduler=" + scheduler + ", identifier=" + identifier
				+ ",logging=" + logging + "]";
	}

}