package com.eastnets.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "ldParseMsgType")
public class LdParseMsgType implements Serializable{
 

	/**
	 * 
	 */
	private static final long serialVersionUID = 768496684715772077L;

	@EmbeddedId
	private LdParseMsgTypePK id;
	
	@Column(name = "ENABLED")
	private int enabled;  
	
	
	
	// bi-directional many-to-one association to Inst
	@OneToMany(mappedBy = "ldParseMsgType", cascade = CascadeType.ALL)
	private List<Mesg>  mesgs = new ArrayList<Mesg>();

	
	
	public List<Mesg> getMesgs() {
		return mesgs;
	}
	public void setMesgs(List<Mesg> mesgs) {
		this.mesgs = mesgs;
	}
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
