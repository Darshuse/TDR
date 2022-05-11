package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the RTEXT database table.
 * 
 */
@Embeddable
public class SystemTextFieldPK implements Serializable {

	private static final long serialVersionUID = 2127975277731680361L;
	@Column(name = "AID")
	private long aid;
	@Column(name = "TEXT_S_UMIDL")
	private long textSUmidl;
	@Column(name = "TEXT_S_UMIDH")
	private long textSUmidh; 
	@Column(name = "FIELD_CNT")
	private long fieldCnt;

	 

	public long getFieldCnt() {
		return fieldCnt;
	}

	public void setFieldCnt(long fieldCnt) {
		this.fieldCnt = fieldCnt;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getTextSUmidl() {
		return textSUmidl;
	}

	public void setTextSUmidl(long textSUmidl) {
		this.textSUmidl = textSUmidl;
	}

	public long getTextSUmidh() {
		return textSUmidh;
	}

	public void setTextSUmidh(long textSUmidh) {
		this.textSUmidh = textSUmidh;
	}

 

}