package com.eastnets.domain.viewer;

import java.io.Serializable;
import java.util.Date;
import com.eastnets.domain.admin.User;


public class MessageSearchTemplate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5539382738558741636L;
	long id;
	String name;
	String templateValue; 
	Date creationDate;
	User createdBy;
	long userId;//it used only for profileMigration 
	long profileId=-1;//it used only for profileMigration 
	
	public long getProfileId() {
		return profileId;
	}
	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTemplateValue() {
		return templateValue;
	}
	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
		
}