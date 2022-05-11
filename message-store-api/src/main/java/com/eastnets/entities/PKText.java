package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PKText implements Serializable {

	private static final long serialVersionUID = -6299196265296204118L;

	@Column(name = "AID")
	private Long aid;

	@Column(name = "TEXT_S_UMIDH")
	private Long textSUmidh;

	@Column(name = "TEXT_S_UMIDL")
	private Long textSUmidl;

	public PKText() {
	}

	public PKText(Long aid, Long textSUmidh, Long textSUmidl) {
		super();
		this.aid = aid;
		this.textSUmidh = textSUmidh;
		this.textSUmidl = textSUmidl;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public Long getTextSUmidh() {
		return textSUmidh;
	}

	public void setTextSUmidh(Long textSUmidh) {
		this.textSUmidh = textSUmidh;
	}

	public Long getTextSUmidl() {
		return textSUmidl;
	}

	public void setTextSUmidl(Long textSUmidl) {
		this.textSUmidl = textSUmidl;
	}

	@Override
	public String toString() {
		return "PKText [aid=" + aid + ", textSUmidh=" + textSUmidh + ", textSUmidl=" + textSUmidl + "]";
	}

}
