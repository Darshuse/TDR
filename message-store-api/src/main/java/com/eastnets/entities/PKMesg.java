package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PKMesg implements Serializable {

	private static final long serialVersionUID = -3187000035816940651L;
	@Column(name = "AID")
	private Long aid;
	@Column(name = "MESG_S_UMIDH")
	private Long umidh;
	@Column(name = "MESG_S_UMIDL")
	private Long umidl;

	public PKMesg() {
	}

	public PKMesg(Long aid, Long umidl, Long umidh) {
		super();
		this.aid = aid;
		this.umidl = umidl;
		this.umidh = umidh;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getUmidh() {
		return umidh;
	}

	public void setUmidh(Long umidh) {
		this.umidh = umidh;
	}

	public Long getUmidl() {
		return umidl;
	}

	public void setUmidl(Long umidl) {
		this.umidl = umidl;
	}

	@Override
	public String toString() {
		return "PKMesg [aid=" + aid + ", umidl=" + umidl + ", umidh=" + umidh + "]";
	}

}
