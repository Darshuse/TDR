
package com.eastnets.notifier.domain;

import java.time.LocalDateTime;

public class Intervention {

	private PrimaryKey primaryKey;
	private String operatorNickName;
	private String instanceNumber;
	private String instanceUnitName;
	private String mergedText;
	private LocalDateTime interventionDateTime;

	private String instanceStatus;

	private Long sequenceNumber;

	public String getMergedText() {
		return mergedText;
	}

	public void setMergedText(String mergedText) {
		this.mergedText = mergedText;
	}

	public String getOperatorNickName() {
		return operatorNickName;
	}

	public void setOperatorNickName(String operatorNickName) {
		this.operatorNickName = operatorNickName;
	}

	public String getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(String instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getInstanceUnitName() {
		return instanceUnitName;
	}

	public void setInstanceUnitName(String instanceUnitName) {
		this.instanceUnitName = instanceUnitName;
	}

	public String getInstanceStatus() {
		return instanceStatus;
	}

	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}

	public LocalDateTime getInterventionDateTime() {
		return interventionDateTime;
	}

	public void setInterventionDateTime(LocalDateTime interventionDateTime) {
		this.interventionDateTime = interventionDateTime;
	}

	@Override
	public String toString() {
		return "Intervention [operatorNickName=" + operatorNickName + ", instanceNumber=" + instanceNumber + "]";
	}

}
