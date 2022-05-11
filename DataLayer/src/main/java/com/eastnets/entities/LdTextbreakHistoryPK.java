package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class LdTextbreakHistoryPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5902114697716384963L;
	@Column(name = "AID")
	private Integer aid;
	@Column(name = "MESG_S_UMIDL")
	private Integer umidl;
	@Column(name = "MESG_S_UMIDH")
	private Integer umidh;



	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getUmidl() {
		return umidl;
	}

	public void setUmidl(Integer umidl) {
		this.umidl = umidl;
	}

	public Integer getUmidh() {
		return umidh;
	}

	public void setUmidh(Integer umidh) {
		this.umidh = umidh;
	}



}
