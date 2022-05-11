package com.eastnets.service.gpi;

import java.util.List;

import com.eastnets.domain.viewer.GpiAgent;

public class GPIServiceBean {

	private List<GpiAgent> messageList;
	private List<GpiAgent> coveAgents;
	private List<GpiAgent> beforeIntermediateAgents;
	private GPIProperties gpiProperties;

	public List<GpiAgent> getCoveAgents() {
		return coveAgents;
	}

	public void setCoveAgents(List<GpiAgent> coveAgents) {
		this.coveAgents = coveAgents;
	}

	public List<GpiAgent> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<GpiAgent> messageList) {
		this.messageList = messageList;
	}

	public GPIProperties getGpiProperties() {
		return gpiProperties;
	}

	public void setGpiProperties(GPIProperties gpiProperties) {
		this.gpiProperties = gpiProperties;
	}

	public List<GpiAgent> getBeforeIntermediateAgents() {
		return beforeIntermediateAgents;
	}

	public void setBeforeIntermediateAgents(List<GpiAgent> beforeIntermediateAgents) {
		this.beforeIntermediateAgents = beforeIntermediateAgents;
	}

}
