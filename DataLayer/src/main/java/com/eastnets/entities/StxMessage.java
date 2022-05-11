package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stxMessage")
public class StxMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083341075707753716L;

	@EmbeddedId
	private StxMessagePK id;
	@Column(name="TYPE")
	private String type; 
	@Column(name="VERSION_IDX")
	private long versionIdx;
	@Column(name="DESCRIPTION")
	private String description; 



	public StxMessagePK getId() {
		return id;
	}
	public void setId(StxMessagePK id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getVersionIdx() {
		return versionIdx;
	}
	public void setVersionIdx(long versionIdx) {
		this.versionIdx = versionIdx;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
