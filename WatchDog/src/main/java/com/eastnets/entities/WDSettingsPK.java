package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WDSettingsPK implements Serializable {

	private static final long serialVersionUID = -8078420513492295124L;

	@Column(name = "OSNDELAY")
	private int osnDelay;

	public int getOsnDelay() {
		return osnDelay;
	}

	public void setOsnDelay(int osnDelay) {
		this.osnDelay = osnDelay;
	}

	@Override
	public String toString() {
		return "WDSettingsPK [osnDelay=" + osnDelay + "]";
	}

}
