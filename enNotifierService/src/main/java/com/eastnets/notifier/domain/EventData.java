
package com.eastnets.notifier.domain;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EventData implements Comparable<EventData> {

	private PrimaryKey primaryKey;
	private Message message;
	private Integer operationType;
	private String tableName;
	private LocalDateTime insertionDateTime;
	private LocalDateTime dateTime;
	private String sequenceNumber;
	private String instanceNumber;
	private LocalDateTime creationDataTime;

	private String interventionSeqNumber;

	private String createdMessageEvent;

	/*
	 * event processed by notifier
	 */

	@Override
	public int compareTo(EventData o) {
		return Comparator.comparing(EventData::getInsertionDateTime).thenComparing(EventData::getOperationType).thenComparing(EventData::getSequenceNumber).compare(this, o);

	}

	private boolean eventProcessed;

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public LocalDateTime getInsertionDateTime() {
		return insertionDateTime;
	}

	public void setInsertionDateTime(LocalDateTime insertionDateTime) {
		this.insertionDateTime = insertionDateTime;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(String instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	/**
	 * 
	 * @return true if the event is processed
	 */
	public boolean isEventProcessed() {
		return eventProcessed;
	}

	public void setEventProcessed(boolean eventProcessed) {
		this.eventProcessed = eventProcessed;
	}

	@Override
	public String toString() {
		return "EventData [primaryKey=" + primaryKey + ", message=" + message + ", operationType=" + operationType + ", tableName=" + tableName + ", insertionDateTime=" + insertionDateTime + ", dateTime=" + dateTime + ", sequenceNumber="
				+ sequenceNumber + ", instanceNumber=" + instanceNumber + "]";
	}

	public LocalDateTime getCreationDataTime() {
		return creationDataTime;
	}

	public void setCreationDataTime(LocalDateTime creationDataTime) {
		this.creationDataTime = creationDataTime;
	}

	public String getCreatedMessageEvent() {
		return createdMessageEvent;
	}

	public void setCreatedMessageEvent(String createdMessageEvent) {
		this.createdMessageEvent = createdMessageEvent;
	}

	public String getInterventionSeqNumber() {
		return interventionSeqNumber;
	}

	public void setInterventionSeqNumber(String interventionSeqNumber) {
		this.interventionSeqNumber = interventionSeqNumber;
	}
}
