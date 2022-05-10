package com.eastnets.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Embeddable
public class AppendixPK implements Serializable {

	private static final long serialVersionUID = -5426752980514494532L;

	@Column
	private long aid;

	@Column(name = "APPE_S_UMIDH")
	private long appeSUmidh;

	@Column(name = "APPE_S_UMIDL")
	private long appeSUmidl;

	@Column(name = "APPE_INST_NUM")
	private long appeInstNum = 0;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPE_DATE_TIME")
	private java.util.Date appeDateTime;

	@Column(name = "APPE_SEQ_NBR")
	private long appeSeqNbr;

	@Transient
	private Date mesgCreateDateTime;

	public Date getMesgCreateDateTime() {
		return mesgCreateDateTime;
	}

	public void setMesgCreateDateTime(Date mesgCreateDateTime) {
		this.mesgCreateDateTime = mesgCreateDateTime;
	}

	public AppendixPK() {

	}

	public AppendixPK(int i, int j, int k) {
		aid = i;
		appeSUmidh = j;
		appeSUmidl = k;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getAppeSUmidh() {
		return appeSUmidh;
	}

	public void setAppeSUmidh(long appeSUmidh) {
		this.appeSUmidh = appeSUmidh;
	}

	public long getAppeSUmidl() {
		return appeSUmidl;
	}

	public void setAppeSUmidl(long appeSUmidl) {
		this.appeSUmidl = appeSUmidl;
	}

	public long getAppeInstNum() {
		return appeInstNum;
	}

	public void setAppeInstNum(long appeInstNum) {
		this.appeInstNum = appeInstNum;
	}

	public java.util.Date getAppeDateTime() {
		return appeDateTime;
	}

	public void setAppeDateTime(java.util.Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}

	public long getAppeSeqNbr() {
		return appeSeqNbr;
	}

	public void setAppeSeqNbr(long appeSeqNbr) {
		this.appeSeqNbr = appeSeqNbr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (aid ^ (aid >>> 32));
		result = prime * result + ((appeDateTime == null) ? 0 : appeDateTime.hashCode());
		result = prime * result + (int) (appeInstNum ^ (appeInstNum >>> 32));
		result = prime * result + (int) (appeSUmidh ^ (appeSUmidh >>> 32));
		result = prime * result + (int) (appeSUmidl ^ (appeSUmidl >>> 32));
		result = prime * result + (int) (appeSeqNbr ^ (appeSeqNbr >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppendixPK other = (AppendixPK) obj;
		if (aid != other.aid)
			return false;
		if (appeDateTime == null) {
			if (other.appeDateTime != null)
				return false;
		} else if (!appeDateTime.equals(other.appeDateTime))
			return false;
		if (appeInstNum != other.appeInstNum)
			return false;
		if (appeSUmidh != other.appeSUmidh)
			return false;
		if (appeSUmidl != other.appeSUmidl)
			return false;
		if (appeSeqNbr != other.appeSeqNbr)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppendixPK [aid=" + aid + ", appeSUmidh=" + appeSUmidh + ", appeSUmidl=" + appeSUmidl + ", appeInstNum=" + appeInstNum + ", appeDateTime=" + appeDateTime + ", appeSeqNbr=" + appeSeqNbr + "]";
	}

}
