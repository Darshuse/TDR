package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryLine {

	private String HistoryDate;
	private String Phase;
	private String Action;
	private String Reason;
	private String Entity;
	private String Description;

	// Getter Methods

	public String getHistoryDate() {
		return HistoryDate;
	}

	public String getPhase() {
		return Phase;
	}

	public String getAction() {
		return Action;
	}

	public String getReason() {
		return Reason;
	}

	public String getEntity() {
		return Entity;
	}

	public String getDescription() {
		return Description;
	}

	// Setter Methods

	public void setHistoryDate(String HistoryDate) {
		this.HistoryDate = HistoryDate;
	}

	public void setPhase(String Phase) {
		this.Phase = Phase;
	}

	public void setAction(String Action) {
		this.Action = Action;
	}

	public void setReason(String Reason) {
		this.Reason = Reason;
	}

	@Override
	public String toString() {
		return "HistoryLine [HistoryDate=" + HistoryDate + ", Phase=" + Phase + ", Action=" + Action + ", Reason=" + Reason + ", Entity=" + Entity + ", Description=" + Description + "]";
	}

	public void setEntity(String Entity) {
		this.Entity = Entity;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}
}