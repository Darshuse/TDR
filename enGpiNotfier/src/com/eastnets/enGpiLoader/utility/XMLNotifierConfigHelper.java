package com.eastnets.enGpiLoader.utility;

import java.util.List;
import java.util.Map;

public class XMLNotifierConfigHelper {

	
	private String tableName; 
	private String where; 
	private List<Queue> queueList;
	private boolean  enabelMailNotify=false;
	private boolean enabelWriteGpiConfirmation=false;
	private Integer timeInterval;
	private Integer numberOfAttempt;
	private String filePathRJE;
	private Integer bulkSize;
	private long sleepPeriod = 30;
	private Map<String, Queue> queueMap;
	private String rejex;
	private String environmentType;
	private String delegatBic;
	private Integer mailAttempts;
	private Integer confirmAttempts;
	private String durationTime;
	private String mesgDirection;
	private String reasonCode;
	public XMLNotifierConfigHelper(String filePathDirectory,String tableName,String where,String filePathRJE,List<Queue> queueList
			,boolean enabelMailNotify , boolean enabelWriteGpiConfirmation,Integer timeInterval  ,Integer bulkSize,Map<String, Queue> queueMap, long sleepPeriod,String rejex,String environmentType,String delegatBic,Integer mailAttempts,Integer confirmAttempts,String durationTime,String mesgDirection,String reasonCode) {
		this.filePathRJE=filePathRJE;
		this.queueList=queueList;
		this.tableName=tableName;
		this.where=where;
		this.filePathRJE=filePathDirectory; 
		this.setEnabelMailNotify(enabelMailNotify);
		this.setEnabelWriteGpiConfirmation(enabelWriteGpiConfirmation);
		this.setTimeInterval(timeInterval);
		this.setBulkSize(bulkSize);
		this.queueMap=queueMap;
		this.sleepPeriod=sleepPeriod;
		this.setRejex(rejex);
		this.environmentType=environmentType;
		this.delegatBic=delegatBic;
		this.mailAttempts=mailAttempts;
		this.confirmAttempts=confirmAttempts;
		this.durationTime=durationTime;
		this.mesgDirection=mesgDirection;
		this.reasonCode=reasonCode;
	}


	public String getFilePathRJE() {
		return filePathRJE;
	}

	public void setFilePathRJE(String filePathRJE) {
		this.filePathRJE = filePathRJE;
	}

	public List<Queue> getQueueList() {
		return queueList;
	}

	public void setQueueList(List<Queue> queueList) {
		this.queueList = queueList;
	}


	public String getWhere() {
		return where;
	}


	public void setWhere(String where) {
		this.where = where;
	}


	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public boolean isEnabelMailNotify() {
		return enabelMailNotify;
	}


	public void setEnabelMailNotify(boolean enabelMailNotify) {
		this.enabelMailNotify = enabelMailNotify;
	}


	public Integer getTimeInterval() {
		return timeInterval;
	}


	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}


	public boolean isEnabelWriteGpiConfirmation() {
		return enabelWriteGpiConfirmation;
	}


	public void setEnabelWriteGpiConfirmation(boolean enabelWriteGpiConfirmation) {
		this.enabelWriteGpiConfirmation = enabelWriteGpiConfirmation;
	}


	public Integer getNumberOfAttempt() {
		return numberOfAttempt;
	}


	public void setNumberOfAttempt(Integer numberOfAttempt) {
		this.numberOfAttempt = numberOfAttempt;
	}


	public Integer getBulkSize() {
		return bulkSize;
	}


	public void setBulkSize(Integer bulkSize) {
		this.bulkSize = bulkSize;
	}


	public Map<String, Queue> getQueueMap() {
		return queueMap;
	}


	public void setQueueMap(Map<String, Queue> queueMap) {
		this.queueMap = queueMap;
	}


	public long getSleepPeriod() {
		return sleepPeriod;
	}


	public void setSleepPeriod(long sleepPeriod) {
		this.sleepPeriod = sleepPeriod;
	}


	public String getRejex() {
		return rejex;
	}


	public void setRejex(String rejex) {
		this.rejex = rejex;
	}


	public String getEnvironmentType() {
		return environmentType;
	}


	public void setEnvironmentType(String environmentType) {
		this.environmentType = environmentType;
	}


	public String getDelegatBic() {
		return delegatBic;
	}


	public void setDelegatBic(String delegatBic) {
		this.delegatBic = delegatBic;
	}


	public Integer getMailAttempts() {
		return mailAttempts;
	}


	public void setMailAttempts(Integer mailAttempts) {
		this.mailAttempts = mailAttempts;
	}


	public Integer getConfirmAttempts() {
		return confirmAttempts;
	}


	public void setConfirmAttempts(Integer confirmAttempts) {
		this.confirmAttempts = confirmAttempts;
	}


	public String getDurationTime() {
		return durationTime;
	}


	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}


	public String getMesgDirection() {
		return mesgDirection;
	}


	public void setMesgDirection(String mesgDirection) {
		this.mesgDirection = mesgDirection;
	}


	public String getReasonCode() {
		return reasonCode;
	}


	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}




}
