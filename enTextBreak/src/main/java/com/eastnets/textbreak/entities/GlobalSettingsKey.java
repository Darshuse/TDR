package com.eastnets.textbreak.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the GlobalSettingsKey database table.
 * 
 */
@Embeddable
public class GlobalSettingsKey implements Serializable {

	private static final long serialVersionUID = -7900502751936082702L;

	@Column(name = "LIVE_SOURCE", nullable = false)
	private String liveSource;

	public String getLiveSource() {
		return liveSource;
	}

	public void setLiveSource(String liveSource) {
		this.liveSource = liveSource;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}