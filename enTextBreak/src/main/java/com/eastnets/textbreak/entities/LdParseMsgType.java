package com.eastnets.textbreak.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ldParseMsgType")
public class LdParseMsgType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 768496684715772077L;

	@EmbeddedId
	private LdParseMsgTypePK id;

	@Column(name = "ENABLED")
	private int enabled;

	public LdParseMsgTypePK getId() {
		return id;
	}

	public void setId(LdParseMsgTypePK id) {
		this.id = id;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

}
