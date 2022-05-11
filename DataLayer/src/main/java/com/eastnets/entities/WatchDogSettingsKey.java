package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WatchDogSettingsKey implements Serializable {

	private static final long serialVersionUID = 7432410823790844609L;

	@Column(name = "EVENTREQUEST", nullable = false)
	private String eventRequest;

	public String getEventRequest() {
		return eventRequest;
	}

	public void setEventRequest(String eventRequest) {
		this.eventRequest = eventRequest;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
