package com.eastnets.domain.watchdog;

import java.io.Serializable;

public class ActiveSyntax implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4568420298587810773L;
	private Long versionIdx;
	private String version;
	
	public Long getVersionIdx() {
		return versionIdx;
	}
	public void setVersionIdx(Long versionIdx) {
		this.versionIdx = versionIdx;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}
