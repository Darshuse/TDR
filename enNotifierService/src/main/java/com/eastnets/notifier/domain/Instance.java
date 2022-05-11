
package com.eastnets.notifier.domain;

import java.util.List;

public class Instance {

	private String unitAssigned;
	private String ququeStatus;
	private String instanceStatus;
	private Appendix lastAppendix;
	private Intervention lastIntervention;
	private List<Intervention> allInterverntion;

	private List<String> allQueueStatuses;

	public Appendix getLastAppendix() {
		return lastAppendix;
	}

	public String getInstanceStatus() {
		return instanceStatus;
	}

	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}

	public void setLastAppendix(Appendix lastAppendix) {
		this.lastAppendix = lastAppendix;
	}

	public Intervention getLastIntervention() {
		return lastIntervention;
	}

	public void setLastIntervention(Intervention lastIntervention) {
		this.lastIntervention = lastIntervention;
	}

	public String getUnitAssigned() {
		return unitAssigned;
	}

	public void setUnitAssigned(String unitAssigned) {
		this.unitAssigned = unitAssigned;
	}

	public String getQuqueStatus() {
		return ququeStatus;
	}

	public void setQuqueStatus(String ququeStatus) {
		this.ququeStatus = ququeStatus;
	}

	public List<Intervention> getAllInterverntion() {
		return allInterverntion;
	}

	public void setAllInterverntion(List<Intervention> allInterverntion) {
		this.allInterverntion = allInterverntion;
	}

	public List<String> getAllQueueStatuses() {
		return allQueueStatuses;
	}

	public void setAllQueueStatuses(List<String> allQueueStatuses) {
		this.allQueueStatuses = allQueueStatuses;
	}

}
