package com.eastnets.domain.loader;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RAPPE database table.
 * 
 */
@Embeddable
public class AppePK extends AbstractReportingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3504725602465914004L;

	@Column
	private long aid;

	@Column(name="APPE_S_UMIDH")
	private long appeSUmidh;

	@Column(name="APPE_S_UMIDL")
	private long appeSUmidl;

	@Column(name="APPE_INST_NUM")
	private long appeInstNum = 0;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPE_DATE_TIME")
	private java.util.Date appeDateTime;

	@Column(name="APPE_SEQ_NBR")
	private long appeSeqNbr;

	public AppePK() {
	}
	public AppePK(int i, int j, int k) {
		aid = i;
		// TODO Auto-generated constructor stub
		appeSUmidh = j;
		appeSUmidl = k;
	}
	public long getAid() {
		return this.aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getAppeSUmidh() {
		return this.appeSUmidh;
	}
	public void setAppeSUmidh(long appeSUmidh) {
		this.appeSUmidh = appeSUmidh;
	}
	public long getAppeSUmidl() {
		return this.appeSUmidl;
	}
	public void setAppeSUmidl(long appeSUmidl) {
		this.appeSUmidl = appeSUmidl;
	}
	public long getAppeInstNum() {
		return this.appeInstNum;
	}
	public void setAppeInstNum(long appeInstNum) {
		this.appeInstNum = appeInstNum;
	}
	public java.util.Date getAppeDateTime() {
		return this.appeDateTime;
	}
	public void setAppeDateTime(java.util.Date appeDateTime) {
		this.appeDateTime = appeDateTime;
	}
	public long getAppeSeqNbr() {
		return this.appeSeqNbr;
	}
	public void setAppeSeqNbr(long appeSeqNbr) {
		this.appeSeqNbr = appeSeqNbr;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AppePK)) {
			return false;
		}
		AppePK castOther = (AppePK)other;
		return 
			(this.aid == castOther.aid)
			&& (this.appeSUmidh == castOther.appeSUmidh)
			&& (this.appeSUmidl == castOther.appeSUmidl)
			&& (this.appeInstNum == castOther.appeInstNum)
			&& this.appeDateTime.equals(castOther.appeDateTime)
			&& (this.appeSeqNbr == castOther.appeSeqNbr);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.aid ^ (this.aid >>> 32)));
		hash = hash * prime + ((int) (this.appeSUmidh ^ (this.appeSUmidh >>> 32)));
		hash = hash * prime + ((int) (this.appeSUmidl ^ (this.appeSUmidl >>> 32)));
		hash = hash * prime + ((int) (this.appeInstNum ^ (this.appeInstNum >>> 32)));
		hash = hash * prime + this.appeDateTime.hashCode();
		hash = hash * prime + ((int) (this.appeSeqNbr ^ (this.appeSeqNbr >>> 32)));
		
		return hash;
	}
	@Override
	public String toString() {
		return "AppePK [aid=" + aid + ", appeSUmidh=" + appeSUmidh + ", appeSUmidl=" + appeSUmidl + ", appeInstNum=" + appeInstNum
				+ ", appeDateTime=" + appeDateTime + ", appeSeqNbr=" + appeSeqNbr + "]";
	}
	
}