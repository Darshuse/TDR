package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stxVersion")
public class StxVersion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4722539695907361628L;

	@EmbeddedId
	private StxVersionPK stxVersionPK;
	
	@Column(name="VERSION")
	private String version;

	public StxVersionPK getStxVersionPK() {
		return stxVersionPK;
	}

	public void setStxVersionPK(StxVersionPK stxVersionPK) {
		this.stxVersionPK = stxVersionPK;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	} 
	
	
	
}
