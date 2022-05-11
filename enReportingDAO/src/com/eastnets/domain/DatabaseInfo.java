package com.eastnets.domain;

import java.io.Serializable;
import java.util.Date;

public class DatabaseInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6728785079809778049L;
	//InstallationDate, InstallationUser, Major, Minor, Revision
	private Date installationDate;
	private String installationUser;
	private Integer major;
	private Integer minor;
	private Integer revision;
	
	public Date getInstallationDate() {
		return installationDate;
	}
	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}
	public String getInstallationUser() {
		return installationUser;
	}
	public void setInstallationUser(String installationUser) {
		this.installationUser = installationUser;
	}
	public Integer getMajor() {
		return major;
	}
	public void setMajor(Integer major) {
		this.major = major;
	}
	public Integer getMinor() {
		return minor;
	}
	public void setMinor(Integer minor) {
		this.minor = minor;
	}
	public Integer getRevision() {
		return revision;
	}
	public void setRevision(Integer revision) {
		this.revision = revision;
	}
}
