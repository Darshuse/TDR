package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;

public class ConnectionLicenseInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7043113856354227060L;
	private boolean newMessages;

	public ConnectionLicenseInfo() {
		this.newMessages = true;
	}

	public ConnectionLicenseInfo(final boolean newMessages) {
		this.newMessages = newMessages;
	}

	/**
	 * @return the newMessages
	 */
	public boolean isNewMessages() {
		return newMessages;
	}

	/**
	 * @param newMessages the newMessages to set
	 */
	public void setNewMessages(boolean newMessages) {
		this.newMessages = newMessages;
	}
}
