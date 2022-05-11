package com.eastnets.notifier.domain;

import java.util.Comparator;

public class NotifierEventObserver implements Comparable<NotifierEventObserver> {

	private Long id;

	private PrimaryKey msgPrimaryKey;

	private String routingPointName;

	private String InstanceSequanceNumber;

	private String queueStatus;

	private String primaryKeyWithInstNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PrimaryKey getMsgPrimaryKey() {
		return msgPrimaryKey;
	}

	public void setMsgPrimaryKey(PrimaryKey msgPrimaryKey) {
		this.msgPrimaryKey = msgPrimaryKey;
	}

	public String getRoutingPointName() {
		return routingPointName;
	}

	public void setRoutingPointName(String routingPointName) {
		this.routingPointName = routingPointName;
	}

	public String getInstanceSequanceNumber() {
		return InstanceSequanceNumber;
	}

	public void setInstanceSequanceNumber(String instanceSequanceNumber) {
		InstanceSequanceNumber = instanceSequanceNumber;
	}

	public String getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}

	public String getPrimaryKeyWithInstNum() {
		return primaryKeyWithInstNum;
	}

	public void setPrimaryKeyWithInstNum(String primaryKeyWithInstNum) {
		this.primaryKeyWithInstNum = primaryKeyWithInstNum;
	}

	@Override
	public int compareTo(NotifierEventObserver o) {

		return Comparator.comparing(NotifierEventObserver::getMsgPrimaryKey).thenComparing(NotifierEventObserver::getRoutingPointName).thenComparing(NotifierEventObserver::getInstanceSequanceNumber).compare(this, o);

	}

}
